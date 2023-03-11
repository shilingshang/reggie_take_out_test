package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

/*    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }*/

    /**
     * 页面显示——输入框查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        //log.info("beginTime:{}",beginTime);
        //log.info("endTime:{}",endTime);
        //页面构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        //查询所有orders表信息
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //查询name
        if (number != null) {
            queryWrapper.like(Orders::getNumber, number);
        }
        //查询beginTime 大于等于这个时间
        if (beginTime != null) {
            queryWrapper.ge(Orders::getOrderTime, beginTime);
        }
        //查询endTime 小于等于这个时间
        if (endTime != null) {
            queryWrapper.le(Orders::getOrderTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 订单状态修改——管理端
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> order(@RequestBody Orders orders){
        //log.info("orders:{}", orders);
        Orders order = orderService.getById(orders.getId());
        if (order.getStatus() == 2){
            orders.setStatus(3);
            orderService.updateById(orders);
            return R.success("订单派送成功");
        }else {
            orders.setStatus(4);
            orderService.updateById(orders);
            return R.success("订单已完成");
        }
    }

}