import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MeanAbsoluteDeviation {
    public static double calculate(BufferedImage image, int x, int y, int width, int height) {
        return (calculateMAD(image, x, y, width, height, 16) +  // Red
                calculateMAD(image, x, y, width, height, 8) +   // Green
                calculateMAD(image, x, y, width, height, 0))    // Blue
                / 3.0; // Rata-rata MAD RGB
    }

    private static double calculateMAD(BufferedImage image, int x, int y, int width, int height, int shift) {
        int sum = 0, count = 0;
        
        // Hitung rata-rata warna
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getRGB(i, j);
                sum += (rgb >> shift) & 0xFF;
                count++;
            }
        }
        double mean = sum / (double) count;
        
        // Hitung Mean Absolute Deviation (MAD)
        double mad = 0;
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getRGB(i, j);
                mad += Math.abs(((rgb >> shift) & 0xFF) - mean);
            }
        }
        return mad / count;
    }
}