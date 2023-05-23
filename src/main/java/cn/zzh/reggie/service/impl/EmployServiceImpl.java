package cn.zzh.reggie.service.impl;

import cn.zzh.reggie.entity.Employee;
import cn.zzh.reggie.mapper.EmployMapper;
import cn.zzh.reggie.service.EmployService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployServiceImpl extends ServiceImpl<EmployMapper, Employee> implements EmployService {
}
