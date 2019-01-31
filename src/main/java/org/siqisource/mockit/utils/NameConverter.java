package org.siqisource.mockit.utils;

import java.util.Arrays;

public class NameConverter {

	public static String firstLetterUpper(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static String firstLetterLower(String string) {
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	public static String convertWithNamespcae(String name, NameConvertStyle style) {
		String names[] = name.split(".");
		if (names.length == 0) {
			return convert(name, style);
		} else {
			names[names.length - 1] = convert(names[names.length - 1], style);
			return Arrays.toString(names);
		}

	}

	public static String convert(String name, NameConvertStyle style) {
		switch (style) {
		case underlineToCamelFirstUpper:
			return underlineToCamelFirstUpper(name);
		case underlineToCamelFirstLower:
			return underlineToCamelFirstLower(name);
		case camelToUnderlineUpper:
			return camelToUnderlineUpper(name);
		case camelToUnderlineLower:
			return camelToUnderlineLower(name);
		default:
			throw new RuntimeException("unknow name style");
		}
	}

	public static String camelToUnderlineUpper(String name) {
		return camelToUnderlineLower(name).toUpperCase();
	}

	public static String camelToUnderlineLower(String name) {
		if (null == name) {
			return "";
		}
		char[] chars = name.toCharArray();
		StringBuffer field = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (i != 0 && Character.isUpperCase(c)) {
				field.append("_");
				field.append(Character.toLowerCase(c));
			} else {
				field.append(c);
			}
		}
		return field.toString().toLowerCase();
	}

	public static String underlineToCamelFirstUpper(String name) {
		if (null == name) {
			return "";
		}
		String[] words = name.toLowerCase().split("_");
		StringBuffer property = new StringBuffer();

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (i == 0) {
				property.append(word);
			} else {
				property.append(Character.toUpperCase(word.charAt(0)));
				property.append(word.substring(1));
			}
		}
		return property.toString();
	}

	public static String underlineToCamelFirstLower(String name) {
		String[] words = name.toLowerCase().split("_");
		StringBuffer property = new StringBuffer();

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (i == 0) {
				property.append(word);
			} else {
				property.append(Character.toUpperCase(word.charAt(0)));
				property.append(word.substring(1));
			}

		}
		return property.toString();
	}

}
