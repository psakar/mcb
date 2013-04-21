package com.chare.mcb.repository;

import com.chare.mcb.entity.Statement;
import com.chare.repository.JpaRepository;

public interface StatementRepository extends JpaRepository<Integer, Statement> {

	Statement findByNumber(String number, int year);

}
