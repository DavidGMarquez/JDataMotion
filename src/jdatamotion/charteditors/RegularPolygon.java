package jdatamotion.charteditors;

import java.awt.*;

public class RegularPolygon extends Polygon {

    private final int x;
    private final int y;
    private final int r;
    private final int vertexCount;
    private final double startAngle;

    public RegularPolygon(int x, int y, int r, int vertexCount) {
        this(x, y, r, vertexCount, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public RegularPolygon(int x, int y, int r, int vertexCount, double startAngle) {
        super(getXCoordinates(x, y, r, vertexCount, startAngle), getYCoordinates(x, y, r, vertexCount, startAngle), vertexCount);
        this.x = x;
        this.y = y;
        this.r = r;
        this.vertexCount = vertexCount;
        this.startAngle = startAngle;
    }

    protected static int[] getXCoordinates(int x, int y, int r, int vertexCount, double startAngle) {
        int res[] = new int[vertexCount];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        for (int i = 0; i < vertexCount; i++) {
            res[i] = (int) Math.round(r * Math.cos(angle)) + x;
            angle += addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates(int x, int y, int r, int vertexCount, double startAngle) {
        int res[] = new int[vertexCount];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        for (int i = 0; i < vertexCount; i++) {
            res[i] = (int) Math.round(r * Math.sin(angle)) + y;
            angle += addAngle;
        }
        return res;
    }
}
