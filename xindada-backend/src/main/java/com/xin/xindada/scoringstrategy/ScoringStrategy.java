package com.xin.xindada.scoringstrategy;

import com.xin.xindada.model.entity.App;
import com.xin.xindada.model.entity.UserAnswer;

import java.util.List;

/**
 * @author lsx
 * @date 2024-06-04
 */
public interface ScoringStrategy {

    UserAnswer doScore(List<String> choices, App app);
}
