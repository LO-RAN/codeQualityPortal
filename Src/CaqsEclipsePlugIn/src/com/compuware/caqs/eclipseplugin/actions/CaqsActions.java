package com.compuware.caqs.eclipseplugin.actions;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

public abstract class CaqsActions extends ActionDelegate implements IObjectActionDelegate, IViewActionDelegate, IWorkbenchWindowActionDelegate {
	// The current selection
	protected IStructuredSelection selection;
	
	// Constants for determining the type of progress. Subclasses may
	// pass one of these values to the run method.
	public final static int PROGRESS_DIALOG = 1;
	public final static int PROGRESS_BUSYCURSOR = 2;

	// The shell, required for the progress dialog
	private Shell shell;

	private List accumulatedStatus = new ArrayList();


	private IWorkbenchPart targetPart;


	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		if(targetPart != null) {
			this.shell = targetPart.getSite().getShell();
			this.targetPart = targetPart;
		}
	}
	
	protected Shell getShell() {
		return this.shell;
	}
	
	/**
	 * Common run method for all SVN actions.
	 */
	final public void run(IAction action) {
		try {
			if (!beginExecution(action)) return;
			execute(action);
			endExecution();
		} catch (InvocationTargetException e) {
			// Handle the exception and any accumulated errors
			//handle(e);
		} catch (InterruptedException e) {
			// Show any problems that have occurred so far
			//handle(null);
		}
	}
	
	/**
	 * This method gets invoked before the <code>SVNAction#execute(IAction)</code>
	 * method. It can preform any pre-checking and initialization required before 
	 * the action is executed. Subclasses may override but must invoke this
	 * inherited method to ensure proper initialization of this superclass is performed.
	 * These included preparation to accumulate IStatus and checking for dirty editors.
	 */
	protected boolean beginExecution(IAction action) {
		accumulatedStatus.clear();
		if(needsToSaveDirtyEditors()) {
			/*if(!saveAllEditors()) {
				return false;
			}*/
		}
		return true;
	}

	/**
	 * Actions must override to do their work.
	 */
	abstract protected void execute(IAction action) throws InvocationTargetException, InterruptedException;

	/**
	 * This method gets invoked after <code>SVNAction#execute(IAction)</code>
	 * if no exception occurred. Subclasses may override but should invoke this
	 * inherited method to ensure proper handling oy any accumulated IStatus.
	 */
	protected void endExecution() {
		if ( ! accumulatedStatus.isEmpty()) {
			//handle(null);
		}
	}
	
	/**
	 * Add a status to the list of accumulated status. 
	 * These will be provided to method handle(Exception, IStatus[])
	 * when the action completes.
	 */
	protected void addStatus(IStatus status) {
		accumulatedStatus.add(status);
	}
	
	/**
	 * Return the list of status accumulated so far by the action. This
	 * will include any OK status that were added using addStatus(IStatus)
	 */
	protected IStatus[] getAccumulatedStatus() {
		return (IStatus[]) accumulatedStatus.toArray(new IStatus[accumulatedStatus.size()]);
	}
	
	/**
	 * Convenience method for running an operation with the appropriate progress.
	 * Any exceptions are propogated so they can be handled by the
	 * <code>SVNAction#run(IAction)</code> error handling code.
	 * 
	 * @param runnable  the runnable which executes the operation
	 * @param cancelable  indicate if a progress monitor should be cancelable
	 * @param progressKind  one of PROGRESS_BUSYCURSOR or PROGRESS_DIALOG
	 */
	final protected void run(final IRunnableWithProgress runnable, boolean cancelable, int progressKind) throws InvocationTargetException, InterruptedException {
		final Exception[] exceptions = new Exception[] {null};
		
		// Ensure that no repository view refresh happens until after the action
		final IRunnableWithProgress innerRunnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                //SVNUIPlugin.getPlugin().getRepositoryManager().run(runnable, monitor);
			}
		};
		
		switch (progressKind) {
			case PROGRESS_BUSYCURSOR :
				BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
					public void run() {
						try {
							innerRunnable.run(new NullProgressMonitor());
						} catch (InvocationTargetException e) {
							exceptions[0] = e;
						} catch (InterruptedException e) {
							exceptions[0] = e;
						}
					}
				});
				break;
			case PROGRESS_DIALOG :
			default :
				new ProgressMonitorDialog(getShell()).run(true, cancelable,/*cancelable, true, */innerRunnable);	
				break;
		}
		if (exceptions[0] != null) {
			if (exceptions[0] instanceof InvocationTargetException)
				throw (InvocationTargetException)exceptions[0];
			else
				throw (InterruptedException)exceptions[0];
		}
	}
	
	/**
	 * Answers if the action would like dirty editors to saved
	 * based on the SVN preference before running the action. By
	 * default, SVNActions do not save dirty editors.
	 */
	protected boolean needsToSaveDirtyEditors() {
		return false;
	}

	/**
	 * Find the object associated with the selected object that is adapted to
	 * the provided class.
	 * 
	 * @param selection
	 * @param c
	 * @return Object
	 */
	public static Object getAdapter(Object selection, Class c) {
		if (c.isInstance(selection)) {
			return selection;
		}
		if (selection instanceof IAdaptable) {
			IAdaptable a = (IAdaptable) selection;
			Object adapter = a.getAdapter(c);
			if (c.isInstance(adapter)) {
				return adapter;
			}
		}
		return null;
	}
	

	/**
	 * @return IWorkbenchPart
	 */
	protected IWorkbenchPart getTargetPart() {
		return targetPart;
	}

	/**
	 * Return the path that was active when the menu item was selected.
	 * @return IWorkbenchPage
	 */
	protected IWorkbenchPage getTargetPage() {
		//if (getTargetPart() == null) return SVNUIPlugin.getActivePage();
		return getTargetPart().getSite().getPage();
	}
	
	/**
	 * Returns an array of the given class type c that contains all
	 * instances of c that are either contained in the selection or
	 * are adapted from objects contained in the selection.
	 * 
	 * @param c
	 * @return
	 */
	protected Object[] getSelectedResources(Class c) {
		return getSelectedAdaptables(selection, c);
	}
	
	/**
	 * Returns the selected resources.
	 * 
	 * @return the selected resources
	 */
	protected IResource[] getSelectedResources() {
		ArrayList resourceArray = new ArrayList();
		IResource[] resources = (IResource[])getSelectedResources(IResource.class);
		for (int i = 0; i < resources.length; i++) resourceArray.add(resources[i]);
		ResourceMapping[] resourceMappings = (ResourceMapping[])getSelectedAdaptables(selection, ResourceMapping.class);
		for (int i = 0; i < resourceMappings.length; i++) {
			ResourceMapping resourceMapping = (ResourceMapping)resourceMappings[i];
			try {
				ResourceTraversal[] traversals = resourceMapping.getTraversals(null, null);
				for (int j = 0; j < traversals.length; j++) {
					IResource[] traversalResources = traversals[j].getResources();
					for (int k = 0; k < traversalResources.length; k++) {
						if (!resourceArray.contains(traversalResources[k]))
							resourceArray.add(traversalResources[k]);
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		IResource[] selectedResources = new IResource[resourceArray.size()];
		resourceArray.toArray(selectedResources);
		return selectedResources;
	}
	
	/**
	 * Creates an array of the given class type containing all the
	 * objects in the selection that adapt to the given class.
	 * 
	 * @param selection
	 * @param c
	 * @return
	 */
	public static Object[] getSelectedAdaptables(ISelection selection, Class c) {
		ArrayList result = null;
		if (selection != null && !selection.isEmpty()) {
			result = new ArrayList();
			Iterator elements = ((IStructuredSelection) selection).iterator();
			while (elements.hasNext()) {
				Object adapter = getAdapter(elements.next(), c);
				if (c.isInstance(adapter)) {
					result.add(adapter);
				}
			}
		}
		if (result != null && !result.isEmpty()) {
			return result.toArray((Object[])Array.newInstance(c, result.size()));
		}
		return (Object[])Array.newInstance(c, 0);
	}
	
	/*
	 * Method declared on IActionDelegate.
	 */
	public void selectionChanged(IAction action, ISelection sel) {
		if (sel instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) sel;
		}
	}
	
}
