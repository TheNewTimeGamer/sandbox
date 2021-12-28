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

    public static CommentField getCommentByID(ObjectId commentID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        //CommentField comment = collection.find(commentID).first();
        return null;
    }

    public static CommentField getCommentsByUserID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<CommentField> collection = db.getCollection(MongoService.COLLECTION_COMMENTS, CommentField.class);
        //CommentField comment = collection.find(userID);
        return null;
    }

    public static Document[] getCommentsByPostID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<Document> collection = db.getCollection(MongoService.COLLECTION_COMMENTS);
        MongoCursor<Document> cursor = collection.find(Filters.eq("postID", postID)).iterator();
        ArrayList<Document> documents = new ArrayList<Document>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new Document[documents.size()]);
    }

    public static boolean createComment(String name, ObjectId userID, ObjectId postID, String data) {
        try{
            CommentField commentField = new CommentField(userID, postID, data);
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
            MongoCollection collection = database.getCollection(MongoService.COLLECTION_COMMENTS);
            collection.insertOne(commentField.toDocument());
        }catch(Exception e){
            return false;
        }
        return true;
    }

}

}

class CommentField {
    public ObjectId id;
    public ObjectId userID;
    public ObjectId postID;
    public String text;

    public CommentField(ObjectId userID, ObjectId postID, String text) {
        this.id = new ObjectId();
        this.userID = userID;
        this.postID = postID;
        this.text = text;
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("_id", this.id);
        document.put("userID", this.userID);
        document.put("postID", this.postID);
        document.put("text", this.text);
        return document;
    }

}