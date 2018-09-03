package succ7.com.filmbase.bean.bmob;

/**
 * 短信实体
 */
public class SmsInfo {

    /**
     * 短信Id
     */
    private long id;
    /**
     * 短信内容
     */
    private String smsbody;
    /**
     * 发送短信的电话号码
     */
    private String phoneNumber;
    /**
     * 发送短信的日期和时间
     */
    private long date;
    /**
     * 发送短信人的姓名
     */
    private String name;
    /**
     * 短信类型1是接收到的，2是已发出
     */
    private String type;
    /**
     * read：是否阅读0未读，1已读
     */
    private int read;
    /**
     * 手表ID
     */
    private long watchId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSmsbody() {
        return smsbody;
    }

    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public long getWatchId() {
        return watchId;
    }

    public void setWatchId(long watchId) {
        this.watchId = watchId;
    }

    @Override
    public String toString() {
        return "SmsInfo [id=" + id + ", smsbody=" + smsbody + ", phoneNumber=" + phoneNumber + ", date=" + date
                + ", name=" + name + ", type=" + type + ", read=" + read + ", watchId=" + watchId + "]";
    }


}
