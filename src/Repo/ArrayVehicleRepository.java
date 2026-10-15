package Repo;

import model.Vehicle;

public class ArrayVehicleRepository implements VehicleRepository{
    private final Vehicle[] v;
    private int size;

    public ArrayVehicleRepository(int capacity){
        this.v = new Vehicle[capacity];
        this.size=0;
    }

    @Override
    public void add(Vehicle vehicle)throws RepositoryException{
        if (vehicle == null){
            throw new RepositoryException("Vehicle cannot be null;");
        }
        if (size == v.length) throw new RepositoryException("Parking is full;");
        v[size++] = vehicle;
    }

    @Override
    public void remove(int index) throws RepositoryException{
        if (index<0 || index>=size ) throw new RepositoryException("Cannot remove vehicle;");
        for (int i=index;i<size-1;i++)
        {
            v[i] = v[i + 1];
        }
        v[size - 1] = null;
        size--;
    }

    @Override
    public Vehicle[] getAll() {
        Vehicle[] copy = new Vehicle[size];
        for (int i = 0; i < size; i++) {
            copy[i] = v[i];
        }
        return copy;
    }

    @Override
    public int size() { return size; }
}