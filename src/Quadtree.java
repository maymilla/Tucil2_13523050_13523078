
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.FileImageOutputStream;


public class Quadtree {
    private int[][][] img;
    private QuadtreeNode root;
    private int nodeCount = 0;
    private int depth     = 0;

    public Quadtree(int[][][] img, int x, int y, int w, int h, int count, int minBlockSize,  double threshold, int method) {

        this.img = img;
        this.nodeCount = 0;  
        this.depth = 0;
        this.root = recursive(x, y, w, h, 0, minBlockSize, threshold, method);
    }

    private QuadtreeNode recursive(int x, int y, int w, int h, int currentDepth, int minBlockSize, double threshold, int method) {
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
            // BufferedImage frame = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            // gifFrames.add(frame);
            return new QuadtreeNode(avgColor(x, y, w, h), true);
        }

        int halfWidth = w / 2, halfHeight = h / 2;
        int[] avg = avgColor(x, y, w, h);
        QuadtreeNode node = new QuadtreeNode(avg, false);
        node.children[0] = recursive(x, y, halfWidth, halfHeight, currentDepth+1, minBlockSize, threshold, method);
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
        int totalPixel = width*height;
        return new int[]{ r/totalPixel, g/totalPixel, b/totalPixel };
    }

    public void saveImage(int w, int h, String path) {
        try {
            BufferedImage outputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            createOutputImage(root, outputImage, 0, 0, w, h);
            ImageIO.write(outputImage, "jpg", new File(path));

            List<BufferedImage> gifFrames = new ArrayList<>();
            for (int d = 0; d <= this.depth; d++) {
                BufferedImage frame = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                renderUpToDepth(root, frame, 0, 0, w, h, 0, d);
                gifFrames.add(frame);
            }

            createGif(gifFrames, "compression_process.gif");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createGif(List<BufferedImage> gifFrames, String outputPath ) throws IOException{
        ImageOutputStream output = new FileImageOutputStream(new File(outputPath));
        GifWriter gifWriter = new GifWriter(output, gifFrames.get(0).getType());
        for (BufferedImage frame : gifFrames) {
            gifWriter.writeToSequence(frame);
        }
        gifWriter.close();
        output.close();
    }

    private void renderUpToDepth(QuadtreeNode node, BufferedImage out, int x, int y, int width, int height,
                                int currentDepth, int maxDepthAllowed) {
        // jika node yg ditemukan adalah leaf node atau di depth yang >= maxdepthAllowed, ambil warna untuk dipakai di node itu
        // jika tidak, rekursi ke anak-anak dari node
        if (node.isLeaf || currentDepth >= maxDepthAllowed) {
            Color c = new Color(node.color[0], node.color[1], node.color[2]);
            for (int i=0; i<height; i++) {
                for (int j=0; j<width; j++) {
                    out.setRGB(x+j, y+i, c.getRGB());
                }
            }
        } else {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            int remWidth = width - halfWidth;
            int remHeight = height - halfHeight;
        
            renderUpToDepth(node.children[0], out, x, y, 
                            halfWidth, halfHeight, currentDepth+1, maxDepthAllowed);
        
            renderUpToDepth(node.children[1], out, x+halfWidth, y, 
                            remWidth, halfHeight, currentDepth+1, maxDepthAllowed);
        
            renderUpToDepth(node.children[2], out, x, y+halfHeight, 
                            halfWidth, remHeight, currentDepth+1, maxDepthAllowed);
        
            renderUpToDepth(node.children[3], out, x+halfWidth, y+halfHeight,
                            remWidth, remHeight, currentDepth+1, maxDepthAllowed);
        }
        
}


    private void createOutputImage(QuadtreeNode n, BufferedImage outputImage, int x, int y, int width, int height) {
        if (n.isLeaf) {
            Color c = new Color(n.color[0], n.color[1], n.color[2]);
            for (int i=0;i<height;i++) {
                for (int j=0;j<width;j++){
                    if (y+i < outputImage.getHeight() && x+j < outputImage.getWidth()){
                        outputImage.setRGB(x+j, y+i, c.getRGB());
                    }
                }
            }

        } else {
            int halfWidth = width/2, halfHeight = height/2;
            int remWidth = width-halfWidth, remHeight = height-halfHeight;
            createOutputImage(n.children[0], outputImage, x, y, halfWidth, halfHeight);
            createOutputImage(n.children[1], outputImage, x+halfWidth, y, remWidth, halfHeight);
            createOutputImage(n.children[2], outputImage, x, y+halfHeight, halfWidth, remHeight);
            createOutputImage(n.children[3], outputImage, x+halfWidth, y+halfHeight, remWidth, remHeight);
        }
    }

    public int getDepth(){ 
        return depth; 
    }
    public int getNodeCount() { 
        return nodeCount; 
    }
}
