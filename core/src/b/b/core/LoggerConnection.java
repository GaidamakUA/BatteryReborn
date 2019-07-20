package b.b.core;

import b.b.site.TopPlayers;
import b.util.Date77;
import b.util.Str77;
import b.util.U77;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class LoggerConnection implements Runnable {
    private ServerLogger serverLogger;
    private BufferedReader in;
    private int id;
    private String ip;
    private String name;
    private int scores = 0;

    private static int maxId = new Random(System.currentTimeMillis()).nextInt();

    public LoggerConnection(ServerLogger serverLogger, Socket sock) {
        try {
            this.serverLogger = serverLogger;
            sock.setSoTimeout(30000);
            maxId++;
            id = maxId;
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            ip = sock.getInetAddress().getHostAddress();
            serverLogger.incConCount(ip);
            serverLogger.log("\n" + id + " addr " + ip + " time " + System.currentTimeMillis() +
                    " " + Date77.time(System.currentTimeMillis()));
            new Thread(this).start();
        } catch (Exception e) {
            e.printStackTrace();
            serverLogger.log(U77.toString(e));
        }
    }

    public void run() {
        while (true) {
            try {
                String s = in.readLine();
                if (s != null) {
                    if (s.startsWith("name ")) {
                        name = Str77.param(s, 1);
                    } else if (s.startsWith("scores ")) {
                        int sc = Str77.iparam(s, 1);
                        if (sc > scores) {
                            TopPlayers.players().add(name, sc, id);
                            scores = sc;
                        }
                    }
                    serverLogger.log("" + id + " " + s);
                } else {
                    serverLogger.decConCount(ip);
                    return;
                }
            } catch (Exception e) {
                serverLogger.decConCount(ip);
                if ((!e.toString().startsWith("java.net.SocketException")) &&
                        (!(e instanceof SocketTimeoutException))) {
                    e.printStackTrace();
                    serverLogger.log(U77.toString(e));
                }
                return;
            }
        }
    }
}
