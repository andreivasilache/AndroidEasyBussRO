package com.example.androideasybussro.models;


import java.util.concurrent.ExecutionException;

public interface OnGetDataListener<SuccessType, ErrorType> {
    public void onStart();
    public void onSuccess(SuccessType data) ;
    public void onFailed(ErrorType error);
}
