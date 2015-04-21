package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

/**
 * this Handler adds a Bullet List to a selection of text
 * this is done by calling {@link ITextEditor#setBullet(String)}
 * with the value of the parameter <code>xstampp.commandParameter.bulletlist</Code>
 * @author Lukas Balzer
 * 
 * @see ITextEditor
 * @since 1.0
 */
public class BulletListHandler extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object activeEditor = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String bulletType= event.getParameter("xstampp.commandParameter.bulletlist");
		if(activeEditor instanceof ITextEditor && bulletType != null){
			((ITextEditor) activeEditor).setBullet(bulletType);
		}
		return null;
	}

}
