import com.mljr.aviator.AviatorContext;
import com.mljr.aviator.AviatorExecutor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 基于Aviator测试类
 * @Date : 2018/9/7 下午6:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class AviatorTest {

    Map<String,Object> evn = new HashMap<String,Object>(){{
        put("a",1.2);
        put("b",6.44);
        put("c",3.2);
        put("d",-5.4);
    }};

    @Test
    public void sum(){
        String expression = "sum(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("sum(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void max(){
        String expression = "max(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("max(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void min(){
        String expression = "min(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("min(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void nil(){
        String expression = "aa == nil ? 1 : 2";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println(AviatorExecutor.execute(ctx));
    }


    @Test
    public void instance(){
        Teacher teacher = new Teacher(){{
            setName("Lim");
            setScore(20);
        }};

        Student student = new Student(){{
            setName("Jack");
            setScore(22);
        }};
        Map<String,Object> env = new HashMap<String,Object>(){{
            put("teacher",teacher);
            put("student",student);
        }};

        Object value = AviatorExecutor.execute(AviatorContext.builder().env(env).expression("student.name").build());
        System.out.println(value);
    }

    @Test
    public void test1(){
        String expression = "165000*0.7==115500";
        AviatorContext context = AviatorContext.builder().expression(expression).cached(false).build();
        System.out.println(AviatorExecutor.execute(context));;
    }
}
