package com.init.products.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.init.products.entity.Product;
import com.init.products.response.response;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;



@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductsRest {
	String [] nameC = null ; int amountGlobalVar=0;

	@PostMapping("/endpoint")
	public ResponseEntity<response[]> procesarDato(@RequestBody String dato) {
		String valor = dato;
		valor=valor.substring(9, valor.length()-2);
		processingInformation(valor); 
		response res []= new response[1];   
		return ResponseEntity.ok(res);
	}

	public void processingInformation(String message) {
		String command = " "; int pos=0; 
		for(int i=0; i<message.length(); i++) {
			if(message.charAt(i)==' ') {
				command=message.substring(0, i); 
				pos=i;
				break;
			}
			i++;
		}
		if(command.contentEquals("Update")) {
			System.out.print(message.substring(pos+1));
			Update(message.substring(pos+1)); 
		}
		else if(command.contentEquals("Create")) {

		}
		else if(command.contentEquals("Select")) {
			Select((message.substring(pos+1))); 
		}
		else if(command.contentEquals("Insert")) {
			message=message.substring(pos+1);
			System.out.print(message.substring(0));
			int i=0; 
			while(message.charAt(i)!=' ') 
				i++;
			String commandAux=message.substring(0,i);
			if(commandAux.contentEquals("Into")) {
				Insert(message.substring(i+1)); 
			}
		}
	}



	public void Select(String command) {
		String [] operation = new String [3]; 
		String tableName= " "; int counter=0; int pos=0; 
		String tableName1= " ";
		String columnName = " ";
		String var=""; String result= "";
		String varCondition1="";  String result1="";
		String varCondition2="";  String result2="";
		String varCondition3="";  String result3=""; int amountCondition=0; 
		System.out.println(varCondition1);
		System.out.println(command.length());
		for(int i=0; i<command.length() ; i++ ) {
			if(command.charAt(i)==' ' || i==command.length()-1 ) {
				if(i==command.length()-1 ) i++; 
				if(counter==0) { //nombre de la columna
					columnName=command.substring(0,i);
					pos=i+1;
					counter++;
				}
				else if(counter==1) {
					if(command.substring(pos, i).contentEquals("from")) {
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==2) { //nombre de la tabla
					tableName=command.substring(pos, i);
					pos=i+1;
					counter++;
				}
				else if(counter==3) {
					if(command.substring(pos, i).contentEquals("innerjoin")) {
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==4) { //nombre de la tabla que hace interseccion
					tableName1=command.substring(pos, i);
					pos=i+1;
					counter++;
				}
				else if(counter==5) {
					if(command.substring(pos, i).contentEquals("on")) {
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
			}
			else if (command.charAt(i)=='=') {
				if(counter==6) {
					var=command.substring(pos, i); 
					result=command.substring(i+1);
					pos=i+1;
					counter++;
				}
			}
		}
		System.out.println(var); 
		System.out.println(result); 

		for(int j=0; j<7; j++); 

	}
	public void Update(String command) {
		String [] operation = new String [3]; 
		String tableName= " "; int counter=0; int pos=0; 
		String var=""; String result= "";
		String varCondition1="";  String result1="";
		String varCondition2="";  String result2="";
		String varCondition3="";  String result3=""; int amountCondition=0; 
		System.out.println(varCondition1);
		System.out.println(command.length());
		for(int i=0; i<command.length() ; i++ ) {
			if(command.charAt(i)==' ' || i==command.length()-1 ) {
				if(i==command.length()-1 ) i++; 
				if(counter==0) {
					tableName=command.substring(0,i);
					pos=i+1;
					counter++;
				}
				else if(counter==1) {
					if(command.substring(pos, i).contentEquals("set")) {
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==3) {
					result=command.substring(pos, i);
					pos=i+1;
					counter++;
				}
				else if(counter==4) {
					if(command.substring(pos, i).contentEquals("where")) {
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==6) {
					result1=command.substring(pos, i);
					pos=i+1;
					counter++;
					amountCondition++;
				}
				else if (counter==7 || counter==10) {
					if(command.substring(pos, i).contentEquals("or") || command.substring(pos, i).contentEquals("and") ) {
						String y=command.substring(pos, i);
						operation[amountCondition-1]=command.substring(pos, i);
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==9) {
					result2=command.substring(pos, i);
					pos=i+1;
					counter++;
					amountCondition++;
				}
				else if(counter==12) {
					result3=command.substring(pos, i);
					pos=i+1;
					counter++;
					amountCondition++;
				}
			}
			else if (command.charAt(i)=='=') {
				if(counter==2) {
					var=command.substring(pos, i); 
					pos=i+1;
					counter++;
				}
				else if(counter==5) {
					varCondition1=command.substring(pos, i); 
					pos=i+1;
					counter++;
				}
				else if(counter==8) {
					varCondition2=command.substring(pos, i); 
					pos=i+1;
					counter++;
				}
				else if(counter==11) {
					varCondition3=command.substring(pos, i); 
					pos=i+1;
					counter++;
				}
			}
		}	

		String filePath = "ruta/directorio/archivo.xml";
		String [] conditions = new String[amountCondition]; 
		String [] results = new String[amountCondition]; 

		if(amountCondition==3) {
			conditions[0]=varCondition1;
			conditions[1]=varCondition2;
			conditions[2]=varCondition3;

			results[0]=result1;
			results[1]=result2;
			results[2]=result3;}
		if(amountCondition==2) {
			conditions[0]=varCondition1;
			conditions[1]=varCondition2;
			results[0]=result1;
			results[1]=result2;}
		conditions[0]=varCondition1;
		results[0]=result1;


		try {
			if(checkConditions(conditions)) {

				int amountVar=0; 
				while(nameC[amountVar]!=null)
					amountVar++;

				File xmlFile = new File(getClass().getResource("studentss.xml").getFile());
				System.out.print((getClass().getResource("studentss.xml").getFile()));
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(xmlFile);

				// Modificar el archivo XML según las condiciones
				Element rootElement = document.getDocumentElement();
				int x=amountCondition;
				boolean flag1=false; boolean flag2=false; boolean flag3=false;

				NodeList elementList = rootElement.getElementsByTagName("estudiante");

				for (int i = 0; i < elementList.getLength(); i++) {
					Element element = (Element) elementList.item(i);
					Node studentNode = elementList.item(i);
					if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
						Element studentElement = (Element) studentNode;
						String[] results1 = new String [amountVar];
						Element[] results11 = new Element [amountVar];
						for(int k=0; k<amountVar; k++) {
							results1[k]= (String) studentElement.getElementsByTagName(nameC[k]).item(0).getTextContent();
							results11[k]=  (Element) studentElement.getElementsByTagName(nameC[k]).item(0);
						}
						if(deniedAcces(amountCondition, results, results1, operation, conditions, amountVar)) {
							for(int p=0; p<amountVar;p++) {
								if(nameC[p].contentEquals(var)) {
									System.out.print(results11[p].getTextContent()); 
									studentElement.getElementsByTagName(nameC[p]).item(0).setTextContent(result);
								}
							}
						}
					}
				}

				// Guardar los cambios en el archivo XML
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(document);
				StreamResult ren = new StreamResult(xmlFile);
				transformer.transform(source, ren);
				System.out.println("Archivo XML modificado y guardado: " + filePath);
			}
		}catch (Exception e) {

			System.err.println("Error al guardar los cambios en el archivo XML: " + e.getMessage());
		}


	}

	public boolean deniedAcces(int amountConditions, String [] results, String [] results1 , String [] operation, String [] Condition, int amountVar) {
		System.out.println(results1[0]);
		String[] auxResult = new String [nameC.length]; int x=0;
		String[] auxResult1 = new String [nameC.length];
		for(int i=0; i<Condition.length; i++ ) {
			for(int j=0; j<amountVar; j++) {
				if(nameC[j].contentEquals(Condition[i])) {
					auxResult[x]=results[i]; 
					auxResult1[x]=results1[j]; x++; 
				}
			}
		}


		if(amountConditions==1) {
			if(auxResult1[0].contentEquals(auxResult[0]))
				return true;
		}
		else if(amountConditions==2) {
			if(Condition[0].contentEquals("or")) {
				if(auxResult1[0].contentEquals(auxResult[0]) ||auxResult1[1].contentEquals(auxResult[1]) ) {
					return true;
				}
			}else {
				if(auxResult1[0].contentEquals(auxResult[0]) || auxResult1[1].contentEquals(auxResult[1]) ) {
					return true;
				}
			}

		}
		else if(amountConditions==3) {
			if(Condition[0].contentEquals("or") && Condition[1].contentEquals("or")) {
				if(auxResult1[0].contentEquals(auxResult[0]) || auxResult1[1].contentEquals(auxResult[1]) || auxResult1[2].contentEquals(auxResult[2]) ) {
					return true;
				}
			}else if(Condition[0].contentEquals("or") && Condition[1].contentEquals("and")){
				if((auxResult1[0].contentEquals(auxResult[0]) || auxResult1[1].contentEquals(auxResult[1])) && auxResult1[2].contentEquals(auxResult[2]) ) {
					return true;
				}
			}
			else if(Condition[0].contentEquals("and") && Condition[1].contentEquals("and")){
				if((auxResult1[0].contentEquals(auxResult[0]) && auxResult1[1].contentEquals(auxResult[1])) && auxResult1[2].contentEquals(auxResult[2]) ) {
					return true;
				}
			}
			else if(Condition[0].contentEquals("and") && Condition[1].contentEquals("or")){
				if((auxResult1[0].contentEquals(auxResult[0]) && auxResult1[1].contentEquals(auxResult[1])) || auxResult1[2].contentEquals(auxResult[2]) ) {
					return true;
				}
			}
		}		
		return false; 
	}
	public boolean checkConditions(String [] conditions) throws XPathExpressionException {
		String value;  boolean flag=false; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			File file = new File(getClass().getResource("studentss.xml").getFile());
			Document document = builder.parse(file);
			Node estudianteNode = document.getDocumentElement();
			Element estudianteElement = (Element) estudianteNode;

			NodeList childNodes = estudianteNode.getChildNodes();
			int amountVar=0;   
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) childNode;
					NodeList variableNodes = element.getChildNodes();

					nameC= new String [variableNodes.getLength()]; 
					for (int j = 0; j < variableNodes.getLength(); j++) {
						Node variableNode = variableNodes.item(j);
						if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
							String variableName = variableNode.getNodeName();
							nameC[amountVar]=variableName; amountVar++;
							System.out.println(variableName);
							amountGlobalVar++; 
						}
					}
					break;
				}
			}

			for(int i=0; i<conditions.length; i++) {
				for(int j=0; j<nameC.length; j++) {
					if(conditions[i].contentEquals(nameC[j])) {
						flag=true;
						break;
					}
				}
				if(!flag) 
					break; 
			}
			System.out.println(flag);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return flag; 
	}

	public void Insert(String command) {
		String tableName=" "; 
		String [][] values = null; 
		String [] variables = null;
		int amountVar=1; 
		int amountIns=0; 
		int auxamountIns=0; 
		int counter=1; int pos=0; 
		for(int i=0; i<command.length(); i++) {
			if(counter==1 && command.charAt(i)==' ') {
				tableName=command.substring(pos, i); 
				pos=i+1; 
				counter++; 
			}
			else if(counter==2 && command.charAt(i)=='(') {
				counter++; 
				pos=i+1;
				int x=i; 
				while(command.charAt(x)!=')' ) {
					if(command.charAt(x)==',')
						amountVar++; 
					x++;
					if(x==command.length()) break;
				}
				variables= new String [amountVar]; 
			}
			else if((counter==3 && command.charAt(i)==',')) {
				int x=i; int j=0;
				while(command.charAt(x)!=')' && x!=command.length()) {
					if(command.charAt(x)==',') {
						variables[j]=command.substring(pos, x); j++;
						pos=x+1;
					}
					x++;
				}
				if(x<command.length()) {
					variables[j]=command.substring(pos, x); 
					pos=x+2;
					counter++;
				}
				else break;
				i=x-1;  
				while( x!=command.length()) {
					if(command.charAt(x)=='(') 
						amountIns++; 
					x++; 

				}
				values= new String [amountIns][amountVar];

			}
			else if(counter==4 && command.charAt(i)==' ') {
				int x=i; x++;
				while(command.charAt(x)!=' ' && x!=command.length()) {
					x++; 
				}
				pos=x+2;
				counter++; 
				i=x; 
			}
			else if(counter==5 && command.charAt(i)=='(') {
				int x=i; int value=0;
				while(command.charAt(x)!=')' && x!=command.length()) {
					if(command.charAt(x)==',') {
						values[auxamountIns][value]=command.substring(pos, x); 
						pos=x+1;
						value++; 
					}
					x++;
				}
				if(x<command.length()) {
					values[auxamountIns][value]=command.substring(pos, x); 
					pos=x+2;
				}
				i=x-1;
			}


		}
		try {
			if(checkConditions(variables)) {
				String[][] auxResult = new String [amountIns][amountGlobalVar]; int x=0;

				for(int i=0; i<amountGlobalVar; i++ ) {
					for(int j=0; j<amountVar; j++) {
						if(nameC[i].contentEquals(variables[j])) {
							while(x!=amountIns) {
								auxResult[x][i]=values[x][j];
								x++;
							}
						}
					}
				}
				createNewNode(amountIns, amountGlobalVar, auxResult);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createNewNode(int amountIns, int amountVar, String values[][]) {
		try {
		    File xmlFile = new File(getClass().getResource("studentss.xml").getFile());
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(xmlFile);
		    for(int i=0; i<amountIns; i++) {
		    	 Element estudianteElement = document.createElement("estudiante");
		    	 for(int j=0; j<amountVar; j++) {
		    		Element nombreElement = document.createElement(nameC[j]);
		    		if(values[i][j]!=null)
		    			nombreElement.setTextContent(values[i][j]);
		    		else
		    			nombreElement.setTextContent(" ");
		  		    estudianteElement.appendChild(nombreElement);
		    	 }
		    	 Element rootElement = document.getDocumentElement();
		    	 rootElement.appendChild(estudianteElement);
		    }

		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    DOMSource source = new DOMSource(document);
		    StreamResult result = new StreamResult(xmlFile);
		    transformer.transform(source, result);

		    System.out.println("Nuevo nodo 'estudiante' agregado al archivo XML.");

		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
		    e.printStackTrace();
		}

	}

}


