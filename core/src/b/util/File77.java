package b.util;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class File77 {
    /**
     * Creates file pathname with content. If some dirs of pathname doesn't
     * exist creates it.
     */
    public static void create(String pathname, String content) {
        try {
            createNotExistingDirs(pathname);
            File f = new File(pathname);
            f.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(f));
            out.print(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            U77.throwException(e, "path:\"" + pathname + "\"");
        }
    }

    /**
     * returns absolutePath of file or dir with largest (not longest) name
     * if no or error returns null
     * example of return: "D:/Hodohi/0000007"
     */
    public static String getLargestPathname(String dir) {
        return getLargestPathnameEndedWith(dir, "");
    }

    /**
     * returns absolutePath of file or dir with largest (not longest) name
     * if no or error returns null
     * example of return: "D:/Hodohi/0000007"
     */
    public static String getLargestPathnameEndedWith(File dir, String endsWith) {
        try {
            return getLargestPathnameEndedWith(dir.getAbsolutePath(), endsWith);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns absolutePath of file or dir with largest (not longest) name
     * if no or error returns null
     * example of return: "D:/Hodohi/0000007"
     */
    public static String getLargestPathnameEndedWith(String dir,
                                                     String endsWith) {
        try {
            File[] fs = new File(dir).listFiles();
            File f = null;
            for (int i = 0; i < fs.length; i++) {
                if (f == null) {
                    if (fs[i].getName().endsWith(endsWith)) f = fs[i];
                } else {
                    if (f.getName().compareTo(fs[i].getName()) < 0 &&
                            fs[i].getName().endsWith(endsWith)) f = fs[i];
                }
            }
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns absolutePath of file or dir with largest (not longest) name
     * if no or error returns null
     * example of return: "D:/Hodohi/0000007"
     */
    public static String getLargestPathnameFromLargestNotEmptyDir(String dir) {
        return getLargestPathnameFromLargestNotEmptyDirEndedWith(dir, "");
    }

    /**
     * returns absolutePath of file or dir with largest (not longest) name
     * if largest dir contains no file ended with endsWith then returns from dir
     * where it is
     * if no or error returns null
     * example of return: "D:/Hodohi/0000007"
     */
    public static String getLargestPathnameFromLargestNotEmptyDirEndedWith(
            String dir, String endsWith) {
        try {
            File[] fs = new File(dir).listFiles();
            sort(fs);
            for (int i = fs.length - 1; i > -1; i--) {
                String largestPathname = getLargestPathnameEndedWith(
                        fs[i].getAbsolutePath(), endsWith);
                if (largestPathname != null) return largestPathname;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * example of param: "D:/Hodohi/0000007"
     * if toFile path contains unexisting dirs then creates them
     */
    public static void copy(String fromFile, String toFile) {
        try {
            createNotExistingDirs(toFile);
            FileInputStream from = new FileInputStream(fromFile);
            FileOutputStream to = new FileOutputStream(toFile);
            int i;
            do {
                i = from.read();
                if (i != -1) to.write(i);
            } while (i != -1);
            from.close();
            to.close();
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    public static String getContent(String file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
            String line = null;
            String res = "";
            while ((line = myInput.readLine()) != null) {
                res += line + '\n';
            }
            if (!res.equals("")) {
                return res.substring(0, res.length() - 1);
            } else {
                return res;
            }
        } catch (Exception e) {
            U77.throwException(e);
            /* heh */
            return null;
        }
    }

    /**
     * example of path: "C:/projects/Hodohi/file.txt"
     * part after last '/' is ignored
     */
    public static void createNotExistingDirs(String path) {
        try {
            char separator = '/';
            if (path.indexOf('\\') != -1) separator = '\\';
            int pos = path.lastIndexOf(separator);
            if (pos == -1) {
                File f = new File(path);
                if (f.exists()) return;
                if (path.indexOf('.') != -1) return;
                f.mkdir();
                return;
            }
            new File(path.substring(0, pos)).mkdirs();
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    /**
     * on error returns false
     */
    public static boolean isEmptyDir(File f) {
        try {
            return f.isDirectory() && getLargestPathname(f.getAbsolutePath()) == null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * on error returns null
     */
    public static String getParentDir(String pathname) {
        try {
            return new File(pathname).getParent();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param to - Destionation directory name
     */
    public static void copyDirsAndFiles(String from, String to) {
        try {
            if (!to.endsWith(File.separator)) {
                to = to + File.separator;
            }
            if (new File(from).isFile()) {
                copy(from, to + new File(from).getName());
            } else {
                File[] fs = new File(from).listFiles();
                for (int i = 0; i < fs.length; i++) {
                    File f = fs[i];
                    if (f.isFile()) {
                        copy(f.getAbsolutePath(), to + f.getName());
                    } else {
                        copyDirsAndFiles(f.getAbsolutePath(), to + new File(from).getName());
                    }
                }
            }
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    public static void delete(String pathname) {
        File f = new File(pathname);
        if (f.isFile()) {
            f.delete();
        } else {
            while (f.listFiles().length != 0) {
                File[] fs = f.listFiles();
                for (int i = 0; i < fs.length; i++) {
                    delete(fs[i].getAbsolutePath());
                }
            }
            f.delete();
        }
    }

    public static void appendLine(String pathname, String what) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(pathname, true));
            out.println(what);
            out.flush();
            out.close();
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    private static void sort(File[] files) {
        Arrays.sort(files, new Comparator() {
            public int compare(Object f1, Object f2) {
                return ((File) f1).getName().compareTo(((File) f2).getName());
            }
        });
    }
}
