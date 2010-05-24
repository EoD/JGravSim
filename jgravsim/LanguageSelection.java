package jgravsim;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class LanguageSelection extends JFrame {
	public static final long serialVersionUID = 64L;
	
	static private final boolean DEBUG = true;

	private class Language {
		private final int id;
		private final String name;
		
		public Language(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}
	
	Vector<Language> languages = new Vector<Language>();
	
	private void debugout(String a){
		if(Controller.CURRENTBUILD && DEBUG )
			System.out.println(a);
	}
	
	public boolean readLanguages(String filename) {
		boolean skip = false;
		
		debugout("LanguageSelection initialised.");
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
							Language language = new Language(							
							Integer.parseInt(element.getAttributeByName(new QName("id")).getValue()),
							element.getAttributeByName(new QName("name")).getValue().toString() );
							
							languages.addElement(language);
							skip = true;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(!skip)
						break;
					
					if(event.asEndElement().getName().toString() == "language") 
							skip = false;
					break;
				case XMLStreamConstants.ATTRIBUTE:
					break;
				default:
					break;
				}
			}
			parser.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "The file "+filename+" could not be found. Please be sure to put the file into the same directory where your JGravSim jar file is!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void writeLanguages() {
		debugout("#Languages "+languages.size());
		for(int i=0; i<languages.size(); i++) {
			Language language = languages.get(i);
			debugout("Language: ID="+language.id+", name="+language.name);
		}
	}
}
