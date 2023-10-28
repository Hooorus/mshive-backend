package com.yimingliao.mshivebackend.controller.version1.stuff;

import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.elastic.ESStuff;
import com.yimingliao.mshivebackend.service.elastic.impl.ESStuffServiceImpl;
import com.yimingliao.mshivebackend.service.mongodb.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Calendo
 * @version 1.0
 * @description Elasticsearch Stuff Controller
 * @date 2023/10/19 20:56
 */

@RestController
@Slf4j
@RequestMapping("/v1/es_stuff")
public class ESStuffController {

    @Autowired
    private ESStuffServiceImpl esStuffService;

    @Autowired
    private UserServiceImpl userService;

    //SEARCH <ESStuff> BY <Name> OR <Description>, NEED <String Keyword>
    @GetMapping("/{user_uuid}/search_nod")
    public R searchESStuffByNameOrDescription(@PathVariable("user_uuid") String userUUId,
                                              @RequestParam String keyword) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Search Forbidden", new Date(), "无权限搜索");
        }
        List<ESStuff> esStuffs = esStuffService.searchESStuffByNameOrDescription(keyword);
        return R.success(200, "ES Search Success", new Date(), esStuffs);
    }

    //SEARCH <ESStuff> BY <String keyword>, intro searching <Name> AND <Description>
    @GetMapping("/{user_uuid}/search")
    public R searchESStuffSimple(@PathVariable("user_uuid") String userUUId, @RequestParam String keyword) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Search Forbidden", new Date(), "无权限搜索");
        }
        SearchHits searchHits = esStuffService.searchESStuffSimple(keyword);
        return R.success(200, "ES Search Success", new Date(), searchHits);
    }

    @GetMapping("/{user_uuid}/search_page")
    public R searchPageESStuffByUserUUId(@PathVariable("user_uuid") String userUUId,
                                         @RequestParam Long lastSeenStuffId,
                                         @RequestParam Integer searchSize) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Search Forbidden", new Date(), "无权限搜索");
        }
        return esStuffService.searchPageESStuffByUserUUId(userUUId, lastSeenStuffId, searchSize);
    }

    @GetMapping("/{user_uuid}/search_user_all")
    public R searchESStuffAllByUserUUId(@PathVariable("user_uuid") String userUUId) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Search Forbidden", new Date(), "无权限搜索");
        }
        return esStuffService.searchESStuffAllByUserUUId(userUUId);
    }

    @PutMapping("/{user_uuid}/insert_one")
    public R insertOneESStuff(@PathVariable("user_uuid") String userUUId, @RequestBody ESStuff esStuff) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Insert Forbidden", new Date(), "无权限新增");
        }
        return esStuffService.insertOneESStuff(esStuff);
    }

    @PatchMapping("/{user_uuid}/update_one")
    public R updateOneESStuff(@PathVariable("user_uuid") String userUUId, @RequestBody ESStuff esStuff) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Update Forbidden", new Date(), "无权限修改");
        }
        return esStuffService.updateOneESStuff(esStuff);
    }

    @DeleteMapping("/{user_uuid}/delete_one")
    public R deleteOneESStuff(@PathVariable("user_uuid") String userUUId, @RequestParam String esstuffUUId) {
        if (userService.searchOneUserByUserUUId(userUUId).getStatus() != 200) {
            return R.error(403, "Delete Forbidden", new Date(), "无权限删除");
        }
        return esStuffService.deleteOneESStuff(esstuffUUId);
    }


}
