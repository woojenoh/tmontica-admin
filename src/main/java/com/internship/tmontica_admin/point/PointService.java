package com.internship.tmontica_admin.point;

import com.internship.tmontica_admin.point.exception.PointException;
import com.internship.tmontica_admin.point.exception.PointExceptionType;
import com.internship.tmontica_admin.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//JWTSERVICE 관련 메소드들은 컨트롤러단에서 커트하는게 이득이니 컨트롤러로 가는게 좋을듯..?
//user는 이미 서비스에서 동작하고있으니 우선 통일하고 나중에 고려해서 리팩토링..
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointDao pointDao;
    private final UserDao userDao;

    @Transactional
    public void updateUserPoint(Point point){

        int finalPoint = getFinalPoint(point);
        if(finalPoint < 0){
            throw new PointException(PointExceptionType.POINT_LESS_THEN_ZERO_EXCEPTION);
        }

        if(userDao.updateUserPoint(finalPoint, point.getUserId()) < 1){
            throw new PointException(PointExceptionType.DATABASE_FAIL_EXCEPTION);
        }

        if(pointDao.addPoint(point) < 1){
            throw new PointException(PointExceptionType.DATABASE_FAIL_EXCEPTION);
        }
    }

    // 기존 포인트 + 변동 포인트 = 최종 포인트 구하는 메소드
    private int getFinalPoint(Point point){

        return userDao.getUserPointByUserId(point.getUserId()) + PointLogType.getRealAmount(point.getAmount(), point.getType());
    }
}
