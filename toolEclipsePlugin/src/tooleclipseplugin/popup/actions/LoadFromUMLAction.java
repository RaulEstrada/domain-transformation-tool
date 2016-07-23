package tooleclipseplugin.popup.actions;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import input.Reader;
import input.ReaderFactoryImpl;
import tooleclipseplugin.popup.actions.utils.Utils;

public class LoadFromUMLAction implements IObjectActionDelegate {

	private Shell shell;
	private String directory;
	private Utils utils = new Utils();
	
	/**
	 * Constructor for Action1.
	 */
	public LoadFromUMLAction() {
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
		Reader reader = ReaderFactoryImpl.getInstance().getUMLReader();
		utils.loadFromFile(reader, directory, shell);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.directory = utils.getSelectionPath(selection, false, false);
	}

}
