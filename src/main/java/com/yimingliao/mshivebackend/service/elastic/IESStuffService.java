package com.yimingliao.mshivebackend.service.elastic;

import com.yimingliao.mshivebackend.entity.elastic.ESStuff;
import org.elasticsearch.search.SearchHits;

import java.util.List;

/**
 * @author Calendo
 * @version 1.0
 * @description Elasticsearch Stuff Interface
 * @date 2023/10/19 20:36
 */
public interface IESStuffService {

    //SEARCH <ESStuff> BY <Name> OR <Description>
    List<ESStuff> searchESStuffByNameOrDescription(String keyword);

    //SEARCH <ESStuff> BY <String keyword>, intro searching <Name> AND <Description>
    SearchHits searchESStuffSimple(String keyword);
}
