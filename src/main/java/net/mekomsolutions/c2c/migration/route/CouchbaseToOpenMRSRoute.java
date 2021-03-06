package net.mekomsolutions.c2c.migration.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import net.mekomsolutions.c2c.migration.entity.Contact;
import net.mekomsolutions.c2c.migration.entity.Diagnosis;
import net.mekomsolutions.c2c.migration.entity.LabTest;
import net.mekomsolutions.c2c.migration.entity.MedicineEvent;
import net.mekomsolutions.c2c.migration.entity.Patient;
import net.mekomsolutions.c2c.migration.entity.Visit;

public class CouchbaseToOpenMRSRoute extends RouteBuilder {

	public void configure() throws Exception {

		from("jms:queue:c2c.couchbase")
		.split().jsonpath("$.{{couchbase.bucket.name}}").streaming()
		.log("BODY: \\n ${body}\\n HEADERS: \\n${headers}")
		.setHeader("type",simple("${body[dataElementKey]}"))
		.choice()
		.when(header("type").isEqualTo("dlm~00~c2c~contact"))
		.to("direct:c2c-contact")
		.when(header("type").isEqualTo("dlm~00~c2c~patient"))
		.to("direct:c2c-patient")
		.when(header("type").isEqualTo("dlm~00~c2c~visit"))
		.to("direct:c2c-visit")
		.when(header("type").isEqualTo("dlm~00~c2c~diagnosis"))
		.to("direct:c2c-diagnosis")
		.when(header("type").isEqualTo("dlm~00~c2c~labtest"))
		.to("direct:c2c-labtest")
		.when(header("type").isEqualTo("dlm~00~c2c~medicineevent"))
		.to("direct:c2c-medicineevent");

		from("direct:c2c-contact").convertBodyTo(Contact.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("direct:c2c-patient").convertBodyTo(Patient.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("direct:c2c-visit").convertBodyTo(Visit.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("direct:c2c-diagnosis").convertBodyTo(Diagnosis.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("direct:c2c-labtest").convertBodyTo(LabTest.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("direct:c2c-medicineevent").convertBodyTo(MedicineEvent.class)
		.split(simple("${body.entities}"))
		.to("jms:queue:openmrs-save");

		from("jms:queue:openmrs-save")
		.setHeader("modelClass", simple("${body.modelClass}"))
		.setHeader("uuid", simple("${body.uuid}"))
		.marshal().json(JsonLibrary.Jackson)
		.to("{{camel.output.endpoint}}");
	}
}
