package server.coupon;

import java.util.List;

public interface CouponDao {
	
	 int insert(Coupon coupon, byte[] image);
	 
	 int update(Coupon coupon, byte[] image);
	 
	 int delete(int id);
	 
	 Coupon findById(int couponId);
	 
	 List<Coupon> getAll();
	 
	 List<Coupon> getAllEnable();
	 
	 byte[] getImage(int id);

}
