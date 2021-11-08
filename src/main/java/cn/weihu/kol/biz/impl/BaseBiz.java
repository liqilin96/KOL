//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.weihu.kol.biz.impl;

import cn.weihu.kol.biz.Biz;
import cn.weihu.kol.db.dao.MyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public abstract class BaseBiz<M extends MyMapper<T>, T> extends ServiceImpl<M, T> implements Biz<T> {
    public BaseBiz() {
    }
}
