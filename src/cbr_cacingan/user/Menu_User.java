/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.user;

import cbr_cacingan.Login;
import cbr_cacingan.model.Penyakit;
import cbr_cacingan.model.PenyakitService;
import cbr_cacingan.model.User;
import cbr_cacingan.model.UserService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Fauzy Wahyudi
 */
public class Menu_User extends javax.swing.JFrame {

    Statement st;
    Connection con;
    ResultSet rs, rs2;
    String sql = "";
    String sql2 = "";
    public static String textUsername;
    public static String textPassword;
    public static int txtIndex;
    User user;
    DefaultTableModel _tblDaftarGejala;
    UserService userService;
    List<User> userList;
    String openHtml = "<HTML>";
    String closeHtml = "</HTML>";
    String openParag = "<P style=\"text-align: justify \">";
    String closeParag = "</P>";
    PenyakitService penyakitService;

    public Menu_User(String username, String password, int index) throws SQLException {
        initComponents();
        setTitle("User");
        koneksiDB();
        tabbedPan.setSelectedIndex(index);
        penyakitService = new PenyakitService(con);
        getSakit();
        cbPenyakit.addActionListener(new ComboBoxListenerPenyakit());
        System.out.println(textUsername + " " + textPassword);
        userService = new UserService(con);
        userList = userService.getDataUserby(username, password);
        labPetunjuk.setText(openHtml + openParag + "1. Tekan tombol \"Mulai Konsultasi\" untuk memulai Konsultasi dengan Sistem.\n" + closeParag
                + openParag + "2. Jawablah pertanyaan yang ditampilkan sistem berdasarkan kondisi tubuh anda.\n" + closeParag
                + openParag + "3. Pilih tombol \"Iya\" jika anda merasakan gejala tersebut.\n" + closeParag
                + openParag + "4. Pilih tombol \"Tidak\" jika anda tidak merasakan gejala tersebut." + closeParag + closeHtml);

        Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
        labKet.setText(openHtml + openParag + penyakit.getKet() + closeParag + closeHtml);
        tampilkanDataTabelRule(penyakit.getKode());
        setProfil();
        setHome();
        setLocationRelativeTo(null);
    }
    
    public void setHome(){
//        labMalaria.setText(openHtml+openParag+"<b>Malaria </b>adalah penyakit yang disebabkan oleh <b>infeksi "
//                + "parasit plasmodium</b>. Parasit ini masuk ke dalam tubuh melalui<b> gigitan nyamuk Anopheles</b>, "
//                + "tepatnya <b>anopheles betina</b>. Setelah berhasil masuk, parasit lantas tumbuh dan berkembang di dalam hati (liver)."
//                + "<b>Malaria</b> adalah penyakit yang tidak dapat ditularkan "
//                + "secara langsung. Penyakit ini hanya dapat tertular apabila seseorang <b>tergigit oleh "
//                + "nyamuk</b> yang sebelumnya menghisap darah orang yang menderita malaria.<b> Transfusi darah </b>"
//                + "juga jadi cara penyebaran parasit malaria. Sedangkan janin dari ibu yang menderita "
//                + "malaria juga tak luput risiko terkena malaria (malaria kongenital)."+closeParag+closeHtml);
        labMalaria.setText("");
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
    
    public void setProfil(){
        User user = userList.get(0);
        String jk = "";
        if(user.getJk().equals("L")){
            jk="Laki-laki";
        }else{
            jk="Perempuan";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tglLahir = null;
        try {
            tglLahir = df.parse(user.getTglLahir());
        } catch (ParseException ex) {
            Logger.getLogger(Menu_User.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date tglNow = new Date();
        int umur = tglNow.getYear() - tglLahir.getYear();
        if(umur==0){
            labelUmur.setText("kurang 1 tahun");
        }else{
            labelUmur.setText(umur + " tahun");
        }
        labelNama.setText(user.getNama());
        labelJK.setText(jk);
        labelTglLahir.setText(user.getTglLahir());
        labelNohp.setText(user.getNohp());
        labelAlamat.setText(user.getAlamat());
        
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
            System.err.println(e);
        }
    }
    
    public String tampilkanDataTabelRule(String kodePenyakit) {
        String kodeAll = null;
        String[] kodeArr;
        Object[] row = {"No", "Kode Gejala", "Nama Gejala", "Bobot"};
        _tblDaftarGejala = new DefaultTableModel(null, row);
        tabelGejalaPenyakit.setModel(_tblDaftarGejala);
        tabelGejalaPenyakit.setBorder(null);
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
                    _tblDaftarGejala.addRow(tampil);

                }
                i++;
            }

            TableColumn column;
            tabelGejalaPenyakit.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            column = tabelGejalaPenyakit.getColumnModel().getColumn(0);
            column.setPreferredWidth(50);
            column = tabelGejalaPenyakit.getColumnModel().getColumn(1);
            column.setPreferredWidth(100);
            column = tabelGejalaPenyakit.getColumnModel().getColumn(2);
            column.setPreferredWidth(550);
            column = tabelGejalaPenyakit.getColumnModel().getColumn(3);
            column.setPreferredWidth(100);
        } catch (Exception e) {
        }
        return kodeAll;
    }

    private class ComboBoxListenerPenyakit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
            
            labKet.setText(openHtml + openParag + penyakit.getKet() + closeParag + closeHtml);
            try {
                tampilkanDataTabelRule(penyakit.getKode());
            } catch (Exception e) {
                
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPan = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        labMalaria = new javax.swing.JLabel();
        btnLihatSolusi1 = new javax.swing.JButton();
        btnLihatSolusi2 = new javax.swing.JButton();
        btnLihatSolusi3 = new javax.swing.JButton();
        btnLihatSolusi4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbPenyakit = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        labKet = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelGejalaPenyakit = new javax.swing.JTable();
        btnLihatSolusi = new javax.swing.JButton();
        btnLihatSolusi5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        labPetunjuk = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        labelNama = new javax.swing.JLabel();
        labelJK = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        labelTglLahir = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labelUmur = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        labelNohp = new javax.swing.JLabel();
        labelAlamat = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabbedPan.setForeground(new java.awt.Color(255, 102, 153));

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Home");

        labMalaria.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labMalaria.setForeground(new java.awt.Color(255, 255, 255));
        labMalaria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/assets/cacingan.jpg"))); // NOI18N
        labMalaria.setText("-");

        btnLihatSolusi1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi1.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatSolusi1.setText("Pelajari Tentang Penyakit");
        btnLihatSolusi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi1ActionPerformed(evt);
            }
        });

        btnLihatSolusi2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi2.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/arrow_forward.png"))); // NOI18N
        btnLihatSolusi2.setText("Langsung Konsultasi");
        btnLihatSolusi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi2ActionPerformed(evt);
            }
        });

        btnLihatSolusi3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi3.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnLihatSolusi3.setText("Perbarui Informasi");
        btnLihatSolusi3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi3ActionPerformed(evt);
            }
        });

        btnLihatSolusi4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi4.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/logout.png"))); // NOI18N
        btnLihatSolusi4.setText("Logout");
        btnLihatSolusi4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi4ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Diagnosa Penyakit Cacingan Metode CBR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labMalaria, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLihatSolusi1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLihatSolusi3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLihatSolusi2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLihatSolusi4, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(107, 107, 107))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(labMalaria, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
        );

        tabbedPan.addTab("Home", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 102, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Informasi Penyakit");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Penyakit  :");

        cbPenyakit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Keterangan :");

        labKet.setBackground(new java.awt.Color(255, 255, 255));
        labKet.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labKet.setForeground(new java.awt.Color(255, 255, 255));
        labKet.setText("-");
        labKet.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        tabelGejalaPenyakit.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabelGejalaPenyakit);

        btnLihatSolusi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatSolusi.setText("Lihat Solusi");
        btnLihatSolusi.setToolTipText("");
        btnLihatSolusi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusiActionPerformed(evt);
            }
        });

        btnLihatSolusi5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi5.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatSolusi5.setText("Lihat Pencegahan");
        btnLihatSolusi5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnLihatSolusi, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLihatSolusi5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(429, 450, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labKet, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(labKet, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPan.addTab("Informasi Penyakit", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 102, 153));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 102, 153));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/arrow_forward.png"))); // NOI18N
        jButton2.setText("Mulai Konsultasi");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Petunjuk Konsultasi");

        labPetunjuk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labPetunjuk.setForeground(new java.awt.Color(255, 255, 255));
        labPetunjuk.setText("petunjuk");
        labPetunjuk.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(labPetunjuk, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(311, 311, 311))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(labPetunjuk, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );

        tabbedPan.addTab("Konsultasi", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 102, 153));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Profil");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        labelNama.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNama.setText("Nama ");

        labelJK.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelJK.setText("JK");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Nama Lengkap ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Jenis Kelamin");

        labelTglLahir.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelTglLahir.setText("Tgl-Lahir");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Tanggal Lahir");

        labelUmur.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelUmur.setText("Umur");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("umur");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("No. Handphone");

        labelNohp.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNohp.setText("No HP");

        labelAlamat.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelAlamat.setText("Alamat");
        labelAlamat.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Alamat");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelAlamat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 286, Short.MAX_VALUE))
                    .addComponent(labelNohp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelUmur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelTglLahir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelJK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(labelNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(30, 30, 30)
                .addComponent(labelJK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(30, 30, 30)
                .addComponent(labelTglLahir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(30, 30, 30)
                .addComponent(labelUmur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(30, 30, 30)
                .addComponent(labelNohp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(30, 30, 30)
                .addComponent(labelAlamat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 102, 153));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/logout.png"))); // NOI18N
        jButton1.setText("Logout");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 102, 153));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        jButton3.setText("Edit Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 102, 153));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        jButton4.setText("Ganti Password");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPan.addTab("Profil", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPan)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPan, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {

            new Konsultasi(userList).show();
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Menu_User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnLihatSolusiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusiActionPerformed
        try {
            Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
            String[] info = new String[3];
            info[0] = penyakit.getKode();
            info[1] = penyakit.getPenyakit();
            info[2] = penyakit.getSolusi();
            Solusi.main(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
            
            System.out.println(e);
        }
    }//GEN-LAST:event_btnLihatSolusiActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        User dataUser = userList.get(0);
        Edit_User.main(dataUser);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        User dataUser = userList.get(0);
        Ganti_Password.main(dataUser);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Anda Yakin Logout dari Sistem?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(ok==0){
            new Login().show();
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnLihatSolusi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi1ActionPerformed
        
        tabbedPan.setSelectedIndex(1);
    }//GEN-LAST:event_btnLihatSolusi1ActionPerformed

    private void btnLihatSolusi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi2ActionPerformed
        tabbedPan.setSelectedIndex(2);
    }//GEN-LAST:event_btnLihatSolusi2ActionPerformed

    private void btnLihatSolusi3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi3ActionPerformed
        tabbedPan.setSelectedIndex(3);
    }//GEN-LAST:event_btnLihatSolusi3ActionPerformed

    private void btnLihatSolusi4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi4ActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Anda Yakin Logout dari Sistem?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(ok==0){
            new Login().show();
            dispose();
        }
    }//GEN-LAST:event_btnLihatSolusi4ActionPerformed

    private void btnLihatSolusi5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi5ActionPerformed
        try {
            Penyakit penyakit = (Penyakit) cbPenyakit.getSelectedItem();
            String[] info = new String[3];
            info[0] = penyakit.getKode();
            info[1] = penyakit.getPenyakit();
            info[2] = penyakit.getPencegahan();
            Pencegahan.main(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
            
            System.out.println(e);
        }
    }//GEN-LAST:event_btnLihatSolusi5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        textUsername = args[0];
        textPassword = args[1];
        txtIndex = Integer.parseInt(args[2]);
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
            java.util.logging.Logger.getLogger(Menu_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu_User(textUsername, textPassword,txtIndex).setVisible(true);
                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLihatSolusi;
    private javax.swing.JButton btnLihatSolusi1;
    private javax.swing.JButton btnLihatSolusi2;
    private javax.swing.JButton btnLihatSolusi3;
    private javax.swing.JButton btnLihatSolusi4;
    private javax.swing.JButton btnLihatSolusi5;
    private javax.swing.JComboBox cbPenyakit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labKet;
    private javax.swing.JLabel labMalaria;
    private javax.swing.JLabel labPetunjuk;
    private javax.swing.JLabel labelAlamat;
    private javax.swing.JLabel labelJK;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelNohp;
    private javax.swing.JLabel labelTglLahir;
    private javax.swing.JLabel labelUmur;
    private javax.swing.JTabbedPane tabbedPan;
    private javax.swing.JTable tabelGejalaPenyakit;
    // End of variables declaration//GEN-END:variables
}
