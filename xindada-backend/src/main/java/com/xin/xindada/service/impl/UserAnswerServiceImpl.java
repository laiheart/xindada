package com.xin.xindada.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.common.ErrorCode;
import com.xin.xindada.constant.CommonConstant;
import com.xin.xindada.exception.ThrowUtils;
import com.xin.xindada.mapper.UserAnswerMapper;
import com.xin.xindada.model.dto.userAnswer.UserAnswerAddRequest;
import com.xin.xindada.model.dto.userAnswer.UserAnswerQueryRequest;
import com.xin.xindada.model.entity.App;
import com.xin.xindada.model.entity.Question;
import com.xin.xindada.model.entity.UserAnswer;
import com.xin.xindada.model.entity.User;
import com.xin.xindada.model.vo.UserAnswerVO;
import com.xin.xindada.model.vo.UserVO;
import com.xin.xindada.scoringstrategy.ScoringStrategyExecutor;
import com.xin.xindada.service.AppService;
import com.xin.xindada.service.QuestionService;
import com.xin.xindada.service.UserAnswerService;
import com.xin.xindada.service.UserService;
import com.xin.xindada.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户回答服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer> implements UserAnswerService {

    @Resource
    private UserService userService;

    @Resource
    private AppService appService;

    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringStrategyExecutor scoringStrategyExecutor;

    /**
     * 校验数据
     *
     * @param userAnswer
     * @param add        对创建的数据进行校验
     */
    @Override
    public void validUserAnswer(UserAnswer userAnswer, boolean add) {
        ThrowUtils.throwIf(userAnswer == null, ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param userAnswerQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest) {
        QueryWrapper<UserAnswer> queryWrapper = new QueryWrapper<>();
        if (userAnswerQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = userAnswerQueryRequest.getId();
        Long notId = userAnswerQueryRequest.getNotId();
        String searchText = userAnswerQueryRequest.getSearchText();
        String sortField = userAnswerQueryRequest.getSortField();
        String sortOrder = userAnswerQueryRequest.getSortOrder();
        Long userId = userAnswerQueryRequest.getUserId();
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
     * 获取用户回答封装
     *
     * @param userAnswer
     * @param request
     * @return
     */
    @Override
    public UserAnswerVO getUserAnswerVO(UserAnswer userAnswer, HttpServletRequest request) {
        // 对象转封装类
        UserAnswerVO userAnswerVO = UserAnswerVO.objToVo(userAnswer);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = userAnswer.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        userAnswerVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long userAnswerId = userAnswer.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
        }
        // endregion

        return userAnswerVO;
    }

    /**
     * 分页获取用户回答封装
     *
     * @param userAnswerPage
     * @param request
     * @return
     */
    @Override
    public Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage, HttpServletRequest request) {
        List<UserAnswer> userAnswerList = userAnswerPage.getRecords();
        Page<UserAnswerVO> userAnswerVOPage = new Page<>(userAnswerPage.getCurrent(), userAnswerPage.getSize(), userAnswerPage.getTotal());
        if (CollUtil.isEmpty(userAnswerList)) {
            return userAnswerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserAnswerVO> userAnswerVOList = userAnswerList.stream().map(userAnswer -> {
            return UserAnswerVO.objToVo(userAnswer);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userAnswerList.stream().map(UserAnswer::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> userAnswerIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> userAnswerIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> userAnswerIdSet = userAnswerList.stream().map(UserAnswer::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
        }
        // 填充信息
        userAnswerVOList.forEach(userAnswerVO -> {
            Long userId = userAnswerVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            userAnswerVO.setUser(userService.getUserVO(user));
        });
        // endregion

        userAnswerVOPage.setRecords(userAnswerVOList);
        return userAnswerVOPage;
    }

    @Override
    public UserAnswerVO addUserAnswer(UserAnswerAddRequest userAnswerAddRequest, HttpServletRequest request) {
        Long appId = userAnswerAddRequest.getAppId();
        App app = appService.getById(appId);
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        question.setQuestionContents(JSONUtil.toList(question.getQuestionContent(), Question.QuestionContent.class));
        app.setQuestion(question);
        UserAnswer userAnswer = scoringStrategyExecutor.doScore(userAnswerAddRequest.getChoices(), app);
        userAnswer.setUserId(userService.getLoginUser(request).getId());
        this.save(userAnswer);
        UserAnswerVO answerVO = new UserAnswerVO();
        BeanUtils.copyProperties(userAnswer, answerVO);
        return answerVO;
    }

}
