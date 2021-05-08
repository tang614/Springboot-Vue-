package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-02
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //上传excel文件
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {

            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //获取数据库中课程分类的数据
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> listOne = baseMapper.selectList(wrapperOne);

        //查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> listTwo = baseMapper.selectList(wrapperTwo);

        //创建list集合，存储一级分类
        List<OneSubject> listFinalOne = new ArrayList<>();

        //封装一级分类
        for (int i = 0; i < listOne.size(); i++) {
            EduSubject eduSubject = listOne.get(i);
            OneSubject oneSubject = new OneSubject();//创建一级分类对象
            oneSubject.setId(eduSubject.getId());
            oneSubject.setTitle(eduSubject.getTitle());
            //listFinalOne.add(oneSubject);
            //创建list集合,存储二级分类
            ArrayList<TwoSubject> listFinalTwo = new ArrayList<>();
            //封装二级分类
            for (int j = 0; j < listTwo.size(); j++) {
                EduSubject eduSubject2 = listTwo.get(j);
                if (oneSubject.getId().equals(eduSubject2.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    twoSubject.setId(eduSubject2.getId());
                    twoSubject.setTitle(eduSubject2.getTitle());
                    listFinalTwo.add(twoSubject);
                }
            }
            //将二级分类集合作为一级分类的属性
            oneSubject.setChildren(listFinalTwo);
            listFinalOne.add(oneSubject);
        }
        return listFinalOne;
    }
}
