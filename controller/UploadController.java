package com.itbank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.itbank.model.SftpDTO;
import com.itbank.service.UploadService;

@Controller
public class UploadController {
	
	@Autowired private UploadService us;

	@PostMapping("/upload")
	public String upload(SftpDTO dto, HttpSession session) throws Exception {
		
		System.out.println(dto.getUploadFile().getOriginalFilename());
		String uploadFilePath = us.upload(dto);
		System.out.println(uploadFilePath);
		
		session.setAttribute("uploadFilePath", uploadFilePath);
		
		return "redirect:/";
	}
}
