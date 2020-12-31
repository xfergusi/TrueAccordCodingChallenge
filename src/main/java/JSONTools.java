import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public final class JSONTools {

    public static JSONArray getJsonArray(String JsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(JsonString);
        return (JSONArray)obj;
    }

    public static String getJsonString(String urlString) throws IOException {
        URL url = new URL(urlString);
        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();
        return inline.toString();

    }

}
