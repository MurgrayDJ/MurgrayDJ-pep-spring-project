package com.example.exception;

public class CustomException extends Exception{
    public CustomException(){}

    public CustomException(String message){
        super(message);
    }
}
