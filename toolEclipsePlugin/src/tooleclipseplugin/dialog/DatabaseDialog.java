package tooleclipseplugin.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DatabaseDialog extends TitleAreaDialog {
	private Text host;
	private Text databaseName;
	private Text port;
	private Text username;
	private Text password;
	private Combo databaseTypeCombo;
	private Button ok;
	
	private String hostInput;
	private String databaseNameInput;
	private String databaseType;
	private String portInput;
	private String usernameInput;
	private String passwordInput;

	public DatabaseDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(composite, SWT.NONE);
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 
				15, 15));
		container.setLayout(gridLayout);
		
		host = createInput(container, "Host", false);
		port = createInput(container, "Port", false);
		databaseName = createInput(container, "Database name", false);
		createDatabaseTypeCombo(container);
		username = createInput(container, "Username", false);
		password = createInput(container, "Password", true);
		
		return composite;
	}
	
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		ok = createButton(parent, IDialogConstants.OK_ID,
	            IDialogConstants.OK_LABEL, true);
		ok.setEnabled(false);
	    createButton(parent, IDialogConstants.CANCEL_ID,
	            IDialogConstants.CANCEL_LABEL, false);
	}

	private Text createInput(Composite container, String text, boolean mask) {
		final Label label = new Label(container, SWT.NONE);
		label.setText(text);
		Text textElement = new Text(container, SWT.BORDER);
		if (mask) {
			textElement.setEchoChar('*');
		}
		textElement.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		textElement.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if (host.getText() != null && !host.getText().isEmpty() && 
						port.getText() != null && !port.getText().isEmpty() && 
						databaseName.getText() != null &&
						!databaseName.getText().isEmpty()
						&& username.getText() != null && 
						!username.getText().isEmpty() && password.getText() != null &&
						!password.getText().isEmpty() && 
						databaseTypeCombo.getText() != null && 
						!databaseTypeCombo.getText().isEmpty()) {
					ok.setEnabled(true);
				} else {
					ok.setEnabled(false);
				}
			}
		});
		return textElement;
	}
	
	private void createDatabaseTypeCombo(Composite container) {
		final Label databaseTypeLabel = new Label(container, SWT.NONE);
		databaseTypeLabel.setText("Database type");
		databaseTypeCombo = new Combo(container, SWT.DROP_DOWN);
		databaseTypeCombo.add("MYSQL");
		databaseTypeCombo.add("ORACLE");
		databaseTypeCombo.add("POSTGRESQL");
		databaseTypeCombo.add("HSQLDB");
		databaseTypeCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if (host.getText() != null && !host.getText().isEmpty() &&
						port.getText() != null && !port.getText().isEmpty() && 
						databaseName.getText() != null && 
						!databaseName.getText().isEmpty() && 
						username.getText() != null && 
						!username.getText().isEmpty() && 
						password.getText() != null && 
						!password.getText().isEmpty() &&
						databaseTypeCombo.getText() != null && 
						!databaseTypeCombo.getText().isEmpty()) {
					ok.setEnabled(true);
				} else {
					ok.setEnabled(false);
				}
			}
		});
	}

	@Override
	public void create() {
		super.create();
		setTitle("TFG: Read from database");
		setMessage("Introduce configuration parameters to connect and read from"
				+ " the database", IMessageProvider.INFORMATION);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		hostInput = host.getText();
		databaseNameInput = databaseName.getText();
		portInput = port.getText();
		usernameInput = username.getText();
		passwordInput = password.getText();
		databaseType = databaseTypeCombo.getText();
		super.okPressed();
	}

	public String getHostInput() {
		return hostInput;
	}

	public String getDatabaseNameInput() {
		return databaseNameInput;
	}

	public String getPortInput() {
		return portInput;
	}

	public String getUsernameInput() {
		return usernameInput;
	}

	public String getPasswordInput() {
		return passwordInput;
	}

	public String getDatabaseType() {
		return databaseType;
	}

}
