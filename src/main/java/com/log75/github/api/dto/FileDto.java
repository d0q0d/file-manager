package com.log75.github.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private String id;
    private String uuid;
    private String fileName;
    private String folderId;
    private String url;
    private InputStream stream;
}
