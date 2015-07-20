package chisw.com.dayit.net;

import android.content.Context;

import com.parse.ParsePush;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.model.Plan;

public class PushManager {
    public static final String REMOTE_PLAN = "remotePlan";
    public static final String ACCEPT_PLAN = "acceptPlan";
    public static final String REJECT_PLAN = "rejectPlan";

    public void sendRemotePlan(Context context, Plan pPlan, long pInterval, String pReciever, SendCallback pSendCallback) {
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        try {
            data.put("type", REMOTE_PLAN);
            data.put(context.getString(R.string.json_alert), pPlan.getTitle());
            data.put(context.getString(R.string.json_title), pPlan.getDetails());
            data.put(context.getString(R.string.json_time), Long.toString(pPlan.getTimeStamp()));
            data.put(context.getString(R.string.json_from), pPlan.getSender());
            if (pPlan.getImagePath() != null) {
                data.put(context.getString(R.string.json_parseId), pPlan.getParseId());
            }

        } catch (JSONException ex) {
            return;
        }
        push.setData(data);
        push.setExpirationTimeInterval(pInterval);
        push.setChannel(pReciever);
        push.sendInBackground(pSendCallback);
    }

    public void sendAcceptAnswer(Plan pPlan, String pSender) {
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        try {
            data.put("type", ACCEPT_PLAN);
            data.put("from", pSender);
        } catch (JSONException ex) {
            return;
        }
        push.setData(data);
        push.setChannel(pPlan.getSender());
        push.sendInBackground();
    }

    public void sendRejectAnswer(Plan pPlan, String pSender) {
        ParsePush push = new ParsePush();
        JSONObject data = new JSONObject();
        try {
            data.put("type", REJECT_PLAN);
            data.put("from", pSender);
        } catch(JSONException ex) {
            return;
        }
        push.setData(data);
        push.setChannel(pPlan.getSender());
        push.sendInBackground();
    }

}
