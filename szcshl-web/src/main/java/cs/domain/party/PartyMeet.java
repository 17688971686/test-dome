package cs.domain.party;

import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description: 党务会议
 * Author: mcl
 * Date: 2018/6/12 11:04
 */
@Entity
@Table(name="cs_party_meet")
public class PartyMeet extends DomainBase{

    /**
     * 党务会议ID
     */
    @Id
    @Column(columnDefinition = "VARCHAR(255)")
    private String mId;

    /**
     * 会议标题
     */
    @Column(columnDefinition = "VARCHAR(500)")
    private String mTitle;

    /**
     * 会议地点
     */
    @Column(columnDefinition = "VARCHAR(500)")
    private String mAddress;

    /**
     * 会议时间
     */
    @Column(columnDefinition = "date")
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