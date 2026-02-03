package com.example.exception;

public class TokenExpiredException extends RuntimeException {
	public TokenExpiredException()
	{ 
		super("token expire"); 
		}

}
