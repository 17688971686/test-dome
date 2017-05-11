package cs.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.type.Type;

/**
 * HQL查询工具类
 * @author ldm
 *
 */
public class HqlBuilder {
	
	private StringBuilder hqlBuilder;
	
	private List<String> params;
	private List<Object> values;
	private List<Type> types;
		

	protected HqlBuilder() {
		hqlBuilder = new StringBuilder();
	}

	protected HqlBuilder(String hql) {
		hqlBuilder = new StringBuilder(hql);
	}
	
	public String getHqlString(){
		return hqlBuilder.toString();
	}

	public static HqlBuilder create() {
		return new HqlBuilder();
	}

	public static HqlBuilder create(String hql) {
		return new HqlBuilder(hql);
	}

	public HqlBuilder append(String hql) {
		hqlBuilder.append(hql);
		return this;
	}
	
	/**
	 * 设置参数
	 * 
	 * @param param
	 * @param value
	 * @return
	 * @see Query#setParameter(String, Object)
	 */
	public HqlBuilder setParam(String param, Object value) {
		return setParam(param, value, null);
	}

	/**
	 * 设置参数。与hibernate的Query接口一致。
	 * 
	 * @param param
	 * @param value
	 * @param type
	 * @return
	 * @see Query#setParameter(String, Object, Type)
	 */
	public HqlBuilder setParam(String param, Object value, Type type) {
		getParams().add(param);
		getValues().add(value);
		getTypes().add(type);
		return this;
	}
	
		
	
	public List<String> getParams() {
		if (params == null) {
			params = new ArrayList<String>();
		}
		return params;
	}

	public List<Object> getValues() {
		if (values == null) {
			values = new ArrayList<Object>();
		}
		return values;
	}

	public List<Type> getTypes() {
		if (types == null) {
			types = new ArrayList<Type>();
		}
		return types;
	}
}
