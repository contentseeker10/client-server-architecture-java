package dev.contentseeker10.packet;

public enum CommandType {
    GET_AMOUNT(1),
    WRITE_OFF(2),
    WRITE_ON(3),
    ADD_GROUP(4),
    ADD_PRODUCT_TO_GROUP(5),
    SET_PRICE(6);

    private final int code;

    CommandType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
