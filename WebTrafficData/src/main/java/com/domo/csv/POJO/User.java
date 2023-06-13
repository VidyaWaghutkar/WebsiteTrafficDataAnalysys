package com.domo.csv.POJO;
import com.opencsv.bean.CsvBindByName;

public class User {
    @CsvBindByName
    private  String timestamp;
    @CsvBindByName
    private  String session_id;
    @CsvBindByName
    private  String page_id;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "timestamp='" + timestamp + '\'' +
                ", session_id='" + session_id + '\'' +
                ", page_id='" + page_id + '\'' +
                '}';
    }
}
