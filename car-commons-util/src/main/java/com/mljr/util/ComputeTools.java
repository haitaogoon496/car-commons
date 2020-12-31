package com.mljr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @description: 该工具类主要提供产品相关常用计算
 * @Date : 上午11:11 2018/5/11
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public final class ComputeTools {
    static final Logger LOGGER = LoggerFactory.getLogger(ComputeTools.class);
    static final String SIGN = "-";
    /**
     * 车龄计算(根据车辆首次登记日期到当前日期间隔月份)
     * @param beginTimeValue 首次登记日期
     * @param endTimeValue 系统当前时间
     * @return 车龄月份
     */
    public static Integer callCarAge(Date beginTimeValue, Date endTimeValue) {
        String beginTime = TimeTools.format4YYYYMMDD(beginTimeValue);
        String endTime = TimeTools.format4YYYYMMDD(endTimeValue);
        // 拆分开始日期的年月日
        String[] beginDate = beginTime.split(SIGN);
        // 拆分开始日期的年月日
        String[] endDate = endTime.split(SIGN);
        // 得到开始日期的月数
        Integer bMonth = Integer.parseInt(beginDate[0]) * 12 + Integer.parseInt(beginDate[1]);
        // 得到结束日
        Integer eMonth = Integer.parseInt(endDate[0]) * 12 + Integer.parseInt(endDate[1]);
        if (Integer.parseInt(endDate[2]) - Integer.parseInt(beginDate[2]) > 0) {
            // 如果结束日期的天大于开始的天 则结束日期的月+1
            eMonth = eMonth + 1;
        }
        // 获取月数
        Integer totalMonth = Math.abs(eMonth - bMonth);
        LOGGER.info("计算车龄beginTime={},endTime={},totalMonth={}",beginTime,endTime,totalMonth);
        return totalMonth;
    }
    /**
     * 计算车辆指导价
     * @modify 2018/5/15 新车：车辆指导价=实际销售价
     * <pre>业务说明：</pre>
     * <p>【新车】：实际销售价</p>
     * <p>【二手车】：提单时，newCarPrice 就是实际销售价；所以min(salePrice*(1-0.0060*carMonth),salePrice)</p>
     * @param carMonth 车龄
     * @param newCarPrice 新车指导价，提单时，也就是实际销售价
     * @param salePrice 实际销售价
     * @param isNewCar 是否新车
     * @return 车辆指导价
     */
    public static BigDecimal callGuidePrice(Integer carMonth, BigDecimal newCarPrice, BigDecimal salePrice, boolean isNewCar) {
        newCarPrice = Optional.ofNullable(newCarPrice).orElse(salePrice);
        // 二手车
        if (!isNewCar) {
            newCarPrice = newCarPrice.multiply(BigDecimal.valueOf(1 - 0.0060d * carMonth));
            LOGGER.info("[计算指导价]:isNewCar={},newCarPrice={}",isNewCar,newCarPrice);
        }
        return isNewCar ? salePrice : newCarPrice.min(salePrice);
    }

    /**
     * 计算车辆指导价 (该方法主要提供给lyqc-loanapply、lyqc-cas、poseidon对接使用)
     * modify: 2018/5/15 新车时：车辆指导价=实际销售价
     * @param carMonth
     * @param newCarPrice
     * @param salePrice
     * @param flag  0/1/2, 0:新车 1/2 二手车
     * @return guidePrice 指导价
     */
    public static Double callGuidePrice(Integer carMonth,Double newCarPrice,Double salePrice,String flag){
        LOGGER.info("[计算指导价]:carMonth={},newCarPrice={},salePrice={},flag={}",carMonth,newCarPrice,salePrice,flag);
        boolean isNewCar = "0".equals(flag);
        BigDecimal newCarPriceValue = newCarPrice == null ? null : new BigDecimal(newCarPrice.toString());
        BigDecimal guidePrice = callGuidePrice(carMonth,newCarPriceValue,new BigDecimal(salePrice.toString()),isNewCar);
        LOGGER.info("[计算指导价]:guidePrice={}",guidePrice);
        return guidePrice.doubleValue();
    }

    /**
     * 贴息后年利率
     *    业务逻辑： 1200 表示： 月利率 = 年利率/12/100 。 例如：年利率54%，在在库中就是54
     * @param loanAmount 贷款总金额
     * @param periods 还款年限
     * @param discountFee 贴息费
     * @param yearRate 贷款利率
     * @return
     */
    public static Double callYearRate(Double loanAmount, Integer periods, Double discountFee, Double yearRate) {
        double a,i;
        int n;
        double c1;
        double c2;
        // 总贷款
        a = loanAmount;
        n = periods;

        i = ArithUtil.div(yearRate - 0.001d, 1200);
        c2 = (a * i * Math.pow((1 + i), n)) / (Math.pow((1 + i), n) - 1);

        double all = ArithUtil.mul(c2, n);
        double zlx = ArithUtil.sub(all, a);
        // 计算总利息,如果贴息金额大于总利息，那么贴息金额=总利息
        if (zlx < discountFee) {
            discountFee = zlx;
        }
        c1 = (((a * yearRate / 1200 * Math.pow((1 + yearRate / 1200), n)) / (Math.pow((1 + yearRate / 1200), n) - 1)) * n - discountFee) / n;
        while ((c1 - c2 <= 0)) {
            i = i - 0.001d / 1200;
            c2 = ArithUtil.div(ArithUtil.mul(ArithUtil.mul(a, i), Math.pow((1 + i), n)), (Math.pow((1 + i), n) - 1));
        }
        double yll = ArithUtil.mul(i, 1200d);
        double temp_yll = yll * 10000;
        // 161102 向下取整到小数点后两位
        return Math.floor(temp_yll) / 10000;
    }

    /**
     * 获取首付比
     * @param salePrice 实价销售价
     * @param carLoanAmount 车辆贷款金额
     * @return
     */
    public static BigDecimal callInitScale(BigDecimal salePrice,BigDecimal carLoanAmount){
        salePrice = Optional.ofNullable(salePrice).orElse(BigDecimal.ZERO);
        carLoanAmount = Optional.ofNullable(carLoanAmount).orElse(BigDecimal.ZERO);
        //如果车辆实际销售价为空，则参数校验不通过
        if(salePrice.equals(BigDecimal.ZERO)){
            throw new RuntimeException("实际销售价不能为空");
        }
        BigDecimal initScale = salePrice.subtract(carLoanAmount).divide(salePrice,16,BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_DOWN);
        return initScale;
    }

    /**
     * 计算PMT
     * @see <a href="https://stackoverflow.com/questions/5352165/pmt-function-payment-type">参考资料</a>
     * @param yearlyInterestRate 年利率(包含百分数)
     * @param totalNumberOfMonths 还款期数（月）
     * @param loanAmount 贷款总额
     * @return
     */
    public static double PMT(double yearlyInterestRate, int totalNumberOfMonths, double loanAmount){
        double rate = yearlyInterestRate / 100 / 12;
        double denominator = Math.pow((1 + rate), totalNumberOfMonths) - 1;
        return (rate + (rate/denominator)) * loanAmount;
    }

    /**
     * 计算月供工具类
     * @param loanAmount 车贷总额
     * @param loanPeriods 贷款期数
     * @param loanRate 贷款利率
     * @return
     */
    public static BigDecimal calcMonthFee(BigDecimal loanAmount,Integer loanPeriods,BigDecimal loanRate){
        loanRate = Optional.ofNullable(loanRate).orElse(BigDecimal.ZERO);
        if(loanRate.compareTo(BigDecimal.ZERO) == 0){
            return loanAmount.divide(new BigDecimal(loanPeriods), 2, BigDecimal.ROUND_HALF_UP);
        }
        //年利率
        BigDecimal tempYearRate = loanRate.divide(new BigDecimal("100"), 16, BigDecimal.ROUND_HALF_UP);
        //月利率
        double monthRate = tempYearRate.divide(new BigDecimal("12"), 16, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        double tempLoanAmount = loanAmount.doubleValue();
        Double monthFee = (tempLoanAmount*monthRate*Math.pow((1+monthRate),loanPeriods))/(Math.pow(1+monthRate,loanPeriods)-1);
        return  new BigDecimal(monthFee.toString()).setScale(2,BigDecimal.ROUND_HALF_UP);
    }


    private ComputeTools(){}

    public static void main(String[] args) {
        /*Integer carMonth = callCarAge(TimeTools.parseYYYY_MM_DD("2016-09-21"),new Date());
        System.out.println(carMonth);
        System.out.println(callGuidePrice(carMonth,new BigDecimal("159700"),new BigDecimal("85000.0"),false));
        BigDecimal a = BigDecimal.valueOf(PMT(10.5412,12,1));
        BigDecimal b = BigDecimal.valueOf(PMT(8.2344,12,1));
        BigDecimal c = a.subtract(b);
        System.out.println(MessageFormat.format("a={0},b={1},c={2}",a.toString(),b.toString(),c.toString()));*/
        System.out.println(calcMonthFee(new BigDecimal(100),12,new BigDecimal(0)));
    }
}
