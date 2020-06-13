package com.jaakkomantyla.gcc.gcodecreator.utils;


import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class EllipticalArc {
    private float rx;
    private  float ry;
    private float xRotation;
    private int largeArcFlag;
    private int sweepFlag;
    private float x;
    private float y;
    private Vector2D previousPoint;

    public EllipticalArc(Vector2D previousPoint, float rx, float ry, float xRotation, int largeArcFlag, int sweepFlag, float x, float y) {
        this.previousPoint = previousPoint;
        this.rx = rx;
        this.ry = ry;
        this.xRotation = xRotation;
        this.largeArcFlag = largeArcFlag;
        this.sweepFlag = sweepFlag;
        this.x = x;
        this.y = y;
    }


    private static double TAU = Math.PI * 2;


    private Vector2D mapToEllipse (Vector2D previousPoint, double rx, double ry, double cosphi, double sinphi, double centerx, double centery) {
        double x = previousPoint.getX();
        double y = previousPoint.getY();
        x *= rx;
        y *= ry;

        double xp = cosphi * x - sinphi * y;
        double yp = sinphi * x + cosphi * y;

        return new Vector2D(xp + centerx, yp + centery);
    }

    private List<Vector2D> approxUnitArc(double ang1, double ang2) {
        // If 90 degree circular arc, use a constant
        // as derived from http://spencermortensen.com/articles/bezier-circle
        //System.out.println("ang2 = "+ang2);
        double a;
        double quarterTurn =  1.5707961751138155;
        double quarterTurn2 =  0.7853983718506589;
        if(Math.abs(ang2 - quarterTurn)<0.00001 || Math.abs(ang2 - quarterTurn2)<0.00001){
            a =  0.551915024494;
        }else if(Math.abs(ang2 + quarterTurn)<0.00001 || Math.abs(ang2 + quarterTurn2)<0.00001){
            a =  -0.551915024494;
        }else{
            a=4 / 3 * Math.tan(ang2 / 4);
        }

        double x1 = Math.cos(ang1);
        double y1 = Math.sin(ang1);
        double x2 = Math.cos(ang1 + ang2);
        double y2 = Math.sin(ang1 + ang2);

        List<Vector2D> list = new LinkedList<>();
        list.add(new Vector2D(x1 - y1 * a, y1 + x1 * a));
        list.add(new Vector2D(x2 + y2 * a, y2 - x2 * a));
        list.add(new Vector2D(x2, y2));

        return list;
    }


    private double vectorAngle(double ux, double uy, double vx, double vy) {
        double sign = (ux * vy - uy * vx < 0) ? -1 : 1;

        double dot = ux * vx + uy * vy;

        if (dot > 1) {
            dot = 1;
        }

        if (dot < -1) {
            dot = -1;
        }

        return sign * Math.acos(dot);
    }

    private double[] getArcCenter(
    double px,
    double py,
    double cx,
    double cy,
    double rx,
    double ry,
    int largeArcFlag,
    int sweepFlag,
    double sinphi,
    double cosphi,
    double pxp,
    double pyp
){
        double rxsq = Math.pow(rx, 2);
        double rysq = Math.pow(ry, 2);
        double pxpsq = Math.pow(pxp, 2);
        double pypsq = Math.pow(pyp, 2);

        double radicant = (rxsq * rysq) - (rxsq * pypsq) - (rysq * pxpsq);

        if (radicant < 0) {
            radicant = 0;
        }

        radicant /= (rxsq * pypsq) + (rysq * pxpsq);
        radicant = Math.sqrt(radicant) * (largeArcFlag == sweepFlag ? -1 : 1);

        double centerxp = radicant * rx / ry * pyp;
        double centeryp = radicant * -ry / rx * pxp;

        double centerx = cosphi * centerxp - sinphi * centeryp + (px + cx) / 2;
        double centery = sinphi * centerxp + cosphi * centeryp + (py + cy) / 2;

        double vx1 = (pxp - centerxp) / rx;
        double vy1 = (pyp - centeryp) / ry;
        double vx2 = (-pxp - centerxp) / rx;
        double vy2 = (-pyp - centeryp) / ry;

        double ang1 = vectorAngle(1, 0, vx1, vy1);
        double ang2 = vectorAngle(vx1, vy1, vx2, vy2);

        if (sweepFlag == 0 && ang2 > 0) {
            ang2 -= TAU;
        }

        if (sweepFlag == 1 && ang2 < 0) {
            ang2 += TAU;
        }

        return new double[]{ centerx, centery, ang1, ang2 };
    }

private List<CubicBezier> toBezier (
    double px,
    double py,
    double cx,
    double cy,
    double rx,
    double ry,
    double xAxisRotation,
    int largeArcFlag,
    int sweepFlag
    ){
    List<List<Vector2D>> curves = new LinkedList<>();

        if (rx == 0 || ry == 0) {
            return new LinkedList<>();
        }

    double sinphi = Math.sin(xAxisRotation * TAU / 360);
    double cosphi = Math.cos(xAxisRotation * TAU / 360);

    double pxp = cosphi * (px - cx) / 2 + sinphi * (py - cy) / 2;
    double pyp = -sinphi * (px - cx) / 2 + cosphi * (py - cy) / 2;


    if (pxp == 0 && pyp == 0) {
        return  new LinkedList<>();
    }
    rx = Math.abs(rx);
    ry = Math.abs(ry);

    double lambda =
        Math.pow(pxp, 2) / Math.pow(rx, 2) +
        Math.pow(pyp, 2) / Math.pow(ry, 2);

        if (lambda > 1) {
            rx *= Math.sqrt(lambda);
            ry *= Math.sqrt(lambda);
        }

        double[] centersAndAngles = getArcCenter(
                px,
                py,
                cx,
                cy,
                rx,
                ry,
                largeArcFlag,
                sweepFlag,
                sinphi,
                cosphi,
                pxp,
                pyp
        );

        double centerx = centersAndAngles[0];
        double centery = centersAndAngles[1];
        double ang1 = centersAndAngles[2];
        double ang2 = centersAndAngles[3];

        // If 'ang2' == 90.0000000001, then `ratio` will evaluate to
        // 1.0000000001. This causes `segments` to be greater than one, which is an
        // unecessary split, and adds extra points to the bezier curve. To alleviate
        // this issue, we round to 1.0 when the ratio is close to 1.0.
        double ratio = Math.abs(ang2) / (TAU / 4);
        //System.out.println("ratio: "+ratio);
        if (Math.abs(1.0 - ratio) < 0.00001) {
            ratio = 1.0;
        }

    double segments = Math.max(Math.ceil(ratio), 1);

        ang2 /= segments;

        for (int i = 0; i < segments; i++) {
            curves.add(approxUnitArc(ang1, ang2));
            ang1 += ang2;
        }
    List<CubicBezier> curvesToReturn = new LinkedList<>();
    final double finalRx = rx;
    final double finalRy = ry;
    List<Vector2D> startPoint = new LinkedList<>();
    startPoint.add(new Vector2D(px, py));
    curves.forEach((curve) -> {
    Vector2D point1 = mapToEllipse(curve.get( 0 ), finalRx, finalRy, cosphi, sinphi, centerx, centery);
    Vector2D point2 = mapToEllipse(curve.get(1), finalRx, finalRy, cosphi, sinphi, centerx, centery);
    Vector2D endPoint = mapToEllipse(curve.get( 2), finalRx, finalRy, cosphi, sinphi, centerx, centery);
    CubicBezier c = new CubicBezier(startPoint.get(0), point1, point2, endPoint);
        System.out.println(c);
    curvesToReturn.add(c);
    startPoint.remove(0);
    startPoint.add(endPoint);
    });
        return curvesToReturn;
    }

    public List<CubicBezier> toCubicBezier(){
       return toBezier(previousPoint.getX(),
                previousPoint.getY(),
                x,
                y,
                rx,
                ry,
                xRotation,
                largeArcFlag,
                sweepFlag);
    }

    private CubicBezier positionedToCubicBezier(EllipticalArc arc){
        return null;
    }

    private EllipticalArc rotate(){
        return null;
    }
    private EllipticalArc translate(){
        return null;
    }
    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getxRotation() {
        return xRotation;
    }

    public void setxRotation(float xRotation) {
        this.xRotation = xRotation;
    }

    public int getLargeArcFlag() {
        return largeArcFlag;
    }

    public void setLargeArcFlag(int largeArcFlag) {
        this.largeArcFlag = largeArcFlag;
    }

    public int getSweepFlag() {
        return sweepFlag;
    }

    public void setSweepFlag(int sweepFlag) {
        this.sweepFlag = sweepFlag;
    }

    public Vector2D getPreviousPoint() {
        return previousPoint;
    }

    public void setPreviousPoint(Vector2D previousPoint) {
        this.previousPoint = previousPoint;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "EllipticalArc{" +
                "rx=" + rx +
                ", ry=" + ry +
                ", xRotation=" + xRotation +
                ", largeArcFlag=" + largeArcFlag +
                ", sweepFlag=" + sweepFlag +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
