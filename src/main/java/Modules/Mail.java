package Modules;

import Data.CONSTANTS;
import Utils.JsonUtils;
import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Mail {
    WebUtils webUtils;
    JsonUtils jsonUtils;

    private String fullMail;
    private String mailCookie;
    public Mail() {
        webUtils = new WebUtils(CONSTANTS.gmailnatorModel);
        jsonUtils = new JsonUtils();
    }

    public void createEmail() {
        Logger.logFuncStart();
        //StartCreateMail
        mailCookie = webUtils.getRequestCon(Connection.Method.GET,
                "https://www.gmailnator.com/",
                "https://yandex.ru/").cookie("csrf_gmailnator_cookie");
        webUtils.clear();
        //System.out.println(mailCookie);

        webUtils.addData("action", "GenerateEmail");
        webUtils.addData("csrf_gmailnator_token", mailCookie);
        webUtils.addCookie("csrf_gmailnator_cookie", mailCookie);
        fullMail = webUtils.getTextCon(Connection.Method.POST,
                "https://www.gmailnator.com/index/indexquery",
                "https://www.gmailnator.com/");
        webUtils.clear();
        Logger.logFuncEnd(fullMail);
    }

    public String getActuallyMessage() {
        webUtils.addData("Email_address", fullMail);
        webUtils.addData("action", "LoadMailList");
        webUtils.addData("csrf_gmailnator_token", mailCookie);
        webUtils.addCookie("csrf_gmailnator_cookie", mailCookie);
        webUtils.clearBlocker();
        while (true) {
            Element link = webUtils.getParsedCon(Connection.Method.POST,
                    "https://www.gmailnator.com/mailbox/mailboxquery",
                    "https://www.gmailnator.com/index/indexquery").select("a").first();
            if (link != null) {
              //  System.out.println("Link: " + link);
                webUtils.clearBlocker();
                return link.attr("href").replaceAll("\\\\", "").replaceAll("\"", ""); // == "/"
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getVerifyLink(String realLink) {
        String[] sost = realLink.split("/");
        String mailPreDog = sost[3];
        String messageId = sost[5].replaceAll("#", "");

        webUtils.addData("email", mailPreDog);
        webUtils.addData("message_id", messageId);
        webUtils.addData("csrf_gmailnator_token", mailCookie);
        webUtils.addCookie("csrf_gmailnator_cookie", mailCookie);
        Document document = Jsoup.parse(jsonUtils.getTextValue(webUtils.getRequestCon(Connection.Method.POST,
                "https://www.gmailnator.com/mailbox/get_single_message/",
                "https://www.gmailnator.com/mailbox/mailboxquery").body(), "content", false));

        Element link = document.select("a").get(1);
        return link.attr("href");


    }

    public String getMail() {
        return fullMail;
    }
}
