package cn.zzh.reggie.service.impl;

import cn.zzh.reggie.entity.OrderDetail;
import cn.zzh.reggie.mapper.OrderDetailMapper;
import cn.zzh.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    private final OrderDetailService orderDetailService;
    @Autowired
    @Lazy
    public OrderDetailServiceImpl(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    /**
     * 抽离的一个方法，通过订单id查询订单明细，得到一个订单明细的集合
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId,orderId);
        return orderDetailService.list(wrapper);
    }
}
