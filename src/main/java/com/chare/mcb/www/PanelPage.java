package com.chare.mcb.www;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.chare.entity.EntityWithId;
import com.chare.repository.EntityFactory;
import com.chare.repository.EntityRepository;
import com.chare.repository.LoaderRepository;
import com.chare.repository.PersistRepository;
import com.chare.wicket.panel.Panel;
import com.chare.wicket.panel.TableLayoutManager;

public class PanelPage extends Page {

	public static final String PANEL_ID = "panel";
	protected final static String ID_PARAM = "id";
	static final String FORM_ID = "form";

	public PanelPage(PageParameters pageParameters) {
		super(pageParameters);
		add(createTitleLabel(createTitleModel()));
	}

	protected Label createTitleLabel(IModel<String> titleModel) {
		return new Label("bodyTitle", titleModel);
	}

	protected Panel<Void> createDefaultPanel() {
		return new Panel<Void>(PANEL_ID, null, new TableLayoutManager(1).setWidth(800).setBorder(0));
	}

	protected String toIdString(StringValue parameter) {
		return parameter.toOptionalString();
	}

	protected Integer toIdInteger(StringValue parameter) {
		return parameter.toOptionalInteger();
	}

	static <Type> PageParameters createEditPageParameters(Type id) {
		PageParameters parameters = new PageParameters();
		parameters.set(PanelPage.ID_PARAM, String.valueOf(id));
		return parameters;
	}

	static class ModelFactory<IdType, Type extends EntityWithId<IdType>> {

		public IModel<Type> createModel(IdType id, LoaderRepository<IdType, Type> repository, EntityFactory<Type> factory) {
			Type entity = null;
			if (id != null)
				entity = repository.findById(id);
			if (entity == null)
				entity = factory.create();
			return new Model<Type>(entity);
		}
	}

	static class Factory<Type> implements EntityFactory<Type> {
		private final Class<Type> clazz;

		public Factory(Class<Type> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Type create() {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new IllegalStateException("Can not create instance of " + clazz.getName(), e);
			}
		}
	};

	static abstract class EntityLink<IdType, Type extends EntityWithId<IdType>> extends org.apache.wicket.markup.html.link.Link<Void> {

		protected EntityRepository<IdType, Type> repository;
		protected final IdType entityId;
		private final String error;

		public EntityLink(String id, IdType entityId, EntityRepository<IdType, Type> repository, String error) {
			super(id);
			this.entityId = entityId;
			this.repository = repository;
			this.error = error;
		}

		@Override
		public void onClick() {
			try {
				Type entity = repository.findById(entityId);
				onClick(entity);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Error " + error + e.getMessage(), e);
				error(error);
			}
		}

		protected abstract void onClick(Type entity);
	}

	static class RemoveEntityLink<IdType, Type extends EntityWithId<IdType>> extends EntityLink<IdType, Type> {

		public RemoveEntityLink(String id, IdType entityId, EntityRepository<IdType, Type> repository, String error) {
			super(id, entityId, repository, error);
			ResourceModel confirmationModel = new ResourceModel("confirmDelete", "Zrušit smazání ? (Pokud opravdu chcete smazat záznam klikněte na tlačítko 'Zrušit'");
			add(new AttributeModifier("onClick", "return !confirm('" + StringEscapeUtils.escapeJavaScript(confirmationModel .getObject()) + "');"));
			//http://jqueryui.com/demos/dialog/#modal-confirmation
		}
		@Override
		protected void onClick(Type entity) {
			repository.delete(entity);
		}

	}

	static class EntityForm<Type extends EntityWithId<?>> extends Form<Type> {

		private final PersistRepository<Type> repository;

		public EntityForm(String id, IModel<Type> model, PersistRepository<Type> repository) {
			super(id, model);
			this.repository = repository;
		}

		@Override
		protected void onSubmit() {
			try {
				@SuppressWarnings("unchecked")
				IModel<Type> model = (IModel<Type>) EntityForm.this.getDefaultModel();
				Type entity = model.getObject();
				boolean isNew = !entity.isPersistent();
				model.setObject(persist(entity));
				afterPersist(isNew, entity);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error("Save error " + e.getMessage(), e);
				error(new ResourceModel("error").getObject() + e.getMessage());//FIXME "Error ({$1} {$2} {$3})_#2000;", new String[] { e.getMessage() });
			}
		}

		protected Type persist(Type entity) {
			return repository.persist(entity);
		};
		protected void afterPersist(boolean wasInserted, Type entity) {
		}
	};

}
