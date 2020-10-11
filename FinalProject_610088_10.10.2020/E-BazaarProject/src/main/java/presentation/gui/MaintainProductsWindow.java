package presentation.gui;

import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.control.ManageProductsUIControl;
import presentation.data.CatalogPres;
import presentation.data.ManageProductsData;
import presentation.data.ProductPres;

public class MaintainProductsWindow extends Stage implements MessageableWindow {
	private Stage primaryStage;
	private TableView<ProductPres> table = new TableView<ProductPres>();
	ManageProductsData.ManageProductsSynchronizer synchronizer = ManageProductsData.INSTANCE
			.getManageProductsSynchronizer();

	private Text messageBar = new Text();
	private ComboBox<String> catalogCombo = new ComboBox<String>();

	// editable column
	private TableColumn<ProductPres, String> quantityCol;
	private TableColumn<ProductPres, String> nameCol;
	private TableColumn<ProductPres, String> unitPriceCol;
	private TableColumn<ProductPres, String> mfgDateCol;

	public MaintainProductsWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		setTitle("Maintain Products");

		// set up top label
		HBox labelHbox = setUpTopLabel();

		setUpCombo();
		// set up table
		setUpTable();

		// set up buttons
		HBox btnBox = setUpButtons();

		// assemble all in a GridPane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);

		grid.add(labelHbox, 0, 1);
		grid.add(catalogCombo, 0, 2);
		grid.add(table, 0, 3);
		grid.add(messageBar, 0, 4);
		grid.add(btnBox, 0, 5);
		grid.add(new HBox(10), 0, 6);

		// set in scene
		Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);
		setScene(scene);

		// Make sure that mouse click outside of table will also restore row selection
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				messageBar.setText("");
				TableUtil.selectByRow(table);
			}
		});
	}

	public void setData(ObservableList<CatalogPres> catalogs, ObservableList<ProductPres> items) {
		setData(items);
		ObservableList<String> comboItems = FXCollections
				.observableList(catalogs.stream().map(c -> c.nameProperty().get()).collect(Collectors.toList()));
		catalogCombo.setItems(comboItems);
		if (comboItems.size() > 0)
			catalogCombo.setValue(comboItems.get(0));
		// turns on cell selection
		// TableView.TableViewSelectionModel<Product> selModel =
		// TableUtil.selectByCell(table);
	}

	public void setData(ObservableList<ProductPres> items) {
		table.setItems(items);
	}

	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Maintain Products"));
		label.setFont(new Font("Arial", 16));
		HBox labelHbox = new HBox(10);
		labelHbox.setAlignment(Pos.CENTER);
		labelHbox.getChildren().add(label);
		return labelHbox;
	}

	private void setUpCombo() {
		catalogCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldval, String newval) {

				// Guaranteed to find a value
				Optional<CatalogPres> temp = ManageProductsData.INSTANCE.getCatalogList().stream()
						.filter(c -> c.nameProperty().get().equals(newval)).findFirst();
				CatalogPres selected = temp.get();
				ObservableList<ProductPres> list = ManageProductsData.INSTANCE.getProductsList(selected);
				setData(list);
			}
		});
	}

	public void addItem(ProductPres item) {
		CatalogPres selected = ManageProductsData.INSTANCE.getSelectedCatalog();
		ManageProductsData.INSTANCE.addToProdList(selected, item);
		setData(ManageProductsData.INSTANCE.getProductsList(selected));
		TableUtil.refreshTable(table, synchronizer);
	}

	@SuppressWarnings("unchecked")
	private void setUpTable() {
		table.setEditable(true);
		table.setPrefWidth(360);

		quantityCol = TableUtil.makeEditableTableColumn(table, new ProductPres(), "Quantity", "quantityAvailProperty",
				80);
		quantityCol.setOnEditCommit(t -> {
			ProductPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			instance.setQuantityAvail(new SimpleStringProperty(t.getNewValue()));
			TableUtil.refreshTable(table, synchronizer);

		});

		nameCol = TableUtil.makeEditableTableColumn(table, new ProductPres(), "Name", "nameProperty", 80);
		nameCol.setOnEditCommit(t -> {
			ProductPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			instance.setName(new SimpleStringProperty(t.getNewValue()));
			TableUtil.refreshTable(table, synchronizer);

		});
		unitPriceCol = TableUtil.makeEditableTableColumn(table, new ProductPres(), "Unit Price", "unitPriceProperty",
				80);
		unitPriceCol.setOnEditCommit(t -> {
			ProductPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			instance.setUnitPrice(new SimpleStringProperty(t.getNewValue()));
			TableUtil.refreshTable(table, synchronizer);

		});
		mfgDateCol = TableUtil.makeEditableTableColumn(table, new ProductPres(), "Mfg Date", "mfgDateProperty", 80);
		mfgDateCol.setOnEditCommit(t -> {
			ProductPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			String date = t.getNewValue();
			instance.setMfgDate(new SimpleStringProperty(date));
			TableUtil.refreshTable(table, synchronizer);

		});

		table.getColumns().addAll(nameCol, unitPriceCol, mfgDateCol, quantityCol);

		// make sure row selection is enabled after any mouse click
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				TableUtil.selectByRow(table);
			}
		});
	}

	private HBox setUpButtons() {
		Button addButton = new Button("Add New Product");
		Button deleteButton = new Button("Delete Product");
		Button backButton = new Button("Back to Main");

		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(deleteButton);
		btnBox.getChildren().add(backButton);

		deleteButton.setOnAction(evt -> {
			TableUtil.selectByRow(table);
			CatalogPres selectedCatalog = ManageProductsData.INSTANCE.getSelectedCatalog();
			ObservableList<ProductPres> tableItems = ManageProductsData.INSTANCE.getProductsList(selectedCatalog);
			ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
			ObservableList<ProductPres> selectedItems = table.getSelectionModel().getSelectedItems();
			if (tableItems.isEmpty()) {
				messageBar.setText("Nothing to delete!");
			} else if (selectedIndices == null || selectedIndices.isEmpty()) {
				messageBar.setText("Please select a row.");
			} else {
				boolean result = ManageProductsData.INSTANCE.removeFromProductList(selectedCatalog, selectedItems);
				if (result) {
					table.setItems(ManageProductsData.INSTANCE.getProductsList(selectedCatalog));
					clearMessages();
				} else {
					displayInfo("No items deleted.");
				}

			}
		});
		backButton.setOnAction(ManageProductsUIControl.INSTANCE.getBackFromProdsButtonHandler());

		// HERE
		addButton.setOnAction(evt -> {
//			AddProductPopup popup = new AddProductPopup(this);
//			String catNameSelected = ManageProductsData.INSTANCE.getSelectedCatalog().getCatalog().getName();
//			popup.setCatalog(catNameSelected);
//			popup.show(this);
		});
		return btnBox;
	}

	@Override
	public Text getMessageBar() {
		return messageBar;
	}

}
