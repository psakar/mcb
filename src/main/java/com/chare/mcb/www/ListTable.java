/**
 *
 */
package com.chare.mcb.www;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;

import com.chare.entity.EntityWithId;

public class ListTable<IdType, Type extends EntityWithId<IdType>> extends WebMarkupContainer {

	public static final String ROWS_ID = "rows";

	private final DataView<Type> dataView;
	private final SortableDataProvider<Type> dataProvider;

	public ListTable(String id, SortableDataProvider<Type> dataProvider) {
		super(id);
		this.dataProvider = dataProvider;
		this.dataView = createDataView(dataProvider);
		setOutputMarkupId(true);
		add(dataView);
	}

	public void setHighligt(Behavior behavior) {
		if (behavior != null)
			add(behavior);
	}

	public final DataView<Type> getDataView() {
		return dataView;
	}

	public final SortableDataProvider<Type> getDataProvider() {
		return dataProvider;
	}

	public final DataView<Type> createDataView(SortableDataProvider<Type> dataProvider) {
		DataView<Type> dataView = new DataView<Type>(ROWS_ID, dataProvider) {
			@Override
			protected void populateItem(final Item<Type> item) {
				ListTable.this.populateRow(item);
			}
		};
		dataView.setOutputMarkupId(true);
		return dataView;
	}

	protected void populateRow(final Item<Type> item) {
		populateItem(item, item.getModelObject());
		modifyRowClass(item);
	}

	protected void populateItem(Item<Type> item, Type entity) {}

	protected void modifyRowClass(final Item<Type> item) {
		String classValue = (item.getIndex() % 2 == 1) ? "even" : "odd";
		item.add(new AttributeModifier("class", new Model<String>(classValue)));
	}


}