package cs.model.party;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;

import java.util.Date;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 11:36
 */
public class PartyMeetDto extends DomainBase {
    /**
     * 党务会议ID
     */
    private String mId;

    /**
     * 会议标题
     */
    private String mTitle;

    /**
     * 会议地点
     */
    private String mAddress;

    /**
     * 会议时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date mDate;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }
}