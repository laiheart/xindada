package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.xindada.model.entity.App;
import generator.service.AppService;
import com.xin.xindada.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
* @author 20441
* @description 针对表【app(应用)】的数据库操作Service实现
* @createDate 2024-06-04 14:11:23
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService{

}




