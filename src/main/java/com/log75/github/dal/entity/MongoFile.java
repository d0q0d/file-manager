package com.log75.github.dal.entity;

import com.mongodb.DBObject;
import lombok.Data;

import java.io.InputStream;

@Data
public class MongoFile {
    private String fileName;
    private String contentType;
    private DBObject metadata;
    private InputStream inputStream;
}
