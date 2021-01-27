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
public class KonsultasiService {
    Connection con;
    PreparedStatement preparedStatement;
    Konsultasi konsul;
    List<Konsultasi> konsulList = new ArrayList<Konsultasi>();
    
    public KonsultasiService(Connection connection){
        this.con=connection;
    }
    
    public List<Konsultasi> getAllKonsul() throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_konsultasi a INNER JOIN tb_penyakit b ON a.kode_penyakit=b.kode_penyakit");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            konsul = new Konsultasi();
            konsul.setId(Integer.parseInt(rs.getString("id_konsultasi")));
            konsul.setIdUser(Integer.parseInt(rs.getString("id_user")));
            konsul.setJawab(rs.getString("jawaban_user"));
            konsul.setKodePenyakit(rs.getString("kode_penyakit"));
            konsul.setHasil(Double.parseDouble(rs.getString("hasil")));
            konsul.setTglKonsul(rs.getString("tgl_konsul"));
            konsul.setNamaPenyakit(rs.getString("nama_penyakit"));
            konsul.setSolusi(rs.getString("solusi"));
            konsulList.add(konsul);
        }
        return konsulList;
    }
    
    public List<Konsultasi> getAllKonsulById(String idKonsul) throws SQLException{
        List<Konsultasi> list = new ArrayList<Konsultasi>();
        preparedStatement = con.prepareStatement("SELECT * FROM tb_konsultasi  a INNER JOIN "
                + "tb_penyakit b ON a.kode_penyakit=b.kode_penyakit WHERE a.id_konsultasi='"+idKonsul+"'");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            konsul = new Konsultasi();
            konsul.setId(Integer.parseInt(rs.getString("id_konsultasi")));
            konsul.setIdUser(Integer.parseInt(rs.getString("id_user")));
            konsul.setJawab(rs.getString("jawaban_user"));
            konsul.setKodePenyakit(rs.getString("kode_penyakit"));
            konsul.setHasil(Double.parseDouble(rs.getString("hasil")));
            konsul.setTglKonsul(rs.getString("tgl_konsul"));
            konsul.setNamaPenyakit(rs.getString("nama_penyakit"));
            konsul.setSolusi(rs.getString("solusi"));
            list.add(konsul);
        }
        return list;
    }
}
