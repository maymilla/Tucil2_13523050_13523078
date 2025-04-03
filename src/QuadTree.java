import java.awt.image.BufferedImage;

public class QuadTree {
    private QuadTreeNode root; // Node utama (akar)
    private int threshold; // Ambang batas error untuk pembagian blok
    private int minBlockSize; // Ukuran minimum blok yang diperbolehkan
    private String errorMethod; // Metode error: "MAD" atau "Entropy"

    public QuadTree(BufferedImage image, int threshold, int minBlockSize, String errorMethod) {
        this.threshold = threshold;
        this.minBlockSize = minBlockSize;
        this.errorMethod = errorMethod;
        this.root = buildTree(image, 0, 0, image.getWidth(), image.getHeight());
    }

    private QuadTreeNode buildTree(BufferedImage image, int x, int y, int width, int height) {
        // Hitung error (MAD atau Entropy) untuk blok ini
        double error = calculateError(image, x, y, width, height);

        // Hitung warna rata-rata untuk blok ini
        int[] avgColor = calculateAverageColor(image, x, y, width, height);
        int avgR = avgColor[0], avgG = avgColor[1], avgB = avgColor[2];

        // Jika error kecil atau ukuran blok sudah minimum, buat daun
        if (error < threshold || width <= minBlockSize || height <= minBlockSize) {
            return new QuadTreeNode(x, y, width, height, avgR, avgG, avgB);
        }

        // Jika tidak, bagi blok menjadi 4 sub-blok
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        QuadTreeNode topLeft = buildTree(image, x, y, halfWidth, halfHeight);
        QuadTreeNode topRight = buildTree(image, x + halfWidth, y, halfWidth, halfHeight);
        QuadTreeNode bottomLeft = buildTree(image, x, y + halfHeight, halfWidth, halfHeight);
        QuadTreeNode bottomRight = buildTree(image, x + halfWidth, y + halfHeight, halfWidth, halfHeight);

        // Buat node internal dan set anak-anaknya
        QuadTreeNode node = new QuadTreeNode(x, y, width, height, error);
        node.setChildren(topLeft, topRight, bottomLeft, bottomRight);

        return node;
    }

    private double calculateError(BufferedImage image, int x, int y, int width, int height) {
        if ("MAD".equals(errorMethod)) {
            return MeanAbsoluteDeviation.calculate(image, x, y, width, height);
        } else if ("Entropy".equals(errorMethod)) {
            return Entropy.calculate(image, x, y, width, height);
        }
        return 0; // Default jika tidak ada metode yang dipilih
    }

    private int[] calculateAverageColor(BufferedImage image, int x, int y, int width, int height) {
        int totalR = 0, totalG = 0, totalB = 0, count = 0;

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                int rgb = image.getRGB(i, j);
                totalR += (rgb >> 16) & 0xFF;
                totalG += (rgb >> 8) & 0xFF;
                totalB += rgb & 0xFF;
                count++;
            }
        }

        int avgR = totalR / count;
        int avgG = totalG / count;
        int avgB = totalB / count;

        return new int[]{avgR, avgG, avgB}; // Kembalikan sebagai array
    }

    public QuadTreeNode getRoot() {
        return root;
    }
}
