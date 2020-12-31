import com.lyqc.gpsprovider.enums.CarGpsConstant;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 根据gps_ids获取gps信息
 * @Date : 2018/9/26 上午11:40
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class GpsDeviceInfoComponentTest {

    @Test
    public void test(){
        List<String> gpsStatusList = Arrays.asList("NORMAL");
        long count = gpsStatusList.stream().filter(each -> !CarGpsConstant.GPSDeviceStatusEnum.NORMAL.getValue().equals(each)).count();
        System.out.println(count);


        System.out.println(System.currentTimeMillis());
    }
}
