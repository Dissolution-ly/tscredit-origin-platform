package com.tscredit.template.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.template.api.entity.DfsFile;

import java.util.List;

/**
 * @author lixuanyu
 * @since 2021-04-21
 */
public interface DfsFileService extends IService<DfsFile> {

    /**
     * 删除文件
     *
     * @param ids       文件id集合
     * @param type
     * @param isNotUsed 参数为false时，删除ids中所有数据。 为true时，只删除没有使用过的文件(文件的fid为null)
     * @return 是否成功
     */
    boolean delFile(List<String> ids, String type, boolean isNotUsed);

    void updateByObjId(Integer objId, String... fileIds);

    List<String> getIds(Integer id);

}
