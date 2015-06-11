/*
 * VerticalHeaderServlet.java
 *
 * Created on 05/02/2007 17:02:25
 */

package com.compuware.caqs.presentation.common.servlets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.i18n.UnifaceViewData;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.AnimatedGifEncoder;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 */
public class TextImageCreatorServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6458761418549983068L;
	
	/**
	 * Logger. 
	 */
	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Locale locale = RequestUtil.getLocale(request);
        
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String unifaceviewImageDirectory = dynProp.getProperty(Constants.UNIFACEVIEW_IMAGE_TMP_DIR_KEY);
        
		// configure all parameters
		String code = request.getParameter("c");

		File imageFile = new File(unifaceviewImageDirectory, code + '_' + locale.getLanguage() + ".gif");
		if (!imageFile.exists()) {
			createImage(imageFile, code, locale, request);
		}
		
		if (imageFile.exists()) {
			// set the content type and get the output stream
			response.setContentType("image/gif");
			OutputStream os = response.getOutputStream();
            try {
                byte[] result = new byte[(int)imageFile.length()];
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(imageFile));
                input.read(result);
                input.close();
                os.write(result);
                os.flush();
            } catch (IOException e) {
                logger.error("Error reading file: "+imageFile.getName(), e);
            }
		}
		
    }
    
    private static final int IMAGE_COLOR_RADIX = 16;
    private static final float IMAGE_DEFAULT_FONT_SIZE = 12.0f;

    private void createImage(File imageFile, String code, Locale locale, HttpServletRequest request) {
		Boolean isBackground = false;

		if (!imageFile.getParentFile().exists()) {
			imageFile.getParentFile().mkdirs();
		}

		UnifaceViewData uv = new UnifaceViewData();
		uv.setId(code);

		// text
		String text = uv.getLib(locale);

		// font
		String fontName = "Dialog";
		if (request.getParameter("font") != null) {
			fontName = request.getParameter("font");
		}

		// font size
		float size = IMAGE_DEFAULT_FONT_SIZE;
		if (request.getParameter("size") != null) {
			size = Float.parseFloat(request.getParameter("size"));
		}

		// background color
		Color background = Color.RED;
		if (request.getParameter("background") != null) {
			background = new Color(Integer.parseInt(request
					.getParameter("background"), IMAGE_COLOR_RADIX));
			isBackground = true;
		}

		// text color
		Color color = Color.BLACK;
		if (request.getParameter("color") != null) {
			color = new Color(Integer.parseInt(request
					.getParameter("color"), IMAGE_COLOR_RADIX));
		}

		Font font = new Font(fontName, Font.PLAIN, (int) size);
		font = font.deriveFont(size);

		FontRenderContext frc = new FontRenderContext(null, true, false);
		int width = (int) font.getStringBounds(text, frc).getWidth();
		LineMetrics lm = font.getLineMetrics(text, frc);
		int height = (int) (lm.getAscent() + lm.getDescent());

		// image width
		if (request.getParameter("width") != null) {
			width = Integer.parseInt(request.getParameter("width"));
		}

		// image height
		if (request.getParameter("height") != null) {
			height = Integer.parseInt(request.getParameter("height"));
		}

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR_PRE);
		Graphics2D g2 = image.createGraphics();
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(background);
		g2.fillRect(0, 0, width, height);
		g2.setColor(color);
		g2.setFont(font);
		g2.drawString(text, 0, lm.getAscent());
		g2.dispose();

		// output the image as gif
		AnimatedGifEncoder age = new AnimatedGifEncoder();
		age.start(imageFile.getAbsolutePath());
		if (!isBackground) {
			age.setTransparent(background);
		}
		age.addFrame(image);
		age.finish();
    }
    
    /**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Formats a gif image for UnifaceView texts";
    }

}
