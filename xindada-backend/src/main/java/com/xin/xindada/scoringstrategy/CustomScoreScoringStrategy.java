package com.xin.xindada.scoringstrategy;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xin.xindada.model.entity.App;
import com.xin.xindada.model.entity.Question;
import com.xin.xindada.model.entity.ScoringResult;
import com.xin.xindada.model.entity.UserAnswer;
import com.xin.xindada.service.ScoringResultService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lsx
 * @date 2024-06-04
 */
@ScoringStrategyType(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        int score = 0;
        List<Question.QuestionContent> questions = app.getQuestion().getQuestionContents();
        for (int i = 0; i < choices.size(); i++) {
            for (Question.Option option : questions.get(i).getOptions()) {
                if (StringUtils.equals(choices.get(i), option.getKey())) {
                    score += option.getScore();
                }
            }
        }
        ScoringResult matchResult = scoringResultService.getOne(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, app.getId())
                        .le(ScoringResult::getResultScoreRange, score)
                        .orderByDesc(ScoringResult::getResultScoreRange)
                        .last("limit 1")
        );

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(matchResult.getId());
        userAnswer.setResultName(matchResult.getResultName());
        userAnswer.setResultDesc(matchResult.getResultDesc());
        userAnswer.setResultPicture(matchResult.getResultPicture());
        userAnswer.setResultScore(score);
        return userAnswer;
    }
}
