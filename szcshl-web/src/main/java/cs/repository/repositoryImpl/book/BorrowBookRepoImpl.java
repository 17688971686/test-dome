package cs.repository.repositoryImpl.book;

import cs.common.HqlBuilder;
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
        sqlBuilder.append("select t.bookscode,t.booksname,t.bookborrower,t.borrownum,t.borrowdate,t.returndate ");
        sqlBuilder.append(" from cs_borrow_info t ");
        sqlBuilder.append("where 1=1 ");
        if(null != bookBorrowInfoDto){
            if(Validate.isString(bookBorrowInfoDto.getBooksCode())){
                sqlBuilder.append(" and t.bookscode like :bookscode ").setParam("bookscode","%"+bookBorrowInfoDto.getBooksCode()+"% ");
            }
            if(Validate.isString(bookBorrowInfoDto.getBooksName())){
                sqlBuilder.append(" and t.booksname like :booksname ").setParam("booksname","%"+bookBorrowInfoDto.getBooksName()+"% ");
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
                    borrowInfoDto.setBooksCode((String)bookBorrowInfo[0]);
                }else{
                    borrowInfoDto.setBooksCode(null);
                }

                if (null != bookBorrowInfo[1]) {
                    borrowInfoDto.setBooksName((String)bookBorrowInfo[1]);
                }else{
                    borrowInfoDto.setBooksName(null);
                }

                if (null != bookBorrowInfo[2]) {
                    borrowInfoDto.setBookBorrower((String)bookBorrowInfo[2]);
                }else{
                    borrowInfoDto.setBookBorrower(null);
                }

                if (null != bookBorrowInfo[3]) {
                    BigDecimal temp = (BigDecimal) bookBorrowInfo[3];
                    borrowInfoDto.setBorrowNum(temp.intValue());
                }else{
                    borrowInfoDto.setBorrowNum(null);
                }

                if (null != bookBorrowInfo[4]) {
                    borrowInfoDto.setBorrowDate((Date) bookBorrowInfo[4]);
                }else{
                    borrowInfoDto.setBorrowDate(null);
                }

                if (null != bookBorrowInfo[5]) {
                    borrowInfoDto.setReturnDate((Date) bookBorrowInfo[5]);
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
        if(null != bookBorrowInfoDto){
            if(Validate.isString(bookBorrowInfoDto.getBooksCode())){
                sqlBuilder.append(" and t.bookscode like :bookscode ").setParam("bookscode","%"+bookBorrowInfoDto.getBooksCode()+"% ");
            }
            if(Validate.isString(bookBorrowInfoDto.getBooksName())){
                sqlBuilder.append(" and t.booksname like :booksname ").setParam("booksname","%"+bookBorrowInfoDto.getBooksName()+"% ");
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