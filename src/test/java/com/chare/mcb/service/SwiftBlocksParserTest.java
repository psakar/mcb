package com.chare.mcb.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.mcb.service.SwiftBlocksParser.BodyParser;
import com.chare.mcb.service.SwiftBlocksParser.HeaderParser;
import com.chare.mcb.service.SwiftBlocksParser.StatementLineBody;
import com.chare.mcb.service.SwiftBlocksParser.TrailerParser;
import com.chare.payment.service.BlockOfLines;

public class SwiftBlocksParserTest {




	private static final String BLOCK_START_LINE = "{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:";
	private SwiftBlocksParser parser;
	private HeaderParser headerParser;
	private BodyParser bodyParser;
	private TrailerParser trailerParser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		headerParser = new HeaderParser();
		bodyParser = new BodyParser();
		trailerParser = new TrailerParser();
		parser = new SwiftBlocksParser(headerParser, bodyParser, trailerParser);
	}



	@Test
	public void testParsingEmptyList() throws Exception {
		List<BlockOfLines> blocks = Arrays.asList();
		List<Statement> expectedStatements = Arrays.asList();

		assertBlocksAreParsed(expectedStatements, blocks);
	}

	private void assertBlocksAreParsed(List<Statement> expectedStatements, List<BlockOfLines> blocks) throws Exception {
		List<Statement> statements = parser.parse(blocks);
		assertEquals(expectedStatements.size(), statements.size());
		for (int index = 0; index < statements.size(); index++) {
			assertEquals(expectedStatements.get(index).toString(), statements.get(index).toString());
		}
	}

	@Test
	public void testParsingOneBlock() throws Exception {
		List<String> lines = Arrays.asList(BLOCK_START_LINE,
				":20:COBASA",
				":25:UNCRSKBX/06600064005",
				":28C:75/1",
				":60F:C120417EUR28999,85",

				":61:1204180418D98,54FMSC20120418003117",
				"//CLT-1205389307",
				":86:999Card 5478-1530-0005-8494",
				"   2012.04.16 SHELL - TN,BRATISLAVSK",
				"   SVK - TRENCIN (01302032)",
				"   98,54 EUR",
				/*
				":61:1204180418D95,43FMSC20120418003111",
				"//CLT-1205380497",
				":86:999Card 5478-1530-0009-5132",
				"   2012.04.16 SHELL - KE, ALEJOVA UL",
				"   SVK - KOSICE (01302581)",
				"   95,43 EUR",
				":61:1204180418D89,88FMSC20120418003092",
				"//CLT-1205276060",
				":86:999Card 5478-1570-0036-5015",
				"   2012.04.16 OMV 2220/",
				"   SVK - Jarovce (OSK0012B)",
				"   89,88 EUR",
				":61:1204180418D42,15FMSC20120418003132",
				"//CLT-1205390880",
				":86:999Card 5478-1570-0000-2394",
				"   2012.04.16 PIZZERIA ROMANTICA,BA,",
				"   SVK - BRATISLAVA (03060399)",
				"   42,15 EUR",
				 */
				":62F:C120418EUR28673,85",
				":64:C120418EUR75874,72",		//available balance
				"-}");
		List<BlockOfLines> blocks = Arrays.asList(new BlockOfLines(1, BlockOfLines.TYPE_BODY, lines));

		Statement statement = new Statement();
		statement.account = "06600064005";
		statement.statementDate = Utils.getDate(2012, 4, 18);
		statement.number = "75";
		statement.openingBalance = new BigDecimal("28999.85");
		statement.closingBalance = new BigDecimal("28673.85");
		StatementLine line = statement.addLine();
		line.valueDate = Utils.getDate(2012, 04, 18);
		line.amount = new BigDecimal("-98.54");
		line.swiftType = "FMSC";
		line.reference1 = "20120418003117";
		line.reference2 = "//CLT-1205389307";
		line.details1 = "999Card 5478-1530-0005-8494";
		line.details2 = "2012.04.16 SHELL - TN,BRATISLAVSK";
		line.details3 = "SVK - TRENCIN (01302032)";
		line.details4 = "98,54 EUR";
		/*
				":61:1204180418D98,54FMSC20120418003117",
				"//CLT-1205389307",
				":86:999Card 5478-1530-0005-8494",
				"   2012.04.16 SHELL - TN,BRATISLAVSK",
				"   SVK - TRENCIN (01302032)",
				"   98,54 EUR",
		 */
		List<Statement> expectedStatements = Arrays.asList(statement);

		assertBlocksAreParsed(expectedStatements, blocks);
	}
	/*
	@Test
	public void testParsingMultipleBlocks() throws Exception {
		List<String> lines = Arrays.asList(BLOCK_START_LINE, "line1", "-}${1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:", "line2", "-}");
		BlockOfLines blockOfLines1 = new BlockOfLines(1, BlockOfLines.TYPE_BODY, Arrays.asList(BLOCK_START_LINE, "line1", SwiftParser.BLOCK_END));
		BlockOfLines blockOfLines2 = new BlockOfLines(3, BlockOfLines.TYPE_BODY, Arrays.asList("{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:", "line2", SwiftParser.BLOCK_END));
		List<BlockOfLines> expectedBlocks = Arrays.asList(blockOfLines1, blockOfLines2);

		assertLinesAreParsedToBlocks(expectedBlocks, lines);
	}
	 */



	@Test
	public void testAssertDetailsLength() throws Exception {
		assertDetailsLength(null, null, null, null, null, null, null, null);
		assertDetailsLength("12345678901234567890123456789012345", null, null, null, "12345678901234567890123456789012345", null, null, null);
		assertDetailsLength("12345678901234567890123456789012345", "67890", null, null, "1234567890123456789012345678901234567890", null, null, null);
		assertDetailsLength("12345678901234567890123456789012345", "67890123456789012345678901234567890", "1234567890", null, "12345678901234567890123456789012345678901234567890123456789012345678901234567890", null, null, null);
	}



	private void assertDetailsLength(String expectedDetails1, String expectedDetails2,
			String expectedDetails3, String expectedDetails4, String details1, String details2, String details3, String details4) {
		StatementLineBody body = new StatementLineBody(null, null, null, null, null, details1, details2, details3, details4, 0);
		body.assertDetailsLength();
		assertEquals(expectedDetails1, body.details1);
		assertEquals(expectedDetails2, body.details2);
		assertEquals(expectedDetails3, body.details3);
		assertEquals(expectedDetails4, body.details4);
	}
}
