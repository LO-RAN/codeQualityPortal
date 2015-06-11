package com.compuware.caqs.security.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.exception.TooManyUserException;
import com.compuware.caqs.security.auth.exception.UserAlreadyConnectedException;
import com.compuware.caqs.security.license.License;
import com.compuware.caqs.security.license.exception.InvalidMacAddressException;
import com.compuware.caqs.security.license.exception.LicenseExpiredException;
import com.compuware.caqs.security.stats.ConnectionStatData;
import com.compuware.caqs.security.stats.ConnectionStats;
import com.compuware.caqs.service.PortalUserSvc;
import com.compuware.caqs.service.SecuritySvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author cwfr-fdubois
 *
 */
public class AuthorizationFilter implements Filter {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private String timeoutPage;

    /**Filter should be configured with an system timeout page.*/
    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig != null) {
            timeoutPage = filterConfig.getInitParameter("timeoutpage");
        }
    }

    /**
     * Obtain user from current session and invoke a singleton AuthorizationManager to determine if
     * user is authorized for the requested resource.  If not, forward them to a standard error page.
     * @param request the servlet request.
     * @param response the servlet response.
     * @param chain the filter chain.
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session.getAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME)
                == null) {
            UserDetails user = (UserDetails) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
            GrantedAuthority[] grantedAuthArray = (SecurityContextHolder.getContext()).getAuthentication().getAuthorities();
            UserData userData = new UserData(user.getUsername(), session.getId(), session.getId(), session);
            Collection<String> userRoles = getRoles(grantedAuthArray);
            initUser(user, userRoles, userData, request, response, session);
        }
        chain.doFilter(request, response);

    }

    public void destroy() {
    }

    /**
     * Init the user information in session.
     * @param user the authenticated user.
     * @param userRoles the user roles.
     * @param usrData the exchange user data.
     * @param request the servlet request.
     * @param response the servlet response.
     * @param session the http session.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void initUser(UserDetails user, Collection<String> userRoles, UserData usrData, ServletRequest request, ServletResponse response, HttpSession session)
            throws ServletException, IOException {
        License lic = (License) session.getServletContext().getAttribute("caqsLicense");
        if (lic != null) {
            try {
                Users newUser = createUserInfos(user, userRoles, usrData);
                SessionManager.getInstance().addUser(usrData);
                ConnectionStatData stats = ConnectionStats.getInstance().getStatData();
                SecuritySvc securitySvc = SecuritySvc.getInstance();
                securitySvc.updateStatistics(stats);
                session.setAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME, newUser);
            } catch (TooManyUserException e) {
                request.getRequestDispatcher("tooManyUsers.do").forward(request, response);
            } catch (UserAlreadyConnectedException e) {
                request.getRequestDispatcher("userAlreadyConnected.do").forward(request, response);
            } catch (InvalidMacAddressException e) {
                request.getRequestDispatcher("invalidMacAddress.do").forward(request, response);
            } catch (LicenseExpiredException e) {
                request.getRequestDispatcher("licenseExpired.do").forward(request, response);
            } catch (CaqsException e) {
                request.getRequestDispatcher("unexpectedException.do").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("invalidLicense.do").forward(request, response);
        }
    }

    /** Positionne les infos de l'utilisateur donn�
     * @param user the connected user.
     * @param userRoles the user roles.
     * @param userData the exchange user data.
     * @throws CaqsException if a portal access error occured.
     */
    private Users createUserInfos(UserDetails user, Collection<String> userRoles, UserData userData) throws CaqsException {
        PortalUserSvc portalSvc = PortalUserSvc.getInstance();
        Users result = portalSvc.getUserInfos(user.getUsername());
        logger.debug("setUserInfos(" + result.getId() + ")");
        result.setCookie(userData.getCookie());
        SecuritySvc securitySvc = SecuritySvc.getInstance();

        // touch user record with system date time
        securitySvc.udpateLastLoginTime(user.getUsername());

        result.setAccessRights(securitySvc.retrieveAccessRightFromGroups(userRoles));
        result.setRoles(securitySvc.retrieveRolesFromGroups(userRoles));
        return result;
    }

    /**
     * Extract roles from granted authotorities.
     * @param userAuthorities user authorities.
     * @return the collection of associated roles.
     */
    private Collection<String> getRoles(GrantedAuthority[] userAuthorities) {
        Collection<String> result = null;
        if (userAuthorities != null) {
            result = new ArrayList<String>(userAuthorities.length);
            for (GrantedAuthority group : userAuthorities) {
                result.add(group.getAuthority());
            }
        }
        return result;
    }

    /** Forwards to timeout page.
     */
    private void returnTimeout(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(timeoutPage).forward(request, response);
    }
}
