package com.chare.mcb.service;

import static com.chare.mcb.service.SwiftBlocksParser.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.service.SwiftBlocksParser.BodyParser;
import com.chare.mcb.service.SwiftBlocksParser.StatementLineBody;
import com.chare.payment.service.BlockOfLines;

public class BodyParserTest {




	private BodyParser parser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		parser = new BodyParser();
	}



	@Test//(expected = IllegalArgumentException.class)
	public void testParsingEmptyList() throws Exception {
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_HEADER, new ArrayList<String>());

		List<StatementLineBody> expected = Arrays.asList();

		assertArrayEquals(expected.toArray(), parser.parse(block, 0).toArray());
	}

	@Test
	public void testParsingOneLine() throws Exception {
		List<String> lines = Arrays.asList("test",
				":61:1204180418D98,54FMSC20120418003117",
				"//CLT-1205389307",
				":86:999Card 5478-1530-0005-8494",
				"   2012.04.16 SHELL - TN,BRATISLAVSK",
				"   SVK - TRENCIN (01302032)",
				"   98,54 EUR"
				);
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_BODY, new ArrayList<String>(lines));

		Date valueDate = Utils.getDate(2012, 4, 18);
		BigDecimal amount = new BigDecimal("-98.54");
		String swiftType = "FMSC";
		String reference1 = "20120418003117";
		String reference2 = "//CLT-1205389307";
		String details1 = "999Card 5478-1530-0005-8494";
		String details2 = "2012.04.16 SHELL - TN,BRATISLAVSK";
		String details3 = "SVK - TRENCIN (01302032)";
		String details4 = "98,54 EUR";
		int lastLineIndex = 6;
		StatementLineBody statementLineBody = new StatementLineBody(valueDate, amount, swiftType, reference1, reference2, details1, details2, details3, details4, lastLineIndex );
		List<StatementLineBody> expected = Arrays.asList(statementLineBody);

		assertEquals(ArrayUtils.toString(expected.toArray()), ArrayUtils.toString(parser.parse(block, 1).toArray()));
	}


	@Test
	public void testParsingMultipleLines() throws Exception {
		List<String> lines = Arrays.asList("test",
				":61:1204180418D98,54FMSC20120418003117",
				"//CLT-1205389307",
				":86:999Card 5478-1530-0005-8494",
				"   2012.04.16 SHELL - TN,BRATISLAVSK",
				"   SVK - TRENCIN (01302032)",
				"   98,54 EUR",
				":61:1204180418D42,15FMSC20120418003132",
				"//CLT-1205390880",
				":86:999Card 5478-1570-0000-2394",
				"   2012.04.16 PIZZERIA ROMANTICA,BA,",
				"   SVK - BRATISLAVA (03060399)",
				"   42,15 EUR",
				":62F:C120418EUR28673,85",
				":64:C120418EUR75874,72",		//available balance
				"-}"
				);
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_BODY, new ArrayList<String>(lines));

		Date valueDate = Utils.getDate(2012, 4, 18);
		BigDecimal amount = new BigDecimal("-98.54");
		String swiftType = "FMSC";
		String reference1 = "20120418003117";
		String reference2 = "//CLT-1205389307";
		String details1 = "999Card 5478-1530-0005-8494";
		String details2 = "2012.04.16 SHELL - TN,BRATISLAVSK";
		String details3 = "SVK - TRENCIN (01302032)";
		String details4 = "98,54 EUR";
		int lastLineIndex = 6;
		StatementLineBody statementLineBody = new StatementLineBody(valueDate, amount, swiftType, reference1, reference2, details1, details2, details3, details4, lastLineIndex );

		Date valueDate_2 = Utils.getDate(2012, 4, 18);
		BigDecimal amount_2 = new BigDecimal("-42.15");
		String swiftType_2 = "FMSC";
		String reference1_2 = "20120418003132";
		String reference2_2 = "//CLT-1205390880";
		String details1_2 = "999Card 5478-1570-0000-2394";
		String details2_2 = "2012.04.16 PIZZERIA ROMANTICA,BA,";
		String details3_2 = "SVK - BRATISLAVA (03060399)";
		String details4_2 = "42,15 EUR";
		int lastLineIndex_2 = 13;
		StatementLineBody statementLineBody_2 = new StatementLineBody(valueDate_2, amount_2, swiftType_2, reference1_2, reference2_2, details1_2, details2_2, details3_2, details4_2, lastLineIndex_2 );

		List<StatementLineBody> expected = Arrays.asList(statementLineBody, statementLineBody_2);

		assertEquals(ArrayUtils.toString(expected.toArray()), ArrayUtils.toString(parser.parse(block, 1).toArray()));
	}

	@Test
	public void testLineWithLongDescriptionIsParsedAndDescriptionIsSplit() throws Exception {
		List<String> lines = Arrays.asList("test",
				":61:1205140514C60000,FMSC20120514460179",
				"//00138097483",
				":86:088?00E-TUZ-DOSLA/DOM-EXPRTRNSF-I?20/8050?21KS 0000000000?22VS 00",
				"00000000?23SS 0000000000?308050"
				);
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_BODY, new ArrayList<String>(lines));

		Date valueDate = Utils.getDate(2012, 5, 14);
		BigDecimal amount = new BigDecimal("60000.00");
		String swiftType = "FMSC";
		String reference1 = "20120514460179";
		String reference2 = "//00138097483";
		String details1 = "088?00E-TUZ-DOSLA/DOM-EXPRTRNSF-I?2";
		String details2 = "0/8050?21KS 0000000000?22VS 0000000";
		String details3 = "000?23SS 0000000000?308050";
		int lastLineIndex = 4;
		StatementLineBody statementLineBody = new StatementLineBody(valueDate, amount, swiftType, reference1, reference2, details1, details2, details3, null, lastLineIndex );
		List<StatementLineBody> expected = Arrays.asList(statementLineBody);

		assertEquals(ArrayUtils.toString(expected.toArray()), ArrayUtils.toString(parser.parse(block, 1).toArray()));
	}


	@Test
	public void testIsTrailerLine() throws Exception {
		assertEquals(false, parser.isTrailerLine(LINE_WITHOUT_TAG));
		assertEquals(false, parser.isTrailerLine(LINE_20));
		assertEquals(false, parser.isTrailerLine(LINE_25));
		assertEquals(false, parser.isTrailerLine(LINE_28C));
		assertEquals(false, parser.isTrailerLine(LINE_60F));
		assertEquals(false, parser.isTrailerLine(LINE_60M));
		assertEquals(false, parser.isTrailerLine(LINE_61));
		assertEquals(true, parser.isTrailerLine(LINE_62F));
		assertEquals(true, parser.isTrailerLine(LINE_62M));
		assertEquals(true, parser.isTrailerLine(LINE_64));
		assertEquals(false, parser.isTrailerLine(LINE_86));
	}


}
