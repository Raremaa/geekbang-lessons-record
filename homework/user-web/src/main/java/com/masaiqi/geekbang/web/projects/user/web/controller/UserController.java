package com.masaiqi.geekbang.web.projects.user.web.controller;

import com.masaiqi.geekbang.mvc.annotation.ResponseType;
import com.masaiqi.geekbang.mvc.controller.Controller;
import com.masaiqi.geekbang.web.context.ComponentContext;
import com.masaiqi.geekbang.web.projects.user.domain.User;
import com.masaiqi.geekbang.web.projects.user.service.UserService;
import com.masaiqi.geekbang.web.projects.user.service.impl.IUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static com.masaiqi.geekbang.mvc.annotation.ResponseType.FORWARD;

/**
 * 用户控制器，Singleton模式，由Simple-mvc的SPI进行实例化
 *
 * @author masaiqi
 * @date 2021/3/1 9:23 PM
 */
@Path("/user")
public class UserController implements Controller {

    private UserService userService;

    @POST
    @Path("/register")
    @ResponseType(value = FORWARD)
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String inputName = request.getParameter("inputName");
        String inputPassword = request.getParameter("inputPassword");
        String inputEmail = request.getParameter("inputEmail");
        String inputPhoneNumber = request.getParameter("inputPhoneNumber");
        User user = new User();
        user.setName(inputName);
        user.setPassword(inputPassword);
        user.setEmail(inputEmail);
        user.setPhoneNumber(inputPhoneNumber);
        userService.register(user);
        return "login-success.jsp";
    }

    @POST
    @Path("/login")
    @ResponseType(value = FORWARD)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String inputName = request.getParameter("inputName");
        String inputPassword = request.getParameter("inputPassword");
        User user = userService.queryUserByNameAndPassword(inputName, inputPassword);
        if(user.getId() != null) {
            return "login-success.jsp";
        }else {
            return "login-fail.jsp";
        }
    }

    @Override
    public void init() {
        this.userService = ComponentContext.getInstance().<UserService>getComponent("bean/UserService");
    }
}
