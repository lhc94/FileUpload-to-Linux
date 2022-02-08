package com.itbank.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itbank.model.SftpDTO;
import com.itbank.model.UploadDAO;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class UploadService {
	// 파일 전송에 필요한 정보
	private final String serverIP = "192.168.0.92";
	private final int serverPort = 22;
	private final String serverUser = "root";
	private final String serverPass = "1";
	private ChannelSftp chSftp = null;
	
	@Autowired private UploadDAO dao;

	public String upload(SftpDTO dto) throws Exception {
		// 1) 업로드 받은 파일을 임시파일로 웹서버에 생성
		MultipartFile file = dto.getUploadFile();
		File dest = new File(file.getOriginalFilename());
		file.transferTo(dest);
		
		// 2) 웹서버에 생성된 임시파일을 파일서버에 전송
		Session sess = null;
		Channel channel = null;
		JSch jsch = new JSch();
		
		sess = jsch.getSession(serverUser, serverIP, serverPort);
		sess.setPassword(serverPass);
		sess.setConfig("StrictHostKeyChecking", "no");
		sess.connect();
		
		System.out.println("sftp> connected");
		
		channel = sess.openChannel("sftp");
		channel.connect();
		
		chSftp = (ChannelSftp) channel;	// 리눅스 서버에 SFTP형식으로 접속
		
		FileInputStream fis = new FileInputStream(dest);
		chSftp.cd("/var/www/html"); 	// 디렉토리 이동
		chSftp.put(fis, dest.getName());
		System.out.println("sftp> transfer complete");
		
		fis.close();
		chSftp.exit();
		
		String uploadFilePath = "";
		uploadFilePath += "http://";
		uploadFilePath += serverIP;
		uploadFilePath += ":1234";	// 기본 포트는 80이며 작성필요없으나, 서비스가 중복된다면 별도로 지정
		uploadFilePath += "/" + dest.getName();
		
		dto.setUploadFilePath(uploadFilePath);
		
		
		// 3) 임시파일은 제거하고, 파일서버상의 경로를 문자열로 반환
		int row = dao.insert(dto);
		dest.delete();
		return row == 1 ? dto.getUploadFilePath() : null;
	}

}
