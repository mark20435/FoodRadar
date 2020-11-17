package server.coupon;

import java.util.List;

public interface CouponDao {
	
	 int insert(Coupon coupon, byte[] image);
	 
	 int couponLoveInsert(int loginUserId, int couPonId);
	 
	 int update(Coupon coupon, byte[] image);
	 
	 int delete(int id);
	 
	 Coupon findById(int id);
	 
	 List<Coupon> getAll(int userId);
	 
	 List<Coupon> getAllEnable(int cupuserId);
	 
	 List<Coupon> getAllcouPonType(int userId, Boolean couPonType);
	 
	// List<Coupon> couponfindById(int couponId);
	 
	 byte[] getImage(int id);
	 
	 

}
