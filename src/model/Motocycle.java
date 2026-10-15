package model;

public class Motocycle implements Vehicle {
    private final String plate;
    private final String color;

    public Motocycle(String color, String plate) {
        this.color = color;
        this.plate = plate;

    }
    @Override
    public String getColor() { return color; }

    @Override
    public boolean hasColor(String color) {
        return this.color.equalsIgnoreCase(color);
    }

    @Override
    public String toString() { return "Motocycle{" + color + ", " + plate + "}"; }

}
