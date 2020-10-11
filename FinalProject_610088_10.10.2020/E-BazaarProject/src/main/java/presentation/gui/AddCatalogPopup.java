package presentation.gui;

import presentation.data.CatalogPres;
import presentation.data.DefaultData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import business.externalinterfaces.*;
import business.productsubsystem.*;

public class AddCatalogPopup extends Popup {
	MaintainCatalogsWindow maintainCatalogsWindow;
	TextField id;
	TextField name;
	HBox sceneTitle;
	HBox topLevel;
	Text messageBar = new Text();
	private HBox setUpTopLabel() {
		Label label = new Label("Add Catalog");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private void setBorder() {
		final String cssDefault = "-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";
                //+ "-fx-border-style: dashed;\n";
        topLevel.setStyle(cssDefault);
	}
	public AddCatalogPopup(MaintainCatalogsWindow maintainCatalogsWindow) {
		setX(50);
        setY(50);
        topLevel = new HBox(10);
        topLevel.setOpacity(1);
        setBackground(Color.KHAKI);
        setBorder();
        
		this.maintainCatalogsWindow = maintainCatalogsWindow;
		messageBar.setFill(Color.FIREBRICK);
		HBox sceneTitle = setUpTopLabel();
		Label idLabel = new Label("Catalog Id:");
		Label nameLabel = new Label("Catalog Name:");
		id = new TextField();
		name = new TextField();
		HBox btnbox = setUpButtons();
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.add(sceneTitle, 0, 0, 2, 1);
		grid.add(idLabel, 0, 1);
		grid.add(id, 1, 1);
		grid.add(nameLabel, 0, 2);
		grid.add(name, 1, 2);
		grid.add(messageBar, 0, 3, 2, 1);
		grid.add(btnbox, 0, 5, 2, 1);
		topLevel.getChildren().add(grid);
		getContent().addAll(topLevel);	
	}
	private HBox setUpButtons() {
		Button addButton = new Button("Add Catalog");
		Button cancelButton = new Button("Cancel");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(cancelButton);
		
	
		addButton.setOnAction(evt -> {
			if(id.getText().trim().equals("")) {
				messageBar.setText("ID field must be nonempty! \n[Type '0' to auto-generate ID.]");
			}
			else if(name.getText().trim().equals("")) messageBar.setText("Name field must be nonempty!");
			else {
				String idNewVal = id.getText();
				if(idNewVal.equals("0")) {
					idNewVal = DefaultData.generateId(10);
				}
				Catalog newCat = ProductSubsystemFacade.createCatalog(Integer.parseInt(idNewVal), name.getText());
				CatalogPres catPres = new CatalogPres();
				catPres.setCatalog(newCat);
				maintainCatalogsWindow.addItem(catPres);
				messageBar.setText("");
				hide();
			}	   
		});
		cancelButton.setOnAction(evt -> {
			messageBar.setText("");
			hide();
		});
		
		return btnBox;
	}
	void setBackground(Color color) {
		topLevel.backgroundProperty().set(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
}
