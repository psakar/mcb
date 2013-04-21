package com.chare.mcb.service;

import java.io.InputStream;
import java.util.List;

import com.chare.mcb.entity.Statement;

public interface UploadFileService {
	List<Statement> uploadFile(InputStream inputStream, String filename);
}
