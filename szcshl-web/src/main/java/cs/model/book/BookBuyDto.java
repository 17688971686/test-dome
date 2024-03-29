package cs.model.book;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.book.BookBuyBusiness;
import cs.model.BaseDto;

import java.util.Date;


/**
 * Description: 图书详细信息 页面数据模型
 * author: zsl
 * Date: 2017-9-8 14:16:17
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
    @JSONField(format = "yyyy-MM-dd")
    private Date publishingTime;
    private Integer bookNumber;
    private Integer storeConfirm;
    private String applyDept;
    private String businessId;
    private String operator;
    private String buyChannel;
    private BookBuyBusiness bookBuyBusiness;
    @JSONField(format = "yyyy-MM-dd")
    private Date storeTime ;
    private String businessName;
    private String applyReason;

    public BookBuyDto() {
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Date getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(Date storeTime) {
        this.storeTime = storeTime;
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
    public Date getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(Date publishingTime) {
        this.publishingTime = publishingTime;
    }

    public BookBuyBusiness getBookBuyBusiness() {
        return bookBuyBusiness;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBuyChannel() {
        return buyChannel;
    }

    public void setBuyChannel(String buyChannel) {
        this.buyChannel = buyChannel;
    }

    public void setBookBuyBusiness(BookBuyBusiness bookBuyBusiness) {
        this.bookBuyBusiness = bookBuyBusiness;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(Integer bookNumber) {
        this.bookNumber = bookNumber;
    }

    public Integer getStoreConfirm() {
        return storeConfirm;
    }

    public void setStoreConfirm(Integer storeConfirm) {
        this.storeConfirm = storeConfirm;
    }
}