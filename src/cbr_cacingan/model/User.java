/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;

/**
 *
 * @author Fauzy Wahyudi
 */
public class User {
    int id;
    String username;
    String password;
    String nama;
    String jk;
    String tglLahir;
    String alamat;
    String nohp;
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getNama(){
        return nama;
    }
    
    public String getJk(){
        return jk;
    }
     public String getTglLahir(){
        return tglLahir;
    }
      public String getAlamat(){
        return alamat;
    }
       public String getNohp(){
        return nohp;
    }
    
    public int getId(){
        return id;
    }
    
    public void setUsername(String username){
       this.username=username;
    }
    public void setPassword(String password){
       this.password=password;
    }
    public void setNama(String nama){
       this.nama=nama;
    }
    public void setJk(String jk){
       this.jk=jk;
    }
    public void setTglLahir(String tglLahir){
       this.tglLahir=tglLahir;
    }
    public void setAlamat(String alamat){
       this.alamat=alamat;
    }
    public void setNohp(String nohp){
       this.nohp=nohp;
    }
    
    
    public void setId(int id){
        this.id=id;
    }
    
    public String toString(){
        return nama;
    }
}
