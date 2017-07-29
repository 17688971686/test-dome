package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import cs.common.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.domain.project.Idea;
import cs.domain.project.Idea_;
import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.IdeaRepo;

@Service
public class IdeaServiceImpl implements IdeaService {

    @Autowired
    private IdeaRepo ideaRepo;

    @Override
    @Transactional
    public void createIdea(IdeaDto ideaDto) {
        Idea idea = new Idea();
        BeanCopierUtils.copyProperties(ideaDto, idea);
        if (ideaDto.getIdeaID() == null) {
            idea.setIdeaID(UUID.randomUUID().toString());
        }
        idea.setCreatedDate(new Date());
        idea.setCreatedBy(SessionUtil.getLoginName());
        idea.setModifiedDate(new Date());
        idea.setModifiedBy(SessionUtil.getLoginName());
        ideaRepo.save(idea);

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
    @Transactional
    public void deleteIdea(String ideaId) {
        List<String> ideaList = StringUtil.getSplit(ideaId, ",");
        if (ideaList != null && ideaList.size() > 0) {
            for (int i = 0; i < ideaList.size(); i++) {
                ideaRepo.deleteById(Idea_.ideaID.getName(), ideaList.get(i));
            }

        }
    }

}
