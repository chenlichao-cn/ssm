package example;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>author: 陈立朝
 * <br>date: 16/6/15 上午11:41
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class Generator {

    public static void main(String[] args) throws Exception{
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(
                Generator.class.getResourceAsStream("/generator-config.xml")
        );
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
        //generator.generate(new CustomCallback());
        generator.generate(null);
    }

    private static class CustomCallback implements ProgressCallback {


        @Override
        public void introspectionStarted(int totalTasks) {
            System.out.println("校验配置, 共 " + totalTasks + " 个校验任务");
        }

        @Override
        public void generationStarted(int totalTasks) {
            System.out.println("开始生成代码, 共 " + totalTasks + " 个生成任务");
        }

        @Override
        public void saveStarted(int totalTasks) {
            System.out.println("开始保存文件, 共 " + totalTasks + " 保存任务");
        }

        @Override
        public void startTask(String taskName) {
            System.out.println("开始执行 " + taskName);
        }

        @Override
        public void done() {
            System.out.println("生成结束");
        }

        @Override
        public void checkCancel() throws InterruptedException {
            System.out.println("取消检查");
        }
    }
}
