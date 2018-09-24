package com.king;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 *     <build>
 <plugins>
 <plugin>
 <groupId>com.king</groupId>
 <artifactId>king-maven-plugin</artifactId>
 <version>1.0-SNAPSHOT</version>
 <executions>
 <execution>
 <phase>package</phase>
 <goals>
 <goal>total</goal>
 </goals>
 </execution>
 </executions>
 </plugin>


 <plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-compiler-plugin</artifactId>
 <configuration>
 <source>1.8</source>
 <target>1.8</target>
 </configuration>
 </plugin>

 * 统计打包的java文件数量
 * 生成application.properties,  env=king
 * 后期优化：properties位置不对，如已经生成则追加内容
 */
@Mojo(name = "total", defaultPhase = LifecyclePhase.PACKAGE)
public class KingMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.basedir}",property = "basedir",required = true,readonly = true)
    private String path;

    /**
     * <options>
     *     <option>aaa</option>
     *     <option>bbb</option>
     * </options>
     */
    @Parameter
    private List<String> options;

    /**
     * 命令模式
     * -Dargs=xxx
     */
    @Parameter(property = "args")
    private String args;

    private int fileCount;
    private int javaCount;
    private int dirCount;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("可以传入的path plugin  !!!" + path);
        System.out.println("options plugin  !!!" + options);
        System.out.println("args表示命令符的传递-Dargs plugin  !!!" + args);
        System.out.println(countFile(path));
        genProperties(path);
    }

    /**
     * 生成application.properties,  env=king
     * @param path
     */
    private void genProperties(String path) {
        FileUtil.createFile(path,"application.properties","env=king");
    }


    /**
     * 统计打包的java文件数量
     * @param dir
     * @return
     */
    public String countFile(String dir) {
        File f = new File(dir);
        File fs[] = f.listFiles();
        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                File currenFile = fs[i];
                if (currenFile.isFile()) {
                    fileCount += 1;   //如果是文件就加1
                    String fileName = currenFile.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (suffix.equals("java")) {
                        javaCount += 1;
                    }
                } else {                    //否则就是目录
                    dirCount += 1;
                    countFile(currenFile.getAbsolutePath());
                }
            }
        }
        return "Total number of File:" + fileCount + "||||The number of dir is:" + dirCount + "||||Total number of Java File:" + javaCount;
    }

}
