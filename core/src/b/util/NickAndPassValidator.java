package b.util;

public class NickAndPassValidator {
  private static final String availableChars = "1234567890_qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

  public static final boolean valide(String s) {
    if (s==null || s.equals("") || s.length()>10 || s.equalsIgnoreCase("dragon")) {
      return false;
    }
    for (int i=0; i<s.length(); i++) {
      boolean found=false;
      for (int j=0; j<availableChars.length(); j++) {
        if (s.charAt(i)==availableChars.charAt(j)) {
          found=true;
          break;
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }
}
