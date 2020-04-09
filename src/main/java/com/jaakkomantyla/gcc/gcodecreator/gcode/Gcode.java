package com.jaakkomantyla.gcc.gcodecreator.gcode;

import java.util.LinkedList;
import java.util.List;

public class Gcode {
    private List<Command> commands;
    private Command currentCommand;
    private float currentX;
    private float currentY;
    private float currentZ;

    public Gcode(){
        commands = new LinkedList<Command>();
        currentX = 0;
        currentY =0;
        currentZ = 0;
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

    public void printCommands(){
        commands.forEach(command -> {
            System.out.println(command+"\n");
        });
    }
}
