package jgravsim;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Model {
	
	public static final int revision = 6;
	public static final boolean DEBUG = false;
	public static final String FILE_ENDING = "wpt";
	public static final String FILE_ENDING_GZIP = "gz";
	public static final String FILE_ENDING_ZIP = "zip";
	public static final String FILE_TEMP = "temp."+FILE_ENDING;
	public static final String FILE_PERCENT = "percent.tmp";
	public static final String FILE_EXE = "cgravsim";
	
	Vector<Masspoint> stDataset = null;
	DynamicWPTLoader dynamicLoader = null;
	
	public static final String DELIMSTEP = "#";
	public static final String DELIMLINE = "\r\n";
	public static final String DELIMDATA = ";";
	public static final int VALUES_HEADER = 3;			//number of values on the main header
	public static final int VALUES_STEP = 2;			//number of values on each step header
	public static final int VALUES_DATA = 9;			//number of values on each masspoint line
	public static final int DATAPRECISION = 20;
	
	public static final int INFILE_NOERROR = -1;
	public static final int INFILE_FILENOTFOUND = -2;
	public static final int INFILE_READERROR = -3;
	public static final int INFILE_EOFSTARTERROR = -4;
	public static final int INFILE_EOFSTEPERROR = -5;
	public static final int INFILE_EOFOBJERROR = -6;
	public static final int INFILE_WPTERROR = -7;
	public static final int INFILE_ACCESSERROR = -8;
	public static final int INFILE_WRITEERROR = -9;
	public static final int INFILE_DIRECTORY = -10;

	public String filename;
	private File file_loaded;
	public static String exe_filename = null;

	int istep;
	double dtimeCount;
	double ddataCount;
	
	Model() {
		istep = 0;
		//file = new File(Defaultname);
	}
	
	public int writetempHeader(double datacount, double timecount) {
		istep = 0;
		//file = new File(Defaultname);
		
		//check if file is really deleted
		int idelete = deleteFile(FILE_TEMP);
		if(idelete != Model.INFILE_NOERROR)
			return idelete;
		
		filename = FILE_TEMP;
		String version = String.valueOf(Controller.WPT_VERSION);
		String steps = String.valueOf((long)(datacount/timecount)+1);
		String steptime = String.valueOf(timecount);
		writetempout(version+DELIMDATA+steps+DELIMDATA+steptime+DELIMLINE, filename);
		return INFILE_NOERROR;
	}
	
	public int correctHeader(File infile, int newSteps) {
		try {
			FileReader fr;
			RandomAccessFile rafile = null;
			BufferedReader br;
			String sCurLine;
			String[] saCurLine;
			fr = new FileReader(infile);
			rafile = new RandomAccessFile(infile, "rw");
			br = new BufferedReader(fr);
			
			sCurLine = br.readLine(); /* read the first line */
			//new FileWriter(infile);
			
			if(sCurLine == null) {
				br.close();
				return INFILE_EOFSTARTERROR;
			}
			
			saCurLine = sCurLine.split(DELIMDATA);
			if(saCurLine.length != VALUES_HEADER) {
				br.close();
				return INFILE_EOFSTARTERROR;
			}

			//String snewCurLine = sCurLine.replaceFirst(saCurLine[1], String.valueOf(newSteps));
			String snewSteps = String.valueOf(newSteps);
			debugout("correcttempHeader() - newSteps("+snewSteps.length()+")="+snewSteps+", saCurLine[1]("+saCurLine[1].length()+")="+saCurLine[1]);
			while(snewSteps.length() < saCurLine[1].length()) {
				debugout("correcttempHeader()-loop-newSteps ="+snewSteps+", saCurLine[1]="+saCurLine[1]);
				snewSteps = "0"+snewSteps;
			}
			
			String snewCurLine = saCurLine[0]+DELIMDATA+snewSteps+DELIMDATA+saCurLine[2]+DELIMLINE;//sCurLine.substring(sCurLine.indexOf(';')+1,sCurLine.lastIndexOf(';'));
			debugout("correcttempHeader() - folder="+snewCurLine);
			
				try {
					rafile.writeBytes(snewCurLine);
					///if(snewCurLine.length() < sCurLine.length())
						//rafile.write("",snewCurLine.length(),sCurLine.length());
				}		
				catch ( IOException e ) {
					e.printStackTrace();
				}
				finally {
					if ( rafile != null ) {
						try { rafile.close(); } 
						catch ( IOException e ) { e.printStackTrace(); }
					}
				}
			br.close();
			fr.close();
		} catch(FileNotFoundException e) {
			return INFILE_FILENOTFOUND;
		} catch(IOException e) {
			return INFILE_READERROR;
		}
		return INFILE_NOERROR; /* no error detected */	
	}
	
	/**
	 * Returns the last complete step
	 * @param infile wpt file
	 * @return max(0, last step found minus 1)
	 */
	public int findlaststep(File infile) {
		debugout("findlaststep() - Looking for last step...");
		int step = 0;
		try {
			FileReader fr;
			BufferedReader br;
			String sCurLine;

			fr = new FileReader(infile);	
			br = new BufferedReader(fr);
			
			//check all lines for the istep count we are looking for
			while( (sCurLine = br.readLine()) != null ) {
				int[] iStepData = parseStep(sCurLine);
				if(iStepData == null)
					break;
				else if(iStepData[0] < 0)
					continue;
				else
					step = iStepData[0];
			}

			br.close();
			fr.close();
		} catch(FileNotFoundException e) {
			debugout("readfile() - INFILE_FILENOTFOUND "+infile);
			return Model.INFILE_FILENOTFOUND;
		} catch(IOException e) {
			debugout("readfile() - INFILE_READERROR!");
			return Model.INFILE_READERROR;
		}
			
		debugout("findlaststep() - Last step found "+step);
		if(step == 0)
			return 0;
		else
			return step-1;
	}
	
	/* 
	 * [Titel (string / max 100 zeichen)];[Anzahl der Schritte (int)];[Empfohlene Zeit zwischen jedem Schritt in ms (int)]
     * #[Schrittnummer (int)];[Objektanzahl (int)]
     * [Objekt ID];[Masse];[Radius];[Geschw.Vektor x];[Geschw.Vektor y];[Geschw.Vektor z];[Beschl.Vektor x];[Beschl.Vektor y];[Beschl.Vektor z];[Position x];[Position y];[Position z]
     */
	@SuppressWarnings("unused")
	private	static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
	
	public static InputStream loadInputStream(File file) {
		InputStream in = null;
		try {
			if (file.getName().endsWith(FILE_ENDING_GZIP)) {
				debugout("loadInputFile - using gzip decompression");
				in = new GZIPInputStream(new FileInputStream(file));
			} else if (file.getName().endsWith(FILE_ENDING_ZIP)) {
				debugout("loadInputFile - using zip decompression");
				ZipFile zipfile = new ZipFile(file);
				/* We only care about the first entry. This may be extended in the future */
				Enumeration<? extends ZipEntry> zipentries = zipfile.entries();
				in = zipfile.getInputStream(zipentries.nextElement());
			} else {
				debugout("loadInputFile - using no decompression");
				in = new FileInputStream(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	public int loadInputFile(File infile) {
		dynamicLoader = new DynamicWPTLoader(infile);
		int ret = dynamicLoader.init();
		dtimeCount = dynamicLoader.getTimeCount();
		return ret;
	}
	
	public void writetempout(String str, String filename) {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile( filename, "rw" );
			//Zeiger ans Ende
			file.seek( file.length() );
			//Werte einf�gen
			//file.writeUTF( str );
			file.writeBytes(str);
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
			if ( file != null ) {
				try { file.close(); } 
				catch ( IOException e ) { e.printStackTrace(); }
			}
		}
	}
	
	public void AddStep(Vector<Masspoint> OneData) {
		String stepid = String.valueOf(istep);
		istep++;
		String numofobj = String.valueOf(OneData.size());
		writetempout(DELIMSTEP+stepid+DELIMDATA+numofobj+DELIMLINE, filename);
    	//new DATAFILE output
    	//Add Data of each object to datafile
    	for(int i=0;i<OneData.size();i++) {
        	//[Objekt ID];[Masse];[Radius];[Geschw.Vektor x];[Geschw.Vektor y];[Geschw.Vektor z];
        	//[Beschl.Vektor x];[Beschl.Vektor y];[Beschl.Vektor z];[Position x];[Position y];[Position z]
    		//debugout("Model - AddStep() - Adding data to file No"+i);
    		Masspoint mp = OneData.get(i);
			String id = String.valueOf(mp.getID());
			String mass = String.valueOf(mp.getAbsMass()); 
			String radius = String.valueOf(mp.getRadius()); 
			String vx = String.valueOf(mp.getSpeedX()); 
			String vy = String.valueOf(mp.getSpeedY()); 
			String vz = String.valueOf(mp.getSpeedZ());
			String px = String.valueOf(mp.getPosX());   
			String py = String.valueOf(mp.getPosY());
			String pz = String.valueOf(mp.getPosZ());
			debugout("px="+px+" / originally="+mp.getPosX());
			debugout(id+DELIMDATA+mass+DELIMDATA+radius+DELIMDATA+vx+DELIMDATA+vy+DELIMDATA+vz+DELIMDATA+px+DELIMDATA+py+DELIMDATA+pz);	                                                                                                                                                                    
			writetempout(id+DELIMDATA+mass+DELIMDATA+radius+DELIMDATA+vx+DELIMDATA+vy+DELIMDATA+vz+DELIMDATA+px+DELIMDATA+py+DELIMDATA+pz+DELIMLINE, filename);	                                                                                                                                                                    
    	}
	}
	
	public static int deleteFile(String filename) {
	    File f = new File(filename);
	    // Make sure the file or directory exists and isn't write protected
	    if (!f.exists()) {
	      debugout("deleteFile() - no such file or directory: "+filename);
	      return Model.INFILE_FILENOTFOUND;
	    }
		    
	    if (!f.canWrite()) {
	    	debugout("deleteFile() - write protected: "+filename);
	    	return Model.INFILE_WRITEERROR;
	    }
		    
		// If it is a directory, make sure it is empty    
	    if (f.isDirectory()) {
	      String[] files = f.list();
	      if (files.length > 0) {
	        debugout("deleteFile() - directory not empty: "+filename);
	        return Model.INFILE_DIRECTORY;
	      }
	    }
	    
		//deleting successfull?
	    if(f.delete())
	    	return Model.INFILE_NOERROR;
	    else
	    	return Model.INFILE_ACCESSERROR;
	}
	
	public int loadDataset(File infile, int istep) {
		//vSteps = new Vector<Step>();
		//debugout("Parsing Inputfile: "+infile.getName());
		file_loaded = infile;
		
		try {

			InputStream in;
			InputStreamReader fr;
			BufferedReader br;
			String sCurLine;
			String[] saCurLine;
			int iCurLine = 1;
			int numObjects = 0;

			in = loadInputStream(infile);
			fr = new InputStreamReader(in);	
			br = new BufferedReader(fr);
			
			sCurLine = br.readLine(); /* read the first line */		
			
			if(sCurLine == null) {
				br.close();
				return INFILE_EOFSTARTERROR;
			}
			
			saCurLine = sCurLine.split(DELIMDATA);
			if(saCurLine.length != Model.VALUES_HEADER) {
				br.close();
				return INFILE_EOFSTARTERROR;
			}

			try {
				int iwpt = Integer.parseInt(saCurLine[0]);
				if(iwpt != Controller.WPT_VERSION) {
					debugout("init() - Found WPT version "+iwpt+", but should be "+Controller.WPT_VERSION);
					return INFILE_WPTERROR;
				}
			}
			catch(NumberFormatException e) {
				debugout("init() - Found WPT version "+saCurLine[0]+"! NumberFormatException!");
				return INFILE_WPTERROR;
			}
			
			int numSteps = Integer.parseInt(saCurLine[1]);
			dtimeCount = Double.parseDouble(saCurLine[2]);
			ddataCount = ((double)numSteps)*dtimeCount;
			
			//istep = -1 means "max" step count
			if(istep < 0)
				istep = numSteps;
			
			iCurLine++;
			
			//check all lines for the istep count we are looking for
			while( (sCurLine = br.readLine()) != null ) {
				int[] iStepData = parseStep(sCurLine);
				if(iStepData == null)
					return iCurLine;
				else if(iStepData[0] < 0)
					continue;
				else {
					if(iStepData[0] != istep)
						continue;
					else {
						numObjects = iStepData[1];
						iCurLine++;
						break;
					}
				}
			}
			if(sCurLine == null) {
				br.close();
				return INFILE_EOFSTEPERROR;
			}
				

				stDataset = new Vector<Masspoint>();
				
				for(int j=0;j<numObjects;j++) {
					sCurLine = br.readLine(); /* read the first line of the block */
	
					if(sCurLine == null) {
						br.close();
						return INFILE_EOFOBJERROR;
					}
					
					saCurLine = sCurLine.split(DELIMDATA);
					if(saCurLine.length != VALUES_DATA) {
						br.close();
						return iCurLine;
					}
					else {
						int ID = Integer.parseInt(saCurLine[0]);
						double Mass = Double.parseDouble(saCurLine[1]);
						double Radius = Double.parseDouble(saCurLine[2]);
						double SpeedX = Double.parseDouble(saCurLine[3]);
						double SpeedY = Double.parseDouble(saCurLine[4]);
						double SpeedZ = Double.parseDouble(saCurLine[5]);
						long PosX = Long.parseLong(saCurLine[6]);
						long PosY = Long.parseLong(saCurLine[7]);
						long PosZ = Long.parseLong(saCurLine[8]);
						Masspoint mptemp = new Masspoint(ID, PosX, PosY, PosZ);
						mptemp.setSpeedx(SpeedX);
						mptemp.setSpeedy(SpeedY);
						mptemp.setSpeedz(SpeedZ);
						mptemp.setAbsRadius(Radius);
						mptemp.setMass(Mass);
						stDataset.add(mptemp);
					}
					iCurLine++;
				}
				
			br.close();
		} catch(FileNotFoundException e) {
			return INFILE_FILENOTFOUND;
		} catch(IOException e) {
			return INFILE_READERROR;
		}
		return INFILE_NOERROR; /* no error detected */
	}
	

	public int loadDataset(File infile) {
		return loadDataset(infile, 0);
	}
	
	public static void copydata(InputStream instream, OutputStream outstream) throws IOException { 
		byte[] buffer = new byte[0xFFFF]; 
	 
	    for(int length; (length = instream.read(buffer)) != -1;) 
	    	outstream.write( buffer, 0, length ); 
	} 
	 
	public void saveOutputfile(File file) {
		FileInputStream fis = null;
		OutputStream os = null;

		try {
			fis = new FileInputStream(FILE_TEMP);
			FileOutputStream fos = new FileOutputStream(file);

			if (file.getName().endsWith(FILE_ENDING_GZIP)) {
				debugout("saveOutputfile - using gzip compression");
				os = new GZIPOutputStream(fos);
			} else if (file.getName().endsWith(FILE_ENDING_ZIP)) {
				debugout("saveOutputfile - using zip compression");
				os = new ZipOutputStream(fos);
				/* Strip off ".zip" of the compressed file */
				String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
				((ZipOutputStream) os).putNextEntry(new ZipEntry(name));
			} else {
				debugout("saveOutputfile - using no compression");
				os = fos;
			}
			copydata(fis, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    finally { 
	    	if ( fis != null ) 
	    		try { fis.close(); } catch ( IOException e ) { } 
	    	if ( os != null ) 
	    		try { os.close(); } catch ( IOException e ) { } 
	    } 
	}
	
	public void saveOutputfile() {
		saveOutputfile(file_loaded);
	}
	
	public int[] parseStep(String sCurLine) {
		int numObjects = -1;
		int istepid = -1;
		
		if(sCurLine.contains(DELIMSTEP)) {
			if(!sCurLine.contains(DELIMDATA))
				return null;
			istepid = Integer.valueOf(String.valueOf(sCurLine.toCharArray(), 1, sCurLine.indexOf(DELIMDATA)-1));
			String[] saCurLine = sCurLine.split(DELIMDATA);
			if(saCurLine.length != VALUES_STEP)
				return null;
			numObjects = Integer.parseInt(saCurLine[1]);
			debugout("parseStep() - We found Step #"+istepid+" (of #"+istep+") and we found "+numObjects+" number of objects");
		}
		return new int[] {istepid, numObjects};
	}
}
