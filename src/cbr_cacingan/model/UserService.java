/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Fauzy Wahyudi
 */
public class UserService {
    Connection con;
    PreparedStatement preparedStatement;
    User user;
    List<User> userList = new ArrayList<User>();
    
    public UserService(Connection connection){
        this.con=connection;
    }
    
    public List<User> getAllUser() throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_user");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            user = new User();
            user.setId(Integer.parseInt(rs.getString("id_user")));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setNama(rs.getString("nama"));
            user.setJk(rs.getString("jk"));
            user.setTglLahir(rs.getString("tgl_lahir"));
            user.setAlamat(rs.getString("alamat"));
            user.setNohp(rs.getString("nohp"));
            userList.add(user);
        }
        return userList;
    }
    
    public List<User> getDataUserby(String username, String password) throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_user WHERE username='"+username+"' AND password='"+password+"'");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            user = new User();
            user.setId(Integer.parseInt(rs.getString("id_user")));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setNama(rs.getString("nama"));
            user.setJk(rs.getString("jk"));
            user.setTglLahir(rs.getString("tgl_lahir"));
            user.setAlamat(rs.getString("alamat"));
            user.setNohp(rs.getString("nohp"));
            userList.add(user);
        }
        return userList;
    }
    
    public List<User> getDataUserbyId(String idUser) throws SQLException{
        preparedStatement = con.prepareStatement("SELECT * FROM tb_user WHERE id_user='"+idUser+"'");
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            user = new User();
            user.setId(Integer.parseInt(rs.getString("id_user")));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setNama(rs.getString("nama"));
            user.setJk(rs.getString("jk"));
            user.setTglLahir(rs.getString("tgl_lahir"));
            user.setAlamat(rs.getString("alamat"));
            user.setNohp(rs.getString("nohp"));
            userList.add(user);
        }
        return userList;
    }
    
    public int getUmur(String tglLahir)throws SQLException{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tgl = null;
        try {
            tgl = df.parse(tglLahir);
        } catch (ParseException ex) {
            System.out.println("Gagal parsing date");
        }
        Date tglNow = new Date();
        int umur = tglNow.getYear() - tgl.getYear();
        
        
        return umur;
    }
}
