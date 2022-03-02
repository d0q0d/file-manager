package com.log75.github.dal.repository;

import com.log75.github.dal.entity.Folder;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends PagingAndSortingRepository<Folder, String> {
    @Override
    List<Folder> findAll();

    List<Folder> findAllByParentId(ObjectId parentId);

    List<Folder> findAllByFolderName(String folderName);
}
