package com.jaakkomantyla.gcc.gcodecreator.gcode;

import java.util.LinkedList;
import java.util.List;

/**
 * The Gcode class represents gcode file. It holds list of commands that are printed to create the actual
 * gcode file.
 */
public class Gcode {
    private List<Command> commands;
    private Command currentCommand;
    private float currentX;
    private float currentY;
    private float currentZ;
    private float currentF;
    private float zMoveHeight;
    private float zWorkHeight;

    /**
     * Instantiates a new Gcode.
     */
    public Gcode(){
        commands = new LinkedList<Command>();
        commands.add(new Command("This file was generated with Gcode Generator"));
        currentX = 0;
        currentY =0;
        currentZ = 0;
        zMoveHeight = 2;
        zWorkHeight = 0;

    }

    /**
     * Instantiates a new Gcode.
     *
     * @param options the options
     */
    public Gcode(Options options){
        commands = new LinkedList<Command>();
        commands.add(new Command("This file was generated with Gcode Generator"));
        currentX = 0;
        currentY =0;
        currentZ = 0;
        zMoveHeight = options.getMoveDepth();
        zWorkHeight = options.getWorkDepth();
        if(options.getUnits().equals("mm")){
            commands.add(new Command(Code.G20));
        }else{
            commands.add(new Command(Code.G21));
        }
        addCommand(new Command(Code.G01, options.getFeed()));

    }

    /**
     * Adds a command to the list and changes current location and speed information. Current information is needed
     * because the current position can affect the next commands, because some of them are relative.
     *
     * @param command the command to add
     */
    public void addCommand(Command command ){
        commands.add(command);
        if(command.getX()!=null){
            setCurrentX(command.getX());
        }
        if(command.getY()!=null){
            setCurrentY(command.getY());
        }
        if(command.getZ()!=null){
            setCurrentZ(command.getZ());
        }
        if(command.getF()!=null){
            setCurrentF(command.getF());
        }
    }

    /**
     * Gets commands.
     *
     * @return the commands
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Sets commands.
     *
     * @param commands the commands
     */
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    /**
     * Gets current command.
     *
     * @return the current command
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /**
     * Sets current command.
     *
     * @param currentCommand the current command
     */
    public void setCurrentCommand(Command currentCommand) {
        this.currentCommand = currentCommand;
    }

    /**
     * Gets current x.
     *
     * @return the current x
     */
    public float getCurrentX() {
        return currentX;
    }

    /**
     * Sets current x.
     *
     * @param currentX the current x
     */
    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    /**
     * Gets current y.
     *
     * @return the current y
     */
    public float getCurrentY() {
        return currentY;
    }

    /**
     * Sets current y.
     *
     * @param currentY the current y
     */
    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    /**
     * Gets current z.
     *
     * @return the current z
     */
    public float getCurrentZ() {
        return currentZ;
    }

    /**
     * Sets current z.
     *
     * @param currentZ the current z
     */
    public void setCurrentZ(float currentZ) {
        this.currentZ = currentZ;
    }

    /**
     * Gets move height.
     *
     * @return the move height
     */
    public float getzMoveHeight() {
        return zMoveHeight;
    }

    /**
     * Sets move height.
     *
     * @param zMoveHeight the z move height
     */
    public void setzMoveHeight(float zMoveHeight) {
        this.zMoveHeight = zMoveHeight;
    }

    /**
     * Gets work height.
     *
     * @return the work height
     */
    public float getzWorkHeight() {
        return zWorkHeight;
    }

    /**
     * Sets work height.
     *
     * @param zWorkHeight the z work height
     */
    public void setzWorkHeight(float zWorkHeight) {
        this.zWorkHeight = zWorkHeight;
    }

    /**
     * Gets current f.
     *
     * @return the current f
     */
    public float getCurrentF() {
        return currentF;
    }

    /**
     * Sets current f.
     *
     * @param currentF the current f
     */
    public void setCurrentF(float currentF) {
        this.currentF = currentF;
    }

    /**
     * Print commands.
     */
    public void printCommands(){
        commands.forEach(command -> {
            System.out.println(command);
        });
    }

    /**
     * Returns Commands as a string. This string can be saved to file and run on a CNC-machine.
     *
     * @return the string
     */
    public String commandsAsString(){
        StringBuilder stringBuilder = new StringBuilder();

        commands.forEach(command -> {
            stringBuilder.append(command.toString()+"\n");
        });
        return stringBuilder.toString();
    }

    /**
     * Returns Commands as byte array. Method is used to send the gcodefile to the front end.
     *
     * @return the byte [ ]
     */
    public byte[] commandsAsByteArray(){
        String str = commandsAsString();
        return str.getBytes();
    }

    @Override
    public String toString() {
        return "Gcode{" +
                "commands=" + commands +
                ", currentCommand=" + currentCommand +
                ", currentX=" + currentX +
                ", currentY=" + currentY +
                ", currentZ=" + currentZ +
                '}';
    }
}
