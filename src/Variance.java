public class Variance {
    public static double calculate(int[][][] img,
                                   int x, int y, int w, int h) {

        int[] avg = avgColor(img, x, y, w, h);
        int avgr = avg[0];
        int avgg = avg[1];
        int avgb = avg[2];

        double sr=0, sg=0, sb=0;
        for (int i=y;i<y+h;i++)
            for (int j=x;j<x+w;j++) {
                sr += Math.pow(img[i][j][0] - avgr, 2);
                sg += Math.pow(img[i][j][1] - avgg, 2);
                sb += Math.pow(img[i][j][2] - avgb, 2);
            }

        double n = w * h;
        double vr = sr/n, vg = sg/n, vb = sb/n;
        return (vr + vg + vb) / 3.0;
    }
    private static int[] avgColor(int[][][] img,
                                  int x, int y, int w, int h) {
        int r=0,g=0,b=0;
        for (int i=y;i<y+h;i++)
            for (int j=x;j<x+w;j++) {
                r += img[i][j][0];
                g += img[i][j][1];
                b += img[i][j][2];
            }
        int total = w*h;
        return new int[]{ r/total, g/total, b/total };
    }
}
