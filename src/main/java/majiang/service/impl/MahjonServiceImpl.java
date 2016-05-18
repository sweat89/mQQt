package majiang.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import majiang.dto.RoomDto;
import majiang.redis.MajiangRedis;
import majiang.service.MahjonService;

@Service("mahjonService")
public class MahjonServiceImpl implements MahjonService {

    @Autowired
    private MajiangRedis majiangRedis;

    @Override
    public RoomDto createRoom(long userid, long roomid) {
        return majiangRedis.createRoom(userid, roomid);
    }

    @Override
    public List<RoomDto> listRoom(int status) {
        List<String> list = majiangRedis.listRoom(status);
        List<RoomDto> listRoom = new ArrayList<>();
        for (String room : list) {
            RoomDto roomDto = new Gson().fromJson(room, RoomDto.class);
            listRoom.add(roomDto);
        }
        return listRoom;
    }

    @Override
    public Map<String, Object> joinRoom(long userid, long roomid) {
        Map<String, Object> map = majiangRedis.joinRoom(userid, roomid);
        return map;
    }

    @Override
    public int ready(long userID, long roomID, int status) {
        return majiangRedis.ready(userID, roomID, status);
    }

    @Override
    public List<String> fapai(long roomid, long userid) {
        return majiangRedis.fapai(roomid, userid);
    }

    @Override
    public Map<String, Object> mopai(long roomid, long userid) {
        return majiangRedis.mopai(roomid, userid);
    }

}
