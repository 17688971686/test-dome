package cs.domain.book;

import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.Date;

/**
 * 还书信息
 * Created by zsl on 2017-09-04.
 */
@Entity
@Table(name = "cs_return_info")
public class ReturnBookInfo extends DomainBase {
    @Id
    private String id;//
    //图书编号（图书分类号+顺序号）
    @Column(columnDefinition = "varchar(64) ")
    private String booksCode;
    //书号/刊号
    @Column(columnDefinition="varchar(128)")
    private String bookNo;
    //图书名称
    @Column(columnDefinition = "varchar(255) ")
    private String booksName;
    //还书人
    @Column(columnDefinition="varchar(100)")
    private String returnBorrower;
    //还书数量
    @Column(columnDefinition = "INTEGER")
    private Integer returnNum;
    /**
     * 应还日期
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date returnDate;

    /**
     * 实际还书日期
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date realReturnDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooksCode() {
        return booksCode;
    }

    public void setBooksCode(String booksCode) {
        this.booksCode = booksCode;
    }

    public String getBooksName() {
        return booksName;
    }

    public void setBooksName(String booksName) {
        this.booksName = booksName;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getRealReturnDate() {
        return realReturnDate;
    }

    public void setRealReturnDate(Date realReturnDate) {
        this.realReturnDate = realReturnDate;
    }

    public String getReturnBorrower() {
        return returnBorrower;
    }

    public void setReturnBorrower(String returnBorrower) {
        this.returnBorrower = returnBorrower;
    }

    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }
}
