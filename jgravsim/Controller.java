package jgravsim;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller /*extends Applet*/ implements ActionListener, ChangeListener, MouseMotionListener, MouseListener, 
MouseWheelListener, ItemListener, WindowListener, KeyListener {

	public static final int revision = 10;
	/* manual version control - if someone knows something nice, I would be pleased */
	public static final int BUILD =	Controller.revision + 
									CalcCode.revision +
									CalcProgress.revision +
									DynamicWPTLoader.revision +
									InterpretInput.revision +
									LanguageSelection.revision +
									MVMath.revision +
									Masspoint.revision +
									Masspoint_Sim.revision +
									Model.revision +
									ObjectView.revision +
									ObjectView2D.revision +
									ObjectView3D.revision +
									TabAbout.revision +
									TabCompute.revision +
									TabComputeAdvanced.revision +
									TabVisualisation.revision +
									TabVisualisationControls.revision +
									TabVisualisationData.revision +
									TabVisualisationOptions.revision +
									TabVisualisationPlot.revision +
									View.revision +
									View_CalcOptions.revision +
									View_CalcProgress.revision +
									View_PlotOption.revision +
									View_SpeedVector.revision +
									XMLParser.revision;

	static final boolean CURRENTBUILD = true;
	static final boolean CPP = true;
	static final boolean CPPDEBUG = false;

	static final boolean DEBUG = true;
	static final float ZOOMLEVEL = 10.0f;
	static final double VERSION = 1.8;
	static final int WPT_VERSION = 2;
	
	static final String HOMEPAGE = "http://jgravsim.eod.xmw.de/";
	static final String EMAIL = "jgravsim@gmail.com";

	static final Masspoint mpdf_sun		=	new Masspoint(CalcCode.SM,CalcCode.SR);
	static final Masspoint mpdf_earth	=	new Masspoint(CalcCode.EM,CalcCode.ER);
	static final Masspoint mpdf_moon	=	new Masspoint(7.3477E22	,1.737103E6);
	static final Masspoint mpdf_mercury	=	new Masspoint(3.3022E23	,2.4397E6);
	static final Masspoint mpdf_venus	=	new Masspoint(4.8685E24	,6.0518E6);
	static final Masspoint mpdf_mars	=	new Masspoint(6.4185E23	,(3.3962E6+3.3762E6)/2.);
	static final Masspoint mpdf_ceres	=	new Masspoint(9.43E20	,(0.4873E6+0.4547E6)/2.);
	static final Masspoint mpdf_jupiter	=	new Masspoint(1.8986E27	,(71.492E6+66.854E6)/2.);
	static final Masspoint mpdf_saturn	=	new Masspoint(5.6846E26	,(60.268E6+54.364E6)/2.);
	static final Masspoint mpdf_uranus	=	new Masspoint(8.681E25	,(25.559E6+24.973E6)/2.);
	static final Masspoint mpdf_neptune	=	new Masspoint(1.0243E26	,(24.764E6+24.341E6)/2.);
	static final Masspoint mpdf_pluto	=	new Masspoint(1.305E22	,1.153E6);
	static final Masspoint mpdf_haumea	=	new Masspoint(4.006E21	,1.436E6);
	static final Masspoint mpdf_makemake	=	new Masspoint(4E21	,0.750E6);
	static final Masspoint mpdf_eris	=	new Masspoint(1.67E22	,1.3E6);
	static final Masspoint mpdf_proxima_centauri =	new Masspoint(0.123*CalcCode.SM,0.145*CalcCode.SR);
	static final Masspoint mpdf_alpha_centauri_a =	new Masspoint(1.100*CalcCode.SM,1.227*CalcCode.SR);
	static final Masspoint mpdf_rigel	=	new Masspoint(17*CalcCode.SM,78*CalcCode.SR);
	static final Masspoint mpdf_zeta_puppis	=	new Masspoint(53.9*CalcCode.SM,18.6*CalcCode.SR);	// http://arxiv.org/abs/1003.0892
	static final Masspoint mpdf_sirius_b	=	new Masspoint(0.978*CalcCode.SM,0.0084*CalcCode.SR);
	static final Masspoint mpdf_cygnus_x1	=	new Masspoint(8.7*CalcCode.SM,1.0);
	
	View myView;
	Model myModel;
	CalcCode myCalculation;
	CalcProgress myCalcProgress;
	View_CalcOptions myView_CalcOptions;
	View_CalcProgress myView_CalcProgress;
	
	Vector<Masspoint> vmasspoints = new Vector<Masspoint>();
	Masspoint mp_default;
    Step currentstep = new Step(0, 0);
    String strlastfolder = null;
    View_SpeedVector mySpeedVector = null;
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

	Controller(int language, String filename) {	
		/* Register Stuff from Visualisation */
			File fpInputFile = new File( filename ); 
			
			myModel = new Model();
			XMLParser myXMLParser = new XMLParser(View.textfile, language);
			
			int errorline = myModel.loadDataset(fpInputFile);
			
			if(errorline != Model.INFILE_NOERROR) {
				if(errorline == Model.INFILE_EOFSTARTERROR) {
					System.out.println(myXMLParser.getText(150)+":\n"+myXMLParser.getText(151));
				}
				else if(errorline == Model.INFILE_EOFSTEPERROR) {
					System.out.println(myXMLParser.getText(150)+":\n"+myXMLParser.getText(152)+"\n"+myXMLParser.getText(153));
				}
				else if(errorline == Model.INFILE_EOFOBJERROR) {
					System.out.println(myXMLParser.getText(150)+":\n"+myXMLParser.getText(152)+"\n"+myXMLParser.getText(154));
				}
				else if(errorline == Model.INFILE_FILENOTFOUND) {
					System.out.println(myXMLParser.getText(150)+":\n"+myXMLParser.getText(155));
				}
				else if(errorline == Model.INFILE_READERROR) {
					System.out.println(myXMLParser.getText(150)+":\n"+myXMLParser.getText(157));
				}
				else if(errorline == Model.INFILE_WPTERROR) {
					System.out.println(myXMLParser.getText(150)+":\n"+String.format(myXMLParser.getText(180), WPT_VERSION));
				}
				else {
					System.out.println(myXMLParser.getText(150)+String.format(myXMLParser.getText(149),errorline));
				}
			}
			else {	/* no error detected ... continue */
				vmasspoints.addAll(myModel.stDataset);
				flagcalc = true;
				calc_datacount = myModel.ddataCount;
				calc_timecount = myModel.dtimeCount;
				calc_timestep = myModel.dtimeCount;
				myModel.writetempHeader(calc_datacount,calc_timecount);
				
				debugout("Starting Java Calculation \""+fpInputFile+"\": datacount="+calc_datacount+", timecount="+calc_timecount+", timestep="+calc_timestep);
				myCalculation = new CalcCode(this, myModel, calc_datacount, calc_timecount, calc_timestep, true);
				myCalculation.start();
			}
	}
	
	Controller(int language) {	
		/* Register Stuff from Visualisation */
		myView = new View(language);
		myModel = new Model();
		
		myView.addWindowListener(this);
		myView.addKeyListener(this);	
		myView.setFocusable(true);

		myView.pa_computetab.ButtonsStart();
		
		myView.pa_computetab.cb_Objects.addItemListener(this);
		myView.pa_computetab.cb_mpdefaults.addItemListener(this);
		myView.pa_visualtab.pa_visual_datatab.cb_Objects.addItemListener(this);
		
		myView.pa_visualtab.pa_visual_contrtab.b_loadfile.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_loadlast.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_unloadfile.addActionListener(this);
		myView.pa_visualtab.pa_visual_optiontab.b_colorch_grid.addActionListener(this);
		myView.pa_visualtab.pa_visual_optiontab.b_colorch_speedvec.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_resetoffset.addActionListener(this);		
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_play.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepback.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stepforw.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_stop.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_start.addActionListener(this);
		myView.pa_visualtab.pa_visual_contrtab.b_playcontr_end.addActionListener(this);
		
		myView.pa_visualtab.pa_visual_plottab.b_plot_draw.addActionListener(this);
		myView.pa_visualtab.pa_visual_plottab.b_plot_remove.addActionListener(this);
		
		myView.pa_visualtab.pa_visual_optiontab.chb_mpids.addItemListener(this);
		myView.pa_visualtab.pa_visual_optiontab.chb_vvector.addItemListener(this);
		
		myView.pa_computetab.b_colorch_grid.addActionListener(this);
		myView.pa_computetab.b_colorch_speedvec.addActionListener(this);
		myView.pa_computetab.b_resetoffset.addActionListener(this);		
		myView.pa_computetab.b_Remove.addActionListener(this);
		myView.pa_computetab.b_clone.addActionListener(this);
		myView.pa_computetab.b_savepreset.addActionListener(this);
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
		myView.pa_computetab.pa_compute_dataeasy.sl_Mass.addChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Speed.addChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Radius.addChangeListener(this);

		myView.pa_computetab.chb_mpids.addItemListener(this);
		myView.pa_computetab.chb_vvector.addItemListener(this);
		myView.pa_computetab.pa_compute_dataeasy.chb_Dense.addItemListener(this);
		myView.pa_computetab.pa_compute_dataadvanced.chb_Dense.addItemListener(this);
	
		myView.pa_visualtab.ov_vis_front.addMouseListener(this);
		myView.pa_visualtab.ov_vis_right.addMouseListener(this);
		myView.pa_visualtab.ov_vis_top.addMouseListener(this);
		myView.pa_computetab.ov_front.addMouseListener(this);
		myView.pa_computetab.ov_top.addMouseListener(this);
		myView.pa_computetab.ov_top.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.addMouseListener(this);
		myView.pa_abouttab.jHomepage.addMouseListener(this);
		myView.pa_abouttab.jContact.addMouseListener(this);
	
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
	
		myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.setValue(95);	//=Zoom 7.5
		myView.pa_visualtab.setGridOffset(25);

		myView.pa_computetab.sl_zoomlevel.setValue(95);	//=Zoom 7.5
		myView.pa_computetab.setGridOffset(25);
	}
	
	public static void main(String[] args) {
		//the two following lines are for an applet of this code
		//Frame frame = new MainFrame(new Controller(), 512, 512);
		//frame.setVisible(false);

		LanguageSelection langsel = new LanguageSelection();
		if(langsel.readLanguages(View.textfile)) {
			langsel.writeLanguages();
			
			int language = -1;
			
			if(args.length > 0 && args[0] != null && args[0] != "") {
				debugout("main() - args.length > 0! file="+args[0]);
				new Controller(language, args[0]);
			} else {
				debugout("main() - args.length = "+args.length+" <= 0!");
				new Controller(language);
			}
		}
	}

	public static void debugout(String a) {
		if(CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
	
	@SuppressWarnings("unused")
	public static void cppdebugout(String a) {
		if(CPP && CPPDEBUG)
			System.out.println("c++: "+a);
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
			
			fc.setFileFilter(new WPTFilter(this));
			fc.setSelectedFile(new File( Model.FILE_TEMP ));
			int ret = fc.showOpenDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				File fpInputFile = fc.getSelectedFile().getAbsoluteFile();
				 strlastfolder = fpInputFile.getPath();
				 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(321));
				 if( isError(myModel.loadInputFile(fpInputFile)) ) {
					 myView.pa_visualtab.setPlayControlsEnabled(false);
					 myView.pa_visualtab.displayStep(null);
					 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(324));
				 }
				 else {	/* no error detected ... continue */
					myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
					myView.pa_visualtab.pa_visual_contrtab.setTimeStep(myModel.dtimeCount);
					myView.pa_visualtab.pa_visual_contrtab.setMaxFrame(myModel.dynamicLoader.iLastStep);
					myView.pa_visualtab.setPlayControlsEnabled(true);
					myView.pa_visualtab.setCurFrame(0);
					myView.pa_visualtab.ov_vis_top.alldots = null;
					myView.pa_visualtab.ov_vis_front.alldots = null;
					myView.pa_visualtab.ov_vis_right.alldots = null;
					myView.pa_visualtab.updateViews(true);
				 }
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_loadlast) {
			 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(321));
			 File fpInputFile = new File( Model.FILE_TEMP );
			 if( isError(myModel.loadInputFile(fpInputFile)) ) {
				 myView.pa_visualtab.setPlayControlsEnabled(false);
				 myView.pa_visualtab.displayStep(null);
				 myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(324));
			 }
			 else {	/* no error detected ... continue */
				myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
				myView.pa_visualtab.pa_visual_contrtab.setTimeStep(myModel.dtimeCount);
				myView.pa_visualtab.pa_visual_contrtab.setMaxFrame(myModel.dynamicLoader.iLastStep);
				myView.pa_visualtab.setPlayControlsEnabled(true);
				myView.pa_visualtab.setCurFrame(0);
				myView.pa_visualtab.ov_vis_top.alldots = null;
				myView.pa_visualtab.ov_vis_front.alldots = null;
				myView.pa_visualtab.ov_vis_right.alldots = null;
				myView.pa_visualtab.updateViews(true);
			 }
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_unloadfile) {
			myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(325));
			
			myView.pa_visualtab.resetOffset();
			myView.pa_visualtab.pa_visual_contrtab.setTimeStep(0);
			myView.pa_visualtab.pa_visual_contrtab.setMaxFrame(0);
			myView.pa_visualtab.setCurFrame(0);
			myModel.stDataset = null;
			myView.pa_visualtab.ov_vis_top.alldots = null;
			myView.pa_visualtab.ov_vis_front.alldots = null;
			myView.pa_visualtab.ov_vis_right.alldots = null;
			myView.pa_visualtab.displayStep(null);
			//myView.pa_visualtab.repaintViews();
			myView.pa_visualtab.setPlayControlsEnabled(false);
			myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(myView.myXMLParser.getText(303));
		}	
		else if(source == myView.pa_visualtab.pa_visual_optiontab.b_colorch_grid) {
			Color newColor = JColorChooser.showDialog(myView, myView.myXMLParser.getText(103), myView.pa_visualtab.ov_vis_front.coGridColor);
			if(newColor != null) {
				myView.pa_visualtab.setGridColor(newColor);
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_optiontab.b_colorch_speedvec) {
			Color newColor = JColorChooser.showDialog(myView, myView.myXMLParser.getText(104), myView.pa_visualtab.ov_vis_front.coSpeedvecColor);
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
		else if(source == myView.pa_visualtab.pa_visual_contrtab.b_resetoffset) {
			myView.pa_visualtab.resetOffset();
		}
		else if(source == myView.pa_visualtab.pa_visual_plottab.b_plot_draw) {
			if(myView.pa_visualtab.pa_visual_plottab.cb_plot_dense.getSelectedIndex() == 0) {
				String data;
				int idata = 0;
				boolean check = true;
				while(check || idata <= 0) {
					data = JOptionPane.showInputDialog(myView.myXMLParser.getText(323),10);
					try {
						 idata = (int)Double.parseDouble(data);
						 check = false;
					} catch(NumberFormatException f) {
						check = true;
					} catch(NullPointerException f) {
						//cancel has been pressed
						return;
					}
				}
				myView.pa_visualtab.drawPlot(myModel.dynamicLoader, idata, true); 
			} else {
				int data = (int)Math.pow(10.0,myView.pa_visualtab.pa_visual_plottab.cb_plot_dense.getSelectedIndex()-1);
				myView.pa_visualtab.drawPlot(myModel.dynamicLoader, data, true); 
			}
		}
		else if(source == myView.pa_visualtab.pa_visual_plottab.b_plot_remove) {
			myView.pa_visualtab.removePlot();
		}
		
		
	//////Compute TAB//////
		else if(source == myView.pa_computetab.b_reset) {
			Object[] options = { myView.myXMLParser.getText(211), myView.myXMLParser.getText(212), myView.myXMLParser.getText(213) };
			int a = JOptionPane.showOptionDialog(myView, myView.myXMLParser.getText(210),myView.myXMLParser.getText(201),
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			debugout("rest() - Button:"+a);
			if(a == 0) {
				for(int i=vmasspoints.size()-1;i>=0;i--) {
					RemoveMp(vmasspoints.get(i));
				}
				myView.pa_visualtab.displayStep(null);
				myView.pa_visualtab.setPlayControlsEnabled(false);
				myView.pa_visualtab.enableCounter(false);
				myView.pa_computetab.cb_mpdefaults.setSelectedIndex(0);
			}
			else if(a == 1){
				File fpInputFile = new File( Model.FILE_TEMP );
				if( !isError(myModel.loadDataset(fpInputFile)) ) {
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
					id = vmasspoints.get(vmasspoints.size()-1).getID();
					
					Masspoint mp = vmasspoints.get(0);
					updateComputePanels(mp, source);
				}
			}	
		}
	else if(source == myView.pa_computetab.b_compute)  {
		//disable while program is running
		myView.pa_computetab.ButtonsAni();
		myView.pa_computetab.b_stop.setEnabled(false);
		myView_CalcOptions = new View_CalcOptions(this, myModel);
		myView.pa_computetab.ov_top.removeMouseListener(this);
		myView.pa_computetab.ov_front.removeMouseListener(this);
		myView.pa_computetab.ov_top.removeMouseMotionListener(this);
		myView.pa_computetab.ov_front.removeMouseMotionListener(this);
		myView.pa_computetab.ov_top.removeMouseWheelListener(this);
		myView.pa_computetab.ov_front.removeMouseWheelListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.removeMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.removeMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.removeMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.removeMouseListener(this);
		if(mySpeedVector != null && mySpeedVector.isVisible())
    		mySpeedVector.dispose();
	} 
	else if(source == myView.pa_computetab.b_stop) {
		//Stop is pressed and timer is not running, so calculation has to be stopped
	   	if(myCalcProgress != null) {
	   		myCalcProgress.halt();
	   		debugout("Halted myCalcProgress "+ myCalcProgress.pcalculation.toString());
	   		int laststep = myModel.findlaststep(new File(Model.FILE_TEMP));
	   		if(laststep > 0)
	   			myModel.correctHeader(new File(Model.FILE_TEMP), laststep);
	   		
	   		myCalcProgress = null;
	   		CalculationFinished(CalcCode.NOERROR);
	   	}
	   	else
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
			fc.setFileFilter(new WPTFilter(this));
			fc.setSelectedFile(new File(String.valueOf(System.currentTimeMillis())+"."+Model.FILE_ENDING));
			int ret = fc.showSaveDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				File fpInputFile = fc.getSelectedFile().getAbsoluteFile();

				myModel.saveOutputfile(fpInputFile);
				JOptionPane.showMessageDialog(myView, String.format(myView.myXMLParser.getText(216),fpInputFile.getName()),myView.myXMLParser.getText(214),JOptionPane.INFORMATION_MESSAGE);			
			}
			myView.pa_computetab.ButtonsDeactive(true);
	}
	else if(source == myView.pa_computetab.b_loadfile) {
			JFileChooser fc;
			if(strlastfolder == null) 
				fc = new JFileChooser(getCurrentFolder());
			else
				fc = new JFileChooser(strlastfolder);
			
			fc.setFileFilter(new WPTFilter(this));
			int ret = fc.showOpenDialog(myView);
			if(ret == JFileChooser.APPROVE_OPTION) {
				File fpInputFile = fc.getSelectedFile().getAbsoluteFile();
				if( isError(myModel.loadDataset(fpInputFile)) )
					myView.pa_computetab.ButtonsStd();
				else {	/* no error detected ... continue */
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
					id = vmasspoints.get(vmasspoints.size()-1).getID();
					
					Masspoint mp = vmasspoints.get(0);
					updateComputePanels(mp, source);
				}
			}
	}
	else if(source == myView.pa_computetab.b_Remove)  {
		int a = JOptionPane.showConfirmDialog(myView, String.format(myView.myXMLParser.getText(217), GetSelectedMasspoint().getID()),myView.myXMLParser.getText(215),JOptionPane.YES_NO_OPTION);
		if(a == 0) {
			Masspoint mp = GetSelectedMasspoint();
			RemoveMp(mp);
		}
	}
	else if(source == myView.pa_computetab.b_clone) {
		Masspoint mp_old = GetSelectedMasspoint();
		Masspoint mp_clone = addMasspoint(0, 0, 0, mp_old);
		int clone_id = mp_clone.getID();
		mp_clone.setData(mp_old);
		mp_clone.setID(clone_id);
		Random randomizer = new Random();
		//don't randomize x-coordinate in order to improve visibility
		mp_clone.addMLVCoord(new MLVector(
				MVMath.ConvertToL(mp_clone.getAbsRadius()*1.5),
				MVMath.ConvertToL(mp_clone.getAbsRadius()*1.5 *(randomizer.nextInt(2)>0?1:-1)),
				MVMath.ConvertToL(mp_clone.getAbsRadius()*1.5 *(randomizer.nextInt(2)>0?1:-1)) ));
		updateComputePanels(mp_clone, source);
	}
	else if(source == myView.pa_computetab.b_savepreset) {
		Masspoint mp_old = GetSelectedMasspoint();
		Masspoint mp_clone = new Masspoint(-1, 0, 0, 0, mp_old);
		mp_clone.setName( JOptionPane.showInputDialog(myView, String.format(myView.myXMLParser.getText(255), mp_old.getID()),myView.myXMLParser.getText(215), JOptionPane.QUESTION_MESSAGE) );
		myView.pa_computetab.cb_mpdefaults.addItem(mp_clone);
	}
	else if(source == myView.pa_computetab.b_edit) {
		if(flagedit == false) {
			myView.pa_computetab.b_edit.setText(myView.myXMLParser.getText(208));
			myView.pa_computetab.b_edit.setBackground(Color.GREEN);
			myView.pa_computetab.ButtonsEdit();
			myView.pa_computetab.ov_top.removeMouseListener(this);
			myView.pa_computetab.ov_top.removeMouseMotionListener(this);
			myView.pa_computetab.ov_front.removeMouseListener(this);
			myView.pa_computetab.ov_front.removeMouseMotionListener(this);
			myView.pa_computetab.ov_top.removeMouseWheelListener(this);
			myView.pa_computetab.ov_front.removeMouseWheelListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.removeMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.removeMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.removeMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.removeMouseListener(this);
			if(mySpeedVector != null && mySpeedVector.isVisible())
				mySpeedVector.dispose();
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
				case 0: myView.pa_computetab.pa_compute_dataadvanced.tf_Mass.setBackground(Color.RED); break;
				case 1: myView.pa_computetab.pa_compute_dataadvanced.tf_Radius.setBackground(Color.RED); break;
				case 2: myView.pa_computetab.pa_compute_dataadvanced.tf_Speedx_exact.setBackground(Color.RED); break;
				case 3: myView.pa_computetab.pa_compute_dataadvanced.tf_Speedy_exact.setBackground(Color.RED); break;
				case 4: myView.pa_computetab.pa_compute_dataadvanced.tf_Speedz_exact.setBackground(Color.RED); break;
				case 5: myView.pa_computetab.pa_compute_dataadvanced.tf_Coordx.setBackground(Color.RED); break;
				case 6: myView.pa_computetab.pa_compute_dataadvanced.tf_Coordy.setBackground(Color.RED); break;
				case 7: myView.pa_computetab.pa_compute_dataadvanced.tf_Coordz.setBackground(Color.RED); break;
				}
				flagcheck = false;
				}
			}
			if(flagcheck) {
				flagedit = false;
				myView.pa_computetab.b_edit.setBackground(null);
				myView.pa_computetab.b_edit.setText(myView.myXMLParser.getText(207));
				myView.pa_computetab.ButtonsStd();
				myView.pa_computetab.ov_top.addMouseListener(this);
				myView.pa_computetab.ov_top.addMouseMotionListener(this);
				myView.pa_computetab.ov_front.addMouseListener(this);
				myView.pa_computetab.ov_front.addMouseMotionListener(this);
				myView.pa_computetab.ov_top.addMouseWheelListener(this);
				myView.pa_computetab.ov_front.addMouseWheelListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.addMouseListener(this);
				updateComputePanels(GetSelectedMasspoint(), source);
			}
			else
			JOptionPane.showMessageDialog(myView, myView.myXMLParser.getText(209),myView.myXMLParser.getText(159),JOptionPane.ERROR_MESSAGE);
		}
	}	
		else if(source == myView.pa_computetab.b_colorch_grid) {
			Color newColor = JColorChooser.showDialog(myView, myView.myXMLParser.getText(103), myView.pa_computetab.ov_front.coGridColor);
			if(newColor != null) {
				myView.pa_computetab.setGridColor(newColor);
			}
		}
		else if(source == myView.pa_computetab.b_colorch_speedvec) {
			Color newColor = JColorChooser.showDialog(myView, myView.myXMLParser.getText(104), myView.pa_computetab.ov_front.coSpeedvecColor);
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
			float zoomLevel = (source.getMaximum()-source.getValue()+CalcCode.SZOOMCONST)/ZOOMLEVEL;
			myView.pa_visualtab.setZoom(zoomLevel, false);
		}
		else if(source == myView.pa_visualtab.pa_visual_contrtab.sl_gridoffset) {
			myView.pa_visualtab.setGridOffset(source.getValue());
		}
		else if(source == myView.pa_computetab.sl_zoomlevel) {
			float zoomLevel = (source.getMaximum()-source.getValue()+CalcCode.SZOOMCONST)/ZOOMLEVEL;
			myView.pa_computetab.setZoom(zoomLevel);
			myView.pa_computetab.repaintViews();
		}
		else if(source == myView.pa_computetab.sl_gridoffset) {
			myView.pa_computetab.setGridOffset(source.getValue());
		}
		
		else if(source == myView.pa_computetab.pa_compute_dataeasy.sl_Mass) {
			double dmass = myView.pa_computetab.pa_compute_dataeasy.sl_Mass.getValue();
			dmass = Math.pow(dmass,CalcCode.SMASSCONST);
			GetSelectedMasspoint().setMass(dmass+Double.MIN_VALUE);
			//debugout("sMass - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dmass);
			updateComputePanels(GetSelectedMasspoint(),source);
		}
		else if(source == myView.pa_computetab.pa_compute_dataeasy.sl_Speed) {
			double dspeed = myView.pa_computetab.pa_compute_dataeasy.sl_Speed.getValue();
			Masspoint mp = GetSelectedMasspoint();
			if(dspeed > 0 && mp.getSpeed() == 0) {
				mp.setSpeed(new MDVector(1.0,1.0,1.0));
			}

			//Maximum v = 299792457.99999994 m/s
			dspeed = Math.pow(dspeed/100.0,CalcCode.SSPEEDCONST)*(CalcCode.LIGHTSPEED-Math.pow(10.0, -7.5));
			mp.setAbsSpeed(dspeed);
			updateComputePanels(GetSelectedMasspoint(),source);	
			//debugout("sSpeed - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dspeed);
		}			
		else if(source == myView.pa_computetab.pa_compute_dataeasy.sl_Radius) {
			double dradius = myView.pa_computetab.pa_compute_dataeasy.sl_Radius.getValue();
			dradius = Math.pow(dradius,CalcCode.SRADIUSCONST);
			GetSelectedMasspoint().setAbsRadius(dradius+Double.MIN_VALUE);
			//debugout("sRadius - Slider moved. Value="+myMPDataView.sRadius.getValue()+", dradius="+dradius);
			updateComputePanels(GetSelectedMasspoint(),source);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(e.getSource() == myView.pa_visualtab.ov_vis_top) {
			ObjectView2D source = (ObjectView2D)e.getSource();
			myView.pa_visualtab.changeOffsetX(e.getX()-source.iLastMouseX);
			myView.pa_visualtab.changeOffsetY(-(e.getY()-source.iLastMouseY));
			source.iLastMouseX = e.getX();
			source.iLastMouseY = e.getY();
		}
		else if(e.getSource() == myView.pa_visualtab.ov_vis_front) {
			ObjectView2D source = (ObjectView2D)e.getSource();
			myView.pa_visualtab.changeOffsetX(e.getX()-source.iLastMouseX);
			myView.pa_visualtab.changeOffsetZ(e.getY()-source.iLastMouseY);
			source.iLastMouseX = e.getX();
			source.iLastMouseY = e.getY();
		}
		else if(e.getSource() == myView.pa_visualtab.ov_vis_right) {
			if(!myView.pa_visualtab.Is3dEnabled()) {
				ObjectView2D source = (ObjectView2D)e.getSource();
				myView.pa_visualtab.changeOffsetY(e.getX()-source.iLastMouseX);
				myView.pa_visualtab.changeOffsetZ(e.getY()-source.iLastMouseY);
				source.iLastMouseX = e.getX();
				source.iLastMouseY = e.getY();
			}
		}
		else if(e.getSource() == myView.pa_computetab.ov_top) {
			ObjectView2D source = (ObjectView2D)e.getSource();
			if(flagclick < 0 && flagctrl == false) {
				Masspoint mphit = GetSelectedMasspoint();
				mphit.setCoordx(pxtomm(e.getX(), 'x'));
				mphit.setCoordy(pxtomm(e.getY(), 'y'));
				updateComputePanels(GetSelectedMasspoint(),source);
			}
			else if(flagctrl == true || (e.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
				myView.pa_computetab.changeOffsetX(e.getX()-source.iLastMouseX);
				myView.pa_computetab.changeOffsetY(e.getY()-source.iLastMouseY);
				source.iLastMouseX = e.getX();
				source.iLastMouseY = e.getY();
			}
		}
		else if(e.getSource() == myView.pa_computetab.ov_front) {
			ObjectView2D source = (ObjectView2D)e.getSource();
			if(flagclick < 0 && flagctrl == false) {
				Masspoint mphit = GetSelectedMasspoint();
				mphit.setCoordx(pxtomm(e.getX(), 'x'));
				mphit.setCoordz(pxtomm(e.getY(), 'z'));
				updateComputePanels(GetSelectedMasspoint(),source);
			}
			else if(flagctrl == true || (e.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
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
		Object source = e.getSource();

		if(source == myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact 
			||	source == myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact
			||	source == myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact
			||  source == myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact) {
			if(GetSelectedMasspoint() != null) {
				if(mySpeedVector == null ) {
					mySpeedVector = new View_SpeedVector(GetSelectedMasspoint(), this);
				} else if(mySpeedVector.masspoint.getID() != GetSelectedMasspoint().getID()) {
					mySpeedVector.dispose();
					mySpeedVector = new View_SpeedVector(GetSelectedMasspoint(), this);
				} else if(!mySpeedVector.isVisible())
					mySpeedVector.setVisible(true);
			}
		}
		else if(source == myView.pa_abouttab.jHomepage) {
			try { 
				Desktop.getDesktop().browse( new URI( HOMEPAGE ) ); 
			} catch ( Exception excep ) { 
				debugout("Desktop.getDesktop().browse() - No program found");
			}
		}
		else if(source == myView.pa_abouttab.jContact) {
			try { 
				Desktop.getDesktop().mail( new URI( "mailto:"+EMAIL ) ); 
			} catch ( Exception excep ) { 
				debugout("Desktop.getDesktop().mail() - No program found");
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		myView.requestFocusInWindow();
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent e) {
		//debugout("Dragging startet at: "+arg0.getPoint());
		Object source = e.getSource();

		if(source == myView.pa_computetab.ov_top) {	
			((ObjectView2D)source).iLastMouseX = e.getX();
			((ObjectView2D)source).iLastMouseY = e.getY();
			//left mouse button
			if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 &&
					(e.getModifiers() & InputEvent.CTRL_MASK) == 0) 
			{	
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), myView.myXMLParser.getText(111));		
				if(mphit == null) {
					flagclick = 1;
				} else {
					myView.pa_computetab.cb_Objects.setSelectedItem(mphit);
					myView.pa_computetab.setSelectedMasspointAndRepaint(mphit);
					flagclick = -1;
				}
			}
		}
		else if(source == myView.pa_computetab.ov_front) {			
				((ObjectView2D)source).iLastMouseX = e.getX();
				((ObjectView2D)source).iLastMouseY = e.getY();	
				//left mouse button
				if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 &&
						(e.getModifiers() & InputEvent.CTRL_MASK) == 0) {
					Masspoint mphit = null;
					mphit = checkforHit(e.getX(), e.getY(), myView.myXMLParser.getText(112));	
					if(mphit == null) {
						flagclick = 1;
					} else {
						myView.pa_computetab.cb_Objects.setSelectedItem(mphit);
						myView.pa_computetab.setSelectedMasspointAndRepaint(mphit);
						flagclick = -1;
					}
				}
			}	
		
		if(source == myView.pa_visualtab.ov_vis_top) {	
			((ObjectView2D)source).iLastMouseX = e.getX();
			((ObjectView2D)source).iLastMouseY = e.getY();
		}
		else if(source == myView.pa_visualtab.ov_vis_front) {			
			((ObjectView2D)source).iLastMouseX = e.getX();
			((ObjectView2D)source).iLastMouseY = e.getY();
		}
		else if(source == myView.pa_visualtab.ov_vis_right) {
			if(!myView.pa_visualtab.Is3dEnabled()) {
				((ObjectView2D)source).iLastMouseX = e.getX();
				((ObjectView2D)source).iLastMouseY = e.getY();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		//debugout("Dragging stopped at: "+arg0.getPoint());
		Object source = e.getSource();	
		
		if(source == myView.pa_computetab.ov_top && flagctrl == false) {
			if(flagclick >= 1) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), myView.myXMLParser.getText(111));
				
				if(mphit == null) {
					addMasspoint(e.getX(),e.getY(), 0, mp_default);
					updateComputePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
			myView.pa_computetab.setSelectedMasspointAndRepaint(null);
		}
		else if(source == myView.pa_computetab.ov_front && flagctrl == false) {
			if(flagclick >= 1) {
				Masspoint mphit = null;
				mphit = checkforHit(e.getX(), e.getY(), myView.myXMLParser.getText(112));
				
				if(mphit == null) {
					addMasspoint(e.getX(), 0, e.getY(), mp_default);
					updateComputePanels(GetSelectedMasspoint(),source);
				}
			}
			flagclick = 0;
			myView.pa_computetab.setSelectedMasspointAndRepaint(null);
		}
		else if(flagedit) {
			debugout("Flagedit1 = true");
			flagedit = false;
			myView.pa_computetab.ButtonsStd();
		}
	}


	public void mouseWheelMoved(MouseWheelEvent e) {
		Object source = e.getSource();
		if(source == myView.pa_visualtab.ov_vis_front || 
				source == myView.pa_visualtab.ov_vis_right || 
				source == myView.pa_visualtab.ov_vis_top) {
			double curZoom = myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.getValue();
			curZoom -= e.getWheelRotation();
			if((curZoom <= myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.getMaximum() 
					&& curZoom >= myView.pa_visualtab.pa_visual_contrtab.sl_zoomlevel.getMinimum())) {
				
				myView.pa_visualtab.addZoom(e.getWheelRotation()/ZOOMLEVEL);
			}
		}		
		else if(source == myView.pa_computetab.ov_front || 
				(ObjectView2D)e.getSource() == myView.pa_computetab.ov_top) {
			float curZoom = myView.pa_computetab.sl_zoomlevel.getValue();
			curZoom -= e.getWheelRotation();
			if((curZoom <= myView.pa_computetab.sl_zoomlevel.getMaximum() 
					&& curZoom >= myView.pa_computetab.sl_zoomlevel.getMinimum())) {
				
				myView.pa_computetab.setZoom(curZoom/ZOOMLEVEL);
				myView.pa_computetab.sl_zoomlevel.setValue((int)curZoom);
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		//Object-ID changed. Update all output data
		if(source == myView.pa_computetab.cb_Objects && myView.pa_computetab.cb_Objects.getItemCount() > 0) {
		Masspoint mp = GetSelectedMasspoint();
		if(mp != null) {
		   	updateComputePanels(mp,source);
		   	//debugout("Object "+(int)mp.id+" selected");
			}
		}		
		if(source == myView.pa_computetab.cb_mpdefaults) {
			debugout("itemStateChanged() - visual chb_vvector, value="+myView.pa_visualtab.pa_visual_optiontab.chb_vvector.isSelected());
			mp_default = (Masspoint)myView.pa_computetab.cb_mpdefaults.getSelectedItem();
		}	
		else if(source == myView.pa_computetab.pa_compute_dataeasy.chb_Dense || source == myView.pa_computetab.pa_compute_dataadvanced.chb_Dense) {
			myView.pa_computetab.pa_compute_dataeasy.chb_Dense.setSelected(e.getStateChange() == ItemEvent.SELECTED);
			myView.pa_computetab.pa_compute_dataadvanced.chb_Dense.setSelected(e.getStateChange() == ItemEvent.SELECTED);
		}	
		else if(source == myView.pa_visualtab.pa_visual_datatab.cb_Objects && myView.pa_visualtab.pa_visual_datatab.cb_Objects.getItemCount() > 0) {
			try {
				Masspoint_Sim mp = (Masspoint_Sim)myView.pa_visualtab.pa_visual_datatab.cb_Objects.getSelectedItem();
				if(mp != null) {
					myView.pa_visualtab.pa_visual_datatab.UpdatePanels(mp);
				   	//debugout("Object "+(int)mp.getID()+" selected");
				}
			}
			catch (RuntimeException exception) {
				//debugout("itemStateChanged() - Illegal Masspoint_Sim cast from cb_Objects");
			}
		}	
		else if(source == myView.pa_computetab.chb_mpids) {
			debugout("itemStateChanged() - compute chb_mpids, value="+myView.pa_computetab.chb_mpids.isSelected());
			myView.pa_computetab.setmpIDEnabled(myView.pa_computetab.chb_mpids.isSelected());
			myView.pa_computetab.repaintViews();
		}	
		else if(source == myView.pa_computetab.chb_vvector) {
			debugout("itemStateChanged() - compute chb_vvector, value="+myView.pa_computetab.chb_vvector.isSelected());
			myView.pa_computetab.setSpeedvecEnabled(myView.pa_computetab.chb_vvector.isSelected());
			myView.pa_computetab.repaintViews();
		}		
		else if(source == myView.pa_visualtab.pa_visual_optiontab.chb_mpids) {
			debugout("itemStateChanged() - visual chb_mpids, value="+myView.pa_visualtab.pa_visual_optiontab.chb_mpids.isSelected());
			myView.pa_visualtab.setmpIDEnabled(myView.pa_visualtab.pa_visual_optiontab.chb_mpids.isSelected());
			myView.pa_visualtab.repaintViews();
		}	
		else if(source == myView.pa_visualtab.pa_visual_optiontab.chb_vvector) {
			debugout("itemStateChanged() - visual chb_vvector, value="+myView.pa_visualtab.pa_visual_optiontab.chb_vvector.isSelected());
			myView.pa_visualtab.setSpeedvecEnabled(myView.pa_visualtab.pa_visual_optiontab.chb_vvector.isSelected());
			myView.pa_visualtab.repaintViews();
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
			myView.pa_visualtab.drawPlot(myModel.dynamicLoader, data, true);
		}
		else if(source == KeyEvent.VK_F10) {
			myView.pa_visualtab.removePlot();
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
			if(!CURRENTBUILD)
				myModel.deleteFile(Model.FILE_TEMP);		
		}
		System.exit(0);
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
	
	public void updateComputePanels(Masspoint mp, Object object) {
		
		if(mp == null) {
			debugout("updateComputePanels() - Sorry, no mp selected");
			return;
		}
		if(flagedit == true) {
			UpdateEdit(mp, object);
			return;
		}

		//debugout("UpdatePanels() - Object:"+String.valueOf(object));
		//debugout("speed,mass: "+mp.getSpeed()+","+mp.getAbsMass());
		MDVector mvspeed = mp.getMDVSpeed();
		MLVector mvcoord = mp.getPos();
		
		myView.pa_computetab.sl_zoomlevel.removeChangeListener(this);		
		myView.pa_computetab.pa_compute_dataeasy.sl_Mass.removeChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Speed.removeChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Radius.removeChangeListener(this);

		DecimalFormat df_d = new DecimalFormat("0.###E0");
		DecimalFormat df_s = new DecimalFormat("0.#####E0");
		DecimalFormat df_l = new DecimalFormat("0.#######E0");
		DecimalFormat df_c = new DecimalFormat("0.###########");
		
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Coordx) 
			myView.pa_computetab.pa_compute_dataeasy.tf_Coordx.setText(InterpretInput.niceInput_Length(mvcoord.x1, myView.myXMLParser,df_l));
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Coordy) 
			myView.pa_computetab.pa_compute_dataeasy.tf_Coordy.setText(InterpretInput.niceInput_Length(mvcoord.x2, myView.myXMLParser,df_l));
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Coordz) 
			myView.pa_computetab.pa_compute_dataeasy.tf_Coordz.setText(InterpretInput.niceInput_Length(mvcoord.x3, myView.myXMLParser,df_l));
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact) {
			if(mvspeed.x1 >= 0.1*CalcCode.LIGHTSPEED)
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.setText(df_c.format(mvspeed.x1/CalcCode.LIGHTSPEED)+" c");
			else
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.setText(df_s.format(mvspeed.x1)+" m/s");
			
		} if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact)  {
			if(mvspeed.x2 >= 0.1*CalcCode.LIGHTSPEED)
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.setText(df_c.format(mvspeed.x2/CalcCode.LIGHTSPEED)+" c");
			else
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.setText(df_s.format(mvspeed.x2)+" m/s");
		} if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact) {
			if(mvspeed.x3 >= 0.1*CalcCode.LIGHTSPEED) {
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.setText(df_c.format(mvspeed.x3/CalcCode.LIGHTSPEED)+" c");
			}
			else 
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.setText(df_s.format(mvspeed.x3)+" m/s");
		} if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Speedabs) {
			if(mp.getSpeed() >= 0.1*CalcCode.LIGHTSPEED)
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedabs.setText(df_c.format(mp.getSpeed()/CalcCode.LIGHTSPEED)+" c");
			else
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedabs.setText(df_s.format(mp.getSpeed())+" m/s");
		} if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Mass) 
			myView.pa_computetab.pa_compute_dataeasy.tf_Mass.setText(InterpretInput.niceInput_Mass(mp.getAbsMass(), myView.myXMLParser, df_s));
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Radius) 
			myView.pa_computetab.pa_compute_dataeasy.tf_Radius.setText(InterpretInput.niceInput_Length(mp.getAbsRadius(), myView.myXMLParser, df_s));
		if(object != myView.pa_computetab.pa_compute_dataeasy.tf_Dense)	{
			if(mp.isBlackHole())
				myView.pa_computetab.pa_compute_dataeasy.tf_Dense.setText(myView.myXMLParser.getText(6));
			else
				myView.pa_computetab.pa_compute_dataeasy.tf_Dense.setText(InterpretInput.niceInput_Density(mp.getDensity(), myView.myXMLParser, df_d));
		} if(object != myView.pa_computetab.pa_compute_dataeasy.sl_Speed) 
			myView.pa_computetab.pa_compute_dataeasy.sl_Speed.setValue((int)(100*Math.pow(mp.getSpeed()/CalcCode.LIGHTSPEED, 1.0/CalcCode.SSPEEDCONST)));
		if(object != myView.pa_computetab.pa_compute_dataeasy.sl_Mass) 
			myView.pa_computetab.pa_compute_dataeasy.sl_Mass.setValue((int)(Math.pow(mp.getAbsMass(),1.0/CalcCode.SMASSCONST)));
		if(object != myView.pa_computetab.pa_compute_dataeasy.sl_Radius)
			myView.pa_computetab.pa_compute_dataeasy.sl_Radius.setValue((int)(Math.pow(mp.getAbsRadius(),1.0/CalcCode.SRADIUSCONST)));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Coordx) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Coordx.setText(InterpretInput.niceInput_Length(mvcoord.x1, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Coordy) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Coordy.setText(InterpretInput.niceInput_Length(mvcoord.x2, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Coordz) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Coordz.setText(InterpretInput.niceInput_Length(mvcoord.x3, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Speedabs) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Speedabs.setText(InterpretInput.niceInput_Speed(mvspeed.abs(), myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Speedx_exact) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Speedx_exact.setText(InterpretInput.niceInput_Speed(mvspeed.x1, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Speedy_exact) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Speedy_exact.setText(InterpretInput.niceInput_Speed(mvspeed.x2, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Speedz_exact) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Speedz_exact.setText(InterpretInput.niceInput_Speed(mvspeed.x3, myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Mass) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Mass.setText(InterpretInput.niceInput_Mass(mp.getAbsMass(), myView.myXMLParser, null));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Radius) 
			myView.pa_computetab.pa_compute_dataadvanced.tf_Radius.setText(InterpretInput.niceInput_Length(mp.getAbsRadius(), myView.myXMLParser));
		if(object != myView.pa_computetab.pa_compute_dataadvanced.tf_Dense) {
			if(mp.isBlackHole())
				myView.pa_computetab.pa_compute_dataadvanced.tf_Dense.setText(myView.myXMLParser.getText(6));	//inf
			else
				myView.pa_computetab.pa_compute_dataadvanced.tf_Dense.setText(InterpretInput.niceInput_Density(mp.getDensity(), myView.myXMLParser, null));
		}

		myView.pa_computetab.sl_zoomlevel.addChangeListener(this);		
		myView.pa_computetab.pa_compute_dataeasy.sl_Mass.addChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Speed.addChangeListener(this);
		myView.pa_computetab.pa_compute_dataeasy.sl_Radius.addChangeListener(this);
		
		if(mp.isBlackHole())
			setBHVisible(true);
		else if(isBHVisible())
			setBHVisible(false);
		
		
		
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
		if(object == myView.pa_computetab.pa_compute_dataeasy.sl_Speed) { 
			updateComputePanels(GetSelectedMasspoint(), object);
		}
		if(object == myView.pa_computetab.pa_compute_dataeasy.sl_Mass) {
			myView.pa_computetab.pa_compute_dataeasy.tf_Mass.setText(InterpretInput.niceInput_Mass(mp.getAbsMass(), myView.myXMLParser, null));
		}
		if(object == myView.pa_computetab.pa_compute_dataeasy.sl_Radius) {
			myView.pa_computetab.pa_compute_dataeasy.tf_Radius.setText(InterpretInput.niceInput_Length(mp.getAbsRadius(), myView.myXMLParser, null));		
		}			
		if(mp.isBlackHole()) 
			setBHVisible(true);
		else if(isBHVisible()) 
			setBHVisible(false);
		
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

	private void setBHVisible(boolean b) {
		myView.pa_computetab.pa_compute_dataeasy.la_Blackhole.setVisible(b);
		myView.pa_computetab.pa_compute_dataadvanced.la_Blackhole.setVisible(b);
	}
	private boolean isBHVisible() {
		if(myView.pa_computetab.pa_compute_dataeasy.la_Blackhole.isVisible() ||
				myView.pa_computetab.pa_compute_dataadvanced.la_Blackhole.isVisible())
			return true;
		else
			return false;
	}

	public Vector<Masspoint> getVMasspoints() {
		debugout("getVMasspoints() - collecting. Size: "+ vmasspoints.size());
		return vmasspoints;
	}
	
	public void ThreadFinished(Vector<Masspoint> vmps, int error) {
    	debugout("Thread Finished()");
		if(vmps != null) {
			String finish = "";
			if(error == CalcCode.UNKNOWN) {
		    	debugout("Thread Finished() - error=UNKNOWN!");
				finish += myView.myXMLParser.getText(160)+"\n";
			} else if(error == CalcCode.NOERROR) {
				debugout("ThreadFinished() - finished without error");
			} else if(error == CalcCode.DOUBLELIMIT) {
		    	debugout("Thread Finished() - error=DOUBLELIMIT!");
				finish +=   myView.myXMLParser.getText(161)+"\n"
							+myView.myXMLParser.getText(162)+"\n";
			} else if(error == CalcCode.LONGLIMIT) {
		    	debugout("Thread Finished() - error=LONGLIMIT!");
				finish += myView.myXMLParser.getText(163)+"\n";
			} else if(error == CalcCode.LIGHTSPEEDERROR ) {
		    	debugout("Thread Finished() - error=LIGHTSPEEDERROR!");
				finish += myView.myXMLParser.getText(164)+"\n";
			}
	
			if(flagschwarzschild == true) {
		    	debugout("Thread Finished() - flagschwarzschild!");
				finish +=   myView.myXMLParser.getText(165)+"\n"
							+myView.myXMLParser.getText(166)+"\n";
			}
			
			if(flagcalc == false) {
		    	debugout("Thread Finished() - !flagcalc");
				finish = myView.myXMLParser.getText(167) + finish;
				JOptionPane.showMessageDialog(myView,finish,myView.myXMLParser.getText(172),JOptionPane.INFORMATION_MESSAGE);
			}
			else {
		    	debugout("Thread Finished() - flagcalc!");
				finish = myView.myXMLParser.getText(168) + finish;
				JOptionPane.showMessageDialog(myView,finish,myView.myXMLParser.getText(171),JOptionPane.INFORMATION_MESSAGE);
			}
		
			//myView.pa_visualtab.pa_visual_contrtab.la_curloaded.setText(fpInputFile.getName());
			vmasspoints.removeAllElements();
			vmasspoints.addAll(vmps);
			myView.pa_computetab.cb_Objects.removeAllItems();
			for(int i=0;i<vmasspoints.size();i++) {
				myView.pa_computetab.cb_Objects.addItem(vmasspoints.get(i));
			}
			myView.pa_computetab.ButtonsStd();
			id = vmasspoints.get(vmasspoints.size()-1).getID();
			Masspoint mp = vmasspoints.get(vmasspoints.size()-1);
			myView.pa_computetab.cb_Objects.setSelectedItem(mp);
			updateComputePanels(mp, null);
		}
		
    	myView.pa_computetab.ButtonsStd();
		myView.pa_computetab.ov_front.addMouseListener(this);
		myView.pa_computetab.ov_top.addMouseListener(this);
		myView.pa_computetab.ov_front.addMouseMotionListener(this);
		myView.pa_computetab.ov_top.addMouseMotionListener(this);
		myView.pa_computetab.ov_front.addMouseWheelListener(this);
		myView.pa_computetab.ov_top.addMouseWheelListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.addMouseListener(this);
		myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.addMouseListener(this);
	}
	
	public void ThreadFinished_Console(int error) {
    	debugout("Thread Finished()");
    	XMLParser myXMLParser = new XMLParser(View.textfile,49);
    	
		String finish = "";
		if(error == CalcCode.UNKNOWN) {
		   	debugout("Thread Finished() - error=UNKNOWN!");
			finish += myXMLParser.getText(160)+" ";
		} else if(error == CalcCode.NOERROR) {
			debugout("ThreadFinished() - finished without error");
		} else if(error == CalcCode.DOUBLELIMIT) {
		   	debugout("Thread Finished() - error=DOUBLELIMIT!");
			finish +=   myXMLParser.getText(161)+" "
						+myXMLParser.getText(162)+" ";
		} else if(error == CalcCode.LONGLIMIT) {
		   	debugout("Thread Finished() - error=LONGLIMIT!");
			finish += myXMLParser.getText(163)+" ";
		} else if(error == CalcCode.LIGHTSPEEDERROR ) {
		   	debugout("Thread Finished() - error=LIGHTSPEEDERROR!");
			finish += myXMLParser.getText(164)+" ";
		}
				
		if(flagschwarzschild == true) {
	    	debugout("Thread Finished() - flagschwarzschild!");
			finish +=   myXMLParser.getText(165)+" "
						+myXMLParser.getText(166)+" ";
		}
			
		if(flagcalc == false) {
		   	debugout("Thread Finished() - !flagcalc");
			finish = myXMLParser.getText(167) + finish;
			System.out.println(finish);
		} else {
		   	debugout("Thread Finished() - flagcalc!");
			finish = myXMLParser.getText(168) + finish;
			System.out.println(finish);
		}
		
		myModel.saveOutputfile();
	}
	
	public void CalculationFinished(int error) {
		if(myView_CalcProgress != null)
			myView_CalcProgress.setVisible(false);
		
		if(error == CalcCode.NOERROR) {
			JOptionPane.showMessageDialog(myView, myView.myXMLParser.getText(169), myView.myXMLParser.getText(170),JOptionPane.INFORMATION_MESSAGE);
		
			//Get the latest step of the c++ temp file and show it
			if( isError(myModel.loadDataset(new File( Model.FILE_TEMP ), -1)) ) {
				//Error, remove everything. Copied from b_reset
				for(int i=vmasspoints.size()-1;i>=0;i--) {
					RemoveMp(vmasspoints.get(i));
				}
				myView.pa_visualtab.displayStep(null);
				myView.pa_visualtab.setPlayControlsEnabled(false);
				myView.pa_visualtab.enableCounter(false);
				myView.pa_computetab.cb_mpdefaults.setSelectedIndex(0);
			}
			else {	/* no error detected ... continue */
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
				id = vmasspoints.get(vmasspoints.size()-1).getID();
				Masspoint mp = vmasspoints.get(vmasspoints.size()-1);
				myView.pa_computetab.cb_Objects.setSelectedItem(mp);
				updateComputePanels(mp, null);
				myView.pa_computetab.ov_front.addMouseListener(this);
				myView.pa_computetab.ov_top.addMouseListener(this);
				myView.pa_computetab.ov_front.addMouseMotionListener(this);
				myView.pa_computetab.ov_top.addMouseMotionListener(this);
				myView.pa_computetab.ov_front.addMouseWheelListener(this);
				myView.pa_computetab.ov_top.addMouseWheelListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.addMouseListener(this);
				myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.addMouseListener(this);
			}
		}
		else {		
			JOptionPane.showMessageDialog(myView, myView.myXMLParser.getText(167), myView.myXMLParser.getText(172),JOptionPane.INFORMATION_MESSAGE);
			myView.pa_computetab.ButtonsStd();
			id = vmasspoints.get(vmasspoints.size()-1).getID();
			Masspoint mp = vmasspoints.get(vmasspoints.size()-1);
			myView.pa_computetab.cb_Objects.setSelectedItem(mp);
			updateComputePanels(mp, null);
			myView.pa_computetab.ov_front.addMouseListener(this);
			myView.pa_computetab.ov_top.addMouseListener(this);
			myView.pa_computetab.ov_front.addMouseMotionListener(this);
			myView.pa_computetab.ov_top.addMouseMotionListener(this);
			myView.pa_computetab.ov_front.addMouseWheelListener(this);
			myView.pa_computetab.ov_top.addMouseWheelListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedx_exact.addMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedy_exact.addMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.tf_Speedz_exact.addMouseListener(this);
			myView.pa_computetab.pa_compute_dataeasy.pa_comp_speed_exact.addMouseListener(this);
		}
	}
	
	public void RemoveMp(Masspoint mp) {
		//check if all objects are going to be removed
    	if(myView.pa_computetab.cb_Objects.getItemCount()<=1) {
    		myView.pa_computetab.cb_Objects.addItem(myView.myXMLParser.getText(240));
    		myView.pa_computetab.ButtonsStart();
    		id = 0;
    	}
    	if(mySpeedVector != null && mySpeedVector.masspoint.getID() == mp.getID()) {
    		mySpeedVector.dispose();
    		mySpeedVector = null;
    	}
    	
    	mp.remove();
    	vmasspoints.remove(mp);
    	myView.pa_computetab.cb_Objects.removeItem(mp);
    	myView.pa_computetab.cb_Objects.repaint();
    	
    	if(vmasspoints.size() > 0) {
    		id = vmasspoints.get(vmasspoints.size()-1).getID();
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
	
	public Masspoint addMasspoint(int x, int y, int z, Masspoint mp_clone) {
		debugout("Point added at Position ("+x+"/"+y+"/"+z+")");
		if(id == 0) {
			myView.pa_computetab.cb_Objects.removeAllItems();
			myView.pa_computetab.ButtonsStd();
		}
		id++;
		//debugout("Controller() - Adding data for object "+id);
		
		for(int i=0; i<vmasspoints.size(); i++) {
			Masspoint mptemp = (Masspoint)vmasspoints.get(i);
			if(mptemp.getID() >= id)
				id = mptemp.getID() + 1;
		}
		
		long lx = pxtomm(x, 'x');
		long ly = pxtomm(y, 'y');
		long lz = pxtomm(z, 'z');
		Masspoint mp;
		
		if(mp_clone == null)
			mp = new Masspoint(id,lx,ly,lz);
		else
			mp = new Masspoint(id,lx,ly,lz,mp_clone);
			
		debugout("addmasspoint() - x1="+lx+", x2="+ly+", x3="+lz);	
		
		//(default)//Masspoint mp = new Masspoint(id, x, y, z);
		vmasspoints.add(mp);
		myView.pa_computetab.cb_Objects.addItem(mp);
		myView.pa_computetab.cb_Objects.setSelectedItem(mp);
		myView.pa_computetab.repaintViews();
		return mp;
	}
	
	public Masspoint checkforHit(int a, int b, String axe) {
		int x,y,z;
		
		if(axe == myView.myXMLParser.getText(111)) 
		{	x=a; y=b; z=0; }
		else if(axe == myView.myXMLParser.getText(112))
		{	x=a; y=0; z=b; }
		else /*if(axe == side)*/
		{	x=0; y=a; z=b; }
		
		long lx = pxtomm(x, 'x');
		long ly = pxtomm(y, 'y');
		long lz = pxtomm(z, 'z');
		
		for(int i=0;i<vmasspoints.size();i++) {
			Masspoint mptemp = (Masspoint)vmasspoints.get(i);
			MLVector mlvcoord = mptemp.getPos();
			long relradius = MVMath.ConvertToL(mptemp.getRadius());
						
			if(axe == myView.myXMLParser.getText(111)) {
				//debugout("CheckForHit() - Checking for hit at x="+lx+"; y="+ly+"; radius="+relradius);
				debugout("CheckForHit() - Checking for object "+mptemp.getID()+" at "+lx+"<"+(mlvcoord.x1+relradius)+"; "+lx+">"+(mlvcoord.x1-relradius));
				if(lx < (mlvcoord.x1+relradius) && lx > (mlvcoord.x1-relradius)) {
					debugout("CheckForHit() - Checking for object at "+ly+"<"+(mlvcoord.x2+relradius)+"; "+ly+">"+(mlvcoord.x2-relradius));
					if(ly < (mlvcoord.x2+relradius) && ly > (mlvcoord.x2-relradius)) {
						return mptemp;
					}
				}
			}
			else if(axe == myView.myXMLParser.getText(112)) {
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
		
		ObjectView2D ov_front = myView.pa_computetab.ov_front;
		ObjectView2D ov_top = myView.pa_computetab.ov_top;
		
		double centerX = ov_front.getWidth()/2;
		double centerY = ov_front.getHeight()/2;
		double centerZ = ov_top.getHeight()/2;
		
		
		switch(axe) {
			case 'x': 
				//debugout("pxtomm() - getCoordOffsetX()="+ov_front.getCoordOffsetX());
				double dx = (a-centerX);
				dx /= ov_front.iGridOffset;
				dx *= Math.pow(10, ov_front.iZoomLevel);
				dx -= ov_front.getCoordOffsetX();
				return MVMath.ConvertToL(dx);
			case 'y': 
				//debugout("pxtomm() - getCoordOffsetY()="+ov_front.getCoordOffsetY());
				double dy = (a-centerY);
				dy /= ov_front.iGridOffset;
				dy *= Math.pow(10, ov_front.iZoomLevel);
				dy += ov_front.getCoordOffsetY();
				return -MVMath.ConvertToL(dy);
			case 'z': 
				//debugout("pxtomm() - getCoordOffsetZ()="+ov_front.getCoordOffsetZ());
				double dz = (a-centerZ);
				dz /= ov_front.iGridOffset;
				dz *= Math.pow(10, ov_front.iZoomLevel);
				dz += ov_front.getCoordOffsetZ();
				return -MVMath.ConvertToL(dz); 
			default: 
				debugout("pxtomm() - ERROR"); 
				return 0;
		}
	}
	
	/**
	 * @return
	 */
	public boolean[] CheckInput() {
		Masspoint mp = GetSelectedMasspoint();
		String strtemp;
		String[] stratemp;
		double dtemp = 0.0;
		double dfactor = 1.0;
		boolean[] bacheck = new boolean[8];
		
		if(mp == null) {
			debugout("CheckInput() - Error");
			return null;
		}
		
		/////////////////MASS/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Mass.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			stratemp = strtemp.split(" ");
			strtemp = stratemp[0];
			try {
				dfactor = 1.0;
				if(stratemp.length > 1) {
					dfactor = InterpretInput.checkInput_Mass(stratemp[1], myView.myXMLParser);
					if(dfactor == 0) {
						debugout("CheckInput() - tf_Mass[1]:"+stratemp[1]);
						bacheck[0] = true;
					}
					dtemp = Double.parseDouble(strtemp);
				}
				if(stratemp.length <= 1) {
					dfactor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[0] = true;
			}
			if(bacheck[0] == false)
				mp.setMass(dtemp*dfactor);
		}
		
		
		/////////////////RADIUS/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Radius.getText();	
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			stratemp = strtemp.split(" ");
			strtemp = stratemp[0];		
			try {
				dfactor = 1.0;
				if(stratemp.length > 1) {
					dfactor = InterpretInput.checkInput_Length(stratemp[1], myView.myXMLParser);
					if(dfactor == 0) {
						debugout("CheckInput() - setRadius[1]:"+stratemp[1]);
						bacheck[1] = true;
					}
					dtemp = Double.parseDouble(strtemp);
				}
				if(stratemp.length <= 1) {
					dfactor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[1] = true;
			}
			if(bacheck[1] == false)
				mp.setAbsRadius(dtemp*dfactor);
		}
		//////////////////SPEEDX//////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Speedx_exact.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			stratemp = strtemp.split(" ");
			strtemp = stratemp[0];
			try {
				dfactor = 1.0;
				if(stratemp.length > 1) {
					dfactor = InterpretInput.checkInput_Speed(stratemp[1], myView.myXMLParser);
					if(dfactor == 0) {
						bacheck[2] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					dtemp = Double.parseDouble(strtemp);
				}
				else if(stratemp.length <= 1) {
					dfactor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[2] = true;
			}
			if(bacheck[2] == false) {
				if(!mp.setSpeedx(dtemp*dfactor)) {
					debugout("CheckInput() - complete Speed1 failed");
					bacheck[2] = true;
					bacheck[3] = true;
					bacheck[4] = true;
				}
			}
		}
		/////////////////SPEEDY/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Speedy_exact.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			stratemp = strtemp.split(" ");
			strtemp = stratemp[0];
			try {
				dfactor = 1.0;
				if(stratemp.length > 1) {
					dfactor = InterpretInput.checkInput_Speed(stratemp[1], myView.myXMLParser);
					if(dfactor == 0) {
						bacheck[3] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					dtemp = Double.parseDouble(strtemp);
				}
				else if(stratemp.length <= 1) {
					dfactor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[3] = true;
			}
			if(bacheck[3] == false) {
				if(!mp.setSpeedy(dtemp*dfactor)) {
					debugout("CheckInput() - complete Speed2 failed");
					bacheck[2] = true;
					bacheck[3] = true;
					bacheck[4] = true;
				}
			}
		}

		/////////////////SPEEDZ/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Speedz_exact.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			stratemp = strtemp.split(" ");
			strtemp = stratemp[0];
			try {
				dfactor = 1.0;
				if(stratemp.length > 1) {
					dfactor = InterpretInput.checkInput_Speed(stratemp[1], myView.myXMLParser);
					if(dfactor == 0) {
						bacheck[4] = true;
						debugout("CheckInput() - tf_Speedz[1]:"+stratemp[1]);
					}
					dtemp = Double.parseDouble(strtemp);
				}
				else if(stratemp.length <= 1) {
					dfactor = 1.0;
					dtemp = Double.parseDouble(strtemp);
				}
			} catch(NumberFormatException e) {
				bacheck[4] = true;
			}
			if(bacheck[4] == false) {
				if(!mp.setSpeedz(dtemp*dfactor)) {
					debugout("CheckInput() - complete Speed3 failed");
					bacheck[2] = true;
					bacheck[3] = true;
					bacheck[4] = true;
				}
			}
		}
		/////////////////COORDX/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Coordx.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			bacheck[5] = bacheck_coords(strtemp, mp, 'x');
		}
		else
			bacheck[7] = false;
		/////////////////COORDY/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Coordy.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			bacheck[6] = bacheck_coords(strtemp, mp, 'y');
		}
		else
			bacheck[7] = false;

		/////////////////COORDZ/////////////////
		strtemp = myView.pa_computetab.pa_compute_dataadvanced.tf_Coordz.getText();
		if(strtemp != null && strtemp != "" && !strtemp.isEmpty()) {		
			strtemp = InterpretInput.stringCommataReplace(strtemp);
			bacheck[7] = bacheck_coords(strtemp, mp, 'z');
		}
		
		return bacheck;
	}
	

	public boolean bacheck_coords(String strtemp, Masspoint mp, char coord) {
		String[] stratemp = strtemp.split(" ");
		strtemp = stratemp[0];
		double dfactor = 1.0;
		double dtemp = 1.0;
		long lfactor = 1;
		long ltemp = 1;
		boolean[] basubcheck = new boolean[2];

		dfactor = 1.0;
		try {
			if(stratemp.length > 1) {
				lfactor = InterpretInput.checkInput_Length_Long(stratemp[1], myView.myXMLParser);
				if(lfactor == 0) {
					debugout("bacheck_coords() - Long:"+stratemp[1]);
					basubcheck[0] = true;
				}
				ltemp = Long.parseLong(strtemp);
			}
			if(stratemp.length <= 1) {
				lfactor = 1;
				ltemp = Long.parseLong(strtemp);
			}
		} catch(NumberFormatException e) {
			basubcheck[0] = true;
			debugout("bacheck_coords() - Long:"+(stratemp.length > 1 ? stratemp[1]:"stratemp[1]")+", e="+e);
		}		
		try {
			if(stratemp.length > 1) {
				dfactor = InterpretInput.checkInput_Length(stratemp[1], myView.myXMLParser);
				if(dfactor == 0) {
					debugout("bacheck_coords() - Double:"+stratemp[1]);
					basubcheck[1] = true;
				}
				dtemp = Double.parseDouble(strtemp);
			}
			if(stratemp.length <= 1) {
				lfactor = 1;
				dtemp = Double.parseDouble(strtemp);
			}
		} catch(NumberFormatException e) {
			basubcheck[1] = true;
			debugout("bacheck_coords() - Double:"+(stratemp.length > 1 ? stratemp[1]:"stratemp[1]")+", e="+e);
		}
		
		if (!basubcheck[0]) {
			switch(coord) {
			case 'x': mp.setCoordx(ltemp*lfactor); break;
			case 'y': mp.setCoordy(ltemp*lfactor); break;
			case 'z': mp.setCoordz(ltemp*lfactor); break;
			default: return true;
			}
			debugout("bacheck_coords() - setCoord"+coord+"() - ltemp="+ltemp+", lfactor="+lfactor);
			return false;
		}
		else if (!basubcheck[1]) {
			switch(coord) {
			case 'x': mp.setCoordx(MVMath.ConvertToL(dtemp*dfactor)); break;
			case 'y': mp.setCoordy(MVMath.ConvertToL(dtemp*dfactor)); break;
			case 'z': mp.setCoordz(MVMath.ConvertToL(dtemp*dfactor)); break;
			default: return true;
			}
			debugout("bacheck_coords() - setCoord"+coord+"() - dtemp="+dtemp+", dfactor="+dfactor);
			return false;
		}
		else
			return true;
		
	}

	public static String getCurrentFolder() {
		return System.getProperty("user.dir").toString();
	}

	public boolean isEditing() {
		return flagedit;		
	}
	public void setEditing(boolean b) {
		flagedit = b;		
	}

	public void startCalcProgress(int i) {
		myView_CalcProgress = new View_CalcProgress(i, this);
	}
	
	private boolean isError(int error) {
		if (error == Model.INFILE_NOERROR)
			return false;
		else if (error == Model.INFILE_EOFSTARTERROR) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n"	+ myView.myXMLParser.getText(151), 
					myView.myXMLParser.getText(158),
					JOptionPane.ERROR_MESSAGE);
		} else if (error == Model.INFILE_EOFSTEPERROR) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n" + myView.myXMLParser.getText(152) + "\n" + myView.myXMLParser.getText(153),
					myView.myXMLParser.getText(150),
					JOptionPane.ERROR_MESSAGE);
		} else if (error == Model.INFILE_EOFOBJERROR) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n"	+ myView.myXMLParser.getText(152) + "\n" + myView.myXMLParser.getText(154),
					myView.myXMLParser.getText(150),
					JOptionPane.ERROR_MESSAGE);
		} else if (error == Model.INFILE_FILENOTFOUND) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n"	+ myView.myXMLParser.getText(155),
					myView.myXMLParser.getText(155),
					JOptionPane.ERROR_MESSAGE);
		} else if (error == Model.INFILE_READERROR) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n"	+ myView.myXMLParser.getText(157),
					myView.myXMLParser.getText(156),
					JOptionPane.ERROR_MESSAGE);
		} else if (error == Model.INFILE_WPTERROR) {
			JOptionPane.showMessageDialog(myView,
					myView.myXMLParser.getText(150) + ":\n"	+ String.format(myView.myXMLParser.getText(180), WPT_VERSION),
					myView.myXMLParser.getText(158),
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}
}

