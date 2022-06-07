package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.entity.CartItem;
import com.cnpm.ecommerce.backend.app.entity.Category;
import com.cnpm.ecommerce.backend.app.repository.CartItemRepository;
import com.cnpm.ecommerce.backend.app.repository.CartRepository;
import com.cnpm.ecommerce.backend.app.repository.CategoryRepository;
import com.cnpm.ecommerce.backend.app.repository.ProductRepository;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticService implements IStatisticService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

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
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate, PageRequest.of(0,5));

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
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate, PageRequest.of(0, 5));

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
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate, PageRequest.of(0, 5));

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
            List<Map<String, Object>> listSoldProduct = cartItemRepository.getAllSoldProductByDay(date, dateEndDate, PageRequest.of(0, 5));

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

    @Override
    public Long getTotalOrderByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            Long total = cartRepository.getTotalOrderByDay(date, dateEndDate);

            return total == null ? 0 : total;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long getTotalOrderByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            Long total = cartRepository.getTotalOrderByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalOrderByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            Long total = cartRepository.getTotalOrderByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalOrderByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            Long total = cartRepository.getTotalOrderByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalOrderDetailByDay(String day) {
        try {
            Timestamp date = CommonUtils.convertStringToTimestamp(day, "dd-MM-yyyy");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 1);
            Long total = cartItemRepository.getTotalOrderDetailByDay(date, dateEndDate);

            return total == null ? 0 : total;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long getTotalOrderDetailByMonth(String month) {
        try {
            Timestamp date = CommonUtils.convertStringToMonth(month, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(month, "-"));
            Long total = cartItemRepository.getTotalOrderDetailByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalOrderDetailByQuarter(String quarter) {
        try {
            Timestamp date = CommonUtils.convertStringToQuarter(quarter, "-");
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfQuarter(quarter, "-"));
            Long total = cartItemRepository.getTotalOrderDetailByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalOrderDetailByYear(String year) {
        try {
            Timestamp date = CommonUtils.convertStringToYear(year);
            Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, 365);
            Long total = cartItemRepository.getTotalOrderDetailByDay(date, dateEndDate);

            return total == null ? 0 : total;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTotalProductSoldGroupByCategoryByMonthInYear(String year) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            long count = categoryRepository.count();

            List<Category> categories = categoryRepository.findAll();

            int i =1 ;
            while (i <= 12) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("month", i);
                Timestamp date = CommonUtils.convertStringToMonth(i + "-" + year, "-");
                Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(i + "-" + year, "-"));
                List<Map<String, Object>> listSoldByCategory = cartItemRepository.getTotalProductSoldGroupByCategory(date, dateEndDate);
                //List<Long> listCategoryId = listSoldByCategory.stream().filter(x -> x.get("categoryId")).collect(Collectors.toList());
                List<Long> listCategoryId = new ArrayList<>();
                for(int j = 0 ; j < listSoldByCategory.size(); j++) {
                    listCategoryId.add(Long.parseLong(listSoldByCategory.get(j).get("categoryId").toString()));
                }
                List<Map<String, Object>> listSoldByCategoryProcess = new ArrayList<>();
                for(Category category : categories) {
                    Map<String, Object> map2 = new HashMap<>();
                    if(listCategoryId.contains(category.getId())) {
                        Optional<Map<String, Object>> ct = listSoldByCategory.stream().filter(x -> x.get("categoryId").equals(category.getId())).findFirst();
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", ct.get().get("quantity"));
                    } else {
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", 0);
                    }
                    listSoldByCategoryProcess.add(map2);


                }
//              map1.put("soldByCategory", listSoldByCategory);
                map1.put("soldByCategory", listSoldByCategoryProcess);
                map.add(map1);
                i++;
            }

            return map;

        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTotalProductSoldGroupByCategoryByMonthInYear1(String year) {
        List<Map<String, Object>> map = new ArrayList<>();
        try {
            long count = categoryRepository.count();

            List<Category> categories = categoryRepository.findAll();

            int i =1 ;
            while (i <= 12) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("month", i);
                Timestamp date = CommonUtils.convertStringToMonth(i + "-" + year, "-");
                Timestamp dateEndDate = CommonUtils.incrementTimestamp(date, CommonUtils.countDayOfMonth(i + "-" + year, "-"));
                List<Map<String, Object>> listSoldByCategory = cartItemRepository.getTotalProductSoldGroupByCategory(date, dateEndDate);
                //List<Long> listCategoryId = listSoldByCategory.stream().filter(x -> x.get("categoryId")).collect(Collectors.toList());
                List<Long> listCategoryId = new ArrayList<>();
                for(int j = 0 ; j < listSoldByCategory.size(); j++) {
                    listCategoryId.add(Long.parseLong(listSoldByCategory.get(j).get("categoryId").toString()));
                }
                List<Map<String, Object>> listSoldByCategoryProcess = new ArrayList<>();
                for(Category category : categories) {
                    Map<String, Object> map2 = new HashMap<>();
                    if(listCategoryId.contains(category.getId())) {
                        Optional<Map<String, Object>> ct = listSoldByCategory.stream().filter(x -> x.get("categoryId").equals(category.getId())).findFirst();
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", ct.get().get("quantity"));
                    } else {
                        map2.put("categoryId", category.getId());
                        map2.put("categoryName", category.getName());
                        map2.put("quantity", 0);
                    }
                    listSoldByCategoryProcess.add(map2);


                }
//              map1.put("soldByCategory", listSoldByCategory);
                map1.put("soldByCategory", listSoldByCategoryProcess);
                map.add(map1);
                i++;
            }
            List<Map<String, Object>> listSoldByCategoryProcess1 = new ArrayList<>();
            for(Category category : categories) {
                Map<String, Object> map2 = new HashMap<>();

                map2.put("categoryName", category.getName());
                List<Long> dataList = new ArrayList<>();
                for(int k = 0; k < 12; k++) {
                    List<Map<String, Object>> objectMaps = (List<Map<String, Object>>) map.get(k).get("soldByCategory");
                    Optional<Map<String, Object>> ct = objectMaps.stream().filter(x -> x.get("categoryId").equals(category.getId())).findFirst();
                    dataList.add(Long.parseLong(ct.get().get("quantity").toString()));
                }
                map2.put("data", dataList);
                listSoldByCategoryProcess1.add(map2);
            }

            return listSoldByCategoryProcess1;

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
