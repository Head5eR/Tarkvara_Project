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
	private String name;
	private int minDamage;
	private int maxDamage;
	private int armor;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private String twohanded;
	private String type;
	private String level;
	
	public Weapon() {
		List <String> weaponStats = getWeaponFromXML(type, level);
		
		this.name = weaponStats.get(0);
		this.minDamage = Integer.parseInt(weaponStats.get(1));
		this.maxDamage = Integer.parseInt(weaponStats.get(2));
		this.strength = Integer.parseInt(weaponStats.get(3));
		this.dexterity = Integer.parseInt(weaponStats.get(4));
		this.stamina = Integer.parseInt(weaponStats.get(5));
		this.wrath = Integer.parseInt(weaponStats.get(6));
		this.twohanded = weaponStats.get(7);
		this.type = weaponStats.get(8);
		this.level = weaponStats.get(9);
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
						
		public String getName() {
			return name;
		}

		public int getMinDamage() {
			return minDamage;
		}

		public int getMaxDamage() {
			return maxDamage;
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

		public String getTwohanded() {
			return twohanded;
		}

		public String getType() {
			return type;
		}
		
		public String getLevel() {
			return level;
		}

		/**public static void main(String argv[]) {
			getWeaponFromXML("shield", "3");
		}**/
}

