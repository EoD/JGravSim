package jgravsim;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TabAbout extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public TabAbout(XMLParser myXMLParser) {
		setLayout(new GridLayout(0,1));
		String sAbouttext = (Controller.MAINDEBUG==true || Controller.CPPDEBUG==true?
								myXMLParser.getText(40)+": "+Controller.VERSION+" current build\n" +
								myXMLParser.getText(45)+":\n"+
									(Controller.CPPDEBUG==true?"* C++ Debug\n":"")+
									(Controller.MAINDEBUG==true?
										"* Java Debug: Controller "+
										(CalcCode.DEBUG==true?"- Calculation ":"")+
										(Masspoint.DEBUG==true?"- Masspoint ":"")+
										(Model.DEBUG==true?"- File I/O ":"")+
										(MVMath.DEBUG==true?"- MVector Math ":"")+
										(View_CalcOptions.DEBUG==true?"- CalcOption":"")
									:"")
							:myXMLParser.getText(0)+"\nv"+Controller.VERSION)+
							"\n\n"+myXMLParser.getText(41)+"\n"+myXMLParser.getText(42)+"\n";
		
		String sunits = "\n\n\n\n"+myXMLParser.getText(43)+":\n\n" +
				myXMLParser.getText(49)+":\t"+
									myXMLParser.getText(440)+", "+
									myXMLParser.getText(441)+", "+
									myXMLParser.getText(442)+", "+
									myXMLParser.getText(443)+", "+
									myXMLParser.getText(444)+", "+
									myXMLParser.getText(445)+", "+
									myXMLParser.getText(446)+", "+
									myXMLParser.getText(447)+", "+
									myXMLParser.getText(448)+", "+
									myXMLParser.getText(449)+"\n" +	
				myXMLParser.getText(45)+":\t"+
											myXMLParser.getText(451)+", "+
											myXMLParser.getText(450)+", "+
											myXMLParser.getText(452)+", "+
											myXMLParser.getText(453)+", "+
											myXMLParser.getText(454)+", "+
											myXMLParser.getText(455)+"\n" +
				myXMLParser.getText(46)+":\t"+
											myXMLParser.getText(462)+", "+
											myXMLParser.getText(461)+", "+
											myXMLParser.getText(460)+", "+
											myXMLParser.getText(463)+", "+
											myXMLParser.getText(464)+", "+
											myXMLParser.getText(465)+"\n" +
				myXMLParser.getText(47)+":\t\t"+
											myXMLParser.getText(470)+", "+
											myXMLParser.getText(471)+", "+
											myXMLParser.getText(472)+", "+
											myXMLParser.getText(473)+", "+
											myXMLParser.getText(474)+", "+
											myXMLParser.getText(475)+", "+
											myXMLParser.getText(476)+", "+
											myXMLParser.getText(477)+", "+
											myXMLParser.getText(478)+", "+
											myXMLParser.getText(479)+"\n" +
				myXMLParser.getText(48)+":\t"+
											myXMLParser.getText(480)+", "+
											myXMLParser.getText(481)+", "+
											myXMLParser.getText(482)+"\n";
		
		sAbouttext += sunits;
		JTextArea ta_abouttext = new JTextArea(sAbouttext);
		ta_abouttext.setEditable(false);
		ta_abouttext.setWrapStyleWord(true);
		ta_abouttext.setLineWrap(true);
		add(ta_abouttext);
	}
}
