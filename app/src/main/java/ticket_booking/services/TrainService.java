package ticket_booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class TrainService {

     List<Train> trainList;
    private final ObjectMapper mapper=new ObjectMapper();

    public TrainService() throws IOException {
        File TrainFile=new File("app/src/main/java/ticket_booking/localDb/Train.json");
        trainList=mapper.readValue(TrainFile, new TypeReference<List<Train>>() {});
    }

    public void saveTrainListToFile() throws IOException {
        File trainFile=new File("app/src/main/java/ticket_booking/localDb/Train.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(trainFile,trainList);
    }

    public List<Train> searchTrain(String sor,String des){
        return trainList.stream().filter(train->validTrain(train,sor,des)).collect(Collectors.toList());  // returns list<Train> of size 1 or more
    }



    private boolean validTrain(Train t,String src, String des) {
             List<String> stationsOrder=t.getStations();
             int srcIndex=stationsOrder.indexOf(src);
            int desIndex=stationsOrder.indexOf(des);
            return srcIndex!=-1 && desIndex!=-1 && desIndex>srcIndex;

    }
    private void updateTrain(Train t) throws IOException {
        for(int i=0;i<trainList.size();i++){
         if(trainList.get(i).getTrainId().equals(t.getTrainId())){
             trainList.set(i,t);
             saveTrainListToFile();
             return;
         }
        }
    }

    public void addTrain(Train t) throws IOException {
        Optional<Train> op=trainList.stream().filter(train->t.getTrainId().equals(train.getTrainId())).findFirst();
        if(op.isPresent()){
           updateTrain(t);
        }
        else {
            trainList.add(t);
            saveTrainListToFile();
        }

    }

//    public static void main(String[] args) throws IOException {
//        try{
//            TrainService t=new TrainService();
//            System.out.println(t.trainList.size());
//        }
//        catch(IOException E){
//            System.out.println("wrong");
//        }
//
//
//
//
//    }


    }



