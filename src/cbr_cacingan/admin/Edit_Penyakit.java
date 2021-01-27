package cbr_cacingan.admin;

import cbr_cacingan.admin.*;
import cbr_cacingan.admin.Menu_Admin;
import cbr_cacingan.model.Admin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Edit_Penyakit extends javax.swing.JFrame {

    Statement st;
    Connection con;
    ResultSet rs, rs2, rsCek;
    String sql = "";
    String sql2 = "";
    String newKodePenyakit = "";
//    String kodeTerakhir="";
    String sqlCek = "";
    Menu_Admin admin;
    int indexTabbed;
    String kodePenyakit;
    String idPenyakit;
    String namaPenyakit;
    String ketPenyakit;
    String solusiPenyakit;
    String pencegahan;
    public static String text;
    Admin user;

    public Edit_Penyakit(int index, String kode, Admin user) {
        initComponents();
        setTitle("Edit Penyakit");
        indexTabbed = index;
        kodePenyakit = text;
        koneksiDB();
        getData();
        this.user=user;
        setLocationRelativeTo(null);
    }

    public void koneksiDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/cbr_cacingan", "root", "");
            st = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Terhubung");
        }
    }

    public void getData() {
        koneksiDB();
        String _kode = kodePenyakit;
        try {
            rs = st.executeQuery("SELECT * FROM tb_penyakit WHERE kode_penyakit ='" + _kode + "'");
            while (rs.next()) {
                idPenyakit = rs.getString(1);
                namaPenyakit = rs.getString(3);
                ketPenyakit = rs.getString(4);
                pencegahan = rs.getString(5);
                solusiPenyakit = rs.getString(6);
                txtPenyakit.setText(namaPenyakit);
                txtKeterangan.setText(ketPenyakit);
                txtSolusi.setText(solusiPenyakit);
                txtKodePenyakit.setText(kodePenyakit);
                txtPencegahan.setText(pencegahan);
            }
        } catch (Exception e) {
        }

    }

    boolean cekKode(String kode, String id) {
        koneksiDB();
        boolean kodeSama = false;

        System.out.println("id : " + id + " kode : " + kode);
        try {
            sqlCek = "SELECT * FROM tb_penyakit WHERE kode_penyakit='" + kode + "'";
            rsCek = st.executeQuery(sqlCek);
            if (rsCek.next()) {
                String _id = rsCek.getString(1);
                String _kode = rsCek.getString(2);
                System.out.println("id : " + _id + " kode : " + _kode);
                if (!id.equals(_id) && kode.equals(_kode)) {
                    kodeSama = true;
                }
            }
        } catch (Exception e) {
        }

        return kodeSama;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPenyakit = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSolusi = new javax.swing.JTextArea();
        btnEdit = new javax.swing.JButton();
        btnTambah1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtKodePenyakit = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtPencegahan = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tambah Penyakit");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nama Penyakit");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Keterangan (optional)");

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane1.setViewportView(txtKeterangan);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Solusi");

        txtSolusi.setColumns(20);
        txtSolusi.setRows(5);
        jScrollPane2.setViewportView(txtSolusi);

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 102, 153));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/save.png"))); // NOI18N
        btnEdit.setText("Edit Penyakit");
        btnEdit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnTambah1.setBackground(new java.awt.Color(255, 255, 255));
        btnTambah1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTambah1.setForeground(new java.awt.Color(255, 102, 153));
        btnTambah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/arrow_back.png"))); // NOI18N
        btnTambah1.setText("Kembali");
        btnTambah1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnTambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambah1ActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kode Penyakit");

        txtPencegahan.setColumns(20);
        txtPencegahan.setRows(5);
        jScrollPane3.setViewportView(txtPencegahan);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pencegahan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(199, 199, 199)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtKodePenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(txtPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKodePenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        String id = String.valueOf(idPenyakit);
        String _kode = String.valueOf(txtKodePenyakit.getText());
        String _penyakit = String.valueOf(txtPenyakit.getText());
        String _keterangan = String.valueOf(txtKeterangan.getText());
        String _solusi = String.valueOf(txtSolusi.getText());
        String _pencegahan = String.valueOf(txtPencegahan.getText());
        boolean kodeSama = cekKode(_kode, id);

        if (kodeSama) {
             JOptionPane.showMessageDialog(null, "Kode Penyakit "+_kode+" sudah ada. Silahkan pilih kode lainnya!");
        } else {
            if (_penyakit.equals("") || _solusi.equals("") || _kode.equals("")) {
                JOptionPane.showMessageDialog(null, "Kode, Nama dan Solusi tidak boleh kosong!");
            } else {

                int ok = JOptionPane.showConfirmDialog(null, "Anda Yakin Simpan Data", "Konfirmasi", JOptionPane.YES_NO_OPTION);

                System.out.println(String.valueOf(ok));
                if (ok == 0) {
                    koneksiDB();
                    try {
                        java.sql.Statement stm = con.createStatement();
                        sql = "UPDATE tb_penyakit SET kode_penyakit='" + _kode + "',"
                                + "nama_penyakit='" + _penyakit + "',ket='" + _keterangan + "',"
                                + "pencegahan='" + _pencegahan + "',"
                                + "solusi='" + _solusi + "' WHERE id_penyakit='" + id + "'";
                        stm.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Edit Berhasil!");
                        new Menu_Admin(user.getUsername(),user.getPassword(),3).show();
                        dispose();

                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Gagal Edit");
                    }

                } else {
                    System.out.println("cancel");
                }

            }
        }



    }//GEN-LAST:event_btnEditActionPerformed

    private void btnTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambah1ActionPerformed
        try {
            new Menu_Admin(user.getUsername(),user.getPassword(),indexTabbed).show();
        } catch (SQLException ex) {
            Logger.getLogger(Edit_Penyakit.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_btnTambah1ActionPerformed

    void cleardata() {
        txtPenyakit.setText("");
        txtKeterangan.setText("");
        txtSolusi.setText("");
        txtPencegahan.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[], final Admin admin) {
        text = args[0];
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
            java.util.logging.Logger.getLogger(Tambah_Penyakit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tambah_Penyakit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tambah_Penyakit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tambah_Penyakit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_Penyakit(3, text,admin).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtKodePenyakit;
    private javax.swing.JTextArea txtPencegahan;
    private javax.swing.JTextField txtPenyakit;
    private javax.swing.JTextArea txtSolusi;
    // End of variables declaration//GEN-END:variables
}
