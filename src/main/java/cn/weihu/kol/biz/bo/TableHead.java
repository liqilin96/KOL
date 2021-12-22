package cn.weihu.kol.biz.bo;

import lombok.Data;

/**
 * 测试表头导出
 * 字段和excel 表头保持一直，理论上一个类只对应一个导出表头
 */
@Data
public class TableHead {

    private String index;
    private String media;
    private String account;
    private String IDorLink;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIDorLink() {
        return IDorLink;
    }

    public void setIDorLink(String IDorLink) {
        this.IDorLink = IDorLink;
    }
}
