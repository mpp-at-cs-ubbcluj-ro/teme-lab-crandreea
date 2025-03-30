package ro.mpp2025.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.JdbcUtils;
import ro.mpp2025.model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository {

    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger(CarsDBRepository.class);

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("finding cars with manufacturer {}", manufacturerN);
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement prepStmt = connection.prepareStatement("SELECT * FROM cars WHERE manufacturer = ?")) {
            prepStmt.setString(1, manufacturerN);
            try(ResultSet result = prepStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("finding cars between years {} and {}", min, max);
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement prepStmt = connection.prepareStatement("SELECT * FROM cars WHERE year >= ? AND year <= ?")) {
            prepStmt.setInt(1, min);
            prepStmt.setInt(2, max);
            try(ResultSet result = prepStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving car {}", elem);
        Connection connection = dbUtils.getConnection();
        try {
            PreparedStatement prepStmtCar = connection.prepareStatement("INSERT INTO cars (manufacturer, model, year) VALUES (?,?,?)");

            prepStmtCar.setString(1, elem.getManufacturer());
            prepStmtCar.setString(2, elem.getModel());
            prepStmtCar.setInt(3, elem.getYear());
            int result = prepStmtCar.executeUpdate();

            logger.trace("Saved {} instance", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("updating car with id {}: {}", integer, elem);
        Connection connection = dbUtils.getConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement(
                    "UPDATE cars SET manufacturer = ?, model = ?, year = ? WHERE id = ?");

            prepStmt.setString(1, elem.getManufacturer());
            prepStmt.setString(2, elem.getModel());
            prepStmt.setInt(3, elem.getYear());
            prepStmt.setInt(4, integer);

            int result = prepStmt.executeUpdate();
            logger.trace("Updated {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement prepStmt = connection.prepareStatement("SELECT * FROM cars")) {
            try(ResultSet result = prepStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;

    }
}
