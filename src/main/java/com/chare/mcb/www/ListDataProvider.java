package com.chare.mcb.www;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
//FIXME move to library.wickets and add test
public class ListDataProvider<Type extends Serializable> extends SortableDataProvider<Type> implements IDataProvider<Type> {

	private final IModel<List<Type>> listModel;

	public ListDataProvider(List<Type> list) {
		this.listModel = new ListModel<Type>(list);
	}
	public ListDataProvider(ListModel<Type> listModel) {
		this.listModel = listModel;
	}
	public ListDataProvider(IModel<List<Type>> listModel) {
		this.listModel = listModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Type> iterator(int first, int count) {
		List<Type> filtered = listModel.getObject();

		if (getSort() != null)
			Collections.sort(filtered, getComparator(getSort()));

		int fromIndex = (first >= 0 && first < filtered.size() ? first : 0);
		int toIndex = (fromIndex + count <= filtered.size() ? fromIndex + count : filtered.size());
		return new ArrayList<Type>(filtered).subList(fromIndex, toIndex).iterator();
	}

	private BeanComparator getComparator(SortParam sort) {
		if (!sort.isAscending())
			return new BeanComparator(sort.getProperty());
		else
			return new BeanComparator(sort.getProperty(), new ReverseComparator(new ComparableComparator()));
	}

	@Override
	public IModel<Type> model(Type object) {
		return new Model<Type>(object);
	}

	@Override
	public int size() {
		return listModel.getObject().size();
	}

}
