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

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "接受对象")
    private String toObject;

    @ApiModelProperty(value = "发布人id")
    private String publishUserId;

    @ApiModelProperty(value = "发布人")
    private String publishUser;

    @ApiModelProperty(value = "发布时间")
    private Long publishTime;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    @ApiModelProperty(value = "更新时间")
    private Long utime;
}
