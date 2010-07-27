package jgravsim;

import java.awt.Color;
import java.io.*;
import java.util.Locale;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class View_CalcOptions extends JFrame implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	static final boolean DEBUG = true;
	static final String exe = "cgravsim";
	static Color defaultColor = null;
	
	Controller myController;
	XMLParser myXMLParser;
	Model myModel;
	ProcessBuilder calculation;
	Process calculationp;
	
	JPanel pan_main;
		JPanel pan_headline;
		
		JPanel pan_input_datacount;
			JTextField tf_datacount;
			JLabel la_datacount;
		JPanel pan_input_timecount;
			JTextField tf_timecount;
			JLabel la_timecount;
		JPanel pan_input_timestep;
			JTextField tf_timestep;
			JLabel la_timestep;
	
		JPanel pan_buttons;
			JButton b_calculate;
			JButton b_cancel ;
			
		JPanel pan_options;
			JCheckBox cb_oldcalculation;
			
	private	static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
			
	View_CalcOptions(Controller MyController, Model MyModel) {
		myModel = MyModel;
		myController = MyController;
		myXMLParser = myController.myView.myXMLParser;
		
		setLayout(new GridLayout(0,1));
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		addWindowListener(this);
		b_calculate = new JButton(myXMLParser.getText(202));
		b_cancel = new JButton(myXMLParser.getText(213));
		b_cancel.addActionListener(this);
		b_calculate.addActionListener(this);
		
		setTitle(myXMLParser.getText(220));
	   
		Dimension dSizeDaten = new Dimension(160,21);  
		
		
		la_datacount = new JLabel(myXMLParser.getText(221));
		//Datacount field
		String datacount;
		if(myController.calc_datacount > 0){
			datacount = InterpretInput.niceInput_Time(myController.calc_datacount, myController.myView.myXMLParser);
		}
		else
			datacount = InterpretInput.niceInput_Time(CalcCode.DATACOUNTX, myController.myView.myXMLParser);
		
		tf_datacount = new JTextField(datacount);
		tf_datacount.setMinimumSize(dSizeDaten);
		tf_datacount.setPreferredSize(dSizeDaten);
		tf_datacount.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_datacount.setEditable(true);
		
		
		la_timecount = new JLabel(myXMLParser.getText(222));
		//Timecount field
		String timecount;
		if(myController.calc_timecount > 0){
			timecount = InterpretInput.niceInput_Time(myController.calc_timecount, myController.myView.myXMLParser);
		}
		else
			timecount = InterpretInput.niceInput_Time(CalcCode.TIMECOUNTX, myController.myView.myXMLParser);
		
		tf_timecount = new JTextField(timecount);
		tf_timecount.setMinimumSize(dSizeDaten);
		tf_timecount.setPreferredSize(dSizeDaten);
		tf_timecount.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_timecount.setEditable(true);
		
		
		la_timestep = new JLabel(myXMLParser.getText(223));
		//Timestep field
		String timestep;
		if(myController.calc_timestep > 0){
			timestep = InterpretInput.niceInput_Time(myController.calc_timestep, myController.myView.myXMLParser);
		}
		else
			timestep = InterpretInput.niceInput_Time(CalcCode.TIMESTEPX, myController.myView.myXMLParser);
		
		tf_timestep = new JTextField(timestep);
		tf_timestep.setMinimumSize(dSizeDaten);
		tf_timestep.setPreferredSize(dSizeDaten);
		tf_timestep.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_timestep.setEditable(true);

		pan_buttons = new JPanel(new GridLayout(0,2));
		
		pan_buttons.add(b_calculate);
		pan_buttons.add(b_cancel);

		pan_options = new JPanel(new FlowLayout(FlowLayout.LEFT));

		cb_oldcalculation = new JCheckBox(myXMLParser.getText(224), true);
		if(Controller.CPP == true) {
			pan_options.add(cb_oldcalculation);
		}
		
		pan_main = new JPanel(new GridLayout(0,1));
		pan_main.add(la_datacount);
		pan_main.add(tf_datacount);
		pan_main.add(la_timecount);
		pan_main.add(tf_timecount);
		pan_main.add(la_timestep);
		pan_main.add(tf_timestep);
		
		pan_main.add(pan_buttons);
		pan_main.add(pan_options);
		pan_main.setSize(pan_main.getPreferredSize().width+20, pan_main.getPreferredSize().height+80);
		
		add(pan_main);
		adjustWindow(this);
		setSize(pan_main.getSize());
		defaultColor = tf_datacount.getBackground();
		setVisible(true);
		setAlwaysOnTop(true);
	}	
	
	void adjustWindow(Component frame) {
		Point ViewPos = myController.myView.getLocation();
		Dimension ViewSize = myController.myView.getSize();
		Dimension frameSize = this.getPreferredSize();
	
		frame.setLocation(
		(2*ViewPos.x - frameSize.width  + ViewSize.width >> 1),
		(2*ViewPos.y - frameSize.height + ViewSize.height >> 1));
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == b_cancel) {
			myController.ThreadFinished(null, 0);
			this.dispose();
		}
		else if(source == b_calculate) {
			tf_datacount.setBackground(defaultColor);
			tf_timecount.setBackground(defaultColor);
			tf_timestep.setBackground(defaultColor);
			b_calculate.setBackground(defaultColor);

			String[] sadata = {tf_datacount.getText(),tf_timecount.getText(),tf_timestep.getText()};
			boolean[] bacheck = CheckInput(sadata);
			boolean flagcheck = true;
			for(int i=0;i<bacheck.length;i++) {
				if(bacheck[i]) {
					b_calculate.setBackground(Color.RED);
					switch(i) {
					case 0: tf_datacount.setBackground(Color.RED); break;
					case 1: tf_timecount.setBackground(Color.RED); break;
					case 2: tf_timestep.setBackground(Color.RED); break;
					}
					flagcheck = false;
				}
			}
			if(flagcheck) {
				if(myController.calc_datacount/myController.calc_timecount > DynamicWPTLoader.STANDARDBUFFERSIZE) {
					String question = String.format(Locale.getDefault(),myController.myView.myXMLParser.getText(174), (int)Math.ceil(myController.calc_datacount/myController.calc_timecount/DynamicWPTLoader.STANDARDBUFFERSIZE));
					int answer = JOptionPane.showConfirmDialog(myController.myView, question,myController.myView.myXMLParser.getText(173),JOptionPane.YES_NO_OPTION);
					if(answer != 0)
						return;
				}
				myController.flagcalc = true;
				myModel.writetempHeader(myController.calc_datacount,myController.calc_timecount);
				myModel.AddStep(myController.getVMasspoints());

				this.dispose();

				if(cb_oldcalculation.isSelected() == false) {
					debugout("actionPerformed - Java - Calculation : datacount="+myController.calc_datacount+", timecount="+myController.calc_timecount+", timestep="+myController.calc_timestep);
					myController.myCalculation = new CalcCode(myController,myModel,myController.calc_datacount,myController.calc_timecount,myController.calc_timestep, false);
					myController.myView.pa_computetab.b_stop.setEnabled(true);
					myController.myCalculation.start();
				}
				else {
					Runtime run = Runtime.getRuntime();
					debugout("actionPerformed - C++ - Calculation : datacount="+myController.calc_datacount+", timecount="+myController.calc_timecount+", timestep="+myController.calc_timestep);
					try {
						debugout("EoD - See: os.name	= "+System.getProperty("os.name"));
						debugout("EoD - See: os.arch	= "+System.getProperty("os.arch"));
						debugout("EoD - See: os.version	= "+System.getProperty("os.version"));
						debugout("EoD - See: file.separator = "+System.getProperty("file.separator"));

						String exedir= System.getProperty("user.dir").toString();

						String filename = exe;
						if(Controller.CPPDEBUG)
							filename += "dbg";
						
						if(System.getProperty("os.arch").matches("i[1-9]86"))
							filename += "_x86";
						else
							filename += "_"+System.getProperty("os.arch");
						
						String[] command = new String[4];
						command[0] = exedir+File.separator+filename;
						command[1] = "-t";
						command[2] = Double.toString(myController.calc_timestep);
						command[3] = exedir+File.separator+Model.FILE_TEMP;
						
						if(System.getProperty("os.name").contains("Windows"))
							command[0] += ".exe";

						File fcalc = new File(command[0]);
						while(!fcalc.exists()) {
							if(System.getProperty("os.arch").contains("amd64") && command[0].contains("amd64")) {
								debugout("WARNING - actionPerformed() - could not find C++ file for amd64, switchting to x86");
								command[0] = command[0].replace("amd64", "x86");
								fcalc = new File(command[0]);
								continue;
							}

							JOptionPane.showMessageDialog(myController.myView, myController.myView.myXMLParser.getText(155) + ": " + command[0], myController.myView.myXMLParser.getText(156), JOptionPane.INFORMATION_MESSAGE);
							throw new IOException("EoD - File "+command[0]+" not found!");
						}

						if(!fcalc.canRead()) {
							String question = myController.myView.myXMLParser.getText(157) + " " + command[0] + "\n" + myController.myView.myXMLParser.getText(178);
							int answer = JOptionPane.showConfirmDialog(myController.myView, question,myController.myView.myXMLParser.getText(156),JOptionPane.YES_NO_OPTION);
							if(answer == 0) {
								if(!fcalc.setReadable(true)) {
									JOptionPane.showMessageDialog(myController.myView, myController.myView.myXMLParser.getText(172), myController.myView.myXMLParser.getText(173), JOptionPane.INFORMATION_MESSAGE);
									throw new IOException("EoD - Could not set read permission for cgravsim!");
								}
							}
							else
								throw new IOException("EoD - File not readable!");
						}

						if(!fcalc.canExecute()) {
							String question = String.format(Locale.getDefault(), myController.myView.myXMLParser.getText(177) + "\n" + myController.myView.myXMLParser.getText(178), command[0]);
							int answer = JOptionPane.showConfirmDialog(myController.myView, question,myController.myView.myXMLParser.getText(176),JOptionPane.YES_NO_OPTION);
							if(answer == 0) {
								if(!fcalc.setExecutable(true)) {
									JOptionPane.showMessageDialog(myController.myView, myController.myView.myXMLParser.getText(172), myController.myView.myXMLParser.getText(173), JOptionPane.INFORMATION_MESSAGE);
									throw new IOException("EoD - Could not set execute permission for cgravsim!");
								}
							}
							else
								throw new IOException("EoD - File not executeable!");
						}

						//getVersion Number and compare it to Frontend version
						debugout("actionPerformed - C++ - Calculation : Command='"+command[0]+" "+" -v"+"'");
						Process versioncheck = run.exec( command[0]+" -v" );
						BufferedReader in_version = new BufferedReader( new InputStreamReader(versioncheck.getInputStream()) );
						String version;
						while ((version = in_version.readLine()) != null) {
							if(version.startsWith("Frontend")) {
								double dversion = Double.parseDouble(version.split(":")[1]);
								if(dversion != Controller.VERSION) {
									debugout("actionPerformed() - wrong version. Expected: v"+Controller.VERSION+", received: v"+dversion);
									String question = String.format(Locale.getDefault(), myController.myView.myXMLParser.getText(225), 1.22, 1.8);
									int awnser = JOptionPane.showConfirmDialog(myController.myView, question, myController.myView.myXMLParser.getText(40),JOptionPane.YES_NO_OPTION);
									if(awnser != 0) {
										in_version.close();
										myController.CalculationFinished(CalcCode.UNKNOWN);
										return;
									}
								}
								else {
									debugout("actionPerformed() - backend dversion:"+dversion);
									break;
								}
							}
							version = null;
						}
						in_version.close();
						if(version == null)
							throw new IOException("EoD - Error while executing "+command[0]+" -v !");

						debugout("actionPerformed - C++ - Calculation : Command='"+command[0]+" "+command[1]+" "+command[2]+" "+command[3]);
						if(System.getProperty("os.name").contains("Windows")) {
							calculation = new ProcessBuilder( new String[]{"cmd.exe", "/C", command[0], command[1], command[2], command[3]} );
						}
						else
							calculation = new ProcessBuilder( command );
						//TODO Fix stop button functionality
						//myController.myView.pa_computetab.b_stop.setEnabled(true);
						calculationp = calculation.start();
						if(calculationp == null)
							throw new IOException("EoD - Error while executing "+command[0]+" "+command[1]+" "+command[2]+" "+command[3]);
						
						myController.myCalcProgress = new CalcProgress(myController, Model.FILE_PERCENT, calculationp);
						myController.myCalcProgress.start();
	    			} catch(Exception excep) {
					    debugout(excep.getMessage());
				   		myController.CalculationFinished(CalcCode.UNKNOWN);
	    			}
	    		}
    		}
		}
	}

	public boolean[] CheckInput(String[] sa_textfields) {
		double dtemp = 0.0;
		double factor = 1.0;
		boolean[] bacheck = new boolean[3];
		
		if(sa_textfields == null) {
			debugout("CheckInput() - Error");
			return null;
		}
		
		for(int i=0;i<sa_textfields.length;i++) {
			String strtemp = sa_textfields[i];
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			String[] stratemp = strtemp.split(" ");
			strtemp = stratemp[0];
			try {
					factor = InterpretInput.checkInput_Time(stratemp[1], myController.myView.myXMLParser);
					if(factor == 0) { 
						bacheck[i] = true;
						debugout("CheckInput() - hmm:"+stratemp[1]);
					}
					dtemp = Double.parseDouble(strtemp);
			} catch(NumberFormatException e) {
				bacheck[i] = true;
			}
			if(i > 0) {	// myController.calc_dtimecount
				if(bacheck[0] == false && bacheck[1] == false) {
					if(myController.calc_datacount < dtemp*factor) {
						bacheck[0] = true;
						bacheck[1] = true;
					}
				}
			}
			if(i > 1) {	 // myController.calc_timestep
				if(bacheck[0] == false && bacheck[2] == false) {
					if(myController.calc_datacount < dtemp*factor) {
						bacheck[0] = true;
						bacheck[2] = true;
					}
				}
				if(bacheck[1] == false && bacheck[2] == false) {
					if(myController.calc_timecount < dtemp*factor) {
						bacheck[1] = true;
						bacheck[2] = true;
					}
				}
			}
			
			if(bacheck[i] == false) {
				switch(i) {
					case 0: myController.calc_datacount = dtemp*factor;
					case 1: myController.calc_timecount = dtemp*factor;
					case 2: myController.calc_timestep = dtemp*factor;
				}
			}
		}
		
		return bacheck;
	}
	
	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
		debugout("windowClosing - CalcOptions has been closed.");
		if(myController.flagcalc == false) {
			myController.ThreadFinished(null, 0);
			myController.myView.pa_computetab.ButtonsStd();
		}
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
}
