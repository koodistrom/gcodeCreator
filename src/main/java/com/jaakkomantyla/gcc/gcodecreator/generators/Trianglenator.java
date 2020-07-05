package com.jaakkomantyla.gcc.gcodecreator.generators;

import com.jaakkomantyla.gcc.gcodecreator.gcode.Code;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Command;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Gcode;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Trianglenator {
    public static void main(String[] args) {
        createGcode(350, 250);
    }
    public static void Triaglenate(){

    }

    private static void createGcode(int height, int width){
        Gcode gCode = new Gcode();
        moveToStart(gCode, height, width);
        createTriangles(gCode,height,width);
        end(gCode);
        System.out.println(gCode);
        gCode.saveToFile("C:\\Users\\Jaakko\\Desktop\\satunnaista\\picassomatic\\piirrettävät\\gcodes\\triangles.gcode");
    }

    private static void moveToStart(Gcode gCode, float height, float width){
        gCode.addCommand(new Command(Code.G00, null, null, 2.0f));
        gCode.addCommand(new Command(Code.G00, width/1.6f, height/1.6f, null));
        gCode.addCommand(new Command(Code.G00, null, null, 0.0f));
    }

    private static void end(Gcode gcode){
        gcode.addCommand(new Command(Code.G00, null, null, 2.0f));
        gcode.addCommand(new Command(Code.G00, 0f, 0f, null));
    }

    private static void createTriangles(Gcode gcode, float height, float width){
        float triangleSide = height/30;
        float triangleHeight = (float) Math.sqrt(triangleSide*triangleSide-(triangleSide/2)*(triangleSide/2));
        Vector2D loc = new Vector2D(gcode.getCurrentX(),gcode.getCurrentY());
        gcode.printCommands();
        while (loc.getX()>0&&loc.getY()>0&&loc.getX()<width&&loc.getY()<height){
            int random = (int) (Math.random()*6);
            loc = moveToDirection(random, loc, triangleSide, triangleHeight);
            gcode.addCommand(new Command(Code.G01, (float) loc.getX(),(float) loc.getY()));
            loc = moveToDirection((random+2)%6, loc, triangleSide, triangleHeight);
            gcode.addCommand(new Command(Code.G01, (float) loc.getX(),(float) loc.getY()));
        }

    }

    private static Vector2D moveToDirection(int direction, Vector2D loc, float triangleSide, float triangleHeight){
        float x = (float) loc.getX();
        float y = (float) loc.getY();
        switch (direction){
            case 0:
                x += triangleSide;
                break;
            case 1:
                x += triangleSide/2;
                y -= triangleHeight;
                break;
            case 2:
                x -= triangleSide/2;
                y -= triangleHeight;
                break;
            case 3:
                x -= triangleSide;
                break;
            case 4:
                x -= triangleSide/2;
                y += triangleHeight;
                break;
            case 5:
                x += triangleSide/2;
                y += triangleHeight;
                break;
        }
        return new Vector2D(x,y);

    }


}
