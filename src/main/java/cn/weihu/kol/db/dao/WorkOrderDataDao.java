package cn.weihu.kol.db.dao;

import cn.weihu.kol.db.po.WorkOrderData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单数据表 Mapper 接口
 * </p>
 *
 * @author Lql
 * @since 2021-11-10
 */
public interface WorkOrderDataDao extends BaseMapper<WorkOrderData> {


    @Insert("<script> " +
            "INSERT INTO kol_work_order_data(fields_id,project_id,work_order_id,status,data,ctime,utime,price_only_day,is_delete) values " +
            "<foreach collection='list' item='item' separator=',' > " +
            " (#{item.fieldsId},#{item.projectId},#{item.workOrderId},#{item.status},#{item.data},#{item.ctime},#{item.utime},#{item.priceOnlyDay}, #{item.isDelete}) " +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("list") List<WorkOrderData> list);

}
