package com.yimingliao.mshivebackend.controller.version1.ai;

import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.yimingliao.mshivebackend.common.R;
import com.yimingliao.mshivebackend.entity.image.ImageInfo;
import com.yimingliao.mshivebackend.entity.image.TagResult;
import com.yimingliao.mshivebackend.service.mongodb.impl.UserServiceImpl;
import com.yimingliao.mshivebackend.utils.huaweicloud.RunImageMediaTaggingSolution;
import com.yimingliao.mshivebackend.utils.tencentcloud.DetectProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Calendo
 * @version 1.0
 * @description AI物品识别模块接口，只有一个函数：imageMediaTagging，拥有1.分类贴标签返回；2.分类贴标签定位返回
 * @date 2023/10/14 20:30
 */
@CrossOrigin
@RestController
@RequestMapping("/v1/ai")
@Slf4j
public class StuffRecognitionController {

    @Autowired
    RunImageMediaTaggingSolution runImageMediaTaggingSolution;

    @Autowired
    DetectProduct detectProduct;

    @Autowired
    UserServiceImpl userService;

    //分类、贴标签或定位
    @PostMapping("{user_uuid}/tag")
    @ResponseBody//自动序列化，自动传回响应体标识，JSON文件返回不加会404找Bug到死，RequestBody针对JSON返回
    public R imageMediaTagging(@PathVariable(value = "user_uuid") String userUUId,
                               @RequestHeader("Server-Access-Key") String serverAk,
                               @RequestHeader("Server-Secret-Key") String serverSk,
                               @RequestBody ImageInfo imageInfo) {
        //查查看是否有这个人的ID注册了，没有就不给使用
        R r = userService.searchOneUserByUserUUId(userUUId);
        if (200 != r.getStatus()){
            return R.error(403, "系统拒绝访问", new Date(), "原因：请求未授权");
        }
        //读取图像的url
        String imgUrl = imageInfo.getImgUrl();
        //读取图像是否需要返回坐标
        String needDet = imageInfo.getNeedDet();
        //读取图像需要哪种语言
        String language = imageInfo.getLanguage();
        //读取图像检测置信度
        String threshold = imageInfo.getThreshold();
        //读取图像物体识别数量
        Integer limit = imageInfo.getLimit();
        //读取图像物体需要调用的云厂商API
        String serverName = imageInfo.getServerName();
        //判断厂商
        if ("huawei".equals(serverName)) {
            if ("true".equals(needDet)) {
                try {
                    List<TagResult> tagResults = runImageMediaTaggingSolution.ImageMediaTaggingDetFunction(imgUrl, language, threshold, limit, serverAk, serverSk);
                    if (tagResults == null) {
                        log.info("Huawei ImageMediaTaggingDetFunction Fail");
                        return R.error(404, "识别失败", new Date(), "未检测到物品");
                    } else {
                        log.info("Huawei ImageMediaTaggingDetFunction Success");
                        return R.success(200, "识别成功", new Date(), tagResults);
                    }
                } catch (ConnectionException | RequestTimeoutException e) {
                    log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
                    return R.error(404, e.getMessage(), new Date(), e.getStackTrace());
                } catch (ServiceResponseException e) {
                    log.error(e.getHttpStatusCode() + e.getErrorCode() + e.getErrorMsg());
                    return R.error(e.getHttpStatusCode(), "识别失败", new Date(), e.getErrorMsg());
                }
            } else {
                try {
                    List<TagResult> tagResults = runImageMediaTaggingSolution.ImageMediaTaggingFunction(imgUrl, language, threshold, limit, serverAk, serverSk);
                    if (tagResults == null) {
                        log.info("Huawei ImageMediaTaggingFunction Fail");
                        return R.error(404, "识别失败", new Date(), "未检测到物品");
                    } else {
                        log.info("Huawei ImageMediaTaggingFunction Success");
                        return R.success(200, "识别成功", new Date(), tagResults);
                    }
                } catch (ConnectionException | RequestTimeoutException e) {
                    log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
                    return R.error(404, e.getMessage(), new Date(), e.getStackTrace());
                } catch (ServiceResponseException e) {
                    log.error(e.getHttpStatusCode() + e.getErrorCode() + e.getErrorMsg());
                    return R.error(e.getHttpStatusCode(), "识别失败", new Date(), e.getErrorMsg());
                }
            }
        } else if ("tencent".equals(serverName)) {
            try {
                List<TagResult> tagResults = detectProduct.detectProductFunction(imgUrl, threshold, serverAk, serverSk);
                if (tagResults == null) {
                    log.info("Tencent ImageMediaTaggingFunction Fail");
                    return R.error(404, "识别失败", new Date(), "未检测到物品");
                } else {
                    log.info("Tencent ImageMediaTaggingFunction Success");
                    return R.success(200, "识别成功", new Date(), tagResults);
                }
            } catch (TencentCloudSDKException e) {
                log.error(e.getErrorCode(), e.getMessage());
                return R.error(400, "识别失败", new Date(), e.getMessage());
            }

        }
        return null;
    }
}
