package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.PricesBiz;
import cn.weihu.kol.db.dao.PricesDao;
import cn.weihu.kol.db.po.Prices;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 达人报价表 服务实现类
 * </p>
 *
 * @author Lql
 * @since 2021-11-11
 */
@Service
public class PricesBizImpl extends ServiceImpl<PricesDao, Prices> implements PricesBiz {

}
