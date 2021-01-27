/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;


public class Konsultasi {
    
    int id;
    int idUser;
    String jawab;
    String kodePenyakit;
    double hasil;
    String tglKonsul;
    String namaPenyakit;
    String solusi;
    
    public int id(){
        return id;
    }
    
    public int getIdUser(){
        return idUser;
    }
    
    public String getJawab(){
        return jawab;
    }
    
    public String getKodePenyakit(){
        return kodePenyakit;
    }
    
    public Double getHasil(){
        return hasil;
    }
    
    public String getTglKonsul(){
        return tglKonsul;
    }
    
    public String getNamaPenyakit(){
        return namaPenyakit;
    }
    
    public String getSolusi(){
        return solusi;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setIdUser(int idUser){
        this.idUser = idUser;
    }
    
    public void setJawab(String jawab){
        this.jawab= jawab;
    }
    
    public void setKodePenyakit(String kodePenyakit){
        this.kodePenyakit = kodePenyakit;
    }
    
    public void setHasil(double hasil){
        this.hasil = hasil;
    }
    
    public void setTglKonsul(String tglKonsul){
        this.tglKonsul = tglKonsul;
    }
    
    public void setNamaPenyakit(String namaPenyakit){
        this.namaPenyakit = namaPenyakit;
    }
    
    public void setSolusi(String solusi){
        this.solusi = solusi;
    }
    
}
