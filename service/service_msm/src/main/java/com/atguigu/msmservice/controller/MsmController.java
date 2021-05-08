package com.atguigu.msmservice.controller;

import com.aliyuncs.utils.StringUtils;
import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //从redis获取验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //生成随机值，交给阿里云发送
        code = RandomUtil.getFourBitRandom();
        HashMap<String, Object> param = new HashMap<>();
        param.put("code",code);

        //调用service发送短信的方法
        boolean idSend = msmService.send(param,phone);

        if (idSend){
            //发送成功，把验证码放到redis中，并设置过期时间5分钟
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }
}
