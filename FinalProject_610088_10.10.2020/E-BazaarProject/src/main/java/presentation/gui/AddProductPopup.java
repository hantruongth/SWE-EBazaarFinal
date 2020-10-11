package presentation.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import presentation.data.DefaultData;
import presentation.data.ProductPres;
import business.externalinterfaces.*;
import business.productsubsystem.*;
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

public class AddProductPopup extends Popup {
	MaintainProductsWindow maintainProductsWindow;
	TextField catalogName = new TextField();
	TextField id = new TextField();
	TextField name = new TextField();
	TextField manufactureDate = new TextField();
	TextField numAvail = new TextField();
	TextField unitPrice = new TextField();
	TextField description = new TextField();
	
	HBox sceneTitle;
	HBox topLevel;
	Text messageBar = new Text();
	private HBox setUpTopLabel() {
		Label label = new Label("Add Product");
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
	public AddProductPopup(MaintainProductsWindow maintainProductsWindow) {
		setX(50);
        setY(50);
        topLevel = new HBox(10);
        topLevel.setOpacity(1);
        setBackground(Color.KHAKI);
        setBorder();
        catalogName.setEditable(false);
        
		this.maintainProductsWindow = maintainProductsWindow;
		messageBar.setFill(Color.FIREBRICK);
		HBox sceneTitle = setUpTopLabel();
		Label catalogNameLabel = new Label("Catalog");
		Label idLabel = new Label("Product Id:");
		Label nameLabel = new Label("Product Name:");
		Label mgfDateLabel = new Label("Manufacture Date (mm/dd/yyyy):");
		Label numAvailLabel = new Label("Number Items In Stock:");
		Label unitPriceLabel = new Label("Unit Price:");
		Label descriptionLabel = new Label("Description:");
		
		HBox btnbox = setUpButtons();
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		grid.add(sceneTitle, 0, 0, 2, 1);
		grid.add(catalogNameLabel, 0,1);
		grid.add(catalogName, 1,1);
		grid.add(idLabel, 0, 2);
		grid.add(id, 1, 2);
		grid.add(nameLabel, 0, 3);
		grid.add(name, 1, 3);
		grid.add(mgfDateLabel, 0, 4);
		grid.add(manufactureDate, 1, 4);
		grid.add(numAvailLabel, 0, 5);
		grid.add(numAvail, 1, 5);
		grid.add(unitPriceLabel, 0, 6);
		grid.add(unitPrice, 1, 6);
		grid.add(descriptionLabel, 0, 7);
		grid.add(description, 1, 7);
		
		grid.add(messageBar, 0, 9, 2, 1);
		grid.add(btnbox, 0, 10, 2, 1);
		topLevel.getChildren().add(grid);
		getContent().addAll(topLevel);	
	}
	private HBox setUpButtons() {
		Button addButton = new Button("Add Product");
		Button cancelButton = new Button("Cancel");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(cancelButton);
		
	
		addButton.setOnAction(evt -> {
			//Rules should be managed in a more maintainable way
			if(id.getText().trim().equals("")) {
				messageBar.setText("Product Id field must be nonempty! \n[Type '0' to auto-generate ID.]");
			}
			else if(name.getText().trim().equals("")) messageBar.setText("Product Name field must be nonempty!");
			else if(manufactureDate.getText().trim().equals("")) messageBar.setText("Manufacture Date field must be nonempty!");
			else if(numAvail.getText().trim().equals("")) messageBar.setText("Number in Stock field must be nonempty!");
			else if(unitPrice.getText().trim().equals("")) messageBar.setText("Unit Price field must be nonempty!");
			else if(description.getText().trim().equals("")) messageBar.setText("Description field must be nonempty!");
			else {
				String idNewVal = id.getText();
				if(idNewVal.equals("0")) {
					idNewVal = DefaultData.generateId(100);
				} //Catalog c, Integer pi, String pn, int qa, double up, LocalDate md, String d
				Product newProd = ProductSubsystemFacade.createProduct(DefaultData.CATALOG_MAP.get(catalogName.getText()), 
						Integer.parseInt(id.getText()), name.getText(), Integer.parseInt(numAvail.getText()), 
						    Double.parseDouble(unitPrice.getText()), LocalDate.parse(manufactureDate.getText(), DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
						      description.getText());
				ProductPres prodPres = new ProductPres();
				prodPres.setProduct(newProd);
				maintainProductsWindow.addItem(prodPres);
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
	public void setCatalog(String name) {
		catalogName.setText(name);
	}
	void setBackground(Color color) {
		topLevel.backgroundProperty().set(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
}