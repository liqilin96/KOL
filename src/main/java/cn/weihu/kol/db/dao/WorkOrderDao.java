package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.WorkOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 工单表 Mapper 接口
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface WorkOrderDao extends BaseMapper<WorkOrder> {

    @Select("SELECT count(1) FROM kol_work_order WHERE project_id = #{projectId}")
    Integer getCount(@Param("projectId") Long projectId);
}
