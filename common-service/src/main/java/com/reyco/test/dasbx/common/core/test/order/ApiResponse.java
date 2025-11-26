package com.reyco.test.dasbx.common.core.test.order;

public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Long timestamp;
    public ApiResponse() {
		// TODO Auto-generated constructor stub
	}
    
    public ApiResponse(boolean success, String message, T data, Long timestamp) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
	}

	public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "success", data, System.currentTimeMillis());
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, System.currentTimeMillis());
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, System.currentTimeMillis());
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}