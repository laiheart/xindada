package com.xin.xindada.model.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xin.xindada.model.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建问题请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionAddRequest implements Serializable {
    /**
     * 应用 id
     */
    @TableField(value = "appId")
    private Long appId;

    private List<Question.QuestionContent> questionContent;

//    private List<Option> options;
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class Option {
//        private String result;
//        private int score;
//        private String value;
//        private String key;
//    }

    private static final long serialVersionUID = 1L;
}