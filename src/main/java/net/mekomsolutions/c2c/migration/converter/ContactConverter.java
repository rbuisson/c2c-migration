package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.Contact;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;

@Converter
public class ContactConverter {

	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String MIDDLE_NAME = "middlename";

	private static final String CELL_PHONE = "cellphone";
	private static final String HOME_PHONE = "homephone";
	private static final String WORK_PHONE = "workphone";

	private static final String CONTACT_TYPE = "type";

	private static final String RELATIONSHIP = "relationship";

	private static final String ADDRESS_LINE_1 = "addressline1";
	private static final String ADDRESS_LINE_2 = "addressline2";

	
	@Converter
	public Contact toContact(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		// Contact Person Name
		String contactPersonName = Utils.concatName(new LinkedList<String>(
				Arrays.asList(data.get(FIRST_NAME), data.get(MIDDLE_NAME), data.get(LAST_NAME))));
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactPersonName.uuid", contactPersonName, Constants.PATIENT_KEY, data, exchange, allEntities);
		// Contact Person Phone Number
		String contactPersonPhoneNmubers = Utils.concatPhoneNumber(new LinkedList<String>(
				Arrays.asList(data.get(CELL_PHONE), data.get(HOME_PHONE), data.get(WORK_PHONE))));
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactPersonPhone.uuid", contactPersonPhoneNmubers, Constants.PATIENT_KEY, data, exchange, allEntities);
		// Contact Type
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactContactType.uuid", data.get(CONTACT_TYPE), Constants.PATIENT_KEY, data, exchange, allEntities);
		// Contact Person Relationship
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactRelationship.uuid", data.get(RELATIONSHIP), Constants.PATIENT_KEY, data, exchange, allEntities);
		// Contact Person Address
		String contactPersonAddresses = Utils.concatAddresses(new LinkedList<String>(
				Arrays.asList(data.get(ADDRESS_LINE_1), data.get(ADDRESS_LINE_2))));
		SyncEntityUtils.createAndAddPersonAttribute("pat.contactPersonAddress.uuid", contactPersonAddresses, Constants.PATIENT_KEY, data, exchange, allEntities);

		return new Contact(allEntities);
	}

	
}
