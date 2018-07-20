package infinitespire.util;

public class StringManip {
	public static String pluralOfString(String string) {
		if(string.endsWith("y")) {
			string = string.split("y")[0] += "ies";
		}else if(string.endsWith("se")) {
		}else if(string.endsWith("s")) {
			string += "'";
		} else {
			string += "s";
		}
		return string;
	}
}
