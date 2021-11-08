package cn.weihu.kol.convert;

import cn.weihu.kol.db.po.Permission;
import cn.weihu.kol.http.resp.PermissionResp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionConverter {

    public static List<PermissionResp> list2BoList(List<Permission> list) {
        List<PermissionResp> respList = list.stream()
                .map(PermissionConverter::entity2PermissionResp)
                .collect(Collectors.toList());
        return rebuildList2Tree(respList);
    }

    public static PermissionResp entity2PermissionResp(Permission entity) {
        PermissionResp resp = new PermissionResp();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setUrl(entity.getUrl());
        resp.setIcon(entity.getIcon());
        resp.setStatus(entity.getStatus());
        resp.setType(entity.getType());
        resp.setParentId(entity.getParentId());
        return resp;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    private static List<PermissionResp> rebuildList2Tree(List<PermissionResp> treeNodes) {
        boolean              existRootNode = false;
        List<PermissionResp> newTree       = new ArrayList<>();//初始化一个新的列表
        for(PermissionResp treeNode : treeNodes) {
            if(isRootNode(treeNode, treeNodes)) {
                newTree.add(findChildren(treeNode, treeNodes));
                existRootNode = true;
            }
        }
        if(!existRootNode) {
            return treeNodes;
        }
        return newTree;
    }

    /**
     * 判断节点是否是根节点
     *
     * @param checkNode
     * @param treeNodes
     * @return
     */
    private static boolean isRootNode(PermissionResp checkNode, List<PermissionResp> treeNodes) {
        for(PermissionResp treeNode : treeNodes) {
            if(checkNode.getParentId().equals(treeNode.getId())) {
                return false;
            }
        }
        return true;
    }


    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    private static PermissionResp findChildren(PermissionResp parentNode, List<PermissionResp> treeNodes) {
        List<PermissionResp> children = parentNode.getChildren();
        for(PermissionResp it : treeNodes) {
            if(parentNode.getId().equals(it.getParentId())) {
                children.add(findChildren(it, treeNodes));
            }
        }
        return parentNode;
    }

}
