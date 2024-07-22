package org.jetlinks.community.standalone.goview.v2.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetlinks.community.standalone.goview.v2.common.base.BaseController;
import org.jetlinks.community.standalone.goview.v2.common.domain.AjaxResult;
import org.jetlinks.community.standalone.goview.v2.common.domain.ResultTable;
import org.jetlinks.community.standalone.goview.v2.common.domain.Tablepar;
import org.jetlinks.community.standalone.goview.v2.model.vo.HaiWeiData;
import org.jetlinks.community.standalone.goview.v2.service.IHaiWeiDataService;
import org.jetlinks.community.standalone.goview.v2.util.ConvertUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
@RestController
@RequestMapping("/api/haiWei")
@Tag(name = "程序管理")
public class HaiWeiDataController extends BaseController {
	@Resource
	private IHaiWeiDataService iHaiWeiDataService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/list")
    @ResponseBody
    public ResultTable list(Tablepar tablepar){
        Page<HaiWeiData> page= new Page<HaiWeiData>(tablepar.getPage(), tablepar.getLimit());
        IPage<HaiWeiData> iPages=iHaiWeiDataService.page(page, new LambdaQueryWrapper<HaiWeiData>());
        ResultTable resultTable=new ResultTable();
        resultTable.setData(iPages.getRecords());
        resultTable.setCode(200);
        resultTable.setCount(iPages.getTotal());
        resultTable.setMsg("获取成功");
        return resultTable;
    }

    @ApiOperation(value = "获取项目存储数据", notes = "获取项目存储数据")
    @GetMapping("/getData")
    @ResponseBody
    public AjaxResult getData(String id)
    {
        return AjaxResult.successData(200,iHaiWeiDataService.getById(id)).put("msg","获取成功");
    }

    /**
     * 新增保存
     * @param
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/create")
    @ResponseBody
    public AjaxResult add(@RequestBody HaiWeiData haiWellData){
        haiWellData.setCreateTime(DateUtil.now());
        boolean b=iHaiWeiDataService.save(haiWellData);
        if(b){
            return successData(200, haiWellData).put("msg", "创建成功");
        }else{
            return error();
        }
    }


    /**
     * 项目表删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public AjaxResult remove(String ids){
        List<String> lista=ConvertUtil.toListStrArray(ids);
        Boolean b=iHaiWeiDataService.removeByIds(lista);
        if(b){
            return success();
        }else{
            return error();
        }
    }

    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody HaiWeiData haiWeiData)
    {
        Boolean b= iHaiWeiDataService.updateById(haiWeiData);
        if(b){
            return success();
        }
        return error();
    }

}
