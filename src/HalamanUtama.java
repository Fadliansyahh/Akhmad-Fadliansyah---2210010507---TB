
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class HalamanUtama extends javax.swing.JFrame {

    /**
     * Creates new form HalamanUtama
     */
    public HalamanUtama() {
        initComponents();
        loadDataToTables();
        loadComboBoxNoPolisi();
        initListeners();
        jTextField19.setEditable(false);
        jTextField20.setEditable(false);
        loadNIKToComboBox();
    }
    
    private void loadDataToTables() {
    // String koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
    String username = "root";  // Ganti dengan username MySQL
    String password = "";  // Ganti dengan password MySQL

    // Query SQL untuk mengambil data dari tabel mobil, pelanggan, penyewaan, dan transaksi
    String queryMobil = "SELECT id_mobil, merek, model, tahun, no_polisi, harga_sewa, status FROM mobil";
    String queryPelanggan = "SELECT id_pelanggan, nama, nik, alamat, no_handphone, email FROM pelanggan";
    String queryPenyewaan = "SELECT id_penyewaan, id_pelanggan, no_polisi, tgl_peminjaman, tgl_pengembalian, lama_peminjaman, total FROM penyewaan";
    String queryTransaksi = "SELECT p.id_penyewaan, p.tgl_peminjaman, p.tgl_pengembalian, p.lama_peminjaman, p.total, t.metode_pembayaran, t.status_pembayaran " +
                            "FROM penyewaan p " +
                            "JOIN transaksi t ON p.id_penyewaan = t.id_penyewaan";

    try (Connection conn = DriverManager.getConnection(url, username, password)) {
        
        // -------------------------------------------
        // Mengambil data mobil dan menampilkannya di jTable1
        Statement stmtMobil = conn.createStatement();
        ResultSet rsMobil = stmtMobil.executeQuery(queryMobil);
        
        // Mendapatkan model tabel jTable1 untuk data mobil
        DefaultTableModel modelMobil = (DefaultTableModel) jTable1.getModel();
        modelMobil.setRowCount(0);  // Menghapus data lama dari jTable1

        // Menambahkan data mobil ke jTable1
        while (rsMobil.next()) {
            int idMobil = rsMobil.getInt("id_mobil");
            String merek = rsMobil.getString("merek");
            String model = rsMobil.getString("model");
            int tahun = rsMobil.getInt("tahun");
            String noPolisi = rsMobil.getString("no_polisi");
            int hargaSewa = rsMobil.getInt("harga_sewa");
            String status = rsMobil.getString("status");
            
            modelMobil.addRow(new Object[]{idMobil, merek, model, tahun, noPolisi, hargaSewa, status});
        }

        // Memastikan jTable1 diperbarui
        jTable1.revalidate();
        jTable1.repaint();

        // -------------------------------------------
        // Mengambil data pelanggan dan menampilkannya di jTable2
        Statement stmtPelanggan = conn.createStatement();
        ResultSet rsPelanggan = stmtPelanggan.executeQuery(queryPelanggan);
        
        // Mendapatkan model tabel jTable2 untuk data pelanggan
        DefaultTableModel modelPelanggan = (DefaultTableModel) jTable2.getModel();
        modelPelanggan.setRowCount(0);  // Menghapus data lama dari jTable2

        // Menambahkan data pelanggan ke jTable2
        while (rsPelanggan.next()) {
            int idPelanggan = rsPelanggan.getInt("id_pelanggan");
            String nama = rsPelanggan.getString("nama");
            String nik = rsPelanggan.getString("nik");
            String alamat = rsPelanggan.getString("alamat");
            String noHandphone = rsPelanggan.getString("no_handphone");
            String email = rsPelanggan.getString("email");
            
            modelPelanggan.addRow(new Object[]{idPelanggan, nama, nik, alamat, noHandphone, email});
        }

        // Memastikan jTable2 diperbarui
        jTable2.revalidate();
        jTable2.repaint();

        // -------------------------------------------
        // Mengambil data penyewaan dan menampilkannya di jTable3
        Statement stmtPenyewaan = conn.createStatement();
        ResultSet rsPenyewaan = stmtPenyewaan.executeQuery(queryPenyewaan);
        
        // Mendapatkan model tabel jTable3 untuk data penyewaan
        DefaultTableModel modelPenyewaan = (DefaultTableModel) jTable3.getModel();
        modelPenyewaan.setRowCount(0);  // Menghapus data lama dari jTable3

        // Menambahkan data penyewaan ke jTable3
        while (rsPenyewaan.next()) {
            int idPenyewaan = rsPenyewaan.getInt("id_penyewaan");
            int idPelanggan = rsPenyewaan.getInt("id_pelanggan");
            String noPolisi = rsPenyewaan.getString("no_polisi");
            Date tglPeminjaman = rsPenyewaan.getDate("tgl_peminjaman");
            Date tglPengembalian = rsPenyewaan.getDate("tgl_pengembalian");
            int lamaPeminjaman = rsPenyewaan.getInt("lama_peminjaman");
            int total = rsPenyewaan.getInt("total");
            
            modelPenyewaan.addRow(new Object[]{idPenyewaan, idPelanggan, noPolisi, tglPeminjaman, tglPengembalian, lamaPeminjaman, total});
        }

        // Memastikan jTable3 diperbarui
        jTable3.revalidate();
        jTable3.repaint();

        // -------------------------------------------
        // Mengambil data transaksi dan menampilkannya di jTable4
        Statement stmtTransaksi = conn.createStatement();
        ResultSet rsTransaksi = stmtTransaksi.executeQuery(queryTransaksi);
        
        // Mendapatkan model tabel jTable4 untuk data transaksi
        DefaultTableModel modelTransaksi = (DefaultTableModel) jTable4.getModel();
        modelTransaksi.setRowCount(0);  // Menghapus data lama dari jTable4

        // Menambahkan data transaksi ke jTable4
        while (rsTransaksi.next()) {
            int idPenyewaan = rsTransaksi.getInt("id_penyewaan");
            Date tglPeminjaman = rsTransaksi.getDate("tgl_peminjaman");
            Date tglPengembalian = rsTransaksi.getDate("tgl_pengembalian");
            int lamaPeminjaman = rsTransaksi.getInt("lama_peminjaman");
            int total = rsTransaksi.getInt("total");
            String metodePembayaran = rsTransaksi.getString("metode_pembayaran");
            String statusPembayaran = rsTransaksi.getString("status_pembayaran");
            
            modelTransaksi.addRow(new Object[]{idPenyewaan, tglPeminjaman, tglPengembalian, lamaPeminjaman, total, metodePembayaran, statusPembayaran});
        }

        // Memastikan jTable4 diperbarui
        jTable4.revalidate();
        jTable4.repaint();

    } catch (SQLException e) {
        // Menangani error jika terjadi masalah dengan database
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}






    
    private void addDataToDatabase() {
    // Mengambil data dari inputan user
    String merek = jComboBox2.getSelectedItem().toString();
    String model = jComboBox3.getSelectedItem().toString();
    int tahun = jYearChooser1.getYear();
    String noPolisi = jTextField3.getText();
    int hargaSewa = Integer.parseInt(jTextField4.getText());
    String status = jComboBox1.getSelectedItem().toString();

    // String koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
    String username = "root";  // Ganti dengan username MySQL
    String password = "";  // Ganti dengan password MySQL

    // Query SQL untuk menambahkan data
    String query = "INSERT INTO mobil (merek, model, tahun, no_polisi, harga_sewa, status) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url, username, password);
         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        // Menyiapkan data untuk disimpan ke database
        stmt.setString(1, merek);
        stmt.setString(2, model);
        stmt.setInt(3, tahun);
        stmt.setString(4, noPolisi);
        stmt.setInt(5, hargaSewa);
        stmt.setString(6, status);

        // Menjalankan query untuk memasukkan data
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            // Mendapatkan generated keys (id_mobil)
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                // id_mobil yang baru tidak ditampilkan di message box
                int idMobil = generatedKeys.getInt(1);  // id_mobil yang baru

                // Menampilkan pemberitahuan sukses tanpa menampilkan id_mobil
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");

                // Menambahkan data ke tabel di UI (termasuk id_mobil)
                DefaultTableModel modelTabel = (DefaultTableModel) jTable1.getModel();
                modelTabel.addRow(new Object[]{idMobil, merek, model, tahun, noPolisi, hargaSewa, status});

                // Memastikan tabel diperbarui
                jTable1.revalidate();
                jTable1.repaint();

                // Mengosongkan field input setelah data ditambahkan
                jComboBox2.setSelectedIndex(0);
                jComboBox3.setSelectedIndex(0);
                jTextField3.setText("");
                jTextField4.setText("");
                jComboBox1.setSelectedIndex(0);
            }
        }

    } catch (SQLException e) {
        // Menangani error
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}
    
    private void loadComboBoxNoPolisi() {
    // Koneksi ke database
    String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
    String username = "root";  // Ganti dengan username MySQL
    String password = "";  // Ganti dengan password MySQL

    String query = "SELECT no_polisi FROM mobil";  // Query untuk mengambil semua nomor polisi

    try (Connection conn = DriverManager.getConnection(url, username, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        // Menghapus item yang ada di JComboBox (jika ada)
        jComboBox5.removeAllItems();

        // Menambahkan data nomor polisi ke JComboBox
        while (rs.next()) {
            String noPolisi = rs.getString("no_polisi");
            jComboBox5.addItem(noPolisi);  // Tambahkan ke JComboBox
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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

        jpframe = new javax.swing.JPanel();
        jpheader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbkeluar = new javax.swing.JButton();
        jpisi = new javax.swing.JPanel();
        jpDatamobil = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jpPenyewaanmobil = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jpLaporan = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jplanjutpenyewaan = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel41 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jpMenu = new javax.swing.JPanel();
        jbdatamobil = new javax.swing.JButton();
        jbdatapelanggan = new javax.swing.JButton();
        jblaporan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpframe.setBackground(new java.awt.Color(204, 204, 204));

        jpheader.setBackground(new java.awt.Color(204, 204, 204));
        jpheader.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("APLIKASI RENTAL MOBIL");

        jbkeluar.setForeground(new java.awt.Color(204, 204, 204));
        jbkeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit_closethesession_close_6317.png"))); // NOI18N
        jbkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbkeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpheaderLayout = new javax.swing.GroupLayout(jpheader);
        jpheader.setLayout(jpheaderLayout);
        jpheaderLayout.setHorizontalGroup(
            jpheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpheaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpheaderLayout.setVerticalGroup(
            jpheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpheaderLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jpheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbkeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpheaderLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jpisi.setBackground(new java.awt.Color(204, 204, 204));
        jpisi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jpDatamobil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel3.setText("DATA MOBIL ");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.lightGray, null, null));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Merek Mobil");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Model Mobil");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Tahun");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("No. Polisi");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Harga Sewa");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Status");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tersedia", "Disewa" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toyota", "Honda", "BMW", "Ford", "Mazda" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sedan", "SUV", "Hatchback", "Convertible", "Pickup" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(98, 98, 98)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, 0, 419, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_Mobil", "Merek", "Model", "Tahun", "No. Polisi", "Harga Sewa", "Status"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jpDatamobilLayout = new javax.swing.GroupLayout(jpDatamobil);
        jpDatamobil.setLayout(jpDatamobilLayout);
        jpDatamobilLayout.setHorizontalGroup(
            jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatamobilLayout.createSequentialGroup()
                .addGroup(jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDatamobilLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)))
                    .addGroup(jpDatamobilLayout.createSequentialGroup()
                        .addGroup(jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpDatamobilLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3))
                            .addGroup(jpDatamobilLayout.createSequentialGroup()
                                .addGap(146, 146, 146)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 393, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpDatamobilLayout.setVerticalGroup(
            jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatamobilLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDatamobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jpPenyewaanmobil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel4.setText("PENYEWAAN MOBIL");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.lightGray, null, null));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Nama");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("NIK");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Alamat");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("No. Handphone");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Email");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton4.setText("INPUT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("LANJUT");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Biodata");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField6)
                                    .addComponent(jTextField8)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpPenyewaanmobilLayout = new javax.swing.GroupLayout(jpPenyewaanmobil);
        jpPenyewaanmobil.setLayout(jpPenyewaanmobilLayout);
        jpPenyewaanmobilLayout.setHorizontalGroup(
            jpPenyewaanmobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPenyewaanmobilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpPenyewaanmobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(565, Short.MAX_VALUE))
        );
        jpPenyewaanmobilLayout.setVerticalGroup(
            jpPenyewaanmobilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPenyewaanmobilLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addGap(44, 44, 44)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        jpLaporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel5.setText("LAPORAN");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Data Pelanggan");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_pelanggan ", "nama", "nik", "alamat", "	no_handphone", "email"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel28.setText("Laporan Penyewaan");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_penyewaan ", "id_pelanggan ", "No.Polisi", "tgl_peminjaman", "tgl_pengembalian", "lama_peminjaman", "total"
            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel29.setText("Data Transaksi");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id_penyewaan ", "tgl_peminjaman", "tgl_pengembalian", "lama_peminjaman", "total", "metode pembayaran", "status pembayaran"
            }
        ));
        jScrollPane5.setViewportView(jTable4);

        jButton9.setText("CETAK DATA TRANSAKSI");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("REFRESH");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("HAPUS");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpLaporanLayout = new javax.swing.GroupLayout(jpLaporan);
        jpLaporan.setLayout(jpLaporanLayout);
        jpLaporanLayout.setHorizontalGroup(
            jpLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLaporanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
                    .addGroup(jpLaporanLayout.createSequentialGroup()
                        .addGroup(jpLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel29)
                            .addComponent(jLabel28)
                            .addGroup(jpLaporanLayout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 689, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpLaporanLayout.setVerticalGroup(
            jpLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLaporanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jplanjutpenyewaan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel32.setText("PENYEWAAN MOBIL");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("No. Polisi");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel34.setText("Merek");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Model");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel36.setText("Tahun");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("Harga (/hari)");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel38.setText("Status");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel39.setText("Tgl Peminjaman");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("Tgl Pengembalian");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel41.setText("Lama Peminjaman");

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setText("Total");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel43.setText("NIK");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel44.setText("Metode Pembayaran");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Transfer Bank" }));

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("Status Pembayaran");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Belum Lunas", "Lunas", " " }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39)
                            .addComponent(jLabel40))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField14)
                            .addComponent(jTextField15)
                            .addComponent(jTextField16)
                            .addComponent(jTextField17)
                            .addComponent(jTextField18)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel43)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 276, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(36, 36, 36)
                        .addComponent(jTextField19))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel44))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField20)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton5.setText("HITUNG");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("SUBMIT");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jplanjutpenyewaanLayout = new javax.swing.GroupLayout(jplanjutpenyewaan);
        jplanjutpenyewaan.setLayout(jplanjutpenyewaanLayout);
        jplanjutpenyewaanLayout.setHorizontalGroup(
            jplanjutpenyewaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jplanjutpenyewaanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jplanjutpenyewaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jplanjutpenyewaanLayout.createSequentialGroup()
                        .addGroup(jplanjutpenyewaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addGroup(jplanjutpenyewaanLayout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jplanjutpenyewaanLayout.setVerticalGroup(
            jplanjutpenyewaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jplanjutpenyewaanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addGap(30, 30, 30)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jplanjutpenyewaanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout jpisiLayout = new javax.swing.GroupLayout(jpisi);
        jpisi.setLayout(jpisiLayout);
        jpisiLayout.setHorizontalGroup(
            jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpDatamobil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpPenyewaanmobil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpLaporan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpisiLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jplanjutpenyewaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jpisiLayout.setVerticalGroup(
            jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpDatamobil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpPenyewaanmobil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpLaporan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpisiLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jplanjutpenyewaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jpMenu.setBackground(new java.awt.Color(204, 204, 204));
        jpMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbdatamobil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/car_front_icon_177220.png"))); // NOI18N
        jbdatamobil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbdatamobilActionPerformed(evt);
            }
        });

        jbdatapelanggan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rentacar_89116.png"))); // NOI18N
        jbdatapelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbdatapelangganActionPerformed(evt);
            }
        });

        jblaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/coordinated_assessment_documents_report_icon_148377.png"))); // NOI18N
        jblaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jblaporanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpMenuLayout = new javax.swing.GroupLayout(jpMenu);
        jpMenu.setLayout(jpMenuLayout);
        jpMenuLayout.setHorizontalGroup(
            jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpMenuLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbdatamobil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbdatapelanggan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jblaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jpMenuLayout.setVerticalGroup(
            jpMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpMenuLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jbdatamobil, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jbdatapelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jblaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(225, 225, 225))
        );

        javax.swing.GroupLayout jpframeLayout = new javax.swing.GroupLayout(jpframe);
        jpframe.setLayout(jpframeLayout);
        jpframeLayout.setHorizontalGroup(
            jpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpframeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpframeLayout.createSequentialGroup()
                        .addComponent(jpMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jpisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpheader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpframeLayout.setVerticalGroup(
            jpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpframeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpheader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpframe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpframe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbdatamobilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbdatamobilActionPerformed
        // Menghapus semua komponen dari panel 'jpisi'
        jpisi.removeAll();
        jpisi.repaint();
        jpisi.revalidate();

        // Menambahkan panel 'jpDatamobil' ke 'jpisi'
        jpisi.add(jpDatamobil);
        jpisi.repaint();
        jpisi.revalidate();
    }//GEN-LAST:event_jbdatamobilActionPerformed

    private void jbdatapelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbdatapelangganActionPerformed
        // Menghapus semua komponen dari panel 'jpisi'
        jpisi.removeAll();
        jpisi.repaint();
        jpisi.revalidate();

        // Menambahkan panel 'jpDatapelanggan' ke 'jpisi'
        jpisi.add(jpPenyewaanmobil);
        jpisi.repaint();
        jpisi.revalidate();
    }//GEN-LAST:event_jbdatapelangganActionPerformed

    private void jblaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jblaporanActionPerformed
        // Menghapus semua komponen dari panel 'jpisi'
        jpisi.removeAll();
        jpisi.repaint();
        jpisi.revalidate();

        // Menambahkan panel 'jpLaporan' ke 'jpisi'
        jpisi.add(jpLaporan);
        jpisi.repaint();
        jpisi.revalidate();
    }//GEN-LAST:event_jblaporanActionPerformed

    private void jbkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbkeluarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jbkeluarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addDataToDatabase();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Mendapatkan baris yang diklik
        int row = jTable1.getSelectedRow();
        
        // Pastikan baris yang dipilih valid
        if (row != -1) {
            // Ambil data dari baris yang dipilih dan masukkan ke komponen input
            String merek = jTable1.getValueAt(row, 1).toString();  // Kolom merek
            String model = jTable1.getValueAt(row, 2).toString();  // Kolom model
            int tahun = Integer.parseInt(jTable1.getValueAt(row, 3).toString());  // Kolom tahun
            String noPolisi = jTable1.getValueAt(row, 4).toString();  // Kolom no_polisi
            int hargaSewa = Integer.parseInt(jTable1.getValueAt(row, 5).toString());  // Kolom harga_sewa
            String status = jTable1.getValueAt(row, 6).toString();  // Kolom status

            // Mengisi data ke dalam komponen input (misalnya jComboBox, jTextField, dll)
            jComboBox2.setSelectedItem(merek);   // Mengisi comboBox dengan merek
            jComboBox3.setSelectedItem(model);   // Mengisi comboBox dengan model
            jYearChooser1.setYear(tahun);        // Mengisi jYearChooser dengan tahun
            jTextField3.setText(noPolisi);       // Mengisi jTextField dengan noPolisi
            jTextField4.setText(String.valueOf(hargaSewa)); // Mengisi jTextField dengan hargaSewa
            jComboBox1.setSelectedItem(status);  // Mengisi comboBox dengan status
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Mendapatkan baris yang dipilih di JTable
        int row = jTable1.getSelectedRow();
        
        // Pastikan ada baris yang dipilih
        if (row != -1) {
            // Ambil data dari komponen input
            String merek = jComboBox2.getSelectedItem().toString();
            String model = jComboBox3.getSelectedItem().toString();
            int tahun = jYearChooser1.getYear();
            String noPolisi = jTextField3.getText();
            int hargaSewa = Integer.parseInt(jTextField4.getText());
            String status = jComboBox1.getSelectedItem().toString();

            // Ambil ID mobil yang dipilih dari kolom pertama (id_mobil)
            int idMobil = (int) jTable1.getValueAt(row, 0); // Misalnya ID ada di kolom pertama

            // Update data di database
            updateDataInDatabase(idMobil, merek, model, tahun, noPolisi, hargaSewa, status);

            // Update data di JTable
            jTable1.setValueAt(merek, row, 1);
            jTable1.setValueAt(model, row, 2);
            jTable1.setValueAt(tahun, row, 3);
            jTable1.setValueAt(noPolisi, row, 4);
            jTable1.setValueAt(hargaSewa, row, 5);
            jTable1.setValueAt(status, row, 6);

            // Memberi feedback kepada pengguna
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } else {
            // Jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diedit.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Mendapatkan baris yang dipilih di JTable
        int row = jTable1.getSelectedRow();

        // Pastikan ada baris yang dipilih
        if (row != -1) {
            // Ambil ID mobil yang dipilih dari kolom pertama (id_mobil)
            int idMobil = (int) jTable1.getValueAt(row, 0);

            // Konfirmasi penghapusan data
            int confirmation = JOptionPane.showConfirmDialog(null, 
                "Apakah Anda yakin ingin menghapus data mobil ini?", 
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Hapus data di database
                deleteDataFromDatabase(idMobil);

                // Hapus data di JTable
                DefaultTableModel modelTabel = (DefaultTableModel) jTable1.getModel();
                modelTabel.removeRow(row);

                // Memberi feedback kepada pengguna
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            }
        } else {
            // Jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Menghapus semua komponen dari panel 'jpisi'
        jpisi.removeAll();
        jpisi.repaint();
        jpisi.revalidate();

        // Menambahkan panel 'jpLaporan' ke 'jpisi'
        jpisi.add(jplanjutpenyewaan);
        jpisi.repaint();
        jpisi.revalidate();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
            // Mengambil data dari inputan pengguna
            String nama = jTextField5.getText();
            String nik = jTextField6.getText();
            String alamat = jTextArea1.getText();
            String noHandphone = jTextField7.getText();
            String email = jTextField8.getText();

            // Pastikan semua field terisi
            if (nama.isEmpty() || nik.isEmpty() || alamat.isEmpty() || noHandphone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Hentikan jika ada yang kosong
            }

            // Koneksi ke database MySQL
            String url = "jdbc:mysql://localhost:3306/rental_mobil";
            String username = "root";  // Ganti dengan username MySQL
            String password = "";  // Ganti dengan password MySQL

            // Query SQL untuk memasukkan data ke tabel pelanggan
            String query = "INSERT INTO pelanggan (nama, nik, alamat, no_handphone, email) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Menyiapkan data untuk disimpan
                stmt.setString(1, nama);
                stmt.setString(2, nik);
                stmt.setString(3, alamat);
                stmt.setString(4, noHandphone);
                stmt.setString(5, email);

                // Menjalankan query untuk memasukkan data
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    // Kosongkan field setelah data berhasil disimpan
                    jTextField5.setText("");
                    jTextField6.setText("");
                    jTextArea1.setText("");
                    jTextField7.setText("");
                    jTextField8.setText("");
                }
            } catch (SQLException e) {
                // Menangani error jika terjadi kesalahan koneksi atau query
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // Ambil item yang dipilih dari JComboBox5
        String selectedNoPolisi = (String) jComboBox5.getSelectedItem();

        if (selectedNoPolisi != null) {
            // Memanggil metode untuk memuat detail mobil berdasarkan no_polisi
            loadDetailsByNoPolisi(selectedNoPolisi);
        }
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // Ambil data dari JTextField harga dan lama peminjaman
            int harga = Integer.parseInt(jTextField17.getText()); // Harga ada di jTextField17
            int lamaPeminjaman = Integer.parseInt(jTextField19.getText()); // Lama peminjaman ada di jTextField12

            // Hitung total harga
            int total = harga * lamaPeminjaman;

            // Tampilkan hasil pada jTextField20
            jTextField20.setText(String.valueOf(total));

            // Pastikan jTextField20 tidak dapat diedit
            jTextField20.setEditable(false);
        } catch (NumberFormatException e) {
            // Jika ada input yang tidak valid, tampilkan pesan error
            JOptionPane.showMessageDialog(null, "Pastikan harga dan lama peminjaman berisi angka yang valid.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed
        private void loadNIKToComboBox() {
        // String koneksi ke database MySQL
        String url = "jdbc:mysql://localhost:3306/rental_mobil"; // Ganti dengan nama database
        String username = "root"; // Ganti dengan username MySQL
        String password = ""; // Ganti dengan password MySQL

        String query = "SELECT nik FROM pelanggan";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Hapus semua item dari jComboBox6 sebelum menambahkan data
            jComboBox6.removeAllItems();
            jComboBox6.addItem("Pilih NIK"); // Tambahkan item default

            // Tambahkan semua NIK dari database ke ComboBox
            while (rs.next()) {
                String nik = rs.getString("nik");
                jComboBox6.addItem(nik);
            }

        } catch (SQLException e) {
            // Tampilkan pesan error jika terjadi masalah
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
      // Ambil data dari input
    String nik = (String) jComboBox6.getSelectedItem(); // NIK (id pelanggan sementara)
    String noPolisi = (String) jComboBox5.getSelectedItem(); // No Polisi mobil
    java.util.Date tglPeminjaman = jDateChooser3.getDate(); // Tanggal Peminjaman
    java.util.Date tglPengembalian = jDateChooser4.getDate(); // Tanggal Pengembalian
    int lamaPeminjaman = Integer.parseInt(jTextField19.getText()); // Lama Peminjaman
    int total = Integer.parseInt(jTextField20.getText()); // Total Harga

    // Ambil data metode pembayaran dan status pembayaran
    String metodePembayaran = (String) jComboBox4.getSelectedItem(); // Cash atau Transfer Bank
    String statusPembayaran = (String) jComboBox7.getSelectedItem(); // Lunas atau Belum Lunas (menggunakan jComboBox7)

    // Validasi input
    if (nik == null || nik.equals("Pilih NIK") || noPolisi == null || noPolisi.equals("Pilih No Polisi")) {
        JOptionPane.showMessageDialog(null, "Mohon lengkapi data NIK dan No Polisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (tglPeminjaman == null || tglPengembalian == null || lamaPeminjaman <= 0 || total <= 0) {
        JOptionPane.showMessageDialog(null, "Mohon lengkapi data tanggal dan hitungan lama peminjaman!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validasi metode pembayaran
    if (!(metodePembayaran.equals("Cash") || metodePembayaran.equals("Transfer Bank"))) {
        JOptionPane.showMessageDialog(null, "Metode Pembayaran tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validasi status pembayaran
    if (!(statusPembayaran.equals("Lunas") || statusPembayaran.equals("Belum Lunas"))) {
        JOptionPane.showMessageDialog(null, "Status Pembayaran tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Query untuk mengambil id_pelanggan berdasarkan nik
    String idPelangganQuery = "SELECT id_pelanggan FROM pelanggan WHERE nik = ?";
    int idPelanggan = -1;

    // Konversi tanggal ke format SQL
    java.sql.Date sqlTglPeminjaman = new java.sql.Date(tglPeminjaman.getTime());
    java.sql.Date sqlTglPengembalian = new java.sql.Date(tglPengembalian.getTime());

    // Koneksi ke database MySQL
    String url = "jdbc:mysql://localhost:3306/rental_mobil"; // Ganti dengan nama database
    String username = "root"; // Ganti dengan username MySQL
    String password = ""; // Ganti dengan password MySQL

    try (Connection conn = DriverManager.getConnection(url, username, password);
         PreparedStatement pstmt = conn.prepareStatement(idPelangganQuery)) {

        // Menyiapkan query untuk mengambil id_pelanggan
        pstmt.setString(1, nik);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                idPelanggan = rs.getInt("id_pelanggan");
            } else {
                JOptionPane.showMessageDialog(null, "Pelanggan dengan NIK " + nik + " tidak ditemukan.");
                return;
            }
        }

        // Query untuk menyimpan data ke tabel penyewaan
        String queryPenyewaan = "INSERT INTO penyewaan (id_pelanggan, no_polisi, tgl_peminjaman, tgl_pengembalian, lama_peminjaman, total) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmtInsertPenyewaan = conn.prepareStatement(queryPenyewaan, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameter query penyewaan
            pstmtInsertPenyewaan.setInt(1, idPelanggan); // id_pelanggan
            pstmtInsertPenyewaan.setString(2, noPolisi);
            pstmtInsertPenyewaan.setDate(3, sqlTglPeminjaman);
            pstmtInsertPenyewaan.setDate(4, sqlTglPengembalian);
            pstmtInsertPenyewaan.setInt(5, lamaPeminjaman);
            pstmtInsertPenyewaan.setInt(6, total);

            // Eksekusi query penyewaan
            int rowsInserted = pstmtInsertPenyewaan.executeUpdate();
            if (rowsInserted > 0) {
                // Ambil id_penyewaan yang baru saja disimpan
                try (ResultSet rs = pstmtInsertPenyewaan.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idPenyewaan = rs.getInt(1);

                        // Query untuk menyimpan data transaksi
                        String queryTransaksi = "INSERT INTO transaksi (id_penyewaan, tgl_peminjaman, tgl_pengembalian, lama_peminjaman, total, metode_pembayaran, status_pembayaran) " +
                                                "VALUES (?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement pstmtInsertTransaksi = conn.prepareStatement(queryTransaksi)) {
                            // Set parameter query transaksi
                            pstmtInsertTransaksi.setInt(1, idPenyewaan); // id_penyewaan
                            pstmtInsertTransaksi.setDate(2, sqlTglPeminjaman); // tgl_peminjaman
                            pstmtInsertTransaksi.setDate(3, sqlTglPengembalian); // tgl_pengembalian
                            pstmtInsertTransaksi.setInt(4, lamaPeminjaman); // lama_peminjaman
                            pstmtInsertTransaksi.setInt(5, total); // total
                            pstmtInsertTransaksi.setString(6, metodePembayaran); // metode_pembayaran
                            pstmtInsertTransaksi.setString(7, statusPembayaran); // status_pembayaran

                            // Eksekusi query transaksi
                            int rowsInsertedTransaksi = pstmtInsertTransaksi.executeUpdate();
                            if (rowsInsertedTransaksi > 0) {
                                JOptionPane.showMessageDialog(null, "Data transaksi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                clearForm(); // Kosongkan form setelah sukses
                            } else {
                                JOptionPane.showMessageDialog(null, "Data transaksi gagal disimpan!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Data penyewaan gagal disimpan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException e) {
        // Tampilkan error jika ada masalah dengan database
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void clearForm() {
    jComboBox6.setSelectedIndex(0); // Reset NIK
    jComboBox5.setSelectedIndex(0); // Reset No Polisi
    jDateChooser3.setDate(null); // Reset Tanggal Peminjaman
    jDateChooser4.setDate(null); // Reset Tanggal Pengembalian
    jTextField19.setText(""); // Reset Lama Peminjaman
    jTextField20.setText(""); // Reset Total Harga
}
        
    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // Mendapatkan NIK yang dipilih dari ComboBox
        String selectedNIK = (String) jComboBox6.getSelectedItem();
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
            // Menampilkan JFileChooser untuk memilih lokasi dan nama file PDF
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Struk Penyewaan Rental Mobil");
    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        // Tambahkan ekstensi .pdf jika pengguna tidak menambahkannya
        if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
        }

        // String koneksi ke database
        String url = "jdbc:mysql://localhost:3306/rental_mobil";  
        String username = "root";  
        String password = "";  

        // Query SQL untuk mengambil data transaksi
        String query = "SELECT t.id_penyewaan, t.tgl_peminjaman, t.lama_peminjaman, t.total, " +
                       "t.metode_pembayaran, t.status_pembayaran " +
                       "FROM transaksi t " +
                       "JOIN penyewaan p ON t.id_penyewaan = p.id_penyewaan";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Membuat dokumen PDF
            PDDocument document = new PDDocument();

            while (rs.next()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 750);

                    // Judul struk
                    contentStream.showText("Struk Penyewaan Rental Mobil");
                    contentStream.newLineAtOffset(0, -30);

                    // Data transaksi
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.showText("ID Penyewaan: " + rs.getString("id_penyewaan"));
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Tanggal Peminjaman: " + rs.getDate("tgl_peminjaman").toString());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Lama Peminjaman: " + rs.getInt("lama_peminjaman") + " hari");
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Total: Rp " + rs.getInt("total"));
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Metode Pembayaran: " + rs.getString("metode_pembayaran"));
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Status Pembayaran: " + rs.getString("status_pembayaran"));
                    contentStream.newLineAtOffset(0, -40);

                    contentStream.showText("----------------------------------------");
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Terima kasih telah menggunakan layanan kami.");
                    contentStream.endText();
                } catch (IOException ex) {
                    Logger.getLogger(HalamanUtama.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Simpan dokumen PDF
            document.save(fileToSave);
            document.close();

            // Tampilkan pesan sukses
            JOptionPane.showMessageDialog(this, "Struk transaksi berhasil diekspor ke " + fileToSave.getAbsolutePath());

        } catch (SQLException e) {
            // Menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(HalamanUtama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
           try {
        // Memanggil metode untuk memuat ulang data ke tabel-tabel
        loadDataToTables();
        
        // Tampilkan pesan sukses
        JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
        } catch (Exception e) {
            // Menangani error jika terjadi masalah
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    
    private void loadDetailsByNoPolisi(String noPolisi) {
        // String koneksi ke database MySQL
        String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
        String username = "root";  // Ganti dengan username MySQL
        String password = "";  // Ganti dengan password MySQL

        // Query SQL untuk mengambil data mobil berdasarkan no_polisi
        String query = "SELECT merek, model, tahun, harga_sewa, status FROM mobil WHERE no_polisi = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameter no_polisi pada query
            stmt.setString(1, noPolisi);

            // Menjalankan query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Ambil data dari ResultSet
                String merek = rs.getString("merek");
                String model = rs.getString("model");
                int tahun = rs.getInt("tahun");
                int hargaSewa = rs.getInt("harga_sewa");
                String status = rs.getString("status");

                // Menampilkan data ke JTextField
                jTextField14.setText(merek);
                jTextField15.setText(model);
                jTextField16.setText(String.valueOf(tahun));
                jTextField17.setText(String.valueOf(hargaSewa));
                jTextField18.setText(status);

                // Menonaktifkan JTextField agar tidak bisa diedit
                jTextField14.setEditable(false);
                jTextField15.setEditable(false);
                jTextField16.setEditable(false);
                jTextField17.setEditable(false);
                jTextField18.setEditable(false);
            } else {
                JOptionPane.showMessageDialog(null, "Mobil dengan no polisi " + noPolisi + " tidak ditemukan.");
            }

        } catch (SQLException e) {
            // Menangani error
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    
    private void deleteDataFromDatabase(int idMobil) {
        // String koneksi ke database MySQL
        String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
        String username = "root";  // Ganti dengan username MySQL
        String password = "";  // Ganti dengan password MySQL

        // Query SQL untuk menghapus data
        String query = "DELETE FROM mobil WHERE id_mobil = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Menyiapkan ID mobil yang akan dihapus
            stmt.setInt(1, idMobil);

            // Menjalankan query untuk menghapus data
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data berhasil dihapus.");
            } else {
                System.out.println("Data gagal dihapus.");
            }

        } catch (SQLException e) {
            // Menangani error
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    private void updateDataInDatabase(int idMobil, String merek, String model, int tahun, String noPolisi, int hargaSewa, String status) {
        // String koneksi ke database MySQL
        String url = "jdbc:mysql://localhost:3306/rental_mobil";  // Ganti dengan nama database
        String username = "root";  // Ganti dengan username MySQL
        String password = "";  // Ganti dengan password MySQL

        // Query SQL untuk memperbarui data
        String query = "UPDATE mobil SET merek = ?, model = ?, tahun = ?, no_polisi = ?, harga_sewa = ?, status = ? WHERE id_mobil = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Menyiapkan data untuk disimpan ke database
            stmt.setString(1, merek);
            stmt.setString(2, model);
            stmt.setInt(3, tahun);
            stmt.setString(4, noPolisi);
            stmt.setInt(5, hargaSewa);
            stmt.setString(6, status);
            stmt.setInt(7, idMobil);

            // Menjalankan query untuk memperbarui data
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data berhasil diperbarui.");
            } else {
                System.out.println("Data gagal diperbarui.");
            }

        } catch (SQLException e) {
            // Menangani error
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    private void initListeners() {
        // Listener untuk jDateChooser1 (Tanggal Peminjaman)
        jDateChooser3.getDateEditor().addPropertyChangeListener("date", evt -> calculateLamaPeminjaman());

        // Listener untuk jDateChooser2 (Tanggal Pengembalian)
        jDateChooser4.getDateEditor().addPropertyChangeListener("date", evt -> calculateLamaPeminjaman());
    }
   
    private void calculateLamaPeminjaman() {
        // Ambil tanggal dari jDateChooser
        java.util.Date tanggalPeminjaman = jDateChooser3.getDate();
        java.util.Date tanggalPengembalian = jDateChooser4.getDate();

        if (tanggalPeminjaman != null && tanggalPengembalian != null) {
            // Hitung selisih dalam milidetik
            long diffInMillis = tanggalPengembalian.getTime() - tanggalPeminjaman.getTime();

            // Konversi milidetik ke hari
            long lamaPeminjaman = diffInMillis / (1000 * 60 * 60 * 24);

            if (lamaPeminjaman >= 0) {
                // Tampilkan lama peminjaman di jTextField12
                jTextField19.setText(String.valueOf(lamaPeminjaman));
            } else {
                // Jika tanggal pengembalian lebih awal dari tanggal peminjaman
                jTextField19.setText("Tanggal tidak valid");
            }
        }  
    }
    
    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(HalamanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalamanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalamanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalamanUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalamanUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private javax.swing.JButton jbdatamobil;
    private javax.swing.JButton jbdatapelanggan;
    private javax.swing.JButton jbkeluar;
    private javax.swing.JButton jblaporan;
    private javax.swing.JPanel jpDatamobil;
    private javax.swing.JPanel jpLaporan;
    private javax.swing.JPanel jpMenu;
    private javax.swing.JPanel jpPenyewaanmobil;
    private javax.swing.JPanel jpframe;
    private javax.swing.JPanel jpheader;
    private javax.swing.JPanel jpisi;
    private javax.swing.JPanel jplanjutpenyewaan;
    // End of variables declaration//GEN-END:variables

   
}
