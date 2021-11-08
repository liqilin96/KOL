package cn.weihu.kol.http.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lql
 * @date 2021/8/17 17:02
 * Description：
 */
@Getter
@Setter
public class MessageReq {

    private String id;

    @ApiModelProperty(value = "是否已读，0未读，1已读")
    private Integer isRead;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "页数")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "条数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "起始时间")
    private Long stime;

    @ApiModelProperty(value = "终止时间")
    private Long etime;

    @ApiModelProperty(value = "消息接受对象")
    private String toObj;
}
