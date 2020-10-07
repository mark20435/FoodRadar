package server.coupon;

import java.util.List;

public interface CouponDao {
	
	 int insert(Coupon coupon, byte[] image);
	 
	 int update(Coupon coupon, byte[] image);
	 
	 int delete(int id);
	 
	 Coupon findById(int id);
	 
	 List<Coupon> getAll();
	 
	// List<Coupon> getAllEnable();
	 
	// List<Coupon> couponfindById(int couponId);
	 
	 byte[] getImage(int id);

}
