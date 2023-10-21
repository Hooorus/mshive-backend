package com.yimingliao.mshivebackend.service.mongodb;

import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.mongodb.Furniture;

/**
 * @author Calendo
 * @version 1.0
 * @description Furniture 业务层接口
 * @date 2023/10/16 21:15
 */
public interface IFurnitureService {

    //Insert One Furniture
    R insertOneFurniture(Furniture furniture);

    //Update One Furniture
    R updateOneFurniture(Furniture furniture);

    //Delete One User's Furniture, need furnitureUUId
    R deleteOneFurnitureByFurnitureUUId(String furnitureUUId);

    //Find One User's Some Furniture, need lastSeenFurnitureId & searchSize
    R searchFurnitureListByUserUUId(String userUUId, Long lastSeenFurnitureId, Integer searchSize);

    //Find One User's All Furniture
    R searchFurnitureAllByUserUUId(String userUUId);

    //Find One User's One Furniture, need userUUId & furnitureId
    R searchOneFurnitureByUserUUId(String userUUId, Long furnitureId);

}