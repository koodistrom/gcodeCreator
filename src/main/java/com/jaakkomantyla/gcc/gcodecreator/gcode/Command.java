package com.jaakkomantyla.gcc.gcodecreator.gcode;

public class  Command {
    private Code code;
    private Float x;
    private Float y;
    private Float z;
    private Float i;
    private Float j;

    public Command(Code code, Float x, Float y, Float z, Float i, Float j) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.z = z;
        this.i = i;
        this.j = j;
    }

    public Command(Code code, Float x, Float y, Float i, Float j) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }

    public Command(Code code, Float x, Float y, Float z) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Command(Code code, Float x, Float y) {
        this.code = code;
        this.x = x;
        this.y = y;
    }





    public Command(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Float getI() {
        return i;
    }

    public void setI(Float i) {
        this.i = i;
    }

    public Float getJ() {
        return j;
    }

    public void setJ(Float j) {
        this.j = j;
    }

    @Override
    public String toString() {
        String str = code + " ";
        if(x!=null){str+="X"+x+" ";}
        if(y!=null){str+="Y"+y+" ";}
        if(z!=null){str+="Z"+z+" ";}
        if(i!=null){str+="I"+i+" ";}
        if(j!=null){str+="J"+j+" ";}
        return str;
    }
}