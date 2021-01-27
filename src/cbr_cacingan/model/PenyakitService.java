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
public class PenyakitService {
    
    Connection con;
    PreparedStatement preparedStatement;
    Penyakit penyakit;
    List<Penyakit> penyakitList = new ArrayList<Penyakit>();
    
    public PenyakitService(Connection connection){
        this.con=connection;
    }
    
    public List<Penyakit> getAllPenyakit() throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_penyakit");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            penyakit = new Penyakit();
            penyakit.setId(Integer.parseInt(rs.getString("id_penyakit")));
            penyakit.setKode(rs.getString("kode_penyakit"));
            penyakit.setPenyakit(rs.getString("nama_penyakit"));
            penyakit.setKet(rs.getString("ket"));
            penyakit.setSolusi(rs.getString("solusi"));
            penyakit.setPencegahan(rs.getString("pencegahan"));
            penyakitList.add(penyakit);
        }
        return penyakitList;
    }
}
