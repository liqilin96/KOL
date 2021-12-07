package cn.weihu.kol.controller;

import cn.weihu.base.result.ResultBean;
import cn.weihu.kol.biz.FieldsBiz;
import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.biz.bo.FieldsBo;
import cn.weihu.kol.db.po.Fields;
import cn.weihu.kol.db.po.Prices;
import cn.weihu.kol.util.EasyExcelUtil;
import cn.weihu.kol.util.GsonUtils;
import cn.weihu.kol.util.MD5Util;
import com.google.gson.reflect.TypeToken;
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

/**
 * @author lql
 * @date 2021/11/17 21:40
 * Description： 用于导入达人信息的
 */

@RestController
@RequestMapping("/import/execl")
public class ImportExcelStar {

    @Autowired
    private PricesBiz pricesBiz;

    @Autowired
    private FieldsBiz fieldsBiz;


    @ApiOperation(value = "测试数据导入", httpMethod = "POST", notes = "测试数据导入")
    @PostMapping("/import")
    public ResultBean<String> ImportData(@RequestParam(value = "file") MultipartFile file) throws Exception {

        List<Object> data = EasyExcelUtil.readExcel(file.getInputStream());

        Prices prices = null;
                        /*
                             0  ---> "平台",
                             1  ---> "序号",
                             2 ---> "名称",
                             3 ---> "ID",
                             4 ---> "粉丝数",
                             5 ---> "账号类型",
                             6---> "报价形式",
                             7  ---> "@",
                             8  ---> "话题",
                             9 ---> "电商链接",
                            10  ---> "双微转发",
                            11 ---> "报备",
                            12 ---> "微任务",
                            13  ---> "星图/快接单",
                            14  ---> "电商肖像授权",
                            15  ---> "信息流授权",
                            16   ---> "线下探店",
                            17   ---> "费用",
                            18   ---> "佣金",
                            19  ---> "备注",
                            20   ---> "入库时间",
                            21  ---> "保价到期时间",
                            22  ---> "供应商"
                     */

        for(int x = 1; x < data.size(); x++) {
            LinkedHashMap<Integer, String> bo = (LinkedHashMap<Integer, String>) data.get(x);
            if("重新制作".equals(bo.get(6)) || "违约费用".equals(bo.get(6))) {
                continue;
            }
            switch(bo.get(0)) {

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
                    bo.put(21, DateToStr(new Date()));
                    break;
                }
                case "微信": {
                    bo.put(9, "是");
                    bo.put(10, "是");
                    bo.put(14, "是");
                    bo.put(15, "否");
                    bo.put(16, "否");
//                    bo.put(17, bo.get(17) == null ? "否" : bo.get(17));
                    bo.put(21, DateToStr(new Date()));
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
                    bo.put(21, DateToStr(new Date()));
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
                    bo.put(21, DateToStr(new Date()));
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
                    bo.put(21, DateToStr(new Date()));
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
                    bo.put(21, DateToStr(new Date()));
                    break;
                }
            }


            prices = new Prices();
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
            //平台+名称+资源 + MD5
            prices.setActorSn(MD5Util.getMD5(bo.get(0) + bo.get(3) + bo.get(6)));
            prices.setCommission(StringUtils.isNotBlank(bo.get(18)) ? (bo.get(18).substring(0, 2).matches("\\d+") ? Integer.parseInt(bo.get(18).substring(0, 2)) : null) : null);
            prices.setPrice(Double.parseDouble(bo.get(17)));
            prices.setProvider(bo.get(22));
            prices.setInsureEndtime(strToDate(bo.get(21)));
            prices.setCtime(strToDate(bo.get(20)));
            prices.setUtime(new Date());
            prices.setCreateUserId(-1L);
            prices.setUpdateUserId(-1L);
            pricesBiz.savePrices(prices);
        }

        return new ResultBean<>("导入OK了");
    }


    public List<String> titles() {
        return Arrays.asList("媒体", "序号", "账号", "账号ID或链接", "粉丝数",
                             "账号类型", "资源位置", "@", "话题", "电商链接", "品牌双微转发授权", "报备", "微任务",
                             "星图/快接单", "电商肖像授权", "信息流授权", "线下探店", "报价", "佣金", "备注", "入库时间", "保价到期时间", "供应商");
//        return Arrays.asList("平台", "序号", "名称", "ID", "粉丝数", "账号类型", "报价形式", "@", "话题", "电商链接", "双微转发",
//                             "报备", "微任务", "星图/快接单", "电商授权", "肖像授权", "信息流授权", "线下探店", "费用", "佣金", "备注",
//                             "入库时间", "保价到期时间", "供应商");
    }


    public Date strToDate(String time) throws Exception {
        if(StringUtils.isBlank(time))
            return null;
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy/MM/dd");
        Date             date = sdf.parse(time);
        return date;
    }


    //返回6个月后的日子
    private String DateToStr(Date date) throws Exception {
        Calendar         c   = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        c.setTime(date);
        c.add(Calendar.MONTH, 6);
        String format = sdf.format(c.getTime());
        return format;
    }

}
