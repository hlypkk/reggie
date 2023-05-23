package cn.zzh.reggie.service;

import cn.zzh.reggie.dto.DishDto;
import cn.zzh.reggie.dto.SetmealDto;
import cn.zzh.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 根据套餐id修改售卖状态
     * @param status
     * @param ids
     */
    void updateSetmealStatusById(Integer status, List<Long> ids);

    /**
     *回显套餐数据：根据套餐id查询套餐
     * @param id
     * @return
     */
    SetmealDto getDate(Long id);

    /**
     * 移动端点击套餐图片查看套餐具体内容
     * @param SetmealId
     * @return
     */
    List<DishDto> dish(Long SetmealId);
}
