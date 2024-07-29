package com.xin.xindada.model.dto.question;

import com.xin.xindada.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新问题请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    private Long id;
    List<Question.QuestionContent> questionContent;
}