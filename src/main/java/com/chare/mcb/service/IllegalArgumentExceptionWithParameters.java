package com.chare.mcb.service;

public final class IllegalArgumentExceptionWithParameters extends IllegalArgumentException {
	public final Object [] parameters;
	public IllegalArgumentExceptionWithParameters(String message, Object[] parameters) {
		super(message);
		this.parameters = parameters;
	}
	public IllegalArgumentExceptionWithParameters(String message, Throwable cause, Object[] parameters) {
		super(message, cause);
		this.parameters = parameters;
	}
}