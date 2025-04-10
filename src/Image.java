import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;



public class Image {
    public static void main(String[] args) {
        try {
            // Record start time for execution time
            long startTime = System.currentTimeMillis();

            // Load the image
            File inputFile = new File("C:\\\\Users\\\\Mayla\\\\Documents\\\\Kuliah\\\\Semester-4\\\\Strategi-Algoritma\\\\Tucil2_13523050_13523078\\\\test\\\\input\\\\kikuk.jpg");
            BufferedImage image = ImageIO.read(inputFile);
            int width = image.getWidth();
            int height = image.getHeight();
            int[][][] imageArray = new int[height][width][3]; // Store R, G, B separately

            // Store imageArray in 3D array (RGB)
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    imageArray[y][x][0] = color.getRed();
                    imageArray[y][x][1] = color.getGreen();
                    imageArray[y][x][2] = color.getBlue();
                }
            }

            // Output image size before compression
            long originalSize = inputFile.length();
            System.out.println("Ukuran gambar sebelum: " + originalSize + " bytes");

            // Build Quadtree
            Quadtree quadtree = new Quadtree(imageArray, 0, 0, width, height, 0, 10, 100, 1);

            // Save compressed image
            String outputFilePath = "test/output/kikuk_variance.jpg";
            quadtree.saveCompressedImage(width, height, outputFilePath);

            // Get the size of the compressed image
            File outputFile = new File(outputFilePath);
            long compressedSize = outputFile.length();
            System.out.println("Ukuran gambar setelah: " + compressedSize + " bytes");

            // Compression percentage
            double compressionPercentage = 100.0 * (1 - (double) compressedSize / originalSize);
            System.out.println("Persentase kompresi: " + compressionPercentage + "%");

            // Output execution time
            long endTime = System.currentTimeMillis();
            System.out.println("Waktu eksekusi: " + (endTime - startTime) + " ms");

            System.out.println("Kedalaman pohon: " + quadtree.getDepth());
            System.out.println("Banyak simpul pada pohon: " + quadtree.getNodeCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class QuadtreeNode {
    int[] color;
    boolean isLeaf;
    QuadtreeNode[] children;

    public QuadtreeNode(int[] color, boolean isLeaf) {
        this.color = color;
        this.isLeaf = isLeaf;
        this.children = new QuadtreeNode[4];
    }
}

class Quadtree {
    QuadtreeNode root;
    int[][][] imageArray;
    int nodeCount;
    int depth;

    public Quadtree(int[][][] imageArray, int x, int y, int width, int height, int count, int maxDepth, int treshold, int method) {
        this.imageArray = imageArray;
        this.nodeCount = 0;  
        this.depth = 0;
        if (method == 1){
            this.root = recursiveVariance(imageArray, x, y, width, height, 0, maxDepth, treshold);
        } else if (method == 3){
            this.root = recursivePixel(imageArray, x, y, width, height, 0, maxDepth, treshold);
        } 
    }

    private QuadtreeNode recursivePixel(int[][][] imageArray, int x, int y, int width, int height, int depth, int maxDepth, int threshold) {
        this.depth = Math.max(this.depth, depth);
        if (depth == maxDepth || maxPixelDifference(x, y, width, height) < threshold || width <= 1 || height <= 1) {
            nodeCount++;  
            return new QuadtreeNode(getAverageColor(x, y, width, height), true);
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        QuadtreeNode node = new QuadtreeNode(null, false);
        node.children[0] = recursivePixel(imageArray, x, y, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[1] = recursivePixel(imageArray, x + halfWidth, y, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[2] = recursivePixel(imageArray, x, y + halfHeight, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[3] = recursivePixel(imageArray, x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth+1, maxDepth, threshold);

        nodeCount++;  
        return node;
    }

    private QuadtreeNode recursiveVariance(int[][][] imageArray, int x, int y, int width, int height, int depth, int maxDepth, int threshold) {
        this.depth = Math.max(this.depth, depth);
        if (depth == maxDepth || variance(x, y, width, height) < threshold || width <= 1 || height <= 1) {
            nodeCount++;  
            return new QuadtreeNode(getAverageColor(x, y, width, height), true);
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        QuadtreeNode node = new QuadtreeNode(null, false);
        node.children[0] = recursiveVariance(imageArray, x, y, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[1] = recursiveVariance(imageArray, x + halfWidth, y, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[2] = recursiveVariance(imageArray, x, y + halfHeight, halfWidth, halfHeight, depth+1, maxDepth, threshold);
        node.children[3] = recursiveVariance(imageArray, x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth+1, maxDepth, threshold);

        nodeCount++;  
        return node;
    }

    private int[] getAverageColor(int x, int y, int width, int height) {
        int r = 0, g = 0, b = 0;
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                r += imageArray[i][j][0];
                g += imageArray[i][j][1];
                b += imageArray[i][j][2];
            }
        }
        int totalPixel = width * height;
        return new int[]{r / totalPixel, g / totalPixel, b / totalPixel};
    }

    private int[] maxMinArray(int x, int y, int width, int height, int channel) {
        int[] maxMinVal = {Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                if (imageArray[i][j][channel] > maxMinVal[0]) {
                    maxMinVal[0] = imageArray[i][j][channel];
                }
                if (imageArray[i][j][channel] < maxMinVal[1]) {
                    maxMinVal[1] = imageArray[i][j][channel];
                }
            }
        }
        return maxMinVal;
    }

    private double maxPixelDifference(int x, int y, int width, int height) {
        int dr = maxMinArray(x, y, width, height, 0)[0] - maxMinArray(x, y, width, height, 0)[1];
        int dg = maxMinArray(x, y, width, height, 1)[0] - maxMinArray(x, y, width, height, 1)[1];
        int db = maxMinArray(x, y, width, height, 2)[0] - maxMinArray(x, y, width, height, 2)[1];
        double drgb = (dr + dg + db) / 3.0;
        return drgb;
    }

    private double variance(int x, int y, int width, int height){
        int avgRed = getAverageColor(x, y, width, height)[0];
        int avgGreen = getAverageColor(x, y, width, height)[1];
        int avgBlue = getAverageColor(x, y, width, height)[2];
        double sigmaRed = 0, sigmaGreen = 0, sigmaBlue = 0;
        double varianceRed = 0, varianceGreen = 0, varianceBlue = 0;
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                sigmaRed += Math.pow(imageArray[i][j][0] - avgRed, 2);
                sigmaGreen += Math.pow(imageArray[i][j][1] - avgGreen, 2);
                sigmaBlue += Math.pow(imageArray[i][j][2] - avgBlue, 2);
            }
        }
        varianceRed = sigmaRed / (width * height);
        varianceGreen = sigmaGreen / (width * height);
        varianceBlue = sigmaBlue / (width * height);
        return ((varianceRed + varianceGreen + varianceBlue) / 3);
    }

    

    public void saveCompressedImage(int width, int height, String filePath) {
        try {
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            reconstructImage(root, outputImage, 0, 0, width, height);
            ImageIO.write(outputImage, "jpg", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconstructImage(QuadtreeNode node, BufferedImage image, int x, int y, int width, int height) {
        if (node.isLeaf) {
            Color color = new Color(node.color[0], node.color[1], node.color[2]);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (y + i < image.getHeight() && x + j < image.getWidth()) {
                        image.setRGB(x + j, y + i, color.getRGB());
                    }
                }
            }
        } else {
            int halfW = width / 2;
            int halfH = height / 2;
            int remW = width - halfW;
            int remH = height - halfH;

            reconstructImage(node.children[0], image, x, y, halfW, halfH);
            reconstructImage(node.children[1], image, x + halfW, y, remW, halfH);
            reconstructImage(node.children[2], image, x, y + halfH, halfW, remH);
            reconstructImage(node.children[3], image, x + halfW, y + halfH, remW, remH);
        }
    }

    // Getter methods for depth and node count
    public int getDepth() {
        return depth;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
