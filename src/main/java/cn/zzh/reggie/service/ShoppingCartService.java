package cn.zzh.reggie.service;

import cn.zzh.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}
