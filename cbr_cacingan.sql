-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 27, 2021 at 02:59 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 7.4.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cbr_cacingan`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_admin`
--

CREATE TABLE `tb_admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  `jk` varchar(1) NOT NULL,
  `tgl_lahir` date NOT NULL,
  `alamat` text NOT NULL,
  `nohp` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_admin`
--

INSERT INTO `tb_admin` (`id_admin`, `nama`, `username`, `password`, `jk`, `tgl_lahir`, `alamat`, `nohp`) VALUES
(1, 'Administrator', 'admin', 'admin', 'P', '1997-06-03', 'Solok Kota', '082288228822');

-- --------------------------------------------------------

--
-- Table structure for table `tb_gejala`
--

CREATE TABLE `tb_gejala` (
  `id_gejala` int(11) NOT NULL,
  `kode_gejala` varchar(15) NOT NULL,
  `nama_gejala` text NOT NULL,
  `bobot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_gejala`
--

INSERT INTO `tb_gejala` (`id_gejala`, `kode_gejala`, `nama_gejala`, `bobot`) VALUES
(36, 'G01', 'Nafsu makan berkurang', 5),
(37, 'G02', 'Nyeri perut', 1),
(38, 'G03', 'Tidak bergairah', 1),
(39, 'G04', 'Demam', 1),
(40, 'G05', 'Diare', 3),
(41, 'G06', 'Kurang konsentrasi', 5),
(42, 'G07', 'Rasa mual', 3),
(43, 'G08', 'Badan terlihat lesu', 3),
(44, 'G09', 'Sering batuk', 1),
(45, 'G10', 'Anemia', 5),
(46, 'G11', 'Suara serak', 1),
(47, 'G12', 'Rentan terhadap penyakit', 1),
(48, 'G13', 'Muntah', 3),
(49, 'G14', 'Iritasi faringeal', 1),
(50, 'G15', 'Berat badan menurun', 3),
(51, 'G16', 'Disentri (BAB disertai darah atau berlendir)', 1),
(52, 'G17', 'Terdapat cacing pada feses', 5),
(53, 'G18', 'Gatal disekitar anus', 5),
(54, 'G19', 'Sulit tidur', 1),
(55, 'G20', 'Perut buncit', 3);

-- --------------------------------------------------------

--
-- Table structure for table `tb_konsultasi`
--

CREATE TABLE `tb_konsultasi` (
  `id_konsultasi` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `jawaban_user` text NOT NULL,
  `kode_penyakit` varchar(50) NOT NULL,
  `hasil` double NOT NULL,
  `tgl_konsul` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_konsultasi`
--

INSERT INTO `tb_konsultasi` (`id_konsultasi`, `id_user`, `jawaban_user`, `kode_penyakit`, `hasil`, `tgl_konsul`) VALUES
(4, 3, 'G05,G08,G12,G15,G17,G18,G20', 'P03', 0.65, '2021-01-26 22:48:23');

-- --------------------------------------------------------

--
-- Table structure for table `tb_penyakit`
--

CREATE TABLE `tb_penyakit` (
  `id_penyakit` int(15) NOT NULL,
  `kode_penyakit` varchar(50) NOT NULL,
  `nama_penyakit` varchar(50) NOT NULL,
  `ket` text DEFAULT NULL,
  `pencegahan` text DEFAULT NULL,
  `solusi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_penyakit`
--

INSERT INTO `tb_penyakit` (`id_penyakit`, `kode_penyakit`, `nama_penyakit`, `ket`, `pencegahan`, `solusi`) VALUES
(1, 'P01', 'Cacing Gelang', 'Cacing jantan mempunyai panjang 10-30 cm sedangkan cacing betina 22-35 cm. Cacing betina dapat bertelur 100 000 – 200 000 butir sehari, terdiri atas telur dibuahi dan telur tidak buahi. Di tanah yang sesuai,telur yang dibuahi tumbuh menjadi bentuk infektif dalam waktu kurang lebih tiga minggu.', '1. Biasakan untuk selalu hidup bersih, seperti cuci tangan sebelum makan dan sesudah buang air besar dengan sabun.\r\n<br>\r\n2. Potonglah kuku secara berkala agar tetap pendek dan bersih.\r\n<br>\r\n3. Biasakan anak untuk selalu menggunakan sandal atau sepatu bila keluar rumah, terutama bila berjalan di tanah. Tanah terutama yang lembab, merupakan tempat favorit cacing untuk berkembang biak.<br>', '1.Penanganan untuk mengatasi infeksi cacing dengan obat-obatan merupakan pilihan yagn dianjurkan. Obat anti cacingan golongan Pirantel Pamoat (Combatrin, dll) merupakan anti cacing yang efektif untuk mengatasi sebagian besar infeksi yang disebabkan oleh parasit cacing.<br>\r\n2.Vitamin penambah nafsu makan untuk balita<br>\r\n3.Obat batuk untuk balita<br>\r\n4.Menggunakan obat Albendazole yang berfungsi untuk memutuskan rantai kehidupan cacing : cacing dewasa, telur, dan larva.<br>'),
(2, 'P02', 'Cacing Tambang', 'Cacing betina berukuran panjang ± 1 cm, cacing jantan ± 0,8 cm. Bentuk badan N. Americanus biasanya menyerupai huruf S, sedangkan A. Duodenale menyerupai huruf C. N. Americanus tiap hari bertelur 5000-10 000 butir, sedangkan A. Duodenale 10 000-25 000 butir. Telur akan menetas dalam waktu 1-2 hari.', '1.Biasakan untuk selalu hidup bersih, seperti cuci tangan sebelum makan dan sesudah buang air besar dengan sabun.<br>\r\n2.Potonglah kuku secara berkala agar tetap pendek dan bersih.<br>\r\n3.Biasakan anak untuk selalu menggunakan sandal atau sepatu bila keluar rumah, terutama bila berjalan di tanah. Tanah terutama yang lembab, merupakan tempat favorit cacing untuk berkembang biak.<br>', '1.Antibiotik untuk meningkatkan kekebalan tubuh atau imunitas pada anak.<br>\r\n2.Pemberian obat batuk.<br>\r\n3.Menggunakan obat Albendazole yang berfungsi untuk memutuskan rantai kehidupan cacing : cacing dewasa, telur, dan larva.<br>\r\n'),
(3, 'P03', 'Cacing Cambuk', 'Cacing betina panjangnya ± 5 cm, sedangkan cacing jantan  ± 4 cm. Bagian posterior bentuknya lebih gemuk; pada cacing betina bulat tumpul sedangkan cacing jantan melingkar dan terdapat satu spikulum. Seekor cacing betina diperkirakan menghasilkan telur setiap hari sebanyak 3000 – 10 000 butir. Telur tersebut menjadi matang dalam waktu 3 sampai 6 minggu dalam lingkungan yang sesuai', '1.Biasakan untuk selalu hidup bersih, seperti cuci tangan sebelum makan dan sesudah buang air besar dengan sabun.<br>\r\n2.Potonglah kuku secara berkala agar tetap pendek dan bersih.<br>\r\n3.Jangan lupa memilih dan mengolah makanan dengan bersih. Misalnya mencuci sayuran dengan air yang mengalir.<br>\r\n4.Tidak membiarkan makanan dihinggapi lalat karena biasanya lalat mampu membawa telur-telur cacing.<br>', '1.Penanganan untuk mengatasi infeksi cacing dengan obat-obatan merupakan pilihan yagn dianjurkan. Obat anti cacingan golongan Pirantel Pamoat (Combatrin, dll) merupakan anti cacing yang efektif untuk mengatasi sebagian besar infeksi yang disebabkan oleh parasit cacing.<br>\r\n2.Vitamin penambah nafsu makan untuk balita.<br>\r\n3.Obat diare untuk balita.<br>\r\n4.Obat disentri untuk balita.<br>\r\n5.Menggunakan obat Albendazole yang berfungsi untuk memutuskan rantai kehidupan cacing : cacing dewasa, telur, dan larva.<br>\r\n'),
(4, 'P04', 'Cacing Kremi', 'Cacing kremi memiliki bentuk lonjong tetapi asimetris, dimana satu sisi berbentuk datar, satu sisi lawannya cembung. Panjang E. Vermicularis betina adalah 8-13 mm, sedangkan panjang tubuh cacing jantan adalah 2-5 mm. Ada 5.000-17.000 telur yang dikeluarkan induk cacing betina.', '1.Jangan lupa memilih dan mengolah makanan dengan bersih. Misalnya mencuci sayuran dengan air yang mengalir.<br>\r\n2.Rajin mengganti pakaian dalam dan sprei.<br>\r\n3.Jangan menggaruk anus ketika gatal.<br>\r\n4.Tidak membiarkan makanan dihinggapi lalat karena biasanya lalat mampu membawa telur-telur cacing.<br>\r\n5.Bila ingin makan sayuran mentah (lalapan) atau buah-buahan, cucilah dengan air bersih yang mengalir.<br>\r\n6.Biasakan anak untuk tidak jajan jenis makanan di tempat sembarangan.<br>', '1.Penanganan untuk mengatasi infeksi cacing dengan obat-obatan merupakan pilihan yang dianjurkan. Obat anti cacingan golongan Pirantel Pamoat (Combatrin).<br>\r\n2.Vitamin penambah nafsu makan.<br>\r\n3.Menggunakan obat Albendazole yang berfungsi untuk memutuskan rantai kehidupan cacing : cacing dewasa.<br>\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `tb_rule`
--

CREATE TABLE `tb_rule` (
  `id_rule` int(11) NOT NULL,
  `kode_rule` varchar(5) NOT NULL,
  `kode_penyakit` varchar(5) NOT NULL,
  `kode_gejala` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_rule`
--

INSERT INTO `tb_rule` (`id_rule`, `kode_rule`, `kode_penyakit`, `kode_gejala`) VALUES
(1, 'R01', 'P01', 'G01,G02,G03,G04,G05,G06,G07,G08,G09'),
(2, 'R02', 'P02', 'G07,G08,G09,G10,G11,G12,G13,G14'),
(3, 'R03', 'P03', 'G05,G10,G15,G16,G17'),
(4, 'R04', 'P04', 'G01,G02,G18,G19,G20');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id_user` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jk` varchar(1) NOT NULL,
  `tgl_lahir` date NOT NULL DEFAULT '0000-00-00',
  `alamat` text NOT NULL,
  `nohp` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id_user`, `username`, `password`, `nama`, `jk`, `tgl_lahir`, `alamat`, `nohp`) VALUES
(3, 'shisuka', 'shisuka', 'Shisuka', 'P', '1999-01-20', 'Solok Kota', '082288220930');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_admin`
--
ALTER TABLE `tb_admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `tb_gejala`
--
ALTER TABLE `tb_gejala`
  ADD PRIMARY KEY (`id_gejala`);

--
-- Indexes for table `tb_konsultasi`
--
ALTER TABLE `tb_konsultasi`
  ADD PRIMARY KEY (`id_konsultasi`);

--
-- Indexes for table `tb_penyakit`
--
ALTER TABLE `tb_penyakit`
  ADD PRIMARY KEY (`id_penyakit`);

--
-- Indexes for table `tb_rule`
--
ALTER TABLE `tb_rule`
  ADD PRIMARY KEY (`id_rule`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_admin`
--
ALTER TABLE `tb_admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tb_gejala`
--
ALTER TABLE `tb_gejala`
  MODIFY `id_gejala` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `tb_konsultasi`
--
ALTER TABLE `tb_konsultasi`
  MODIFY `id_konsultasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tb_penyakit`
--
ALTER TABLE `tb_penyakit`
  MODIFY `id_penyakit` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tb_rule`
--
ALTER TABLE `tb_rule`
  MODIFY `id_rule` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
