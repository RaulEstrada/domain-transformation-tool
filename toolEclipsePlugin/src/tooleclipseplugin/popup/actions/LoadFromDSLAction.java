package tooleclipseplugin.popup.actions;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import input.Reader;
import input.ReaderFactoryImpl;
import tooleclipseplugin.popup.actions.utils.Utils;

public class LoadFromDSLAction implements IObjectActionDelegate {
	
	private Shell shell;
	private String directory;
	private Utils utils = new Utils();

	@Override
	public void run(IAction arg0) {
		Reader reader = ReaderFactoryImpl.getInstance().getDSLReader();
		utils.loadFromFile(reader, directory, shell);
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		this.directory = utils.getSelectionPath(arg1, false, false);
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		shell = arg1.getSite().getShell();
	}

}
