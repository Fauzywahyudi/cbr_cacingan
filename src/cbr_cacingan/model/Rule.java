/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;

/**
 *
 * @author Fauzy Wahyudi
 */
public class Rule {
    int id;
    String kodeRule;
    String kodePenyakit;
    String kodeGejala;
    String namaPenyakit;
    
    public String getKodeRule(){
        return kodeRule;
    }
    
    public String getKodePenyakit(){
        return kodePenyakit;
    }
    
    public String getKodeGejala(){
        return kodeGejala;
    }
    
    public String getNamaPenyakit(){
        return namaPenyakit;
    }
    
    public int getId(){
        return id;
    }
    
    public void setKodeRule(String kodeRule){
       this.kodeRule=kodeRule;
    }
    
    public void setKodePenyakit(String kodePenyakit){
       this.kodePenyakit=kodePenyakit;
    }
    
    public void setKodeGejala(String kodeGejala){
       this.kodeGejala=kodeGejala;
    }
    
    public void setNamaPenyakit(String namaPenyakit){
        this.namaPenyakit=namaPenyakit;
    }
    
    public void setId(int id){
        this.id=id;
    }
    
    public String toString(){
        return namaPenyakit;
    }
}
