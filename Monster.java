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

public class Monster {
	private static String strength;
	private static String dexterity;
	private static String stamina;
	private static String wrath;
	
	
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
	
	public Monster (String randomName, String randomLevel, String strength, String dexterity, String stamina, String wrath) {
		Monster.strength = strength;
		Monster.dexterity = dexterity;
		Monster.stamina = stamina;
		Monster.wrath = wrath;
	}
	
	public static void main(String[] args) {
      try {
    	 File inputFile1 = new File("monsters.xml");
         File inputFile2 = new File("stats.xml");
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc1 = dBuilder.parse(inputFile1);
         Document doc2 = dBuilder.parse(inputFile2);
         doc1.getDocumentElement().normalize();
         doc2.getDocumentElement().normalize();

         XPath xPath =  XPathFactory.newInstance().newXPath();

         String expression1 = "//monster[@name]";
         String expression2 = "//difficulty[@level]";	        
         NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(doc1, XPathConstants.NODESET);
         NodeList nodeList2 = (NodeList) xPath.compile(expression2).evaluate(doc2, XPathConstants.NODESET);
         List<String> myList1 = new ArrayList<String>();
         for (int i = 0; i < nodeList1.getLength(); i++) {
            Node nNode1 = nodeList1.item(i);
            System.out.println("\nCurrent Element : " 
               + nNode1.getNodeName());
            if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement1 = (Element) nNode1;
               myList1.add(eElement1.getAttribute("name"));
               System.out.println("Monster name : " 
                       + eElement1.getAttribute("name"));               
            }
         }              
         
         List<String> myList2 = new ArrayList<String>();
         for (int i = 0; i < nodeList2.getLength(); i++) {
             Node nNode2 = nodeList2.item(i);
             System.out.println("\nCurrent Element : " 
                + nNode2.getNodeName());
             if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement2 = (Element) nNode2;
                myList2.add(eElement2.getAttribute("level"));
               System.out.println("Difficulty : " 
                  + eElement2.getAttribute("level"));
               System.out.println("Strength : " 
                  + eElement2
                     .getElementsByTagName("strength")
                     .item(0)
                     .getTextContent());
               System.out.println("Dexterity : " 
                  + eElement2
                     .getElementsByTagName("dexterity")
                     .item(0)
                     .getTextContent());
               System.out.println("Stamina : " 
                  + eElement2
                     .getElementsByTagName("stamina")
                     .item(0)
                     .getTextContent());
               System.out.println("Wrath : " 
                  + eElement2
                     .getElementsByTagName("wrath")
                     .item(0)
                     .getTextContent());
            }
         }
         
         int x = new Random().nextInt(myList1.size());
         int y = new Random().nextInt(myList2.size());
         
         String randomName = myList1.get(x);
         System.out.println(randomName);
         
         String randomLevel = myList2.get(y);
         System.out.println(randomLevel);
         
         Element levels = (Element) nodeList2;
         
         NodeList strength = levels.getElementsByTagName("strength");
         NodeList dexterity = levels.getElementsByTagName("dexterity");
         NodeList stamina = levels.getElementsByTagName("stamina");
         NodeList wrath = levels.getElementsByTagName("wrath");
         
         
         setStrength(strength.item(y).getFirstChild().getTextContent());
         setDexterity(dexterity.item(y).getFirstChild().getTextContent());
         setStamina(stamina.item(y).getFirstChild().getTextContent());
         setWrath(wrath.item(y).getFirstChild().getTextContent());        	 
         
         
         
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (XPathExpressionException e) {
         e.printStackTrace();
      }
      Monster m = new Monster(null, null, null, null, null, null);
      System.out.println(m);
   }	
}
