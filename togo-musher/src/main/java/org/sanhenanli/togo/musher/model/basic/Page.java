package org.sanhenanli.togo.musher.model.basic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * datetime 2020/1/8 9:54
 * 分页对象
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Data
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 757206732440217695L;
    private List<T> list;
    private int total;
    private int current;
    private int size;
    private int pages;
}
