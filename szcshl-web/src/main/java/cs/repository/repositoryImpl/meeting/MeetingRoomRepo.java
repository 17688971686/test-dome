package cs.repository.repositoryImpl.meeting;

import cs.domain.meeting.MeetingRoom;
import cs.repository.IRepository;

public interface MeetingRoomRepo extends IRepository<MeetingRoom, String> {

    /**
     * 缓存
     */
    void fleshMeetingCache();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    MeetingRoom findMeetingByCacheId(String id);
}
