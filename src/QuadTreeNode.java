class QuadTreeNode {
    int x, y, width, height;  // Posisi dan ukuran blok
    int avgR, avgG, avgB;     // Warna rata-rata jika node adalah daun
    double error;             // Variansi/error blok
    boolean isLeaf;           // Apakah ini node daun?
    
    QuadTreeNode topLeft, topRight, bottomLeft, bottomRight; // Empat anak

    // Konstruktor untuk membuat node daun
    public QuadTreeNode(int x, int y, int width, int height, int avgR, int avgG, int avgB) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.avgR = avgR;
        this.avgG = avgG;
        this.avgB = avgB;
        this.isLeaf = true;
    }

    // Konstruktor untuk node internal yang masih bisa dibagi
    public QuadTreeNode(int x, int y, int width, int height, double error) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.error = error;
        this.isLeaf = false;
    }

    // Method untuk menambahkan anak pada node internal
    public void setChildren(QuadTreeNode topLeft, QuadTreeNode topRight, QuadTreeNode bottomLeft, QuadTreeNode bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }
}
