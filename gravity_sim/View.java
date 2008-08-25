package gravity_sim;

import javax.swing.*;

public class View extends JFrame  {
	private static final long serialVersionUID = 1L;
	
	JTabbedPane tp_tabs = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
	TabCompute pa_computetab = new TabCompute(); /* Berechnung und Ausgabe der Daten */
	TabVisualisation pa_visualtab = new TabVisualisation(); /* Visualisierung berechneter Daten */
	TabAbout pa_abouttab = new TabAbout(); /* Ãœber dieses Programm */
	
	View(String title) {
		super(title);
		// TODO add remove temp-file method!
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		tp_tabs.addTab("Rechnung", pa_computetab);
		tp_tabs.addTab("Darstellung", pa_visualtab);
		tp_tabs.addTab("Über", pa_abouttab);

		
		add(tp_tabs);
		setSize(950,740);
		setVisible(true);
	}
}