package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.FileLibrary;
import cs.model.PageModelDto;
import cs.model.sys.FileLibraryDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Created by MCL
 * 2017/8/21
 */
public interface FileLibraryService {

    List<FileLibraryDto> initFolder(ODataObj oDataObj,String libraryType);

    /**
     * 质量管理文件库
     * @param fileLibraryDto
     * @param libraryType
     * @return
     */
    ResultMsg addFolder(FileLibraryDto fileLibraryDto, String libraryType);

    PageModelDto<FileLibraryDto> initFileList(ODataObj oDataObj ,String fileId);
    FileLibraryDto saveFile(FileLibraryDto fileLibraryDto,String libraryType);

    String findFileUrlById(String fileId);

    FileLibraryDto findFileById(String fileId);
    void updateFile(FileLibraryDto fileLibraryDto);
    void deleteFile(String fileId);

    void deleteRootDirectory(String parentFileId);
}
