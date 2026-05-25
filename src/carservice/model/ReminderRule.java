package carservice.model;

public class ReminderRule {
    private int id;
    private int carId;
    private int serviceTypeId;
    private String serviceTypeName;
    private int intervalKm;
    private int intervalMonths;
    private boolean active;

    public ReminderRule(int id, int carId, int serviceTypeId, String serviceTypeName, int intervalKm, int intervalMonths, boolean active) {
        this.id = id;
        this.carId = carId;
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
        this.intervalKm = intervalKm;
        this.intervalMonths = intervalMonths;
        this.active = active;
    }

    public int getId() { return id; }
    public int getCarId() { return carId; }
    public int getServiceTypeId() { return serviceTypeId; }
    public String getServiceTypeName() { return serviceTypeName; }
    public int getIntervalKm() { return intervalKm; }
    public int getIntervalMonths() { return intervalMonths; }
    public boolean isActive() { return active; }
    public String getActiveText() { return active ? "Да" : "Нет"; }
}
