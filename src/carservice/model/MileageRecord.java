package carservice.model;

import java.time.LocalDate;

public class MileageRecord {
    private int id;
    private int carId;
    private LocalDate date;
    private int mileage;
    private String note;

    public MileageRecord(int id, int carId, LocalDate date, int mileage, String note) {
        this.id = id;
        this.carId = carId;
        this.date = date;
        this.mileage = mileage;
        this.note = note;
    }

    public int getId() { return id; }
    public int getCarId() { return carId; }
    public LocalDate getDate() { return date; }
    public int getMileage() { return mileage; }
    public String getNote() { return note; }
}
