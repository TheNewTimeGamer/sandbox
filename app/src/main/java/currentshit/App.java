/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package currentshit;

public class App {

    public static HttpService httpService;
    public static MongoService mongoService;

    public static void main(String[] args) {
        System.out.print("Starting HttpService..");
        App.httpService = HttpService.start(80);
        System.out.println(" Done!");
        System.out.print("Starting MongoService..");
        App.mongoService = MongoService.start("mongodb://127.0.0.1");
        System.out.println(" Done!");
    }
}