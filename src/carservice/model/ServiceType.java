package carservice.model;

public class ServiceType {
    private int id;
    private String name;
    private int defaultIntervalKm;
    private int defaultIntervalMonths;

    public ServiceType(int id, String name, int defaultIntervalKm, int defaultIntervalMonths) {
        this.id = id;
        this.name = name;
        this.defaultIntervalKm = defaultIntervalKm;
        this.defaultIntervalMonths = defaultIntervalMonths;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getDefaultIntervalKm() { return defaultIntervalKm; }
    public int getDefaultIntervalMonths() { return defaultIntervalMonths; }

    @Override
    public String toString() { return name; }
}
