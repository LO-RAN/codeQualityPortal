package code2html;

import com.microstar.xml.HandlerBase;
import java.io.File;
import java.io.StringReader;

// Referenced classes of package studio.beansoft.syntax:
//            ModeLoader, Mode

public class ModeCatalogHandler extends HandlerBase
{

    public ModeCatalogHandler(String directory, boolean resource)
    {
        this.directory = directory;
        this.resource = resource;
    }

    public Object resolveEntity(String publicId, String systemId)
    {
        if("catalog.dtd".equals(systemId))
            return new StringReader("<!-- -->");
        else
            return null;
    }

    public void attribute(String aname, String value, boolean isSpecified)
    {
        aname = aname != null ? aname.intern() : null;
        if(aname == "NAME")
            modeName = value;
        else
        if(aname == "FILE")
        {
            if(value == null)
                System.out.println(directory + "catalog:" + " mode " + modeName + " doesn't have" + " a FILE attribute");
            else
                file = value;
        } else
        if(aname == "FILE_NAME_GLOB")
            filenameGlob = value;
        else
        if(aname == "FIRST_LINE_GLOB")
            firstlineGlob = value;
    }

    public void doctypeDecl(String name, String publicId, String systemId)
        throws Exception
    {
        if("CATALOG".equals(name) || "MODES".equals(name))
        {
            return;
        } else
        {
            System.out.println(directory + "catalog: DOCTYPE must be CATALOG");
            return;
        }
    }

    public void endElement(String name)
    {
        if(name.equals("MODE"))
        {
            Mode mode = ModeLoader.getMode(modeName);
            if(mode == null)
            {
                mode = new Mode(modeName);
                ModeLoader.addMode(mode);
            }
            Object path;
            if(resource)
                path = ModeLoader.class.getResource(directory + file);
            else
                path = constructPath(directory, file);
            mode.setProperty("file", path);
            if(filenameGlob != null)
                mode.setProperty("filenameGlob", filenameGlob);
            else
                mode.unsetProperty("filenameGlob");
            if(firstlineGlob != null)
                mode.setProperty("firstlineGlob", firstlineGlob);
            else
                mode.unsetProperty("firstlineGlob");
            mode.init();
            modeName = file = filenameGlob = firstlineGlob = null;
        }
    }

    private Object constructPath(String directory, String file)
    {
        return (new File(directory, file)).getAbsolutePath();
    }

    private String directory;
    private boolean resource;
    private String modeName;
    private String file;
    private String filenameGlob;
    private String firstlineGlob;
}
