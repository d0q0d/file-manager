package com.log75.github.service;

import com.log75.github.dal.entity.File;
import com.log75.github.dal.entity.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileManagerService {

    File uploadFile(File file) throws IOException;
    File getFile(String id);
    File getFileByUUID(String id);
    InputStream getStream(String id);
    void deleteFile(String id);
    Page<File> getFilesPaginated(Pageable pageable);
    List<File> getAllFiles();
    List<File> getFolderFiles(String folderId);
    Page<File> getFolderFilesPaginated(String folderId, Pageable pageable);

    Folder getFolder(String id);
    Folder createFolder(Folder folder);
    List<Folder> getAllFolders();
    Page<Folder> getFoldersPaginated(Pageable pageable);

}
