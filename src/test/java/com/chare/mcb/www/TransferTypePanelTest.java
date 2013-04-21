package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Description;
import com.chare.mcb.entity.TransferType;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.www.TransferTypePage.TransferTypePanel;
import com.chare.wicket.TextField;

public class TransferTypePanelTest extends WicketTestCase {

	private TransferType cardType;

	@Test
	public void testRendering() throws Exception {
		tester.startComponentInPage(createPanel());
		tester.assertNoErrorMessage();
		assertDisplaysEntity();
	}

	private TransferTypePanel createPanel() {
		cardType = createTransferType();
		return new TransferTypePanel("id", new Model<TransferType>(cardType));
	}

	static TransferType createTransferType() {
		return new TransferType("5", new Description("description1", "description2", "description3"));
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertEquals(TextField.class, createPanel().get(getPath(PanelPage.FORM_ID, "code")).getClass());
	}


	private void assertDisplaysEntity() {
		tester.assertContains(cardType.code);
		tester.assertContains(cardType.description.description1);
		tester.assertContains(cardType.description.description2);
		tester.assertContains(cardType.description.description3);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {
		@Bean
		public TransferTypeRepository rootAccountFullRepository() {
			return mock(TransferTypeRepository.class);
		}
	}

}
