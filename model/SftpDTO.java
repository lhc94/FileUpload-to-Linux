package com.itbank.model;

import org.springframework.web.multipart.MultipartFile;

public class SftpDTO {

	private MultipartFile uploadFile;
	private String uploadFilePath;

	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getUploadFilePath() {
		return uploadFilePath;
	}
	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}
}
