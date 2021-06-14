import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Server
{
    private int port;
    private ServerSocket serverSocket;
	private boolean isValid;
	private boolean isUnique;
	private ArrayList<Zadatak> zadaci;
	private ArrayList<Zadatak> zadaciFiltered;

    public Server(int port)
            throws IOException
    {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
        zadaci = new ArrayList<Zadatak>();
        Zadatak z1 = new Zadatak("0001/01", "Zadatak 1", "Srdjan Srdjanovic", "0649585855", TezinaZadatka.LAK, 10, false);
        Zadatak z2 = new Zadatak("0001/02", "Zadatak 2", "Stefan Srdjanovic", "0649775855", TezinaZadatka.TEZAK, 15, false);
        Zadatak z3 = new Zadatak("0001/03", "Zadatak 3", "Srdjana Srdjanovic", "0644485855", TezinaZadatka.LAK, 10, false);
        zadaci.add(z1);
        zadaci.add(z2);
        zadaci.add(z3);
        
        isValid = true;
        isUnique = true;
        zadaciFiltered = new ArrayList<Zadatak>();
    }


    public void run()
    {
        System.out.println("Web server running on port: " + port);
        System.out.println("Document root is: " + new File("/static").getAbsolutePath() + "\n");

        Socket socket;

        while (true)
        {
            try
            {
                // prihvataj zahteve
                socket = serverSocket.accept();
                InetAddress addr = socket.getInetAddress();

                // dobavi resurs zahteva
                String resource = this.getResource(socket.getInputStream());
                
                if(!isValid) {
                	PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"),true);
                	out.print("HTTP/1.1 200 OK\r\nContent-type: text/html;charset=utf-8\r\n\r\n");
					out.print("<h1>Neadekvatan unos</h1>");
					out.close();
					isValid = true;
					continue;
                }
                
                if(!isUnique) {
                	PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"),true);
                	out.print("HTTP/1.1 200 OK\r\nContent-type: text/html;charset=utf-8\r\n\r\n");
					out.print("<h1>Zadatak je vec unet.</h1>");
					out.close();
					isUnique = true;
					continue;
                }
                
                // fail-safe
                if (resource == null)
                    continue;

                if (resource.equals(""))
                    resource = "static/index.html";

                System.out.println("Request from " + addr.getHostName() + ": " +  resource);

                // posalji odgovor
                this.sendResponse(resource, socket.getOutputStream());
                socket.close();
                socket = null;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }


    private String getResource(InputStream is)
            throws IOException
    {
        BufferedReader dis = new BufferedReader(new InputStreamReader(is));
        String s = dis.readLine();

        // fail-safe
        if (s == null)
            return null;

        String[] tokens = s.split(" ");

        // prva linija HTTP zahteva: METOD /resurs HTTP/verzija
        // obradjujemo samo GET metodu
        String method = tokens[0];
        //if (!method.equals("GET"))
            //return null;

        // String resursa
        String resource = tokens[1];

        // izbacimo znak '/' sa pocetka
        resource = resource.substring(1);
        
        if (resource.contains("id=")) {
        	System.out.println("GET ------>");
        	System.out.println(resource);
        	for(Zadatak z : zadaci) {
        		if(z.getBrojZadatka().equals(resource.split("=")[1])) {
        			z.setZapocet(true);
        		}
        	}
        	return resource;
        }
        
        if (resource.contains("Success")) {
        	String s2;
        	int numChars = 0;
        	String ime = "";
            while (!(s2 = dis.readLine()).equals("")) {
            	if(s2.contains("Content-Length")) {
            		String[] cl = s2.split(":");
            		numChars = Integer.parseInt(cl[1].trim());
            	}
            }
            StringBuilder resultBuilder = new StringBuilder();
            for(int i = 0; i < numChars; ++i) {
            	resultBuilder.append((char)dis.read());
            }

            String url = resultBuilder.toString();
            String[] url_param_splits = url.split("&");
            HashMap<String, String> map = new HashMap<>();
            for(String param : url_param_splits){
            	System.out.println(param);
            	if(param.split("=").length == 2)
            		map.put(param.split("=")[0], convertString(param.split("=")[1]));
            }
            
            
            Zadatak zadatak = validate(map);
            if(zadatak == null) {
            	isValid = false;
            	return resource;
            }
            if(!isZadatakUnique(zadatak)) {
            	isUnique = false;
            	return resource;
            }
            
            zadaci.add(zadatak);
            return resource;
            
        }
        
        if(resource.contains("Search")) {
        	String s2;
        	int numChars = 0;
        	String ime = "";
            while (!(s2 = dis.readLine()).equals("")) {
            	if(s2.contains("Content-Length")) {
            		System.out.println(s2);
            		String[] cl = s2.split(":");
            		numChars = Integer.parseInt(cl[1].trim());
            	}
            }
            StringBuilder resultBuilder = new StringBuilder();
            for(int i = 0; i < numChars; ++i) {
            	resultBuilder.append((char)dis.read());
            }

            String url = resultBuilder.toString();
            System.out.println(url);
            String zaduzeniSearch = url.split("=")[1];
            
            for(Zadatak z : zadaci) {
        		if(z.getZaduzeni().toLowerCase().contains(convertString(zaduzeniSearch).toLowerCase())) {
        			zadaciFiltered.add(z);
        		}
        	}
            
            return resource;
        }

        // ignorisemo ostatak zaglavlja
        String s1;
        while (!(s1 = dis.readLine()).equals(""))
            System.out.println(s1);

        return resource;
    }


    private void sendResponse(String resource, OutputStream os)
            throws IOException
    {
        PrintStream ps = new PrintStream(os);

        // zamenimo web separator sistemskim separatorom
        resource = resource.replace('/', File.separatorChar);
        
        int idx = resource.indexOf("?");
        if(idx != -1)
        	resource = resource.substring(0, idx);
        
        File file = new File(resource);

        if (!file.exists())
        {
            // ako datoteka ne postoji, vratimo kod za gresku
            String errorCode = "HTTP/1.0 404 File not found\r\n"
                    + "Content-type: text/html; charset=UTF-8\r\n\r\n<b>404 Not found:"
                    + file.getName() + "</b>";

            ps.print(errorCode);

//            ps.flush();
            System.out.println("Could not find resource: " + file);
            return;
        }

        // ispisemo zaglavlje HTTP odgovora
        ps.print("HTTP/1.0 200 OK\r\n\r\n");
        
        if(resource.contains("Success")) {
        	ps.print("<html>");
        	ps.print("<head><meta charset=\"UTF-8\"></head>");
        	ps.print("HTTP. Pregled zadataka ");
        	ps.print("<table border=\"1\"><tr><th> Broj zadatka(id) </th><th> Naziv </th><th> Zaduzeni </th><th> Broj telefona </th><th>Tezina zadatka</th><th>Bodovi</th><th></th></tr>");
        	for(Zadatak zadatak : zadaci) {
        		if(zadatak.getTezinaZadatka() != TezinaZadatka.LAK)
        			ps.print("<tr>");
        		else
        			ps.print("<tr style=\"background-color:grey\">");
        		ps.print("<td>");
        		ps.print(zadatak.getBrojZadatka());
        		ps.print("</td>");
        		ps.print("<td>");
        		ps.print(zadatak.getNaziv());
        		ps.print("</td>");
        		ps.print("<td>");
        		ps.print(zadatak.getZaduzeni());
        		ps.print("</td>");
        		ps.print("<td>");
        		ps.print(zadatak.getBrojTelefona());
        		ps.print("</td>");
        		ps.print("<td>");
        		ps.print(zadatak.getTezinaZadatka());
        		ps.print("</td>");
        		ps.print("<td>");
        		ps.print(zadatak.getBodovi());
        		ps.print("</td>");
        		ps.print("<td>");
        			if(!zadatak.isZapocet())
        		ps.print("<a href=http://localhost/static/zadatakSuccess.html?id=" + zadatak.getBrojZadatka() +"> U toku </a>");
        		ps.print("</td>");
        		ps.print("</tr>");
        	}
        	ps.print("</table>");
        	ps.print("<h2> Pretraga zadatka</h2>\r\n"
        			+ "          <form action=\"http://localhost/static/zadatakSearch.html\" method=\"POST\">\r\n"
        			+ "              <table>\r\n"
        			+ "                  <tr>\r\n"
        			+ "                      <td>Unesite ime osobe koja je zaduzena za obavljanje zadatka: </td>\r\n"
        			+ "                      <td>\r\n"
        			+ "                          <input type=\"text\" class=\"fixed_width\" name=\"zaduzeniSearch\">\r\n"
        			+ "                      </td>\r\n"
        			+ "                  </tr>\r\n"
        			+ "                  <tr>\r\n"
        			+ "                      <td></td>\r\n"
        			+ "                      <td>\r\n"
        			+ "                          <input type=\"submit\" value=\"Pretrazi\">\r\n"
        			+ "                      </td>\r\n"
        			+ "                  </tr>\r\n"
        			+ "              </table>\r\n"
        			+ "          </form>");
        	ps.print("</html>");
        	ps.flush();
        	return;
        }
        
        if(resource.contains("Search")) {
        	ps.print("<html>");
        	ps.print("<head><meta charset=\"UTF-8\"></head>");
        	ps.print("HTTP. Pregled zadataka ");
        	if(zadaciFiltered.size() == 0) {
        		ps.print("<br>Ne postoji zadatak za koji je zaduzena osoba sa unetim imenom");
        	}
        	else {
        		ps.print("<table border=\"1\"><tr><th> Broj zadatka(id) </th><th> Naziv </th><th> Zaduzeni </th><th> Broj telefona </th><th>Tezina zadatka</th><th>Bodovi</th><th></th></tr>");
            	for(Zadatak zadatak : zadaciFiltered) {
            		if(zadatak.getTezinaZadatka() != TezinaZadatka.LAK)
            			ps.print("<tr>");
            		else
            			ps.print("<tr style=\"background-color:grey\">");
            		ps.print("<td>");
            		ps.print(zadatak.getBrojZadatka());
            		ps.print("</td>");
            		ps.print("<td>");
            		ps.print(zadatak.getNaziv());
            		ps.print("</td>");
            		ps.print("<td>");
            		ps.print(zadatak.getZaduzeni());
            		ps.print("</td>");
            		ps.print("<td>");
            		ps.print(zadatak.getBrojTelefona());
            		ps.print("</td>");
            		ps.print("<td>");
            		ps.print(zadatak.getTezinaZadatka());
            		ps.print("</td>");
            		ps.print("<td>");
            		ps.print(zadatak.getBodovi());
            		ps.print("</td>");
            		ps.print("<td>");
            			if(!zadatak.isZapocet())
            		ps.print("<a href=http://localhost/static/zadatakSuccess.html?id=" + zadatak.getBrojZadatka() +"> U toku </a>");
            		ps.print("</td>");
            		ps.print("</tr>");
            	}
            	ps.print("</table>");
            	ps.print("</html>");
            	ps.flush();
            	zadaciFiltered.clear();
        	}
        	
        	return;
        }
        
        // a, zatim datoteku
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[8192];
        int len;

        while ((len = fis.read(data)) != -1)
            ps.write(data, 0, len);

        ps.flush();
        fis.close();
    }

    public String convertString(String brljotina) {
    	String normalan = "";
    	try {
    		normalan = java.net.URLDecoder.decode(brljotina, StandardCharsets.UTF_8.name());
    	} catch (UnsupportedEncodingException e) {
    	    // not going to happen - value came from JDK's own StandardCharsets
    	}
    	return normalan;
    }
    
    public Zadatak validate(HashMap<String, String> map) {
    	String brojZadatka = map.get("brojZadatka");
        String naziv = map.get("naziv");
        String zaduzeni = map.get("zaduzeni");
        String brojTelefona = map.get("brojTelefona");
        String tezinaZadatkaStr = map.get("tezinaZadatka");
        String bodoviStr = map.get("bodovi");
        
        if(naziv == null || zaduzeni == null || brojTelefona == null || bodoviStr == null) {
        	return null;
        }
        
        System.out.println("Broj zadatka:  ---     " + brojZadatka);
        if(!brojZadatka.matches("[0-9]{4}\\/[0-9]{2}")) {
        	System.out.println(convertString(brojZadatka));
        	return null;
        }
        	
        
        TezinaZadatka tezinaEnum;
        if(tezinaZadatkaStr.charAt(0) == 'T')
        	tezinaEnum = TezinaZadatka.TEZAK;
        else
        	tezinaEnum = TezinaZadatka.LAK;
        

        int bodoviInt = 0;
        try {
			bodoviInt = Integer.parseInt(bodoviStr);
		} catch (Exception e) {
			return null;
		}
        
        Zadatak zadatak = new Zadatak(brojZadatka, naziv, zaduzeni, brojTelefona, tezinaEnum, bodoviInt, false);
        return zadatak;
    }
    
    public boolean isZadatakUnique(Zadatak zadatak) {
    	for(Zadatak z : zadaci) {
    		if(z.getBrojZadatka().equals(zadatak.getBrojZadatka())) {
    			return false;
    		}
    	}
    	return true;
    }
}
