package gui;

public class Constants {
  static final String USER_DIRECTORY = "user.dir";
  static final String ENCODING = "UTF-8";
  static final String NEW_LINE = "\n";

  private static final String UNKNOWN_LANGUAGE_ERROR = "Pronouns for %s " +
          " are not available.";
  
  public static final String[] SPANISH = {"Yo", "Nosotros", "T�", "Vosotros",
    "�l/Ella/Usted", "Ellos/Ellas/Ustedes"}; 

  public static String[] getPronouns(String language) {
    if(language.equalsIgnoreCase("spanish")) {
      return SPANISH;
    } else {
      throw new IllegalArgumentException(String.format(UNKNOWN_LANGUAGE_ERROR,
              language));
    }
  }
}
