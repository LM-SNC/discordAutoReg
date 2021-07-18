import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.invoke.empty.Empty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebUtils {

    private HashMap<String, String> data, header, cookie;
    private String body;

    public WebUtils () {
        data = new HashMap();
        cookie = new HashMap();
        header = new HashMap();
        body = "";

    }
    public String sendRequest(Connection.Method method, String url, String referer) {

        try {
            Connection loginForm = Jsoup.connect(url)
                    .header("content-type", "application/json")
                    .header("scheme", "https")
                    .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("authority", "discord.com")
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("referer", referer)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/1.0.9002 Chrome/83.0.4103.122 Electron/9.3.5 Safari/537.36")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(method)
                    .timeout(30000);

            if (!data.isEmpty())
                loginForm.data(data);
            if(!cookie.isEmpty())
                loginForm.cookies(cookie);
            if (!header.isEmpty())
                loginForm.headers(header);
            if (!body.isEmpty())
                loginForm.requestBody(body);

            clear();
            return loginForm.execute().parse().text();

        } catch (IOException e) {
            e.printStackTrace();
        }
        clear();
        return null;
    }

    private JsonNode getValue (String json, String key, boolean debug) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
        JsonNode value = null;
        while (fieldsIterator.hasNext()) {
            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            if (debug)
                System.out.println("Key: " + field.getKey() + " Value: " + field.getValue());
            if (field.getKey().equalsIgnoreCase(key)) {
                //System.out.println(field.getValue().textValue());
                if (field.getValue().isArray())
                    value = field.getValue().get(0);
                else {
                    value = field.getValue();
                }
            }
        }
        return value;
    }

    public String getTextValue(String json, String key, boolean debug) {
        JsonNode response = getValue(json, key, debug);
        if (response != null)
            return response.textValue();
        else
            return null;
    }

    public String getStringValue(String json, String key, boolean debug) {
        JsonNode response = getValue(json, key, debug);
        if (response != null)
            return response.toString();
        else
            return null;
    }

    public void addCookie(String key, String value) {
        cookie.put(key, value);
    }

    public void addHeader(String key, String value){
        header.put(key, value);
    }

    public void addData(String key, String value) {
        data.put(key, value);
    }

    public void addBody(String body) {
        this.body = body;
    }

    public void clear() {
        data.clear();
        header.clear();
        cookie.clear();
        body = "";
    }
}
