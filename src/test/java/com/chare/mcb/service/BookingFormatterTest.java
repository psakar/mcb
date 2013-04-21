package com.chare.mcb.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.Utils;
import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;

public class BookingFormatterTest {

	private BookingFormatter settlementWriter;

	@Before
	public void setUp() {
		settlementWriter = new BookingFormatter();
	}

	@Test
	public void testWrittenSettlement() throws Exception {
		PostingFile postingFile = new PostingFile();
		Booking settlement = postingFile.addBooking();
		setupSettlement(settlement);
		String actual = settlementWriter.format(settlement);

		StringBuffer buffer = new StringBuffer();
		buffer.append("090101");
		buffer.append("SO");
		buffer.append(Booking.BRANCH);
		buffer.append("00002");
		buffer.append("2");
		buffer.append("000000000001.00");
		buffer.append("CZK");
		buffer.append("drAccount      ");
		buffer.append("crAccount      ");
		buffer.append("090103");
		buffer.append("090102");
		buffer.append("   ");
		buffer.append("090101");
		buffer.append("trReference     ");
		buffer.append("                ");
		buffer.append("details                                                                                                                                     ");
		buffer.append("bankToBank                                                                                                                                                                                                        ");
		buffer.append("0000");
		buffer.append("0000");
		buffer.append("090101_00000000");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("CZK");
		buffer.append("000000000001.00");
		buffer.append("                ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("                                   ");
		buffer.append("          ");
		buffer.append("                                                                           ");
		buffer.append("                                                                                                    ");
		buffer.append("                                                                                                    ");
		buffer.append("                                                                                                    ");
		buffer.append("                                                                                                    ");
		buffer.append("\r\n");

		assertEquals(buffer.toString(), actual);
	}

	public static Booking setupSettlement(Booking settlement) {
		settlement.amount = new BigDecimal("1.0");
		settlement.bankToBankInfo = "bankToBank";
		settlement.businessDate = Utils.getDate(2009, 1, 1);
		settlement.creditAccount = "crAccount";
		settlement.creditValueDate = Utils.getDate(2009, 1, 2);
		settlement.currency = "CZK";
		settlement.debitValueDate = Utils.getDate(2009, 1, 3);
		settlement.details = "details";
		settlement.debitAccount= "drAccount";
		settlement.orderingBankAddress= "orderingBankAddress";
		settlement.orderingBankName= "orderingBankName";
		settlement.sequenceNr = 2;
		settlement.source= "SO";
		settlement.trReference= "trReference";
		return settlement;
	}


	@Test
	public void testCharactersAreReplaced() throws Exception {
		String input = "ÁČŇň";
		assertEquals("acnn", settlementWriter.formatString(input, 4, ' '));
	}

	@Test
	public void testGermanyCharactersAreReplaced() throws Exception {
		String input = "ÄÜÖäüö";
		assertEquals("auoauo", settlementWriter.formatString(input, 6, ' '));
	}

	@Test
	public void testWrongCharactersAreReplacedBySpace() throws Exception {
		String input = "! abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-.()[]ěščřžýáíé";
		assertEquals("  abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-.    escrzyaie", settlementWriter.formatString(input, 70, ' '));
	}
}
