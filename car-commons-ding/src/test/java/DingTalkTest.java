import com.alibaba.fastjson.JSON;
import com.mljr.ding.auth.DingAuth;
import com.mljr.ding.client.DingRobotClient;
import com.mljr.ding.client.DingRobotClientFactory;
import com.mljr.ding.client.DingRobotService;
import com.mljr.ding.dto.builder.MarkdownDingRobotReqBuilder;
import com.mljr.ding.dto.req.LinkDingRobotReq;
import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.ding.dto.req.TextDingRobotReq;
import com.mljr.ding.dto.resp.DingBaseRobotResp;
import org.junit.Test;

/**
 * @description: 钉钉测试类
 * @Date : 2018/8/2 下午7:07
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class DingTalkTest {

    final DingRobotService dingRobotService = new DingRobotClient();

    //@Test
    public void text() {
        TextDingRobotReq robotReq = new TextDingRobotReq();
        String content = "기다리게 해서 죄송합니다";
        robotReq.setContent(content);
        DingBaseRobotResp resp = dingRobotService.sendText(DingAuth.TOKEN, robotReq);
        System.out.println(JSON.toJSON(resp));
    }

    //@Test
    public void markdown() {
        MarkdownDingRobotReq robotReq = new MarkdownDingRobotReq();
        String content = "La capital de nuestro país es Beijing. ";
        robotReq.setTitle("Notice");
        robotReq.setText(content);
        DingBaseRobotResp resp = dingRobotService.sendMarkdown(DingAuth.TOKEN, robotReq);
        System.out.println(JSON.toJSON(resp));
    }

    //@Test
    public void markdown2() {
        MarkdownDingRobotReq robotReq = new MarkdownDingRobotReq();
        robotReq.setTitle("【测试一下】大数据GPS设备号状态接口");
        StringBuffer content = new StringBuffer("### 大数据GPS设备号状态接口");
        content.append("\n\n").append("### gpsInfoUrl：").append("http://dataapi.mljr.com/auth/queryToken?uuid=83c01f57ec8f416aa8be0ecacf0a4c99");
        content.append("\n\n").append("### gpsTokenUrl：").append("http://dataapi.mljr.com/checkgps");
        content.append("\n\n").append("### 报警内容：\n\n").append("> 获取到Webhook地址后，用户可以使用任何方式向这个地址发起HTTP POST 请求，即可实现给该群组发送消息。注意，发起POST请求时，必须将字符集编码设置成UTF-8。\n" +
                "当前自定义机器人支持文本（text）、连接（link）、markdown（markdown）三种消息类型，大家可以根据自己的使用场景选择合适的消息类型，达到最好的展示样式。具体的消息类型参考下一节内容。\n");
        robotReq.setText(content.toString());
        DingBaseRobotResp resp = dingRobotService.sendMarkdown(DingAuth.TOKEN, robotReq);
        System.out.println(JSON.toJSON(resp));
    }

    //@Test
    public void link() {
        LinkDingRobotReq robotReq = new LinkDingRobotReq();
        String content = "La capital de nuestro país es Beijing. ";
        robotReq.setTitle("Notice");
        robotReq.setText(content);
        robotReq.setMessageUrl("http://blog.sina.com.cn/s/blog_14b88cad50102x97b.html");
        robotReq.setPicUrl("http://s7.sinaimg.cn/large/0064qwnjzy78VDhJwKab6&690");
        DingBaseRobotResp resp = dingRobotService.sendLink(DingAuth.TOKEN, robotReq);
        System.out.println(JSON.toJSON(resp));
    }

    //@Test
    public void flyweight() {
        for (int i = 0; i < 10; i++) {
           new Thread( () -> {
               DingRobotService service = DingRobotClientFactory.create("测试");
               System.out.println(service);
           }).run();
        }
    }
    //@Test
    public void builderReq(){
        try {
            MarkdownDingRobotReq robotReq = new MarkdownDingRobotReqBuilder().title("【GPS认证汇总结果】")
                    .applicationName("test").content("La capital de nuestro país es Beijing.").build();
            dingRobotService.sendMarkdown(DingAuth.NORMAL_TOKEN, robotReq);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
