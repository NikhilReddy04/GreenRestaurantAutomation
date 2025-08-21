package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.context.ApiTestContext;
import org.epam.devbistro.model.ClientBookingsPayload;
import org.epam.devbistro.model.EditReservationWaiterPayload;
import org.epam.devbistro.model.WaiterBookingPayload;
import org.epam.devbistro.services.ApiServices;
import org.epam.devbistro.utils.ApiConstants;
import org.epam.devbistro.services.AuthService;
import org.epam.devbistro.utils.DateUtil;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class BookingsSteps {
    private final ApiTestContext context;

    public BookingsSteps(ApiTestContext context) {
        this.context = context;
    }


    @Given("The user wants to fetch available tables")
    public void userWantsToFetchAvailableTables(){
        String locationId="loc123";
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = tomorrow.format(formatter);
        String time="12:00";
        int guests=4;


        context.setRequest(RestAssured.given().contentType("application/json")
                .queryParam("locationId",locationId)
                .queryParam("date",formattedDate)
                .queryParam("time",time)
                .queryParam("guests",guests));

    }

    @Given("The user wants to fetch available tables of current day")
    public void theUserWantsToFetchAvailableTablesOfCurrentDay(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // removes seconds/nanos

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = today.format(dateFormatter);
        String time = now.format(timeFormatter);
        int guests = 2;

        context.setRequest(RestAssured.given()
                .contentType("application/json")
                .queryParam("locationId", "loc123")
                .queryParam("date", date)
                .queryParam("time", time)
                .queryParam("guests", guests));
    }

    @Given("The user sets locationId {string}, date {string}, time {string}, and guests {string}")
    public void setFetchBookingRequestWithParams(String locationId, String date, String time, String guests) {
        context.setRequest(RestAssured.given()
                .contentType("application/json")
                .queryParam("locationId", locationId)
                .queryParam("date", date)
                .queryParam("time", time)
                .queryParam("guests", guests));
    }

    @Given("User wants to book a table without authorization token")
    public void userWantsToBookATableWithoutAuthorizationToken(){
        ClientBookingsPayload clientBookingsPayload =new ClientBookingsPayload();
        RequestSpecification requestSpecification=RestAssured.given().contentType("application/json").body(clientBookingsPayload);
        context.setRequest(requestSpecification);
    }

    @Given("User wants to book a table with invalid authorization token")
    public void userWantsToBookATableWithInvalidAuthorizationToken(){
        ClientBookingsPayload clientBookingsPayload =new ClientBookingsPayload();
        RequestSpecification requestSpecification=RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + "Invalid token")
                .body(clientBookingsPayload);
        context.setRequest(requestSpecification);
    }


    @Given("User wants to book a table with valid authorization token and sets invalid {string}")
    public void userWantsToBookATableWithValidAuthorizationTokenAndSetsInvalid(String invalidParam) {
        String token = AuthService.getAccessToken();

        // Create a default valid payload
        ClientBookingsPayload clientBookingsPayload = ApiServices.buildAndGetABookingsPayloadWithLocationAndDateAndGuestCount("loc123", DateUtil.getFormattedDateInFutureFromToday(1),"2");

        // Override the invalid field based on the scenario
        switch (invalidParam) {
            case "locationId":
                clientBookingsPayload.setLocationId("loc8999"); // invalid location
                break;
            case "date":
                clientBookingsPayload.setDate("2024-04-01"); // past date
                break;
            case "guestsNumber":
                clientBookingsPayload.setGuestsNumber("-2"); // invalid guest number
                break;
            case "tableNumber":
                clientBookingsPayload.setTableNumber("kkkkkkkk"); // invalid table number
                break;
            default:
                throw new IllegalArgumentException("Unsupported invalid parameter: " + invalidParam);
        }

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(clientBookingsPayload);

        context.setRequest(requestSpecification);
    }

    @Given("User wants to book a table with valid authorization token and sets valid {string},{string},{string}")
    public void userWantsToBookATableWithValidAuthorizationToken(String locationId, String date, String guestsNumber) throws Exception {
        String accessToken = AuthService.getAccessToken();
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(date.equals("past")){
            tomorrow="15-04-2025";
        }

        ClientBookingsPayload clientBookingsPayload = new ClientBookingsPayload();
        clientBookingsPayload.setLocationId(locationId);
        clientBookingsPayload.setDate(tomorrow);
        clientBookingsPayload.setGuestsNumber(guestsNumber);


        //To get the tableId and available slots calling the function here

        Map<String,String>availableTimeSlotMap=ApiServices.getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(date,locationId,guestsNumber);
        String tableNumber=availableTimeSlotMap.get("table");
        String firstSlot=availableTimeSlotMap.get("slot");

        context.setTableNumber(tableNumber);
        context.setFirstSlot(firstSlot);

        String[] parts = firstSlot.split(" - ");
        String timeFrom = parts[0].trim();
        String timeTo = parts[1].trim();

        clientBookingsPayload.setTimeFrom(timeFrom);
        clientBookingsPayload.setTimeTo(timeTo);
        clientBookingsPayload.setTableNumber(tableNumber);
        //System.out.println(clientBookingsPayload);

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(clientBookingsPayload);

        context.setBookingsPayload(clientBookingsPayload);
        context.setRequest(requestSpecification);
    }

    @Given("Waiter wants to book a table for the client")
    public void waiterWantsToBookATableForTheClient() throws Exception {
        this.userWantsToBookATableWithValidAuthorizationToken("loc123","tomorrow","2");

        WaiterBookingPayload waiterBookingPayload=new WaiterBookingPayload(context.getBookingsPayload());
        waiterBookingPayload.setClientType("Existing");
        waiterBookingPayload.setCustomerEmail("nikhil@gmail.com");
        System.out.println(waiterBookingPayload);
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .body(waiterBookingPayload);
        //System.out.println(waiterBookingPayload);
        requestSpecification.log().all();
        context.setRequest(requestSpecification);
    }

    @Given("Waiter wants to book a table for a visitor")
    public void waiterWantsToBookATableForAVisitor() throws Exception {
        this.userWantsToBookATableWithValidAuthorizationToken("loc123","tomorrow","2");
        WaiterBookingPayload waiterBookingPayload=new WaiterBookingPayload(context.getBookingsPayload());
        waiterBookingPayload.setClientType("Visitor");
        waiterBookingPayload.setCustomerEmail("visitor@gmail.com");
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .body(waiterBookingPayload);
        //System.out.println(waiterBookingPayload);
        context.setRequest(requestSpecification);
    }

    @Given("Waiter wants to book a table for the client with invalid {string}")
    public void waiterWantsToBookATableForTheClientWithInvalid(String parameter) throws Exception {
        this.userWantsToBookATableWithValidAuthorizationToken("loc123","tomorrow","2");
        WaiterBookingPayload waiterBookingPayload=new WaiterBookingPayload(context.getBookingsPayload());
        waiterBookingPayload.setClientType("existing");
        waiterBookingPayload.setCustomerEmail("nikhil@gmail.com");

        switch (parameter) {
            case "date":
                waiterBookingPayload.setDate("invalid-date"); // Set an invalid date string
                break;
            case "past date":
                waiterBookingPayload.setDate("2000-01-01"); // Set a past date
                break;
            case "guestNumber":
                waiterBookingPayload.setGuestsNumber("-1"); // Set an invalid guest number (e.g., negative)
                break;
            case "locationId":
                waiterBookingPayload.setLocationId("invalid-location"); // Set an invalid location ID
                break;
            case "tableNumber":
                waiterBookingPayload.setTableNumber("invalid-table"); // Assuming it's a string. Adjust type if needed
                break;
            case "incorrect email":
                waiterBookingPayload.setCustomerEmail("invalid-email"); // Set an invalid email format
                break;
            case "incorrect type":
                waiterBookingPayload.setClientType("INVALID_TYPE"); // Set an unsupported client type
                break;
            default:
                System.out.println("Invalid parameter provided: " + parameter);
        }
                String waiterAccessToken = AuthService.getAccessTokenOfWaiter();
                RequestSpecification requestSpecification = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + waiterAccessToken)
                        .body(waiterBookingPayload);
                System.out.println(waiterBookingPayload);
                context.setRequest(requestSpecification);

    }

    @Given("Waiter books a table for a customer and has its reservation id")
    public void waiterBooksATableForACustomerAndHasItsReservationId() throws Exception {
        this.waiterWantsToBookATableForTheClient();
        CommonSteps commonSteps=new CommonSteps(context);
        commonSteps.userSendsAPostRequestTo("/bookings/waiter");

        Response response=context.getResponse();
        String reservationId = response.jsonPath().getString("id");
        context.setReservationId(reservationId);

        this.userWantsToBookATableWithValidAuthorizationToken("loc123","tomorrow","2");
        ClientBookingsPayload actualClientBookingsPayload =context.getBookingsPayload();
        EditReservationWaiterPayload editReservationWaiterPayload=new EditReservationWaiterPayload(actualClientBookingsPayload);

        context.setEditReservationWaiterPayload(editReservationWaiterPayload);
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .body(editReservationWaiterPayload);
        context.setRequest(requestSpecification);
    }

    @Given("Waiter books a table for a customer")
    public void waiterBooksATableForACustomer() throws Exception {
        this.waiterBooksATableForACustomerAndHasItsReservationId();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        String tomorrow_str = tomorrow.toString();
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .queryParam("date",tomorrow_str)
                .queryParam("time","10:00")
                .queryParam("table","Any Table");
        context.setRequest(requestSpecification);
    }



    @Given("Waiter books a table for a customer and wants to fetch with invalid {string}")
    public void waiterBooksATableForACustomerAndSetsInvalid(String parameter) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        String date = tomorrow.toString();
        String time="10:00";
        String table="Any Table";

        if(parameter.equals("date")){
            date="2025-04-16";
        }
        else if(parameter.equals("table")){
            table="invalidTable";
        }
        else if(parameter.equals("time")){
            time="77:333";
        }
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .queryParam("date",date)
                .queryParam("time",time)
                .queryParam("table",table);
        context.setRequest(requestSpecification);
    }

    @And("Waiter sets invalid {string}")
    public void waiterSetsInvalid(String parameter) {
        EditReservationWaiterPayload editReservationWaiterPayload=context.getEditReservationWaiterPayload();
        if(parameter.equals("date")){
            editReservationWaiterPayload.setDate("2025-04-14");
        }
        if(parameter.equals("timeslot")){
            editReservationWaiterPayload.setTimeFrom("14:00");
            editReservationWaiterPayload.setTimeTo("16:00");
        }
        if(parameter.equals("table")){
            editReservationWaiterPayload.setTableNumber("invalid");
        }

        if(parameter.equals("reservation id")){
            context.setReservationId("Invalid");
        }
        String waiterAccessToken=AuthService.getAccessTokenOfWaiter();
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + waiterAccessToken)
                .body(editReservationWaiterPayload);
        context.setRequest(requestSpecification);
    }

    @When("The User tries to book the table again")
    public void userTriesToBookSameTableAgain(){
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = context.getRequest().when().post(BASE_URL + "/bookings/client");
        context.setResponse(response);
    }



    @And("The response should contain a list of tables")
    public void validateResponseContainsAListOfTables() {
        Response response = context.getResponse();
        List<Map<String, Object>> tables = response.jsonPath().getList("$");

        for (Map<String, Object> table : tables) {
            assertNotNull(table.get("locationId"));
            assertNotNull(table.get("locationAddress"));
            assertNotNull(table.get("tableNumber"));
            assertNotNull(table.get("availableSlots"));
            assertTrue(table.get("tableNumber").toString().startsWith("table"));
            assertNotNull(table.get("capacity"));
            int capacity = Integer.parseInt(table.get("capacity").toString());
            assertTrue(capacity >= 1);
        }

    }

    @And("The response should contain a list of tables with valid current day slots")
    public void validateTheResponseHasTablesOfValidSlotsOfCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0); // trimming seconds and nanos for consistency

        Response response = context.getResponse();
        List<Map<String, Object>> tables = response.jsonPath().getList("$");

        for (Map<String, Object> table : tables) {
            List<String> slots = (List<String>) table.get("availableSlots");
            for (String slot : slots) {
                String slotEnd = slot.split(" - ")[1].trim(); // e.g., "19:00"
                LocalTime endTime = LocalTime.parse(slotEnd);

                Assert.assertTrue(endTime.isAfter(currentTime) || endTime.equals(currentTime),
                        "Invalid slot returned: " + slot + " (ends before current time: " + currentTime + ")");
            }
        }
    }

    @And("The response should contain a successful booking message")
    public void validateResponseShouldContainSuccessfulBookingMessage(){
//        Response response=context.getResponse();
//        Map<String, Object> booking = response.jsonPath().getMap("");
//
//        assertTrue(booking.containsKey("date"), "Missing field: date");
//        assertTrue(booking.containsKey("timeSlot"), "Missing field: timeSlot");
//        assertTrue(booking.containsKey("preOrder"), "Missing field: preOrder");
//        assertTrue(booking.containsKey("feedbackId"), "Missing field: feedbackId");
//        assertTrue(booking.containsKey("id"), "Missing field: id");
//        assertTrue(booking.containsKey("locationAddress"), "Missing field: locationAddress");
//        assertTrue(booking.containsKey("guestNumber"), "Missing field: guestNumber");
//        assertTrue(booking.containsKey("status"), "Missing field: status");
    }

    @And("User should not get the booked slot in the available slots")
    public void validateThatUserShouldNotGetBookedSlotInAvailableSlots(){
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.setFetchBookingRequestWithParams("loc123",tomorrow,"12:00","2");
        Response response=context.getRequest().get(ApiConstants.BASE_URI+"/bookings/tables");

        List<Map<String, Object>> tables = response.jsonPath().getList("");
        String tableNumber= context.getTableNumber();
        String bookedSlot =context.getFirstSlot();
        boolean slotFound = false;
        for (Map<String, Object> table : tables) {
            if (table.get("tableNumber").equals(tableNumber)) {
                List<String> slots = (List<String>) table.get("availableSlots");
                if (slots.contains(bookedSlot)) {
                    slotFound = true;
                    break;
                }
            }
        }

        assertFalse(slotFound, "Booked slot '" + bookedSlot + "' should not appear in available slots");


    }



}
