package org.epam.devbistro.model;

public class EditReservationWaiterPayload {
    String date;
    String timeFrom;
    String timeTo;
    String tableNumber;

    public EditReservationWaiterPayload(ClientBookingsPayload clientBookingsPayload){
        this.date= clientBookingsPayload.getDate();
        this.tableNumber= clientBookingsPayload.getTableNumber();
        this.timeFrom= clientBookingsPayload.getTimeFrom();
        this.timeTo= clientBookingsPayload.getTimeTo();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    @Override
    public String toString() {
        return "EditReservationWaiterPayload{" +
                "date='" + date + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                '}';
    }
}
