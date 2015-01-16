/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.osgi.framework.ServiceReference;

import xstampp.ui.common.IProcessController;
import xstampp.ui.common.ViewContainer;
import xstampp.update.UpdateJob;
import xstampp.util.ChooseWorkLocation;

/**
 * Configures the workbench window.
 * 
 * @author Patrick Wickenhaeuser
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * The minimum and the initial size of the window
	 */
	private static final Point MINIMUM_WINDOW_SIZE = new Point(1024, 768);

	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = Logger.getRootLogger();

	private UpdateJob updateJob;

	/**
	 * Constructor of the advisor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param configurer
	 *            the configurer used to configure the workbench.
	 */
	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@SuppressWarnings("restriction")
	@Override
	public void postWindowOpen() {
		if(ChooseWorkLocation.initiateWorkspace()){
			PlatformUI.getWorkbench().restart();
		}
		AbstractExtensionWizardRegistry wizardRegistry = (AbstractExtensionWizardRegistry) PlatformUI
				.getWorkbench().getExportWizardRegistry();
		IWizardCategory[] categories = wizardRegistry.getRootCategory()
				.getCategories();
		for (IWizardDescriptor wizard : this.getAllWizards(categories)) {

			if (wizard.getCategory().getId().matches("org.eclipse.ui.Basic") || //$NON-NLS-1$
					wizard.getCategory().getId().endsWith("category")) { //$NON-NLS-1$

				WorkbenchWizardElement wizardElement = (WorkbenchWizardElement) wizard;
				if (!wizardElement.getId().matches(
						"org.eclipse.ui.wizards.export.Preferences")) { //$NON-NLS-1$
					wizardRegistry.removeExtension(wizardElement
							.getConfigurationElement().getDeclaringExtension(),
							new Object[] { wizardElement });
				}
			}
		}

		wizardRegistry = (AbstractExtensionWizardRegistry) PlatformUI
				.getWorkbench().getImportWizardRegistry();
		categories = wizardRegistry.getRootCategory().getCategories();
		for (IWizardDescriptor wizard : this.getAllWizards(categories)) {
			WorkbenchWizardElement wizardElement = (WorkbenchWizardElement) wizard;
			if (!wizardElement.getId().matches(
					"org.eclipse.ui.wizards.import.Preferences")) { //$NON-NLS-1$
				wizardRegistry.removeExtension(wizardElement
						.getConfigurationElement().getDeclaringExtension(),
						new Object[] { wizardElement });
			}
		}
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = this.getWindowConfigurer();
		configurer
				.setInitialSize(ApplicationWorkbenchWindowAdvisor.MINIMUM_WINDOW_SIZE);
		configurer.setShowCoolBar(true);
		configurer.setShellStyle(SWT.APPLICATION_MODAL);
		configurer.getActionBarConfigurer().getCoolBarManager()
				.setLockLayout(false);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle(Messages.PlatformName);

	}

	private IWizardDescriptor[] getAllWizards(IWizardCategory[] categories) {
		List<IWizardDescriptor> results = new ArrayList<IWizardDescriptor>();
		for (IWizardCategory wizardCategory : categories) {
			results.addAll(Arrays.asList(wizardCategory.getWizards()));
			results.addAll(Arrays.asList(this.getAllWizards(wizardCategory
					.getCategories())));
		}
		return results.toArray(new IWizardDescriptor[0]);
	}

	@Override
	public void postWindowCreate() {
		super.postWindowCreate();
		final Shell shell = this.getWindowConfigurer().getWindow().getShell();
		shell.setMinimumSize(ApplicationWorkbenchWindowAdvisor.MINIMUM_WINDOW_SIZE);

		IProcessController viewContainer = ViewContainer.getContainerInstance();

		if (viewContainer == null) {
			ApplicationWorkbenchWindowAdvisor.LOGGER
					.error("View Container not found"); //$NON-NLS-1$
			return;
		}

		ServiceReference<?> reference = Activator.getContext()
				.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		final IProvisioningAgent agent = (IProvisioningAgent) Activator
				.getContext().getService(reference);
		Activator.getContext().ungetService(reference);
		this.updateJob = new UpdateJob(Messages.UpdatingASTPA, agent, shell,
				viewContainer, true);
		this.updateJob.schedule();
	}

	@Override
	public boolean isDurableFolder(String perspectiveId, String folderId) {
		return true;
	}

	@Override
	public boolean preWindowShellClose() {
		ViewContainer viewContainerViewPart = ViewContainer
				.getContainerInstance();

		if (viewContainerViewPart instanceof ViewContainer) {
			ViewContainer viewContainer = viewContainerViewPart;
			if (viewContainer.getUnsavedChanges()) {
				MessageDialog dialog = new MessageDialog(Display.getCurrent()
						.getActiveShell(), Messages.PlatformName, null,
						Messages.ThereAreUnsafedChangesDoYouWantToStoreThem,
						MessageDialog.CONFIRM, new String[] { Messages.Store,
								Messages.Discard, Messages.Abort }, 0);
				int resultNum = dialog.open();
				switch (resultNum) {
				case -1:
					return false;
				case 0:
					return viewContainer.saveAllDataModels();
				case 1:
					return true;
				case 2:
					return false;
				default:
					break;
				}
			}
		}
		this.updateJob.cancel();
		return true;
	}
}