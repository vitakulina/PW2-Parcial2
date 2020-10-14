package com.vitakulina.apiEcommerce.service.business.exception;
import org.springframework.http.HttpStatus;

public enum ProductError {
	
	PRODUCT_ID_REQUIRED("PRODUCT_ID_REQUIRED", "Product id is required", HttpStatus.BAD_REQUEST),
	PRODUCT_ID_NOT_REQUIRED("PRODUCT_ID_NOT_REQUIRED", "Product id is not required", HttpStatus.BAD_REQUEST),
	PRODUCT_DESCRIPTION_REQUIRED("PRODUCT_DESCRIPTION_REQUIRED", "Product description is required",  HttpStatus.BAD_REQUEST),
	PRODUCT_STOCK_REQUIRED("PRODUCT_STOCK_REQUIRED", "Product stock is required", HttpStatus.BAD_REQUEST),
	PRODUCT_STOCK_INVALID("PRODUCT_STOCK_INVALID", "Product stock must be greater than 0", HttpStatus.BAD_REQUEST),
	PRODUCT_UNITPRICE_REQUIRED("PRODUCT_UNITPRICE_REQUIRED", "Product unitprice is required", HttpStatus.BAD_REQUEST),
	PRODUCT_UNITPRICE_INVALID("PRODUCT_UNITPRICE_INVALID", "Product unitprice must be greater than 0", HttpStatus.BAD_REQUEST),
	PRODUCT_PRESENT_IN_CART("PRODUCT_PRESENT_IN_CART", "Product is present in carts. Deletion is not possible", HttpStatus.CONFLICT);
	
	private final String errCode;
	private final String errMessage;
	private final HttpStatus httpStatus;
	
	ProductError(String errCode, String errMessage, HttpStatus httpStatus){
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.httpStatus = httpStatus;
	}
	
	public String getErrCode() {
		return this.errCode;
	}
	
	public String getErrMessage() {
		return this.errMessage;
	}
	
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}
