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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DetailedViewController implements Initializable{
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField titleField;

    @FXML
    private ChoiceBox<String> moodBox;
    
    @FXML
    private CheckBox editingBox;

    @FXML
    private TextArea contentField;

    @FXML
    private Button saveButton;
    
    private String[] moods = {"Happy","Sad","Angry","Neutral"};

    DiarySystem diarySystem = new DiarySystem(UserSession.getCurrentUser().getUsername());

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        moodBox.getItems().addAll(moods);
        setDisplay();
    }
    
    public void editing() {
        if (editingBox.isSelected()) {
            datePicker.setEditable(true);
            titleField.setEditable(true);
            moodBox.setDisable(false);
            contentField.setEditable(true);
            saveButton.setVisible(true);
        }
        else {
            datePicker.setEditable(false);
            titleField.setEditable(false);
            moodBox.setDisable(true);
            contentField.setEditable(false);
            saveButton.setVisible(false);
        }
    }

    public void saveEdit() {
        Alert validationAlert = new Alert(AlertType.ERROR);
        validationAlert.setTitle("Missing Information");
        validationAlert.setHeaderText("Please fill out all required fields.");
        validationAlert.setContentText("Make sure that you select a date, enter a title, select a mood, and write your content.");
        
        Diary editedEntry = new Diary();
        editedEntry.setDate(datePicker.getValue());
        editedEntry.setTitle(titleField.getText().trim());
        editedEntry.setMood(moodBox.getValue());
        String content = contentField.getText().replace("\n", "`");
        editedEntry.setContent(content);

        // Check for blank or null fields
        if (editedEntry.getDate() ==  null || editedEntry.getTitle().isEmpty() || editedEntry.getTitle() == null || editedEntry.getContent().trim().isEmpty() || editedEntry.getContent() == null || editedEntry.getMood() == null || editedEntry.getMood().trim().isEmpty()) {
            validationAlert.showAndWait();
            return; // Exit the method to prevent saving if fields are invalid
        }

        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Save Edit Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to save current edit?");
        confirmationAlert.setContentText("The changes cannot be undone.");

        if (confirmationAlert.showAndWait().get() ==  ButtonType.OK) {
            diarySystem.getDiaryEntries().set(ViewEntryController.currentIndex, editedEntry);
            if (diarySystem.saveEntries()) {
                Alert saveAlert = new Alert(AlertType.INFORMATION);
                saveAlert.setTitle("Edits Saved Successfully");
                saveAlert.setHeaderText("Your diary entry has been edited!");
                saveAlert.setContentText("You can view your edited entry in the [View Entry] tab. Keep journaling!");
                saveAlert.showAndWait();
            }
            else {
                Alert failedSaveAlert = new Alert(AlertType.ERROR);
                failedSaveAlert.setTitle("Oops! Something Went Wrong");
                failedSaveAlert.setHeaderText("We couldn’t save your edits.");
                failedSaveAlert.setContentText("Please make sure all required fields are filled out. Try saving again.");
                failedSaveAlert.showAndWait();
            }
        }
        
        
    }

    public void discardEntry() {
        Alert confirmDiscard = new Alert(AlertType.CONFIRMATION);
        confirmDiscard.setTitle("Discard Confirmation");
        confirmDiscard.setHeaderText("Are you sure you want to discard changes?");
        confirmDiscard.setContentText("This action cannot be undone.");

        if (confirmDiscard.showAndWait().get() == ButtonType.OK) {
            setDisplay();
        }
    }

    public void setDisplay() {
        datePicker.setValue(diarySystem.getDiaryEntries().get(ViewEntryController.currentIndex).getDate());
        titleField.setText(diarySystem.getDiaryEntries().get(ViewEntryController.currentIndex).getTitle());
        moodBox.setValue(diarySystem.getDiaryEntries().get(ViewEntryController.currentIndex).getMood());
        String content = diarySystem.getDiaryEntries().get(ViewEntryController.currentIndex).getContent().replace("`", "\n");
        contentField.setText(content);
    }

    public void homeScene() throws IOException {
        App.setRoot("HomePage");
    }

    public void newEntryScene() throws IOException {
        App.setRoot("NewEntryPage");
    }

    public void viewEntryScene() throws IOException {
        App.setRoot("ViewEntryPage");
    }
}
