package cs.repository.repositoryImpl;

import org.springframework.stereotype.Repository;

import cs.domain.RoomBooking;
import cs.repository.AbstractRepository;

@Repository
public class RoomBookingRepoImpl extends AbstractRepository<RoomBooking, String>  implements RoomBookingRepo{

}
