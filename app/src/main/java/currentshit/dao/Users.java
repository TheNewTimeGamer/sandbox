package currentshit.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import currentshit.MongoService;

public class Users {
    
    private Users(){}

    public static Document[] getUsers() {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<Document> collection = db.getCollection(MongoService.COLLECTION_USERS);
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<Document> documents = new ArrayList<Document>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new Document[documents.size()]);
    }

    public static UserField getUserByID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<UserField> collection = db.getCollection(MongoService.COLLECTION_USERS, UserField.class);
        //UserField user = collection.find(userID).first();
        return null;
    }

    public static UserField getUserByPostID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<UserField> collection = db.getCollection(MongoService.COLLECTION_USERS, UserField.class);
        //UserField user = collection.find(postID).first();
        return null;
    }

    public static boolean createUser(String name, String password, String email) {
        try{
            UserField userField = new UserField(name, password, email);            
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
            MongoCollection collection = database.getCollection(MongoService.COLLECTION_USERS);         
            collection.insertOne(userField.toDocument());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

class UserField {
    public ObjectId id;
    public String name;
    public String password;
    public String email;
    public List<ObjectId> posts;
    public List<ObjectId> comments;
    public List<ObjectId> likes;
    public List<ObjectId> dislikes;
    public List<ObjectId> follows;

    public UserField(String name, String password, String email) {
        this.id = new ObjectId();
        this.name = name;
        this.password = password;
        this.email = email;
        this.posts = Arrays.asList();
        this.comments = Arrays.asList();
        this.likes = Arrays.asList();
        this.dislikes = Arrays.asList();
        this.follows = Arrays.asList();
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("_id", this.id);
        document.put("name", this.name);
        document.put("password", this.password);
        document.put("email", this.email);
        document.put("posts", this.posts);
        document.put("comments", this.comments);
        document.put("likes", this.likes);
        document.put("dislikes", this.dislikes);
        document.put("follows", this.follows);
        return document;
    }
}