import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sint152P2 extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		int consulta, fase, interprete, album, año, estilo;
		consulta = fase = interprete = album = año = estilo = 0;
		try{
			if(!req.getParameterMap().isEmpty()){
				consulta = Integer.parseInt(req.getParameter("consulta"));
				fase = Integer.parseInt(req.getParameter("fase"));
				interprete = Integer.parseInt(req.getParameter("interprete"));
				album = Integer.parseInt(req.getParameter("album"));
				año = Integer.parseInt(req.getParameter("año"));
				estilo = Integer.parseInt(req.getParameter("estilo"));	
			}
		}catch(NumberFormatException e){
				System.out.println("Error de parseo de int");
		}
		printPage(res);
	}
	
	void printPage(HttpServletResponse res) throws IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servicio de consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div align=\"center\">");
		out.println("<h1>Servicio de consulta de información musical</h1>");
		
		out.println("<h2>Selecciona una consulta:</h2>");
		
		out.println("<form>");
		out.println("<input type=\"radio\" name=\"consulta\" value=1 checked>Lista de canciones de un álbum<br>");
		out.println("<input type=\"radio\" name=\"consulta\" value=2>Número de canciones de un álbum<br>");
		out.println("<input type=\"submit\" value=\"enviar\">");
		out.println("</form>");
		out.println("<hr>");
		out.println("Práctica de la asignatura Servicios de Internet - Curso 2015/16<br>");
		out.println("Escuela de Ingeniería de Telecomunicación - Universidad de Vigo<br>");
		out.println("Alumno: Jacobo Quintáns Castro<br>");
		out.println("<a href=\"mailto:jaquintans@alumnos.uvigo.es\">Contacto</a>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}
}
