package code2html;

import javax.swing.text.Segment;

public interface TokenHandler
{

    public abstract void handleToken(Segment segment, byte byte0, int i, int j, TokenMarker.LineContext linecontext);

//    public abstract void setLineContext(TokenMarker.LineContext linecontext);
}
