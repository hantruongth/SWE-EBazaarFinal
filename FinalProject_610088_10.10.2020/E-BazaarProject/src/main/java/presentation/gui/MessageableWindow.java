package presentation.gui;

import javafx.scene.text.Text;

public interface MessageableWindow {
	public Text getMessageBar();

	public default void displayError(String msg) {
		getMessageBar().setFill(GuiConstants.ERROR_MESSAGE_COLOR);
		getMessageBar().setText(msg);
	}

	public default void displayInfo(String msg) {
		getMessageBar().setFill(GuiConstants.INFO_MESSAGE_COLOR);
		getMessageBar().setText(msg);
	}

	public default void clearMessages() {
		getMessageBar().setText("");
	}
}
