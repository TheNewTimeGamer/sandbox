package currentshit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.events.Comment;

import com.google.gson.Gson;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.MongoClientURI;

import org.bson.Document;
import org.bson.types.ObjectId;


public class MongoService {
    
    public static final String DATABASE_NAME = "currentshit";
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_POSTS = "posts";
    public static final String COLLECTION_COMMENTS = "comments";

    public static MongoService instance;

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
            this.mongoClient = new MongoClient(new MongoClientURI(connectionString));
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        return this.connected;
    }

class GenericMetaField {

    public boolean validated;

    public GenericMetaField(boolean validated) {
        this.validated = validated;
    }

}