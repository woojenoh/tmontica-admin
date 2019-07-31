package com.internship.tmontica_admin.user;

import com.internship.tmontica_admin.user.exception.UserException;
import com.internship.tmontica_admin.user.exception.UserExceptionType;
import com.internship.tmontica_admin.user.model.request.AdminSignInReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void signIn(AdminSignInReqDTO adminSignInReqDTO) {

        User user = userDao.getUserByUserId(adminSignInReqDTO.getId());
        checkUserIdNotFoundException(adminSignInReqDTO.getId());
        checkPasswordMismatchException(adminSignInReqDTO.getPassword(), user.getPassword());
        checkAdminRoleException(user.getRole());
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

    private void checkAdminRoleException(String role){

        for(AdminRole adminRole : AdminRole.values()){
            if(role.equals(adminRole.getRole())){
                return;
            }
        }

        throw new UserException(UserExceptionType.NOT_ADMIN_EXCEPTION);
    }

}
