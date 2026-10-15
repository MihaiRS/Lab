package Controller;

import model.Vehicle;
import Repo.RepositoryException;
import Repo.VehicleRepository;

public class ParkingController {
    private final VehicleRepository repository;

    public ParkingController(VehicleRepository repository) {
        this.repository = repository;
    }

    public void addVehicle(Vehicle vehicle) throws RepositoryException {
        repository.add(vehicle);
    }

    public void removeVehicle(int index) throws RepositoryException {
        repository.remove(index);
    }

    public Vehicle[] getVehiclesByColor(String color) {
        Vehicle[] all = repository.getAll();

        int count = 0;
        for (Vehicle i : all)
            if (i.hasColor(color)) count++;

        Vehicle[] result = new Vehicle[count];

        int j = 0;
        for (Vehicle i : all) {
            if (i.hasColor(color)) {
                result[j] = i;
                j++;
            }
        }
        return result;
    }
}