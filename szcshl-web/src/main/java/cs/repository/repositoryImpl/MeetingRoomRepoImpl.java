package cs.repository.repositoryImpl;

import org.springframework.stereotype.Repository;

import cs.domain.MeetingRoom;
import cs.repository.AbstractRepository;
@Repository
public class MeetingRoomRepoImpl extends AbstractRepository<MeetingRoom, String> implements MeetingRoomRepo{

}
