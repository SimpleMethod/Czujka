package pl.simplemethod.czujka.botparser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class BotController {
    private static final Logger logger = LogManager.getLogger(BotController.class);

    private final String token;
    private final String verificationToken;
    private final String botChannel;

    /**
     * @param token Token for query authorization
     * @param verificationToken Token confirming the authenticity of the request
     * @param botChannel Channel where bot can wite notifications
     */
    public BotController(String token, String verificationToken, String botChannel) {
        this.token = token;
        this.verificationToken = verificationToken;
        this.botChannel = botChannel;
    }

    public String getbotChannel() {
        return botChannel;
    }

    /**
     *  Verifies that the authorization token is correct.
     * @param token  Verification token.
     * @return Returns true if the key is correct and false if there are errors.
     */
    public Boolean tokenAuth(String token)
    {
        return token.equals(this.verificationToken);
    }


    /**
     * https://api.slack.com/methods/chat.scheduleMessage
     * Method schedules a message for delivery to a public channel, private channel, or direct message/IM channel at a specified time in the future.
     * Tier 3
     * @param channel Channel on which the bot can write.
     * @param post_at Determining the date of sending the message at timestamp.
     * @param text Required argument, simple text.
     * @return JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject postScheduleMessage(String channel, Integer post_at, String text)
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> postChatMessage = Unirest.post("https://slack.com/api/chat.scheduleMessage")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).queryString("post_at", post_at).queryString("text", text).queryString("channel", channel).asJson();
            obj = parser.parse(postChatMessage.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                if(status)
                {
                    body.put("ok", status);
                    body.put("channel", jsonObject.get("channel"));
                    body.put("post_at", jsonObject.get("post_at"));
                    body.put("scheduled_message_id", jsonObject.get("scheduled_message_id"));
                }
                else
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }
        }
        return body;
    }

    /**
     * https://api.slack.com/methods/chat.scheduleMessage
     * Method schedules a message for delivery to a public channel, private channel, or direct message/IM channel at a specified time in the future.
     * Tier 3
     * @param channel Channel on which the bot can write.
     * @param post_at Determining the date of sending the message at timestamp.
     * @param text Required argument, send one space.
     * @param blocks  Send as JSON -> https://api.slack.com/tools/block-kit-builder .
     * @return JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject postRichScheduleMessage(String channel, Integer post_at, String text, String blocks)
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> postRichChatMessage = Unirest.post("https://slack.com/api/chat.scheduleMessage")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).queryString("post_at", post_at).queryString("text", text).queryString("channel", channel).queryString("blocks", blocks).asJson();
            obj = parser.parse(postRichChatMessage.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                if(status)
                {
                    body.put("ok", status);
                    body.put("post_at", jsonObject.get("post_at"));
                    body.put("scheduled_message_id", jsonObject.get("scheduled_message_id"));
                }
                else
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }
        }
        return body;
    }


    /**
     * https://api.slack.com/methods/chat.postMessage
     * This method posts a message to a public channel, private channel, or direct message.
     * TIER: Special (Posting messages	1 per second Incoming webhooks	1 per second).
     * @param channel Channel on which the bot can write.
     * @param text Required argument, simple text.
     * @return JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject postChatMessage(String channel, String text)
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> postChatMessage = Unirest.post("https://slack.com/api/chat.postMessage")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).queryString("text", text).queryString("channel", channel).asJson();
            obj = parser.parse(postChatMessage.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                if(status)
                {
                    body.put("ok", status);
                    body.put("channel", jsonObject.get("channel"));
                    body.put("ts", jsonObject.get("ts"));
                }
                else
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }
        }
        return body;
    }

    /**
     * https://api.slack.com/methods/chat.postMessage
     * This method posts a message to a public channel, private channel, or direct message.
     * TIER: Special (Posting messages	1 per second Incoming webhooks	1 per second).
     * @param channel Channel on which the bot can write.
     * @param text Required argument, send one space.
     * @param blocks Send as JSON -> https://api.slack.com/tools/block-kit-builder .
     * @return  JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject postRichChatMessage(String channel, String text, String blocks)
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> postRichChatMessage = Unirest.post("https://slack.com/api/chat.postMessage")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).queryString("text", text).queryString("channel", channel).queryString("blocks", blocks).asJson();
            obj = parser.parse(postRichChatMessage.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                if(status)
                {
                    body.put("ok", status);
                    body.put("channel", jsonObject.get("channel"));
                    body.put("ts", jsonObject.get("ts"));
                }
                else
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }
        }
        return body;
    }

    /**
     * https://api.slack.com/methods/im.open
     * Method opens a direct message channel with another member of your Slack team.
     * TIER:4
     * @param user User to open a direct message channel with.
     * @return Returns the private channel of the specified user.
     */
    public String getUserPrivateChannelID(String user)
    {
        JSONParser parser = new JSONParser();
        String ret=String.valueOf(false);
        Object obj = null;
        try {
            HttpResponse<JsonNode> getUserPrivateChannelID = Unirest.post("https://slack.com/api/im.open")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).queryString("user", user).asJson();
            obj = parser.parse(getUserPrivateChannelID.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            logger.error("Founds exception: "+e);
            ret = String.valueOf(false);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                if(status)
                {
                   JSONObject channel = (JSONObject) jsonObject.get("channel");
                   ret = String.valueOf(channel.get("id"));
                }
                else
                {
                    ret = String.valueOf(false);
                }
            }
        }
        return ret;
    }

    /**
     * https://api.slack.com/methods/api.test
     * Method to test connection.
     * TIER 4
     * @return  JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject getApiStatus()
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> getApiStatus = Unirest.post("https://slack.com/api/api.test")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).asJson();
            obj = parser.parse(getApiStatus.getBody().toString());
            if(obj==null)
            {
                throw new  NullPointerException();
            }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                body.put("ok",status);
                if(!status)
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }

        }
        return body;
    }

    /**
     * https://api.slack.com/methods/auth.test
     *  Method checks authentication and tells "you" who you are, even if you might be a bot.
     *  TIER: Special ( Posting messages	1 per second Incoming webhooks	1 per second )
     * @return  JSON object with information on the outcome of the operation.
     */
    public org.json.JSONObject postAuthTest()
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;

        try {
            HttpResponse<JsonNode> postAuthTest = Unirest.post("https://slack.com/api/auth.test")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("token", token).asJson();
            obj = parser.parse(postAuthTest.getBody().toString());
            body=(org.json.JSONObject)obj;
        }
        catch (ParseException | UnirestException e)
        {
            logger.error("Founds exception: "+e);
            body.put("exception",e);
        }
        return body;
    }

    /**
     * https://api.slack.com/methods/users.list
     * Method returns a list of all users in the workspace. This includes deleted/deactivated users.
     * TIER 2
     * @return Returns a list of users as a JSON object
     */
    public org.json.JSONObject getUsersList()
    {
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;

        try
        {
            HttpResponse<JsonNode> getUsersList = Unirest.get("https://slack.com/api/users.list")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("pretty","1").queryString("token", token).asJson();
             obj = parser.parse(getUsersList.getBody().toString());
             if(obj==null)
             {
                 throw new  NullPointerException();
             }
        }
        catch (NullPointerException | ParseException | UnirestException e)
        {
            body.put("ok",false);
            body.put("exception",e);
            logger.error("Founds exception: "+e);
        }
        finally {
            if(obj!=null)
            {
                JSONObject jsonObject = (JSONObject)obj;
                Boolean status = (Boolean) jsonObject.get("ok");
                body.put("ok",status);
                if(status)
                {
                    JSONArray members = (JSONArray) jsonObject.get("members");
                    org.json.JSONArray users = new org.json.JSONArray();
                    Iterator<JSONObject> iterator = members.iterator();
                    while (iterator.hasNext()) {
                        org.json.JSONObject user = new org.json.JSONObject();
                        JSONObject profile = iterator.next();
                        user.put("id", profile.get("id"));
                        user.put("team_id", profile.get("team_id"));
                        user.put("real_name", profile.get("real_name"));
                        user.put("name", profile.get("name"));
                        user.put("is_bot", profile.get("is_bot"));
                        user.put("tz", profile.get("tz"));
                        users.put(user);
                    }
                    body.put("users", users);
                }
                else
                {
                    String error = (String) jsonObject.get("error");
                    body.put("error",error);
                }
            }
            System.out.println(body.toString());
        }
        return body;
    }
}
