/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package languageapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author s39910
 */
public class AddWordDialogController implements Initializable {

    @FXML
    Button OKButton;
    
    @FXML
    TextField WordField;
            
    @FXML
    TextField TranslationField;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void CloseApp() throws IOException {
        System.out.println("Close!");
        Node node = (Node) OKButton;
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void AddWord() throws IOException {
        String word = WordField.getText();
        String translation = TranslationField.getText();
        if (word.isEmpty() || translation.isEmpty()) return;
        

        
        System.out.println("Add!");
        Node node = (Node) OKButton;
        Stage stage = (Stage) node.getScene().getWindow();
        PrimaryController primaryController = (PrimaryController)stage.getUserData();
        primaryController.addAWord(WordField.getText(), TranslationField.getText());
        
        WordField.setText("");
        TranslationField.setText("");
    }
    
}
