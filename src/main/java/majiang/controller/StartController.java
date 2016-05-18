package majiang.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import majiang.dto.Result;
import majiang.dto.RoomDto;
import majiang.service.MahjonService;

@Controller
@RequestMapping("/mahjong")
public class StartController extends BaseController {

    @Autowired
    private MahjonService mahjonService;

    private AtomicLong roomid = new AtomicLong(1);

    @RequestMapping(value = "/createRoom")
    @ResponseBody
    public Result start(HttpServletRequest request) {
        String userID = request.getParameter("userID").trim();
        RoomDto room = mahjonService.createRoom(Long.valueOf(userID), roomid.getAndIncrement());
        if (room != null) {
            return this.result(0, room, "创建成功");
        }
        return this.result(-1, null, "创建失败");
    }

    @RequestMapping(value = "/listRoom")
    @ResponseBody
    public Result listRoom(HttpServletRequest request) {
        List<RoomDto> list = mahjonService.listRoom(0);
        if (list != null) {
            return this.result(0, list, "创建成功");
        }
        return this.result(-1, null, "创建失败");
    }

    @RequestMapping(value = "/joinRoom")
    @ResponseBody
    public Result joinRoom(HttpServletRequest request) {
        String userID = request.getParameter("userID").trim();
        String roomid = request.getParameter("roomID").trim();
        Map<String, Object> map = mahjonService.joinRoom(Long.valueOf(userID), Long.valueOf(roomid));
        String status = String.valueOf(map.get("status"));
        if ("-1".equals((status))) {
            return this.result(-1, null, "房间不存在");
        }
        if ("-2".equals(status)) {
            return this.result(-1, null, "房间人数已满");
        }
        if ("-3".equals(status)) {
            RoomDto room = (RoomDto) map.get("data");
            return this.result(-1, map.get("data"), "您已经在" + room.getRoomId() + "号房间");
        }
        Object data = map.get("data");
        return this.result(0, data, "加入成功");
    }

    @RequestMapping(value = "/ready")
    @ResponseBody
    public Result ready(HttpServletRequest request) {
        String userID = request.getParameter("userID").trim();
        String roomid = request.getParameter("roomID").trim();
        String status = request.getParameter("status").trim();
        mahjonService.ready(Long.valueOf(userID), Long.valueOf(roomid), Integer.parseInt(status));
        return this.result(0, null, "ok");
    }

    @RequestMapping(value = "/fapai")
    @ResponseBody
    public Result fapai(HttpServletRequest request) {
        String userID = request.getParameter("userID").trim();
        String roomid = request.getParameter("roomID").trim();
        List<String> list = mahjonService.fapai(Long.valueOf(roomid), Long.valueOf(userID));
        return this.result(0, list, "ok");
    }

    @RequestMapping(value = "/mopai")
    @ResponseBody
    public Result mopai(HttpServletRequest request) {
        String userID = request.getParameter("userID").trim();
        String roomid = request.getParameter("roomID").trim();
        Map<String, Object> map = mahjonService.mopai(Long.valueOf(roomid), Long.valueOf(userID));
        return this.result(0, map, "ok");
    }

}
