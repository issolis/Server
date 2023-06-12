package com.init.products.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.init.products.response.response;
import java.io.File;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;


@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductsRest {
	String [] nameC = null ; int amountGlobalVar=0; 	response [] res; String path="studentss.xml";

	linkedList convergenceList = new linkedList(); 
	@PostMapping("/endpoint")
	public ResponseEntity<response[]> procesarDato(@RequestBody String dato) {
		String valor = dato;
		valor=valor.substring(9, valor.length()-2);
		processingInformation(valor); 

		return ResponseEntity.ok(res);
	}
	@PostMapping("/validacion")
	public ResponseEntity<response[]> Arturo(@RequestBody String dato) {
		String valor = dato;
		valor=valor.substring(9, valor.length()-2);
		processingInformation(valor); 

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
			res=new response[1];
			res[0]= new response(); 
			System.out.print(message.substring(pos+1));
			Update(message.substring(pos+1)); 
		}
		else if(command.contentEquals("Create")) {
			int i=pos+1; 
			while(message.charAt(i)!=' ') 
				i++;
			command=message.substring(pos+1,i);
			pos=i;
			create(message.substring(pos+1), command);
		}
		else if(command.contentEquals("Select")) {
			int i=pos+1; int q=1; 
			if(message.substring(pos+1).charAt(0)=='(') {
				while(message.charAt(i)!=')' && i!=message.length() && message.charAt(i)!=' ' ) {
					if(message.charAt(i)==',') 
						q++;
					i++; 
				}
			}
			if(message.charAt(i)==')' ) {
				String variables[] = new String [q]; 
				int x=pos+1; int j=0; pos=pos+2; 
				while(message.charAt(x)!=')' && x!=message.length()) {
					if(message.charAt(x)==',') {
						variables[j]=message.substring(pos, x); j++;
						pos=x+1;
					}
					x++;
				}
				if(message.charAt(x)==')') {
					variables[j]=message.substring(pos, x); 
				}
				i=x-1;
				try {
					String aux=message.substring(x+2); int y=0;
					while(aux.charAt(y)!=' ' && y!=aux.length()  ) {
						y++; 
					}
					int z=y+1; y++;
					while(aux.charAt(y)!=' ' && y!=aux.length()  ) {
						y++;  
					}
					path=aux.substring(z,y); 
					if(checkConditions(variables) && message.charAt(x)==')') {
						amountGlobalVar=0;
						Select(message.substring(x+2), variables);}
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}

		}

		else if(command.contentEquals("Drop")) {
			deleteTable((message.substring(pos+1))); 
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
		else if(command.contentEquals("Delete")) {
			message=message.substring(pos+1);
			System.out.print(message.substring(0));
			int i=0; 
			while(message.charAt(i)!=' ') 
				i++;
			String commandAux=message.substring(0,i);
			if(commandAux.contentEquals("From")) {
				delete(message.substring(i+1)); 
			}
		}
		else {
			res=new response[1];
			res[0]= new response(); 
		}
		amountGlobalVar=0; convergenceList.head=null;  
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
			path=tableName; 
			if(checkConditions(conditions)) {

				int amountVar=0; 
				while(nameC[amountVar]!=null)
					amountVar++;

				File xmlFile = new File(getClass().getResource(tableName+".xml").getFile());
				System.out.print((getClass().getResource(tableName+".xml").getFile()));
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(xmlFile);

				// Modificar el archivo XML según las condiciones
				Element rootElement = document.getDocumentElement();
				int x=amountCondition;
				boolean flag1=false; boolean flag2=false; boolean flag3=false;

				NodeList elementList = rootElement.getElementsByTagName(tableName);

				for (int i = 0; i < elementList.getLength(); i++) {
					Element element = (Element) elementList.item(i);
					Node studentNode = elementList.item(i);
					if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
						Element studentElement = (Element) studentNode;
						String[] results1 = new String [amountVar];
						Element[] results11 = new Element [amountVar];
						for(int k=0; k<amountVar; k++) {
							results1[k]= (String) studentElement.getElementsByTagName(nameC[k]).item(0).getTextContent();
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
		boolean flag=false; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			File file = new File(getClass().getResource(path+".xml").getFile());
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
				flag=false;
				for(int j=0; j<amountVar; j++) {
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
		int z=0; 
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
				variables= new String [amountVar]; i--; 
			}
			else if((counter==3 && (command.charAt(i)=='(' ))) {
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
						values[z][value]=command.substring(pos, x); 
						pos=x+1;
						value++; 
					}
					x++;
				}
				if(x<command.length()) {
					values[z][value]=command.substring(pos, x); 
					pos=x+2;
				}
				i=x-1;
				z++;
			}


		}
		try {
			path=tableName;
			if(checkConditions(variables)) {
				String[][] auxResult = new String [amountIns][amountGlobalVar]; int x=0;

				for(int i=0; i<amountGlobalVar; i++ ) {
					for(int j=0; j<amountVar; j++) {
						if(nameC[i].contentEquals(variables[j])) {
							while(x!=amountIns) {
								auxResult[x][i]=values[x][j];
								x++;
							}
							x=0;
						}
					}
				}
				createNewNode(amountIns, amountGlobalVar, auxResult);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		amountGlobalVar=0; 
	}

	public void createNewNode(int amountIns, int amountVar, String values[][]) {
		try {
			File xmlFile = new File(getClass().getResource(path+".xml").getFile());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			for(int i=0; i<amountIns; i++) {
				Element estudianteElement = document.createElement(path);
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

	public void delete(String command) {
		int counter=0; 
		String tableName=""; int pos=0; 
		String varCondition1="";  String result1="";
		String varCondition2="";  String result2="";
		String varCondition3="";  String result3=""; int amountCondition=0; 
		String [] operation = new String [3]; 

		for(int i=0; i<command.length(); i++) {
			if(command.charAt(i)==' ' || i==command.length()-1) {
				if(i==command.length()-1) i++;
				if(counter==0) {
					tableName=command.substring(pos,i); 
					pos=i+1; counter++;
				}
				else if(counter==1) {
					if(command.substring(pos,i).contentEquals("where")) {
						counter++; 
						pos=i+1;
					}
					else 
						break; 
				}
				else if(counter==3) {
					result1=command.substring(pos,i); 
					pos=i+1; 
					counter++;
					amountCondition++; 
				}
				else if (counter==4 || counter==7) {
					if(command.substring(pos, i).contentEquals("or") || command.substring(pos, i).contentEquals("and") ) {
						operation[amountCondition-1]=command.substring(pos, i);
						counter++; 
						pos=i+1; 
					}
					else
						break;
				}
				else if(counter==6) {
					result2=command.substring(pos,i); 
					pos=i+1; 
					counter++;
					amountCondition++; 
				}
				else if(counter==9) {
					result2=command.substring(pos,i); 
					pos=i+1; 
					counter++;
					amountCondition++; 
				}


			}
			else if(command.charAt(i)=='=') {
				if(counter==2) {
					varCondition1=command.substring(pos,i); 
					counter++;
					pos=i+1;
				}
				else if(counter==5) {
					varCondition2=command.substring(pos,i); 
					pos=i+1;
					counter++; 
				}
				else if(counter==8) {
					varCondition3=command.substring(pos,i); 
					pos=i+1;
					counter++; 
				}

			}
		}
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
			path=tableName;
			if(checkConditions(conditions)) {
				int amountVar=0; 
				while(nameC[amountVar]!=null)
					amountVar++;

				File xmlFile = new File(getClass().getResource(tableName+".xml").getFile());
				System.out.print((getClass().getResource(tableName+".xml").getFile()));
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
						for(int k=0; k<amountVar; k++) {
							results1[k]= (String) studentElement.getElementsByTagName(nameC[k]).item(0).getTextContent();
						}
						if(deniedAcces(amountCondition, results, results1, operation, conditions, amountVar)) {
							studentElement.getParentNode().removeChild(studentElement);
						}
					}
				}

				// Guardar los cambios en el archivo XML
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(xmlFile);
				transformer.transform(source, result);
				System.out.println("Archivo XML modificado y guardado: " + xmlFile.getAbsolutePath());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}

	public void create(String command, String tableName) {

		int x=0; int j=0; 
		int amountVar=1; String [] variables; int pos=0; 
		if(command.charAt(x)=='(') {
			while(command.charAt(x)!=')' ) {
				if(command.charAt(x)==',')
					amountVar++; 
				x++;
			}
			variables= new String [amountVar]; x=0; pos++;
			while(command.charAt(x)!=')' && x!=command.length()) {
				if(command.charAt(x)==',') {
					variables[j]=command.substring(pos, x); j++;
					pos=x+1;
				}
				x++;
			}
			if(x<command.length()) {
				variables[j]=command.substring(pos, x); 
			}

			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();

				Element rootElement = document.createElement(tableName);
				document.appendChild(rootElement);

				// Crear elementos y agregarlos al elemento raíz
				Element estudianteElement = document.createElement(tableName);
				rootElement.appendChild(estudianteElement);

				for(int i=0; i<amountVar; i++) {
					Element element = document.createElement(variables[i]);
					element.appendChild(document.createTextNode(variables[i]));
					estudianteElement.appendChild(element);
				}


				// Guardar el contenido del documento XML en un archivo
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);

				String path=(getClass().getResource("students.xml").getFile());
				path=path.substring(0, path.length()-13)+"/"+tableName+".xml";
				System.out.println(path);
				StreamResult result = new StreamResult(new File(path));
				transformer.transform(source, result);

				System.out.println("Archivo XML creado correctamente.");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void deleteTable(String tableName) {
		String path=(getClass().getResource("students.xml").getFile());
		path=path.substring(0, path.length()-13)+"/"+tableName+".xml";

		File file = new File(path);

		// Verificar si el archivo existe antes de borrarlo
		if (file.exists()) {
			// Intentar borrar el archivo
			if (file.delete()) {
				System.out.println("El archivo se ha borrado exitosamente.");
			} else {
				System.out.println("No se pudo borrar el archivo.");
			}
		} else {
			System.out.println("El archivo no existe en la ruta especificada.");
		}
	}

	public void Select (String command, String [] variables) {
		String [] operation = new String [3];
		String varCondition1="";  String result1="";
		String varCondition2="";  String result2="";
		String varCondition3="";  String result3=""; int amountCondition=0;
		String tableName=""; int counter=0; int pos=0;
		for(int i=0; i<command.length(); i++) {		
			if(command.charAt(i)==' ' || i==command.length()-1) {
				if(i==command.length()-1 ) i++; 
				if(counter==0) {
					if(command.substring(pos, i).contentEquals("From")) {
						counter++; 
						pos=i+1;
					}
					else
						break;
				}
				else if(counter==1) {
					tableName=command.substring(pos, i);
					counter++; 
					pos=i+1;
				}
				else if(counter==2) {				
					if(command.substring(pos, i).contentEquals("where")) {
						counter++; 
						pos=i+1;
					}
					else if(command.substring(pos, i).contentEquals("inner")) {
						int x=i+1; pos=i+1;  
						while(command.charAt(x)!=' ')		
							x++;
						if(command.substring(pos, x).contentEquals("join")) {
							inner(command.substring(x+1), tableName, variables); 
						}

						break;
					}
					else
						break;
				}
				else if(counter==4) {
					result1=command.substring(pos, i); 
					counter++; 
					amountCondition++;
					pos=i+1;
				}
				else if(counter==5) {
					operation[0]=command.substring(pos, i); 
					counter++; 
					pos=i+1;
				}
				else if(counter==7) {
					result2=command.substring(pos, i); 
					counter++; 
					amountCondition++;
					pos=i+1;
				}
				else if(counter==8) {
					operation[1]=command.substring(pos, i); 
					counter++; 
					pos=i+1;
				}
				else if(counter==10) {
					result3=command.substring(pos, i); 
				}
			}
			else if(command.charAt(i)=='=') {
				if(counter==3) {
					varCondition1=command.substring(pos, i); 
					counter++; 
					pos=i+1; 
				}
				else if(counter==6) {
					varCondition2=command.substring(pos, i); 
					counter++; 
					pos=i+1;
				}
				else if(counter==9) {
					varCondition3=command.substring(pos, i); 
					counter++; 
					pos=i+1;
				}
			}



		}
		if(counter!=2) {
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
				path=tableName;
				if(checkConditions(conditions)) {
					int amountVar=0; 
					while(nameC[amountVar]!=null)
						amountVar++;

					File xmlFile = new File(getClass().getResource(tableName+".xml").getFile());
					System.out.print((getClass().getResource(tableName+".xml").getFile()));
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(xmlFile);

					// Modificar el archivo XML según las condiciones
					Element rootElement = document.getDocumentElement();
					int x=amountCondition;
					boolean flag1=false; boolean flag2=false; boolean flag3=false;

					NodeList elementList = rootElement.getElementsByTagName(tableName);

					linkedList list = new linkedList(); 
					for (int i = 0; i < elementList.getLength(); i++) {
						Element element = (Element) elementList.item(i);
						Node studentNode = elementList.item(i);
						if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
							Element studentElement = (Element) studentNode;
							String response=" ";
							String[] results1 = new String [amountVar];
							for(int k=0; k<amountVar; k++) {
								results1[k]= (String) studentElement.getElementsByTagName(nameC[k]).item(0).getTextContent();
							}
							if(deniedAcces(amountCondition, results, results1, operation, conditions, amountVar)) {
								for(int k=0; k<variables.length; k++) {
									response=response + " " + (String) studentElement.getElementsByTagName(variables[k]).item(0).getTextContent();
								}
								list.insert(response);
							}
						}
					}
					node aux= list.head; 
					res = new response[list.length]; int reP=0;
					while(aux!=null) {
						res[reP]= new response(); 
						res[reP].resp=aux.data; 
						reP++;
						aux=aux.next; 
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public void inner(String command, String tableName, String variables[]) {
		int counter=0; int pos=0; 
		String tableName2=" ";
		String conditionTable1=""; String conditionTable2=""; 
		String varTable1=""; String varTable2=""; 
		int amountCondition1=0;int amountCondition2=0; int amountOperations = 0; 

		String[] operation = new String [2]; int cond=0; 
		String[] operation1 = new String [3]; int z=0; 

		linkedList listVarT1= new linkedList(); linkedList listVarT2= new linkedList();
		linkedList listResT1= new linkedList(); linkedList listResT2= new linkedList();
		operation1[0]="-1";operation1[1]="-1";operation1[2]="-1";

		for(int i=0; i<command.length(); i++) {
			if(command.charAt(i)==' ') {
				if(counter==0) {
					tableName2=command.substring(pos, i); 
					pos=i+1;
					counter++;
				}
				else if(counter==1) {
					if(command.substring(pos,i).contentEquals("on")) {
						counter++;
						pos=i+1;
					}
					else
						break; 
				}
				else if(counter==3 ) {
					System.out.print(command.substring(pos, i));
					if(command.substring(pos, i).contentEquals("where")) {
						counter++; 
						pos=i+1; 
					}
					else 
						break; 

				}
				else if(counter==5  || counter==7) {
					if(command.substring(pos, i).contentEquals("and") || command.substring(pos, i).contentEquals("or")) {
						operation[cond]=command.substring(pos, i);
						cond++; 
						pos=i+1;
						counter++; 
						amountOperations++; 
					}else
						break; 
				}
			}
			else if(command.charAt(i)=='=') {
				if(counter==2) {
					int x=pos; int condition=1; boolean flag=false;
					while(command.length()!=x && command.charAt(x)!=' ') {
						if(command.charAt(x)=='.') {
							if(command.substring(pos, x).contentEquals(tableName2)) {
								flag=false;
							}
							else if(command.substring(pos, x).contentEquals(tableName)) { 
								flag=true;
							}
							pos=x+1;
						}
						else if(command.charAt(x)=='=') {
							if(flag) {
								varTable1=command.substring(pos, x);  
								pos=x+1; 
							}
							else {
								varTable2=command.substring(pos, x); 
								pos=x+1; 
							}

						}
						x++;
					}
					if(flag) {
						varTable1=command.substring(pos, x); 
						pos=x+1; 
					}
					else {
						varTable2=command.substring(pos, x); 
						pos=x+1; 
					}
					pos=x+1;
					i=x; 
					counter++;

				}
				else if(counter==4) {
					int x=pos; boolean flag=false; 
					while(command.length()!=x && command.charAt(x)!=' ' ) {
						if(command.charAt(x)=='.') {
							if(command.substring(pos, x).contentEquals(tableName2)) {
								flag=false;
							}
							else if(command.substring(pos, x).contentEquals(tableName)) {
								flag=true;
							}
							pos=x+1;
						}
						else if(command.charAt(x)=='=') {
							if(flag) {
								listVarT1.insert(command.substring(pos, x));
								pos=x+1; 

							}
							else {
								listVarT2.insert(command.substring(pos, x));
								pos=x+1; 
							}
						}
						x++;
					}
					if(!flag) {	
						listResT2.insert(command.substring(pos, x));
						pos=x+1; 
						amountCondition2++;
						operation1[z]="0";
						z++;
					}
					else {
						System.out.println(command.substring(pos, x));
						listResT1.insert(command.substring(pos, x));
						pos=x+1; 
						amountCondition1++;
						operation1[z]="1";
						z++;
					}
					pos=x+1;
					i=x; 
					counter++;
				}
				else if(counter==6) {
					int x=pos; boolean flag=false; 
					while(command.length()!=x && command.charAt(x)!=' ') {
						if(command.charAt(x)=='.') {
							if(command.substring(pos, x).contentEquals(tableName2)) {
								flag=false;
							}
							else if(command.substring(pos, x).contentEquals(tableName)) {
								flag=true;
							}
							pos=x+1;
						}
						else if(command.charAt(x)=='=') {
							if(flag) {
								listVarT1.insert(command.substring(pos, x));
								pos=x+1; 
							}
							else {
								listVarT2.insert(command.substring(pos, x));
								pos=x+1; 
							}
						}
						x++;
					}
					if(!flag) {
						listResT2.insert(command.substring(pos, x));
						pos=x+1;
						amountCondition2++;
						operation1[z]="0";
						z++;
					}
					else {
						listResT1.insert(command.substring(pos, x));
						pos=x+1; 
						amountCondition1++;
						operation1[z]="1";
						z++;
					}
					pos=x+1;
					i=x; 
					counter++;
				}
				else if(counter==8) {
					int x=pos; boolean flag=false; 
					while(command.length()!=x && command.charAt(x)!=' ') {
						if(command.charAt(x)=='.') {
							if(command.substring(pos, x).contentEquals(tableName2)) {
								flag=false;
							}
							else if(command.substring(pos, x).contentEquals(tableName)) {
								flag=true;
							}
							pos=x+1;
						}
						else if(command.charAt(x)=='=') {
							if(flag) {

								listVarT1.insert(command.substring(pos, x));
								pos=x+1; 
							}
							else {

								listVarT2.insert(command.substring(pos, x));
								pos=x+1; 
							}
						}
						x++;
					}
					if(!flag) {

						listResT2.insert(command.substring(pos, x));
						pos=x+1;
						amountCondition2++;
						operation1[z]="0";

					}
					else {
						listResT1.insert(command.substring(pos, x));
						pos=x+1; 
						amountCondition1++;
						operation1[z]="1";
					}
					pos=x+1;
					i=x; 
					counter++;
				}
			}
		}
		String [] conditions1 = new String[amountCondition1]; 
		String [] results1 = new String[amountCondition1]; 
		String [] conditions2 = new String[amountCondition2]; 
		String [] results2 = new String[amountCondition2]; 

		node auxVT1 = listVarT1.head;
		node auxRT1 = listResT1.head;
		node auxVT2 = listVarT2.head;
		node auxRT2 = listResT2.head;
		int x=0;
		while(auxVT1!=null) {
			conditions1[x]=auxVT1.data;
			results1[x]=auxRT1.data;
			x++;
			auxVT1=auxVT1.next;
			auxRT1=auxRT1.next;
		}x=0;
		while(auxVT2!=null) {
			conditions2[x]=auxVT2.data;
			results2[x]=auxRT2.data;
			x++;
			auxVT2=auxVT2.next;
			auxRT2=auxRT2.next;
		}


		try {

			path=tableName;
			String auxPath=path; String path=tableName2+".xml";
			boolean permission1=false;
			if(amountCondition1==0 ) {
				permission1=true;
			}
			else {
				permission1=checkConditions(conditions1);
			}
			boolean permission2=false;
			if(amountCondition2==0 ) {
				permission2=true;
			}
			else {
				permission2=checkConditions(conditions2);
			}
			if(permission1 && permission2) {
				amountGlobalVar=0; 
				int amountVar=0; 
				while(nameC[amountVar]!=null)
					amountVar++;

				try {
					File xmlFile = new File(getClass().getResource(auxPath+".xml").getFile());
					System.out.print((getClass().getResource(auxPath+".xml").getFile()));
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(xmlFile);

					// Modificar el archivo XML según las condiciones
					Element rootElement = document.getDocumentElement();
					NodeList elementList = rootElement.getElementsByTagName(tableName);

					linkedList list = new linkedList(); 
					for (int i = 0; i < elementList.getLength(); i++) {
						Element element = (Element) elementList.item(i);
						Node studentNode = elementList.item(i);
						if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
							Element studentElement = (Element) studentNode;
							checkElements(studentElement, varTable1, varTable2, path, conditions1, conditions2, results1, results2, operation, operation1, variables, amountOperations);
						}
					}
					node aux=convergenceList.head;
					res = new response [convergenceList.length]; int g=0; 
					while(aux!=null) {
						res[g]=new response();
						res[g].resp=aux.data;
						g++;
						aux=aux.next;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void checkElements(Element element1, String condition1, String condition2, String path, String [] conditions1, String conditions2[],
			String[] results1, String[] results2, String []operation, String[] operation1, String variables[], int amountOperations) {
		try {
			File xmlFile = new File(getClass().getResource(path+".xml").getFile());
			System.out.print((getClass().getResource(path+".xml").getFile()));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);

			// Modificar el archivo XML según las condiciones
			Element rootElement = document.getDocumentElement();


			NodeList elementList = rootElement.getElementsByTagName(path);

			linkedList list = new linkedList();  boolean added=false;
			for (int i = 0; i < elementList.getLength(); i++) {
				Element element = (Element) elementList.item(i);
				Node studentNode = elementList.item(i);
				if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element studentElement = (Element) studentNode;
					String booleanOutput="";
					String auxOperation[] = new String[amountOperations+1];
					for(int r=0; r<(amountOperations+1 );r++)
						auxOperation[r]="0"; 
					String con=(String) studentElement.getElementsByTagName(condition2).item(0).getTextContent();
					System.out.println(element1.getElementsByTagName(condition1).item(0).getTextContent());
					System.out.println(studentElement.getElementsByTagName(condition2).item(0).getTextContent()); 

					if(con.contentEquals(element1.getElementsByTagName(condition1).item(0).getTextContent()) ) {
						for(int p=0; p<conditions1.length;p++) {
							boolean flag=false;
							System.out.println(""); 
							System.out.println(element1.getElementsByTagName(conditions1[p]).item(0).getTextContent()); 
							if(element1.getElementsByTagName(conditions1[p]).item(0).getTextContent().contentEquals(results1[p]))
								flag=true;
							for(int i1=0; i1<3;i1++) {
								if(operation1[i1].contentEquals("1")) {
									if(flag) {
										auxOperation[i1]="1";
										operation1[i1]="-1";
									}
								}
							}
						}
						for(int p=0; p<conditions2.length;p++) {
							boolean flag=false;
							if(studentElement.getElementsByTagName(conditions2[p]).item(0).getTextContent().contentEquals(results2[p]))
								flag=true;
							for(int i1=0; i1<operation1.length;i1++) {
								if(operation1[i1].contentEquals("0")) {
									if(flag) {
										auxOperation[i1]="1";
										operation1[i1]="-1";
									}
								}
							}

						}
						String boolMessage=""; 
						for(int r=0; r<(amountOperations+1 );r++) {
							boolMessage=boolMessage+auxOperation[r];  
						}


						if(checkBooleanMessage(amountOperations, operation, boolMessage, variables)) {
							String message1=""; String message=""; 
							for(int j=0; j<variables.length; j++) {
								if(!added) {
									message1=message1+element1.getElementsByTagName(variables[j]).item(0).getTextContent()+" ";
								}
								message=message+studentElement.getElementsByTagName(variables[j]).item(0).getTextContent()+" ";
							}
							if(!added)
								convergenceList.insert(message1);
							convergenceList.insert(message);

							added=true;
						}
					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public boolean checkBooleanMessage(int amountConditions, String conditions[], String boolMessage, String variables[]) {

		if(amountConditions==0) {
			return true;
		}
		else if(amountConditions==1) {
			if(conditions[0].contentEquals("or")) {
				if(!boolMessage.contentEquals("00"))
					return true;
			}
			else {
				if(boolMessage.contentEquals("11"))
					return true;
			}

		}
		else if(amountConditions==2) {
			if(conditions[0].contentEquals("or") && conditions[1].contentEquals("and")) {
				if(boolMessage.contentEquals("101") || boolMessage.contentEquals("011") || boolMessage.contentEquals("111"))
					return true;
			}
			else if(conditions[0].contentEquals("or") && conditions[1].contentEquals("or")) {
				if(!boolMessage.contentEquals("000"))
					return true;
			}
			else if(conditions[0].contentEquals("and") && conditions[1].contentEquals("or")) {
				if(boolMessage.contentEquals("110") || boolMessage.contentEquals("111"))
					return true;
			}
			else if(conditions[0].contentEquals("and") && conditions[1].contentEquals("and")) {

				return true;
			}



		}

		return false;
	}
}

