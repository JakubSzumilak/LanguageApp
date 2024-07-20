/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataBasePackage;

/**
 *
 * @author s39910
 */
public class Translation {
    private int id;
    private int termID;
    private String translation;
    
    public final int getID() { return id; };
    public final int getTermID() { return termID; };
    public final String getTranslation() { return translation; };
    
    public Translation(int id, int termID, String translation)
    {
        this.id = id;
        this.termID = termID;
        this.translation = translation;
    }
}