import java.awt.image.BufferedImage;

public class QuadTree {
    private QuadTreeNode root; // Node utama (akar)
    private int threshold; // Ambang batas error untuk pembagian blok
    private int minBlockSize; // Ukuran minimum blok yang diperbolehkan
    private int errorMethod; // Metode error: "MAD" atau "Entropy"

    public QuadTree(BufferedImage image, int threshold, int minBlockSize, int errorMethod) {
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
        int remainingWidth = width - halfWidth; // Sisa lebar setelah dibagi dua
        int remainingHeight = height - halfHeight; // Sisa tinggi setelah dibagi dua

        QuadTreeNode topLeft = buildTree(image, x, y, halfWidth, halfHeight);
        QuadTreeNode topRight = buildTree(image, x + halfWidth, y, remainingWidth, halfHeight);
        QuadTreeNode bottomLeft = buildTree(image, x, y + halfHeight, halfWidth, remainingHeight);
        QuadTreeNode bottomRight = buildTree(image, x + halfWidth, y + halfHeight, remainingWidth, remainingHeight);

        // Buat node internal dan set anak-anaknya
        QuadTreeNode node = new QuadTreeNode(x, y, width, height, error);
        node.setChildren(topLeft, topRight, bottomLeft, bottomRight);

        return node;
    }

    private double calculateError(BufferedImage image, int x, int y, int width, int height) {
        if (errorMethod == 1) {
            return MeanAbsoluteDeviation.calculate(image, x, y, width, height);
        } else if (errorMethod == 2) {
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

    public BufferedImage toBufferedImage() {
        // DEBUGGING
        if (root == null) {
            System.out.println("QuadTree root masih null! Tidak bisa membuat gambar.");
            return null;
        }        

        // Hitung ukuran gambar yang terkompresi berdasarkan jumlah blok (misalnya, daun dari QuadTree)
        int newWidth = root.width;
        int newHeight = root.height;
    
        BufferedImage img = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        drawQuadTree(img, root);
        return img;
    }  

    public int getDepth(QuadTreeNode node) {
        if (node == null) return 0;
        return Math.max(getDepth(node.topLeft), Math.max(getDepth(node.topRight), Math.max(getDepth(node.bottomLeft), getDepth(node.bottomRight)))) + 1;
    }    

    public int countNodes(QuadTreeNode node) {
        if (node == null) return 0;
        int count = 1; // hitung node saat ini
        count += countNodes(node.topLeft);
        count += countNodes(node.topRight);
        count += countNodes(node.bottomLeft);
        count += countNodes(node.bottomRight);
        return count;
    }

    public int getTotalBlocks() {
        return countLeafNodes(root);
    }
    
    private int countLeafNodes(QuadTreeNode node) {
        if (node == null) return 0;
    
        // Kalau ini adalah node daun (tidak punya anak), hitung sebagai 1 blok
        if (node.isLeaf()) return 1;
    
        // Rekursif ke anak-anaknya
        return countLeafNodes(node.topLeft) + countLeafNodes(node.topRight) +
               countLeafNodes(node.bottomLeft) + countLeafNodes(node.bottomRight);
    }    
    
    private void drawQuadTree(BufferedImage img, QuadTreeNode node) {
        if (node.isLeaf) {
            int color = (node.avgR << 16) | (node.avgG << 8) | node.avgB;
            for (int i = node.x; i < node.x + node.width; i++) {
                for (int j = node.y; j < node.y + node.height; j++) {
                    img.setRGB(i, j, color);
                }
            }
        } else {
            if (node.topLeft != null) drawQuadTree(img, node.topLeft);
            if (node.topRight != null) drawQuadTree(img, node.topRight);
            if (node.bottomLeft != null) drawQuadTree(img, node.bottomLeft);
            if (node.bottomRight != null) drawQuadTree(img, node.bottomRight);
        }
    }    
}
