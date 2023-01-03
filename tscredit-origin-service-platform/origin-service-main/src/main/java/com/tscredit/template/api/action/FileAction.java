package com.tscredit.template.api.action;



import com.aizuda.oss.OSS;
import com.aizuda.oss.model.OssResult;
import com.aurora.base.entity.response.ActionMessage;
import com.tscredit.template.api.entity.DfsFile;
import com.tscredit.template.api.service.DfsFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "文件", description = "UserAction")
@RequestMapping(value = "/file")
public class FileAction {


    private final DfsFileService dfsFileService;

    public FileAction(DfsFileService dfsFileService) {
        this.dfsFileService = dfsFileService;
    }


    @Operation(summary = "上传文件", description = "上传文件")
    @Parameters({
            @Parameter(name = "file", description = "文件", required = true),
    })
    @PostMapping(value = "/upload.do")
    public ActionMessage fileUpload(@RequestParam MultipartFile file, Integer order, String type) {
        if (file == null) {
            return ActionMessage.error().msg("文件不能为空");
        }

        try {
            //最后一次出现“.”后分割子串 【文件后缀】
            String fileName = file.getOriginalFilename();   //文件名
            String extName = StringUtils.substringAfterLast(fileName, "."); //后缀名
            if (file.getSize() > 3096000 && ("jpg".equals(extName) || "png".equals(extName) || "jpeg".equals(extName))) {
                return ActionMessage.error().msg("文件大小不能大于3M");
            }

            OssResult oss = OSS.fileStorage("default-oss").upload(file.getInputStream(), fileName);
            String url = OSS.fileStorage("default-oss").getUrl(oss.getObjectName());

            // 文件信息保存到数据库
            DfsFile dfsFile = new DfsFile();
            dfsFile.setLocation(url);
            dfsFile.setRealName(fileName);
            dfsFile.setFileGroup(oss.getBucketName());
            dfsFile.setSite(oss.getObjectName());
            dfsFile.setType(type);
            dfsFile.setSort(order);
            dfsFileService.save(dfsFile);

            //结果集封装
            Map<String, Object> result = new HashMap<>();
            result.put("fileId", dfsFile.getId());
            result.put("extName", extName);
            result.put("site", oss.getObjectName());
            return ActionMessage.success().msg("文件上传成功").data(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(ExceptionUtils.getFullStackTrace(e));
            return ActionMessage.error().msg("文件上传失败");
        }
    }

    @Operation(summary = "获取文件URL", description = "获取文件URL")
    @Parameters({
            @Parameter(name = "site", description = "文件标识符", required = true),
    })
    @PostMapping(value = "/getUrl")
    public ActionMessage fileUpload(@RequestParam String site) throws Exception {
        return ActionMessage.success().data(OSS.fileStorage("default-oss").getUrl(site));
    }

    @Operation(summary = "下载文件", description = "下载文件")
    @Parameters({
            @Parameter(name = "file", description = "文件", required = true),
    })
    @PostMapping(value = "/downLoad.do")
    public void downLoad(HttpServletResponse response, @RequestBody String objectName) {
        try {
            InputStream oss = OSS.fileStorage("default-oss").download(objectName);

            response.setHeader("Content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("文件名".getBytes(), StandardCharsets.ISO_8859_1));
            response.setHeader("Content-Encoding", "UTF-8");
            IOUtils.copy(oss, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Operation(summary = "删除文件", description = "删除文件")
    @Parameters({
            @Parameter(name = "file", description = "文件 [字符串集合]", required = true),
    })
    @PostMapping(value = "/delete.do")
    public ActionMessage delete(@RequestBody List<String> objectNames) {
        try {
            OSS.fileStorage("default-oss").delete(objectNames);
            return ActionMessage.success().msg("文件删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionMessage.error().msg("文件删除失败");
        }
    }

    @Operation(summary = "下载模板文件", description = "下载模板文件")
    @Parameters({
    })
    @GetMapping(value = "/downloadFile.do")
    public void downloadFile(HttpServletResponse response, String id, String type) throws IOException {
        OutputStream out = response.getOutputStream();
        // windows 下
        String fileName = "";
        String realName = "";
        if (StringUtils.equalsIgnoreCase("1", type)) {
            fileName = "1.xlsx";
            realName = "厂房数据模板.xlsx";
        } else if (StringUtils.equalsIgnoreCase("3", type)) {
            fileName = "2.xlsx";
            realName = "仓库数据模板.xlsx";
        } else if (StringUtils.equalsIgnoreCase("2", type)) {
            fileName = "3.xlsx";
            realName = "宗地数据模板.xlsx";
        }
        String resource = FileAction.class.getClassLoader().getResource(fileName).getPath();
        String s = new String(resource.getBytes(), StandardCharsets.UTF_8);
        InputStream in = new FileInputStream(s);
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
        IOUtils.copy(in, out);
        in.close();
        out.close();
    }
}
