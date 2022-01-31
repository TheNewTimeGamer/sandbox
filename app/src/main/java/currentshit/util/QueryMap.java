package currentshit.util;

import java.util.HashMap;
import java.util.Map;

public class QueryMap {

    public HashMap<String, String> parameters = new HashMap<String, String>();

    public QueryMap() {}

    public QueryMap(String query) {
        if(query == null || !query.contains("=")) {
            System.err.println("Bad query: " + query);
            return;
        }
        if(query.contains("&")){
            String[] parameters = query.split("&");
            for(String parameter : parameters){
                if(!parameter.contains("=")){
                    continue;
                }
                String[] keyValue = parameter.split("=");
                if(keyValue.length < 2){
                    continue;
                }
                this.parameters.put(keyValue[0], keyValue[1]);
            }
            return;
        }
        String[] keyValue = query.split("=");
        if(keyValue.length < 2){
            return;
        }
        this.parameters.put(keyValue[0], keyValue[1]);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return builder.toString().substring(0, builder.length()-1);
    }

}