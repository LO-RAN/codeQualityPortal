package com.compuware.caqs.security.license;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.compuware.caqs.security.license.exception.InvalidMacAddressException;
import com.compuware.caqs.security.license.exception.LicenseExpiredException;
import com.compuware.caqs.security.net.AddressMacInfo;

public class LicenseManager {
	
    private static org.apache.log4j.Logger mLog = Logger.getLogger("Security");

    private static final String KEY_FILE_PATH = "/public.der";
    private static final String CONFIG_FILE_PATH = "/license.properties";
    private static final String LICENSE_FILE_PATH_KEY = "license.file.path";
    
    private static LicenseManager singleton = new LicenseManager();
    
    private License currentLicense = null;
    
    public static LicenseManager getInstance() {
    	return LicenseManager.singleton;
    }
    
    private LicenseManager() {
    	currentLicense = decrypt();
    }
    
    public License getLicense() {
    	return this.currentLicense;
    }
    
    public void checkLicense() throws InvalidMacAddressException, LicenseExpiredException {
    	if (currentLicense != null) {
    		if (!currentLicense.isTrialLicense()) {
	        	String macAddress = AddressMacInfo.getMacAddress();
	    		if (!currentLicense.checkMacAddress(macAddress)) {
	    			throw new InvalidMacAddressException();
	    		}
    		}
    		if (!currentLicense.checkExpiringDate(new Date())) {
    			throw new LicenseExpiredException();
    		}
    	}
    }
    
    private License decrypt() throws SecurityException {
		
		String license = null;
		String licensePath = getLicensePath();
		if (licensePath != null) {
			try {
				mLog.info("Read license file");
	
				byte[] encodedKey = this.getEncodedKey();
				X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		
				KeyFactory kf = KeyFactory.getInstance("RSA");
				PublicKey pk = kf.generatePublic(publicKeySpec);
		
				Cipher rsa = Cipher.getInstance("RSA");
		
				rsa.init(Cipher.DECRYPT_MODE, pk);
				InputStream is = new CipherInputStream(new FileInputStream(licensePath), rsa);
		
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				license = in.readLine();
				in.close();
				is.close();
			}
			catch (FileNotFoundException e) {
				throw new SecurityException("License file not found", e);
			}
			catch (IOException e) {
				throw new SecurityException("Error reading license file", e);
			}
			catch (NoSuchAlgorithmException e) {
				throw new SecurityException("Error in license file encoding algorithm", e);
			}
			catch (InvalidKeySpecException e) {
				throw new SecurityException("Error in public key", e);
			} catch (NoSuchPaddingException e) {
				throw new SecurityException("Error in public key", e);
			} catch (InvalidKeyException e) {
				throw new SecurityException("Invalid public key", e);
			}
		}
		else {
			throw new SecurityException("License file not found");
		}
		License result = createLicense(license);
		mLog.info(result);
		return result;
	}
    
    private String getLicensePath() {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream(CONFIG_FILE_PATH));
		}
		catch (IOException e) {
			mLog.error("Error reading license configuration file", e);
		}
		return prop.getProperty(LICENSE_FILE_PATH_KEY);
    }
	
    private byte[] getEncodedKey() throws IOException {
		InputStream keyFileIn = this.getClass().getResourceAsStream(KEY_FILE_PATH);
		byte[] result = new byte[2048];
		keyFileIn.read(result);
    	return result;
    }
    
	private Date getDate(String dateStr) {
		Date result = null;
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		try {
			result = df.parse(dateStr);
		}
		catch (ParseException e) {
			throw new SecurityException("Error reading license file", e);
		}
		return result;
	}
	
	private int getInt(String intValue) {
		return Integer.parseInt(intValue);
	}
	
	private License createLicense(String licenseContent) {
		License result = new License();
		if (licenseContent != null) {
			Pattern p = Pattern.compile("([0-9]{8})\\|([0-9]{6})\\|([0-9A-Z-]{17})\\|([0-9A-Z]+)");
			Matcher m = p.matcher(licenseContent);
			if (m.find() && m.groupCount() == 4) {
				result.setExpiringDate(getDate(m.group(1)));
				result.setMaxConcurrentUsers(getInt(m.group(2)));
				result.setMacAddress(m.group(3));
				result.setTrialLicense(m.group(4));
			}
			else {
				throw new SecurityException("License file content does not match");
			}
		}
		return result;
	}
	
    public static void main(String[] args) {
        LicenseManager kg = LicenseManager.getInstance();
        License lic = kg.getLicense();
        System.out.println(lic);
      }

}
