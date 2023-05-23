package cn.zzh.reggie.service.impl;

import cn.zzh.reggie.entity.DishFlavor;
import cn.zzh.reggie.mapper.DishFlavorMapper;
import cn.zzh.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
