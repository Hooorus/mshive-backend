package com.yimingliao.mshivebackend.service.mongodb.impl;

import com.mongodb.client.result.UpdateResult;
import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.mongodb.User;
import com.yimingliao.mshivebackend.mapper.mongodb.UserRepository;
import com.yimingliao.mshivebackend.service.mongodb.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Calendo
 * @version 1.0
 * @description TODO
 * @date 2023/10/20 16:44
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    //-----------------------------------------INSERT---------------------------------------
    //Insert One User
    @Override
    public R insertOneUser(User user) {
        user.setIsAdmin(false);
        user.setIsDeleted(false);
        user.setAccountStatus("1");
        user.setLastLoginTime(new Date().toString());
        user.setModifyTime(new Date().toString());
        //保存新增的用户
        log.info("insertOneUser: " + user);
        String returnUserUUId = mongoTemplate.save(user).getId();
        //判定是否保存成功
        if (returnUserUUId == null || "".equals(returnUserUUId)) {
            return R.error(404, "Insert User Failed", new Date());
        }
        return R.success(200, "Insert User Success", new Date(), returnUserUUId);
    }

    //-----------------------------------------UPDATE---------------------------------------
    //Update One User
    @Override
    public R updateOneUser(User user) {
        //新建Query
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(user.getId()));
        Update update = new Update()
                .set("account_status", "1")
                .set("username", user.getUsername())
                .set("password", user.getPassword())
                .set("email", user.getEmail())
                .set("avatar_url", user.getAvatarUrl())
                .set("is_admin", false)
                .set("is_deleted", false)
                .set("modify_time", new Date().toString())//处理时间：后端插入时间
                .set("last_login_time", new Date().toString());
        //保存修改的用户
        log.info("updateOneUser: " + update);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        long modifiedCount = updateResult.getModifiedCount();
        //判定是否保存成功
        if (updateResult.getModifiedCount() == 0) {
            return R.error(404, "Update User Failed", new Date(), modifiedCount);
        }
        return R.success(200, "Update User Success", new Date(), modifiedCount);
    }

    //-----------------------------------------DELETE---------------------------------------
    //Delete One User, need userUUId
    @Override
    public R deleteOneUser(String userUUId) {
        userRepository.deleteById(userUUId);
        log.info("deleteOneFurnitureByUserUUId success");
        return R.success(200, "User Deleted", new Date());
    }

    //-----------------------------------------SEARCH---------------------------------------
    //Find One User, need userUUId
    @Override
    public R searchOneUserByUserUUId(String userUUId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userUUId));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return R.error(404, "Search Failed", new Date());
        }
        return R.success(200, "Search Success", new Date(), user);
    }
}
