package cn.zzh.reggie.dto;

import cn.zzh.reggie.entity.OrderDetail;
import cn.zzh.reggie.entity.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
