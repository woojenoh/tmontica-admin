package com.internship.tmontica_admin.user;

import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.user.exception.UserException;
import com.internship.tmontica_admin.user.exception.UserExceptionType;
import com.internship.tmontica_admin.user.model.response.AdminSignInResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtService jwtService;

    public void signInCheck(User user) {

        User data = userDao.getUserByUserId(user.getId());
        checkUserIdNotFoundException(user.getId());
        checkPasswordMismatchException(user.getPassword(), data.getPassword());
        checkAdminRoleException(data.getRole());
    }

    public String makeJwtToken(User user){

        return jwtService.getToken(makeTokenUser(user.getId()));
    }

    private User makeTokenUser(String id){

        return userDao.getUserByUserId(id);
    }

    private boolean isExistUser(String id){

        return userDao.getUserByUserId(id)!=null;
    }

    private void checkUserIdNotFoundException(String id){

        if(!isExistUser(id)){
            throw new UserException(UserExceptionType.USER_ID_NOT_FOUND_EXCEPTION);
        }
    }

    private void checkPasswordMismatchException(String password, String comparePassword){

        if(password.equals(comparePassword)){
            return;
        }
        throw new UserException(UserExceptionType.PASSWORD_MISMATCH_EXCEPTION);
    }

    public void checkAdminRoleException(String role){

        for(AdminRole adminRole : AdminRole.values()){
            if(role.equals(adminRole.getRole())){
                return;
            }
        }
        throw new UserException(UserExceptionType.NOT_ADMIN_EXCEPTION);
    }

}
