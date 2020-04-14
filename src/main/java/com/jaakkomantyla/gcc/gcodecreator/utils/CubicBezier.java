package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public CubicBezier(Vector2D point1, Vector2D controlPoint1, Vector2D controlPoint2, Vector2D point2)
    {
        this.p1 = point1;
        this.c1 = controlPoint1;
        this.c2 = controlPoint2;
        this.p2 = point2;
    }

    /// <summary>
    /// Implement the parametric equation.
    /// </summary>
    /// <param name="t">Parameter of the curve. Must be in [0,1]</param>
    /// <returns></returns>
    public Vector2D pointAt(float t)
    {
        Vector2D point =p1.scalarMultiply((float)Math.pow(1 - t, 3)).add(
                c1.scalarMultiply((float)(3 * Math.pow(1 - t, 2) * t)).add(
                c2.scalarMultiply((float)(3 * (1 - t) * Math.pow(t, 2))).add(
                p2.scalarMultiply((float)Math.pow(t, 3))
                )));


        return point;
    }

    /// <summary>
    /// Split a bezier curve at a given parameter value. It returns both of the new ones
    /// </summary>
    /// <param name="t">Parameter of the curve. Must be in [0,1]</param>
    /// <returns></returns>
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

    /// <summary>
    /// The orientation of the Bezier curve
    /// </summary>
    public boolean IsClockwise()
    {
            float sum = 0;
            sum += (c1.getX() - p1.getX()) * (c1.getY() + p1.getY());
            sum += (c2.getX() - c1.getX()) * (c2.getY() + c1.getY());
            sum += (p2.getX() - c2.getX()) * (p2.getY() + c2.getY());
            sum += (p1.getX() - p2.getX()) * (p1.getY() + p2.getY());
            return sum < 0;

    }

    /// <summary>
    /// Inflexion points of the Bezier curve. They only valid if they are real and in the range of [0,1]
    /// </summary>
    /// <param name="bezier"></param>
    /// <returns></returns>
    public List<Complex> inflexionPoints() {
            // http://www.caffeineowl.com/graphics/2d/vectorial/cubic-inflexion.html

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

    public static CubicBezier fromQuadratic(Vector2D point1, Vector2D control, Vector2D point2){
        Vector2D p1 = point1;
        Vector2D p2 = point2;

        Vector2D c1 = point1.add((control.subtract(point1).scalarMultiply( 2/3 )));
        Vector2D c2 = point2.add( (control.subtract(point2).scalarMultiply(2/3 )));
        CubicBezier cb = new CubicBezier(p1, c1,c2,p2);
        return cb;
    }

    public Vector2D getP1() {
        return p1;
    }

    public void setP1(Vector2D p1) {
        this.p1 = p1;
    }

    public Vector2D getP2() {
        return p2;
    }

    public void setP2(Vector2D p2) {
        this.p2 = p2;
    }

    public Vector2D getC1() {
        return c1;
    }

    public void setC1(Vector2D c1) {
        this.c1 = c1;
    }

    public Vector2D getC2() {
        return c2;
    }

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
