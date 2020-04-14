package com.jaakkomantyla.gcc.gcodecreator.gcode;

import java.util.LinkedList;
import java.util.List;

public class Gcode {
    private List<Command> commands;
    private Command currentCommand;
    private float currentX;
    private float currentY;
    private float currentZ;
    private float currentF;
    private float zMoveHeight;
    private float zWorkHeight;

    public Gcode(){
        commands = new LinkedList<Command>();
        commands.add(new Command("This file was generated with Gcode Generator"));
        currentX = 0;
        currentY =0;
        currentZ = 0;
        zMoveHeight = 2;
        zWorkHeight = 0;

    }

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

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public Command getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(Command currentCommand) {
        this.currentCommand = currentCommand;
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getCurrentZ() {
        return currentZ;
    }

    public void setCurrentZ(float currentZ) {
        this.currentZ = currentZ;
    }

    public float getzMoveHeight() {
        return zMoveHeight;
    }

    public void setzMoveHeight(float zMoveHeight) {
        this.zMoveHeight = zMoveHeight;
    }

    public float getzWorkHeight() {
        return zWorkHeight;
    }

    public void setzWorkHeight(float zWorkHeight) {
        this.zWorkHeight = zWorkHeight;
    }

    public float getCurrentF() {
        return currentF;
    }

    public void setCurrentF(float currentF) {
        this.currentF = currentF;
    }

    public void printCommands(){
        commands.forEach(command -> {
            System.out.println(command);
        });
    }

    public String commandsAsString(){
        StringBuilder stringBuilder = new StringBuilder();

        commands.forEach(command -> {
            stringBuilder.append(command.toString()+"\n");
        });
        return stringBuilder.toString();
    }

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
