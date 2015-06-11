// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.compuware.optimalview.tasklist;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

// Referenced classes of package com.compuware.optimalview.tasklist:
//            UnknownUserException, User, AuthenticationException
public class Authenticator {

    public static Authenticator getInstance() {
        if (instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    private Authenticator() {
    }

    public User getUser(HttpServletRequest httpservletrequest, MessageResources messageresources, Locale locale)
            throws AuthenticationException {
        User user = null;
        String s = null;
        s = httpservletrequest.getParameter("userId");
        if (s != null) {
            if (s.equals("[unknown]")) {
                throw new UnknownUserException();
            }
            user = new User(s, messageresources, locale);
        }
        return user;
    }
    private static Authenticator instance;
}
