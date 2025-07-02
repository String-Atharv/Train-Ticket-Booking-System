package ticket_booking.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;

public class Train {
    private String trainId;
    private int trainNo;;
    private List<String> stations;
    private HashMap<String, String> stationTimes;
    private List<List<Integer>> seats ;

    public Train() {
    }

    public Train(String trainId, int trainNo, HashMap<String, String> stationTimes, List<String> stations, List<List<Integer>> seats) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.stationTimes = stationTimes;
        this.stations = stations;
        this.seats = seats;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public int getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(int trainNo) {
        this.trainNo = trainNo;
    }

    public HashMap<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(HashMap<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    @JsonIgnore
    public String getTrainInfo(){
        return String.format("Train ID : %s,Train No : %d",trainId,trainNo);
    }

}
