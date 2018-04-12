package cs.repository.repositoryImpl.project;

import cs.domain.project.ProjMaxSeq;
import cs.domain.project.SignBranch;
import cs.repository.IRepository;

import java.util.Date;

/**
 * Created by shenning on 2018/4/11.
 */
public interface ProjMaxSeqRepo extends IRepository<ProjMaxSeq, String> {

    ProjMaxSeq findByDate(String year, String type);
}
