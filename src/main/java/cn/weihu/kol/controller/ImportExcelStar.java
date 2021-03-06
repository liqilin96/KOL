package cn.weihu.kol.controller;

import cn.hutool.core.date.DateUtil;
import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.UserBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.constants.Constants;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.db.po.User;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.GsonUtils;
import cn.weihu.kol.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lql
 * @date 2021/11/17 21:40
 * Description： 用于导入达人信息的
 */

@RestController
@RequestMapping("/import/execl")
@Api(value = "初始化达人数据", tags = "初始化达人数据")
public class ImportExcelStar {

    @Autowired
    private PricesBiz pricesBiz;

    @Autowired
    private FieldsBiz fieldsBiz;

    @Autowired
    private UserBiz userBiz;
    //去重使用
    private List<String> starList = new ArrayList<>();


    @ApiOperation(value = "初始化达人数据导入", httpMethod = "POST", notes = "初始化达人数据导入")
    @PostMapping("/import")
    public ResultBean<String> ImportData(@RequestParam(value = "file") MultipartFile file) throws Exception {

        List<Object> data = EasyExcelUtil.readExcel(file.getInputStream());

        List<Prices> all = new ArrayList<>();
        Prices prices = null;
        //以下序号参考如下列表，如 map.get(0)就是获取平台的数值，列表随导入模板的改动而有所改动
                        /*
                             0  ---> "平台",
                             1  ---> "序号",
                             2 ---> "名称",
                             3 ---> "账号ID",
                             4 ---> "粉丝数",
                             5 ---> "账号类型",
                             6---> "资源形式",
                             7  ---> "@",
                             8  ---> "话题",
                             9 ---> "电商链接",
                            10  ---> "双微转发",
                            11 ---> "报备（小红书）",
                            12 ---> "微任务（微博）",
                            13  ---> "星图/快接单",
                            14  ---> "电商肖像授权",
                            15  ---> "信息流授权",
                            16   ---> "线下探店",
                            17   ---> "报价",
                            18   ---> "佣金",
                            19  ---> "备注",
                            20   ---> "入库时间",
                            21  ---> "保价到期时间",
                            22  ---> "供应商"
                     */
        Date xinyiTime = null;
        Date weigeTime = null;

        //获取供应商合同到期时间
        User xinyi = userBiz.getOne(new LambdaQueryWrapper<>(User.class).eq(User::getName, "xinyi"));
        if(xinyi != null && xinyi.getContractTime() != null) {
            xinyiTime = xinyi.getContractTime();
        }
        User weige = userBiz.getOne(new LambdaQueryWrapper<>(User.class).eq(User::getName, "weige"));
        if(weige != null && weige.getContractTime() != null) {
            weigeTime = weige.getContractTime();
        }

        for(int x = 1; x < data.size(); x++) {
            LinkedHashMap<Integer, String> bo = (LinkedHashMap<Integer, String>) data.get(x);

            String md5 = MD5Util.getMD5(bo.get(0) + bo.get(3) + bo.get(6));
            //去重处理，耗时，可以优化
            if(starList.contains(md5)) {
                continue;
            } else {
                starList.add(md5);
            }

            if(!"重新制作".equals(bo.get(6)) && !"违约费用".equals(bo.get(6))) {
                prices = new Prices();
                String provider = bo.get(22);
                //合同过期时间
                Date time = null;
                if(Constants.SUPPLIER_XIN_YI.equals(provider)) {
                    time = xinyiTime;
                } else if(Constants.SUPPLIER_WEI_GE.equals(provider)) {
                    time = weigeTime;
                } else {

                }

                if(time != null) {
                    if(time.compareTo(DateUtil.offsetMonth(DateUtil.date(), 6)) < 0) {
//                        prices.setInsureEndtime(user.getContractTime());
                        prices.setInsureEndtime(DateUtil.offsetMonth(time, 0));
                        bo.put(21, DateToStr(time,0));
                    } else {
                        prices.setInsureEndtime(DateUtil.offsetMonth(DateUtil.date(), 6));
                        bo.put(21, DateToStr(new Date(),6));
                    }
                } else {
                    prices.setInsureEndtime(strToDate(bo.get(21)));
                }

                switch(bo.get(0)) {
                    //权益赋值
                    case "小红书": {
                        bo.put(7, "是");
                        bo.put(8, "是");
                        bo.put(9, "是");
                        bo.put(10, "否");
                        bo.put(11, "是");
                        bo.put(14, "否");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                    case "微信": {
                        bo.put(9, "是");
                        bo.put(10, "是");
                        bo.put(14, "是");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                    case "微博": {
                        bo.put(7, "是");
                        bo.put(8, "是");
                        bo.put(9, "是");
                        bo.put(10, "是");
                        bo.put(12, "否");
                        bo.put(14, "是");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                    case "抖音": {
                        bo.put(7, "是");
                        bo.put(8, "是");
                        bo.put(9, "是");
                        bo.put(10, "否");
                        bo.put(13, bo.get(13) == null ? "是" : bo.get(13));
//                    bo.put(19, "是");
                        bo.put(14, "否");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                    case "快手": {
                        bo.put(7, "是");
                        bo.put(8, "是");
                        bo.put(9, "是");
                        bo.put(10, "否");
                        bo.put(13, bo.get(13) == null ? "是" : bo.get(13));
                        bo.put(14, "否");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                    case "B站": {
                        bo.put(7, "是");
                        bo.put(8, "是");
                        bo.put(9, "否");
//                    bo.put(19, "否");
                        bo.put(10, "否");
                        bo.put(14, "否");
                        bo.put(15, "否");
                        bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                        break;
                    }
                }
            }

            Fields              fields = fieldsBiz.getById(1);
            Map<String, String> map    = new HashMap<>();
            //获取字段列表
            List<FieldsBo> fieldsBos = GsonUtils.gson.fromJson(fields.getFieldList(), new TypeToken<ArrayList<FieldsBo>>() {
            }.getType());
            List<String> title = titles();
            for(int i = 0; i < fieldsBos.size(); i++) {
                for(int j = 0; j < title.size(); j++) {
                    if(title.get(j).equalsIgnoreCase(fieldsBos.get(i).getTitle())) {
                        map.put(fieldsBos.get(i).getDataIndex(), bo.get(j));
                        break;
                    }
                }
            }

            prices.setActorData(GsonUtils.gson.toJson(map));
            //平台+账号ID+资源 + MD5
            prices.setActorSn(MD5Util.getMD5(bo.get(0) + bo.get(3) + bo.get(6)));
            prices.setCommission(StringUtils.isNotBlank(bo.get(18)) ? (bo.get(18).substring(0, 2).matches("\\d+") ? Integer.parseInt(bo.get(18).substring(0, 2)) : null) : null);
            prices.setPrice(Double.parseDouble(bo.get(17)));
            prices.setProvider(bo.get(22));
            prices.setCtime(strToDate(bo.get(20)));
            prices.setUtime(new Date());
            prices.setCreateUserId(-1L);
            prices.setUpdateUserId(-1L);
            all.add(prices);
        }
        pricesBiz.saveOrUpdateBatch(all,all.size());
        //清空数据，保证下次导入数据的准确性
        starList.clear();
        return new ResultBean<>("导入OK了");
    }


    public List<String> titles() {
        return Arrays.asList("平台", "序号", "名称", "账号ID", "粉丝数", "账号类型", "资源形式", "@", "话题", "电商链接", "双微转发", "报备（小红书）",
                             "微任务（微博）", "星图/快接单", "电商肖像授权", "信息流授权", "线下探店", "报价", "佣金",
                             "备注", "入库时间", "保价到期时间", "供应商");
    }


    public Date strToDate(String time) throws Exception {
        if(StringUtils.isBlank(time))
            return null;
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy/MM/dd");
        Date             date = sdf.parse(time);
        return date;
    }


    //返回指定月后的日子
    private String DateToStr(Date date,int month) throws Exception {
        Calendar         c   = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        String format = sdf.format(c.getTime());
        return format;
    }

    //资源库导出固定表头(按照顺序)
    public static List<String> kolTitle() {

        String[] arr = {"平台", "序号", "名称", "账号ID", "粉丝数", "账号类型", "资源形式", "@", "话题", "电商链接", "双微转发",
                        "电商肖像授权", "信息流授权", "报备（小红书）", "微任务（微博）",
                        "星图/快接单", "线下探店", "报价", "佣金", "备注", "入库时间", "保价到期时间", "供应商"};

        return new ArrayList<>(Arrays.asList(arr));
    }

    public static List<FieldsBo> title2List(List<FieldsBo> fieldsBos, List<String> titles) {

        List<FieldsBo> newList = new ArrayList<>();
        for(String title : titles) {
            for(int i = 0; i < fieldsBos.size(); i++) {
                if(title.equals(fieldsBos.get(i).getTitle())) {
                    newList.add(fieldsBos.get(i));
                }
            }
        }
        return newList;
    }

    public static List<String> list2Title(List<FieldsBo> fieldsBos, List<String> titles) {

        List<FieldsBo> newStr = new ArrayList<>();

        for(int i = 0; i < fieldsBos.size(); i++) {
            boolean flag = false;
            for(String title : titles) {
                if(title.equals(fieldsBos.get(i).getTitle())) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                newStr.add(fieldsBos.get(i));
            }
        }
        return newStr.stream().filter(x -> x.isEffect()).map(FieldsBo::getTitle).collect(Collectors.toList());
    }
}
