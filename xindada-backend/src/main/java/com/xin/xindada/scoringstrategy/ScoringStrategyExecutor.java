package com.xin.xindada.scoringstrategy;

import com.xin.xindada.common.ErrorCode;
import com.xin.xindada.exception.BusinessException;
import com.xin.xindada.model.entity.*;
import com.xin.xindada.service.ScoringResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lsx
 * @date 2024-06-04
 */
@Service
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    public UserAnswer doScore(List<String> choices, App app) {
        for (ScoringStrategy strategy : scoringStrategyList) {
            ScoringStrategyType annotation = strategy.getClass().getAnnotation(ScoringStrategyType.class);
            Integer appType = app.getAppType();
            Integer appScoringStrategy = app.getScoringStrategy();
            if (appType == annotation.appType() && appScoringStrategy == annotation.scoringStrategy()) {
                return strategy.doScore(choices, app);
            }
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "未找到匹配的计分策略");
    }

}
