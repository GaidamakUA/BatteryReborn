/* refactoring0 */
package b.util;

import java.io.*;
import java.util.*;

public class FileReader77 {
  private List<String> strings;
  private int currentLine=0;

  public FileReader77(String filename) {
    try {
      strings=new ArrayList<String>();
      FileInputStream fstream=new FileInputStream(filename);
      BufferedReader in=new BufferedReader(new InputStreamReader(fstream));
      while (in.ready()) strings.add(in.readLine());
      in.close();
    } catch(Exception e) {
      U77.throwException(e);
    }
  }

  public final int size() {
    return strings.size();
  }

  public final String read() {
    return (String)strings.get(currentLine++);
  }

  public final String get(int i) {
    return strings.get(i);
  }

  public final void drop() {
    currentLine=0;
  }
}
