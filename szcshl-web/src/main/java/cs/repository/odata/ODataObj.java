package cs.repository.odata;

import cs.common.utils.DateUtils;
import cs.common.utils.ObjectUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    public void setTop(int top) {
        this.top = top;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean count) {
        isCount = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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
    public void setFilter(List<ODataFilterItem> filter) {
        this.filter = filter;
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
            odataOtherPattern = Pattern.compile("([\\w|\\.|/]+\\s+(eq|ne|gt|ge|lt|le|ni|in)\\s+(((datetime|date|bigDecimal|integer|double|float)?\\'[^\\']*\\'|\\d+)|(\\([^\\)]*\\))))"),
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
                    //过滤String类型参数中可能存在的XSS注入
                    String strValue = matcherValue.group(1).trim();
                    strValue = StringUtil.sqlInjectionFilter(strValue);
                    oDataFilterItem.setField(matcherField.group(1).trim());
                    oDataFilterItem.setValue(strValue);
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
                    if (StringUtil.startsWithIgnoreCase(value, "bigDecimal'")) {// 如果是bigDecimal
                        oDataFilterItem = new ODataFilterItem<BigDecimal>();
                        String rgex = "bigDecimal'(.*?)'";
                        oDataFilterItem.setValue(new BigDecimal(StringUtil.getSubUtilSimple(value, rgex)));
                    }else if(StringUtil.startsWithIgnoreCase(value, "float'")){// 如果是float
                        oDataFilterItem = new ODataFilterItem<Float>();
                        String rgex = "float'(.*?)'";
                        oDataFilterItem.setValue(new Float(StringUtil.getSubUtilSimple(value, rgex)));
                    }else if (StringUtil.startsWithIgnoreCase(value, "integer'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Integer>();
                        String rgex = "integer'(.*?)'";
                        oDataFilterItem.setValue(Integer.parseInt(StringUtil.getSubUtilSimple(value, rgex)));
                    }
                    else if (StringUtil.startsWithIgnoreCase(value, "double'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Integer>();
                        String rgex = "double'(.*?)'";
                        oDataFilterItem.setValue(Double.parseDouble(StringUtil.getSubUtilSimple(value, rgex)));
                    }
                    else if (StringUtil.startsWithIgnoreCase(value, "datetime'") && value.endsWith("'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Date>();
                        String rgex = "datetime'(.*?)'";
                        String dateString = StringUtil.getSubUtilSimple(value, rgex);
                        oDataFilterItem.setValue(DateUtils.converToDate(dateString.replace("T", " "), DateUtils.DATE_TIME_PATTERN));
                    } else if (StringUtil.startsWithIgnoreCase(value, "date'") && value.endsWith("'")) {// 如果是datetime
                        oDataFilterItem = new ODataFilterItem<Date>();
                        String operate = filterItems[1];
                        String rgex = "date'(.*?)'";
                        String dateString = StringUtil.getSubUtilSimple(value, rgex);
                        if("le".equals(operate.toLowerCase())){
                            oDataFilterItem.setValue(DateUtils.converToDate((dateString+" 23:59:59"), DateUtils.DATE_TIME_PATTERN));
                        }else{
                            oDataFilterItem.setValue(DateUtils.converToDate(dateString, DateUtils.DATE_PATTERN));
                        }

                    } else if (StringUtil.startsWithIgnoreCase(value, "guid'") && value.endsWith("'")) {// 如果是guid
                        oDataFilterItem = new ODataFilterItem<UUID>();

                        oDataFilterItem.setValue(UUID.fromString(value.substring("guid'".length(), value.length() - 1)));
                    } else if (value.startsWith("'") && value.endsWith("'")) {// 如果是string
                        oDataFilterItem = new ODataFilterItem<String>();
                        oDataFilterItem.setValue(value.substring(1, value.length() - 1));
                    } else if (value.startsWith("(") && value.endsWith(")")) {// 如果是string[]
                        oDataFilterItem = new ODataFilterItem<String[]>();
                        oDataFilterItem.setValue(value.replace("'","").substring(1, value.length() - 1).split(","));
                    // 其它为Number
                    } else {
                        oDataFilterItem = new ODataFilterItem<Object>();
                        oDataFilterItem.setValue(value);
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

    /**
     * 把 ODataFilterItem 封装成 criteria
     * @param criteria
     * @return
     */
    public Criteria buildFilterToCriteria(Criteria criteria){
        if (Validate.isList(filter)) {
            Object value;
            for (ODataFilterItem item : filter) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(),value));
            }
        }
        return criteria;
    }

    @SuppressWarnings("rawtypes")
    public Criteria buildQuery(Criteria criteria) {
        logger.debug("begin:buildQuery");
        this.buildFilterToCriteria(criteria);
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

}
