package code2html;
import com.microstar.xml.XmlException;
import com.microstar.xml.XmlHandler;
import com.microstar.xml.XmlParser;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.text.Segment;

public class ModeLoader {

	private ModeLoader() {
	}

	public static void parseTokens(String string, String languageType,
			TokenHandler tokenHandler) {
		TokenMarker marker = getMode(languageType).getTokenMarker();
		StringTokenizer tokenizer = new StringTokenizer(string,"\r\n");
		char lineChars[];
		for (TokenMarker.LineContext context = null; tokenizer
				.hasMoreElements(); context = marker.markTokens(context,
				tokenHandler, new Segment(lineChars, 0, lineChars.length))) {
			String line = tokenizer.nextElement().toString() + "\n";
			lineChars = line.toCharArray();
		}

	}

	public static void parseTokens(String string, Mode mode,
			TokenHandler tokenHandler) {
		TokenMarker marker = mode.getTokenMarker();
		StringTokenizer tokenizer = new StringTokenizer(string, "\r\n");
		char lineChars[];
		for (TokenMarker.LineContext context = null; 
		      tokenizer.hasMoreElements(); 
		      context = marker.markTokens(context, tokenHandler, new Segment(lineChars, 0, lineChars.length)))
     		{
			String line = tokenizer.nextElement().toString() + "\n";
			lineChars = line.toCharArray();
	    	}

	}

	static void loadMode(Mode mode) {
		String oldfileName = mode.getProperty("file").toString();
		if (oldfileName.startsWith("file:/"))
			oldfileName = oldfileName.substring("file:/".length(), oldfileName
					.length());
		String fileName = oldfileName;
		XmlParser parser = new XmlParser();
		XModeHandler xmh = new MyModeHandler(fileName, parser);
		mode.setTokenMarker(xmh.getTokenMarker());
		Reader grammar = null;
		parser.setHandler((XmlHandler) xmh);
		try {
			grammar = new BufferedReader(new FileReader(fileName));
			parser.parse(null, null, grammar);
			mode.setProperties(xmh.getModeProperties());
		} catch (Throwable e) {
			if (e instanceof XmlException)
				e.printStackTrace();
		} finally {
			try {
				if (grammar != null)
					grammar.close();
			} catch (IOException ioexception) {
			}
		}
		return;
	}

	public static void loadModeCatalog(String path, boolean resource) {
		if (catalogLoaded)
			return;
		ModeCatalogHandler handler = new ModeCatalogHandler(
				getParentOfPath(path), resource);
		XmlParser parser = new XmlParser();
		parser.setHandler(handler);
		Reader in = null;
		try {
			java.io.InputStream _in;
			if (resource)
				_in = ModeLoader.class.getResourceAsStream(path);
			else
				_in = new FileInputStream(path);
			in = new BufferedReader(new InputStreamReader(_in));
			parser.parse(null, null, in);
			catalogLoaded = true;
		} catch (XmlException xe) {
			String message = xe.getMessage();
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ioexception) {
			}
		}
		return;
	}

	public static Mode getMode(String name) {
		for (int i = 0; i < modes.size(); i++) {
			Mode mode = (Mode) modes.elementAt(i);
			if (mode.getName().equals(name))
				return mode;
		}

		return null;
	}

	public static void addMode(Mode mode) {
		modes.addElement(mode);
	}

	public static String getParentOfPath(String path) {
		int lastIndex;
		for (lastIndex = path.length() - 1; 
		     lastIndex > 0 && (path.charAt(lastIndex) == File.separatorChar); 
		     lastIndex--)
			;
		int count = Math.max(0, lastIndex);
		int index = path.lastIndexOf(File.separatorChar, count);
		if (index == -1)
			index = path.lastIndexOf(File.separatorChar, count);
		if (index == -1)
			index = path.lastIndexOf(File.pathSeparatorChar);
		return path.substring(0, index + 1);
	}

	private static Vector modes = new Vector(50);
	private static boolean catalogLoaded = false;


	private static class MyModeHandler extends XModeHandler
	{
    private XmlParser parser;
    private String fileName;
    
	public MyModeHandler(String fileName, XmlParser parser) {
			super(fileName);
			this.parser=parser;
	    	this.fileName=fileName;
		}

		public void error(String what, Object subst)
	    {
	        int line = parser.getLineNumber();
	        int column = parser.getColumnNumber();
	        String msg = "";
	        Object args[] = {
	            fileName, new Integer(line), new Integer(column), msg
	        };
	    }

	    public TokenMarker getTokenMarker(String modeName)
	    {
	        Mode mode = ModeLoader.getMode(modeName);
	        if(mode == null)
	            return null;
	        else
	            return mode.getTokenMarker();
	    }

	}
	
	public static Vector getModes(){
		return modes;
	}
	}
