package com.compuware.caqs.eclipseplugin.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.compuware.caqs.plugin.Activator;
import com.compuware.caqs.webservices.login.LoginWS;
import com.compuware.caqs.webservices.login.LoginWSImpl;
import com.compuware.caqs.webservices.util.WebServicesUtils;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class CaqsPreferencesPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private StringFieldEditor loginEditor = null;
	private StringFieldEditor passwordEditor = null;
	private StringFieldEditor urlEditor = null;

	public CaqsPreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Caqs connections settings");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		this.loginEditor = new StringFieldEditor(
				PreferenceConstants.P_CAQS_LOGIN, "Login:",
				getFieldEditorParent());
		addField(this.loginEditor);
		this.passwordEditor = new StringFieldEditor(
				PreferenceConstants.P_CAQS_PASSWORD, "Password:",
				getFieldEditorParent());
		this.passwordEditor.getTextControl(getFieldEditorParent()).setEchoChar(
				'*');
		addField(this.passwordEditor);
		this.urlEditor = new StringFieldEditor(
				PreferenceConstants.P_CAQS_URL, "URL:",
				getFieldEditorParent());
		addField(this.urlEditor);
	}

	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		Button button = new Button((Composite) control, SWT.PUSH);
		button.setText("Test connection");
		button.addSelectionListener(new TestConnectionButtonSelectionListener());
		return control;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	private class TestConnectionButtonSelectionListener implements
			SelectionListener {

		private TestConnectionButtonSelectionListener() {
		}

		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent evt) {
			String msg = "";
			String login = loginEditor.getStringValue();
			if (login == null || "".equals(login)) {
				msg += "Veuillez indiquer un identifiant.";
			}
			String password = passwordEditor.getStringValue();
			if (password == null || "".equals(password)) {
				if (!"".equals(msg)) {
					msg += "\n";
				}
				msg += "Veuillez indiquer un mot de passe.";
			}
			String url = urlEditor.getStringValue();
			if (url == null || "".equals(url)) {
				if (!"".equals(msg)) {
					msg += "\n";
				}
				msg += "Veuillez indiquer une url.";
			}
			
			if (!"".equals(msg)) {
				MessageBox mb = new MessageBox(Display.getCurrent()
						.getActiveShell(), SWT.ICON_ERROR);
				mb.setMessage(msg);
				mb.open();
			} else {
				try {
					IPreferenceStore store = Activator.getDefault()
							.getPreferenceStore();
					store.setValue(PreferenceConstants.P_CAQS_TMP_PASSWORD,
							password);
					LoginWS svc = new LoginWS();
					LoginWSImpl ws = svc.getLoginWSImplPort();
					org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy
							.getClient(ws);
					WebServicesUtils.addAuthenticationDetails(login, password, client);
					if (ws.validateAuthentication(login, password)) {
						setMessage("Authentication succeeded", INFORMATION);
					} else {
						setMessage("Authentication failed.", ERROR);
					}
					store.setValue(PreferenceConstants.P_CAQS_TMP_PASSWORD,
							"");

				} catch (org.apache.cxf.binding.soap.SoapFault t) {
					setErrorMessage("Une erreur est survenue : "
							+ t.getMessage());
				} catch(Throwable t) {
					setErrorMessage("Une erreur est survenue : "
							+ t.getMessage());
				}
			}
		}

	}
}