/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;

public class Gejala {
    int id;
    String kode;
    String gejala;
    int bobot;
    
    public String getKode(){
        return kode;
    }
    
    public String getGejala(){
        return gejala;
    }
    
    public int getBobot(){
        return bobot;
    }
    
    public int getId(){
        return id;
    }
    
    public void setKode(String kode){
       this.kode=kode;
    }
    public void setGejala(String gejala){
       this.gejala=gejala;
    }
    public void setBobot(int bobot){
       this.bobot=bobot;
    }
    
    public void setId(int id){
        this.id=id;
    }
    
    public String toString(){
        return gejala;
    }
}
