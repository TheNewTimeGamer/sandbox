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

    public static CommentField[] getByPostID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        MongoCursor<CommentField> cursor = collection.find(Filters.eq("postID", postID)).iterator();
        ArrayList<CommentField> documents = new ArrayList<CommentField>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new CommentField[documents.size()]);
    }

    public static boolean create(String name, ObjectId userID, ObjectId postID, String data) {
        try{
            CommentField commentField = new CommentField(userID, postID, data);
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME).withCodecRegistry(MongoService.pojoCodecRegistry);
            MongoCollection collection = database.getCollection(MongoService.COLLECTION_COMMENTS);
            collection.insertOne(commentField);

            MongoCollection userCollection = database.getCollection(MongoService.COLLECTION_USERS);
            MongoCollection postCollection = database.getCollection(MongoService.COLLECTION_POSTS);

            userCollection.updateOne(Filters.eq("userID",userID), new Document("$push", new Document("comments", commentField.id)));
            postCollection.updateOne(Filters.eq("postID",postID), new Document("$push", new Document("comments", commentField.id)));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}