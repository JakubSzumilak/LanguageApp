/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataBasePackage;

/**
 *
 * @author s39910
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Use public int insertTerm(String term, String translation) to add new terms

// Use public boolean insertTranslation(int termID, String translation) 
// to add additional translations. 
// termID can be obtained from the insertTerm method

public class DataBaseManager {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:dictionary.db";
    private Connection conn;
    private Statement stat;
    
    private int nextTermIndex;
    
    public DataBaseManager() {
        try {
            Class.forName(DataBaseManager.DRIVER);
        } catch (ClassNotFoundException ex) {
            System.err.println("Brak sterownika JDBC");
            ex.printStackTrace();
        }
        
        
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException ex) {
            System.err.println("Problem z otwarciem połączenia");
            ex.printStackTrace();
        }
        createTables();
        nextTermIndex = getLastTermIndex() + 1;
    }
    
    public boolean createTables() {
        String createTerms = "CREATE TABLE IF NOT EXISTS "
                + "terms(term_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "term VARCHAR(255) NOT NULL)";
        
        String createTranslations = "CREATE TABLE IF NOT EXISTS "
                + "translations(translation_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "translation VARCHAR(255) NOT NULL, "
                + "term_id INT NOT NULL, "
                + "CONSTRAINT fk_terms FOREIGN KEY (term_id) REFERENCES terms(term_id))";
        
        try {
        stat.execute(createTerms);
        stat.execute(createTranslations);
        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Deprecated, I suggest you not touch it
    public boolean insertTerm(int id, String term)
    {
        // Later on, check if a term already exists
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
            "INSERT INTO terms VALUES (?, ?);");
            prepStmt.setInt(1, id);
            prepStmt.setString(2, term);
            prepStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Błąd przy wstawianiu pojęcia");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    protected boolean insertTerm(String term)
    {
        // Later on, check if a term already exists
        PreparedStatement prepStmt;
        try {
            prepStmt = conn.prepareStatement(
            "INSERT INTO terms VALUES (?, ?);");
            prepStmt.setInt(1, nextTermIndex);
            prepStmt.setString(2, term);
            prepStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Błąd przy wstawianiu pojęcia");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Use this method to add multiple translations to one term.
    // You can get the termID from the insertTerm method
    public boolean insertTranslation(int termID, String translation)
    {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
            "INSERT INTO translations VALUES (NULL, ?, ?);");
            prepStmt.setString(1, translation);
            prepStmt.setInt(2, termID);
            prepStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Błąd przy wprowadzaniu tłumaczenia");
            return false;
        }
        return true;
    }
    
    protected boolean insertTranslation(String translation)
    {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
            "INSERT INTO translations VALUES (NULL, ?, ?);");
            prepStmt.setString(1, translation);
            prepStmt.setInt(2, nextTermIndex);
            prepStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Błąd przy wprowadzaniu tłumaczenia");
            return false;
        }
        return true;
    }
    
    // The right method to be called when adding new terms
    public int insertTerm(String term, String translation)
    {
        insertTerm(term);
        insertTranslation(translation);
        nextTermIndex++;
        return nextTermIndex - 1;
    }
    
    public List<Term> selectTerms() {
        List<Term> Terms = new LinkedList<Term>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM terms");
            int id;
            String term;
            while(result.next()) {
                id = result.getInt("term_id");
                term = result.getString("term");
                Terms.add(new Term(id, term));
            }   
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return Terms;
    }
    
    public List<Translation> selectTranslations() {
        List<Translation> Translations = new LinkedList<Translation>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM translations");
            int id, term_id;
            String translation;
            while(result.next()) {
                id = result.getInt("translation_id");
                term_id = result.getInt("term_id");
                translation = result.getString("translation");
                Translations.add(new Translation(id, term_id, translation));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return Translations;
    }
    
    public int getTermsCount()
    {
        int size = 0;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM terms");
            while (result.next())
            {
                size++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return size;
    }
    
    public int getLastTermIndex()
    {
        int index = -1;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM terms");
            while(result.next()) {
                int id = result.getInt("term_id");
                if (id > index)
                {
                    index = id;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return index;
    }
    
    public int getTranslationsCount()
    {
        int size = 0;
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM translations");
            while (result.next())
            {
                size++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return size;
    }
    
    public void wipeData()
    {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("DELETE FROM translations");
            prepStmt.execute();
            prepStmt = conn.prepareStatement("DELETE FROM terms");
            prepStmt.execute();
            prepStmt = conn.prepareStatement("DELETE FROM sqlite_sequence");
            prepStmt.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Failed to wipe data");
        }
    }
    
    private void removeTerm(int index)
    {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
            "DELETE FROM translations WHERE term_id == " + index);
            prepStmt.execute();
            prepStmt = conn.prepareStatement(
            "DELETE FROM terms WHERE term_id == " + index);
            prepStmt.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Failed to delete term with index of " + index);
        }
    }
    
    // Returns the ID of the term, if it's present, if not, returns -1
    public int findTerm(String Term) {
        try {
            ResultSet result = stat.executeQuery
            ("SELECT terms.term_id FROM terms WHERE terms.term = '" + Term + "'");
            if(result.next()) {
                return result.getInt("term_id");
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }
    
    public boolean deleteTerm(String term) {
        int index = findTerm(term);
        if (index == -1) return false;
        removeTerm(index);
        return true;
    }
    
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Problem z zamknięciem połączenia");
            ex.printStackTrace();
        }
    }
}
