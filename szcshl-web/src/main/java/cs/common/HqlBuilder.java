package cs.common;

import java.util.ArrayList;
import java.util.List;

import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.WorkProgram_;
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

    /**
     * id封装方法
     * @param sqlKeyWord(where 、and、 or)等连接关键词
     * @param idPropoty
     * @param id
     * @return
     */
	public 	HqlBuilder bulidIdString(String sqlKeyWord,String idPropoty,String id){
        List<String> idList = StringUtil.getSplit(id, ",");
        if(Validate.isString(sqlKeyWord)){
            hqlBuilder.append(sqlKeyWord +" ");
        }
        if (idList.size() > 1) {
            hqlBuilder.append(idPropoty + " in ( ");
            for (int i = 0,l=idList.size(); i < l; i++) {
                if (i > 0) {
                    hqlBuilder.append(",");
                }
                hqlBuilder.append(" :id" + i);
                setParam("id" + i, idList.get(i));
            }
            hqlBuilder.append(" )");
        } else {
            hqlBuilder.append(idPropoty + " = :id ");
            setParam("id", idList.get(0));
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
