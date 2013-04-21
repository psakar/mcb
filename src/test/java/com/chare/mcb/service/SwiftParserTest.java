package com.chare.mcb.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.chare.payment.service.BlockOfLines;

public class SwiftParserTest {




	private static final String BLOCK_START_LINE = "{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:";
	private SwiftParser parser;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		parser = new SwiftParser();
	}



	@Test
	public void testParsingEmptyList() throws Exception {
		List<String> lines = Arrays.asList();
		List<BlockOfLines> expectedBlocks = Arrays.asList();

		assertLinesAreParsedToBlocks(expectedBlocks, lines);
	}
	@Test
	public void testParsingOneBlock() throws Exception {
		List<String> lines = Arrays.asList(BLOCK_START_LINE, "line1", "-}");
		List<BlockOfLines> expectedBlocks = Arrays.asList(new BlockOfLines(1, BlockOfLines.TYPE_BODY, lines));

		assertLinesAreParsedToBlocks(expectedBlocks, lines);
	}

	@Test
	public void testParsingMultipleBlocks() throws Exception {
		List<String> lines = Arrays.asList(BLOCK_START_LINE, "line1", "-}${1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:", "line2", "-}");
		BlockOfLines blockOfLines1 = new BlockOfLines(1, BlockOfLines.TYPE_BODY, Arrays.asList(BLOCK_START_LINE, "line1", SwiftParser.BLOCK_END));
		BlockOfLines blockOfLines2 = new BlockOfLines(3, BlockOfLines.TYPE_BODY, Arrays.asList("{1:F01UNCRSKBXAXXX0000000000}{2:I940UNCRSKBXAXXXN}{4:", "line2", SwiftParser.BLOCK_END));
		List<BlockOfLines> expectedBlocks = Arrays.asList(blockOfLines1, blockOfLines2);

		assertLinesAreParsedToBlocks(expectedBlocks, lines);
	}

	private void assertLinesAreParsedToBlocks(List<BlockOfLines> expectedBlocks, List<String> lines) throws Exception {
		assertArrayEquals(expectedBlocks.toArray(), parser.parse(lines).toArray());
	}

}
