package cs.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HomeServiceImpl implements HomeService {
	@Autowired
	 private SessionFactory sessionFactory;
	@Override
	@Transactional
	public void test() {
//		Resource resource=new Resource();
//	
//		resource.setId( UUID.randomUUID());
//		resource.setDescription("ss");
//		resource.setName("dd");
//		Session currentSession=sessionFactory.getCurrentSession();
//	
//		currentSession.save(resource);
	}
}
