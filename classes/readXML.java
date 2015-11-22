import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

import javax.xml.xpath.*;

public class readXML {
	public static void main (String args[]){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new XML_DTD_ErrorHandler());
			Document doc = db.parse("/home/jacoboqc/public_html/webapps/WEB-INF/radiohead.xml");
			System.out.println("El tipo de documento es: " + doc.getDoctype().getName());
			XPath xpath= XPathFactory.newInstance().newXPath();
		}catch(ParserConfigurationException | SAXException | IOException e){
			e.printStackTrace();
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

