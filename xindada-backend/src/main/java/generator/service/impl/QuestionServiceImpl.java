package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.model.entity.Question;
import generator.service.QuestionService;
import com.xin.xindada.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 20441
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-06-04 14:11:23
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




