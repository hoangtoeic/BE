package com.cnpm.ecommerce.backend.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;

public class CommonUtils {

    public static Sort.Direction getSortDirection(String sort) {
        return sort.contains("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static Pageable sortItem(int page, int limit, String [] sort) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                Sort.Direction dire = CommonUtils.getSortDirection(_sort[1]);
                Sort.Order order = new Sort.Order(dire,_sort[0]);
                orders.add( order );
            }
        } else {
            // sort=[field, direction]
            Sort.Direction dire = CommonUtils.getSortDirection(sort[1]);
            Sort.Order order = new Sort.Order(dire, sort[0]);
            orders.add( order );
        }
        Pageable pagingSort = PageRequest.of(page, limit, Sort.by(orders));

        return pagingSort;
    }

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url = new StringBuffer();

        url.append(scheme).append("://").append(serverName);

        if(serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")) {
            url.append("/");
        }

        return url.toString();
    }

    public static Double exchangeCurrency(){
        String url = "https://v6.exchangerate-api.com/v6/675b2f4248bd548f7b3f133f/latest/USD";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> object = restTemplate.getForObject(url, HashMap.class);

        Map<String, Object> conversionRates = (Map<String, Object>) object.get("conversion_rates");

        Object usd = conversionRates.get("VND");

        return Double.parseDouble(usd.toString());
    }

    public static Date convertStringToDate(String date, String format) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date1 = formatter.parse(date);

        return date1;
    }

    public static Timestamp convertStringToTimestamp(String date, String format) {

        try {
            DateFormat formatter = new SimpleDateFormat(format);
            // you can change format of date
            Date date1 = formatter.parse(date);
            Timestamp timeStampDate = new Timestamp(date1.getTime());

            return timeStampDate;
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
        }
        return null;
    }

    public static Timestamp incrementTimestamp(Timestamp timestamp, Integer dateIncrement) {

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.DAY_OF_WEEK, dateIncrement);

            return new Timestamp(cal.getTime().getTime());
        }  catch(Exception e) { //this generic but you can control another types of exception
        // look the origin of exception
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp convertStringToMonth(String month, String format) {
        try {
            String [] monthArr = month.split(format);
            if(monthArr.length == 2 && Integer.parseInt(monthArr[0]) <= 12 && Integer.parseInt(monthArr[0]) > 0) {
                String date = "01" + format + month;

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                // you can change format of date
                Date date1 = formatter.parse(date);
                Timestamp timeStampDate = new Timestamp(date1.getTime());

                return timeStampDate;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Integer countDayOfMonth(String month, String format) {
        try {
            String [] monthArr = month.split(format);
            if(monthArr.length == 2 && Integer.parseInt(monthArr[0]) <= 12 && Integer.parseInt(monthArr[0]) > 0) {
                YearMonth yearMonth = YearMonth.of(Integer.parseInt(monthArr[1]), Integer.parseInt(monthArr[0]));
                Integer dayOfMonth = yearMonth.lengthOfMonth();

                return dayOfMonth;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Timestamp convertStringToQuarter(String quarter, String format) {
        try {
            String [] monthArr = quarter.split(format);
            Integer quarterConvert =  Integer.parseInt(monthArr[0]);
            if(monthArr.length == 2 && quarterConvert <= 4 && quarterConvert > 0) {
                String date = null;
                switch (quarterConvert) {
                    case 1: date = "01" + format + "01" + format + monthArr[1]; break;
                    case 2: date = "01" + format + "04" + format + monthArr[1]; break;
                    case 3: date = "01" + format + "07" + format + monthArr[1]; break;
                    case 4: date = "01" + format + "10" + format + monthArr[1]; break;
                    default: date = null; break;
                }

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                // you can change format of date
                Date date1 = formatter.parse(date);
                Timestamp timeStampDate = new Timestamp(date1.getTime());

                return timeStampDate;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Integer countDayOfQuarter(String quarter, String format) {
        try {
            String [] monthArr = quarter.split(format);
            Integer quarterConvert =  Integer.parseInt(monthArr[0]);
            Integer yearConvert = Integer.parseInt(monthArr[1]);
            if(monthArr.length == 2 && Integer.parseInt(monthArr[0]) <= 4 && Integer.parseInt(monthArr[0]) > 0) {
                if(quarterConvert == 1) {
                    YearMonth yearMonth1 = YearMonth.of(yearConvert, 1);
                    YearMonth yearMonth2 = YearMonth.of(yearConvert, 2);
                    YearMonth yearMonth3 = YearMonth.of(yearConvert, 3);
                    Integer dayOfQuarter = yearMonth1.lengthOfMonth() + yearMonth2.lengthOfMonth() + yearMonth3.lengthOfMonth() ;

                    return dayOfQuarter;
                } else if(quarterConvert == 2) {
                    YearMonth yearMonth1 = YearMonth.of(yearConvert, 4);
                    YearMonth yearMonth2 = YearMonth.of(yearConvert, 5);
                    YearMonth yearMonth3 = YearMonth.of(yearConvert, 6);
                    Integer dayOfQuarter = yearMonth1.lengthOfMonth() + yearMonth2.lengthOfMonth() + yearMonth3.lengthOfMonth() ;

                    return dayOfQuarter;
                }   else if(quarterConvert == 3) {
                    YearMonth yearMonth1 = YearMonth.of(yearConvert, 7);
                    YearMonth yearMonth2 = YearMonth.of(yearConvert, 8);
                    YearMonth yearMonth3 = YearMonth.of(yearConvert, 9);
                    Integer dayOfQuarter = yearMonth1.lengthOfMonth() + yearMonth2.lengthOfMonth() + yearMonth3.lengthOfMonth() ;

                    return dayOfQuarter;
                } else if(quarterConvert == 4) {
                    YearMonth yearMonth1 = YearMonth.of(yearConvert, 10);
                    YearMonth yearMonth2 = YearMonth.of(yearConvert, 11);
                    YearMonth yearMonth3 = YearMonth.of(yearConvert, 12);
                    Integer dayOfQuarter = yearMonth1.lengthOfMonth() + yearMonth2.lengthOfMonth() + yearMonth3.lengthOfMonth() ;

                    return dayOfQuarter;
                }

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp convertStringToYear(String year) {
        try {
                String date = "01-01-" + year ;

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                // you can change format of date
                Date date1 = formatter.parse(date);
                Timestamp timeStampDate = new Timestamp(date1.getTime());

                return timeStampDate;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String covertDateNowToMonthString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        String[] strDateArr = strDate.split("/");

        return strDateArr[0] + "-" + strDateArr[2];
    }

    public static String covertDateNowToYearString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(date);
        String[] strDateArr = strDate.split("/");

        return strDateArr[2];
    }


}
