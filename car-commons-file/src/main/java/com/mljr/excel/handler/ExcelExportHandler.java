package com.mljr.excel.handler;

import com.alibaba.fastjson.JSON;
import com.mljr.common.SyUser;
import com.mljr.excel.export.AbstractExportHandler;
import com.mljr.excel.export.Column;
import com.mljr.excel.export.ExportComponent;
import com.mljr.excel.export.ExportEnum;
import com.mljr.excel.export.ExportException;
import com.mljr.excel.util.ExcelUtil;
import com.mljr.file.FileUtil;
import com.mljr.file.ZipUtil;
import com.mljr.util.HttpUtils;
import com.mljr.util.TimeTools;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @description: Excel导出处理器
 * @Date : 2018/7/17 下午2:22
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public abstract class ExcelExportHandler implements ExcelExportHandle {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelExportHandler.class);
    @Value(value = "${common.excel.export.path}")
    private String exportPath;

    /**
     * 外部调用
     * @param beanName
     * @param request
     * @param response
     */
    public final void invoke(String beanName, HttpServletRequest request, HttpServletResponse response){
        StopWatch time = new StopWatch();
        try {
            response.setHeader("content-type","text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession();
            Object sessionUserObject = session.getAttribute("user");
            if(hasLock()){
                String userId = "1",userName = "";
                try {
                    if(null != sessionUserObject){
                        Object user = session.getAttribute("user");
                        userId = String.valueOf((Integer) getValueByKey(user,"userId"));
                        userName = (String) getValueByKey(user, "userName");
                        //userId = user.getUserId().toString();
                        //userName = user.getUserName();
                    }
                    LOGGER.info("excel下载处理中....,beanName={},userId={},userName={}",beanName,userId,userName);
                    response.getWriter().print(MessageFormat.format(callbackClient(),"excel下载处理中"));
                } catch (IOException ex) {
                    LOGGER.error("excel下载处理中",ex);
                }
            }
            time.start();
            //获得参数和数据
            Map<String, String> params = HttpUtils.getRequestParams(request);
            params.put("beanName",beanName);
            //删除已过期的文件
            FileUtil.delExpireFile(exportPath, fileExpire());
            ExportComponent exportComponent = new ExportComponent();//实例化导出组件
            exportComponent.init(params);//初始化组件
            LOGGER.info("doExport,params={}", JSON.toJSON(params));
            String type = Optional.ofNullable(request.getParameter("exportType")).orElse("xls");
            String fileNamePrefix = UUID.randomUUID().toString();
            if(null != sessionUserObject){
                Integer userId = (Integer) getValueByKey(sessionUserObject,"userId");
                if(userId != null) {
                    fileNamePrefix = String.valueOf(userId);
                }
            }
            String fileName = fileNamePrefix + "_" + System.currentTimeMillis();
            File excelPath = new File(exportPath + fileName);//防止测试同一个账号
            String excelAbsolutePath = excelPath.getAbsolutePath();
            FileUtil.makeDir(excelPath);//创建文件临时存放目录
            AbstractExportHandler exportHandler = exportComponent.getHandler();
            String exportFileName = exportHandler.getFileName();
            List<Column> columns = exportHandler.getColumns();
            List<?> list;
            if ("zip".equalsIgnoreCase(type)){
                timeDot(time, "数据初始化耗时时长");
                int index = 0;
                do{
                    list = exportComponent.getListData(params, ExportEnum.ZIP);
                    if (!CollectionUtils.isEmpty(list)){
                        timeDot(time, "第"+(index+1)+"个文件数据加载耗时时长");
                        HSSFWorkbook workbook = ExcelUtil.generateExcel(exportHandler.getColumns(), list);
                        FileOutputStream fos = new FileOutputStream(excelAbsolutePath + File.separator + exportFileName + "_" + (index + 1) + ".xls");
                        ExcelUtil.save(fos,workbook);
                        timeDot(time, "第"+(index+1)+"个文件写入磁盘耗时时长");
                    }else{
                        break;
                    }
                    index++;
                }while(!CollectionUtils.isEmpty(list));
                response.setContentType("application/x-download");
                response.setHeader("Content-disposition", "attachment;filename=" +exportFileName+".zip");
                timeDot(time, "数据加载总耗时时长");
                ZipUtil.zip(response.getOutputStream(),new File(excelAbsolutePath));
                timeDot(time, "文件压缩耗时时长");
                FileUtil.delExcelFile(excelAbsolutePath);
                timeDot(time, "删除临时文件耗时时长");
                response.flushBuffer();
            }else {
                list = exportComponent.getListData(params, ExportEnum.XLS);
                response.setContentType("application/x-download");
                response.setHeader("Content-disposition", "attachment;filename=" +exportFileName+".xls");
                ExcelUtil.save(response.getOutputStream(), ExcelUtil.generateExcel(columns, list));
                response.flushBuffer();
            }
        }catch (ExportException e) {
            LOGGER.error("下载获取bean实例异常",e);
            try {
                response.getWriter().print(MessageFormat.format(callbackClient(),e.getMessage()));
            } catch (IOException ex) {
                LOGGER.error("下载获取bean实例异常",e);
            }
        }catch (Exception e) {
            LOGGER.error("下载过程出现异常", e);
            try {
                response.getWriter().print(MessageFormat.format(callbackClient(),"下载过程出现异常"));
            } catch (IOException ex) {
                LOGGER.error("下载获取bean实例异常",e);
            }
        }finally {
            releaseLock();
        }
        time.stop();
    }

    /**
     * 输出
     * @param w
     * @param text
     */
    private void timeDot(StopWatch w, String text){
        w.stop();
        LOGGER.info(TimeTools.spend(w.getLastTaskTimeMillis(), text));
        w.start();
    }

    /**
     * 单个对象的某个键的值
     *
     * @param object
     *            对象
     *
     * @param key
     *            键
     *
     * @return Object 键在对象中所对应得值 没有查到时返回空字符串
     */
    public static Object getValueByKey(Object obj, String key) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            try {

                if (f.getName().endsWith(key)) {
                    System.out.println("单个对象的某个键的值==反射==" + f.get(obj));
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }

}
