package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Arc {
    /// <summary>
    /// Center point
    /// </summary>

    private Vector2D center;
    /// <summary>
    /// Radius
    /// </summary>
    private float r;
    /// <summary>
    /// Start angle in radian
    /// </summary>
    private float startAngle;
    /// <summary>
    /// Sweep angle in radian
    /// </summary>
    private float sweepAngle;
    /// <summary>
    /// Start point of the arc
    /// </summary>
    private Vector2D start;
    /// <summary>
    /// End point of the arc
    /// </summary>
    private Vector2D end;

    public Arc(Vector2D center, float r, float startAngle, float sweepAngle, Vector2D start, Vector2D end)
    {
        this.center = center;
        this.r = r;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.start = start;
        this.end = end;
    }

    /// <summary>
    /// Orientation of the arc
    /// </summary>
    public boolean isClockwise()
    {
        return sweepAngle > 0;
    }

    /// <summary>
    /// Implement the parametric equation.
    /// </summary>
    /// <param name="t">Parameter of the curve. Must be in [0,1]</param>
    /// <returns></returns>
    public Vector2D pointAt(float t)
    {
        float x = (float)(center.getX() + r * Math.cos(startAngle + t * sweepAngle));
        float y = (float)(center.getY() + r * Math.sin(startAngle + t * sweepAngle));
        return new Vector2D(x, y);
    }

    public float length()
    {
        return r * Math.abs(sweepAngle);
    }

    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public Vector2D getStart() {
        return start;
    }

    public void setStart(Vector2D start) {
        this.start = start;
    }

    public Vector2D getEnd() {
        return end;
    }

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
