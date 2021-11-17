package cn.weihu.kol.util;

import lombok.Data;

@Data
public class BaseResponse {

    private String code;

    private String codeDesc;

    private String thirdPartyId;

    public BaseResponse(String code, String codeDesc, String thirdPartyId) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.thirdPartyId = thirdPartyId;
    }

    public static BaseResponse ok(String thirdPartyId) {
        return new BaseResponse("ok", "成功", thirdPartyId);
    }

    public static BaseResponse fail(String desc) {
        return new BaseResponse("fail", desc, null);
    }

    @Override
    public String toString() {
        return GsonUtils.gson.toJson(this);
    }
}
