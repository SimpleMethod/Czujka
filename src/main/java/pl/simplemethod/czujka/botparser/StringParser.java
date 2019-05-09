package pl.simplemethod.czujka.botparser;

public class StringParser {

    // Use only for response!
    private final static String unsubscribePerson ="{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Wypisałeś się z dzisiejszego zamykania biura :cry:\"}]}";
    private final static String unsubscribeGlobal ="{\"response_type\":\"in_channel\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Użytkownik *user_name* wypisał się z dzisiejszego zamykania biura :cry:\"}]}";
    private final static String signUpPerson = "{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: zapisałeś się do dzisiejszego zamykania biura o godzinie *close_hours*\"}]}";
    private final static String signUpGlobal = "{\"response_type\":\"in_channel\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: użytkownik *user_name* zapisał się do dzisiejszego zamykania biura o godzinie *close_hours*\"}]}";

    private final static String leavePerson = "{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Wypisałeś się z dzisiejszego zamykania biura :cry:\"}]}";
    private final static String leavePenultimatePerson = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Zostałeś wybrany do zamykania biura :( Twój poprzednik się wypisał.\"}}]";

    private final static String leavePersonFound = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Nie jesteś już ostatnią osobą wychodzącą z biura, nie zamykasz ;)\"}}]";
    private final static String leavePersonAttend = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Jesteś ostatnią osobą, zamykasz biuro :)\"}}]";
    private final static String queuePerson = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Jesteś *user_que* w kolejce\"}}]";

    // Exceptions handlers
    private final static String leavePersonNull = "{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Wystąpił błąd, prawdopodobnie nie byłeś zapisany do zamykania biura dzisiaj.\"}]}";
    private final static String leavePersonSaveNull = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Wystąpił błąd z bazą danych. Spróbuj ponownie :(\"}}]";

    private final static String leavePersonEntry = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Wprowadzone dane są niepoprawne. Spróbuj ponownie! \\nPrawidłowy format godziny to \\\"HH:MM\\\".\"}}]";


    private final static String leavePersonEntryPerson = "{\"response_type\":\"ephemeral\",\"attachments\":[{\"type\":\"mrkdwn\",\"text\":\"Wprowadzone dane są niepoprawne. Spróbuj ponownie! \\nPrawidłowy format godziny to \\\"HH:MM\\\".\"}]}";

    // Use only for global messages (API -> chat.)!Wypisałeś się z dzisiejszego zamykania biura.
    private final static String unsubscribeGlobalBlock = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Użytkownik *user_name* wypisał się z dzisiejszego zamykania biura :cry:\"}}]";
    private final static String signUpGlobalBlock = "[{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"Hura :smile: użytkownik *user_name* zapisał się do dzisiejszego zamykania biura o godzinie *close_hours*\"}}]";

    public String getUnsubscribePerson()
    {
        return unsubscribePerson;
    }

    public String getLeavePersonEntry() {
        return leavePersonEntry;
    }

    public  String getLeavePersonEntryPerson() {
        return leavePersonEntryPerson;
    }


    public String getLeavePersonSaveNull() {
        return getLeavePersonNull();
    }

    public String getLeavePersonAttend() {
        return leavePersonAttend;
    }

    public static String getQueuePerson(String user_que) {
        String master =queuePerson;
        return master.replace("user_que", user_que);
    }

    public String getLeavePersonFound() {
        return leavePersonFound;
    }

    public String getLeavePersonNull() {
        return leavePersonNull;
    }

    public String getLeavePerson() {
        return leavePerson;
    }

    public String getLeavePenultimatePerson() {
        return leavePenultimatePerson;
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
