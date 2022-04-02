package com.example.androideasybussro.models;


public interface OnGetDataListener<SuccessType, ErrorType> {
    public void onStart();
    public void onSuccess(SuccessType data);
    public void onFailed(ErrorType error);
}
