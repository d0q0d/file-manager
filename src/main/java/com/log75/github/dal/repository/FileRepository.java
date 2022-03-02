package com.log75.github.dal.repository;

import com.log75.github.dal.entity.File;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, String> {
    List<File> findAllByFolderId(ObjectId folderId);
    Page<File> findAllByFolderId(ObjectId folderId, Pageable pageable);
    Optional<File> findByUuid(String uuid);
}
