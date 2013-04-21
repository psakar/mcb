package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

import com.chare.mcb.entity.Description;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.repository.CardTypeRepository;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.www.FeeTypePage.FeeTypePanel;
import com.chare.wicket.TextField;

public class FeeTypePanelTest extends WicketTestCase {

	private FeeType feeType;

	@Test
	public void testRendering() throws Exception {
		tester.startComponentInPage(createPanel());
		tester.assertNoErrorMessage();
		assertDisplaysEntity();
	}

	private FeeTypePanel createPanel() {
		feeType = createFeeType();
		return new FeeTypePanel("id", new Model<FeeType>(feeType));
	}

	static FeeType createFeeType() {
		FeeType feeType = new FeeType("5", new Description("description1", "description2", "description3"));
		feeType.cardType = CardTypePanelTest.createCardType();
		feeType.transferType = TransferTypePanelTest.createTransferType();
		return feeType;
	}

	@Test
	public void testContainsChildren() throws Exception {
		assertEquals(TextField.class, createPanel().get(getPath(PanelPage.FORM_ID, "code")).getClass());
	}


	private void assertDisplaysEntity() {
		tester.assertContains(feeType.code);
		tester.assertContains(feeType.description.description1);
		tester.assertContains(feeType.description.description2);
		tester.assertContains(feeType.description.description3);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	static class TestConfig extends PageConfig {


		@Bean
		public CardTypeRepository cardTypeRepository() {
			return mock(CardTypeRepository.class);
		}

		@Bean
		public FeeTypeRepository feeTypeRepository() {
			return mock(FeeTypeRepository.class);
		}

		@Bean
		public TransferTypeRepository transferTypeRepository() {
			return mock(TransferTypeRepository.class);
		}
	}

}
