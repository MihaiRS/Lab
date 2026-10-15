package model;

public class Bicycle implements Vehicle {
    private final String color;

    public Bicycle(String color) {
        this.color = color;
    }
    @Override
    public String getColor() { return color; }

    @Override
    public boolean hasColor(String color) {
        return this.color.equalsIgnoreCase(color);
    }

    @Override
    public String toString() { return "Bicycle{" + color + "}";}

}