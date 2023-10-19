package com.yimingliao.mshivebackend.controller.elastic;

import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.elastic.ESStuff;
import com.yimingliao.mshivebackend.service.elastic.impl.ESStuffServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/es_stuff")
public class ESStuffController {

    @Autowired
    private ESStuffServiceImpl esStuffService;

    //SEARCH <ESStuff> BY <Name> OR <Description>, NEED <String Keyword>
    @GetMapping("/search_nod")
    public R searchESStuffByNameOrDescription(String keyword) {
        List<ESStuff> esStuffs = esStuffService.searchESStuffByNameOrDescription(keyword);
        return R.success(200, "ES Search Success", new Date(), esStuffs);
    }

    //SEARCH <ESStuff> BY <String keyword>, intro searching <Name> AND <Description>
    @GetMapping("/search")
    public R searchESStuffSimple(String keyword) {
        SearchHits searchHits = esStuffService.searchESStuffSimple(keyword);
        return R.success(200, "ES Search Success", new Date(), searchHits);
    }


}
