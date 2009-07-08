package jgravsim;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import javax.swing.JPanel;

public class ObjectView extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final double LIGHTSPEED = CalcCode.LIGHTSPEED;
	public static final Color BLACKHOLE = Color.BLACK;
	public static final Color BLACKHOLE_SELECTED = new Color(0, 0, 128);
	public static final Color STANDARD = Color.DARK_GRAY;
	public static final Color STANDARD_SELECTED = Color.RED; // Color(34,139,34); // ==ForestGreen
	public static final Color HIDDEN = Color.LIGHT_GRAY; // gainsboro
	public static final Color SPEEDVEC = Color.RED;
	public static final Color STRING = Color.BLACK;
	public static final Color STRING_BRIGHT = new Color(220, 220, 220);
	public static final Color CLICKME = Color.ORANGE; // gainsboro
	public static final float CONVERT3D = 2.0E7f; // new
	public static final int speedvecmax = 55;
	protected int iGridOffset = 25;
	protected int iLastMouseX = 0;
	protected int iLastMouseY = 0;
	protected double iZoomLevel = 12.0;
	protected boolean draw_strings = true;
	protected boolean draw_speed = true;
	boolean boxed = true;
	protected Masspoint mp_selected = null;
	protected Color coGridColor = Color.decode("#DDDDDD");
	protected Color coSpeedvecColor = SPEEDVEC;
	Color coObjColor = STANDARD;
	protected char[] cAxes;
	protected double dCoordOffsetX = 0.0;
	protected double dCoordOffsetY = 0.0;
	protected double dCoordOffsetZ = 0.0;
	protected Step[] alldots;
	protected String str_clickme;
	protected Step stCurrent = null;

	public ObjectView() {
		super();
	}

	public ObjectView(LayoutManager layout) {
		super(layout);
	}

	public ObjectView(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public ObjectView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	protected double getVectorLength(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	void drawGrid(Graphics g, int centerX, int centerY) {
		g.setColor(coGridColor);
		g.drawLine(centerX, 0, centerX, getHeight());
		g.drawLine(0, centerY, getWidth(), centerY);

		int gridXstart;
		int gridYstart;

		for (gridXstart = centerX; gridXstart > 0; gridXstart -= iGridOffset)
			;
		for (gridYstart = centerY; gridYstart > 0; gridYstart -= iGridOffset)
			;

		for (int offsetX = gridXstart; offsetX < getWidth(); offsetX += iGridOffset) {
			for (int offsetY = gridYstart; offsetY < getHeight(); offsetY += iGridOffset) {
				drawGridPoint(g, offsetX, offsetY);
			}
		}
		g.setColor(Color.black);
		int string_width = 10;
		int string_height = 20;
		int string_size = string_width;
		int string_size2 = string_width;
		if (!boxed) {
			string_size = 5;
			string_size2 = 5;
			string_width = 7;
			string_height = 7;
		}
		g.drawString("" + cAxes[0], this.getWidth() - string_width, centerY
				+ string_size);
		g.drawString("" + cAxes[1], centerX - string_size2, string_height);
	}

	void drawGridPoint(Graphics g, int offsetX, int offsetY) {
		g.drawLine(offsetX, offsetY - 2, offsetX, offsetY + 2);
		g.drawLine(offsetX - 2, offsetY, offsetX + 2, offsetY);
	}

	void drawClickmeOverlay(Graphics g, int centerX, int centerY) {
		// int size = 48;
		Font arialFont = new Font("Arial", Font.ITALIC, 22);

		AttributedString as_clickme = new AttributedString(str_clickme);
		as_clickme.addAttribute(TextAttribute.FONT, arialFont);
		as_clickme.addAttribute(TextAttribute.FOREGROUND, Color.red);

		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout tl = new TextLayout(str_clickme, arialFont, frc);
		float fwidth = (float) tl.getBounds().getWidth();
		float fheight = (float) tl.getBounds().getHeight();

		/*
		 * while(fwidth+20 > this.getWidth()) { size--; arialFont = new
		 * Font("Arial", Font.ITALIC, size); frc = g2.getFontRenderContext(); tl
		 * = new TextLayout(str_clickme, arialFont, frc); fwidth = (float)
		 * tl.getBounds().getWidth(); }
		 * System.out.println("drawClickmeOverlay() - size="+size);
		 */

		g2.drawString(as_clickme.getIterator(), centerX - fwidth / 2, centerY
				+ fheight / 2);
	}
	
	public void setCoordOffset(double x1, double x2, double x3) {
		dCoordOffsetX = x1;
		dCoordOffsetY = x2;
		dCoordOffsetZ = x3;
	}

	public void setCoordOffset(double[] offset) {
		if (offset.length != 3)
			return;
		setCoordOffset(offset[0], offset[1], offset[2]);
	}

	public void setCoordOffset(MDVector offset) {
		setCoordOffset(offset.x1, offset.x2, offset.x3);
	}

	public void resetCoordOffset() {
		setCoordOffset(0.0, 0.0, 0.0);
	}

	public void addCoordOffsetX(double x1) {
		setCoordOffset(dCoordOffsetX + x1, dCoordOffsetY, dCoordOffsetZ);
	}

	public void addCoordOffsetY(double x2) {
		setCoordOffset(dCoordOffsetX, dCoordOffsetY + x2, dCoordOffsetZ);
	}

	public void addCoordOffsetZ(double x3) {
		setCoordOffset(dCoordOffsetX, dCoordOffsetY, dCoordOffsetZ + x3);
	}

	public double getCoordOffsetX() {
		return dCoordOffsetX;
	}

	public double getCoordOffsetY() {
		return dCoordOffsetY;
	}

	public double getCoordOffsetZ() {
		return dCoordOffsetZ;
	}

	public void resetAllSteps() {
		alldots = null;
	}

	public void resetCurrentStep() {
		stCurrent = null;
	}

	public Step getCurrentStep() {
		return stCurrent;
	}

	public void setBoxed(boolean b) {
		boxed = b;
	}

	void displayStep(Step next) {
		stCurrent = next;
	}

}
