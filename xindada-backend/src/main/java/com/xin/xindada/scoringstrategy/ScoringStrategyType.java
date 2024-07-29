package com.xin.xindada.scoringstrategy;

import com.xin.xindada.model.enums.AppScoringStrategyEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lsx
 * @date 2024-06-06
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScoringStrategyType {
    int appType();

    int scoringStrategy();
}
