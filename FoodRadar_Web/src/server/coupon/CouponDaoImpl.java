package server.coupon;

import java.util.List;

import javax.sql.DataSource;

public class CouponDaoImpl implements CouponDao {
	
	DataSource dataSource;

	@Override
	public int insert(Coupon coupon, byte[] image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Coupon coupon, byte[] image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Coupon findById(int couponId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> getAllEnable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getImage(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
