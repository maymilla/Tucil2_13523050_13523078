public class QuadtreeNode {
    int[] color;               // {R,G,B}
    boolean isLeaf;
    QuadtreeNode[] children;    

    public QuadtreeNode(int[] color, boolean isLeaf) {
        this.color   = color;
        this.isLeaf  = isLeaf;
        this.children = new QuadtreeNode[4];
    }
}