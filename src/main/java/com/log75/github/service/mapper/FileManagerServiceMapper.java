package com.log75.github.service.mapper;

import com.log75.github.dal.entity.File;
import com.log75.github.dal.entity.Folder;
import com.log75.github.dal.entity.MongoFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

@Mapper
public interface FileManagerServiceMapper {
   FileManagerServiceMapper INSTANCE = Mappers.getMapper(FileManagerServiceMapper.class);

   MongoFile getMongoFile(InputStream inputStream, String fileName, String contentType, DBObject metadata);

   default DBObject getDBObject(File file, String folderName) {
      DBObject metaData = new BasicDBObject();
      metaData.put("type", file.getMultipart().getContentType());
      metaData.put("title", file.getFileName());
      metaData.put("folderId", file.getFolderId());
      metaData.put("folderName", folderName);
      metaData.put("id", file.getId());
      return metaData;
   }

   default void updateFolderIdList(Folder parentFolder, Set<ObjectId> folderIdList) {
      if (Objects.isNull(parentFolder.getFolderIdList()))
         parentFolder.setFolderIdList(folderIdList);
      else
         parentFolder.getFolderIdList().addAll(folderIdList);
   }

   default Folder getFolderByFolderName(String defaultFolderName) {
      Folder folder = new Folder();
      folder.setFolderName(defaultFolderName);
      return folder;
   }
}
