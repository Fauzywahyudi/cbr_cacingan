/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cbr_cacingan.user;

import cbr_cacingan.model.Gejala;
import cbr_cacingan.model.GejalaService;
import cbr_cacingan.model.Penyakit;
import cbr_cacingan.model.PenyakitService;
import cbr_cacingan.model.Rule;
import cbr_cacingan.model.RuleService;
import cbr_cacingan.model.User;
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
public class Hasil_Konsultasi extends javax.swing.JFrame {

    Statement st;
    Statement st2;
    Connection con;
    ResultSet rs, rs2;
    String sql = "";
    String sql2 = "";
    GejalaService gejalaService;
    PenyakitService penyakitService;
    RuleService ruleService;
    List<Gejala> gejalaList;
    List<Penyakit> penyakitList;
    List<Rule> ruleList;
    List<Gejala> gejalaRuleList;
    String strJawaban = "";
    DefaultTableModel _tblGejalaJawaban;
    String hasilKodePenyakit = "";
    String hasilNamaPenyakit = "";
    String openHtml = "<HTML><P style=\"text-align: justify \">";
    String closeHtml = "</P></HTML>";
    public static String arg;
    public static List<User> userList;

    public Hasil_Konsultasi(String strJawab, List<User> listData) throws SQLException {
        initComponents();
        koneksiDB();
        setTitle("Hasil Konsultasi");
        strJawaban = strJawab;
        gejalaService = new GejalaService(con);
        gejalaList = gejalaService.getAllGejala();

        penyakitService = new PenyakitService(con);
        penyakitList = penyakitService.getAllPenyakit();

        ruleService = new RuleService(con);
        ruleList = ruleService.getAllRule();
        this.userList = listData;
        System.out.println("parsing ke loading :" + userList.get(0).toString());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cekGejalaRule();
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

    public void cekGejalaRule() throws SQLException {
        int jumlahRule = ruleService.getJumlahRule();
        System.out.println("jumlah rule: " + jumlahRule);
        String penyakitMax = "";
        String penyakitMaxNama = "";
        int i = 0;
        double hasilAkhir = 0;
        while (i < jumlahRule) {
            double cek = 0;
            String strKodeGejalaRule = ruleList.get(i).getKodeGejala();
            cek = Proses_CBR(strJawaban, strKodeGejalaRule, i);
            if (cek > hasilAkhir) {
                hasilAkhir = cek;
                penyakitMax = ruleList.get(i).getKodePenyakit();
                penyakitMaxNama = ruleList.get(i).getNamaPenyakit();
            }
            i++;
        }

        String strAmbilKode = "";
        int z = 0;
        String[] index = strJawaban.split(",");
        for (String ind : index) {
            int no = Integer.parseInt(ind);
            if (z == 0) {
                strAmbilKode = String.valueOf(gejalaList.get(no - 1).getKode());
            } else {
                strAmbilKode = strAmbilKode + "," + String.valueOf(gejalaList.get(no - 1).getKode());
            }
            z++;
        }

        System.out.println("kode jawaban : " + strAmbilKode);

        System.out.println("Nilai tertinggi adalah " + penyakitMax + " (" + hasilAkhir + ")");
        insert(userList.get(0).getId(), strAmbilKode, penyakitMax, hasilAkhir);
        hasilKodePenyakit = penyakitMax;
        hasilNamaPenyakit = penyakitMaxNama;

//        gejalaRuleList = ruleService.getGejalaFromRule();
    }

    public void insert(int idUser, String strJawabanKode, String kodePenyakit, double hasil) throws SQLException {
        String setHasil;
        DecimalFormat decFor = new DecimalFormat("#.##");
        setHasil = decFor.format(hasil);
        koneksiDB();
        sql = "INSERT INTO tb_konsultasi VALUES (NULL,'" + idUser + "','" + strJawabanKode + "','" + kodePenyakit + "','" + setHasil + "', NOW())";
        st.executeUpdate(sql);
//            JOptionPane.showMessageDialog(null, "Konsultasi Berhasil");
        showGejala(idUser);
        try {
            showInfoUser();
        } catch (ParseException ex) {
            Logger.getLogger(Hasil_Konsultasi.class.getName()).log(Level.SEVERE, null, ex);
        }
        showInfoDiagnosa(idUser);
    }

    public void showGejala(int idUser) throws SQLException {
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
            rs = st.executeQuery("SELECT * FROM tb_konsultasi WHERE id_user='" + idUser + "' ORDER BY tgl_konsul DESC LIMIT 1");
            while (rs.next()) {
                strJawabanUser = rs.getString("jawaban_user");
            }
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
        String JK = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tglLahir = df.parse(dataUser.getTglLahir());
        Date tglNow = new Date();
        int umur = tglNow.getYear() - tglLahir.getYear();
        if (umur == 0) {
            labUmur.setText("kurang 1 tahun");
        } else {

            labUmur.setText(umur + " tahun");
        }
        labNama.setText(dataUser.getNama());
        if (dataUser.getJk().equals("L")) {
            JK = "Laki-laki";
        } else {
            JK = "Perempuan";
        }
        labJk.setText(JK);
        labAlamat.setText(dataUser.getAlamat());
        labNohp.setText(dataUser.getNohp());
    }

    public void showInfoDiagnosa(int idUser) throws SQLException {
        try {
            koneksiDB();
            rs = st.executeQuery("SELECT * FROM tb_konsultasi a INNER JOIN tb_penyakit b ON a.kode_penyakit= b.kode_penyakit WHERE a.id_user='" + idUser + "' ORDER BY a.tgl_konsul DESC LIMIT 1");
            while (rs.next()) {
                labPenyakit.setText(rs.getString("nama_penyakit"));
                double hasil = Double.parseDouble(rs.getString("hasil"));
                DecimalFormat decFor = new DecimalFormat("#.##");
                labHasil.setText(decFor.format(hasil) + " (" + (decFor.format(hasil * 100)) + "%)");
                labSolusi.setText(openHtml + rs.getString("solusi") + closeHtml);
            }
        } catch (SQLException e) {
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

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelJawaban = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labNama = new javax.swing.JLabel();
        labJk = new javax.swing.JLabel();
        labUmur = new javax.swing.JLabel();
        labNohp = new javax.swing.JLabel();
        labAlamat = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labPenyakit = new javax.swing.JLabel();
        labHasil = new javax.swing.JLabel();
        labSolusi = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnHome = new javax.swing.JButton();

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tambah Penyakit");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(491, 339));

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hasil Konsultasi");

        jPanel2.setBackground(new java.awt.Color(255, 102, 153));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Lengkap");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Jenis Kelamin");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Umur");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("No. HP");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Alamat");

        labNama.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labNama.setForeground(new java.awt.Color(255, 255, 255));
        labNama.setText("nama");

        labJk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labJk.setForeground(new java.awt.Color(255, 255, 255));
        labJk.setText("jk");

        labUmur.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labUmur.setForeground(new java.awt.Color(255, 255, 255));
        labUmur.setText("umur");

        labNohp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labNohp.setForeground(new java.awt.Color(255, 255, 255));
        labNohp.setText("nohp");

        labAlamat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labAlamat.setForeground(new java.awt.Color(255, 255, 255));
        labAlamat.setText("alamat");
        labAlamat.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labAlamat, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labNohp)
                            .addComponent(labUmur)
                            .addComponent(labJk)
                            .addComponent(labNama))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labNama))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(labJk))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labUmur))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labNohp))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labAlamat, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 102, 153));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Hasil Diagnosa : ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Penyakit");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Hasil");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Solusi");

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSolusi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labHasil)
                            .addComponent(labPenyakit))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(labPenyakit))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labHasil))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labSolusi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Gejala dipilih");

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 102, 153));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cbr_cacingan/asseticon/home.png"))); // NOI18N
        btnHome.setText("Back Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
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
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        try {
            String info[] = new String[3];
            info[0] = userList.get(0).getUsername();
            info[1] = userList.get(0).getPassword();
            info[2] = "2";
            Menu_User.main(info);
            this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data pada Tabel Penyakit!", "Error", 1);
        }
    }//GEN-LAST:event_btnHomeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[], List<User> list) {
        arg = args[0];
        userList = list;
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
            java.util.logging.Logger.getLogger(Hasil_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hasil_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hasil_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hasil_Konsultasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Hasil_Konsultasi(arg, userList).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Hasil_Konsultasi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHome;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labAlamat;
    private javax.swing.JLabel labHasil;
    private javax.swing.JLabel labJk;
    private javax.swing.JLabel labNama;
    private javax.swing.JLabel labNohp;
    private javax.swing.JLabel labPenyakit;
    private javax.swing.JLabel labSolusi;
    private javax.swing.JLabel labUmur;
    private javax.swing.JTable tabelJawaban;
    // End of variables declaration//GEN-END:variables

    public double Proses_CBR(String strJawaban, String strKodeGejalaRule, int i) throws SQLException {

        System.out.println("---------------------------");
        System.out.println(i);
        int sumbot = ruleService.getSumBobotRule(strKodeGejalaRule);
        System.out.println(sumbot);
        System.out.println(strJawaban);
        System.out.println(strKodeGejalaRule);

        String strAmbilKode = "";
        int j = 0;
        String[] index = strJawaban.split(",");
        for (String ind : index) {
            int no = Integer.parseInt(ind);
            if (j == 0) {
                strAmbilKode = String.valueOf(gejalaList.get(no - 1).getKode());
            } else {
                strAmbilKode = strAmbilKode + "," + String.valueOf(gejalaList.get(no - 1).getKode());
            }
            j++;
        }
        System.out.println("kode jawaban : " + strAmbilKode);
        List<Gejala> gejalaJawabList = ruleService.getGejalaFromRule(strAmbilKode);
        List<Gejala> gejalaRuleList = ruleService.getGejalaFromRule(strKodeGejalaRule);
        int BobotJawaban = 0;
        int indexx = 0;
//        for(Gejala gejJawab : gejalaRuleList){
//            System.out.println("=>"+gejJawab.getKode());
//        }
        for (Gejala gejRul : gejalaRuleList) {
            if (indexx == 0) {
                BobotJawaban = 0;
            }
            for (Gejala gejUser : gejalaJawabList) {
//                System.out.println(gejRul.getKode()+"=="+gejUser.getKode()+" "+gejUser.getBobot());
                if (gejUser.getKode().equals(gejRul.getKode())) {
                    BobotJawaban = BobotJawaban + gejUser.getBobot();
                    System.out.println("--------");
                    System.out.println(BobotJawaban);
                    System.out.println("--------");
                    break;
                }
            }
            indexx++;
        }

        System.out.println("Rule " + (i + 1) + " jumlah Bobot : " + sumbot);
        System.out.println("Bobot Jawaban Pasien P0" + (i + 1) + " : " + BobotJawaban);
        double hasil = Double.parseDouble(String.valueOf(BobotJawaban)) / Double.parseDouble(String.valueOf(sumbot));
        System.out.println("Hasil = " + hasil);

        System.out.println("---------------------------");
        return hasil;
    }
}
