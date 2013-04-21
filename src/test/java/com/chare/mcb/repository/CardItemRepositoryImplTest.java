package com.chare.mcb.repository;

import com.chare.mcb.ListAndCountRepositoryTestCase;
import com.chare.mcb.entity.CardItem;
import com.chare.mcb.entity.EntityWithIdInteger;

public class CardItemRepositoryImplTest extends ListAndCountRepositoryTestCase<CardItem, CardItemRepository> {

	@Override
	protected String getTableName() {
		return EntityWithIdInteger.CARD_TABLE;
	}


	@Override
	protected CardItemRepository getRepository() {
		return new CardItemRepositoryImpl(entityManager);
	}



	//SELECT new CustInfo(c.name,	a) FROM Customer c JOIN c.address a
	/* FIXME Implement ListAndCountRepositoryImpl using named query
TypedQuery<Employee> q = em.createNamedQuery(“Employee.findByName”,
Employee.class);
q.setParameter(“empName”, “Smith”);
List<Employee> emps = q.getResultList();
	 */
}
