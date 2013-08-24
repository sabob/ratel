package com.google.ratel.deps.jackson.databind.jsonFormatVisitors;

import com.google.ratel.deps.jackson.annotation.JsonCreator;
import com.google.ratel.deps.jackson.annotation.JsonValue;

public enum JsonFormatTypes {
	
	STRING,
	NUMBER,
	INTEGER,
	BOOLEAN,
	OBJECT,
	ARRAY,
	NULL,
	ANY;
	
	
	@JsonValue
	public String value() {
		return this.name().toLowerCase();
	}
	
	@JsonCreator
	public static JsonFormatTypes forValue(String s) {
		return valueOf(s.toUpperCase());
	}
}