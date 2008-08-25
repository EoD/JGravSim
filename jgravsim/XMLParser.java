package jgravsim;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLParser {
	
	static private final boolean DEBUG = true;

	String text[];
	int size;
	
	private void debugout(String a){
		if(DEBUG == true && Controller.MAINDEBUG==true)
			System.out.println(a);
	}
	
	public XMLParser(String filename, int ilanguage) {
		size = 0;
		boolean skip = false;
		int id = 0;
		
		debugout("XMLParser initialised. lang id="+ilanguage);
		try {
			InputStream in = new FileInputStream(filename);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader parser = factory.createXMLEventReader(in);
			while (parser.hasNext()) {
				XMLEvent event = parser.nextEvent();
				switch (event.getEventType()) {
				case XMLStreamConstants.START_DOCUMENT:
					break;
				case XMLStreamConstants.END_DOCUMENT:
					parser.close();
					break;
				case XMLStreamConstants.START_ELEMENT:
					if(skip)
						break;
					
					StartElement element = event.asStartElement();
					if(element.getName().toString() == "language") {
						if(Integer.parseInt(element.getAttributeByName(new QName("id")).getValue()) == ilanguage) {
							size = Integer.parseInt(element.getAttributeByName(new QName("maxid")).getValue());
							text = new String[size];
							DynamicWPTLoader.STANDARDBUFFERSIZE = Integer.parseInt(element.getAttributeByName(new QName("maxbuffer")).getValue());
							break;
						}
						else {
							debugout("XMLParser() - Wrong language - skipping. "+QName.valueOf("id")+"="+Integer.parseInt(element.getAttributeByName(new QName("id")).getValue()));
							skip = true;
						}
					}
					else if(element.getName().toString() == "text" && skip == false) {
						if(id >= Integer.MAX_VALUE) {
							debugout("XMLParser - ID ERROR");
							id = Integer.MAX_VALUE-1;
							break;
						}
						id = Integer.parseInt(element.getAttributeByName(new QName("id")).getValue());
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(skip)
						break;
					
					Characters characters = event.asCharacters();
					if (!characters.isWhiteSpace()) {
						text[id] = replaceEscape(characters.getData());
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(skip) {
						if(event.asEndElement().getName().toString() == "language") 
							skip = false;
					}
					
					//debugout(spacer.toString() + "END_ELEMENT: "+ event.asEndElement().getName());
					//spacer.delete((spacer.length() - 2), spacer.length());
					break;
				case XMLStreamConstants.ATTRIBUTE:
					break;
				default:
					break;
				}
			}
		} catch (FileNotFoundException e) {
			debugout("FileNotFoundException");
		} catch (XMLStreamException e) {
			debugout("XMLStreamException");
		}
	}

	public String getText(int id) {
		if(text[id] == null || text[id] == "" || id >= size)
			return "MISSING ID#"+String.valueOf(id);
		else
			return text[id];
	}
	
	public void getAll() {
		for(int i=0;i<size;i++) {
			debugout("getAll() - id="+i+", text="+text[i]);
		}
	}
	
	private String replaceEscape(String str_in) {
		String lb = "\\n";
		String tb = "\\t";
		
		if(str_in.contains(lb)) {
			debugout("replaceEscape - lb="+lb+", str_in="+str_in);
			str_in = str_in.replace(lb, "\n");
		}		
		if(str_in.contains(tb)) {
			debugout("replaceEscape - tb="+tb+", str_in="+str_in);
			str_in = str_in.replace(tb, "\t");
		}
		return str_in;
	}
}
