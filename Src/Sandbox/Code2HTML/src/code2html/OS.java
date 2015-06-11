package code2html;
import java.awt.Toolkit;

public class OS
{

    public OS()
    {
    }

    public static boolean isMacOSX()
    {
        return osIsMacOsX;
    }

    public static boolean isWindows()
    {
        return osIsWindows;
    }

    public static boolean isWindowsXP()
    {
        return osIsWindowsXP;
    }

    public static boolean isWindows2003()
    {
        return osIsWindows2003;
    }

    public static boolean isLinux()
    {
        return osIsLinux;
    }

    public static boolean isUsingWindowsVisualStyles()
    {
        if(!isWindows())
            return false;
        boolean xpthemeActive = Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive"));
        if(!xpthemeActive)
            return false;
        if(System.getProperty("swing.noxp") != null)
            return true;
		return xpthemeActive;
    }

    private static final boolean osIsMacOsX;
    private static final boolean osIsWindows;
    private static final boolean osIsWindowsXP;
    private static final boolean osIsWindows2003;
    private static final boolean osIsLinux;

    static 
    {
        String os = System.getProperty("os.name").toLowerCase();
        osIsMacOsX = "mac os x".equals(os);
        osIsWindows = os.indexOf("windows") != -1;
        osIsWindowsXP = "windows xp".equals(os);
        osIsWindows2003 = "windows 2003".equals(os);
        osIsLinux = "linux".equals(os);
    }
}
