package cs.repository.repositoryImpl.book;

import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.book.BorrowBookInfo;
import cs.model.book.BookBorrowInfoDto;
import cs.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 图书信息 数据操作实现类
 * author: zsl
 * Date: 2017-9-8 10:24:51
 */
@Repository
public class BorrowBookRepoImpl extends AbstractRepository<BorrowBookInfo, String> implements BorrowBookRepo {
    @Autowired
    BorrowBookRepo borrowBookRepo;

    /**
     * 获取图书详细信息
     * @param bookBorrowInfoDto
     * @return
     */
    @Override
    public List<BookBorrowInfoDto> getBookBorrowList(BookBorrowInfoDto bookBorrowInfoDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.id,t.bookno,t.booksname,t.bookborrower,t.borrownum,t.borrowdate,t.returndate ");
        sqlBuilder.append(" from cs_borrow_info t ");
        sqlBuilder.append("where 1=1 ");
        sqlBuilder.append("and t.bookborrower = :bookborrower ").setParam("bookborrower",""+ SessionUtil.getLoginName()+"");
        if(null != bookBorrowInfoDto){
            if(Validate.isString(bookBorrowInfoDto.getBookNo())){
                sqlBuilder.append(" and t.bookno like :bookno ").setParam("bookno","%" + bookBorrowInfoDto.getBookNo() + "%");
            }
            if(Validate.isString(bookBorrowInfoDto.getBooksName())){
                sqlBuilder.append(" and t.booksname like :booksname ").setParam("booksname","%" + bookBorrowInfoDto.getBooksName() + "%");
            }

            if (Validate.isString(bookBorrowInfoDto.getbTime())) {
                sqlBuilder.append(" and t.borrowdate >= to_date(:bTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("bTime",bookBorrowInfoDto.getbTime().trim() + " 00:00:00");
            }
            if (Validate.isString(bookBorrowInfoDto.getEndTime())) {
                sqlBuilder.append(" and t.borrowdate <= to_date(:eTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("eTime",bookBorrowInfoDto.getEndTime().trim() + " 00:00:00");
            }
        }
        sqlBuilder.append(" order by t.bookscode,t.bookborrower ");
        List<Object[]> bookBorrowInfoList = borrowBookRepo.getObjectArray(sqlBuilder);
        List<BookBorrowInfoDto> bookBorrowInfoDtoList = new ArrayList<BookBorrowInfoDto>();
        if (bookBorrowInfoList.size() > 0) {
            for (int i = 0; i < bookBorrowInfoList.size(); i++) {
                Object[] bookBorrowInfo = bookBorrowInfoList.get(i);
                BookBorrowInfoDto borrowInfoDto = new BookBorrowInfoDto();

                if (null != bookBorrowInfo[0]) {
                    borrowInfoDto.setId((String)bookBorrowInfo[0]);
                }else{
                    borrowInfoDto.setId(null);
                }

                if (null != bookBorrowInfo[1]) {
                    borrowInfoDto.setBookNo((String)bookBorrowInfo[1]);
                }else{
                    borrowInfoDto.setBookNo(null);
                }

                if (null != bookBorrowInfo[2]) {
                    borrowInfoDto.setBooksName((String)bookBorrowInfo[2]);
                }else{
                    borrowInfoDto.setBooksName(null);
                }

                if (null != bookBorrowInfo[3]) {
                    borrowInfoDto.setBookBorrower((String)bookBorrowInfo[3]);
                }else{
                    borrowInfoDto.setBookBorrower(null);
                }

                if (null != bookBorrowInfo[4]) {
                    BigDecimal temp = (BigDecimal) bookBorrowInfo[4];
                    borrowInfoDto.setBorrowNum(temp.intValue());
                }else{
                    borrowInfoDto.setBorrowNum(null);
                }

                if (null != bookBorrowInfo[5]) {
                    borrowInfoDto.setBorrowDate((Date) bookBorrowInfo[5]);
                }else{
                    borrowInfoDto.setBorrowDate(null);
                }

                if (null != bookBorrowInfo[6]) {
                    borrowInfoDto.setReturnDate((Date) bookBorrowInfo[6]);
                }else{
                    borrowInfoDto.setReturnDate(null);
                }

                bookBorrowInfoDtoList.add(borrowInfoDto);
            }
        }

        return  bookBorrowInfoDtoList;
    }

    /**
     * 获取个人借书总数
     * @param bookBorrowInfoDto
     * @return
     */
    @Override
    public List<BookBorrowInfoDto> getBookBorrowSum(BookBorrowInfoDto bookBorrowInfoDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select t.bookborrower,sum(t.borrownum) ");
        sqlBuilder.append(" from cs_borrow_info t  ");
        sqlBuilder.append("where 1=1 ");
        sqlBuilder.append("and t.bookborrower = :bookborrower ").setParam("bookborrower",""+ SessionUtil.getLoginName()+"");
        if(null != bookBorrowInfoDto){
            if(Validate.isString(bookBorrowInfoDto.getBookNo())){
                sqlBuilder.append(" and t.bookno like :bookno ").setParam("bookno","%" + bookBorrowInfoDto.getBookNo() + "%");
            }
            if(Validate.isString(bookBorrowInfoDto.getBooksName())){
                sqlBuilder.append(" and t.booksname like :booksname ").setParam("booksname","%" + bookBorrowInfoDto.getBooksName() + "%");
            }
            if (Validate.isString(bookBorrowInfoDto.getbTime())) {
                sqlBuilder.append(" and t.borrowdate >= to_date(:bTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("bTime",bookBorrowInfoDto.getbTime().trim() + " 00:00:00");
            }
            if (Validate.isString(bookBorrowInfoDto.getEndTime())) {
                sqlBuilder.append(" and t.borrowdate <= to_date(:eTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("eTime",bookBorrowInfoDto.getEndTime().trim() + " 00:00:00");
            }
        }
        sqlBuilder.append("group by t.bookborrower  ");
        List<Object[]> bookBorrowInfoList = borrowBookRepo.getObjectArray(sqlBuilder);
        List<BookBorrowInfoDto> bookBorrowInfoDtoList = new ArrayList<BookBorrowInfoDto>();
        if (bookBorrowInfoList.size() > 0) {
            for (int i = 0; i < bookBorrowInfoList.size(); i++) {
                Object[] bookBorrowInfo = bookBorrowInfoList.get(i);
                BookBorrowInfoDto borrowInfoDto = new BookBorrowInfoDto();
                if (null != bookBorrowInfo[0]) {
                    borrowInfoDto.setBookBorrower((String)bookBorrowInfo[0]);
                }else{
                    borrowInfoDto.setBookBorrower(null);
                }
                if (null != bookBorrowInfo[1]) {
                    BigDecimal temp = (BigDecimal) bookBorrowInfo[1];
                    borrowInfoDto.setTotalCount(temp.intValue());
                }else{
                    borrowInfoDto.setTotalCount(null);
                }
                bookBorrowInfoDtoList.add(borrowInfoDto);
            }
        }

        return  bookBorrowInfoDtoList;
    }
}