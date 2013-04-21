package com.chare.mcb.service;

import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.User;

public interface PostingFileGenerator {

	PostingFile generatePostingFileFor(Statement statement, User user);
}
