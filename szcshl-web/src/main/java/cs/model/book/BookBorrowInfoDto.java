package cs.model.book;

import cs.domain.book.BookBuyBusiness;
import cs.model.BaseDto;

import java.util.Date;


/**
 * Description: 图书详细信息 页面数据模型
 * author: zsl
 * Date: 2017-9-8 14:16:17
 */
public class BookBorrowInfoDto extends BaseDto {

    private String id;
    private String booksCode;
    private String booksName;
    private String bookBorrower;
    private Integer borrowNum;
    private Date borrowDate;
    private Date returnDate;
    private Date realReturnDate;
    private Integer totalCount;//个人借书总数

    private BookBuyBusiness bookBuyBusiness;

    public BookBorrowInfoDto() {
    }

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

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
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

    public BookBuyBusiness getBookBuyBusiness() {
        return bookBuyBusiness;
    }

    public void setBookBuyBusiness(BookBuyBusiness bookBuyBusiness) {
        this.bookBuyBusiness = bookBuyBusiness;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}