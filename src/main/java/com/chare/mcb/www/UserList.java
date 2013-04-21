package com.chare.mcb.www;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.chare.mcb.entity.Role;
import com.chare.mcb.entity.User;
import com.chare.mcb.entity.UserItem;
import com.chare.mcb.repository.UserItemRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.repository.EntityRepository;
import com.chare.wicket.Label;

@Secured(required = { Role.USER_ADMIN }, requiredAnyOf = {  })
public class UserList extends PanelPage {

	public UserList() {
		super(new PageParameters());
		add(new UserListPanel(PANEL_ID));
	}

	@Override
	protected String getTitleCode() {
		return "users.title";
	}

	static class UserListPanel extends AbstractListPanel<Integer, UserItem> {

		@SpringBean
		private UserItemRepository repository;
		@SpringBean
		private UserRepository userRepository;

		public UserListPanel(String id) {
			super(id, CREATE_NAVIGATOR, HIDE_EXPORT_TO_CSV_LINK);
			addOrdering(getTable());
		}

		@Override
		public SortableDataProvider<UserItem> createDataProvider(IModel<List<UserItem>> listModel) {
			return new RepositorySortableDataProvider<UserItem>(repository);
		}

		private void addOrdering(ListTable<Integer, UserItem> table) {
			table.add(new BookmarkablePageLink<Void>("new", UserPage.class));
			ISortStateLocator dataProvider = table.getDataProvider();
			table.add(new OrderByBorder("username", "username", dataProvider));
			table.add(new OrderByBorder("fullname", "surname", dataProvider));
			table.add(new OrderByBorder("locale", "languageId", dataProvider));
			table.add(new OrderByBorder("unsuccessfulCount", "unsuccessfulCount", dataProvider));
			table.add(new OrderByBorder("enabled", "enabled", dataProvider));

		}

		@Override
		public void populateItem(Item<UserItem> item, UserItem entity) {
			Integer id = entity.getId();
			item.add(new BookmarkablePageLink<Void>("edit", UserPage.class, createEditPageParameters(id)));
			item.add(new Label("username", entity.username));
			item.add(new Label("fullname", entity.getFullname()));
			item.add(new Label("unsuccessfulCount", String.valueOf(entity.getUnsuccessfulCount())));
			item.add(new Label("enabled", new BooleanResourceModel(entity.enabled)));
			item.add(new Label("locale",  ChoiceValueDropDownChoice.findDescription(entity.languageId)));
			item.add(new ResetPasswordLink("resetPassword", id, userRepository));
			item.add(new RemoveEntityLink<Integer, User>("remove", id, userRepository, "errorDeleteUser"));
		}

		static class ResetPasswordLink extends EntityLink<Integer, User> {

			public ResetPasswordLink(String id, Integer entityId, EntityRepository<Integer, User> repository) {
				super(id, entityId, repository, "errorResetUserPassword");
			}

			@Override
			public void onClick(User entity) {
				entity.resetPassword();
				repository.persist(entity);
			}
		}

	}
}