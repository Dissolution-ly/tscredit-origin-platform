package com.tscredit.template.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.template.api.dao.DfsFileMapper;
import com.tscredit.template.api.entity.DfsFile;
import com.tscredit.template.api.service.DfsFileService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lixuanyu
 * @since 2021-04-21
 */
@Service
public class DfsFileServiceImpl extends ServiceImpl<DfsFileMapper, DfsFile> implements DfsFileService {

    private final DfsFileMapper dfsFileMapper;


    public DfsFileServiceImpl(DfsFileMapper dfsFileMapper) {
        this.dfsFileMapper = dfsFileMapper;
    }

    /**
     * @param ids       文件id集合
     * @param type
     * @param isNotUsed 参数为false时，删除ids中所有数据。 为true时，只删除没有使用过的文件(文件的fid为null)
     * @return 是否成功
     */
    @Override
    public boolean delFile(List<String> ids, String type, boolean isNotUsed) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }

        QueryWrapper<DfsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.in("id", ids);
        if (isNotUsed) {
            queryWrapper.isNull("fid");
        }
        List<DfsFile> dfsFiles = dfsFileMapper.selectList(queryWrapper);

        // 遍历删除 fastdfs 文件
        for (DfsFile dfsFile : dfsFiles) {
            try {
                // TODO 原来是 fastdfs，选在改成 阿里云OSS 了，二者并不兼容，如有需要，则重写下面注释
//                OSS.fileStorage("aliyun").delete();
                if (!removeById(dfsFile.getId())) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        // 如果在这里使用 return removeByIds(ids) 可能出现文件没有被删除,但数据库被删除的情况
        return true;
    }

    @Override
    public void updateByObjId(Integer objId, String... fileIds) {
        if (fileIds != null && fileIds.length > 0) {
            QueryWrapper<DfsFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", fileIds);

            DfsFile dfsFile = new DfsFile();
            dfsFile.setFid(objId);
            dfsFileMapper.update(dfsFile, queryWrapper);
        }
    }

    @Override
    public List<String> getIds(Integer fid) {
        return dfsFileMapper.getIds(fid);
    }

/*
    @Override
    public List<String> getBanner() {
        QueryWrapper<DfsFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DfsFile::getStatus, 1).eq(DfsFile::getType, 0).orderByAsc(DfsFile::getOrderd).orderByAsc(DfsFile::getIdt);
        return dfsFileMapper.getBanner(queryWrapper);
    }

    @Override
    public List<String> getPicList(QueryWrapper<DfsFile> picQueryWrapper) {
        return dfsFileMapper.getBanner(picQueryWrapper);
    }



    @Override
    public List<Map<String, Object>> getBannerList() {
        QueryWrapper<DfsFile> picQueryWrapper = new QueryWrapper<>();
        picQueryWrapper.lambda().eq(DfsFile::getType, 0).eq(DfsFile::getStatus, 1);
        List<Map<String, Object>> bannerList = dfsFileMapper.getBannerList(picQueryWrapper);
        return bannerList;
    }

    @Override
    public void setEntScoreAndLogo() {
        HashMap<String, String> parameterMap = new HashMap<>();
        HashMap<String, String> parameterMap2 = new HashMap<>();
        List<String> entList = picMapper.getEntList();
        for (String entName : entList) {
            parameterMap.put("keyWord", entName);
            parameterMap.put("type", "1");
            parameterMap.put("tswsNum", "Tsws00");
            Map<String, Object> map = entSearchService.getInterfaceSearchInfoTsws(parameterMap);
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            Integer strength = MapUtils.getInteger(data, "STRENGTH");

            parameterMap2.put("entName", entName);
            parameterMap2.put("dcwsNum", "Dcws153");
            Map<String, Object> map1 = entSearchService.getInterfaceSearchInfoDwcs(parameterMap2);
            List<Map<String, String>> data1 = (List<Map<String, String>>) map1.get("data");
            String logo = null;
            if (CollectionUtils.isNotEmpty(data1)) {
                logo = data1.get(0).get("LOGO");
            }
            dfsFileMapper.setEntScoreAndLogo(entName, strength, logo);
        }
    }

    @Override
    public void setEntScore() {
        List<String> entList = dfsFileMapper.getEntList();
        for (String entName : entList) {
            this.setEntScore(entName);
        }
    }

    @Override
    public void setEntLogo() {
        List<String> entList = dfsFileMapper.getEntList();
        for (String entName : entList) {
            this.setEntLogo(entName);
        }
    }

    @Override
    public void delWasteFile() {
        dfsFileMapper.delWasteFile();
    }

    @Override
    public void createNewCompany(String company) {

    }

    @Override
    public int getCompanyCountByName(String company) {
        int count = dfsFileMapper.getCompanyCountByName(company);
        return count;
    }

    *//**
     * 给指定名称的公司匹配分数
     * @param company 公司名称
     *//*
    private void setEntScore(String company) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("keyWord", company);
        parameterMap.put("type", "1");
        parameterMap.put("tswsNum", "Tsws00");
        Map<String, Object> map = entSearchService.getInterfaceSearchInfoTsws(parameterMap);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        Integer strength = MapUtils.getInteger(data, "STRENGTH");

        dfsFileMapper.setEntScore(company, strength);
    }

    */

    /**
     * 给指定名称的公司匹配logo
     *//*
    private void setEntLogo(String company) {
        Map<String, String> parameterMap2 = new HashMap<>();
        parameterMap2.put("entName", company);
        parameterMap2.put("dcwsNum", "Dcws153");
        Map<String, Object> map1 = entSearchService.getInterfaceSearchInfoDwcs(parameterMap2);
        List<Map<String, String>> data1 = (List<Map<String, String>>) map1.get("data");
        String logo = null;
        if (CollectionUtils.isNotEmpty(data1)) {
            logo = data1.get(0).get("LOGO");
        }
        dfsFileMapper.setEntLogo(company, logo);
    }*/
}
