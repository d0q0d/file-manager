package com.log75.github.service;

import com.log75.github.component.ObjectStorageClient;
import com.log75.github.dal.entity.File;
import com.log75.github.dal.entity.Folder;
import com.log75.github.dal.repository.FileRepository;
import com.log75.github.dal.repository.FolderRepository;
import com.vasl.connect.utils.crud.service.NotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//ObjectStorage implementation
@Service
public class ObjectStorageFileManagerService implements FileManagerService{
    private final FileRepository fileRepository;

    private final FolderRepository folderRepository;
    @Qualifier("arvanStorageClient")
    private final ObjectStorageClient objectStorageClient;

    @Value( "${folder.name.default}")
    private String defaultFolderName;

    @Autowired
    public ObjectStorageFileManagerService(FileRepository fileRepository, FolderRepository folderRepository, ObjectStorageClient objectStorageClient) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.objectStorageClient = objectStorageClient;
    }

    @Override
    public File uploadFile(File fileEntity) throws IOException {
        Folder folder = getFolder(fileEntity.getFolderId().toString());
        objectStorageClient.createObject(fileEntity.getMultipart().getInputStream(),
                fileEntity.getFileName(),
                fileEntity.getMultipart().getSize(),
                fileEntity.getMultipart().getContentType(),
                folder.getFolderName()
        );
        return fileRepository.save(fileEntity);
    }

    @Override
    public File getFile(String id) {
        return getFileById(id);
    }

    @Override
    public File getFileByUUID(String id) {
        var file = fileRepository.findByUuid(id).orElseThrow(() -> new NotFoundException("file.not.found"));
        return getFileById(file.getId());
    }

    @Override
    public InputStream getStream(String id) {
        return null;
    }

    private void setLink(File file) {
        String folderName = Objects.nonNull(file.getFolder()) ? file.getFolder().getFolderName() : defaultFolderName;
        String linkSinged = objectStorageClient.getLinkSinged(folderName, file.getId());
        file.setUrl(linkSinged);
    }

    @Override
    public void deleteFile(String id) {
        File file = getFile(id);
        fileRepository.delete(file);
        objectStorageClient.deleteObject(file.getFolder().getFolderName(), file.getId());
    }

    @Override
    public Page<File> getFilesPaginated(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    @Override
    public List<File> getAllFiles() {
        return (List<File>) fileRepository.findAll();
    }

    @Override
    public List<File> getFolderFiles(String folderId) {
        Folder folder = getFolder(folderId);
        List<File> fileList = fileRepository.findAllByFolderId(new ObjectId(folder.getId()));
        return fileList.stream().peek(file -> {
            setLink(file);
            file.setFolder(folder);
        }).collect(Collectors.toList());
    }

    @Override
    public Page<File> getFolderFilesPaginated(String folderId, Pageable pageable) {
        Folder folder = getFolder(folderId);
        return fileRepository.findAllByFolderId(new ObjectId(folder.getId()), pageable);
    }

    @Override
    public Folder getFolder(String id) {
        return folderRepository.findById(id).orElseThrow(() -> new NotFoundException("folder.not.found"));
    }

    @Override
    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    @Override
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    @Override
    public Page<Folder> getFoldersPaginated(Pageable pageable) {
        return folderRepository.findAll(pageable);
    }

    private File getFileById(String id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new NotFoundException("file.not.found"));
        Folder folder = folderRepository.findById(file.getFolderId().toString()).orElse(null);
        file.setFolder(folder);
        setLink(file);
        return file;
    }

}
