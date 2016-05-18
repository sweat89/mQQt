package majiang.dto;

public class User {
    private long id;
    private String userName;
    private int status;

    public User() {

    }

    public User(long userid, int status) {
        this.id = userid;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
