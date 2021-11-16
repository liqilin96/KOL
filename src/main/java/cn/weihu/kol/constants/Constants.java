package cn.weihu.kol.constants;

public interface Constants {

    /**
     * 工单类型：
     * 1：需求工单，2：询价&询档工单，3：报价到期工单
     */
    int WORK_ORDER_DEMAND        = 1;
    int WORK_ORDER_ENQUIRY       = 2;
    int WORK_ORDER_EXPIRE_DEMAND = 3;

    /**
     * 工单状态
     * 新建、询价、审核、下单
     */
    String WORK_ORDER_NEW    = "NEW";
    String WORK_ORDER_ASK    = "ASK";
    String WORK_ORDER_REVIEW = "REVIEW";
    String WORK_ORDER_ORDER  = "ORDER";

    /**
     * 工单数据状态
     * 新建、询价、询档、审核、下单
     */
    String WORK_ORDER_DATA_NEW       = "NEW";
    String WORK_ORDER_DATA_ASK_PRICE = "ASK_PRICE";
    String WORK_ORDER_DATA_ASK_DATE  = "ASK_DATE";
    String WORK_ORDER_DATA_REVIEW    = "REVIEW";
    String WORK_ORDER_DATA_ORDER     = "ORDER";
}
