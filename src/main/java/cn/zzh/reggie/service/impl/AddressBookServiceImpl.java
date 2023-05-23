package cn.zzh.reggie.service.impl;

import cn.zzh.reggie.entity.AddressBook;
import cn.zzh.reggie.mapper.AddressBookMapper;
import cn.zzh.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
