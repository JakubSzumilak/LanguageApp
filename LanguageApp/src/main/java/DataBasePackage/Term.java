/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataBasePackage;

/**
 *
 * @author s39910
 */
public class Term {
    private int id;
    private String term;
    
    public final int getID() { return id; };
    public final String getTerm() { return term; };
    
    public Term(int id, String term)
    {
        this.id = id;
        this.term = term;
    }
}
