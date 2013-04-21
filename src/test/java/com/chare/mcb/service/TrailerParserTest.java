package com.chare.mcb.service;

import static com.chare.mcb.service.SwiftBlocksParser.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.core.Utils;
import com.chare.mcb.service.SwiftBlocksParser.StatementTrailer;
import com.chare.mcb.service.SwiftBlocksParser.TrailerParser;
import com.chare.payment.service.BlockOfLines;

public class TrailerParserTest {




	private TrailerParser parser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		parser = new TrailerParser();
	}


	@Ignore //FIXME saki test if any of 62/64 lines found
	@Test(expected = IllegalArgumentException.class)
	public void testParsingEmptyList() throws Exception {
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_HEADER, new ArrayList<String>());

		parser.parse(block, 0);
	}

	@Test
	public void testParsingBlock() throws Exception {
		List<String> lines = Arrays.asList("test",
				":62F:C120418EUR84909,9",
				":64:C120418EUR75874,72",
				"-}"
				);
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_BODY, new ArrayList<String>(lines));

		StatementTrailer expected = new StatementTrailer(new BigDecimal("84909.90"), new BigDecimal("75874.72"), Utils.getDate(2012, 4, 18));

		assertEquals(expected, parser.parse(block, 1));
	}


	@Test
	public void testParsingBlockWithLine65() throws Exception {
		List<String> lines = Arrays.asList("test",
				":62F:C120418EUR84909,9",
				":64:C120418EUR75874,72",
				":65:D120419EUR1,23",
				"-}"
				);
		BlockOfLines block = new BlockOfLines(0, BlockOfLines.TYPE_BODY, new ArrayList<String>(lines));

		StatementTrailer expected = new StatementTrailer(new BigDecimal("84909.90"), new BigDecimal("75874.72"), Utils.getDate(2012, 4, 18));

		assertEquals(expected, parser.parse(block, 1));
	}

	@Test
	public void testFindLineType() throws Exception {
		assertLineType(LINE_WITHOUT_TAG, null);
		assertLineType(LINE_WITHOUT_TAG, "");
		assertLineType(LINE_WITHOUT_TAG, "  ");
		assertLineType(LINE_20, ":20:");
		assertLineType(LINE_25, ":25:");
	}

	private void assertLineType(int expectedType, String line) {
		assertEquals(expectedType, SwiftBlocksParser.findLineType(line));
	}
}
