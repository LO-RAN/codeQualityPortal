package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Tools {
	static public ImageIcon createAppletImageIcon(String path, String description) {
		int MAX_IMAGE_SIZE = 75000; // Change this to the size of
		// your biggest image, in bytes.
		int count = 0;
		BufferedInputStream imgStream = new BufferedInputStream(Tools.class
				.getResourceAsStream(path));
		if (imgStream != null) {
			byte buf[] = new byte[MAX_IMAGE_SIZE];
			try {
				count = imgStream.read(buf);
			} catch (IOException ieo) {
				System.err.println("Couldn't read stream from file: " + path);
			}

			try {
				imgStream.close();
			} catch (IOException ieo) {
				System.err.println("Can't close file " + path);
			}

			if (count <= 0) {
				System.err.println("Empty file: " + path);
				return null;
			}
			return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf),
					description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
