package cn.weihu.kol.constants;

public interface Constants {

    /**
     * 工单类型：
     * 1：需求工单，2：询价&询档工单
     */
    int WORK_ORDER_DEMAND  = 1;
    int WORK_ORDER_ENQUIRY = 2;
    int WORK_ORDER_EXPIRE_DEMAND  = 3;

    /**
     * 工单状态
     * new : 新建
     */
    String WORK_ORDER_NEW = "new";
}
