package com.xin.xindada.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xin.xindada.model.entity.App;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 应用视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class AppVO implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名
     */
    @TableField(value = "appName")
    private String appName;

    /**
     * 应用描述
     */
    @TableField(value = "appDesc")
    private String appDesc;

    /**
     * 应用图标
     */
    @TableField(value = "appIcon")
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    @TableField(value = "appType")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @TableField(value = "scoringStrategy")
    private Integer scoringStrategy;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    @TableField(value = "reviewStatus")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @TableField(value = "reviewMessage")
    private String reviewMessage;

    /**
     * 审核人 id
     */
    @TableField(value = "reviewerId")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @TableField(value = "reviewTime")
    private Date reviewTime;

    /**
     * 创建用户 id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param appVO
     * @return
     */
    public static App voToObj(AppVO appVO) {
        if (appVO == null) {
            return null;
        }
        App app = new App();
        BeanUtils.copyProperties(appVO, app);
        return app;
    }

    /**
     * 对象转封装类
     *
     * @param app
     * @return
     */
    public static AppVO objToVo(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        return appVO;
    }
}
