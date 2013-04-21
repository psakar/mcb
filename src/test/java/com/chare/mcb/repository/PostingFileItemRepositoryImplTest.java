package com.chare.mcb.repository;

import com.chare.mcb.ListAndCountRepositoryTestCase;
import com.chare.mcb.entity.EntityWithIdInteger;
import com.chare.mcb.entity.PostingFileItem;

public class PostingFileItemRepositoryImplTest extends ListAndCountRepositoryTestCase<PostingFileItem, PostingFileItemRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.POSTING_FILE_TABLE;
	}


	@Override
	protected PostingFileItemRepository getRepository() {
		return new PostingFileItemRepositoryImpl(entityManager);
	}
}
