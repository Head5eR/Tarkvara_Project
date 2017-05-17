package com.mygdx.game;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Body implements Serializable {
	private String type;
	private ArrayList<Bodypart> bodyParts;
	
	private Body(String type, ArrayList<Bodypart> bodyParts) {
		this.type = type;
		this.bodyParts = bodyParts;
	}
	
	public Body(String type) {
		this.type = type;
		bodyParts = readFromXML(type);
	}
	
	private ArrayList<Bodypart> readFromXML(String type) {
		String expression = "//body[@type='" + type + "']";
		//String expression = "//body";
		try {
    	 File inputFile = new File("standartBody.xml");
		    
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
        
         doc.getDocumentElement().normalize();
         
         XPath xPath =  XPathFactory.newInstance().newXPath();
        
         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);              
         
         ArrayList<Bodypart> bodyList = new ArrayList<Bodypart>();
         
         
         
         Node bodyNode = nodeList.item(0);
         
            if (bodyNode.getNodeType() == Node.ELEMENT_NODE) {
               Element bodyXMLElement = (Element) bodyNode;
      
               
               NodeList listOfBodyparts = bodyXMLElement.getElementsByTagName("*");
               
               int counter;
               for(int i=0; i<listOfBodyparts.getLength(); i++) {
            	   if(((Element) listOfBodyparts.item(i)).getAttribute("count") != "") {
            		   counter = Integer.parseInt(((Element) listOfBodyparts.item(i)).getAttribute("count"));
            	   } else {
            		   counter = 1;
            	   }
            	   
            	   for(int a = counter; a > 0; a--) {
            		   if(((Element) listOfBodyparts.item(i)).getAttribute("critical") != "") {
                		   bodyList.add(new Bodypart(listOfBodyparts.item(i).getTextContent(), true));
                	   } else {
                		   bodyList.add(new Bodypart(listOfBodyparts.item(i).getTextContent(), false));
                	   }
        		   }
            	   	
				}
               	return bodyList;
            } 
            
			return null;
            
		} catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (XPathExpressionException e) {
         e.printStackTrace();
      }
		return null;
	}
	
	@Override
	public String toString() {
		return type + " " + bodyParts.toString();
	}
	
	public ArrayList<Bodypart> getBodyParts() {
		return bodyParts;
	}

	public String getType() {
		return type;
	}
}
