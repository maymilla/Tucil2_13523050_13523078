import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Entropy {
    public static double calculate(BufferedImage image, int x, int y, int width, int height) {
        return (calculateEntropy(image, x, y, width, height, 16) + // Red
                calculateEntropy(image, x, y, width, height, 8) +  // Green
                calculateEntropy(image, x, y, width, height, 0))  // Blue
                / 3.0; // Rata-rata Entropy RGB
    }

    private static double calculateEntropy(BufferedImage image, int x, int y, int width, int height, int shift) {
        Map<Integer, Integer> histogram = new HashMap<>();
        int totalPixels = width * height;
        
        // Hitung histogram
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getRGB(i, j);
                int value = (rgb >> shift) & 0xFF;
                histogram.put(value, histogram.getOrDefault(value, 0) + 1);
            }
        }
        
        // Hitung entropy
        double entropy = 0;
        for (int count : histogram.values()) {
            double probability = count / (double) totalPixels;
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }
}
