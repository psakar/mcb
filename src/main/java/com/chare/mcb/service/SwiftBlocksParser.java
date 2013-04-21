package com.chare.mcb.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.chare.core.Utils;
import com.chare.mcb.entity.Statement;
import com.chare.mcb.entity.StatementLine;
import com.chare.payment.service.BlockOfLines;

class SwiftBlocksParser {

	static final int LINE_WITHOUT_TAG = 0;

	static final int LINE_20 = 1;
	static final int LINE_25 = 2;
	static final int LINE_28C = 3;
	static final int LINE_60F = 4;
	static final int LINE_60M = 5;

	static final int LINE_61 = 6;
	static final int LINE_86 = 7;
	static final int LINE_62F = 8;
	static final int LINE_64 = 9;
	static final int LINE_65 = 10;

	static final int LINE_62M = 11;

	static final int LINE_BLOCK_END = 99;

	private final HeaderParser headerParser;

	private final TrailerParser trailerParser;

	private final BodyParser bodyParser;


	SwiftBlocksParser(HeaderParser headerParser, BodyParser bodyParser, TrailerParser trailerParser) {
		this.headerParser = headerParser;
		this.bodyParser = bodyParser;
		this.trailerParser = trailerParser;
	}

	public List<Statement> parse(List<BlockOfLines> blocks) {
		List<Statement> statements = new ArrayList<Statement>();

		Statement statement = null;
		StatementHeader lastHeader = null;
		for (BlockOfLines block : blocks) {
			StatementHeader header = headerParser.parse(block);
			if (!isSameStatement(lastHeader, header)) {
				if (statement != null) {
					if (statement.closingBalance == null) //FIXME test saki
						throw new IllegalArgumentException("Missing line :62F (closing balance)");

				}

				lastHeader = header;
				statement = new Statement();
				statement.number = normalizeNumber(header.number);
				statement.account = normalizeAccount(header.account);
				statement.openingBalance = header.openingBalance; //FIXME assert openning balance and statement date are filled in (number of statement is ?/1 and field :60F: exists
				statements.add(statement);
			}

			int lineIndex = header.countOfLines - 1;
			List<StatementLineBody> lines = bodyParser.parse(block, lineIndex);
			for (StatementLineBody statementLineBody : lines) {
				lineIndex = statementLineBody.lastLineNr;
				addLineToStatement(statement, statementLineBody);
			}

			StatementTrailer trailer = trailerParser.parse(block, lineIndex);
			statement.closingBalance = trailer.closingBalance;
			statement.statementDate = trailer.closingBalanceDate;
		}
		return statements ;
	}

	private void addLineToStatement(Statement statement,
			StatementLineBody lineBody) {
		StatementLine line = statement.addLine();
		line.valueDate = lineBody.valueDate;
		line.amount = lineBody.amount;
		line.swiftType = lineBody.swiftType;
		line.reference1 = lineBody.reference1;
		line.reference2 = lineBody.reference2;
		line.details1 = lineBody.details1;
		line.details2 = lineBody.details2;
		line.details3 = lineBody.details3;
		line.details4 = lineBody.details4;
	}

	private boolean isSameStatement(StatementHeader lastHeader,
			StatementHeader header) {
		return lastHeader != null && Utils.isEqual(normalizeNumber(lastHeader.number), normalizeNumber(header.number));
	}

	String normalizeNumber(String number) {
		String separator = "/";
		return number == null || !number.contains(separator) ? number : number.substring(0, number.indexOf(separator));
	}

	String normalizeAccount(String account) {
		String separator = "/";
		return account == null || !account.contains(separator) ? account : account.substring(account.indexOf(separator) + 1);
	}

	static int findLineType(String line) {
		if (line == null) return 0;
		if (line.startsWith(":20:")) return LINE_20;
		if (line.startsWith(":25:")) return LINE_25;
		if (line.startsWith(":28C:")) return LINE_28C;
		if (line.startsWith(":60F:")) return LINE_60F;
		if (line.startsWith(":61:")) return LINE_61;
		if (line.startsWith(":86:")) return LINE_86;
		if (line.startsWith(":62F:")) return LINE_62F;

		if (line.startsWith(":64:")) return LINE_64;
		if (line.startsWith(":65:")) return LINE_65;
		if (line.startsWith(":62M:")) return LINE_62M;
		if (line.startsWith(":60M:")) return LINE_60M;
		if (line.startsWith(SwiftParser.BLOCK_END)) return LINE_BLOCK_END;
		return LINE_WITHOUT_TAG;
	}

	static class StatementHeader {
		public final String bank;
		public final String account;
		public final String number;
		public final BigDecimal openingBalance;
		public final BigDecimal openingBalanceM;
		public final Date statementDate;
		public final int countOfLines;

		public StatementHeader(String bank, String account, String number, BigDecimal openingBalance, BigDecimal openingBalanceM, Date statementDate, int countOfLines) {
			this.bank = bank;
			this.account = account;
			this.number = number;
			this.openingBalance = openingBalance;
			this.openingBalanceM = openingBalanceM;
			this.statementDate = statementDate;
			this.countOfLines = countOfLines;
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof StatementHeader))
				return false;
			StatementHeader other = (StatementHeader) obj;
			return toString().compareTo(other.toString()) == 0;
		}
	}

	static class Parser {
		protected Date parseDate(String content) {
			try {
				return new SimpleDateFormat("yyMMdd").parse(content);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Wrong date format " + e.getMessage(), e);
			}
		}

		protected BigDecimal parseBalance(String line) {
			return new BigDecimal(line.substring(10).replace(",", ".")).setScale(2, RoundingMode.HALF_EVEN);//FIXME C/D
		}

		protected BigDecimal parseAmount(String line) {
			int beginIndex = 11;
			int endIndex = findNumberEnd(line.substring(beginIndex));
			BigDecimal amount = new BigDecimal(line.substring(beginIndex, beginIndex + endIndex).replace(",", ".")).setScale(2, RoundingMode.HALF_EVEN);//FIXME C/D
			if ( line.charAt(beginIndex - 1) == 'D')
				return amount.negate();
			return amount;
		}

		protected int findNumberEnd(String string) {
			int index = -1;
			for (char c : string.toCharArray()) {
				index ++;
				if (! (c == ',' || c == '.' || ( (c >= '0') && (c<='9'))))
					return index;
			}
			return index;
		}
	}

	static class HeaderParser extends Parser {

		public StatementHeader parse(BlockOfLines block) {
			String bank = null;
			String account = null;
			String number = null;
			BigDecimal openingBalance = null;
			BigDecimal openingBalanceM = null;
			Date statementDate = null;
			int count = block.lines.size();
			int lineNr = 0;
			while (lineNr < count) {
				String line = block.lines.get(lineNr);
				lineNr++;

				if (lineNr == 1) {
					assertBlockStart(line);
					continue;
				}
				if (line.length()==0)
					continue;

				int type = findLineType(line);
				if (!isHeaderLine(type))
					break;

				switch (type) {
					case LINE_20 :
						bank = line.substring(4);
						break;
					case LINE_25 :
						account = line.substring(4);
						break;
					case LINE_28C :
						number = line.substring(5);
						break;
					case LINE_60F :
						String content = line.substring(5);
						openingBalance = parseBalance(content);
						statementDate = parseDate(content.substring(1, 7));
						break;
					case LINE_60M :
						openingBalanceM = parseBalance(line.substring(5));
						break;
					default :
						throw new IllegalArgumentException("Unexpected type of line - " + line);
				}
			}
			if (bank == null)
				throw new IllegalArgumentException("Missing line :20 (bank)");
			if (account == null)
				throw new IllegalArgumentException("Missing line :25 (account)");
			if (number == null)
				throw new IllegalArgumentException("Missing line :28C (number)");
			return new StatementHeader(bank, account, number, openingBalance, openingBalanceM, statementDate, lineNr);
		}

		private void assertBlockStart(String line) {
			if (! (line!=null && line.startsWith(SwiftParser.BLOCK_START)))
				throw new IllegalArgumentException("Block is not starting with " + SwiftParser.BLOCK_START);
		}

		boolean isHeaderLine(int type) {
			return type <= LINE_60M;
		}


	}

	static class StatementTrailer {
		public final BigDecimal closingBalance;
		public final BigDecimal closingAvailableBalance;
		public final Date closingBalanceDate;

		public StatementTrailer(BigDecimal closingBalance, BigDecimal closingAvailableBalance, Date closingBalanceDate) {
			this.closingBalance = closingBalance;
			this.closingAvailableBalance = closingAvailableBalance;
			this.closingBalanceDate = closingBalanceDate;
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof StatementTrailer))
				return false;
			StatementTrailer other = (StatementTrailer) obj;
			return toString().compareTo(other.toString()) == 0;
		}
	}


	static class TrailerParser extends Parser {

		public StatementTrailer parse(BlockOfLines block, int fromLineNr) {
			BigDecimal closingBalance = null;
			BigDecimal closingAvailableBalance = null;
			Date closingBalanceDate = null;
			int count = block.lines.size();
			for (int lineNr = fromLineNr; lineNr < count; ) {
				String line = block.lines.get(lineNr);
				lineNr++;
				int type = findLineType(line);
				if (type == LINE_BLOCK_END)
					break;
				switch (type) {
					case LINE_62F : {
						String content = line.substring(5);
						closingBalance = parseBalance(content);
						closingBalanceDate = parseDate(content.substring(1, 7));
						break;
					}
					case LINE_62M :
						break;
					case LINE_65 :
						break;
					case LINE_64 : {
						String content = line.substring(4);
						closingAvailableBalance = parseBalance(content);
						break;
					}
					default :
						throw new IllegalArgumentException("Unexpected type of line - " + line);
				}
			}
			//			if (closingBalance == null || closingBalanceDate == null)
			//				throw new IllegalArgumentException("Missing line :62F (closing balance)");
			return new StatementTrailer(closingBalance, closingAvailableBalance, closingBalanceDate);
		}
	}

	static class StatementLineBody {
		public int lastLineNr;
		public Date valueDate;
		public BigDecimal amount;
		public String swiftType;
		public String reference1;
		public String reference2;
		public String details1;
		public String details2;
		public String details3;
		public String details4;


		public StatementLineBody(Date valueDate, BigDecimal amount, String swiftType, String reference1, String reference2, String details1, String details2, String details3, String details4, int lastLineNr) {
			this.valueDate = valueDate;
			this.amount = amount;
			this.swiftType = swiftType;
			this.reference1 = reference1;
			this.reference2 = reference2;
			this.details1 = details1;
			this.details2 = details2;
			this.details3 = details3;
			this.details4 = details4;
			this.lastLineNr = lastLineNr;
		}

		public StatementLineBody() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof StatementLineBody))
				return false;
			StatementLineBody other = (StatementLineBody) obj;
			return toString().compareTo(other.toString()) == 0;
		}

		public void assertDetailsLength() {
			String oversize1 = getOversize(details1);
			if (oversize1 != null) {
				details1 = details1.substring(0, 35);
				details2 = oversize1 + StringUtils.defaultString(details2);
			}
			String oversize2 = getOversize(details2);
			if (oversize2 != null) {
				details2 = details2.substring(0, 35);
				details3 = oversize2 + StringUtils.defaultString(details3);
			}
			String oversize3 = getOversize(details3);
			if (oversize3 != null) {
				details3 = details3.substring(0, 35);
				details4 = oversize3 + StringUtils.defaultString(details4);
			}
		}

		private String getOversize(String details) {
			return details != null && details.length() > 35 ? details.substring(35) : null;
		}
	}

	static class BodyParser extends Parser {

		public List<StatementLineBody> parse(BlockOfLines block, int startLineIndex) {
			List<StatementLineBody> lines = new ArrayList<StatementLineBody>();

			StatementLineBody statementLine = null;

			int count = block.lines.size();
			int lastType = LINE_WITHOUT_TAG;
			int lastTypeLineNr = 0;
			int lineIndex = startLineIndex;
			while (lineIndex < count) {
				String line = block.lines.get(lineIndex);
				lineIndex++;
				int type = findLineType(line);
				if (isTrailerLine(type))
					break;

				switch (type) {
					case LINE_61 : {
						if (statementLine != null) {
							statementLine.lastLineNr = lineIndex - 2;
							statementLine.assertDetailsLength();
							lines.add(statementLine);
						}
						statementLine = new StatementLineBody();
						String content = line.substring(4);
						statementLine.amount = parseAmount(content);
						statementLine.valueDate = parseDate(content.substring(0, 6));
						int numberEnd = findNumberEnd(content.substring(11)) + 11;
						statementLine.swiftType = content.substring(numberEnd, numberEnd + 4);
						statementLine.reference1 = content.substring(numberEnd + 4);
						lastType = type;
						lastTypeLineNr = 0;
						break;
					}
					case LINE_86 : {
						lastType = type;
						statementLine.details1 = line.substring(4);
						break;
					}
					case LINE_WITHOUT_TAG : {
						String content = org.apache.commons.lang.StringUtils.stripStart(line, " ");
						if (lastType == LINE_61)
							statementLine.reference2 = content;
						if (lastType == LINE_86) {
							if (lastTypeLineNr == 0)
								statementLine.details2 = content;
							if (lastTypeLineNr == 1)
								statementLine.details3 = content;
							if (lastTypeLineNr == 2)
								statementLine.details4 = content;
							lastTypeLineNr++;
						}
						break;
					}
					default :
						throw new IllegalArgumentException("Unexpected type of line - " + line);
				}
			}
			//			if (closingBalance == null || closingBalanceDate == null)
			//				throw new IllegalArgumentException("Missing line :62F (closing balance)");
			if (statementLine != null) {
				statementLine.lastLineNr = lineIndex - 1;
				statementLine.assertDetailsLength();
				lines.add(statementLine);
			}
			return lines;
		}
		boolean isTrailerLine(int type) {
			return type == LINE_62F || type == LINE_62M || type == LINE_64;
		}
	}

}
