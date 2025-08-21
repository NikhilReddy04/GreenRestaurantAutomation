package org.epam.devbistro.model;

public class CopilotTest {
    private String locationId;
    private String tableNumber;
    private String date;
    private String guestsNumber;
    private String timeFrom;
    private String timeTo;

    public CopilotTest (){
    }

    public CopilotTest(String locationId, String date, String tableNumber, String guestsNumber, String timeFrom, String timeTo) {
        this.locationId = locationId;
        this.date = date;
        this.tableNumber = tableNumber;
        this.guestsNumber = guestsNumber;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    // Getters and Setters
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(String guestsNumber) {
        this.guestsNumber = guestsNumber;
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
}