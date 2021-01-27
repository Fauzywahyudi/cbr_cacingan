/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.admin;

import cbr_cacingan.model.Admin;
import cbr_cacingan.model.Gejala;
import cbr_cacingan.model.Konsultasi;
import cbr_cacingan.model.KonsultasiService;
import cbr_cacingan.model.RuleService;
import cbr_cacingan.model.User;
import cbr_cacingan.model.UserService;
import cbr_cacingan.user.Menu_User;
import cbr_cacingan.user.Solusi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Fauzy Wahyudi
 */
public class Lihat_Konsultasi extends javax.swing.JFrame {

    Statement st;
    Statement st2;
    Connection con;
    ResultSet rs, rs2;
    String sql = "";
    String sql2 = "";
    DefaultTableModel _tblGejalaJawaban;
    UserService userService;
    List<User> userList;
    RuleService ruleService;
    public static String idPars;
    String idKonsul;
    KonsultasiService konsultasiService;
    List<Konsultasi> konsultasiList;
    int idUser;
    String openHtml = "<HTML><P style=\"text-align: justify \">";
    String closeHtml = "</P></HTML>";
    Admin admin;

    public Lihat_Konsultasi(String idKonsul, Admin admin) throws SQLException, ParseException {
        initComponents();
        koneksiDB();
        this.admin = admin;
        this.idKonsul = idKonsul;
        konsultasiService = new KonsultasiService(con);
        ruleService = new RuleService(con);
        userService = new UserService(con);
        
        konsultasiList = konsultasiService.getAllKonsulById(idKonsul);
        idUser = konsultasiList.get(0).getIdUser();
        userList = userService.getDataUserbyId(String.valueOf(this.idUser));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            showGejala(idKonsul);
            showInfoUser();
            showInfoDiagnosa();

        } catch (SQLException ex) {
            Logger.getLogger(Lihat_Konsultasi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void koneksiDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/cbr_cacingan", "root", "");
            st = con.createStatement();
            st2 = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Terhubung");
        }
    }

    public void showGejala(String idKonsul) throws SQLException {
        koneksiDB();
        Object[] row = {"No", "Kode Gejala", "Nama Gejala", "Bobot"};
        _tblGejalaJawaban = new DefaultTableModel(null, row);
        tabelJawaban.setModel(_tblGejalaJawaban);
        tabelJawaban.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "";
        int kolom4 = 0;
        String strJawabanUser = "";
        try {
            koneksiDB();
//            rs = st.executeQuery("SELECT * FROM tb_konsultasi WHERE id_konsul='" + idKonsul + "' ORDER BY tgl_konsul DESC LIMIT 1");
//            while (rs.next()) {
//                strJawabanUser = rs.getString("jawaban_user");
//            }
            strJawabanUser = konsultasiList.get(0).getJawab();
            List<Gejala> listGejala = ruleService.getGejalaFromRule(strJawabanUser);
            int i = 1;
            for (Gejala gejala : listGejala) {
                kolom1 = String.valueOf(i);
                kolom2 = gejala.getKode();
                kolom3 = gejala.getGejala();
                kolom4 = gejala.getBobot();
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4};
                _tblGejalaJawaban.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal");
            tabelJawaban.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }

        TableColumn column;
        tabelJawaban.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column = tabelJawaban.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = tabelJawaban.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = tabelJawaban.getColumnModel().getColumn(2);
        column.setPreferredWidth(700);
        column = tabelJawaban.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);
    }

    public void showInfoUser() throws ParseException {
        User dataUser = userList.get(0);
        System.out.println("Show Info User");
        System.out.println(dataUser.getNama() + dataUser.getTglLahir());
        String JK = "";
        System.out.println(dataUser.getNama());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tglLahir = df.parse(dataUser.getTglLahir());
        System.out.println(dataUser.getTglLahir());
        Date tglNow = new Date();
        System.out.println(tglNow.getYear()+"=> "+ tglLahir.getYear());
        int umur = tglNow.getYear() - tglLahir.getYear();
        System.out.println("Umur : "+umur);
        if (umur == 0) {
            labUmur1.setText("kurang 1 tahun");
        } else {
            labUmur1.setText(umur + " tahun");
        }
        labNama1.setText(dataUser.getNama());
        if (dataUser.getJk().equals("L")) {
            JK = "Laki-laki";
        } else {
            JK = "Perempuan";
        }
        labJk1.setText(JK);
        labAlamat1.setText(userList.get(0).getAlamat());
        labNohp1.setText(userList.get(0).getNohp());
    }

    public void showInfoDiagnosa() throws SQLException {
        labPenyakit.setText(konsultasiList.get(0).getNamaPenyakit());
        double hasil = konsultasiList.get(0).getHasil();
        DecimalFormat decFor = new DecimalFormat("#.##");
        labHasil.setText(decFor.format(hasil) + " (" + (decFor.format(hasil * 100)) + "%)");
        labSolusi.setText(openHtml + konsultasiList.get(0).getSolusi() + closeHtml);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labNama1 = new javax.swing.JLabel();
        labJk1 = new javax.swing.JLabel();
        labUmur1 = new javax.swing.JLabel();
        labNohp1 = new javax.swing.JLabel();
        labAlamat1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelJawaban = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        labPenyakit = new javax.swing.JLabel();
        labHasil = new javax.swing.JLabel();
        labSolusi = new javax.swing.JLabel();
        btnLihatSolusi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));

        jPanel3.setBackground(new java.awt.Color(255, 102, 153));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Nama Lengkap");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Jenis Kelamin");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Umur");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("No. HP");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Alamat");

        labNama1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labNama1.setForeground(new java.awt.Color(255, 255, 255));
        labNama1.setText("nama");

        labJk1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labJk1.setForeground(new java.awt.Color(255, 255, 255));
        labJk1.setText("jk");

        labUmur1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labUmur1.setForeground(new java.awt.Color(255, 255, 255));
        labUmur1.setText("umur");

        labNohp1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labNohp1.setForeground(new java.awt.Color(255, 255, 255));
        labNohp1.setText("nohp");

        labAlamat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labAlamat1.setForeground(new java.awt.Color(255, 255, 255));
        labAlamat1.setText("alamat");
        labAlamat1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labAlamat1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labNohp1)
                            .addComponent(labUmur1)
                            .addComponent(labJk1)
                            .addComponent(labNama1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labNama1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labJk1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(labUmur1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labNohp1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(labAlamat1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Gejala dipilih");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hasil Konsultasi");

        tabelJawaban.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelJawaban);

        jPanel4.setBackground(new java.awt.Color(255, 102, 153));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Hasil Diagnosa : ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Penyakit");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Hasil");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Solusi");

        labPenyakit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labPenyakit.setForeground(new java.awt.Color(255, 255, 255));
        labPenyakit.setText("Penyakit");
        labPenyakit.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        labHasil.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labHasil.setForeground(new java.awt.Color(255, 255, 255));
        labHasil.setText("hasil");
        labHasil.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        labSolusi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labSolusi.setForeground(new java.awt.Color(255, 255, 255));
        labSolusi.setText("solusi");
        labSolusi.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSolusi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labHasil)
                            .addComponent(labPenyakit))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(labPenyakit))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(labHasil))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labSolusi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnLihatSolusi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatSolusi.setText("Tabel Solusi");
        btnLihatSolusi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLihatSolusi, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(btnLihatSolusi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLihatSolusiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusiActionPerformed
        try {
            String[] info = new String[2];
            info[0] = konsultasiList.get(0).getKodePenyakit();
            info[1] = konsultasiList.get(0).getNamaPenyakit();
            Solusi.main(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }
    }//GEN-LAST:event_btnLihatSolusiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[],final Admin admin) {
        idPars = args[0];
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lihat_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lihat_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lihat_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lihat_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Lihat_Konsultasi(idPars,admin).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Lihat_Konsultasi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Lihat_Konsultasi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLihatSolusi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labAlamat1;
    private javax.swing.JLabel labHasil;
    private javax.swing.JLabel labJk1;
    private javax.swing.JLabel labNama1;
    private javax.swing.JLabel labNohp1;
    private javax.swing.JLabel labPenyakit;
    private javax.swing.JLabel labSolusi;
    private javax.swing.JLabel labUmur1;
    private javax.swing.JTable tabelJawaban;
    // End of variables declaration//GEN-END:variables
}
