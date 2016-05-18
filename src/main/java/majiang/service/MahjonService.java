package majiang.service;

import java.util.List;
import java.util.Map;

import majiang.dto.RoomDto;

public interface MahjonService {
    RoomDto createRoom(long userid, long roomid);

    List<RoomDto> listRoom(int status);

    Map<String, Object> joinRoom(long userid, long roomid);

    int ready(long userID, long roomID, int status);

    List<String> fapai(long roomid, long userid);

    Map<String, Object> mopai(long roomid, long userid);
}
