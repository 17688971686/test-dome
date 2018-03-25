package cs.model.project;

import cs.model.BaseDto;

public class SignPreDto extends BaseDto {

    private String state;

    private SignDto data;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public SignDto getData() {
        return data;
    }

    public void setData(SignDto data) {
        this.data = data;
    }
}