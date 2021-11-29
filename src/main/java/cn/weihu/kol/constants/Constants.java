package cn.weihu.kol.constants;

public interface Constants {

    /**
     * 工单类型：
     * 1：需求工单，2：询价&询档工单，3：报价到期工单，4：重新询价工单
     */
    int WORK_ORDER_DEMAND        = 1;
    int WORK_ORDER_ENQUIRY       = 2;
    int WORK_ORDER_EXPIRE_DEMAND = 3;
    int WORK_ORDER_ENQUIRY_AGAIN = 4;

    /**
     * 工单状态
     * 新建、询价中、已报价、审核中、已下单
     */
    String WORK_ORDER_NEW    = "NEW";
    String WORK_ORDER_ASK    = "ASK";
    String WORK_ORDER_QUOTE  = "QUOTE";
    String WORK_ORDER_REVIEW = "REVIEW";
    String WORK_ORDER_ORDER  = "ORDER";

    /**
     * 工单数据状态
     * 新建、询价、询档、已报价、报价失效、审核中、审核通过、审核驳回、下单
     */
    String WORK_ORDER_DATA_NEW              = "NEW";
    String WORK_ORDER_DATA_ASK_PRICE        = "ASK_PRICE";
    String WORK_ORDER_DATA_ASK_DATE         = "ASK_DATE";
    String WORK_ORDER_DATA_QUOTE            = "QUOTE";
    String WORK_ORDER_DATA_QUOTE_UNSELECTED = "QUOTE_UNSELECTED";
    String WORK_ORDER_DATA_REVIEW           = "REVIEW";
    String WORK_ORDER_DATA_REVIEW_PASS      = "REVIEW_PASS";
    String WORK_ORDER_DATA_REVIEW_REJECT    = "REVIEW_REJECT";
    String WORK_ORDER_DATA_ORDER            = "ORDER";

    /**
     * 供应商
     */
    String SUPPLIER_FIELD  = "supplier";
    String SUPPLIER_XIN_YI = "新意";
    String SUPPLIER_WEI_GE = "微格";

    /**
     * 属性筛选标题
     * 媒体、账号ID或链接、资源位置
     */
    String TITLE_MEDIA             = "media";
    String TITLE_ID_OR_LINK        = "IDorLink";
    String TITLE_RESOURCE_LOCATION = "address";

    /**
     * 达人
     * KOL价格表数据ID,KOL报价表数据ID
     * 佣金、价格、供应商、报价到期时间
     * 数量、发布时间、其他特殊说明、产品提供方、发布内容brief概述
     */
    String ACTOR_KOL_PRICE_ID        = "kolPriceId";
    String ACTOR_KOL_QUOTE_ID        = "kolQuoteId";
    //
    String ACTOR_COMMISSION          = "commission";
    String ACTOR_PRICE               = "price";
    String ACTOR_PROVIDER            = "supplier";
    String ACTOR_INSURE              = "priceTime";
    //
    String ACTOR_DATA_SN             = "actorSn";
    String ACTOR_INBOUND             = "inbound";
    String ACTOR_COMPARE_FLAG        = "compareFlag";
    //
    String ACTOR_COUNT               = "count";
    String ACTOR_POST_START_TIME     = "postStartTime";
    String ACTOR_POST_END_TIME       = "postEndTime";
    String ACTOR_OTHER               = "other";
    String ACTOR_PRODUCT             = "product";
    String ACTOR_BRIEF               = "brief";
    String ACTOR_SCHEDULE_START_TIME = "scheduleStartTime";
    String ACTOR_SCHEDULE_END_TIME   = "scheduleEndTime";

    /**
     * 字段组类型
     * 1:KOL,2:需求单,3:报价单
     */
    int FIELD_TYPE_KOL    = 1;
    int FIELD_TYPE_DEMAND = 2;
    int FIELD_TYPE_QUOTE  = 3;
}
