package com.chare.mcb.repository;

import java.util.Date;

import com.chare.mcb.entity.PostingFile;
import com.chare.repository.JpaRepository;

public interface PostingFileRepository extends JpaRepository<Integer, PostingFile> {

	int findLastSequenceNr(Date businessDate);

	int findLastReferenceNr(Date businessDate);

}
