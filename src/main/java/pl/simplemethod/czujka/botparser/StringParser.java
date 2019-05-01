package pl.simplemethod.czujka.botparser;

public class StringParser {

    // Use only for response!
    private final static String unsubscribePerson ="{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Wypisałeś się z dzisiejszego zamykania biura :cry:\"}]}";
    private final static String unsubscribeGlobal ="{\"response_type\":\"in_channel\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Użytkownik *user_name* wypisał się z dzisiejszego zamykania biura :cry:\"}]}";
    private final static String signUpPerson = "{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: zapisałeś się do dzisiejszego zamykania biura o godzinie *close_hours*\"}]}";
    private final static String signUpGlobal = "{\"response_type\":\"in_channel\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: użytkownik *user_name* zapisał się do dzisiejszego zamykania biura o godzinie *close_hours*\"}]}";

    // Use only for global messages (API -> chat.)!
    private final static String unsubscribeGlobalBlock = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Użytkownik *user_name* wypisał się z dzisiejszego zamykania biura :cry:\"}}]";
    private final static String signUpGlobalBlock = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: użytkownik *user_name* zapisał się do dzisiejszego zamykania biura o godzinie *close_hours*\"}}]";

    public String getUnsubscribePerson()
    {
        return unsubscribePerson;
    }

    public String getUnsubscribeGlobal(String user_name)
    {
        String master =unsubscribeGlobal;
        return master.replace("user_name", user_name);
    }
    public String getSignUpPerson( String user_name, String close_hours)
    {
        String master =signUpPerson;
        master = master.replace("close_hours", close_hours);
        return master.replace("user_name", user_name);
    }
    public String getSignUpGlobal( String user_name, String close_hours)
    {
        String master =signUpGlobal;
        master = master.replace("close_hours", close_hours);
        return master.replace("user_name", user_name);
    }

    public String getUnsubscribeGlobalBlock(String user_name)
    {
        String master =unsubscribeGlobalBlock;
        return master.replace("user_name", user_name);
    }
    public String getSignUpGlobalBlock( String user_name, String close_hours)
    {
        String master =signUpGlobalBlock;
        master = master.replace("close_hours", close_hours);
        return master.replace("user_name", user_name);
    }
}
