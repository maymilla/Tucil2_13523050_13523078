import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    public static void main(String[] args) {
        // Ganti path gambar dengan file yang ingin diuji
        
        try {
            String imagePath = "C:/Users/Anella Utari/Pictures/Camera Roll/WIN_20250318_10_33_05_Pro.jpg"; // Sesuaikan dengan gambar yang ada
            int threshold = 10; // Nilai ambang batas untuk pembagian blok
            int minBlockSize = 16; // Ukuran minimum blok
            String errorMethod = "MAD"; 

            // Baca gambar
            BufferedImage image = ImageIO.read(new File(imagePath));
            int originalImageSize = image.getWidth() * image.getHeight() * 3;
            
            long startTime = System.nanoTime();
            
            // Bangun QuadTree
            QuadTree quadTree = new QuadTree(image, threshold, minBlockSize, errorMethod);
            
            // Ambil akar pohon
            QuadTreeNode root = quadTree.getRoot();
            
            // Cetak hasil error untuk root
            double error = (errorMethod.equals("MAD"))
            ? MeanAbsoluteDeviation.calculate(image, 0, 0, image.getWidth(), image.getHeight())
            : Entropy.calculate(image, 0, 0, image.getWidth(), image.getHeight());
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // Waktu dalam nanodetik
            System.out.println("Waktu eksekusi: " + duration + " ms");
            
            System.out.println("Ukuran gambar sebelum: " + image.getWidth() + "x" + image.getHeight());

            System.out.println("Metode Error: " + errorMethod);
            System.out.println("Nilai Error: " + error);
            
            int compressedWidth = quadTree.getRoot().width;
            int compressedHeight = quadTree.getRoot().height;
            System.out.println("Ukuran gambar setelah: " + compressedWidth + "x" + compressedHeight);
            
            // Simpan hasil gambar
            String outputPath = "C:/Users/Anella Utari/Documents/GitHub/Tucil2_13523050_13523078/test/image_compressed.jpg";
            BufferedImage compressedImage = quadTree.toBufferedImage();
            ImageIO.write(compressedImage, "jpg", new File(outputPath));
            File compressedFile = new File(outputPath);
            if (!compressedFile.exists()) {
                compressedFile.mkdirs(); // Buat folder jika belum ada
            }
            int compressedImageSize = quadTree.getTotalBlocks() * minBlockSize * minBlockSize * 3;
            compressedImageSize = Math.min(compressedImageSize, originalImageSize);
            
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
