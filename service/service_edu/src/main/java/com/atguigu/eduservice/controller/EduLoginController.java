package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {

    //login
    @PostMapping("login")
    public R login(){

        return R.ok().data("token","admin");
    }

    //info
    @GetMapping("info")
    public R info(){

        return R.ok().data("roles","123").data("name","456")
                .data("avatar","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp1-q.mafengwo.net%2Fs7%2FM00%2F25%2FE2%2FwKgB6lPh4UeARKPpAABh4pruLDc72.jpeg%3FimageMogr2%252Fthumbnail%252F%21310x207r%252Fgravity%252FCenter%252Fcrop%252F%21310x207%252Fquality%252F90&refer=http%3A%2F%2Fp1-q.mafengwo.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619144624&t=bbfad3b8c5564aaa80d982f9726f03ee");
    }
}
