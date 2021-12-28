package currentshit.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import org.bson.Document;

import currentshit.App;
import currentshit.HttpService;
import currentshit.MongoService;
import currentshit.dao.CommentField;
import currentshit.dao.Comments;
import currentshit.dao.PostField;
import currentshit.dao.Posts;
import currentshit.dao.UserField;
import currentshit.dao.Users;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class ApiHandler implements HttpHandler {

    private final HttpHandler next;

    public ApiHandler(HttpHandler next) {
        this.next = next;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().equals("/api/users")){
            if(exchange.getRequestMethod().equals("GET")){
                StringBuilder builder = new StringBuilder();
                UserField[] users = Users.get();
                if(users.length == 0){
                    builder.append("[]");
                }else{
                    Gson gson = new Gson();
                    for(int i = 0; i < users.length; i++){
                        builder.append(gson.toJson(users[i]));
                        if(i != users.length-1){
                            builder.append(",");
                        }
                    }
                }
                System.out.println("Sending users: " + builder.toString());
                HttpService.serve(exchange, 200, "text/json", builder.toString().getBytes());
            }else if(exchange.getRequestMethod().equals("POST")){
                Map<String, String> body = new Gson().fromJson(new String(exchange.getRequestBody().readAllBytes()), Map.class);
                if(body.get("username") == null || body.get("password") == null || body.get("email") == null){
                    HttpService.sendErrorResponse(exchange, 400, null, "Missing username, password or email.".getBytes());
                    return;
                }
                Users.create(body.get("username"), body.get("password"), body.get("email"));
                HttpService.serve(exchange, 200, "text/plain", new String("Done!").getBytes());
            }
        }
        if(exchange.getRequestURI().getPath().equals("/api/posts")){
            if(exchange.getRequestMethod().equals("GET")){
                String[] tags = new String[0];
                String query = exchange.getRequestURI().getQuery();
                if(query != null){
                    if(query.contains("tags=")){
                        String raw = query.split("tags=")[1];
                        if(raw.contains(",")){
                            tags = raw.split(",");
                        }else{
                            tags = new String[]{raw};
                        }
                    }
                }
                StringBuilder builder = new StringBuilder();
                PostField[] posts = Posts.get(tags);
                System.out.println("found posts: " + posts.length);
                if(posts.length == 0){
                    builder.append("[]");
                }else{
                    Gson gson = new Gson();
                    for(int i = 0; i < posts.length; i++){
                        builder.append(gson.toJson(posts[i]));
                        if(i != posts.length-1){
                            builder.append(",");
                        }
                    }
                }
                HttpService.serve(exchange, 200, "text/json", builder.toString().getBytes());
            }else if(exchange.getRequestMethod().equals("POST")){
                Map<String, String> body = new Gson().fromJson(new String(exchange.getRequestBody().readAllBytes()), Map.class);
                if(body.get("title") == null || body.get("content") == null || body.get("tags") == null){
                    HttpService.sendErrorResponse(exchange, 400, null, "Missing title, content or tags.".getBytes());
                    return;
                }
                String[] tags = null;
                if(body.get("tags").contains(",")){
                    tags = body.get("tags").split(",");
                }else{
                    tags = new String[]{body.get("tags")};
                }
                boolean success = Posts.create(body.get("title"), body.get("userid"), body.get("content"), tags);
                HttpService.serve(exchange, 200, "text/plain", new String("Done! " + success).getBytes());
            }
        }
        if(exchange.getRequestURI().getPath().equals("/api/comments")){
            if(exchange.getRequestMethod().equals("GET")){
                String postID = null;
                String query = exchange.getRequestURI().getQuery();
                if(query != null){
                    if(query.contains("postid=")){
                        postID = query.split("postid=")[1];
                        // TODO: implements class to handle get queries and parse them to a map.
                    }
                }
                StringBuilder builder = new StringBuilder();
                System.out.println("Request for comment.");
                CommentField[] comments = Comments.getByPostID(postID);
                System.out.println("Request for comment2.");
                if(comments.length == 0){
                    builder.append("[]");
                }else{
                    Gson gson = new Gson();
                    for(int i = 0; i < comments.length; i++){
                        builder.append(gson.toJson(comments[i]));
                        if(i != comments.length-1){
                            builder.append(",");
                        }
                    }
                }
                HttpService.serve(exchange, 200, "text/json", builder.toString().getBytes());
            }else if(exchange.getRequestMethod().equals("POST")){
                Map<String, String> body = new Gson().fromJson(new String(exchange.getRequestBody().readAllBytes()), Map.class);
                if(body.get("userid") == null || body.get("postid") == null || body.get("content") == null){
                    HttpService.sendErrorResponse(exchange, 400, null, "Missing userID, postID or content.".getBytes());
                    return;
                }
                Comments.create(body.get("userid"), body.get("postid"), body.get("content"));
                HttpService.serve(exchange, 200, "text/json", "Done!".getBytes());
            }
        }
    }
}