package com.dasmo.drs_api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dasmo.drs_api.dto.UploadedFile;
import com.dasmo.drs_api.exception.FileUploadException;

@Service
public class FileStorageService {

	@Value("${file.upload-dir}")
	private String UPLOAD_DIR;

	private String generateUniqId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public UploadedFile uploadFile(MultipartFile file, String subFolder) {
		if (file.isEmpty()) {
			throw new FileUploadException("No file to upload");
		}

		try {
			//create directory if not exist
			String destination = UPLOAD_DIR + subFolder;
			File dir = new File(destination);
			if(!dir.exists()) {
				dir.mkdirs();
			}

			// Build the filename to store
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			String fileNameToStore = generateUniqId() + "." + extension;

			File destinationFile = new File(dir, fileNameToStore);

			// transfer the file
			Files.copy(file.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setStoredName(fileNameToStore);
			uploadedFile.setExtension(extension);
			uploadedFile.setContentType(file.getContentType());
			uploadedFile.setOriginalName(file.getOriginalFilename());
			uploadedFile.setFilePath(subFolder + "/" + fileNameToStore);
			return uploadedFile;
		} catch (IOException  e) {
			e.printStackTrace();
			throw new FileUploadException("File upload failed", e);
		}
	}

	public List<UploadedFile> uploadFiles(List<MultipartFile> files, String subFolder) {
		if (files.isEmpty() || files.size() == 0) {
			throw new FileUploadException("No file to upload");
		}
		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		for(MultipartFile file : files) {
			uploadedFiles.add(uploadFile(file, subFolder));
		}
		return uploadedFiles;
	}
}
