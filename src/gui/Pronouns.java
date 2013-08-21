package gui;

public class Pronouns {
  public static final String[] SPANISH = {"Yo", "Nosotros", "T�", "Vosotros",
    "�l/Ella/Usted", "Ellos/Ellas/Ustedes"}; 

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
