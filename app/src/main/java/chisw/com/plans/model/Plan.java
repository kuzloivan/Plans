package chisw.com.plans.model;

/**
 * Created by Alexander on 17.06.2015.
 */
public class Plan {
    private String parseId;
    private String title;
    private long timeStamp;

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
