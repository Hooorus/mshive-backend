package com.yimingliao.mshivebackend.service.mongodb;

import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.mongodb.Room;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Calendo
 * @version 1.0
 * @description Room 业务层接口
 * @date 2023/10/16 21:15
 */
public interface IRoomService {

    //Insert One Room
    R insertOneRoom(Room room);

    //Update One Room
    R updateOneRoom(Room room);

    //Delete One User's Room, need roomUUId
    R deleteOneRoomByRoomUUId(String roomUUId);

    //Find One User's Some Rooms, need lastSeenRoomId & searchSize
    R searchRoomListByUserUUId(String userUUId, Long lastSeenRoomId, Integer searchSize);

    //Find One User's All Rooms
    R searchRoomAllByUserUUId(String userUUId);

    //Find One User's One Room, need userUUId & roomId
    R searchOneRoomByUserUUId(String userUUId, Long roomId);

    //Search RoomList By RoomUUId List
    R searchRoomListByRoomUUIdList(List<String> roomUUIds);

    //Download One User's Optional Room Report
    ResponseEntity downloadOneUserRoomReportForm(String userUUId, String startDate, String endDate, Boolean onlyBookmarks, Boolean needAll) throws IOException;

}
