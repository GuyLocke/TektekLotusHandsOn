package sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;

/**
 * Utility class for importing sample data.
 * @author priand
 */
public class SampleDataUtil {
	
	private static final boolean TRACE = false;
	
	public static String[] readLastNameEntries() throws IOException {
		return readFile("lastnames.txt",true);
	}
	
	public static String[] readFirstNameEntries() throws IOException {
		System.out.println("readFirstNames() is called.");
		List<String> entries = new ArrayList<String>(512);
		readFile("firstnames_male.txt",entries,true);
		readFile("firstnames_female.txt",entries,true);
		return entries.toArray(new String[entries.size()]);
	}
	
	public static String getPrimary(String s) throws IOException {
		int pos = s.indexOf(',');
		if(pos>=0) {
			return s.substring(0,pos);
		}
		return capitalize(s);
	}
	public static String getAlternate(String s) throws IOException {
		int pos = s.indexOf(',');
		if(pos>=0) {
			return s.substring(pos+1);
		}
		return "";
	}
	
	public static String[] readCityEntries() throws IOException {
		return readFile("cities.txt",false);
	}
	
	public static String getPrefecture(String s) throws IOException {
		int pos = s.indexOf(',');
		if(pos>=0) {
			return s.substring(0,pos);
		}
		return capitalize(s);
	}
	public static String getCity(String s) throws IOException {
		int pos = s.indexOf(',');
		if(pos>=0) {
			return s.substring(pos+1);
		}
		return "";
	}
	
	// ===============================================================
	// Actual file reading
	// ===============================================================

	// Read a file
	public static String[] readFile(String resourceName, boolean capitalize) throws IOException {
		List<String> entries = new ArrayList<String>(512);
		readFile(resourceName, entries,capitalize);
		return entries.toArray(new String[entries.size()]);
	}

	
	// Read a file
	public static void readFile(String resourceName, List<String> entries, boolean capitalize) throws IOException {
		readFromClassResource(resourceName, entries, capitalize);
	}
	
	// Read a file from a class resource in the classpath
	private static void readFromClassResource(String resourceName, List<String> entries, boolean capitalize) throws IOException {
		if(TRACE) {System.out.println("Reading: "+resourceName);}
		InputStream is = SampleDataUtil.class.getResourceAsStream(resourceName);
		if(is!=null) {
			if(TRACE) {
				System.out.println("Find the following resource: "+resourceName);
				System.out.println(" " + is.getClass().getName()); 
			}
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String s;
			    while ((s = r.readLine()) != null)   {
			    	if(StringUtil.isNotEmpty(s)) {
			    		entries.add(capitalize?capitalize(s):s);
			    	}
			    }
			} finally {
				is.close();
			}
		} else {
			if(TRACE) {System.out.println("Can not find the following resource: "+resourceName);}
		}
	}

	// Read a file from a resource in the NSF
	@SuppressWarnings("unused")
	private  static void readFromFileResource(String resourceName, List<String> entries, boolean capitalize) throws IOException {
		if(TRACE) {System.out.println("Reading: "+resourceName);}
		InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(resourceName);
		if(is!=null) {
			if(TRACE) {System.out.println("Find the following resource: "+resourceName);}
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String s;
			    while ((s = r.readLine()) != null)   {
			    	if(StringUtil.isNotEmpty(s)) {
			    		entries.add(capitalize?capitalize(s):s);
			    	}
			    }
			} finally {
				is.close();
			}
		} else {
			if(TRACE) {System.out.println("Can not find the following resource:"+resourceName);}
		}
	}
	
	// Make the first letter of words capitalized
	private static String capitalize(String s) {
		boolean cap = true;
		StringBuilder b = new StringBuilder();
		int sz = s.length();
		for(int i=0; i<sz; i++) {
			char ch = s.charAt(i);
			if(Character.isSpaceChar(ch)) {
				cap = true;
				b.append(ch);
			} else {
				ch = cap ? Character.toUpperCase(ch) : Character.toLowerCase(ch);
				cap = false;
				b.append(ch);
			}
		}
		return b.toString();
	}
}
