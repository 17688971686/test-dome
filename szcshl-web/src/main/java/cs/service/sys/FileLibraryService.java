package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.FileLibrary;
import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;

/**
 * Created by MCL
 * 2017/8/21
 */
public interface FileLibraryService {

    /**
     * 初始化文件夹 树
     * @param libraryType
     * @return
     */
    List<FileLibraryDto> initFolder(String libraryType);

    /**
     * 新增文件夹
     * @param fileLibraryDto
     * @return
     */
    ResultMsg addFolder(FileLibraryDto fileLibraryDto);

    /**
     * 通过父id初始化文件夹下的所有文件
     * @param fileId
     * @return
     */
    ResultMsg initFileList(String fileId , String fileType);

    /**
     * 新增文件
     * @param fileLibraryDto
     * @return
     */
    ResultMsg saveFile(FileLibraryDto fileLibraryDto);

    String findFileUrlById(String fileId);

    FileLibraryDto findFileById(String fileId);

    /**
     * 更新文件
     * @param fileLibraryDto
     * @return
     */
    ResultMsg updateFile(FileLibraryDto fileLibraryDto);


    void deleteFile(String fileId);

    /**
     * 删除文件夹
     * @param parentFileId
     * @return
     */
    ResultMsg deleteRootDirectory(String parentFileId);

    /**
     * 文件指标库数据查询
     * @return
     */
    Map<String , Object> getFiles();

    Map<String , Object> findFiles();
}
