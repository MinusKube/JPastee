package fr.minuskube.pastee.response;

import java.util.Optional;

public class ResponseError {

    private int code;
    private String message;
    private String field;

    public ResponseError(int code, String message, String field) {
        this.code = code;
        this.message = message;
        this.field = field;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Optional<String> getField() { return Optional.ofNullable(field); }

}
