package jgravsim;

import java.io.*;
import java.util.Vector;


public class Model {

	public static final long serialVersionUID = 42 ;
	
	public static boolean DEBUG = false;
	public static final String FILE_ENDING = "wpt";
	public static final String FILE_TEMP = "temp."+FILE_ENDING;
	public static final String FILE_PERCENT = "percent.tmp";
	
	Vector<Masspoint> stDataset = null;
	DynamicWPTLoader dynamicLoader = null;
	
	public static final String DELIMSTEP = "#";
	public static final String DELIMLINE = "\r\n";
	public static final String DELIMDATA = ";";
	public static final int DATAPRECISION = 20;
	
	public static final int INFILE_NOERROR = -1;
	public static final int INFILE_FILENOTFOUND = -2;
	public static final int INFILE_READERROR = -3;
	public static final int INFILE_EOFSTARTERROR = -4;
	public static final int INFILE_EOFSTEPERROR = -5;
	public static final int INFILE_EOFOBJERROR = -6;
	String filename;

	int istep;
	double dtimeCount;
	double ddataCount;
	
	Model() {
		istep = 0;
		//file = new File(Defaultname);
	}
	
	public void writetempHeader(double datacount, double timecount) {
		istep = 0;
		//file = new File(Defaultname);
		
		//check if file is really deleted
		int idelete = deleteFile(FILE_TEMP);
		if(idelete == 1)
			debugout("writeHeader() - file has been deleted");
		else if(idelete == -1)
			debugout("writeHeader() - file doesn't exist yet");
		else 
			debugout("writeHeader() - WARNING file couldn't be deleted. ID "+idelete);
		
		filename = FILE_TEMP;
		String version = String.valueOf(Controller.WPT_VERSION);
		String steps = String.valueOf((long)(datacount/timecount)+1);
		String steptime = String.valueOf(timecount);
		writetempout(version+DELIMDATA+steps+DELIMDATA+steptime+DELIMLINE, filename);
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
			if(saCurLine.length != 3) {
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
		return step;
	}
	
	/* 
	 * [Titel (string / max 100 zeichen)];[Anzahl der Schritte (int)];[Empfohlene Zeit zwischen jedem Schritt in ms (int)]
     * #[Schrittnummer (int)];[Objektanzahl (int)]
     * [Objekt ID];[Masse];[Radius];[Geschw.Vektor x];[Geschw.Vektor y];[Geschw.Vektor z];[Beschl.Vektor x];[Beschl.Vektor y];[Beschl.Vektor z];[Position x];[Position y];[Position z]
     */
	private	static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
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
			String id = String.valueOf(mp.id);
			String mass = String.valueOf(mp.mass); 
			String radius = String.valueOf(mp.radius); 
			String vx = String.valueOf(mp.mdvunitspeed.x1 * mp.dabsspeed); 
			String vy = String.valueOf(mp.mdvunitspeed.x2 * mp.dabsspeed); 
			String vz = String.valueOf(mp.mdvunitspeed.x3 * mp.dabsspeed);  
			String ax = String.valueOf(0); //TODO not implemented yet
			String ay = String.valueOf(0); //TODO not implemented yet
			String az = String.valueOf(0); //TODO not implemented yet  
			String px = String.valueOf(mp.mlvpos.x1);   
			String py = String.valueOf(mp.mlvpos.x2);
			String pz = String.valueOf(mp.mlvpos.x3);
			debugout("px="+px+" / originally="+mp.mlvpos.x1);
			debugout(id+DELIMDATA+mass+DELIMDATA+radius+DELIMDATA+vx+DELIMDATA+vy+DELIMDATA+vz+DELIMDATA+ax+DELIMDATA+ay+DELIMDATA+az+DELIMDATA+px+DELIMDATA+py+DELIMDATA+pz);	                                                                                                                                                                    
			writetempout(id+DELIMDATA+mass+DELIMDATA+radius+DELIMDATA+vx+DELIMDATA+vy+DELIMDATA+vz+DELIMDATA+ax+DELIMDATA+ay+DELIMDATA+az+DELIMDATA+px+DELIMDATA+py+DELIMDATA+pz+DELIMLINE, filename);	                                                                                                                                                                    
    	}
	}
	
	public int deleteFile(String filename) {
	    File f = new File(filename);
	    // Make sure the file or directory exists and isn't write protected
	    if (!f.exists()) {
	      debugout("deleteFile() - no such file or directory: "+filename);
	      return -1;
	    }
		    
	    if (!f.canWrite()) {
	    	debugout("deleteFile() - write protected: "+filename);
	    	return -2;
	    }
		    
		// If it is a directory, make sure it is empty    
	    if (f.isDirectory()) {
	      String[] files = f.list();
	      if (files.length > 0) {
	        debugout("deleteFile() - directory not empty: "+filename);
	        return -3;
	      }
	    }
	    
		//deleting successfull?
	    if(f.delete())
	    	return 1;
	    else
	    	return 0;
	}
	
	public int loadDataset(File infile, int istep) {
		//vSteps = new Vector<Step>();
		//debugout("Parsing Inputfile: "+infile.getName());
		
		try {
		
			FileReader fr;
			BufferedReader br;
			String sCurLine;
			String[] saCurLine;
			int iCurLine = 1;
			int numObjects = 0;
			fr = new FileReader(infile);	
			br = new BufferedReader(fr);
			
			sCurLine = br.readLine(); /* read the first line */		
			
			if(sCurLine == null) {
				br.close();
				return INFILE_EOFSTARTERROR;
			}
			
			saCurLine = sCurLine.split(DELIMDATA);
			if(saCurLine.length != 3) {
				br.close();
				return iCurLine;
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
					if(saCurLine.length != 12) {
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
						//double AccX = Double.parseDouble(saCurLine[6]);
						//double AccY = Double.parseDouble(saCurLine[7]);
						//double AccZ = Double.parseDouble(saCurLine[8]);
						long PosX = Long.parseLong(saCurLine[9]);
						long PosY = Long.parseLong(saCurLine[10]);
						long PosZ = Long.parseLong(saCurLine[11]);
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
	
	public void copydata(InputStream instream, OutputStream outstream) throws IOException { 
		byte[] buffer = new byte[0xFFFF]; 
	 
	    for(int length; (length = instream.read(buffer)) != -1;) 
	    	outstream.write( buffer, 0, length ); 
	} 
	 
	public void saveOutputfile(File file)  { 
		FileInputStream  fis = null; 
		FileOutputStream fos = null; 
	 
	    try { 
	    	fis = new FileInputStream( FILE_TEMP ); 
	    	fos = new FileOutputStream( file ); 
	 
	    	copydata( fis, fos ); 
	    } 
	    catch ( IOException e ) { 
	    	e.printStackTrace(); 
	    } 
	    finally { 
	    	if ( fis != null ) 
	    		try { fis.close(); } catch ( IOException e ) { } 
	    	if ( fos != null ) 
	    		try { fos.close(); } catch ( IOException e ) { } 
	    } 
	}
	public int[] parseStep(String sCurLine) {
		int numObjects = -1;
		int istepid = -1;
		
		if(sCurLine.contains(DELIMSTEP)) {
			if(!sCurLine.contains(DELIMDATA))
				return null;
			istepid = Integer.valueOf(String.valueOf(sCurLine.toCharArray(), 1, sCurLine.indexOf(DELIMDATA)-1));
			String[] saCurLine = sCurLine.split(DELIMDATA);
			if(saCurLine.length != 2)
				return null;
			numObjects = Integer.parseInt(saCurLine[1]);
			debugout("parseStep() - We found Step #"+istepid+" (of #"+istep+") and we found "+numObjects+" number of objects");
		}
		return new int[] {istepid, numObjects};
	}
}
