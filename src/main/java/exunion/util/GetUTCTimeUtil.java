package exunion.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.print.attribute.standard.RequestingUserName;

/**
 * 
 * @ref https://blog.csdn.net/sf_cyl/article/details/52184873
 *
 */
public final class GetUTCTimeUtil {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     * @return
     */
    public static Date getUTCTime() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }


    public static void main(String[] args) { 
        Date UTCTimeStr = getUTCTime() ;
        System.out.println(UTCTimeStr); 
    }

}