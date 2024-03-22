package pers.hll.aigc4chat.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.entity.User;
import pers.hll.aigc4chat.server.service.IUserService;

import java.util.List;

/**
 * controller 测试
 *
 * @author hll
 * @since 2024/03/21
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/save")
    public R<User> save() {
        User user = new User();
        user.setId(10L);
        user.setUsername("miaolinlin");
        user.setPwd("121212");
        userService.save(user);
        return R.data(user);
    }

    @GetMapping("/update")
    public R<?> update(Long id) {
        User user = new User();
        user.setId(id);
        user.setPwd("1111111111");
        userService.updateById(user);
        return R.success();
    }

    @GetMapping("/list")
    public R<List<User>> list() {
        List<User> list = userService.list();
        return R.data(list);
    }

    @GetMapping("/listByCondition")
    public R<List<User>> listByCondition() {
        List<User> list = userService.list(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, "miao")
                .like(User::getPwd, "%111%")
                .orderByDesc(User::getCreateTime)
        );
        return R.data(list);
    }

    @GetMapping("/getById")
    public R<User> getById(Integer id) {
        return R.data(userService.getById(id));
    }

    @GetMapping("/delete")
    public R<Boolean> delete(Integer id) {
        boolean isDelete = userService.removeById(id);
        return R.data(isDelete);
    }

    @GetMapping("/page")
    public R<IPage<User>> page(int pageNum, int pageSize, String name) {
        IPage<User> page = new Page<>(pageNum, pageSize);

        userService.page(page, new LambdaQueryWrapper<User>()
                .like(StringUtils.isNotEmpty(name), User::getUsername, "%" + name + "%"));

        return R.data(page);
    }
}
