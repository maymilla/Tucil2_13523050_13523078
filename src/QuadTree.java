
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Quadtree {
    private int[][][] img;
    private QuadtreeNode root;
    private int nodeCount = 0;
    private int depth     = 0;

    public Quadtree(int[][][] img, int x, int y, int w, int h, int count, int minBlockSize,  int threshold, int method) {

        this.img = img;
        this.nodeCount = 0;  
        this.depth = 0;
        this.root = recursive(x, y, w, h, 0, minBlockSize, threshold, method);
    }

    private QuadtreeNode recursive(int x, int y, int w, int h, int currentDepth, int minBlockSize, int threshold, int method) {
        depth = Math.max(depth, currentDepth);
        double error;

        if (method == 1) {            // variance
            error = Variance.calculate(img, x, y, w, h);
        } else if (method == 2) {     // max‑pixel‑diff
            error = MeanAbsoluteDeviation.calculate(img, x, y, w, h);
        } else if (method == 3) {     // max‑pixel‑diff
            error = MaxPixelDifference.calculate(img, x, y, w, h);
        } else if (method == 4) {     // max‑pixel‑diff
            error = Entropy.calculate(img, x, y, w, h);
        } else {
            throw new IllegalArgumentException("Unknown method: " + method);
        }
        if (error < threshold || w <= minBlockSize || h <= minBlockSize) {
            nodeCount++;
            return new QuadtreeNode(avgColor(x, y, w, h), true);
        }

        int halfWidth = w / 2, halfHeight = h / 2;
        QuadtreeNode node = new QuadtreeNode(null, false);
        node.children[0] = recursive(x, y, halfWidth, halfWidth, currentDepth+1, minBlockSize, threshold, method);
        node.children[1] = recursive(x + halfWidth, y,  halfWidth, halfHeight, currentDepth+1, minBlockSize, threshold, method);
        node.children[2] = recursive(x, y + halfHeight, halfWidth, halfHeight, currentDepth+1, minBlockSize, threshold, method);
        node.children[3] = recursive(x + halfWidth, y + halfHeight, halfWidth, halfHeight, currentDepth+1, minBlockSize, threshold, method);
        nodeCount++;
        return node;
    }


    private int[] avgColor(int x, int y, int width, int height) {
        int r=0,g=0,b=0;
        for (int i=y;i<y+height;i++)
            for (int j=x;j<x+width;j++) {
                r += img[i][j][0];
                g += img[i][j][1];
                b += img[i][j][2];
            }
        int total = width*height;
        return new int[]{ r/total, g/total, b/total };
    }

    public void saveCompressedImage(int w, int h, String path) {
        try {
            BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            paint(root, out, 0, 0, w, h);
            ImageIO.write(out, "jpg", new File(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void paint(QuadtreeNode n, BufferedImage imgOut, int x, int y, int width, int height) {
        if (n.isLeaf) {
            Color c = new Color(n.color[0], n.color[1], n.color[2]);
            for (int i=0;i<height;i++)
                for (int j=0;j<width;j++)
                    if (y+i < imgOut.getHeight() && x+j < imgOut.getWidth())
                        imgOut.setRGB(x+j, y+i, c.getRGB());
        } else {
            int halfWidth = width/2, halfHeight = height/2;
            int remWidth = width-halfWidth, remHeight = height-halfHeight;
            paint(n.children[0], imgOut, x, y, halfWidth, halfHeight);
            paint(n.children[1], imgOut, x+halfWidth, y, remWidth, halfHeight);
            paint(n.children[2], imgOut, x, y+halfHeight, halfWidth, remHeight);
            paint(n.children[3], imgOut, x+halfWidth, y+halfHeight, remWidth, remHeight);
        }
    }

    public int getDepth(){ 
        return depth; 
    }
    public int getNodeCount() { 
        return nodeCount; 
    }
}
