package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class represents two arcs that approximate bezier curve and is used in converting Bezier curves used in svg
 * format to simple arcs used in gcode.
 * The utils for converting Bezier curves to arcs are made with help of this very informative blog-post:
 * http://dlacko.org/blog/2016/10/19/approximating-bezier-curves-by-biarcs/
 * And this Github repo written in c#:
 * https://github.com/domoszlai/bezier2biarc
 */
public class BiArc {

    private Arc arc1;
    private Arc arc2;

    /**
     * Instantiates a new Bi arc.
     *
     * @param p1    the start point
     * @param t1    the tangent vector at start point
     * @param p2    the end point
     * @param t2    the tangent vector at end point
     * @param trans the transition point between the two arcs
     */

    public BiArc(Vector2D p1, Vector2D t1, Vector2D p2, Vector2D t2, Vector2D trans)
    {
        // Calculate the orientation
        // https://en.wikipedia.org/wiki/Curve_orientation

        // Create a Logger
        Logger logger = Logger.getLogger(BiArc.class.getName());
        logger.setLevel(Level.OFF);
        float sum = 0;
        sum += (trans.getX() - p1.getX()) * (trans.getY() + p1.getY());
        sum += (p2.getX() - trans.getX()) * (p2.getY() + trans.getY());
        sum += (p1.getX() - p2.getX()) * (p1.getY()+ p2.getY());
        boolean counterClockWise = sum < 0;

        // Calculate perpendicular lines to the tangent at p1 and p2
        Line tl1 = Line.CreatePerpendicularAt(p1, p1.add(t1));
        logger.log(Level.INFO, "perp at start: " + tl1);
        Line tl2 = Line.CreatePerpendicularAt(p2, p2.add(t2));
        logger.log(Level.INFO, "perp at end: " + tl2);

        // Calculate the perpendicular bisector of P1T and P2T
        Vector2D p1t2 = (p1.add(trans)).scalarMultiply(0.5);
        Line pbp1t = Line.CreatePerpendicularAt(p1t2, trans);
        logger.log(Level.INFO, "perp at start to trans: " + pbp1t);
        Vector2D p2t2 = (p2.add(trans)).scalarMultiply(0.5);
        Line pbp2t = Line.CreatePerpendicularAt(p2t2, trans);
        logger.log(Level.INFO, "perp at end to trans: " + pbp2t);

        // The origo of the circles are at the intersection points
        Vector2D c1 = tl1.Intersection(pbp1t);
        logger.log(Level.INFO, "intersect1: " + c1);
        Vector2D c2 = tl2.Intersection(pbp2t);
        logger.log(Level.INFO, "intersect2: " + c2);

        // Calculate the radii
        float r1 = (float)((c1.subtract(p1)).getNorm());
        float r2 = (float)((c2.subtract(p2)).getNorm());

        // Calculate start and sweep angles
        Vector2D startVector1 = p1.subtract(c1);
        Vector2D endVector1 = trans.subtract(c1);
        float startAngle1 = (float) Math.atan2(startVector1.getY(), startVector1.getX());
        float sweepAngle1 = (float) Math.atan2(endVector1.getY(), endVector1.getX()) - startAngle1;

        Vector2D startVector2 = trans.subtract(c2);
        Vector2D endVector2 = p2.subtract(c2);
        float startAngle2 = (float) Math.atan2(startVector2.getY(), startVector2.getX());
        float sweepAngle2 = (float) Math.atan2(endVector2.getY(), endVector2.getX()) - startAngle2;

        // Adjust angles according to the orientation of the curve
        if (counterClockWise && sweepAngle1 < 0) sweepAngle1 = (float)(2 * Math.PI + sweepAngle1);
        if (!counterClockWise && sweepAngle1 > 0) sweepAngle1 = (float)(sweepAngle1 - 2 * Math.PI);
        if (counterClockWise && sweepAngle2 < 0) sweepAngle2 = (float)(2 * Math.PI + sweepAngle2);
        if (!counterClockWise && sweepAngle2 > 0) sweepAngle2 = (float)(sweepAngle2 - 2 * Math.PI);

        arc1 = new Arc(c1, r1, (float)startAngle1, (float)sweepAngle1, p1, trans);
        arc2 = new Arc(c2, r2, (float)startAngle2, (float)sweepAngle2, trans, p2);
    }

    /**
     * Returns a point on the biArc at t when t is in [0,1] and represents relative distance from the start.
     * eg. t=0.5 returns a point on the middle of the arc
     *
     * @param t the relative distance from start of the curve. Must be in [0,1]
     * @return the point on arc as Vector2D
     */

    public Vector2D pointAt(float t)
    {
        float s = arc1.length() / (arc1.length() + arc2.length());

        if (t <= s)
        {
            return arc1.pointAt(t / s);
        }
        else
        {
            return arc2.pointAt((t - s) / (1 - s));
        }
    }

    /**
     * Length float.
     *
     * @return the length of the biArc
     */
    public float length(){
        return arc1.length() + arc2.length();
    }

    /**
     * Gets arc 1.
     *
     * @return the arc 1
     */
    public Arc getArc1() {
        return arc1;
    }

    /**
     * Sets arc 1.
     *
     * @param arc1 the arc 1
     */
    public void setArc1(Arc arc1) {
        this.arc1 = arc1;
    }

    /**
     * Gets arc 2.
     *
     * @return the arc 2
     */
    public Arc getArc2() {
        return arc2;
    }

    /**
     * Sets arc 2.
     *
     * @param arc2 the arc 2
     */
    public void setArc2(Arc arc2) {
        this.arc2 = arc2;
    }

    @Override
    public String toString() {
        return "\nBiArc{" +
                "\narc1=" + arc1.toString() +
                ", \narc2=" + arc2.toString() +
                '}';
    }
}
