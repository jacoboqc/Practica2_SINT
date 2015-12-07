import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;

import java.util.*;

import javax.xml.xpath.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class Sint152P2 extends HttpServlet {
	public static ArrayList<Document> docs = new ArrayList<Document>();
	public static XML_DTD_ErrorHandler eh = new XML_DTD_ErrorHandler();

	public void init() throws ServletException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		Document doc = null;
		String path = "/home/jacoboqc/public_html/webapps/WEB-INF/radiohead.xml";
		String nextIML = "";
		ArrayList<String> imls = new ArrayList<String>();
		imls.add(path);
		for(int i = 0; i<imls.size(); i++){
			try{
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.setErrorHandler(eh);
				doc = db.parse(imls.get(i));
			}catch(ParserConfigurationException e){
				e.printStackTrace();
			}catch(SAXException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			XPath xpath= XPathFactory.newInstance().newXPath();
			Element raiz = doc.getDocumentElement();
			try{
				System.out.println("El nombre del elemento actual es: " + xpath.evaluate("/Interprete/Nombre/NombreG", raiz));
				nextIML = xpath.evaluate("//IML", raiz);
			}catch(XPathExpressionException e){
				e.printStackTrace();
			}
			if(!nextIML.isEmpty()){
				if(!nextIML.startsWith("/")){
					path = path.replaceFirst(new File(path).getName(), nextIML);
				}else path = nextIML;
				if(!imls.contains(path)) imls.add(path);
			}
			System.out.println("El IML del siguiente elemento es: " + nextIML);
			System.out.println("El path del siguiente elemento es: " + path);
			docs.add(doc);
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
		out.println("</head>");
		out.println("<body>");
		out.println("<div align=\"center\">");
		out.println("<h1>Servicio de consulta de información musical</h1>");
		if(!eh.warning.isEmpty()) out.println(eh.warning);
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
		try{
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
					getInterpreters(out);
					out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==2){
					String interprete = sesion.getAttribute("interprete").toString().replace("+", " ");
					out.println("<h2>Selecciona un álbum:</h2>");
					out.println("<h3>Intérprete: " + interprete + "</h3>");
					out.println("<form>");
					getAlbumsPerArtist(out, interprete);
					out.println("<input type=\"hidden\" name=\"consulta\" value=1><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					out.println("<input type=\"submit\" value=\"enviar\">");
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
					getYears(out);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=2><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==2){
					String año = sesion.getAttribute("año").toString();
					out.println("<h2>Selecciona un álbum:</h2>");
					out.println("<h3>Año: " + año + "</h3>");
					out.println("<form>");
					getAlbumsPerYear(out, año);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=3><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					out.println("<input type=\"submit\" value=\"enviar\">");
				}
				if(fase==3){
					String año = sesion.getAttribute("año").toString();
					String album = sesion.getAttribute("album").toString().replace("+", " ");
					out.println("<h2>Selecciona un estilo:</h2>");
					out.println("<h3>Año: " + año + " - Álbum: " + album + "</h3>");
					out.println("<form>");
					getStyle(out, año, album);
					out.println("<input type=\"hidden\" name=\"consulta\" value=2><br>");
					out.println("<input type=\"hidden\" name=\"fase\" value=4><br>");
					out.println("<input type=\"submit\" value=\"atrás\" onclick=\"form.fase.value="+(fase-1)+"\">");
					out.println("<input type=\"submit\" value=\"inicio\" onclick=\"form.consulta.value=0\">");
					out.println("<input type=\"submit\" value=\"enviar\">");
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
	
	//LISTO
	void getInterpreters(PrintWriter out) throws XPathExpressionException{
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
		out.println("<input type=\"radio\" name=\"interprete\" value=Todos>Todos<br>");
	}
	
	//LISTO
	void getAlbumsPerArtist(PrintWriter out, String artist) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		Map<Integer, List<String>> albums = new TreeMap<Integer, List<String>>();
		String expression = "";
		if(artist.equals("Todos")) expression = "//Album";
		else expression = "//Nombre[NombreG=\"" + artist + "\"]/../Album";
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
		out.println("<input type=\"radio\" name=\"album\" value=Todos>Todos<br>");
	}
	
	//LISTO
	void getSongs(PrintWriter out, String artist, String album) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		ArrayList<String> songs = new ArrayList<String>();
		String expression = "";
		if(album.equals("Todos")){
			if(artist.equals("Todos")) expression = "//Cancion";
			else expression = "//Nombre[NombreG=\"" + artist + "\"]/..//Cancion";
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
			out.println("<li>" + it.next() + "<l/i>");
		}
	}
	
	//LISTO
	void getYears(PrintWriter out) throws XPathExpressionException{
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
		out.println("<input type=\"radio\" name=\"año\" value=Todos>Todos<br>");
	}
	
	//LISTO
	void getAlbumsPerYear(PrintWriter out, String año) throws XPathExpressionException{
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
		out.println("<input type=\"radio\" name=\"album\" value=Todos>Todos<br>");
	}
	
	//LISTO
	void getStyle(PrintWriter out, String año, String album) throws XPathExpressionException{
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
		out.println("<input type=\"radio\" name=\"estilo\" value=Todos>Todos<br>");
	}
	
	//LISTO	
	void getNumber(PrintWriter out, String año, String album, String style) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		String expression = "";
		if(style.equals("Todos")){
			if(album.equals("Todos")){
				expression = "//Cancion";
			}else expression = "//Album[NombreA\"" + album + "\"]/Cancion";
		}else expression = "//Album[NombreA=\"" + album + "\"]/Cancion[@estilo=\"" + style + "\"]";
		NodeList node = null;
		int num = 0;
		for(int i = 0; i<docs.size(); i++){
			node = (NodeList) xpath.evaluate(expression, docs.get(i).getDocumentElement(), XPathConstants.NODESET);
			if(node.getLength()!=0) num = node.getLength();
		}
		out.println("<h2>El número de canciones es: " + num + "</h2>");
	}
	
}

class XML_DTD_ErrorHandler extends DefaultHandler {
	public static String warning = ""; 
	public XML_DTD_ErrorHandler () {}
	public void warning(SAXParseException spe) {
		warning = "Warning: "+spe.toString();
	}
	public void error(SAXParseException spe) {
		warning = "Error: "+spe.toString();
	}
	public void fatalerror(SAXParseException spe) {
		warning = "Fatal Error: "+spe.toString();
	}
}
