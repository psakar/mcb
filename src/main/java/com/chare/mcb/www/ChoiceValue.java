package com.chare.mcb.www;

import java.io.Serializable;

import com.chare.core.Utils;


public class ChoiceValue implements Serializable {

	private Integer id;
	private String description;

	public ChoiceValue(Integer id, String value) {
		this.id = id;
		this.description = value;
	}

	public ChoiceValue(Long id, String value) {
		this.id = id != null ? id.intValue() : null;
		this.description = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChoiceValue))
			return false;
		ChoiceValue choiceValue = (ChoiceValue) obj;
		if (id == null)
			return false;
		return id.equals(choiceValue.id) && Utils.isEqual(description, choiceValue.description);
	}

	@Override
	public String toString() {
		return String.valueOf(id) + ":" + String.valueOf(description);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		this.description = value;
	}

}
