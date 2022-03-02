package com.log75.github.api.facade.mapper;

import com.log75.github.api.dto.FileDto;
import com.log75.github.api.dto.UploadFileDto;
import com.log75.github.dal.entity.File;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(imports = java.util.UUID.class)
public interface FileFacadeMapper {
    FileFacadeMapper INSTANCE = Mappers.getMapper(FileFacadeMapper.class);

    @Mapping(source = "folderId", target = "folderId", qualifiedByName = "stringToObjectId")
    @Mapping(source = "file", target = "multipart")
    @Mapping(target = "uuid", expression = "java(UUID.randomUUID().toString())")
    File getFile(UploadFileDto uploadFileDto);

    @Mapping(source = "folderId", target = "folderId", qualifiedByName = "objectIdToString")
    FileDto getFileDto(File file);

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
}
