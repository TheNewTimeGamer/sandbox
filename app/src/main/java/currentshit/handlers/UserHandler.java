package currentshit.handlers;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

import currentshit.HttpService;
import currentshit.dao.UserField;
import currentshit.dao.Users;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UserHandler implements HttpHandler {
    
    public void handle(HttpExchange exchange) throws IOException{
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

}
