package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-02
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
@CrossOrigin
public class EduTeacherController {

    //访问地址
    //http://localhost:8001/eduservice/edu-teacher/findAll

    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //查询讲师表所有数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    //逻辑删除讲师的方法
    //id值需要通过路径传输
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
        @ApiParam(name = "id", value = "讲师ID", required = true)
        @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if (flag)
            return R.ok();
        else
            return R.error();
    }

    //分页查询讲师的方法
    //current表示当前页
    //limit表示每页记录数
    @ApiOperation(value = "分页查询所有讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        teacherService.page(pageTeacher,null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> list = pageTeacher.getRecords();//当前页数据变为集合

        return R.ok().data("total",total).data("rows",list);
    }

    //条件查询+分页
    @ApiOperation(value = "条件查询+分页效果")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (level != null ) {
            queryWrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }
        //调用方法实现条件查询
        teacherService.page(teacherPage,queryWrapper);
        List<EduTeacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();

        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师的方法
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save)
            return R.ok();
        else
            return R.error();
    }

    //通过讲师id查询单个讲师的信息
    @ApiOperation(value = "通过讲师id查询单个讲师的信息")
    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    //更新讲师信息
    @ApiOperation(value = "更新讲师信息")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody(required = false) EduTeacher teacher){
        boolean b = teacherService.updateById(teacher);
        if (b)
            return R.ok();
        else
            return R.error();
    }

}

