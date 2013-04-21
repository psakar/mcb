package com.chare.mcb.www;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.chare.entity.EntityWithId;
import com.chare.repository.ListAndCountRepository;
import com.chare.repository.Restriction;
import com.chare.repository.Sort;

//FIXME saki - move to library.wicket
public class RepositorySortableDataProvider<Type extends EntityWithId<?>> extends SortableDataProvider<Type> {

	private final List<Sort> sorts;
	private final List<Restriction> restrictions;
	private final ListAndCountRepository<Type> repository;

	public RepositorySortableDataProvider(ListAndCountRepository<Type> repository) {
		this.repository = repository;
		this.restrictions = new ArrayList<Restriction>();
		this.sorts = new ArrayList<Sort>();
	}

	public RepositorySortableDataProvider(String defaultSortColumn, SortOrder sortOrder, ListAndCountRepository<Type> repository) {
		this(repository);
		setSort(defaultSortColumn, sortOrder);
	}

	public RepositorySortableDataProvider(String defaultSortColumn, ListAndCountRepository<Type> repository) {
		this(defaultSortColumn, SortOrder.ASCENDING, repository);
	}

	public void clearRestrictions() {
		restrictions.clear();
	}

	public void clearRestriction(String name) {
		restrictions.remove(name);

	}

	public void addRestriction(Restriction restriction) {
		restrictions.add(restriction);
	}

	public void addSort(Sort sort) {
		sorts.add(sort);
	}

	@Override
	public Iterator<Type> iterator(int first, int count) {
		SortParam param = getSort();
		List<Sort> sorts = new ArrayList<Sort>();
		sorts.addAll(this.sorts);
		if (param != null)
			sorts.add(new Sort(param.getProperty(), (param.isAscending() ? Sort.UP : Sort.DOWN)));
		return repository.find(first, count, restrictions, sorts).iterator();
	}

	@Override
	public int size() {
		return repository.getCount(restrictions);
	}

	@Override
	public IModel<Type> model(Type object) {
		return new Model<Type>(object);
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public List<Sort> getSorts() {
		return sorts;
	}

}
