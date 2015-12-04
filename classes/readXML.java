import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.File;

import javax.xml.xpath.*;

public class readXML {
	public static void main (String args[]) throws XPathExpressionException{
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		Document[] docs = parseXML(dbf);
		getInterpreters(docs);
		getAlbums(docs);
		getSongs(docs);
		getYear(docs);
		getStyle(docs);
	}
	
	static Document[] parseXML (DocumentBuilderFactory dbf){
		Document doc = null;
		Document docs[] = new Document[3];
		String path = "/home/jacoboqc/public_html/webapps/WEB-INF/radiohead.xml";
		String nextIML = "first";
		int i = 0;
		while(!nextIML.isEmpty()){
			try{
				DocumentBuilder db = dbf.newDocumentBuilder();
				db.setErrorHandler(new XML_DTD_ErrorHandler());
				doc = db.parse(path);
			}catch(ParserConfigurationException | SAXException | IOException e){
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
			path = path.replaceFirst(new File(path).getName(), nextIML);
			System.out.println("El IML del siguiente elemento es: " + nextIML);
			System.out.println("El path del siguiente elemento es: " + path);
			docs[i] = doc;
			i++;
		}
		return docs;
	}
	
	static void getInterpreters(Document[] docs) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		for(int i = 0; i<3; i++){
			System.out.println(xpath.evaluate("/Interprete/Nombre/NombreG", docs[i].getDocumentElement()));
		}
	}
	
	static void getAlbums(Document[] docs) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		for(int i = 0; i<3; i++){
			NodeList node = (NodeList) xpath.evaluate("/Interprete/Album/NombreA", docs[i].getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				System.out.println(node.item(j).getTextContent());
			}
		}
	}
	
	static void getSongs(Document[] docs) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		for(int i = 0; i<3; i++){
			NodeList node = (NodeList) xpath.evaluate("/Interprete/Album/Cancion", docs[i].getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String nameSong = (String) xpath.evaluate("NombreT", node.item(j), XPathConstants.STRING);
				String timeSong = (String) xpath.evaluate("Duracion", node.item(j), XPathConstants.STRING);
				String description = ((String) xpath.evaluate("text()[normalize-space()]", node.item(j), XPathConstants.STRING)).trim(); 
				System.out.println(nameSong + timeSong + description);
			}
		}
	}
	
	static void getYear(Document[] docs) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		for(int i = 0; i<3; i++){
			NodeList node = (NodeList) xpath.evaluate("//Año", docs[i].getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				System.out.println(node.item(j).getTextContent());
			}
		}
	}
	
	static void getStyle(Document[] docs) throws XPathExpressionException{
		XPath xpath= XPathFactory.newInstance().newXPath();
		for(int i = 0; i<3; i++){
			NodeList node = (NodeList) xpath.evaluate("/Interprete/Album/Cancion", docs[i].getDocumentElement(), XPathConstants.NODESET);
			for(int j = 0; j<node.getLength(); j++){
				String nameStyle = (String) xpath.evaluate("@estilo", node.item(j), XPathConstants.STRING);
				System.out.println(nameStyle);
			}
		}
	}
}

class XML_DTD_ErrorHandler extends DefaultHandler {
	public XML_DTD_ErrorHandler () {}
	public void warning(SAXParseException spe) {
		System.out.println("Warning: "+spe.toString());
	}
	public void error(SAXParseException spe) {
		System.out.println("Error: "+spe.toString());
	}
	public void fatalerror(SAXParseException spe) {
		System.out.println("Fatal Error: "+spe.toString());
	}
}

