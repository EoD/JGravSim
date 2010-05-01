package jgravsim;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class TabVisualisationOptions  extends JPanel {
	private static final long serialVersionUID = 1L;

	XMLParser myXMLParser;

	JPanel pa_vis_option_chbs;
		JCheckBox chb_mpids;
		JCheckBox chb_vvector;
		JCheckBox chb_autoresize;
	JPanel pa_vis_option_color;
		JButton b_colorch_grid;
		JButton b_colorch_speedvec;

	public TabVisualisationOptions(XMLParser parser) {
		myXMLParser = parser;		

		setLayout(new GridLayout(8,1));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(33)));

		pa_vis_option_chbs = new JPanel(new FlowLayout(FlowLayout.LEFT));
		chb_vvector = new JCheckBox(myXMLParser.getText(218), true);
		chb_mpids = new JCheckBox(myXMLParser.getText(219), true);
		chb_autoresize = new JCheckBox(myXMLParser.getText(234), true);
		pa_vis_option_chbs.add(chb_vvector);
		pa_vis_option_chbs.add(chb_mpids);
		pa_vis_option_chbs.add(chb_autoresize);
		
		pa_vis_option_color = new JPanel(new GridLayout());
		b_colorch_grid = new JButton(myXMLParser.getText(103));
		b_colorch_speedvec = new JButton(myXMLParser.getText(104));
		pa_vis_option_color.add(b_colorch_grid);
		pa_vis_option_color.add(b_colorch_speedvec);
		
		add(pa_vis_option_chbs);
		add(new JPanel());
		add(pa_vis_option_color);
	}
}