package model;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

public class Request implements Identifiable {
    private int id;
    private RequestType type;
    private int userId;
    private LocalDate dateCreated;
    private String details;
    private RequestStatus status;
    private Map<String, String> params;

    public Request(RequestType type, int userId, String details) {
        this.id = IdGen.getInstance().newId();
        this.type = type;
        this.userId = userId;
        this.dateCreated = LocalDate.now();
        this.details = details;
        this.status = RequestStatus.PENDING;
        this.params = new HashMap<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    public RequestType getType() {
        return this.type;
    }
    public int getUserId() {
        return this.userId;
    }
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }
    public String getDetails() {
        return this.details;
    }
    public RequestStatus getStatus() {
        return this.status;
    }

    public void setApproved() {
        this.status = RequestStatus.APPROVED;
    }
    public void setRejected() {
        this.status = RequestStatus.REJECTED;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }
    public String getParam(String key) {
        return this.params.get(key);
    }
}
