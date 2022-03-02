package com.log75.github.component;

import lombok.Data;

import java.util.Date;

@Data
public class StorageObject {
    private String name;
    private long size;
    private Date lastModified;

    public StorageObject(String name, long size, Date lastModified) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
    }
}
