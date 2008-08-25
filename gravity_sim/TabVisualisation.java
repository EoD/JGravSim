package gravity_sim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;


public class TabVisualisation extends JPanel  {
	private static final long serialVersionUID = 1L;

	//private int iCurFrame = 0;
	//private double dCurTime = 0;
	//private int iMaxFrame;
	//private double dMaxTime;
	//private double dstep;

	static final String strtop = "Oben";
	static final String strfront = "Vorne";
	static final String strright = "Rechts";

/*	ObjectView ov_top; // Ansicht von Oben (x,y) /
	ObjectView ov_front; // Ansicht von Vorne (x,z) /
	ObjectView ov_right; // Ansicht von Rechts (y,z) /	
*/	ObjectView ov_vis_top = new ObjectView(strtop,"xy");
	ObjectView ov_vis_front = new ObjectView(strfront,"xz");
	ObjectView ov_vis_right = new ObjectView(strright,"yz");
	
	//TabVisualisationControls pa_visual_contrtab;
	
	TabVisualisationData pa_visual_datatab;		
	TabVisualisationControls pa_visual_contrtab;
		
	JTabbedPane tp_tabs = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
	
	public TabVisualisation() {
		setLayout(new GridLayout(2,2));
		
		pa_visual_datatab = new TabVisualisationData(); /* Visualisierung berechneter Daten */
		pa_visual_contrtab = new TabVisualisationControls(ov_vis_right); /* Visualisierung berechneter Daten */
		
		ov_vis_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),strtop));
		ov_vis_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),strfront));
		ov_vis_right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),strright));
		
		tp_tabs.addTab("Kontrollen", pa_visual_contrtab);
		tp_tabs.addTab("Daten", pa_visual_datatab);
		
		add(ov_vis_top);
		add(tp_tabs);
		add(ov_vis_front);
		add(ov_vis_right);
		
		setPlayControlsEnabled(false);
	}
	
	public void setGridColor(Color newColor) {
		ov_vis_front.coGridColor = newColor;
		ov_vis_right.coGridColor = newColor;
		ov_vis_top.coGridColor = newColor;
		repaintViews();
	}
	
	public void setObjectColor(Color newColor) {
		ov_vis_front.coSpeedvecColor = newColor;
		ov_vis_right.coSpeedvecColor = newColor;
		ov_vis_top.coSpeedvecColor = newColor;
		repaintViews();		
	}
	
	public void repaintViews() {
		updateCurFrame();
	}
	
	public void setPlayControlsEnabled(boolean state) {
		pa_visual_contrtab.sl_playcontr_slider.setEnabled(state);
		pa_visual_contrtab.sp_fpschooser.setEnabled(state);
		pa_visual_contrtab.b_playcontr_start.setEnabled(state);
		pa_visual_contrtab.b_playcontr_end.setEnabled(state);
		pa_visual_contrtab.b_playcontr_play.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepback.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepforw.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stop.setEnabled(state);
		pa_visual_datatab.ButtonsDeactive(state);
	}
	
	private void updateCurFrame() {
		ov_vis_top.repaint();
		ov_vis_front.repaint();
		ov_vis_right.repaint();
		pa_visual_datatab.cb_Objects.removeAllItems();
		
		if(ov_vis_top.stCurrent != null && ov_vis_top.stCurrent.getMasspoints().length > 0) {
			for(int i=0;i<ov_vis_top.stCurrent.getMasspoints().length;i++)
				pa_visual_datatab.cb_Objects.addItem(ov_vis_top.stCurrent.getMasspoints()[i]);
			
			pa_visual_datatab.cb_Objects.setSelectedIndex(0);
		}
	}
	
	public void setCurFrame(int frame) {
		pa_visual_contrtab.sl_playcontr_slider.setValue(frame);
		
		pa_visual_contrtab.setCurFrame(frame);
		pa_visual_contrtab.updateFrameCounter();
	}
	
	public int getCurFrame() {
		return pa_visual_contrtab.getCurFrame();
	}
	
	public int getMaxFrame() {
		return pa_visual_contrtab.getMaxFrame();
	}
	
	public double getCurTime() {
		return pa_visual_contrtab.getCurTime();
	}	
	
	public double getMaxTime() {
		return pa_visual_contrtab.getMaxTime();
	}
	
	public void setTimeStep(double step) {
		pa_visual_contrtab.setTimeStep(step);
	}
	
	public void setMaxFrame(int maxframe) {
		pa_visual_contrtab.sl_playcontr_slider.setMaximum(maxframe);
		
		pa_visual_contrtab.setMaxFrame(maxframe);
		pa_visual_contrtab.updateFrameCounter();
	}
	
	public void displayStep(Step nextStep) {
		ov_vis_front.displayStep(nextStep);
		ov_vis_right.displayStep(nextStep);
		ov_vis_top.displayStep(nextStep);
		updateCurFrame();
	}
	
	public void setZoom(double zoomLevel) {
		ov_vis_front.iZoomLevel = zoomLevel;
		ov_vis_right.iZoomLevel = zoomLevel;
		ov_vis_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));
		pa_visual_contrtab.la_zoomlevel.setText("Vergr��erung: "+zoomLevel+" (1Unit = "+ df.format(zoomUnit) + "km)");
		updateCurFrame();
	}
	
	public void setGridOffset(int gridOffset) {
		ov_vis_front.iGridOffset = gridOffset;
		ov_vis_right.iGridOffset = gridOffset;
		ov_vis_top.iGridOffset = gridOffset;
		
		pa_visual_contrtab.la_gridoffset.setText("Rastergr��e: "+gridOffset);
		updateCurFrame();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//System.out.println("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_vis_front.dCoordOffsetX += deltaX;
		ov_vis_top.dCoordOffsetX += deltaX;
		updateCurFrame();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//System.out.println("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_vis_right.dCoordOffsetY += deltaY;
		ov_vis_top.dCoordOffsetY -= deltaY;
		updateCurFrame();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//System.out.println("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_vis_front.dCoordOffsetZ -= deltaZ;
		ov_vis_right.dCoordOffsetZ -= deltaZ;
		updateCurFrame();
	}
	
	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_vis_top.iZoomLevel))/ov_vis_top.iGridOffset;
	}
	
	public void resetOffset() {
		ov_vis_front.dCoordOffsetX = 0.0;
		ov_vis_front.dCoordOffsetY = 0.0;
		ov_vis_front.dCoordOffsetZ = 0.0;
		
		ov_vis_right.dCoordOffsetX = 0.0;
		ov_vis_right.dCoordOffsetY = 0.0;
		ov_vis_right.dCoordOffsetZ = 0.0;
		
		ov_vis_top.dCoordOffsetX = 0.0;
		ov_vis_top.dCoordOffsetY = 0.0;
		ov_vis_top.dCoordOffsetZ = 0.0;
		updateCurFrame();
	}

	public void enableCounter(boolean b) {
		pa_visual_contrtab.la_centurycounter.setVisible(b);
		pa_visual_contrtab.la_decadecounter.setVisible(b);
		pa_visual_contrtab.la_yearcounter.setVisible(b);
		pa_visual_contrtab.la_monthcounter.setVisible(b);
		pa_visual_contrtab.la_weekcounter.setVisible(b);
		pa_visual_contrtab.la_dcounter.setVisible(b);
		pa_visual_contrtab.la_hcounter.setVisible(b);
		pa_visual_contrtab.la_mincounter.setVisible(b);
		pa_visual_contrtab.la_scounter.setVisible(b);
		pa_visual_contrtab.la_mscounter.setVisible(b);
		pa_visual_contrtab.la_framecounter.setVisible(b);		
	}
}
