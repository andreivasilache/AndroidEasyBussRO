package com.example.androideasybussro.constants;

public enum MessageCodes {
    SUCCESS(200),
    NOT_FOUND(404),
    INVALID_PASSWORD(401),
    ALREADY_EXISTS(402),
    SOMETHING_IS_WRONG(403);

    MessageCodes(final int value) {
        this.value = value;
    }

    public final int value;

    public static MessageCodes findByVal(int val){
        for(MessageCodes v : values()){
            if( v.value == val){
                return v;
            }
        }
        return null;
    }
}
