# Tugas Kecil 2 IF2211 Strategi Algoritma
Program kompresi gambar menggunakan Java.


<div id="contributor">
  <strong>
    <h3>Dibuat oleh: </h3>
    <table align="center">
      <tr>
        <td>NIM</td>
        <td>Nama</td>
      </tr>
      <tr>
        <td>13523050</td>
        <td>Mayla Yaffa Ludmilla</td>
      </tr>
      <tr>
        <td>13523078</td>
        <td>Anella Utari Gunadi</td>
      </tr>
    </table>
  </strong>
</div>

## Features
Perhitungan error menggunakan
* Variance
* Mean Absolute Deviaton
* Max Pixel Difference
* Entropy

## Technologies Used
Java

## Installing / Getting started
Anda perlu menginstall Java JDK 17 atau versi lebih baru.

## Cara Compile
1. Buka terminal
2. Navigasi ke folder src
3. Jalankan perintah berikut untuk compile semua file:
```
javac -d bin src/*.java
```

## Cara Menjalankan Program
1. Ketikan command di terminal
```
   java -cp bin ImageProcessor
```
3. Program akan berjalan di terminal, dan Anda akan diminta memasukkan path input absolut, metode perhitungan error, target kompresi (opsional), threshold, minimum block size, path output gambar absolut, dan path output gif absolut.
4. Setelah program berjalan, akan ada hasil berupa File gambar hasil kompresi (JPG/PNG), file GIF animasi proses kompresi dari awal hingga akhir, serta beberapa informasi tambahan di terminal:
- Waktu eksekusi
- Ukuran file sebelum & sesudah kompresi
- Persentase kompresi
- Kedalaman pohon Quadtree
- Jumlah simpul (node) pada pohon

## Contoh Eksekusi
```
Masukkan alamat absolut gambar yang ingin dikompresi:
C:\Users\Username\Pictures\example.jpg

Metode perhitungan error:
1. Variance
2. Mean Absolute Deviaton (MAD)
3. Max Pixel Difference
4. Entropy
Masukkan metode perhitungan error (1-4): 1

Masukkan target kompresi (dalam desimal antara 0-1, 0 untuk nonaktifkan target): 0

Masukkan ambang batas (0 - 65025): 5000

Masukkan ukuran minimum blok: 8

Masukkan alamat absolut gambar hasil kompresi:
C:\Users\Username\Pictures\output.jpg

Masukkan path absolut untuk output GIF animasi:
C:\Users\Username\Pictures\process.gif

Waktu eksekusi: 120 ms
Ukuran gambar sebelum: 204800 bytes
Ukuran gambar setelah: 51200 bytes
Persentase kompresi: 75.0%
Kedalaman pohon: 6
Banyak simpul pada pohon: 456

```

## Contributing
Apabila Anda ingin berkontribusi dalam projek ini, silakan fork repository ini dan gunakan feature branch. Pull requests akan diterima dengan hangat.
