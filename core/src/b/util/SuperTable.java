package b.util;

import b.tools.PipeHtml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuperTable {
    private static int groupFor = 7;

    private static List<String> columnNames;
    private static List<List<String>> data;
    private static int column;
    private static int chartedCol;

    public static synchronized final String generate(String table, String dir,
                                                     String chartedColumn, List<String> fieldsForAnotherTables) {
        P.p("supertable starts");
        columnNames = new ArrayList<String>();
        data = new ArrayList<List<String>>();
        int i = createColumnNames(table);
        chartedCol = index(chartedColumn);
        StringBuffer res = new StringBuffer(100000);
        res.append("<table border=\"1\">\n");//width=\"3000\"
        P.p("supertable before headers");
        res.append(headers());
        P.p("supertable after headers");
        getData(table.substring(i));
        column = chartedCol;
        P.p("supertable before columns");
        res.append(columns());
        P.p("supertable after columns");
        createTables(dir);
        for (String field : fieldsForAnotherTables) {
            P.p("SuperTable field: " + field);
            tableForLength(dir, field);
        }
        for (String field : columnNames) {
            if (!fieldsForAnotherTables.contains(field)) {
                P.p("SuperTable field: " + field);
                tableForLengthString(dir, field);
            }
        }
        P.p("supertable ends");
        return res + "</table>";
    }

    private static final int index(String field) {
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equals(field)) return i;
        }
        return -1;
    }

    private static void tableForLength(String dir, String field) {
        sort(index(field));
        String res = minPageStart("By " + field + " (" + Date77.datetime() + ")") +
                "records:" + data.size() + " avgLength:" + getAvg(index("length")) + "<br>\n" +
                "<table border=\"1\" width=\"300\">\n";
        int row = 0;
        List<List<Pair>> groups = new ArrayList<List<Pair>>();
        List<Pair> group = new ArrayList<Pair>();
        groups.add(group);
        while (row < data.size()) {
            if (U77.brem(row + 1, groupFor)) {
                group = new ArrayList<Pair>();
                groups.add(group);
            }
            Double what = new Double(get(row, field));
            Double length = new Double(get(row, "length"));
            group.add(new Pair(what, length));
            row++;
        }
        for (List<Pair> g : groups) {
            res +=
                    groupRow(g, field);
        }
        res +=
                "</table>\n" +
                        "</body>\n" +
                        "</html>";
        File77.create(dir + "/" + "for_" + field + ".html", res);
    }

    private static final String minPageStart(String title) {
        return
                "<html>\n" +
                        "<head>\n" +
                        "<title>" + title + "</title>\n" +
                        "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<b>" + title + "</b><br>\n";
    }

    private static void tableForLengthString(String dir, String field) {
        sort(index(field));
        String res = minPageStart("By " + field + " (" + Date77.datetime() + ")") +
                "records:" + data.size() + " avgLength:" + getAvg(index("length")) + "<br>\n" +
                "<table border=\"1\" width=\"300\">\n";
        int row = 0;
        List<List<Pair>> groups = new ArrayList<List<Pair>>();
        List<Pair> group = new ArrayList<Pair>();
        groups.add(group);
        while (row < data.size()) {
            if (U77.brem(row + 1, groupFor)) {
                group = new ArrayList<Pair>();
                groups.add(group);
            }
            String what = get(row, field);
            Double length = new Double(get(row, "length"));
            group.add(new Pair(what, length));
            row++;
        }
        for (List<Pair> g : groups) {
            res +=
                    groupRowString(g, field);
        }
        res +=
                "</table>\n" +
                        "</body>\n" +
                        "</html>";
        File77.create(dir + "/" + "for_" + field + ".html", res);
    }

    private static final String groupRowString(List<Pair> group, String field) {
        String res =
                "<tr>";
        double sum1 = 0;
        for (Pair p : group) {
            sum1 += (Double) (p.o2);
        }
        double max = getAvg(index("length")) * 3;
        double avg1 = sum1 / group.size();
        boolean groupEmpty = group.isEmpty();
        if (groupEmpty) {
            P.p("group for " + field + " is empty");
            return "";
        } else {
            return res +
                    "<td" + PipeHtml.inTd(avg1 / max) + ">" + "for " + field + ":" +
                    (group.get(0).o1 + "-" + group.get(group.size() - 1).o1) +
                    " " + U77.ssprecision(avg1, 2) + "</td></tr>\n";
        }
    }

    private static final String groupRow(List<Pair> group, String field) {
        String res =
                "<tr>";
        double sum0 = 0;
        double sum1 = 0;
        for (Pair p : group) {
            sum0 += (Double) (p.o1);
            sum1 += (Double) (p.o2);
        }
        double max = getAvg(index("length")) * 3;
        double avg0 = sum0 / group.size();
        double avg1 = sum1 / group.size();
        return res +
                "<td" + PipeHtml.inTd(avg1 / max) + ">" + "avg " + field + ":" + U77.ssprecision(avg0, 3) +
                " " + U77.ssprecision(avg1, 2) + "</td></tr>\n";
    }

    private static final String get(int row, String field) {
        return data.get(row).get(index(field));
    }

    private static final int createColumnNames(String table) {
        int i = table.indexOf("<td>");
        while (table.substring(i).startsWith("<td>")) {
            int nextI = table.indexOf("</td>", i);
            columnNames.add(table.substring(i + "<td>".length(), nextI));
            i = nextI + "</td>".length();
        }
        return i;
    }

    private static final void getData(String t) {
        String table = "" + t;
        while (table.contains("<td>")) {
            List<String> row = new ArrayList<String>();
            data.add(row);
            for (int j = 0; j < columnNames.size(); j++) {
                int start = table.indexOf("<td>") + "<td>".length();
                int end = table.indexOf("</td>");
                row.add(table.substring(start, end));
                table = table.substring(end + "</td>".length());
            }
        }
    }

    private static final void createTables(String dir) {
        column = 0;
        for (String name : columnNames) {
            String res = minPageStart("By " + name + " (" + Date77.datetime() + ")") +
                    "<table border=\"1\">\n" +// width=\"3000\"
                    by(name) +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>";
            File77.create(dir + "/" + "by_" + name + ".html", res);
            column++;
        }
    }

    private static final String headers() {
        String res = "<tr>";
        int i = 0;
        for (String s : columnNames) {
            String linkToAnotherTable = "";
            if (!(s.equals("length"))) {
                linkToAnotherTable = "<br><a href=\"for_" + s + ".html\">" + s + "</a>";
            }
            res += "<td" + (i == chartedCol ? " width=\"300\"" : "") +
                    (i == column ? " bgcolor=\"#777777\"" : "") + ">" +
                    "<a href=\"by_" + s + ".html\"><b>" + s + "</b></a>" +
                    linkToAnotherTable + "</td>";
            i++;
        }
        res += "</tr>\n";
        return res;
    }

    /* Used only in sort method*/
    private static int sortColumn;

    private static final String by(String by) {
        P.p("by " + by);
        String res = headers();
        sort(column);
        return res + columns();
    }

    private static final void sort(int column) {
        sortColumn = column;
        Collections.sort(data, new Comparator() {
            public int compare(Object o1, Object o2) {
                List<String> l1 = (List<String>) o1;
                List<String> l2 = (List<String>) o2;
                String v1 = l1.get(sortColumn);
                String v2 = l2.get(sortColumn);
                return v1.compareTo(v2);
            }

            public boolean equals(Object o) {
                return false;
            }
        });
    }

    private static final String columns() {
        P.p("columns start");
        boolean odd = false;
        double max = getMax(chartedCol);
        String last = "####";
        String res = "";
        int prevProc = -1;
        int counter = 0;
        for (List<String> row : data) {
            counter++;
            int proc = (int) (((double) counter * 10) / row.size());
            if (prevProc < proc) {
                prevProc = proc;
                P.p("columns " + (proc * 10) + "%");
            }
            String newLast = row.get(column);
            if (!last.equals(newLast)) {
                last = newLast;
                odd = !odd;
            }
            String bgcol = " bgcolor=\"#" + (odd ? "000000" : "474747") + "\"";
            res += "<tr>";
            int i = 0;
            double m = Double.parseDouble(row.get(chartedCol));
            for (String s : row) {
                res += "<td" + ((i == chartedCol) ? PipeHtml.inTd(m / max) :
                        ((i == column) ? " bgcolor=\"#777777\"" : bgcol)) + ">" +
                        (s.equals("") ? "&nbsp;" : s) + "</td>";
                i++;
            }
            res += "</tr>\n";
        }
        P.p("columns end");
        return res;
    }

    private static final double getMax(int col) {
        double max = 0;
        try {
            max = Double.parseDouble(data.get(0).get(col));
        } catch (Exception ignored) {
        }
        for (List<String> row : data) {
            try {
                double m = Double.parseDouble(row.get(col));
                if (m > max) max = m;
            } catch (Exception ignored) {
            }
        }
        return max;
    }

    private static final double getAvg(int col) {
        double sum = 0;
        int count = 0;
        for (List<String> row : data) {
            try {
                sum += Double.parseDouble(row.get(col));
                count++;
            } catch (Exception ignored) {
            }
        }
        return ((double) sum) / count;
    }
}
