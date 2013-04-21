package com.chare.mcb.service;

import static com.chare.mcb.service.SwiftBlocksParser.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.service.SwiftBlocksParser.HeaderParser;
import com.chare.mcb.service.SwiftBlocksParser.StatementHeader;
import com.chare.payment.service.BlockOfLines;

public class HeaderParserTest {




	private static final String BLOCK_START_LINE = "{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:";
	private HeaderParser parser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		parser = new HeaderParser();
	}



	@Test(expected = IllegalArgumentException.class)
	public void testParsingEmptyList() throws Exception {
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_HEADER, new ArrayList<String>());

		parser.parse(block);
	}

	@Test
	public void testParsingBlock() throws Exception {
		List<String> lines = Arrays.asList(BLOCK_START_LINE,
				":20:COBASA",
				":25:UNCRSKBX/06600064005",
				":28C:75/1",
				":60F:C120417EUR28999,85");
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_HEADER, new ArrayList<String>(lines));

		StatementHeader expected = new StatementHeader("COBASA", "UNCRSKBX/06600064005", "75/1", new BigDecimal("28999.85"), null, Utils.getDate(2012, 4, 17), 5);

		assertEquals(expected, parser.parse(block));
	}



	@Test
	public void testFindLineType() throws Exception {
		assertLineType(LINE_WITHOUT_TAG, null);
		assertLineType(LINE_WITHOUT_TAG, "");
		assertLineType(LINE_WITHOUT_TAG, "  ");
		assertLineType(LINE_20, ":20:");
		assertLineType(LINE_25, ":25:");
	}

	@Test
	public void testIsHeaderLine() throws Exception {
		assertEquals(true, parser.isHeaderLine(LINE_WITHOUT_TAG));
		assertEquals(true, parser.isHeaderLine(LINE_20));
		assertEquals(true, parser.isHeaderLine(LINE_25));
		assertEquals(true, parser.isHeaderLine(LINE_28C));
		assertEquals(true, parser.isHeaderLine(LINE_60F));
		assertEquals(true, parser.isHeaderLine(LINE_60M));
		assertEquals(false, parser.isHeaderLine(LINE_61));
		assertEquals(false, parser.isHeaderLine(LINE_62F));
		assertEquals(false, parser.isHeaderLine(LINE_62M));
		assertEquals(false, parser.isHeaderLine(LINE_64));
		assertEquals(false, parser.isHeaderLine(LINE_86));
	}



	private void assertLineType(int expectedType, String line) {
		assertEquals(expectedType, SwiftBlocksParser.findLineType(line));
	}
}
