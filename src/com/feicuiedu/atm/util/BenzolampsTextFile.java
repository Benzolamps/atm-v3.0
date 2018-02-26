package com.feicuiedu.atm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 处理文本文件的类
 *
 * @author Benzolamps
 */
public class BenzolampsTextFile {

    private String fileName; // 文件名
    private String encoding; // 文件编码

    /**
     * 通过文件名构造对象, 文件编码默认为UTF-8
     *
     * @param fileName 文件名
     */
    public BenzolampsTextFile(String fileName) {
        this(fileName, "UTF-8");
    }

    /**
     * 通过文件名和文件编码构造对象
     * @param fileName 文件名
     * @param encoding 文件编码
     */
    public BenzolampsTextFile(String fileName, String encoding) {
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(encoding);

        this.fileName = fileName;
        this.encoding = encoding;
    }

    /**
     * 将文件中的数据存储到字符串中
     * @return
     * @throws IOException
     */
    public String read() throws IOException {

        File file = new File(fileName); // 创建文件对象
        byte[] content = new byte[4096]; // 创建缓存字节数组
        FileInputStream fis = null; // 文件输入流
        StringBuilder sb = new StringBuilder(); // 创建存储字符串的StringBuilder

        try {

            int count = 0; // 本次读到的字符数
            fis = new FileInputStream(file); // 构建文件输入流

            while (count != -1) { // 当读到-1个字符时退出循环

                // 将独到的数据拼接到StringBuilder
                sb.append(new String(content, 0, count, encoding));

                count = fis.read(content); // 读取下一组
            }
            return sb.toString(); // 返回字符串

        } catch (IOException e) {

            throw e;

        } finally {

            if (fis != null) {
                fis.close(); // 关闭输入流
            }
        }
    }
}
