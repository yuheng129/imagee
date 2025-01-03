package com.mycompany.fopassignmentfxml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewEntryController implements Initializable {
    @FXML
    private Label homeLabel;
    @FXML
    private Label viewEntryLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField titleField;
    @FXML
    private ChoiceBox<String> moodBox;
    @FXML
    private TextArea contentArea;
    @FXML
    private Button discardButton;
    @FXML
    private Button saveButton;

    private String[] moods = {"Happy","Sad","Angry","Neutral"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        moodBox.getItems().addAll(moods);
    }

    public void homeScene() throws IOException {
        App.setRoot("HomePage");
    }

    public void viewEntryScene() throws IOException {
        App.setRoot("ViewEntryPage");
    }
    
    public DiarySystem diarySystem = new DiarySystem(UserSession.getCurrentUser().getUsername());

    
    public void saveEntry() {
        Alert validationAlert = new Alert(AlertType.ERROR);
        validationAlert.setTitle("Missing Information");
        validationAlert.setHeaderText("Please fill out all required fields.");
        validationAlert.setContentText("Make sure that you select a date, enter a title, select a mood, and write your content.");
        
        Diary entry = new Diary();
        entry.setDate(datePicker.getValue());
        entry.setTitle(titleField.getText().trim());
        entry.setMood(moodBox.getValue());
        String content = contentArea.getText().replace("\n", "`");
        entry.setContent(content);
        
        // Check for blank or null fields
        if (entry.getDate() ==  null || entry.getTitle().isEmpty() || entry.getTitle() == null || entry.getContent().trim().isEmpty() || entry.getContent() == null || entry.getMood() == null || entry.getMood().trim().isEmpty()) {
            validationAlert.showAndWait();
            return; // Exit the method to prevent saving if fields are invalid
        }

        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Save Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to save current entry?");
        confirmationAlert.setContentText("You can still edit it later in [View Entry] tab.");
        
        if (confirmationAlert.showAndWait().get() ==  ButtonType.OK) {
            diarySystem.addEntry(entry);
            clearInputs();            
            if (diarySystem.saveEntries()) {
                Alert saveAlert = new Alert(AlertType.INFORMATION);
                saveAlert.setTitle("Entry Saved Successfully");
                saveAlert.setHeaderText("Your diary entry has been saved!");
                saveAlert.setContentText("You can view your saved entry in the [View Entry] tab. Keep journaling!");
                saveAlert.showAndWait();
            }
            else {
                Alert failedSaveAlert = new Alert(AlertType.ERROR);
                failedSaveAlert.setTitle("Oops! Something Went Wrong");
                failedSaveAlert.setHeaderText("We couldn’t save your entry.");
                failedSaveAlert.setContentText("Please make sure all required fields are filled out. Try saving again.");
                failedSaveAlert.showAndWait();
            }
        }

    }

    public void discardEntry() {
        Alert confirmDiscard = new Alert(AlertType.CONFIRMATION);
        confirmDiscard.setTitle("Discard Confirmation");
        confirmDiscard.setHeaderText("Are you sure you want to discard the entry?");
        confirmDiscard.setContentText("This action cannot be undone.");

        if (confirmDiscard.showAndWait().get() == ButtonType.OK) {
            clearInputs();
        }
    }

    public void clearInputs() {
        datePicker.setValue(null);
        titleField.clear();
        moodBox.setValue(null);
        contentArea.clear();
    }
}
