package org.sanhenanli.togo.musher.model.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * datetime 2020/1/10 17:17
 * 网关返回值
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class Rs<T> implements Serializable {
    private static final int CODE_SUCCESS = 200;

    private static final int CODE_FAIL = 90000;

    private static final int CODE_ERROR = 90000;

    private static final long serialVersionUID = 5393448305311433341L;
    protected int code;
    protected String msg;
    protected T data;

    public static <T> Rs<T> success() {
        return new Rs<>(CODE_SUCCESS, "success", null);
    }
    public static <T> Rs<T> success(String message) {
        return new Rs<>(CODE_SUCCESS, message, null);
    }
    public static <T> Rs<T> success(T data) {
        return new Rs<>(CODE_SUCCESS, "success", data);
    }
    public static <T, P extends Tip> Rs<T> success(Op<T, P> data) {
        if (data.getExp() != null) {
            return error(data.exp().getMessage());
        } else if (data.getTip() != null) {
            return error(data.getTip().getContent());
        } else if (data.getSome() != null) {
            return success(data.some());
        } else {
            return error();
        }
    }

    public static <T> Rs<T> success(String message, T data) {
        return new Rs<>(CODE_SUCCESS, message, data);
    }

    public static <T> Rs<T> error() {
        return new Rs<>(CODE_ERROR, "fail", null);
    }
    public static <T> Rs<T> error(String message) {
        return new Rs<>(CODE_ERROR, message, null);
    }
    public static <T> Rs<T> error(T data) {
        return new Rs<>(CODE_ERROR, "fail", data);
    }
    public static <T> Rs<T> error(String message, T data) {
        return new Rs<>(CODE_ERROR, message, data);
    }

    public static <T> Rs<T> badrequest() {
        return new Rs<>(CODE_FAIL, "no identifier arguments", null);
    }
    public static <T> Rs<T> badrequest(String message) {
        return new Rs<>(CODE_FAIL, message, null);
    }
    public static <T> Rs<T> badrequest(T data) {
        return new Rs<>(CODE_FAIL, "no identifier arguments", data);
    }
    public static <T> Rs<T> badrequest(String message, T data) {
        return new Rs<>(CODE_FAIL, message, data);
    }
}
