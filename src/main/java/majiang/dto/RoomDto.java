package majiang.dto;

import java.util.List;

public class RoomDto implements Cloneable {
    private long roomId;
    private int status;
    private List<User> users;

    public Object clone() {
        RoomDto o = null;
        try {
            o = (RoomDto) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        return o;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
