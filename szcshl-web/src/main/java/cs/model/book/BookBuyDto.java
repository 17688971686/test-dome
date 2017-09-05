package cs.model.book;

import cs.model.BaseDto;


/**
 * Description: 图书管理模型
 * author: zsl
 * Date: 2017-9-5 16:16:21
 */
public class BookBuyDto extends BaseDto {

    private String id;
    private String booksBarCode;
    private String booksCode;
    private String booksName;
    private String booksPrice;
    private String booksType;
    private String professionalType;
    private String storePosition;
    private String buyer;
    private String publishingCompany;
    private String bookNo;
    private String author;
    private String publishingTime;
    private String bookNumber;

    public BookBuyDto() {
    }
   
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
    public String getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }
    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

}