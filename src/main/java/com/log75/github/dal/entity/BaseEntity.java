package com.log75.github.dal.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    protected Boolean deleted=false;

    @CreatedDate
    protected LocalDateTime createdDate;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }
}
