package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Zadatak;
import dao.ZadaciDAO;

@Path("/zadaci")
public class ZadaciService {
	@Context
	ServletContext ctx;
	
	public ZadaciService() {
		
	}
	
	@PostConstruct
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	public void init() {
		// Ovaj objekat se instancira vi�e puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("zadaciDAO") == null) {
			ctx.setAttribute("zadaciDAO", new ZadaciDAO());
		}
	}
	
	@POST
	@Path("/dodaj")
	@Produces(MediaType.APPLICATION_JSON)
	public Zadatak saveZadatak(Zadatak zadatak) {
		ZadaciDAO dao = (ZadaciDAO) ctx.getAttribute("zadaciDAO");
		
		boolean success = dao.addZadatak(zadatak);
		if(success)
			return zadatak;
		return null;
		
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Zadatak> getZadaci() {
		ZadaciDAO dao = (ZadaciDAO) ctx.getAttribute("zadaciDAO");
		return dao.getZadaci();
	}
	
	@GET
	@Path("/odradi")
	public void zarazi(@QueryParam("id") String id) {
		ZadaciDAO dao = (ZadaciDAO) ctx.getAttribute("zadaciDAO");
		dao.odradi(id);
	}
	
	@GET
	@Path("/filter")
	public void filtriraj(@QueryParam("id") String id) {
		ZadaciDAO dao = (ZadaciDAO) ctx.getAttribute("zadaciDAO");
		dao.filtriraj(id);
		
	}
	
	@GET
	@Path("/filteredZadaci")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Zadatak> getFiltrirani() {
		ZadaciDAO dao = (ZadaciDAO) ctx.getAttribute("zadaciDAO");
		return dao.getFiltrirani();
	}
}
