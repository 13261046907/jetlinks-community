package org.jetlinks.community.standalone.goview.v2.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.jetlinks.community.standalone.goview.v2.common.base.BaseController;
import org.jetlinks.community.standalone.goview.v2.common.config.V2Config;
import org.jetlinks.community.standalone.goview.v2.common.domain.AjaxResult;
import org.jetlinks.community.standalone.goview.v2.common.domain.ResultTable;
import org.jetlinks.community.standalone.goview.v2.common.domain.Tablepar;
import org.jetlinks.community.standalone.goview.v2.model.GoviewProject;
import org.jetlinks.community.standalone.goview.v2.model.GoviewProjectData;
import org.jetlinks.community.standalone.goview.v2.model.vo.GoviewProjectVo;
import org.jetlinks.community.standalone.goview.v2.service.IGoviewProjectDataService;
import org.jetlinks.community.standalone.goview.v2.service.IGoviewProjectService;
import org.jetlinks.community.standalone.goview.v2.service.ISysFileService;
import org.jetlinks.community.standalone.goview.v2.util.ConvertUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/api/goview/project")
@Authorize
@Tag(name = "程序管理")
public class GoviewProjectController  extends BaseController {
	@Resource
	private ISysFileService iSysFileService;
	@Resource
	private V2Config v2Config;
	@Resource
	private IGoviewProjectService iGoviewProjectService;
	@Resource
	private IGoviewProjectDataService iGoviewProjectDataService;


    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/list")
    @ResponseBody
    public ResultTable list(Tablepar tablepar){
        Page<GoviewProject> page= new Page<GoviewProject>(tablepar.getPage(), tablepar.getLimit());
        IPage<GoviewProject> iPages=iGoviewProjectService.page(page, new LambdaQueryWrapper<GoviewProject>());
        ResultTable resultTable=new ResultTable();
        resultTable.setData(iPages.getRecords());
        resultTable.setCode(200);
        resultTable.setCount(iPages.getTotal());
        resultTable.setMsg("获取成功");
        return resultTable;
    }


    /**
     * 新增保存
     * @param
     * @return
     */
    //@Log(title = "项目表新增", action = "111")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/create")
    @ResponseBody
    public AjaxResult add(@RequestBody GoviewProject goviewProject){
        goviewProject.setCreateTime(DateUtil.now());
        goviewProject.setState(-1);
        boolean b=iGoviewProjectService.save(goviewProject);
        if(b){
            return successData(200, goviewProject).put("msg", "创建成功");
        }else{
            return error();
        }
    }


    /**
     * 项目表删除
     * @param ids
     * @return
     */
    //@Log(title = "项目表删除", action = "111")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public AjaxResult remove(String ids){
        List<String> lista=ConvertUtil.toListStrArray(ids);
        Boolean b=iGoviewProjectService.removeByIds(lista);
        if(b){
            return success();
        }else{
            return error();
        }
    }

    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody GoviewProject goviewProject)
    {
        Boolean b= iGoviewProjectService.updateById(goviewProject);
        if(b){
            return success();
        }
        return error();
    }


    @ApiOperation(value = "项目重命名", notes = "项目重命名")
    @PostMapping("/rename")
    @ResponseBody
    public AjaxResult rename(@RequestBody GoviewProject goviewProject)
    {

        LambdaUpdateWrapper<GoviewProject> updateWrapper=new LambdaUpdateWrapper<GoviewProject>();
        updateWrapper.eq(GoviewProject::getId, goviewProject.getId());
        updateWrapper.set(GoviewProject::getProjectName, goviewProject.getProjectName());
        Boolean b=iGoviewProjectService.update(updateWrapper);
        if(b){
            return success();
        }
        return error();
    }


    //发布/取消项目状态
    @PutMapping("/publish")
    @ResponseBody
    public AjaxResult updateVisible(@RequestBody GoviewProject goviewProject){
        if(goviewProject.getState()==-1||goviewProject.getState()==1) {

            LambdaUpdateWrapper<GoviewProject> updateWrapper=new LambdaUpdateWrapper<GoviewProject>();
            updateWrapper.eq(GoviewProject::getId, goviewProject.getId());
            updateWrapper.set(GoviewProject::getState, goviewProject.getState());
            Boolean b=iGoviewProjectService.update(updateWrapper);
            if(b){
                return success();
            }
            return error();
        }
        return error("警告非法字段");
    }


    @ApiOperation(value = "获取项目存储数据", notes = "获取项目存储数据")
    @GetMapping("/getData")
    @ResponseBody
    public AjaxResult getData(String projectId, ModelMap map)
    {
        GoviewProject goviewProject= iGoviewProjectService.getById(projectId);

        GoviewProjectData blogText=iGoviewProjectDataService.getProjectid(projectId);
        if(blogText!=null) {
            GoviewProjectVo goviewProjectVo=new GoviewProjectVo();
            BeanUtils.copyProperties(goviewProject,goviewProjectVo);
            goviewProjectVo.setContent(blogText.getContent());
            return AjaxResult.successData(200,goviewProjectVo).put("msg","获取成功");
        }
        return AjaxResult.successData(200, null).put("msg","无数据");

    }

    @ApiOperation(value = "保存项目数据", notes = "保存项目数据")
    @PostMapping("/save/data")
    @ResponseBody
    public AjaxResult saveData(@RequestBody GoviewProjectData data) {

        GoviewProject goviewProject= iGoviewProjectService.getById(data.getProjectId());
        if(goviewProject==null) {
            return error("没有该项目ID");
        }
        GoviewProjectData goviewProjectData= iGoviewProjectDataService.getProjectid(goviewProject.getId());
        if(goviewProjectData!=null) {
            data.setId(goviewProjectData.getId());
            iGoviewProjectDataService.updateById(data);
            return success("数据保存成功");
        }else {
            iGoviewProjectDataService.save(data);
            return success("数据保存成功");
        }
    }


}
