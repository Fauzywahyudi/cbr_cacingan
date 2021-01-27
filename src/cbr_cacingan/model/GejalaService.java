/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fauzy Wahyudi
 */
public class GejalaService {
    
    Connection con;
    PreparedStatement preparedStatement;
    Gejala gejala;
    List<Gejala> gejalaList = new ArrayList<Gejala>();
    
    public GejalaService(Connection connection){
        this.con=connection;
    }
    
    public List<Gejala> getAllGejala() throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_gejala");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            gejala = new Gejala();
            gejala.setId(Integer.parseInt(rs.getString("id_gejala")));
            gejala.setKode(rs.getString("kode_gejala"));
            gejala.setGejala(rs.getString("nama_gejala"));
            gejala.setBobot(Integer.parseInt(rs.getString("bobot")));
            gejalaList.add(gejala);
        }
        return gejalaList;
    }
    
    public int getCountGejala() throws SQLException{
        int jumlah = 0;
        preparedStatement = con.prepareStatement("SELECT COUNT(kode_gejala) as jumlah FROM tb_gejala");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            jumlah = Integer.parseInt(rs.getString("jumlah"));
        }
        return jumlah;
    }
}
