package code2html;

import java.io.IOException;
import java.io.PrintStream;

// Referenced classes of package studio.beansoft.syntax.sample:
//            HtmlOutputTokenHandler

public class HtmlSyntaxHighlighter
{

    private HtmlSyntaxHighlighter()
    {
    }

    public static String syntaxTextToHtml(String string, String languageType)
    {
        HtmlOutputTokenHandler tokenHandler = new HtmlOutputTokenHandler();
        ModeLoader.parseTokens(string, languageType, tokenHandler);
        return tokenHandler.getOutputText();
    }

}
