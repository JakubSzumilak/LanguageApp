/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package languageapp;

import DataBasePackage.Term;
import DataBasePackage.Translation;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author s39910
 */
public class DataBaseViewerController implements Initializable {
    
    private List<Term> Terms;
    private List<Translation> Translations;
    private PrimaryController primaryController;
    
    boolean bInitialized = false;
    int TotalLoadedTerms = 0;
    int NextIndex = 0;
    private Label[] TermLabels = new Label[20];
    private Label[] TranslationLabels = new Label[20];
    
    @FXML
    Button OkButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    // Binds text labels to array elements of TermLabels and TranslationLabels
    private void SetupLabels()
    {
        // Getting the scene
        Scene scene = OkButton.getScene();
        
        // Setting up the term labels
        for (int i = 0; i < TermLabels.length; i++)
        {
            String Name = "#Term" + (i + 1);
            TermLabels[i] = (Label)scene.lookup(Name);
        }
        
        // Setting up the translation labels
        for (int i = 0; i < TranslationLabels.length; i++)
        {
            String Name = "#Translation" + (i + 1);
            TranslationLabels[i] = (Label)scene.lookup(Name);

        }
    }
    
    // Gets the lists of terms and translations that are to be displayed
    private void SetupLists()
    {
        Node node = (Node) OkButton;
        Stage stage = (Stage) node.getScene().getWindow();
        primaryController = (PrimaryController)stage.getUserData();
        Terms = primaryController.getTerms();
        Translations = primaryController.getTranslations();
    }
    
    private void ClearLabels()
    {
        for (int i = 0; i < 20; i++)
        {
            TermLabels[i].setText("");
            TranslationLabels[i].setText("");
        }
    }
    
    @FXML
    private void PopulateDictionary() throws IOException {
        
        // Initializing
        if (!bInitialized)
        {
            SetupLabels();
            SetupLists();
            bInitialized = true;
        }
        
        if (TotalLoadedTerms >= Terms.size())
        {
            Node node = (Node) OkButton;
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
        
        // Wiping out any text in both translation and term labels
        ClearLabels();
        
        int termsToLoad;
        if (Terms.size() - TotalLoadedTerms > 20)
        {
            // If there are more than 20 terms to load,
            // load just 20
            
            termsToLoad = 20;
        }
        else
        {
            // If there are less than 20 terms to load,
            // load just the remaining ones
            
            termsToLoad = Terms.size() - TotalLoadedTerms;
        }
        TotalLoadedTerms += termsToLoad;
        
        for (int i = 0; i < termsToLoad; i++)
        {
            Term term = Terms.get(NextIndex);
            int termID = term.getID();
            String termText = term.getTerm();
            String translationText = "";
            TermLabels[i].setText(termText);
            NextIndex++;
            
            for (int j = 0; j < Translations.size(); j++)
            {
                Translation transl = Translations.get(j);
                if (transl.getTermID() == termID)
                {
                    translationText += transl.getTranslation() + ", ";
                }
            }
            
            TranslationLabels[i].setText(translationText);
        }
        
        
        
        /*
        for (int i = 0; i < Terms.size(); i++)
        {
            Term term = Terms.get(i);
            System.out.println("Word [" + i + "] - " + term.getTerm());
            int termID = term.getID();
            int translationNum = 0;
            for (int j = 0; j < Translations.size(); j++)
            {
                Translation transl = Translations.get(j);
                if (transl.getTermID() == termID)
                {
                    System.out.println(
                    "\t\tTranslation [" + translationNum++ + "] - " + transl.getTranslation()
                    );
                }

            }
        }
*/
    }
    
    
}
