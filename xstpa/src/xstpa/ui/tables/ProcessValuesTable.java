package xstpa.ui.tables;

import java.util.ArrayList;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import xstpa.model.ProcessModelValue;
import xstpa.model.XSTPADataController;
import xstpa.ui.View;
import xstpa.ui.tables.utils.EntryCellModifier;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class ProcessValuesTable extends AbstractTableComposite {

	private abstract class ValueLabelsProvider extends ColumnLabelProvider{
			
		@Override
		public Color getBackground(Object element) {
			ArrayList<?> list = (ArrayList<?>) mainViewer.getInput();
			int index = list.indexOf(element);
			if ((index % 2) == 0) {
				return View.BACKGROUND;
			} else {	    
				return null;
			}
		}
	}
	
	private TableViewer mainViewer;
	private Table table;

	public ProcessValuesTable(Composite parent,XSTPADataController controller) {
		super(parent, controller);
		TableColumnLayout tLayout = new TableColumnLayout();
		setLayout(tLayout);
		 // Add the TableViewers   
		mainViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		mainViewer.setContentProvider(new MainViewContentProvider());
		
		table = mainViewer.getTable();
		table.setHeaderVisible(true);
	    table.setLinesVisible(true);
		 // Create the cell editors
	    CellEditor[] editors = new CellEditor[5];
	    editors[4] = new TextCellEditor(table);
	    
	    mainViewer.setColumnProperties(View.PROPS_COLUMNS);
	    mainViewer.setCellModifier(new EntryCellModifier(mainViewer));
	    mainViewer.setCellEditors(editors);
	    
		// add Columns for the mainTable
	    TableViewerColumn valuesColumn = new TableViewerColumn(mainViewer, SWT.LEFT);
	    valuesColumn.getColumn().setText(View.CONTROLLER);
	    valuesColumn.setLabelProvider(new ValueLabelsProvider() {
	    	public String getText(Object element) {
				return ((ProcessModelValue) element).getController();
			}
		});
	    
	    tLayout.setColumnData(valuesColumn.getColumn(), new ColumnWeightData(1, 30, false));
	    
	    valuesColumn = new TableViewerColumn(mainViewer, SWT.LEFT);
	    valuesColumn.getColumn().setText(View.PM);
	    valuesColumn.setLabelProvider(new ValueLabelsProvider() {
	    	public String getText(Object element) {
				return ((ProcessModelValue) element).getPM();
			}
		});
	    
	    tLayout.setColumnData(valuesColumn.getColumn(), new ColumnWeightData(1, 30, false));
	    
	    valuesColumn = new TableViewerColumn(mainViewer, SWT.LEFT);
	    valuesColumn.getColumn().setText(View.PMV);
	    valuesColumn.setLabelProvider(new ValueLabelsProvider() {
	    	public String getText(Object element) {
				return ((ProcessModelValue) element).getPMV();
			}
		});
	    
	    tLayout.setColumnData(valuesColumn.getColumn(), new ColumnWeightData(2, 50, false));
	    
	    valuesColumn = new TableViewerColumn(mainViewer, SWT.LEFT);
	    valuesColumn.getColumn().setText(View.PMVV);
	    valuesColumn.setLabelProvider(new ValueLabelsProvider() {
	    	public String getText(Object element) {
				return ((ProcessModelValue) element).getValueText();
			}
		});
	    
	    tLayout.setColumnData(valuesColumn.getColumn(), new ColumnWeightData(1, 30, false));
	    
	    valuesColumn = new TableViewerColumn(mainViewer, SWT.LEFT);
	    valuesColumn.getColumn().setText(View.COMMENTS);
	    valuesColumn.setLabelProvider(new ValueLabelsProvider() {
	    	public String getText(Object element) {
				return ((ProcessModelValue) element).getComments();
			}
		});
	    tLayout.setColumnData(valuesColumn.getColumn(), new ColumnWeightData(1, 30, false));

	    setVisible(false);
	}

	public void activate(){
		 mainViewer.setInput(dataController.getValuesList());
		  for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			  
			  table.getColumn(i).pack();
		  }
		  setVisible(true);
	}
	
	public void refreshTable(){
		if(mainViewer.getControl() != null && !mainViewer.getControl().isDisposed()){
	    	  mainViewer.setInput(dataController.getValuesList());	      
	      }
	     
		  for (int i = 0; i < 5 && !table.isDisposed(); i++) {
			  if (table.getColumn(i).getWidth() < 1) {
				  table.getColumn(i).pack();
			  }
		  }
	}
}