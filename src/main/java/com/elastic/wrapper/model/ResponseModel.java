package com.elastic.wrapper.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "RESPONSE" })
@Getter
@Setter
@ToString
public class ResponseModel implements Serializable {

	@JsonProperty("RESPONSE")
	public Response response;
	private final static long serialVersionUID = -1764475197323276074L;

}
