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

     HashMap<String, String> data;
     HashMap<String, String> header;
     HashMap<String, String> cookies;
     HashMap<String, String> headerModel;
     String body;

    public WebUtils (HashMap<String, String> headerModel) {
        data = new HashMap();
        cookies = new HashMap();
        header = new HashMap();

        this.headerModel = headerModel;
        body = "";

    }
    private Connection.Response sendRequest(Connection.Method method, String url, String referer) {
        try {
            Connection loginForm = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/1.0.9002 Chrome/83.0.4103.122 Electron/9.3.5 Safari/537.36")
                    .referrer(referer)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(method)
                    .timeout(30000);
            loginForm.headers(headerModel);

            if (!data.isEmpty())
                loginForm.data(data);
            if(!cookies.isEmpty())
                loginForm.cookies(cookies);
            if (!header.isEmpty())
                loginForm.headers(header);
            if (!body.isEmpty())
                loginForm.requestBody(body);

            return loginForm.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection.Response getRequestCon(Connection.Method method, String url, String referer) {
        return sendRequest(method, url, referer);
    }

    public String getTextCon(Connection.Method method, String url, String referer) {
        try {
            Connection.Response response = sendRequest(method, url, referer);
            if (response != null) {
                return response.parse().text();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document getParsedCon(Connection.Method method, String url, String referer) {
        try {
            Document response = sendRequest(method, url, referer).parse();
            if (response != null) {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        cookies.put(key, value);
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
        cookies.clear();
        body = "";
    }
}
