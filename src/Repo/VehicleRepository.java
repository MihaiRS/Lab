package Repo;

import model.Vehicle;

public interface VehicleRepository {
    void add(Vehicle vehicle) throws RepositoryException;
    void remove(int index) throws RepositoryException;
    Vehicle[] getAll();
    int size();
}
