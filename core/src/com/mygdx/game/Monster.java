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
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private int id;
	private Modificator mod;
	private static String expression = "//monster[@name]";
	
	public Monster (int id) {
		List<String> monsterStats = readFromXML();
		
		this.mod = new Modificator();
		this.setId(id);
		this.name = monsterStats.get(0);	
		this.strength = Integer.parseInt(monsterStats.get(1)) + Integer.parseInt(mod.getStrength()); // should be changed to monsterStat + modificator in future!
		this.dexterity = Integer.parseInt(monsterStats.get(2)) + Integer.parseInt(mod.getDexterity());
		this.stamina = Integer.parseInt(monsterStats.get(3)) + Integer.parseInt(mod.getStamina());
		this.wrath = Integer.parseInt(monsterStats.get(4)) + Integer.parseInt(mod.getWrath());
	}
	
	private List<String> readFromXML() {
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
         
         int randomMonster = new Random().nextInt(nodeList.getLength());
         
         Node monsterNode = nodeList.item(randomMonster);
         
            if (monsterNode.getNodeType() == Node.ELEMENT_NODE) {
               Element monsterXMLElement = (Element) monsterNode;
               monsterList.add(monsterXMLElement.getAttribute("name"));
               System.out.println("Monster name : " 
                       + monsterXMLElement.getAttribute("name")); 
               NodeList listOfMonsterAttributes = monsterXMLElement.getElementsByTagName("*");
               
               for(int i=0; i<listOfMonsterAttributes.getLength(); i++) {
					monsterList.add(listOfMonsterAttributes.item(i).getTextContent());					
				}
               
               	System.out.println(monsterList.toString());
               
               	return monsterList;
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
	
	
    public String getStrength() {
		return strength + mod.getStrength();
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public String getDexterity() {
		return dexterity + mod.getDexterity();
	}
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}
	public String getStamina() {
		return stamina + mod.getStamina();
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public String getWrath() {
		return wrath + mod.getWrath();
	}
	public void setWrath(int wrath) {
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
		return name + " " + strength + " " + dexterity + " " + stamina + " " + wrath;
	} 
	
	/*public static void main(String[] args) {
		*Monster monster = new Monster(1);
		*System.out.println(monster);
		*System.out.println(monster.getMod());		
	*}
	*/
}