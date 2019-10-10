package net.crandor;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

public class GameShell extends Applet implements Runnable, MouseListener,
        MouseMotionListener, MouseWheelListener, KeyListener, FocusListener,
        WindowListener {

    public static long mainRedrawCache[] = new long[32];
    public static int mainRedrawCachePos;
    final int keyArray[] = new int[128];
    private final int charQueue[] = new int[128];
    public int mouseX;
    public int mouseY;
    public int clickMode3;
    public int saveClickX;
    public int saveClickY;
    public boolean isApplet;
    public Frame frame;
    public Insets insets;
    public Canvas canvas;
    int fps;
    int frameWidth;
    static int frameHeight;
    RSImageProducer fullGameScreen;
    volatile boolean awtFocus;
    int idleTime;
    int clickMode2;
    long aLong29;
    volatile boolean fullRedraw;
    private int clickMode1;
    private int clickX;
    private int clickY;
    private long clickTime;
    private int charQueueTail;
    private int charQueueHead;
    public NanoTimer nanoTimer = new NanoTimer();
    public int gameLoopCount;
    private int minSleep = 1;
    private int maxSleep = 20;
    private long killtime;
    private Image loadingImage;
    private Font helveticaFont;
    private FontMetrics helveticaFontMetrics;
    private Color loadingBarColor = new Color(140, 17, 17);
    public int notches;
    private int canvasRefreshCycle = 500;

    GameShell() {
        fullRedraw = true;
        awtFocus = true;
    }

    public void rebuildFrame(boolean undecorated, int width, int height,
                             boolean resizable, boolean full) {
        boolean createdByApplet = (isApplet && !full);
        frameWidth = width;
        frameHeight = height;
        if (frame != null) {
            frame.dispose();
        }
        if (!createdByApplet) {
            frameWidth = width;
            frameHeight = height;
            createFrame(resizable, undecorated);
            addCanvas();
            canvas.addMouseWheelListener(this);
            canvas.addMouseListener(this);
            canvas.addMouseMotionListener(this);
            canvas.addKeyListener(this);
            canvas.setFocusTraversalKeysEnabled(false);
        }
    }

    synchronized void addCanvas() {
        if (canvas != null) {
            canvas.removeFocusListener(this);
            canvas.getParent().remove(canvas);
        }
        Container container;
        if (frame != null) {
            container = frame;
        } else {
            container = this;
        }
        container.setLayout(null);
        canvas = new CanvasWrapper(this);
        container.add(canvas);
        canvas.setSize(frameWidth, frameHeight);
        canvas.setVisible(true);
        if (frame == container) {
            canvas.setLocation(insets.left, insets.top);
        } else {
            canvas.setLocation(0, 0);
        }
        canvas.addFocusListener(this);
        canvas.requestFocus();
        fullRedraw = true;
    }

    final void createFrame(boolean resizable, boolean undecorated) {
        frame = new Frame();
        frame.setTitle(Constants.CLIENT_NAME + " " + Constants.CLIENT_VERSION);
        frame.setResizable(resizable);
        frame.addWindowListener(this);
        frame.setUndecorated(undecorated);
        frame.setVisible(true);
        frame.toFront();
        insets = frame.getInsets();
        frame.setSize(insets.left + frameWidth + insets.right, insets.bottom + frameHeight + insets.top);
    }

    final void startApplication(int w, int h) {
        isApplet = false;
        frameWidth = w;
        frameHeight = h;
        createFrame(Client.clientSize == 1, Client.clientSize == 2);
        fullGameScreen = new RSImageProducer(frameWidth, frameHeight, canvas);
        startRunnable(this, 1);
    }

    final void startApplet(int w, int h) {
        isApplet = true;
        frameWidth = w;
        frameHeight = h;
        fullGameScreen = new RSImageProducer(frameWidth, frameHeight, canvas);
        startRunnable(this, 1);
    }

    @Override
    public void run() {
        addCanvas();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        canvas.addFocusListener(this);
        canvas.addMouseWheelListener(this);
        canvas.setFocusTraversalKeysEnabled(false);
        drawLoadingText(0, "Loading...");
        mainInit();
        while (killtime == 0L || System.currentTimeMillis() < killtime) {
            try {
                gameLoopCount = nanoTimer.sleep(minSleep, maxSleep);
                for (int loop = 0; loop < gameLoopCount; loop++) {
                    clickMode3 = clickMode1;
                    saveClickX = clickX;
                    saveClickY = clickY;
                    aLong29 = clickTime;
                    clickMode1 = 0;
                    mainLoop();
                    charQueueTail = charQueueHead;
                }
                mainRedrawWrapper();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        shutdownApplet();
    }

    private void shutdownApplet() {
        cleanUpForQuit();
        if (frame != null) {
            try {
                System.exit(0);
            } catch (Throwable throwable) {
                /* empty */
            }
        }
    }

    final void setFps(int fps) {
        maxSleep = 1000 / fps;
    }

    @Override
    public final void start() {
        killtime = 0L;
    }

    @Override
    public final void stop() {
        killtime = System.currentTimeMillis() + 4000;
    }

    @Override
    public final void destroy() {
        killtime = System.currentTimeMillis();
        Client.sleepWrapper(5000L);
        shutdownApplet();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public synchronized void paint(Graphics g) {
        fullRedraw = true;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        if (mouseX > 0 && mouseX < 512 && mouseY > Client.frameHeight - 165
				&& mouseY < Client.frameHeight - 25) {
			if (Client.messageCount <= 5) {
				return;
			}
        }
        if (event.isControlDown()) {
            if (rotation == -1) {
                if (Client.cameraZoom > 200) {
                    Client.cameraZoom -= 50;
                }
            } else {
                if (Client.cameraZoom < 900) {
                    Client.cameraZoom += 50;
                }
            }
        } else {
        	notches += rotation;
        }
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        idleTime = 0;
        clickX = x;
        clickY = y;
        clickTime = System.currentTimeMillis();
        if (e.isAltDown()) {
            clickMode1 = 3;
            clickMode2 = 3;
        } else if (e.isMetaDown()) {
            clickMode1 = 2;
            clickMode2 = 2;
        } else {
            clickMode1 = 1;
            clickMode2 = 1;
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        idleTime = 0;
        clickMode2 = 0;
    }

    @Override
    public final void mouseClicked(MouseEvent mouseevent) {
    }

    @Override
    public final void mouseEntered(MouseEvent mouseevent) {
    }

    @Override
    public final void mouseExited(MouseEvent mouseevent) {
        idleTime = 0;
        mouseX = -1;
        mouseY = -1;
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        idleTime = 0;
        mouseX = x;
        mouseY = y;
    }

    @Override
    public final void mouseMoved(MouseEvent mouseevent) {
        int x = mouseevent.getX();
        int y = mouseevent.getY();
        idleTime = 0;
        mouseX = x;
        mouseY = y;
    }

    @Override
    public final void keyPressed(KeyEvent keyevent) {
        idleTime = 0;
        int i = keyevent.getKeyCode();
        int j = keyevent.getKeyChar();
        if (keyevent.isShiftDown() && keyevent.isControlDown()) {
            Client.teleportClick = true;
        }
		if (keyevent.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (Client.openInterfaceID != -1) {
				if (Client.openInterfaceID == 5292 && Client.instance.playerVariables[0] == 1) {
					Client.instance.playerVariables[0] = 0;
					Client.instance.messagePromptRaised = false;
				}
				Client.instance.clearTopInterfaces();
			} else if (Client.clientSize == 2) {
				Client.toggleSize(0, false);
			} else {
				Client.setTab(Client.LOGOUT_TAB);
			}
			return;
		}
        if ((keyevent.isControlDown() && keyevent.getKeyCode() == KeyEvent.VK_V)) {
            if (Client.loginScreenCursorPos == 0) {
                Client.myUsername += Client.getClipboardContents();
            } else if (Client.loginScreenCursorPos == 1) {
                Client.myPassword += Client.getClipboardContents();
            } else {
                if (Client.myPrivilege == 10) {
                    Client.inputString += Client.getClipboardContents();
                    Client.reDrawChatArea = true;
                }
            }
        }

        if (i == KeyEvent.VK_F1) {
            Client.setTab(Client.INVENTORY_TAB);
        } else if (i == KeyEvent.VK_F2) {
            Client.setTab(Client.EQUIPMENT_TAB);
        } else if (i == KeyEvent.VK_F3) {
            Client.setTab(Client.PRAYER_TAB);
        } else if (i == KeyEvent.VK_F4) {
            Client.setTab(Client.MAGIC_TAB);
        } else if (i == KeyEvent.VK_F5) {
            Client.setTab(Client.COMBAT_TAB);
        }
        if (j < 30)
            j = 0;
        if (i == 37)
            j = 1;
        if (i == 39)
            j = 2;
        if (i == 38)
            j = 3;
        if (i == 40)
            j = 4;
        if (i == 17)
            j = 5;
        if (i == 8)
            j = 8;
        if (i == 127)
            j = 8;
        if (i == 9)
            j = 9;
        if (i == 10)
            j = 10;
        if (i >= 112 && i <= 123)
            j = (1008 + i) - 112;
        if (i == 36)
            j = 1000;
        if (i == 35)
            j = 1001;
        if (i == 33)
            j = 1002;
        if (i == 34)
            j = 1003;
        if (j > 0 && j < 128)
            keyArray[j] = 1;
        if (j > 4) {
            charQueue[charQueueHead] = j;
            charQueueHead = charQueueHead + 1 & 0x7f;
        }
    }

    @Override
    public final void keyReleased(KeyEvent keyevent) {
        idleTime = 0;
        int i = keyevent.getKeyCode();
        char c = keyevent.getKeyChar();
        if (c < '\036') {
            c = '\0';
        }
        if (i == 37) {
            c = '\001';
        }
        if (i == 39) {
            c = '\002';
        }
        if (i == 38) {
            c = '\003';
        }
        if (i == 40) {
            c = '\004';
        }
        if (i == 17) {
            c = '\005';
        }
        if (i == 8) {
            c = '\b';
        }
        if (i == 127) {
            c = '\b';
        }
        if (i == 9) {
            c = '\t';
        }
        if (i == 10) {
            c = '\n';
        }
        if (c > 0 && c < '\200') {
            keyArray[c] = 0;
        }
        Client.teleportClick = false;
    }

    @Override
    public final void keyTyped(KeyEvent keyevent) {
    }

    public final int readChar() {
        int k = -1;
        if (charQueueHead != charQueueTail) {
            k = charQueue[charQueueTail];
            charQueueTail = charQueueTail + 1 & 0x7f;
        }
        return k;
    }

    @Override
    public final void focusGained(FocusEvent focusevent) {
        awtFocus = true;
        fullRedraw = true;
    }

    @Override
    public final void focusLost(FocusEvent focusevent) {
        awtFocus = false;
        Client.teleportClick = false;
        for (int i = 0; i < 128; i++) {
            keyArray[i] = 0;
        }

    }

    @Override
    public final void windowActivated(WindowEvent windowevent) {
    }

    @Override
    public final void windowClosed(WindowEvent windowevent) {
    }

    @Override
    public final void windowClosing(WindowEvent windowevent) {
        String options[] = {"Yes", "No"};
        int userPrompt = JOptionPane.showOptionDialog(null, "Are you sure you wish to exit?", "" + Constants.CLIENT_NAME + "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        if (userPrompt == JOptionPane.YES_OPTION) {
            destroy();
        }
    }

    @Override
    public final void windowDeactivated(WindowEvent windowevent) {
    }

    @Override
    public final void windowDeiconified(WindowEvent windowevent) {
    }

    @Override
    public final void windowIconified(WindowEvent windowevent) {
    }

    @Override
    public final void windowOpened(WindowEvent windowevent) {
    }

    void mainInit() {
    }

    void mainLoop() {
    }

    void cleanUpForQuit() {
    }

    void mainRedrawWrapper() {
        long currentTime = System.currentTimeMillis();
        long oldTime = mainRedrawCache[mainRedrawCachePos];
        mainRedrawCache[mainRedrawCachePos] = currentTime;
        mainRedrawCachePos = mainRedrawCachePos + 1 & 0x1f;
        if (oldTime != 0 && oldTime < currentTime) {
            int timeDelta = (int) (currentTime - oldTime);
            fps = ((timeDelta >> 1) + 32000) / timeDelta;
        }
        if (canvasRefreshCycle++ > 50) {
        	canvasRefreshCycle -= 50;
        	fullRedraw = true;
        	canvas.setSize(frameWidth, frameHeight);
        	canvas.setVisible(true);
            if (frame != null) {
                canvas.setLocation(insets.left, insets.top);
            } else {
                canvas.setLocation(0, 0);
            }
        }
        mainRedraw();
    }

    void mainRedraw() {
    }

    public Graphics getGraphics() {
        return canvas.getGraphics();
    }

    public void startRunnable(Runnable runnable, int i) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
    }
    
    public void discardLoadingVariales() {
    	helveticaFont = null;
    	loadingImage = null;
    	helveticaFontMetrics = null;
    	loadingBarColor = null;
    }

    void drawLoadingText(int percentage, String loadingText) {
        Client.getClient().checkSize();
        if (helveticaFont == null) {
            helveticaFont = new Font("Helvetica", 1, 13);
            helveticaFontMetrics = canvas.getFontMetrics(helveticaFont);
        }
        if (fullRedraw) {
            getGraphics().setColor(Color.black);
            getGraphics().fillRect(0, 0, frameWidth, frameHeight);
            fullRedraw = false;
        }
        int x = frameWidth / 2 - 152;
        int y = frameHeight / 2 - 18;
        try {
            if (loadingImage == null) {
                loadingImage = canvas.createImage(304, 34);
            }
            Graphics graphics = loadingImage.getGraphics();
            graphics.setColor(loadingBarColor);
            graphics.drawRect(0, 0, 303, 33);
            graphics.fillRect(2, 2, percentage * 3, 30);
            graphics.setColor(Color.black);
            graphics.drawRect(1, 1, 301, 31);
            graphics.fillRect(2 + percentage * 3, 2, 300 - percentage * 3, 30);
            graphics.setFont(helveticaFont);
            graphics.setColor(Color.white);
            graphics.drawString(loadingText, (304 - helveticaFontMetrics.stringWidth(loadingText)) / 2, 22);
            getGraphics().drawImage(loadingImage, x, y, null);
        } catch (Exception e) {
            canvas.repaint();
        }
    }

}
