package com.position;

import com.lyqc.base.entity.CaAppInfoEntity;
import com.lyqc.base.entity.CaAppInfoFeeEntity;
import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.lyqc.base.entity.CaAutoApprRulePropEntity;
import com.lyqc.base.entity.CaCarInfoEntity;
import com.lyqc.base.enums.AutoApprConstant;
import com.lyqc.util.BeanMapper;
import com.mljr.position.AbstractPositionExecutor;
import com.mljr.position.PositionContext;
import com.mljr.position.PositionException;
import com.mljr.position.PrePositionExecutor;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 测试
 * @Date : 2018/12/5 下午4:57
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class PositionExecutorTest {
    String appInfoJSON = "{\"accountAmount\":0,\"appCode\":\"F1812031000100180503\",\"appFeeInfoDTO\":{\"commFloatFeeRate\":0,\"gpsFloatFee\":0},\"carInfoDTO\":{\"carAge\":3,\"carFirstBook\":1535904000000,\"carNewPrice\":122800,\"course\":100},\"carLoanAmount\":100000,\"comRate\":10,\"dealerCode\":10001001,\"gpsLevel\":\"0\",\"grayTarget\":4,\"isGrayControl\":true,\"isHighRisk\":false,\"isHouse\":\"0\",\"isLcv\":\"0\",\"isOld\":\"1\",\"isSubmit\":true,\"licenseType\":\"0\",\"lifeInsuranceRate\":0,\"loanPeriods\":\"12\",\"loanRate\":10.5412,\"notarizationFee\":0,\"productCode\":364,\"rateLevel\":\"2\",\"salePrice\":200000,\"takenMode\":1,\"theftProtectionFee\":100,\"theftProtectionId\":103}";
    /**
     * 贷款申请单信息
     */
    //protected CaAppInfoEntity appInfo = JSON.parseObject(appInfoJSON,CaAppInfoEntity.class);
    protected CaAppInfoEntity appInfo = new CaAppInfoEntity(){{
        setAppCode("F1812031000100180503");
        setAccountFee(BigDecimal.valueOf(2000));
        setaComFee(BigDecimal.valueOf(2200));
        setDealerCode(10010001);
    }};
    /**
     * 贷款申请单费用信息
     */
    protected CaAppInfoFeeEntity appInfoFee =  new CaAppInfoFeeEntity(){{

    }};
    /**
     * 贷款申请单车辆信息
     */
    protected CaCarInfoEntity carInfo = new CaCarInfoEntity(){{

    }};

    protected List<CaAutoApprRulePropEntity> props1 = new ArrayList<CaAutoApprRulePropEntity>(){{
        add(new CaAutoApprRulePropEntity(){{
            setRuleId("BBH0011");setPropCode("accountFee");setOpIdn("<");setPropValue("200");setPropValueType(AutoApprConstant.PropValueTypeEnum.FLOAT.getIndex());
        }});
    }};
    protected List<CaAutoApprRulePropEntity> props2 = new ArrayList<CaAutoApprRulePropEntity>(){{
        add(new CaAutoApprRulePropEntity(){{
            setRuleId("BBH0012");setPropCode(AutoApprConstant.PropNameEnum.EXPRESSION.getName());setOpIdn("=");
            //setPropValue("sum(accountFee,aComFee) > min(accountFee,aComFee)");
            setPropValue("accountFee < aComFee");
            setPropValueType(AutoApprConstant.PropValueTypeEnum.EXPRESSION.getIndex());
        }});
    }};
    /**
     * 置入规则
     */
    protected List<CaAutoApprRuleEntity> rules = new ArrayList<CaAutoApprRuleEntity>(){{
        add(new CaAutoApprRuleEntity(){{
            setRuleId("BBH0011");setType("B");setTypeName("自动退回");setRuleName("规则1");setMsgTemplate("Jack");
            setProps(props1);
        }});

        add(new CaAutoApprRuleEntity(){{
            setRuleId("BBH0012");setType("B");setTypeName("自动退回");setRuleName("规则2");setMsgTemplate("Lime");
            setProps(props2);
        }});
    }};

    @Test
    public void test(){
        try {
            PositionContext context = PositionContext.builder().appInfo(appInfo).carInfo(carInfo).appInfoFee(appInfoFee).rules(rules).build();
            AbstractPositionExecutor executor = new PrePositionExecutor(context);
            executor.execute((env -> {
                env.put("aaa",1);
                env.put("bbb",2);
            }));
            System.out.println(context.getResult().toString());
        } catch (PositionException e) {
            e.printStackTrace();
        }
    }

}
