package com.chare.mcb.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.CardStatementLine;
import com.chare.mcb.service.CardStatementGeneratorImpl.CardStatementLinesMerger;

public class CardStatementLinesMergerTest {


	private CardStatementLinesMerger merger;

	@Mock
	private CardStatementLine line1;

	@Mock
	private CardStatementLine line2;

	private List<CardStatementLine> lines;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		merger = new CardStatementLinesMerger();
		lines = new ArrayList<CardStatementLine>();
		lines.add(line1);
		lines.add(line2);
	}

	@Test
	public void testLineNotRelatedIsNotMerged() throws Exception {

		List<CardStatementLine> result = merger.convertToLinesWithFees(lines);

		assertArrayEquals(lines.toArray(), result.toArray());
	}


	@Test
	public void testLineWithRelatedFeeIsMerged() throws Exception {
		setupLine2AsRelatedFeeToLine1();

		List<CardStatementLine> result = merger.convertToLinesWithFees(lines);

		assertLineRelatedToOtherWasMerged(result, line1, line2);
	}

	private void setupLine2AsRelatedFeeToLine1() {
		when(line2.isRelatedFeeLine()).thenReturn(true);
		when(line2.isRelatedFeeLineOfLine(line1)).thenReturn(true);
	}



	private void assertLineRelatedToOtherWasMerged(List<CardStatementLine> result, CardStatementLine line, CardStatementLine feeLine) {
		assertEquals(1, result.size());

		assertEquals(line, result.get(0));
		verify(line).setRelatedLineWithFee(feeLine);
	}


	@Test
	public void testLineWithRelatedFeeIsMergedWhenLinesOrderIsReversed() throws Exception {
		setupLine1AsRelatedFeeToLine2();

		List<CardStatementLine> result = merger.convertToLinesWithFees(lines);

		assertLineRelatedToOtherWasMerged(result, line2, line1);
	}

	private void setupLine1AsRelatedFeeToLine2() {
		when(line1.isRelatedFeeLine()).thenReturn(true);
		when(line1.isRelatedFeeLineOfLine(line2)).thenReturn(true);
	}

}
