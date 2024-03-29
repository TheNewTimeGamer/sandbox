package currentshit.dao;

import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;

import currentshit.MongoService;

public class Comments {
    
    private Comments() {}

    public static CommentField getByID(ObjectId commentID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        return collection.find(Filters.eq("_id", commentID)).first();
    }

    public static CommentField[] getByUserID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        MongoCursor<CommentField> cursor = collection.find(Filters.eq("userID", userID)).iterator();
        ArrayList<CommentField> documents = new ArrayList<CommentField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new CommentField[documents.size()]);
    }

    public static CommentField[] getByPostID(String postID) {
        try{
            MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
            MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
            MongoCursor<CommentField> cursor = collection.find(Filters.eq("postID", postID)).iterator();
            ArrayList<CommentField> documents = new ArrayList<CommentField>();
            while(cursor.hasNext()) {
                documents.add(cursor.next());
            }
            return documents.toArray(new CommentField[documents.size()]);
        }catch(Exception e){
            e.printStackTrace();            
        }
        return null;
    }

    public static CommentField[] getByPostAndUserID(ObjectId postID, ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        MongoCursor<CommentField> cursor = collection.find(Filters.and(Filters.eq("postID", postID), Filters.eq("userID", userID))).iterator();
        ArrayList<CommentField> documents = new ArrayList<CommentField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new CommentField[documents.size()]);
    }

    public static boolean create(String userID, String postID, String data) {
        try{
            CommentField commentField = new CommentField(userID, postID, data);
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
            MongoCollection<CommentField> collection = database.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
            collection.insertOne(commentField);

            MongoCollection<CommentField> userCollection = database.getCollection(MongoService.COLLECTION_USERS, CommentField.class);
            MongoCollection<CommentField> postCollection = database.getCollection(MongoService.COLLECTION_POSTS, CommentField.class);

            userCollection.updateOne(Filters.eq("userID",userID), new Document("$push", new Document("comments", commentField.id)));
            postCollection.updateOne(Filters.eq("postID",postID), new Document("$push", new Document("comments", commentField.id)));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}