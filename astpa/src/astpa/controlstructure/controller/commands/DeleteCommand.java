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

package astpa.controlstructure.controller.commands;

import java.util.Map;
import java.util.UUID;

import astpa.model.controlstructure.interfaces.IRectangleComponent;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class DeleteCommand extends ControlStructureAbstractCommand {
	
	private IRectangleComponent model;
	private IRectangleComponent parentModel;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * @param idMap the map in which all components are mapped so that a delete
	 *            and a change of the id can be tracked
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public DeleteCommand(Map<UUID, UUID> idMap, IControlStructureEditorDataModel model) {
		super(idMap, model, null);
	}
	
	@Override
	public void execute() {
		
		this.getDataModel().removeComponent(this.model.getId());
		
	}
	
	@Override
	public boolean canExecute() {
		return ((this.model != null) && (this.parentModel != null));
	}
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param model the model which is to delete
	 * 
	 */
	public void setModel(IRectangleComponent model) {
		this.model = model;
		
	}
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param parentModel the parentModel which contains the model as child
	 */
	public void setParentModel(Object parentModel) {
		if (parentModel instanceof IRectangleComponent) {
			this.parentModel = (IRectangleComponent) parentModel;
		}
	}
	
	@Override
	public void undo() {
		UUID tmpID =
			this.getDataModel().addComponent(this.parentModel.getId(), this.model.getLayout(true),
				this.model.getText(), this.model.getComponentType());
		this.getDataModel().changeComponentLayout(tmpID, this.model.getLayout(false), false);
		
	}
	
}
