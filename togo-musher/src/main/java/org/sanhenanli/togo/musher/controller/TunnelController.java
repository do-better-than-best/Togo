package org.sanhenanli.togo.musher.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sanhenanli.togo.musher.model.basic.Rs;
import org.sanhenanli.togo.musher.model.enums.TunnelTypeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * datetime 2020/1/26 13:53
 *
 * @author zhouwenxiang
 */
@Api("通道管理")
@RestController
@RequestMapping("/tunnel")
public class TunnelController {

    @ApiOperation("列举现有的通道类型")
    @GetMapping("/const/type/list")
    public Rs<List<TunnelTypeEnum>> listAvailableTunnelTypes() {
        return Rs.success();
    }

}
