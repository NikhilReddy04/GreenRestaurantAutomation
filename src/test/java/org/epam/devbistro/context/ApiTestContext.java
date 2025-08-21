package org.epam.devbistro.context;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.model.ClientBookingsPayload;
import org.epam.devbistro.model.EditReservationWaiterPayload;
import org.epam.devbistro.model.FeedbackPayload;

public abstract class ApiTestContext {

    private RequestSpecification request;
    private Response response;
    private String bearerToken;
    private String tableNumber;
    private String firstSlot;
    private String reservationId;
    private ClientBookingsPayload clientBookingsPayload;
    private EditReservationWaiterPayload editReservationWaiterPayload;
    private FeedbackPayload feedbackPayload;

    public FeedbackPayload getFeedbackPayload() {
        return feedbackPayload;
    }

    public void setFeedbackPayload(FeedbackPayload feedbackPayload) {
        this.feedbackPayload = feedbackPayload;
    }

    public EditReservationWaiterPayload getEditReservationWaiterPayload() {
        return editReservationWaiterPayload;
    }

    public void setEditReservationWaiterPayload(EditReservationWaiterPayload editReservationWaiterPayload) {
        this.editReservationWaiterPayload = editReservationWaiterPayload;
    }

    public ClientBookingsPayload getBookingsPayload() {
        return clientBookingsPayload;
    }

    public void setBookingsPayload(ClientBookingsPayload clientBookingsPayload) {
        this.clientBookingsPayload = clientBookingsPayload;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getFirstSlot() {
        return firstSlot;
    }

    public void setFirstSlot(String firstSlot) {
        this.firstSlot = firstSlot;
    }



    public RequestSpecification getRequest() {
        return request;
    }

    public void setRequest(RequestSpecification request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
