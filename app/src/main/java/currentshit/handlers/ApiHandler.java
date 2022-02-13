package currentshit.handlers;

import com.sun.net.httpserver.*;

import java.io.IOException;

public class ApiHandler implements HttpHandler {

    private final HttpHandler next;

    private final PostHandler postHandler;
    private final Commenthandler commentHandler;
    private final UserHandler userHandler;

    public ApiHandler(HttpHandler next) {
        this.next = next;
        this.postHandler = new PostHandler();
        this.commentHandler = new Commenthandler();
        this.userHandler = new UserHandler();
    }

    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().equals("/api/users")){
            this.userHandler.handle(exchange);
        }   
        if(exchange.getRequestURI().getPath().equals("/api/posts")){
            this.postHandler.handle(exchange);
        }
        if(exchange.getRequestURI().getPath().equals("/api/comments")){
            this.commentHandler.handle(exchange);
        }
    }
}