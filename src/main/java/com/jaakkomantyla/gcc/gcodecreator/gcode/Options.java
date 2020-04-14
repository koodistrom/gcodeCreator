package com.jaakkomantyla.gcc.gcodecreator.gcode;

public class Options {
    private float moveDepth;
    private float workDepth;
    private String units;
    private float feed;


    public Options() {
    }

    public float getMoveDepth() {
        return moveDepth;
    }

    public void setMoveDepth(float moveDepth) {
        this.moveDepth = moveDepth;
    }

    public float getWorkDepth() {
        return workDepth;
    }

    public void setWorkDepth(float workDepth) {
        this.workDepth = workDepth;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public float getFeed() {
        return feed;
    }

    public void setFeed(float feed) {
        this.feed = feed;
    }


}
