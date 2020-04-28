package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The class represents a cubic Bezier curve and is used in converting Bezier curves used in svg format to simple arcs
 * used in gcode.
 * The utils for converting Bezier curves to arcs are made with help of this very informative blog-post:
 * http://dlacko.org/blog/2016/10/19/approximating-bezier-curves-by-biarcs/
 * And this Github repo written in c#:
 * https://github.com/domoszlai/bezier2biarc
 */
public class CubicBezier {
    /// <summary>
    /// Start point
    /// </summary>
    private Vector2D p1;
    /// <summary>
    /// End point
    /// </summary>
    private Vector2D p2;
    /// <summary>
    /// First control point
    /// </summary>
    private Vector2D c1;
    /// <summary>
    /// Second control point
    /// </summary>
    private Vector2D c2;

    /**
     * Instantiates a new Cubic bezier.
     *
     * @param point1        the start point
     * @param controlPoint1 the control point 1
     * @param controlPoint2 the control point 2
     * @param point2        the end point
     */
    public CubicBezier(Vector2D point1, Vector2D controlPoint1, Vector2D controlPoint2, Vector2D point2)
    {
        this.p1 = point1;
        this.c1 = controlPoint1;
        this.c2 = controlPoint2;
        this.p2 = point2;
    }

    /**
     * Returns a point on the curve at t. When t is in [0,1] and represents distance from the start.
     * eg. t=0.5 returns a point on the middle of the curve.
     *
     * @param t the relative distance from start of the curve. Must be in [0,1]
     * @return the point on arc as Vector2D
     */

    public Vector2D pointAt(float t)
    {
        Vector2D point =p1.scalarMultiply((float)Math.pow(1 - t, 3)).add(
                c1.scalarMultiply((float)(3 * Math.pow(1 - t, 2) * t)).add(
                c2.scalarMultiply((float)(3 * (1 - t) * Math.pow(t, 2))).add(
                p2.scalarMultiply((float)Math.pow(t, 3))
                )));


        return point;
    }

    /**
     * Splits curve on the point t. When t is in [0,1] and represents distance from the start.
     * eg. t=0.5 splits the curve at the middle.
     *
     * @param t the t
     * @return a list containing the two new CubicBeziers
     */

    public List<CubicBezier> split(float t)
    {
        Vector2D start = p1;
        Vector2D end = p2;

        Vector2D control11 = splitHelper(p1, c1, t);
        Vector2D controlMid = splitHelper(c1, c2, t);
        Vector2D control22 = splitHelper(c2, end, t);
        Vector2D control12 = splitHelper(control11, controlMid, t);
        Vector2D control21 = splitHelper(controlMid, control22, t);
        Vector2D split = splitHelper(control12, control21, t);


        List<CubicBezier> createdBeziers = new LinkedList<CubicBezier>();
        createdBeziers.add(new CubicBezier(start, control11, control12, split));
        createdBeziers.add(new CubicBezier(split, control21, control22, end));

        return createdBeziers;
    }

    private static Vector2D splitHelper(Vector2D pointBefore, Vector2D pointAfter, float scalar){
        return pointBefore.add((pointAfter.subtract(pointBefore).scalarMultiply(scalar)));
    }

    /**
     * Is clockwise boolean. Indicates orientation of the curve; clockwise or counter clockwise
     *
     * @return the boolean
     */
    public boolean IsClockwise()
    {
            float sum = 0;
            sum += (c1.getX() - p1.getX()) * (c1.getY() + p1.getY());
            sum += (c2.getX() - c1.getX()) * (c2.getY() + c1.getY());
            sum += (p2.getX() - c2.getX()) * (p2.getY() + c2.getY());
            sum += (p1.getX() - p2.getX()) * (p1.getY() + p2.getY());
            return sum < 0;

    }

    /**
     * Calculates inflexion points of the curve and returns the as a list.
     * The points are valid only if they are in the range of 0-1.
     * http://www.caffeineowl.com/graphics/2d/vectorial/cubic-inflexion.html
     * @return the inflection points in a list as Complexes
     */

    public List<Complex> inflexionPoints() {

        Vector2D A = c1.subtract(p1);
        Vector2D B = c2.subtract(c1.subtract(A));
        Vector2D C = p2.subtract(c2.subtract(A.subtract(B.scalarMultiply(2))));

        Complex a = new Complex(B.getX() * C.getY() - B.getY() * C.getX(), 0);
        Complex b = new Complex(A.getX() * C.getY() - A.getY() * C.getX(), 0);
        Complex c = new Complex(A.getX() * B.getY() - A.getY() * B.getX(), 0);

        Complex t1 = b.negate().add( b.pow(2).subtract(a.multiply(c.multiply(4))).sqrt()).divide(a.multiply(2));
        Complex t2 = b.negate().subtract( b.pow(2).subtract(a.multiply(c.multiply(4))).sqrt()).divide(a.multiply(2));

        List<Complex> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }

    /**
     * Returns cubic Bezier from quadratic bezier curve.
     *
     * @param point1  the start point of quadratic bezier
     * @param control the control point of quadratic bezier
     * @param point2  the the end point  of quadratic bezier
     * @return the cubic bezier
     */
    public static CubicBezier fromQuadratic(Vector2D point1, Vector2D control, Vector2D point2){
        Vector2D p1 = point1;
        Vector2D p2 = point2;

        Vector2D c1 = point1.add((control.subtract(point1).scalarMultiply( 2/3 )));
        Vector2D c2 = point2.add( (control.subtract(point2).scalarMultiply(2/3 )));
        CubicBezier cb = new CubicBezier(p1, c1,c2,p2);
        return cb;
    }

    /**
     * Gets p 1.
     *
     * @return the p 1
     */
    public Vector2D getP1() {
        return p1;
    }

    /**
     * Sets p 1.
     *
     * @param p1 the p 1
     */
    public void setP1(Vector2D p1) {
        this.p1 = p1;
    }

    /**
     * Gets p 2.
     *
     * @return the p 2
     */
    public Vector2D getP2() {
        return p2;
    }

    /**
     * Sets p 2.
     *
     * @param p2 the p 2
     */
    public void setP2(Vector2D p2) {
        this.p2 = p2;
    }

    /**
     * Gets c 1.
     *
     * @return the c 1
     */
    public Vector2D getC1() {
        return c1;
    }

    /**
     * Sets c 1.
     *
     * @param c1 the c 1
     */
    public void setC1(Vector2D c1) {
        this.c1 = c1;
    }

    /**
     * Gets c 2.
     *
     * @return the c 2
     */
    public Vector2D getC2() {
        return c2;
    }

    /**
     * Sets c 2.
     *
     * @param c2 the c 2
     */
    public void setC2(Vector2D c2) {
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "CubicBezier{" +
                "p1=" + p1.toString() +
                ", p2=" + p2.toString() +
                ", c1=" + c1.toString() +
                ", c2=" + c2.toString() +
                '}';
    }
}
