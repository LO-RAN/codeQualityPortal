/*
 * VerticalHeaderServlet.java
 *
 * Created on 05/02/2007 17:02:25
 */

package com.compuware.caqs.presentation.common.servlets;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import java.text.BreakIterator;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-lizac
 */
public class VerticalHeaderServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656685055707209067L;

	/**
	 * 
	 */
	private static org.apache.log4j.Logger logger = LoggerManager
			.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

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

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			// configure all parameters

			// text 
			String text = "text not set";
			if (request.getParameter("text") != null) {
				text = request.getParameter("text");
                //text = new String ( text.getBytes(), "UTF-8" );
			}

			// font
			String fontName = "Dialog";
			if (request.getParameter("font") != null) {
				fontName = request.getParameter("font");
			}

			// font size 
			float size = 12.0f;
			if (request.getParameter("size") != null) {
				size = Float.parseFloat(request.getParameter("size"));
			}

			// background color
			Color background = Color.LIGHT_GRAY;
			if (request.getParameter("background") != null) {
				background = new Color(Integer.parseInt(request
						.getParameter("background"), 16));
			}

			// text color
			Color color = Color.BLACK;
			if (request.getParameter("color") != null) {
				color = new Color(Integer.parseInt(request
						.getParameter("color"), 16));
			}

			//Font font = Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(font_file));
			Font font = new Font(fontName, Font.PLAIN, (int) size);
			font = font.deriveFont(size);

			// image width
			int width = 50;
			if (request.getParameter("width") != null) {
				width = Integer.parseInt(request.getParameter("width"));
			}

			// image height
			int height = 120;
			if (request.getParameter("height") != null) {
				width = Integer.parseInt(request.getParameter("height"));
			}

			BufferedImage buffer = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = buffer.createGraphics();

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			GradientPaint gp = new GradientPaint(20, 0, background, 20, 20,
					Color.WHITE);
			g2.setPaint(gp);
			g2.fillRect(0, 0, width, height);

			//g2.setColor(background);
			//g2.fillRect(0,0,width,height);
			g2.setColor(color);

			g2.rotate(Math.PI / 2);

			AttributedString as = new AttributedString(text);
			as.addAttribute(TextAttribute.FONT, font);
            //as.addAttribute(TextAttribute., as)
			AttributedCharacterIterator aci = as.getIterator();

			FontRenderContext frc = g2.getFontRenderContext();
			LineBreakMeasurer lbm = new LineBreakMeasurer(aci,
                    BreakIterator.getWordInstance(),
                    frc);
			//Insets insets = getInsets();
			float wrappingWidth = height;
			float x = 1;
			float y = 1 - width;

			while (lbm.getPosition() < aci.getEndIndex()) {
				TextLayout textLayout = lbm.nextLayout(wrappingWidth);
				y += textLayout.getAscent();
				textLayout.draw(g2, x, y);
				y += textLayout.getDescent() + textLayout.getLeading();
				x = 1;
			}

			// set the content type and get the output stream
			response.setContentType("image/png");
			OutputStream os = response.getOutputStream();

			// output the image as png
			ImageIO.write(buffer, "png", os);
			os.close();
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Formats an image containing a vertical text";
	}

}
