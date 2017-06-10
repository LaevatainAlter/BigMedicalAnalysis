package com.bjtu.config;

import com.bjtu.bean.LoginLogBean;
import com.bjtu.bean.UserBean;
import com.bjtu.dao.LoginLogDAO;
import com.bjtu.dao.UserDAO;
import com.bjtu.util.GlobalVariableHolder;
import com.bjtu.util.IPHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjtu.service.JsonService.toJson;

/**
 * Created by Gimling on 2017/4/7.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements AuthenticationSuccessHandler, AuthenticationFailureHandler,UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginLogDAO loginLogDAO;

    private static Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    public static Md5PasswordEncoder getPasswordEncoder() {
        return  passwordEncoder;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this).passwordEncoder(passwordEncoder);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.formLogin().loginPage("/login");
        http.formLogin().loginProcessingUrl("/login");
        http.formLogin().successHandler(this);
        http.formLogin().failureHandler(this);
        http.logout().logoutUrl("/logout")
                // 登陆成功后跳转的地址，以及删除的cookie名称
                .and().logout().logoutSuccessUrl("/login")
                .and().logout().deleteCookies("JSESSIONID");
        http.rememberMe().tokenValiditySeconds(1209600)
                .and().rememberMe().rememberMeParameter("remember-me");
        http.csrf().disable();

        CharacterEncodingFilter encodeFilter = new CharacterEncodingFilter();
        encodeFilter.setEncoding("utf-8");
        encodeFilter.setForceEncoding(true);
        http.addFilterBefore(encodeFilter, CsrfFilter.class); // 放在csrf filter前面
        http.headers().disable();
        HeaderWriter headerWriter = (request, response) -> {
            response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
            response.setHeader("X-XSS-Protection", "1; mode=block");
            response.setHeader("x-content-type-options", "nosniff");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            request.getSession(true);
        };
        List<HeaderWriter> headerWriterFilterList = new ArrayList<HeaderWriter>();
        headerWriterFilterList.add(headerWriter);
        HeaderWriterFilter headerFilter = new HeaderWriterFilter(headerWriterFilterList);
        http.addFilter(headerFilter);
    }

    /*登录成功处理*/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserBean ub = userDAO.findUserById(GlobalVariableHolder.getCurrentUserId());
        try {
            LoginLogBean loginLog = new LoginLogBean();
            loginLog.setRecordIP(IPHelper.getRealIp(request));
            loginLog.setRecordPosition(IPHelper.getIpLocation(loginLog.getRecordIP()));
            loginLog.setUserBean(ub);
            loginLogDAO.saveLoginLog(loginLog);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }
        Map json = new HashMap();
        json.put("status",true);
        response.getWriter().write(toJson(json));
    }

    /*登录失败处理*/
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();
        response.setCharacterEncoding("UTF-8");
        ObjectMapper om = new ObjectMapper();
        Map json = new HashMap();
        json.put("status",false);
        if(session.getAttribute("errorField")==null){
            json.put("errorField","password");
            json.put("errorMsg","密码错误");
        }else{
            json.put("errorField",session.getAttribute("errorField"));
            json.put("errorMsg",session.getAttribute("errorMsg"));
            session.removeAttribute("errorField");
            session.removeAttribute("errorMsg");
        }
        response.getWriter().write(om.writeValueAsString(json));
        response.getWriter().flush();;
    }

    /**
     *
     * @param username 用户名，即邮箱/昵称
     * @return        包含用户基本信息的对象
     * @throws UsernameNotFoundException    未找到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = GlobalVariableHolder.getCurrentRequest();

        HttpSession session = request.getSession();
        //用户输入的验证码
        String kaptcha = request.getParameter("code");
        if(StringUtils.isEmpty(kaptcha)){
            session.setAttribute("errorMsg","验证码不能为空");
            session.setAttribute("errorField","code");
            throw new UsernameNotFoundException("ERROR");
        }
        if(StringUtils.isEmpty(username)){
            session.setAttribute("errorMsg","用户名不能为空");
            session.setAttribute("errorField","username");
            throw new UsernameNotFoundException("ERROR");
        }
        //期望的验证码
        String expect  = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!(kaptcha != null && kaptcha.equalsIgnoreCase(expect))) {
            session.setAttribute("errorMsg","验证码错误");
            session.setAttribute("errorField","code");
            throw new UsernameNotFoundException("ERROR");
        }
        UserBean ub = userDAO.findUserByName(username);
        if(ub==null){
            session.setAttribute("errorMsg","用户不存在");
            session.setAttribute("errorField’","username");
            throw new UsernameNotFoundException("ERROR");
        }else{
            return new com.bjtu.bean.UserDetails(ub);
        }
    }

}