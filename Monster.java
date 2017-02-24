package com.mygdx.game;

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
	private int id;
	private Modificator mod;
	private static String expression = "//monster[@name]";
	
	public Monster (int id) {
		this.mod = new Modificator();
		this.setId(id);
		this.name = readFromXML();		
		this.strength = mod.getStrength(); // should be changed to monsterStat + modificator in future!
		this.dexterity = mod.getDexterity();
		this.stamina = mod.getStamina();
		this.wrath = mod.getWrath();
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
            Node monsterNode = nodeList.item(i);
            System.out.println("\nCurrent Element : " 
               + monsterNode.getNodeName());
            if (monsterNode.getNodeType() == Node.ELEMENT_NODE) {
               Element monsterXMLElement = (Element) monsterNode;
               monsterList.add(monsterXMLElement.getAttribute("name"));
               System.out.println("Monster name : " 
                       + monsterXMLElement.getAttribute("name"));               
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
	public void setStrength(String strength) {
		this.strength = strength;
	}
	public String getDexterity() {
		return dexterity + mod.getDexterity();
	}
	public void setDexterity(String dexterity) {
		this.dexterity = dexterity;
	}
	public String getStamina() {
		return stamina + mod.getStamina();
	}
	public void setStamina(String stamina) {
		this.stamina = stamina;
	}
	public String getWrath() {
		return wrath + mod.getWrath();
	}
	public void setWrath(String wrath) {
		this.wrath = wrath;
	}

	public int getId() {
		return id;
	}
	
	public Modificator getMod() {
		return mod;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return name + " " + strength + " " + dexterity + " " + wrath + " " + stamina;
	} 
	
}	