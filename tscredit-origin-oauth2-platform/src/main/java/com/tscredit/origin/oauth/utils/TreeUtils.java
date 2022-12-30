package com.tscredit.origin.oauth.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java 对象快速构建树形数据
 */
public class TreeUtils {
    /**
     * 构建树形结构
     *
     * @param list      源数据
     * @param findChild
     * @param <T>       数据类型
     * @return
     */
    public static <T> List<Map<String, Object>> buildTree(List<T> list, FindChild<T> findChild) {
        List<Map<String, Object>> rootList = new ArrayList<>();
        for (T child : list) {
            Map<String, Object> root = new HashMap<>();
            T rootRow = findChild.findRoot(child);
            if (rootRow != null) {
                buildChildren(root, rootRow, list, findChild);
                rootList.add(root);
            }
        }
        return rootList;
    }


    /**
     * Java 对象构建树形结构
     *
     * @param rootMap
     * @param root
     * @param list
     * @param findChild
     * @param <T>
     */
    private static <T> void buildChildren(Map<String, Object> rootMap, T root, List<T> list, FindChild<T> findChild) {
        List<T> childrenList = findChild.findChildren(root, list);
        Map<String, Object> extendsMap = new HashMap<>();
        //扩展属性
        findChild.buildExtendProperty(extendsMap, root, childrenList);
        if (childrenList != null && childrenList.size() > 0) {
            List<Map<String, Object>> cl = new ArrayList<>();
            for (T c : childrenList) {
                Map<String, Object> element = new HashMap<>();
                cl.add(element);
                buildChildren(element, c, list, findChild);
            }
            rootMap.put("children", cl);
        }
        Map<String, Object> objProperty = findChild.buildObjectToMap(root);
        rootMap.putAll(objProperty);
        rootMap.putAll(extendsMap);
        if (!rootMap.containsKey("children")) {
            rootMap.put("children", null);
        }
    }

    /**
     * 寻找节点
     *
     * @param <T>
     */
    public interface FindChild<T> {
        /**
         * 筛选List集合
         */
        T findRoot(T row);

        /**
         * 查找所有子
         *
         * @param rootRow
         * @param list
         * @return
         */
        List<T> findChildren(T rootRow, List<T> list);

        /**
         * 节点扩展属性
         *
         * @param property
         * @param element
         * @param children
         */
        void buildExtendProperty(Map<String, Object> property, T element, List<T> children);

        /**
         * 节点转Map
         *
         * @param element
         * @return
         */
        Map<String, Object> buildObjectToMap(T element);
    }

}
