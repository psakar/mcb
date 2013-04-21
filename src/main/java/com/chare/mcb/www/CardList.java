package com.chare.mcb.www;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.CardItem;
import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.StatementPeriod;
import com.chare.mcb.repository.CardItemRepository;
import com.chare.mcb.repository.CardRepository;
import com.chare.wicket.Label;

@Secured(required = { }, requiredAnyOf = { Role.EXPORT_POSTINGS, Role.UPLOAD_STATEMENTS })
public class CardList extends PanelPage {

	public CardList() {
		super(new PageParameters());
		add(new CardListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "cards.title";
	}

	static class CardListPanel extends AbstractListPanel<Integer, CardItem> {

		@SpringBean
		private CardItemRepository repository;

		@SpringBean
		private CardRepository entityRepository;

		public CardListPanel(String id) {
			super(id, CREATE_NAVIGATOR, SHOW_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<CardItem> createDataProvider(IModel<List<CardItem>> listModel) {
			return new RepositorySortableDataProvider <CardItem>("number", repository);
		}

		private void addOrdering(ListTable<Integer, CardItem> table) {
			ISortStateLocator dataProvider = table.getDataProvider();
			table.add(new BookmarkablePageLink<Void>("new", CardPage.class));
			table.add(new OrderByBorder("settlementAccount", "settlementAccount", dataProvider));
			table.add(new OrderByBorder("number", "number", dataProvider));
			table.add(new OrderByBorder("cardType", "cardType.description.description" + getLanguageIndex(), dataProvider));
			table.add(new OrderByBorder("holderName", "holderName", dataProvider));

			table.add(new OrderByBorder("email", "email", dataProvider));
			table.add(new OrderByBorder("phone", "phone", dataProvider));
			table.add(new OrderByBorder("addressTitle", "address.title", dataProvider));
			table.add(new OrderByBorder("addressNameName2", "address.name", dataProvider));
			table.add(new OrderByBorder("addressStreet", "address.street", dataProvider));
			table.add(new OrderByBorder("addressZipTown", "address.zip", dataProvider));
			table.add(new OrderByBorder("addressCountry", "address.country", dataProvider));
			table.add(new OrderByBorder("activeFrom", "activeFrom", dataProvider));
			table.add(new OrderByBorder("activeTo", "activeTo", dataProvider));
			table.add(new OrderByBorder("statementPeriod", "statementPeriod", dataProvider));
			table.add(new OrderByBorder("lastStatementNr", "lastStatementNr", dataProvider));

			table.add(new OrderByBorder("language", "languageId", dataProvider));
			table.add(new OrderByBorder("limit", "limit", dataProvider));
		}

		@Override
		public void populateItem(Item<CardItem> item, CardItem entity) {
			Integer id = entity.getId();
			item.add(new BookmarkablePageLink<Void>("edit", CardPage.class, createEditPageParameters(id)));
			item.add(new Label("number", entity.number));
			item.add(new Label("cardType", entity.cardType.description.getDescription(getLanguageIndex())));
			item.add(new Label("holderName", entity.holderName));

			item.add(new Label("email", entity.email));
			item.add(new Label("phone", entity.phone));

			item.add(new Label("addressTitle", StringUtils.defaultString(entity.address.title)));
			item.add(new Label("addressNameName2", entity.address.getFullName()));
			item.add(new Label("addressStreet", entity.address.street));
			item.add(new Label("addressZipTown", entity.address.zip + " " + entity.address.town));
			item.add(new Label("addressCountry", StringUtils.defaultString(entity.address.country)));
			item.add(new Label("activeFrom", formatDate(entity.activeFrom)));
			item.add(new Label("activeTo", formatDate(entity.activeTo)));
			item.add(new Label("settlementAccount", entity.settlementAccount));
			item.add(new Label("statementPeriod",  getStatementPeriodDescription(entity.statementPeriod)));
			item.add(new Label("lastStatementNr", entity.lastStatementNr == null ? "" : entity.lastStatementNr + ""));

			item.add(new Label("language", ChoiceValueDropDownChoice.findDescription(entity.languageId)));
			item.add(new Label("limit", formatCurrencyNumber(entity.limit)));

			/*
	private Date activeFrom;
	public Date activeTo;
	public String settlementAccount;
	public StatementPeriod statementPeriod;
			 */
			//			item.add(new Label("fullname", entity.getFullname()));
			//			item.add(new Label("unsuccessfulCount", String.valueOf(entity.getUnsuccessfulCount())));
			//			item.add(new Label("enabled", new BooleanResourceModel(entity.enabled)));
			//			item.add(new Label("locale",  ChoiceValueDropDownChoice.findDescription(entity.languageId, LanguageChoice.CHOICES)));
			item.add(new RemoveEntityLink<Integer, Card>("remove", id, entityRepository, "errorDeleteCard"));
		}

		@Override
		protected String createCsvHeader(String separator) {
			StringBuffer sb = new StringBuffer();
			sb.append("id");

			sb.append(separator);
			sb.append("number");

			sb.append(separator);
			sb.append("cardTypeCode");

			sb.append(separator);
			sb.append("cardType");

			sb.append(separator);
			sb.append("holderName");

			sb.append(separator);
			sb.append("email");

			sb.append(separator);
			sb.append("phone");

			sb.append(separator);
			sb.append("addressTitle");

			sb.append(separator);
			sb.append("addressName");

			sb.append(separator);
			sb.append("addressName2");

			sb.append(separator);
			sb.append("addressStreet");

			sb.append(separator);
			sb.append("addressTown");

			sb.append(separator);
			sb.append("addressZip");

			sb.append(separator);
			sb.append("addressCountry");

			sb.append(separator);
			sb.append("activeFrom");

			sb.append(separator);
			sb.append("activeTo");

			sb.append(separator);
			sb.append("settlementAccount");

			sb.append(separator);
			sb.append("statementPeriod");

			sb.append(separator);
			sb.append("lastStatementNr");

			sb.append(separator);
			sb.append("language");

			sb.append(separator);
			sb.append("limit");

			sb.append(separator);
			return sb.toString();
		}


		@Override
		protected String exportToCsv(CardItem entity, String separator) {
			StringBuffer sb = new StringBuffer();
			sb.append(formatCsvInteger(entity.getId()));

			sb.append(separator);
			sb.append(formatCsvText(entity.number));

			sb.append(separator);
			sb.append(formatCsvText(entity.cardType.code));

			sb.append(separator);
			sb.append(formatCsvText(entity.cardType.description.getDescription(getLanguageIndex())));

			sb.append(separator);
			sb.append(formatCsvText(entity.holderName));

			sb.append(separator);
			sb.append(formatCsvText(entity.email));

			sb.append(separator);
			sb.append(formatCsvText(entity.phone));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.title));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.name));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.name2));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.street));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.town));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.zip));

			sb.append(separator);
			sb.append(formatCsvText(entity.address.country));

			sb.append(separator);
			sb.append(formatCsvDate(entity.activeFrom));

			sb.append(separator);
			sb.append(formatCsvDate(entity.activeTo));

			sb.append(separator);
			sb.append(formatCsvText(entity.settlementAccount));

			sb.append(separator);
			sb.append(formatCsvText(getStatementPeriodDescription(entity.statementPeriod).getObject()));

			sb.append(separator);
			sb.append(formatCsvInteger(entity.lastStatementNr));

			sb.append(separator);
			sb.append(formatCsvText(ChoiceValueDropDownChoice.findDescription(entity.languageId)));

			sb.append(separator);
			sb.append(formatCsvNumber(entity.limit));

			return sb.toString();
		}
	}

	static IModel<String> getStatementPeriodDescription(StatementPeriod statementPeriod) {
		return new ResourceModel("statementPeriod." + statementPeriod.ordinal());
	}

}