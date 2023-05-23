package cn.zzh.reggie.service;

import cn.zzh.reggie.entity.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getOrderDetailListByOrderId(Long orderId);
}
