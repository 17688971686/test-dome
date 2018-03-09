package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Company;
import cs.repository.IRepository;

public interface CompanyRepo extends IRepository<Company, String> {
    /**
     * 根据单位名称查询
     * @param coName
     */
    Company  findCompany(String coName);
}
