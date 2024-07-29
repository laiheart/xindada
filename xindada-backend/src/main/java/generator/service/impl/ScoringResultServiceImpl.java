package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.model.entity.ScoringResult;
import generator.service.ScoringResultService;
import com.xin.xindada.mapper.ScoringResultMapper;
import org.springframework.stereotype.Service;

/**
* @author 20441
* @description 针对表【scoring_result(评分结果)】的数据库操作Service实现
* @createDate 2024-06-04 14:11:23
*/
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
    implements ScoringResultService{

}




