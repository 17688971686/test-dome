package cs.repository.odata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

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

	@SuppressWarnings("rawtypes")
	public void BuildObj(String filter, String orderby, String select, String skip, String top, String inlinecount)
			throws ParseException {
		// build odata object
		// build orderby
		if (orderby != null && !orderby.isEmpty()) {
			String[] orderbyItems = orderby.trim().split(" ");
			this.orderby = orderbyItems[0];
			if (orderbyItems.length == 2 && orderbyItems[1].toLowerCase().equals("desc")) {
				this.orderbyDesc = true;
			}
		}
		// build select
		if (select != null && !select.isEmpty()) {
			this.select = select.split(",");
			
		}
		// build skip,top,inlinecount
		if (skip != null && !skip.isEmpty()) {
			this.skip = Integer.parseInt(skip);
		}
		if (top != null && !top.isEmpty()) {
			this.top = Integer.parseInt(top);
		}
		if (inlinecount != null && inlinecount.toLowerCase().equals("allpages")) {
			this.isCount = true;
		}

		// build filter
		if (filter != null && !filter.isEmpty()) {
			List<ODataFilterItem> filterItemsList = new ArrayList<ODataFilterItem>();
			String[] filters = filter.split("and");
			for (String filterItem : filters) {
				filterItem = filterItem.trim();
				// handle like
				if (filterItem.contains("substringof")) {
					ODataFilterItem<String> oDataFilterItem = new ODataFilterItem<String>();
					Pattern patternField = Pattern.compile(",(.*?)\\)");
					Pattern patternValue = Pattern.compile("'(.*?)'");
					Matcher matcherField = patternField.matcher(filterItem);
					Matcher matcherValue = patternValue.matcher(filterItem);
					if (matcherField.find() && matcherValue.find()) {
						oDataFilterItem.setField(matcherField.group(1));
						oDataFilterItem.setValue(matcherValue.group(1));
						oDataFilterItem.setOperator("like");
						filterItemsList.add(oDataFilterItem);
					}

				}
				// handle eq,ne,gt,ge,lt,le
				else {

					String[] filterItems = filterItem.split(" ");
					if (filterItems.length == 3) {
						if (filterItems[2].toLowerCase().contains("datetime")) {// 如果是datetime
							ODataFilterItem<Date> oDataFilterItem = new ODataFilterItem<Date>();

							oDataFilterItem.setField(filterItems[0]);
							oDataFilterItem.setOperator(filterItems[1]);
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String date = filterItems[2].toLowerCase().replaceAll("datetime", "").replaceAll("'", "");
							oDataFilterItem.setValue(dateFormat.parse(date));
							filterItemsList.add(oDataFilterItem);
						} 
						else if(filterItems[2].toLowerCase().contains("guid")){//如果是guid
							ODataFilterItem<UUID> oDataFilterItem = new ODataFilterItem<UUID>();

							oDataFilterItem.setField(filterItems[0]);
							oDataFilterItem.setOperator(filterItems[1]);
							
							UUID id = UUID.fromString( filterItems[2].toLowerCase().replaceAll("guid", "").replaceAll("'", ""));
							oDataFilterItem.setValue(id);
							filterItemsList.add(oDataFilterItem);
						}
						else if (filterItems[2].contains("'")) {// 如果是string
							ODataFilterItem<String> oDataFilterItem = new ODataFilterItem<String>();

							oDataFilterItem.setField(filterItems[0]);
							oDataFilterItem.setOperator(filterItems[1]);
							oDataFilterItem.setValue(filterItems[2].replaceAll("'", ""));
							filterItemsList.add(oDataFilterItem);
						} else {// 其它为int
							ODataFilterItem<Integer> oDataFilterItem = new ODataFilterItem<Integer>();

							oDataFilterItem.setField(filterItems[0]);
							oDataFilterItem.setOperator(filterItems[1]);
							oDataFilterItem.setValue(Integer.parseInt(filterItems[2]));
							filterItemsList.add(oDataFilterItem);
						}

					}

				}

			} // for
			this.filter = filterItemsList;
		} // if
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
				logger.debug(String.format("fitler:field:%s,operator:%s,value:%s", field,operator,value));
				switch (operator) {
				case "like":
					criteria.add(Restrictions.like(field, "%" + value + "%"));
					break;
				case "eq":
					criteria.add(Restrictions.eq(field, value));
					break;
				case "ne":
					criteria.add(Restrictions.ne(field, value));
					break;
				case "gt":
					criteria.add(Restrictions.gt(field, value));
					break;
				case "ge":
					criteria.add(Restrictions.ge(field, value));
					break;
				case "lt":
					criteria.add(Restrictions.lt(field, value));
					break;
				case "le":
					criteria.add(Restrictions.le(field, value));
					break;
				default:
					break;
				}
			}
		}
		//统计总数
		if(this.isCount){		
			
			Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			this.count=totalResult;
			criteria.setProjection(null);
			logger.debug("count:"+totalResult);
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
