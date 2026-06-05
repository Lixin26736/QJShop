package cn.mikuyun.qjshopbackend.controller.admin;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.common.PageResult;
import cn.mikuyun.qjshopbackend.entity.User;
import cn.mikuyun.qjshopbackend.service.UserService;
import cn.mikuyun.qjshopbackend.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    public ApiResponse<PageResult<User>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status
    ) {
        return ApiResponse.success(userService.page(pageNum, pageSize, keyword, role, status));
    }

    /**
     * 导出用户数据到Excel
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response
    ) throws IOException {
        // 获取所有符合条件的用户数据
        List<User> userList = userService.listAll(keyword, role, status);

        // 准备Excel数据
        String[] headers = {"ID", "用户名", "昵称", "手机号", "邮箱", "角色", "状态", "创建时间"};
        List<List<Object>> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (User user : userList) {
            List<Object> row = new ArrayList<>();
            row.add(user.getId());
            row.add(user.getUsername());
            row.add(user.getNickname());
            row.add(user.getPhone());
            row.add(user.getEmail());
            row.add(user.getRole() == 1 ? "管理员" : "普通用户");
            row.add(user.getStatus() == 1 ? "正常" : "禁用");
            row.add(user.getCreateTime() != null ? user.getCreateTime().format(formatter) : "");
            dataList.add(row);
        }

        // 导出Excel
        ExcelExportUtil.exportExcel(response, "用户数据", "用户列表", headers, dataList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        return ApiResponse.success(userService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Void> create(@RequestBody User user) {
        userService.save(user);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return ApiResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success();
    }
}
