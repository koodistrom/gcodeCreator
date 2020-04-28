package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The class represents an arc and is used in converting Bezier curves used in svg format to simple arcs used
 * in gcode.
 * The utils for converting Bezier curves to arcs are made with help of this very informative blog-post:
 * http://dlacko.org/blog/2016/10/19/approximating-bezier-curves-by-biarcs/
 * And this Github repo written in c#:
 * https://github.com/domoszlai/bezier2biarc
 */
public class Arc {

    /**
     * Center point of the Arc
     */
    private Vector2D center;

    /**
     * Radius of the Arc
     */
    private float r;

    /**
     * Start angle of the Arc as radians
     */
    private float startAngle;

    /**
     * Angle swept by the arc as radians
     */
    private float sweepAngle;

    /**
     * Start point of the Arc
     */
    private Vector2D start;

    /**
     * End point of the Arc
     */
    private Vector2D end;

    /**
     * Instantiates a new Arc.
     *
     * @param center     the center
     * @param r          the r
     * @param startAngle the start angle
     * @param sweepAngle the sweep angle
     * @param start      the start
     * @param end        the end
     */
    public Arc(Vector2D center, float r, float startAngle, float sweepAngle, Vector2D start, Vector2D end)
    {
        this.center = center;
        this.r = r;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.start = start;
        this.end = end;
    }

    /**
     * Is clockwise? boolean returns orientation of the arc.
     *
     * @return the boolean
     */

    public boolean isClockwise()
    {
        return sweepAngle > 0;
    }

    /**
     * Returns a point on the arc at t when t is in [0,1] and represents distance from the start.
     * eg. t=0.5 returns a point on the midle of the arc
     *
     * @param t the relative distance from start of the curve. Must be in [0,1]
     * @return the point on arc as Vector2D
     */

    public Vector2D pointAt(float t)
    {

        float x = (float)(center.getX() + r * Math.cos(startAngle + t * sweepAngle));
        float y = (float)(center.getY() + r * Math.sin(startAngle + t * sweepAngle));
        return new Vector2D(x, y);
    }

    /**
     * Length float.
     *
     * @return the length of the arc
     */
    public float length()
    {
        return r * Math.abs(sweepAngle);
    }

    /**
     * Gets center.
     *
     * @return the center
     */
    public Vector2D getCenter() {
        return center;
    }

    /**
     * Sets center.
     *
     * @param center the center
     */
    public void setCenter(Vector2D center) {
        this.center = center;
    }

    /**
     * Gets r.
     *
     * @return the r
     */
    public float getR() {
        return r;
    }

    /**
     * Sets r.
     *
     * @param r the r
     */
    public void setR(float r) {
        this.r = r;
    }

    /**
     * Gets start angle.
     *
     * @return the start angle
     */
    public float getStartAngle() {
        return startAngle;
    }

    /**
     * Sets start angle.
     *
     * @param startAngle the start angle
     */
    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * Gets sweep angle.
     *
     * @return the sweep angle
     */
    public float getSweepAngle() {
        return sweepAngle;
    }

    /**
     * Sets sweep angle.
     *
     * @param sweepAngle the sweep angle
     */
    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public Vector2D getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(Vector2D start) {
        this.start = start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public Vector2D getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(Vector2D end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "center=" + center +
                ", r=" + r +
                ", startAngle=" + startAngle +
                ", sweepAngle=" + sweepAngle +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
