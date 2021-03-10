package com.masaiqi.geekbang.web.projects.user.validator.bean.validation;

import com.masaiqi.geekbang.web.projects.user.domain.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidAnnotationValidator implements ConstraintValidator<UserValid, User> {

    @Override
    public void initialize(UserValid annotation) {
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        // Id：必须大于 0 的整数
        if (value.getId() != null) {
            if (value.getId() < 0) {
                return false;
            }
        }

        // 密码：6-32 位
        String password = value.getPassword();
        if (password.length() < 6 || password.length() > 32) {
            return false;
        }

        // 电话号码: 采用中国大陆方式（11 位校验）
        String phoneNumber = value.getPhoneNumber();
        if (phoneNumber.length() != 11) {
            return false;
        }

        return true;
    }


}
