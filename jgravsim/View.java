package jgravsim;

import javax.swing.*;

@SuppressWarnings("serial")
public class View extends JFrame  {
	public static final int revision = 5;
	
	static final String textfile = "JGravSim_text.xml";
	
	JTabbedPane tp_tabs; /* Die Tabs werden hierrin dargestellt ... */
	TabCompute pa_computetab; /* Berechnung und Ausgabe der Daten */
	TabVisualisation pa_visualtab; /* Visualisierung berechneter Daten */
	TabAbout pa_abouttab; /* Über dieses Programm */
    XMLParser myXMLParser;

	public static final float ZOOM_STEP = 0.1f;
	public static final float ZOOM_DEFAULT = 7.5f;
	public static final int ZOOM_MIN = 13;
	public static final int ZOOM_MAX = 162;
	
	public static final int GRID_MIN = 15;
	public static final int GRID_MAX = 200;
	public static final int GRID_DEFAULT = 25;
	
	public static final int SL_MINTICK = 10;
	public static final int SL_MAJTICK = 50;

	public static final int TAB_VISUAL_ID = 1;

	public static final int WIDTH_VIEW = 975;
	public static final int HEIGHT_VIEW = 740;
	/* Variables for 3D visual controls JFrame */
	public static final int WIDTH_VISUALFRAME = 500;
	public static final int HEIGHT_VISUALFRAME = 400;
	public static final int HEIGHT_VISUALFRAME_OFFSET = 50;
	
	View(int lang) {
		super();
		myXMLParser = new XMLParser(textfile,lang);
		setTitle(myXMLParser.getText(0));
		
		Masspoint.myXMLParser = myXMLParser;
		Masspoint_Sim.myXMLParser = myXMLParser;
		
		int answer = 1;
		if(Controller.CURRENTBUILD && hasJ3D())
			answer = JOptionPane.showConfirmDialog(this, myXMLParser.getText(1), myXMLParser.getText(1),JOptionPane.YES_NO_OPTION);
		
		tp_tabs = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
		pa_computetab = new TabCompute(myXMLParser); /* Berechnung und Ausgabe der Daten */
		pa_visualtab = new TabVisualisation(myXMLParser, answer==0?true:false ); /* Visualisierung berechneter Daten */
		pa_abouttab = new TabAbout(myXMLParser); /* Über dieses Programm */
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		tp_tabs.addTab(myXMLParser.getText(2), pa_computetab);
		tp_tabs.addTab(myXMLParser.getText(3), pa_visualtab);
		tp_tabs.addTab(myXMLParser.getText(4), pa_abouttab);

		add(tp_tabs);
		setSize(WIDTH_VIEW, HEIGHT_VIEW);
		setVisible(true);
	}

	private boolean hasJ3D() {
		try {
			//Check for (one of the) Java3D classes
			Class.forName("com.sun.j3d.utils.universe.SimpleUniverse");
			Controller.debugout("Java3D found");
		}
		catch(ClassNotFoundException e) {
			Controller.debugout("Java3D not found!");
			return false;
		}
		catch(UnsatisfiedLinkError e) {
			Controller.debugout("Java3D binary problems!");
			return false;	
		}
		return true;
	}
}
