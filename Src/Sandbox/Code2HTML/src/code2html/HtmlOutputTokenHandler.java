package code2html;

import javax.swing.text.Segment;

public class HtmlOutputTokenHandler
    implements TokenHandler
{

    public HtmlOutputTokenHandler()
    {
        buff = new StringBuffer();
    }

    public void handleToken(Segment seg, byte id, int offset, int length, TokenMarker.LineContext context)
    {
        String segText = new String(seg.array, offset, length);
        if(segText != null)
        {
            segText = segText.replaceAll("<", "&lt");
            segText = segText.replaceAll(">", "&gt");
        }
        
        
 if(id !=Token.END && id!=Token.NULL && ! segText.equals("\n")){       
        buff.append("<span class=syntax"+id+">");
        buff.append(segText).append("</span>");
 }
 else{
     buff.append(segText); 
 }

    }

    public void reset()
    {
        buff.delete(0, buff.length());
    }

    public String getOutputText()
    {
		StringBuffer output = new StringBuffer();
		int index = 0;
		int previousIndex = -1;
		int count = 0;
		String lineNumber = "";
		String spacer = "      ";
		
		while ((index = buff.indexOf("\n", previousIndex + 1)) >= 0) {
			count++;
			lineNumber = Integer.toString(count);
			lineNumber = spacer.substring(0, spacer.length()
					- lineNumber.length())
					+ lineNumber;
			output.append("\n<span class='gutter'><a name='line");
			output.append(count); 
			output.append("'>");
			output.append(lineNumber); 
			output.append(":</a></span>");
			output.append(buff.substring(previousIndex + 1, index));
			previousIndex = index;
		}
    	
    	
        return output.toString();
    }

    private StringBuffer buff;
}
