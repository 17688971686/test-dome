package cs.domain.book;

import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.Date;

/**借书历史
 * Created by zsl on 2017-09-04.
 */
@Entity
@Table(name = "cs_borrow_his")
public class BorrowBookHis extends DomainBase {
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
    //借书人
    @Column(columnDefinition="varchar(100)")
    private String bookBorrower;
    //借书数量
    @Column(columnDefinition = "INTEGER")
    private Integer borrowNum;
    /**
     * 借书日期
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date borrowDate;
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

    public String getBookBorrower() {
        return bookBorrower;
    }

    public void setBookBorrower(String bookBorrower) {
        this.bookBorrower = bookBorrower;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
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

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }
}
