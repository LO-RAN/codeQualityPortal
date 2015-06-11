package com.compuware.caqs.presentation.util;

import com.compuware.caqs.domain.dataschemas.settings.Settings;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.SettingsSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class RequestUtil {

    private static final String USER_SELECTED_THEME = "userSelectedTheme";
    private static final String USER_SELECTED_MSG_LOC = "userSelectedMsgLoc";
    private static final String USER_SELECTED_STARTING_PAGE = "userSelectedStartingPage";
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static Locale getLocale(HttpServletRequest request) {
        Locale locale = null;
        if (request != null) {
            HttpSession session = request.getSession();
            locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
            if (locale == null) {
                locale = request.getLocale();
            }
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static ResourceBundle getCaqsResourceBundle(HttpServletRequest request) {
        return ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", RequestUtil.getLocale(request));
    }

    /**
     * <p>Return the default message resources for the current module.</p>
     *
     * @param request The servlet request we are processing
     * @return The default message resources for the current module.
     * @since Struts 1.1
     */
    public static MessageResources getResources(HttpServletRequest request) {
        return ((MessageResources) request.getAttribute(Globals.MESSAGES_KEY));
    }
    public static final String USER_SESSION_ATTRIBUTE_NAME = "user";

    public static Users getConnectedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Users) session.getAttribute(USER_SESSION_ATTRIBUTE_NAME);
    }

    public static String getConnectedUserId(HttpServletRequest request) {
        String userId = null;
        Users user = getConnectedUser(request);
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

    public static String formatDate(Date value, HttpServletRequest request) {
        String result = null;
        if (value != null) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, getLocale(request));
            result = df.format(value);
        }
        return result;
    }

    public static String getUsedThemeForConnectedUser(HttpServletRequest request) {
        String selectedTheme = (String) request.getSession().getAttribute(RequestUtil.USER_SELECTED_THEME);
        if (selectedTheme == null) {
            String userId = RequestUtil.getConnectedUserId(request);
            SettingsSvc settingsSvc = SettingsSvc.getInstance();
            selectedTheme = settingsSvc.getPreferenceForUser(Settings.COLOR_THEME, userId);
            request.getSession().setAttribute(RequestUtil.USER_SELECTED_THEME, selectedTheme);
        }
        return selectedTheme;
    }

    public static String getMessageLocationForConnectedUser(HttpServletRequest request) {
        String selectedTheme = (String) request.getSession().getAttribute(RequestUtil.USER_SELECTED_MSG_LOC);
        if (selectedTheme == null) {
            String userId = RequestUtil.getConnectedUserId(request);
            SettingsSvc settingsSvc = SettingsSvc.getInstance();
            selectedTheme = settingsSvc.getPreferenceForUser(Settings.MESSAGE_LOCATION, userId);
            request.getSession().setAttribute(RequestUtil.USER_SELECTED_MSG_LOC, selectedTheme);
        }
        return selectedTheme;
    }

    public static void updateMessageLocationForUser(String msgLoc,
            HttpServletRequest request) {
        request.getSession().setAttribute(RequestUtil.USER_SELECTED_MSG_LOC, msgLoc);
    }

    public static String getStartingPageForConnectedUser(HttpServletRequest request) {
        String startingPage = (String) request.getSession().getAttribute(RequestUtil.USER_SELECTED_STARTING_PAGE);
        if (startingPage == null) {
            String userId = RequestUtil.getConnectedUserId(request);
            SettingsSvc settingsSvc = SettingsSvc.getInstance();
            startingPage = settingsSvc.getPreferenceForUser(Settings.STARTING_PAGE, userId);
            request.getSession().setAttribute(RequestUtil.USER_SELECTED_STARTING_PAGE, startingPage);
        }
        return startingPage;
    }

    public static void updateStartingPageForUser(String startingPage,
            HttpServletRequest request) {
        request.getSession().setAttribute(RequestUtil.USER_SELECTED_STARTING_PAGE, startingPage);
    }

    public static void updateSelectedThemeForUser(String newTheme,
            HttpServletRequest request) {
        request.getSession().setAttribute(RequestUtil.USER_SELECTED_THEME, newTheme);
    }

    public static int getIntParam(HttpServletRequest request, String paramName, int defaultValue) {
        String param = request.getParameter(paramName);
        int startIndex = defaultValue;
        if (param != null && !"".equals(param)) {
            try {
                startIndex = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                //nothing to do
                logger.error("The parameter sent is not a number : number=" +
                        param + ", paramName=" + paramName, e);
            }
        }
        return startIndex;
    }

    public static double getDoubleParam(HttpServletRequest request, String paramName, double defaultValue) {
        String param = request.getParameter(paramName);
        double startIndex = defaultValue;
        if (param != null && !"".equals(param)) {
            try {
                startIndex = Double.parseDouble(param);
            } catch (NumberFormatException e) {
                //nothing to do
                logger.error("The parameter sent is not a number : number=" +
                        param + ", paramName=" + paramName, e);
            }
        }
        return startIndex;
    }

    public static Timestamp getTimestampParam(HttpServletRequest request, String paramName, SimpleDateFormat dateFormat) {
        String date = request.getParameter(paramName);
        Timestamp t = null;
        try {
            if (date != null && !"".equals(date)) {
                t = new Timestamp(dateFormat.parse(date).getTime());
            }
        } catch (java.text.ParseException exc) {
            logger.debug("impossible to parse timestamp : ", exc);
        }
        return t;
    }

    public static boolean getBooleanParam(HttpServletRequest request, String paramName, boolean defaultValue) {
        String start = request.getParameter(paramName);
        boolean startIndex = defaultValue;
        if (start != null) {
            startIndex = Boolean.parseBoolean(start);
        }
        return startIndex;
    }

    public static Properties getWebResources(HttpSession session) {
        Properties dbProps = (Properties) session.getAttribute("webResources");
        if (dbProps == null) {
            InputStream is = RequestUtil.class.getResourceAsStream("/WebResources.properties");
            dbProps = new Properties();
            try {
                dbProps.load(is);
                session.setAttribute("webResources", dbProps);
            } catch (Exception e) {
                logger.error("CheckResources.getWebResources", e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    logger.error("CheckResources.getWebResources", e);
                }
            }
        }
        return dbProps;
    }
}
