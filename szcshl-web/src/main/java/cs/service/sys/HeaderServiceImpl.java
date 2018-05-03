package cs.service.sys;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Header;
import cs.domain.sys.Header_;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.HeaderRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Author: mcl
 * Date: 2017/9/12 14:28
 */
@Service
public class HeaderServiceImpl implements  HeaderService {

    @Autowired
    private HeaderRepo headerRepo;

    @Override
    @Transactional
    public ResultMsg createHeader(HeaderDto headerDto) {
        boolean isHeaderExist = headerRepo.isHeaderExist(headerDto.getHeaderType() , headerDto.getHeaderKey());
        if(!isHeaderExist) {
            Header header = new Header();
            BeanCopierUtils.copyProperties(headerDto, header);
            header.setId(UUID.randomUUID().toString());
            header.setCreatedBy(SessionUtil.getDisplayName());
            header.setCreatedDate(new Date());
            header.setModifiedBy(SessionUtil.getDisplayName());
            header.setModifiedDate(new Date());
            header.setHeaderstate(Constant.EnumState.NO.getValue());//默认为未选中状态
            headerRepo.save(header);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "创建成功", header);
        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , String.format("表头key值：%s,已经存在，请重新输入！",headerDto.getHeaderKey()));
        }
    }

    @Override
    public List<HeaderDto> findHeaderList(String headerType,String headState) {
        if(!Validate.isString(headerType)){
            headerType="项目类型";
        }
        if(!Validate.isString(headState)){
            headState= Constant.EnumState.NO.getValue();
        }
        Criteria criteria = headerRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Header_.headerType.getName(),headerType));
        criteria.add(Restrictions.eq(Header_.headerstate.getName(), headState));

        List<Header> headerList = criteria.list();
        List<HeaderDto> headerDtoList = new ArrayList<>();
        if(Validate.isList(headerList)){
            for(Header header : headerList){
                HeaderDto headerDto = new HeaderDto();
                BeanCopierUtils.copyProperties(header,headerDto);
                headerDtoList.add(headerDto);
            }
        }
        return headerDtoList;
    }

    @Override
    @Transactional
    public void updateSelectedHeader(String idStr) {
        String[] ids = idStr.split(",");
        for(int i=0 ; i<ids.length ; i++){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("update " + Header.class.getSimpleName() + " set "+ Header_.headerstate.getName()+"=:headerState where " +Header_.id.getName()+"=:id" );
            hqlBuilder.setParam("headerState" , Constant.EnumState.YES.getValue());
            hqlBuilder.setParam("id" , ids[i]);
            headerRepo.executeHql(hqlBuilder);
        }

    }

    @Override
    @Transactional
    public void updateCancelHeader(String idStr) {
        String[] ids = idStr.split(",");
        for(int i=0 ; i<ids.length ; i++){
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append("update " + Header.class.getSimpleName() + " set "+ Header_.headerstate.getName()+"=:headerState where " +Header_.id.getName()+"=:id" );
            hqlBuilder.setParam("headerState" , Constant.EnumState.NO.getValue());
            hqlBuilder.setParam("id" , ids[i]);
            headerRepo.executeHql(hqlBuilder);
        }
    }

   /* @Override
    public List<HeaderDto> findHeaderListSelected(String headerType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select h from " + Header.class.getSimpleName() + " h where "+ Header_.headerstate.getName()+ "=:headerState and ");
        hqlBuilder.append(" " + Header_.headerType.getName() + "=:headerType");
        hqlBuilder.setParam("headerState" , Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("headerType" , headerType);
        List<Header> headerList = headerRepo.findByHql(hqlBuilder);

        List<HeaderDto> headerDtoList = new ArrayList<>();
        for(Header header : headerList){
            HeaderDto headerDto = new HeaderDto();
            BeanCopierUtils.copyProperties(header , headerDto);
            headerDtoList.add(headerDto);
        }

        return headerDtoList;
    }*/

    @Override
    public PageModelDto<HeaderDto> get(ODataObj oDataObj) {
        PageModelDto<HeaderDto> pageModelDto = new PageModelDto<>();
        List<Header> headerList = headerRepo.findByOdata(oDataObj);
        List<HeaderDto> headerDtoList = new ArrayList<>();
        for(Header header : headerList){
            HeaderDto headerDto = new HeaderDto();
            BeanCopierUtils.copyProperties(header , headerDto);
            headerDtoList.add(headerDto);
        }

        pageModelDto.setValue(headerDtoList);
        pageModelDto.setCount(oDataObj.getCount());
        return pageModelDto;
    }

    @Override
    @Transactional
    public void deleteHeader(String id) {
        String[] ids = id.split(",");
        for(String headerId : ids){
            headerRepo.deleteById(Header_.id.getName()  , headerId);
        }

    }

    @Override
    public HeaderDto getHeaderById(String id) {
        Header header = headerRepo.findById(id);
        HeaderDto headerDto = new HeaderDto();
        BeanCopierUtils.copyProperties(header , headerDto);
        return headerDto;
    }

    @Override
    @Transactional
    public ResultMsg updateHeader(HeaderDto headerDto) {
        boolean isHeaderExist = headerRepo.isHeaderExist(headerDto.getHeaderType() , headerDto.getHeaderKey());
        if(!isHeaderExist) {
            Header header = headerRepo.findById(headerDto.getId());
            BeanCopierUtils.copyProperties(headerDto , header);
            header.setModifiedDate(new Date());
            header.setModifiedBy(SessionUtil.getDisplayName());
            headerRepo.save(header);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "创建成功", header);
    }else{
        return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , String.format("表头key值：%s,已经存在，请重新输入！",headerDto.getHeaderKey()));
    }
    }

    @Override
    public List<Header> findHeaderByType(String type) {
        Criteria criteria = headerRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Header_.headerType.getName(),type));
        return criteria.list();
    }
}