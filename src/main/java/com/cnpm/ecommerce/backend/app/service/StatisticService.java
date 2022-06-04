package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import com.cnpm.ecommerce.backend.app.repository.CartItemRepository;
import com.cnpm.ecommerce.backend.app.repository.CartRepository;
import com.cnpm.ecommerce.backend.app.repository.ProductRepository;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatisticService implements IStatisticService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public BigDecimal getAllRevenueByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            BigDecimal total = cartRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BigDecimal getAllRevenueByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            BigDecimal total = cartRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getAllRevenueByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            BigDecimal total = cartRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getAllRevenueByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            BigDecimal total = cartRepository.getAllRevenueByDay(date, dateEndDate);

            return total == null ? BigDecimal.ZERO : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllSoldProductByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate);

//            for (Map<String, Object> item : listSoldProduct) {
//                Optional<Product> product = productRepository.findById(Long.parseLong(item.get("productId").toString()));
//                item.put("productName", product.get().getName());
//                item.put("productThumbnail", Base64Utils.encodeToString(product.get().getThumbnailArr()));
//            }
            return listSoldProduct;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getAllSoldProductByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate);

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllSoldProductByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate);

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllSoldProductByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate);

            return listSoldProduct;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Cart> getAllCartByDay(String day, Pageable pagingSort) {

        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);

            Page<Cart> cartPage = cartRepository.findByCreatedDateBetween(date, dateEndDate, pagingSort);

            return getCarts(cartPage);
        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Page<Cart> getAllCartByMonth(String month, Pageable pagingSort) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));

            Page<Cart> cartPage = cartRepository.findByCreatedDateBetween(date, dateEndDate, pagingSort);

            return getCarts(cartPage);
        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Cart> getAllCartByQuarter(String quarter, Pageable pagingSort) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));

            Page<Cart> cartPage = cartRepository.findByCreatedDateBetween(date, dateEndDate, pagingSort);

            return getCarts(cartPage);
        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Cart> getAllCartByYear(String year, Pageable pagingSort) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);

            Page<Cart> cartPage = cartRepository.findByCreatedDateBetween(date, dateEndDate, pagingSort);

            return getCarts(cartPage);
        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    private Page<Cart> getCarts(Page<Cart> cartPage) {
        for(Cart cart : cartPage.getContent()) {
            cart.setCustomerIds(cart.getCustomer().getId());
            for(CartItem cartItem : cart.getCartItems()) {
                cartItem.setCartIds(cart.getId());
                cartItem.setProductIds(cartItem.getProduct().getId());
                cartItem.setProductName(cartItem.getProduct().getName());
                cartItem.setProductThumbnail(Base64Utils.encodeToString(cartItem.getProduct().getThumbnailArr()));
            }
        }
        return  cartPage;
    }


}
