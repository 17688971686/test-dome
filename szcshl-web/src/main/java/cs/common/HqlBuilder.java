package cs.common;

import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import org.hibernate.query.Query;
import org.hibernate.type.Type;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * id封装方法
	 * @param sqlKeyWord(where 、and、 or)等连接关键词
	 * @param propoty
	 * @param values
	 * @return
	 */
	public 	HqlBuilder bulidPropotyString(String sqlKeyWord,String propoty,String values){
		List<String> idList = StringUtil.getSplit(values, ",");
		if (idList.size() > 1) {
			hqlBuilder.append(sqlKeyWord +" "+ propoty + " in (");
		}else{
			if(Validate.isString(sqlKeyWord)){
				hqlBuilder.append(sqlKeyWord +" ");
			}
		}
		for (int i = 0; i< idList.size(); i++) {
			if (idList.size() > 1) {
				if (i == (idList.size() - 1)) {
					hqlBuilder.append(" :id" + i);
					setParam("id" + i, idList.get(i));
				}else if((i%999)==0 && i>0){
					hqlBuilder.append(" :id" + i);
					setParam("id" + i, idList.get(i));
					hqlBuilder.append(") or "+propoty+" in (");
					//hqlBuilder.append(idList.get(i)).append(") or "+propoty+" in (");
				}else{
					hqlBuilder.append(" :id" + i);
					setParam("id" + i, idList.get(i));
					hqlBuilder.append(",");
				}
			}else{
				hqlBuilder.append(propoty + " = :id ");
				setParam("id", idList.get(0));
			}

		}

		if (idList.size() > 1) {
			hqlBuilder.append(") ");
		}

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
