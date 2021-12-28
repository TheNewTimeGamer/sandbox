package currentshit;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


public class MongoService {
    
    public static final String DATABASE_NAME = "currentshit";
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_POSTS = "posts";
    public static final String COLLECTION_COMMENTS = "comments";

    public static MongoService instance;
    public static CodecRegistry pojoCodecRegistry;

    public static MongoService start(String connectionString) {
        if(MongoService.instance == null){
            MongoService.instance = new MongoService();
        }
        
        MongoService.instance.connected = MongoService.instance.connect(connectionString);

        return MongoService.instance;
    }

    public MongoClient mongoClient;

    private boolean connected;

    private MongoService() {}
        
    public boolean connect(String connectionString) {
        try {
            this.pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            this.mongoClient = new MongoClient(new MongoClientURI(connectionString));
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        return this.connected;
    }

}

class GenericMetaField {

    public boolean validated;

    public GenericMetaField(boolean validated) {
        this.validated = validated;
    }
}