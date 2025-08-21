package org.epam.devbistro.services;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.mainContext.MainContext;
import org.epam.devbistro.model.ClientBookingsPayload;
import org.epam.devbistro.model.WaiterBookingPayload;
import org.epam.devbistro.utils.ApiConstants;
import org.epam.devbistro.utils.Slot;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApiServices {
    static MainContext context=new MainContext();

    public static String bookATableForAGivenDateAndLocationAndGuestCountAsClient(String date, String location, String guestCount, MainContext mainContext){

        //Building clientBookingsPayload
        ClientBookingsPayload clientBookingsPayload =buildAndGetABookingsPayloadWithLocationAndDateAndGuestCount(location,date,guestCount);
        mainContext.setBookingsPayload(clientBookingsPayload);

        //Building a POST request
        String accessToken = AuthService.getAccessToken();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(clientBookingsPayload);

        //Sending a POST request to book the table
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = requestSpecification.when().post(BASE_URL + "/bookings/client");

        return response.jsonPath().getString("[0].id");
    }

    public static ClientBookingsPayload buildAndGetABookingsPayloadWithLocationAndDateAndGuestCount(String location, String date, String guestCount){
        ClientBookingsPayload clientBookingsPayload =new ClientBookingsPayload();
        HashMap<String,String>dateSlotMap= (HashMap<String, String>) getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(date,location,guestCount);
        String[] times = dateSlotMap.get("slot").split("-");
        String timeFrom = times[0].strip();
        String timeTo = times[1].strip();

        clientBookingsPayload.setDate(date);
        clientBookingsPayload.setLocationId(location);
        clientBookingsPayload.setGuestsNumber(guestCount);
        clientBookingsPayload.setTableNumber(dateSlotMap.get("table"));
        clientBookingsPayload.setTimeTo(timeTo);
        clientBookingsPayload.setTimeFrom(timeFrom);

        return clientBookingsPayload;
    }

    public static List<Slot> getAvailableSlotsAsListFromApi(String date, String locationId, String guestCount) {
        String baseUrl = ApiConstants.BASE_URI;

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType("application/json")
                .queryParam("locationId", locationId)
                .queryParam("date", date)
                .queryParam("time", "09:00")
                .queryParam("guests", guestCount);

        requestSpecification.log().all();

        Response response = requestSpecification.when().get(baseUrl + "/bookings/tables");

        JSONArray bookingTablesArray = new JSONArray(response.getBody().asString());
        List<Slot> slotsList = new ArrayList<>();

        for (int i = 0; i < bookingTablesArray.length(); i++) {
            JSONObject tableObj = bookingTablesArray.getJSONObject(i);
            String tableNumber = tableObj.getString("tableNumber");
            JSONArray availableSlots = tableObj.getJSONArray("availableSlots");

            for (int j = 0; j < availableSlots.length(); j++) {
                String slotString = availableSlots.getString(j); // e.g., "17:30 - 19:00"
                String[] parts = slotString.split(" - ");

                if (parts.length == 2) {
                    String startTime = parts[0].trim();
                    String endTime = parts[1].trim();

                    Slot slot = new Slot(date, startTime, endTime, tableNumber);
                    slotsList.add(slot);
                }
            }
        }

        return slotsList;
    }

    public static class SlotLocker {
        // Thread-safe list to hold available slots dynamically
        private static final List<Slot> availableSlots = new CopyOnWriteArrayList<>();
        private static final Object lock = new Object();

        // A flag to track initialization state
        private static volatile boolean initialized = false;

        // Initialize the slot pool by fetching available slots via an API (lazy initialization)
        public static void initializeSlotPool(String date,String locationId,String guestCount) {
            synchronized (lock) {
                if (!initialized) {
                    // Fetch available slots from the API
                    List<Slot> slotsFromApi = getAvailableSlotsAsListFromApi(date,locationId,guestCount);
                    if (slotsFromApi.isEmpty()) {
                        throw new RuntimeException("No available slots fetched from the API.");
                    }

                    // Add slots to the thread-safe list
                    availableSlots.addAll(slotsFromApi);

                    initialized = true; // Mark slot pool as initialized
                    System.out.println("Slot pool initialized with slots: " + availableSlots);
                }
            }
        }

        // Get a unique slot (ensures thread-safe access to the pool)
        public static Slot getUniqueSlot() {
            // Ensure pool is initialized before serving slots
            if (!initialized) {
                throw new IllegalStateException("Slot pool not initialized. Call initializeSlotPool() first.");
            }

            synchronized (lock) {
                if (!availableSlots.isEmpty()) {
                    Slot slot = availableSlots.get(0); // Get the first available slot
                    availableSlots.remove(slot);       // Remove it directly (safe with CopyOnWriteArrayList)
                    context.setSlot(slot);
                    return slot;
                } else {
                    throw new RuntimeException("No available slots left for booking.");
                }
            }
        }

        // Optional: Add a method to release a slot (if slot reuse is needed for other tests)
        public static void releaseSlot(Slot slot) {
            synchronized (lock) {
                availableSlots.add(slot);
            }
        }
    }

    public static Map<String, String> getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(String date, String locationId, String guestCount) {

        // Initialize slot pool once with the provided parameters
        SlotLocker.initializeSlotPool(date, locationId, guestCount);

        // Fetch a unique slot from SlotLocker
        Slot slot = SlotLocker.getUniqueSlot();

        Map<String, String> slotAndTableMap = new HashMap<>();
        slotAndTableMap.put("table", slot.getTable());
        slotAndTableMap.put("slot", slot.getStartTime()+" - "+slot.getEndTime());
        return slotAndTableMap;
    }

    public static String bookATableForAGivenDateAndLocationAndGuestCountAsAWaiterForClient(String date,String locationId,String guestCount,String customerEmail){
        HashMap<String,String>slotAndTableMap= (HashMap<String, String>) getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(date,locationId,guestCount);
        String[] times = slotAndTableMap.get("slot").split("-");
        String timeFrom = times[0].strip();
        String timeTo = times[1].strip();


        ClientBookingsPayload clientBookingsPayload =buildAndGetABookingsPayloadWithLocationAndDateAndGuestCount(locationId,date,guestCount);
        WaiterBookingPayload waiterBookingPayload=new WaiterBookingPayload(clientBookingsPayload);
        waiterBookingPayload.setClientType("existing");
        waiterBookingPayload.setCustomerEmail(customerEmail);

        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .body(waiterBookingPayload);


        //Sending a POST request to book the table
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = requestSpecification.when().post(BASE_URL + "/bookings/waiter");
        return response.jsonPath().getString("[0].id");
    }

    public static void deleteAReservation(String reservationId){
        String BASE_URL = ApiConstants.BASE_URI;
        String accessToken = AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken);
        Response response = request.when().delete(BASE_URL +"/reservations/"+reservationId);

    }
}
