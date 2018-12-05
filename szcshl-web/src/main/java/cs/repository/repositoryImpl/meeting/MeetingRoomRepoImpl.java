package cs.repository.repositoryImpl.meeting;

import cs.common.cache.CacheConstant;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.utils.Validate;
import cs.domain.meeting.MeetingRoom;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingRoomRepoImpl extends AbstractRepository<MeetingRoom, String> implements MeetingRoomRepo {

    /**
     * 缓存
     */
    @Override
    public void fleshMeetingCache() {
        ICache cache = CacheManager.getCache();
        List<MeetingRoom> allList = findAll();
        cache.put(CacheConstant.MEETING_CACHE, allList);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public MeetingRoom findMeetingByCacheId(String id) {
        MeetingRoom meetingRoom = null;
        List<MeetingRoom> roomList = null;
        ICache cache = CacheManager.getCache();
        Object roomObj = cache.get(CacheConstant.MEETING_CACHE);
        if (Validate.isObject(roomObj)) {
            roomList = (List<MeetingRoom>) roomObj;
            for (MeetingRoom room : roomList) {
                if (room.getId().equals(id)) {
                    meetingRoom = room;
                    break;
                }
            }
        }

        if (null == meetingRoom) {
            meetingRoom = findById(User_.id.getName(), id);
            if (null != meetingRoom) {
                roomList = (null == roomList) ? new ArrayList<>() : roomList;
                roomList.add(meetingRoom);
                cache.put(CacheConstant.MEETING_CACHE, roomList);
            }
        }
        return meetingRoom;
    }
}
