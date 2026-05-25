package carservice;

import carservice.dao.Repository;
import carservice.db.Database;
import carservice.model.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MainApp extends Application {
    private final Repository repository = new Repository();

    private ComboBox<User> userBox = new ComboBox<User>();
    private ListView<Car> carList = new ListView<Car>();
    private TableView<ServiceRecord> serviceTable = new TableView<ServiceRecord>();
    private TableView<FuelRecord> fuelTable = new TableView<FuelRecord>();
    private TableView<MileageRecord> mileageTable = new TableView<MileageRecord>();
    private TableView<ReminderRule> reminderRuleTable = new TableView<ReminderRule>();
    private TextArea infoArea = new TextArea();

    @Override
    public void start(Stage stage) {
        try {
            Database.init();
        } catch (Exception ex) {
            showError("Не удалось подключиться к БД: " + ex.getMessage());
            return;
        }

        initTables();
        buildUi(stage);
        reloadUsers();
    }

    private void buildUi(Stage stage) {
        Button addUserButton = new Button("Добавить пользователя");
        Button addCarButton = new Button("Добавить авто");
        Button refreshButton = new Button("Обновить");

        addUserButton.setOnAction(e -> openAddUserDialog());
        addCarButton.setOnAction(e -> openAddCarDialog());
        refreshButton.setOnAction(e -> reloadUsers());

        userBox.setPrefWidth(260);
        userBox.setOnAction(e -> reloadCars());
        carList.getSelectionModel().selectedItemProperty().addListener((obs, oldCar, newCar) -> reloadDetails());

        VBox left = new VBox(8,
                new Label("Пользователь"), userBox,
                new HBox(6, addUserButton, refreshButton),
                new Label("Автомобили"), carList, addCarButton
        );
        left.setPadding(new Insets(10));
        left.setPrefWidth(330);

        Button addServiceButton = new Button("Добавить сервис");
        Button addFuelButton = new Button("Добавить заправку");
        Button addMileageButton = new Button("Добавить пробег");
        Button addRuleButton = new Button("Добавить правило");
        Button enableRuleButton = new Button("Включить");
        Button disableRuleButton = new Button("Отключить");
        Button statsButton = new Button("Статистика/напоминания");

        addServiceButton.setOnAction(e -> openAddServiceDialog());
        addFuelButton.setOnAction(e -> openAddFuelDialog());
        addMileageButton.setOnAction(e -> openAddMileageDialog());
        addRuleButton.setOnAction(e -> openAddReminderRuleDialog());
        enableRuleButton.setOnAction(e -> setSelectedReminderRuleActive(true));
        disableRuleButton.setOnAction(e -> setSelectedReminderRuleActive(false));
        statsButton.setOnAction(e -> showStatsAndReminders());

        TabPane tabs = new TabPane();
        Tab serviceTab = new Tab("Сервис", new VBox(8, serviceTable, addServiceButton));
        Tab fuelTab = new Tab("Заправки", new VBox(8, fuelTable, addFuelButton));
        Tab mileageTab = new Tab("Пробег", new VBox(8, mileageTable, addMileageButton));
        Tab rulesTab = new Tab("Правила ТО", new VBox(8, reminderRuleTable, new HBox(6, addRuleButton, enableRuleButton, disableRuleButton)));
        Tab infoTab = new Tab("Итоги", new VBox(8, statsButton, infoArea));
        serviceTab.setClosable(false); fuelTab.setClosable(false); mileageTab.setClosable(false); rulesTab.setClosable(false); infoTab.setClosable(false);
        tabs.getTabs().addAll(serviceTab, fuelTab, mileageTab, rulesTab, infoTab);
        tabs.setPadding(new Insets(10));

        HBox root = new HBox(left, tabs);
        Scene scene = new Scene(root, 1150, 620);
        stage.setScene(scene);
        stage.setTitle("Учёт обслуживания личного автомобиля: JDBC + PostgreSQL/MySQL");
        stage.show();
    }

    private void initTables() {
        TableColumn<ServiceRecord, String> stType = new TableColumn<ServiceRecord, String>("Вид работ");
        stType.setCellValueFactory(new PropertyValueFactory<ServiceRecord, String>("serviceTypeName"));
        TableColumn<ServiceRecord, String> stDate = new TableColumn<ServiceRecord, String>("Дата");
        stDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        TableColumn<ServiceRecord, Integer> stMileage = new TableColumn<ServiceRecord, Integer>("Пробег");
        stMileage.setCellValueFactory(new PropertyValueFactory<ServiceRecord, Integer>("mileage"));
        TableColumn<ServiceRecord, Double> stCost = new TableColumn<ServiceRecord, Double>("Стоимость");
        stCost.setCellValueFactory(new PropertyValueFactory<ServiceRecord, Double>("cost"));
        serviceTable.getColumns().addAll(stType, stDate, stMileage, stCost);

        TableColumn<FuelRecord, String> fDate = new TableColumn<FuelRecord, String>("Дата");
        fDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        TableColumn<FuelRecord, Integer> fMileage = new TableColumn<FuelRecord, Integer>("Пробег");
        fMileage.setCellValueFactory(new PropertyValueFactory<FuelRecord, Integer>("mileage"));
        TableColumn<FuelRecord, Double> fLiters = new TableColumn<FuelRecord, Double>("Литры");
        fLiters.setCellValueFactory(new PropertyValueFactory<FuelRecord, Double>("liters"));
        TableColumn<FuelRecord, Double> fPrice = new TableColumn<FuelRecord, Double>("Цена/л");
        fPrice.setCellValueFactory(new PropertyValueFactory<FuelRecord, Double>("pricePerLiter"));
        TableColumn<FuelRecord, String> fStation = new TableColumn<FuelRecord, String>("АЗС");
        fStation.setCellValueFactory(new PropertyValueFactory<FuelRecord, String>("stationName"));
        fuelTable.getColumns().addAll(fDate, fMileage, fLiters, fPrice, fStation);

        TableColumn<MileageRecord, String> mDate = new TableColumn<MileageRecord, String>("Дата");
        mDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        TableColumn<MileageRecord, Integer> mMileage = new TableColumn<MileageRecord, Integer>("Пробег");
        mMileage.setCellValueFactory(new PropertyValueFactory<MileageRecord, Integer>("mileage"));
        TableColumn<MileageRecord, String> mNote = new TableColumn<MileageRecord, String>("Примечание");
        mNote.setCellValueFactory(new PropertyValueFactory<MileageRecord, String>("note"));
        mileageTable.getColumns().addAll(mDate, mMileage, mNote);

        TableColumn<ReminderRule, String> rType = new TableColumn<ReminderRule, String>("Вид ТО");
        rType.setCellValueFactory(new PropertyValueFactory<ReminderRule, String>("serviceTypeName"));
        TableColumn<ReminderRule, Integer> rKm = new TableColumn<ReminderRule, Integer>("Интервал, км");
        rKm.setCellValueFactory(new PropertyValueFactory<ReminderRule, Integer>("intervalKm"));
        TableColumn<ReminderRule, Integer> rMonths = new TableColumn<ReminderRule, Integer>("Интервал, мес.");
        rMonths.setCellValueFactory(new PropertyValueFactory<ReminderRule, Integer>("intervalMonths"));
        TableColumn<ReminderRule, String> rActive = new TableColumn<ReminderRule, String>("Активно");
        rActive.setCellValueFactory(new PropertyValueFactory<ReminderRule, String>("activeText"));
        reminderRuleTable.getColumns().addAll(rType, rKm, rMonths, rActive);

        infoArea.setEditable(false);
    }

    private void reloadUsers() {
        try {
            List<User> users = repository.findUsers();
            userBox.setItems(FXCollections.observableArrayList(users));
            if (!users.isEmpty()) userBox.getSelectionModel().selectFirst();
            reloadCars();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void reloadCars() {
        User user = userBox.getSelectionModel().getSelectedItem();
        if (user == null) return;
        try {
            List<Car> cars = repository.findCarsByUser(user.getId());
            carList.setItems(FXCollections.observableArrayList(cars));
            if (!cars.isEmpty()) carList.getSelectionModel().selectFirst();
            reloadDetails();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void reloadDetails() {
        Car car = carList.getSelectionModel().getSelectedItem();
        if (car == null) {
            serviceTable.setItems(FXCollections.observableArrayList());
            fuelTable.setItems(FXCollections.observableArrayList());
            mileageTable.setItems(FXCollections.observableArrayList());
            reminderRuleTable.setItems(FXCollections.observableArrayList());
            infoArea.clear();
            return;
        }
        try {
            serviceTable.setItems(FXCollections.observableArrayList(repository.findServices(car.getId())));
            fuelTable.setItems(FXCollections.observableArrayList(repository.findFuelRecords(car.getId())));
            mileageTable.setItems(FXCollections.observableArrayList(repository.findMileageRecords(car.getId())));
            reminderRuleTable.setItems(FXCollections.observableArrayList(repository.findReminderRules(car.getId())));
            updateInfoArea(car);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void openAddUserDialog() {
        TextField name = new TextField();
        TextField email = new TextField();
        TextField phone = new TextField();
        boolean ok = showForm("Добавить пользователя", new String[]{"ФИО", "Email", "Телефон"}, new Control[]{name, email, phone});
        if (!ok) return;
        try {
            repository.addUser(name.getText(), email.getText(), phone.getText());
            reloadUsers();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void openAddCarDialog() {
        User user = userBox.getSelectionModel().getSelectedItem();
        if (user == null) { showError("Сначала выберите пользователя"); return; }
        TextField brand = new TextField();
        TextField model = new TextField();
        TextField year = new TextField();
        TextField vin = new TextField();
        TextField plate = new TextField();
        TextField mileage = new TextField("0");
        boolean ok = showForm("Добавить автомобиль", new String[]{"Марка", "Модель", "Год", "VIN", "Госномер", "Начальный пробег"},
                new Control[]{brand, model, year, vin, plate, mileage});
        if (!ok) return;
        try {
            repository.addCar(user.getId(), brand.getText(), model.getText(), Integer.parseInt(year.getText()), vin.getText(), plate.getText(), Integer.parseInt(mileage.getText()));
            reloadCars();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void openAddServiceDialog() {
        Car car = selectedCar(); if (car == null) return;
        try {
            ComboBox<ServiceType> type = new ComboBox<ServiceType>(FXCollections.observableArrayList(repository.findServiceTypes()));
            type.getSelectionModel().selectFirst();
            DatePicker date = new DatePicker(LocalDate.now());
            TextField mileage = new TextField();
            TextField cost = new TextField();
            TextField comment = new TextField();
            boolean ok = showForm("Добавить сервис", new String[]{"Вид работ", "Дата", "Пробег", "Стоимость", "Комментарий"},
                    new Control[]{type, date, mileage, cost, comment});
            if (!ok) return;
            repository.addService(car.getId(), type.getValue().getId(), date.getValue(), Integer.parseInt(mileage.getText()), Double.parseDouble(cost.getText()), comment.getText());
            reloadDetails();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void openAddFuelDialog() {
        Car car = selectedCar(); if (car == null) return;
        DatePicker date = new DatePicker(LocalDate.now());
        TextField mileage = new TextField();
        TextField liters = new TextField();
        TextField price = new TextField();
        TextField station = new TextField();
        boolean ok = showForm("Добавить заправку", new String[]{"Дата", "Пробег", "Литры", "Цена за литр", "АЗС"},
                new Control[]{date, mileage, liters, price, station});
        if (!ok) return;
        try {
            repository.addFuel(car.getId(), date.getValue(), Integer.parseInt(mileage.getText()), Double.parseDouble(liters.getText()), Double.parseDouble(price.getText()), station.getText());
            reloadDetails();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void openAddMileageDialog() {
        Car car = selectedCar(); if (car == null) return;
        DatePicker date = new DatePicker(LocalDate.now());
        TextField mileage = new TextField();
        TextField note = new TextField();
        boolean ok = showForm("Добавить пробег", new String[]{"Дата", "Пробег", "Примечание"}, new Control[]{date, mileage, note});
        if (!ok) return;
        try {
            repository.addMileage(car.getId(), date.getValue(), Integer.parseInt(mileage.getText()), note.getText());
            reloadDetails();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void openAddReminderRuleDialog() {
        Car car = selectedCar(); if (car == null) return;
        try {
            ComboBox<ServiceType> type = new ComboBox<ServiceType>(FXCollections.observableArrayList(repository.findServiceTypes()));
            type.getSelectionModel().selectFirst();
            TextField intervalKm = new TextField();
            TextField intervalMonths = new TextField();
            CheckBox active = new CheckBox("Активно");
            active.setSelected(true);

            type.setOnAction(e -> {
                ServiceType selected = type.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    intervalKm.setText(String.valueOf(selected.getDefaultIntervalKm()));
                    intervalMonths.setText(String.valueOf(selected.getDefaultIntervalMonths()));
                }
            });
            ServiceType first = type.getSelectionModel().getSelectedItem();
            if (first != null) {
                intervalKm.setText(String.valueOf(first.getDefaultIntervalKm()));
                intervalMonths.setText(String.valueOf(first.getDefaultIntervalMonths()));
            }

            boolean ok = showForm("Добавить правило ТО", new String[]{"Вид ТО", "Интервал, км", "Интервал, месяцев", "Статус"},
                    new Control[]{type, intervalKm, intervalMonths, active});
            if (!ok) return;

            repository.addReminderRule(
                    car.getId(),
                    type.getValue().getId(),
                    Integer.parseInt(intervalKm.getText()),
                    Integer.parseInt(intervalMonths.getText()),
                    active.isSelected()
            );
            reloadDetails();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void setSelectedReminderRuleActive(boolean active) {
        ReminderRule rule = reminderRuleTable.getSelectionModel().getSelectedItem();
        if (rule == null) { showError("Сначала выберите правило ТО"); return; }
        try {
            repository.setReminderRuleActive(rule.getId(), active);
            reloadDetails();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void showStatsAndReminders() {
        Car car = selectedCar(); if (car == null) return;
        try {
            updateInfoArea(car);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Статистика");
            a.setHeaderText("Статистика и напоминания: " + car);
            a.setContentText(infoArea.getText());
            a.showAndWait();
        } catch (Exception ex) { showError(ex.getMessage()); }
    }

    private void updateInfoArea(Car car) throws Exception {
        double serviceTotal = repository.getServiceTotal(car.getId());
        double fuelTotal = repository.getFuelTotal(car.getId());
        double liters = repository.getTotalLiters(car.getId());
        List<ReminderItem> reminders = repository.findReminderItems(car.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Автомобиль: ").append(car).append("\n");
        sb.append("Расходы на сервис: ").append(String.format("%.2f", serviceTotal)).append("\n");
        sb.append("Расходы на топливо: ").append(String.format("%.2f", fuelTotal)).append("\n");
        sb.append("Общие расходы: ").append(String.format("%.2f", serviceTotal + fuelTotal)).append("\n");
        sb.append("Всего заправлено литров: ").append(String.format("%.2f", liters)).append("\n\n");
        sb.append("Напоминания:\n");
        if (reminders.isEmpty()) {
            sb.append("Активных напоминаний нет.\n");
        } else {
            for (ReminderItem item : reminders) sb.append("- ").append(item.getText()).append("\n");
        }
        infoArea.setText(sb.toString());
    }

    private Car selectedCar() {
        Car car = carList.getSelectionModel().getSelectedItem();
        if (car == null) showError("Сначала выберите автомобиль");
        return car;
    }

    private boolean showForm(String title, String[] labels, Control[] controls) {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
        for (int i = 0; i < labels.length; i++) {
            grid.add(new Label(labels[i]), 0, i);
            controls[i].setPrefWidth(260);
            grid.add(controls[i], 1, i);
        }
        dialog.getDialogPane().setContent(grid);
        return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Операция не выполнена");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
