import java.io.File;

public class CompressionResult {
    public double threshold;
    public int minBlockSize;

    public CompressionResult(double threshold, int minBlockSize) {
        this.threshold = threshold;
        this.minBlockSize = minBlockSize;
    }

    public static CompressionResult CompressionTarget(int[][][] imageArray, int width, int height, double targetCompression, int errorMethod, long originalSize) {
        double bestThreshold = 0;
        int bestBlockSize = 1;
        double bestCompression = 0;
        long bestSize = Long.MAX_VALUE;

        double maxThreshold = getMaxThreshold(errorMethod);
        double tolerance = 0.001;

        for (int blockSize : new int[]{1, 4, 8, 16, 32}) {
            double low = 0;
            double high = maxThreshold;

            while (low <= high) {
                double mid = (low + high) / 2.0;

                Quadtree qt = new Quadtree(imageArray, 0, 0, width, height, 0, blockSize, mid, errorMethod);
                qt.saveImage(width, height, "temp_compress.jpg");

                File temp = new File("temp_compress.jpg");
                long size = temp.length();
                double compression = 1 - ((double) size / originalSize);

                if (compression >= targetCompression) {
                    if (Math.abs(compression - targetCompression) < Math.abs(bestCompression - targetCompression)) {
                        bestThreshold = mid;
                        bestBlockSize = blockSize;
                        bestCompression = compression;
                        bestSize = size;
                    }
                    high = mid - tolerance;
                } else {
                    low = mid + tolerance;
                }
                temp.delete(); 
            }
        }
        return new CompressionResult(bestThreshold, bestBlockSize);
    }

    public static double getMaxThreshold(int method) {
        return switch (method) {
            case 1 -> 65025.0;
            case 2, 3 -> 255.0;
            case 4 -> 8.0;
            default -> 255.0;
        };
    }
}