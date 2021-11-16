package cn.weihu.kol.biz.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/11/16 9:58
 * Description：
 */
@Setter
@Getter
public class WorkOrderBo {

    private String id;

    private String platform;

    private String account;

    private String accountId;

    private String accountType;

    private Integer fans;

    private String local;
    private Integer num;

    private String startTime;

    private String endTime;

    private String price;

    private String isQuan;
    private String 话题;
    private String 肖像权;
    private String 转发授权;
    private String 微任务;
    private String 其他说明;
    private String 提供方;
    private String 概述;


}
