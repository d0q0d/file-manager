package com.log75.github.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDto {
    private String id;
    private String folderName;
    private String url;
    private String parentId;
    private Set<String> fileIdList;
    private Set<String> folderIdList;
}
