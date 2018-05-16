package com.n26.controller.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RestError
{
    private HttpStatus status;

    private List<String> errors;


    public RestError()
    {

    }


    public RestError(HttpStatus status, List<String> errors)
    {
        this.status = status;
        this.errors = errors;
    }


    public RestError(HttpStatus status, String error)
    {
        this.status = status;
        errors = Arrays.asList(error);
    }


    public HttpStatus getStatus()
    {
        return status;
    }


    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }


    public List<String> getErrors()
    {
        return errors;
    }


    public void setErrors(List<String> errors)
    {
        this.errors = errors;
    }

}
