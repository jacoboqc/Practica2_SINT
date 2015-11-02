import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sint152P2 extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		Map<String, Integer> vars = createMap(req);
		printPage(res, vars);
	}

	Map<String, Integer> createMap(HttpServletRequest req){
		int consulta, fase, interprete, album, año, estilo;
		consulta = fase = interprete = album = año = estilo = 0;
		Map<String, Integer> vars = new HashMap<String, Integer>();
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
		vars.put("consulta", consulta);
		vars.put("fase", fase);
		vars.put("interprete", interprete);
		vars.put("album", album);
		vars.put("año", año);
		vars.put("estilo", estilo);
		return vars;
	}

	void printPage(HttpServletResponse res, Map vars) throws IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servicio de consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div align=\"center\">");
		out.println("<h1>Servicio de consulta de información musical</h1>");
		printForm(out, vars);
		out.println("<hr>");
		out.println("Práctica de la asignatura Servicios de Internet - Curso 2015/16<br>");
		out.println("Escuela de Ingeniería de Telecomunicación - Universidad de Vigo<br>");
		out.println("Alumno: Jacobo Quintáns Castro<br>");
		out.println("<a href=\"mailto:jaquintans@alumnos.uvigo.es\">Contacto</a>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	void printForm(PrintWriter out, Map vars){
		if((Integer)vars.get("consulta")==1){
			if((Integer)vars.get("fase")==1){
				out.println("<h2>Selecciona un intérprete:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"interprete\" value=1 checked>Intérprete 1<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=2>Intérprete 2<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=3>Intérprete 3<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=4>Intérprete 4<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if((Integer)vars.get("fase")==2){
				out.println("<h2>Selecciona un álbum:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"album\" value=1 checked>Álbum 1<br>");
				out.println("<input type=\"radio\" name=\"album\" value=2>Álbum 2<br>");
				out.println("<input type=\"radio\" name=\"album\" value=3>Álbum 3<br>");
				out.println("<input type=\"radio\" name=\"album\" value=4>Álbum 4<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if((Integer)vars.get("fase")==3){
				out.println("<h2>Listado de canciones especificadas:</h2>");
				out.println("<form>");
				out.println("<li>Canción 1 (duración, descripción)</li>");
				out.println("<li>Canción 2 (duración, descripción)</li>");
				out.println("<li>Canción 3 (duración, descripción)</li>");
				out.println("<li>Canción 4 (duración, descripción)</li><br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
			}
		}else if((Integer)vars.get("consulta")==2){
			if((Integer)vars.get("fase")==1){
				out.println("<h2>Selecciona un año:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"año\" value=1 checked>Año 1<br>");
				out.println("<input type=\"radio\" name=\"año\" value=2>Año 2<br>");
				out.println("<input type=\"radio\" name=\"año\" value=3>Año 3<br>");
				out.println("<input type=\"radio\" name=\"año\" value=4>Año 4<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if((Integer)vars.get("fase")==2){
				out.println("<h2>Selecciona un álbum:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"album\" value=1 checked>Álbum 1<br>");
				out.println("<input type=\"radio\" name=\"album\" value=2>Álbum 2<br>");
				out.println("<input type=\"radio\" name=\"album\" value=3>Álbum 3<br>");
				out.println("<input type=\"radio\" name=\"album\" value=4>Álbum 4<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if((Integer)vars.get("fase")==3){
				out.println("<h2>Selecciona un estilo:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"estilo\" value=1 checked>Estilo 1<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=2>Estilo 2<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=3>Estilo 3<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=4>Estilo 4<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if((Integer)vars.get("fase")==4){
				out.println("<h2>El número de canciones es: 4</h2>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
			}
		}else if((Integer)vars.get("consulta")==0){
			out.println("<h2>Selecciona una consulta:</h2>");
			out.println("<form>");
			out.println("<input type=\"radio\" name=\"consulta\" value=1 checked>Lista de canciones de un álbum<br>");
			out.println("<input type=\"radio\" name=\"consulta\" value=2>Número de canciones de un álbum<br>");
			out.println("<input type=\"hidden\" name=\"fase\" value=1><br>");
			out.println("<input type=\"submit\" value=\"enviar\">");
		}
		out.println("</form>");
	}
}
