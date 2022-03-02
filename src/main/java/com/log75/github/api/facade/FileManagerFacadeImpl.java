package com.log75.github.api.facade;

import com.log75.github.api.dto.CreateFolderDto;
import com.log75.github.api.dto.FileDto;
import com.log75.github.api.dto.FolderDto;
import com.log75.github.api.dto.UploadFileDto;
import com.log75.github.dal.entity.File;
import com.log75.github.dal.entity.Folder;
import com.log75.github.service.FileManagerService;
import com.log75.github.api.facade.mapper.FileFacadeMapper;
import com.log75.github.api.facade.mapper.FolderFacadeMapper;
import com.vasl.connect.utils.crud.api.model.PageQueryParams;
import com.vasl.connect.utils.crud.dal.RepositoryUtils;
import com.vasl.restful_query_builder.model.QueryString;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileManagerFacadeImpl implements FileManagerFacade {
    private final FileManagerService fileManagerService;
    private final FileFacadeMapper fileFacadeMapper;
    private final FolderFacadeMapper folderFacadeMapper;

    public FileManagerFacadeImpl(@Qualifier("mongoStorageFileManagerService") FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
        this.fileFacadeMapper = FileFacadeMapper.INSTANCE;
        this.folderFacadeMapper = FolderFacadeMapper.INSTANCE;
    }

    @Override
    public FileDto uploadFile(UploadFileDto uploadFileDto) throws IOException {
        File file = fileFacadeMapper.getFile(uploadFileDto);
        file = fileManagerService.uploadFile(file);
        file.setStream(null);
        return fileFacadeMapper.getFileDto(file);
    }

    @Override
    public FileDto getFile(String id) {
        File file = fileManagerService.getFile(id);
        file.setStream(null);
        return fileFacadeMapper.getFileDto(file);
    }

    @SneakyThrows
    @Override
    public void downloadFile(String id, HttpServletResponse response) {
        FileDto fileDto = getFileForDownload(id);
        response.setHeader("Content-disposition", "attachment;filename=" + fileDto.getFileName());
        FileCopyUtils.copy(fileDto.getStream(), response.getOutputStream());
    }


    @Override
    public Page<FileDto> getFolderFilesPage(String folderId, PageQueryParams pageQueryParams) {
        Pageable pageable = RepositoryUtils.getPageableFromPageQueryParams(pageQueryParams);
        if (Objects.isNull(folderId))
            return fileManagerService.getFilesPaginated(pageable).map(fileFacadeMapper::getFileDto);
        return fileManagerService.getFolderFilesPaginated(folderId, pageable).map(fileFacadeMapper::getFileDto);
    }

    @Override
    public FolderDto createFolder(CreateFolderDto createFolderDto) {
        Folder folder = folderFacadeMapper.getFolder(createFolderDto);
        folder = fileManagerService.createFolder(folder);
        return folderFacadeMapper.getFolderDto(folder);
    }

    @Override
    public FolderDto getFolder(String id) {
        Folder folder = fileManagerService.getFolder(id);
        return folderFacadeMapper.getFolderDto(folder);
    }

    @Override
    public List<FolderDto> getFolderListByQuery(QueryString queryString) {
        return new ArrayList<>();
    }


    @Override
    @SneakyThrows
    public void downloadAndDelete(String uuid, HttpServletResponse response) {
        var fileDto = getFileForDownloadByUUID(uuid);
        response.setHeader("Content-disposition", "attachment;filename=" + fileDto.getFileName());
        FileCopyUtils.copy(fileDto.getStream(), response.getOutputStream());
        fileManagerService.deleteFile(fileDto.getId());
    }

    private FileDto getFileForDownload(String fileId) {
        var file = fileManagerService.getFile(fileId);
        return fileFacadeMapper.getFileDto(file);
    }

    private FileDto getFileForDownloadByUUID(String uuid) {
        var file = fileManagerService.getFileByUUID(uuid);
        return fileFacadeMapper.getFileDto(file);
    }

}
