package currentshit.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import org.bson.Document;

import currentshit.App;
import currentshit.HttpService;
import currentshit.MongoService;

import java.io.IOException;
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
                Document[] users = MongoService.getUsers();
                if(users.length == 0){
                    builder.append("[]");
                }else{
                    for(int i = 0; i < users.length; i++){
                        builder.append(users[i].toJson());
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
                App.mongoService.createUser(body.get("username"), body.get("password"), body.get("email"));
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
                Document[] posts = MongoService.getPosts(tags);
                System.out.println("found posts: " + posts.length);
                if(posts.length == 0){
                    builder.append("[]");
                }else{
                    for(int i = 0; i < posts.length; i++){
                        builder.append(posts[i].toJson());
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
                boolean success = App.mongoService.createPost(body.get("title"), body.get("userid"), body.get("content"), tags);
                HttpService.serve(exchange, 200, "text/plain", new String("Done! " + success).getBytes());
            }
        }
        if(exchange.getRequestURI().getPath().equals("/api/comments")){
            if(exchange.getRequestMethod().equals("GET")){

            }else if(exchange.getRequestMethod().equals("POST")){
                
            }
        }
    }

    public void serveUsers() {

    }

}