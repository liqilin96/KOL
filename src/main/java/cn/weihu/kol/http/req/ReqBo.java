package cn.weihu.kol.http.req;

import lombok.Data;

@Data
public class ReqBo {
    String token;
    String mode;
    Object data;
}
