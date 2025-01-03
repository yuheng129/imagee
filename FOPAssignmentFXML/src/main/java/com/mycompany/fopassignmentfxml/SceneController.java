package com.mycompany.fopassignmentfxml;

import java.io.IOException;

import javafx.fxml.FXML;

public class SceneController {
    @FXML

    public void loginScene() throws IOException {
        App.setRoot("LoginPage");        
    }

    public void signUpScene() throws IOException {
        App.setRoot("SignUpPage");
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
