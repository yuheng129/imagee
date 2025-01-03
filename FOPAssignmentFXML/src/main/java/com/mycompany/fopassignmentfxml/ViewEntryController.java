package com.mycompany.fopassignmentfxml;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewEntryController implements Initializable{
    @FXML
    private TableView<Diary> table;

    @FXML
    private TableColumn<Diary, LocalDate> dateCol;

    @FXML
    private TableColumn<Diary, String> titleCol;

    @FXML
    private TableColumn<Diary, String> moodCol;

    @FXML
    private Button deleteButton;

    private DiarySystem diarySystem = new DiarySystem(UserSession.getCurrentUser().getUsername());
    ObservableList<Diary> list = FXCollections.observableArrayList(diarySystem.getDiaryEntries());

    public static int currentIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateCol.setCellValueFactory(new PropertyValueFactory<Diary, LocalDate>("date"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
        moodCol.setCellValueFactory(new PropertyValueFactory<Diary, String>("mood"));
        
        //Set a cell factory to format LocalDate as dd/MM/yyyy
        dateCol.setCellFactory(column -> {
            return new TableCell<Diary, LocalDate>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null); //If the cell is empty or the date is null, clear the cell
                    } 
                    else {
                        setText(date.format(formatter));  //Format the LocalDate to dd/MM/yyyy
                    }
                }
            };
        });
        table.setItems(list);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Detect double-click
                currentIndex = table.getSelectionModel().getSelectedIndex();
                if (currentIndex>=0 && currentIndex<diarySystem.getDiaryEntries().size()) {
                    try {
                        detailedViewScene();
                        
                    } catch (IOException e) {
                        System.out.println("An error occured while entering detailed view.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void homeScene() throws IOException {
        App.setRoot("HomePage");
    }

    public void newEntryScene() throws IOException {
        App.setRoot("NewEntryPage");
    }

    public void detailedViewScene() throws IOException {
        App.setRoot("DetailedViewPage");
    }


    public void deleteEntry() {
        int entryIdx = table.getSelectionModel().getSelectedIndex();
        if (entryIdx >= 0) { // Ensure an entry is selected
            Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
            deleteAlert.setTitle("Delete Confirmation");
            deleteAlert.setHeaderText("Are you sure you want to delete this entry?");
            deleteAlert.setContentText("The deleted entry can still be restored from recycle bin for 30 days.");
            
            if (deleteAlert.showAndWait().get() == ButtonType.OK) {
                diarySystem.recycleEntry(entryIdx);
                list.remove(entryIdx);  // Remove the entry directly from the list
                if (diarySystem.saveEntries()) {
                    Alert saveAlert = new Alert(AlertType.INFORMATION);
                    saveAlert.setTitle("Entry Deleted Successfully");
                    saveAlert.setHeaderText("Your diary entry has been deleted!");
                    saveAlert.setContentText("You can view your deleted entry in the recycle bin. Keep journaling!");
                    saveAlert.showAndWait();
                }
                else {
                    Alert failedSaveAlert = new Alert(AlertType.ERROR);
                    failedSaveAlert.setTitle("Oops! Something Went Wrong");
                    failedSaveAlert.setHeaderText("We couldn’t delete your entry.");
                    failedSaveAlert.showAndWait();
                }
            }
        }
        else {
            Alert warningAlert = new Alert(AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Entry Selected");
            warningAlert.setContentText("Please select an entry to delete.");
            warningAlert.showAndWait();
        }
    }
}
