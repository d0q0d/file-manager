package com.log75.github.api.facade;

import com.vasl.connect.filemanager.api.dto.*;
import com.vasl.connect.utils.crud.api.model.PageQueryParams;
import com.vasl.restful_query_builder.model.QueryString;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// TODO: 15.07.21 consider by Milad: why do you don't separate file and folder adapter
public interface FileManagerFacade {
    FileDto uploadFile(UploadFileDto uploadFileDto) throws IOException;

    FileDto getFile(String id);

    void downloadFile(String id, HttpServletResponse response);

    void downloadAndDelete(String uuid, HttpServletResponse response);

    Page<FileDto> getFolderFilesPage(String folderId, PageQueryParams pageQueryParams);

    FolderDto createFolder(CreateFolderDto createFolderDto);

    FolderDto getFolder(String id);

    List<FolderDto> getFolderListByQuery(QueryString queryString);

}
