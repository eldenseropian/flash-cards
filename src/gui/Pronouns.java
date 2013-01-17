package gui;

public class Pronouns {
	public static final String[] SPANISH = {"Yo", "Nosotros", "Tœ", "Vosotros", "ƒl/Ella/Usted", 
	"Ellos/Ellas/Ustedes"}; 

	public static String[] getPronouns(String language) {
		if(language.equalsIgnoreCase("spanish")) {
			return SPANISH;
		}
		else {
			String[] list = new String[1];
			list[0] = language;
			return list;
		}
	}
}
