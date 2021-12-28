package currentshit.dao;

import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.types.ObjectId;

import currentshit.MongoService;

public class Users {

    private Users(){}

    public static UserField[] get() {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<UserField> collection = db.getCollection(MongoService.COLLECTION_USERS, UserField.class);
        MongoCursor<UserField> cursor = collection.find().iterator();
        ArrayList<UserField> documents = new ArrayList<UserField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new UserField[documents.size()]);
    }

    public static UserField getByID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<UserField> collection = db.getCollection(MongoService.COLLECTION_USERS, UserField.class);
        return collection.find(Filters.eq("_id", userID)).first();
    }

    public static UserField getByPostID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<UserField> collection = db.getCollection(MongoService.COLLECTION_USERS, UserField.class);
        ObjectId userID = collection.find(Filters.in("posts", postID)).first().id;
        return collection.find(Filters.eq("_id", userID)).first();
    }

    public static boolean create(String name, String password, String email) {
        try{
            UserField userField = new UserField(name, password, email);            
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
            MongoCollection<UserField> collection = database.getCollection(MongoService.COLLECTION_USERS, UserField.class);         
            collection.insertOne(userField);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}