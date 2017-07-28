package cs.repository.odata;

import cs.common.utils.Validate;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Description: odata查询条件组装策略
 * Author: tzg
 * Date: 2017/7/7 18:46
 */
public enum ODataObjFilterStrategy {
    LIKE {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return Restrictions.like(fieldName, String.valueOf(value), MatchMode.ANYWHERE);
        }
    },
    EQ {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.eq(field, val));
        }
    },
    NE {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.ne(field, val));
        }
    },
    GT {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.gt(field, val));
        }
    },
    GE {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.ge(field, val));
        }
    },
    LT {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.lt(field, val));
        }
    },
    LE {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return getCriterions(fieldName, value, (String field, Object val) -> Restrictions.le(field, val));
        }
    },
    NI {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return Restrictions.not(Restrictions.in(fieldName, splitObj(value, ",")));
        }
    },
    IN {
        @Override
        public Criterion getCriterion(String fieldName, Object value) {
            return Restrictions.in(fieldName, splitObj(value, ","));
        }
    };

    private final static Logger logger = Logger.getLogger(ODataObjFilterStrategy.class);

    /**
     * 添加过滤添加
     *
     * @param fieldName
     * @param value
     * @return
     */
    public abstract Criterion getCriterion(String fieldName, Object value);

    /**
     * 获取过滤策略
     *
     * @param strategyName
     * @return
     */
    public static ODataObjFilterStrategy getStrategy(String strategyName) {
        if (Validate.isString(strategyName)) {
            return ODataObjFilterStrategy.valueOf(strategyName.toUpperCase());
        }
        logger.info(String.format("不支持【%s】查询策略", strategyName));
        return null;
    }


    /**
     * Description: odata查询的现在条件接口
     * Author: tzg
     * Date: 2017/7/7 18:50
     */
    interface ODataObjRestrictions {

        /**
         * 设置条件
         *
         * @param field
         * @param value
         * @return
         */
        Criterion toRestrictions(String field, Object value);

    }

    public static Criterion getCriterions(String field, Object value, ODataObjRestrictions restrictions) {
        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            int i = 0, len = values.length;
            Criterion[] criterias = new Criterion[len];
            for (; i < len; i++) {
                criterias[i] = includeNull(field, values[i], restrictions);
            }
            return Restrictions.or(criterias);
        } else {
            return includeNull(field, value, restrictions);
        }
    }

    private static Criterion includeNull(String field, Object value, ODataObjRestrictions restrictions) {
        if ("isNull".equals(value.toString())) {
            return Restrictions.isNull(field);
        } else {
            return restrictions.toRestrictions(field, value);
        }
    }

    private static Object[] splitObj(Object value, String splitOperate) {
        if (value instanceof CharSequence) {
            String s = value.toString();
            String[] sArr = s.split(splitOperate);
            Object[] resultObj = new Object[sArr.length];
            for (int i = 0, l = sArr.length; i < l; i++) {
                resultObj[i] = sArr[i];
            }
            return resultObj;
        }
        return null;
    }

}



