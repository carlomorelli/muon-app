package com.csoft.muon.events;

import com.csoft.muon.handler.Result;

/**
 * Enum collecting noteworthy HTTP errors and messages handled by the application
 * To be used by main and test code
 * @author Carlo Morelli
 *
 */
public enum HttpErrorEvent {

	SC_400_FORBIDDEN_BODY(400, "Forbidded to send body"),
	SC_403_MALFORMED_BODY(403, "ClientError: malformed / unparsable input body"),
	SC_403_MISSING_FIELDS_IN_BODY(403, "ClientError: required fields not available in body"),
	SC_403_INVALID_INDEX_IN_BODY(403, "ClientError: invalid / null index or already used index in input item"),
	SC_404_NOT_FOUND(404, "ClientError: requested index not found"),
	SC_503_ERROR_CREATING_RETURN_BODY(503, "ServerError: unable to process correctly reflected item from database");
	
	private final Integer statusCode;
	private final String errorMsg;
	
	HttpErrorEvent(final Integer statusCode, final String errorMsg) {
		this.statusCode = statusCode;
		this.errorMsg = errorMsg;
	}
	
	public Result asResult() {
		return new Result(statusCode, errorMsg);
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
}
