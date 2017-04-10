package com.mygdx.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class Weapon extends Item {
	public Weapon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Weapon(String name, boolean equipable) {
		super(name, equipable);
		// TODO Auto-generated constructor stub
	}

	public Weapon(String name, int rarity, boolean equipable) {
		super(name, rarity, equipable);
		// TODO Auto-generated constructor stub
	}
	
	public static List<String> readFromXML() {
		try {
    	 File inputFile = new File("weapons.xml");
		    
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
        
         doc.getDocumentElement().normalize();
         
         XPath xPath =  XPathFactory.newInstance().newXPath();
        
         NodeList nodeList = (NodeList) xPath.compile("//weapon[@type]").evaluate(doc, XPathConstants.NODESET);              
         
         List<String> weaponList = new ArrayList<String>();
         
         int randomWeapon = new Random().nextInt(nodeList.getLength());
         
         Node weaponNode = nodeList.item(randomWeapon);
         
            if (weaponNode.getNodeType() == Node.ELEMENT_NODE) {
               Element weaponXMLElement = (Element) weaponNode;
               weaponList.add(weaponXMLElement.getAttribute("type"));
               
               NodeList listOfWeapons = weaponXMLElement.getElementsByTagName("*");
               
               for(int i=0; i<listOfWeapons.getLength(); i++) {
					weaponList.add(listOfWeapons.item(i).getTextContent());					
               }
               
               System.out.println(weaponList.toString());
               		return weaponList;
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
}
