package jgravsim;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InterpretInput {

	public static final int revision = 2;
	private static final boolean DEBUG = false;

	private static final DecimalFormat df_s = new DecimalFormat("+0.00000E0;-0.00000E0");
	private static final DecimalFormat df_c = new DecimalFormat("+0.000000000;-0.000000000");
	private static final DecimalFormat df_c_unsigned = new DecimalFormat("0.0000000000");
	private static final DecimalFormat df_l = new DecimalFormat("+0.000000E0;-0.000000E0");
	private static final DecimalFormat df_l_unsigned = new DecimalFormat("0.0000E0");
	
	@SuppressWarnings("unused")
	private static void debugout(String a) {
		if (Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}

	public static String niceInput_Time(double time, XMLParser myXMLParser, DecimalFormat df) {
		debugout("getNiceOutput - value=" + time);
		time = Math.abs(time);
		if(time == 0) {
			return "0.0 "+myXMLParser.getText(470);
		}

		String unit = myXMLParser.getText(44);
		double factor = 1.0;

		if (time >= 1000.0 * 365.25 * 24.0 * 3600.0) {
			unit = myXMLParser.getText(479);
		}
		else if (time >= 100.0 * 365.25 * 24.0 * 3600.0) {
			unit = myXMLParser.getText(478);
			factor = 100.0 * 365.25 * 24.0 * 3600.0;
		} else if (time >= 10.0 * 365.25 * 24.0 * 3600.0) {
			unit = myXMLParser.getText(477);
			factor = 10.0 * 365.25 * 24.0 * 3600.0;
		} else if (time >= 365.25 * 24.0 * 3600.0) {
			unit = myXMLParser.getText(476);
			factor = 365.25 * 24.0 * 3600.0;
		} else if (time >= 365.25 * 24.0 * 3600.0 / 12.0) {
			unit = myXMLParser.getText(475);
			factor = 365.25 * 24.0 * 3600.0 / 12.0;
		} else if (time >= 2.0 * 7.0 * 24.0 * 60.0 * 60.0) {
			unit = myXMLParser.getText(474);
			factor = 7.0 * 24.0 * 60.0 * 60.0;
		} else if (time >= 2.0 * 24.0 * 60.0 * 60.0) {
			unit = myXMLParser.getText(473);
			factor = 24.0 * 60.0 * 60.0;
		} else if (time >= 60.0 * 60.0) {
			unit = myXMLParser.getText(472);
			factor = 60.0 * 60.0;
		} else if (time >= 60.0) {
			unit = myXMLParser.getText(471);
			factor = 60.0;
		} else if (time >= 1.0) {
			factor = 1.0;
			unit = myXMLParser.getText(470);
		} else if (time >= 1.0E-3) {
			factor = 1.0E-3;
			unit = myXMLParser.getText(442)+myXMLParser.getText(470);
		} else if (time >= 1.0E-6) {
			factor = 1.0E-6;
			unit = myXMLParser.getText(443)+myXMLParser.getText(470);
		} else if (time >= 1.0E-9) {
			factor = 1.0E-9;
			unit = myXMLParser.getText(444)+myXMLParser.getText(470);
		} else if (time >= 1.0E-12) {
			factor = 1.0E-12;
			unit = myXMLParser.getText(445)+myXMLParser.getText(470);
		} else if (time >= 1.0E-15) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(446)+myXMLParser.getText(470);
		} else if (time >= 1.0E-18) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(447)+myXMLParser.getText(470);
		} else if (time >= 1.0E-21) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(448)+myXMLParser.getText(470);
		} else { //if (time >= 1.0E-24) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(449)+myXMLParser.getText(470);
		} 
		// else
		//	return myXMLParser.getText(159);

		if(df == null)
			return stringCommataRevert(String.valueOf(time / factor))+" "+unit;
		else
			return (df.format(time / factor) + " " + unit);
	}

	public static String niceInput_Length(long lvar, XMLParser myXMLParser, DecimalFormat df) {
		long var = Math.abs(lvar);
		debugout("getNice_Length() - Lvalue=" + var);
		if(var == 0) {
			return "0.0 "+myXMLParser.getText(450);
		}
		
		String unit = myXMLParser.getText(44);
		double factor = 1.0;

		if (var >= MVMath.ConvertToL(CalcCode.ps)) {
			unit = myXMLParser.getText(455);
			factor = CalcCode.ps;
		} else if (var >= MVMath.ConvertToL(CalcCode.LY)) {
			unit = myXMLParser.getText(454);
			factor = CalcCode.LY;
		} else if (var >= MVMath.ConvertToL(CalcCode.AE)) {
			unit = myXMLParser.getText(453);
			factor = CalcCode.AE;
		} else if (var >= MVMath.ConvertToL(1000)) {
			factor = 1.0E3;	// km
			unit = myXMLParser.getText(437)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0)) {
			factor = 1.0;	// m
			unit = myXMLParser.getText(450);		
		} else if (var >= MVMath.ConvertToL(1.0E-1)) {
			factor = 1.0E-1;
			unit = myXMLParser.getText(440)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-2)) {
			factor = 1.0E-2;
			unit = myXMLParser.getText(441)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-3)) {
			factor = 1.0E-3;
			unit = myXMLParser.getText(442)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-6)) {
			factor = 1.0E-6;
			unit = myXMLParser.getText(443)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-9)) {
			factor = 1.0E-9;
			unit = myXMLParser.getText(444)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-12)) {
			factor = 1.0E-12;
			unit = myXMLParser.getText(445)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-15)) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(446)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-18)) {
			factor = 1.0E-18;
			unit = myXMLParser.getText(447)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-21)) {
			factor = 1.0E-21;
			unit = myXMLParser.getText(448)+myXMLParser.getText(450);
		} else if (var >= MVMath.ConvertToL(1.0E-24)) {
			factor = 1.0E-24;
			unit = myXMLParser.getText(449)+myXMLParser.getText(450);
		} else {//if (var >= MVMath.ConvertToL(CalcCode.A)) {
			factor = CalcCode.A;
			unit = myXMLParser.getText(451);
		} 
		//else
		//	return myXMLParser.getText(159);

		if(df == null)
			return stringCommataRevert(String.valueOf( MVMath.ConvertToD(lvar)/factor ))+" "+unit;
		else
			return (df.format( MVMath.ConvertToD(lvar)/factor )) + " " + unit;
	}
	
	public static String niceInput_Length(double dvar, XMLParser myXMLParser, DecimalFormat df) {
		double var = Math.abs(dvar);
		debugout("getNice_Length() - Dvalue=" + var);
		if(var == 0) {
			return "0.0 "+myXMLParser.getText(450);
		}

		String unit = myXMLParser.getText(44);
		double factor = 1.0;

		if (var >= CalcCode.ps) {
			unit = myXMLParser.getText(455);
			factor = CalcCode.ps;
		} else if (var >= CalcCode.LY) {
			unit = myXMLParser.getText(454);
			factor = CalcCode.LY;
		} else if (var >= CalcCode.AE) {
			unit = myXMLParser.getText(453);
			factor = CalcCode.AE;
		// } else if (var >= CalcCode.mi) {
		// unit = myXMLParser.getText(452);
		// factor = CalcCode.mi;
		} else if (var >= 1.0E3) {
			factor = 1.0E3;	// m
			unit = myXMLParser.getText(437)+myXMLParser.getText(450);
		} else if (var >= 1.0) {
			factor = 1.0;	// m
			unit = myXMLParser.getText(450);
		} else if (var >= 1.0E-1) {
			factor = 1.0E-1;
			unit = myXMLParser.getText(440)+myXMLParser.getText(450);
		} else if (var >= 1.0E-2) {
			factor = 1.0E-2;
			unit = myXMLParser.getText(441)+myXMLParser.getText(450);
		} else if (var >= 1.0E-3) {
			factor = 1.0E-3;
			unit = myXMLParser.getText(442)+myXMLParser.getText(450);
		} else if (var >= 1.0E-6) {
			factor = 1.0E-6;
			unit = myXMLParser.getText(443)+myXMLParser.getText(450);
		} else if (var >= 1.0E-9) {
			factor = 1.0E-9;
			unit = myXMLParser.getText(444)+myXMLParser.getText(450);
		} else if (var >= 1.0E-12) {
			factor = 1.0E-12;
			unit = myXMLParser.getText(445)+myXMLParser.getText(450);
		} else if (var >= 1.0E-15) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(446)+myXMLParser.getText(450);
		} else if (var >= 1.0E-18) {
			factor = 1.0E-18;
			unit = myXMLParser.getText(447)+myXMLParser.getText(450);
		} else if (var >= 1.0E-21) {
			factor = 1.0E-21;
			unit = myXMLParser.getText(448)+myXMLParser.getText(450);
		} else if (var >= 1.0E-24) {
			factor = 1.0E-24;
			unit = myXMLParser.getText(449)+myXMLParser.getText(450);
		} else { //if (var >= CalcCode.A) {
			factor = CalcCode.A;
			unit = myXMLParser.getText(451);
		} 
		//else
		//	return myXMLParser.getText(159);
		
		if(df == null)
			return stringCommataRevert(String.valueOf(dvar / factor))+" "+unit;
		else
			return (df.format(dvar / factor) + " " + unit);
	}

	public static String niceInput_Mass(double var, XMLParser myXMLParser, DecimalFormat df) {
		debugout("getNice_Length() - value=" + var);
		var = Math.abs(var);
		if(var == 0) {
			return "0.0 "+myXMLParser.getText(461);
		}

		String unit = myXMLParser.getText(44);
		double factor = 1.0;

		if (var >= CalcCode.SM) {
			factor = CalcCode.SM;
			unit = myXMLParser.getText(465);
		} else if (var >= CalcCode.EM) {
			factor = CalcCode.EM;
			unit = myXMLParser.getText(464);
		} else if (var >= 1.0E3) {
			factor = 1.0E3;
			unit = myXMLParser.getText(463);
		} else if (var >= 1.0) {
			factor = 1.0;	// kg
			unit = myXMLParser.getText(460);
		} else if (var >= 1.0E-3) {
			factor = 1.0E-3;
			unit = myXMLParser.getText(442)+myXMLParser.getText(461);
		} else if (var >= 1.0E-6) {
			factor = 1.0E-6;
			unit = myXMLParser.getText(443)+myXMLParser.getText(461);
		} else if (var >= 1.0E-9) {
			factor = 1.0E-9;
			unit = myXMLParser.getText(444)+myXMLParser.getText(461);
		} else if (var >= 1.0E-12) {
			factor = 1.0E-12;
			unit = myXMLParser.getText(445)+myXMLParser.getText(461);
		} else if (var >= 1.0E-15) {
			factor = 1.0E-15;
			unit = myXMLParser.getText(446)+myXMLParser.getText(461);
		} else if (var >= 1.0E-18) {
			factor = 1.0E-18;
			unit = myXMLParser.getText(447)+myXMLParser.getText(461);
		} else if (var >= 1.0E-21) {
			factor = 1.0E-21;
			unit = myXMLParser.getText(448)+myXMLParser.getText(461);
		} else if (var >= 1.0E-24) {
			factor = 1.0E-24;
			unit = myXMLParser.getText(449)+myXMLParser.getText(461);
		} else { //if (var >= CalcCode.u) {
			factor = CalcCode.u;
			unit = myXMLParser.getText(462);
		}
		//else
		//	return myXMLParser.getText(159);

		if(df == null)
			return stringCommataRevert(String.valueOf(var / factor))+" "+unit;
		else
			return (df.format(var / factor) + " " + unit);
	}

	/**
	 * 
	 * Returns a nice String for value of var. If dfc is null, the value will
	 * always be displayed in the df format. If dfc is not null and if the value
	 * >= 10% CalcCode.LIGHTSPEED it will be displayed in units of 'c' instead of
	 * length/s.
	 * 
	 * @param var
	 *            Number
	 * @param myXMLParser
	 * @param df
	 *            DecimalFormat for length/s or null
	 * @param dfc
	 *            DecimalFormat for c or null
	 * @return String like "5.000 m/s"
	 */
	public static String niceInput_Speed(double var, XMLParser myXMLParser, DecimalFormat df, DecimalFormat dfc) {		
		if(dfc != null && Math.abs(var) >= 0.1*CalcCode.LIGHTSPEED)
			return dfc.format(var/CalcCode.LIGHTSPEED)+" c";
		else
			return niceInput_Length(var, myXMLParser, df)+"/s";
	}
	public static String niceInput_Density(double var, XMLParser myXMLParser, DecimalFormat df) {
		return niceInput_Mass(var, myXMLParser, df)+"/m³";
	}

	public static double checkInput_Time(String strtemp, XMLParser myXMLParser) {
		
		double factor = 1.0;
		if (strtemp.compareTo(myXMLParser.getText(478)) == 0
				|| strtemp.compareTo("centuries") == 0)
			factor = (100.0 * 365.25 * 24.0 * 3600.0);
		else if (strtemp.compareTo(myXMLParser.getText(477)) == 0
				|| strtemp.compareTo("decades") == 0)
			factor = (10.0 * 365.25 * 24.0 * 3600.0);
		else if (strtemp.compareTo(myXMLParser.getText(476)) == 0)
			factor = (365.25 * 24.0 * 3600.0);
		else if (strtemp.compareTo(myXMLParser.getText(475)) == 0
				|| strtemp.compareTo("months") == 0)
			factor = (365.25 * 24.0 * 3600.0 / 12.0);
		else if (strtemp.compareTo(myXMLParser.getText(474)) == 0
				|| strtemp.compareTo("weeks") == 0)
			factor = (7.0 * 24.0 * 3600.0);
		else if (strtemp.compareTo(myXMLParser.getText(473)) == 0)
			factor = (24.0 * 3600.0);
		else if (strtemp.compareTo(myXMLParser.getText(472)) == 0)
			factor = (60.0 * 60.0);
		else if (strtemp.compareTo(myXMLParser.getText(471)) == 0)
			factor = 60.0;
		else if (strtemp.compareTo(myXMLParser.getText(470)) == 0)
			factor = 1.0;	// s
		else if (strtemp.endsWith(myXMLParser.getText(470)))
			factor = getLatinUnit(strtemp, myXMLParser);
		else {
			debugout("checkInput_Time - unknown unit: " + strtemp);
			return 0;
		}
		debugout("checkInput_Time - Factor=" + factor);
		return factor;
	}

	public static double checkInput_Speed(String strtemp, XMLParser myXMLParser) {
		
		String[] safactor = strtemp.split("/");
		double factor = 1.0;

		if (safactor.length < 2) {
			if (safactor[0].compareTo(myXMLParser.getText(480)) == 0) {
				factor = CalcCode.LIGHTSPEED;
			}
			else if (safactor[0].compareTo(myXMLParser.getText(481)) == 0) {
				factor = CalcCode.KNOT;
		} 
			else if (safactor[0].compareTo(myXMLParser.getText(482)) == 0) {
				factor = checkInput_Length(myXMLParser.getText(452), myXMLParser);
				factor /= checkInput_Time(myXMLParser.getText(472), myXMLParser);
		} 
			else {
				debugout("checkInput_Speed - missing units");
				return 0;
			}
		} else {
			double factor2;

			factor = checkInput_Length(safactor[0], myXMLParser);
			if (factor == 0) {
				debugout("checkInput_Speed - 1.unit error");
				return 0;
			}

			factor2 = checkInput_Time(safactor[1], myXMLParser);
			if (factor2 == 0) {
				debugout("checkInput_Speed - 2.unit error");
				return 0;
			}

			factor /= factor2;
		}
		return factor;
	}

	public static double checkInput_Mass(String strtemp, XMLParser myXMLParser) {
		double factor = 1.0;
		
		if (strtemp.compareTo(myXMLParser.getText(465)) == 0) {
			factor = CalcCode.SM;
		} else if (strtemp.compareTo(myXMLParser.getText(464)) == 0) {
			factor = CalcCode.EM;
		} else if (strtemp.compareTo(myXMLParser.getText(463)) == 0) {
			factor = 1.0E3;
		} else if (strtemp.compareTo(myXMLParser.getText(460)) == 0) {
			factor = 1.0;	// kg
		} else if (strtemp.compareTo(myXMLParser.getText(461)) == 0) {
			factor = 1.0E-3;
		} else if (strtemp.compareTo(myXMLParser.getText(462)) == 0) {
			factor = CalcCode.u;
		// SI Unit is kg, but there is already a prefix. So use 461(g) instead
		// of 460(kg)
		} else if (strtemp.endsWith(myXMLParser.getText(461))) {
				factor = getLatinUnit(strtemp, myXMLParser) / 1.0E3;	//10E-3 because of kg
		} else {
			return 0;
		}
		return factor;
	}

	public static double checkInput_Length(String strtemp, XMLParser myXMLParser) {
		double factor = -1.0;

		if (strtemp.compareTo(myXMLParser.getText(455)) == 0)
			factor = CalcCode.ps;
		else if (strtemp.compareTo(myXMLParser.getText(454)) == 0 || strtemp.compareTo("ly") == 0)
			factor = CalcCode.LY;
		else if (strtemp.compareTo(myXMLParser.getText(453)) == 0 || strtemp.compareTo("AU") == 0)
			factor = CalcCode.AE;
		else if (strtemp.compareTo(myXMLParser.getText(452)) == 0 )
			factor = CalcCode.mi;
		else if (strtemp.compareTo(myXMLParser.getText(437)+myXMLParser.getText(450)) == 0)
			factor = 1.0E3;
		else if (strtemp.compareTo(myXMLParser.getText(450)) == 0)
			factor = 1.0;		// m
		else if (strtemp.compareTo(myXMLParser.getText(451)) == 0)
			factor = CalcCode.A;
		else if (strtemp.endsWith(myXMLParser.getText(450)))
			factor = getLatinUnit(strtemp, myXMLParser);
		else
			return 0;
		
		return factor;
	}

	private static double getLatinUnit(String strtemp, XMLParser myXMLParser) {
		
		if (strtemp.startsWith(myXMLParser.getText(440)))
			return 1.0E-1;
		else if (strtemp.startsWith(myXMLParser.getText(441)))
			return 1.0E-2;
		else if (strtemp.startsWith(myXMLParser.getText(442)))
			return 1.0E-3;
		else if (strtemp.startsWith(myXMLParser.getText(443)))
			return 1.0E-6;
		else if (strtemp.startsWith(myXMLParser.getText(444)))
			return 1.0E-9;
		else if (strtemp.startsWith(myXMLParser.getText(445)))
			return 1.0E-12;
		else if (strtemp.startsWith(myXMLParser.getText(446)))
			return 1.0E-15;
		else if (strtemp.startsWith(myXMLParser.getText(447)))
			return 1.0E-18;
		else if (strtemp.startsWith(myXMLParser.getText(448)))
			return 1.0E-21;
		else if (strtemp.startsWith(myXMLParser.getText(449)))
			return 1.0E-24;
		else
			return 0;		
	}

	public static long checkInput_Length_Long(String strtemp, XMLParser myXMLParser) {
		double dtemp = checkInput_Length(strtemp, myXMLParser);
		long ltemp;
		if (dtemp < CalcCode.LACCURACY) {
			ltemp = MVMath.ConvertToL((double) checkInput_Length(strtemp, myXMLParser));
		} else {
			ltemp = (long) checkInput_Length(strtemp, myXMLParser) * (long) CalcCode.LACCURACY;
		}
		// debugout("checkInput_Length_Long() - strtemp="+strtemp+",
		// dtemp="+dtemp+", ltemp="+ltemp);
		return ltemp;
	}
	public static String stringCommataReplace(String strtemp) {
		char group = DecimalFormatSymbols.getInstance(Locale.getDefault()).getGroupingSeparator();
		char decimal = DecimalFormatSymbols.getInstance(Locale.getDefault()).getDecimalSeparator();
		
		if(strtemp.contains(String.valueOf(group)))
			strtemp = strtemp.replace(String.valueOf(group),"");

		if(decimal != '.') {
			if(strtemp.contains(String.valueOf(decimal))) {
				debugout("stringCommataReplace() - commata, new strtemp="+strtemp.replace(',','.'));
				strtemp = strtemp.replace(decimal,'.');
			}
		}
		return strtemp;
	}
	public static String stringCommataRevert(String strtemp) {
		char group = DecimalFormatSymbols.getInstance(Locale.getDefault()).getGroupingSeparator();
		char decimal = DecimalFormatSymbols.getInstance(Locale.getDefault()).getDecimalSeparator();
		char temp = '?';
		
		if(strtemp.contains(","))
			strtemp = strtemp.replace(',',temp);

		if(decimal != '.') {
			if(strtemp.contains(".")) {
				debugout("stringCommataReplace() - commata, new strtemp="+strtemp.replace(',','.'));
				strtemp = strtemp.replace('.',decimal);
			}
		}

		if(strtemp.contains(String.valueOf(temp)))
			strtemp = strtemp.replace(temp,group);
			
		return strtemp;
	}

	
	public static String niceInput_Time(double time, XMLParser myXMLParser) {
		return niceInput_Time(time, myXMLParser, null);
	}
	
	public static String niceInput_Length(double var, XMLParser myXMLParser) {
		return niceInput_Length(var, myXMLParser, df_l);
	}
	public static String niceInput_Length(long var, XMLParser myXMLParser) {
		return niceInput_Length(var, myXMLParser, df_l);
	}
	public static String niceInput_UnsignedLength(double var, XMLParser myXMLParser) {
		return niceInput_Length(var, myXMLParser, df_l_unsigned);
	}
	
	public static String niceInput_Speed(double var, XMLParser myXMLParser) {
		return niceInput_Speed(var, myXMLParser, df_s, df_c);
	}
	public static String niceInput_UnsignedSpeed(double var, XMLParser myXMLParser) {
		return niceInput_Speed(var, myXMLParser, df_l_unsigned, df_c_unsigned);
	}
	
	public static String niceInput_Mass(double var, XMLParser myXMLParser) {
		return niceInput_Mass(var, myXMLParser, df_l_unsigned);
	}
	public static String niceInput_Density(double var, XMLParser myXMLParser) {
		return niceInput_Density(var, myXMLParser, df_l_unsigned);
	}
}
