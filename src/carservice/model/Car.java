package carservice.model;

public class Car {
    private int id;
    private int userId;
    private String brand;
    private String model;
    private int year;
    private String vin;
    private String plateNumber;
    private int initialMileage;

    public Car(int id, int userId, String brand, String model, int year, String vin, String plateNumber, int initialMileage) {
        this.id = id;
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.plateNumber = plateNumber;
        this.initialMileage = initialMileage;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getVin() { return vin; }
    public String getPlateNumber() { return plateNumber; }
    public int getInitialMileage() { return initialMileage; }

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ")";
    }
}
