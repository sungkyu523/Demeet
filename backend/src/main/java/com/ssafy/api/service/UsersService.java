package com.ssafy.api.service;

import com.ssafy.DTO.userSimpleInfoDTO;
import com.ssafy.api.request.UsersRegisterPostReq;
import com.ssafy.common.customException.UidNullException;
import com.ssafy.db.entity.Users;

import java.util.List;

/**
 * Users 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의
 */
public interface UsersService {

    boolean deleteUser(String username);

    Users createUsers(UsersRegisterPostReq usersRegisterInfo);

    Users getUsersByUserEmail(String userEmail);
    Boolean checkEmailDuplicate(String email);

    List<userSimpleInfoDTO> getUserList();

    Boolean changeUserPassword(Long uid, String newPassword);

    Boolean changeUserNickname(Long uid, String newNickname);

    userSimpleInfoDTO getUsersByUid(Long ownerId) throws UidNullException;
}