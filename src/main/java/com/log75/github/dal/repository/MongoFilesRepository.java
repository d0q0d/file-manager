package com.log75.github.dal.repository;

import com.log75.github.dal.entity.MongoFile;
import com.mongodb.client.gridfs.model.GridFSFile;

import java.util.Optional;

public interface MongoFilesRepository {
    Optional<GridFSFile> findById(String id);
    Optional<GridFSFile> findByFileName(String fileName);
    GridFSFile store(MongoFile mongoFile);
    void delete(String id);
}
