package com.log75.github.service;

import com.log75.github.dal.entity.File;
import com.log75.github.dal.entity.Folder;
import com.log75.github.dal.entity.MongoFile;
import com.log75.github.dal.repository.FileRepository;
import com.log75.github.dal.repository.FolderRepository;
import com.log75.github.dal.repository.MongoFilesRepository;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.log75.github.service.mapper.FileManagerServiceMapper;
import com.vasl.connect.utils.crud.service.NotFoundException;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MongoStorageFileManagerService implements FileManagerService {
    private final MongoFilesRepository mongoFilesRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final GridFsOperations operations;
    @Value( "${folder.name.default:root}")
    private String defaultFolderName;
    @Value( "${download.baseUrl}")
    private String downloadBaseUrl;

    private final FileManagerServiceMapper fileManagerServiceMapper = FileManagerServiceMapper.INSTANCE;


    public MongoStorageFileManagerService(MongoFilesRepository mongoFilesRepository, FolderRepository folderRepository, FileRepository fileRepository, GridFsOperations operations) {
        this.mongoFilesRepository = mongoFilesRepository;
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.operations = operations;
    }

    @Override
    @SneakyThrows
    @Transactional
    public File uploadFile(File file) {
        Folder folder;
        if (Objects.isNull(file.getFolderId())){
            folder = folderRepository.findAllByFolderName(defaultFolderName).get(0);
            file.setFolderId(new ObjectId(folder.getId()));
        } else
            folder = folderRepository.findById(file.getFolderId().toString()).orElseThrow(() -> new NotFoundException("folder.not.found"));
        DBObject metaData = fileManagerServiceMapper.getDBObject(file, folder.getFolderName());
        File savedFile = fileRepository.save(file);
        MongoFile mongoFile = fileManagerServiceMapper.getMongoFile(file.getMultipart().getInputStream(), savedFile.getId(), file.getMultipart().getContentType(), metaData);
        GridFSFile gridFSFile = mongoFilesRepository.store(mongoFile);
        savedFile.setStream(operations.getResource(gridFSFile).getInputStream());
        savedFile.setUrl(downloadBaseUrl + "/api/v1/fm/files/stream/" + file.getId());
        return savedFile;
    }

    @Override
    @SneakyThrows
    public File getFile(String id) {
        return getFileById(id);
    }

    @Override
    @SneakyThrows
    public File getFileByUUID(String uuid) {
        var file = fileRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("file.not.found"));
        return getFileById(file.getId());
    }

    @Override
    @SneakyThrows
    public InputStream getStream(String id) {
        GridFSFile gridFSFile = mongoFilesRepository.findByFileName(id).orElseThrow(() -> new NotFoundException("file.not.found"));
        return operations.getResource(gridFSFile).getInputStream();
    }

    @Override
    public void deleteFile(String id) {
        mongoFilesRepository.delete(id);
        fileRepository.deleteById(id);
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
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NotFoundException("folder.not.found"));
        return fileRepository.findAllByFolderId(new ObjectId(folder.getId()));
    }

    @Override
    @SneakyThrows
    public Page<File> getFolderFilesPaginated(String folderId, Pageable pageable) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NotFoundException("file.not.found"));
        return fileRepository.findAllByFolderId(new ObjectId(folder.getId()), pageable);
    }

    public Folder getFolder(String id) {
        Folder folder = folderRepository.findById(id).orElseThrow(() -> new NotFoundException("folder.not.found"));
        Set<ObjectId> fileIdList = getFolderFiles(folder.getId()).stream().map(file -> new ObjectId(file.getId())).collect(Collectors.toSet());
        Set<ObjectId> folderIdList = getFoldersByParentId(new ObjectId(folder.getId())).stream().map(existingFolder -> new ObjectId(existingFolder.getId())).collect(Collectors.toSet());
        folder.setFileIdList(fileIdList);
        folder.setFolderIdList(folderIdList);
        return folder;
    }

    @Override
    public Folder createFolder(Folder folder) {
        if (Objects.nonNull(folder.getParentId()))
            getFolder(folder.getParentId().toString());
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

    private List<Folder> getFoldersByParentId(ObjectId parentId){
        return folderRepository.findAllByParentId(parentId);
    }

    private File getFileById(String id) throws IOException {
        File file = fileRepository.findById(id).orElseThrow(() -> new NotFoundException("file.not.found"));
        GridFSFile gridFSFile = mongoFilesRepository.findByFileName(id).orElseThrow(() -> new NotFoundException("file.not.found"));
        file.setStream(operations.getResource(gridFSFile).getInputStream());
        file.setUrl(downloadBaseUrl + "/api/v1/fm/files/stream/" + file.getId());
        return file;
    }

}
