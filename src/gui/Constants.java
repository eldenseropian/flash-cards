package gui;

public class Constants {
  static final String USER_DIRECTORY = "user.dir";
  static final String ENCODING = "UTF-8";
  static final String NEW_LINE = "\n";

  public static final String[] SPANISH = {"Yo", "Nosotros", "Tœ", "Vosotros",
    "ƒl/Ella/Usted", "Ellos/Ellas/Ustedes"}; 

  public static String[] getPronouns(String language) {
    if(language.equalsIgnoreCase("spanish")) {
      return SPANISH;
    }
    else {
      throw new IllegalArgumentException("Pronouns for " + language +
              " are not available.");
    }
  }
}
