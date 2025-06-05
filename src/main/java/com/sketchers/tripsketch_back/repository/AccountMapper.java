package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.dto.TripRespDto;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.entity.TripShare;
import com.sketchers.tripsketch_back.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface AccountMapper {
    public int deleteUser(int userId);
    public int updateEnabledToEmail(String email);
    public int updatePassword(User user);
    public List<Trip> findTripsByUserId(int userId);
    public int deleteTrip(int tripId);
    public Optional<User> findOwnerByTripId(int tripId);
    @MapKey("email")  // 이메일 컬럼을 Map의 key로 사용하겠다는 의미
    public List<User> findUserIdByEmails(List<String> email);
    public boolean insertTripShare(TripShare tripShare);
    public List<TripShare> getSharedUsers(int userId, int tripId);
    public boolean cancelShare(int userId, int tripId, int shareId);
    public List<TripRespDto> getReceivedInvitations(String email);
    public boolean acceptTripInvitation(int shareId);
    public boolean declineTripInvitation(int shareId);
}
