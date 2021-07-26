package Modules;

import Data.CONSTANTS;
import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;

public class Phone {
    WebUtils webUtils;
    String[] data;
    public String createDiscordNumber() {
        webUtils = new WebUtils(CONSTANTS.discordModel);

        String response =
        webUtils.getTextCon(Connection.Method.POST,
                "http://cheapsms.pro/stubs/handler_api.php?api_key=" + CONSTANTS.numberToken + "&action=getNumber&service=dc_0",
                "https://google.com");

        System.out.println(response);
        data = response.split(":");

        return data[2];//ACCESS_NUMBER:10629021:+79855032860
    }

    public String getCode() {
        Logger.logFuncStart();
        String response;
        response = webUtils.getTextCon(Connection.Method.POST,
                "http://cheapsms.pro/stubs/handler_api.php?api_key=" + CONSTANTS.numberToken + "=setStatus&status=1&id=" + data[1],
                "https://google.com");
        while (true) {
            response = webUtils.getTextCon(Connection.Method.POST,
                    "http://cheapsms.pro/stubs/handler_api.php?api_key=" + CONSTANTS.numberToken + "&action=getStatus&id=" + data[1],
                    "https://google.com");
            System.out.println(response);
            if(!response.contains("WAIT")) {
                String code = response.replaceAll("[^\\d.]", "");
                Logger.logFuncEnd(code);
                return code;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
