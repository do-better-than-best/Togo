package org.sanhenanli.togo.musher.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * datetime 2020/1/26 14:15
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Rs<T> {

    public static final int CODE_OK = HttpStatus.OK.value();
    public static final int CODE_ERROR = 90000;

    protected int code;
    protected String msg;
    protected T data;

    public static <T> Rs<T> ok() {
        return new Rs<>(CODE_OK, null, null);
    }

    public static <T> Rs<T> ok(T data) {
        return new Rs<>(CODE_OK, null, data);
    }

    public static <T> Rs<T> from(Op<T> data) {
        if (data.getExp() != null) {
            return error(data.exp().getMessage());
        } else if (data.getTip() != null) {
            return error(data.getTip().getContent());
        } else {
            return ok(data.some());
        }
    }

    public static <T> Rs<T> error(String message) {
        return new Rs<>(CODE_ERROR, message, null);
    }


}
