package com.xin.xindada.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.common.ErrorCode;
import com.xin.xindada.constant.CommonConstant;
import com.xin.xindada.exception.ThrowUtils;
import com.xin.xindada.mapper.ScoringResultMapper;
import com.xin.xindada.model.dto.scoringResult.ScoringResultQueryRequest;
import com.xin.xindada.model.entity.ScoringResult;
import com.xin.xindada.model.entity.User;
import com.xin.xindada.model.vo.ScoringResultVO;
import com.xin.xindada.model.vo.UserVO;
import com.xin.xindada.service.ScoringResultService;
import com.xin.xindada.service.UserService;
import com.xin.xindada.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 得分结果服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult> implements ScoringResultService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param scoringResult
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validScoringResult(ScoringResult scoringResult, boolean add) {
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param scoringResultQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest) {
        QueryWrapper<ScoringResult> queryWrapper = new QueryWrapper<>();
        if (scoringResultQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = scoringResultQueryRequest.getId();
        Long notId = scoringResultQueryRequest.getNotId();
        String searchText = scoringResultQueryRequest.getSearchText();
        String sortField = scoringResultQueryRequest.getSortField();
        String sortOrder = scoringResultQueryRequest.getSortOrder();
        Long userId = scoringResultQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取得分结果封装
     *
     * @param scoringResult
     * @param request
     * @return
     */
    @Override
    public ScoringResultVO getScoringResultVO(ScoringResult scoringResult, HttpServletRequest request) {
        // 对象转封装类
        ScoringResultVO scoringResultVO = ScoringResultVO.objToVo(scoringResult);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = scoringResult.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        scoringResultVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long scoringResultId = scoringResult.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
        }
        // endregion

        return scoringResultVO;
    }

    /**
     * 分页获取得分结果封装
     *
     * @param scoringResultPage
     * @param request
     * @return
     */
    @Override
    public Page<ScoringResultVO> getScoringResultVOPage(Page<ScoringResult> scoringResultPage, HttpServletRequest request) {
        List<ScoringResult> scoringResultList = scoringResultPage.getRecords();
        Page<ScoringResultVO> scoringResultVOPage = new Page<>(scoringResultPage.getCurrent(), scoringResultPage.getSize(), scoringResultPage.getTotal());
        if (CollUtil.isEmpty(scoringResultList)) {
            return scoringResultVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ScoringResultVO> scoringResultVOList = scoringResultList.stream().map(scoringResult -> {
            return ScoringResultVO.objToVo(scoringResult);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = scoringResultList.stream().map(ScoringResult::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> scoringResultIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> scoringResultIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> scoringResultIdSet = scoringResultList.stream().map(ScoringResult::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
        }
        // 填充信息
        scoringResultVOList.forEach(scoringResultVO -> {
            Long userId = scoringResultVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            scoringResultVO.setUser(userService.getUserVO(user));
        });
        // endregion

        scoringResultVOPage.setRecords(scoringResultVOList);
        return scoringResultVOPage;
    }

}
