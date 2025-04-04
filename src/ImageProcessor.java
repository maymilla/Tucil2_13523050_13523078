import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ImageProcessor {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        try {
            String imagePath = "C:/Users/Anella Utari/Pictures/Camera Roll/WIN_20250318_10_33_05_Pro.jpg";
            System.out.println("Metode perhitungan error:");
            System.out.println("1. Mean Absolute Deviaton (MAD)");
            System.out.println("2. Entropy");
            System.out.print("Masukkan metode perhitungan error: ");
            int errorMethod = input.nextInt();
            while (errorMethod != 1 && errorMethod != 2){
                System.out.println("Input metode tidak valid. Silakan coba lagi.");
                System.out.print("Masukkan metode perhitungan error: ");
                errorMethod = input.nextInt();
            }
            System.out.print("Masukkan ambang batas (threshold): ");
            int threshold = input.nextInt();
            System.out.print("Masukkan ukuran minimum blok: ");
            int minBlockSize = input.nextInt();

            // Baca gambar
            BufferedImage image = ImageIO.read(new File(imagePath));
            int originalImageSize = image.getWidth() * image.getHeight() * 3;
            
            long startTime = System.nanoTime();
            
            // Bangun QuadTree
            QuadTree quadTree = new QuadTree(image, threshold, minBlockSize, errorMethod);
            
            // Ambil akar pohon
            QuadTreeNode root = quadTree.getRoot();
            
            // Cetak hasil error untuk root
            double error = 0;
            
            if (errorMethod == 1){
                error = MeanAbsoluteDeviation.calculate(image, 0, 0, image.getWidth(), image.getHeight());
            } else if (errorMethod == 2){
                Entropy.calculate(image, 0, 0, image.getWidth(), image.getHeight());
            }
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // Waktu dalam nanodetik
            System.out.println("Waktu eksekusi: " + duration + " ms");
            
            // Simpan hasil gambar
            String outputPath = "C:/Users/Anella Utari/Documents/GitHub/Tucil2_13523050_13523078/test/output/image_compressed.jpg";
            BufferedImage compressedImage = quadTree.toBufferedImage();
            ImageIO.write(compressedImage, "jpg", new File(outputPath));
            File compressedFile = new File(outputPath);

            int compressedImageSize = quadTree.getTotalBlocks() * minBlockSize * minBlockSize * 3;
            compressedImageSize = Math.min(compressedImageSize, originalImageSize);

            System.out.println("Ukuran gambar sebelum: " + originalImageSize + " bytes");

            //System.out.println("Nilai Error: " + error);

            System.out.println("Ukuran gambar setelah: " + compressedImageSize + " bytes");
            
            // Hitung persentase kompresi
            double compressionPercentage = ((1 - (double) compressedImageSize / originalImageSize) * 100);
            System.out.println("Persentase kompresi: " + compressionPercentage + "%");

            int depthtree = quadTree.getDepth(root);
            System.out.println("kedalaman pohon: " + depthtree);

            int totalnodes = quadTree.countNodes(root);
            System.out.println("Banyak simpul pada pohon: " + totalnodes);
            
        } catch (IOException e) {
            System.out.println("Gagal membaca gambar: " + e.getMessage());
        }
    }
}
