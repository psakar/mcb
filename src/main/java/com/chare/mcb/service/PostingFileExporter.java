package com.chare.mcb.service;

import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.User;

public interface PostingFileExporter {

	void export(PostingFile postingFile, User user);
	void disapprove(PostingFile postingFile, User user);
}
