package com.dm.sample.controller;

import com.dm.sample.entity.pojodo.User;
import com.dm.sample.entity.dto.ApiResponse;
import com.dm.sample.mapper.UserMapper;
import com.dm.spring.annotation.EnableDmAsync;
import com.dm.util.JedisUtil;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: zy
 * @Date: 2020/2/28 21:09
 * @Description:
 */
@RestController
public class MyController {
	@Autowired
	private UserMapper userMapper;


	@GetMapping(value = "/selectUserOne")
	@Transactional
	public ApiResponse selectUserOne(@RequestParam("id") int id) {
		User user = userMapper.selectUserOne(id);
		return ApiResponse.success(user);
	}

	@GetMapping("/insertUser")
	@Transactional
	@EnableDmAsync
	public ApiResponse insertOne(@RequestParam("type") int type){
		User user = new User();
		user.setPassword("123456");
		user.setUsername("zym");
		int i = 0;
		if (type == 0) {
			i = userMapper.insertUserInc(user);
			int a = 1 / type;
		} else if(type == 1) {
			user.setId(new Random().nextInt(2000));
			i = userMapper.insertUser(user);

		} else {
			user.setId(new Random().nextInt(3000));
			i = userMapper.insertUserWithID(user);
		}
		return i > 0 ? ApiResponse.success("插入成功") :ApiResponse.fail("插入失败");
	}

	@GetMapping("/deleteUser")
	@Transactional
	@EnableDmAsync
	public ApiResponse deleteUser(User user){
		int i = userMapper.deleteUser(user);
		return i > 0? ApiResponse.success("删除成功") :ApiResponse.fail("删除失败");
	}
}
