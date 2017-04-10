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

public class Monster extends Character {
	private int wrath;
	private Modifier mod;
	private static String expression = "//monster[@name]";
	private static List<String> monsterStats;
	private ArrayList<Integer> pickedDefs = new ArrayList<Integer>();
	
	public static Monster getMonster() {
		
		monsterStats = readFromXML();
		String bodytype = monsterStats.get(1);
		
		int str = Integer.parseInt(monsterStats.get(2));
		int dex = Integer.parseInt(monsterStats.get(3));
		int stam =  Integer.parseInt(monsterStats.get(4));
		String name = monsterStats.get(0);	
		return new Monster(str, dex, stam, bodytype, name);
		
		
	}
	
	private Monster(int str, int dex, int stam, String bodytype, String name) {
		super(str, dex, stam, bodytype, name);
		this.mod = new Modifier();
		this.wrath = Integer.parseInt(monsterStats.get(5));
		setHp(getMaxHp());
	}
	
	private static List<String> readFromXML() {
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
               
               NodeList listOfMonsterAttributes = monsterXMLElement.getElementsByTagName("*");
               
               for(int i=0; i<listOfMonsterAttributes.getLength(); i++) {
					monsterList.add(listOfMonsterAttributes.item(i).getTextContent());					
				}
               
               //System.out.println(monsterList.toString());
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
	
	@Override
	public int getMaxHp() {
		return getModStrength()*13;
	}
	
	@Override
	public int getWrath() {
		return (int) Math.round(getModStrength()*getModStamina()*getModDexterity()*0.001);
	}
	
	@Override
	public float getEvasion() {
		return (float) (getModDexterity()*0.1);
	}
	
	
    public int getModStrength() {
		return getStrength() + mod.getStrength();
	}
	
	public int getModDexterity() {
		return getDexterity() + mod.getDexterity();
	}
	
	public int getModStamina() {
		return getStamina() + mod.getStamina();
	}
	
	public int getModWrath() {
		return wrath + mod.getWrath();
	}
	public void setWrath(int wrath) {
		this.wrath = wrath;
	}

	public Modifier getMod() {
		return mod;
	}	
	
	public ArrayList<Integer> getDefenceMoves() {
		return pickedDefs;
	}	
	
	@Override
	public int getMaxAttackDamage() {
		return (int) Math.round(getModStrength()*getModStamina()*0.2);
	}

	@Override
	public String toString() {
		//return name + " " + strength + " " + dexterity + " " + stamina + " " + wrath + "\n Modifier: " + mod;
		return getMod().getName() + " " + getName() + 
				"\n" + "HP: " + getHp() + "/" + getMaxHp() 
				+ "\n Attack: " + getAttackDamageStr();
	}

	
	
}