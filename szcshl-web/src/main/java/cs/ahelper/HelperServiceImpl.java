package cs.ahelper;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.domain.Role;
import cs.domain.User;
@Service
public class HelperServiceImpl implements HelperService {
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	@Transactional
	public void DataInit() {
		Session session =sessionFactory.getCurrentSession();
		for (int i = 0; i < 50; i++) {
			
			Role role =new Role();
			role.setId(UUID.randomUUID().toString());
			role.setRoleName("role"+i);
			//role.setModifiedDate(new Date());
			
			
			User user=new User();
			user.setId(UUID.randomUUID().toString());
			user.setLoginName("user"+i);
			user.setDisplayName("");
			user.setPassword("");
			session.saveOrUpdate(user);
					
			role.getUsers().add(user);
			session.saveOrUpdate(role);
			
			System.out.println(role.getRoleName());
		}
	}
	@Override
	@Transactional
	public void delete() {
		Session session =sessionFactory.getCurrentSession();
		Role role=session.get(Role.class, "8446088a-5026-45a3-83da-4a410f83e27a");
		session.delete(role);
	}
}
