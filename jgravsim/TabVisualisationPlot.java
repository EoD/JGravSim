package jgravsim;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TabVisualisationPlot extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final boolean DEBUG = true;
	
	XMLParser myXMLParser;
	
	JPanel pa_plot_options;   
		JButton b_plot_draw;
		JButton b_plot_remove;
		JComboBox cb_plot_dense;

	JPanel pa_plot_la;
		JLabel la_plot_mps;
	JPanel pa_plot_mps;
		JScrollPane sp_plot_mp;
			JPanel pa_plot_mps_sub;
				View_PlotOption vpo_plot_mp[];
		
	public TabVisualisationPlot(XMLParser parser) {
		myXMLParser = parser;
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(32)));
			
		//Panel 1
		pa_plot_options = new JPanel();
		pa_plot_options.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		b_plot_draw = new JButton(myXMLParser.getText(307));
		b_plot_remove = new JButton(myXMLParser.getText(308));
		
		cb_plot_dense = new JComboBox();
		cb_plot_dense.addItem( myXMLParser.getText(322) );	// 1/?
		cb_plot_dense.addItem( myXMLParser.getText(316) );	// 1/1
		cb_plot_dense.addItem( myXMLParser.getText(317) );	// 1/10
		cb_plot_dense.addItem( myXMLParser.getText(318) );	// 1/100
		cb_plot_dense.addItem( myXMLParser.getText(319) );	// 1/1000
		cb_plot_dense.addItem( myXMLParser.getText(320) );	// 1/10000
		cb_plot_dense.setSelectedIndex(3);	//==1/100
		//Panel 2
		pa_plot_la = new JPanel();
		pa_plot_la.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		la_plot_mps = new JLabel(myXMLParser.getText(309));
		la_plot_mps.setFont(new Font("Sans Serif", Font.BOLD, 14));

		//Panel 3
		pa_plot_mps_sub = new JPanel();
		pa_plot_mps_sub.setLayout(new BoxLayout(pa_plot_mps_sub, BoxLayout.Y_AXIS));

		pa_plot_mps = new JPanel(new GridLayout(0, 1));
		//pa_plot_mps.setMinimumSize(new Dimension(400,400));
		sp_plot_mp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp_plot_mp.setViewportView(pa_plot_mps_sub);

		pa_plot_options.add(cb_plot_dense);
		pa_plot_options.add(b_plot_draw);
		pa_plot_options.add(b_plot_remove);

		pa_plot_la.add(la_plot_mps);
		
		pa_plot_mps.add(sp_plot_mp);

		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JPanel());
		add(pa_plot_options);
		add(new JPanel());
		add(pa_plot_la);
		add(pa_plot_mps);
		updateVisualplotoptions(null);
	}

	private static void debugout(String a) {
		if(DEBUG && Controller.CURRENTBUILD)
			System.out.println(a);
	}	
	
	public void updateDrawStatus(Step[] sta_all, Step st_reference) {
		if(sta_all != null && sta_all.length > 0
				&& st_reference != null
				&& st_reference.getMasspoints() != null
				&& st_reference.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints_ref = st_reference.getMasspoints();
			
			for(int j=0; j < sta_all.length; j++) {
				if(sta_all[j] != null 
						&& sta_all[j].getMasspoints() != null
						&& sta_all[j].getMasspoints().length > 0) {
					Masspoint_Sim[] masspoints = sta_all[j].getMasspoints();
					
					for (int i = 0; i < masspoints.length; i++) {
						Masspoint_Sim masspoint = masspoints[i];
						for (int k = 0; k < masspoints_ref.length; k++) {
							if(masspoint.getID() == masspoints_ref[k].getID())
								masspoint.setDrawStatus(masspoints_ref[k].getDrawStatus());
						}
					}
					
				}
			}
			
		}
	}

	public void updateVisualplotoptions(Step stCurrent) {
		pa_plot_mps_sub = new JPanel();
		pa_plot_mps_sub.setLayout(new BoxLayout(pa_plot_mps_sub, BoxLayout.Y_AXIS));
		sp_plot_mp.setViewportView(pa_plot_mps_sub);

		if(stCurrent == null 
				|| stCurrent.getMasspoints() == null 
				|| stCurrent.getMasspoints().length <= 0) {
			
			pa_plot_mps_sub.setLayout(new FlowLayout(FlowLayout.CENTER));
			pa_plot_mps_sub.add(new JLabel(myXMLParser.getText(303)));
			return;
		}
		
		Masspoint_Sim[] mps_ref = stCurrent.getMasspoints();
		vpo_plot_mp = new View_PlotOption[mps_ref.length];
		//debugout("updateVisualplotoptions() - NumofMPs="+mps_ref.length);
		
		for (int i = 0; i < mps_ref.length; i++) {
			vpo_plot_mp[i] = new View_PlotOption(mps_ref[i],myXMLParser);
			pa_plot_mps_sub.add(vpo_plot_mp[i]);
		}

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public void buttonsInactive(boolean state) {
		b_plot_draw.setEnabled(state);
		b_plot_remove.setEnabled(state);
		cb_plot_dense.setEnabled(state);
		updateVisualplotoptions(null);
	}
}
