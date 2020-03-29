package com.dm.sample.mapper;

import com.dm.sample.entity.pojodo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zy
 * @create 2019-03-17 22:30
 */
public interface UserMapper {

    /**
     * 新增数据
     *
     * @param user
     */
    @Insert("insert into user values(#{id},#{username},#{password});")
    int insertUser(User user);

    /**
     * 新增数据
     * 自动增长
     *
     * @param user
     */
    @Insert("insert into user(username, password) values(#{username},#{password});")
    int insertUserInc(User user);

    /**
     * 新增数据
     *
     * @param user
     */
    @Insert("insert into user(id, username) values(#{id},#{username});")
    int insertUserWithID(User user);

    /**
     * 修改数据
     *
     * @param user
     */
    public void updateUser(User user);

    /**
     * 删除数据
     *
     * @param user
     */
    @Delete("delete from user where id =#{id} and username=#{username};")
    int deleteUser(User user);

    /**
     * 查找单条数据
     *
     * @param id
     * @return
     */
    public User selectUser(int id);

    /**
     * 查找多条数据
     *
     * @param user
     * @return
     */
    public List<User> selectUserList(User user);

    @Select("select * from user where id=#{id};")
    User selectUserOne(int id);
}
