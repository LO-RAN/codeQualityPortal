package com.compuware.caqs.security.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.compuware.caqs.security.auth.exception.TooManyUserException;
import com.compuware.caqs.security.auth.exception.UserAlreadyConnectedException;
import com.compuware.caqs.security.license.License;
import com.compuware.caqs.security.license.LicenseManager;
import com.compuware.caqs.security.license.exception.InvalidMacAddressException;
import com.compuware.caqs.security.license.exception.LicenseExpiredException;
import com.compuware.caqs.security.stats.ConnectionStats;

public class SessionManager {

    // déclaration du logger
    static protected Logger mLog = Logger.getLogger("Security");
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    private Map<String, UserData> userMap = new HashMap<String, UserData>();
    private int maxAllowedNbUser = 0;
    private ConnectionStats stats = ConnectionStats.getInstance();
    private static SessionManager singleton = new SessionManager();

    public static SessionManager getInstance() {
        return SessionManager.singleton;
    }

    private SessionManager() {
        maxAllowedNbUser = getMaxAllowedNbUser();
    }

    public void removeUser(String cookie) {
        UserData usrData = null;
        w.lock();
        try {
            usrData = userMap.get(cookie);
            if (usrData != null) {
                userMap.remove(cookie);
                usrData.cleanSessions();
                mLog.info("Disconnecting user:" + usrData.getId());
                mLog.info("Total users:" + getTotalUser());
                stats.decUser();
            }
        } finally {
            w.unlock();
        }
    }

    public boolean isUserStillConnected(String userId) {
        boolean retour = false;
        if (userId != null && !"".equals(userId)) {
            for (Map.Entry<String, UserData> entry : this.userMap.entrySet()) {
                UserData user = entry.getValue();
                if (userId.equals(user.getId())) {
                    retour = true;
                    break;
                }
            }
        }
        return retour;
    }

    public int getTotalUser() {
        int result = 0;
        r.lock();
        try {
            result = userMap.size();
        } finally {
            r.unlock();
        }
        return result;
    }

    public List<UserData> getUserList() {
        List<UserData> result = new ArrayList<UserData>();
        r.lock();
        try {
            result.addAll(userMap.values());
        } finally {
            r.unlock();
        }
        return result;
    }

    public boolean addUser(UserData user) throws TooManyUserException, UserAlreadyConnectedException, InvalidMacAddressException, LicenseExpiredException {
        boolean result = false;
        LicenseManager.getInstance().checkLicense();
        w.lock();
        try {
            if (userMap.containsKey(user.getCookie())) {
                UserData existingUser = userMap.get(user.getCookie());
                result = true;
                stats.incUser(true);
                existingUser.addSessions(user.getSessionMap());
            } else if (userMap.size() < maxAllowedNbUser) {
                userMap.put(user.getCookie(), user);
                stats.incUser(false);
                result = true;
            } else {
                throw new TooManyUserException();
            }
        } finally {
            w.unlock();
        }
        if (result) {
            mLog.info("Connecting user: " + user.getId() + ", cookie="
                    + user.getCookie());
            mLog.info("Total users:" + getTotalUser());
        }
        return result;
    }

    public void updateUserActivity(String cookie) {
        w.lock();
        try {
            if (userMap.containsKey(cookie)) {
                UserData existingUser = userMap.get(cookie);
                existingUser.setLastAccessDate(new Date());
            }
        } finally {
            w.unlock();
        }
    }

    private int getMaxAllowedNbUser() {
        int result = 0;
        LicenseManager lm = LicenseManager.getInstance();
        License lic = lm.getLicense();
        if (lic != null) {
            result = lic.getMaxConcurrentUsers();
        }
        return result;
    }
}
