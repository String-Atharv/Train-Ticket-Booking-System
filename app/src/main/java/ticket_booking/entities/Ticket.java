package ticket_booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.HashMap;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dot;
    private Train train;  // this ticket is of which train

    public Ticket() {

    }

    public Ticket(String ticketId, String source, String destination, String dot, Train train,String userId) {
        this.ticketId = ticketId;
        this.source = source;
        this.destination = destination;
        this.dot = dot;
        this.train = train;
        this.userId=userId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getdot() {
        return dot;
    }

    public void setdot(String dot) {
        this.dot = dot;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @JsonIgnore
    public String getTicketInfo() {
        return String.format("Ticket ID: %s | User ID: %s | From: %s | To: %s | Date: %s | Train: %s",
                ticketId, userId, source, destination, dot,
                (train != null ? train.getTrainId() : "N/A"));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
