package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.context.ApiTestContext;
import org.epam.devbistro.mainContext.MainContext;
import org.epam.devbistro.model.ClientBookingsPayload;
import org.epam.devbistro.model.FeedbackPayload;
import org.epam.devbistro.services.ApiServices;
import org.epam.devbistro.utils.ApiConstants;
import org.epam.devbistro.services.AuthService;
import org.epam.devbistro.utils.DateUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReservationsSteps {
    private final ApiTestContext context;
    private final MainContext mainContext;

    public ReservationsSteps(ApiTestContext context, MainContext mainContext) {
        this.context = context;
        this.mainContext=mainContext;
    }

    @Given("The user wants to fetch reservations")
    public void userWantsToFetchReservations(){
        String accessToken= AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .header("Authorization","Bearer "+accessToken);
        context.setRequest(request);
    }

    @Given("The user has their own reservation id")
    public void userHasHisOwnReservationId(){
        String date= DateUtil.getFormattedDateInFutureFromToday(1);
        String reservationId = ApiServices.bookATableForAGivenDateAndLocationAndGuestCountAsClient(date,"loc123","2",mainContext);
        context.setReservationId(reservationId);
        
        String accessToken= AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .header("Authorization","Bearer "+accessToken);
        context.setRequest(request);
    }

    @Given("The user wants to cleanup all the reservations")
    public void userWantsToCancelAllReservations() {
        this.userWantsToFetchReservations();
        CommonSteps commonSteps = new CommonSteps(context);
        commonSteps.userSendsAGetRequestTo("/reservations");

        String accessToken = AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken);
        context.setRequest(request);

        Response response = context.getResponse();
        JSONArray reservations = new JSONArray(response.asString());

        LocalDate today = LocalDate.now();

        for (int i = 0; i < reservations.length(); i++) {
            JSONObject reservation = reservations.getJSONObject(i);
            String status = reservation.getString("status");
            String reservationDateStr = reservation.getString("date"); // assuming format yyyy-MM-dd

            LocalDate reservationDate = LocalDate.parse(reservationDateStr);

            if ("Reserved".equalsIgnoreCase(status) && reservationDate.isAfter(today)) {
                String id = reservation.getString("id");
                context.setReservationId(id);
                System.out.println("Deleting " + context.getReservationId() + " with date " + reservationDate);
                this.userSendsADeleteRequestTo("/reservations");
                commonSteps.theResponseStatusCodeShouldBe(200);
            }
        }}

    @Given("The user wants to edit their booking")
    public void theUserWantsToEditTheirBooking() throws Exception {
        //Creating a booking and fetching its reservation id
        this.userHasHisOwnReservationId();

        String accessToken=AuthService.getAccessToken();
        ClientBookingsPayload clientBookingsPayload =ApiServices.buildAndGetABookingsPayloadWithLocationAndDateAndGuestCount("loc123",DateUtil.getFormattedDateInFutureFromToday(1),"2");
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(clientBookingsPayload);

        context.setBookingsPayload(clientBookingsPayload);
        context.setRequest(requestSpecification);

    }

    @Given("The user wants to edit another persons booking")
    public void userWantsToEditAnotherPersonsBooking() throws Exception {
        this.theUserWantsToEditTheirBooking();
        String anotherToken=AuthService.getAccessTokenOfAnotherUser();
        RequestSpecification updatedRequest = RestAssured.given()
                .contentType("application/json")
                .header("Authorization","Bearer "+anotherToken)
                .body(context.getBookingsPayload());
        context.setRequest(updatedRequest);
    }


    @Given("The user has another users reservation id")
    public void userHasAnotherUsersReservationId() throws Exception {
        this.userHasHisOwnReservationId();
        String accessToken= AuthService.getAccessTokenOfAnotherUser();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization","Bearer "+accessToken);
        context.setRequest(request);
    }

    @Given("The user has a reservation id and is not logged in")
    public void userHasAReservationIdAndIsNotLoggedIn() throws Exception {
        this.userHasHisOwnReservationId();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }

    @Given("The user wants to edit a booking with invalid {string},{string},{string}")
    public void theUserWantsToEditABookingWithInvalid(String date, String guests, String locationId) throws Exception {
        this.userHasHisOwnReservationId();
        ClientBookingsPayload actualClientBookingsPayload =mainContext.getBookingsPayload();
        if(date.equals("tomorrow")){
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            date = tomorrow.format(formatter);
        }

        ClientBookingsPayload clientBookingsPayload =new ClientBookingsPayload();
        clientBookingsPayload.setTableNumber(actualClientBookingsPayload.getTableNumber());
        clientBookingsPayload.setTimeTo(actualClientBookingsPayload.getTimeTo());
        clientBookingsPayload.setTimeFrom(actualClientBookingsPayload.getTimeFrom());
        clientBookingsPayload.setLocationId(locationId);
        if(date.equals("sameDay")){
            clientBookingsPayload.setDate(actualClientBookingsPayload.getDate());
        }
        else{
        clientBookingsPayload.setDate(date);
        }
        clientBookingsPayload.setGuestsNumber(guests);
        String accessToken=AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization","Bearer "+accessToken)
                .body(clientBookingsPayload);
        context.setRequest(request);
    }

    @Given("The user wants to leave feedback")
    public void theUserWantsToLeaveFeedback() {
        context.setReservationId("2025-05-21loc123table102slot2");
        FeedbackPayload feedbackPayload =new FeedbackPayload();
        feedbackPayload.setCuisineComment("Good Food");
        feedbackPayload.setCuisineRating("4");
        feedbackPayload.setReservationId(context.getReservationId());
        feedbackPayload.setServiceRating("4");
        feedbackPayload.setCuisineRating("4");
        feedbackPayload.setServiceComment("Good service");
        System.out.println(feedbackPayload);
        context.setFeedbackPayload(feedbackPayload);
        String accessToken=AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization","Bearer "+accessToken)
                .body(feedbackPayload);
        context.setRequest(request);
    }

    @Given("The user wants to leave feedback for another user's reservation")
    public void theUserWantsToLeaveFeedbackForAnotherUserSReservation() {
        this.theUserWantsToLeaveFeedback();
        String accessToken=AuthService.getAccessTokenOfAnotherUser();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization","Bearer "+accessToken)
                .body(context.getFeedbackPayload());
        context.setRequest(request);
    }

    @Given("The user wants to leave feedback with invalid {string}")
    public void theUserWantsToLeaveFeedbackWithInvalidParameters(String parameter) {
        context.setReservationId("2025-05-21loc123table102slot2");
        FeedbackPayload feedbackPayload =new FeedbackPayload();
        feedbackPayload.setCuisineComment("Good Food");
        feedbackPayload.setCuisineRating("4");
        feedbackPayload.setReservationId(context.getReservationId());
        feedbackPayload.setServiceRating("4");
        feedbackPayload.setCuisineRating("4");
        feedbackPayload.setServiceComment("Good service");
        context.setFeedbackPayload(feedbackPayload);
        switch (parameter) {
            case "emptyCuisineComment":
                feedbackPayload.setCuisineComment("");
                break;

            case "cuisineRating":
                feedbackPayload.setCuisineRating("9"); // invalid or missing
                break;

            case "reservationId":
                feedbackPayload.setReservationId("res123"); // invalid or missing
                break;

            case "serviceRating":
                feedbackPayload.setServiceRating("333"); // invalid or missing
                break;

            case "emptyServiceComment":
                feedbackPayload.setServiceComment(""); // valid
                break;

            case "emptyCuisineRating":
                feedbackPayload.setCuisineRating(""); // empty string
                break;

            case "emptyServiceRating":
                feedbackPayload.setServiceRating(""); // empty string
                break;

            case "emptyReservationId":
                feedbackPayload.setReservationId(""); // empty string
                break;

            default:
                throw new IllegalArgumentException("Invalid parameter: " + parameter);
        }

        System.out.println(feedbackPayload);

        String accessToken=AuthService.getAccessToken();
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .headers("Authorization","Bearer "+accessToken)
                .body(feedbackPayload);
        context.setRequest(request);
    }

    @When("The user sends a PUT request to {string}")
    public void theUserSendsAPutRequestToReservations(String endpoint){
        context.getRequest().log().all();
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = context.getRequest().when().put(BASE_URL + endpoint+context.getReservationId());
        context.setResponse(response);
    }




    @And("The response should contain a list of reservations")
    public void validateTheResponseContainsListOfReservations(){
        Response response=context.getResponse();
        List<Map<String, Object>> bookings = response.jsonPath().getList("$");

        for (Map<String, Object> booking : bookings) {
            Assert.assertTrue(booking.containsKey("date"));
            Assert.assertTrue(booking.containsKey("timeSlot"));
            Assert.assertTrue(booking.containsKey("preOrder"));
            Assert.assertTrue(booking.containsKey("feedbackId"));
            Assert.assertTrue(booking.containsKey("id"));
            Assert.assertTrue(booking.containsKey("locationAddress"));
            Assert.assertTrue(booking.containsKey("guestNumber"));
            Assert.assertTrue(booking.containsKey("status"));
        }
    }

    @And("The response should contain a list of reservations created by waiter")
    public void validateTheResponseContainsListOfReservationsByWaiter(){
        Response response=context.getResponse();
        List<Map<String, Object>> bookings = response.jsonPath().getList("$");

        for (Map<String, Object> booking : bookings) {
            Assert.assertTrue(booking.containsKey("date"));
            Assert.assertTrue(booking.containsKey("slotTime"));
            Assert.assertTrue(booking.containsKey("tableId"));
            Assert.assertTrue(booking.containsKey("bookedBy"));
            Assert.assertTrue(booking.containsKey("locationAddress"));
            Assert.assertTrue(booking.containsKey("guests"));
            Assert.assertTrue(booking.containsKey("reservationId"));
        }
    }

    @When("The user sends a DELETE reservations request to {string}")
    public void userSendsADeleteRequestTo(String endPoint){
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = context.getRequest().when().delete(BASE_URL + endPoint+"/"+context.getReservationId());
        context.setResponse(response);
    }


    @And("The reservation should be cancelled")
    public void validateThatTheReservationIsCancelled(){
        this.userWantsToFetchReservations();
        CommonSteps commonSteps=new CommonSteps(context);
        commonSteps.userSendsAGetRequestTo("/reservations");
        commonSteps.theResponseStatusCodeShouldBe(200);

        String storedReservationId=context.getReservationId();
        Response response=context.getResponse();
        List<Map<String, Object>> bookings = response.jsonPath().getList("$");

        boolean cancelled = false;

        for (Map<String, Object> jsonObject : bookings) {
            String reservationId = (String) jsonObject.get("id");

            if (reservationId.equalsIgnoreCase(storedReservationId)) {
                String status = (String) jsonObject.get("status");


                if ("Cancelled".equalsIgnoreCase(status)) {
                    cancelled = true;
                    break;
                }


            }
        }
        Assert.assertTrue(cancelled);
    }

}
