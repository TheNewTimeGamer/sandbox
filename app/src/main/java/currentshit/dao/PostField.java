package currentshit.dao;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

public class PostField {
    public ObjectId id;
    public String title;
    public String userID;
    public String content;
    public List<ObjectId> comments;

    public int visibility;
    public String password;

    public List<String> tags;

    public PostField() {}

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

    public PostField(ObjectId id, String title, String userID, String content, List<ObjectId> comments, int visibility, String password, String[] tags) {
        this.id = id;
        this.title = title;
        this.userID = userID;
        this.content = content;
        this.comments = comments;

        this.visibility = visibility;
        this.password = password;

        this.tags = Arrays.asList(tags);
    }
}