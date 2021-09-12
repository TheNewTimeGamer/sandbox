package currentshit;

import java.net.InetSocketAddress;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.io.OutputStream;

import com.sun.net.httpserver.*;

import currentshit.util.FileManager;

public class HttpService {

    public static String rootDirectory = "D:/programming/java/currentshit/static/";

    public static final HashMap<String,String> MIME = new HashMap<String, String>();
    static {
        MIME.put("appcache", "text/cache-manifest");
        MIME.put("css", "text/css");
        MIME.put("gif", "image/gif");
        MIME.put("html", "text/html");
        MIME.put("js", "application/javascript");
        MIME.put("json", "application/json");
        MIME.put("jpg", "image/jpeg");
        MIME.put("jpeg", "image/jpeg");
        MIME.put("mp4", "video/mp4");
        MIME.put("pdf", "application/pdf");
        MIME.put("png", "image/png");
        MIME.put("svg", "image/svg+xml");
        MIME.put("xlsm", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME.put("xml", "application/xml");
        MIME.put("zip", "application/zip");
        MIME.put("md", "text/plain");
        MIME.put("txt", "text/plain");
        MIME.put("php", "text/plain");
    };

    private static HttpService instance;

    public static HttpService start(int port) {
        if(HttpService.instance == null){
            try{
                HttpService.instance = new HttpService(80);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return HttpService.instance;
    }

    public static boolean sendErrorResponse(HttpExchange exchange, int status, byte[] data){
        try{
        exchange.sendResponseHeaders(status, data.length);
        OutputStream out = exchange.getResponseBody();
        out.write(data);
        out.flush();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        exchange.close();
        return true;
    }

    protected HttpServer server;

    public HttpService(int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/", new StaticHandler());
        this.server.start();
    }

}

class StaticHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equals("GET")){
            HttpService.sendErrorResponse(exchange, 405, "Method Not Allowed".getBytes());
            return;
        }

        String path = exchange.getRequestURI().getPath();
        if(!path.startsWith("/")){
            path = "/" + path;
        }

        if(path.equals("/")){
            path = "/index.html";
        }

        File file = new File(HttpService.rootDirectory + path);
        System.out.println("Trying to send file: " + file.getAbsolutePath());
        if(!file.exists()) {
            HttpService.sendErrorResponse(exchange, 404, "File Not Found.".getBytes());
            return;
        }
        if(file.isDirectory()){
            HttpService.sendErrorResponse(exchange, 404, "File Not Found.".getBytes());
            return;
        }
        if(!file.isFile()){
            HttpService.sendErrorResponse(exchange, 404, "File Not Found.".getBytes());
            return;
        }

        byte[] buffer = new byte[(int)file.length()];
        boolean success = FileManager.tryGetFile(file, buffer);
        if(!success){
            HttpService.sendErrorResponse(exchange, 500, "Internal Server Error.".getBytes());
            return;
        }

        String fileExtension = FileManager.getExtension(file);

        exchange.sendResponseHeaders(200, buffer.length);
        exchange.getResponseHeaders().add("Content-Type", HttpService.MIME.get(fileExtension));
        OutputStream out = exchange.getResponseBody();
        out.write(buffer);
        out.flush();
        exchange.close();
    }
    
}
