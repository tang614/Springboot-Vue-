package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectExcelListener不能交给spring进行管理，需要自己
    private EduSubjectService eduSubjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData != null){

            //添加一级分类
            EduSubject oneSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
            if (oneSubject == null){//表示数据库表里没有一级字段
                oneSubject = new EduSubject();
                oneSubject.setParentId("0");
                oneSubject.setTitle(subjectData.getOneSubjectName());
                eduSubjectService.save(oneSubject);
            }

            String pid = oneSubject.getId();

            //添加二级分类
            EduSubject twoSubject = this.existTwoSubject(eduSubjectService,subjectData.getTwoSubjectName(),pid);
            if (twoSubject == null){
                twoSubject = new EduSubject();
                twoSubject.setTitle(subjectData.getTwoSubjectName());
                twoSubject.setParentId(pid);
                eduSubjectService.save(twoSubject);
            }
        }

    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        return eduSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        return eduSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
