package com.mygdx.game;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Armor extends Item {
	private int armor;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private String type;
	private String level;
	
	public Armor(int str, int dex, int stam, int wrath, String type, String level, String name, int armor) {
		super(name,true);
		this.armor = armor;
		this.strength = str;
		this.stamina = stam;
		this.wrath = wrath;
		this.type = type;
		this.level = level;
	}
	
	protected static List <String> getArmorFromXML(String type, String armorLevel) {
		try {
	        File file = new File("armor.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(file);         
	        doc.getDocumentElement().normalize(); 
//	        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nList = doc.getElementsByTagName("item");
//	        System.out.println("----------------------------");
	        
	        List <String> armorList = new ArrayList<String>();
	        
	        if(type.equals("random") && armorLevel.equals("random")) {
	        	int getArmor = new Random().nextInt(nList.getLength());
	        	
	        	Node nNode = nList.item(getArmor);               
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	Element eElement = (Element) nNode;
                	
                	String name = eElement.getAttribute("name");            	           	
                	String strength = eElement.getElementsByTagName("strength").item(0).getTextContent();
                	String dexterity = eElement.getElementsByTagName("dexterity").item(0).getTextContent();
                	String stamina = eElement.getElementsByTagName("stamina").item(0).getTextContent();
                	String wrath = eElement.getElementsByTagName("wrath").item(0).getTextContent();
                	String armorType = eElement.getElementsByTagName("type").item(0).getTextContent();
                	String level = eElement.getElementsByTagName("level").item(0).getTextContent();
                	String armor = eElement.getElementsByTagName("armor").item(0).getTextContent();     
                		
            		armorList.add(name);           		
            		armorList.add(armor);               	
                	armorList.add(strength);                	
                	armorList.add(dexterity);              	
                	armorList.add(stamina);                	
                	armorList.add(wrath);               	
                	armorList.add(armorType);                	
                	armorList.add(level);
                	
//                	System.out.println("Name : " + name); 
//                	System.out.println("Armor : " + armor);
//                	System.out.println("STR : " + strength);
//                	System.out.println("DEX : " + dexterity);
//                	System.out.println("STA : " + stamina);
//                	System.out.println("WTH : " + wrath);
//                	System.out.println("Type : " + armorType);
//                	System.out.println("Level : " + level);
                	
                	return armorList;
                }
	        }else {
	        	try {
	        		XPath xPath =  XPathFactory.newInstance().newXPath();
	        		String expression = "/items/item[type='"+type+"' and ./level/text()='"+armorLevel+"']";
	        		javax.xml.xpath.XPathExpression expr = xPath.compile(expression);
	        		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	        		Node selectedArmor = nodes.item(0);
	        		if (selectedArmor.getNodeType() == Node.ELEMENT_NODE) {
	                	Element eElement = (Element) selectedArmor;
	                	
	                	String name = eElement.getAttribute("name");            	           	
	                	String strength = eElement.getElementsByTagName("strength").item(0).getTextContent();
	                	String dexterity = eElement.getElementsByTagName("dexterity").item(0).getTextContent();
	                	String stamina = eElement.getElementsByTagName("stamina").item(0).getTextContent();
	                	String wrath = eElement.getElementsByTagName("wrath").item(0).getTextContent();
	                	String armorType = eElement.getElementsByTagName("type").item(0).getTextContent();
	                	String level = eElement.getElementsByTagName("level").item(0).getTextContent();
	                	String armor = eElement.getElementsByTagName("armor").item(0).getTextContent();     

                		armorList.add(name);              		
                		armorList.add(armor);                    	
                    	armorList.add(strength);                  	
                    	armorList.add(dexterity);                   	
                    	armorList.add(stamina);                    	
                    	armorList.add(wrath);
                    	armorList.add(armorType);                    	
                    	armorList.add(level);
                    	
//	                	System.out.println("Name : " + name); 
//	                	System.out.println("Armor : " + armor);
//	                	System.out.println("STR : " + strength);
//	                	System.out.println("DEX : " + dexterity);
//	                	System.out.println("STA : " + stamina);
//	                	System.out.println("WTH : " + wrath);
//	                	System.out.println("Type : " + armorType);
//	                	System.out.println("Level : " + level);
                    	
	        		} 
	        		return armorList;
	        		
	        	}catch (Exception e) {
	    	        e.printStackTrace();
	 	       	}
	        }	
       } catch (Exception e) {
        e.printStackTrace();
       }
	            	
		return null;
	}

	public static Armor getRandomArmor(String type, String armorLevel) { // BEWARE INDIAN CODE!!! (or mby not?)
		if(type == "feet") {
			return Boots.getBootsRandRarity(armorLevel);
		}
		else if(type == "body") {
			return Bodyarmor.getBodyarmorRandRarity(armorLevel);
		}
		else if(type == "arms") {
			return Gloves.getGlovesRandRarity(armorLevel);
		}
		else if(type == "legs") {
			return Legarmor.getLegarmorRandRarity(armorLevel);
		}
		else if(type == "head") {
			return Headgear.getHeadgearRandRarity(armorLevel);
		}
		return null;
	}
	
	public int getArmor() {
		return armor;
	}

	public int getStrength() {
		return strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getStamina() {
		return stamina;
	}

	public int getWrath() {
		return wrath;
	}

	public String getType() {
		return type;
	}
	
	public String getLevel() {
		return level;
	}
}

