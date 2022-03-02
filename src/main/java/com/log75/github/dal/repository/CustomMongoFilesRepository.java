package com.log75.github.dal.repository;

import com.log75.github.dal.entity.MongoFile;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.log75.github.service.exceptions.FileUploadFailedException;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomMongoFilesRepository implements MongoFilesRepository {
    private final GridFsTemplate gridFsTemplate;

    public CustomMongoFilesRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    @Override
    public Optional<GridFSFile> findById(String id) {
        return Optional.ofNullable(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id)))));
    }

    @Override
    public Optional<GridFSFile> findByFileName(String fileName) {
        return Optional.ofNullable(gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName))));
    }

    @Override
    public GridFSFile store(MongoFile mongoFile) {
        ObjectId id = gridFsTemplate.store(mongoFile.getInputStream(), mongoFile.getFileName(), mongoFile.getContentType(), mongoFile.getMetadata());
        return findById(id.toString()).orElseThrow(() -> new FileUploadFailedException("file not uploaded"));
    }

    @Override
    public void delete(String id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(id))));
    }

}
