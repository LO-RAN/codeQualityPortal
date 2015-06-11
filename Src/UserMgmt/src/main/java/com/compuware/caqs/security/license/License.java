package com.compuware.caqs.security.license;

import java.util.Date;

public class License {

	// The license expiring date.
	private Date expiringDate;
	
	// The server MAC address.
	private String macAddress;
	
	// The maximum number of concurrent user.
	private int maxConcurrentUsers;
	
	// Is the license a trial one
	private boolean trialLicense = false;

	/**
	 * @return Returns the expiringDate.
	 */
	public Date getExpiringDate() {
		return expiringDate;
	}

	/**
	 * @param expiringDate The expiringDate to set.
	 */
	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	/**
	 * @return Returns the macAddress.
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAdress The macAddress to set.
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * @return Returns the maxConcurrentUsers.
	 */
	public int getMaxConcurrentUsers() {
		return maxConcurrentUsers;
	}

	/**
	 * @param maxConcurrentUsers The maxConcurrentUsers to set.
	 */
	public void setMaxConcurrentUsers(int maxConcurrentUsers) {
		this.maxConcurrentUsers = maxConcurrentUsers;
	}
	
	/**
	 * @return Returns the trialLicense.
	 */
	public boolean isTrialLicense() {
		return trialLicense;
	}

	/**
	 * @param trialLicense The trialLicense to set.
	 */
	public void setTrialLicense(boolean trialLicense) {
		this.trialLicense = trialLicense;
	}

	/**
	 * @param trialLicense The trialLicense to set.
	 */
	public void setTrialLicense(String trialLicense) {
		this.trialLicense = "TRIAL".equalsIgnoreCase(trialLicense);
	}

	/**
	 * Check if the given MAC is valid for license specification.
	 * used only for non trial versions.
	 * @param currentMacAddress the given MAC address.
	 * @return true if the access is allowed, false if not.
	 */
	public boolean checkMacAddress(String currentMacAddress) {
		boolean result = false;
		if (this.trialLicense) {
			result = true;
		}
		else if (currentMacAddress != null && this.macAddress != null) {
			result = this.macAddress.equals(currentMacAddress);
		}
		return result;
	}
	
	/**
	 * Check if the given date is valid for license specification.
	 * @param currentDate the given date.
	 * @return true if the access is allowed, false if not.
	 */
	public boolean checkExpiringDate(Date currentdate) {
		boolean result = false;
		if (currentdate != null && this.expiringDate != null) {
			result = this.expiringDate.after(currentdate);
		}
		return result;
	}
	
	/**
	 * Check if the given number of user is valid for license specification.
	 * @param nbUser the given number of user.
	 * @return true if the access is allowed, false if not.
	 */
	public boolean checkNbUser(int nbUser) {
		return nbUser < this.maxConcurrentUsers;
	}
	
	/**
	 * Verify if the server acces is allowed according to the license specification.
	 * @param currentMacAddress the server MAC address.
	 * @param currentDate the server date.
	 * @param nbUsers the current number of user.
	 * @return true if the access is allowed, false if not.
	 */
	public boolean allowServerAccess(String currentMacAddress, Date currentDate, int nbUsers) {
		return checkMacAddress(currentMacAddress)
				&& checkExpiringDate(currentDate)
				&& checkNbUser(nbUsers);
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Trial: ").append(isTrialLicense()).append('\n');
		result.append("Expiring Date: ").append(getExpiringDate()).append('\n');
		result.append("Maximum concurrent users: ").append(getMaxConcurrentUsers()).append('\n');
		result.append("MAC address: ").append(getMacAddress());
		return result.toString();
	}
}
