package carservice.model;

import java.time.LocalDate;

public class ServiceRecord {
    private int id;
    private int carId;
    private int serviceTypeId;
    private String serviceTypeName;
    private LocalDate date;
    private int mileage;
    private double cost;
    private String comment;

    public ServiceRecord(int id, int carId, int serviceTypeId, String serviceTypeName, LocalDate date, int mileage, double cost, String comment) {
        this.id = id;
        this.carId = carId;
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
        this.date = date;
        this.mileage = mileage;
        this.cost = cost;
        this.comment = comment;
    }

    public int getId() { return id; }
    public int getCarId() { return carId; }
    public int getServiceTypeId() { return serviceTypeId; }
    public String getServiceTypeName() { return serviceTypeName; }
    public LocalDate getDate() { return date; }
    public int getMileage() { return mileage; }
    public double getCost() { return cost; }
    public String getComment() { return comment; }
}
