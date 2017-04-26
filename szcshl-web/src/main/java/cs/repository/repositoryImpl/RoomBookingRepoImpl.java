package cs.repository.repositoryImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cs.domain.RoomBooking;
import cs.repository.AbstractRepository;

@Repository
public class RoomBookingRepoImpl extends AbstractRepository<RoomBooking, String>  implements RoomBookingRepo{

	@Override
	public List<RoomBooking> findByHql(String hql) {		
		return findByHql(hql);
	}


}
