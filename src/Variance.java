public class Variance {
    public static double calculate(int[][][] img,
                                   int x, int y, int w, int h) {

        int[] avg = avgColor(img, x, y, w, h);
        int ar = avg[0], ag = avg[1], ab = avg[2];

        double sr=0, sg=0, sb=0;
        for (int i=y;i<y+h;i++)
            for (int j=x;j<x+w;j++) {
                sr += Math.pow(img[i][j][0] - ar, 2);
                sg += Math.pow(img[i][j][1] - ag, 2);
                sb += Math.pow(img[i][j][2] - ab, 2);
            }

        double area = w * h;
        double vr = sr/area, vg = sg/area, vb = sb/area;
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
