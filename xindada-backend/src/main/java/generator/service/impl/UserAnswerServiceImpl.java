package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.model.entity.UserAnswer;
import generator.service.UserAnswerService;
import com.xin.xindada.mapper.UserAnswerMapper;
import org.springframework.stereotype.Service;

/**
* @author 20441
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service实现
* @createDate 2024-06-04 14:11:23
*/
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
    implements UserAnswerService{

}




