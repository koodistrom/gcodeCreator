package com.jaakkomantyla.gcc.gcodecreator.gcode;

import java.util.Locale;

/**
 * The Command class represents command written to gcode file that specifies a operation on CNC-machine.
 * Command holds an enum Code and parameters (coordinates and speeds) and possibly a comment
 */
public class  Command {
    private Code code;
    private Float x;
    private Float y;
    private Float z;
    private Float i;
    private Float j;
    private Float f;
    private String comment;

    /**
     * Instantiates a new Command.
     *
     * @param code the code
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param z    the z coordinate
     * @param i    the i x offset for arc centers
     * @param j    the j y offset for arc centers
     * @param f    the f feed speed
     */
    public Command(Code code, Float x, Float y, Float z, Float i, Float j, Float f) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.z = z;
        this.i = i;
        this.j = j;
        this.f = f;
    }

    /**
     * Instantiates a new Command.
     *
     * @param code the code
     * @param x    the x
     * @param y    the y
     * @param i    the
     * @param j    the j
     */
    public Command(Code code, Float x, Float y, Float i, Float j) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }

    /**
     * Instantiates a new Command.
     *
     * @param code the code
     * @param x    the x
     * @param y    the y
     * @param z    the z
     */
    public Command(Code code, Float x, Float y, Float z) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Instantiates a new Command.
     *
     * @param code the code
     * @param x    the x
     * @param y    the y
     */
    public Command(Code code, Float x, Float y) {
        this.code = code;
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a new Command.
     *
     * @param code the code
     * @param fValue    the f
     */
    public Command(Code code, Float fValue) {
        this.code = code;
        this.f = fValue;
    }

    /**
     * Instantiates a new Command.
     *
     * @param comment the comment
     */
    public Command(String comment){
        this.comment = comment;
    }


    /**
     * Instantiates a new Command.
     *
     * @param code the code
     */
    public Command(Code code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Code getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(Code code) {
        this.code = code;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public Float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public Float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public Float getZ() {
        return z;
    }

    /**
     * Sets z.
     *
     * @param z the z
     */
    public void setZ(Float z) {
        this.z = z;
    }

    /**
     * Gets i.
     *
     * @return the i
     */
    public Float getI() {
        return i;
    }

    /**
     * Sets i.
     *
     * @param i the
     */
    public void setI(Float i) {
        this.i = i;
    }

    /**
     * Gets j.
     *
     * @return the j
     */
    public Float getJ() {
        return j;
    }

    /**
     * Sets j.
     *
     * @param j the j
     */
    public void setJ(Float j) {
        this.j = j;
    }

    /**
     * Gets f.
     *
     * @return the f
     */
    public Float getF() { return f; }

    /**
     * Sets f.
     *
     * @param f the f
     */
    public void setF(Float f) { this.f = f; }

    /**
     * To string returns the command as string in the format that it is written to gcode file:
     * Code (see Code enum) is printed first and each parameter is represented by uppercase letter and the numeric value
     *
     */
    @Override
    public String toString() {
        String str;
        if(code != null){
            str = code + " ";
            if(x!=null){str+="X"+String.format(Locale.ROOT,"%.4f",x)+" ";}
            if(y!=null){str+="Y"+String.format(Locale.ROOT,"%.4f",y)+" ";}
            if(z!=null){str+="Z"+String.format(Locale.ROOT,"%.4f",z)+" ";}
            if(i!=null){str+="I"+String.format(Locale.ROOT,"%.4f",i)+" ";}
            if(j!=null){str+="J"+String.format(Locale.ROOT,"%.4f",j)+" ";}
            if(f!=null){str+="F"+f+" ";}
        }else{
            str = "("+comment+")";
        }

        return str;
    }
}
