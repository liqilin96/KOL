package cn.weihu.kol.http.resp;

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
public class MessageResp {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "内容")
    private String message;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    @ApiModelProperty(value = "更新时间")
    private Long utime;

    @ApiModelProperty(value = "是否已读,1是0否")
    private Integer isReceived;
}
