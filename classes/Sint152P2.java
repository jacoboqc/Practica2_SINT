import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;

import java.io.*;

import java.util.*;

import javax.xml.xpath.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class Sint152P2 extends HttpServlet {
	public static ArrayList<Document> docs = new ArrayList<Document>();
	public static ArrayList<String> errors = new ArrayList<String>();

	public void init() throws ServletException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		Document doc = null;
		String path = "http://clave.det.uvigo.es:8080/~sintprof/15-16/p2/sabina.xml";
		String relative = path.replaceFirst(new File(path).getName(), "");
		NodeList nextIMLs = null;
		String nextIML = "";
		ArrayList<String> imls = new ArrayList<String>();
		imls.add(path);
		for(int i = 0; i<imls.size(); i++){
			try{
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.setErrorHandler(new XML_DTD_ErrorHandler());
				doc = db.parse(new URL(imls.get(i)).openStream(), "http://localhost:8152/sint152/");
				XPath xpath= XPathFactory.newInstance().newXPath();
				Element raiz = doc.getDocumentElement();
				nextIMLs = (NodeList) xpath.evaluate("//IML", raiz, XPathConstants.NODESET);
				for(int j = 0; j<nextIMLs.getLength(); j++){
					nextIML = nextIMLs.item(j).getTextContent();
					if(!nextIML.startsWith("http://")){
						path = relative + nextIML;
					}else path = nextIML;
					if(!imls.contains(path)) imls.add(path);
				}
				docs.add(doc);
			}catch(XPathExpressionException e){
				errors.add("En " + path + ": " + e.getMessage());
				break;
			}catch(ParserConfigurationException e){
				errors.add("En " + path + ": " + e.getMessage());
				break;
			}catch(SAXException e){
				errors.add("En " + path + ": " + e.getMessage());
				break;
			}catch(IOException e){
				errors.add("En " + path + ": " + e.getMessage());
				break;
			}
		}
	}
	
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
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"iml.css\">");
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
		boolean flag;
		try{
			if(consulta==0 || fase==0){
				if(!errors.isEmpty()){
					out.println("<div class=\"table\">");
					out.println("<strong>Ha habido errores de parseo:</strong><br>");
					Iterator it = errors.iterator();
					while(it.hasNext()){
						out.println(it.next() + "<br>");
					}
					if(docs.size()==0) out.println("No se ha podido parsear ningún fichero<br>");
					else out.println("Sólo se han podido parsear " + docs.size() + " fichero(s).<br>");
					out.println("</div>");
				}
				if(!docs.isEmpty()){
					out.println("<h2>Selecciona una consulta:</h2>");
					out.println("<form>");
					out.println("<input type=\"radio\" name=\"consulta\" value=1 checked>Lista de canciones de un álbum<br>");
					out.println("<input type=\"radio\" name=\"consulta\" value=2>Número de canciones de un álbum<br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=1><br>");
					out.println("<input type=\"submit\" value=\"enviar\">");
				}
			}else if(consulta==1){
				if(fase==1){
					out.println("<h2>Selecciona un intérprete:</h2>");
					out.println("<form>");
					flag = getInterpreters(out);
					out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					if(flag) out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==2){
					String interprete = sesion.getAttribute("interprete").toString().replace("+", " ");
					out.println("<h2>Selecciona un álbum:</h2>");
					out.println("<h3>Intérprete: " + interprete + "</h3>");
					out.println("<form>");
					flag = getAlbumsPerArtist(out, interprete);
					out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					if(flag) out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==3){
					String interprete = sesion.getAttribute("interprete").toString().replace("+", " ");
					String album = sesion.getAttribute("album").toString().replace("+", " ");
					out.println("<h2>Listado de canciones especificadas:</h2>");
					out.println("<h3>Intérprete: " + interprete + " - Álbum: " + album + "</h3>");
					out.println("<form>");
					getSongs(out, interprete, album);
					out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				}
			}else if(consulta==2){
				if(fase==1){
					out.println("<h2>Selecciona un año:</h2>");
					out.println("<form>");
					flag = getYears(out);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					if(flag) out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==2){
					String año = sesion.getAttribute("año").toString();
					out.println("<h2>Selecciona un álbum:</h2>");
					out.println("<h3>Año: " + año + "</h3>");
					out.println("<form>");
					flag = getAlbumsPerYear(out, año);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					if(flag) out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==3){
					String año = sesion.getAttribute("año").toString();
					String album = sesion.getAttribute("album").toString().replace("+", " ");
					out.println("<h2>Selecciona un estilo:</h2>");
					out.println("<h3>Año: " + año + " - Álbum: " + album + "</h3>");
					out.println("<form>");
					flag = getStyle(out, año, album);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					if(flag) out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==4){
					String año = sesion.getAttribute("año").toString();
					String album = sesion.getAttribute("album").toString().replace("+", " ");
					String estilo = sesion.getAttribute("estilo").toString().replace("+", " ");
					getNumber(out, año, album, estilo);
					out.println("<h3>Año: " + año + " - Álbum: " + album + " - Estilo: " + estilo + "</h3>");
					out.println("<form>");
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=5><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
				}
			}
			out.println("</form>");
		}catch(XPathExpressionException e){
			e.printStackTrace();
		}
	}
	
	boolean getInterpreters(PrintWriter out) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		ArrayList<String> interpreters = new ArrayList<String>();
		for(int i = 0; i<docs.size(); i++){
			interpreters.add((String) xpath.evaluate("//NombreG | //NombreC", docs.get(i).getDocumentElement(), XPathConstants.STRING));
		}
		Collections.sort(interpreters);
		Iterator it = interpreters.iterator();
		String checked = " checked";
		while(it.hasNext()){
			String interpreter = (String) it.next();
			out.println("<input type=\"radio\" name=\"interprete\" value=" + interpreter.replace(" ", "+") + checked + ">" + interpreter + "<br>");
			checked = "";
		}
		if(interpreters.size()!=0){
			out.println("<input type=\"radio\" name=\"interprete\" value=Todos>Todos<br>");
			return true;
		}else{
			out.println("<strong>Ups! Tu consulta no ha devuelto ningún resultado</strong><br>");
			return false;
		}			
	}
	
	boolean getAlbumsPerArtist(PrintWriter out, String artist) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		Map<Integer, List<String>> albums = new TreeMap<Integer, List<String>>();
		String expression = "";
		if(artist.equals("Todos")) expression = "//Album";
		else expression = "//Nombre[NombreG=\"" + artist + "\" or NombreC=\"" + artist + "\"]/../Album";
		for(int i = 0; i<docs.size(); i++){
			NodeList node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String album = (String) xpath.evaluate("NombreA", node.item(j), XPathConstants.STRING);
				String year = (String) xpath.evaluate("Año", node.item(j), XPathConstants.STRING);
				List<String> sameYear = albums.get(Integer.parseInt(year));
				if(sameYear == null) sameYear = new ArrayList<String>();
				sameYear.add(album);
				albums.put(Integer.parseInt(year), sameYear);
			}
		}
		Iterator it = albums.keySet().iterator();
		String checked = " checked";
		while(it.hasNext()){
			List<String> sameYear = albums.get(it.next());
			ListIterator<String> listIt = sameYear.listIterator();
			while(listIt.hasNext()){
				String album = listIt.next();
				out.println("<input type=\"radio\" name=\"album\" value=" + album.replace(" ", "+") + checked + ">" + album + "<br>");
				checked = "";
			}
		}
		if(albums.size()!=0){
			out.println("<input type=\"radio\" name=\"album\" value=Todos>Todos<br>");
			return true;
		}else{
			out.println("<strong>Ups! Tu consulta no ha devuelto ningún resultado</strong><br>");
			return false;
		}
	}
	
	void getSongs(PrintWriter out, String artist, String album) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		ArrayList<String> songs = new ArrayList<String>();
		String expression = "";
		if(album.equals("Todos")){
			if(artist.equals("Todos")) expression = "//Cancion";
			else expression = "//Nombre[NombreG=\"" + artist + "\" or NombreC=\"" + artist + "\"]/..//Cancion";
		}else expression = "//Album[NombreA=\"" + album + "\"]/Cancion";
		String str;
		for(int i = 0; i<docs.size(); i++){
			NodeList node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String nameSong = (String) xpath.evaluate("NombreT", node.item(j), XPathConstants.STRING);
				String timeSong = (String) xpath.evaluate("Duracion", node.item(j), XPathConstants.STRING);
				String description = ((String) xpath.evaluate("text()[normalize-space()]", node.item(j), XPathConstants.STRING)).trim(); 
				str = nameSong + " - " +timeSong;
				if(!description.isEmpty()){
					str += " (" + description + ")";
				}
				songs.add(str);
			}
		}
		Iterator it = songs.iterator();
		while(it.hasNext()){
			out.println("<li>" + it.next() + "</li>");
		}
	}
	
	boolean getYears(PrintWriter out) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		ArrayList<Integer> years = new ArrayList<Integer>();
		for(int i = 0; i<docs.size(); i++){
			NodeList node = (NodeList) xpath.evaluate("//Año", docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				Integer year = Integer.parseInt(node.item(j).getTextContent());
				if(!years.contains(year)){
					years.add(year);
				}
			}
		}
		Collections.sort(years);
		Iterator it = years.iterator();
		String checked = " checked";
		while(it.hasNext()){
			Integer year = (Integer) it.next();
			out.println("<input type=\"radio\" name=\"año\" value=" + year + checked + ">" + year + "<br>");
			checked = "";
		}
		if(years.size()!=0){
			out.println("<input type=\"radio\" name=\"año\" value=Todos>Todos<br>");
			return true;
		}else{
			out.println("<strong>Ups! Tu consulta no ha devuelto ningún resultado</strong><br>");
			return false;
		}
	}
	
	boolean getAlbumsPerYear(PrintWriter out, String año) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		Map<Integer, List<String>> albums = new TreeMap<Integer, List<String>>();
		String expression = "";
		if(año.equals("Todos")) expression = "//Album";
		else expression = "//Album[Año=\"" + año + "\"]";
		for(int i = 0; i<docs.size(); i++){
			NodeList node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String album = (String) xpath.evaluate("NombreA", node.item(j), XPathConstants.STRING);
				String year = (String) xpath.evaluate("Año", node.item(j), XPathConstants.STRING);
				List<String> sameYear = albums.get(Integer.parseInt(year));
				if(sameYear == null) sameYear = new ArrayList<String>();
				sameYear.add(album);
				albums.put(Integer.parseInt(year), sameYear);				
			}
		}
		Iterator it = albums.keySet().iterator();
		String checked = " checked";
		while(it.hasNext()){
			List<String> sameYear = albums.get(it.next());
			ListIterator<String> listIt = sameYear.listIterator();
			while(listIt.hasNext()){
				String album = listIt.next();
				out.println("<input type=\"radio\" name=\"album\" value=" + album.replace(" ", "+") + checked + ">" + album + "<br>");
				checked = "";
			}
		}
		if(albums.size()!=0){
			out.println("<input type=\"radio\" name=\"album\" value=Todos>Todos<br>");
			return true;
		}else{
			out.println("<strong>Ups! Tu consulta no ha devuelto ningún resultado</strong><br>");
			return false;
		}
	}
	
	boolean getStyle(PrintWriter out, String año, String album) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		ArrayList<String> styles = new ArrayList<String>();
		String expression = "";
		if(album.equals("Todos")){
			if(año.equals("Todos")) expression = "//Cancion";
			else expression = "//Album[Año=\"" + año + "\"]/Cancion";
		}else expression = "//Album[NombreA=\"" + album + "\"]/Cancion";	
		for(int i = 0; i<docs.size(); i++){
			NodeList node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String style = (String) xpath.evaluate("@estilo", node.item(j), XPathConstants.STRING);
				if(!styles.contains(style)){
					styles.add(style);
				}
			}
		}
		Iterator it = styles.iterator();
		String checked = " checked";
		while(it.hasNext()){
			String style = (String) it.next();
			out.println("<input type=\"radio\" name=\"estilo\" value=" + style.replace(" ", "+") + checked + ">" + style + "<br>");
			checked = "";
		}
		if(styles.size()!=0){
			out.println("<input type=\"radio\" name=\"estilo\" value=Todos>Todos<br>");
			return true;
		}else{
			out.println("<strong>Ups! Tu consulta no ha devuelto ningún resultado</strong><br>");
			return false;
		}
	}
	
	void getNumber(PrintWriter out, String año, String album, String style) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		String expression = "";
		if(año.equals("Todos")){
			if(album.equals("Todos")){
				if(style.equals("Todos")) expression = "//Cancion";
				else expression = "//Cancion[@estilo=\"" + style + "\"]";
			}else{
				if(style.equals("Todos")) expression = "//Album[NombreA=\"" + album + "\"]/Cancion";
				else expression = "//Album[NombreA=\"" + album + "\"]/Cancion[@estilo=\"" + style + "\"]";
			}
		}else{
			if(album.equals("Todos")){
				if(style.equals("Todos")) expression = "//Album[Año=\"" + año + "\"]/Cancion";
				else expression = "//Album[Año=\"" + año + "\"]/Cancion[@estilo=\"" + style + "\"]";
			}else{
				if(style.equals("Todos")) expression = "//Album[NombreA=\"" + album + "\" and Año=\"" + año + "\"]/Cancion";
				else expression = "//Album[NombreA=\"" + album + "\" and Año=\"" + año + "\"]/Cancion[@estilo=\"" + style + "\"]";
			}
		}
		NodeList node = null;
		int num = 0;
		for(int i = 0; i<docs.size(); i++){
			node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			num += node.getLength();
		}
		out.println("<h2>El número de canciones es: " + num + "</h2>");
	}
	
}

class XML_DTD_ErrorHandler extends DefaultHandler {
	public XML_DTD_ErrorHandler () {}
	public void warning(SAXParseException spe)throws SAXException {
		String warning = "Warning: "+spe.getMessage();
		throw new SAXException(warning);
	}
	public void error(SAXParseException spe)throws SAXException {
		String warning = "Error: "+spe.getMessage();
		throw new SAXException(warning);
	}
	public void fatalerror(SAXParseException spe)throws SAXException {
		String warning = "Fatal Error: "+spe.getMessage();
		throw new SAXException(warning);
	}
}
