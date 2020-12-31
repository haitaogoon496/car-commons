package com.mljr.oval;

import lombok.Data;
import net.sf.oval.Validator;
import net.sf.oval.constraint.Assert;

import java.util.Date;

/**
 * @description: 基于 oval 验证
 * @Date : 2018/10/8 下午6:07
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
public class Student {
    @Assert(expr="_value != null && _value > 0" ,lang="javascript",message="性别男性时，[年龄]不能为空且大于0"  ,when="javascript:_this.sex == 1")
    /**
     * 年龄
     */
    private Integer age;
    @Assert(expr="_value != null" ,lang="javascript",message="性别男性时，[入学日期]不能为空"  ,when="javascript:_this.sex == 1")
    /**
     * 入学日期
     */
    private Date enterDate;
    /**
     * 性别
     */
    private Integer sex;


    public static void main(String[] args) {
        Student student = new Student();
        student.setSex(1);
        student.setAge(0);
        //student.setEnterDate(new Date());
        Validator validator = new Validator();
        System.out.println(validator.validate(student).toString());
    }
}
