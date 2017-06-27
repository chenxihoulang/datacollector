package common.datacollector;

/**
 * Created by ChaiHongwei on 2017/6/26 13:58.
 */

public class LogData {
    public String pageName;
    public String eventId;
    public long logTime;

    @Override
    public String toString() {
        return "LogData{" +
                "pageName='" + pageName + '\'' +
                ", eventId='" + eventId + '\'' +
                ", logTime=" + logTime +
                "}\n";
    }
}
