package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.Idea;
import cs.domain.project.Idea_;
import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.IdeaRepo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdeaServiceImpl implements IdeaService {

    @Autowired
    private IdeaRepo ideaRepo;
    private ICache cache = CacheManager.getCache();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createIdea(IdeaDto ideaDto) {
        Idea idea = new Idea();
        boolean isCreate = true;
        if (Validate.isString(ideaDto.getIdeaID())) {
            idea = ideaRepo.findById(Idea_.ideaID.getName(), ideaDto.getIdeaID());
            if (Validate.isObject(idea) && Validate.isString(idea.getIdeaID())) {
                isCreate = false;
            }
        }
        BeanCopierUtils.copyProperties(ideaDto, idea);
        if (isCreate) {
            idea.setCreatedDate(new Date());
            idea.setCreatedBy(SessionUtil.getUserId());
        }
        idea.setModifiedDate(new Date());
        idea.setModifiedBy(SessionUtil.getUserId());
        ideaRepo.save(idea);
        cache.clear(SessionUtil.getUserId() + "_IDEA");
    }

    @Override
    public List<IdeaDto> get(ODataObj odataObj) {
        List<Idea> ideaList = ideaRepo.findByOdata(odataObj);
        List<IdeaDto> ideaDtoList = new ArrayList<IdeaDto>();
        for (Idea idea : ideaList) {
            if (idea.getCreatedBy().equals(SessionUtil.getLoginName())) {
                IdeaDto ideaDto = new IdeaDto();
                BeanCopierUtils.copyProperties(idea, ideaDto);
                ideaDtoList.add(ideaDto);
            }
        }
        return ideaDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIdea(String ideaId) {
        ideaRepo.deleteById(Idea_.ideaID.getName(), ideaId);
        //清除缓存
        cache.clear(SessionUtil.getUserId() + "_IDEA");
    }

    /**
     * 查询个人的常用意见(先从缓存取，取不到再读数据库)
     *
     * @return
     */
    @Override
    public List<IdeaDto> findMyIdea() {
        List<IdeaDto> resultDto = (List<IdeaDto>) cache.get(SessionUtil.getUserId() + "_IDEA");
        if (!Validate.isList(resultDto)) {
            Criteria criteria = ideaRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(Idea_.createdBy.getName(), SessionUtil.getUserId()));
            List<Idea> ideaList = criteria.list();
            if (Validate.isList(ideaList)) {
                resultDto = new ArrayList<>(ideaList.size());
                for (Idea il : ideaList) {
                    IdeaDto ideaDto = new IdeaDto();
                    BeanCopierUtils.copyProperties(il, ideaDto);
                    resultDto.add(ideaDto);
                }
                cache.put(SessionUtil.getUserId() + "_IDEA", resultDto);
            }
        }
        return resultDto == null ? new ArrayList<>() : resultDto;
    }

    @Override
    public ResultMsg bathSave(IdeaDto[] ideaDtos) {
        List<Idea> saveList = new ArrayList<>(ideaDtos.length);
        for (IdeaDto ideaDto : ideaDtos) {
            Idea idea = null;
            if (Validate.isString(ideaDto.getIdeaID())) {
                idea = ideaRepo.findById(Idea_.ideaID.getName(), ideaDto.getIdeaID());
                BeanCopierUtils.copyPropertiesIgnoreNull(ideaDto, idea);
                idea.setModifiedDate(new Date());
                idea.setModifiedBy(SessionUtil.getUserId());
                saveList.add(idea);
            } else {
                //内容为空的不添加
                if (Validate.isString(ideaDto.getIdeaContent())) {
                    idea = new Idea();
                    BeanCopierUtils.copyProperties(ideaDto, idea);
                    idea.setCreatedDate(new Date());
                    idea.setCreatedBy(SessionUtil.getUserId());
                    idea.setModifiedDate(new Date());
                    idea.setModifiedBy(SessionUtil.getUserId());
                    saveList.add(idea);
                }
            }
        }
        if (Validate.isList(saveList)) {
            ideaRepo.bathUpdate(saveList);
            List<IdeaDto> resultList = new ArrayList<>(saveList.size());
            for (Idea idea : saveList) {
                IdeaDto ideaDto = new IdeaDto();
                BeanCopierUtils.copyProperties(idea, ideaDto);
                resultList.add(ideaDto);
            }
            cache.clear(SessionUtil.getUserId() + "_IDEA");
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", resultList);
        } else {
            return ResultMsg.error("添加失败，请先编辑评审意见信息再保存！");
        }
    }

}
