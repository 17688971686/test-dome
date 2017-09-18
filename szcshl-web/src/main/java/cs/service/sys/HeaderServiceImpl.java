package cs.service.sys;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.domain.sys.Header;
import cs.domain.sys.Header_;
import cs.model.PageModelDto;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.HeaderRepo;
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
    public void createHeader(HeaderDto headerDto) {
        Header header = new Header();
        BeanCopierUtils.copyProperties(headerDto , header);
        header.setId(UUID.randomUUID().toString());
        header.setCreatedBy(SessionUtil.getDisplayName());
        header.setCreatedDate(new Date());
        header.setModifiedBy(SessionUtil.getDisplayName());
        header.setModifiedDate(new Date());
        header.setHeaderstate(Constant.EnumState.NO.getValue());//默认为未选中状态
        headerRepo.save(header);
//        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"创建成功",header);
    }

    @Override
    public List<HeaderDto> getHeaderList(String headerType) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select h from " + Header.class.getSimpleName() + " h where " + Header_.headerType.getName() + "=:headerType ");
        hqlBuilder.append(" and " + Header_.headerstate.getName() + " =:headerState");
        if(headerType ==null || "".equals(headerType)){
            headerType="项目类型";
        }
        hqlBuilder.setParam("headerType" , headerType);
        hqlBuilder.setParam("headerState" , Constant.EnumState.NO.getValue());
        List<Header> headerList = headerRepo.findByHql(hqlBuilder);
        List<HeaderDto> headerDtoList = new ArrayList<>();
        if(headerList !=null && headerList.size()>0){
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

    @Override
    public List<HeaderDto> findHeaderListByState() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select h from " + Header.class.getSimpleName() + " h where "+ Header_.headerstate.getName()+ "=:headerState");
        hqlBuilder.setParam("headerState" , Constant.EnumState.YES.getValue());
        List<Header> headerList = headerRepo.findByHql(hqlBuilder);

        List<HeaderDto> headerDtoList = new ArrayList<>();
        for(Header header : headerList){
            HeaderDto headerDto = new HeaderDto();
            BeanCopierUtils.copyProperties(header , headerDto);
            headerDtoList.add(headerDto);
        }

        return headerDtoList;
    }

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
}