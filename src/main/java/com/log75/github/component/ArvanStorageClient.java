package com.log75.github.component;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.log75.github.config.ArvanStorageConfiguration;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ArvanStorageClient implements ObjectStorageClient{
    private final AmazonS3 conn;

    public ArvanStorageClient(ArvanStorageConfiguration arvanStorageConfiguration) {
        AWSCredentials credentials = new BasicAWSCredentials(arvanStorageConfiguration.getAccessKey(), arvanStorageConfiguration.getSecretKey());
        conn = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .enableRegionalUsEast1Endpoint()
                .enableForceGlobalBucketAccess()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(arvanStorageConfiguration.getEndpoint(), "us-east-1")).build();

    }

    @Override
    public void createObject(InputStream file, String fileName, long size, String contentType, String bucketName) {
        createObject(file, fileName, size, contentType, bucketName,null);
    }

    @Override
    public void createObject(InputStream file, String fileName, long size, String contentType, String bucketName, CannedAccessControlList cannedAccessControlList) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file, objectMetadata);
        if (Objects.nonNull(cannedAccessControlList)) {
            putObjectRequest.setCannedAcl(cannedAccessControlList);
        }
        conn.putObject(putObjectRequest);
    }

    @Override
    public List<StorageObject> getBucketObjects(String bucketName) {
        ObjectListing objects = conn.listObjects(bucketName);

        ArrayList<StorageObject> storageObjects = new ArrayList<>();

        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                storageObjects.add(new StorageObject(objectSummary.getKey(), objectSummary.getSize(), objectSummary.getLastModified()));
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        return storageObjects;
    }

    @Override
    public void deleteObject(String bucket, String object) {
        conn.deleteObject(bucket, object);
    }

    @Override
    public void createBucket(String name) {
        conn.createBucket(name);
    }

    @Override
    public void deleteBucket(String bucketName) {
        List<StorageObject> bucketObjects = getBucketObjects(bucketName);
        if (bucketObjects.size() != 0) {
            bucketObjects.forEach(storageObject -> deleteObject(bucketName, storageObject.getName()));
        }
        conn.deleteBucket(bucketName);
    }

    @Override
    public List<Bucket> getBucketList() {
        return conn.listBuckets();
    }

    @Override
    public String getLinkSinged(String bucketName, String fileName) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
        return conn.generatePresignedUrl(request).toString();
    }

    @Override
    public String getLink(String bucketName, String fileName) {
        return conn.getUrl(bucketName, fileName).toString();
    }
}
