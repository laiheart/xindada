package com.xin.xindada.scoringstrategy;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xin.xindada.model.entity.*;
import com.xin.xindada.service.ScoringResultService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lsx
 * @date 2024-06-04
 */
@ScoringStrategyType(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy{

    @Resource
    private ScoringResultService scoringResultService;
    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        Map<String, Integer> scoreCount = new HashMap<>();
        List<Question.QuestionContent> questions = app.getQuestion().getQuestionContents();
        for (int i = 0; i < choices.size(); i++) {
            for (Question.Option option : questions.get(i).getOptions()) {
                if (StringUtils.equals(choices.get(i), option.getKey())) {
                    String result = option.getResult();
                    scoreCount.put(result, scoreCount.getOrDefault(result, 0) + 1);
                }
            }
        }
        List<ScoringResult> resultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, app.getId())
        );
        int maxScore = 0;
        ScoringResult matchResult = resultList.get(0);
        for (ScoringResult result : resultList) {
            List<String> resultPropList = JSONUtil.toList(result.getResultProp(), String.class);
            int score = resultPropList.stream().mapToInt(prop -> scoreCount.getOrDefault(prop, 0)).sum();
            if (score > maxScore) {
                maxScore = score;
                matchResult = result;
            }
        }

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(matchResult.getId());
        userAnswer.setResultName(matchResult.getResultName());
        userAnswer.setResultDesc(matchResult.getResultDesc());
        userAnswer.setResultPicture(matchResult.getResultPicture());
        userAnswer.setResultScore(maxScore);
        return userAnswer;
    }
}
