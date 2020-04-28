package com.jaakkomantyla.gcc.gcodecreator.gcode;

/**
 * The enum Code represents codes given to cnc machines.
 */
public enum Code {

    /**
     * G 00 code. rapid move
     */
    G00, //rapid move
    /**
     * G 01 code. line to
     */
    G01, //line to
    /**
     * G 02 code. clockwise arc
     */
    G02, //clockwise arc
    /**
     * G 03 code. ccw arc
     */
    G03, //ccw arc
    /**
     * G 20 code. metric
     */
    G20, //metric
    /**
     * G 21 code. imperial
     */
    G21, //imperial
    /**
     * F code. feedrate
     */
    F //feedrate
}
