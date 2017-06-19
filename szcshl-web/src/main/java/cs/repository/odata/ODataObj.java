package cs.repository.odata;

import cs.common.utils.ObjectUtils;
import cs.common.utils.StringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ODataObj {
    private String orderby;
    private boolean orderbyDesc;
    private String[] select;
    private int skip;
    private int top;
    private boolean isCount;
    private int count;


    @SuppressWarnings("rawtypes")
    private List<ODataFilterItem> filter;

    public String getOrderby() {
        return orderby;
    }

    public boolean isOrderbyDesc() {
        return orderbyDesc;
    }

    public String[] getSelect() {
        return select;
    }

    public int getSkip() {
        return skip;
    }

    public int getTop() {
        return top;
    }

    public boolean getIsCount() {
        return isCount;
    }

    public int getCount() {
        return count;
    }

    @SuppressWarnings("rawtypes")
    public List<ODataFilterItem> getFilter() {
        return filter;
    }

    private static Logger logger = Logger.getLogger(ODataObj.class);

    // method
    public ODataObj(HttpServletRequest request) throws ParseException {
        // get parameter
        String filter = request.getParameter("$filter");
        String orderby = request.getParameter("$orderby");
        String select = request.getParameter("$select");
        String skip = request.getParameter("$skip");
        String top = request.getParameter("$top");
        String inlinecount = request.getParameter("$inlinecount");

        BuildObj(filter, orderby, select, skip, top, inlinecount);
    }

    public final static Pattern odataLikePattern = Pattern.compile("(substringof\\(\\s*\\'?[^\\']*\\'\\s*\\,\\s*[\\w|\\.|/]+\\s*\\))"),
            odataOtherPattern = Pattern.compile("([\\w|\\.|/]+\\s+(eq|ne|gt|ge|lt|le|ni|in)\\s+(((datetime|date)?\\'[^\\']*\\'|\\d+)|(\\([^\\)]*\\))))"),
            patternField = Pattern.compile(",(.*?)\\)"),
            patternValue = Pattern.compile("'(.*?)'");

    @SuppressWarnings("rawtypes")
    public void BuildObj(String filter, String orderby, String select, String skip, String top, String inlinecount)
            throws ParseException {
        if (ObjectUtils.isNotEmpty(orderby)) {
            String[] orderbyItems = orderby.trim().split(" ");
            this.orderby = orderbyItems[0];
            if (orderbyItems.length == 2 && orderbyItems[1].toLowerCase().equals("desc")) {
                this.orderbyDesc = true;
            }
        }
        // build select
        if (ObjectUtils.isNotEmpty(select)) {
            this.select = select.split(",");
        }
        // build skip,top,inlinecount
        if (ObjectUtils.isNotEmpty(skip)) {
            this.skip = Integer.parseInt(skip);
        }
        if (ObjectUtils.isNotEmpty(top)) {
            this.top = Integer.parseInt(top);
        }
        if (null != inlinecount && "allpages".equalsIgnoreCase(inlinecount)) {
            this.isCount = true;
        }

        // build filter
        if (ObjectUtils.isNotEmpty(filter)) {
            List<ODataFilterItem> filterItemsList = new ArrayList<ODataFilterItem>();
            String[] filterItems;
            Matcher odataMatcher, matcherField, matcherValue;
            ODataFilterItem oDataFilterItem;
            String filterItem, value;
            // 解析like的查询方式
            odataMatcher = odataLikePattern.matcher(filter);
            while (odataMatcher.find()) {
                filterItem = odataMatcher.group();
                matcherField = patternField.matcher(filterItem);
                matcherValue = patternValue.matcher(filterItem);
                if (matcherField.find() && matcherValue.find()) {
                    if (ObjectUtils.isEmpty(matcherValue.group(1))) {   // 避免空字符串的查询
                        continue;
                    }
                    oDataFilterItem = new ODataFilterItem<String>();
                    oDataFilterItem.setField(matcherField.group(1).trim());
                    oDataFilterItem.setValue(matcherValue.group(1));
                    oDataFilterItem.setOperator("like");
                    filterItemsList.add(oDataFilterItem);
                }
            }

            // 解析非like的方式
            odataMatcher = odataOtherPattern.matcher(filter);
            while (odataMatcher.find()) {
                filterItems = getFilterItems(odataMatcher.group());
                if (ObjectUtils.isNoneEmpty(filterItems)) {
                    value = filterItems[2];
                    if (StringUtil.startsWithIgnoreCase(value, "datetime'") && value.endsWith("'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Date>();

                        oDataFilterItem.setValue(DateUtils.parseDate(value.substring("datetime'".length(), value.length() - 1).replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
                    } else if (StringUtil.startsWithIgnoreCase(value, "date'") && value.endsWith("'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Date>();

                        oDataFilterItem.setValue(DateUtils.parseDate(value.substring("date'".length(), value.length() - 1), "yyyy-MM-dd"));
                    } else if (StringUtil.startsWithIgnoreCase(value, "guid'") && value.endsWith("'")) {// 如果是guid
                        oDataFilterItem = new ODataFilterItem<UUID>();

                        oDataFilterItem.setValue(UUID.fromString(value.substring("guid'".length(), value.length() - 1)));
                    } else if (value.startsWith("'") && value.endsWith("'")) {// 如果是string
                        oDataFilterItem = new ODataFilterItem<String>();
                        oDataFilterItem.setValue(value.substring(1, value.length() - 1));
                    } else if (value.startsWith("(") && value.endsWith(")")) {// 如果是string[]
                        oDataFilterItem = new ODataFilterItem<String[]>();
                        oDataFilterItem.setValue(value.replace("'","").substring(1, value.length() - 1).split(","));
                    } else {// 其它为Number
                        oDataFilterItem = new ODataFilterItem<Number>();

                        oDataFilterItem.setValue(NumberFormat.getInstance().parse(value));
                    }

                    oDataFilterItem.setField(filterItems[0]);
                    oDataFilterItem.setOperator(filterItems[1]);
                    filterItemsList.add(oDataFilterItem);
                }
            }
            this.filter = filterItemsList;
        } // if
    }

    /**
     * 获取过滤参数
     *
     * @param filterItem
     * @return
     */
    private static String[] getFilterItems(String filterItem) {
        String[] filterItems = new String[3];
        int site1 = filterItem.indexOf(" "), site2 = site1 + 1;
        if (site1 > 1) {
            filterItems[0] = filterItem.substring(0, site1);
            int site3 = filterItem.indexOf(" ", site2);
            filterItems[1] = filterItem.substring(site2, site3).trim();
            filterItems[2] = filterItem.substring(site3 + 1).trim();
        }
        return filterItems;
    }

    @SuppressWarnings("rawtypes")
    public Criteria buildQuery(Criteria criteria) {
        logger.debug("begin:buildQuery");

        // 处理filter
        if (filter != null) {
            for (ODataFilterItem oDataFilterItem : filter) {
                String field = oDataFilterItem.getField();
                String operator = oDataFilterItem.getOperator();
                Object value = oDataFilterItem.getValue();
                logger.debug(String.format("fitler:field:%s,operator:%s,value:%s", field, operator, value));
                switch (operator) {
                    case "like":
                        criteria.add(Restrictions.like(field, "%" + value + "%"));
                        break;
                    case "eq":
                            criteria.add(getCriterions(field, value, new MyRestrictions() {
                                @Override
                                public Criterion toRestrictions(String field, Object val) {
                                    return Restrictions.eq(field, val);
                                }
                            }));
                        break;
                    case "ne":
                        criteria.add(getCriterions(field, value, new MyRestrictions() {
                            @Override
                            public Criterion toRestrictions(String field, Object val) {
                                return Restrictions.ne(field, val);
                            }
                        }));
                        break;
                    case "gt":
                        criteria.add(getCriterions(field, value, new MyRestrictions() {
                            @Override
                            public Criterion toRestrictions(String field, Object val) {
                                return Restrictions.gt(field, val);
                            }
                        }));
                        break;
                    case "ge":
                        criteria.add(getCriterions(field, value, new MyRestrictions() {
                            @Override
                            public Criterion toRestrictions(String field, Object val) {
                                return Restrictions.ge(field, val);
                            }
                        }));
                        break;
                    case "lt":
                        criteria.add(getCriterions(field, value, new MyRestrictions() {
                            @Override
                            public Criterion toRestrictions(String field, Object val) {
                                return Restrictions.lt(field, val);
                            }
                        }));
                        break;
                    case "le":
                        criteria.add(getCriterions(field, value, new MyRestrictions() {
                            @Override
                            public Criterion toRestrictions(String field, Object val) {
                                return Restrictions.le(field, val);
                            }
                        }));
                        break;
                    case "ni":    //not in
                        Object[] notInObjValues = splitObj(value, ",");
                        criteria.add(Restrictions.not(Restrictions.in(field, notInObjValues)));
                        break;
                    case "in":    //in
                        Object[] inObjValues = splitObj(value, ",");
                        criteria.add(Restrictions.in(field, inObjValues));
                        break;
                    default:
                        break;
                }
            }
        }
        //统计总数
        if (this.isCount) {
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            this.count = totalResult;
            criteria.setProjection(null);
            logger.debug("count:" + totalResult);
        }
        // 处理分页
        if (this.top != 0) {
            criteria.setFirstResult(this.skip);
            criteria.setMaxResults(this.top);
        }
        // 处理orderby
        if (this.orderby != null && !this.orderby.isEmpty()) {
            if (this.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(this.orderby).desc());
            } else {
                criteria.addOrder(Property.forName(this.orderby).asc());
            }
        }
        logger.debug("end:buildQuery");
        return criteria;
    }

    public interface MyRestrictions {

        Criterion toRestrictions(String field, Object value);

    }

    public Criterion getCriterions(String field, Object value, MyRestrictions myRestrictions) {
        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            int i = 0, len = values.length;
            Criterion[] criterias = new Criterion[len];
            for (; i < len; i++) {
                if("isNull".equals(values[i].toString())){
                    criterias[i] = Restrictions.isNull(field);
                }else{
                    criterias[i] = myRestrictions.toRestrictions(field, values[i]);
                }

            }
            return Restrictions.or(criterias);
        } else {
            if("isNull".equals(value.toString())){
               return Restrictions.isNull(field);
            }else {
                return myRestrictions.toRestrictions(field, value);
            }
        }
    }

    private Object[] splitObj(Object value, String splitOperate) {
        if (value instanceof String) {
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
