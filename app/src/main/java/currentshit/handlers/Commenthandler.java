package currentshit.handlers;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

import currentshit.HttpService;
import currentshit.dao.CommentField;
import currentshit.dao.Comments;
import currentshit.util.QueryMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Commenthandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException{
        if(exchange.getRequestMethod().equals("GET")){
            QueryMap queryMap = new QueryMap(exchange.getRequestURI().getQuery());
            String postID = queryMap.parameters.get("postid");
            StringBuilder builder = new StringBuilder();
            CommentField[] comments = Comments.getByPostID(postID);
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
