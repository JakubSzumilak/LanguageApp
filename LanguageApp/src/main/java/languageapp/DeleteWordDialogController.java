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
public class DeleteWordDialogController implements Initializable {

    @FXML
    Button OKButton;
    
    @FXML
    TextField WordField;
    
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
    private void DeleteWord() throws IOException {
        
        String term = WordField.getText();
        if (term.isEmpty()) return;
        
        System.out.println("Delete!");
        Node node = (Node) OKButton;
        Stage stage = (Stage) node.getScene().getWindow();
        PrimaryController primaryController = (PrimaryController)stage.getUserData();
        primaryController.deleteAWord(term);
        WordField.setText("");
    }
    
}
