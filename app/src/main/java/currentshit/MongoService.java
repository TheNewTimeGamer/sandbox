package currentshit;

public class MongoService {
    
    private static MongoService instance;

    public static MongoService start(String connectionString) {
        if(MongoService.instance == null){
            MongoService.instance = new MongoService(connectionString);
        }
        return MongoService.instance;
    }

    public MongoService(String connectionString) {

    }

}
