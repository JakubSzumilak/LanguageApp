package languageapp;

import DataBasePackage.DataBaseManager;
import DataBasePackage.Term;
import DataBasePackage.Translation;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    DataBaseManager DBManager;
    
    @FXML
    private Slider entriesNumSlider;
    @FXML
    private Label entriesCountLabel;
    @FXML
    private Button checkButton;
    @FXML
    private Button loadNextButton;
    @FXML
    private ProgressBar progressBar;
    
    Scene scene;

    private Label[] TermLabels = new Label[12];
    private TextField[] TranslationFields = new TextField[12];
    private int[] TranslationIndexes = new int[12];
    
    private List<Term> Terms;
    private List<Translation> Translations;
    
    private int EntriesCount = 0;
    private int TotalEntries = 0;
    private int NextEntryIndex = 0; 
    private int LastEntryIndex = 0;
    private int TotalCorrect = 0;
    private boolean bInit = true;
    private boolean bInitTables = true;
    private boolean bReset = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        DBManager = new DataBaseManager();
        Terms = DBManager.selectTerms();
        Translations = DBManager.selectTranslations();
        
    }
    
    @FXML
    private void wipeData() throws IOException {
        DBManager.wipeData();
    }
    
    @FXML
    private void showDatabase() throws IOException {
        List<Term> Terms = DBManager.selectTerms();
        List<Translation> Translations = DBManager.selectTranslations();
        int termCount = Terms.size();
        int translationCount = Translations.size();
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DataBaseViewer.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.setUserData(this);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }
    
    @FXML
    private void showAbout() throws IOException {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.setUserData(this);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }
    
    @FXML
    private void addWord() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddWordDialog.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.setUserData(this);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }
    
        public void addAWord(String term, String translations) throws IOException {
            
            List<String> translationList = Arrays.asList(translations.split(","));
            
            int index = DBManager.findTerm(term);
            if (index != -1)
            {
                // The term already exists in the dictionary
                DBManager.insertTranslation(index, translationList.get(0));
                
            } else {
                // The term is new
                index = DBManager.insertTerm(term, translationList.get(0));
            }
            
            
            for (int i = 1; i < translationList.size(); i++)
            {
                DBManager.insertTranslation(index, translationList.get(i));
            }
        }
    
    @FXML
    private void deleteWord() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeleteWordDialog.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.setUserData(this);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't load new window");
        }
    }
    
    public void deleteAWord(String term) throws IOException {
        boolean bDeleted = DBManager.deleteTerm(term);
        String text;
        if (bDeleted) {
            text = term + " deleted successfully!";
        } else {
            text = "Failed to delete " + term + "!";
        }
        System.out.println(text);
    }
    
    @FXML
    private void checkAnswers() throws IOException {
        
        boolean bCorrect = true;
        for (int i = 0; i < EntriesCount; i++)
        {
            boolean translationWasCorrect = false;
            for (int j = 0; j < Translations.size(); j++)
            {
                if (Translations.get(j).getTermID() == TranslationIndexes[i])
                {
                    String transl = Translations.get(j).getTranslation();
                    transl = transl.toLowerCase();
                    if (TranslationFields[i].isDisable())
                    {
                        translationWasCorrect = true;
                    } else if (transl.equals(TranslationFields[i].getText().toLowerCase()))
                    {
                        TranslationFields[i].setText("CORRECT!");
                        TranslationFields[i].setDisable(true);
                        translationWasCorrect = true;
                        TotalCorrect += 1;
                    } else {
                        translationWasCorrect = false;
                    }
                }
            }
            if (!translationWasCorrect)
            {
                bCorrect = false;
            }
            updateProgressBar();
        }
        
        if (bCorrect)
        {
            checkButton.setDisable(true);
            loadNextButton.setDisable(false);
            entriesNumSlider.setDisable(false);
        }
    }
    
    void updateProgressBar() {
        double progress = ((double)TotalCorrect / (double)Terms.size());
        progressBar.setProgress(progress);
    }
    
    @FXML
    private void loadNext() throws IOException {
        

        
        entriesNumSlider.setDisable(true);
        checkButton.setDisable(false);
        loadNextButton.setDisable(true);
        if (bInit)
        {
            bInit = false;
            loadNextButton.setText("Next...");
            if (bInitTables)
            {
                scene = checkButton.getScene();
                SetupLabels();
                SetupFields();
                bInitTables = false;
            }
        }
        PopulateTermsAndTranslations();
    }
    
    @FXML
    private void changeEntriesNumber() throws IOException {
        Double data = entriesNumSlider.getValue();
        int temp = data.intValue();
        EntriesCount = temp;
        entriesCountLabel.setText("Number of Entries: " + EntriesCount);
    }
    
    // Setups Term Labels
    private void SetupLabels()
    {
        for (int i = 0; i < TermLabels.length; i++)
        {
            String Name = "#Term" + (i + 1);
            TermLabels[i] = (Label)scene.lookup(Name);
        }
    }
    
    // Setups Translation Fields
    private void SetupFields()
    {
        for (int i = 0; i < TranslationFields.length; i++)
        {
            String Name = "#Translation" + (i + 1);
            TranslationFields[i] = (TextField)scene.lookup(Name);
        }
    }
    
    // Loads the next chunk of terms and buffers their indexes in TranslationIndexes
    private void PopulateTermsAndTranslations() throws IOException
    {
        if (bReset)
        {
            NextEntryIndex = 0;
            TotalCorrect = 0;
            updateProgressBar();
        }
        
        changeEntriesNumber();
        LastEntryIndex = NextEntryIndex;
        NextEntryIndex += EntriesCount;
        
        if (LastEntryIndex == Terms.size())
        {
            bReset = true;
        }
        if (NextEntryIndex > Terms.size())
        {
            NextEntryIndex = Terms.size();
        }

       // Enabling possibly disabled translation fields and disabling the unused ones
       for (int i = 0; i < 12; i++)
       {
           if (i < (NextEntryIndex - LastEntryIndex))
           {
           TranslationFields[i].setDisable(false);
           } else {
           TranslationFields[i].setDisable(true);
           }
           
           TranslationFields[i].setText("");
           TermLabels[i].setText("");
       }
        
        for (int i = LastEntryIndex; i < NextEntryIndex; i++)
        {
            int index = NextEntryIndex - (NextEntryIndex - i) - LastEntryIndex;
            TermLabels[index].setText(Terms.get(i).getTerm());
            
            // Preparing translation indexes
            TranslationIndexes[index] = Terms.get(i).getID();
        }
    }
    
    public List<Translation> getTranslations()
    {
        Translations = DBManager.selectTranslations();
        return Translations;
    }
    
    public List<Term> getTerms()
    {
        Terms = DBManager.selectTerms();
        return Terms;
    }
}
