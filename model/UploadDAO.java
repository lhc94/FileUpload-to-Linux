package com.itbank.model;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadDAO {

	@Insert("insert into sftp values (#{uploadFilePath})")
	public int insert(SftpDTO dto);
}
