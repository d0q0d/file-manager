package com.log75.github.api.facade.mapper;

import com.log75.github.api.dto.CreateFolderDto;
import com.log75.github.api.dto.FolderDto;
import com.log75.github.dal.entity.Folder;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mapper
public interface FolderFacadeMapper {
    FolderFacadeMapper INSTANCE = Mappers.getMapper(FolderFacadeMapper.class);

    @Mapping(source = "parentId", target = "parentId", qualifiedByName = "stringToObjectId")
    Folder getFolder(CreateFolderDto createFolderDto);

    @Mapping(source = "parentId", target = "parentId", qualifiedByName = "objectIdToString")
    @Mapping(source = "fileIdList", target = "fileIdList", qualifiedByName = "objectIdSetToString")
    @Mapping(source = "folderIdList", target = "folderIdList", qualifiedByName = "objectIdSetToString")
    FolderDto getFolderDto(Folder folder);

    @Named("stringToObjectId")
    static ObjectId toObjectId(String string) {
        if (string == null) {
            return null;
        }
        return new ObjectId(string);
    }

    @Named("objectIdToString")
    static String objectIdToString(ObjectId objectId) {
        if (objectId == null) {
            return null;
        }
        return objectId.toString();
    }

    
    @Named("objectIdSetToString")
    static Set<String> objectIdSetToString(Set<ObjectId> objectIdSet) {
        Set<String> targetStringIdSet = new HashSet<>();
        if (Objects.isNull(objectIdSet))
            return null;
        objectIdSet.forEach(objectId -> targetStringIdSet.add(objectId.toString()));
        return targetStringIdSet;
    }

    @Named("stringSetToObjectId")
    static Set<ObjectId> stringSetToObjectId(Set<String> stringSet) {
        Set<ObjectId> targetObjectIdSet = new HashSet<>();
        if (Objects.isNull(stringSet) || stringSet.isEmpty())
            return null;
        stringSet.forEach(s -> targetObjectIdSet.add(new ObjectId(s)));
        return targetObjectIdSet;
    }
}
