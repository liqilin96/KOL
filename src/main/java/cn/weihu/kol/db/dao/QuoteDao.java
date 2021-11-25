package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.Quote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 达人报价表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-11-21
 */
public interface QuoteDao extends MyMapper<Quote> {

    void batchSaveOrUpdate(@Param("list") List<Quote> list);
}
