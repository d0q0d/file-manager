package com.log75.github.dal.entity;

import com.vasl.connect.utils.crud.dal.Entity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Document("files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends Entity {
    @Transient
    private String url;
    private String fileName;
    private String uuid;

    @Indexed
    private ObjectId folderId;

    @Transient
    private MultipartFile multipart;
    @Transient
    private Folder folder;

    @Transient
    private InputStream stream;
}
