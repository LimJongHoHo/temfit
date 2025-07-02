package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(@Param(value = "user") UserEntity user);

    UserEntity selectByEmail(@Param("email") String email);

    String selectEmail(@Param(value = "user") UserEntity user);

    int selectCountByContact(@Param(value = "contactFirst") String contactFirst, @Param(value = "contactSecond") String contactSecond, @Param(value = "contactThird") String contactThird);

    int selectCountByEmail(@Param(value = "email") String email);

    int selectCountByNickname(@Param(value = "nickname") String nickname);

    int update(@Param(value = "user") UserEntity user);
}
