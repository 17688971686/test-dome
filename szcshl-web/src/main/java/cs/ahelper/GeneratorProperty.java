package cs.ahelper;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import cs.domain.Role_;
import cs.model.RoleDto;

public class GeneratorProperty {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("roleName:"+Role_.roleName.getName());
	
		
		Class<?> dto=RoleDto.class;
		
		Method[] methods=dto.getMethods();
		Set<String> methods2=new HashSet<>();
		
		for (Method m : methods) {			
			if(m.getName().contains("get")||m.getName().contains("set")){
				if(m.getName()!="getClass"){
					methods2.add(m.getName().replace("get", "").replace("set", ""));
				}					
			}
					
		}
		for (String string : methods2) {
			System.out.println(String.format("roleDto.set%s(role.get%s());", string,string));
		}
	}

}
