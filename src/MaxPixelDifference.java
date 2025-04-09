
public class MaxPixelDifference {

    /** Mean of (max‑min) across R, G, B channels in the region. */
    public static double calculate(int[][][] img,
                                   int x, int y, int w, int h) {

        int dr = range(img, x, y, w, h, 0);
        int dg = range(img, x, y, w, h, 1);
        int db = range(img, x, y, w, h, 2);
        return (dr + dg + db) / 3.0;
    }

    // -- helpers -----------------------------------------------------
    private static int range(int[][][] img,
                             int x, int y, int w, int h, int ch) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i=y;i<y+h;i++)
            for (int j=x;j<x+w;j++) {
                int v = img[i][j][ch];
                if (v > max) max = v;
                if (v < min) min = v;
            }
        return max - min;
    }
}
