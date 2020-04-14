package com.jaakkomantyla.gcc.gcodecreator.gcode;

public enum Code {

    G00, //rapid move
    G01, //line to
    G02, //clockwise arc
    G03, //ccw arc
    G20, //metric
    G21, //imperial
    F //feedrate
}
