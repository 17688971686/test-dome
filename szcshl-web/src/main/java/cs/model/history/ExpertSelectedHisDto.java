package cs.model.history;

import cs.domain.expert.ExpertSelectedBase;
import cs.model.expert.ExpertDto;

public class ExpertSelectedHisDto extends ExpertSelectedBase {

    private ExpertDto expertDto;

    public ExpertDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertDto expertDto) {
        this.expertDto = expertDto;
    }
}
