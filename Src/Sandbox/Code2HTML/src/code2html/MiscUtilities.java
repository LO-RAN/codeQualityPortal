package code2html;
import java.util.Stack;

public class MiscUtilities
{

    private MiscUtilities()
    {
    }

    public static String globToRE(String glob)
    {
        Object NEG = new Object();
        Object GROUP = new Object();
        Stack state = new Stack();
        StringBuffer buf = new StringBuffer();
        boolean backslash = false;
        for(int i = 0; i < glob.length(); i++)
        {
            char c = glob.charAt(i);
            if(backslash)
            {
                buf.append('\\');
                buf.append(c);
                backslash = false;
            } else
            {
                switch(c)
                {
                case 92: // '\\'
                    backslash = true;
                    break;

                case 63: // '?'
                    buf.append('.');
                    break;

                case 40: // '('
                case 41: // ')'
                case 43: // '+'
                case 46: // '.'
                    buf.append('\\');
                    buf.append(c);
                    break;

                case 42: // '*'
                    buf.append(".*");
                    break;

                case 124: // '|'
                    if(backslash)
                        buf.append("\\|");
                    else
                        buf.append('|');
                    break;

                case 123: // '{'
                    buf.append('(');
                    if(i + 1 != glob.length() && glob.charAt(i + 1) == '!')
                    {
                        buf.append('?');
                        state.push(NEG);
                    } else
                    {
                        state.push(GROUP);
                    }
                    break;

                case 44: // ','
                    if(!state.isEmpty() && state.peek() == GROUP)
                        buf.append('|');
                    else
                        buf.append(',');
                    break;

                case 125: // '}'
                    if(!state.isEmpty())
                    {
                        buf.append(")");
                        if(state.pop() == NEG)
                            buf.append(".*");
                    } else
                    {
                        buf.append('}');
                    }
                    break;

                default:
                    buf.append(c);
                    break;
                }
            }
        }

        return buf.toString();
    }
}
