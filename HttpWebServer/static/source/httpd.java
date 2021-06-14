import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Jednostavan web server
 */
public class httpd {
   public static void main(String args[]) throws IOException {
      int port = getPort(args);

      ServerSocket srvr = new ServerSocket(port);

      System.out.println("httpd running on port: " + port);
      System.out.println("document root is: " +
                         new File(".").getAbsolutePath() + "\n");

      Socket skt = null;

      while(true) {
         try {
            skt = srvr.accept();
            InetAddress addr = skt.getInetAddress();

            String resource = getResource(skt.getInputStream());

            if (resource.equals(""))
              resource = "index.html";

            System.out.println("Request from " + addr.getHostName() + ": " +
                             resource);

            sendResponse(resource, skt.getOutputStream());
            skt.close();
            skt = null;
         } catch(IOException ex) {
            ex.printStackTrace();
         }
      }
   }

   static int getPort(String args[]) {
      int port = 80;

      if (args.length == 0) {
         return port;
      }

      try {
         port = Integer.parseInt(args[0]);
      } catch(NumberFormatException ex) {}

      return port;
   }

   static String getResource(InputStream is) throws IOException {
      BufferedReader dis
          = new BufferedReader(new InputStreamReader(is));
      String s = dis.readLine();
      System.out.println(s);

      StringTokenizer hdr = new StringTokenizer(s);

      // prva linija HTTP zahteva: METOD /resurs HTTP/verzija
      // obradjujemo samo GET metodu
      String method = hdr.nextToken();
      if (!"GET".equals(method)) {
         return null;
      }

      String rsrc = hdr.nextToken();

      // izbacimo znak '/' sa pocetka
      rsrc = rsrc.substring(1);

      // ignorisemo ostatak zaglavlja
      String s1;
      while (!(s1 = dis.readLine()).equals(""))
        System.out.println(s1);

      return rsrc;
   }

   static void sendResponse(String resource, OutputStream os) throws IOException {
      PrintStream ps = new PrintStream(os);
      // zamenimo web separator sistemskim separatorom
      resource = resource.replace('/', File.separatorChar);
      File file = new File(resource);

      if (!file.exists()) {
         // ako datoteka ne postoji, vratimo kod za gresku
         ps.print("HTTP/1.0 404 File not Found\r\n\r\n");
         ps.flush();
         System.out.println("Could not find resource: " + file);
         return;
      }

      // ispisemo zaglavlje HTTP odgovora
      ps.print("HTTP/1.0 200 OK\r\n\r\n");

      // a, zatim datoteku
      FileInputStream fis = new FileInputStream(file);
      byte[] data = new byte[8192];
      int len;

      while((len = fis.read(data)) != -1) {
         ps.write(data, 0, len);
      }

      ps.flush();
      fis.close();
   }
}
