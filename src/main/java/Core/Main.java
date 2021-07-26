package Core;

import Data.CONSTANTS;
import Utils.DataUtils;
import Utils.FileUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CONSTANTS.setDiscordModel();
        CONSTANTS.setGmailnatorModel();

        DataUtils dataUtils = new DataUtils();

        DiscordAccountsManager discordAccountsManager = new DiscordAccountsManager();

        String login = dataUtils.getDiscordLogin();
        String password = dataUtils.getDiscordPassword();

        discordAccountsManager.createAccount(login, password);
        discordAccountsManager.verifyMail();
        discordAccountsManager.verifyNumber();

       //fileUtils.fileWriter(mail + ":" + login + ":" + password);
    }
}
