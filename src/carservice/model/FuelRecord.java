package carservice.model;

import java.time.LocalDate;

public class FuelRecord {
    private int id;
    private int carId;
    private LocalDate date;
    private int mileage;
    private double liters;
    private double pricePerLiter;
    private String stationName;

    public FuelRecord(int id, int carId, LocalDate date, int mileage, double liters, double pricePerLiter, String stationName) {
        this.id = id;
        this.carId = carId;
        this.date = date;
        this.mileage = mileage;
        this.liters = liters;
        this.pricePerLiter = pricePerLiter;
        this.stationName = stationName;
    }

    public int getId() { return id; }
    public int getCarId() { return carId; }
    public LocalDate getDate() { return date; }
    public int getMileage() { return mileage; }
    public double getLiters() { return liters; }
    public double getPricePerLiter() { return pricePerLiter; }
    public String getStationName() { return stationName; }
    public double getTotalCost() { return liters * pricePerLiter; }
}
