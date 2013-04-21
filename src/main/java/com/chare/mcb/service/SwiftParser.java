package com.chare.mcb.service;

import java.util.List;

import com.chare.payment.ForeignPaymentItemDefinition;
import com.chare.payment.service.BlockOfLines;
import com.chare.payment.service.ForeignBlockParser;

class SwiftParser {
	static final String BLOCK_START = ForeignPaymentItemDefinition.BLOCK_START;
	static final String BLOCK_END = ForeignPaymentItemDefinition.BLOCK_END;
	static final String BLOCK_SEPARATOR = ForeignPaymentItemDefinition.BLOCK_SEPARATOR;
	private final ForeignBlockParser parser;

	SwiftParser() {
		parser = new ForeignBlockParser();
	}

	public List<BlockOfLines> parse(List<String> lines) {
		List<BlockOfLines> blocks = parser.parse(lines);
		return removeFirstOptionalHeaderBlock(blocks);
	}

	private List<BlockOfLines> removeFirstOptionalHeaderBlock(List<BlockOfLines> blocks) {
		if (blocks.size() > 0 && blocks.get(0).type == BlockOfLines.TYPE_HEADER)
			blocks.remove(0);
		return blocks;
	}

}
