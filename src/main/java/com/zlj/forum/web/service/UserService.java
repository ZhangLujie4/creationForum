package com.zlj.forum.web.service;

import com.zlj.forum.common.VO.ResultVO;
import com.zlj.forum.common.dataobject.UserAuthentication;
import com.zlj.forum.common.dataobject.UserRoleEnum;
import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.common.token.TokenProvider;
import com.zlj.forum.common.utils.Blowfish;
import com.zlj.forum.common.utils.ResultVOUtil;
import com.zlj.forum.common.utils.SecurityUtil;
import com.zlj.forum.enums.ResultEnum;
import com.zlj.forum.web.dao.UserJpaDAO;
import com.zlj.forum.web.dataobject.UserDO;
import com.zlj.forum.web.form.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglujie
 * @description
 * @date 2019-05-20 14:31
 */

@Slf4j
@Service
public class UserService {

    /**
     *
     */
    public static long uniqueStartKey = 1558338676;

    @Autowired
    private UserJpaDAO userJpaDAO;

    @Autowired
    private TokenProvider tokenProvider;

    public void register(RegisterForm registerForm) {

        UserDO user = new UserDO();
        String encodePwd;
        try {
            encodePwd = Blowfish.encode(registerForm.getPassword());
        } catch (Exception e) {
            log.error("密码加密失败");
            throw new ResultException(ResultEnum.ENCODE_ERROR);
        }
        user.setUsername(registerForm.getUsername());
        user.setPassword(encodePwd);
        user.setType(UserRoleEnum.ROLE_USER);
        userJpaDAO.save(user);
        user.setUid(user.getId() + System.currentTimeMillis() / 1000 - uniqueStartKey);
        userJpaDAO.save(user);
    }

    public ResultVO login(RegisterForm registerForm) throws Exception {
        UserDO userDO  = userJpaDAO.findByUsername(registerForm.getUsername());
        if (null == userDO) {
            throw new ResultException(ResultEnum.LOGIN_FAIL);
        }

        String encodePwd = Blowfish.encode(registerForm.getPassword());
        if (!userDO.getPassword().equals(encodePwd)) {
            throw new ResultException(ResultEnum.LOGIN_FAIL);
        }

        Map<String, Object> map = new HashMap<>();
        UserAuthentication userAuthentication = new UserAuthentication(SecurityUtil.convertUser(userDO));
        String token = tokenProvider.createToken(userAuthentication);
        map.put("authorization", token);
        return ResultVOUtil.success(map);
    }
}
