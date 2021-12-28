package currentshit.dao;

import org.bson.types.ObjectId;

public class CommentField {
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

}