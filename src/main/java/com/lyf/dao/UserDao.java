package com.lyf.dao;


import com.lyf.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface UserDao {

    @Select("select * from user")
    List<User> getUser();

    @Update("update user set username='vvv' where id=1 ")
    void updateUser();
}
