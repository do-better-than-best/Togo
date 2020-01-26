package org.sanhenanli.togo.musher.model;

import lombok.Getter;

import java.util.function.Function;

/**
 * datetime 2020/1/26 14:16
 *
 * @author zhouwenxiang
 */
@Getter
public class Op<T> {

    private T some;
    private Tip tip;
    private Throwable exp;

    public static <T, R> Op<T> some(R r, Function<R, T> f) {
        try {
            return some(f.apply(r));
        } catch (Exception e) {
            return exp(e);
        }
    }

    public static <T> Op<T> some(T t) {
        return new Op<>(t);
    }

    public static <T> Op<T> tip(Tip tip) {
        return new Op<>(tip);
    }

    public static <T> Op<T> exp(Throwable throwable) {
        return new Op<>(throwable);
    }

    private Op(T some) {
        this.some = some;
    }

    private Op(Tip tip) {
        this.tip = tip;
    }

    private Op(Throwable exp) {
        this.exp = exp;
    }

    public T some() {
        return some;
    }

    public Tip tip() {
        return tip;
    }

    public Throwable exp() {
        return exp;
    }

}
