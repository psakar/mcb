package com.chare.mcb.repository;

import com.chare.mcb.entity.Card;
import com.chare.mcb.entity.FeeType;
import com.chare.mcb.entity.TransferType;
import com.chare.repository.JpaRepository;

public interface FeeTypeRepository extends JpaRepository<String, FeeType> {

	FeeType find(Card card, TransferType transferType);

}
