package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The class represents a line in point slope form and is used in converting Bezier curves used in svg format
 * to simple arcs used in gcode.
 * y - y1 = m * (x - x1)
 * Vertical line: m = NaN
 * Horizontal line: m = 0
 * The utils for converting Bezier curves to arcs are made with help of this very informative blog-post:
 * http://dlacko.org/blog/2016/10/19/approximating-bezier-curves-by-biarcs/
 * And this Github repo written in c#:
 * https://github.com/domoszlai/bezier2biarc
 */
public class Line {

    private Float slope;
    private Vector2D point;

    /**
     * Instantiates a new Line that goes through the given points
     *
     * @param p1
     * @param p2
     */
        public Line(Vector2D p1, Vector2D p2) {
            this.point = p1;
            this.slope = calculateSlope(p1, p2);
        }

    /**
     * Instantiates a new Line that goes through the given point and has the given slope.
     *
     * @param P     the point
     * @param slope the slope
     */

        public Line(Vector2D P, Float slope)
        {
            this.point = P;
            this.slope = slope;
        }

    /**
     * Calculates the intersection point of this line and the given line.
     *
     * @param l the line
     * @return the intersection point as Vector2D
     */

        public Vector2D Intersection(Line l)
        {
            if(slope.isNaN())
            {
                return verticalIntersection(this, l);
            }
            else if(l.getSlope().isNaN())
            {
                return verticalIntersection(l, this);
            }
            else
            {
                float x = (float)(slope * point.getX() - l.getSlope() * l.getPoint().getX() - point.getY() + l.getPoint().getY()) / (slope - l.getSlope());
                float y = (float)(slope * x - slope * point.getX() + point.getY());
                return new Vector2D(x, y);

            }
        }

        //checks for special cases on the intersection
        private static Vector2D verticalIntersection(Line vl, Line l)
        {
            double x = vl.getPoint().getX();
            double y = l.getSlope() * (x - l.getPoint().getX()) + l.getPoint().getY();
            return new Vector2D(x, y);
        }

    /**
     * Create perpendicular at line to the line defined by the points. The returned line
     * goes through p.
     *
     * @param p
     * @param p1
     * @return the perpendicular line
     */

        public static Line CreatePerpendicularAt(Vector2D p, Vector2D p1)
        {
            Float m = calculateSlope(p, p1);

            if (m == 0)
            {
                return new Line(p, Float.NaN);
            }
            else if(m.isNaN())
            {
                return new Line(p, 0f);
            }
            else
            {
                return new Line(p, -1f / m);
            }
        }

        private static float calculateSlope(Vector2D p1, Vector2D p2)
        {
            if(p2.getX() == p1.getX())
            {
                return Float.NaN;
            }
            else
            {
                return (float)((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
            }
        }

    /**
     * Gets slope.
     *
     * @return the slope
     */
    public Float getSlope() {
        return slope;
    }

    /**
     * Sets slope.
     *
     * @param slope the slope
     */
    public void setSlope(Float slope) {
        this.slope = slope;
    }

    /**
     * Gets point.
     *
     * @return the point
     */
    public Vector2D getPoint() {
        return point;
    }

    /**
     * Sets point.
     *
     * @param point the point
     */
    public void setPoint(Vector2D point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Line{" +
                "slope=" + slope +
                ", point=" + point +
                '}';
    }
}
