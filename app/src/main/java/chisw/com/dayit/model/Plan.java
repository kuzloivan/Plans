package chisw.com.dayit.model;

/**
 * Created by Alexander on 17.06.2015.
 */
public class Plan {
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

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
    // todo: add field for plan status.

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

    public void setDaysToAlarm(String pDaysToAlarm){ //DOW
        this.daysToAlarm = pDaysToAlarm;
    }

    public String getDaysToAlarm(){   //DOW
        return this.daysToAlarm;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public int getIsSynchronized() {
        return isSynchronized;
    }

    public void setIsSynchronized(int isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
