package ticket_booking.entities;

import java.util.List;
import java.util.UUID;

public class User {
    private String userId;
    private String name;
    private String password;
    private List<Ticket> ticketBooked;  // lets say he have booked two tickets t1 and t2

    public User(){}
    public static String generateID(String name){
        return name+ UUID.randomUUID().toString().substring(0, 8);
    }
    public User(String name,String password){
        this.name=name;
        this.password=password;
        this.userId=generateID(this.name);
    }

    // getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Ticket> getTicketBooked() {
        return ticketBooked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTicketBooked(List<Ticket> ticketBooked) {
        this.ticketBooked = ticketBooked;
    }

    public void printTickets(){
        if (ticketBooked == null || ticketBooked.isEmpty()) {
            System.out.println("No tickets booked yet.");
            return;
        }

        System.out.println("=== Your Tickets ===");
        for (Ticket ticket : ticketBooked) {
            System.out.println(ticket.getTicketInfo());
        }
    }
    public void clearTickets(){
        if (ticketBooked != null) {
            ticketBooked.clear();
        }
    }

    @Override
    public String toString() {
        return String.format("User{userId='%s', name='%s', ticketsBooked=%d}",
                userId, name, (ticketBooked != null ? ticketBooked.size() : 0));
    }
}
