package jgravsim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;


public class TabVisualisation extends JPanel  {
	private static final long serialVersionUID = 1L;

	//private int iCurFrame = 0;
	//private double dCurTime = 0;
	private int data_plot;
	private int dynamic_iCurMax;
	private DynamicWPTLoader loader_plot;
	//private double dMaxTime;
	//private double dstep;

	ObjectView2D ov_vis_top = new ObjectView2D("xy");
	ObjectView2D ov_vis_front = new ObjectView2D("xz");
	ObjectView3D ov_vis_right = new ObjectView3D(null);//("yz");
	
	//TabVisualisationControls pa_visual_contrtab;
	
	TabVisualisationData pa_visual_datatab;		
	TabVisualisationControls pa_visual_contrtab;
	TabVisualisationPlot pa_visual_plottab;
	TabVisualisationOptions pa_visual_optiontab;
		
	JTabbedPane tp_visual = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
	XMLParser myXMLParser;
	
	public TabVisualisation(XMLParser parser) {
		myXMLParser = parser;
		setLayout(new GridLayout(2,2));
		
		pa_visual_datatab = new TabVisualisationData(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_contrtab = new TabVisualisationControls(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_plottab = new TabVisualisationPlot(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_optiontab = new TabVisualisationOptions(myXMLParser); /* Visualisierung berechneter Daten */
		
		ov_vis_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(111)));
		ov_vis_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(112)));
		ov_vis_right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(113)));
		
		tp_visual.addTab(myXMLParser.getText(30), pa_visual_contrtab);
		tp_visual.addTab(myXMLParser.getText(31), pa_visual_datatab);
		tp_visual.addTab(myXMLParser.getText(32), pa_visual_plottab);
		tp_visual.addTab(myXMLParser.getText(33), pa_visual_optiontab);
		
		add(ov_vis_top);
		add(tp_visual);
		add(ov_vis_front);
		add(ov_vis_right);
		
		setPlayControlsEnabled(false);
	}
	
	public void setGridColor(Color newColor) {
		ov_vis_front.coGridColor = newColor;
		ov_vis_right.coGridColor = newColor;
		ov_vis_top.coGridColor = newColor;
		updateViews();
	}
	
	public void setObjectColor(Color newColor) {
		ov_vis_front.coSpeedvecColor = newColor;
		ov_vis_right.coSpeedvecColor = newColor;
		ov_vis_top.coSpeedvecColor = newColor;
		updateViews();		
	}
	
	public void updateViews() {
		updateCurFrame();
	}

	public void repaintViews() {
		ov_vis_top.repaint();
		ov_vis_front.repaint();
		ov_vis_right.repaint();
	}
	public void setPlayControlsEnabled(boolean state) {
		pa_visual_contrtab.b_unloadfile.setEnabled(state);
		pa_visual_contrtab.sl_playcontr_slider.setEnabled(state);
		pa_visual_contrtab.sp_fpschooser.setEnabled(state);
		pa_visual_contrtab.b_playcontr_start.setEnabled(state);
		pa_visual_contrtab.b_playcontr_end.setEnabled(state);
		pa_visual_contrtab.b_playcontr_play.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepback.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepforw.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stop.setEnabled(state);
		pa_visual_datatab.buttonsInactive(state);
		pa_visual_plottab.buttonsInactive(state);
	}
	
	private void updateCurFrame() {
		if(data_plot > 0)
			if(loader_plot.iCurMax > dynamic_iCurMax)
				drawPlot(loader_plot, data_plot, false);
		
		pa_visual_plottab.updateVisualplotoptions(ov_vis_top.getCurrentStep());
		ov_vis_top.repaint();
		ov_vis_front.repaint();
		ov_vis_right.repaint();
		pa_visual_datatab.cb_Objects.removeAllItems();
		
		if(ov_vis_top.getCurrentStep() != null 
				&& ov_vis_top.getCurrentStep().getMasspoints() != null
				&& ov_vis_top.getCurrentStep().getMasspoints().length > 0) {
			for(int i=0;i<ov_vis_top.getCurrentStep().getMasspoints().length;i++)
				pa_visual_datatab.cb_Objects.addItem(ov_vis_top.getCurrentStep().getMasspoints()[i]);
			
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
		pa_visual_contrtab.la_zoomlevel.setText(myXMLParser.getText(101)+": "+zoomLevel+" (1 "+myXMLParser.getText(107)+" = "+ df.format(zoomUnit) + "km)");
		updateCurFrame();
	}
	
	public void setGridOffset(int gridOffset) {
		ov_vis_front.iGridOffset = gridOffset;
		ov_vis_right.iGridOffset = gridOffset;
		ov_vis_top.iGridOffset = gridOffset;
		
		pa_visual_contrtab.la_gridoffset.setText(myXMLParser.getText(102)+": "+gridOffset);
		updateCurFrame();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//Controller.debugout("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_vis_front.addCoordOffsetX(deltaX);
		ov_vis_top.addCoordOffsetX(deltaX);
		updateCurFrame();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//Controller.debugout("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		//ov_vis_front.addCoordOffsetY(deltaY);
		ov_vis_top.addCoordOffsetY(-deltaY);
		ov_vis_right.addCoordOffsetY(deltaY);
		updateCurFrame();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//Controller.debugout("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_vis_front.addCoordOffsetZ(-deltaZ);
		ov_vis_right.addCoordOffsetZ(-deltaZ);
		updateCurFrame();
	}
	
	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_vis_top.iZoomLevel))/ov_vis_top.iGridOffset;
	}
	
	public void resetOffset() {
		ov_vis_front.resetCoordOffset();
		ov_vis_right.resetCoordOffset();
		ov_vis_top.resetCoordOffset();
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

	public void removePlot() {
		pa_visual_plottab.cb_plot_dense.setSelectedIndex(3);
		ov_vis_top.alldots = null;
		ov_vis_front.alldots = null;
		ov_vis_right.alldots = null;
		updateViews();
	}
	public void drawPlot(DynamicWPTLoader dynamicLoader, int data, boolean repaint) {
		data_plot = data;
		loader_plot = dynamicLoader;
		dynamic_iCurMax = dynamicLoader.iCurMax;
		int dynamic_iCurMin;
		int steps = dynamic_iCurMax;
		
		if(repaint)
			dynamic_iCurMin = getCurFrame();
		else
			dynamic_iCurMin = dynamicLoader.iCurMin;
		
		steps -= dynamic_iCurMin;

		if(getMaxFrame() > DynamicWPTLoader.STANDARDBUFFERSIZE && repaint) {
			JOptionPane.showMessageDialog(this,String.format(myXMLParser.getText(175),dynamic_iCurMin, dynamicLoader.iCurMax)
								,myXMLParser.getText(173),JOptionPane.INFORMATION_MESSAGE);
		}
		steps /= data;
		
		ov_vis_top.alldots   = 	new Step[steps];
		ov_vis_front.alldots = 	new Step[steps];
		ov_vis_right.alldots = 	new Step[steps];
		for(int k=0;k<steps;k++) {
			if(dynamic_iCurMin + k*data > dynamic_iCurMax)
				break;
			ov_vis_top.alldots[k] 	= dynamicLoader.getStep(dynamic_iCurMin + k*data);
			ov_vis_front.alldots[k] = dynamicLoader.getStep(dynamic_iCurMin + k*data);
			ov_vis_right.alldots[k] = dynamicLoader.getStep(dynamic_iCurMin + k*data);
		}
		pa_visual_plottab.cb_plot_dense.setSelectedIndex((int)Math.log10(data)+1);

		pa_visual_plottab.updateDrawStatus(dynamicLoader.steps, ov_vis_front.getCurrentStep());
		if(repaint)
			updateViews();
	}
	public void setSpeedvecEnabled(boolean state) {
		pa_visual_optiontab.chb_vvector.setSelected(state);
		ov_vis_front.draw_speed = state;
		ov_vis_right.draw_speed = state;
		ov_vis_top.draw_speed = state;
	}
	public void setmpIDEnabled(boolean state) {
		pa_visual_optiontab.chb_mpids.setSelected(state);
		ov_vis_front.draw_strings = state;
		ov_vis_right.draw_strings = state;
		ov_vis_top.draw_strings = state;
	}
}
