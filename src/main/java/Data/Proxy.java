package Data;

import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Proxy {
    WebUtils webUtils = new WebUtils(CONSTANTS.defaultModel);
    String proxyType = "";

    public Proxy(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxy() {
        Logger.logFuncStart();
        if (proxyType.isEmpty() || proxyType == null) {
            Logger.logError("Invalid proxy type");
            return "";
        }
        String proxyPage = "https://hidemy.name/ru/proxy-list/?type=" + proxyType + "#list";
        Document response = webUtils.getParsedCon(Connection.Method.GET,
                proxyPage,
                "https://google.com/",
                true);
        if (response == null) {
            Logger.logError("Null");
            return "";
        }
        Elements proxyList = response.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Logger.logInfo("Start gathering free proxy...");
        for (Element element : proxyList) {
            Elements td = element.getElementsByTag("td");
            String ip = td.get(0).text();
            String port = td.get(1).text();
            webUtils.addProxy(ip, port);
            response = webUtils.getParsedCon(Connection.Method.POST,
                    "https://discord.com/register",
                    "https://discord.com/",
                    true);
            if (response != null) {
                Logger.logInfo("Success - " + ip + ":" + port);
                return ip + ":" + port;
            }
        }
        Logger.logFuncEnd();
        return "";

    }

    public void resetProxy() {
        System.setProperty("https.proxyHost", "");
        System.setProperty("https.proxyPort", "");
    }
}
