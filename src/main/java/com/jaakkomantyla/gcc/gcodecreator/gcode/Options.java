package com.jaakkomantyla.gcc.gcodecreator.gcode;

/**
 * The Options class holds information given by the user that affect the creation of gcode file from svg.
 */
public class Options {
    private float moveDepth;
    private float workDepth;
    private String units;
    private float feed;


    /**
     * Instantiates a new Options.
     */
    public Options() {
        this.units = "mm";
    }

    /**
     * Gets move depth. A Z-height value used for the movement from a one path to another when the machine
     * isn't cutting/drawing
     *
     * @return the move depth
     */
    public float getMoveDepth() {
        return moveDepth;
    }

    /**
     * Sets move depth.  A Z-height value used for the movement from a one path to another when the machine
     * isn't cutting/drawing
     *
     * @param moveDepth the move depth
     */
    public void setMoveDepth(float moveDepth) {
        this.moveDepth = moveDepth;
    }

    /**
     * Gets work depth.  A Z-height value used for the movement when the machine
     * is cutting/drawing
     *
     * @return the work depth
     */
    public float getWorkDepth() {
        return workDepth;
    }

    /**
     * Sets work depth.   A Z-height value used for the movement when the machine
     * is cutting/drawing
     *
     * @param workDepth the work depth
     */
    public void setWorkDepth(float workDepth) {
        this.workDepth = workDepth;
    }

    /**
     * Gets units. mm or inch
     *
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * Sets units.  mm or inch
     *
     * @param units the units
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Gets feed. The speed used when cutting/drawing
     *
     * @return the feed
     */
    public float getFeed() {
        return feed;
    }

    /**
     * Sets feed.The speed used when cutting/drawing
     *
     * @param feed the feed
     */
    public void setFeed(float feed) {
        this.feed = feed;
    }


}
