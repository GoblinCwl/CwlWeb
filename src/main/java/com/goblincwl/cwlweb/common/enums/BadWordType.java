package com.goblincwl.cwlweb.common.enums;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 敏感词类型 Enum
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/10 14:17
 */
@Getter
@AllArgsConstructor
public enum BadWordType {

    //暴恐
    VIOLENCE_COULD("暴恐", 1),
    //反动
    REACTIONARY("反动", 1),
    //民生
    PEOPLE_LIVELIHOOD("民生", 1),
    //色情
    PORN("色情", 1),
    //贪腐
    CORRUPTION("贪腐", 1),
    //辱骂
    ABUSE("辱骂",1),
    //其他
    OTHER("其他", 1);

    /**
     * 违禁词类型名
     */
    private final String name;
    /**
     * 违禁词过滤等级
     */
    private final Integer filterLevel;

}
