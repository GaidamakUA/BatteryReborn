package b.b.core;

import b.b.site.*;
import java.net.*;
import java.io.*;
import b.b.stats.*;
import b.util.*;

public class ServerLogger implements Runnable {
  private PrintWriter logFile;
  private int conCount;

  public static void main(String[] args) {
    String out=Config.Pathes.logFile;
    if (args.length>0) {
      out=args[0];
    } else {
      P.p("Call with path parameter - path to log file");
      P.p("  Default path:"+out);
    }
    new Thread(new ServerLogger(out)).start();
  }

  private ServerLogger(String logFile) {
    conCount=0;
    try {
      this.logFile=new PrintWriter(new FileOutputStream(logFile, true), true);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      ServerSocket acceptSocket=new ServerSocket(Config.Server.port);
      log("ServerLogger started at "+Date77.datetime()+
          " and listens "+Config.Server.port+" port...");
      while (true) {
        try {
          Socket sock=acceptSocket.accept();
          if (!sock.getInetAddress().getHostAddress().equals(BannedIPs.myIP)) {
            new LoggerConnection(this, sock);
          } else {
            P.p("hi M77");
          }
        } catch(Exception ex) {
          if ((!ex.toString().startsWith("java.net.SocketException")) &&
              (!(ex instanceof SocketTimeoutException))) {
            ex.printStackTrace();
            logFile.println(U77.toString(ex));
          }
        }
      }
    } catch(Exception e) {
      if (!e.toString().startsWith("java.net.SocketException")) {
        e.printStackTrace();
        logFile.println(U77.toString(e));
      }
    }
  }

  synchronized public void log(String s) {
    try {
      logFile.println(s);
    } catch(Exception ignored) {}
  }

  synchronized public void incConCount(String ip) {
    conCount++;
    log("connection " + conCount + " ("+ip+") created "+
        Date77.time(System.currentTimeMillis()));
    P.p("connection " + conCount + " ("+ip+") created " +
        Date77.time(System.currentTimeMillis()));
  }

  synchronized public void decConCount(String ip) {
    TopPlayers.players().save();
    TopPlayers.players().createJSPs();
    conCount--;
    log("connection ("+ip+") terminated "+
        Date77.time(System.currentTimeMillis()));
    P.p("connection ("+ip+") terminated "+
        Date77.time(System.currentTimeMillis()));
  }
}
