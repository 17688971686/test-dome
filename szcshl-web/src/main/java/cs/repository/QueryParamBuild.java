package cs.repository;

import cs.common.HqlBuilder;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import org.hibernate.query.Query;
import org.hibernate.type.Type;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

import java.util.List;

/**
 * Created by Administrator on 2018/12/7 0007.
 */
public class QueryParamBuild<T> {
    private Query<T> query;

    public Query<T> getQuery() {
        return query;
    }

    public void setQuery(Query<T> query) {
        this.query = query;
    }

    public QueryParamBuild(Query<T> query) {
        this.query = query;
    }


    public QueryParamBuild buildParams(HqlBuilder hqlBuilder){
        if (!Validate.isObject(query)) {
            return null;
        }
        List<String> params = hqlBuilder.getParams();
        List<Object> values = hqlBuilder.getValues();
        List<Type> types = hqlBuilder.getTypes();
        Codec oracleCodec = new OracleCodec();
        if (Validate.isList(params)) {
            for (int i = 0, l = params.size(); i < l; i++) {
                String paramName = StringUtil.sqlInjectionFilter(params.get(i));
                Object value = values.get(i);
                if(!Validate.isString(paramName) || !Validate.isObject(value)){
                    continue;
                }
                if (value instanceof String) {
                    value = ESAPI.encoder().encodeForSQL(oracleCodec,value.toString());
                    if (Validate.isString(value)) {
                        value = StringUtil.sqlInjectionFilter(value.toString());
                        if(!Validate.isString(value)){
                            continue;
                        }
                    }
                }
                if (types.get(i) == null) {
                    query.setParameter(paramName, value);
                } else {
                    query.setParameter(paramName, value, types.get(i));
                }
            }
        }
        return this;
    }
}
