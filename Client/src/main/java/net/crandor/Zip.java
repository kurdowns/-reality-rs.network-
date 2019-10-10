package net.crandor;

import java.io.*;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class Zip {

    public HashMap<String, byte[]> files = new HashMap<>();

    void load(byte[] buffer) {
        try {
            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(buffer));
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];

                int count = -1;
                while ((count = zipStream.read(buff)) != -1) {
                    out.write(buff, 0, count);
                }

                files.put(entry.getName().toLowerCase(), out.toByteArray());
            }

            zipStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] getFile(String str) {
        str = str.toLowerCase();

        byte[] image = files.get(str);

        // if (image == null) {
        //     System.out.println("no image entry for " + str);
        // }

        return image;
    }

}
