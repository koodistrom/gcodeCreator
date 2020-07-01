package com.jaakkomantyla.gcc.gcodecreator.gcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    private List<Command> header;
    private List<Command> footer;
    private Options options;
    private final String fileInfo = "This file was generated with Gcode Generator";

    /**
     * Instantiates a new Gcode.
     */
    public Gcode(){
        commands = new LinkedList<Command>();

        commands.add(new Command(fileInfo));
        currentX = 0;
        currentY =0;
        currentZ = 0;
        options = new Options();
        options.setFeed(4000);
        options.setWorkDepth(0);
        options.setMoveDepth(2);
        header = new LinkedList<Command>();
        footer = new LinkedList<Command>();
        createDefaultHeader(options);
        createDefaultFooter(options);
    }

    /**
     * Instantiates a new Gcode.
     *
     * @param options the options
     */
    public Gcode(Options options){
        commands = new LinkedList<Command>();
        header = new LinkedList<Command>();
        footer = new LinkedList<Command>();
        this.options = options;
        currentX = 0;
        currentY =0;
        currentZ = 0;
        createDefaultHeader(options);
        createDefaultFooter(options);
    }

    public void createDefaultHeader(Options options){
        header.add(new Command("%"));
        header.add(new Command(fileInfo));
        if(options.getUnits().equals("mm")){
            header.add(new Command(Code.G21));
        }else{
            header.add(new Command(Code.G20));
        }

    }

    public void createDefaultFooter(Options options){
        Command rise = new Command(Code.G00);
        rise.setZ(getzMoveHeight());
        footer.add(rise);
        footer.add(new Command(Code.G00,0f,0f));
        footer.add(new Command("%"));
    }

    /**
     * Adds a command to the list and changes current location and speed information. Current information is needed
     * because the current position can affect the next commands, because some of them are relative.
     *
     * @param command the command to add
     */
    public void addCommand(Command command ){
        Code code = command.getCode();
        if(code == Code.G01 || code == Code.G02 || code == Code.G03){
            if(currentCommand== null || currentCommand.getCode() == null  || currentCommand.getCode() != code){
                command.setF(options.getFeed());
            }
        }

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
        setCurrentCommand(command);
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
        return options.getMoveDepth();
    }

    /**
     * Sets move height.
     *
     * @param zMoveHeight the z move height
     */
    public void setzMoveHeight(float zMoveHeight) {
        this.options.setMoveDepth(zMoveHeight);
    }

    /**
     * Gets work height.
     *
     * @return the work height
     */
    public float getzWorkHeight() {
        return options.getWorkDepth();
    }

    /**
     * Sets work height.
     *
     * @param zWorkHeight the z work height
     */
    public void setzWorkHeight(float zWorkHeight) {
        this.options.setWorkDepth(zWorkHeight);
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
        String str = commandsAsString(header);
        str +=  commandsAsString(commands);
        str +=  commandsAsString(footer);
        System.out.println(str);
    }

    /**
     * Returns Commands as a string. This string can be saved to file and run on a CNC-machine.
     *
     * @return the string
     */
    public String commandsAsString(List<Command> commands){
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
        String str = commandsAsString(header);
        str +=  commandsAsString(commands);
        str +=  commandsAsString(footer);
        return str.getBytes();
    }

    public void saveToFile(String filePath){

        File file = new File(filePath);
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(commandsAsByteArray());
            System.out.println("File successfully wrote");
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
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
