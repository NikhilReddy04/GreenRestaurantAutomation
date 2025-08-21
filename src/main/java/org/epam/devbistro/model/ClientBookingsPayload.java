package org.epam.devbistro.model;

import java.util.Objects;

public class ClientBookingsPayload extends BaseBookingsPayload {

    private String locationId;
    private String tableNumber;
    private String date;
    private String guestsNumber;
    private String timeFrom;
    private String timeTo;

    @Override
    public String toString() {
        return "ClientBookingsPayload{" +
                "locationId='" + locationId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", date='" + date + '\'' +
                ", guestsNumber='" + guestsNumber + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                '}';
    }

    public ClientBookingsPayload() {}

    public ClientBookingsPayload(String locationId, String tableNumber, String date,
                                 String guestsNumber, String timeFrom, String timeTo) {
        super(locationId,date,tableNumber,guestsNumber,timeFrom,timeTo);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientBookingsPayload that = (ClientBookingsPayload) o;
        return Objects.equals(locationId, that.locationId) && Objects.equals(tableNumber, that.tableNumber) && Objects.equals(date, that.date) && Objects.equals(guestsNumber, that.guestsNumber) && Objects.equals(timeFrom, that.timeFrom) && Objects.equals(timeTo, that.timeTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, tableNumber, date, guestsNumber, timeFrom, timeTo);
    }
}
