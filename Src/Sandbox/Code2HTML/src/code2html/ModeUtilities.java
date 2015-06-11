package code2html;

import java.io.*;
import java.net.URL;
import java.util.Vector;

import com.microstar.xml.*;

public class ModeUtilities
{
    private static Vector modes = new Vector(50);


    //{{{ getMode() method
    /**
     * Returns the edit mode with the specified name.
     * @param name The edit mode
     */
    public static Mode getMode(String name)
    {
        for(int i = 0; i < modes.size(); i++)
        {
            Mode mode = (Mode)modes.elementAt(i);
            if(mode.getName().equals(name))
                return mode;
        }
        return null;
    } //}}}


    //{{{ getModes() method
    /**
     * Returns an array of installed edit modes.
     */
    public static Mode[] getModes()
    {
        Mode[] array = new Mode[modes.size()];
        modes.copyInto(array);
        return array;
    } //}}}
}

