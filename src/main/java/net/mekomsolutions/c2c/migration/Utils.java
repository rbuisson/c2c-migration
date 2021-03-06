package net.mekomsolutions.c2c.migration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Utils {

	private Utils() {
	}

	public static <T> String getModelClassFullFromType(Class<T> type) {
		return getModelClassFullFromString(type.getSimpleName());
	}

	public static String getModelClassFullFromString(String typeAsString) {
		return Constants.FULL_MODEL_CLASS_NAMES.get(typeAsString);
	}

	public static String getModelClassLight(String typeAsString, UUID uuid) {
		return Constants.LIGHT_MODEL_CLASS_NAMES.get(typeAsString) + "(" + uuid.toString() + ")";
	}
	
	public static String getModelClassLight(String typeAsString, String uuid) {
		return Constants.LIGHT_MODEL_CLASS_NAMES.get(typeAsString) + "(" + uuid + ")";
	}
	
	public static List<Integer> dateLongToArray(Long timeInMillisecs) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timeInMillisecs);

		List<Integer> array = new ArrayList<>();
		array.add(calendar.get(Calendar.YEAR));
		array.add(calendar.get(Calendar.MONTH) + 1);
		array.add(calendar.get(Calendar.DAY_OF_MONTH));
		array.add(calendar.get(Calendar.HOUR));
		array.add(calendar.get(Calendar.MINUTE));
		array.add(calendar.get(Calendar.SECOND));
		return array;
	}

	public static List<Integer> dateStringToArray(String formattedDateString) {
		// 2017-07-28T08:48:17.429Z

		Date date = new Date();
		List<Integer> array = new ArrayList<>();

		SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

		try {
			date = s1.parse(formattedDateString);
		}
		catch (ParseException pe1)
		{
			try
			{
				date = s2.parse(formattedDateString);
			}
			catch (ParseException pe2)
			{
				pe2.printStackTrace();
			}    
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		array.add(calendar.get(Calendar.YEAR));
		array.add(calendar.get(Calendar.MONTH) + 1);
		array.add(calendar.get(Calendar.DAY_OF_MONTH));
		array.add(calendar.get(Calendar.HOUR));
		array.add(calendar.get(Calendar.MINUTE));
		array.add(calendar.get(Calendar.SECOND));

		return array;
	}

	public static List<Integer> convertBirthdate(String string) {
		List<Integer> birthdate = dateStringToArray(string);
		return new ArrayList<Integer>(birthdate.subList(0, 3));
	}

	private static List<String> trimAndCapitalize(LinkedList<String> list) {

		list.removeAll(Arrays.asList("", null));
		list.removeAll(Arrays.asList("	", null));

		// Trim
		List<String> trimmedList = new ArrayList<>();
		for (String str : list) {
			trimmedList.add(str.trim());
		}
		// Capitalize
		List<String> capitalizedList = new ArrayList<>();
		for (String str : trimmedList) {
			capitalizedList.add(str.substring(0, 1).toUpperCase() + str.substring(1));
		}

		return capitalizedList;
	}

	public static String concatName (LinkedList<String> list) {
		return String.join(" ", trimAndCapitalize(list));
	}

	public static String concatPhoneNumber (LinkedList<String> list) {
		return String.join(" / ", trimAndCapitalize(list));
	}

	public static String concatAddresses (LinkedList<String> list) {
		return String.join(", ", trimAndCapitalize(list));
	}

	public static boolean hasKeyOrValue (String value) {
		return !(value == null || value.isEmpty());
	}
}
