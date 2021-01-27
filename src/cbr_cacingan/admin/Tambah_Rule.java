/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.admin;

import cbr_cacingan.model.Admin;
import cbr_cacingan.model.Gejala;
import cbr_cacingan.model.GejalaService;
import cbr_cacingan.model.Penyakit;
import cbr_cacingan.model.PenyakitService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Tambah_Rule extends javax.swing.JFrame {

    Statement st, st2;
    Connection con;
    ResultSet rs, rs2;
    String sql = "";
    String sql2 = "";
    PenyakitService penyakitService;
    GejalaService gejalaService;
    DefaultTableModel _tblDaftarRule;
    String _selectedPenyakit;
    int indexTabbed;
    Admin user;

    public Tambah_Rule(int index, Admin user, int cbIndex) throws SQLException {
        initComponents();
        koneksiDB();
        indexTabbed = index;
        penyakitService = new PenyakitService(con);
        gejalaService = new GejalaService(con);
        getSakit();
        getGejala();
        cbPenyakit.setSelectedIndex(cbIndex);
        cbPenyakit.addActionListener(new ComboBoxListenerPenyakit());
        cbGejala.addActionListener(new ComboBoxListenerGejala());
        this.user=user;

        Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
        tampilkanDataTabelRule(penyakit.getKode());
        setLocationRelativeTo(null);
        
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

    public void getSakit() throws SQLException {
        cbPenyakit.removeAllItems();
        try {
            koneksiDB();
            List<Penyakit> penyakitList = penyakitService.getAllPenyakit();
            for (Penyakit pnykt : penyakitList) {
                cbPenyakit.addItem(pnykt);
            }

        } catch (Exception e) {
        }
    }

    public void getGejala() throws SQLException {
        cbGejala.removeAllItems();
        try {
            koneksiDB();
            List<Gejala> gejalaList = gejalaService.getAllGejala();
            for (Gejala gjl : gejalaList) {
                cbGejala.addItem(gjl);
            }

        } catch (Exception e) {
        }
    }

    public String tampilkanDataTabelRule(String kodePenyakit) {
        String kodeAll = null;
        String[] kodeArr;
        Object[] row = {"No", "Kode Gejala", "Nama Gejala", "Bobot"};
        _tblDaftarRule = new DefaultTableModel(null, row);
        tabelAddRule.setModel(_tblDaftarRule);
        tabelAddRule.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "";

        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_rule WHERE kode_penyakit='" + kodePenyakit + "'");
            while (rs.next()) {
                kodeAll = rs.getString("kode_gejala");
            }

            kodeArr = kodeAll.split(",");
            Arrays.sort(kodeArr);
//            System.out.println(kodeAll.toString());
            int i = 1;
            for (String text : kodeArr) {
                rs = st.executeQuery("SELECT * FROM tb_gejala WHERE kode_gejala='" + text + "'");

                while (rs.next()) {
                    kolom1 = String.valueOf(i);
                    kolom2 = rs.getString("kode_gejala");
                    kolom3 = rs.getString("nama_gejala");
                    kolom4 = rs.getString("bobot");
                    Object[] tampil = {kolom1, kolom2, kolom3, kolom4};
                    _tblDaftarRule.addRow(tampil);

                }
                i++;
            }

            TableColumn column;
            tabelAddRule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            column = tabelAddRule.getColumnModel().getColumn(0);
            column.setPreferredWidth(50);
            column = tabelAddRule.getColumnModel().getColumn(1);
            column.setPreferredWidth(100);
            column = tabelAddRule.getColumnModel().getColumn(2);
            column.setPreferredWidth(450);
            column = tabelAddRule.getColumnModel().getColumn(3);
            column.setPreferredWidth(100);
        } catch (Exception e) {
        }
        return kodeAll;
    }

    private class ComboBoxListenerPenyakit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
            labelRule.setText("Rule " + penyakit.getPenyakit());
            try {
                String kodeAll = tampilkanDataTabelRule(penyakit.getKode());
                String ruleAll = kodeAll.replaceAll(",", " AND ");
                labelRulePenyakit.setText("<HTML><P style=\"text-align: justify \">IF " + ruleAll + " THEN " + penyakit.getPenyakit() + " (" + penyakit.getKode() + ")</P></HTML>");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gejala pada penyakit ini masih kosong. \nSilahkan tambahkan!", "Informasi", 1);
                tabelAddRule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
                labelRulePenyakit.setText("-");
            }

        }
    }

    private class ComboBoxListenerGejala implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
//            Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
//            labelRule.setText(penyakit.getPenyakit());
        }
    }

    public boolean cekAdaGejala(String kodePenyakit, String kodeGejala) {
        koneksiDB();
        boolean cek = false;
        try {
            rs2 = st2.executeQuery("SELECT * FROM tb_rule WHERE kode_penyakit='" + kodePenyakit + "' AND kode_gejala LIKE '%" + kodeGejala + "%'");
            if (rs2.next()) {
                cek = true;
            } else {
                cek = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tambah_Rule.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cek;
    }

    public String getLastKodeRule() {
        String lastKode = null;
        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_rule ORDER BY kode_rule DESC LIMIT 1");
            while (rs.next()) {
                lastKode = rs.getString("kode_rule");
            }
        } catch (Exception e) {
            lastKode = "R00";
        }
        return lastKode;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbPenyakit = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cbGejala = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelAddRule = new javax.swing.JTable();
        labelRule = new javax.swing.JLabel();
        btnAddRule = new javax.swing.JButton();
        btnHapusListRule = new javax.swing.JButton();
        labelRulePenyakit = new javax.swing.JLabel();
        btnTambah1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pilih Penyakit");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tambah Rule");

        cbPenyakit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Gejala");

        cbGejala.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Daftar Rule");

        tabelAddRule.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabelAddRule);

        labelRule.setBackground(new java.awt.Color(255, 255, 255));
        labelRule.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelRule.setForeground(new java.awt.Color(255, 255, 255));
        labelRule.setText("Rule");

        btnAddRule.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAddRule.setForeground(new java.awt.Color(255, 102, 153));
        btnAddRule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/addlist.png"))); // NOI18N
        btnAddRule.setText("Tambah");
        btnAddRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRuleActionPerformed(evt);
            }
        });

        btnHapusListRule.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHapusListRule.setForeground(new java.awt.Color(255, 102, 153));
        btnHapusListRule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/delete.png"))); // NOI18N
        btnHapusListRule.setText("Hapus");
        btnHapusListRule.setPreferredSize(new java.awt.Dimension(85, 29));
        btnHapusListRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusListRuleActionPerformed(evt);
            }
        });

        labelRulePenyakit.setBackground(new java.awt.Color(255, 255, 255));
        labelRulePenyakit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelRulePenyakit.setForeground(new java.awt.Color(255, 255, 255));
        labelRulePenyakit.setText("-");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labelRule)
                        .addGap(734, 734, 734))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelRulePenyakit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(94, 94, 94))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel5)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(56, 56, 56)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAddRule)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cbGejala, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnHapusListRule, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(btnTambah1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbGejala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnAddRule)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnHapusListRule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelRule)
                .addGap(18, 18, 18)
                .addComponent(labelRulePenyakit)
                .addGap(42, 42, 42)
                .addComponent(btnTambah1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 687, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRuleActionPerformed
        Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
        Gejala gejala = (Gejala) cbGejala.getSelectedItem();
        String kodePenyakit = penyakit.getKode();
        String namaPenyakit = penyakit.getPenyakit();
        String kodeGejala = gejala.getKode();
        String namaGejala = gejala.getGejala();
        String strRule = tampilkanDataTabelRule(kodePenyakit);

        boolean adaGejala;
        if (strRule == null || strRule.equals("")) {
            strRule = ""+kodeGejala;
        } else {
            strRule = strRule+","+kodeGejala;
            String[] arrRule = strRule.split(",");
            Arrays.sort(arrRule);
            int i = 0;
            for(String txt : arrRule){
                if(i==0){
                    strRule = ""+txt;
                }else{
                    strRule = strRule+","+txt;
                }
                i++;
            }
            System.out.println("setelah di sort"+strRule);
        }
        System.out.println(strRule);
        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_rule WHERE kode_penyakit='" + kodePenyakit + "'");
            if (rs.next()) {
                System.out.println("Ada");
                System.out.println(kodePenyakit + kodeGejala);
                adaGejala = cekAdaGejala(kodePenyakit, kodeGejala);
                if (adaGejala) {
                    System.out.println("Gejala Ada");
                    JOptionPane.showMessageDialog(null, "Penambahan Gagal! \n"
                            + "Gejala " + namaGejala + " sudah ditambahkan \n"
                            + "pada Penyakit " + namaPenyakit, "Error", 1);

                } else {
                    System.out.println("Tidak Ada Gejala");
                    java.sql.Statement stm = con.createStatement();
                    System.out.println(strRule);
                    sql = "UPDATE tb_rule SET kode_gejala='" + strRule + "'"
                            + "WHERE kode_penyakit='" + kodePenyakit + "'";
                    stm.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Penambahan Berhasil!");
                }

                tampilkanDataTabelRule(kodePenyakit);

            } else {
                System.out.println("Tidak ada");
                String lastKode = getLastKodeRule();
                if (lastKode == null) {
                    lastKode = "R00";
                }
                System.out.println(lastKode);
                int angka = Integer.parseInt(lastKode.substring(1));
                String newKode;
                if (angka < 9) {
                    newKode = String.valueOf("R0" + (angka + 1));
                } else {
                    newKode = String.valueOf("R" + (angka + 1));
                }
                System.out.println(String.valueOf(angka));
                System.out.println(newKode);
                sql = "INSERT INTO tb_rule VALUES (NULL,'" + newKode + "','" + kodePenyakit + "','" + kodeGejala + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Penambahan Berhasil!");
                tampilkanDataTabelRule(kodePenyakit);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }


    }//GEN-LAST:event_btnAddRuleActionPerformed

    private void btnHapusListRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusListRuleActionPerformed
       
        
        try {
                String kodeGejala = _tblDaftarRule.getValueAt(tabelAddRule.getSelectedRow(), 1).toString();
                String namaGejala = _tblDaftarRule.getValueAt(tabelAddRule.getSelectedRow(), 2).toString();
                Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
                String kodePenyakit = penyakit.getKode();
                String namaPenyakit = penyakit.getPenyakit();
            int reply = JOptionPane.showConfirmDialog(this,
                    "Apakah yakin menghapus penyakit " + _tblDaftarRule.getValueAt(tabelAddRule.getSelectedRow(), 2) + "?",
                    "Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                
                System.out.println(kodePenyakit);
                try {
                    koneksiDB();
                    rs = st.executeQuery("SELECT * FROM tb_rule WHERE kode_penyakit='"+kodePenyakit+"'");
                    String ruleKode=null;
                    while(rs.next()){
                        ruleKode = rs.getString("kode_gejala");
                    }
                    System.out.println(ruleKode);
                    String[] arrRuleKode = ruleKode.split(",");
                    ArrayList objArray = new ArrayList();
                    int i = 0;
                    for(String txt : arrRuleKode){
                        objArray.add(i, txt);
                        i++;
                    }
                    
                    System.out.println("Sebelum dihapus "+objArray);
                    objArray.remove(kodeGejala);
                    System.out.println("Setelah dihapus "+objArray);
                    int j = 0;
                    String newStrKode="";
                    for(Object kode : objArray){
                        System.out.println(kode);
                        if(j==0){
                            newStrKode = newStrKode+kode;
                        }else{
                            newStrKode = newStrKode+","+kode;
                        }
                        j++;
                    }
                    
                    System.out.println("Susunan terbaru kode gajala "+newStrKode);
                    
                    java.sql.Statement stm = con.createStatement();
                    stm.executeUpdate("UPDATE tb_rule SET kode_gejala='"+newStrKode+"' WHERE kode_penyakit='" + kodePenyakit + "'");
                    JOptionPane.showMessageDialog(null, "Gejala "+namaGejala+" sudah dihapus dari Penyakit"+namaPenyakit, "", 1);
                } catch (Exception e) {
                    System.err.println("Gagal Hapus");
                }
            }

            tampilkanDataTabelRule(kodePenyakit);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }
    }//GEN-LAST:event_btnHapusListRuleActionPerformed

    private void btnTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambah1ActionPerformed
        try {
            new Menu_Admin(user.getUsername(),user.getPassword(),indexTabbed).show();
        } catch (SQLException ex) {
            Logger.getLogger(Tambah_Rule.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_btnTambah1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(int index, final Admin admin, final int cbIndex) {
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
            java.util.logging.Logger.getLogger(Tambah_Rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tambah_Rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tambah_Rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tambah_Rule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Tambah_Rule(4,admin,cbIndex).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Tambah_Rule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRule;
    private javax.swing.JButton btnHapusListRule;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JComboBox cbGejala;
    private javax.swing.JComboBox cbPenyakit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelRule;
    private javax.swing.JLabel labelRulePenyakit;
    private javax.swing.JTable tabelAddRule;
    // End of variables declaration//GEN-END:variables
}
