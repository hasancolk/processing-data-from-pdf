-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 21 Ara 2021, 21:41:04
-- Sunucu sürümü: 10.4.20-MariaDB
-- PHP Sürümü: 8.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `yazlab3`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `ad` text COLLATE utf8_turkish_ci NOT NULL,
  `sifre` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `admin`
--

INSERT INTO `admin` (`id`, `ad`, `sifre`) VALUES
(1, 'admin', 'admin');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `anahtar_kelime`
--

CREATE TABLE `anahtar_kelime` (
  `id` int(11) NOT NULL,
  `kelime` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kullanici`
--

CREATE TABLE `kullanici` (
  `id` int(11) NOT NULL,
  `kullaniciAd` text COLLATE utf8_turkish_ci NOT NULL,
  `sifre` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `kullanici`
--

INSERT INTO `kullanici` (`id`, `kullaniciAd`, `sifre`) VALUES
(12, 'teyfik', '1234'),
(13, 'hasan', '1234');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `mentor`
--

CREATE TABLE `mentor` (
  `id` int(11) NOT NULL,
  `ad` text COLLATE utf8_turkish_ci NOT NULL,
  `soyad` text COLLATE utf8_turkish_ci NOT NULL,
  `unvan` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `proje`
--

CREATE TABLE `proje` (
  `id` int(11) NOT NULL,
  `kullaniciId` int(11) NOT NULL,
  `ad` text COLLATE utf8_turkish_ci NOT NULL,
  `teslimDonemi` text COLLATE utf8_turkish_ci NOT NULL,
  `ozet` text COLLATE utf8_turkish_ci NOT NULL,
  `dersAd` text COLLATE utf8_turkish_ci NOT NULL,
  `pdfAdres` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `proje_anahtar_kelime`
--

CREATE TABLE `proje_anahtar_kelime` (
  `projeId` int(11) NOT NULL,
  `anahtarKelimeId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `proje_mentorleri`
--

CREATE TABLE `proje_mentorleri` (
  `projeId` int(11) NOT NULL,
  `mentorId` int(11) NOT NULL,
  `mentorTur` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `proje_yazarlari`
--

CREATE TABLE `proje_yazarlari` (
  `projeId` int(11) NOT NULL,
  `yazarId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `yazar`
--

CREATE TABLE `yazar` (
  `id` int(11) NOT NULL,
  `ad` text COLLATE utf8_turkish_ci NOT NULL,
  `soyad` text COLLATE utf8_turkish_ci NOT NULL,
  `ogrenciNo` text COLLATE utf8_turkish_ci NOT NULL,
  `ogretimTur` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `anahtar_kelime`
--
ALTER TABLE `anahtar_kelime`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `kullanici`
--
ALTER TABLE `kullanici`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `mentor`
--
ALTER TABLE `mentor`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `proje`
--
ALTER TABLE `proje`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kullaniciId` (`kullaniciId`);

--
-- Tablo için indeksler `proje_anahtar_kelime`
--
ALTER TABLE `proje_anahtar_kelime`
  ADD UNIQUE KEY `projeId` (`projeId`,`anahtarKelimeId`),
  ADD KEY `anahtarKelimeId` (`anahtarKelimeId`);

--
-- Tablo için indeksler `proje_mentorleri`
--
ALTER TABLE `proje_mentorleri`
  ADD UNIQUE KEY `projeId` (`projeId`,`mentorId`),
  ADD KEY `mentorId` (`mentorId`);

--
-- Tablo için indeksler `proje_yazarlari`
--
ALTER TABLE `proje_yazarlari`
  ADD UNIQUE KEY `projeId` (`projeId`,`yazarId`),
  ADD KEY `yazarId` (`yazarId`);

--
-- Tablo için indeksler `yazar`
--
ALTER TABLE `yazar`
  ADD PRIMARY KEY (`id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `anahtar_kelime`
--
ALTER TABLE `anahtar_kelime`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- Tablo için AUTO_INCREMENT değeri `kullanici`
--
ALTER TABLE `kullanici`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Tablo için AUTO_INCREMENT değeri `mentor`
--
ALTER TABLE `mentor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- Tablo için AUTO_INCREMENT değeri `proje`
--
ALTER TABLE `proje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Tablo için AUTO_INCREMENT değeri `yazar`
--
ALTER TABLE `yazar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `proje`
--
ALTER TABLE `proje`
  ADD CONSTRAINT `proje_ibfk_3` FOREIGN KEY (`kullaniciId`) REFERENCES `kullanici` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Tablo kısıtlamaları `proje_anahtar_kelime`
--
ALTER TABLE `proje_anahtar_kelime`
  ADD CONSTRAINT `proje_anahtar_kelime_ibfk_1` FOREIGN KEY (`projeId`) REFERENCES `proje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proje_anahtar_kelime_ibfk_2` FOREIGN KEY (`anahtarKelimeId`) REFERENCES `anahtar_kelime` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Tablo kısıtlamaları `proje_mentorleri`
--
ALTER TABLE `proje_mentorleri`
  ADD CONSTRAINT `proje_mentorleri_ibfk_1` FOREIGN KEY (`projeId`) REFERENCES `proje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proje_mentorleri_ibfk_2` FOREIGN KEY (`mentorId`) REFERENCES `mentor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Tablo kısıtlamaları `proje_yazarlari`
--
ALTER TABLE `proje_yazarlari`
  ADD CONSTRAINT `proje_yazarlari_ibfk_1` FOREIGN KEY (`projeId`) REFERENCES `proje` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `proje_yazarlari_ibfk_2` FOREIGN KEY (`yazarId`) REFERENCES `yazar` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
