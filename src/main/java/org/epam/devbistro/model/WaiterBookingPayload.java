package org.epam.devbistro.model;

public class WaiterBookingPayload extends BaseBookingsPayload {

    private String clientType;
    private String customerEmail;

    public WaiterBookingPayload() {}

    public WaiterBookingPayload(String locationId, String tableNumber, String date,
                                String guestsNumber, String timeFrom, String timeTo,
                                String clientType, String customerEmail) {
        super(locationId, tableNumber, date, guestsNumber, timeFrom, timeTo);
        this.clientType = clientType;
        this.customerEmail = customerEmail;
    }

    // Constructor that takes a ClientBookingsPayload and adds new fields
    public WaiterBookingPayload(BaseBookingsPayload bookingsPayload) {
        // Initialize inherited fields using setters
        this.setLocationId(bookingsPayload.getLocationId());
        this.setTableNumber(bookingsPayload.getTableNumber());
        this.setDate(bookingsPayload.getDate());
        this.setGuestsNumber(bookingsPayload.getGuestsNumber());
        this.setTimeFrom(bookingsPayload.getTimeFrom());
        this.setTimeTo(bookingsPayload.getTimeTo());
    }

    // Getters and Setters
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmailId) {
        this.customerEmail = customerEmailId;
    }

    @Override
    public String toString() {
        return "WaiterBookingPayload{" +
                "locationId='" + getLocationId() + '\'' +
                ", tableNumber='" + getTableNumber() + '\'' +
                ", date='" + getDate() + '\'' +
                ", guestsNumber='" + getGuestsNumber() + '\'' +
                ", timeFrom='" + getTimeFrom() + '\'' +
                ", timeTo='" + getTimeTo() + '\'' +
                ", clientType='" + clientType + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
