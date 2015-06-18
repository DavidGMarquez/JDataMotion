package jdatamotion.charteditors;

import java.awt.*;

public class StarPolygon extends Polygon {

    private final int x;
    private final int y;
    private final int r;
    private final int vertexCount;
    private final double startAngle;

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

    public StarPolygon(int x, int y, int r, int innerR, int vertexCount) {
        this(x, y, r, innerR, vertexCount, 0);
    }

    public StarPolygon(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
        super(getXCoordinates(x, y, r, innerR, vertexCount, startAngle), getYCoordinates(x, y, r, innerR, vertexCount, startAngle), vertexCount * 2);
        this.x = x;
        this.y = y;
        this.r = r;
        this.vertexCount = vertexCount;
        this.startAngle = startAngle;
    }

    protected static int[] getXCoordinates(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
        int res[] = new int[vertexCount * 2];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        double innerAngle = startAngle + Math.PI / vertexCount;
        for (int i = 0; i < vertexCount; i++) {
            res[i * 2] = (int) Math.round(r * Math.cos(angle)) + x;
            angle += addAngle;
            res[i * 2 + 1] = (int) Math.round(innerR * Math.cos(innerAngle)) + x;
            innerAngle += addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
        int res[] = new int[vertexCount * 2];
        double addAngle = 2 * Math.PI / vertexCount;
        double angle = startAngle;
        double innerAngle = startAngle + Math.PI / vertexCount;
        for (int i = 0; i < vertexCount; i++) {
            res[i * 2] = (int) Math.round(r * Math.sin(angle)) + y;
            angle += addAngle;
            res[i * 2 + 1] = (int) Math.round(innerR * Math.sin(innerAngle)) + y;
            innerAngle += addAngle;
        }
        return res;
    }
}
