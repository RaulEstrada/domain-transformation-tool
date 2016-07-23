package tooleclipseplugin.popup.actions.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import input.Reader;
import model.structure.Package;
import output.Writer;
import tooleclipseplugin.Core;

public class Utils {
	public void loadFromFile(Reader reader, String directory, Shell shell) {
		try {
			Package model = reader.loadModel(directory);
			if (!model.isEmpty()) {
				new Core().setIntermediateModel(model);
				MessageDialog.openInformation(shell, "Model loaded",
				"The model has been loaded with " + 
				model.getAllEntitiesInPackageTree().size() + " entities, " +
				model.getAllValueObjectsInPackageTree().size() + " value objects and " + 
				model.getAllEnumsInPackageTree().size() + " custom types");
			} else {
				MessageDialog.openInformation(
						shell,
						"Model loaded",
						"No model was loaded");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(shell, "Error",
				"An error was raised while loading the model: " + e.getMessage());
		}
	}
	
	public void writeOutputFromModel(Writer writer, Shell shell, Core core) {
		DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.OPEN);
		directoryDialog.setText("Select output directory");
		String outputDirectory = directoryDialog.open();
		try {
			writer.write(core.getIntermediateModel(), outputDirectory);
			MessageDialog.openInformation(
					shell,
					"Model generated",
					"Generation process completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(shell, "Error",
				"An error was raised while generating the output: " + e.getMessage());
		}
	}
	
	public String getSelectionPath(ISelection arg1, boolean folder, boolean packg) {
		if (arg1 instanceof TreeSelection) {
			TreeSelection tSelection = (TreeSelection)arg1;
			TreePath treePath = tSelection.getPaths()[0];
			treePath.getLastSegment();
			if (folder && !packg) {
				IFolder iFolder = (IFolder)((IAdaptable)treePath.getLastSegment())
						.getAdapter(IFolder.class);
				return iFolder.getRawLocation().toOSString();
			} else if(packg){
				IPackageFragment iPckg = (IPackageFragment)((IAdaptable)treePath
						.getLastSegment()).getAdapter(IPackageFragment.class);
				return iPckg.getResource().getRawLocation().toOSString();
			} else {
				IFile iFile = (IFile)((IAdaptable)treePath.getLastSegment())
						.getAdapter(IFile.class);
				return iFile.getRawLocation().toOSString();
			}
		}
		return null;
	}
}
