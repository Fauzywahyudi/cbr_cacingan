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
public class RuleService {
    Connection con;
    PreparedStatement preparedStatement;
    Rule rule;
    Gejala gejala;
    List<Rule> ruleList = new ArrayList<Rule>();
    List<Gejala> gejalaList = new ArrayList<Gejala>();
    
    public RuleService(Connection connection){
        this.con = connection;
    }
    
    public List<Rule> getAllRule() throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_rule a INNER JOIN tb_penyakit b ON a.kode_penyakit=b.kode_penyakit");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            rule = new Rule();
            rule.setId(Integer.parseInt(rs.getString("id_rule")));
            rule.setKodeRule(rs.getString("kode_rule"));
            rule.setKodePenyakit(rs.getString("kode_penyakit"));
            rule.setKodeGejala(rs.getString("kode_gejala"));
            rule.setNamaPenyakit(rs.getString("nama_penyakit"));
            ruleList.add(rule);
        }
        return ruleList;
    }
    
    public int getJumlahRule()throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_rule a INNER JOIN tb_penyakit b ON a.kode_penyakit=b.kode_penyakit");
        ResultSet rs = preparedStatement.executeQuery();
        int jumlah = 0;
        while(rs.next()){
            jumlah++;
        }
        return jumlah;
    }
    
    public int getSumBobotRule(String strKodeGejala)throws SQLException{
        String[] arrKode = strKodeGejala.split(",");
        String where="";
        int sum=0;
        int i = 0;
        for(String kode : arrKode){
            if(i==0){
                where = "kode_gejala='"+kode+"'";
            }else{
                where = where+" OR kode_gejala='"+kode+"'";
            }
            i++;
        }
        preparedStatement = con.prepareStatement("SELECT SUM(bobot)as sum FROM tb_gejala WHERE "+where+" ORDER BY kode_gejala ASC");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            sum=Integer.parseInt(rs.getString("sum"));
        }
        return sum;
    }
    
    public List<Gejala> getGejalaFromRule(String strKodeGejala)throws SQLException{
        String[] arrKode = strKodeGejala.split(",");
        String where="";
        int i = 0;
        for(String kode : arrKode){
            if(i==0){
                where = "kode_gejala='"+kode+"'";
            }else{
                where = where+" OR kode_gejala='"+kode+"'";
            }
            i++;
        }
        List<Gejala> list = new ArrayList<Gejala>();
        preparedStatement = con.prepareStatement("SELECT * FROM tb_gejala WHERE "+where+" ORDER BY kode_gejala ASC");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            gejala = new Gejala();
            gejala.setId(Integer.parseInt(rs.getString("id_gejala")));
            gejala.setKode(rs.getString("kode_gejala"));
            gejala.setGejala(rs.getString("nama_gejala"));
            gejala.setBobot(Integer.parseInt(rs.getString("bobot")));
            list.add(gejala);
        }
        return list;
    }
}
