

import Controller.ParkingController;
import model.Bicycle;
import model.Car;
import model.Motocycle;
import model.Vehicle;
import Repo.ArrayVehicleRepository;
import Repo.RepositoryException;
import Repo.VehicleRepository;

public class Main {
    public static void main(String[] args) {
        VehicleRepository repo = new ArrayVehicleRepository(5);
        ParkingController controller = new ParkingController(repo);

        try {
            controller.addVehicle(new Car("Red", "CJ01ABC"));
            controller.addVehicle(new Bicycle("Blue"));
            controller.addVehicle(new Motocycle("RED", "CJ02XYZ"));
            controller.addVehicle(new Car("Green", "CJ03DEF"));
        } catch (RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            controller.removeVehicle(1);
        } catch (RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }
        printByColor(controller, "blue");
        printByColor(controller, "red");
        printByColor(controller, "green");

        try {
            controller.addVehicle(new Bicycle("Red"));
            controller.addVehicle(new Car("Black", "CJ04GHI"));
        } catch (RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            controller.removeVehicle(10);
        } catch (RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }


    private static void printByColor(ParkingController controller, String color) {
        Vehicle[] found = controller.getVehiclesByColor(color);
        System.out.println(color + " vehicles: " + found.length);
        for (Vehicle v : found) {
            System.out.println(v);
        }
    }
}