package com.jaakkomantyla.gcc.gcodecreator.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
// http://dlacko.org/blog/2016/10/19/approximating-bezier-curves-by-biarcs/
public class BezierToArcs {


    private static boolean IsRealInflexionPoint(Complex t)
    {
        return t.getImaginary() == 0 && t.getReal() > 0 && t.getReal() < 1;
    }

    public static List<BiArc> ApproxCubicBezier(CubicBezier bezier, float samplingStep, float tolerance)
    {
        // The result will be put here
        List<BiArc> biarcs = new LinkedList<BiArc>();

        // The bezier curves to approximate
        Stack<CubicBezier> curves = new Stack<CubicBezier>();
        curves.push(bezier);

        // ---------------------------------------------------------------------------
        // First, calculate the inflexion points and split the bezier at them (if any)

        CubicBezier toSplit = curves.pop();
        List<Complex> inflex = toSplit.inflexionPoints();

        boolean i1 = IsRealInflexionPoint(inflex.get(0));
        boolean i2 = IsRealInflexionPoint(inflex.get(1));

        List<CubicBezier> splited;

        if (i1 && !i2)
        {
            splited = toSplit.split((float)inflex.get(0).getReal());
            curves.push(splited.get(1));
            curves.push(splited.get(0));
        }
        else if (!i1 && i2)
        {
            splited = toSplit.split((float)inflex.get(1).getReal());
            curves.push(splited.get(1));
            curves.push(splited.get(0));
        }
        else if (i1 && i2)
        {
            float t1 = (float)inflex.get(0).getReal();
            float t2 = (float)inflex.get(1).getReal();

            // I'm not sure if I need, but it does not hurt to order them
            if (t1 > t2)
            {
                float tmp = t1;
                t1 = t2;
                t2 = tmp;
            }

            // Make the first split and save the first new curve. The second one has to be splitted again
            // at the recalculated t2 (it is on a new curve)

            List<CubicBezier> splited1 = toSplit.split(t1);

            t2 = (1 - t1) * t2;

            toSplit = splited1.get(1);
            List<CubicBezier> splited2 = toSplit.split(t2);

            curves.push(splited2.get(1));
            curves.push(splited2.get(0));
            curves.push(splited1.get(0));
        }
        else
        {
            curves.push(toSplit);
        }

        // ---------------------------------------------------------------------------
        // Second, approximate the curves until we run out of them

        while (curves.size() > 0)
        {
            bezier = curves.pop();

            // ---------------------------------------------------------------------------
            // Calculate the transition point for the BiArc

            // tangentIntersect: Intersection point of tangent lines
            Line T1 = new Line(bezier.getP1(), bezier.getC1());
            Line T2 = new Line(bezier.getP2(), bezier.getC2());
            Vector2D tangentIntersect = T1.Intersection(T2);

            // triangleCenter: incenter point of the triangle (P1, tangentIntersect, P2)
            // http://www.mathopenref.com/coordincenter.html
            double dP2V = Vector2D.distance(bezier.getP2(), tangentIntersect);
            double dP1V = Vector2D.distance(bezier.getP1(), tangentIntersect);
            double dP1P2 = Vector2D.distance(bezier.getP1(), bezier.getP2());
            Vector2D triangleCenter = bezier.getP1().scalarMultiply(dP2V)
                .add(bezier.getP2().scalarMultiply(dP1V)
                .add(tangentIntersect.scalarMultiply(dP1P2))).scalarMultiply(1 / (dP2V + dP1V + dP1P2));

            // ---------------------------------------------------------------------------
            // Calculate the BiArc

            BiArc biarc = new BiArc(bezier.getP1(), (bezier.getP1().subtract( bezier.getC1())),
                bezier.getP2(), (bezier.getP2().subtract(bezier.getC2())), triangleCenter);

            // ---------------------------------------------------------------------------
            // Calculate the maximum error

            float maxDistance = 0f;
            float maxDistanceAt = 0f;

            float nrPointsToCheck = biarc.length() / samplingStep;
            float parameterStep = 1f / nrPointsToCheck;

            for (int i = 0; i <= nrPointsToCheck; i++)
            {
                float t = parameterStep * i;
                Vector2D u1 = biarc.pointAt(t);
                Vector2D u2 = bezier.pointAt(t);
                float distance = (float)(u1.subtract(u2)).getNorm();

                if (distance > maxDistance)
                {
                    maxDistance = distance;
                    maxDistanceAt = t;
                }
            }

            // Check if the two curves are close enough
            if (maxDistance > tolerance)
            {
                // If not, split the bezier curve the point where the distance is the maximum
                // and try again with the two halfs
                List<CubicBezier> bs = bezier.split(maxDistanceAt);
                curves.push(bs.get(1));
                curves.push(bs.get(0));
            }
            else
            {
                // Otherwise we are done with the current bezier
                biarcs.add(biarc);
            }
        }

        return biarcs;
    }


}
