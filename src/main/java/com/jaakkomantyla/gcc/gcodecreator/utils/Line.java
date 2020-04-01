package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Line {
    /// <summary>
    /// Defines a line in point-slope form: y - y1 = m * (x - x1)
    /// Vertical line: m = NaN
    /// Horizontal line: m = 0
    /// </summary>


    private Float slope;
    private Vector2D point;

        /// <summary>
        /// Define a line by two points
        /// </summary>
        /// <param name="P1"></param>
        /// <param name="P2"></param>
        public Line(Vector2D p1, Vector2D p2) {
            this.point = p1;
            this.slope = calculateSlope(p1, p2);
        }

        /// <summary>
        /// Define a line by a point and slope
        /// </summary>
        /// <param name="P"></param>
        /// <param name="m"></param>
        public Line(Vector2D P, Float slope)
        {
            this.point = P;
            this.slope = slope;
        }

        /// <summary>
        /// Calculate the intersection point of this line and another one
        /// </summary>
        /// <param name="l"></param>
        /// <returns></returns>
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

        /// <summary>
        /// Special case, the first one is vertical (we suppose that the other one is not,
        /// otherwise they do not cross)
        /// </summary>
        /// <param name="hl"></param>
        /// <param name="l"></param>
        /// <returns></returns>
        private static Vector2D verticalIntersection(Line vl, Line l)
        {
            double x = vl.getPoint().getX();
            double y = l.getSlope() * (x - l.getPoint().getX()) + l.getPoint().getY();
            return new Vector2D(x, y);
        }

        /// <summary>
        /// Creates a a line which is perpendicular to the line defined by p and p1 and goes through p
        /// </summary>
        /// <param name="p"></param>
        /// <param name="p1"></param>
        /// <returns></returns>
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

    public Float getSlope() {
        return slope;
    }

    public void setSlope(Float slope) {
        this.slope = slope;
    }

    public Vector2D getPoint() {
        return point;
    }

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
