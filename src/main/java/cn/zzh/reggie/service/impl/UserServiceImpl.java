package cn.zzh.reggie.service.impl;

import cn.zzh.reggie.entity.User;
import cn.zzh.reggie.mapper.UserMapper;
import cn.zzh.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
