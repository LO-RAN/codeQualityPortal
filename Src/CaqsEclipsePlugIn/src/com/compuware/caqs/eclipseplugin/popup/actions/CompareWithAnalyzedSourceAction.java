package com.compuware.caqs.eclipseplugin.popup.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.compuware.caqs.eclipseplugin.actions.CaqsActions;
import com.compuware.caqs.eclipseplugin.preferences.PreferenceConstants;
import com.compuware.caqs.eclipseplugin.utils.CaqsCompwareInput;
import com.compuware.caqs.webservices.projectinfos.ProjectInfosWS;
import com.compuware.caqs.webservices.projectinfos.ProjectInfosWSImpl;
import com.compuware.caqs.webservices.util.WebServicesUtils;

public class CompareWithAnalyzedSourceAction extends CaqsActions {
	private boolean refresh;

	/**
	 * Constructor for Action1.
	 */
	public CompareWithAnalyzedSourceAction() {
		super();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void execute(IAction action) throws InvocationTargetException,
			InterruptedException {
		refresh = false;
		/*
		 * // Setup holders final ISVNRemoteFile[] file = new ISVNRemoteFile[] {
		 * null }; final ILogEntry[][] entries = new ILogEntry[][] { null };
		 */
		// Get the selected file
		run(new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				// file[0] = getSelectedRemoteFile();
			}
		}, false /* cancelable */, PROGRESS_BUSYCURSOR);
		/*
		 * if (file[0] == null) { // No revisions for selected file
		 * MessageDialog.openWarning(this.shell, "pas d'equivalent caqs",
		 * "pas d'equivalent caqs"); return; }
		 * 
		 * if (!file[0].getResource().isSynchronized(Depth.immediates)) {
		 * refresh = MessageDialog.openQuestion(this.shell, "question title",
		 * "file changed"); }
		 */

		// Fetch the log entries
		run(new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					monitor.beginTask("recuperation ?", 100); //$NON-NLS-1$
					IResource[] resources = getSelectedResources();
					/*
					 * GetLogsCommand logCmd = new GetLogsCommand(file[0],
					 * SVNRevision.HEAD, SVNRevision.HEAD, new
					 * SVNRevision.Number(0), false, 0, tagManager, false);
					 * logCmd.run(Policy.subMonitorFor(monitor, 100));
					 * entries[0] = logCmd.getLogEntries();
					 */
					monitor.done();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				}
			}
		}, true /* cancelable */, PROGRESS_DIALOG);

		/*
		 * if (entries[0] == null) return;
		 */
		// Show the compare viewer

		IFile selectedFile = (IFile) this.getSelectedResources()[0];
		ICompilationUnit compilationUnit = (ICompilationUnit)JavaCore.create(selectedFile);

		/*
		 * run(new IRunnableWithProgress() { public void run(IProgressMonitor
		 * monitor) throws InvocationTargetException, InterruptedException {
		 * CompareEditorInput compi = new CaqsCompwareInput( (IFile)
		 * getSelectedResources()[0], (IFile) getSelectedResources()[0]);
		 * CompareUI.openCompareEditorOnPage(compi, getTargetPage()); } }, false
		 * // , PROGRESS_BUSYCURSOR);
		 */
		ProjectInfosWS svc = new ProjectInfosWS();
		ProjectInfosWSImpl ws = svc.getProjectInfosWSImplPort();
		org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy
				.getClient(ws);
		WebServicesUtils.addAuthenticationDetails(client);
		IProject containerProject = selectedFile.getProject();
		try {
			String eaId = containerProject
					.getPersistentProperty(new QualifiedName("",
							PreferenceConstants.CAQS_PROJECT_PROPERTY));
			byte[] analyzedSourceArray = ws.retrieveAnalyzedSource(eaId,
					compilationUnit.getTypes()[0].getFullyQualifiedName());
			CompareEditorInput compi = new CaqsCompwareInput(
					(IFile) getSelectedResources()[0],
					analyzedSourceArray);
			CompareUI.openCompareEditorOnPage(compi, getTargetPage());
		} catch (CoreException exc) {
			// TODO log
		}
	}

	public void init(IViewPart arg0) {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}
}
