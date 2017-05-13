package com.mygdx.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Weapon extends Item {
	
	// We totally need to use HashMap for all the stats, but i'm too lazy to rewrite it all	
	
	private int minDamage;
	private int maxDamage;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private boolean twohanded;
	private String type;
	private String level;
	
	public Weapon(int minDamage, int maxDamage, int str, int dex, int stam, int wrath, String twohanded, String type, String level, String name) {
		super(name,true);
		this.maxDamage = maxDamage;
		this.minDamage = minDamage;
		this.strength = str;
		this.dexterity = dex;
		this.stamina = stam;
		this.wrath = wrath;
		if(twohanded.equals("yes")) { // Robert.setName("Aleksei");
			this.twohanded = true;
		} else {
			this.twohanded = false;
		}
	}
	

		protected static List <String> getWeaponFromXML(String type, String weaponLevel) {
			try {
		        File file = new File("weapons.xml");
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(file);         
		        doc.getDocumentElement().normalize(); 
//		        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		        NodeList nList = doc.getElementsByTagName("weapon");
//		        System.out.println("----------------------------");
		        
		        List <String> weaponList = new ArrayList<String>();
		        	try {
		        		XPath xPath =  XPathFactory.newInstance().newXPath();
		        		String expression = "/weapons/weapon[type='"+type+"' and ./level/text()='"+weaponLevel+"']";
		        		javax.xml.xpath.XPathExpression expr = xPath.compile(expression);
		        		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		        		Node selectedWeapon = nodes.item(0);
		        		if (selectedWeapon.getNodeType() == Node.ELEMENT_NODE) {
		                	Element eElement = (Element) selectedWeapon;
		                	
		                	String name = eElement.getAttribute("name");            	           	
		                	String strength = eElement.getElementsByTagName("strength").item(0).getTextContent();
		                	String dexterity = eElement.getElementsByTagName("dexterity").item(0).getTextContent();
		                	String stamina = eElement.getElementsByTagName("stamina").item(0).getTextContent();
		                	String wrath = eElement.getElementsByTagName("wrath").item(0).getTextContent();
		                	String twohanded = eElement.getElementsByTagName("twohanded").item(0).getTextContent();
		                	String weaponType = eElement.getElementsByTagName("type").item(0).getTextContent();
		                	String level = eElement.getElementsByTagName("level").item(0).getTextContent();
		                	
		                	if(weaponType.equals("shield")) {
		                		String armor = eElement.getElementsByTagName("armor").item(0).getTextContent();     	                		
		                		weaponList.add(name);
		                		weaponList.add(armor);
		                    	weaponList.add(strength);
		                    	weaponList.add(dexterity);
		                    	weaponList.add(stamina);
		                    	weaponList.add(wrath);
		                    	weaponList.add(twohanded);
		                    	weaponList.add(weaponType);
		                    	weaponList.add(level);
		                    	
//		                    	System.out.println("Level : " + level);
//		                		System.out.println("Name : " + name); 
//		                    	System.out.println("Armor : " + armor);
//		                    	System.out.println("STR : " + strength);
//		                    	System.out.println("DEX : " + dexterity);
//		                    	System.out.println("STA : " + stamina);
//		                    	System.out.println("WTH : " + wrath);
//		                    	System.out.println("Twohanded : " + twohanded);
//		                    	System.out.println("Type : " + weaponType);
		                	} else {
		                		String minDamage = eElement.getElementsByTagName("mindamage").item(0).getTextContent();
		                    	String maxDamage = eElement.getElementsByTagName("maxdamage").item(0).getTextContent();
		                		
//		                		System.out.println("Name : " + name);
//		                		System.out.println("MinDamage : " + minDamage);
//		                		System.out.println("MaxDamage : " + maxDamage);
//		                		System.out.println("STR : " + strength);
//		                		System.out.println("DEX : " + dexterity);
//		                		System.out.println("STA : " + stamina);
//		                		System.out.println("WTH : " + wrath);
//		                		System.out.println("Twohanded : " + twohanded);
//		                		System.out.println("Type : " + weaponType);
//		                		System.out.println("Level : " + level);
		                    	weaponList.add(name);
		                		weaponList.add(minDamage);
		                    	weaponList.add(maxDamage);		                    	
		                    	weaponList.add(strength);		                    	
		                    	weaponList.add(dexterity);		                    	
		                    	weaponList.add(stamina);		                    	
		                    	weaponList.add(wrath);		                    	
		                    	weaponList.add(twohanded);		                    	
		                    	weaponList.add(weaponType);		                    	
		                    	weaponList.add(level);
		                	} 
		                	return weaponList;
		                }
		        } catch (Exception e) {
		        	e.printStackTrace();
		 	    }
	       
		} catch (Exception e) {
	        e.printStackTrace();
	    }      	
			return null;
		}
		
		public boolean isTwohanded() {
			return twohanded;
		}
						
		public int getMinDamage() {
			return minDamage;
		}

		public int getMaxDamage() {
			return maxDamage;
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

