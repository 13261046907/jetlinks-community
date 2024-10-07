package org.jetlinks.community.device.rk.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.jetlinks.community.device.rk.config.WebConfig;
import org.jetlinks.community.device.rk.utils.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping({"/sporeAnalyzer", "/worm"})
public class DeviceFileController {
    public DeviceFileController() {
    }

    @ResponseBody
    @RequestMapping(
            value = {"/wormImageUpload"},
            method = {RequestMethod.POST}
    )
    public Object wormImageUpload(@RequestParam("file") MultipartFile attach, String deviceAddr, String dateTime) {
        if (!StrUtil.isEmpty(deviceAddr) && !StrUtil.isEmpty(dateTime)) {
            String file = null;

            try {
                Map<String, Object> map = FileUtil.Image(attach, WebConfig.getUploadPath(), 52428800L);
                file = map.get("paths") + "";
            } catch (Exception var8) {
                return "error";
            }

            if (StrUtil.isEmpty(file)) {
                return "error";
            } else {
                Map<String, String> params = new HashMap();
                params.put("deviceAddr", deviceAddr);
                params.put("datetime", dateTime);
                params.put("file", file);
                params.put("file", WebConfig.getUploadPath() + (String)params.get("file"));

                try {
                    String var6 = HttpRequest.post(WebConfig.getFileAddress()).body(JSONUtil.toJsonStr(params), "application/json").execute().body();
                } catch (Exception var7) {
                    var7.printStackTrace();
                }

                return "ok";
            }
        } else {
            return "error!!";
        }
    }

    @ResponseBody
    @RequestMapping(
            value = {"/filesUploadWorm"},
            method = {RequestMethod.POST}
    )
    public Object filesUploadWorm(@RequestParam("file") MultipartFile attach) {
        try {
            Map<String, Object> map = FileUtil.Image(attach, WebConfig.getUploadPath(), 52428800L);
            return map.get("paths");
        } catch (Exception var3) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(
            value = {"/saveFilesUpload"},
            method = {RequestMethod.POST}
    )
    public Object filesUpload(@RequestParam Map<String, String> params) {
        String deviceAddr = (String)params.get("deviceAddr");
        String datetime = (String)params.get("datetime");
        String file = (String)params.get("file");
        params.put("file", WebConfig.getUploadPath() + (String)params.get("file"));

        try {
            String var5 = HttpRequest.post(WebConfig.getFileAddress()).body(JSONUtil.toJsonStr(params), "application/json").execute().body();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return "ok";
    }
}
