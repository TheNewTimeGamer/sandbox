package currentshit.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;

import currentshit.MongoService;

public class Posts {

    private Posts() {}

    public static Document[] getPosts(String[] tags) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<Document> collection = db.getCollection(MongoService.COLLECTION_POSTS);
        MongoCursor<Document> cursor = collection.find(Filters.in("tags", Arrays.asList(tags))).iterator();
        ArrayList<Document> documents = new ArrayList<Document>();
        while(cursor.hasNext()) {
            documents.add(cursor.next());
        }
        return documents.toArray(new Document[documents.size()]);
    }

    public static PostField getPostByID(ObjectId postID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<PostField> collection = db.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
        //PostField post = collection.find(postID).first();
        return null;
    }

    public static Document[] getPostsByUserID(ObjectId userID) {
        MongoDatabase db = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
        MongoCollection<PostField> collection = db.getCollection(MongoService.COLLECTION_POSTS, PostField.class);
        //PostField post = collection.find(userID);
        return null;
    }

    public static boolean createPost(String title, String userID, String content, String[] tags) {
        try{
            PostField postField = new PostField(title, userID, content, 0, "", tags);
            MongoDatabase database = MongoService.instance.mongoClient.getDatabase(MongoService.DATABASE_NAME);
            MongoCollection collection = database.getCollection(MongoService.COLLECTION_POSTS);
            collection.insertOne(postField.toDocument());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
    
class PostField {
    public ObjectId id;
    public String title;
    public String userID;
    public String content;
    public List<ObjectId> comments;

    public int visibility;
    public String password;

    public List<String> tags;

    public PostField(String title, String userID, String content, int visibility, String password, String[] tags) {
        this.id = new ObjectId();
        this.title = title;
        this.userID = userID;
        this.content = content;
        this.comments = Arrays.asList(new ObjectId[0]);

        this.visibility = visibility;
        this.password = password;

        this.tags = Arrays.asList(tags);
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("_id", this.id);
        document.put("name", this.title);
        document.put("userID", this.userID);
        document.put("content", this.content);
        document.put("comments", this.comments);
        document.put("visibility", this.visibility);
        document.put("password", this.password);
        document.put("tags", this.tags);
        return document;
    }

}