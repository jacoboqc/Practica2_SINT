import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sint152P2 extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		int consulta = 0;
		int fase = 0;
		HttpSession sesion = req.getSession();
		if(!req.getParameterMap().isEmpty()){
			consulta = Integer.parseInt(req.getParameter("consulta"));
			fase = Integer.parseInt(req.getParameter("fase"));
		}
		saveCookie(req, sesion);
		printPage(res, sesion, consulta, fase);
	}

	void saveCookie(HttpServletRequest req, HttpSession sesion){
		Enumeration params = req.getParameterNames();
		while(params.hasMoreElements()){
			String nextParam = (String) params.nextElement();
			sesion.setAttribute(nextParam, req.getParameter(nextParam));
		}
	}

	void printPage(HttpServletResponse res, HttpSession sesion, int consulta, int fase) throws IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servicio de consulta</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div align=\"center\">");
		out.println("<h1>Servicio de consulta de información musical</h1>");
		printForm(out, sesion, consulta, fase);
		out.println("<hr>");
		out.println("Práctica de la asignatura Servicios de Internet - Curso 2015/16<br>");
		out.println("Escuela de Ingeniería de Telecomunicación - Universidad de Vigo<br>");
		out.println("Alumno: Jacobo Quintáns Castro<br>");
		out.println("<a href=\"mailto:jaquintans@alumnos.uvigo.es\">Contacto</a>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	void printForm(PrintWriter out, HttpSession sesion, int consulta, int fase){
		if(consulta==0 || fase==0){
			out.println("<h2>Selecciona una consulta:</h2>");
			out.println("<form>");
			out.println("<input type=\"radio\" name=\"consulta\" value=1 checked>Lista de canciones de un álbum<br>");
			out.println("<input type=\"radio\" name=\"consulta\" value=2>Número de canciones de un álbum<br>");
			out.println("<input type=\"hidden\" name=\"fase\" value=1><br>");
			out.println("<input type=\"submit\" value=\"enviar\">");
		}else if(consulta==1){
			if(fase==1){
				out.println("<h2>Selecciona un intérprete:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"interprete\" value=1 checked>Intérprete 1<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=2>Intérprete 2<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=3>Intérprete 3<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=4>Intérprete 4<br>");
				out.println("<input type=\"radio\" name=\"interprete\" value=todos>Todos<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if(fase==2){
				out.println("<h2>Selecciona un álbum:</h2>");
				out.println("<h3>Intérprete: "+sesion.getAttribute("interprete") + "</h3>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"album\" value=1 checked>Álbum 1<br>");
				out.println("<input type=\"radio\" name=\"album\" value=2>Álbum 2<br>");
				out.println("<input type=\"radio\" name=\"album\" value=3>Álbum 3<br>");
				out.println("<input type=\"radio\" name=\"album\" value=4>Álbum 4<br>");
								out.println("<input type=\"radio\" name=\"album\" value=todos>Todos<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if(fase==3){
				out.println("<h2>Listado de canciones especificadas:</h2>");
				out.println("<h3>Intérprete: "+sesion.getAttribute("interprete") + " Álbum: " + sesion.getAttribute("album") + "</h3>");
				out.println("<form>");
				out.println("<li>Canción 1 (duración, descripción)</li>");
				out.println("<li>Canción 2 (duración, descripción)</li>");
				out.println("<li>Canción 3 (duración, descripción)</li>");
				out.println("<li>Canción 4 (duración, descripción)</li><br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
			}
		}else if(consulta==2){
			if(fase==1){
				out.println("<h2>Selecciona un año:</h2>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"año\" value=1 checked>Año 1<br>");
				out.println("<input type=\"radio\" name=\"año\" value=2>Año 2<br>");
				out.println("<input type=\"radio\" name=\"año\" value=3>Año 3<br>");
				out.println("<input type=\"radio\" name=\"año\" value=4>Año 4<br>");
				out.println("<input type=\"radio\" name=\"año\" value=todos>Todos<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if(fase==2){
				out.println("<h2>Selecciona un álbum:</h2>");
				out.println("<h3>Año: "+sesion.getAttribute("año") + "</h3>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"album\" value=1 checked>Álbum 1<br>");
				out.println("<input type=\"radio\" name=\"album\" value=2>Álbum 2<br>");
				out.println("<input type=\"radio\" name=\"album\" value=3>Álbum 3<br>");
				out.println("<input type=\"radio\" name=\"album\" value=4>Álbum 4<br>");
				out.println("<input type=\"radio\" name=\"album\" value=todos>Todos<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if(fase==3){
				out.println("<h2>Selecciona un estilo:</h2>");
				out.println("<h3>Año: "+sesion.getAttribute("año") + " Álbum: " + sesion.getAttribute("album") + "</h3>");
				out.println("<form>");
				out.println("<input type=\"radio\" name=\"estilo\" value=1 checked>Estilo 1<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=2>Estilo 2<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=3>Estilo 3<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=4>Estilo 4<br>");
				out.println("<input type=\"radio\" name=\"estilo\" value=todos>Todos<br>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				out.println("<input type=\"submit\" value=\"enviar\">");
			}
			if(fase==4){
				out.println("<h2>El número de canciones es: 4</h2>");
				out.println("<h3>Año: "+sesion.getAttribute("año") + " Álbum: " + sesion.getAttribute("album")+ " Estilo: " + sesion.getAttribute("estilo") + "</h3>");
				out.println("<form>");
				out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
				out.println("<input type=\"hidden\" name=\"fase\" value=5><br>");
				out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
				out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
			}
		}
		out.println("</form>");
	}
}
