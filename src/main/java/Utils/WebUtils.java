package Utils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.HashMap;

public class WebUtils {

     HashMap<String, String> data;
     HashMap<String, String> header;
     HashMap<String, String> cookies;
     HashMap<String, String> headerModel;
     String body;
     boolean block = false;

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

            Connection.Response resp = loginForm.execute();
            if(!block)
                clear();

            return resp;
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

    public boolean clearBlocker() {
        block = !block;
        if (!block)
            clear();
        return block;
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