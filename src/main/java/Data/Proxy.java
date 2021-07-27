package Data;

import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Proxy {
    WebUtils webUtils = new WebUtils(CONSTANTS.defaultModel);

    public void getProxy(String type) {
        Logger.logFuncStart();
        String proxyPage = "https://hidemy.name/ru/proxy-list/?type=" + type + "#list";
        Elements proxyList = webUtils.getParsedCon(Connection.Method.GET,
                proxyPage,
                "https://google.com/").getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Logger.logInfo("Start gathering free proxy...");
        for (Element element : proxyList) {
            Elements td = element.getElementsByTag("td");
            String ip = td.get(0).text();
            String port = td.get(1).text();
            System.setProperty("https.proxyHost", ip);
            System.setProperty("https.proxyPort", port);

            Document response = webUtils.getParsedCon(Connection.Method.POST,
                    "https://discord.com/register",
                    "https://discord.com/");
            if (response != null) {
                Logger.logInfo("Success - " + ip + ":" + port);
                Logger.logFuncEnd();
                break;
            }
        }
        Logger.logFuncEnd();
    }
}
