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
cd bin
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
Masukkan alamat absolut gambar yang ingin dikompresi: C:\Users\Username\Pictures\example.jpg
Metode perhitungan error:
1. Variance
2. Mean Absolute Deviaton (MAD)
3. Max Pixel Difference
4. Entropy
Masukkan metode perhitungan error (1-4): 1
Masukkan target kompresi (dalam desimal antara 0-1, 0 untuk nonaktifkan target): 0.5
Masukkan alamat absolut gambar hasil kompresi: C:\Users\Username\Pictures\outputImage.jpg
Apakah Anda ingin menyimpan output dalam bentuk GIF animasi?
1. Ya
2. Tidak
Masukkan pilihan Anda (1/2): 1
Masukkan path absolut untuk output GIF animasi: C:\Users\Username\Pictures\process.gif

------------------------------------------------------------------------    
                    Mohon tunggu. Sedang diproses...
------------------------------------------------------------------------    

Waktu eksekusi: 28307 ms
Ukuran gambar sebelum kompresi: 161732 bytes
Ukuran gambar setelah kompresi: 80865 bytes
Persentase kompresi: 50.000618306828585%
Kedalaman pohon: 10
Banyak simpul pada pohon: 40177

```

## Contributing
Apabila Anda ingin berkontribusi dalam projek ini, silakan fork repository ini dan gunakan feature branch. Pull requests akan diterima dengan hangat.
