package com.compuware.caqs.eclipseplugin.properties;

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.compuware.caqs.eclipseplugin.preferences.PreferenceConstants;
import com.compuware.caqs.webservices.projectslist.ProjectWS;
import com.compuware.caqs.webservices.projectslist.ProjectsListForUserWS;
import com.compuware.caqs.webservices.projectslist.ProjectsListForUserWSImpl;
import com.compuware.caqs.webservices.util.WebServicesUtils;

public class CaqsProjectPropertiesPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private ComboViewer projectComboViewer;
	private Object temporarySelectedProject = null;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public CaqsProjectPropertiesPage() {
		super();
	}

	public List<ProjectWS> retrieveProjectsList() throws SOAPFaultException {
		ProjectsListForUserWS svc = new ProjectsListForUserWS();
		ProjectsListForUserWSImpl ws = svc.getProjectsListForUserWSImplPort();
		org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy
				.getClient(ws);
		WebServicesUtils.addAuthenticationDetails(client);
		return ws.getProjectsListForUser();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for path field
		Label pathLabel = new Label(composite, SWT.READ_ONLY);
		pathLabel.setText("Caqs project :");
		Combo projectCombo = new Combo(composite, SWT.READ_ONLY);
		this.projectComboViewer = new ComboViewer(projectCombo);
		this.projectComboViewer
				.setContentProvider(new org.eclipse.jface.viewers.ArrayContentProvider());
		this.projectComboViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						temporarySelectedProject = selection.getFirstElement();
					}
				});
		this.projectComboViewer
				.setLabelProvider(new org.eclipse.jface.viewers.LabelProvider());
		try {
			List<ProjectWS> projects = this.retrieveProjectsList();
			this.projectComboViewer.setInput(projects);
			try {
				IProject prj = this.getProject();
				if (prj != null) {
					String selectedProjectId = prj
							.getPersistentProperty(new QualifiedName("",
									PreferenceConstants.CAQS_PROJECT_PROPERTY));
					if (selectedProjectId != null
							&& !"".equals(selectedProjectId)) {
						for (ProjectWS p : projects) {
							if (selectedProjectId.equals(p.getId())) {
								this.temporarySelectedProject = p;
								break;
							}
						}
						if (this.temporarySelectedProject != null) {
							StructuredSelection selection = new StructuredSelection(
									new Object[] { this.temporarySelectedProject });
							this.projectComboViewer.setSelection(selection,
									true);
						}
					}
				}
			} catch (CoreException exc) {
				this.setErrorMessage("An error occured : " + exc.getMessage());
			}
		} catch (SOAPFaultException exc) {
			this.setErrorMessage("An error occured : " + exc.getMessage());
		}
		// this.projectCombo.setText("DAVID");//((IProject)
		// getElement()).getFullPath().toString());
	}

	/*
	 * private void addSeparator(Composite parent) { Label separator = new
	 * Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL); GridData gridData = new
	 * GridData(); gridData.horizontalAlignment = GridData.FILL;
	 * gridData.grabExcessHorizontalSpace = true;
	 * separator.setLayoutData(gridData); }
	 * 
	 * private void addSecondSection(Composite parent) { Composite composite =
	 * createDefaultComposite(parent);
	 * 
	 * // Label for owner field Label ownerLabel = new Label(composite,
	 * SWT.NONE); ownerLabel.setText(OWNER_TITLE);
	 * 
	 * // Owner text field ownerText = new Text(composite, SWT.SINGLE |
	 * SWT.BORDER); GridData gd = new GridData(); gd.widthHint =
	 * convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
	 * ownerText.setLayoutData(gd);
	 * 
	 * // Populate owner text field try { String owner = ((IResource)
	 * getElement()).getPersistentProperty( new QualifiedName("",
	 * OWNER_PROPERTY)); ownerText.setText((owner != null) ? owner :
	 * DEFAULT_OWNER); } catch (CoreException e) {
	 * ownerText.setText(DEFAULT_OWNER); } }
	 */

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		this.addFirstSection(composite);
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		// Populate the owner text field with the default value
		// ownerText.setText(DEFAULT_OWNER);
		this.projectComboViewer.setSelection(new StructuredSelection(
				new Object[] {}), true);
		this.temporarySelectedProject = null;
	}

	public boolean performOk() {
		boolean retour = false;
		if (this.temporarySelectedProject == null) {
			MessageBox mb = new MessageBox(Display.getCurrent()
					.getActiveShell(), SWT.ICON_ERROR);
			mb.setMessage("Veuillez sélectionner un projet.");
			mb.open();
		} else {
			if (super.performOk()) {
				try {
					IProject prj = this.getProject();
					if (prj != null) {
						if (this.temporarySelectedProject != null) {
							ProjectWS projectWS = (ProjectWS) this.temporarySelectedProject;
							prj.setPersistentProperty(new QualifiedName("",
									PreferenceConstants.CAQS_PROJECT_PROPERTY),
									projectWS.getId());
							retour = true;
						}
					}
				} catch (CoreException e) {
					retour = false;
				}
			}
		}
		return retour;
	}

	private IProject getProject() {
		IProject retour = null;
		if (getElement() != null) {
			if (getElement() instanceof IProject) {
				retour = (IProject) getElement();
			} else {
				Object adapter = getElement().getAdapter(
						org.eclipse.core.resources.IProject.class);
				if (adapter instanceof IProject) {
					retour = (IProject) adapter;
				} else {
					adapter = getElement().getAdapter(
							org.eclipse.core.resources.IResource.class);
					if (adapter instanceof IProject) {
						retour = (IProject) adapter;
					}
				}
			}
		}
		return retour;
	}

}