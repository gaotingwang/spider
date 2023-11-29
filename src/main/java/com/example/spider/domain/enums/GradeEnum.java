package com.example.spider.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GradeEnum {

    PRIMARY(1, "小学"),
    SECONDORY(2, "中学"),
    HIGH(3, "高中");

    @EnumValue //标记数据库存的值是code
    private final int code;

    @JsonValue //标记响应json值
    private final String desc;

    // Jackson在处理Enum类型上有坑, @JsonValue在int类型上，反序列化根据index进行处理，非实际code进行反序列化，可以使用@JsonCreator
    // https://sunzy.me/2021/03/jackson-enum/
    /*
    @JsonCreator
    public static GradeEnum forValues(Integer code){
        for(GradeEnum gradeEnum : GradeEnum.values()){
            if (gradeEnum.port == code){
                return GradeEnum;
            }
        }
        return null;
    }
    */
}
