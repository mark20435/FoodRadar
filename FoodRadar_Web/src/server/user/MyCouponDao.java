package server.user;

import java.util.List;

import server.coupon.Coupon;

public interface MyCouponDao {
	
	int insert(MyCoupon mycoupon);
	
	int update(MyCoupon mycoupon);
	
	int delete(int userId, int couPonId);
	
	MyCoupon findById(int id);
	
	List<MyCoupon> getAll();
	
	List<MyCoupon> getAllById(int id);
	
	byte[] getImage(int id);
	
	
	List<MyCoupon> getCouponById(Integer userId, Integer couPonId);

	int setcouPonIsUsedStatus(int userId, Boolean couPonIsUsedStatus);

	int isusedupdate(Integer couPonId, Integer userId, Boolean couPonIsUsed);

	

}
