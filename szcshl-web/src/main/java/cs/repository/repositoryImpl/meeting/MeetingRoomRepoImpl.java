package cs.repository.repositoryImpl.meeting;

import org.springframework.stereotype.Repository;

import cs.domain.meeting.MeetingRoom;
import cs.repository.AbstractRepository;
@Repository
public class MeetingRoomRepoImpl extends AbstractRepository<MeetingRoom, String> implements MeetingRoomRepo{

}
