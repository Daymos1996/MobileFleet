package users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class courses {

    private String fromWhere, toWhere, plateNumber, firstName, lastName, distance, numberInvoice, numberOfPallets, cost,courseTime;


    public courses() {
    }

    public courses(String fromWhere, String toWhere, String plateNumber, String firstName, String lastName, String distance,
                   String numberInvoice, String numberOfPallets, String cost) {
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.plateNumber = plateNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.distance = distance;
        this.numberInvoice = numberInvoice;
        this.numberOfPallets = numberOfPallets;
        this.cost = cost;
        courseTime = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());

    }

    public void setNumberInvoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
    }

    public String getNumberInvoice() {
        return numberInvoice;
    }

    public void setCourseTime(String  courseTime) {
        this.courseTime = courseTime;
    }

    public String  getCourseTime() {
        return courseTime;
    }

    public String getFromWhere() { return fromWhere; }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getToWhere() {
        return toWhere;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(String numberOfPallets) { this.numberOfPallets = numberOfPallets; }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) { this.cost = cost; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }
}