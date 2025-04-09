public class QuadtreeNode {
    int[] color;               // {R,G,B}
    boolean isLeaf;
    QuadtreeNode[] children;    // always length‑4 (NW, NE, SW, SE)

    public QuadtreeNode(int[] color, boolean isLeaf) {
        this.color   = color;
        this.isLeaf  = isLeaf;
        this.children = new QuadtreeNode[4];
    }
}