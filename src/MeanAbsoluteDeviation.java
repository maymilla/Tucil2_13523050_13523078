public class MeanAbsoluteDeviation {
    public static double calculate(int[][][] rgbImage, int x, int y, int width, int height) {
        return (calculateMAD(rgbImage, x, y, width, height, 0) +  // Red
                calculateMAD(rgbImage, x, y, width, height, 1) +  // Green
                calculateMAD(rgbImage, x, y, width, height, 2))   // Blue
                / 3.0;
    }

    private static double calculateMAD(int[][][] rgbImage, int x, int y, int width, int height, int channel) {
        int sum = 0, count = 0;
        
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                sum += rgbImage[i][j][channel];
                count++;
            }
        }

        double mean = sum / (double) count;

        double mad = 0;
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                mad += Math.abs(rgbImage[i][j][channel] - mean);
            }
        }

        return mad / count;
    }
}
