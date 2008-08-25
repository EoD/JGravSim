package gravity_sim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class TabVisualisationControls extends JPanel {
	private static final long serialVersionUID = 1L;

	private int iCurFrame = 0;
	private double dCurTime = 0;
	private int iMaxFrame;
	private double dMaxTime;
	private double dstep;
	
	JPanel pa_vis_controls; /* Kontrollelemente für die Visualisierung */
	JPanel pa_vis_contr_top;
		JButton b_loadfile = new JButton("Datei laden");
		JButton b_loadlast = new JButton("letzte Berechnung laden");
		JLabel la_curloaded = new JLabel("Keine Datei geladen");
		
	JPanel pa_vis_contr_center;
		JPanel pa_vis_contr_center_panel1;
			JLabel la_fpschooser;
			JSpinner sp_fpschooser;
		JPanel pa_vis_contr_center_paneltime;
			JLabel la_mscounter;
			JLabel la_scounter;
			JLabel la_mincounter;
			JLabel la_hcounter;
			JLabel la_dcounter;
			JLabel la_weekcounter;
			JLabel la_monthcounter;
			JLabel la_yearcounter;
			JLabel la_decadecounter;
			JLabel la_centurycounter;
		JPanel pa_vis_contr_center_panel2;
			JLabel la_zoomlevel;
			JSlider sl_zoomlevel;
		JPanel pa_vis_contr_center_panel3;
			JLabel la_gridoffset;
			JSlider sl_gridoffset;

	JPanel pa_vis_contr_bottom;
		JPanel pa_vis_contr_bottom_row1;
			JLabel la_framecounter;
	
		JPanel pa_vis_contr_bottom_row2;
			JSlider sl_playcontr_slider;
		JPanel pa_vis_contr_bottom_row3;
			JButton b_playcontr_start = new JButton("<<");
			JButton b_playcontr_stepback = new JButton("<");
			JButton b_playcontr_play = new JButton("Start");
			JButton b_playcontr_stop = new JButton("Stop");
			JButton b_playcontr_stepforw = new JButton(">");
			JButton b_playcontr_end = new JButton(">>");
		JPanel pa_vis_contr_bottom_row4;
			JButton b_colorch_grid = new JButton("Rasterfarbe");
			JButton b_colorch_speedvec = new JButton("v-Vektor Farbe");
			JButton b_resetoffset = new JButton("Verschiebung zurücksetzen");

	//ObjectView ov_top; /* Ansicht von Oben (x,y) */
	//ObjectView ov_front; /* Ansicht von Vorne (x,z) */
	//ObjectView ov_right; /* Ansicht von Rechts (y,z) */	
	
	public TabVisualisationControls(ObjectView ovright) {
		setLayout(new GridLayout(0,1));
		
		pa_vis_controls = new JPanel(new BorderLayout());
		pa_vis_controls.setBackground(Color.white);
		pa_vis_contr_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_top.setBackground(Color.white);

		pa_vis_contr_top.add(b_loadfile);
		pa_vis_contr_top.add(b_loadlast);
		pa_vis_contr_top.add(la_curloaded);
		
		pa_vis_contr_center = new JPanel(new GridLayout(0,1));
		pa_vis_contr_center.setBackground(Color.white);
		
		pa_vis_contr_center_panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_center_panel1.setBackground(Color.white);
		
		SpinnerNumberModel spmodel = new SpinnerNumberModel(25,1,100,1);
		sp_fpschooser = new JSpinner(spmodel);
		pa_vis_contr_center_panel1.add(sp_fpschooser);
		
		la_fpschooser = new JLabel("FPS");
		pa_vis_contr_center_panel1.add(la_fpschooser);

		// TODO check
		pa_vis_contr_center_paneltime = new JPanel(new GridLayout(2,5));
		la_mscounter = new JLabel();
		la_scounter = new JLabel();
		la_mincounter = new JLabel();
		la_hcounter = new JLabel();
		la_dcounter = new JLabel();
		la_weekcounter = new JLabel();
		la_monthcounter = new JLabel();
		la_yearcounter = new JLabel();
		la_decadecounter = new JLabel();
		la_centurycounter = new JLabel();
		pa_vis_contr_center_paneltime.add(la_centurycounter);
		pa_vis_contr_center_paneltime.add(la_decadecounter);
		pa_vis_contr_center_paneltime.add(la_yearcounter);
		pa_vis_contr_center_paneltime.add(la_monthcounter);
		pa_vis_contr_center_paneltime.add(la_weekcounter);
		pa_vis_contr_center_paneltime.add(la_dcounter);
		pa_vis_contr_center_paneltime.add(la_hcounter);
		pa_vis_contr_center_paneltime.add(la_mincounter);
		pa_vis_contr_center_paneltime.add(la_scounter);
		pa_vis_contr_center_paneltime.add(la_mscounter);
		pa_vis_contr_center_paneltime.setBackground(Color.white);
		
		pa_vis_contr_center_panel2 = new JPanel(new GridLayout(2,0));
		pa_vis_contr_center_panel2.setBackground(Color.white);
		la_zoomlevel = new JLabel("Vergrößerung: ");
		sl_zoomlevel = new JSlider(10,162,130);
		sl_zoomlevel.setPaintTicks(true);
		sl_zoomlevel.setMinorTickSpacing(10);
		sl_zoomlevel.setMajorTickSpacing(50);
		sl_zoomlevel.setBackground(Color.white);
		
		pa_vis_contr_center_panel2.add(la_zoomlevel);
		pa_vis_contr_center_panel2.add(sl_zoomlevel);
		
		pa_vis_contr_center_panel3 = new JPanel(new GridLayout(2,0));
		pa_vis_contr_center_panel3.setBackground(Color.white);
		la_gridoffset = new JLabel("Rastergröße: ");
		sl_gridoffset = new JSlider(15,200,25);
		sl_gridoffset.setPaintTicks(true);
		sl_gridoffset.setMinorTickSpacing(10);
		sl_gridoffset.setMajorTickSpacing(50);
		sl_gridoffset.setBackground(Color.white);
		pa_vis_contr_center_panel3.add(la_gridoffset);
		pa_vis_contr_center_panel3.add(sl_gridoffset);
				
		pa_vis_contr_center.add(pa_vis_contr_center_panel1);
		pa_vis_contr_center.add(pa_vis_contr_center_paneltime);
		pa_vis_contr_center.add(pa_vis_contr_center_panel2);
		pa_vis_contr_center.add(pa_vis_contr_center_panel3);
		
		pa_vis_contr_bottom = new JPanel(new GridLayout(4,0));
		pa_vis_contr_bottom.setBackground(Color.white);
		
		pa_vis_contr_bottom_row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_bottom_row1.setBackground(Color.white);
		la_framecounter = new JLabel();
		pa_vis_contr_bottom_row1.add(la_framecounter);
		
		pa_vis_contr_bottom_row2 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row2.setBackground(Color.white);
		sl_playcontr_slider = new JSlider(0,749,0);
		sl_playcontr_slider.setPaintTicks(true);
		sl_playcontr_slider.setMinorTickSpacing(10);
		sl_playcontr_slider.setMajorTickSpacing(100);
		sl_playcontr_slider.setBackground(Color.white);
		pa_vis_contr_bottom_row2.add(sl_playcontr_slider);
				
		pa_vis_contr_bottom_row3 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row3.add(b_playcontr_start);
		pa_vis_contr_bottom_row3.add(b_playcontr_stepback);
		pa_vis_contr_bottom_row3.add(b_playcontr_play);
		pa_vis_contr_bottom_row3.add(b_playcontr_stop);
		pa_vis_contr_bottom_row3.add(b_playcontr_stepforw);
		pa_vis_contr_bottom_row3.add(b_playcontr_end);
		
		pa_vis_contr_bottom_row4 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row4.add(b_colorch_grid);
		pa_vis_contr_bottom_row4.add(b_colorch_speedvec);
		pa_vis_contr_bottom_row4.add(b_resetoffset);
		
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row1);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row2);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row3);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row4);
		
		pa_vis_controls.add(pa_vis_contr_top,BorderLayout.NORTH);
		pa_vis_controls.add(pa_vis_contr_center,BorderLayout.CENTER);
		pa_vis_controls.add(pa_vis_contr_bottom,BorderLayout.SOUTH);
		
		
		//ov_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Oben"));
		pa_vis_controls.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Steuerung"));
		//ov_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Vorne"));
		//ov_right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Rechts"));
		
		//add(ov_top);
		add(pa_vis_controls);
		//add(ov_front);
		//add(ov_right);

	}
	
/*	public void setGridColor(Color newColor) {
		ov_front.coGridColor = newColor;
		ov_right.coGridColor = newColor;
		ov_top.coGridColor = newColor;
		repaintViews();
	}
	
	public void setObjectColor(Color newColor) {
		ov_front.coSpeedvecColor = newColor;
		ov_right.coSpeedvecColor = newColor;
		ov_top.coSpeedvecColor = newColor;
		repaintViews();		
	}
	
	public void repaintViews() {
		ov_front.repaint();
		ov_right.repaint();
		ov_top.repaint();
	}
*/	
/*	public void setPlayControlsEnabled(boolean state) {
		sl_playcontr_slider.setEnabled(state);
		sp_fpschooser.setEnabled(state);
		b_playcontr_start.setEnabled(state);
		b_playcontr_end.setEnabled(state);
		b_playcontr_play.setEnabled(state);
		b_playcontr_stepback.setEnabled(state);
		b_playcontr_stepforw.setEnabled(state);
		b_playcontr_stop.setEnabled(state);
	}
*/	
/*	private void updateCurFrame() {
		ov_top.repaint();
		ov_front.repaint();
		ov_right.repaint();
	}
*/	
	public void setCurFrame(int frame) {
		sl_playcontr_slider.setValue(frame);
		
		iCurFrame = frame;
		dCurTime = ((double)frame)*dstep;
		updateFrameCounter();
	}
	
	public int getCurFrame() {
		return iCurFrame;
	}
	
	public int getMaxFrame() {
		return iMaxFrame;
	}
	
	public double getCurTime() {
		return dCurTime;
	}	
	
	public double getMaxTime() {
		return dMaxTime;
	}
	
	public void setTimeStep(double step) {
		dstep = step;
	}
	
	public void setMaxFrame(int maxframe) {
		sl_playcontr_slider.setMaximum(maxframe);
		
		iMaxFrame = maxframe;
		dMaxTime = ((double)maxframe)*dstep;
		updateFrameCounter();
	}
	
	public void updateFrameCounter() {
		la_framecounter.setText("Schritt "+iCurFrame+" von "+iMaxFrame+" anzeigen ");
		
		String unit = "ms";
		double factor = 1.0;
		double time = dCurTime;
		DecimalFormat df = new DecimalFormat("0");
		if(dCurTime == 0)
			time = dMaxTime;

		// TODO complete!
		//if(dCurTime < 1000.0*12.0*4.0*7.0*24.0*60.0*60.0*1000.0)
		unit = "Jahrhunderte";
		if(time >= 100.0*365.25*24.0*3600.0) {
			factor = 100.0*365.25*24.0*3600.0;
			la_centurycounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_centurycounter.setText(df.format(0)+" "+unit);

		unit = "Jahrzehnte";
		if(time >= 10.0*365.25*24.0*3600.0) {
			factor = 10.0*365.25*24.0*3600.0;
			la_decadecounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_decadecounter.setText(df.format(0)+" "+unit);
 		
		unit = "Jahre";
		if(time >= (365.25*24.0)*3600.0) {
			factor = (365.25*24.0)*3600.0;
			la_yearcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_yearcounter.setText(df.format(0)+" "+unit);

		unit = "Monate";
		if(time >= 365.25*24.0*3600.0/12.0) {
			factor = 365.25*24.0*3600.0/12.0;
			la_monthcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_monthcounter.setText(df.format(0)+" "+unit);

		unit = "Wochen";
		if(time >= 7.0*24.0*60.0*60.0) {
			factor = 7.0*24.0*60.0*60.0;
			la_weekcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_weekcounter.setText(df.format(0)+" "+unit);

		unit = "Tage";
		if(time >= 24.0*60.0*60.0) {
			factor = 24.0*60.0*60.0;
			la_dcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_dcounter.setText(df.format(0)+" "+unit);

		unit = "h";
		if(time >= 60.0*60.0) {
			factor = 60.0*60.0;
			la_hcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_hcounter.setText(df.format(0)+" "+unit);

		unit = "min";
		if(time >= 60.0) {
			factor = 60.0;
			la_mincounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_mincounter.setText(df.format(0)+" "+unit);

		unit = "s";
		if(time >= 1.0) {
			factor = 1.0;
			la_scounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_scounter.setText(df.format(0)+" "+unit);

		unit = "ms";
		df.applyPattern("0.######");
		if(time >= 1.0E-3) {
			//factor = 1.0;
			la_mscounter.setText(df.format(time)+" "+unit);
			//time = time % factor;
		}
		else
			la_mscounter.setText(df.format(0)+" "+unit);
		
		//la_timecounter.setText("Time: "+df.format(dCurTime/factor)+" "+unit+" of "+df.format(dMaxTime/factor)+" "+unit);
		
	}

/*	public void displayStep(Step nextStep) {
		ov_front.displayStep(nextStep);
		ov_right.displayStep(nextStep);
		ov_top.displayStep(nextStep);
	}
*/	
/*	public void setZoom(double zoomLevel) {
		ov_front.iZoomLevel = zoomLevel;
		ov_right.iZoomLevel = zoomLevel;
		ov_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));
		la_zoomlevel.setText("Vergrößerung: "+zoomLevel+" (1Unit = "+ df.format(zoomUnit) + "km)");
		updateCurFrame();
	}
	
	public void setGridOffset(int gridOffset) {
		ov_front.iGridOffset = gridOffset;
		ov_right.iGridOffset = gridOffset;
		ov_top.iGridOffset = gridOffset;
		
		la_gridoffset.setText("Rastergröße: "+gridOffset);
		updateCurFrame();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//System.out.println("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_front.dCoordOffsetX += deltaX;
		ov_top.dCoordOffsetX += deltaX;
		updateCurFrame();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//System.out.println("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_right.dCoordOffsetY += deltaY;
		ov_top.dCoordOffsetY -= deltaY;
		updateCurFrame();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//System.out.println("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_front.dCoordOffsetZ -= deltaZ;
		ov_right.dCoordOffsetZ -= deltaZ;
		updateCurFrame();
	}
*/	
/*	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_top.iZoomLevel))/ov_top.iGridOffset;
	}
*/	
/*	public void resetOffset() {
		ov_front.dCoordOffsetX = 0.0;
		ov_front.dCoordOffsetY = 0.0;
		ov_front.dCoordOffsetZ = 0.0;
		
		ov_right.dCoordOffsetX = 0.0;
		ov_right.dCoordOffsetY = 0.0;
		ov_right.dCoordOffsetZ = 0.0;
		
		ov_top.dCoordOffsetX = 0.0;
		ov_top.dCoordOffsetY = 0.0;
		ov_top.dCoordOffsetZ = 0.0;
		updateCurFrame();
	}
*/
	public void enableCounter(boolean b) {
		la_centurycounter.setVisible(b);
		la_decadecounter.setVisible(b);
		la_yearcounter.setVisible(b);
		la_monthcounter.setVisible(b);
		la_weekcounter.setVisible(b);
		la_dcounter.setVisible(b);
		la_hcounter.setVisible(b);
		la_mincounter.setVisible(b);
		la_scounter.setVisible(b);
		la_mscounter.setVisible(b);
		la_framecounter.setVisible(b);		
	}	


}
