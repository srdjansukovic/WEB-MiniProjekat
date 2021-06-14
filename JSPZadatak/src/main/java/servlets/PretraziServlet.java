package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ZadaciDAO;

/**
 * Servlet implementation class PretraziServlet
 */
@WebServlet("/PretraziServlet")
public class PretraziServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PretraziServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		ZadaciDAO dao = (ZadaciDAO)getServletContext().getAttribute("zadaci");
		String nazivZadatka = request.getParameter("nazivSearch");
		
		dao.filterByNaziv(nazivZadatka);
		
		if(dao.getFiltrirani().size() == 0) {
			request.setAttribute("error", "Ne postoje zadaci sa navedenim nazivom.");
    		RequestDispatcher disp = request.getRequestDispatcher("/JSP/error.jsp");
	    	disp.forward(request, response);
		}
		else {
			RequestDispatcher disp = request.getRequestDispatcher("/JSP/filtrirani.jsp");
	    	disp.forward(request, response);
		}
	}

}
