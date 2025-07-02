package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.entities.Ticket;
import ticket_booking.entities.Train;
import ticket_booking.entities.User;
import ticket_booking.services.TrainService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserBookingService {

    private User user;
    private List<User>userList;
    private  TrainService trainService;
    private static final ObjectMapper mapper=new ObjectMapper();

   void loadUsers() throws IOException{
        File userFile=new File("app/src/main/java/ticket_booking/localDb/User.json");
        this.userList=mapper.readValue(userFile, new TypeReference<List<User>>() {});
    }

    public UserBookingService(User user1) throws IOException {
        this.user=user1;
       loadUsers(); // json obj -> java obj and stored in list
        this.trainService = new TrainService();
    }

    public UserBookingService()throws IOException {
       loadUsers();
    }


    void saveUserListToFile() throws IOException{
        File userFile=new File("app/src/main/java/ticket_booking/localDb/User.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(userFile,userList); // java obj -> json obj and stored in
    }
    public  User login(String name,String password){
       Optional<User> op= userList.stream().filter(user1 -> {
            return user1.getName().equals(name) && user1.getPassword().equals(password);
        }).findFirst();
        op.ifPresent(value -> user = value);
       return user;
    }

    public boolean signUp(User user1){
        try{

            user1.setTicketBooked(new ArrayList<>());
            userList.add(user1);
            saveUserListToFile();
            return true;
        }
        catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
            return false;
        }
    }

    public void fetchBooking(User user1){
        if (user1.getTicketBooked() == null || user1.getTicketBooked().isEmpty()) {
            System.out.println("No bookings found for user: " + user1.getName());
            return;
        }
        user1.printTickets();
    }
    public boolean cancelBooking(User currentUser, String ticketId) throws IOException {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return false;
        }

        List<Ticket> userTickets = currentUser.getTicketBooked();
        if (userTickets == null) {
            return false;
        }

        // Find the ticket to cancel
        Optional<Ticket> ticketToCancel = userTickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(ticketId))
                .findFirst();

        if (ticketToCancel.isPresent()) {
            Ticket ticket = ticketToCancel.get();

            // Free up the seat in the train (this would require storing seat position in ticket)
            // For now, we'll just remove the ticket

            boolean removed = userTickets.removeIf(t -> t.getTicketId().equals(ticketId));

            if (removed) {
                // Update the user in the userList
                updateUserInList(currentUser);
                saveUserListToFile();
                return true;
            }
        }

        return false;
    }

    private void updateUserInList(User updatedUser) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(updatedUser.getUserId())) {
                userList.set(i, updatedUser);
                break;
            }
        }
    }

    public List<List<Integer>> fetchSeats(Train t){
        return t.getSeats();
    }



    private List<Train> TrainList;

    public List<Train> getTrains(String src, String des) throws IOException {
        try{
            TrainService trainService = new TrainService(); // trainList=list of trains
            return trainService.searchTrain(src, des);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }
    public boolean bookSeat(User currentUser, Train train, int row, int seatNum,
                            String source, String destination) throws IOException {

        List<List<Integer>> seatList = train.getSeats();

        // Check if trainService is initialized
        if (this.trainService == null) {
            try {
                this.trainService = new TrainService();
            } catch (IOException e) {
                System.err.println("Failed to initialize TrainService: " + e.getMessage());
                return false;
            }
        }

        // Validate row and seat numbers first
        if (row < 0 || row >= seatList.size() ||
                seatNum < 0 || seatNum >= seatList.get(row).size()) {
            System.err.println("Invalid seat position entered");
            return false;
        }

        // Check if seat is already booked
        if (seatList.get(row).get(seatNum) == 1) {
            System.err.println("Seat already booked");
            return false;
        }

        // Validate that source and destination are valid stations for this train
        if (!train.getStations().contains(source) || !train.getStations().contains(destination)) {
            System.err.println("Invalid source or destination for this train");
            return false;
        }

        // Check if source comes before destination in the route
        List<String> stations = train.getStations();
        int srcIndex = stations.indexOf(source);
        int destIndex = stations.indexOf(destination);

        if (srcIndex >= destIndex) {
            System.err.println("Source must come before destination in the train route");
            return false;
        }

        try {
            // Book the seat
            seatList.get(row).set(seatNum, 1);
            train.setSeats(seatList);

            // Create ticket
            String ticketId = generateTicketId();
            String dateOfTravel = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Ticket newTicket = new Ticket(ticketId, source, destination, dateOfTravel,
                    train, currentUser.getUserId());

            // Add ticket to user's booking list
            if (currentUser.getTicketBooked() == null) {
                currentUser.setTicketBooked(new ArrayList<>());
            }
            currentUser.getTicketBooked().add(newTicket);

            // Update train data
            trainService.addTrain(train);

            // Update user data
            updateUserInList(currentUser);
            saveUserListToFile();

            System.out.println("Ticket created with ID: " + ticketId);
            return true;

        } catch (Exception e) {
            System.err.println("Error booking seat: " + e.getMessage());
            return false;
        }
    }

    private String generateTicketId() {
        return "TKT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }


    }

//    public static void main(String[] args) throws IOException {
//        UserBookingService u = null;
//        try{
//          u=new UserBookingService();
//        }
//        catch (IOException E){
//            System.out.println("Object cannot be formed");
//        }
//        assert u != null;
//        List<Train> tlist=u.getTrains("Pune","Lucknow");
//        System.out.println(tlist.size());
//
//    }










