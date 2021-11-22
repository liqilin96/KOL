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
     * 媒体、账号、资源位置
     * 含电商链接单价、@、话题、电商肖像权、品牌双微转发授权、微任务
     */
    String TITLE_MEDIA             = "media";
    String TITLE_ACCOUNT           = "account";
    String TITLE_RESOURCE_LOCATION = "address";
    String TITLE_LINK_PRICE        = "linkPrice";
    String TITLE_AT                = "at";
    String TITLE_TOPIC             = "topic";
    String TITLE_STORE_AUTH        = "storeAuth";
    String TITLE_SHARE_AUTH        = "shareAuth";
    String TITLE_MICRO_TASK        = "microTask";

    /**
     * 达人
     * 佣金、价格、供应商、报价到期时间
     */
    String ACTOR_COMMISSION   = "commission";
    String ACTOR_PRICE        = "implPrice";
    String ACTOR_PROVIDER     = "supplier";
    String ACTOR_INSURE       = "priceTime";
    String ACTOR_DATA_SN      = "actorSn";
    String ACTOR_INBOUND      = "inbound";
    String ACTOR_COMPARE_FLAG = "compareFlag";

    /**
     * 字段组类型
     * 1:KOL,2:需求单,3:报价单
     */
    int FIELD_TYPE_KOL    = 1;
    int FIELD_TYPE_DEMAND = 2;
    int FIELD_TYPE_QUOTE  = 3;
}
