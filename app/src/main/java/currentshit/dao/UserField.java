package currentshit.dao;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

public class UserField {
    public ObjectId id;
    public String name;
    public String password;
    public String email;
    public List<ObjectId> posts;
    public List<ObjectId> comments;
    public List<ObjectId> likes;
    public List<ObjectId> dislikes;
    public List<ObjectId> follows;

    public UserField() {}

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

    public UserField(ObjectId id, String name, String password, String email, List<ObjectId> posts, List<ObjectId> comments, List<ObjectId> likes, List<ObjectId> dislikes, List<ObjectId> follows) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.posts = posts;
        this.comments = comments;
        this.likes = likes;
        this.dislikes = dislikes;
        this.follows = follows;
    }

}