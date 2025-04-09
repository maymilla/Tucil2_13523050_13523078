import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ImageProcessor {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        try {
            String imagePath = "C:/Users/Mayla/Documents/Kuliah/Semester-4/Strategi-Algoritma/Tucil2_13523050_13523078/test/input/kikuk.jpg";
            System.out.println("Metode perhitungan error:");
            System.out.println("1. Mean Absolute Deviaton (MAD)");
            System.out.println("2. Entropy");
            System.out.print("Masukkan metode perhitungan error: ");
            int errorMethod = input.nextInt();
            while (errorMethod < 1 || errorMethod > 4){
                System.out.println("Input metode tidak valid. Silakan coba lagi.");
                System.out.print("Masukkan metode perhitungan error: ");
                errorMethod = input.nextInt();
            }
            System.out.print("Masukkan ambang batas (threshold): ");
            int threshold = input.nextInt();
            System.out.print("Masukkan ukuran minimum blok: ");
            int minBlockSize = input.nextInt();

            // Baca gambar
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            int width = image.getWidth();
            int height = image.getHeight();
            int[][][] imageArray = new int[height][width][3];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    imageArray[y][x][0] = color.getRed();
                    imageArray[y][x][1] = color.getGreen();
                    imageArray[y][x][2] = color.getBlue();
                }
            }
            
            long originalSize = imageFile.length();
            System.out.println("Ukuran gambar sebelum: " + originalSize + " bytes");

            long startTime = System.nanoTime();
            
            // Bangun QuadTree
            Quadtree quadtree = new Quadtree(imageArray, 0, 0, width, height, 0, 10, 100, errorMethod);
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // Waktu dalam nanodetik
            System.out.println("Waktu eksekusi: " + duration + " ms");
            
            // Simpan hasil gambar
            String outputPath = "test/output/test3.jpg";
            quadtree.saveCompressedImage(width, height, outputPath);

            File outputFile = new File(outputPath);
            long compressedSize = outputFile.length();
            System.out.println("Ukuran gambar setelah: " + compressedSize + " bytes");
            
            // Hitung persentase kompresi
            double compressionPercentage = ((1 - (double) compressedSize / originalSize) * 100);
            System.out.println("Persentase kompresi: " + compressionPercentage + "%");

            int depthtree = quadtree.getDepth();
            System.out.println("kedalaman pohon: " + depthtree);

            int totalnodes = quadtree.getNodeCount();
            System.out.println("Banyak simpul pada pohon: " + totalnodes);
            
        } catch (IOException e) {
            System.out.println("Gagal membaca gambar: " + e.getMessage());
        }
    }
}
