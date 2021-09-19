package com.filestorage.s3.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.filestorage.s3.model.Files;
import com.filestorage.s3.utils.FormatadoresUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
	
	public String uploadFile(MultipartFile file) {
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = file.getOriginalFilename();
		s3Client.putObject( new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();
		
		return "File uploaded: " + fileName;
				
	}
	
	public byte[] downloadFile(String fileName) {
		S3Object s3Object = s3Client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}
	
	public String deleteFile(String fileName) {
		s3Client.deleteObject(bucketName, fileName);
		return fileName + " removed ...";
	}
	
	private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
	
	public List<Files> getObjectslistFromFolder() {
		   
		 ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
		                                            .withBucketName(bucketName);
		  
		 List<Files> keys = new ArrayList<>();
		 		  
		 ObjectListing objects = s3Client.listObjects(listObjectsRequest);
		  
		 for (;;) {
		     List<S3ObjectSummary> summaries = objects.getObjectSummaries();
		     if (summaries.size() < 1) {
		         break;
		     }
		     
		 
		  summaries.forEach(s -> keys.add(new Files(s.getKey(), FormatadoresUtils.formatadorDataBR(s.getLastModified()), FormatadoresUtils.extensaoArquivo(s.getKey()))));
		  objects = s3Client.listNextBatchOfObjects(objects);
		  
		 }
		  
		 return keys;
		}

	public void renameFile(Files fileRename) {
		s3Client.copyObject(bucketName, fileRename.getNomeArquivo(), bucketName, fileRename.getNovoArquivo());
		s3Client.deleteObject(bucketName, fileRename.getNomeArquivo());
	}
	
	
}
