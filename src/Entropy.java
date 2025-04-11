import java.util.HashMap;
import java.util.Map;

public class Entropy {
    public static double calculate(int[][][] rgbImage, int x, int y, int width, int height) {
        return (calculateEntropy(rgbImage, x, y, width, height, 0) +  // Red
                calculateEntropy(rgbImage, x, y, width, height, 1) +  // Green
                calculateEntropy(rgbImage, x, y, width, height, 2))  // Blue
                / 3.0;
    }

    private static double calculateEntropy(int[][][] rgbImage, int x, int y, int width, int height, int channel) {
        Map<Integer, Integer> histogram = new HashMap<>();
        int totalPixels = width * height;

        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                int value = rgbImage[i][j][channel];
                histogram.put(value, histogram.getOrDefault(value, 0) + 1);
            }
        }

        double entropy = 0;
        for (int count : histogram.values()) {
            double probability = count / (double) totalPixels;
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }
}
