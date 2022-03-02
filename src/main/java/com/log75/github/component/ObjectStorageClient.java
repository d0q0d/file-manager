package com.log75.github.component;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.InputStream;
import java.util.List;

public interface ObjectStorageClient {
    void createObject(InputStream file, String fileName, long size, String contentType, String bucketName);
    void createObject(InputStream file, String fileName, long size, String contentType, String bucketName, CannedAccessControlList cannedAccessControlList);
    List<StorageObject> getBucketObjects(String bucketName);
    void deleteObject(String bucket, String object);
    void createBucket(String name);
    void deleteBucket(String bucketName);
    List<Bucket> getBucketList();
    String getLinkSinged(String bucketName, String fileName);
    String getLink(String bucketName, String fileName);
}
