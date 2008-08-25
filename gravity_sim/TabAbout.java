package gravity_sim;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TabAbout extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public TabAbout() {
		setLayout(new GridLayout(0,1));
		String sAbouttext = (Controller.MAINDEBUG==true?
								"Version: "+Controller.VERSION+" current build\n" +
								"Debug: - Controller -"+
									(CalcCode.DEBUG==true?" Calculation -":"")+
									(Masspoint.DEBUG==true?" Masspoint -":"")+
									(Model.DEBUG==true?" File I/O -":"")+
									(MVMath.DEBUG==true?" Vector Math -":"")+
									(CalcOptions.DEBUG==true?" CalcOption Frame -":"")
								+"\n":
							"Relativistischer Gravitations Simulator von C. Schmidbauer\nv"+Controller.VERSION)+
							"\n\nDieses Programm wurde zur Simulation von Gravitationseinflüssen zwischen Objekten entwickelt.\n"+
							"Ich habe mich bemüht einen gewissen Grad physikalischer Genauigkeit einzuhalten, kann aber " +
							"keine Garantie für die Verwendbarkeit der berechneten Ergebnisse dieses Programms übernehmen.\n";
		
		String sunits = "\n\n\n\nBekannte Einheiten:\n\n" +
				"Längeneinheiten: nm, µm, mm, m, km, AE, Lj\n" +
				"Zeiteinheiten:        ms, s, min, h, d, a\n" +
				"Masseneinheiten: µg, mg, g, kg, t\n";
		
		sAbouttext += sunits;
		JTextArea ta_abouttext = new JTextArea(sAbouttext);
		ta_abouttext.setEditable(false);
		ta_abouttext.setWrapStyleWord(true);
		ta_abouttext.setLineWrap(true);
		add(ta_abouttext);
	}
}
