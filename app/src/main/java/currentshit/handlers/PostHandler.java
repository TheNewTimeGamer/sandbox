package currentshit.handlers;

import java.io.IOException;

import com.google.gson.Gson;

import org.bson.types.ObjectId;

import currentshit.HttpService;
import currentshit.dao.PostField;
import currentshit.dao.Posts;
import currentshit.util.QueryMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PostHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException{
        if(exchange.getRequestMethod().equals("GET")){
            String[] tags = new String[0];
            QueryMap queryMap = new QueryMap(exchange.getRequestURI().getQuery());
            if(queryMap.parameters.containsKey("tags")){
                tags = queryMap.parameters.get("tags").split(",");
            }
            StringBuilder builder = new StringBuilder();
            PostField[] posts = null;
            if(queryMap.parameters.containsKey("id")){
                posts = new PostField[]{Posts.getByID(new ObjectId(queryMap.parameters.get("id")))};
            }else{
                if(queryMap.parameters.containsKey("limit") && queryMap.parameters.containsKey("skip")){
                    posts = Posts.get(tags, Integer.parseInt(queryMap.parameters.get("skip")), Integer.parseInt(queryMap.parameters.get("limit")));
                }else{
                    posts = Posts.get(tags);
                }
            }
            if(posts.length == 0){
                builder.append("[]");
            }else{
                Gson gson = new Gson();
                builder.append("[");
                for(int i = 0; i < posts.length; i++){
                    builder.append(gson.toJson(posts[i]));
                    if(i < posts.length-1){
                        builder.append(",");
                    }
                }
                builder.append("]");
            }
            HttpService.serve(exchange, 200, "text/json", builder.toString().getBytes());
        }else if(exchange.getRequestMethod().equals("POST")){
            PostQuery postQuery = new Gson().fromJson(new String(exchange.getRequestBody().readAllBytes()), PostQuery.class);
            if(postQuery.title == null || postQuery.content == null || postQuery.tags == null || postQuery.userId == null){
                HttpService.sendErrorResponse(exchange, 400, null, "Missing title, content or tags.".getBytes());
                return;
            }
            boolean success = Posts.create(postQuery.title, postQuery.userId, postQuery.content, postQuery.tags);
            HttpService.serve(exchange, 200, "text/plain", new String("Done! " + success).getBytes());
        }
    }

}
