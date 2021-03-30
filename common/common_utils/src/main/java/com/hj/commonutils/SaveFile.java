package com.hj.commonutils;

import java.io.*;

public class SaveFile {
    public static void savePic(InputStream inputStream, String fileName,String pathLocal) {
        OutputStream os = null;

        try {
            String path = pathLocal ;

            // 2、保存到临时文件

            // 1K的数据缓冲

            byte[] bs = new byte[1024];

            // 读取到的数据长度

            int len;

            // 输出的文件流保存到本地文件

            File tempFile = new File(path);

            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }

            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);

            // 开始读取

            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);

            }

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try {
                os.close();

                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }
}
