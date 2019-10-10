package net.crandor;

import java.io.*;
import java.net.Socket;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class OnDemandFetcher implements Runnable {

    private final NodeList requested;
    private final CRC32 crc32;
    private final byte[] ioBuffer;
    private final NodeList aClass19_1344;
    private final NodeList aClass19_1358;
    private final byte[] gzipInputBuffer;
    private final NodeSubList nodeSubList;
    private final int[][] crcs;
    private final NodeList aClass19_1368;
    private final NodeList aClass19_1370;
    public String statusString;
    public int onDemandCycle;
    public Client clientInstance;
    public int anInt1349;
    int i1 = 0;
    private int[] regionIds;
    private int[] regionMapIds;
    private int[] regionMapObjectIds;
    private int anInt1332;
    private int writeLoopCycle;
    private long openSocketTime;
    private int completedSize;
    private int expectedSize;
    private int[] anIntArray1348;
    private boolean running;
    private OutputStream outputStream;
    @SuppressWarnings("unused")
    private boolean waiting;
    @SuppressWarnings("unused")
    private int[] anIntArray1360;
    private InputStream inputStream;
    private Socket socket;
    private int uncompletedCount;
    private int completedCount;
    private OnDemandData current;
    private int loopCycle;
    private GzipDecompressor gzipDecompressor = new GzipDecompressor();

    public OnDemandFetcher() {
        requested = new NodeList();
        statusString = "";
        crc32 = new CRC32();
        ioBuffer = new byte[500];
        aClass19_1344 = new NodeList();
        running = true;
        waiting = false;
        aClass19_1358 = new NodeList();
        gzipInputBuffer = new byte[999999];
        nodeSubList = new NodeSubList();
        crcs = new int[6][];
        aClass19_1368 = new NodeList();
        aClass19_1370 = new NodeList();
    }

    /**
     * Grabs the checksum of a file from the cache.
     *
     * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
     * @param id   The id of the file.
     * @return
     */
    public int getChecksum(int type, int id) {
        int crc = 0;
        byte[] data = clientInstance.decompressors[type + 1].decompress(id);
        if (data != null) {
            int length = data.length - 2;
            crc32.reset();
            crc32.update(data, 0, length);
            crc = (int) crc32.getValue();
        }
        return crc;
    }

    /**
     * Writes the checksum list for the specified archive type and length.
     *
     * @param type   The type of archive (0 = model, 1 = anim, 2 = midi, 3 = map, 4, textures, 6 osrs ).
     * @param length The number of files in the archive.
     */
    public void writeChecksumList(String file, int type) {

        System.out.println(type + ": " + clientInstance.decompressors[type + 1].getFileCount());

        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(signlink.findcachedir() + "crc/" + file));
            for (int index = 0; index < clientInstance.decompressors[type + 1].getFileCount(); index++) {
                out.writeInt(getChecksum(type, index));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        try {
            int j = inputStream.available();
            if (expectedSize == 0 && j >= 11) {
                waiting = true;
                for (int k = 0; k < 11; k += inputStream.read(ioBuffer, k, 11 - k));

                int l = ioBuffer[0] & 0xff;
                int j1 = ((ioBuffer[1] & 0xff) << 24) + ((ioBuffer[2] & 0xff) << 16) + ((ioBuffer[3] & 0xff) << 8) + (ioBuffer[4] & 0xff);
                int l1 = ((ioBuffer[5] & 0xff) << 24) + ((ioBuffer[6] & 0xff) << 16) + ((ioBuffer[7] & 0xff) << 8) + (ioBuffer[8] & 0xff);
                int i2 = ((ioBuffer[9] & 0xff) << 8) + (ioBuffer[10] & 0xff);

                current = null;
                for (OnDemandData onDemandData = (OnDemandData) requested.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested .reverseGetNext()) {
                    if (onDemandData.dataType == l && onDemandData.ID == j1)
                        current = onDemandData;
                    if (current != null)
                        onDemandData.loopCycle = 0;
                }
                if (current != null) {
                    loopCycle = 0;
                    if (l1 == 0) {
                        signlink.reporterror("Rej: " + l + "," + j1);
                        current.buffer = null;
                        if (current.incomplete)
                            synchronized (aClass19_1358) {
                                aClass19_1358.insertHead(current);
                            }
                        else
                            current.unlink();
                        current = null;
                    } else {
                        if (current.buffer == null && i2 == 0)
                            current.buffer = new byte[l1];
                        if (current.buffer == null && i2 != 0)
                            throw new IOException("missing start of file");
                    }
                }
                completedSize = i2 * 500;
                expectedSize = 500;
                if (expectedSize > l1 - i2 * 500)
                    expectedSize = l1 - i2 * 500;
            }
            if (expectedSize > 0 && j >= expectedSize) {
                waiting = true;
                byte abyte0[] = ioBuffer;
                int i1 = 0;
                if (current != null) {
                    abyte0 = current.buffer;
                    i1 = completedSize;
                }
                for (int k1 = 0; k1 < expectedSize; k1 += inputStream.read(
                        abyte0, k1 + i1, expectedSize - k1))
                    ;
                if (expectedSize + completedSize >= abyte0.length
                        && current != null) {
                    if (clientInstance.decompressors[0] != null)
                        clientInstance.decompressors[current.dataType + 1]
                                .pack(abyte0.length, abyte0, current.ID);
                    if (!current.incomplete && current.dataType == 3) {
                        current.incomplete = true;
                        current.dataType = 93;
                    }
                    if (current.incomplete)
                        synchronized (aClass19_1358) {
                            aClass19_1358.insertHead(current);
                        }
                    else
                        current.unlink();
                }
                expectedSize = 0;
            }
        } catch (IOException ioexception) {
            try {
                socket.close();
            } catch (Exception _ex) {
            }
            socket = null;
            inputStream = null;
            outputStream = null;
            expectedSize = 0;
        }
    }

    public void start(StreamLoader streamLoader, Client client1) {

        String as1[] = {"model_crc", "anim_crc", "midi_crc", "map_crc", "texture_crc", "osrs_crc"};

            for (int k = 0; k < as1.length; k++) {
                byte abyte1[] = streamLoader.getDataForName(as1[k]);
                int i1 = abyte1.length / 4;
                Stream stream_1 = new Stream(abyte1);

                System.out.println(as1[k] + " " + (i1 + 1));

                crcs[k] = new int[i1 + 1];
                for (int l1 = 0; l1 < i1; l1++)
                    crcs[k][l1] = stream_1.readDWord();
            }

        /**
         * * 530 map index
         */
        byte[] abyte2 = streamLoader.getDataForName("map_index");
        Stream stream2 = new Stream(abyte2);
        int j1 = stream2.readUnsignedWord();
        System.out.println("Map Amount: " + j1);
        regionIds = new int[j1];
        regionMapIds = new int[j1];
        regionMapObjectIds = new int[j1];

        int maxId = 0;

        for (int i2 = 0; i2 < j1; i2++) {
            regionIds[i2] = stream2.readUnsignedWord();
            regionMapIds[i2] = stream2.readUnsignedWord();
            regionMapObjectIds[i2] = stream2.readUnsignedWord();

            if (regionMapIds[i2] > maxId) {
                maxId = regionMapIds[i2];
            }
            if (regionMapObjectIds[i2] > maxId) {
                maxId = regionMapObjectIds[i2];
            }
        }

        //System.out.println("max id: " + maxId);

        abyte2 = streamLoader.getDataForName("midi_index");
        stream2 = new Stream(abyte2);
        j1 = abyte2.length;
        anIntArray1348 = new int[j1];
        for (int k2 = 0; k2 < j1; k2++)
            anIntArray1348[k2] = stream2.readUnsignedByte();

        clientInstance = client1;
        running = true;
        clientInstance.startRunnable(this, 2);

         /*for(int i = 0; i < as1.length; i++) {
              writeChecksumList(as1[i], i);
         }*/

    }

    public int getNodeCount() {
        synchronized (nodeSubList) {
            return nodeSubList.getNodeCount();
        }
    }

    public void disable() {
        running = false;
    }

    private void closeRequest(OnDemandData onDemandData) {
        try {
            if (socket == null) {
                long l = System.currentTimeMillis();
                if (l - openSocketTime < 4000L)
                    return;
                openSocketTime = l;
                socket = clientInstance.openSocket(43596);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(15);
                for (int j = 0; j < 8; j++)
                    inputStream.read();

                loopCycle = 0;
            }

            ioBuffer[0] = (byte) onDemandData.dataType;

            ioBuffer[1] = (byte) (onDemandData.ID >> 24);
            ioBuffer[2] = (byte) (onDemandData.ID >> 16);
            ioBuffer[3] = (byte) (onDemandData.ID >> 8);
            ioBuffer[4] = (byte) onDemandData.ID;

            //System.out.println(onDemandData.dataType + " " + (onDemandData.ID >> 8) + " " + onDemandData.ID);

            if (onDemandData.incomplete)
                ioBuffer[5] = 2;
            else if (!clientInstance.loggedIn)
                ioBuffer[5] = 1;
            else
                ioBuffer[5] = 0;

            outputStream.write(ioBuffer, 0, 6);
            writeLoopCycle = 0;
            anInt1349 = -10000;
            return;
        } catch (IOException ioexception) {
        }
        try {
            socket.close();
        } catch (Exception _ex) {
        }
        socket = null;
        inputStream = null;
        outputStream = null;
        expectedSize = 0;
        anInt1349++;
    }

    public void loadToCache(int type, int id) {

        // System.out.println(type + " " + id);

        // if (type == 5)
        //     System.out.println("loadToCache: " + type + " " + id);

       /* if (type == 5) {
            try {
                Files.copy(Paths.get("./osrs_models/" + id + ".dat"), Paths.get("./required_models/" + id + ".dat"), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        if (type < 0 || id < 0)
            return;

        /*if(type == 0 && (id == 2339 || id == 57650 || id == 7910)) {
            return;
        }

        if(type == 1 && id == 3622) {
            return;
        }*/

        /*if(i == 3) {
            try {
                copyFile(new File("./667maps/" + j + ".dat"), new File("./maps/" + j + ".dat"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        /*if(i == 0) {
            try {
                copyFile(new File("./667Objmodels/" + j + ".dat"), new File("./missing/" + j + ".dat"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        synchronized (nodeSubList) {
            for (OnDemandData onDemandData = (OnDemandData) nodeSubList.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) nodeSubList.reverseGetNext())
                if (onDemandData.dataType == type && onDemandData.ID == id)
                    return;



           // System.out.println("loadToCache: " + type + " " + id);


            OnDemandData onDemandData_1 = new OnDemandData();
            onDemandData_1.dataType = type;
            onDemandData_1.ID = id;
            onDemandData_1.incomplete = true;
            synchronized (aClass19_1370) {
                aClass19_1370.insertHead(onDemandData_1);
            }
            nodeSubList.insertHead(onDemandData_1);
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                onDemandCycle++;
                int i = 20;
                if (anInt1332 == 0 && clientInstance.decompressors[0] != null)
                    i = 50;
                try {
                    Thread.sleep(i);
                } catch (Exception _ex) {
                }
                waiting = true;
                for (int j = 0; j < 100; j++) {
                    waiting = false;
                    checkReceived();
                    handleFailed();
                    if (uncompletedCount == 0 && j >= 5)
                        break;
                    //method568();
                    if (inputStream != null)
                        readData();
                }
                boolean flag = false;
                for (OnDemandData onDemandData = (OnDemandData) requested
                        .reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested
                        .reverseGetNext())
                    if (onDemandData.incomplete) {
                        flag = true;
                        onDemandData.loopCycle++;
                        if (onDemandData.loopCycle > 50) {
                            onDemandData.loopCycle = 0;
                            closeRequest(onDemandData);
                        }
                    }
                if (!flag) {
                    for (OnDemandData onDemandData_1 = (OnDemandData) requested
                            .reverseGetFirst(); onDemandData_1 != null; onDemandData_1 = (OnDemandData) requested
                            .reverseGetNext()) {
                        flag = true;
                        onDemandData_1.loopCycle++;
                        if (onDemandData_1.loopCycle > 50) {
                            onDemandData_1.loopCycle = 0;
                            closeRequest(onDemandData_1);
                        }
                    }
                }
                if (flag) {
                    loopCycle++;
                    if (loopCycle > 750) {
                        try {
                            socket.close();
                        } catch (Exception _ex) {

                        }
                        socket = null;
                        inputStream = null;
                        outputStream = null;
                        expectedSize = 0;
                    }
                } else {
                    loopCycle = 0;
                    statusString = "";
                }
                if (clientInstance.loggedIn
                        && socket != null
                        && outputStream != null
                        && (anInt1332 > 0 || clientInstance.decompressors[0] == null)) {
                    writeLoopCycle++;
                    if (writeLoopCycle > 500) {
                        writeLoopCycle = 0;
                        ioBuffer[0] = 0;
                        ioBuffer[1] = 0;
                        ioBuffer[2] = 0;
                        ioBuffer[3] = 10;
                        try {
                            outputStream.write(ioBuffer, 0, 4);
                        } catch (IOException _ex) {
                            loopCycle = 5000;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFailed() {
        uncompletedCount = 0;
        completedCount = 0;
        for (OnDemandData onDemandData = (OnDemandData) requested.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext())
            if (onDemandData.incomplete)
                uncompletedCount++;
            else
                completedCount++;

        while (uncompletedCount < 10) {
            OnDemandData onDemandData_1 = (OnDemandData) aClass19_1368.popHead();
            if (onDemandData_1 == null)
                break;

            try {
                requested.insertHead(onDemandData_1);
                uncompletedCount++;
            } catch (Exception E) {
                System.out.println("Couldn't load model: " + onDemandData_1.ID + " Type: " + onDemandData_1.dataType);
            }

            closeRequest(onDemandData_1);
            waiting = true;
        }
    }

    public void loadExtra(int i, int j) {
        if (clientInstance.decompressors[0] == null)
            return;
        if (anInt1332 == 0)
            return;

        OnDemandData onDemandData = new OnDemandData();
        onDemandData.dataType = j;
        onDemandData.ID = i;
        onDemandData.incomplete = false;
        synchronized (aClass19_1344) {
            aClass19_1344.insertHead(onDemandData);
        }
    }

    public OnDemandData getNextNode() {
        OnDemandData onDemandData;
        synchronized (aClass19_1358) {
            onDemandData = (OnDemandData) aClass19_1358.popHead();
        }
        if (onDemandData == null) {
            return null;
        }

        synchronized (nodeSubList) {
            onDemandData.unlinkSub();
        }
        if (onDemandData.buffer == null)
            return onDemandData;
        int bufferLen = gzipDecompressor.decompress(onDemandData.buffer, gzipInputBuffer);
        onDemandData.buffer = new byte[bufferLen];
        System.arraycopy(gzipInputBuffer, 0, onDemandData.buffer, 0, bufferLen);
        return onDemandData;
    }

    public int getCrcCount(int index) {
        return crcs[index].length;
    }

    public int getMapIndex(int i, int k, int l) {

        int regionId = (l << 8) + k;

        for (int j1 = 0; j1 < regionIds.length; j1++) {
            if (regionIds[j1] == regionId) {
                if (i == 0)
                    return regionMapIds[j1];
                else
                    return regionMapObjectIds[j1];
            }
        }

        return -1;
    }

    public void method548(int type, int i) {
        loadToCache(type, i);
    }

    public void method548(int i) {
        loadToCache(0, i);
    }

    public boolean method564(int i) {
        for (int k = 0; k < regionIds.length; k++)
            if (regionMapObjectIds[k] == i)
                return true;
        return false;
    }

    public void method566() {
        synchronized (aClass19_1344) {
            aClass19_1344.clear();
        }
    }

    private void checkReceived() {
        try {
            OnDemandData onDemandData;
            synchronized (aClass19_1370) {
                onDemandData = (OnDemandData) aClass19_1370.popHead();
            }
            while (onDemandData != null) {
                waiting = true;
                byte abyte0[] = null;
                if (clientInstance.decompressors[0] != null)
                    abyte0 = clientInstance.decompressors[onDemandData.dataType + 1].decompress(onDemandData.ID);

                /*try {
                    System.out.println(onDemandData.ID + " " + onDemandData.dataType + " " + abyte0.length);
                } catch (Exception e) {
                    System.out.println("abyte0.length == null " + onDemandData.ID + " " + onDemandData.dataType);
                }*/

                try {
                    if (!crcMatches(crcs[onDemandData.dataType][onDemandData.ID], abyte0)) {
                        abyte0 = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(onDemandData.dataType + " " + onDemandData.ID + " " + crcs[onDemandData.dataType].length);
                }

                synchronized (aClass19_1370) {
                    if (abyte0 == null) {
                        aClass19_1368.insertHead(onDemandData);
                    } else {
                        onDemandData.buffer = abyte0;
                        synchronized (aClass19_1358) {
                            aClass19_1358.insertHead(onDemandData);
                        }
                    }
                    onDemandData = (OnDemandData) aClass19_1370.popHead();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean method569(int i) {
        return anIntArray1348[i] == 1;
    }

    private boolean crcMatches(int crc, byte abyte0[]) {

        if (abyte0 == null || abyte0.length < 2)
            return false;
        int k = abyte0.length - 2;
        crc32.reset();
        crc32.update(abyte0, 0, k);
        int i1 = (int) crc32.getValue();

        return i1 == crc;
    }
}
