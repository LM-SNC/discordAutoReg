package Modules;

import Data.CONSTANTS;
import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;

public class Phone {
    WebUtils webUtils;
    String[] data;
    String previewCode = "";
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
        int counter = 0;
        String code = "";
        while (true) {
            if (counter >=5)
                return code;
            response = webUtils.getTextCon(Connection.Method.POST,
                    "http://cheapsms.pro/stubs/handler_api.php?api_key=" + CONSTANTS.numberToken + "&action=getStatus&id=" + data[1],
                    "https://google.com");
            System.out.println(response);
            if(!response.contains("WAIT")) {
                code = response.replaceAll("[^\\d.]", "");
                Logger.logFuncEnd(code);
                if (!code.equalsIgnoreCase(previewCode)) {
                    previewCode = code;
                    return code;
                }
                else
                    continue;
            }
            //Get new sms, else INVALID_PHONE_VERIFICATION_CODE, LOG in txt file
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }
}
