package com.compuware.caqs.business.locking;

import java.util.HashMap;
import java.util.Map;

import com.compuware.caqs.security.auth.SessionManager;

public class LockManager {

	/**
	 * Setting the user id for the lock to <code>ALL_USERS</code> will lock for all users
	 */
	public static final String ALL_USERS = "LockManagerAllUsers";
	
	private static LockManager instance = new LockManager();
	private Map<String, String> locks = null;
	
	private LockManager() {
		this.locks = new HashMap<String, String>();
	}
	
	public static LockManager getInstance() {
		return LockManager.instance;
	}
	
	public void setLock(Locks lock, String idUser, String idElt, String sessionId) {
		synchronized (this.locks) {
			this.locks.put(lock.toString()+idElt, idUser+":"+sessionId);
		}
	}
	
	public boolean hasAccess(Locks lock, String idUser, String idElt, String sessionId) {
		boolean retour = false;
		if (idUser != null) {
			synchronized (this.locks) {
				String id = this.locks.get(lock.toString()+idElt);
				if (id==null) {
					retour = true;
				}
				else {
					String idUserInMap = idUser+":"+sessionId;
					if (idUserInMap.equals(id)) {
						retour = true;
					}
					else {
						String[] s = id.split(":");
						String otherUserId = "";
						if(s!=null && s.length>0) {
							otherUserId = s[0];
						}
						boolean stillConnected = SessionManager.getInstance().isUserStillConnected(otherUserId);
						if(!stillConnected) {
							this.locks.remove(lock.toString()+idElt);
							retour = true;
						}
					}
				}
			}
		}
		return retour;
	}
	
	public void removeLock(Locks lock, String idUser, String idElt, String sessionId) {
		synchronized (this.locks) {
			String lockingUser = this.locks.get(lock.toString()+idElt);
			String idUserInMap = idUser+":"+sessionId;
			if (lockingUser != null && lockingUser.equals(idUserInMap)) {
				this.locks.remove(lock.toString()+idElt);
			}
		}
	}

}
