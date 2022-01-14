package currentshit.dao;

import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;

import currentshit.MongoService;

public class Posts {

    private Posts() {}

    public static PostField[] get(String[] tags) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<PostField> collection = db.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
        MongoCursor<PostField> cursor = collection.find(Filters.in("tags", tags)).iterator();
        ArrayList<PostField> documents = new ArrayList<PostField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new PostField[documents.size()]);
    }

    public static PostField getByID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<PostField> collection = db.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
        return collection.find(Filters.eq("_id",postID)).first();
    }

    public static PostField[] getByUserID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<PostField> collection = db.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
        MongoCursor<PostField> cursor = collection.find(Filters.eq("userID", userID)).iterator();
        ArrayList<PostField> documents = new ArrayList<PostField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new PostField[documents.size()]);
    }

    public static boolean create(String title, String userID, String content, String[] tags) {
        try{
            PostField postField = new PostField(title, userID, content, 0, "", tags);
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
            MongoCollection<PostField> collection = database.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
            collection.insertOne(postField);

            MongoCollection<UserField> userCollection = database.getCollection(MongoService.COLLECTION_USERS, UserField.class);
            userCollection.updateOne(Filters.eq("_id",userID), new Document("$push", new Document("posts", postField.id)));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
