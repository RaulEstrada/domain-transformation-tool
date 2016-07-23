package tooleclipseplugin.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import input.Reader;
import input.ReaderFactoryImpl;
import input.bbdd.DatabaseType;
import model.structure.Package;
import tooleclipseplugin.Core;
import tooleclipseplugin.dialog.DatabaseDialog;

public class LoadFromDatabaseAction implements IObjectActionDelegate {
	public static boolean ACTIVE = false;

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public LoadFromDatabaseAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		DatabaseDialog dbDialog = new DatabaseDialog(shell);
		dbDialog.create();
		if (dbDialog.open() == Window.OK) {
			Reader reader = getReader(dbDialog);
			String host = dbDialog.getHostInput();
			String port = dbDialog.getPortInput();
			String dbName = dbDialog.getDatabaseNameInput();
			String username = dbDialog.getUsernameInput();
			String password = dbDialog.getPasswordInput();
			try {
				Package model = reader.loadModel(host, port, dbName, username, password);
				if (!model.isEmpty()) {
					new Core().setIntermediateModel(model);
					MessageDialog.openInformation(shell, "Model loaded",
					"The model has been loaded with " + 
					model.getAllEntitiesInPackageTree().size() + " entities, " +
					model.getAllValueObjectsInPackageTree().size() + " value objects and " + 
					model.getAllEnumsInPackageTree().size() + " custom types");
				} else {
					MessageDialog.openInformation(shell, "Model loaded",
							"No model was loaded");
				}
			} catch (Exception e) {
				MessageDialog.openInformation(shell, "Error",
						"An error was raised while loading the model: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private Reader getReader(DatabaseDialog dbDialog) {
		Reader reader = null;
		DatabaseType dbType = DatabaseType.valueOf(dbDialog.getDatabaseType());
		switch(dbType) {
		case HSQLDB: reader = 
				ReaderFactoryImpl.getInstance().getHSQLDatabaseReader(); break;
		case MYSQL: reader = 
				ReaderFactoryImpl.getInstance().getMySQLDatabaseReader(); break;
		case ORACLE: reader = 
				ReaderFactoryImpl.getInstance().getOracleDatabaseReader(); break;
		case POSTGRESQL: reader = 
				ReaderFactoryImpl.getInstance().getPostGreSQLDatabaseReader(); break;
		default: 
			throw new RuntimeException("Database type not recognized: " + dbType);
		}
		return reader;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
