/*******************************************************************************
 * Copyright (c) 2003, 2006 Subclipse project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Subclipse project committers - initial API and implementation
 ******************************************************************************/
package com.compuware.caqs.eclipseplugin.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;

/**
 * A class for comparing ISVNRemoteResource objects
 * 
 * <p>
 * 
 * <pre>
 * ResourceEditionNode left = new ResourceEditionNode(editions[0]);
 * ResourceEditionNode right = new ResourceEditionNode(editions[1]);
 * CompareUI.openCompareEditorOnPage(new SVNCompareEditorInput(left, right),
 * 		getTargetPage());
 * </pre>
 * 
 * </p>
 * 
 */
public class CaqsResourceNode implements IStructureComparator,
		ITypedElement, IStreamContentAccessor, IEncodedStreamContentAccessor {
	private String charset = null;
	private String name = null;
	private InputStream contents = null;

	/**
	 * Creates a new ResourceEditionNode on the given resource edition.
	 */
	public CaqsResourceNode(String n, byte[] contentsByteArray) {
		this.name = n;
		this.contents = new ByteArrayInputStream(contentsByteArray);
	}

	/**
	 * Returns true if both resources names are identical. The content is not
	 * considered.
	 * 
	 * @see IComparator#equals
	 */
	public boolean equals(Object other) {
		if (other instanceof ITypedElement) {
			String otherName = ((ITypedElement) other).getName();
			return getName().equals(otherName);
		}
		return super.equals(other);
	}

	/**
	 * @see IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		return this.contents;
	}

	/*
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/*
	 * Returns the name of this node.
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the comparison type for this node.
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return TEXT_TYPE;
	}

	/**
	 * @see IComparator#equals
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	public String getCharset() throws CoreException {
		return charset;
	}

	public void setCharset(String charset) throws CoreException {
		this.charset = charset;
	}

	public Object[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
}