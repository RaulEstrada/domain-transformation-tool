package tooleclipseplugin.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import output.Writer;
import output.WriterFactoryImpl;
import tooleclipseplugin.Core;
import tooleclipseplugin.popup.actions.utils.Utils;

public class GenerateORMAction implements IObjectActionDelegate {
	private Shell shell;
	private Core core = new Core();
	private Utils utils = new Utils();

	@Override
	public void run(IAction arg0) {
		Writer writer = WriterFactoryImpl.getInstance().getORMWriter();
		utils.writeOutputFromModel(writer, shell, core);
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		if (core.getIntermediateModelLoaded()) {
			arg0.setEnabled(true);
		}
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		shell = arg1.getSite().getShell();
	}

}
