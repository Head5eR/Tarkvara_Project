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

public class Weapon extends Item {
	private int minDamage;
	private int maxDamage;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private boolean twohanded;
	private String type;
	private String level;
	
	public static Weapon getWeaponRandRarity(String type, String level) {
		List <String> weaponStats = getWeaponFromXML(type, level);
		String name = weaponStats.get(0);
		int  minDamage = Integer.parseInt(weaponStats.get(1));
		int maxDamage = Integer.parseInt(weaponStats.get(2));
		int strength = Integer.parseInt(weaponStats.get(3));
		int dexterity = Integer.parseInt(weaponStats.get(4));
		int stamina = Integer.parseInt(weaponStats.get(5));
		int wrath = Integer.parseInt(weaponStats.get(6));
		String twohanded = weaponStats.get(7);
		
		return new Weapon(minDamage, maxDamage, strength, dexterity, stamina, wrath, twohanded, type, level, name);
	}
	
	private Weapon(int minDamage, int maxDamage, int str, int dex, int stam, int wrath, String twohanded, String type, String level, String name) {
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
	

		public static List <String> getWeaponFromXML(String type, String weaponLevel) {
			try {
		        File file = new File("weapons.xml");
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(file);         
		        doc.getDocumentElement().normalize(); 
		        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		        NodeList nList = doc.getElementsByTagName("weapon");
		        System.out.println("----------------------------");
		        
		        List <String> weaponList = new ArrayList<String>();
		        
		        if(type.equals("random") && weaponLevel.equals("random")) {
		        	int getWeapon = new Random().nextInt(nList.getLength());
		        	
		        	Node nNode = nList.item(getWeapon);
	                System.out.println("\nCurrent Element :" + nNode.getNodeName());
	                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                	Element eElement = (Element) nNode;
	                	
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
	                		System.out.println("Name : " + name); 
	                		weaponList.add(armor);
	                    	System.out.println("Armor : " + armor);
	                    	weaponList.add(strength);
	                    	System.out.println("STR : " + strength);
	                    	weaponList.add(dexterity);
	                    	System.out.println("DEX : " + dexterity);
	                    	weaponList.add(stamina);
	                    	System.out.println("STA : " + stamina);
	                    	weaponList.add(wrath);
	                    	System.out.println("WTH : " + wrath);
	                    	weaponList.add(twohanded);
	                    	System.out.println("Twohanded : " + twohanded);
	                    	weaponList.add(weaponType);
	                    	System.out.println("Type : " + weaponType);
	                    	weaponList.add(level);
	                    	System.out.println("Level : " + level);
	                	} else {
	                		String minDamage = eElement.getElementsByTagName("mindamage").item(0).getTextContent();
	                    	String maxDamage = eElement.getElementsByTagName("maxdamage").item(0).getTextContent();
	                		
	                    	weaponList.add(name);
	                		System.out.println("Name : " + name);
	                		weaponList.add(minDamage);
	                    	System.out.println("MinDamage : " + minDamage);
	                    	weaponList.add(maxDamage);
	                    	System.out.println("MaxDamage : " + maxDamage);
	                    	weaponList.add(strength);
	                    	System.out.println("STR : " + strength);
	                    	weaponList.add(dexterity);
	                    	System.out.println("DEX : " + dexterity);
	                    	weaponList.add(stamina);
	                    	System.out.println("STA : " + stamina);
	                    	weaponList.add(wrath);
	                    	System.out.println("WTH : " + wrath);
	                    	weaponList.add(twohanded);
	                    	System.out.println("Twohanded : " + twohanded);
	                    	weaponList.add(weaponType);
	                    	System.out.println("Type : " + weaponType);
	                    	weaponList.add(level);
	                    	System.out.println("Level : " + level);
	                		} 
	                	return weaponList;
	                }
		        }else {
		        	try {
		        		XPath xPath =  XPathFactory.newInstance().newXPath();
		        		String expression = "/weapons/weapon[type='"+type+"' and ./level/text()='"+weaponLevel+"']";
		        		javax.xml.xpath.XPathExpression expr = xPath.compile(expression);
		        		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		        		System.out.println(nodes.getLength());
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
		                		System.out.println("Name : " + name); 
		                		weaponList.add(armor);
		                    	System.out.println("Armor : " + armor);
		                    	weaponList.add(strength);
		                    	System.out.println("STR : " + strength);
		                    	weaponList.add(dexterity);
		                    	System.out.println("DEX : " + dexterity);
		                    	weaponList.add(stamina);
		                    	System.out.println("STA : " + stamina);
		                    	weaponList.add(wrath);
		                    	System.out.println("WTH : " + wrath);
		                    	weaponList.add(twohanded);
		                    	System.out.println("Twohanded : " + twohanded);
		                    	weaponList.add(weaponType);
		                    	System.out.println("Type : " + weaponType);
		                    	weaponList.add(level);
		                    	System.out.println("Level : " + level);
		                	} else {
		                		String minDamage = eElement.getElementsByTagName("mindamage").item(0).getTextContent();
		                    	String maxDamage = eElement.getElementsByTagName("maxdamage").item(0).getTextContent();
		                		
		                    	weaponList.add(name);
		                		System.out.println("Name : " + name);
		                		weaponList.add(minDamage);
		                    	System.out.println("MinDamage : " + minDamage);
		                    	weaponList.add(maxDamage);
		                    	System.out.println("MaxDamage : " + maxDamage);
		                    	weaponList.add(strength);
		                    	System.out.println("STR : " + strength);
		                    	weaponList.add(dexterity);
		                    	System.out.println("DEX : " + dexterity);
		                    	weaponList.add(stamina);
		                    	System.out.println("STA : " + stamina);
		                    	weaponList.add(wrath);
		                    	System.out.println("WTH : " + wrath);
		                    	weaponList.add(twohanded);
		                    	System.out.println("Twohanded : " + twohanded);
		                    	weaponList.add(weaponType);
		                    	System.out.println("Type : " + weaponType);
		                    	weaponList.add(level);
		                    	System.out.println("Level : " + level);
		                		} 
		                	return weaponList;
		                }
		        	} catch (Exception e) {
		    	        e.printStackTrace();
		 	       }
		        }
	       } catch (Exception e) {
	        e.printStackTrace();
	       }
		            	
			return null;
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

