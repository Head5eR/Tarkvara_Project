package com.mygdx.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Boss extends Monster {
	private int wrath;
	private static String expression = "//boss[@name]";
	private static List<String> bossStats;
	private ArrayList<Integer> pickedDefs = new ArrayList<Integer>();
	
	public static Boss getBoss() {
		
		bossStats = readFromXML();
		String bodytype = bossStats.get(1);
		
		int str = Integer.parseInt(bossStats.get(3));
		int dex = Integer.parseInt(bossStats.get(4));
		int stam =  Integer.parseInt(bossStats.get(5));
		String name = bossStats.get(0);	
		return new Boss(str, dex, stam, bodytype, name);
				
	}
	
	private Boss(int str, int dex, int stam, String bodytype, String name) {
		super(str, dex, stam, bodytype, name);		
		this.wrath = Integer.parseInt(bossStats.get(6));
		setHp(getMaxHp());
	}
	
	private static List<String> readFromXML() {
		try {
    	 File inputFile = new File("bosses.xml");
		    
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
        
         doc.getDocumentElement().normalize();
         
         XPath xPath =  XPathFactory.newInstance().newXPath();
        
         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);              
         
         List<String> bossList = new ArrayList<String>();
         
         int randomBoss = new Random().nextInt(nodeList.getLength());
         
         Node bossNode = nodeList.item(randomBoss);
         
            if (bossNode.getNodeType() == Node.ELEMENT_NODE) {
               Element bossXMLElement = (Element) bossNode;
               bossList.add(bossXMLElement.getAttribute("name"));
               
               NodeList listOfBossAttributes = bossXMLElement.getElementsByTagName("*");
               
               for(int i=0; i<listOfBossAttributes.getLength(); i++) {
					bossList.add(listOfBossAttributes.item(i).getTextContent());					
				}
               
               System.out.println(bossList.toString());
               return bossList;
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
		return getStamina()*10;
	}
	
	@Override
	public int getWrath() {
		return (int) Math.round(getStrength()*getStamina()*getDexterity()*0.001);
	}
	
	@Override
	public float getEvasion() {
		return (float) (getDexterity()*0.1);
	}
		    
	public void setWrath(int wrath) {
		this.wrath = wrath;
	}	
	
	public ArrayList<Integer> getDefenceMoves() {
		return pickedDefs;
	}	
	
	@Override
	public int getMaxAttackDamage() {
		return (int) Math.round(getStrength()*3);
	}

	@Override
	public String toString() {
		//return name + " " + strength + " " + dexterity + " " + stamina + " " + wrath + "\n Modifier: " + mod;
		return getName() + "Boss lvl " + bossStats.get(2) +
				"\n" + "HP: " + getHp() + "/" + getMaxHp() 
				+ "\n Attack: " + getAttackDamageStr();
	}
}
