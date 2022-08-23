package com.wintex.douane;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class HomeController implements Initializable {

	private List<Record> records = new ArrayList<>();

	@FXML
	private Button selectFileButton;

	@FXML
	private TableView<Record> detectionResultTable;

	@FXML
	private TableColumn<Record, String> idTrackingColumn;

	@FXML
	private TableColumn<Record, String> numberTrackingColumn;

	@FXML
	private TableColumn<Record, String> fullNamColumn;

	@FXML
	private TableColumn<Record, String> adresseColumn;

	@FXML
	private TableView<Record> detectionResultTableAdr;

	@FXML
	private TableColumn<Record, String> idTrackingColumnAdr;

	@FXML
	private TableColumn<Record, String> numberTrackingColumnAdr;

	@FXML
	private TableColumn<Record, String> fullNamColumnAdr;

	@FXML
	private TableColumn<Record, String> adresseColumnAdr;

	@FXML
	private ProgressBar detectionProgressBar;

	FileChooser fileChooser = new FileChooser();
	// Set extension filter
	FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("Excel fichiers (*.xlsx)", "*.xlsx");
	FileChooser.ExtensionFilter xlsFilter = new FileChooser.ExtensionFilter("Excel fichiers (*.xls)", "*.xls");
	List<ExtensionFilter> filters = new ArrayList<ExtensionFilter>();

	@FXML
	public void onSelectFile(ActionEvent event) {
		filters.add(xlsxFilter);
		filters.add(xlsFilter);
		fileChooser.getExtensionFilters().addAll(filters);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {

			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws InterruptedException {
					selectFileButton.setVisible(false);
					detectionProgressBar.setVisible(true);

					createRecordsFromSheet(file);
					createTableViewObservableList();

					Map<String, List<Record>> grouped = ServicePhonetic.groupByFullNamePhonetic(records);
					List<Record> duplicatedRecordsByFullName = ServicePhonetic.getDuplicatedRecords(grouped);
					detectionResultTable.setItems(getRecords(duplicatedRecordsByFullName));

					grouped = ServicePhonetic.groupByAddressPhonetic(records);
					List<Record> duplicatedRecordsByAdress = ServicePhonetic.getDuplicatedRecords(grouped);
					detectionResultTableAdr.setItems(getRecords(duplicatedRecordsByAdress));
					return null;
				}
			};

			task.setOnSucceeded(e -> {
				showAlert("Détection Terminée", null, AlertType.INFORMATION);
				detectionProgressBar.setVisible(false);
				selectFileButton.setVisible(true);
			});

			task.setOnFailed(e -> {
				showAlert("Détection Echoué", null, AlertType.ERROR);
				detectionProgressBar.setVisible(false);
				selectFileButton.setVisible(true);
			});

			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public void createRecordsFromSheet(File file) {
		records = ServiceXlsxRead.readXlsxFromFile(file);
	}

	public ObservableList<Record> createTableViewObservableList() {
		ObservableList<Record> recordObservableList = FXCollections.observableArrayList();
		records.forEach(record -> recordObservableList.add(record));
		return recordObservableList;
	}

	public ObservableList<Record> getRecords(List<Record> records) {
		ObservableList<Record> recordObservableList = FXCollections.observableArrayList();
		records.forEach(record -> recordObservableList.add(record));
		return recordObservableList;
	}

	private static void showAlert(String title, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(title);
		alert.setContentText(content);
		alert.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		detectionProgressBar.setVisible(false);
		/*detectionResultTable.setOnMousePressed((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Record record = detectionResultTable.getSelectionModel().getSelectedItem();
					if (record.getIdTracking() != null)
						try {
							ServiceJasperReport.exportToPdf(record);
						} catch (URISyntaxException | JRException e) {
							e.printStackTrace();
						}
				}
			}
		});*/
		detectionResultTable.setRowFactory(tv -> new TableRow<Record>() {
			@Override
			protected void updateItem(Record item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null)
					setStyle("");
				else if (item.getIdTracking() == null)
					setStyle("-fx-background-color: #E1F5FE;");
				else
					setStyle("");
			}

		});

		/*detectionResultTableAdr.setOnMousePressed((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					Record record = detectionResultTableAdr.getSelectionModel().getSelectedItem();
					if (record.getIdTracking() != null)
						try {
							ServiceJasperReport.exportToPdf(record);
						} catch (URISyntaxException | JRException e) {
							e.printStackTrace();
						}
				}
			}
		});*/

		detectionResultTableAdr.setRowFactory(tv -> new TableRow<Record>() {
			@Override
			protected void updateItem(Record item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null)
					setStyle("");
				else if (item.getIdTracking() == null)
					setStyle("-fx-background-color: #FFEBEE;");
				else
					setStyle("");
			}
		});

		idTrackingColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("idTracking"));
		numberTrackingColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("numberTacking"));
		fullNamColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("fullName"));
		adresseColumn.setCellValueFactory(new PropertyValueFactory<Record, String>("addresse"));

		idTrackingColumnAdr.setCellValueFactory(new PropertyValueFactory<Record, String>("idTracking"));
		numberTrackingColumnAdr.setCellValueFactory(new PropertyValueFactory<Record, String>("numberTacking"));
		fullNamColumnAdr.setCellValueFactory(new PropertyValueFactory<Record, String>("fullName"));
		adresseColumnAdr.setCellValueFactory(new PropertyValueFactory<Record, String>("addresse"));

	}
}
