package cs.domain.book;

import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zsl on 2017-09-04.
 */
@Entity
@Table(name = "cs_books_buy")
public class BookBuy extends DomainBase {
    @Id
    private String id;//专家
    @Column(columnDefinition = "varchar(64) ")
    private String booksBarCode;//图书条码号
    @Column(columnDefinition = "varchar(64) ")
    private String booksCode;//图书编号（图书分类号+顺序号）
    @Column(columnDefinition = "varchar(255) ")
    private String booksName;//图书名称
    @Column(columnDefinition="NUMBER")
    private String booksPrice;//图书价格
    @Column(columnDefinition="varchar(6)")
    private String booksType;//图书分类
    @Column(columnDefinition="varchar(6)")
    private String professionalType;//专业类别
    @Column(columnDefinition="varchar(30)")
    private String storePosition;//存放位置
    @Column(columnDefinition="varchar(30)")
    private String buyer;//购买人员
    @Column(columnDefinition="varchar(64)")
    private String publishingCompany;//出版社
    @Column(columnDefinition="varchar(128)")
    private String bookNo;//书号/刊号
    @Column(columnDefinition="varchar(255)")
    private String author;//作者
    /**
     * 出版时间
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date publishingTime;
    @Column(columnDefinition="NUMBER")
    private String bookNumber;//图书价格

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooksBarCode() {
        return booksBarCode;
    }

    public void setBooksBarCode(String booksBarCode) {
        this.booksBarCode = booksBarCode;
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

    public String getBooksPrice() {
        return booksPrice;
    }

    public void setBooksPrice(String booksPrice) {
        this.booksPrice = booksPrice;
    }

    public String getBooksType() {
        return booksType;
    }

    public void setBooksType(String booksType) {
        this.booksType = booksType;
    }

    public String getProfessionalType() {
        return professionalType;
    }

    public void setProfessionalType(String professionalType) {
        this.professionalType = professionalType;
    }

    public String getStorePosition() {
        return storePosition;
    }

    public void setStorePosition(String storePosition) {
        this.storePosition = storePosition;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public Date getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(Date publishingTime) {
        this.publishingTime = publishingTime;
    }
}
