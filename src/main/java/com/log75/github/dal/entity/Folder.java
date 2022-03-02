package com.log75.github.dal.entity;

import com.vasl.connect.utils.crud.dal.Entity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Document("folders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Folder extends Entity {
    @Indexed
    private ObjectId parentId;
    private String url;
    private String folderName;
    @Transient
    private Set<ObjectId> fileIdList;
    @Transient
    private Set<ObjectId> folderIdList;
}
