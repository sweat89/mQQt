package majiang.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import majiang.dto.RoomDto;
import majiang.dto.User;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class MajiangRedis {
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    private Gson gson = new Gson();

    private static final String ROOM_DETAIL = "mahjong_room_detail";

    private static final String ROOM_ALL = "mahjong_all_rooms";

    private static final String ROOM_MAHJONG = "room_mahjong";

    private static final String USER_ROOM = "user_rooms";

    private static final String READY_ROOM = "ready_room";

    private static final String USER_SHOUPAI = "user_shoupai";

    private final static String[] mahjong = { "1T", "2T", "3T", "4T", "5T", "6T", "7T", "8T", "9T", "1S", "2S", "3S",
            "4S", "5S", "6S", "7S", "8S", "9S", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "8W", "9W", "DONG", "NAN",
            "XI", "BEI", "ZHONG", "FA", "BAI", "1T", "2T", "3T", "4T", "5T", "6T", "7T", "8T", "9T", "1S", "2S", "3S",
            "4S", "5S", "6S", "7S", "8S", "9S", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "8W", "9W", "DONG", "NAN",
            "XI", "BEI", "ZHONG", "FA", "BAI", "1T", "2T", "3T", "4T", "5T", "6T", "7T", "8T", "9T", "1S", "2S", "3S",
            "4S", "5S", "6S", "7S", "8S", "9S", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "8W", "9W", "DONG", "NAN",
            "XI", "BEI", "ZHONG", "FA", "BAI", "1T", "2T", "3T", "4T", "5T", "6T", "7T", "8T", "9T", "1S", "2S", "3S",
            "4S", "5S", "6S", "7S", "8S", "9S", "1W", "2W", "3W", "4W", "5W", "6W", "7W", "8W", "9W", "DONG", "NAN",
            "XI", "BEI", "ZHONG", "FA", "BAI", };

    public RoomDto createRoom(long userID, long roomID) {
        RoomDto room = null;
        ShardedJedis redis = null;
        List<User> userList = new ArrayList<>();
        try {
            redis = shardedJedisPool.getResource();
            room = new RoomDto();
            room.setRoomId(roomID);
            User user = new User();
            user.setId(userID);
            userList.add(user);
            room.setUsers(userList);
            String json = gson.toJson(room);
            redis.lpush(ROOM_ALL, json);
            redis.hset(ROOM_DETAIL, String.valueOf(roomID), json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return room;
    }

    public List<String> listRoom(int status) {
        List<String> response = null;
        ShardedJedis redis = null;
        try {
            redis = shardedJedisPool.getResource();
            List<String> listRoom = redis.lrange(ROOM_ALL, 0, -1);
            return listRoom;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return response;
    }

    public Map<String, Object> joinRoom(long userid, long roomid) {

        Map<String, Object> map = new HashMap<>();
        ShardedJedis redis = null;
        try {
            redis = shardedJedisPool.getResource();
            String roomInfo = redis.hget(ROOM_DETAIL, String.valueOf(roomid));
            if (roomInfo == null || "".equals(roomInfo)) {
                map.put("status", -1);
                return map;
            }

            RoomDto roomDto = new Gson().fromJson(roomInfo, RoomDto.class);
            if (roomDto.getUsers().size() >= 4 || roomDto.getStatus() == 1) {
                map.put("status", -2);
                return map;
            }
            redis.lrem(ROOM_ALL, 0, roomInfo);
            List<User> userList = roomDto.getUsers();
            for (User user : userList) {
                if (user.getId() == userid) {
                    map.put("status", -3);
                    map.put("data", roomDto);
                    return map;
                }
            }
            User user = new User(userid, 0);
            userList.add(user);
            String json = gson.toJson(roomDto);
            redis.lpush(ROOM_ALL, json);
            redis.hset(ROOM_DETAIL, String.valueOf(roomid), json);
            map.put("status", 0);
            map.put("data", roomDto);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return map;
    }

    public int ready(long userid, long roomid, int status) {
        ShardedJedis redis = null;
        try {
            redis = shardedJedisPool.getResource();
            String roomInfo = redis.hget(ROOM_DETAIL, String.valueOf(roomid));
            RoomDto roomDto = new Gson().fromJson(roomInfo, RoomDto.class);
            for (User user : roomDto.getUsers()) {
                if (user.getId() == userid) {
                    user.setStatus(status);
                }
            }
            if (status == 0) { // 取消准备
                this.updateRoomStatus(redis, roomDto, roomInfo, 0);
                return 2;
            }
            int roomStatus = 0;
            if (roomDto.getUsers().size() > 1) { // 已超过1人准备
                boolean ok = true;
                for (User user : roomDto.getUsers()) {
                    if (user.getStatus() != 1) {
                        ok = false;
                        break;
                    }
                }
                if (ok)
                    roomStatus = 1;
            }
            this.updateRoomStatus(redis, roomDto, roomInfo, roomStatus);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return 0;
    }

    public void updateRoomStatus(ShardedJedis redis, RoomDto roomDto, String roomInfo, int status) {
        redis.lrem(ROOM_ALL, 0, roomInfo);
        roomDto.setStatus(status);
        redis.lpush(ROOM_ALL, gson.toJson(roomDto));
        String json2 = gson.toJson(roomDto);
        redis.hset(ROOM_DETAIL, String.valueOf(roomDto.getRoomId()), json2);
    }

    public List<String> fapai(long roomid, long userid) {
        ShardedJedis redis = null;
        List<String> list = null;
        try {
            redis = shardedJedisPool.getResource();
            String key = ROOM_MAHJONG + "_" + roomid;
            list = Arrays.asList(mahjong);
            if (!redis.exists(key)) {
                Collections.shuffle(list);
                redis.lpush(key, (String[]) list.toArray());
                redis.expire(key, 24 * 60 * 60);
            }
            list = redis.lrange(key, 0, 12);
            redis.ltrim(key, 12, -1);
            redis.hset(USER_SHOUPAI, String.valueOf(userid), gson.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return list;
    }

    public Map<String, Object> mopai(long roomid, long userid) {
        Map<String, Object> map = new HashMap<>();
        ShardedJedis redis = null;
        List<String> list = null;
        try {
            redis = shardedJedisPool.getResource();
            String shoupai = redis.hget(USER_SHOUPAI, String.valueOf(userid));
            list = (List<String>) gson.fromJson(shoupai, List.class);
            String pai = redis.lpop(ROOM_MAHJONG + "_" + roomid);
            list.add(pai);
            String[] pais = list.toArray(new String[0]);
            map.put("mPai", pai);
            map.put("isWin", "1"); // 0hu 1no hu
            map.put("shoupai", list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedisPool.returnResource(redis);
        }
        return map;
    }

}
