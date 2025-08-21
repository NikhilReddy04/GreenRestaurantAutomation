package org.epam.devbistro.context;

public class UiTestContext {
    private String availableSlot;
    private String bookedSlot;
    private String availableTable;

    public String getAvailableTable() {
        return availableTable;
    }

    public void setAvailableTable(String availableTable) {
        this.availableTable = availableTable;
    }

    public String getBookedSlot() {
        return bookedSlot;
    }

    public void setBookedSlot(String bookedSlot) {
        this.bookedSlot = bookedSlot;
    }

    public String getAvailableSlot() {
        return availableSlot;
    }

    public void setAvailableSlot(String availableSlot) {
        this.availableSlot = availableSlot;
    }
}
