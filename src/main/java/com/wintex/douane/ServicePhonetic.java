package com.wintex.douane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.CharMatcher;

public class ServicePhonetic {

	public static final int wordLength = 40;
	public static final List<String> specials = List.of("EURL", "SARL", "MONSIEUR");

	public static List<Record> getDuplicatedRecords(Map<String, List<Record>> grouped) {
		List<Record> records = new ArrayList<>();
		for (Map.Entry<String, List<Record>> entry : grouped.entrySet()) {
			if (entry.getValue().size() > 1) {
				records.add(new Record());
				records.addAll(entry.getValue());
			}
		}
		return records;
	}

	public static Map<String, List<Record>> groupByFullNamePhonetic(List<Record> records) {
		return records.stream().filter(e -> e.getFullNamePhonetic() != null)
				.collect(Collectors.groupingBy(Record::getFullNamePhonetic));
	}

	public static Map<String, List<Record>> groupByAddressPhonetic(List<Record> records) {
		return records.stream().filter(e -> e.getAdressePhonetic() != null)
				.collect(Collectors.groupingBy(Record::getAdressePhonetic));
	}

	public static void calculatePhonetic(List<Record> records) {
		for (Record record : records) {
			String fullNamePhonetic = phonetic(record.getFullName(), wordLength, specials);
			String addressPhonetic = phonetic(record.getAddresse(), wordLength, specials);

			record.setFullNamePhonetic(fullNamePhonetic);
			record.setAdressePhonetic(addressPhonetic);
		}
	}

	public static String phonetic(String word, int length, List<String> specials) {
		if (word == null)
			return null;
		word = CharMatcher.whitespace().removeFrom(word);
		word = word.replace("ç", "s");
		word = word.toUpperCase();
		word = removeSpecialCaractere(word);
		word = removeSpecialWords(specials, word);
		// remplacement
		word = word.replace("QU", "K");
		word = word.replace("Q", "K");
		word = word.replace("KH", "K");
		// le cas du C
		word = word.replace("ACE", "ASSE");
		word = word.replace("OCE", "OSSE");
		word = word.replace("UCE", "USSE");
		word = word.replace("ECE", "ESSE");
		word = word.replace("ICE", "ISSE");
		// Pour le cas de w --ou
		word = word.replace("OUA", "WA");
		word = word.replace("OUE", "WE");
		word = word.replace("OUA", "WA");
		word = word.replace("OUI", "WI");
		word = word.replace("AOU", "AW");
		word = word.replace("ACI", "ASSI");
		word = word.replace("OCI", "OSI");
		word = word.replace("UCI", "USSI");
		word = word.replace("ECI", "ESSI");
		word = word.replace("ICI", "ISSI");
		word = word.replace("C", "K");
		word = word.replace("CK", "K");
		word = word.replace("GI", "JI");
		word = word.replace("GE", "JE");

		if (word.isEmpty())
			return null;
		String firstLetter = word.substring(0, 1);
		String lastLetter = word.substring(word.length() - 1);
		word = word.replace("I", "A");
		word = word.replace("E", "A");
		word = word.replace("U", "A");
		word = word.replace("O", "A");
		word = firstLetter.concat(word.substring(1));
		if (lastLetter.equals("I"))
			word = word.substring(0, word.length() - 1).concat("I");

		// remplacement
		word = word.replace("ASA", "AZA");
		word = word.replace("SCH", "CH");
		word = word.replace("PH", "FF");
		word = word.replace("DJ", "J");
		word = word.replace("SH", "CH");

		// suppression des H sauf cels qui sont précédés pas C ou S

		word = removeHLetter(word);

		// suppression des voyelle sauf la premier et la dernier lettre
		String begin = word.substring(0, 1);
		String middle = "";
		if (word.length() >= 2)
			middle = word.substring(1, word.length()).replace("A", "");
		// String end = word.substring(word.length() - 1);
		word = begin.concat(middle); // .concat(end);

		// enlever les répétitions
		word = removeRespectedSideBySideChar(word);

		if (length > word.length())
			length = word.length();
		return word.substring(0, length);
	}

	public static String removeHLetter(String word) {
		if (!word.contains("CH"))
			word.replace("H", "");
		return word;
	}

	public static String removeRespectedSideBySideChar(String word) {
		if (word.length() > 0) {
			String comparedChar = word.substring(0, 1);
			String temp = comparedChar;
			for (int i = 1; i < word.length(); i++) {
				String character = word.substring(i, i + 1);
				if (!comparedChar.equals(character)) {
					temp += character;
					comparedChar = character;
				}
			}
			word = temp;
		}
		return word;
	}

	public static String removeSpecialCaractere(String word) {
		word = word.replace("Â", "A");
		word = word.replace("Ä", "A");
		word = word.replace("À", "A");
		word = word.replace("Ç", "C");
		word = word.replace("È", "E");
		word = word.replace("É", "E");
		word = word.replace("Ê", "E");
		word = word.replace("Ë", "E");
		word = word.replace("Œ", "E");
		word = word.replace("Î", "I");
		word = word.replace("Ï", "I");
		word = word.replace("Ô", "O");
		word = word.replace("Ö", "O");
		word = word.replace("Ù", "O");
		word = word.replace("Ù", "O");
		word = word.replace("Û", "U");
		return word.replaceAll("[^a-zA-Z]", "");
	}

	public static String removeSpecialWords(List<String> specials, String word) {
		for (String special : specials) {
			word = word.replace(special, "");
		}
		return word;
	}
}
