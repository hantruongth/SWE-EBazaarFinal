package presentation.gui;

import javafx.scene.control.TableView;

public interface ModifiableTableWindow {
	public TableView getTable();

	default public void setTableAccessByRow() {
		TableUtil.selectByRow(getTable());
	}

	default public void setTableAccessByCell() {
		TableUtil.selectByCell(getTable());
	}
}
