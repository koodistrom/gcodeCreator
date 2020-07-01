package com.jaakkomantyla.gcc.gcodecreator.utils;

import com.jaakkomantyla.gcc.gcodecreator.gcode.Code;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Command;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Gcode;
import org.apache.batik.parser.DefaultPathHandler;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.List;

/**
 * ToGCodeHandler extends Apache's Batik Parser's DefaultPathHandler and is used when iterating over svg file.
 * The class holds methods to handle information of the path elements in svgs. Class uses the utils classes that
 * converts Bezier curves to arcs to convert svg data to suitable format for gcode. Then it creates Commands
 * and adds them to Gcode object.
 */
public class ToGCodeHandler extends DefaultPathHandler {
    private Gcode gCode;
    private float curveAprTolerance;
    private float curveSamplingStep;
    private float pathStartX;
    private float pathStartY;
    private float previousX;
    private float previousY;
    /**
     * Instantiates a new  ToGCodeHandler.
     *
     * @param gCode the Gcode object to add the generated commands in.
     */
    public ToGCodeHandler(Gcode gCode) {
        super();
        this.gCode = gCode;
        curveAprTolerance = 0.5f;
        curveSamplingStep = 8;

    }
    @Override
    public void startPath(){
        previousX = 0;
        previousY = 0;
        pathStartY = 0;
        pathStartX = 0;
    }
    @Override
    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) {
        Vector2D previousPoint = new Vector2D(previousX, previousY);
        int largeArc = largeArcFlag? 1:0;
        int sweep = sweepFlag? 1:0;
        EllipticalArc arc = new EllipticalArc(previousPoint, rx, ry, xAxisRotation, largeArc, sweep, x, y);
        List<CubicBezier> beziers = arc.toCubicBezier();
        beziers.forEach( curve -> addBezierToGcode(curve));
    }
    @Override
    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) {
        Vector2D previousPoint = new Vector2D(previousX, previousY);
        int largeArc = largeArcFlag? 1:0;
        int sweep = sweepFlag? 1:0;
        EllipticalArc arc = new EllipticalArc(previousPoint, rx, ry, xAxisRotation, largeArc, sweep, x+previousX, y+previousY);
        List<CubicBezier> beziers = arc.toCubicBezier();
        beziers.forEach( curve -> addBezierToGcode(curve));
    }
    @Override
    public void closePath() {
        gCode.addCommand(new Command(Code.G01, pathStartX, pathStartY));
    }

    @Override
    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y){


        Vector2D p1 = new Vector2D(previousX,previousY);
        Vector2D c1 = new Vector2D(previousX+x1,previousY+y1);
        Vector2D c2 = new Vector2D(previousX+x2,previousY+y2);
        Vector2D p2 = new Vector2D(previousX+x,previousY+y);
        addBezierToGcode(new CubicBezier(p1,c1,c2,p2));

    }

    @Override
    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) {

        Vector2D p1 = new Vector2D(previousX,previousY);
        Vector2D c1 = new Vector2D(x1,y1);
        Vector2D c2 = new Vector2D(x2,y2);
        Vector2D p2 = new Vector2D(x,y);

        addBezierToGcode(new CubicBezier(p1,c1,c2,p2));

    };

    @Override
    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y){

    }

    @Override
    public void 	curvetoCubicSmoothRel(float x2, float y2, float x, float y){

    }

    @Override
    public void 	curvetoQuadraticAbs(float x1, float y1, float x, float y){
        Vector2D p1 = new Vector2D(previousX,previousY);
        Vector2D c1 = new Vector2D(x1,y1);
        Vector2D p2 = new Vector2D(x,y);

        CubicBezier cb = CubicBezier.fromQuadratic(p1,c1,p2);
        addBezierToGcode(cb);
    }

    @Override
    public void 	curvetoQuadraticRel(float x1, float y1, float x, float y){
        System.out.println("hadler method called");
        Vector2D p1 = new Vector2D(previousX,previousY);
        Vector2D c1 = new Vector2D(previousX+x1,previousY+y1);
        Vector2D p2 = new Vector2D(previousX+x,previousY+y);

        CubicBezier cb = CubicBezier.fromQuadratic(p1,c1,p2);
        addBezierToGcode(cb);
    }

    @Override
    public void 	curvetoQuadraticSmoothAbs(float x, float y){

    }

    @Override
    public void 	curvetoQuadraticSmoothRel(float x, float y){

    }

    @Override
    public void 	linetoAbs(float x, float y){
        gCode.addCommand(new Command(Code.G01, x, y));
        setPreviousPoints();
    }
    @Override
    public void 	linetoHorizontalAbs(float x){
        gCode.addCommand(new Command(Code.G01, x, previousY));
        setPreviousPoints();
    }
    @Override
    public void 	linetoHorizontalRel(float x){
        gCode.addCommand(new Command(Code.G01, x + previousX, previousY));
        setPreviousPoints();
    }
    @Override
    public void 	linetoRel(float x, float y){
        gCode.addCommand(new Command(Code.G01, x + previousX, y+previousY));
        setPreviousPoints();
    }
    @Override
    public void 	linetoVerticalAbs(float y){
        gCode.addCommand(new Command(Code.G01, previousX, y));
        setPreviousPoints();
    }
    @Override
    public void 	linetoVerticalRel(float y){
        gCode.addCommand(new Command(Code.G01, previousX, y+previousY));
        setPreviousPoints();
    }
    @Override
    public void movetoAbs(float x, float y){
        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzMoveHeight()));
        gCode.addCommand(new Command(Code.G00, x, y));
        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzWorkHeight()));
        setPreviousPoints();
        pathStartX = previousX;
        pathStartY = previousY;
    };
    @Override
    public void movetoRel(float x, float y){

        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzMoveHeight()));
        gCode.addCommand(new Command(Code.G00, x+previousX, y+previousY));
        gCode.addCommand(new Command(Code.G00, null, null, gCode.getzWorkHeight()));
        setPreviousPoints();
        pathStartX = previousX;
        pathStartY = previousY;
        /*svg on paskaa jostain syystä ilmeisesti m relative toimii samoin kuin M absoluuttinen joten
        kommentoin nämä pois ja kokeilen tuikata tuon absoluuttisen tähän

        gCode.addCommand(new Command(Code.G00, x+gCode.getCurrentX(), y+gCode.getCurrentY()));
        pathStartX = gCode.getCurrentX();
        pathStartY = gCode.getCurrentY();

         */
    }
    @Override
    public void endPath(){

    };

    private void addArcToGcode(Arc arc){
        Code code = arc.isClockwise() ? Code.G03 : Code.G02;
        float x =  (float) arc.getEnd().getX();
        float y = (float) arc.getEnd().getY();
        float i = (float) arc.getCenter().getX()-previousX;
        float j = (float) arc.getCenter().getY()-previousY;
        Command cmd = new Command(code, x, y, i, j);
        gCode.addCommand(cmd);
        setPreviousPoints();
    }

    private void addBezierToGcode(CubicBezier cb){
        List<BiArc> biArcs = BezierToArcs.ApproxCubicBezier(cb, curveSamplingStep, curveAprTolerance);

        biArcs.forEach(biArc -> {
            addArcToGcode(biArc.getArc1());
            addArcToGcode(biArc.getArc2());
        });
    }


    private void setPreviousPoints(){
        previousY = gCode.getCurrentY();
        previousX = gCode.getCurrentX();
    }
}
