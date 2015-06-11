package code2html;
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil
{

    public static String getRealFilePath(String resourcePath)
    {
        URL inputURL = StringUtil.class.getResource(resourcePath);
        String filePath = inputURL.getFile();
        if(OS.isWindows() && filePath.startsWith("/"))
            filePath = filePath.substring(1);
        return filePath;
    }

}
