package cbr_cacingan.admin;

import cbr_cacingan.Koneksi;
import cbr_cacingan.Login;
import cbr_cacingan.Login_Admin;
import cbr_cacingan.model.Admin;
import cbr_cacingan.model.AdminService;
import cbr_cacingan.model.User;
import cbr_cacingan.model.UserService;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

public class Menu_Admin extends javax.swing.JFrame {

    DefaultTableModel _tblPenyakit;
    DefaultTableModel _tblGejala;
    DefaultTableModel _tblRule;
    DefaultTableModel _tblKonsultasi;
    DefaultTableModel _tblUser;
    Statement st;
    Connection con;
    ResultSet rs, rs2;
    String sql = "";
    String sql2 = "";
    Admin user;
    AdminService userService;
    List<Admin> userList;
    UserService uservisce;
    
    public static String textUsername;
    public static String textPassword;
    public static int txtIndex;

    public Menu_Admin(String username, String password, int index) throws SQLException {
        initComponents();
        tabbedPanel.setSelectedIndex(index);
        setTitle("Admin");
        koneksiDB();
        userService = new AdminService(con);
        uservisce = new UserService(con);
        userList = userService.getDataUserby(username, password);
        System.out.println(userList.get(0).getNama());
        tampilkanDataPenyakit();
        tampilkanDataGejala();
        tampilkanDataRule();
        tampilkanDataKonsultasi();
        tampilkanDataUser();
        setProfil();
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

    public void tampilkanDataPenyakit() {
        Object[] row = {"No", "Kode Penyakit", "Nama Penyakit", "Keterangan", "Pencegahan", "Solusi"};
        _tblPenyakit = new DefaultTableModel(null, row);
        tabelPenyakit.setModel(_tblPenyakit);
        tabelPenyakit.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "", kolom5 = "", kolom6 = "";

        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_penyakit order by kode_penyakit asc");
            int i = 1;
            while (rs.next()) {
                kolom1 = String.valueOf(i);
                kolom2 = rs.getString(2);
                kolom3 = rs.getString(3);
                kolom4 = rs.getString(4);
                kolom5 = rs.getString(5);
                kolom6 = rs.getString(6);
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4, kolom5, kolom6};
                _tblPenyakit.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal");
            tabelPenyakit.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        
        TableColumn column;
        tabelPenyakit.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column=tabelPenyakit.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column=tabelPenyakit.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column=tabelPenyakit.getColumnModel().getColumn(2);
        column.setPreferredWidth(150);
        column=tabelPenyakit.getColumnModel().getColumn(3);
        column.setPreferredWidth(200);
        column=tabelPenyakit.getColumnModel().getColumn(4);
        column.setPreferredWidth(200);
    }

    public void tampilkanDataGejala() {
        Object[] row = {"No", "Kode Gejala", "Nama Gejala", "Bobot"};
        _tblGejala = new DefaultTableModel(null, row);
        tabelGejala.setModel(_tblGejala);
        tabelGejala.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "";

        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_gejala order by kode_gejala asc");
            int i = 1;
            while (rs.next()) {
                kolom1 = String.valueOf(i);
                kolom2 = rs.getString(2);
                kolom3 = rs.getString(3);
                kolom4 = rs.getString(4);
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4};
                _tblGejala.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal");
            tabelGejala.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        
        TableColumn column;
        tabelGejala.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column=tabelGejala.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column=tabelGejala.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column=tabelGejala.getColumnModel().getColumn(2);
        column.setPreferredWidth(450);
        column=tabelGejala.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);
    }

    public void tampilkanDataRule(){
        
         Object[] row = {"No", "Kode Rule", "Nama Penyakit", "Rule"};
        _tblRule = new DefaultTableModel(null, row);
        tabelRule.setModel(_tblRule);
        tabelRule.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "";
        

        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_rule a INNER JOIN tb_penyakit b ON a.kode_penyakit=b.kode_penyakit order by a.kode_rule asc");
            int i = 1;
            while (rs.next()) {
                kolom1 = String.valueOf(i);
                kolom2 = rs.getString("kode_rule");
                kolom3 = rs.getString("nama_penyakit");
                kolom4 = rs.getString("kode_gejala");
                String set = kolom4.replaceAll(",", " AND ");
                kolom4 = "IF "+set + " THEN "+rs.getString("nama_penyakit")+" ("+rs.getString("kode_penyakit")+" )";
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4};
                _tblRule.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal");
            tabelRule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        
        TableColumn column;
        tabelRule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column=tabelRule.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column=tabelRule.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column=tabelRule.getColumnModel().getColumn(2);
        column.setPreferredWidth(200);
        column=tabelRule.getColumnModel().getColumn(3);
        column.setPreferredWidth(700);
    }
    
    public void tampilkanDataKonsultasi(){
         Object[] row = {"Id", "Nama User", "Umur", "Penyakit", "Hasil", "Tanggal Konsul"};
        _tblKonsultasi = new DefaultTableModel(null, row);
        tabelKonsultasi.setModel(_tblKonsultasi);
        tabelKonsultasi.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "", kolom5 = "", kolom6 = "";
        
        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_konsultasi a INNER JOIN tb_user b ON a.id_user=b.id_user INNER JOIN "
                    + "tb_penyakit c ON a.kode_penyakit=c.kode_penyakit order by a.tgl_konsul asc");
            int i = 1;
            while (rs.next()) {
                kolom1 = rs.getString("id_konsultasi");
                kolom2 = rs.getString("nama");
                kolom3 = String.valueOf(userService.getUmur(rs.getString("tgl_lahir")));
                kolom4 = rs.getString("nama_penyakit");
                String set = kolom4.replaceAll(",", " AND ");
                kolom4 = rs.getString("nama_penyakit")+" ("+rs.getString("kode_penyakit")+" )";
                double hasil = Double.parseDouble(rs.getString("hasil"));
                DecimalFormat decFor = new DecimalFormat("#.##");
                kolom5 = decFor.format(hasil).toString();
                kolom6 = rs.getString("tgl_konsul");
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4,kolom5, kolom6};
                _tblKonsultasi.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal" + e);
            tabelKonsultasi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        
        TableColumn column;
        tabelKonsultasi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column=tabelKonsultasi.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column=tabelKonsultasi.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column=tabelKonsultasi.getColumnModel().getColumn(2);
        column.setPreferredWidth(200);
        column=tabelKonsultasi.getColumnModel().getColumn(3);
        column.setPreferredWidth(250);
        column=tabelKonsultasi.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column=tabelKonsultasi.getColumnModel().getColumn(5);
        column.setPreferredWidth(250);
    }
    
    public void tampilkanDataUser(){
         Object[] row = {"Id", "Nama User", "Jenis Kelamin", "Umur", "Tanggal Lahir", "No. HP"};
        _tblUser = new DefaultTableModel(null, row);
        tabelUser.setModel(_tblUser);
        tabelUser.setBorder(null);
        String kolom1 = "", kolom2 = "", kolom3 = "", kolom4 = "", kolom5 = "", kolom6 = "";
        
        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_user ORDER BY id_user ASC");
            int i = 1;
            while (rs.next()) {
                kolom1 = 
                kolom2 = rs.getString("id_user");
                kolom2 = rs.getString("nama");
                if(rs.getString("jk").equals("L")){
                    kolom3 = "Laki-laki";
                }else{
                    kolom3 = "Perempuan";
                }
                kolom4 = String.valueOf(userService.getUmur(rs.getString("tgl_lahir")))+" tahun";
                kolom5 = rs.getString("tgl_lahir");
                kolom6 = rs.getString("nohp");
                Object[] tampil = {kolom1, kolom2, kolom3, kolom4,kolom5, kolom6};
                _tblUser.addRow(tampil);
                i++;
            }
        } catch (Exception e) {
            System.err.println("Gagal" + e);
            tabelUser.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        
        TableColumn column;
        tabelUser.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column=tabelUser.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column=tabelUser.getColumnModel().getColumn(1);
        column.setPreferredWidth(350);
        column=tabelUser.getColumnModel().getColumn(2);
        column.setPreferredWidth(150);
        column=tabelUser.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);
        column=tabelUser.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column=tabelUser.getColumnModel().getColumn(5);
        column.setPreferredWidth(200);
    }
    
    public void setProfil(){
        Admin user = userList.get(0);
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
            Logger.getLogger(Menu_Admin.class.getName()).log(Level.SEVERE, null, ex);
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPanel = new javax.swing.JTabbedPane();
        panelHome = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnLihatSolusi1 = new javax.swing.JButton();
        btnLihatSolusi2 = new javax.swing.JButton();
        btnLihatSolusi3 = new javax.swing.JButton();
        btnLihatSolusi4 = new javax.swing.JButton();
        btnLihatSolusi5 = new javax.swing.JButton();
        btnLihatSolusi6 = new javax.swing.JButton();
        panelUser = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelUser = new javax.swing.JTable();
        btnLihatUser = new javax.swing.JButton();
        btnEditUser = new javax.swing.JButton();
        btnTambahUser = new javax.swing.JButton();
        btnHapusUser = new javax.swing.JButton();
        btnEditUser1 = new javax.swing.JButton();
        btnLihatKonsultasi2 = new javax.swing.JButton();
        panelKonsultasi = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelKonsultasi = new javax.swing.JTable();
        btnLihatKonsultasi = new javax.swing.JButton();
        btnLihatKonsultasi1 = new javax.swing.JButton();
        panelPenyakit = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnTambahPenyakit = new javax.swing.JButton();
        btnHapusPenyakit = new javax.swing.JButton();
        btnEditPenyakit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelPenyakit = new javax.swing.JTable();
        btnLihatPenyakit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelGejala = new javax.swing.JTable();
        btnHapusGejala = new javax.swing.JButton();
        btnEditGejala = new javax.swing.JButton();
        btnTambahGejala = new javax.swing.JButton();
        btnEditPenyakit2 = new javax.swing.JButton();
        panelProfil = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelRule = new javax.swing.JTable();
        btnTambahRule = new javax.swing.JButton();
        btnLihatRule = new javax.swing.JButton();
        btnEditRule = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabbedPanel.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPanel.setForeground(new java.awt.Color(255, 102, 153));
        tabbedPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPanelMouseClicked(evt);
            }
        });

        panelHome.setBackground(new java.awt.Color(255, 102, 153));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("SISTEM PAKAR DIAGNOSA PENYAKIT CACINGAN");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        btnLihatSolusi1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi1.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/keluser.png"))); // NOI18N
        btnLihatSolusi1.setText("Kelola User");
        btnLihatSolusi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi1ActionPerformed(evt);
            }
        });

        btnLihatSolusi2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi2.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/coment.png"))); // NOI18N
        btnLihatSolusi2.setText("Lihat Hasil Konsultasi");
        btnLihatSolusi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi2ActionPerformed(evt);
            }
        });

        btnLihatSolusi3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi3.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnLihatSolusi3.setText("Kelola Penyakit");
        btnLihatSolusi3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi3ActionPerformed(evt);
            }
        });

        btnLihatSolusi4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi4.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnLihatSolusi4.setText("Kelola Rule");
        btnLihatSolusi4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi4ActionPerformed(evt);
            }
        });

        btnLihatSolusi5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi5.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnLihatSolusi5.setText("Perbaharui Informasi");
        btnLihatSolusi5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi5ActionPerformed(evt);
            }
        });

        btnLihatSolusi6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLihatSolusi6.setForeground(new java.awt.Color(255, 102, 153));
        btnLihatSolusi6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/logout.png"))); // NOI18N
        btnLihatSolusi6.setText("Logout");
        btnLihatSolusi6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatSolusi6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLihatSolusi5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLihatSolusi2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(184, 184, 184))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatSolusi5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatSolusi6, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(136, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPanel.addTab("Halaman Utama", panelHome);

        jPanel3.setBackground(new java.awt.Color(255, 102, 153));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Data User");

        tabelUser.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tabelUser);

        btnLihatUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatUser.setText("Lihat");
        btnLihatUser.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatUserActionPerformed(evt);
            }
        });

        btnEditUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnEditUser.setText("Edit");
        btnEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUserActionPerformed(evt);
            }
        });

        btnTambahUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/addperson.png"))); // NOI18N
        btnTambahUser.setText("Tambah User");
        btnTambahUser.setPreferredSize(new java.awt.Dimension(115, 29));
        btnTambahUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahUserActionPerformed(evt);
            }
        });

        btnHapusUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/delete.png"))); // NOI18N
        btnHapusUser.setText("Hapus");
        btnHapusUser.setPreferredSize(new java.awt.Dimension(85, 29));
        btnHapusUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusUserActionPerformed(evt);
            }
        });

        btnEditUser1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/refresh.png"))); // NOI18N
        btnEditUser1.setText("Refresh");
        btnEditUser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUser1ActionPerformed(evt);
            }
        });

        btnLihatKonsultasi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/print.png"))); // NOI18N
        btnLihatKonsultasi2.setText("Cetak");
        btnLihatKonsultasi2.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatKonsultasi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatKonsultasi2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addComponent(jScrollPane5)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnTambahUser, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLihatUser, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnEditUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnLihatKonsultasi2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEditUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHapusUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatKonsultasi2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatUser, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        javax.swing.GroupLayout panelUserLayout = new javax.swing.GroupLayout(panelUser);
        panelUser.setLayout(panelUserLayout);
        panelUserLayout.setHorizontalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelUserLayout.setVerticalGroup(
            panelUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPanel.addTab("Data User", panelUser);

        panelKonsultasi.setBackground(new java.awt.Color(255, 102, 153));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Data Konsultasi");

        tabelKonsultasi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tabelKonsultasi);

        btnLihatKonsultasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatKonsultasi.setText("Lihat");
        btnLihatKonsultasi.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatKonsultasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatKonsultasiActionPerformed(evt);
            }
        });

        btnLihatKonsultasi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/print.png"))); // NOI18N
        btnLihatKonsultasi1.setText("Cetak");
        btnLihatKonsultasi1.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatKonsultasi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatKonsultasi1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKonsultasiLayout = new javax.swing.GroupLayout(panelKonsultasi);
        panelKonsultasi.setLayout(panelKonsultasiLayout);
        panelKonsultasiLayout.setHorizontalGroup(
            panelKonsultasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKonsultasiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKonsultasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKonsultasiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLihatKonsultasi1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLihatKonsultasi, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelKonsultasiLayout.setVerticalGroup(
            panelKonsultasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKonsultasiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(74, 74, 74)
                .addGroup(panelKonsultasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLihatKonsultasi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLihatKonsultasi1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPanel.addTab("Data Konsultasi", panelKonsultasi);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Penyakit");

        btnTambahPenyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/addlist.png"))); // NOI18N
        btnTambahPenyakit.setText("Tambah");
        btnTambahPenyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPenyakitActionPerformed(evt);
            }
        });

        btnHapusPenyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/delete.png"))); // NOI18N
        btnHapusPenyakit.setText("Hapus");
        btnHapusPenyakit.setPreferredSize(new java.awt.Dimension(85, 29));
        btnHapusPenyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPenyakitActionPerformed(evt);
            }
        });

        btnEditPenyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnEditPenyakit.setText("Edit");
        btnEditPenyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPenyakitActionPerformed(evt);
            }
        });

        tabelPenyakit.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tabelPenyakit);

        btnLihatPenyakit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatPenyakit.setText("Lihat");
        btnLihatPenyakit.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatPenyakit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatPenyakitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambahPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLihatPenyakit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditPenyakit)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapusPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnHapusPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditPenyakit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLihatPenyakit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahPenyakit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 102, 153));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Gejala");

        tabelGejala.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabelGejala);

        btnHapusGejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/delete.png"))); // NOI18N
        btnHapusGejala.setText("Hapus");
        btnHapusGejala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusGejalaActionPerformed(evt);
            }
        });

        btnEditGejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnEditGejala.setText("Edit");
        btnEditGejala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditGejalaActionPerformed(evt);
            }
        });

        btnTambahGejala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/addlist.png"))); // NOI18N
        btnTambahGejala.setText("Tambah");
        btnTambahGejala.setPreferredSize(new java.awt.Dimension(103, 29));
        btnTambahGejala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahGejalaActionPerformed(evt);
            }
        });

        btnEditPenyakit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnEditPenyakit2.setText("Lihat");
        btnEditPenyakit2.setPreferredSize(new java.awt.Dimension(71, 29));
        btnEditPenyakit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPenyakit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnTambahGejala, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditPenyakit2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnEditGejala, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHapusGejala)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHapusGejala)
                    .addComponent(btnEditGejala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditPenyakit2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahGejala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout panelPenyakitLayout = new javax.swing.GroupLayout(panelPenyakit);
        panelPenyakit.setLayout(panelPenyakitLayout);
        panelPenyakitLayout.setHorizontalGroup(
            panelPenyakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPenyakitLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPenyakitLayout.setVerticalGroup(
            panelPenyakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPanel.addTab("Data Penyakit", panelPenyakit);

        panelProfil.setBackground(new java.awt.Color(255, 102, 153));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Rule");

        tabelRule.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabelRule);

        btnTambahRule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/addlist.png"))); // NOI18N
        btnTambahRule.setText("Tambah Rule");
        btnTambahRule.setPreferredSize(new java.awt.Dimension(115, 29));
        btnTambahRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahRuleActionPerformed(evt);
            }
        });

        btnLihatRule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/search.png"))); // NOI18N
        btnLihatRule.setText("Lihat");
        btnLihatRule.setPreferredSize(new java.awt.Dimension(71, 29));
        btnLihatRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLihatRuleActionPerformed(evt);
            }
        });

        btnEditRule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        btnEditRule.setText("Edit");
        btnEditRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRuleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProfilLayout = new javax.swing.GroupLayout(panelProfil);
        panelProfil.setLayout(panelProfilLayout);
        panelProfilLayout.setHorizontalGroup(
            panelProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProfilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(panelProfilLayout.createSequentialGroup()
                        .addComponent(btnTambahRule, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLihatRule, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditRule)))
                .addContainerGap())
        );
        panelProfilLayout.setVerticalGroup(
            panelProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProfilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(49, 49, 49)
                .addGroup(panelProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btnEditRule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLihatRule, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahRule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPanel.addTab("Data Rule", panelProfil);

        jPanel4.setBackground(new java.awt.Color(255, 102, 153));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Profil");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 51, 51));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/logout.png"))); // NOI18N
        jButton2.setText("Logout");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 102, 153));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        jButton5.setText("Edit Data");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 102, 153));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/edit.png"))); // NOI18N
        jButton6.setText("Ganti Password");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(186, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(187, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                        .addGap(0, 371, Short.MAX_VALUE))
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
                .addContainerGap()
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
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPanel.addTab("Profil", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPanel)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahPenyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPenyakitActionPerformed
        int index = tabbedPanel.getSelectedIndex();
        new Tambah_Penyakit(index,userList.get(0)).show();
        dispose();
    }//GEN-LAST:event_btnTambahPenyakitActionPerformed

    private void tabbedPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPanelMouseClicked
    }//GEN-LAST:event_tabbedPanelMouseClicked

    private void btnHapusGejalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusGejalaActionPerformed
        try {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Apakah yakin menghapus gejala " + _tblGejala.getValueAt(tabelGejala.getSelectedRow(), 2) + "?",
                    "Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                String kode = _tblGejala.getValueAt(tabelGejala.getSelectedRow(), 1).toString();
                System.out.println(kode);
                try {
                    koneksiDB();
                    java.sql.Statement stm = con.createStatement();
                    stm.executeUpdate("DELETE FROM tb_gejala WHERE kode_gejala='" + kode + "'");
                    JOptionPane.showMessageDialog(null, "Data gejala berhasil dihapus", "", 1);
                } catch (Exception e) {
                    System.err.println("Gagal Hapus");
                }
            }

            tampilkanDataGejala();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Gejala!", "Error", 1);
        }
    }//GEN-LAST:event_btnHapusGejalaActionPerformed

    private void btnEditGejalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditGejalaActionPerformed
        try {
            String info[] = new String[1];
            info[0] = _tblGejala.getValueAt(tabelGejala.getSelectedRow(), 1).toString();
            Edit_Gejala.main(info,userList.get(0));
            this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Gejala!", "Error", 1);
        }
    }//GEN-LAST:event_btnEditGejalaActionPerformed

    private void btnHapusPenyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPenyakitActionPerformed

        try {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Apakah yakin menghapus penyakit " + _tblPenyakit.getValueAt(tabelPenyakit.getSelectedRow(), 2) + "?",
                    "Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                String kode = _tblPenyakit.getValueAt(tabelPenyakit.getSelectedRow(), 1).toString();
                System.out.println(kode);
                try {
                    koneksiDB();
                    java.sql.Statement stm = con.createStatement();
                    java.sql.Statement stmDel = con.createStatement();
                    stm.executeUpdate("DELETE FROM tb_penyakit WHERE kode_penyakit='" + kode + "'");
                    stmDel.executeUpdate("DELETE FROM tb_rule WHERE kode_penyakit='" + kode + "'");
                    JOptionPane.showMessageDialog(null, "Data penyakit berhasil dihapus", "", 1);
                } catch (Exception e) {
                    System.err.println("Gagal Hapus");
                }
            }

            tampilkanDataPenyakit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }

    }//GEN-LAST:event_btnHapusPenyakitActionPerformed

    private void btnEditPenyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPenyakitActionPerformed
        try {
            String info[] = new String[1];
            info[0] = _tblPenyakit.getValueAt(tabelPenyakit.getSelectedRow(), 1).toString();
            Edit_Penyakit.main(info,userList.get(0));
            this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }


    }//GEN-LAST:event_btnEditPenyakitActionPerformed

    private void btnTambahGejalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahGejalaActionPerformed
        int index = tabbedPanel.getSelectedIndex();
        Admin user = userList.get(0);
        new Tambah_Gejala(index,user).show();
        dispose();
    }//GEN-LAST:event_btnTambahGejalaActionPerformed

    private void btnLihatPenyakitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatPenyakitActionPerformed
        try {
            String info[] = new String[1];
            info[0] = _tblPenyakit.getValueAt(tabelPenyakit.getSelectedRow(), 1).toString();
            Lihat_Penyakit.main(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }
    }//GEN-LAST:event_btnLihatPenyakitActionPerformed

    private void btnEditPenyakit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPenyakit2ActionPerformed
        try {
            String info[] = new String[1];
            info[0] = _tblGejala.getValueAt(tabelGejala.getSelectedRow(), 1).toString();
            Lihat_Gejala.main(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Gejala!", "Error", 1);
        }
    }//GEN-LAST:event_btnEditPenyakit2ActionPerformed

    private void btnTambahRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahRuleActionPerformed
        try {
            int index = tabbedPanel.getSelectedIndex();
            new Tambah_Rule(index,userList.get(0),0).show();
            dispose();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Rule!", "Error", 1);
        }
    }//GEN-LAST:event_btnTambahRuleActionPerformed

    private void btnLihatRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatRuleActionPerformed
        try {
            int index = tabbedPanel.getSelectedIndex();
            int cbRow = tabelRule.getSelectedRow();
            new Tambah_Rule(index,userList.get(0),cbRow).show();
            dispose();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Rule!", "Error", 1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Konsultasi!", "Error", 1);
        }
    }//GEN-LAST:event_btnLihatRuleActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Anda Yakin Logout dari Sistem?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(ok==0){
           new Login_Admin().show();
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Admin dataUser = userList.get(0);
        Edit_Admin.main(dataUser);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Admin dataUser = userList.get(0);
        Ganti_Password_Admin.main(dataUser);
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnEditRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRuleActionPerformed
        try {
            int index = tabbedPanel.getSelectedIndex();
            int cbRow = tabelRule.getSelectedRow();
            new Tambah_Rule(index,userList.get(0),cbRow).show();
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Menu_Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Konsultasi!", "Error", 1);
        }
    }//GEN-LAST:event_btnEditRuleActionPerformed

    private void btnLihatKonsultasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatKonsultasiActionPerformed
        try {
            String info[] = new String[1];
            info[0] = _tblKonsultasi.getValueAt(tabelKonsultasi.getSelectedRow(), 0).toString();
            Lihat_Konsultasi.main(info,userList.get(0));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Konsultasi!", "Error", 1);
        }
    }//GEN-LAST:event_btnLihatKonsultasiActionPerformed

    private void btnLihatUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatUserActionPerformed
        try {
            String id = _tblUser.getValueAt(tabelUser.getSelectedRow(), 0).toString();
            System.out.println("id " +id);
            User user = uservisce.getDataUserbyId(id).get(0);
            Lihat_User.main(user);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel User!", "Error", 1);
        }
    }//GEN-LAST:event_btnLihatUserActionPerformed

    private void btnEditUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUserActionPerformed
         try {
            String id = _tblUser.getValueAt(tabelUser.getSelectedRow(), 0).toString();
            System.out.println("id " +id);
            User user = uservisce.getDataUserbyId(id).get(0);
            Edit_User.main(user);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel User!", "Error", 1);
        }
    }//GEN-LAST:event_btnEditUserActionPerformed

    private void btnTambahUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahUserActionPerformed
       new Tambah_User().show();
    }//GEN-LAST:event_btnTambahUserActionPerformed

    private void btnHapusUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusUserActionPerformed
        try {
            int reply = JOptionPane.showConfirmDialog(this,
                    "Apakah yakin menghapus User " + _tblUser.getValueAt(tabelUser.getSelectedRow(), 1) + "?",
                    "Hapus Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                String id = _tblUser.getValueAt(tabelUser.getSelectedRow(), 0).toString();
                try {
                    koneksiDB();
                    java.sql.Statement stm = con.createStatement();
                    stm.executeUpdate("DELETE FROM tb_user WHERE id_user='" + id + "'");
                    JOptionPane.showMessageDialog(null, "Data User berhasil dihapus", "", 1);
                } catch (Exception e) {
                    System.err.println("Gagal Hapus");
                }
            }

            tampilkanDataUser();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel User!", "Error", 1);
        }
    }//GEN-LAST:event_btnHapusUserActionPerformed

    private void btnEditUser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUser1ActionPerformed
        tampilkanDataUser();
    }//GEN-LAST:event_btnEditUser1ActionPerformed

    private void btnLihatKonsultasi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatKonsultasi1ActionPerformed
        try {
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("reportKonsultasi.jasper"), null,Koneksi.getConnection());
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_btnLihatKonsultasi1ActionPerformed

    private void btnLihatKonsultasi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatKonsultasi2ActionPerformed
       try {
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("reportUser.jasper"), null,Koneksi.getConnection());
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_btnLihatKonsultasi2ActionPerformed

    private void btnLihatSolusi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi1ActionPerformed

        tabbedPanel.setSelectedIndex(1);
    }//GEN-LAST:event_btnLihatSolusi1ActionPerformed

    private void btnLihatSolusi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi2ActionPerformed
        tabbedPanel.setSelectedIndex(2);
    }//GEN-LAST:event_btnLihatSolusi2ActionPerformed

    private void btnLihatSolusi3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi3ActionPerformed
        tabbedPanel.setSelectedIndex(3);
    }//GEN-LAST:event_btnLihatSolusi3ActionPerformed

    private void btnLihatSolusi4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi4ActionPerformed
        tabbedPanel.setSelectedIndex(4);
    }//GEN-LAST:event_btnLihatSolusi4ActionPerformed

    private void btnLihatSolusi5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi5ActionPerformed
        tabbedPanel.setSelectedIndex(5);
    }//GEN-LAST:event_btnLihatSolusi5ActionPerformed

    private void btnLihatSolusi6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatSolusi6ActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Anda Yakin Logout dari Sistem?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(ok==0){
            new Login_Admin().show();
            dispose();
        }
    }//GEN-LAST:event_btnLihatSolusi6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(Menu_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu_Admin(textUsername, textPassword, txtIndex).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Menu_Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditGejala;
    private javax.swing.JButton btnEditPenyakit;
    private javax.swing.JButton btnEditPenyakit2;
    private javax.swing.JButton btnEditRule;
    private javax.swing.JButton btnEditUser;
    private javax.swing.JButton btnEditUser1;
    private javax.swing.JButton btnHapusGejala;
    private javax.swing.JButton btnHapusPenyakit;
    private javax.swing.JButton btnHapusUser;
    private javax.swing.JButton btnLihatKonsultasi;
    private javax.swing.JButton btnLihatKonsultasi1;
    private javax.swing.JButton btnLihatKonsultasi2;
    private javax.swing.JButton btnLihatPenyakit;
    private javax.swing.JButton btnLihatRule;
    private javax.swing.JButton btnLihatSolusi1;
    private javax.swing.JButton btnLihatSolusi2;
    private javax.swing.JButton btnLihatSolusi3;
    private javax.swing.JButton btnLihatSolusi4;
    private javax.swing.JButton btnLihatSolusi5;
    private javax.swing.JButton btnLihatSolusi6;
    private javax.swing.JButton btnLihatUser;
    private javax.swing.JButton btnTambahGejala;
    private javax.swing.JButton btnTambahPenyakit;
    private javax.swing.JButton btnTambahRule;
    private javax.swing.JButton btnTambahUser;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelAlamat;
    private javax.swing.JLabel labelJK;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelNohp;
    private javax.swing.JLabel labelTglLahir;
    private javax.swing.JLabel labelUmur;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelKonsultasi;
    private javax.swing.JPanel panelPenyakit;
    private javax.swing.JPanel panelProfil;
    private javax.swing.JPanel panelUser;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JTable tabelGejala;
    private javax.swing.JTable tabelKonsultasi;
    private javax.swing.JTable tabelPenyakit;
    private javax.swing.JTable tabelRule;
    private javax.swing.JTable tabelUser;
    // End of variables declaration//GEN-END:variables
}
