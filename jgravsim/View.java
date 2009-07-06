package jgravsim;

import javax.swing.*;

public class View extends JFrame  {
	private static final long serialVersionUID = 1L;
	static final String textfile = "JGravSim_text.xml";
	
	JTabbedPane tp_tabs; /* Die Tabs werden hierrin dargestellt ... */
	TabCompute pa_computetab; /* Berechnung und Ausgabe der Daten */
	TabVisualisation pa_visualtab; /* Visualisierung berechneter Daten */
	TabAbout pa_abouttab; /* Über dieses Programm */
    XMLParser myXMLParser;
	
	View(int lang) {
		super();
		myXMLParser = new XMLParser(textfile,lang);
		setTitle(myXMLParser.getText(0));
		int answer = JOptionPane.showConfirmDialog(this, myXMLParser.getText(1), myXMLParser.getText(1),JOptionPane.YES_NO_OPTION);
		
		tp_tabs = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
		pa_computetab = new TabCompute(myXMLParser); /* Berechnung und Ausgabe der Daten */
		pa_visualtab = new TabVisualisation(myXMLParser, answer==0?true:false ); /* Visualisierung berechneter Daten */
		pa_abouttab = new TabAbout(myXMLParser); /* Über dieses Programm */
		
		// TODO add remove temp-file method!
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		tp_tabs.addTab(myXMLParser.getText(2), pa_computetab);
		tp_tabs.addTab(myXMLParser.getText(3), pa_visualtab);
		tp_tabs.addTab(myXMLParser.getText(4), pa_abouttab);

		
		add(tp_tabs);
		setSize(975,740);
		setVisible(true);
	}
}