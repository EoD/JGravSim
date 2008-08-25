package gravity_sim;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller implements ActionListener, ChangeListener, MouseMotionListener, MouseListener, MouseWheelListener, ItemListener, WindowListener, KeyListener {

	static boolean MAINDEBUG = true;
	static double ZOOMLEVEL = 10.0;
	static final double VERSION = 1.22;
	// TODO update version
	static final String top = "oben";
	static final String front = "vorne";
	static final String right = "rechts";
	static final String error = "Fehler während dem Einlesen der Datei";
	
	
	View myView = new View("Relativistischer Gravitations Simulator");
	Model myModel = new Model();
	CalcCode myCalculation;
	File fpInputFile = null;
	
	Vector<Masspoint> vmasspoints = new Vector<Masspoint>();
    Step currentstep = new Step(0, 0);
    String strlastfolder = null;
	boolean flagcalc = false;
	boolean flagschwarzschild = false;
	boolean flagedit = false;
	boolean flagctrl = false;
    int id = 0;
    int flagclick = 0;
	
	double calc_timestep;
	double calc_timecount;
	double calc_datacount;
	
	Timer tiPlayer;
	    
	Controller() {	
		/* Register Stuff from Visualisation */
		//Locale l = new Locale("en","US");
		//Locale.setDefault(l);

		myView.addWindowListener(this);
		myView.addKeyListener(this);	
		myView.setFocusable(true);
		
		myView.pa_computetab.cb_Objects.addItemListener(this);
		myView.pa_visualtab.pa_visual_datatab.cb_Objects.addItemListener(this);
		
		myView.pa_visualtab.pa_visual_contrtab.b_loadfile.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_loadlast.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_colorch_grid.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_colorch_speedvec.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_resetoffset.addActionListener(this);		
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_play.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepback.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepforw.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stop.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_start.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_end.addActionListener(this);
		myView.pa_computetab.b_colorch_grid.addActionListener(this);
		myView.pa_computetab.b_colorch_speedvec.addActionListener(this);
		myView.pa_computetab.b_resetoffset.addActionListener(this);		
		myView.pa_computetab.b_Remove.addActionListener(this);
		myView.pa_computetab.b_edit.addActionListener(this);
		myView.pa_computetab.b_reset.addActionListener(this);
		myView.pa_computetab.b_stop.addActionListener(this);
		myView.pa_computetab.b_compute.addActionListener(this);
		myView.pa_computetab.b_savefile.addActionListener(this);
		myView.pa_computetab.b_loadfile.addActionListener(this);

		myView.pa_visualtab.pa_visual_contrtab.sl_playcontr_slider.addChangeListener(this);
		myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.addChangeListener(this);
		myView.pa_visualtab.pa_visual_contrtab.sl_gridoffset.addChangeListener(this);
		myView.pa_computetab.sl_zoomlevel.addChangeListener(this);	
		myView.pa_computetab.sl_gridoffset.addChangeListener(this);	
		myView.pa_computetab.sl_Mass.addChangeListener(this);
		myView.pa_computetab.sl_Speed.addChangeListener(this);
		myView.pa_computetab.sl_Radius.addChangeListener(this);
	
		myView.pa_visualtab.ov_vis_front.addMouseListener(this);
		myView.pa_visualtab.ov_vis_right.addMouseListener(this);
		myView.pa_visualtab.ov_vis_top.addMouseListener(this);
		myView.pa_computetab.ov_front.addMouseListener(this);
		myView.pa_computetab.ov_top.addMouseListener(this);
	
		myView.pa_visualtab.ov_vis_front.addMouseMotionListener(this);
		myView.pa_visualtab.ov_vis_right.addMouseMotionListener(this);
		myView.pa_visualtab.ov_vis_top.addMouseMotionListener(this);
		myView.pa_computetab.ov_front.addMouseMotionListener(this);
		myView.pa_computetab.ov_top.addMouseMotionListener(this);

		myView.pa_visualtab.ov_vis_front.addMouseWheelListener(this);
		myView.pa_visualtab.ov_vis_right.addMouseWheelListener(this);
		myView.pa_visualtab.ov_vis_top.addMouseWheelListener(this);		
		myView.pa_computetab.ov_front.addMouseWheelListener(this);
		myView.pa_computetab.ov_top.addMouseWheelListener(this);
	
		myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.setValue(100);
		myView.pa_visualtab.pa_visual_contrtab.sl_gridoffset.setValue(25);
	
		myView.pa_computetab.sl_zoomlevel.setValue(100);
		myView.pa_computetab.sl_gridoffset.setValue(25);

		myView.pa_computetab.ButtonsStart();
	}
	
	public static void main(String[] args) {
		new Controller();
	}

	public static void debugout(String a) {
		if(MAINDEBUG)
			System.out.println(a);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == tiPlayer) {
			int curFrame = myView.pa_visualtab.getCurFrame();
			if(curFrame+1<=myView.pa_visualtab.getMaxFrame()) {
				myView.pa_visualtab.setCurFrame(curFrame+1);
			}
			else {
				tiPlayer.stop();
				tiPlayer = null;
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_loadfile) {
			JFileChooser fc;
			if(strlastfolder == null) 
				fc = new JFileChooser(getCurrentFolder());
			else
				fc = new JFileChooser(strlastfolder);

			fc.setSelectedFile(new File( Model.Defaultname ));
			int ret = fc.showOpenDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				 fpInputFile = fc.getSelectedFile().getAbsoluteFile();
				 strlastfolder = fpInputFile.getPath();
				 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Lade ...");
				 int errorline = myModel.loadInputFile(fpInputFile);
				 if(errorline != Model.INFILE_NOERROR) {
					 if(errorline == Model.INFILE_EOFSTARTERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarteter Dateianfang", "Aktuelle Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_EOFSTEPERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Schritte\nDie Anzahl der Schritte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_EOFOBJERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Massenpunkte\nAnzahl der Massenpunkte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_FILENOTFOUND) {
						 JOptionPane.showMessageDialog(myView, error+":\nDatei nicht gefunden!", "Datei nicht gefunden", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_READERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nLesefehler!", "Lesefehler beim Einlesen Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else {
						 JOptionPane.showMessageDialog(myView, error+"in Zeile "+errorline, "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 myView.pa_visualtab.setPlayControlsEnabled(false);
					 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Laden fehlgeschlagen");
				 }
				 else if(errorline == Model.INFILE_NOERROR) { /* no error detected ... continue */
					myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
					myView.pa_visualtab.pa_visual_contrtab.setTimeStep(myModel.dtimeCount);
					myView.pa_visualtab.pa_visual_contrtab.setMaxFrame(myModel.dynamicLoader.iLastStep);
					myView.pa_visualtab.setPlayControlsEnabled(true);
					myView.pa_visualtab.setCurFrame(0);
					myView.pa_visualtab.ov_vis_top.alldots = null;
					myView.pa_visualtab.ov_vis_front.alldots = null;
					myView.pa_visualtab.ov_vis_right.alldots = null;
					myView.pa_visualtab.repaintViews();
				 }
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_loadlast) {
			 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Lade ...");
			 fpInputFile = new File( Model.Defaultname ); 
			 int errorline = myModel.loadInputFile(fpInputFile);
			 if(errorline != Model.INFILE_NOERROR) {
				 if(errorline == Model.INFILE_EOFSTARTERROR) {
					 JOptionPane.showMessageDialog(myView, error+":\nUnerwarteter Dateianfang", "Aktuelle Datei", JOptionPane.ERROR_MESSAGE);
				 }
				 else if(errorline == Model.INFILE_EOFSTEPERROR) {
					 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Schritte\nDie Anzahl der Schritte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
				 }
				 else if(errorline == Model.INFILE_EOFOBJERROR) {
					 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Massenpunkte\nAnzahl der Massenpunkte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
				 }
				 else if(errorline == Model.INFILE_FILENOTFOUND) {
					 JOptionPane.showMessageDialog(myView, error+":\nDatei nicht gefunden!", "Datei nicht gefunden", JOptionPane.ERROR_MESSAGE);
				 }
				 else if(errorline == Model.INFILE_READERROR) {
					 JOptionPane.showMessageDialog(myView, error+":\nLesefehler!", "Lesefehler beim Einlesen Datei", JOptionPane.ERROR_MESSAGE);
				 }
				 else {
					 JOptionPane.showMessageDialog(myView, error+"in Zeile "+errorline, "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
				 }
				 myView.pa_visualtab.setPlayControlsEnabled(false);
				 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Laden fehlgeschlagen");
			 }
			 else if(errorline == Model.INFILE_NOERROR) { // no error detected ... continue //
				myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
				myView.pa_visualtab.pa_visual_contrtab.setTimeStep(myModel.dtimeCount);
				myView.pa_visualtab.pa_visual_contrtab.setMaxFrame(myModel.dynamicLoader.iLastStep);
				myView.pa_visualtab.setPlayControlsEnabled(true);
				myView.pa_visualtab.setCurFrame(0);
				myView.pa_visualtab.ov_vis_top.alldots = null;
				myView.pa_visualtab.ov_vis_front.alldots = null;
				myView.pa_visualtab.ov_vis_right.alldots = null;
				myView.pa_visualtab.repaintViews();
			 }
		}		
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_colorch_grid) {
			Color newColor = JColorChooser.showDialog(myView, "Rasterfarbe", myView.pa_visualtab.ov_vis_front.coGridColor);
			if(newColor != null) {
				myView.pa_visualtab.setGridColor(newColor);
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_colorch_speedvec) {
			Color newColor = JColorChooser.showDialog(myView, "Massenpunktfarbe", myView.pa_visualtab.ov_vis_front.coSpeedvecColor);
			if(newColor != null) {
				myView.pa_visualtab.setObjectColor(newColor);
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepback) {
			int curFrame = myView.pa_visualtab.getCurFrame();
			if(curFrame-1>=0) {
				myView.pa_visualtab.setCurFrame(curFrame-1);
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_play) {
			if(tiPlayer!=null) {
				tiPlayer.stop();
				tiPlayer=null;
			}
			int delay = 1000 / Integer.parseInt(myView.pa_visualtab.pa_visual_contrtab.sp_fpschooser.getValue().toString());
			tiPlayer = new Timer(delay,this);
			tiPlayer.start();
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stop) {
			if(tiPlayer!=null) {
				tiPlayer.stop();
				tiPlayer = null;
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_start) {
			myView.pa_visualtab.setCurFrame(0);
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_end) {
			myView.pa_visualtab.setCurFrame(myView.pa_visualtab.getMaxFrame());
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepforw) {
			int curFrame = myView.pa_visualtab.getCurFrame();
			if(curFrame+1<=myView.pa_visualtab.getMaxFrame()) {
				myView.pa_visualtab.setCurFrame(curFrame+1);
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_resetoffset) {
			myView.pa_visualtab.resetOffset();
		}

		else if(source == myView.pa_computetab.b_reset) {
			// TODO check
			Object[] options = { "Alles zurücksetzten", "Berechnung zurücksetzten", "Abbrechen" };
			int a = JOptionPane.showOptionDialog(myView, "Was soll zurückgesetzt werden?","Reset",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			debugout("rest() - Button:"+a);
			if(a == 0) {
				for(int i=vmasspoints.size()-1;i>=0;i--) {
					RemoveMp(vmasspoints.get(i));
				}
				myView.pa_visualtab.displayStep(null);
				myView.pa_visualtab.setPlayControlsEnabled(false);
				myView.pa_visualtab.enableCounter(false);
			}
			else if(a == 1){
				fpInputFile = new File( Model.Defaultname ); 
				//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Lade ...");
				int errorline = myModel.loadDataset(fpInputFile);
				if(errorline != Model.INFILE_NOERROR) {
					if(errorline == Model.INFILE_EOFSTARTERROR) {
						JOptionPane.showMessageDialog(myView, error+":\nUnerwarteter Dateianfang", "Aktuelle Datei", JOptionPane.ERROR_MESSAGE);
					}
					else if(errorline == Model.INFILE_EOFSTEPERROR) {
						JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Schritte\nDie Anzahl der Schritte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					}
					else if(errorline == Model.INFILE_EOFOBJERROR) {
						JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Massenpunkte\nAnzahl der Massenpunkte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					}
					else if(errorline == Model.INFILE_FILENOTFOUND) {
						JOptionPane.showMessageDialog(myView, error+":\nDatei nicht gefunden!", "Datei nicht gefunden", JOptionPane.ERROR_MESSAGE);
					}
					else if(errorline == Model.INFILE_READERROR) {
						JOptionPane.showMessageDialog(myView, error+":\nLesefehler!", "Lesefehler beim Einlesen Datei", JOptionPane.ERROR_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(myView, error+"in Zeile "+errorline, "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(errorline == Model.INFILE_NOERROR) { /* no error detected ... continue */
					//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
					vmasspoints.removeAllElements();
					vmasspoints.addAll(myModel.stDataset);
					calc_datacount = myModel.ddataCount;
					calc_timecount = myModel.dtimeCount;
					calc_timestep = myModel.dtimeCount;
					
					myView.pa_computetab.cb_Objects.removeAllItems();
					for(int i=0;i<vmasspoints.size();i++) {
						myView.pa_computetab.cb_Objects.addItem(vmasspoints.get(i));
					}
					myView.pa_computetab.ButtonsStd();
					id = vmasspoints.get(vmasspoints.size()-1).id;
					
					Masspoint mp = vmasspoints.get(0);
					UpdatePanels(mp, source);
				}
			}	
		}
        else if(source == myView.pa_computetab.b_compute)  {
        	//JOptionPane.showMessageDialog(myView, "Nun laeuft das Programm...","Läuft",-1);
			//disable while program is running
        	myView.pa_computetab.ButtonsAni();
			myView.pa_computetab.b_stop.setEnabled(false);
        	new CalcOptions(this, myModel);
    		myView.pa_computetab.ov_top.removeMouseListener(this);
    		myView.pa_computetab.ov_front.removeMouseListener(this);
    		myView.pa_computetab.ov_top.removeMouseMotionListener(this);
    		myView.pa_computetab.ov_front.removeMouseMotionListener(this);
    		myView.pa_computetab.ov_top.removeMouseWheelListener(this);
    		myView.pa_computetab.ov_front.removeMouseWheelListener(this);
    		// TODO check
    		/*
        	flagcalc = true;
    		myModel.writetempHeader();
    		myModel.AddStep(vmasspoints);
    		//only add data, if it is the first start
    		//if(myModel.GetDataSize() <= 0) {
    		//}
			CalcCode myCalculation = new CalcCode(this,myModel);
			myCalculation.start();*/
        } 
        else if(source == myView.pa_computetab.b_stop) {
        	//Stop is pressed and timer is not running, so calculation has to be stopped
           	flagcalc = false;
        }
        else if(source == myView.pa_computetab.b_savefile) {
			JFileChooser fc;
			
			if(strlastfolder == null) 
				fc = new JFileChooser(getCurrentFolder());
			else {
				fc = new JFileChooser(strlastfolder);
				debugout("Loading last file - "+strlastfolder);
			}
			myView.pa_computetab.ButtonsDeactive(false);
			
			fc.setSelectedFile(new File(String.valueOf(System.currentTimeMillis())+".wpt"));
			int ret = fc.showSaveDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				 fpInputFile = fc.getSelectedFile().getAbsoluteFile();
				 
	  /*      	String filename = myView.pa_computetab.tf_filename.getText();
	        	if(filename.isEmpty() || filename == null) {
	        		filename = String.valueOf(System.currentTimeMillis());
	        	}
	        	filename += ".wpt";
	   */     	myModel.saveOutputfile(fpInputFile);
	        	JOptionPane.showMessageDialog(myView, "Die Berechnung wurde in der Datei \""+fpInputFile.getName()+"\"gespeicert","Gespeichert",JOptionPane.INFORMATION_MESSAGE);			
			}
			myView.pa_computetab.ButtonsDeactive(true);
        }
        else if(source == myView.pa_computetab.b_loadfile) {
			JFileChooser fc;
			if(strlastfolder == null) 
				fc = new JFileChooser(getCurrentFolder());
			else
				fc = new JFileChooser(strlastfolder);
			
			int ret = fc.showOpenDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				fpInputFile = fc.getSelectedFile().getAbsoluteFile();
				//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Lade ...");
				int errorline = myModel.loadDataset(fpInputFile);
				 if(errorline != Model.INFILE_NOERROR) {
					 if(errorline == Model.INFILE_EOFSTARTERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarteter Dateianfang", "Aktuelle Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_EOFSTEPERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Schritte\nDie Anzahl der Schritte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_EOFOBJERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nUnerwarter Fehler beim Einlesen der Massenpunkte\nAnzahl der Massenpunkte scheint fehlerhaft", "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_FILENOTFOUND) {
						 JOptionPane.showMessageDialog(myView, error+":\nDatei nicht gefunden!", "Datei nicht gefunden", JOptionPane.ERROR_MESSAGE);
					 }
					 else if(errorline == Model.INFILE_READERROR) {
						 JOptionPane.showMessageDialog(myView, error+":\nLesefehler!", "Lesefehler beim Einlesen Datei", JOptionPane.ERROR_MESSAGE);
					 }
					 else {
						 JOptionPane.showMessageDialog(myView, error+"in Zeile "+errorline, "Fehlerhafte Datei", JOptionPane.ERROR_MESSAGE);
					 }
					myView.pa_computetab.ButtonsStd();
					//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText("Laden fehlgeschlagen");
				}
				else if(errorline == Model.INFILE_NOERROR) { /* no error detected ... continue */
					//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
					vmasspoints.removeAllElements();
					vmasspoints.addAll(myModel.stDataset);
					calc_datacount = myModel.ddataCount;
					calc_timecount = myModel.dtimeCount;
					calc_timestep = myModel.dtimeCount;
					
					myView.pa_computetab.cb_Objects.removeAllItems();
					for(int i=0;i<vmasspoints.size();i++) {
						myView.pa_computetab.cb_Objects.addItem(vmasspoints.get(i));
					}
					myView.pa_computetab.ButtonsStd();
					id = vmasspoints.get(vmasspoints.size()-1).id;
					
					Masspoint mp = vmasspoints.get(0);
					UpdatePanels(mp, source);
				}
			}
        }
        else if(source == myView.pa_computetab.b_Remove)  {
        	int a = JOptionPane.showConfirmDialog(myView, "Wollen sie den Massenpunkt "+GetSelectedMasspoint().id+"entfernen?","Löschen",JOptionPane.YES_NO_OPTION);
        	if(a == 0) {
	        	Masspoint mp = GetSelectedMasspoint();
	        	RemoveMp(mp);
        	}
        } 
        else if(source == myView.pa_computetab.b_edit)  {
        	if(flagedit == false) {
        		myView.pa_computetab.b_edit.setText("Werte speichern");
        		myView.pa_computetab.b_edit.setBackground(Color.GREEN);
        		myView.pa_computetab.ButtonsEdit();
        		myView.pa_computetab.ov_top.removeMouseListener(this);
        		myView.pa_computetab.ov_top.removeMouseMotionListener(this);
        		myView.pa_computetab.ov_front.removeMouseListener(this);
        		myView.pa_computetab.ov_front.removeMouseMotionListener(this);
        		myView.pa_computetab.ov_top.removeMouseWheelListener(this);
        		myView.pa_computetab.ov_front.removeMouseWheelListener(this);
        		flagedit = true;
        	}
        	else if(flagedit == true) {
        		myView.pa_computetab.TFColorStd();
        		boolean flagcheck = true;
        		boolean[] bacheck = CheckInput();
        		for(int i=0;i<bacheck.length;i++) {
        			if(bacheck[i]) {
                		myView.pa_computetab.b_edit.setBackground(Color.RED);
                		switch(i) {
                		case 0: myView.pa_computetab.tf_Mass.setBackground(Color.RED); break;
                		case 1: myView.pa_computetab.tf_Radius.setBackground(Color.RED); break;
                		case 2: myView.pa_computetab.tf_Speedx.setBackground(Color.RED); break;
                		case 3: myView.pa_computetab.tf_Speedy.setBackground(Color.RED); break;
                		case 4: myView.pa_computetab.tf_Speedz.setBackground(Color.RED); break;
                		case 5: myView.pa_computetab.tf_Coordx.setBackground(Color.RED); break;
                		case 6: myView.pa_computetab.tf_Coordy.setBackground(Color.RED); break;
                		case 7: myView.pa_computetab.tf_Coordz.setBackground(Color.RED); break;
                		}
                		flagcheck = false;
        			}
        		}
        		if(flagcheck) {
	        		flagedit = false;
	        		myView.pa_computetab.b_edit.setBackground(null);
	        		myView.pa_computetab.b_edit.setText("Werte bearbeiten");
	        		myView.pa_computetab.ButtonsStd();
	        		myView.pa_computetab.ov_top.addMouseListener(this);
	        		myView.pa_computetab.ov_top.addMouseMotionListener(this);
	        		myView.pa_computetab.ov_front.addMouseListener(this);
	        		myView.pa_computetab.ov_front.addMouseMotionListener(this);
	        		myView.pa_computetab.ov_top.addMouseWheelListener(this);
	        		myView.pa_computetab.ov_front.addMouseWheelListener(this);
	        		UpdatePanels(GetSelectedMasspoint(), source);
        		}
        		else
                	JOptionPane.showMessageDialog(myView, "Einige der Eingaben waren fehlerhaft.\nDie fehlerhaften Eingaben wurde rot markiert.","Eingabefehler",JOptionPane.ERROR_MESSAGE);
        	}
        }	
		else if(source == myView.pa_computetab.b_colorch_grid) {
			Color newColor = JColorChooser.showDialog(myView, "Rasterfarbe", myView.pa_computetab.ov_front.coGridColor);
			if(newColor != null) {
				myView.pa_computetab.setGridColor(newColor);
			}
		}
		else if(source == myView.pa_computetab.b_colorch_speedvec) {
			Color newColor = JColorChooser.showDialog(myView, "Massenpunktfarbe", myView.pa_computetab.ov_front.coSpeedvecColor);
			if(newColor != null) {
				myView.pa_computetab.setObjectColor(newColor);
			}
		}     
		else if(source == myView.pa_computetab.b_resetoffset) {
			myView.pa_computetab.resetOffset();
		}    
    }

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		
		if(source == myView.pa_visualtab.pa_visual_contrtab.sl_playcontr_slider) {
			myView.pa_visualtab.setCurFrame(source.getValue());
			myView.pa_visualtab.displayStep(myModel.dynamicLoader.getStep(source.getValue()));
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel) {
			double zoomLevel = source.getValue()/ZOOMLEVEL;
			myView.pa_visualtab.setZoom(zoomLevel);
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.sl_gridoffset) {
			myView.pa_visualtab.setGridOffset(source.getValue());
		}
		else if(source == myView.pa_computetab.sl_zoomlevel) {
			double zoomLevel = source.getValue()/ZOOMLEVEL;
			myView.pa_computetab.setZoom(zoomLevel);
			myView.pa_computetab.repaintViews();
		}
		else if(source == myView.pa_computetab.sl_gridoffset) {
			myView.pa_computetab.setGridOffset(source.getValue());
		}
		
		else if(source == myView.pa_computetab.sl_Mass) {
			double dmass = myView.pa_computetab.sl_Mass.getValue();
			dmass = Math.pow(dmass,CalcCode.SMASSCONST);
			GetSelectedMasspoint().setMass(dmass+Double.MIN_VALUE);
			//debugout("sMass - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dmass);
			UpdatePanels(GetSelectedMasspoint(),source);
		}
		else if(source == myView.pa_computetab.sl_Speed) {
			double dspeed = myView.pa_computetab.sl_Speed.getValue();
			Masspoint mp = GetSelectedMasspoint();
			if(dspeed > 0 && mp.mdvzerospeed.abs() == 0) {
				mp.setSpeed(new MDVector(1.0,1.0,1.0));
			}

			//Maximum v = 299792457.99999994 m/s
			dspeed = Math.pow(dspeed/100.0,CalcCode.SSPEEDCONST)*(CalcCode.LIGHTSPEED-Math.pow(10.0, -7.5));
			mp.setAbsSpeed(dspeed);
			UpdatePanels(GetSelectedMasspoint(),source);	
			//debugout("sSpeed - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dspeed);
		}			
		else if(source == myView.pa_computetab.sl_Radius) {
			double dradius = myView.pa_computetab.sl_Radius.getValue();
			dradius = Math.pow(dradius,CalcCode.SRADIUSCONST);
			GetSelectedMasspoint().setRadius(dradius+Double.MIN_VALUE*CalcCode.RACCURACY);
			//debugout("sRadius - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dradius);
			UpdatePanels(GetSelectedMasspoint(),source);
		}
	}

	public void mouseDragged(MouseEvent e) {
		//System.out.println("Position: "+e.getPoint());
		ObjectView source = (ObjectView) e.getSource();
		
		if(source == myView.pa_visualtab.ov_vis_top) {
			myView.pa_visualtab.changeOffsetX(e.getX()-source.iLastMouseX);
			myView.pa_visualtab.changeOffsetY(e.getY()-source.iLastMouseY);
			source.iLastMouseX = e.getX();
			source.iLastMouseY = e.getY();
		}
		else if(source == myView.pa_visualtab.ov_vis_front) {
			myView.pa_visualtab.changeOffsetX(e.getX()-source.iLastMouseX);
			myView.pa_visualtab.changeOffsetZ(e.getY()-source.iLastMouseY);
			source.iLastMouseX = e.getX();
			source.iLastMouseY = e.getY();
		}
		else if(source == myView.pa_visualtab.ov_vis_right) {
			myView.pa_visualtab.changeOffsetY(e.getX()-source.iLastMouseX);
			myView.pa_visualtab.changeOffsetZ(e.getY()-source.iLastMouseY);
			source.iLastMouseX = e.getX();
			source.iLastMouseY = e.getY();
		}
		// TODO check compute tab dragging
		else if(source == myView.pa_computetab.ov_top) {
			if(flagclick < 0 && flagctrl == false) {
				Masspoint mphit = GetSelectedMasspoint();
				mphit.setCoordx(pxtomm(e.getX(), 'x'));
				mphit.setCoordy(pxtomm(e.getY(), 'y'));
				UpdatePanels(GetSelectedMasspoint(),source);
			}
			else if(flagctrl == true) {
				myView.pa_computetab.changeOffsetX(e.getX()-source.iLastMouseX);
				myView.pa_computetab.changeOffsetY(e.getY()-source.iLastMouseY);
				source.iLastMouseX = e.getX();
				source.iLastMouseY = e.getY();
			}
		}
		else if(source == myView.pa_computetab.ov_front) {
			if(flagclick < 0 && flagctrl == false) {
				Masspoint mphit = GetSelectedMasspoint();
				mphit.setCoordx(pxtomm(e.getX(), 'x'));
				mphit.setCoordz(pxtomm(e.getY(), 'z'));
				UpdatePanels(GetSelectedMasspoint(),source);
			}
			else if(flagctrl == true) {
				myView.pa_computetab.changeOffsetX(e.getX()-source.iLastMouseX);
				myView.pa_computetab.changeOffsetZ(e.getY()-source.iLastMouseY);
				source.iLastMouseX = e.getX();
				source.iLastMouseY = e.getY();
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		// TODO remove for final
/*		ObjectView source = (ObjectView) e.getSource();
		
		if(source == myView.pa_computetab.ov_top) {
			if(flagclick > 0) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), top);
				
				if(mphit == null) {
					addMasspoint(e.getX(),e.getY(), 0);
					UpdatePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
		}
		else if(source == myView.pa_computetab.ov_front) {
			if(flagclick > 0) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), front);
				
				if(mphit == null) {
					addMasspoint(e.getX(), 0, e.getY());
					UpdatePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
		}
		*/
	}

	public void mouseEntered(MouseEvent e) {
		//Object source = e.getSource();
		//debugout("mouseEntered - source="+source.toString());
		//just used by the ObjectViews
		//if(source == myView.pa_computetab.ov_top ||
		//	source == myView.pa_computetab.ov_front) {
			myView.requestFocusInWindow();
		//	debugout("mouseEntered - FocusInWindow requested");
		//}
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent e) {
		//System.out.println("Dragging startet at: "+arg0.getPoint());
		ObjectView source = (ObjectView) e.getSource();
		
		source.iLastMouseX = e.getX();
		source.iLastMouseY = e.getY();
		

		if(source == myView.pa_computetab.ov_top && flagctrl == false) {				
			Masspoint mphit = null;
			mphit = checkforHit(e.getX(), e.getY(), top);		
			if(mphit == null) {
				flagclick = 1;
			}
			else {
				myView.pa_computetab.cb_Objects.setSelectedItem(mphit);
				myView.pa_computetab.repaintViews();
				flagclick = -1;
			}
		}
		else if(source == myView.pa_computetab.ov_front && flagctrl == false) {				
			Masspoint mphit = null;
			mphit = checkforHit(e.getX(), e.getY(), front);	
			if(mphit == null) {
				flagclick = 1;
			}
			else {
				myView.pa_computetab.cb_Objects.setSelectedItem(mphit);
				myView.pa_computetab.repaintViews();
				flagclick = -1;
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		//System.out.println("Dragging stopped at: "+arg0.getPoint());
		ObjectView source = (ObjectView) e.getSource();

		if(source == myView.pa_computetab.ov_top && flagctrl == false) {
			if(flagclick >= 1) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), top);
				
				if(mphit == null) {
					addMasspoint(e.getX(),e.getY(), 0);
					UpdatePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
		}
		else if(source == myView.pa_computetab.ov_front && flagctrl == false) {
			if(flagclick >= 1) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), front);
				
				if(mphit == null) {
					addMasspoint(e.getX(), 0, e.getY());
					UpdatePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		ObjectView source = (ObjectView) e.getSource();
		if(source == myView.pa_visualtab.ov_vis_front || 
				source == myView.pa_visualtab.ov_vis_right || 
				source == myView.pa_visualtab.ov_vis_top) {
			double curZoom = myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.getValue();
			myView.pa_visualtab.setZoom((curZoom+ e.getWheelRotation())/ZOOMLEVEL);
			myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.setValue((int)curZoom + e.getWheelRotation());
		}		
		else if(source == myView.pa_computetab.ov_front || 
				source == myView.pa_computetab.ov_top) {
			double curZoom = myView.pa_computetab.sl_zoomlevel.getValue();
			myView.pa_computetab.setZoom((curZoom+e.getWheelRotation())/ZOOMLEVEL);
			myView.pa_computetab.sl_zoomlevel.setValue((int)curZoom + e.getWheelRotation());
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		//Object-ID changed. Update all output data
		if(source == myView.pa_computetab.cb_Objects && myView.pa_computetab.cb_Objects.getItemCount() > 0) {
	        Masspoint mp = GetSelectedMasspoint();
	        if(mp != null) {
	           	UpdatePanels(mp,source);
	           	debugout("Object "+(int)mp.id+" selected");
			}
		}		
		else if(source == myView.pa_visualtab.pa_visual_datatab.cb_Objects && myView.pa_visualtab.pa_visual_datatab.cb_Objects.getItemCount() > 0) {
	        Masspoint_Sim mp = (Masspoint_Sim)myView.pa_visualtab.pa_visual_datatab.cb_Objects.getSelectedItem();
	        if(mp != null) {
	        	myView.pa_visualtab.pa_visual_datatab.UpdatePanels(mp);
	           	debugout("Object "+(int)mp.getID()+" selected");
			}
		}			
	}
	
	public void keyPressed(KeyEvent e) {
		int source = e.getKeyCode();
		//debugout("keyPressed - key:"+KeyEvent.getKeyText(source));
		if(source == KeyEvent.VK_CONTROL && flagctrl == false) {
			debugout("keyPressed - key:"+KeyEvent.getKeyText(source));
			flagctrl = true;
		}
		else if(source == KeyEvent.VK_F5 
				|| source == KeyEvent.VK_F6
				|| source == KeyEvent.VK_F7
				|| source == KeyEvent.VK_F8
				|| source == KeyEvent.VK_F9) {
			int data;
			if(source == KeyEvent.VK_F5)
				data = 10000;
			else if(source == KeyEvent.VK_F6)
				data = 1000;
			else if(source == KeyEvent.VK_F7)
				data = 100;
			else if(source == KeyEvent.VK_F8)
				data = 10;
			else //if(source == KeyEvent.VK_F9)
				data = 1;
			myView.pa_visualtab.ov_vis_top.alldots = new Step[myView.pa_visualtab.getMaxFrame()/data];
			myView.pa_visualtab.ov_vis_front.alldots = new Step[myView.pa_visualtab.getMaxFrame()/data];
			myView.pa_visualtab.ov_vis_right.alldots = new Step[myView.pa_visualtab.getMaxFrame()/data];
			for(int k=0;k<myView.pa_visualtab.getMaxFrame()/data;k++) {
				myView.pa_visualtab.ov_vis_top.alldots[k] = myModel.dynamicLoader.getStep(k*data);
				myView.pa_visualtab.ov_vis_front.alldots[k] = myModel.dynamicLoader.getStep(k*data);
				myView.pa_visualtab.ov_vis_right.alldots[k] = myModel.dynamicLoader.getStep(k*data);
			}
			myView.pa_visualtab.repaintViews();
		}
		else if(source == KeyEvent.VK_F10) {
			myView.pa_visualtab.ov_vis_top.alldots = null;
			myView.pa_visualtab.ov_vis_front.alldots = null;
			myView.pa_visualtab.ov_vis_right.alldots = null;
			myView.pa_visualtab.repaintViews();
		}
	}

	public void keyReleased(KeyEvent e) {
		int source = e.getKeyCode();
		//debugout("keyReleased - key:"+KeyEvent.getKeyText(source));
		if(source == KeyEvent.VK_CONTROL && flagctrl == true) {
			debugout("keyReleased - key:"+KeyEvent.getKeyText(source));
			flagctrl = false;
		}
	}

	public void keyTyped(KeyEvent e) {
		int source = e.getKeyCode();
		debugout("keyTyped - key:"+KeyEvent.getKeyText(source));
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}
	
	//delete temp file when window is closed
	public void windowClosing(WindowEvent e) {
		Object source= e.getSource();
		if(source == myView) {
			debugout("Controller() - Closing...");
			if(!MAINDEBUG)
				myModel.deleteFile(Model.Defaultname);		
		}
	}
	
	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
	
	public Masspoint GetSelectedMasspoint() {
		//check if there are still items in the list(itemcount>0), and if there are some masspoints (id>0)
		if(myView.pa_computetab.cb_Objects.getItemCount() > 0 && vmasspoints.size() > 0) {
			return (Masspoint)myView.pa_computetab.cb_Objects.getSelectedObjects()[0];
		}
		else
			return null;
	}
	
	public void UpdatePanels(Masspoint mp, Object object) {
		
		if(mp == null) {
			debugout("UpdatePanels() - Sorry");
			return;
		}
		if(flagedit == true) {
			UpdateEdit(mp, object);
			return;
		}

//		if(myView.pa_computetab.isEnabled() == false) 
//			myView.pa_computetab.setEnabled(true);

		//debugout("UpdatePanels() - Object:"+String.valueOf(object));
		//debugout("speed,mass: "+mp.getSpeed()+","+mp.getAbsMass());
		MDVector mvspeed = mp.getMDVSpeed();
		MLVector mvcoord = mp.getCoordMLV();
		//myView.pa_computetab.la_headline.setText(TabCompute.sheadline+" "+ mp.id);
		
		myView.pa_computetab.sl_zoomlevel.removeChangeListener(this);		
		myView.pa_computetab.sl_Mass.removeChangeListener(this);
		myView.pa_computetab.sl_Speed.removeChangeListener(this);
		myView.pa_computetab.sl_Radius.removeChangeListener(this);
		
		if(object != myView.pa_computetab.tf_Coordx) 
			myView.pa_computetab.tf_Coordx.setText(String.valueOf(mvcoord.x1)+" cm");
		if(object != myView.pa_computetab.tf_Coordy) 
			myView.pa_computetab.tf_Coordy.setText(String.valueOf(mvcoord.x2)+" cm");
		if(object != myView.pa_computetab.tf_Coordz) 
			myView.pa_computetab.tf_Coordz.setText(String.valueOf(mvcoord.x3)+" cm");
		if(object != myView.pa_computetab.tf_Speedx) 
			myView.pa_computetab.tf_Speedx.setText(String.valueOf(mvspeed.x1)+" m/s");
		if(object != myView.pa_computetab.tf_Speedy) 
			myView.pa_computetab.tf_Speedy.setText(String.valueOf(mvspeed.x2)+" m/s");
		if(object != myView.pa_computetab.tf_Speedz) 
			myView.pa_computetab.tf_Speedz.setText(String.valueOf(mvspeed.x3)+" m/s");
		if(object != myView.pa_computetab.tf_Mass) 
			myView.pa_computetab.tf_Mass.setText(String.valueOf(mp.getAbsMass())+" kg");
		if(object != myView.pa_computetab.tf_Radius) 
			myView.pa_computetab.tf_Radius.setText(String.valueOf(mp.getRadius()/CalcCode.RACCURACY)+" km");
		if(object != myView.pa_computetab.sl_Speed) 
			myView.pa_computetab.sl_Speed.setValue((int)(100*Math.pow(mp.getSpeed()/CalcCode.LIGHTSPEED, 1.0/CalcCode.SSPEEDCONST)));
		if(object != myView.pa_computetab.sl_Mass) 
			myView.pa_computetab.sl_Mass.setValue((int)(Math.pow(mp.getAbsMass(),1.0/CalcCode.SMASSCONST)));
		if(object != myView.pa_computetab.sl_Radius)
			myView.pa_computetab.sl_Radius.setValue((int)(Math.pow(mp.getRadius(),1.0/CalcCode.SRADIUSCONST)));

		myView.pa_computetab.sl_zoomlevel.addChangeListener(this);		
		myView.pa_computetab.sl_Mass.addChangeListener(this);
		myView.pa_computetab.sl_Speed.addChangeListener(this);
		myView.pa_computetab.sl_Radius.addChangeListener(this);
		
		if(mp.getRadius() < mp.getSchwarzschildRadius()) 
			myView.pa_computetab.la_Blackhole.setVisible(true);
		else if(myView.pa_computetab.la_Blackhole.isVisible()) 
			myView.pa_computetab.la_Blackhole.setVisible(false);
		
		
		if(vmasspoints.size() > 0) {		
			//debugout("UpdatePanels() - Collecting steps");
			//vmasspoints ist der (Start)Vector mit allen Massenpunkte
			Vector<Masspoint_Sim> vmpsim = new Vector<Masspoint_Sim>();
			for(int i=0;i<vmasspoints.size();i++) {
				Masspoint mptemp = (Masspoint)vmasspoints.get(i);
				vmpsim.add(mptemp.toMasspoint_Sim());
			}	
			Masspoint_Sim [] masspoints = new Masspoint_Sim [1];
			masspoints = vmpsim.toArray(masspoints);
			currentstep = new Step(0, 0);
			currentstep.setMasspoint_Sim(masspoints);
		}
		else
			currentstep = null;
		
		myView.pa_computetab.displayStep(currentstep);
	}
	
	public void UpdateEdit(Masspoint mp, Object object) {
		MDVector mvspeed = mp.getMDVSpeed();
		
		if(object == myView.pa_computetab.sl_Speed) { 
			//myView.pa_computetab.sl_Speed.setValue((int)(100*Math.pow(mp.getSpeed()/CalcCode.LIGHTSPEED, 1.0/CalcCode.SSPEEDCONST)));
			myView.pa_computetab.tf_Speedz.setText(String.valueOf(mvspeed.x3)+" m/s");
			myView.pa_computetab.tf_Speedy.setText(String.valueOf(mvspeed.x2)+" m/s");
			myView.pa_computetab.tf_Speedx.setText(String.valueOf(mvspeed.x1)+" m/s");
		}
		if(object == myView.pa_computetab.sl_Mass) {
			//myView.pa_computetab.sl_Mass.setValue((int)(Math.pow(mp.getAbsMass(),1.0/CalcCode.SMASSCONST)));
			myView.pa_computetab.tf_Mass.setText(String.valueOf(mp.getAbsMass())+" kg");
		}
		if(object == myView.pa_computetab.sl_Radius) {
			myView.pa_computetab.tf_Radius.setText(String.valueOf(mp.getRadius()/CalcCode.RACCURACY)+" km");		
		}			
		if(mp.getRadius() < mp.getSchwarzschildRadius()) 
			myView.pa_computetab.la_Blackhole.setVisible(true);
		else if(myView.pa_computetab.la_Blackhole.isVisible()) 
			myView.pa_computetab.la_Blackhole.setVisible(false);
		
		if(vmasspoints.size() > 0) {		
			//debugout("UpdatePanels() - Collecting steps");
			//vmasspoints ist der (Start)Vector mit allen Massenpunkte
			Vector<Masspoint_Sim> vmpsim = new Vector<Masspoint_Sim>();
			for(int i=0;i<vmasspoints.size();i++) {
				Masspoint mptemp = (Masspoint)vmasspoints.get(i);
				vmpsim.add(mptemp.toMasspoint_Sim());
			}	
			Masspoint_Sim [] masspoints = new Masspoint_Sim [1];
			masspoints = vmpsim.toArray(masspoints);
			currentstep = new Step(0, 0);
			currentstep.setMasspoint_Sim(masspoints);
		}
		else
			currentstep = null;
		
		myView.pa_computetab.displayStep(currentstep);
	}

	public Vector<Masspoint> getVMasspoints() {
		return vmasspoints;
	}
	
	public void ThreadFinished(Vector<Masspoint> vmps, int error) {
    	//debugout("Animation starting");
		if(vmps != null) {
			String finish = "";
			if(error == CalcCode.UNKNOWN)
				finish += "\nDie Berechnung wurde aufgrund eines internen Fehlers abgebrochen.";
			else if(error == CalcCode.NOERROR) {
				//finish += "\nKein Fehler.";*/
				debugout("ThreadFinished() - finished without error");
			}
			else if(error == CalcCode.DOUBLELIMIT)
				finish += "\n" +
						"Der Computer schaffte es nicht die Berechnung genau genug \n"+
						"zu machen, um unterhalb der Lichtgeschwindigkeit zu bleiben";
			else if(error == CalcCode.LONGLIMIT)
				finish += "\nEin Massenpunkt erreichte den Rand des Koordinatensystems.";
			else if(error == CalcCode.LIGHTSPEEDERROR ) 
				finish += "\nEin Massenpunkt wurde schneller als das Licht.";
	
			if(flagschwarzschild == true) {
				finish += "\nZwei Objekte kamen sich näher als deren doppelter " +
							"Schwarzschild-Radius.\n"+
							"Die Berechnung wurde ungenau!";
			}
			
			if(flagcalc == false) {
				finish = "Die Berechnung wurde abgebrochen." + finish;
				JOptionPane.showMessageDialog(myView,finish,"Abgebrochen",JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				finish = "Die Berechnung wurde erfolgreich beendet." + finish;
				JOptionPane.showMessageDialog(myView,finish,"Erfolgreich",JOptionPane.INFORMATION_MESSAGE);
			}
		
			//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
			vmasspoints.removeAllElements();
			vmasspoints.addAll(vmps);
			myView.pa_computetab.cb_Objects.removeAllItems();
			for(int i=0;i<vmasspoints.size();i++) {
				myView.pa_computetab.cb_Objects.addItem(vmasspoints.get(i));
			}
			myView.pa_computetab.ButtonsStd();
			id = vmasspoints.get(vmasspoints.size()-1).id;
			Masspoint mp = vmasspoints.get(vmasspoints.size()-1);
			myView.pa_computetab.cb_Objects.setSelectedItem(mp);
			UpdatePanels(mp, null);
		}
		
    	myView.pa_computetab.ButtonsStd();
		myView.pa_computetab.ov_front.addMouseListener(this);
		myView.pa_computetab.ov_top.addMouseListener(this);
		myView.pa_computetab.ov_front.addMouseMotionListener(this);
		myView.pa_computetab.ov_top.addMouseMotionListener(this);
		myView.pa_computetab.ov_front.addMouseWheelListener(this);
		myView.pa_computetab.ov_top.addMouseWheelListener(this);
	}
	
	public void RemoveMp(Masspoint mp) {
		//check if all objects are going to be removed
    	if(myView.pa_computetab.cb_Objects.getItemCount()<=1) {
    		myView.pa_computetab.cb_Objects.addItem("Objektliste");
    		myView.pa_computetab.ButtonsStart();
    		id = 0;
    	}

    	mp.remove();
    	vmasspoints.remove(mp);
    	myView.pa_computetab.cb_Objects.removeItem(mp);
    	myView.pa_computetab.cb_Objects.repaint();
    	
    	if(vmasspoints.size() > 0) {
    		id = vmasspoints.get(vmasspoints.size()-1).id;
			//vmasspoints ist der (Start)Vector mit allen Massenpunkte
			Vector<Masspoint_Sim> vmpsim = new Vector<Masspoint_Sim>();
			for(int i=0;i<vmasspoints.size();i++) {
				Masspoint mptemp = (Masspoint)vmasspoints.get(i);
				vmpsim.add(mptemp.toMasspoint_Sim());
			}		
			Masspoint_Sim [] masspoints = new Masspoint_Sim [1];
			masspoints = vmpsim.toArray(masspoints);
			currentstep = new Step(0, 0);
			currentstep.setMasspoint_Sim(masspoints);
    	}
    	else
    		currentstep = null;
		
		myView.pa_computetab.displayStep(currentstep);
	}
	
	public void addMasspoint(int x, int y, int z) {
		debugout("Point added at Position ("+x+"/"+y+"/"+z+")");
		if(id == 0) {
			myView.pa_computetab.cb_Objects.removeAllItems();
			myView.pa_computetab.ButtonsStd();
		}
		id++;
		//debugout("Controller() - Adding data for object "+id);
/*		ObjectView ov_front = myView.pa_computetab.ov_front;
		ObjectView ov_top = myView.pa_computetab.ov_top;
		double centerX = ov_front.getWidth()/2;
		double centerY = ov_front.getHeight()/2;
		double centerZ = ov_top.getHeight()/2;*/

		//*** double dradius = Masspoint.DFTRADIUS;
		// TODO dradius wird nicht verwendet!
		long lx = pxtomm(x, 'x');
		long ly = pxtomm(y, 'y');
		long lz = pxtomm(z, 'z');

		Masspoint mp = new Masspoint(id,lx,ly,lz);
		debugout("addmasspoint() - x1="+lx+", x2="+ly+", x3="+lz);	
		
		//(default)//Masspoint mp = new Masspoint(id, x, y, z);
		vmasspoints.add(mp);
		myView.pa_computetab.cb_Objects.addItem(mp);
		myView.pa_computetab.cb_Objects.setSelectedItem(mp);
		myView.pa_computetab.repaintViews();
	}
	
	public Masspoint checkforHit(int a, int b, String axe) {
		int x,y,z;
		
		if(axe == top) 
		{	x=a; y=b; z=0; }
		else if(axe == front)
		{	x=a; y=0; z=b; }
		else /*if(axe == side)*/
		{	x=0; y=a; z=b; }
		
		long lx = pxtomm(x, 'x');
		long ly = pxtomm(y, 'y');
		long lz = pxtomm(z, 'z');
		
		for(int i=0;i<vmasspoints.size();i++) {
			Masspoint mptemp = (Masspoint)vmasspoints.get(i);
			MLVector mlvcoord = mptemp.getCoordMLV();
			long relradius = Math.round(mptemp.getRadius()*CalcCode.LACCURACY);
			
			if(mptemp.getRadius() < mptemp.getSchwarzschildRadius())
				relradius = Math.round(mptemp.getSchwarzschildRadius()*CalcCode.LACCURACY);
						
			if(axe == top) {
				//debugout("CheckForHit() - Checking for hit at x="+lx+"; y="+ly+"; radius="+relradius);
				debugout("CheckForHit() - Checking for object "+mptemp.id+" at "+lx+"<"+(mlvcoord.x1+relradius)+"; "+lx+">"+(mlvcoord.x1-relradius));
				if(lx < (mlvcoord.x1+relradius) && lx > (mlvcoord.x1-relradius)) {
					debugout("CheckForHit() - Checking for object at "+ly+"<"+(mlvcoord.x2+relradius)+"; "+ly+">"+(mlvcoord.x2-relradius));
					if(ly < (mlvcoord.x2+relradius) && ly > (mlvcoord.x2-relradius)) {
						return mptemp;
					}
				}
			}
			else if(axe == front) {
				debugout("CheckForHit() - Checking for hit at x="+lx+"; z="+lz);
				if(lx < (mlvcoord.x1+relradius) && lx > (mlvcoord.x1-relradius)) {
					if(lz < (mlvcoord.x3+relradius) && lz > (mlvcoord.x3-relradius)) {
						return mptemp;
					}
				}
			}
			else /*if(axe == right)*/ {
				debugout("CheckForHit() - Checking for hit at y="+ly+"; z="+lz);
				if(ly < (mlvcoord.x2+relradius) && ly > (mlvcoord.x2-relradius)) {
					if(lz < (mlvcoord.x3+relradius) && lz > (mlvcoord.x3-relradius)) {
						return mptemp;
					}
				}
			}
		}
		return null;
	}
	
	public long pxtomm(int a, char axe) {
		if(a == 0) {
			debugout("pxtomm() - a = 0");
			return 0;
		}
		
		ObjectView ov_front = myView.pa_computetab.ov_front;
		ObjectView ov_top = myView.pa_computetab.ov_top;
		double centerX = ov_front.getWidth()/2;
		double centerY = ov_front.getHeight()/2;
		double centerZ = ov_top.getHeight()/2;
		
		
		switch(axe) {
			case 'x': 
				debugout("pxtomm() - dCoordOffsetX="+ov_front.dCoordOffsetX);
				double dx = (a-centerX);
				dx /= ov_front.iGridOffset;
				dx *= Math.pow(10, ov_front.iZoomLevel);
				dx -= ov_front.dCoordOffsetX;
				return Math.round(CalcCode.LACCURACY*dx);
			case 'y': 
				debugout("pxtomm() - dCoordOffsetY="+ov_front.dCoordOffsetY);
				double dy = (a-centerY);
				dy /= ov_front.iGridOffset;
				dy *= Math.pow(10, ov_front.iZoomLevel);
				dy += ov_front.dCoordOffsetY;
				return -Math.round(CalcCode.LACCURACY*dy);
			case 'z': 
				debugout("pxtomm() - dCoordOffsetZ="+ov_front.dCoordOffsetZ);
				double dz = (a-centerZ);
				dz /= ov_front.iGridOffset;
				dz *= Math.pow(10, ov_front.iZoomLevel);
				dz += ov_front.dCoordOffsetZ;
				return -Math.round(CalcCode.LACCURACY*dz); 
			default: 
				debugout("pxtomm() - ERROR"); 
				return 0;
		}
	}
	
	public boolean[] CheckInput() {
		Masspoint mp = GetSelectedMasspoint();
		String strtemp;
		String[] stratemp;
		double dtemp = 0.0;
		long ltemp = 0;
		double factor = 1.0;
		boolean[] bacheck = new boolean[8];
		
		if(mp == null) {
			debugout("CheckInput() - Error");
			return null;
		}
		// TODO Verfeinern! (abfrage ob m/mm/km oder ähnliches eingegeben wurde)
		
		/////////////////MASS/////////////////
		strtemp = myView.pa_computetab.tf_Mass.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				if(stratemp[1].compareTo("AE") == 0 || stratemp[1].compareTo("AU") == 0) {
					factor = CalcCode.AE;
				}
				else if(stratemp[1].compareTo("µg") == 0) {
					factor = 1.0E-9;
				}
				else if(stratemp[1].compareTo("mg") == 0) {
					factor = 1.0E-6;
				}
				else if(stratemp[1].compareTo("g") == 0) {
					factor = 1.0E-3;
				}
				else if(stratemp[1].compareTo("kg") == 0) {
					factor = 1.0;
				}
				else if(stratemp[1].compareTo("t") == 0) {
					factor = 1.0E3;
				}
				else {
					debugout("CheckInput() - tf_Mass[1]:"+stratemp[1]);
					bacheck[0] = true;
				}
				dtemp = Double.parseDouble(strtemp);
			}
			if(stratemp.length <= 1) {
					dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[0] = true;
		}
		if(bacheck[0] == false)
			mp.setMass(dtemp*factor);
		
		
		/////////////////RADIUS/////////////////
		strtemp = myView.pa_computetab.tf_Radius.getText();	
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];		
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				if(stratemp[1].compareTo("AE") == 0 || stratemp[1].compareTo("AU") == 0) {
					factor = CalcCode.AE;
				}
				else if(stratemp[1].compareTo("Lj") == 0 || stratemp[1].compareTo("ly") == 0) {
					factor = CalcCode.LY;
				}
				else if(stratemp[1].compareTo("nm") == 0) {
					factor = 1.0E-12;
				}
				else if(stratemp[1].compareTo("µm") == 0) {
					factor = 1.0E-9;
				}
				else if(stratemp[1].compareTo("mm") == 0) {
					factor = 1.0E-6;
				}
				else if(stratemp[1].compareTo("cm") == 0) {
					factor = 1.0E-4;
				}
				else if(stratemp[1].compareTo("m") == 0) {
					factor = 1.0E-3;
				}
				else if(stratemp[1].compareTo("km") == 0) {
					factor = 1.0;
				}
				else {
					debugout("CheckInput() - setRadius[1]:"+stratemp[1]);
					bacheck[1] = true;
				}
				dtemp = Double.parseDouble(strtemp);
			}
			if(stratemp.length <= 1) {
					dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[1] = true;
		}
		if(bacheck[1] == false)
			mp.setRadius(dtemp*CalcCode.RACCURACY*factor);
		
		//////////////////SPEEDX//////////////////
		strtemp = myView.pa_computetab.tf_Speedx.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				String[] safactor;
				safactor = stratemp[1].split("/");
				
				if(safactor.length < 2)
					bacheck[2] = true;
				else {
					if(safactor[0].compareTo("AE") == 0 || safactor[0].compareTo("AU") == 0)
						factor = CalcCode.AE;
					else if(safactor[0].compareTo("km") == 0)
						factor = 1.0E3;
					else if(safactor[0].compareTo("m") == 0)
						factor = 1.0;
					else if(safactor[0].compareTo("mm") == 0)
						factor = 1.0E-3;
					else if(safactor[0].compareTo("µm") == 0)
						factor = 1.0E-6;
					else { 
						bacheck[2] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					
					if(safactor[1].compareTo("ms") == 0)
						factor *= 1.0E3;
					else if(safactor[1].compareTo("s") == 0)
						factor /= 1.0;
					else if(safactor[1].compareTo("min") == 0)
						factor /= 60.0;
					else if(safactor[1].compareTo("h") == 0)
						factor /= 3600.0;
					else if(safactor[1].compareTo("d") == 0)
						factor /= (24.0*3600.0);
					else if(safactor[1].compareTo("a") == 0)
						factor /= (365.0*24.0*3600.0);
					else { 
						bacheck[2] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
				}
				dtemp = Double.parseDouble(strtemp);
			}
			else if(stratemp.length <= 1) {
				factor = 1.0;
				dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[2] = true;
		}
		if(bacheck[2] == false) {
			if(!mp.setSpeedx(dtemp*factor)) {
				bacheck[2] = true;
				bacheck[3] = true;
				bacheck[4] = true;
			}
		}
		
		/////////////////SPEEDY/////////////////
		strtemp = myView.pa_computetab.tf_Speedy.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				String[] safactor;
				safactor = stratemp[1].split("/");
				
				if(safactor.length < 2)
					bacheck[3] = true;
				else {
					if(safactor[0].compareTo("AE") == 0 || safactor[0].compareTo("AU") == 0)
						factor = CalcCode.AE;
					else if(safactor[0].compareTo("km") == 0)
						factor = 1.0E3;
					else if(safactor[0].compareTo("m") == 0)
						factor = 1.0;
					else if(safactor[0].compareTo("mm") == 0)
						factor = 1.0E-3;
					else if(safactor[0].compareTo("µm") == 0)
						factor = 1.0E-6;
					else { 
						bacheck[3] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					
					if(safactor[1].compareTo("ms") == 0)
						factor *= 1.0E3;
					else if(safactor[1].compareTo("s") == 0)
						factor /= 1.0;
					else if(safactor[1].compareTo("min") == 0)
						factor /= 60.0;
					else if(safactor[1].compareTo("h") == 0)
						factor /= 3600.0;
					else if(safactor[1].compareTo("d") == 0)
						factor /= (24.0*3600.0);
					else if(safactor[1].compareTo("a") == 0)
						factor /= (365.0*24.0*3600.0);
					else { 
						bacheck[3] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
				}
				dtemp = Double.parseDouble(strtemp);
			}
			else if(stratemp.length <= 1) {
				factor = 1.0;
				dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[3] = true;
		}
		if(bacheck[3] == false) {
			if(!mp.setSpeedy(dtemp*factor)) {
				bacheck[2] = true;
				bacheck[3] = true;
				bacheck[4] = true;
			}
		}

		/////////////////SPEEDZ/////////////////
		strtemp = myView.pa_computetab.tf_Speedz.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				String[] safactor;
				safactor = stratemp[1].split("/");
				
				if(safactor.length < 2)
					bacheck[4] = true;
				else {
					if(safactor[0].compareTo("AE") == 0 || safactor[0].compareTo("AU") == 0)
						factor = CalcCode.AE;
					else if(safactor[0].compareTo("km") == 0)
						factor = 1.0E3;
					else if(safactor[0].compareTo("m") == 0)
						factor = 1.0;
					else if(safactor[0].compareTo("mm") == 0)
						factor = 1.0E-3;
					else if(safactor[0].compareTo("µm") == 0)
						factor = 1.0E-6;
					else { 
						bacheck[4] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					
					if(safactor[1].compareTo("ms") == 0)
						factor *= 1.0E3;
					else if(safactor[1].compareTo("s") == 0)
						factor /= 1.0;
					else if(safactor[1].compareTo("min") == 0)
						factor /= 60.0;
					else if(safactor[1].compareTo("h") == 0)
						factor /= 3600.0;
					else if(safactor[1].compareTo("d") == 0)
						factor /= (24.0*3600.0);
					else if(safactor[1].compareTo("a") == 0)
						factor /= (365.0*24.0*3600.0);
					else { 
						bacheck[4] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
				}
				dtemp = Double.parseDouble(strtemp);
			}
			else if(stratemp.length <= 1) {
				factor = 1.0;
				dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[4] = true;
		}
		if(bacheck[4] == false) {
			if(!mp.setSpeedz(dtemp*factor)) {
				bacheck[2] = true;
				bacheck[3] = true;
				bacheck[4] = true;
			}
		}

		/////////////////COORDX/////////////////
		strtemp = myView.pa_computetab.tf_Coordx.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				if(stratemp[1].compareTo("AE") == 0 || stratemp[1].compareTo("AU") == 0) {
					factor = CalcCode.AE;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("Lj") == 0 || stratemp[1].compareTo("ly") == 0) {
					factor = CalcCode.LY;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("km") == 0) {
					ltemp = 1000*1000*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("m") == 0) {
					ltemp = 1000*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("cm") == 0) {
					ltemp = Long.parseLong(strtemp);
				}
				else {
					debugout("CheckInput() - setCoordx[1]:"+stratemp[1]);
					bacheck[5] = true;
				}
	
			}
			if(stratemp.length <= 1) {
					ltemp = Long.parseLong(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[5] = true;
		}
		if(bacheck[5] == false)
			mp.setCoordx(ltemp);

		/////////////////COORDY/////////////////
		strtemp = myView.pa_computetab.tf_Coordy.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				if(stratemp[1].compareTo("AE") == 0 || stratemp[1].compareTo("AU") == 0) {
					factor = CalcCode.AE;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("Lj") == 0 || stratemp[1].compareTo("ly") == 0) {
					factor = CalcCode.LY;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("km") == 0) {
					ltemp = 1000*1000*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("m") == 0) {
					ltemp = 1000*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("cm") == 0) {
					ltemp = Long.parseLong(strtemp);
				}
				else {
					debugout("CheckInput() - setCoordy[1]:"+stratemp[1]);
					bacheck[6] = true;
				}
				
			}
			if(stratemp.length <= 1) {
					ltemp = Long.parseLong(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[6] = true;
		}
		if(bacheck[6] == false)
			mp.setCoordy(ltemp);

		/////////////////COORDZ/////////////////
		strtemp = myView.pa_computetab.tf_Coordz.getText();
		stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		try {
			factor = 1.0;
			if(stratemp.length > 1) {
				if(stratemp[1].compareTo("AE") == 0 || stratemp[1].compareTo("AU") == 0) {
					factor = CalcCode.AE;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("Lj") == 0 || stratemp[1].compareTo("ly") == 0) {
					factor = CalcCode.LY;
					dtemp = Double.parseDouble(strtemp);
					ltemp = Math.round(factor*dtemp*CalcCode.LACCURACY);
				}
				else if(stratemp[1].compareTo("km") == 0) {
					ltemp = Math.round(1000*CalcCode.LACCURACY)*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("m") == 0) {
					ltemp = Math.round(CalcCode.LACCURACY)*Long.parseLong(strtemp);
				}
				else if(stratemp[1].compareTo("cm") == 0) {
					ltemp = Long.parseLong(strtemp);
				}
				else {
					debugout("CheckInput() - setCoordz[1]:"+stratemp[1]);
					bacheck[7] = true;
				}
			}
			if(stratemp.length <= 1) {
					ltemp = Long.parseLong(strtemp);
			}
		} catch(NumberFormatException e) {
			bacheck[7] = true;
		}
		if(bacheck[7] == false)
			mp.setCoordz(ltemp);
		
		return bacheck;
	}
	

	public static String getCurrentFolder() {
		return System.getProperty("user.dir").toString();
	}
}