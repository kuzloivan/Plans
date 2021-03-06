package chisw.com.dayit.model;

import com.parse.ParseObject;

import chisw.com.dayit.db.entity.PlansEntity;

public class Plan {
    public static final String PLAN_STATE_LOCAL = "localPlan";
    public static final String PLAN_STATE_REMOTE_NOT_ANSWERED = "remotePlanNotAnswered";
    public static final String PLAN_STATE_REMOTE_ACCEPTED = "remotePlanAccepted";
    public static final String PLAN_STATE_REMOTE_REJECTED = "remotePlanRejected";

    private String parseId;
    private String title;
    private String details;
    private long timeStamp;
    private String audioPath;
    private int localId;
    private int audioDuration;
    private String daysToAlarm;
    private String imagePath;
    private int isDeleted;
    private int isSynchronized;
    private long updatedAtParseTime;
    private int isRemote;
    private String sender;
    private String planState;
    private String sourcePlanID;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
    // todo: add field for plan repeating info (Example: Sun/Mon/Tue instead of 18/03/10)

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }

    public void setDaysToAlarm(String pDaysToAlarm) { //DOW
        this.daysToAlarm = pDaysToAlarm;
    }

    public String getDaysToAlarm() {   //DOW
        return daysToAlarm;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsSynchronized() {
        return isSynchronized;
    }

    public void setIsSynchronized(int isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public long getUpdatedAtParseTime() {
        return updatedAtParseTime;
    }

    public void setUpdatedAtParseTime(long updatedAtParseTime) {
        this.updatedAtParseTime = updatedAtParseTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(int pIsRemote) {
        isRemote = pIsRemote;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String pSender) {
        sender = pSender;
    }

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String pPlanState) {
        planState = pPlanState;
    }

    public String getSourcePlanID() {
        return sourcePlanID;
    }

    public void setSourcePlanID(String pSourcePlanID) {
        sourcePlanID = pSourcePlanID;
    }

    public Plan setPlanFromParse(ParseObject pParsePlan) {
        parseId = pParsePlan.getObjectId();
        title = pParsePlan.getString(PlansEntity.TITLE);
        details = pParsePlan.getString(PlansEntity.DETAILS);
        timeStamp = pParsePlan.getLong(PlansEntity.TIMESTAMP);
        imagePath = pParsePlan.getString(PlansEntity.IMAGE_PATH);
        audioPath = pParsePlan.getString(PlansEntity.AUDIO_PATH);
        audioDuration = pParsePlan.getInt(PlansEntity.AUDIO_DURATION);
        daysToAlarm = pParsePlan.getString(PlansEntity.DAYS_TO_ALARM);
        updatedAtParseTime = pParsePlan.getUpdatedAt().getTime();
        planState = pParsePlan.getString(PlansEntity.PLAN_STATE);
        sender = pParsePlan.getString(PlansEntity.SENDER);

        if (pParsePlan.getBoolean(PlansEntity.IS_REMOTE) == false) {
            isRemote = 0;

        } else {
            isRemote = 1;
            sourcePlanID = pParsePlan.getString(PlansEntity.SOURCE_PLAN_ID);
        }
        return this;
    }
}
