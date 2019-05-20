package io.choerodon.devops.api.controller.v1;

import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.base.annotation.Permission;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.devops.api.dto.CheckAuditDTO;
import io.choerodon.devops.api.dto.IamUserDTO;
import io.choerodon.devops.api.dto.PipelineCheckDeployDTO;
import io.choerodon.devops.api.dto.PipelineDTO;
import io.choerodon.devops.api.dto.PipelineRecordDTO;
import io.choerodon.devops.api.dto.PipelineRecordListDTO;
import io.choerodon.devops.api.dto.PipelineRecordReqDTO;
import io.choerodon.devops.api.dto.PipelineReqDTO;
import io.choerodon.devops.api.dto.PipelineUserRecordRelDTO;
import io.choerodon.devops.api.dto.iam.UserDTO;
import io.choerodon.devops.app.service.PipelineService;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;

/**
 * Creator: ChangpingShi0213@gmail.com
 * Date:  19:48 2019/4/3
 * Description:
 */
@RestController
@RequestMapping(value = "/v1/projects/{project_id}/pipeline")
public class PipelineController {
    @Autowired
    private PipelineService pipelineService;

    /**
     * 项目下创建流水线
     *
     * @param projectId      项目id
     * @param pipelineReqDTO 流水线信息
     * @return PipelineAppDeployDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "项目下创建流水线")
    @PostMapping
    public ResponseEntity create(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "应用信息", required = true)
            @RequestBody PipelineReqDTO pipelineReqDTO) {
        return Optional.ofNullable(pipelineService.create(projectId, pipelineReqDTO))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.create"));
    }


    /**
     * 项目下更新流水线
     *
     * @param projectId      项目id
     * @param pipelineReqDTO 流水线信息
     * @return PipelineAppDeployDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "项目下更新流水线")
    @PutMapping
    public ResponseEntity<PipelineReqDTO> update(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "应用信息", required = true)
            @RequestBody PipelineReqDTO pipelineReqDTO) {
        return Optional.ofNullable(pipelineService.update(projectId, pipelineReqDTO))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.update"));
    }

    /**
     * 项目下删除流水线
     *
     * @param projectId  项目Id
     * @param pipelineId 流水线Id
     * @return PipelineAppDeployDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "项目下删除流水线")
    @DeleteMapping(value = "/{pipeline_id}")
    public ResponseEntity delete(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_id") Long pipelineId) {
        pipelineService.delete(projectId, pipelineId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 启/停用流水线
     *
     * @param projectId  项目id
     * @param pipelineId 流水线Id
     * @param isEnabled  是否启用
     * @return PipelineDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "启/停用流水线")
    @PutMapping(value = "/{pipeline_id}")
    public ResponseEntity<PipelineDTO> updateIsEnabled(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_id") Long pipelineId,
            @ApiParam(value = "是否启用", required = true)
            @RequestParam Integer isEnabled) {
        return Optional.ofNullable(pipelineService.updateIsEnabled(projectId, pipelineId, isEnabled))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.update.enable "));
    }

    /**
     * 查询流水线详情
     *
     * @param projectId  项目id
     * @param pipelineId 流水线Id
     * @return PipelineReqDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "查询流水线详情")
    @GetMapping(value = "/{pipeline_id}/detail")
    public ResponseEntity<PipelineReqDTO> queryById(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_id") Long pipelineId) {
        return Optional.ofNullable(pipelineService.queryById(projectId, pipelineId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.query"));
    }

    /**
     * 项目下获取流水线
     *
     * @param projectId   项目Id
     * @param pageRequest 分页参数
     * @param params      查询参数
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "项目下获取流水线")
    @CustomPageRequest
    @PostMapping("/list_by_options")
    public ResponseEntity<Page<PipelineDTO>> listByOptions(
            @ApiParam(value = "项目Id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "分页参数")
            @ApiIgnore PageRequest pageRequest,
            @ApiParam(value = "查询参数")
            @RequestBody(required = false) String params) {
        return Optional.ofNullable(pipelineService.listByOptions(projectId, pageRequest, params))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.list"));
    }

    /**
     * 项目下获取流水线记录
     *
     * @param projectId   项目Id
     * @param pageRequest 分页参数
     * @param params      查询参数
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "项目下获取流水线记录")
    @CustomPageRequest
    @PostMapping("/list_record")
    public ResponseEntity<Page<PipelineRecordDTO>> listRecords(
            @ApiParam(value = "项目Id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = false)
            @RequestParam(value = "pipeline_id", required = false) Long pipelineId,
            @ApiParam(value = "分页参数")
            @ApiIgnore PageRequest pageRequest,
            @ApiParam(value = "查询参数")
            @RequestBody(required = false) String params) {
        return Optional.ofNullable(pipelineService.listRecords(projectId, pipelineId, pageRequest, params))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.list.record"));
    }


    /**
     * 执行流水线
     *
     * @param projectId  项目id
     * @param pipelineId 流水线Id
     * @return PipelineReqDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "执行流水线")
    @GetMapping(value = "/{pipeline_id}/execute")
    public ResponseEntity execute(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_id") Long pipelineId) {
        pipelineService.execute(projectId, pipelineId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 人工审核
     *
     * @param projectId
     * @param userRecordRelDTO
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "人工审核")
    @PostMapping("/audit")
    public ResponseEntity<List<IamUserDTO>> audit(
            @ApiParam(value = "项目Id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "PipelineUserRelDTO", required = true)
            @RequestBody PipelineUserRecordRelDTO userRecordRelDTO) {
        return Optional.ofNullable(pipelineService.audit(projectId, userRecordRelDTO))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.audit.check"));
    }

    /**
     * 人工审核预检
     *
     * @param projectId
     * @param userRecordRelDTO
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "人工审核")
    @PostMapping("/check_audit")
    public ResponseEntity<CheckAuditDTO> checkAudit(
            @ApiParam(value = "项目Id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "PipelineUserRelDTO", required = true)
            @RequestBody PipelineUserRecordRelDTO userRecordRelDTO) {
        return Optional.ofNullable(pipelineService.checkAudit(projectId, userRecordRelDTO))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.audit.check"));
    }


    /**
     * 条件校验
     *
     * @param projectId
     * @param pipelineId
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "条件校验")
    @GetMapping("/check_deploy")
    public ResponseEntity<PipelineCheckDeployDTO> checkDeploy(
            @ApiParam(value = "项目Id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "记录Id", required = true)
            @RequestParam(value = "pipeline_id") Long pipelineId) {
        return Optional.ofNullable(pipelineService.checkDeploy(projectId, pipelineId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.check.deploy"));
    }

    /**
     * 查询流水线记录详情
     *
     * @param projectId 项目id
     * @param recordId  流水线记录Id
     * @return PipelineRecordReqDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "查询流水线记录详情")
    @GetMapping(value = "/{pipeline_record_id}/record_detail")
    public ResponseEntity<PipelineRecordReqDTO> getRecordById(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_record_id") Long recordId) {
        return Optional.ofNullable(pipelineService.getRecordById(projectId, recordId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.record.query"));
    }

    /**
     * 流水线重试
     *
     * @param projectId 项目id
     * @param recordId  流水线记录Id
     * @return PipelineRecordReqDTO
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "流水线重试")
    @GetMapping(value = "/{pipeline_record_id}/retry")
    public ResponseEntity<Boolean> retry(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线记录Id", required = true)
            @PathVariable(value = "pipeline_record_id") Long recordId) {
        return Optional.ofNullable(pipelineService.retry(projectId, recordId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.retry"));
    }

    /**
     * 流水线所有记录
     *
     * @param projectId  项目id
     * @param pipelineId 流水线记录Id
     * @return List<PipelineRecordDTO>
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "流水线所有记录")
    @GetMapping(value = "/{pipeline_id}/list")
    public ResponseEntity<List<PipelineRecordListDTO>> queryByPipelineId(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线Id", required = true)
            @PathVariable(value = "pipeline_id") Long pipelineId) {
        return Optional.ofNullable(pipelineService.queryByPipelineId(pipelineId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.record.list"));
    }

    /**
     * @param projectId 项目id
     * @param name      流水线名称
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "名称校验")
    @GetMapping(value = "/check_name")
    public ResponseEntity checkName(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线名称", required = true)
            @RequestParam(value = "name") String name) {
        pipelineService.checkName(projectId, name);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 获取所有流水线
     *
     * @param projectId 项目id
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "获取所有流水线")
    @GetMapping(value = "/all_pipeline")
    public ResponseEntity<List<PipelineDTO>> listPipelineDTO(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId) {
        return Optional.ofNullable(pipelineService.listPipelineDTO(projectId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.pipeline.all.list"));
    }


    /**
     * 获取所有项目成员和项目所有者
     *
     * @param projectId 项目id
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "获取所有项目成员和项目所有者")
    @GetMapping(value = "/all_users")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId) {
        return Optional.ofNullable(pipelineService.getAllUsers(projectId))
                .map(target -> new ResponseEntity<>(target, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.users.all.list"));
    }

    /**
     * 获取所有项目成员和项目所有者
     *
     * @param projectId 项目id
     * @return
     */
    @Permission(roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "获取所有项目成员和项目所有者")
    @GetMapping(value = "/stop")
    public ResponseEntity stop(
            @ApiParam(value = "项目id", required = true)
            @PathVariable(value = "project_id") Long projectId,
            @ApiParam(value = "流水线记录Id", required = true)
            @RequestParam(value = "pipeline_record_id") Long recordId) {
        pipelineService.stop(projectId, recordId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
