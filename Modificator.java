package tarkvaraproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Modificator {
	private String name;
	private static String strength;
	private static String dexterity;
	private static String stamina;
	private static String wrath;
	private static String expression = "//difficulty[@level]";	        
	
	public Modificator(String st, String dex, String stam, String wrath) {
		List<String> stats = readFromXML();
		
		this.name = stats.get(0);
		this.strength = stats.get(1);
		this.dexterity = stats.get(2);
		this.stamina = stats.get(3);
		this.wrath = stats.get(4);
	}
	
	private List<String> readFromXML() {
		try {
			File inputFile = new File("stats.xml");
			
			DocumentBuilderFactory dbFactory 
				= DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder;

			 dBuilder = dbFactory.newDocumentBuilder();
			 
			 Document doc = dBuilder.parse(inputFile);
			 doc.getDocumentElement().normalize();
			 
			 XPath xPath =  XPathFactory.newInstance().newXPath();
			 
			 NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			 
			 List<String> statList = new ArrayList<String>();
			 
			 int y = new Random().nextInt(nodeList.getLength());
			 
			
			 
			 Node nNode = nodeList.item(y);
			 System.out.println("\nCurrent Element : " 
				+ nNode.getNodeName());
			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				statList.add(eElement.getAttribute("level"));
			   System.out.println("Difficulty : " 
				  + eElement.getAttribute("level"));
			   System.out.println("Strength : " 
				  + eElement
					 .getElementsByTagName("strength")
					 .item(0)
					 .getTextContent());
					 statList.add(eElement
					 .getElementsByTagName("strength")
					 .item(0)
					 .getTextContent());
			   System.out.println("Dexterity : " 
				  + eElement
					 .getElementsByTagName("dexterity")
					 .item(0)
					 .getTextContent());
					 statList.add(eElement
					 .getElementsByTagName("dexterity")
					 .item(0)
					 .getTextContent());
			   System.out.println("Stamina : " 
				  + eElement
					 .getElementsByTagName("stamina")
					 .item(0)
					 .getTextContent());
					 statList.add(eElement
					 .getElementsByTagName("stamina")
					 .item(0)
					 .getTextContent());
			   System.out.println("Wrath : " 
				  + eElement
					 .getElementsByTagName("wrath")
					 .item(0)
					 .getTextContent());
					 statList.add(eElement
					 .getElementsByTagName("wrath")
					 .item(0)
					 .getTextContent());
				return statList;
			}
			return null;
		}
		catch (ParserConfigurationException e) {
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
	
	
	public String getStrength() {
		return strength;
	}
	public static void setStrength(String newStrength) {
		strength = newStrength;
	}
	public String getDexterity() {
		return dexterity;
	}
	public static void setDexterity(String newDexterity) {
		dexterity = newDexterity;
	}
	public String getStamina() {
		return stamina;
	}
	public static void setStamina(String newStamina) {
		stamina = newStamina;
	}
	public String getWrath() {
		return wrath;
	}
	public static void setWrath(String newWrath) {
		wrath = newWrath;
	}
}
