package org.sanhenanli.togo.musher.model.basic;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.function.Function;

/**
 * datetime 2020/1/10 14:38
 * service层返回值
 *
 * @author zhouwenxiang
 */
@Slf4j
@Getter
public class Op<S, T extends Tip> implements Serializable {
    private static final long serialVersionUID = 2207688419354339056L;
    
    private S some;
    private T tip;
    private Throwable exp;

    public static <S, T extends Tip, R> Op<S, T> some(R r, Function<R, S> f) {
        try {
            return some(f.apply(r));
        } catch (Exception e) {
            log.error("{}", r.getClass(), e);
            return exp(e);
        }
    }

    public static <S, T extends Tip> Op<S, T> some(S t) {
        return new Op<>(t);
    }

    public static <S, T extends Tip> Op<S, T> tip(T tip) {
        return new Op<>(tip);
    }

    public static <S, T extends Tip> Op<S, T> exp(Throwable throwable) {
        return new Op<>(throwable);
    }

    private Op(S some) {
        this.some = some;
    }

    private Op(T tip) {
        this.tip = tip;
    }

    private Op(Throwable exp) {
        this.exp = exp;
    }

    public S some() {
        return some;
    }

    public T tip() {
        return tip;
    }

    public Throwable exp() {
        return exp;
    }

    @Override
    public String toString() {
        return "Op{" +
                "some=" + some +
                ", tip=" + tip +
                ", exp=" + exp +
                '}';
    }
}
