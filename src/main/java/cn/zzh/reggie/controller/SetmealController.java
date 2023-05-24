package cn.zzh.reggie.controller;

import cn.zzh.reggie.common.R;
import cn.zzh.reggie.dto.DishDto;
import cn.zzh.reggie.dto.SetmealDto;
import cn.zzh.reggie.entity.Category;
import cn.zzh.reggie.entity.Setmeal;
import cn.zzh.reggie.entity.SetmealDish;
import cn.zzh.reggie.service.CategoryService;
import cn.zzh.reggie.service.SetmealDishService;
import cn.zzh.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    private final SetmealService setmealService;
    private final SetmealDishService setmealDishService;
    private final CategoryService categoryService;
    private final RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    public SetmealController(SetmealService setmealService, SetmealDishService setmealDishService,CategoryService categoryService,RedisTemplate<Object,Object> redisTemplate) {
        this.setmealService = setmealService;
        this.setmealDishService = setmealDishService;
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
    }
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "SetmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

        //清理所有套餐缓存
        /*Set<Object> keys = redisTemplate.keys("Setmeal_*");
        assert keys != null;
        redisTemplate.delete(keys);*/
        /*String key = "Setmeal_" + setmealDto.getCategoryId() + "_1";
        redisTemplate.delete(key);*/
        log.info("缓存清理！");

        return R.success("新增套餐成功");
    }
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        //添加排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(setmealPage,wrapper);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    /**
     * 单个删除和批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "SetmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }
    /**
     * 对菜品批量或者是单个 进行停售或者是起售
     * @return
     */
    @PostMapping("/status/{status}")
    //这个参数这里一定记得加注解才能获取到参数，否则这里非常容易出问题
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        setmealService.updateSetmealStatusById(status,ids);
        return R.success("售卖状态修改成功");
    }
    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getData(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getDate(id);
        return R.success(setmealDto);
    }
    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        if (setmealDto == null){
            return R.error("系统错误，请稍后尝试");
        }
        if (setmealDto.getSetmealDishes() == null){
            return R.error("套餐没有菜品，请添加！");
        }

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null,SetmealDish::getSetmealId,id);
        setmealDishService.remove(queryWrapper);

        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);
        setmealService.updateById(setmealDto);

        //清理所有套餐缓存
        /*Set<Object> keys = redisTemplate.keys("Setmeal_*");
        assert keys != null;
        redisTemplate.delete(keys);*/
        String key = "Setmeal_" + setmealDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        log.info("缓存清理！");

        return R.success("套餐修改成功");
    }
    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "SetmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        /*List<Setmeal> list;
        String key = "Setmeal_" + setmeal.getCategoryId() + "_" +setmeal.getStatus();
        //先从redis获取缓存数据
        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        //如果存在，直接返回，无需查询数据库
        if (list != null){
            log.info("从redis中获取数据");
            return R.success(list);
        }*/
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus, setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        /*redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);*/
        log.info("从数据库获取数据");
        return R.success(list);
    }
    /**
     * 移动端点击套餐图片查看套餐具体内容
     * 这里返回的是dto 对象，因为前端需要copies这个属性
     * 前端主要要展示的信息是:套餐中菜品的基本信息，图片，菜品描述，以及菜品的份数
     * @param SetmealId
     * @return
     */
    //这里前端是使用路径来传值的，要注意，不然你前端的请求都接收不到，就有点尴尬哈
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long SetmealId){
        return R.success(setmealService.dish(SetmealId));
    }
}
