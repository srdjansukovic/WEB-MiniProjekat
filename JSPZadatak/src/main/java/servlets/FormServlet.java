package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.TezinaZadatka;
import beans.Zadatak;
import dao.ZadaciDAO;

/**
 * Servlet implementation class FormServlet
 */
@WebServlet("/FormServlet")
public class FormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
    	// Dodaju se korisnici u kontekst kako bi mogli servleti da rade sa njima
    	context.setAttribute("zadaci", new ZadaciDAO());
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String brojZadatka = request.getParameter("brojZadatka");
		String naziv = request.getParameter("naziv");
		String zaduzeni = request.getParameter("zaduzeni");
		String brojTelefona = request.getParameter("brojTelefona");
		String tezinaZadatkaStr = request.getParameter("tezinaZadatka");
		String bodoviStr = request.getParameter("bodovi");
		
		TezinaZadatka tezinaEnum;
		if(tezinaZadatkaStr.charAt(0) == 'T')
			tezinaEnum = TezinaZadatka.TEZAK;
        else
        	tezinaEnum = TezinaZadatka.LAK;
		
		int bodoviInt = 0;
        try {
			bodoviInt = Integer.parseInt(bodoviStr);
		} catch (Exception e) {
			request.setAttribute("error", "Bodovi pogresno uneti.");
		}
        
        if(!brojZadatka.matches("[0-9]{4}\\/[0-9]{2}")) {
        	request.setAttribute("error", "Format broja zadatka: 0000/00.");
        }
        
        if(naziv == "" || naziv == null || zaduzeni == "" || zaduzeni == null || brojTelefona == "" || brojTelefona == null) {
        	request.setAttribute("error", "Polja ne smeju biti prazna.");
        }
        
        ZadaciDAO zadaciDAO = (ZadaciDAO) getServletContext().getAttribute("zadaci");
        Zadatak noviZadatak;
        if(request.getAttribute("error") == null) {
        	noviZadatak = new Zadatak(brojZadatka, naziv, zaduzeni, brojTelefona, tezinaEnum, bodoviInt, false);
        	boolean success = zadaciDAO.addZadatak(noviZadatak);
        	if(success) {
        		RequestDispatcher disp = request.getRequestDispatcher("/ZadaciPrikazServlet");
    	    	disp.forward(request, response);
        	}
        	else {
        		request.setAttribute("error", "Zadatak vec postoji.");
        		RequestDispatcher disp = request.getRequestDispatcher("/JSP/error.jsp");
    	    	disp.forward(request, response);
        	}
        }
        else {
        	RequestDispatcher disp = request.getRequestDispatcher("/JSP/error.jsp");
	    	disp.forward(request, response);
        }
        
	}

}
