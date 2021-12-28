package currentshit.dao;

import org.bson.types.ObjectId;

public class CommentField {
    public ObjectId id;
    public String userID;
    public String postID;
    public String text;

    public CommentField() {}

    public CommentField(String userID, String postID, String text) {
        this.id = new ObjectId();
        this.userID = userID;
        this.postID = postID;
        this.text = text;
    }
}