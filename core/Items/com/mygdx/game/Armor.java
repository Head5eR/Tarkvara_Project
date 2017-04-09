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

public class Armor extends Item {
	private String name;
	private int armor;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private String type;
	private String level;
	
	public Armor() {
		List <String> armorStats = getArmorFromXML(type, level);
		
		this.name = armorStats.get(0);
		this.strength = Integer.parseInt(armorStats.get(1));
		this.dexterity = Integer.parseInt(armorStats.get(2));
		this.stamina = Integer.parseInt(armorStats.get(3));
		this.wrath = Integer.parseInt(armorStats.get(4));
		this.type = armorStats.get(5);
		this.level = armorStats.get(6);
	}
	

		public static List <String> getArmorFromXML(String type, String armorLevel) {
			try {
		        File file = new File("armor.xml");
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(file);         
		        doc.getDocumentElement().normalize(); 
		        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		        NodeList nList = doc.getElementsByTagName("item");
		        System.out.println("----------------------------");
		        
		        List <String> armorList = new ArrayList<String>();
		        
		        if(type.equals("random") && armorLevel.equals("random")) {
		        	int getArmor = new Random().nextInt(nList.getLength());
		        	
		        	Node nNode = nList.item(getArmor);
	                System.out.println("\nCurrent Element :" + nNode.getNodeName());
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
                		System.out.println("Name : " + name); 
                		armorList.add(armor);
                    	System.out.println("Armor : " + armor);
                    	armorList.add(strength);
                    	System.out.println("STR : " + strength);
                    	armorList.add(dexterity);
                    	System.out.println("DEX : " + dexterity);
                    	armorList.add(stamina);
                    	System.out.println("STA : " + stamina);
                    	armorList.add(wrath);
                    	System.out.println("WTH : " + wrath);
                    	armorList.add(armorType);
                    	System.out.println("Type : " + armorType);
                    	armorList.add(level);
                    	System.out.println("Level : " + level);
	                	
	                	return armorList;
	                }
		        }else {
		        	try {
		        		XPath xPath =  XPathFactory.newInstance().newXPath();
		        		String expression = "/items/item[type='"+type+"' and ./level/text()='"+armorLevel+"']";
		        		javax.xml.xpath.XPathExpression expr = xPath.compile(expression);
		        		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		        		System.out.println(nodes.getLength());
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
	                		System.out.println("Name : " + name); 
	                		armorList.add(armor);
	                    	System.out.println("Armor : " + armor);
	                    	armorList.add(strength);
	                    	System.out.println("STR : " + strength);
	                    	armorList.add(dexterity);
	                    	System.out.println("DEX : " + dexterity);
	                    	armorList.add(stamina);
	                    	System.out.println("STA : " + stamina);
	                    	armorList.add(wrath);
	                    	System.out.println("WTH : " + wrath);
	                    	armorList.add(armorType);
	                    	System.out.println("Type : " + armorType);
	                    	armorList.add(level);
	                    	System.out.println("Level : " + level);
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
						
		public String getName() {
			return name;
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

		/**public static void main(String argv[]) {
			getArmorFromXML("random", "random");
		}**/
}

