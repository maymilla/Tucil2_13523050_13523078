import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ImageProcessor {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // inisialisasi
        String imagePath = "";
        String outputPath = "";
        int errorMethod = -1;
        int threshold = -1;
        int minBlockSize = -1;
        
        try {
            while (true) {
                System.out.print("Masukkan alamat absolut gambar: ");
                imagePath = input.nextLine().trim();
                if (imagePath.isEmpty()) {
                    System.out.println("Path tidak boleh kosong.");
                    continue;
                }
                File f = new File(imagePath);
                if (!f.exists()) {
                    System.out.println("File tidak ditemukan. Coba lagi.");
                    continue;
                }
                break;
            }
            System.out.println("Metode perhitungan error:");
            System.out.println("1. Variance");
            System.out.println("2. Mean Absolute Deviaton (MAD)");
            System.out.println("3. Max Pixel Difference");
            System.out.println("4. Entropy");
            while (true) {
                System.out.print("Masukkan metode perhitungan error (1-4): ");
                String inputLine = input.nextLine().trim();
            
                // Cek kalau kosong
                if (inputLine.isEmpty()) {
                    System.out.println("Input tidak boleh kosong. Masukkan angka 1-4.");
                    continue;
                }
            
                try {
                    errorMethod = Integer.parseInt(inputLine);
                    if (errorMethod >= 1 && errorMethod <= 4) {
                        break; // valid, keluar loop
                    } else {
                        System.out.println("Input metode tidak valid. Masukkan angka 1-4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input harus berupa angka. Masukkan angka 1-4.");
                }
            }            

            
            while (true) {
                if (errorMethod == 1) 
                    System.out.print("Masukkan ambang batas (0 - 65025): ");
                else if (errorMethod == 2 || errorMethod == 3) 
                    System.out.print("Masukkan ambang batas (0 - 255): ");
                else 
                    System.out.print("Masukkan ambang batas (0 - 8): ");

                String inputLine = input.nextLine().trim();
            
                if (inputLine.isEmpty()) {
                    System.out.println("Input tidak boleh kosong.");
                    continue;
                }
            
                try {
                    threshold = Integer.parseInt(inputLine);
                    boolean valid = switch (errorMethod) {
                        case 1 -> threshold >= 0 && threshold <= 65025;
                        case 2, 3 -> threshold >= 0 && threshold <= 255;
                        case 4 -> threshold >= 0 && threshold <= 8;
                        default -> false;
                    };
                    if (valid) {
                        break;
                    } else {
                        System.out.println("Ambang batas tidak valid untuk metode error yang dipilih.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input harus berupa angka. Silakan coba lagi.");
                }
            }
    
            while (true) {
                System.out.print("Masukkan ukuran minimum blok: ");
                String inputLine = input.nextLine().trim();
            
                if (inputLine.isEmpty()) {
                    System.out.println("Input tidak boleh kosong.");
                    continue;
                }
            
                try {
                    minBlockSize = Integer.parseInt(inputLine);
                    if (minBlockSize > 0) {
                        break;
                    } else {
                        System.out.println("Ukuran blok harus lebih dari 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input harus berupa angka. Silakan coba lagi.");
                }
            }
            
            while (true) {
                System.out.print("Masukkan alamat absolut gambar hasil kompresi: ");
                outputPath = input.nextLine().trim();

                if (outputPath.isEmpty()) {
                    System.out.println("Path tidak boleh kosong.");
                    continue;
                }

                if (outputPath.equals(imagePath)) {
                    System.out.println("Path output tidak boleh sama dengan input!");
                    continue;
                }

                // Cek apakah path diakhiri dengan ekstensi file gambar yang valid
                if (!(outputPath.endsWith(".jpg") || outputPath.endsWith(".jpeg") || outputPath.endsWith(".png"))) {
                    System.out.println("Output harus berupa file gambar (.jpg, .jpeg, .png).");
                    continue;
                }

                break;
            }

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
            
            long startTime = System.nanoTime();
            
            // Bangun QuadTree
            Quadtree quadtree = new Quadtree(imageArray, 0, 0, width, height, 0, minBlockSize, threshold, errorMethod);
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // Waktu dalam nanodetik
            System.out.println("Waktu eksekusi: " + duration + " ms");
            
            // Simpan hasil gambar
            quadtree.saveCompressedImage(width, height, outputPath);
            
            File outputFile = new File(outputPath);

            long originalSize = imageFile.length();
            System.out.println("Ukuran gambar sebelum: " + originalSize + " bytes");
            
            long compressedSize = outputFile.length();
            System.out.println("Ukuran gambar setelah: " + compressedSize + " bytes");
            
            // Hitung persentase kompresi
            double compressionPercentage = ((1 - (double) compressedSize / originalSize) * 100);
            System.out.println("Persentase kompresi: " + compressionPercentage + "%");

            int depthtree = quadtree.getDepth();
            System.out.println("Kedalaman pohon: " + depthtree);

            int totalnodes = quadtree.getNodeCount();
            System.out.println("Banyak simpul pada pohon: " + totalnodes);
            
        } catch (IOException e) {
            System.out.println("Gagal membaca gambar: " + e.getMessage());
        }
    }
}
