package org.epam.devbistro.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Slot {
    private String date;       // The date for the slot
    private String startTime;  // The start time for the slot
    private String endTime;    // The end time for the slot
    private String table;         // Table number

    public Slot(String date,String startTime, String endTime, String table) {
        this.date = date;  // Default to tomorrow
        this.startTime = startTime;
        this.endTime = endTime;
        this.table = table;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", table='" + table + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slot)) return false;
        Slot slot = (Slot) o;
        return Objects.equals(date, slot.date) &&
                Objects.equals(startTime, slot.startTime) &&
                Objects.equals(endTime, slot.endTime) &&
                Objects.equals(table, slot.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime, table);
    }
}
