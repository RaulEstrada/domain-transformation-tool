package tooleclipseplugin.popup.actions;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import input.Reader;
import input.ReaderFactoryImpl;
import tooleclipseplugin.popup.actions.utils.Utils;

public class LoadFromJavaAction implements IObjectActionDelegate {
	private Shell shell;
	private String directory;
	private Utils utils = new Utils();

	@Override
	public void run(IAction arg0) {
		String classpath = System.getProperty("java.class.path");
		File hibernateFile = null;
		File dir = null;
		File n = null;
		try {
			try {
			URL hibernateURL = new URL("platform:/plugin/core/resources/hibernate-jpa-2.1-api-1.0.0.Final.jar");
			dir = new File("tmp/");
			hibernateFile = new File(dir, "hibernate-jpa-2.1-api-1.0.0.Final");
			FileUtils.copyInputStreamToFile(
					hibernateURL.openConnection().getInputStream(), 
					hibernateFile);
			n = new File(dir, "hibernate-jpa.jar");
			FileUtils.copyFile(hibernateFile, n);
			classpath += System.getProperty("path.separator") + n.getAbsolutePath();
			System.setProperty("java.class.path", classpath);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reader reader = ReaderFactoryImpl.getInstance().getJavaReader();
		File inputDirectory = new File(directory);
		utils.loadFromFile(reader, inputDirectory.getParentFile().getAbsolutePath(), shell);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (hibernateFile != null) {
				hibernateFile.delete();
			}
			if (n != null) {
				n.delete();
			}
			if (dir != null) {
				dir.delete();
			}
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		this.directory = utils.getSelectionPath(arg1, false, true);
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		shell = arg1.getSite().getShell();
	}

}
