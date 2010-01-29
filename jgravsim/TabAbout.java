package jgravsim;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TabAbout extends JPanel {
	public static final long serialVersionUID = LastChangedRevision ;
	
	public JTextArea jHomepage;
	public JTextArea jContact;
	
	public TabAbout(XMLParser myXMLParser) {
		setLayout(new FlowLayout(FlowLayout.LEADING, 15, 17) );	
		setBackground(Color.WHITE);
		
		String sTitle = "JGravSim";
			//+Controller.VERSION+(Controller.MAINDEBUG==true || (Controller.CPP==true && Controller.CPPDEBUG==true)?" current build":"");
		
		String sSubTitle = myXMLParser.getText(0) + "\nv" + Controller.VERSION +
			(Controller.MAINDEBUG==true || (Controller.CPP==true && Controller.CPPDEBUG==true)?" current build":"");
		
		String sAbouttext =	(Controller.MAINDEBUG==true || (Controller.CPP==true && Controller.CPPDEBUG==true)? 
								myXMLParser.getText(41)+": " + Controller.BUILD + "\n" +
								myXMLParser.getText(400)+":\n"+
									((Controller.CPP==true && Controller.CPPDEBUG==true)?"  C++ Debug\n":"")+
									(Controller.MAINDEBUG==true?
										"  Java Debug: Controller "+
										(CalcCode.DEBUG==true?"- Calculation ":"")+
										(Masspoint.DEBUG==true?"- Masspoint ":"")+
										(Model.DEBUG==true?"- File I/O ":"")+
										(MVMath.DEBUG==true?"- MVector Math ":"")+
										(View_CalcOptions.DEBUG==true?"- CalcOption":"")
									:"")
							+"\n\n":"")
							+myXMLParser.getText(401)+"\n"+myXMLParser.getText(402); //+"\n\n";
		
		String sHomepage = "\n"+myXMLParser.getText(403)+":\t"+Controller.HOMEPAGE+"\n";
		sHomepage += "\t"+myXMLParser.getText(404); //+"\n";
		
		String sContact = "\n"+myXMLParser.getText(405)+":\t"+Controller.EMAIL+"\n";
		sContact += "\t"+myXMLParser.getText(406); //+"\n";

		
		String sunits = "\n\n"+myXMLParser.getText(43)+":\n\n" +
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
				myXMLParser.getText(47)+":\t"+
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
		
		Font font_default = this.getFont();
		
		JTextArea jTitle = new JTextArea(sTitle);
		jTitle.setEditable(false);
		jTitle.setWrapStyleWord(true);
		jTitle.setLineWrap(true);
		Font font_title = font_default.deriveFont(Font.BOLD, font_default.getSize2D()*2.0f);
		jTitle.setFont(font_title);

		JTextArea jSubtitle = new JTextArea(sSubTitle);
		jSubtitle.setEditable(false);
		jSubtitle.setWrapStyleWord(true);
		jSubtitle.setLineWrap(true);
		Font font_subtitle = font_default.deriveFont(Font.ITALIC, font_default.getSize2D()*1.3f);
		jSubtitle.setFont(font_subtitle);
		
		JTextArea jAboutText = new JTextArea(sAbouttext);
		jAboutText.setEditable(false);
		jAboutText.setWrapStyleWord(true);
		jAboutText.setLineWrap(true);

		jHomepage = new JTextArea(sHomepage);
		jHomepage.setEditable(false);
		jHomepage.setWrapStyleWord(true);
		jHomepage.setLineWrap(true);
		
		jContact = new JTextArea(sContact);
		jContact.setEditable(false);
		jContact.setWrapStyleWord(true);
		jContact.setLineWrap(true);
		
		JTextArea jUnits = new JTextArea(sunits);
		jUnits.setEditable(false);
		jUnits.setWrapStyleWord(true);
		jUnits.setLineWrap(true);
		
		add(jTitle);
		add(jSubtitle);
		add(jAboutText);
		add(jHomepage);
		add(jContact);
		add(jUnits);
	}
}
