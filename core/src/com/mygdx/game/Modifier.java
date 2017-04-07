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

public class Modifier {
	private String name;
	private int strength;
	private int dexterity;
	private int stamina;
	private int wrath;
	private String expression = "//difficulty[@level]";	        
	
	public Modifier() {
		List<String> stats = readFromXML();

		this.name = stats.get(0);
		this.strength = Integer.parseInt(stats.get(1));
		this.dexterity = Integer.parseInt(stats.get(2));
		this.stamina = Integer.parseInt(stats.get(3));
		this.wrath = Integer.parseInt(stats.get(4));
	}
	
	private List<String> readFromXML() {
		try {
			File inputFile = new File("stats.xml");
			
			DocumentBuilderFactory dbFactory 
				= DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder;

			 dBuilder = dbFactory.newDocumentBuilder();
			 
			 Document doc = dBuilder.parse(inputFile);
			 doc.getDocumentElement().normalize();
			 
			 XPath xPath =  XPathFactory.newInstance().newXPath();
			 
			 NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			 
			 List<String> statList = new ArrayList<String>();
			 
			 int randomDifficultyNumber = new Random().nextInt(nodeList.getLength());

			 Node difficultyNode = nodeList.item(randomDifficultyNumber);

			 if (difficultyNode.getNodeType() == Node.ELEMENT_NODE) {
				Element difficultyElement = (Element) difficultyNode; //casting node to element
				statList.add(difficultyElement.getAttribute("level"));
				NodeList listOfAttributes = difficultyElement.getElementsByTagName("*");

				for(int i=0; i<listOfAttributes.getLength(); i++) {
					statList.add(listOfAttributes.item(i).getTextContent());					
				}

				return statList;
			}
			return null;
		}
		catch (ParserConfigurationException e) {
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
	
	
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getDexterity() {
		return dexterity;
	}
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getWrath() {
		return wrath;
	}
	public void setWrath(int wrath) {
		this.wrath = wrath;
	}
	
	@Override
	public String toString() {
		return name + " " + strength + " " + dexterity + " " + stamina + " " + wrath;
	}

	public String getName() {
		return name;
	}
}