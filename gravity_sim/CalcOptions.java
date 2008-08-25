package gravity_sim;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class CalcOptions extends JFrame implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	static final boolean DEBUG = true;
	
	Controller myController;
	Model myModel;
	boolean flagclose;
	
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
			JButton b_calculate = new JButton("Berechnung starten");
			JButton b_cancel = new JButton("Abbrechen");
			
	public void debugout(String a) {
		if(Controller.MAINDEBUG && DEBUG)
			System.out.println(a);
	}
			
	CalcOptions(Controller MyController, Model MyModel) {
		myModel = MyModel;
		myController = MyController;
		
		
		setLayout(new GridLayout(0,1));
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		addWindowListener(this);
		b_cancel.addActionListener(this);
		b_calculate.addActionListener(this);
		
		setTitle("Optionen zur Berechnung");;
	   
		Dimension dSizeDaten = new Dimension(160,21);  
	    
		
		la_datacount = new JLabel("" +
				"Wie lange soll voraus berechnet werden?");
		//Datacount field
		String datacount;
		if(myController.calc_datacount > 0){
			datacount = getNiceOutput(myController.calc_datacount);
		}
		else
			datacount = getNiceOutput(CalcCode.DATACOUNTX);
		
		tf_datacount = new JTextField(datacount);
		tf_datacount.setMinimumSize(dSizeDaten);
		tf_datacount.setPreferredSize(dSizeDaten);	
		tf_datacount.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_datacount.setEditable(true);
		
		
		la_timecount = new JLabel("" +
				"In welchen Zeitabständen soll die Berechnung gespeichert werden?");
		//Timecount field
		String timecount;
		if(myController.calc_timecount > 0){
			timecount = getNiceOutput(myController.calc_timecount);
		}
		else
			timecount = getNiceOutput(CalcCode.TIMECOUNTX);
		
		tf_timecount = new JTextField(timecount);
		tf_timecount.setMinimumSize(dSizeDaten);
		tf_timecount.setPreferredSize(dSizeDaten);	
		tf_timecount.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_timecount.setEditable(true);
		
		
		la_timestep = new JLabel("" +
				"Mit welchen Zeitabständen soll gerechnet werden?");
		//Timestep field
		String timestep;
		if(myController.calc_timestep > 0){
			timestep = getNiceOutput(myController.calc_timestep);
		}
		else
			timestep = getNiceOutput(CalcCode.TIMESTEPX);
		
		tf_timestep = new JTextField(timestep);
		//tf_timestep.setHorizontalAlignment(JTextField.CENTER);
		tf_timestep.setMinimumSize(dSizeDaten);
		tf_timestep.setPreferredSize(dSizeDaten);	
		tf_timestep.setBorder( new BevelBorder(BevelBorder.RAISED) );
		tf_timestep.setEditable(true);

		pan_buttons = new JPanel(new GridLayout(0,2));
		
		pan_buttons.add(b_calculate);
		pan_buttons.add(b_cancel);

		pan_main = new JPanel(new GridLayout(0,1));
		pan_main.add(la_datacount);
		pan_main.add(tf_datacount);
		pan_main.add(la_timecount);
		pan_main.add(tf_timecount);
		pan_main.add(la_timestep);
		pan_main.add(tf_timestep);
		
		//pan_main = new JPanel(new GridLayout(0,1));
		pan_main.add(pan_buttons);
		pan_main.setSize(pan_main.getPreferredSize().width+20, pan_main.getPreferredSize().height+80);
		
		add(pan_main);
		adjustWindow(this);	
		setSize(pan_main.getSize());
		setVisible(true);
	}	
	
	void adjustWindow(Component frame) {	
		Point ViewPos = myController.myView.getLocation();
		Dimension ViewSize = myController.myView.getSize();
		Dimension frameSize = this.getPreferredSize();
        
		frame.setLocation(
                (2*ViewPos.x - frameSize.width  + ViewSize.width >> 1),
                (2*ViewPos.y - frameSize.height + ViewSize.height >> 1)
        );
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == b_cancel) {
			myController.ThreadFinished(null, 0);
			this.dispose();
		}
		else if(source == b_calculate) {            		
			tf_datacount.setBackground(null);
			tf_timecount.setBackground(null);
			tf_timestep.setBackground(null);
			b_calculate.setBackground(null);

    		// TODO add check!
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
				myController.flagcalc = true;
	    		myModel.writetempHeader(myController.calc_datacount,myController.calc_timecount);    		
	    		myModel.AddStep(myController.getVMasspoints());
	    		debugout("actionPerformed - Starting Calculation: datacount="+myController.calc_datacount+", timecount="+myController.calc_timecount+", timestep="+myController.calc_timestep);
	    		//debugout("actionPerformed - Default Calculation : datacount="+CalcCode.DATACOUNTX+", timecount="+CalcCode.TIMECOUNTX+", timestep="+CalcCode.TIMESTEPX);
	    		myController.myCalculation = new CalcCode(myController,myModel,myController.calc_datacount,myController.calc_timecount,myController.calc_timestep);
				myController.myCalculation.start();
				myController.myView.pa_computetab.b_stop.setEnabled(true);
				this.dispose();
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
			String[] stratemp = strtemp.split(" ");
			strtemp = stratemp[0];		
			try {
				factor = 1.0;
				if(stratemp.length > 1) {
					if(stratemp[1].compareTo("µs") == 0)
						factor = 1.0E-6;
					else if(stratemp[1].compareTo("ms") == 0)
						factor = 1.0E-3;
					else if(stratemp[1].compareTo("s") == 0)
						factor = 1.0;
					else if(stratemp[1].compareTo("min") == 0)
						factor = 60.0;
					else if(stratemp[1].compareTo("h") == 0)
						factor = (60.0*60.0);
					else if(stratemp[1].compareTo("d") == 0 || stratemp[1].compareTo("Tage") == 0)
						factor = (24.0*3600.0);
					else if(stratemp[1].compareTo("Wochen") == 0)
						factor = (7.0*24.0*3600.0);
					else if(stratemp[1].compareTo("Monate") == 0)
						factor = (365.25*24.0*3600.0/12.0);
					else if(stratemp[1].compareTo("a") == 0 || stratemp[1].compareTo("Jahre") == 0)
						factor = (365.25*24.0*3600.0);
					else if(stratemp[1].compareTo("Jahrzehnte") == 0)
						factor = (10.0*365.25*24.0*3600.0);
					else if(stratemp[1].compareTo("Jarhunderte") == 0)
						factor = (100.0*365.25*24.0*3600.0);
					else { 
						bacheck[i] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					dtemp = Double.parseDouble(strtemp);
				}
				else if(stratemp.length <= 1) {
					factor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[i] = true;
			}
			if(i > 0) {	//myController.calc_dtimecount
				if(bacheck[0] == false && bacheck[1] == false) {
					if(myController.calc_datacount < dtemp*factor) {
						bacheck[0] = true;
						bacheck[1] = true;
					}
				}
			}
			if(i > 1) {	 //myController.calc_timestep
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

	public String getNiceOutput(double time) {
		
		debugout("getNiceOutput - value="+time);
			
		String unit = "ms";
		double factor = 1.0;
		Locale l = new Locale("en","US");
		Locale.setDefault(l);
		DecimalFormat df = new DecimalFormat("0.#####");
		// TODO check
		
		//if(dCurTime < 1000.0*12.0*4.0*7.0*24.0*60.0*60.0)
		if(time >= 100.0*365.25*24.0*3600.0) {
			unit = "Jahrhunderte";
			factor = 100.0*365.25*24.0*3600.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 10.0*365.25*24.0*3600.0) {
			unit = "Jahrzehnte";
			factor = 10.0*365.25*24.0*3600.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 365.25*24.0*3600.0) {
			unit = "a";
			factor = 365.25*24.0*3600.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 365.25*24.0*3600.0/12.0) {
			unit = "Monate";
			factor = 365.25*24.0*3600.0/12.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 2.0*7.0*24.0*60.0*60.0) {
			unit = "Wochen";
			factor = 7.0*24.0*60.0*60.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 2.0*24.0*60.0*60.0) {
			unit = "d";
			factor = 24.0*60.0*60.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 60.0*60.0) {
			unit = "h";
			factor = 60.0*60.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 60.0) {
			unit = "min";
			factor = 60.0;
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 1.0) {
			factor = 10.0;
			unit = "s";
			return (df.format((time/factor))+" "+unit);
		}
		else if(time >= 1.0E-3) {
			factor = 1.0E-3;
			unit = "ms";
			return (df.format(time/factor)+" "+unit);
		}
		else if(time >= 1.0E-6) {
			factor = 1.0E-6;
			unit = "µs";
			return (df.format(time/factor)+" "+unit);
		}
		else if(time >= 1.0E-9) {
			factor = 1.0E-9;
			unit = "ns";
			return (df.format(time/factor)+" "+unit);
		}
		else if(time >= 1.0E-12) {
			factor = 1.0E-12;
			unit = "ps";
			return (df.format(time/factor)+" "+unit);
		}
		else if(time >= 1.0E-15) {
			factor = 1.0E-15;
			unit = "fs";
			return (df.format(time/factor)+" "+unit);
		}
		
		return "keine Werte eingegeben";
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
