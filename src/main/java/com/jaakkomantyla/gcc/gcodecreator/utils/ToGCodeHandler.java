package com.jaakkomantyla.gcc.gcodecreator.utils;

import com.jaakkomantyla.gcc.gcodecreator.gcode.Code;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Command;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Gcode;
import org.apache.batik.parser.DefaultPathHandler;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

public class ToGCodeHandler extends DefaultPathHandler {
    private Gcode gCode;
    private float curveAprTolerance;
    private float curveSamplingStep;
    private float pathStartX;
    private float pathStartY;

    public ToGCodeHandler(Gcode gCode) {
        super();
        this.gCode = gCode;
        curveAprTolerance = 0.5f;
        curveSamplingStep = 10;

    }

    public void startPath(){
        pathStartX = gCode.getCurrentX();
        pathStartY = gCode.getCurrentY();
    }

    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) {

    }

    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) {

    }

    public void closePath() {
        gCode.addCommand(new Command(Code.G01, pathStartX, pathStartY));
    }

    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y){
        System.out.println("hadler method called");
        float curX = gCode.getCurrentX();
        float curY = gCode.getCurrentY();
        Vector2D p1 = new Vector2D(gCode.getCurrentX(),gCode.getCurrentY());
        Vector2D c1 = new Vector2D(curX+x1,curY+y1);
        Vector2D c2 = new Vector2D(curX+x2,curY+y2);
        Vector2D p2 = new Vector2D(curX+x,curY+y);
        addBezierToGcode(new CubicBezier(p1,c1,c2,p2));

    }

    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) {

        Vector2D p1 = new Vector2D(gCode.getCurrentX(),gCode.getCurrentY());
        Vector2D c1 = new Vector2D(x1,y1);
        Vector2D c2 = new Vector2D(x2,y2);
        Vector2D p2 = new Vector2D(x,y);

        addBezierToGcode(new CubicBezier(p1,c1,c2,p2));

    };

    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y){

    }

    public void 	curvetoCubicSmoothRel(float x2, float y2, float x, float y){

    }

    public void 	curvetoQuadraticAbs(float x1, float y1, float x, float y){
        Vector2D p1 = new Vector2D(gCode.getCurrentX(),gCode.getCurrentY());
        Vector2D c1 = new Vector2D(x1,y1);
        Vector2D p2 = new Vector2D(x,y);

        CubicBezier cb = CubicBezier.fromQuadratic(p1,c1,p2);
        addBezierToGcode(cb);
    }

    public void 	curvetoQuadraticRel(float x1, float y1, float x, float y){
        System.out.println("hadler method called");
        float curX = gCode.getCurrentX();
        float curY = gCode.getCurrentY();
        Vector2D p1 = new Vector2D(gCode.getCurrentX(),gCode.getCurrentY());
        Vector2D c1 = new Vector2D(curX+x1,curY+y1);
        Vector2D p2 = new Vector2D(curX+x,curY+y);

        CubicBezier cb = CubicBezier.fromQuadratic(p1,c1,p2);
        addBezierToGcode(cb);
    }

    public void 	curvetoQuadraticSmoothAbs(float x, float y){

    }

    public void 	curvetoQuadraticSmoothRel(float x, float y){

    }

    public void 	linetoAbs(float x, float y){
        gCode.addCommand(new Command(Code.G01, x, y));
    }
    public void 	linetoHorizontalAbs(float x){
        gCode.addCommand(new Command(Code.G01, x, gCode.getCurrentY()));
    }
    public void 	linetoHorizontalRel(float x){
        gCode.addCommand(new Command(Code.G01, x + gCode.getCurrentX(), gCode.getCurrentY()));
    }
    public void 	linetoRel(float x, float y){
        gCode.addCommand(new Command(Code.G01, x + gCode.getCurrentX(), y+gCode.getCurrentY()));
    }
    public void 	linetoVerticalAbs(float y){
        gCode.addCommand(new Command(Code.G01, gCode.getCurrentX(), y));
    }
    public void 	linetoVerticalRel(float y){
        gCode.addCommand(new Command(Code.G01, gCode.getCurrentX(), y+gCode.getCurrentY()));
    }

    public void movetoAbs(float x, float y){
        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzMoveHeight()));
        gCode.addCommand(new Command(Code.G00, x, y));
        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzWorkHeight()));
        pathStartX = gCode.getCurrentX();
        pathStartY = gCode.getCurrentY();
    };

    public void movetoRel(float x, float y){

        gCode.addCommand(new Command(Code.G00, x+gCode.getCurrentX(), y+gCode.getCurrentY()));
        pathStartX = gCode.getCurrentX();
        pathStartY = gCode.getCurrentY();
    }
    public void endPath(){

    };

    private void addArcToGcode(Arc arc){
        Code code = arc.isClockwise() ? Code.G02 : Code.G03;
        float x =  (float) arc.getEnd().getX();
        float y = (float) arc.getEnd().getY();
        float i = (float) arc.getCenter().getX()-gCode.getCurrentX();
        float j = (float) arc.getCenter().getY()-gCode.getCurrentY();
        Command cmd = new Command(code, x, y, i, j);
        gCode.addCommand(cmd);

    }

    private void addBezierToGcode(CubicBezier cb){
        List<BiArc> biArcs = BezierToArcs.ApproxCubicBezier(cb, curveSamplingStep, curveAprTolerance);

        biArcs.forEach(biArc -> {
            addArcToGcode(biArc.getArc1());
            addArcToGcode(biArc.getArc2());
        });
    }


}
