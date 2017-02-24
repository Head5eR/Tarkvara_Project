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
	private String name;
	private String strength;
	private String dexterity;
	private String stamina;
	private String wrath;
	private Modificator mod;
	private static String expression = "//monster[@name]";
	
	public Monster (int id) {
		this.mod = new Modificator(strength, dexterity, stamina, wrath);
		this.name = name;		
		this.strength = strength;
		this.dexterity = dexterity;
		this.stamina = stamina;
		this.wrath = wrath;
	}
	
	private String readFromXML() {
		try {
    	 File inputFile = new File("monsters.xml");
		    
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
        
         doc.getDocumentElement().normalize();
         
         XPath xPath =  XPathFactory.newInstance().newXPath();
        
         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
         
         List<String> monsterList = new ArrayList<String>();
         for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            System.out.println("\nCurrent Element : " 
               + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               monsterList.add(eElement.getAttribute("name"));
               System.out.println("Monster name : " 
                       + eElement.getAttribute("name"));               
            }
         } 
			String monsterName = monsterList.get(new Random().nextInt(monsterList.size()));
			return monsterName;
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
	
	
    public String getStrength() {
		return strength + mod.getStrength();
	}
	public void setStrength(String newStrength) {
		strength = newStrength;
	}
	public String getDexterity() {
		return dexterity + mod.getDexterity();
	}
	public void setDexterity(String newDexterity) {
		dexterity = newDexterity;
	}
	public String getStamina() {
		return stamina + mod.getStamina();
	}
	public void setStamina(String newStamina) {
		stamina = newStamina;
	}
	public String getWrath() {
		return wrath + mod.getWrath();
	}
	public void setWrath(String newWrath) {
		wrath = newWrath;
	}
	
}	