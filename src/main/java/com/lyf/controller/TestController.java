package com.lyf.controller;


import com.alibaba.fastjson.JSON;
import com.lyf.config.Page;
import com.lyf.config.PageUtil;
import com.lyf.dao.UserDao;
import com.lyf.model.User;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@RestController
public class TestController {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private UserDao userDao;

    @RequestMapping("/select")
    public String testS(@RequestParam("offset") int offset,@RequestParam("limit") int limit){
        PageUtil.setPagingParam(offset,limit);
        List<User> user = (List<User>) userDao.getUser();

        return JSON.toJSON(user).toString();
    }

    @RequestMapping("/test")
    public String testRest(){

        DefaultRedisScript<Long> script = new DefaultRedisScript();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("a.lua")));

        List<String> keys = new ArrayList<>();
        keys.add("test_lua");


        Long str = redisTemplate.execute(script,keys,0,0);

        return str+"";
    }


    @RequestMapping("/task")
    public String testTask() throws ExecutionException, InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("asd");
            }
        });

        thread.start();


        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {

                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }

                return "task结束了";
            }
        });
        task.run();
        String str  = task.get();


        return str;
    }

}
