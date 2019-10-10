package net.crandor;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;


// note(Stuart): please use the sprites.zip in the titles archive, saves messing around with galkons rubbish

public class Client extends GameShell {

    public static final int REGULAR_WIDTH = 765, REGULAR_HEIGHT = 503;
    public static final String[] skillNames = {"Attack", "Constitution", "Mining",
            "Strength", "Agility", "Smithing", "Defence", "Herblore",
            "Fishing", "Range", "Thieving", "Cooking", "Prayer", "Crafting",
            "Firemaking", "Magic", "Fletching", "Woodcutting", "Runecrafting",
            "Slayer", "Farming", "Construction", "Hunter", "Summoning",
            "Dungeoneering"};
	private static final int[] CAPE_COLORS = { 65214, 65200, 65186, 62995 };
    static final int COMBAT_TAB = 0;
    static final int STATS_TAB = 1;
	static final int QUEST_TAB = 2;
	static final int INVENTORY_TAB = 3;
	static final int EQUIPMENT_TAB = 4;
	static final int PRAYER_TAB = 5;
	static final int MAGIC_TAB = 6;
	static final int CLAN_CHAT_TAB = 7;
	static final int FRIENDS_TAB = 8;
	static final int IGNORES_TAB = 9;
	static final int LOGOUT_TAB = 10;
	static final int SETTINGS_TAB = 11;
	static final int EMOTES_TAB = 12;
	static final int MUSIC_TAB = 13;
	static final int ACHIEVEMENTS_TAB = 14;
	static final int SUMMONING_TAB = 15;
	static final int INFORMATION_TAB = 16;
    static final int[] XP_DROP_SKILL_IDS = {273, 278, 295, 285, 290, 289, 287, 276, 299, 283, 282, 281, 277, 294, 288, 284, 272, 297, 293, 280, 291, 286, 275, 296, 279, 0, 0, 0, 0, 0, 0, 0};
    static final int[] SKILL_TAB_IDS = {0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6, 9, 8, 20, 18, 19, 21, 22, 23, 24};
    static final int[] BANK_TAB_IDS = {34176, 36224, 39552, 42880, 46208, 49536, 52864, 56192, 59520};
    static final String[] TAB_AREA_TOOLTIPS = {"Combat Styles", "Achievements", "Stats",
            "Inventory", "Worn Equipment", "Prayer List",
            "Magic Spellbook", "Clan Chat", "Friends List", "Ignore List",
            "Logout", "Options", "Emotes", "Music", "Quest Journal",
            "Summoning", "Information"};
    static final String[] CLAN_CHAT_RANKS = {"", "", "General", "Captain", "Lieutenant", "Sergeant", "Corporal", "Recruit", "Member"};
    static final int[] BANK_WITHDRAW_OPS_IDS = {15292, 15293, 15294, 15295, 15296, 15297, 15298, 15299, 15300, 15302};
    static final int[] SPELL_HOVER_IDS = {1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258,
            1267, 1274, 1283, 1573, 1290, 1299, 1308, 1315, 1324, 1333,
            1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583,
            12038, 1414, 1421, 1430, 1437, 1446, 1453, 1460, 1469,
            15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503,
            1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426,
            12436, 12446, 12456, 6004};
    static final int[] RESIZABLE_TAB_POS_X = {0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60,
            90, 120, 150, 180, 210};

	static final int[] FIXED_SIDE_ICON_TAB_ID = { COMBAT_TAB, ACHIEVEMENTS_TAB, STATS_TAB, QUEST_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB, MAGIC_TAB, SUMMONING_TAB, FRIENDS_TAB, IGNORES_TAB, CLAN_CHAT_TAB, SETTINGS_TAB, EMOTES_TAB, MUSIC_TAB, INFORMATION_TAB };
	static final int[] FIXED_SIDE_ICON_ID = { 20, 40, 21, 22, 23, 24, 25, 26, 42, 28, 29, 27, 31, 32, 33, 41 };
	static final int[] FIXED_SIDE_ICON_POS_X = { 14, 42, 72, 102, 132, 164, 192, 222, 12, 43, 74, 102, 132, 162, 192, 222 };
	static final int[] FIXED_SIDE_ICON_POS_Y = { 9, 9, 8, 8, 8, 8, 8, 8, 305, 306, 306, 307, 306, 306, 306, 306 };
	static final int[] FIXED_TAB_CLICK_X = { 3, 63, 93, 123, 153, 183, 213, 93, 33, 63, 0, 123, 153, 183, 33, 3, 213 };
	static final int[] FIXED_TAB_CLICK_Y = { 0, 0, 0, 0, 0, 0, 0, 299, 299, 299, 0, 299, 299, 299, 0, 299, 299 };
	static final int[] FIXED_TAB_HOVER_X = { 8, 68, 98, 128, 158, 188, 218, 98, 38, 68, 0, 128, 158, 188, 38, 8, 218 };
	static final int[] FIXED_TAB_HOVER_Y = { 0, 0, 0, 0, 0, 0, 0, 298, 298, 298, 0, 298, 298, 298, 0, 298, 298 };

    static final int[] RESIZABLE_SIDE_ICON_ID = {20, 40, 21, 22, 23, 24, 25, 26, 42, 28, 29, 27, 31,
            32, 33, 41};
    static final int[] RESIZABLE_SIDE_ICON_POS_X = {8, 37, 67, 97, 127, 159, 187, 217, 7, 38, 69,
            97, 127, 157, 187, 217};
    static final int[] RESIZABLE_SIDE_ICON_POS_Y = {9, 9, 8, 8, 8, 8, 8, 8, 7, 8, 8, 9, 8, 8, 8, 8};
    static final int[][] anIntArrayArray1003 = {
            {6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983,
                    54193},
            {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153,
                    56621, 4783, 1341, 16578, 35003, 25239},
            {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094,
                    10153, 56621, 4783, 1341, 16578, 35003},
            {4626, 11146, 6439, 12, 4758, 10270},
            {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
    static final int[] skinColor2 = 
    	    {4540, 4529, 5674, 5667, 5785, 6802, 8072, 4562};
    final static int[] tabInterfaceIDs = {-1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    static final int[] anIntArray1204 = {9104, 10275, 7595, 3610, 7975, 8526,
            918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486};
    static final String CHAT_BUTTONS_OPS_TEXTS[] = {"On", "Friends", "Off", "Hide"};
    static final int CHAT_BUTTONS_TEXT_COLORS[] = {65280, 0xffff00, 0xff0000, 65535};
    static final int[] CHAT_BUTTONS_POS_X = {5, 62, 119, 176, 233, 290, 347, 404};
    private static final long serialVersionUID = 1L;
    private static final String[] INFUSE_OPTIONS = {
            "Infuse",
            "Infuse-5",
            "Infuse-10",
            "Infuse-All",
            "Infuse-X"
    };
    private static final String[] TRANSFORM_OPTIONS = {
            "Transform",
            "Transform-5",
            "Transform-10",
            "Transform-All",
            "Transform-X"
    };
    private static final int[] xpForLvl;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    public static int cameraZoom = 600;
    public static Zip spriteCache;
    public static Model playerModel;
    public static boolean teleportClick = false;
    public static boolean preloadedCache = false;
    public static int targetInterfaceId = 0;
    public static Client instance;
    public static int clientSize = 0;
    public static boolean showTab = true;
    public static boolean showChat = true;
    public static RSFont small;
    public static int hoverPos = -1;
    public static int myPrivilege;
    public static String inputString;
    public static boolean lowMem;
    public static int loginScreenCursorPos;
    public static boolean reDrawChatArea;
    public static int bitfieldMask[];
    public static boolean disableUnderlayTexture = false;
    public static boolean disableOverlayTexture = false;
    public static boolean buildingMode = true;
    public static boolean inHouse = false;
    public static int totalItemResults;
    public static int itemResultScrollPos;
    public static int gameAreaWidth = 512;
    public static int inputDialogState;
    protected static String myUsername;
    protected static String myPassword;
    static int openInterfaceID;
    static int portOff;
    static boolean clientData;
    static int chatboxScrollPos;
    static Player myPlayer;
    static CustomizedItem customizedItem;
    static boolean reDrawTabArea;
    static int loopCycle;
    static Stream stream;
    static int chatboxMaxScroll;
    static int tabID;
    private static ExpDrop[] expDrops = new ExpDrop[30];
    private static int cButtonHPos;
    private static int cButtonCPos;
    private static int nodeID = 10;
    private static boolean isMembers = true;
    private static int baseX;
    private static int baseY;
    private static int anInt1061;
    private static NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    static {
        xpForLvl = new int[99];
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int l = j + 1;
            int i1 = (int) (l + 300D * Math.pow(2D, l / 7D));
            i += i1;
            xpForLvl[j] = i / 4;
        }
        bitfieldMask = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            bitfieldMask[k] = i - 1;
            i += i;
        }
    }

    final Decompressor[] decompressors;
    private final int[] statsXp;
    private final int[] anIntArray873;
    private final boolean[] useCustomCamera;
    private final int maxPlayers;
    private final int myPlayerIndex;
    private final int[] stats;
    private final long[] ignoreListAsLongs;
    private final int[] anIntArray928;
    private final int[] anIntArray965 = {0xffff00, 0xff0000, 65280, 65535,
            0xff00ff, 0xffffff};
    private final int[] anIntArray968;
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] anIntArray980;
    private final int[] chat_effects;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private final int[] anIntArray990;
    private final int[] cameraTransVars2;
    public final int[] statsBase;
    private final int[] anIntArray1045;
    private static int[] minimapShape1;
    private final int[] anIntArray1057;
    private final RSInterface chatboxInterface;
    private final int barFillColor;
    private final int[] anIntArray1065;
    private final int[] expectedCRCs;
    private final String[] atPlayerActions;
    private final boolean[] atPlayerArray;
    private final int[][][] constructionMapInformation;
    private final int[] anIntArray1177 = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    private final int[] customLowestYaw;
    private final int[] songIDS;
    private static int[] minimapShape2;
    private final int[] songVolumes;
    private final int[] anIntArray1250;
    private final boolean rsAlreadyLoaded;
    private final Sprite[] modIcons;
    private final int expArray[] = {
            0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
            3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247,
            20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886,
            273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087,
            2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };
    // ge variables
    public int slotUsing = 0;
    public int slotSelected;
    public int geSlots[] = new int[7];
    public String geSlotsText[] = new String[7];
    public int[] slotPrices = new int[7];
    public int slotItemId[] = new int[7];
    public int slotColor[] = new int[7];
    public int slotColorPercent[] = new int[7];
    public int slotItems[] = new int[7];
    public int slotQuantity[] = new int[7];
    public boolean slotAborted[] = new boolean[7];
    public int GEItemId = -1;
    public boolean autoCast = false;
    public Sprite[] cacheSprite;
    public int worldMapHPos = 0;
    public int chatTypeView;
    public int clanChatMode;
    public int challengeChatMode;
    public RSFont bold;
    public RSFont fancy;
    public RSFont regular;
    public int xpCounter;
    public int anInt945;
    public static int[] playerVariables;
    public boolean loggedIn;
    public int fullscreenInterfaceID;
    public int anInt1044;// 377
    public int anInt1129;// 377
    public int anInt1315;// 377
    public int anInt1500;// 377
    public int anInt1501;// 377
    public String itemResultNames[] = new String[100];
    public int itemResultIDs[] = new int[100];
    public Sprite per0;
    public Sprite per1;
    public Sprite per2;
    public Sprite per3;
    public Sprite per4;
    public Sprite per5;
    public Sprite per6;
    public Sprite abort1;
    public Sprite abort2;
    public Sprite SellHover;
    public Sprite geImage30;
    public Sprite geSprite29;
    public Sprite geSprite28;
    public Sprite geSprite680;
    public Sprite geSprite678;
    public Sprite geSprite27;
    public Sprite BuyHover656;
    public Sprite BuyHover654;
    public Sprite sellSubmitHover;
    OnDemandFetcher onDemandFetcher;
    private Sprite[] moneyPouchSprites;
    private int withdrawAmount = -1;
    private Sprite magicAuto = null;
    private int autocastId = 0;
    private int gameAreaHeight = 334;
    private CRC32 aCRC32_930 = new CRC32();
    private int hoverOnShopTile = -1;
    private int currency = 0;
    /* Declare custom sprites */
    private Sprite[] chatButtons;
    private Set<String> clanNames = new HashSet<>();
    private Sprite multiOverlay;
    private int smallTabs = 1000;
    private int tabHover = -1;
    private boolean counterOn = false;
    private RSImageProducer leftFrame;
    private RSImageProducer topFrame;
    private RSImageProducer rightFrame;
    private int ignoreCount;
    private long aLong824;
    private int[][] anIntArrayArray825;
    private int[] friendsNodeIDs;
    private NodeList[][][] groundArray;
    private Socket aSocket832;
    private Stream aStream_834;
    private NPC[] npcArray;
    private int npcCount;
    private int[] npcIndices;
    private int anInt839;
    private int[] anIntArray840;
    private int anInt841;
    private int anInt842;
    private int anInt843;
    private String aString844;
    private int privateChatMode;
    private Stream aStream_847;
    private boolean waveON;
    private int headiconDrawType;
    private int xCameraPos;
    private int zCameraPos;
    private int yCameraPos;
    private int yCameraCurve;
    private int xCameraCurve;
    private Sprite mapFlag;
    private Sprite mapMarker;
    private int anInt874;
    private int weight;
    private String reportAbuseInput;
    private int playerID;
    private int anInt886;
    private Player[] playerArray;
    private Sprite loyalty1;
    private Sprite loyalty2;
    private int playerCount;
    private int[] playerIndices;
    private int sessionNpcsAwaitingUpdatePtr;
    private int[] sessionNpcsAwaitingUpdate;
    private Stream[] playerUpdateStreams;
    private int friendsCount;
    private int networkFriendsServerStatus;
    private int[][] anIntArrayArray901;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    private int plane;
    private boolean loadingError;
    private int[][] anIntArrayArray929;
    private Sprite aClass30_Sub2_Sub1_Sub1_931;
    private Sprite aClass30_Sub2_Sub1_Sub1_932;
    private int otherPlayerID;
    private int headiconX;
    private int headiconY;
    private int headiconHeight;
    private int arrowDrawTileX;
    private int arrowDrawTileY;
    private WorldController worldController;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int menuHeight;
    private long aLong953;
    private boolean aBoolean954;
    private long[] friendsListAsLongs;
    private int currentSong;
    private int spriteDrawX;
    private int spriteDrawY;
    private int tabClickDelay;
    private boolean aBoolean972;
    private int anInt984;
    private int anInt985;
    private Sprite[] hitMarks;
    private Sprite[] hitMarks562;
    private Sprite[] hitMark;
    private Sprite[] hitIcon;
    private int anInt989;
    private int anInt995;
    private int anInt996;
    private int anInt997;
    private int anInt998;
    private int anInt999;
    private ISAACRandomGen encryption;
    private Random random = new Random();
    private String amountOrNameInput;
    private int daysSinceLastLogin;
    private int pktSize;
    private int pktType;
    private int anInt1009;
    private int anInt1010;
    private int anInt1011;
    private NodeList projectileNode;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int currentStatusInterface;
    private int minimapLock;
    private int loadingStage;
    private Sprite scrollBar1;
    private Sprite scrollBar2;
    private int anInt1026;
    private boolean aBoolean1031;
    private Sprite[] mapFunctions;
    private int anInt1036;
    private int anInt1037;
    private int loginFailures;
    private int anInt1039;
    private int dialogID;
    private int isMember;
    private boolean aBoolean1047;
    private int anInt1048;
    private String aString1049;
    private StreamLoader titleStreamLoader;
    private int anInt1055;
    private NodeList stillGraphicsNode;
    private Background[] mapScenes;
    private int anInt1062;
    private int chatActionID;
    private int clanChatAction = -1;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;
    private static int anInt1069;
    private static int anInt1070;
    private int numOfMapMarkers;
    private int[] markPosX;
    private int[] markPosY;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;
    private Sprite mapDotClan;
    private Sprite mapDotElite;
    private int anInt1079;
    private boolean aBoolean1080;
    private String[] friendsList;
    private Stream inStream;
    private int selectedItemInterfaceID;
    private int selectedItemSlot;
    private int activeInterfaceType;
    private int anInt1087;
    private int anInt1088;
    private Sprite[] headIcons;
    private Sprite[] skullIcons;
    private Sprite[] headIconsHint;
    private int anInt1098;
    private int anInt1099;
    private int anInt1100;
    private int anInt1101;
    private int anInt1102;
    private int systemUpdateTime;
    private int membersInt;
    private String messagePromptText;
    private Sprite compass;
    private int anInt1131;
    private int targetBitMask;
    private String targetOption;
    private int[] markGraphic;
    private boolean tutorialIsland;
    private int energy;
    private boolean isDialogueInterface;
    private Sprite[] crosses;
    private boolean musicEnabled;
    private int welcomeScreenUnreadMessagesCount;
    private boolean canMute;
    private boolean loadGeneratedMap;
    private boolean inCutscene;
    private RSImageProducer tabAreaProducer;
    private RSImageProducer minimapProducer;
    private RSImageProducer gameScreen;
    private RSImageProducer chatAreaProducer;
    private int welcomeScreenDaysSinceRecovChange;
    private RSSocket socketStream;
    private long aLong1172;
    private boolean genericLoadingError;
    private int reportAbuseInterfaceID;
    private NodeList aClass19_1179;
    private byte[][] terrainData;
    private int anInt1184;
    private int minimapInt1;
    private int anInt1186;
    private int anInt1187;
    private int invOverlayInterfaceID;
    private int lastLoginIP;
    private int splitPrivateChat;
    private Background mapBack;
    private int savedMouseX;
    private int savedMouseY;
    private int anInt1208;
    private String promptInput;
    private int anInt1213;
    private int[][][] intGroundArray;
    private long aLong1215;
    private long aLong1220;
    private int headiconNpcID;
    private int nextSong;
    private boolean songChanging;
    private TileSetting[] tileSettings;
    private int[] mapCoordinates;
    private int[] terrainIndices;
    private int[] anIntArray1236;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] aByteArrayArray1247;
    private int tradeChatMode;
    private int anInt1249;
    private int anInt1251;
    private int anInt1253;
    boolean messagePromptRaised;
    private int anInt1257;
    private byte[][][] tileSettingBits;
    private int prevSong;
    private int destX;
    private int destY;
    private Sprite minimapImage;
    private int anInt1264;
    private int anInt1265;
    private String loginMessage1;
    private String loginMessage2;
    private RSFont[] interfaceFonts;
    private int bigRegionX;
    private int bigRegionY;
    private RSFont smallHit;
    private RSFont bigHit;
    private int backDialogID;
    private int anInt1278;
    private int[] bigX;
    private int[] bigY;
    private int itemSelected;
    private int anInt1283;
    private int anInt1284;
    private int anInt1285;
    private String selectedItemName;
    private int publicChatMode;
    private int anInt1289;
    private String tooltipHoverText = "";
    private boolean tooltipHoverActive;
    private Sprite coinOrb;
    private Sprite coinPart;
    private boolean titleScreenInitialized = false;
    private boolean coinToggle;
    private long lastCoinUpdate;
    private StringBuilder skillTabTooltip = new StringBuilder();
    private Pattern filter;
    private String inputType;
    private Sprite hueChooseSprite = new Sprite(24, 128);
    private Sprite colorPaletteSprite = new Sprite(128, 128);
    private int clickedHue = 0;
    private int clickedHueMouseY = -1;
    private int clickedColor = 0;
    private int clickedColorPaletteIndex = 0;
    private int clickedColorX = -1;
    private int clickedColorY = -1;
    private int customizingSlot = 0;

    public Client() {
        modIcons = new Sprite[7];
        fullscreenInterfaceID = -1;
        xpCounter = 0;
        chatTypeView = 0;
        clanChatMode = 0;
        cButtonHPos = -1;
        cButtonCPos = 0;
        anIntArrayArray825 = new int[104][104];
        friendsNodeIDs = new int[200];
        groundArray = new NodeList[4][104][104];
        aStream_834 = new Stream(new byte[5000]);
        npcArray = new NPC[16384];
        npcIndices = new int[16384];
        anIntArray840 = new int[1000];
        aStream_847 = Stream.create();
        waveON = true;
        openInterfaceID = -1;
        statsXp = new int[SkillConstants.skillsCount];
        anIntArray873 = new int[5];
        anInt874 = -1;
        useCustomCamera = new boolean[5];
        reportAbuseInput = "";
        playerID = -1;
        MiniMenu.open = false;
        inputString = "";
        maxPlayers = 2048;
        myPlayerIndex = 2047;
        playerArray = new Player[maxPlayers];
        playerIndices = new int[maxPlayers];
        sessionNpcsAwaitingUpdate = new int[maxPlayers];
        playerUpdateStreams = new Stream[maxPlayers];
        anIntArrayArray901 = new int[104][104];
        stats = new int[SkillConstants.skillsCount];
        ignoreListAsLongs = new long[100];
        loadingError = false;
        anIntArray928 = new int[5];
        anIntArrayArray929 = new int[104][104];
        chatButtons = new Sprite[4];
        aBoolean954 = true;
        friendsListAsLongs = new long[200];
        currentSong = -1;
        spriteDrawX = -1;
        spriteDrawY = -1;
        anIntArray968 = new int[33];
        decompressors = new Decompressor[7];
        playerVariables = new int[10000];
        aBoolean972 = false;
        anInt975 = 50;
        anIntArray976 = new int[anInt975];
        anIntArray977 = new int[anInt975];
        anIntArray978 = new int[anInt975];
        anIntArray979 = new int[anInt975];
        anIntArray980 = new int[anInt975];
        chat_effects = new int[anInt975];
        anIntArray982 = new int[anInt975];
        aStringArray983 = new String[anInt975];
        anInt985 = -1;
        hitMarks = new Sprite[20];
        hitMarks562 = new Sprite[4];
        hitMark = new Sprite[37];
        hitIcon = new Sprite[7];
        anIntArray990 = new int[5];
        amountOrNameInput = "";
        projectileNode = new NodeList();
        aBoolean1017 = false;
        currentStatusInterface = -1;
        cameraTransVars2 = new int[5];
        aBoolean1031 = false;
        mapFunctions = new Sprite[100];
        dialogID = -1;
        statsBase = new int[SkillConstants.skillsCount];
        anIntArray1045 = new int[10000];
        aBoolean1047 = true;
        minimapShape1 = new int[152];
        stillGraphicsNode = new NodeList();
        anIntArray1057 = new int[33];
        chatboxInterface = new RSInterface();
        mapScenes = new Background[100];
        barFillColor = 0x4d4233;
        anIntArray1065 = new int[7];
        markPosX = new int[1000];
        markPosY = new int[1000];
        aBoolean1080 = false;
        friendsList = new String[200];
        inStream = Stream.create();
        expectedCRCs = new int[9];
        headIcons = new Sprite[20];
        skullIcons = new Sprite[20];
        headIconsHint = new Sprite[20];
        messagePromptText = "";
        atPlayerActions = new String[5];
        atPlayerArray = new boolean[5];
        constructionMapInformation = new int[4][13][13];
        markGraphic = new int[1000];
        tutorialIsland = false;
        isDialogueInterface = false;
        crosses = new Sprite[8];
        musicEnabled = true;
        reDrawTabArea = false;
        loggedIn = false;
        canMute = false;
        loadGeneratedMap = false;
        inCutscene = false;
        myUsername = "";
        myPassword = "";
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        aClass19_1179 = new NodeList();
        anInt1184 = 1024;
        invOverlayInterfaceID = -1;
        stream = Stream.create();
        customLowestYaw = new int[5];
        songIDS = new int[50];
        chatboxMaxScroll = 78;
        promptInput = "";
        tabID = INVENTORY_TAB;
        reDrawChatArea = false;
        songChanging = true;
        minimapShape2 = new int[152];
        tileSettings = new TileSetting[4];
        songVolumes = new int[50];
        aBoolean1242 = false;
        anIntArray1250 = new int[50];
        rsAlreadyLoaded = false;
        messagePromptRaised = false;
        loginMessage1 = "";
        loginMessage2 = "";
        backDialogID = -1;
        bigX = new int[4000];
        bigY = new int[4000];
        anInt1289 = -1;
    }

    public static void setTab(int id) {
        tabID = id;
        reDrawTabArea = true;
    }

    public static void toggleSize(int size, boolean force) {
        if (clientSize != size || force) {
            clientSize = size;
            int width = REGULAR_WIDTH;
            int height = REGULAR_HEIGHT;
            if (size == 0) {
                showChat = true;
                showTab = true;
            } else if (size == 1) {
                width = 766;
                height = 560;
            } else if (size == 2) {
                width = getMaxWidth();
                height = getMaxHeight();
            }
            instance.rebuildFrame(size, width, height);

            if (size == 0) {
                for (int x = 0; x < minimapShape2.length; x++) {
                    minimapShape2[x] = 172;
                    minimapShape1[x] = -23;
                }
            } else {
                for (int i = 0; i < 76; i++) {
                    int amt = (int) (Math.sqrt(5929 - Math.pow(75 - i, 2)));
                    minimapShape2[i] = 2 * amt + 2;
                    minimapShape2[151 - i] = 2 * amt + 2;
                    minimapShape1[i] = -amt + 73;
                    minimapShape1[151 - i] = -amt + 73;
                }
            }
        }
    }

    public static Client getClient() {
        return instance;
    }

    public static int getMaxWidth() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    public static int getMaxHeight() {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    private static String intToKOrMilLongName(int i) {
        String s = String.valueOf(i);
        for (int k = s.length() - 3; k > 0; k -= 3)
            s = s.substring(0, k) + "," + s.substring(k);

        // if(j != 0)
        // aBoolean1224 = !aBoolean1224;
        if (s.length() > 8)
            s = "<col=00ff00>" + s.substring(0, s.length() - 8) + " million <col=ffffff>("
                    + s + ")";
        else if (s.length() > 4)
            s = "<col=00ffff>" + s.substring(0, s.length() - 4) + "K <col=ffffff>(" + s + ")";
        return " " + s;
    }

    public static String getFileNameWithoutExtension(String fileName) {
        File tmpFile = new File(fileName);
        tmpFile.getName();
        int whereDot = tmpFile.getName().lastIndexOf('.');
        if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
            return tmpFile.getName().substring(0, whereDot);
        }
        return "";
    }

    private static void setHighMem() {
        WorldController.lowMem = false;
        Rasterizer.lowMem = false;
        lowMem = false;
        ObjectManager.lowMem = false;
        ObjectDefinition.lowMem = false;
    }

    private static void appendDetails() {
        if (Settings.detail == Constants.DETAIL_LD) {
            setLowMem();
            disableOverlayTexture = true;
            disableUnderlayTexture = true;
        } else if (Settings.detail == Constants.DETAIL_SD) {
            setHighMem();
            disableOverlayTexture = true;
            disableUnderlayTexture = true;
        } else if (Settings.detail == Constants.DETAIL_HD) {
            setHighMem();
            disableOverlayTexture = false;
            disableUnderlayTexture = false;
        }
    }

    public static void main(String args[]) {
        try {

            //nodeID = 10;
            portOff = 0;
            isMembers = true;
            signlink.startpriv(InetAddress.getLocalHost());
            instance = new Client();
            instance.startApplication(REGULAR_WIDTH, REGULAR_HEIGHT);

            Settings.load();
            appendDetails();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static String combatDiffColor(int i, int j) {
        int k = i - j;
        if (k < -9)
            return "<col=ff0000>";
        if (k < -6)
            return "<col=ff3000>";
        if (k < -3)
            return "<col=ff7000>";
        if (k < 0)
            return "<col=ffb000>";
        if (k > 9)
            return "<col=00ff00>";
        if (k > 6)
            return "<col=40ff00>";
        if (k > 3)
            return "<col=80ff00>";
        if (k > 0)
            return "<col=c0ff00>";

        return "<col=ffff00>";
    }

    private static void setLowMem() {
        WorldController.lowMem = false;
        Rasterizer.lowMem = true;
        lowMem = true;
        ObjectManager.lowMem = true;
        ObjectDefinition.lowMem = true;
    }

    public static String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String getUUID() {
        File file = new File(System.getProperty("user.home") + "/systemid.dat");
        if (!file.exists() || file.length() < 4L) {
            try (FileOutputStream fout = new FileOutputStream(file)) {
                fout.write(UUID.randomUUID().toString().getBytes());
            } catch (IOException e) {
                // ignore exception
            }
        }

        try (FileInputStream fin = new FileInputStream(file)) {

            byte[] buffer = new byte[36];

            fin.read(buffer);

            return new String(buffer);
        } catch (IOException e) {
            // ignore exception
        }

        return "";
    }

    private static void loadGoals(String name) {
        File file = new File(signlink.findcachedir() + "data/goals/" + name.trim().toLowerCase() + "-goals.dat");
        if (!file.exists()) {
            for (int index = 0; index < SkillConstants.goalData.length; index++) {
                SkillConstants.goalData[index][0] = -1;
                SkillConstants.goalData[index][1] = -1;
                SkillConstants.goalData[index][2] = -1;
            }
            return;
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            for (int index = 0; index < SkillConstants.goalData.length; index++) {
                SkillConstants.goalData[index][0] = in.readInt();
                SkillConstants.goalData[index][1] = in.readInt();
                SkillConstants.goalData[index][2] = in.readInt();
                SkillConstants.goalType = in.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatGeTotalPrice(int price) {
        return numberFormat.format(price) + " gp";
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interruptedexception) {
            /* empty */
        }
    }

    public static void sleepWrapper(long millis) {
        if (millis > 0L) {
            if (millis % 10L == 0L) {
                sleep(millis - 1L);
                sleep(1L);
            } else {
                sleep(millis);
            }
        }
    }

    public static void writePackFile(String name, byte[] data, int off, int pos) {
        try (FileOutputStream outputStream = new FileOutputStream("C:/Users/Kompas/Desktop/maps/" + name + ".new")) {
            outputStream.write(data, 0, pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String formatObjCount(int count) {
        if (count < 100000) {
            return "<col=ffff00>" + count + "</col>";
        }
        if (count < 10000000) {
            return "<col=ffffff>" + (count / 1000) + "K</col>";
        }
        return "<col=00ff80>" + (count / 1000000) + "M</col>";
    }

    public void setNorth() {
        anInt1278 = 0;
        anInt1131 = 0;
        minimapInt1 = 0;
    }

    private void drawXPCounter(boolean isFixed) {
        cacheSprite[92].drawSprite(isFixed ? 1 : frameWidth - 210, isFixed ? 48 : 4);
        if (hoverPos == 0) {
            cacheSprite[93].drawSprite(isFixed ? 1 : frameWidth - 210, isFixed ? 48 : 4);
        }
    }

    private int findFreeXpIndex() {
        for (int i = 0; i < 30; i++) {
            ExpDrop drop = expDrops[i];
            if (drop == null) {
                return i;
            }
        }
        return -1;
    }

    private void drawCounterOnScreen(boolean isFixed) {
        int x2 = isFixed ? 403 : frameWidth - 320;
        cacheSprite[94].drawSprite(x2 + 5, (isFixed ? 53 : 12));
        if (xpCounter >= 214_748_364) {
            small.drawBasicRightAlignedString("Lots!", x2 + 90, (isFixed ? 65 : 25), 0xFF0000, 0);
        } else {
            small.drawBasicRightAlignedString(numberFormat.format(xpCounter), x2 + 95,  (isFixed ? 65 : 25), 0xFFFFFD, 0);
        }
        small.drawBasicStringCentered("XP:", x2 + 16, (isFixed ? 66 : 25), 0xFFFFFD, 0);
        for (int i = 0; i < 30; i++) {
            ExpDrop drop = expDrops[i];
            if (drop != null) {
                drop.y--;
                if (drop.y <= (isFixed ? -5 : 20)) {
                    expDrops[i] = null;
                    continue;
                }
                cacheSprite[drop.spriteId].drawSprite(x2 + 10, (isFixed ? 50 : 10) + drop.y);
                small.drawBasicRightAlignedString(numberFormat.format(drop.xp) + "xp", x2 + 100, (isFixed ? 65 : 25) + drop.y, 0xff9933, 0);
            }
        }
    }

    private void addTabOptions(boolean isFixed) {
        if (isFixed) {
        	int oldTabHover = tabHover;

            int positionX = frameWidth - 244;
            int positionY = 169, positionY2 = frameHeight - 36;
            if (mouseInRegion(frameWidth - 21, 0, frameWidth, 20)) {
                tabHover = LOGOUT_TAB;
            } else if (mouseInRegion(positionX, positionY, positionX + 30,
                    positionY + 36)) {
                tabHover = COMBAT_TAB;
            } else if (mouseInRegion(positionX + 30, positionY, positionX + 60,
                    positionY + 36)) {
                tabHover = ACHIEVEMENTS_TAB;
            } else if (mouseInRegion(positionX + 60, positionY, positionX + 90,
                    positionY + 36)) {
                tabHover = STATS_TAB;
            } else if (mouseInRegion(positionX + 90, positionY,
                    positionX + 120, positionY + 36)) {
                tabHover = QUEST_TAB;
            } else if (mouseInRegion(positionX + 120, positionY,
                    positionX + 150, positionY + 36)) {
                tabHover = INVENTORY_TAB;
            } else if (mouseInRegion(positionX + 150, positionY,
                    positionX + 180, positionY + 36)) {
                tabHover = EQUIPMENT_TAB;
            } else if (mouseInRegion(positionX + 180, positionY,
                    positionX + 210, positionY + 36)) {
                tabHover = PRAYER_TAB;
            } else if (mouseInRegion(positionX + 210, positionY,
                    positionX + 240, positionY + 36)) {
                tabHover = MAGIC_TAB;
            } else if (mouseInRegion(positionX, positionY2, positionX + 30,
                    positionY2 + 36)) {
                tabHover = SUMMONING_TAB;
            } else if (mouseInRegion(positionX + 30, positionY2,
                    positionX + 60, positionY2 + 36)) {
                tabHover = FRIENDS_TAB;
            } else if (mouseInRegion(positionX + 60, positionY2,
                    positionX + 90, positionY2 + 36)) {
                tabHover = IGNORES_TAB;
            } else if (mouseInRegion(positionX + 90, positionY2,
                    positionX + 120, positionY2 + 36)) {
                tabHover = CLAN_CHAT_TAB;
            } else if (mouseInRegion(positionX + 120, positionY2,
                    positionX + 150, positionY2 + 36)) {
                tabHover = SETTINGS_TAB;
            } else if (mouseInRegion(positionX + 150, positionY2,
                    positionX + 180, positionY2 + 36)) {
                tabHover = EMOTES_TAB;
            } else if (mouseInRegion(positionX + 180, positionY2,
                    positionX + 210, positionY2 + 36)) {
                tabHover = MUSIC_TAB;
            } else if (mouseInRegion(positionX + 210, positionY2,
                    positionX + 240, positionY2 + 36)) {
                tabHover = INFORMATION_TAB;
            } else {
                tabHover = -1;
            }

            if (oldTabHover != tabHover) {
                reDrawTabArea = true;
            }
        } else {
            int offsetX = (frameWidth >= smallTabs ? frameWidth - 480
                    : frameWidth - 240);
            int positionY = (frameWidth >= smallTabs ? frameHeight - 37
                    : frameHeight - 74);
            int secondPositionY = frameHeight - 37;
            int secondOffsetX = frameWidth >= smallTabs ? 240 : 0;
            if (mouseInRegion(frameWidth - 21, 0, frameWidth, 21)) {
                tabHover = 10;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[0] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[0] + offsetX + 30, positionY + 37)) {
                tabHover = COMBAT_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[1] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[1] + offsetX + 30, positionY + 37)) {
                tabHover = ACHIEVEMENTS_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[2] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[2] + offsetX + 30, positionY + 37)) {
                tabHover = STATS_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[3] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[3] + offsetX + 30, positionY + 37)) {
                tabHover = QUEST_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[4] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[4] + offsetX + 30, positionY + 37)) {
                tabHover = INVENTORY_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[5] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[5] + offsetX + 30, positionY + 37)) {
                tabHover = EQUIPMENT_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[6] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[6] + offsetX + 30, positionY + 37)) {
                tabHover = PRAYER_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[7] + offsetX, positionY,
                    RESIZABLE_TAB_POS_X[7] + offsetX + 30, positionY + 37)) {
                tabHover = MAGIC_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[8] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[8] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = SUMMONING_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[9] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[9] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = FRIENDS_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[10] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[10] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = IGNORES_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[11] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[11] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = CLAN_CHAT_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[12] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[12] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = SETTINGS_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[13] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[13] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = EMOTES_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[14] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[14] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = MUSIC_TAB;
            } else if (mouseInRegion(RESIZABLE_TAB_POS_X[15] + offsetX + secondOffsetX,
                    secondPositionY, RESIZABLE_TAB_POS_X[15] + offsetX + secondOffsetX
                            + 30, secondPositionY + 37
            )) {
                tabHover = INFORMATION_TAB;
            } else {
                tabHover = -1;
            }
        }
        if (tabHover != -1) {
            processTabAreaTooltips(tabHover);
        }
    }

    private void connectServer() {
        int j = 5;
        expectedCRCs[8] = 0;
        int k = 0;
        while (expectedCRCs[8] == 0) {
            String s = "Unknown problem";
            drawLoadingText(20, "Connecting to web server");
            try {
                DataInputStream datainputstream = openJagGrabInputStream("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
                Stream class30_sub2_sub2 = new Stream(new byte[40]);
                datainputstream.readFully(class30_sub2_sub2.buffer, 0, 40);
                datainputstream.close();
                datainputstream = null;
                for (int i1 = 0; i1 < 9; i1++)
                    expectedCRCs[i1] = class30_sub2_sub2.readDWord();

                int j1 = class30_sub2_sub2.readDWord();
                int k1 = 1234;
                for (int l1 = 0; l1 < 9; l1++)
                    k1 = (k1 << 1) + expectedCRCs[l1];

                if (j1 != k1) {
                    s = "checksum problem";
                    expectedCRCs[8] = 0;
                }
                class30_sub2_sub2 = null;
            } catch (EOFException _ex) {
                s = "EOF problem";
                expectedCRCs[8] = 0;
            } catch (IOException _ex) {
                _ex.printStackTrace();
                s = "connection problem";
                expectedCRCs[8] = 0;
            } catch (Exception _ex) {
                s = "logic problem";
                expectedCRCs[8] = 0;
            }
            if (expectedCRCs[8] == 0) {
                k++;
                for (int l = j; l > 0; l--) {
                    if (k >= 10) {
                        drawLoadingText(10, "Game updated - please reload page");
                        l = 10;
                    } else {
                        drawLoadingText(10, s + " - Will retry in " + l
                                + " secs.");
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }

                j *= 2;
                if (j > 60)
                    j = 60;
            }
        }
    }

    private void processTabAreaTooltips(int tabId) {
    	MiniMenu.addOption(TAB_AREA_TOOLTIPS[tabId], 1076, tabId, 0, 0);
    }

    private void addMinimapOptions(boolean isFixed) {
        if (super.mouseX >= frameWidth - (isFixed ? 242 : 164) && super.mouseX <= frameWidth - (isFixed ? 203 : 125) && super.mouseY > (isFixed ? 6 : 0) && super.mouseY < (isFixed ? 42 : 37)) {
        	MiniMenu.addOption("Face north", 1014);
        }

        if (super.mouseX >= frameWidth - (isFixed ? 253 : 68) && super.mouseX <= frameWidth - (isFixed ? 219 : 40) && super.mouseY >= (isFixed ? 87 : 160) && super.mouseY <= (isFixed ? 118 : 187)) {
        	MiniMenu.addOption("Withdraw money pouch", 713);
        	MiniMenu.addOption("Toggle money pouch", 712);
        }

        worldMapHPos = 0;
        if (super.mouseX >= frameWidth - (isFixed ? 238 : 41) && super.mouseX <= frameWidth - (isFixed ? 206 : 8) && super.mouseY >= (isFixed ? 124 : 124) && super.mouseY <= (isFixed ? 156 : 156)) {
        	MiniMenu.addOption("View Teleports", 476);
        	worldMapHPos = 1;
        }

        if (super.mouseX >= frameWidth - (isFixed ? 249 : 210) && super.mouseX <= frameWidth - (isFixed ? 215 : 176) && super.mouseY > (isFixed ? 46 : 3) && super.mouseY < (isFixed ? 80 : 38)) {
        	MiniMenu.addOption("Reset XP Total", 475);
        	MiniMenu.addOption("Toggle XP Display", 474);
        }

        if (mouseInRegion(isFixed ? frameWidth - 58 : getOrbX(2, isFixed), getOrbY(2, isFixed), (isFixed ? frameWidth - 58 : getOrbX(2, isFixed)) + 57, getOrbY(2, isFixed) + 34)) {
        	MiniMenu.addOption("Rest", 1052);
        	MiniMenu.addOption(playerVariables[173] == 1 ? "Toggle run mode on" : "Toggle run mode off", 1051);
        }

        if (mouseInRegion(isFixed ? frameWidth - 58 : getOrbX(1, isFixed), getOrbY(1, isFixed), (isFixed ? frameWidth - 58 : getOrbX(1, isFixed)) + 57, getOrbY(1, isFixed) + 34)) {
        	String prayers = tabInterfaceIDs[5] == 22500 ? "curses" : "prayers";
         	MiniMenu.addOption("Select quick-", prayers, 1505);
        	MiniMenu.addOption(QuickPrayers.quickPrayersActive ? "Turn quick-" + prayers + " off" : "Turn quick-" + prayers + " on", 1500);
        }

        if (mouseInRegion(isFixed ? frameWidth - 74 : getOrbX(3, isFixed), getOrbY(3, isFixed), (isFixed ? frameWidth - 74 : getOrbX(3, isFixed)) + 57, getOrbY(3, isFixed) + 34)) {
        	MiniMenu.addOption(RSInterface.interfaceCache[42567].message, 1506);
        	MiniMenu.addOption("Take BoB", 1507);
            MiniMenu.addOption("Attack", 1508);
            MiniMenu.addOption("Interact", 1509);
            MiniMenu.addOption("Call familiar", 1510);
            MiniMenu.addOption("Renew familiar", 1511);
            MiniMenu.addOption("Dismiss familiar", 1512);
            MiniMenu.addOption("Follower details", 1513);
        }
    }

    public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
        return Math.pow((circleX + radius - clickX), 2) + Math.pow((circleY + radius - clickY), 2) < Math.pow(radius, 2);
    }

    public int getOrbX(int orb, boolean isFixed) {
        switch (orb) {
            case 0:
                return !isFixed ? frameWidth - 207 : 172;
            case 1:
                return !isFixed ? frameWidth - 210 : 188;
            case 2:
                return !isFixed ? frameWidth - 198 : 188;
            case 3:
                return !isFixed ? frameWidth - 176 : 172;
        }
        return 0;
    }

    public int getOrbY(int orb, boolean isFixed) {
        switch (orb) {
            case 0:
                return !isFixed ? 40 : 15;
            case 1:
                return !isFixed ? 75 : 54;
            case 2:
                return !isFixed ? 109 : 93;
            case 3:
                return !isFixed ? 138 : 128;
        }
        return 0;
    }

    public void rebuildFrame(int size, int width, int height) {
        try {
            gameAreaWidth = size == 0 ? 512 : width;
            gameAreaHeight = size == 0 ? 334 : height;
            frameWidth = width;
            frameHeight = height;
            instance.rebuildFrame(clientSize == 2, width, height, clientSize == 1, clientSize >= 1);
            updateCanvas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawUnfixedGame() {
    	drawChatArea(false);
    	drawTabArea(false);
    	drawMinimap(false);
    }

    private void updateCanvas() {
        try {
            canvas.setSize(frameWidth, frameHeight);
            canvas.setLocation(insets.left, insets.top);
            if (loggedIn) {
                initGameProducers(true);
            } else {
                fullGameScreen = new RSImageProducer(frameWidth, frameHeight, canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkSize() {
        if (clientSize == 1) {
            Container container;
            if (frame != null) {
                container = frame;
            } else {
                container = this;
            }
            int newWidth = container.getWidth();
            int newHeight = container.getHeight();
            if (container == frame) {
                newWidth -= insets.left + insets.right;
                newHeight -= insets.bottom + insets.top;
            }
            if (frameWidth != newWidth || frameHeight != newHeight) {
                frameWidth = newWidth;
                gameAreaWidth = frameWidth;
                frameHeight = newHeight;
                gameAreaHeight = frameHeight;
                updateCanvas();
            }
        }
    }

    public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
        return super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2;
    }

    public boolean clickInRegion(int x1, int y1, int x2, int y2) {
        return super.saveClickX >= x1 && super.saveClickX <= x2 && super.saveClickY >= y1 && super.saveClickY <= y2;
    }

    public void drawTabOverlay(boolean isFixed) {
        if (isFixed) {
            cacheSprite[13].drawSprite(0, 0);//Draw the tab sprite
        } else {
            if (frameWidth >= smallTabs) {//Expanded tabs
            	//Draw the tab row
                for (int positionX = frameWidth - 480, positionY = frameHeight - 37, index = 0; positionX <= frameWidth - 30 && index < 16; positionX += 30, index++) {
                    int spriteId = 15;
                    if (invOverlayInterfaceID == -1 && tabHover != -1 && tabHover == FIXED_SIDE_ICON_TAB_ID[index]) {
                        spriteId = 16;
                    }
                    cacheSprite[spriteId].drawSprite(positionX, positionY);
                }
                if (showTab) {
                    cacheSprite[18].drawTransparentSprite(frameWidth - 197, frameHeight - 37 - 267, 150);//Draw background
                    cacheSprite[19].drawSprite(frameWidth - 204, frameHeight - 37 - 274);//Draw frame
                }
            } else {//Collapsed tabs
                int tabId = 0;
                //Draw first row of the tabs
                for (int positionX = frameWidth - 240, positionY = frameHeight - 74, index = 0; positionX <= frameWidth - 30 && index < 8; positionX += 30, index++) {
                    int spriteId = 15;
                    if (invOverlayInterfaceID == -1 && tabHover != -1 && tabHover == FIXED_SIDE_ICON_TAB_ID[tabId]) {
                        spriteId = 16;
                    }
                    cacheSprite[spriteId].drawSprite(positionX, positionY);
                    tabId++;
                }
                //Draw secon row of the tabs
                for (int positionX = frameWidth - 240, positionY = frameHeight - 37, index = 0; positionX <= frameWidth - 30 && index < 8; positionX += 30, index++) {
                    int spriteId = 15;
                    if (invOverlayInterfaceID == -1 && tabHover != -1 && tabHover == FIXED_SIDE_ICON_TAB_ID[tabId]) {
                        spriteId = 16;
                    }
                    cacheSprite[spriteId].drawSprite(positionX, positionY);
                    tabId++;
                }
                if (showTab) {
                    cacheSprite[18].drawTransparentSprite(frameWidth - 197, frameHeight - 74 - 267, 150);//Draw background
                    cacheSprite[19].drawSprite(frameWidth - 204, frameHeight - 74 - 274);//Draw frame
                }
            }
        }
        if (invOverlayInterfaceID == -1) {
            drawSideIcons(isFixed);
        }
    }

    /**
     * Index Repacking Methods
     */

    public String indexLocation(int cacheIndex, int index) {
        return signlink.findcachedir() + "/index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
    }

    public void repackCacheIndex(int cacheIndex) {
        System.out.println("Started repacking index " + cacheIndex + ".");

        System.out.println(indexLocation(cacheIndex, -1));

        int indexLength = new File(indexLocation(cacheIndex, -1)).listFiles().length;
        File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
        try {
            for (int index = 0; index < indexLength; index++) {

                if (file[index].getName().equalsIgnoreCase(".DS_Store")) {
                    continue;
                }

                int fileIndex = Integer.parseInt(getFileNameWithoutExtension(file[index].toString()));
                byte[] data = fileToByteArray(cacheIndex, fileIndex);
                if (data != null && data.length > 0) {
                    decompressors[cacheIndex].pack(data.length, data, fileIndex);
                    System.out.println("Repacked " + fileIndex + ".");
                } else {
                    //   System.out.println("Unable to locate index " + fileIndex + ".");
                }
            }
        } catch (Exception e) {
            System.out.println("Error packing cache index " + cacheIndex + ". ");
            e.printStackTrace();
        }
        System.out.println("Finished repacking " + cacheIndex + ".");
    }

    public byte[] fileToByteArray(int cacheIndex, int index) {
        try {
            if (indexLocation(cacheIndex, index).length() <= 0
                    || indexLocation(cacheIndex, index) == null) {
                return null;
            }
            File file = new File(indexLocation(cacheIndex, index));
            byte[] fileData = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileData);
            fis.close();
            return fileData;
        } catch (Exception e) {
            return null;
        }
    }

    private void stopMidi() {
    	/* empty */
    }

    private boolean menuHasAddFriend() {
    	MiniMenuOption miniMenuOption = (MiniMenuOption) MiniMenu.options.head.next;
    	if (miniMenuOption == null || MiniMenu.options.head == miniMenuOption) {
    		return false;
    	}
        if (miniMenuOption.uid >= 2000) {
        	miniMenuOption.uid -= 2000;
        }
        return miniMenuOption.uid == 337;
    }

    private void drawChannelButtons(int xPosOffset, int yPosOffset) {
        cacheSprite[5].drawSprite(5 + xPosOffset, 142 + yPosOffset);
        if (showChat)
            chatButtons[1].drawSprite(CHAT_BUTTONS_POS_X[cButtonCPos] + xPosOffset, yPosOffset + 142);
        if (cButtonHPos == cButtonCPos && showChat) {
        	int sprite = cButtonHPos == 7 ? 3 : 2;
        	chatButtons[sprite].drawSprite(CHAT_BUTTONS_POS_X[cButtonHPos] + xPosOffset, yPosOffset + 142);
        } else if (cButtonHPos != -1) {
        	int sprite = cButtonHPos == 7 ? 3 : 0;
        	chatButtons[sprite].drawSprite(CHAT_BUTTONS_POS_X[cButtonHPos] + xPosOffset, yPosOffset + 142);
        }
        small.drawCenteredString("All", CHAT_BUTTONS_POS_X[0] + 28 + xPosOffset, 157 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Game", CHAT_BUTTONS_POS_X[1] + 28 + xPosOffset, 157 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Public", CHAT_BUTTONS_POS_X[2] + 28 + xPosOffset, 152 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Private", CHAT_BUTTONS_POS_X[3] + 28 + xPosOffset, 152 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Clan", CHAT_BUTTONS_POS_X[4] + 28 + xPosOffset, 152 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Trade", CHAT_BUTTONS_POS_X[5] + 28 + xPosOffset, 152 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Challenge", CHAT_BUTTONS_POS_X[6] + 28 + xPosOffset, 152 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString("Report Abuse", CHAT_BUTTONS_POS_X[7] + 55 + xPosOffset, 157 + yPosOffset, 0xffffff, 0);
        small.drawCenteredString(CHAT_BUTTONS_OPS_TEXTS[publicChatMode], CHAT_BUTTONS_POS_X[2] + 28 + xPosOffset, 163 + yPosOffset, CHAT_BUTTONS_TEXT_COLORS[publicChatMode], 0);
        small.drawCenteredString(CHAT_BUTTONS_OPS_TEXTS[privateChatMode], CHAT_BUTTONS_POS_X[3] + 28 + xPosOffset, 163 + yPosOffset, CHAT_BUTTONS_TEXT_COLORS[privateChatMode], 0);
        small.drawCenteredString(CHAT_BUTTONS_OPS_TEXTS[clanChatMode], CHAT_BUTTONS_POS_X[4] + 28 + xPosOffset, 163 + yPosOffset, CHAT_BUTTONS_TEXT_COLORS[clanChatMode], 0);
        small.drawCenteredString(CHAT_BUTTONS_OPS_TEXTS[tradeChatMode], CHAT_BUTTONS_POS_X[5] + 28 + xPosOffset, 163 + yPosOffset, CHAT_BUTTONS_TEXT_COLORS[tradeChatMode], 0);
        small.drawCenteredString(CHAT_BUTTONS_OPS_TEXTS[challengeChatMode], CHAT_BUTTONS_POS_X[6] + 28 + xPosOffset, 163 + yPosOffset, CHAT_BUTTONS_TEXT_COLORS[challengeChatMode], 0);
    }

    public void drawBlackBox(int xPos, int yPos) {
        DrawingArea.fillRectAlpha(xPos - 2, yPos - 2, 178, 72, 0, 220);
        DrawingArea.drawRect(xPos - 1, yPos - 1, 177, 71, 0x2E2B23);
        DrawingArea.drawRect(xPos - 2, yPos - 2, 177, 71, 0x726451);
    }

    public void drawChatArea(boolean isFixed) {
        int offsetY = isFixed ? 0 : frameHeight - 165;
        if (isFixed) {
            chatAreaProducer.initDrawingArea();
            cacheSprite[0].drawSprite(0, offsetY);
        } else if (showChat) {
        	cacheSprite[52].drawAdvancedSprite(7, offsetY + 7);
        }
        drawChannelButtons(0, offsetY);
        if (messagePromptRaised) {
            cacheSprite[38].drawSprite(0, offsetY);
            regular.drawCenteredString(messagePromptText, 259, 60 + offsetY, 0, -1);
            regular.drawBasicStringCentered(promptInput + "*", 259, 80 + offsetY, 128, -1);
        } else if (inputDialogState == 1) {
            cacheSprite[38].drawSprite(0, offsetY);
            regular.drawCenteredString("Enter amount:", 259, 60 + offsetY, 0, -1);
            regular.drawBasicStringCentered(amountOrNameInput + "*", 259, 80 + offsetY, 128, -1);
        } else if (inputDialogState == 2) {
            cacheSprite[38].drawSprite(0, offsetY);
            regular.drawCenteredString("Enter name:", 259, 60 + offsetY, 0, -1);
            regular.drawBasicStringCentered(amountOrNameInput + "*", 259, 80 + offsetY, 128, -1);
        } else if (inputDialogState == 3) {
            cacheSprite[38].drawSprite(0, offsetY);
            displayItemSearch(isFixed);
        } else if (inputDialogState == 4) {
            cacheSprite[38].drawSprite(0, offsetY);
            regular.drawCenteredString("Enter amount to withdraw from money pouch:", 259, 60 + offsetY, 0, -1);
            regular.drawBasicStringCentered(amountOrNameInput + "*", 259, 80 + offsetY, 128, -1);
        } else if (aString844 != null) {
            cacheSprite[38].drawSprite(0, offsetY);
            regular.drawCenteredString(aString844, 259, 60 + offsetY, 0, -1);
            regular.drawCenteredString("Click to continue", 259, 80 + offsetY, 128, -1);
        } else if (backDialogID != -1) {
            cacheSprite[38].drawSprite(0, offsetY);
            Rasterizer.setDefaultBounds();
            drawInterface(0, 20, RSInterface.interfaceCache[backDialogID], 20 + offsetY, true);
        } else if (dialogID != -1) {
            cacheSprite[38].drawSprite(0, offsetY);
            Rasterizer.setDefaultBounds();
            drawInterface(0, 20, RSInterface.interfaceCache[dialogID], 20 + offsetY, true);
        } else if (showChat) {
            int messageY = 0;
            DrawingArea.setDrawingArea(8, 7 + offsetY, 497, 121 + offsetY);
			for (int index = ChatMessageManager.getHighestId(); index != -1; index = ChatMessageManager.getPrevMessageId(index)) {
				ChatMessage message = (ChatMessage) ChatMessageManager.messageHashtable.findNodeByID(index);
				String chatMessage = message.message;
				int chatType = message.type;
				int positionY = (111 - messageY * 14) + chatboxScrollPos + 6;
				String name = message.name;
				String title = message.title;

				if (chatType == ChatMessage.GAME_MESSAGE && (chatTypeView == 5 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(isFixed ? 0 : 0xffffff, isFixed ? -1 : 0);
						int xPos = 11;
						regular.drawString(chatMessage, xPos, positionY + offsetY);
					}
					messageY++;
				}
				if ((chatType == ChatMessage.PUBLIC_MESSAGE_STAFF || chatType == ChatMessage.PUBLIC_MESSAGE_PLAYER) && (chatType == ChatMessage.PUBLIC_MESSAGE_STAFF || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(name)) && (chatTypeView == 1 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(isFixed ? 0 : 0xffffff, isFixed ? -1 : 0);
						int xPos = 11;
						if (title != null && !title.isEmpty()) {
							regular.drawString(title, xPos, positionY + offsetY);
							xPos += regular.getTextWidth(title) + 3;
						}
						regular.drawString(name, xPos, positionY + offsetY);
						xPos += regular.getTextWidth(name);
						regular.drawBasicString(":", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(":") + 5;
						regular.drawBasicString(chatMessage, xPos - 2, positionY + offsetY, isFixed ? 250 : 0x7FA9FF, isFixed ? -1 : 0);
					}
					messageY++;
				}
				if ((chatType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER || chatType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF) && (splitPrivateChat == 0 || chatTypeView == 2) && (chatType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name)) && (chatTypeView == 2 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(isFixed ? 0 : 0xffffff, isFixed ? -1 : 0);
						int xPos = 11;
						regular.drawBasicString("From ", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth("From ");
						if (title != null && !title.isEmpty()) {
							regular.drawString(title, xPos, positionY + offsetY);
							xPos += regular.getTextWidth(title);
						}
						regular.drawString(name, xPos, positionY + offsetY);
						xPos += regular.getTextWidth(name);
						regular.drawBasicString(":", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(":") + 5;
						regular.drawBasicString(chatMessage, xPos, positionY + offsetY, isFixed ? 0x800000 : 0xFF5256, isFixed ? -1 : 0);
					}
					messageY++;
				}
				if (chatType == ChatMessage.TRADE_REQUEST && (tradeChatMode == 0 || tradeChatMode == 1 && isFriendOrSelf(name)) && (chatTypeView == 3 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(isFixed ? 0x800080 : 0xFF00D4, isFixed ? -1 : 0);
						int xPos = 11;
						regular.drawBasicString(name, xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(name) + 3;
						regular.drawBasicString(chatMessage, xPos, positionY + offsetY);
					}
					messageY++;
				}
				if (chatType == ChatMessage.PRIVATE_MESSAGE_RECEIVED && (splitPrivateChat == 0 || chatTypeView == 2) && privateChatMode < 2 && (chatTypeView == 2 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.drawBasicString(chatMessage, 11, positionY + offsetY, isFixed ? 0x800000 : 0xFF5256, isFixed ? -1 : 0);
					}
					messageY++;
				}
				if (chatType == ChatMessage.PRIVATE_MESSAGE_SENT && (splitPrivateChat == 0 || chatTypeView == 2) && privateChatMode < 2 && (chatTypeView == 2 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(isFixed ? 0 : 0xffffff, isFixed ? -1 : 0);
						int xPos = 11;
						regular.drawBasicString("To ", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth("To ");
						regular.drawBasicString(name, xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(name);
						regular.drawBasicString(":", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(":") + 5;
						regular.drawBasicString(chatMessage, xPos, positionY + offsetY, isFixed ? 0x800000 : 0xFF5256, isFixed ? -1 : 0);
					}
					messageY++;
				}
				if (chatType == ChatMessage.CHALLENGE_REQUEST && (challengeChatMode == 0 || challengeChatMode == 1 && isFriendOrSelf(name)) && (chatTypeView == 6 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						regular.setColorAndShadow(0x7e3200, -1);
						int xPos = 11;
						regular.drawBasicString(name, xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(name) + 3;
						regular.drawBasicString(chatMessage, xPos, positionY + offsetY);
					}
					messageY++;
				}
				if (chatType == ChatMessage.CLAN_MESSAGE && (clanChatMode == 0 || clanChatMode == 1 && isFriendOrSelf(name)) && (chatTypeView == 11 || chatTypeView == 0)) {
					if (positionY > 3 && positionY < 130) {
						int xPos = 11;
						int shade = isFixed ? -1 : 0;
						int color = isFixed ? 0 : 0xffffff;
						if (title != null && !title.isEmpty()) {
							regular.drawBasicString("[", xPos, positionY + offsetY, color, shade);
							xPos += regular.getBasicWidth("[");
							regular.drawBasicString(title, xPos, positionY + offsetY, 0xff, shade);
							xPos += regular.getTextWidth(title);
							regular.drawBasicString("]", xPos, positionY + offsetY, color, shade);
							xPos += regular.getBasicWidth("]") + 4;
						}
						regular.setColorAndShadow(color, shade);
						regular.drawString(name, xPos, positionY + offsetY);
						xPos += regular.getTextWidth(name);
						regular.drawBasicString(":", xPos, positionY + offsetY);
						xPos += regular.getBasicWidth(":") + 4;
						regular.drawBasicString(chatMessage, xPos, positionY + offsetY, isFixed ? 0x800000 : 0xFF5256, shade);
					}
					messageY++;
				}
			}
            DrawingArea.defaultDrawingAreaSize();
            chatboxMaxScroll = messageY * 14 + 7 + 5;
            if (chatboxMaxScroll < 111) {
                chatboxMaxScroll = 111;
            }
            drawScrollbar(114, chatboxMaxScroll - chatboxScrollPos - 113, 7 + offsetY, 496, chatboxMaxScroll, false, !isFixed);
            String name;
            String title = null;
            if (myPlayer != null && myPlayer.name != null) {
                name = myPlayer.name;
                title = myPlayer.titleText;
            } else {
                name = TextClass.fixName(myUsername);
            }
            DrawingArea.setDrawingArea(8, 120 + offsetY, 509, 140 + offsetY);
            regular.setColorAndShadow(isFixed ? 0 : 0xffffff, isFixed ? -1 : 0);
            int xPos = 11;
            if (title != null && !title.isEmpty()) {
                regular.drawString(title, xPos, 133 + offsetY);
                xPos += regular.getTextWidth(title) + 3;
            }
            regular.drawString(name, xPos, 133 + offsetY);
            xPos += regular.getTextWidth(name);
            cacheSprite[14].drawSprite(xPos, 123 + offsetY);
            xPos += cacheSprite[14].myWidth;
            regular.drawBasicString(": ", xPos, 133 + offsetY);
            xPos += regular.getBasicWidth(": ");
            regular.setColorAndShadow(isFixed ? 255 : 0x7FA9FF, isFixed ? -1 : 0);
            regular.drawBasicString(inputString, xPos, 133 + offsetY);
            xPos += regular.getBasicWidth(inputString);
            regular.drawBasicString("*", xPos, 133 + offsetY);
            DrawingArea.defaultDrawingAreaSize();
            if (isFixed)
                DrawingArea.drawHorizontalLine(7, 121 + offsetY, 505, isFixed ? 0x807660 : 0xffffff);
        }
        if (isFixed) {
            if (MiniMenu.open) {
                drawMenu(0, 338);
            } else {
                drawTooltipHover(0, 338);
            }
            chatAreaProducer.drawGraphics(super.getGraphics(), 0, 338);
        }
    }

    @Override
    public void init() {
        try {
            nodeID = 10;
            portOff = 0;
            setHighMem();
            isMembers = true;
            signlink.startpriv(InetAddress.getLocalHost());
            startApplet(REGULAR_WIDTH, REGULAR_HEIGHT);
            instance = this;
            appendDetails();
        } catch (Exception exception) {
            return;
        }
    }

    @Override
    public void startRunnable(Runnable runnable, int i) {
        if (i > 10)
            i = 10;
        super.startRunnable(runnable, i);
    }

    Socket openSocket(int port) throws IOException {
		System.out.println("Constants.SERVER_IP: " + Constants.SERVER_IP+" - "+port);
		return new Socket(Constants.SERVER_IP, port);
    }

    private boolean processMenuClick() {
        if (activeInterfaceType != 0) {
            return false;
        }

        int j = super.clickMode3;

        if (MiniMenu.open) {
            if (j != 1) {
                int k = super.mouseX;
                int j1 = super.mouseY;
                if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10
                        || j1 < menuOffsetY - 10
                        || j1 > menuOffsetY + menuHeight + 10) {
                    MiniMenu.open = false;
                    reDrawTabArea = true;
                    reDrawChatArea = true;
                }
            }
            if (j == 1) {
                int l = menuOffsetX;
                int k1 = menuOffsetY;
                int i2 = menuWidth;
                int k2 = super.saveClickX;
                int l2 = super.saveClickY;
                MiniMenuOption i3 = null;
                int j3 = 0;
                for (MiniMenuOption menuOption = (MiniMenuOption) MiniMenu.options.reverseGetFirst(); menuOption != null; menuOption = (MiniMenuOption) MiniMenu.options.reverseGetNext()) {
                    int k3 = k1 + 31 + (MiniMenu.optionCount - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3) {
                        i3 = menuOption;
                    }
                    j3++;
                }
                if (i3 != null) {
                    doAction(i3);
                }
                MiniMenu.open = false;
                reDrawTabArea = true;
                reDrawChatArea = true;
            }
            return true;
        } else {
            if (j == 1 && MiniMenu.optionCount > 0) {
            	MiniMenuOption miniMenuOption = (MiniMenuOption) MiniMenu.options.head.next;
                int i1 = miniMenuOption.uid;
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53
                        || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493
                        || i1 == 847 || i1 == 447 || i1 == 1125) {
                    int l1 = miniMenuOption.cmd2;
                    int j2 = miniMenuOption.cmd3;
                    RSInterface rsi = RSInterface.interfaceCache[j2];
                    if (rsi.aBoolean259 || rsi.itemsDraggable) {
                        aBoolean1242 = false;
                        anInt989 = 0;
                        selectedItemInterfaceID = j2;
                        selectedItemSlot = l1;

                        activeInterfaceType = 2;
                        anInt1087 = super.saveClickX;
                        anInt1088 = super.saveClickY;
                        if (RSInterface.interfaceCache[j2].parentID == openInterfaceID)
                            activeInterfaceType = 1;
                        if (RSInterface.interfaceCache[j2].parentID == backDialogID)
                            activeInterfaceType = 3;
                        return true;
                    }
                }
            }
            if (j == 1
                    && (anInt1253 == 1 || menuHasAddFriend())
                    && MiniMenu.optionCount > 2)
                j = 2;
            if (j == 1 && MiniMenu.optionCount > 0)
                doAction((MiniMenuOption) MiniMenu.options.head.next);
            if (j == 2 && MiniMenu.optionCount > 0)
                determineMenuSize();
            return false;
        }
    }

    private void saveMidi(boolean flag, byte abyte0[]) {
    	/* empty */
    }

    private void loadRegion() {
        try {
            anInt985 = -1;
            stillGraphicsNode.clear();
            projectileNode.clear();
            clearCaches();

            for (int l = 0; l < 4; l++) {
                for (int k1 = 0; k1 < 104; k1++) {
                    for (int j2 = 0; j2 < 104; j2++)
                        tileSettingBits[l][k1][j2] = 0;
                }
            }
            ObjectManager.init(tileSettingBits, intGroundArray);
            int dataLength = terrainData.length;
            stream.createFrame(0);
            if (!loadGeneratedMap) {
                for (int i3 = 0; i3 < dataLength; i3++) {
                    byte abyte0[] = terrainData[i3];
                    if (abyte0 != null) {
                        int xOffset = (mapCoordinates[i3] >> 8) * 64 - baseX;
                        int yOffset = (mapCoordinates[i3] & 0xff) * 64 - baseY;
                        ObjectManager.loadTerrainBlock(abyte0, yOffset, xOffset, (anInt1069 - 6) * 8, (anInt1070 - 6) * 8, tileSettings);
                    }
                }

                for (int j4 = 0; j4 < dataLength; j4++) {
                    byte abyte2[] = terrainData[j4];
                    if (abyte2 == null && anInt1070 < 800) {
                        int l5 = (mapCoordinates[j4] >> 8) * 64 - baseX;
                        int k7 = (mapCoordinates[j4] & 0xff) * 64 - baseY;
                        ObjectManager.clearRegion(k7, 64, 64, l5);
                    }
                }
                stream.createFrame(0);
                for (int i6 = 0; i6 < dataLength; i6++) {
                    byte abyte1[] = aByteArrayArray1247[i6];
                    if (abyte1 != null) {
                        /*String name = (mapCoordinates[i6] >> 8) + "_" + (mapCoordinates[i6] & 0xff);
                      	System.out.println(mapCoordinates[i6]+":"+name);
                        try (RandomAccessFile inputStream = new RandomAccessFile("C:/Users/Kompas/workspace/rs2 tools/map_editor/maps/l" + name + ".new", "r")) {
    						byte[] b = new byte[(int) inputStream.length()];
    						inputStream.read(b);
    						abyte1 = b;
    					}*/
                        int l8 = (mapCoordinates[i6] >> 8) * 64 - baseX;
                        int k9 = (mapCoordinates[i6] & 0xff) * 64 - baseY;
                        ObjectManager.loadObjectBlock(l8, tileSettings, k9,
                                worldController, abyte1, baseX, baseY);
                    }
                }
            }
            if (loadGeneratedMap) {
                for (int z = 0; z < 4; z++) {
                    for (int x = 0; x < 13; x++) {
                        for (int y = 0; y < 13; y++) {
                            int bits = constructionMapInformation[z][x][y];
                            if (bits != -1) {
                                int tileZ = bits >> 24 & 3;
                                int tileRotation = bits >> 1 & 3;
                                int tileX = bits >> 14 & 0x3ff;
                                int tileY = bits >> 3 & 0x7ff;
                                int j11 = (tileX / 8 << 8) + tileY / 8;
                                for (int l11 = 0; l11 < mapCoordinates.length; l11++) {
                                    if (mapCoordinates[l11] != j11
                                            || terrainData[l11] == null)
                                        continue;
                                    ObjectManager.loadTerrainSubBlock(tileZ,
                                            tileRotation, tileSettings, x * 8,
                                            (tileX & 7) * 8, terrainData[l11],
                                            (tileY & 7) * 8, z, y * 8);
                                    break;
                                }
                            }
                        }
                    }
                }

                for (int l4 = 0; l4 < 13; l4++) {
                    for (int k6 = 0; k6 < 13; k6++) {
                        int i8 = constructionMapInformation[0][l4][k6];
                        if (i8 == -1)
                        	ObjectManager.clearRegion(k6 * 8, 8, 8, l4 * 8);
                    }
                }

                stream.createFrame(0);
                for (int z = 0; z < 4; z++) {
                    for (int x = 0; x < 13; x++) {
                        for (int y = 0; y < 13; y++) {
                            int bits = constructionMapInformation[z][x][y];
                            if (bits != -1) {
                                int tileZ = bits >> 24 & 3;
                                int tileRotation = bits >> 1 & 3;
                                int tileX = bits >> 14 & 0x3ff;
                                int tileY = bits >> 3 & 0x7ff;
                                int j12 = (tileX / 8 << 8) + tileY / 8;
                                for (int k12 = 0; k12 < mapCoordinates.length; k12++) {
                                    if (mapCoordinates[k12] != j12 || aByteArrayArray1247[k12] == null)
                                        continue;
                                    ObjectManager.loadObjectSubBlock(tileSettings, worldController, tileZ, x * 8, (tileY & 7) * 8, z, aByteArrayArray1247[k12], (tileX & 7) * 8, tileRotation, y * 8);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            stream.createFrame(0);
            unlinkMRUNodes();
            ObjectManager.addTiles(tileSettings, worldController);
            stream.createFrame(0);
            int k3 = ObjectManager.setZ;
            if (k3 > plane)
                k3 = plane;
            if (k3 < plane - 1)
                k3 = plane - 1;
            if (lowMem)
                worldController.setHeightLevel(ObjectManager.setZ);
            else
                worldController.setHeightLevel(0);
            for (int i5 = 0; i5 < 104; i5++) {
                for (int i7 = 0; i7 < 104; i7++)
                    spawnGroundItem(i5, i7);

            }

            clearObjectSpawnRequests();

            ObjectDefinition.modelCache.unlinkAll();
            if (frame != null) {
                stream.createFrame(210);
                stream.putInt(0x3f008edd);
            }
            onDemandFetcher.method566();
            int k = (anInt1069 - 6) / 8 - 1;
            int j1 = (anInt1069 + 6) / 8 + 1;
            int i2 = (anInt1070 - 6) / 8 - 1;
            int l2 = (anInt1070 + 6) / 8 + 1;
            if (tutorialIsland) {
                k = 49;
                j1 = 50;
                i2 = 49;
                l2 = 50;
            }


            // System.out.println("new region requested");

            for (int l3 = k; l3 <= j1; l3++) {
                for (int j5 = i2; j5 <= l2; j5++)
                    if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {

                        int j7 = onDemandFetcher.getMapIndex(0, j5, l3);
                        if (j7 != -1)
                            onDemandFetcher.loadExtra(j7, 3);

                        int k8 = onDemandFetcher.getMapIndex(1, j5, l3);

                        // System.out.println("region id: " + ((l3 << 8) + j5) + " map ground: " + j7 + " map objects: " + k8);


                       /* if(j7 > -1) {
                            copyFile(new File("./667maps/" + j7 + ".dat"), new File("./maps/" + j7 + ".dat"));
                        }
                        if(k8 > -1) {
                            copyFile(new File("./667maps/" + k8 + ".dat"), new File("./maps/" + k8 + ".dat"));
                        }


                        System.out.println(j7);
                        System.out.println(k8); */

                        if (k8 != -1)
                            onDemandFetcher.loadExtra(k8, 3);
                    }

            }
            
            ObjectManager.reset();
            nanoTimer.reset();
            for (int i = 0; i < GameShell.mainRedrawCache.length; i++) {
            	GameShell.mainRedrawCache[i] = 0L;
            }
            GameShell.mainRedrawCachePos = 0;
            gameLoopCount = 0;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void unlinkMRUNodes() {
        ObjectDefinition.recentUse.unlinkAll();
        ObjectDefinition.modelCache.unlinkAll();
        ObjectDefinition.modelCache2.unlinkAll();
        NpcDefinition.modelCache.unlinkAll();
        Rasterizer.textureManager.resetTextures();
        NpcDefinition.recentUse.unlinkAll();
        ItemDefinition.modelCache.unlinkAll();
        ItemDefinition.itemSpriteCache.unlinkAll();
        ItemDefinition.recentUse.unlinkAll();
        Player.modelCache.unlinkAll();
        SpotAnim.modelCache.unlinkAll();
        RSInterface.aMRUNodes_264.unlinkAll();
    }

    private void renderMapScene(int z) {
        int pixels[] = minimapImage.myPixels;
        int pixelAmount = pixels.length;
        for (int k = 0; k < pixelAmount; k++)
            pixels[k] = 0;

        for (int y = 1; y < 103; y++) {
            int pixelPointer = 24628 + (103 - y) * 512 * 4;
            for (int x = 1; x < 103; x++) {
                if ((tileSettingBits[z][x][y] & 0x18) == 0)
                    worldController.drawMinimapTile(pixels, pixelPointer, z, x,
                            y);
                if (z < 3 && (tileSettingBits[z + 1][x][y] & 8) != 0)
                    worldController.drawMinimapTile(pixels, pixelPointer,
                            z + 1, x, y);
                pixelPointer += 4;
            }

        }

        int j1 = 0xffffff;
        int l1 = 0xff0000;
        minimapImage.method343();
        for (int i2 = 1; i2 < 103; i2++) {
            for (int j2 = 1; j2 < 103; j2++) {
                if ((tileSettingBits[z][j2][i2] & 0x18) == 0)
                    drawMapScenes(i2, j1, j2, l1, z);
                if (z < 3 && (tileSettingBits[z + 1][j2][i2] & 8) != 0)
                    drawMapScenes(i2, j1, j2, l1, z + 1);
            }

        }

        gameScreen.initDrawingArea();
        numOfMapMarkers = 0;
        for (int k2 = 0; k2 < 104; k2++) {
            for (int l2 = 0; l2 < 104; l2++) {
                long i3 = worldController.method303(plane, k2, l2);
                if (i3 != 0L) {
                    int objectMapFunctionID = ObjectDefinition.forID((int) (i3 >>> 32) & 0x7fffffff).mapFunctionID;
                    if (objectMapFunctionID >= 0) {
                    	markGraphic[numOfMapMarkers] = objectMapFunctionID;
                    	markPosX[numOfMapMarkers] = k2;
                    	markPosY[numOfMapMarkers] = l2;
                    	numOfMapMarkers++;
                    }
                }
            }

        }

    }

    private void spawnGroundItem(int x, int y) {
        NodeList class19 = groundArray[plane][x][y];
        if (class19 == null) {
            worldController.removeGroundItemTile(plane, x, y);
            return;
        }
        int k = 0xfa0a1f01;
        Item obj = null;
        for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19
                .reverseGetNext()) {
            ItemDefinition itemDef = ItemDefinition.forID(item.ID);
            int l = itemDef.value;
            if (itemDef.stackable)
                l *= item.itemCount + 1;
            if (l > k) {
                k = l;
                obj = item;
            }
        }

        class19.insertTail(obj);
        Item obj1 = null;
        Item obj2 = null;
        for (Item class30_sub2_sub4_sub2_1 = (Item) class19.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item) class19
                .reverseGetNext()) {
            if (class30_sub2_sub4_sub2_1.ID != obj.ID
                    && obj1 == null)
                obj1 = class30_sub2_sub4_sub2_1;
            if (class30_sub2_sub4_sub2_1.ID != obj.ID
                    && class30_sub2_sub4_sub2_1.ID != obj1.ID
                    && obj2 == null)
                obj2 = class30_sub2_sub4_sub2_1;
        }

        long i1 = x + (y << 7) | 0x60000000;
        worldController.addGroundItemTile(x, i1, obj1,
                getFloorDrawHeight(plane, y * 128 + 64, x * 128 + 64),
                obj2, obj, plane, y);
    }

    private void renderNPCs(boolean flag) {
        for (int j = 0; j < npcCount; j++) {
            NPC npc = npcArray[npcIndices[j]];
            if (npc == null || !npc.isVisible() || npc.desc.aBoolean93 != flag)
                continue;
            int l = npc.x >> 7;
            int i1 = npc.y >> 7;
            if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
                continue;
            if (npc.size == 1 && (npc.x & 0x7f) == 64
                    && (npc.y & 0x7f) == 64) {
                if (anIntArrayArray929[l][i1] == anInt1265)
                    continue;
                anIntArrayArray929[l][i1] = anInt1265;
            }
            long k = 0x20000000L | (long) npcIndices[j] << 32;
            if (!npc.desc.clickable) {
                k |= ~0x7fffffffffffffffL;
            }
            worldController
                    .addEntity(plane, npc.currentRotation,
                            getFloorDrawHeight(plane, npc.y, npc.x), k, npc.y,
                            (npc.size - 1) * 64 + 60, npc.x, npc,
                            npc.aBoolean1541);
        }
    }

    private void buildInterfaceMenu(int x, RSInterface class9, int mouseX, int y,
                                    int mouseY, int scrollY) {
    	hoverOnShopTile = -1;
        if (class9.type != 0 || class9.children == null
                || class9.isMouseoverTriggered) {
            return;
        }

        if (mouseX < x || mouseY < y || mouseX > x + class9.width || mouseY > y + class9.height) {
        	return;
        }

        int k1 = class9.children.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int componentX = class9.childX[l1] + x;
            int componentY = (class9.childY[l1] + y) - scrollY;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[l1]];
            componentX += class9_1.offsetX;
            componentY += class9_1.offsetY;

            boolean isMouseOver = mouseX >= componentX && mouseY >= componentY && mouseX < componentX + class9_1.width && mouseY < componentY + class9_1.height;

            if ((class9_1.mOverInterToTrigger >= 0 || class9_1.textHoverColor != 0) && isMouseOver) {
                if (class9_1.mOverInterToTrigger >= 0)
                    anInt886 = class9_1.mOverInterToTrigger;
                else
                    anInt886 = class9_1.id;
            }
            if (class9_1.type == 8 && isMouseOver) {
                anInt1315 = class9_1.id;
            }
			if (class9_1.contentType >= 1338 && class9_1.contentType <= 1341 && isMouseOver) {
				int id = class9_1.contentType - 1338;
				MiniMenu.addOption("Choose color", 692, 0L, 0, id);
				continue;
			}
            if (class9_1.type == 0) {
                buildInterfaceMenu(componentX, class9_1, mouseX, componentY, mouseY, class9_1.scrollPosition);
                if (class9_1.scrollMax > class9_1.height) {
                    method65(componentX + class9_1.width, class9_1.height, mouseX, mouseY, class9_1, componentY, true, class9_1.scrollMax, 0, class9_1.newScroller);
                }
            } else {
                if (class9_1.atActionType == 1 && isMouseOver) {
                    boolean flag = false;
                    if (class9_1.contentType != 0)
                        flag = buildFriendsListMenu(class9_1);
                    if (!flag) {
                        if (class9_1.tooltip != null && class9_1.tooltip.length() > 0) {
                            MiniMenu.addOption(class9_1.tooltip, 315, 0L, 0, class9_1.id);
                        }
                    }
                } else if (class9_1.atActionType == 2 && targetInterfaceId == 0 && isMouseOver) {
                    String s = class9_1.targetVerb;
                    if (s.indexOf(" ") != -1)
                        s = s.substring(0, s.indexOf(" "));


                    if (class9_1.targetName.endsWith("Armadyl") || class9_1.targetName.endsWith("Surge") || class9_1.targetName.endsWith("strike") || class9_1.targetName.endsWith("bolt") || class9_1.targetName.equals("Crumble undead") || class9_1.targetName.endsWith("blast") || class9_1.targetName.endsWith("wave") || class9_1.targetName.equals("Claws of Guthix") || class9_1.targetName.equals("Flames of Zamorak") || class9_1.targetName.equals("Magic Dart") || class9_1.targetName.endsWith("Rush") || class9_1.targetName.endsWith("Burst") || class9_1.targetName.endsWith("Blitz") || class9_1.targetName.endsWith("Barrage")) {
                        MiniMenu.addOption("Autocast", "<col=00ff00>" + class9_1.targetName, 104, 0L, 0, class9_1.id);
                    }

                    MiniMenu.addOption(s, "<col=00ff00>" + class9_1.targetName, 626, 0L, 0, class9_1.id);
                } else if (class9_1.atActionType == 3 && isMouseOver) {
                	MiniMenu.addOption("Close", 200, 0L, 0, class9_1.id);
                } else if (class9_1.atActionType == 4 && isMouseOver) {
                    MiniMenu.addOption(class9_1.tooltip, 169, 0L, 0, class9_1.id);
                } else if (class9_1.atActionType == 5 && isMouseOver) {
                    if (class9_1.tooltip.toLowerCase().startsWith("infuse")) {
                        String itemName = class9_1.tooltip.substring(class9_1.tooltip.indexOf('>') + 1);

                        for (int index = INFUSE_OPTIONS.length - 1; index >= 0; index--) {
                        	int option = 0;
                            if (index == 0) {
                            	option = 632;//1
                            } else if (index == 1) {
                            	option = 78;//5
                            } else if (index == 2) {
                            	option = 867;//10
                            } else if (index == 3) {
                            	option = 431;//all
                            } else  if (index == 4) {
                            	option = 53;//x
                            }

                            MiniMenu.addOption(INFUSE_OPTIONS[index], itemName, option, class9_1.itemSpriteId1, class9_1.itemSpriteIndex, class9.parentID);
                        }
                    } else if (class9_1.tooltip.toLowerCase().startsWith("transform")) {
                        String itemName = class9_1.tooltip.substring(class9_1.tooltip.indexOf('>') + 1);

                        for (int index = TRANSFORM_OPTIONS.length - 1; index >= 0; index--) {
                        	int option = 0;
                            if (index == 0) {
                            	option = 632;//1
                            } else if (index == 1) {
                            	option = 78;//5
                            } else if (index == 2) {
                            	option = 867;//10
                            } else if (index == 3) {
                            	option = 431;//all
                            } else  if (index == 4) {
                            	option = 53;//x
                            }

                            MiniMenu.addOption(TRANSFORM_OPTIONS[index], itemName, option, class9_1.itemSpriteId1, class9_1.itemSpriteIndex, class9.parentID);
                        }
                    } else {
                        MiniMenu.addOption(class9_1.tooltip, 646, 0L, 0, class9_1.id);
                    }
                } else if (class9_1.atActionType == 6 && !isDialogueInterface && isMouseOver) {
                    MiniMenu.addOption(class9_1.tooltip, 679, 0L, 0, class9_1.id);
                }

                if (class9_1.type == 2) {
                    int k2 = 0;
                    for (int l2 = 0; l2 < class9_1.height; l2++) {
                        for (int i3 = 0; i3 < class9_1.width; i3++) {
                            int j3 = componentX + i3 * (32 + class9_1.invSpritePadX);
                            int k3 = componentY + l2 * (32 + class9_1.invSpritePadY);
                            if (class9_1.sprites != null && k2 < 20) {
                                j3 += class9_1.spritesX[k2];
                                k3 += class9_1.spritesY[k2];
                            }
                            if (mouseX >= j3 && mouseY >= k3 && mouseX < j3 + 32
                                    && mouseY < k3 + 32) {
                                mouseInvInterfaceIndex = k2;
                                lastActiveInvInterface = class9_1.id;
                                if (class9_1.inv[k2] > 0) {
                                    ItemDefinition itemDef = ItemDefinition
                                            .forID(class9_1.inv[k2] - 1);
                                    if (itemSelected == 1
                                            && class9_1.isInventoryInterface) {
                                        if (class9_1.id != anInt1284
                                                || k2 != anInt1283) {
											MiniMenu.addOption("Use " + selectedItemName + " with", "<col=ff9040>" + itemDef.name, 870, itemDef.id, k2, class9_1.id);
                                        }
                                    } else if (targetInterfaceId != 0
                                            && class9_1.isInventoryInterface) {
                                        if ((targetBitMask & 0x10) == 16) {
                                            MiniMenu.addOption(targetOption, "<col=ff9040>" + itemDef.name, 543, itemDef.id, k2, class9_1.id);
                                        }
                                    } else {
                                        if (class9_1.isInventoryInterface) {
                                            for (int l3 = 4; l3 >= 3; l3--)
                                                if (itemDef.actions != null && itemDef.actions[l3] != null) {
													int option = 0;
                                                    if (l3 == 3) {
                                                    	option = 493;
                                                    } else if (l3 == 4) {
                                                    	option = 847;
                                                    }

                                                    MiniMenu.addOption(itemDef.actions[l3], "<col=ff9040>" + itemDef.name, option, itemDef.id, k2, class9_1.id);
                                                } else if (l3 == 4) {
													MiniMenu.addOption("Drop", "<col=ff9040>" + itemDef.name, 847, itemDef.id, k2, class9_1.id);
                                                }

                                        }
                                        if (class9_1.usableItemInterface) {
                                        	int option;
                                        	String text;
                                            if (openInterfaceID == 24700) {
                                                text = "Offer";
                                                option = 24700;
                                                GEItemId = itemDef.id;
                                            } else {
												text = "Use";
                                                option = 447;
                                            }

                                            MiniMenu.addOption(text, "<col=ff9040>" + itemDef.name, option, itemDef.id, k2, class9_1.id);
                                        }
                                        if (class9_1.isInventoryInterface
                                                && itemDef.actions != null) {
                                            for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDef.actions[i4] != null) {
													int option = 0;
                                                    if (i4 == 0) {
                                                    	option = 74;
                                                    } else if (i4 == 1) {
                                                    	option = 454;
                                                    } else if (i4 == 2) {
                                                    	option = 539;
                                                    }

                                                    MiniMenu.addOption(itemDef.actions[i4], "<col=ff9040>" + itemDef.name, option, itemDef.id, k2, class9_1.id);
                                                }
                                            }
                                        }
                                        if (class9_1.actions != null) {
                                            for (int j4 = class9_1.actions.length - 1; j4 >= 0; j4--) {
                                                if (class9_1.actions[j4] != null) {
													int option = 0;
                                                    if (j4 == 0) { // 1
                                                    	option = 632;
                                                    } else if (j4 == 1) { // 5
                                                    	option = 78;
                                                    } else if (j4 == 2) { // 10
                                                    	option = 867;
                                                    } if (j4 == 3) { // 100
                                                        if (class9_1.actions[j4].startsWith("Withdraw-100")) {
                                                        	option = 868; // withdraw 100
                                                        } else {
                                                            option = 431; // withdraw all
                                                        }
                                                    } else if (j4 == 4) { // x
                                                        option = 53;
                                                    } else if (j4 == 5) { // all
                                                        if (openInterfaceID == 3824) {
                                                            option = 434;
                                                        } else {
                                                            option = 431;
                                                        }
                                                    } else if (j4 == 6) { // all but one
                                                        option = 432;
                                                    } else if (j4 == 7) { // withdraw last x amount
                                                        option = 433;
                                                    }

                                                    MiniMenu.addOption(class9_1.actions[j4], "<col=ff9040>" + itemDef.name, option, itemDef.id, k2, class9_1.id);
                                                    if (class9_1.parentID == 3824) {
                                                        hoverOnShopTile = k2;
                                                    }
                                                }
                                            }
                                        }

                                        // disabled examine option on bank tabs
                                        if (class9_1.id >= 22035 && class9_1.id <= 22042) {//TODO add a boolean if to show examine
                                            continue;
                                        }

                                        MiniMenu.addOption("Examine", "<col=ff9040>" + itemDef.name, 1125, itemDef.id, k2, class9_1.id);
                                    }
                                }
                            }
                            k2++;
                        }

                    }
                } else if ((class9_1.type == 4 && class9_1.message.length() > 0)
                        || class9_1.type == 5) {
                    if (class9_1.actions != null && isMouseOver) {
                    	for (int action = class9_1.actions.length - 1; action >= 0; action--) {
                    		if (class9_1.actions[action] != null) {
                    			MiniMenu.addOption(class9_1.actions[action], (class9_1.type == 4 ? class9_1.message : ""), 647, 0L, action, class9_1.id);
                    		}
                    	}
                    }
                }

            }
        }
    }

    private void drawScrollbar(int height, int scrollPosition, int drawY,
                               int drawX, int scrollMax, boolean newScroll) {
        if (newScroll) {
            cacheSprite[111].drawSprite(drawX, drawY);
            cacheSprite[112].drawSprite(drawX, (drawY + height) - 16);
        } else {
            scrollBar1.drawSprite(drawX, drawY);//Top scroll button
            scrollBar2.drawSprite(drawX, (drawY + height) - 16);//Bottom scroll button
        }
        if (!newScroll) {//Scroll background
            DrawingArea
                    .fillRect(drawX, drawY + 16, 16, height - 32, 0x000001);
            DrawingArea
                    .fillRect(drawX, drawY + 16, 15, height - 32, 0x3d3426);
            DrawingArea
                    .fillRect(drawX, drawY + 16, 13, height - 32, 0x342d21);
            DrawingArea
                    .fillRect(drawX, drawY + 16, 11, height - 32, 0x2e281d);
            DrawingArea
                    .fillRect(drawX, drawY + 16, 10, height - 32, 0x29241b);
            DrawingArea.fillRect(drawX, drawY + 16, 9, height - 32, 0x252019);
            DrawingArea.fillRect(drawX, drawY + 16, 1, height - 32, 0x000001);
        }
        int k1 = ((height - 32) * height) / scrollMax;
        if (k1 < 8)
            k1 = 8;
        int l1 = ((height - 32 - k1) * scrollPosition) / (scrollMax - height);
        if (!newScroll) {//Draw actual scroll
            DrawingArea
                    .fillRect(drawX, drawY + 16 + l1, 16, k1, barFillColor);
            DrawingArea.drawVerticalLine(drawX, drawY + 16 + l1, k1, 0x000001);
            DrawingArea.drawVerticalLine(drawX + 1, drawY + 16 + l1, k1, 0x817051);
            DrawingArea.drawVerticalLine(drawX + 2, drawY + 16 + l1, k1, 0x73654a);
            DrawingArea.drawVerticalLine(drawX + 3, drawY + 16 + l1, k1, 0x6a5c43);
            DrawingArea.drawVerticalLine(drawX + 4, drawY + 16 + l1, k1, 0x6a5c43);
            DrawingArea.drawVerticalLine(drawX + 5, drawY + 16 + l1, k1, 0x655841);
            DrawingArea.drawVerticalLine(drawX + 6, drawY + 16 + l1, k1, 0x655841);
            DrawingArea.drawVerticalLine(drawX + 7, drawY + 16 + l1, k1, 0x61553e);
            DrawingArea.drawVerticalLine(drawX + 8, drawY + 16 + l1, k1, 0x61553e);
            DrawingArea.drawVerticalLine(drawX + 9, drawY + 16 + l1, k1, 0x5d513c);
            DrawingArea.drawVerticalLine(drawX + 10, drawY + 16 + l1, k1, 0x5d513c);
            DrawingArea.drawVerticalLine(drawX + 11, drawY + 16 + l1, k1, 0x594e3a);
            DrawingArea.drawVerticalLine(drawX + 12, drawY + 16 + l1, k1, 0x594e3a);
            DrawingArea.drawVerticalLine(drawX + 13, drawY + 16 + l1, k1, 0x514635);
            DrawingArea.drawVerticalLine(drawX + 14, drawY + 16 + l1, k1, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 16 + l1, 15, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 15, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 14, 0x655841);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 13, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 11, 0x6d5f48);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 10, 0x73654a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 7, 0x76684b);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 5, 0x7b6a4d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 4, 0x7e6e50);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 3, 0x817051);
            DrawingArea.drawHorizontalLine(drawX, drawY + 17 + l1, 2, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 15, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 14, 0x5d513c);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 11, 0x625640);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 10, 0x655841);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 7, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 5, 0x6e6046);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 4, 0x716247);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 3, 0x7b6a4d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 2, 0x817051);
            DrawingArea.drawHorizontalLine(drawX, drawY + 18 + l1, 1, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 15, 0x514635);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 14, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 11, 0x5d513c);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 9, 0x61553e);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 7, 0x655841);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 5, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 4, 0x6e6046);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 3, 0x73654a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 2, 0x817051);
            DrawingArea.drawHorizontalLine(drawX, drawY + 19 + l1, 1, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 15, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 14, 0x544936);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 13, 0x594e3a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 10, 0x5d513c);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 8, 0x61553e);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 6, 0x655841);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 4, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 3, 0x73654a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 2, 0x817051);
            DrawingArea.drawHorizontalLine(drawX, drawY + 20 + l1, 1, 0x000001);
            DrawingArea.drawVerticalLine(drawX + 15, drawY + 16 + l1, k1, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 15 + l1 + k1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 15, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 14, 0x3f372a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 10, 0x443c2d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 9, 0x483e2f);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 7, 0x4a402f);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 4, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 3, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 14 + l1 + k1, 2, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 15, 0x443c2d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 11, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 9, 0x514635);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 7, 0x544936);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 6, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 4, 0x594e3a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 3, 0x625640);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 2, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 13 + l1 + k1, 1, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 15, 0x443c2d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 14, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 12, 0x544936);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 11, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 10, 0x594e3a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 7, 0x5d513c);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 4, 0x61553e);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 3, 0x6e6046);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 2, 0x7b6a4d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 12 + l1 + k1, 1, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 16, 0x000001);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 15, 0x4b4131);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 14, 0x514635);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 13, 0x564b38);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 11, 0x594e3a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 9, 0x5d513c);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 7, 0x61553e);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 5, 0x655841);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 4, 0x6a5c43);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 3, 0x73654a);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 2, 0x7b6a4d);
            DrawingArea.drawHorizontalLine(drawX, drawY + 11 + l1 + k1, 1, 0x000001);
        } else {
            cacheSprite[102].drawSprite(drawX, drawY + 16 + l1);
        }
    }

    private void getNpcPos(Stream stream, int packetSize) {
        anInt839 = 0;
        sessionNpcsAwaitingUpdatePtr = 0;
        updateNPCs(stream);
        networkReadNpcSpawnRequests(packetSize, stream);
        readNpcUpdateMask(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (npcArray[l].time != loopCycle) {
                npcArray[l].desc = null;
                npcArray[l] = null;
            }
        }

        if (stream.currentOffset != packetSize) {
            signlink.reporterror(myUsername
                    + " size mismatch in getnpcpos - pos:"
                    + stream.currentOffset + " psize:" + packetSize);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < npcCount; i1++)
            if (npcArray[npcIndices[i1]] == null) {
                signlink.reporterror(myUsername
                        + " null entry in npc list - pos:" + i1 + " size:"
                        + npcCount);
                throw new RuntimeException("eek");
            }

    }

    private void processChatModeClick(boolean isFixed) {
    	int oldcButtonHPos = cButtonHPos;
    	int startY = frameHeight - 23;

        if (super.mouseX >= CHAT_BUTTONS_POS_X[0] && super.mouseX <= CHAT_BUTTONS_POS_X[0] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 0;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[1] && super.mouseX <= CHAT_BUTTONS_POS_X[1] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 1;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[2] && super.mouseX <= CHAT_BUTTONS_POS_X[2] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 2;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[3] && super.mouseX <= CHAT_BUTTONS_POS_X[3] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 3;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[4] && super.mouseX <= CHAT_BUTTONS_POS_X[4] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 4;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[5] && super.mouseX <= CHAT_BUTTONS_POS_X[5] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 5;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[6] && super.mouseX <= CHAT_BUTTONS_POS_X[6] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 6;
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[7] && super.mouseX <= CHAT_BUTTONS_POS_X[7] + 111
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            cButtonHPos = 7;
        } else {
            cButtonHPos = -1;
        }
        if (oldcButtonHPos != cButtonHPos) {
        	reDrawChatArea = true;
        }
        if (super.clickMode3 == 1) {
        	int clickX = super.saveClickX;
        	int clickY = super.saveClickY;
        	
            if (clickX >= CHAT_BUTTONS_POS_X[7] && clickX <= CHAT_BUTTONS_POS_X[7] + 111
                    && clickY >= startY
                    && clickY <= frameHeight) {
                if (openInterfaceID == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = "";
                    canMute = false;
                    for (int i = 0; i < RSInterface.interfaceCache.length; i++) {
                        if (RSInterface.interfaceCache[i] == null
                                || RSInterface.interfaceCache[i].contentType != 600)
                            continue;
                        reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i].parentID;
                        break;
                    }
                } else {
                    pushMessage("Please close the interface you have open before using 'report abuse'", ChatMessage.GAME_MESSAGE);
                }
            }
        }
    }

	private void chatButtonClicked(int id) {
		if (clientSize != 0) {
			if (chatTypeView != id) {
				showChat = true;
			} else {
				showChat = !showChat;
			}
		}

		chatTypeView = id;
		reDrawChatArea = true;

		stream.createFrame(95);
		stream.putByte(publicChatMode);
		stream.putByte(privateChatMode);
		stream.putByte(tradeChatMode);
	}

    void postVarpChange(int id) {
        int clientCode = Varp.cache[id].clientCode;
        if (clientCode == 0) {
            return;
        }

        int value = playerVariables[id];
        if (clientCode == 1) {
            if (value == 1) {
                Rasterizer.calculatePalette(0.9F);
                Rasterizer.textureManager.resetTextures();
            } else if (value == 2) {
                Rasterizer.calculatePalette(0.8F);
                Rasterizer.textureManager.resetTextures();
            } else if (value == 3) {
                Rasterizer.calculatePalette(0.7F);
                Rasterizer.textureManager.resetTextures();
            } else if (value == 4) {
                Rasterizer.calculatePalette(0.6F);
                Rasterizer.textureManager.resetTextures();
            }
            ItemDefinition.itemSpriteCache.unlinkAll();
            fullRedraw = true;
        } else if (clientCode == 3) {
            boolean flag1 = musicEnabled;
            if (value == 0) {
                adjustVolume(musicEnabled, 0);
                musicEnabled = true;
            } else if (value == 1) {
                adjustVolume(musicEnabled, -400);
                musicEnabled = true;
            } else if (value == 2) {
                adjustVolume(musicEnabled, -800);
                musicEnabled = true;
            } else if (value == 3) {
                adjustVolume(musicEnabled, -1200);
                musicEnabled = true;
            } else if (value == 4) {
                musicEnabled = false;
            }

            if (musicEnabled != flag1 && !lowMem) {
                if (musicEnabled) {
                    nextSong = currentSong;
                    songChanging = true;
                    onDemandFetcher.loadToCache(2, nextSong);
                } else {
                    stopMidi();
                }
                prevSong = 0;
            }
        } else if (clientCode == 4) {
            if (value == 0) {
                waveON = true;
                setWaveVolume(0);
            } else if (value == 1) {
                waveON = true;
                setWaveVolume(-400);
            } else if (value == 2) {
                waveON = true;
                setWaveVolume(-800);
            } else if (value == 3) {
                waveON = true;
                setWaveVolume(-1200);
            } else if (value == 4) {
                waveON = false;
            }
        } else if (clientCode == 5) {
            anInt1253 = value;
        } else if (clientCode == 6) {
            anInt1249 = value;
        } else if (clientCode == 8) {
            splitPrivateChat = value;
            reDrawChatArea = true;
        } else if (clientCode == 9) {
            anInt913 = value;
        }
    }

    private void drawScrollbar(int barHeight, int scrollPos, int yPos, int xPos, int contentHeight, boolean newScroller, boolean isTransparent) {
        int backingAmount = (barHeight - 32) / (isTransparent ? 2 : 5);
        int scrollPartHeight = ((barHeight - 32) * barHeight) / contentHeight;
        if (scrollPartHeight < 10)
            scrollPartHeight = 10;
        int scrollPartAmount = (scrollPartHeight / (isTransparent ? 1 : 5)) - 2;
        int scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos) / (contentHeight - barHeight) + 16 + yPos;
        for (int i = 0, yyPos = yPos + 16; i <= (isTransparent ? backingAmount / 2 + 6 : backingAmount); i++, yyPos += (isTransparent ? 3 : 5)) {
            if (isTransparent) {
                cacheSprite[48].drawAdvancedSprite(xPos, yyPos);
            } else {
                cacheSprite[98].drawSprite(xPos, yyPos);
            }
        }
        if (isTransparent) {
            cacheSprite[49].drawAdvancedSprite(xPos, scrollPartPos);
        } else
            cacheSprite[99].drawSprite(xPos, scrollPartPos);
        scrollPartPos += 5;
        for (int i = 0; i <= (isTransparent ? scrollPartAmount - 9 : scrollPartAmount); i++) {
            if (isTransparent) {
                cacheSprite[50].drawAdvancedSprite(xPos, scrollPartPos);
            } else
                cacheSprite[100].drawSprite(xPos, scrollPartPos);
            scrollPartPos += isTransparent ? 1 : 5;
        }
        scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos) / (contentHeight - barHeight) + 16 + yPos + (scrollPartHeight - 5);
        if (isTransparent) {
            cacheSprite[51].drawAdvancedSprite(xPos, scrollPartPos);
        } else
            cacheSprite[97].drawSprite(xPos, scrollPartPos);
        if (newScroller) {
            cacheSprite[111].drawSprite(xPos, yPos);
            cacheSprite[112].drawSprite(xPos, (yPos + barHeight) - 16);
        } else if (isTransparent) {
            cacheSprite[46].drawAdvancedSprite(xPos, yPos);
            cacheSprite[47].drawAdvancedSprite(xPos, (yPos + barHeight) - 16);
        } else {
            cacheSprite[109].drawSprite(xPos, yPos);
            cacheSprite[110].drawSprite(xPos, (yPos + barHeight) - 16);
        }
    }

    /**
     * hitMarks
     */
    private void hitmarkDraw(int type, int icon, int damage, int move, int opacity) {
        if (damage > 0) {
        	RSFont font = type == 1 ? bigHit : smallHit;
        	int hitLength = digitCount(damage) * 2;
        	String damageString = String.valueOf(damage);
            // fixes poison hitmarker
            if (icon == 255) {
                icon = -1;
            }

            Sprite end1 = null, middle = null, end2 = null;

            switch (type) {
                case 0:
                    end1 = hitMark[0];
                    middle = hitMark[1];
                    end2 = hitMark[2];
                    break;
                case 1:
                    end1 = hitMark[3];
                    middle = hitMark[4];
                    end2 = hitMark[5];
                    break;
                case 2:
                    end1 = hitMark[6];
                    middle = hitMark[7];
                    end2 = hitMark[8];
                    break;
                case 3:
                    end1 = hitMark[9];
                    middle = hitMark[10];
                    end2 = hitMark[11];
                    break;
                case 4:
                    end1 = hitMark[12];
                    middle = hitMark[13];
                    end2 = hitMark[14];
                    break;
                case 5:
                    end1 = hitMark[15];
                    middle = hitMark[16];
                    end2 = hitMark[17];
                    break;
                case 6:
                    end1 = hitMark[18];
                    middle = hitMark[19];
                    end2 = hitMark[20];
                    break;
                case 7:
                    end1 = hitMark[21];
                    middle = hitMark[22];
                    end2 = hitMark[23];
                    break;
                case 8:
                    end1 = hitMark[24];
                    middle = hitMark[25];
                    end2 = hitMark[26];
                    break;
                case 9:
                    end1 = hitMark[27];
                    middle = hitMark[28];
                    end2 = hitMark[29];
                    break;
                case 10:
                    end1 = hitMark[30];
                    middle = hitMark[33];
                    end2 = hitMark[32];
                    break;
                case 11:
                    end1 = hitMark[34];
                    middle = hitMark[36];
                    end2 = hitMark[35];
                    break;
                case 12:
                    end1 = cacheSprite[1026];
                    middle = cacheSprite[1027];
                    end2 = cacheSprite[1028];
                    break;
            }
            int x = spriteDrawX - 34;
            int y = spriteDrawY - 14 + move;
            if (type <= 1 || icon != -1) {
                hitIcon[icon].drawAdvancedSprite(x, y, opacity);
                x += hitIcon[icon].myWidth;
            }
            end1.drawAdvancedSprite(x, y, opacity);
            x += end1.myWidth;
            int hitX = x + middle.myWidth / 2;
            for (int i = 0; i < hitLength; i++) {
                middle.drawAdvancedSprite(x, y, opacity);
                x += middle.myWidth;
            }
            end2.drawAdvancedSprite(x, y, opacity);
            
            if (type == 1) {
            	y += 14;
            } else {
            	y += 44;
            }
            font.drawBasicString(damageString, type == 1 ? hitX - 2 : hitX, y, 0xffffff, 0, opacity);
        } else {
            Sprite block = cacheSprite[806];
            block.drawAdvancedSprite(spriteDrawX - 12, spriteDrawY - 14 + move, opacity);
        }
    }

	private static int digitCount(int n) {
		if (n < 100000) {
			if (n < 100) {
				if (n < 10)
					return 1;
				else
					return 2;
			} else {
				if (n < 1000)
					return 3;
				else {
					if (n < 10000)
						return 4;
					else
						return 5;
				}
			}
		} else {
			if (n < 10000000) {
				if (n < 1000000)
					return 6;
				else
					return 7;
			} else {
				if (n < 100000000)
					return 8;
				else {
					if (n < 1000000000)
						return 9;
					else
						return 10;
				}
			}
		}
	}

    private Mobile getEntity(int index) {
        if (index < 32768)
            return npcArray[index];
        else
            return playerArray[index - 32768];
    }

    private void updateEntities() {
        try {
            int anInt974 = 0;
            for (int j = -1; j < playerCount + npcCount; j++) {
                Mobile entity;
                if (j == -1)
                    entity = myPlayer;
                else if (j < playerCount)
                    entity = playerArray[playerIndices[j]];
                else
                    entity = npcArray[npcIndices[j - playerCount]];
                if (entity == null || !entity.isVisible())
                    continue;
                if (entity instanceof NPC) {
                    NpcDefinition npcDef = ((NPC) entity).desc;
                    if (npcDef.childrenIDs != null)
                        npcDef = npcDef.morph();
                    if (npcDef == null)
                        continue;
                }
                if (j < playerCount) {
                    int l = 30;
                    Player player = (Player) entity;
                    if (player.headIcon >= 0) {
                        npcScreenPos(entity, entity.height + 15);
                        if (spriteDrawX > -1) {
                            if (player.skullIcon < 2) {
                                skullIcons[player.skullIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                                l += 25;
                            }
                            if (player.headIcon <= 18) {
                                headIcons[player.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                                l += 18;
                            }
                        }
                    }
                    if (j >= 0 && headiconDrawType == 10 && otherPlayerID == playerIndices[j]) {
                        npcScreenPos(entity, entity.height + 15);
                        if (spriteDrawX > -1)
                            headIconsHint[player.hintIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
                    }
                } else {
                    NpcDefinition npcDef_1 = ((NPC) entity).desc;
                    if (npcDef_1.headIcon >= 0 && npcDef_1.headIcon < headIcons.length) {
                        npcScreenPos(entity, entity.height + 15);
                        if (spriteDrawX > -1)
                            headIcons[npcDef_1.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - 30);
                    }
                    if (headiconDrawType == 1 && headiconNpcID == npcIndices[j - playerCount] && loopCycle % 20 < 10) {
                        npcScreenPos(entity, entity.height + 15);
                        if (spriteDrawX > -1)
                            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
                    }
                }
                if (entity.textSpoken != null && (j >= playerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) entity).name))) {
                    npcScreenPos(entity, entity.height);
                    if (spriteDrawX > -1 && anInt974 < anInt975) {
                        anIntArray979[anInt974] = bold.getBasicWidth(entity.textSpoken) / 2;
                        anIntArray978[anInt974] = bold.baseCharacterHeight;
                        anIntArray976[anInt974] = spriteDrawX;
                        anIntArray977[anInt974] = spriteDrawY;
                        anIntArray980[anInt974] = entity.anInt1513;
                        chat_effects[anInt974] = entity.anInt1531;
                        anIntArray982[anInt974] = entity.textCycle;
                        aStringArray983[anInt974++] = entity.textSpoken;
                        if (anInt1249 == 0 && entity.anInt1531 >= 1 && entity.anInt1531 <= 3) {
                            anIntArray978[anInt974] += 10;
                            anIntArray977[anInt974] += 5;
                        }
                        if (anInt1249 == 0 && entity.anInt1531 == 4)
                            anIntArray979[anInt974] = 60;
                        if (anInt1249 == 0 && entity.anInt1531 == 5)
                            anIntArray978[anInt974] += 5;
                    }
                }
				if (entity.loopCycleStatus > loopCycle) {
					try {
						npcScreenPos(entity, entity.height + 15);
						if (spriteDrawX > -1) {
							if (!Settings.newHpBar) {
								int i1 = (entity.currentHealth * 30) / entity.maxHealth;
								if (i1 > 30)
									i1 = 30;
								int barX = spriteDrawX - 15;
								int barY = spriteDrawY - 3;
								DrawingArea.fillRect(barX, barY, i1, 5, 65280);
								DrawingArea.fillRect(barX + i1, barY, 30 - i1, 5, 0xff0000);
							} else {
								int barWidth = (entity.currentHealth * 56) / entity.maxHealth;
								if (barWidth > 56) {
									barWidth = 56;
								}
								int barX = spriteDrawX - 28;
								int barY = spriteDrawY - 5;
								cacheSprite[556].drawSprite(barX, barY);
								DrawingArea.setDrawingArea(barX, barY, barX + barWidth, barY + cacheSprite[557].myHeight);
								cacheSprite[557].drawSprite(barX, barY);
								DrawingArea.defaultDrawingAreaSize();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
                if (Settings.hitmarks == Constants.HITMARKS_317) {
                    for (int j1 = 0; j1 < 4; j1++)
                        if (entity.hitsLoopCycle[j1] > loopCycle) {
                            npcScreenPos(entity, entity.height / 2);
                            if (spriteDrawX > -1) {
                                if (j1 == 1)
                                    spriteDrawY -= 20;
                                if (j1 == 2) {
                                    spriteDrawX -= 15;
                                    spriteDrawY -= 10;
                                }
                                if (j1 == 3) {
                                    spriteDrawX += 15;
                                    spriteDrawY -= 10;
                                }
                                hitMarks[entity.hitMarkTypes[j1]].drawSprite(spriteDrawX - 12, spriteDrawY - 12);
                                small.drawBasicStringCentered(String.valueOf(entity.hitArray[j1]), spriteDrawX - 1, spriteDrawY + 3, 0xffffff, 0);
                            }
                        }
                } else if (Settings.hitmarks == Constants.HITMARKS_562) {
                    for (int j2 = 0; j2 < 4; j2++) {
                        if (entity.hitsLoopCycle[j2] > loopCycle) {
                            npcScreenPos(entity, entity.height / 2);
                            if (spriteDrawX > -1) {
                                if (j2 == 0 && entity.hitArray[j2] > 99)
                                    entity.hitMarkTypes[j2] = 3;
                                else if (j2 == 1 && entity.hitArray[j2] > 99)
                                    entity.hitMarkTypes[j2] = 3;
                                else if (j2 == 2 && entity.hitArray[j2] > 99)
                                    entity.hitMarkTypes[j2] = 3;
                                else if (j2 == 3 && entity.hitArray[j2] > 99)
                                    entity.hitMarkTypes[j2] = 3;
                                if (j2 == 1) {
                                    spriteDrawY -= 20;
                                }
                                if (j2 == 2) {
                                    spriteDrawX -= (entity.hitArray[j2] > 99 ? 30 : 20);
                                    spriteDrawY -= 10;
                                }
                                if (j2 == 3) {
                                    spriteDrawX += (entity.hitArray[j2] > 99 ? 30 : 20);
                                    spriteDrawY -= 10;
                                }
                                if (entity.hitMarkTypes[j2] == 3) {
                                    spriteDrawX -= 8;
                                }
                                hitMarks562[entity.hitMarkTypes[j2]].drawAdvancedSprite(spriteDrawX - 12, spriteDrawY - 12, 256);
                                small.drawBasicStringCentered(String.valueOf(entity.hitArray[j2]), (entity.hitMarkTypes[j2] == 3 ? spriteDrawX + 7 : spriteDrawX - 1), spriteDrawY + 3, 0xffffff);
                            }
                        }
                    }
                } else {
                    for (int j1 = 0; j1 < 4; j1++) {
                        if (entity.hitsLoopCycle[j1] > loopCycle) {
                            npcScreenPos(entity, entity.height / 2);
                            if (spriteDrawX > -1) {
                                switch (j1) {
                                    case 1:
                                        spriteDrawY += 20;
                                        break;
                                    case 2:
                                        spriteDrawY += 40;
                                        break;
                                    case 3:
                                        spriteDrawY += 60;
                                        break;
                                    case 4:
                                        spriteDrawY += 80;
                                        break;
                                    case 5:
                                        spriteDrawY += 100;
                                        break;
                                    case 6:
                                        spriteDrawY += 120;
                                        break;
                                }
                                if (entity.hitmarkMove[j1] > -30)
                                	entity.hitmarkMove[j1]--;
                                if (entity.hitmarkMove[j1] < -26)
                                	entity.hitmarkTrans[j1] -= 5;
                                if (entity.hitmarkTrans[j1] < 0) {
                                	entity.hitmarkTrans[j1] = 0;
                                }
                                hitmarkDraw(entity.hitMarkTypes[j1], entity.hitIcon[j1], entity.hitArray[j1], entity.hitmarkMove[j1], entity.hitmarkTrans[j1]);
                            }
                        }
                    }
                }
            }
            for (int k = 0; k < anInt974; k++) {
                int k1 = anIntArray976[k];
                int l1 = anIntArray977[k];
                int j2 = anIntArray979[k];
                int k2 = anIntArray978[k];
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (int l2 = 0; l2 < k; l2++)
                        if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2 && k1 - j2 < anIntArray976[l2] + anIntArray979[l2] && k1 + j2 > anIntArray976[l2] - anIntArray979[l2] && anIntArray977[l2] - anIntArray978[l2] < l1) {
                            l1 = anIntArray977[l2] - anIntArray978[l2];
                            flag = true;
                        }

                }
                spriteDrawX = anIntArray976[k];
                spriteDrawY = anIntArray977[k] = l1;
                String s = aStringArray983[k];
                if (anInt1249 == 0) {
                    int color = 0xffff00;
                    if (anIntArray980[k] < 6)
                        color = anIntArray965[anIntArray980[k]];
                    if (anIntArray980[k] == 6)
                        color = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
                    if (anIntArray980[k] == 7)
                        color = anInt1265 % 20 >= 10 ? 65535 : 255;
                    if (anIntArray980[k] == 8)
                        color = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
                    if (anIntArray980[k] == 9) {
                        int j3 = 150 - anIntArray982[k];
                        if (j3 < 50)
                            color = 0xff0000 + 1280 * j3;
                        else if (j3 < 100)
                            color = 0xffff00 - 0x50000 * (j3 - 50);
                        else if (j3 < 150)
                            color = 65280 + 5 * (j3 - 100);
                    }
                    if (anIntArray980[k] == 10) {
                        int k3 = 150 - anIntArray982[k];
                        if (k3 < 50)
                            color = 0xff0000 + 5 * k3;
                        else if (k3 < 100)
                            color = 0xff00ff - 0x50000 * (k3 - 50);
                        else if (k3 < 150)
                            color = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                    }
                    if (anIntArray980[k] == 11) {
                        int l3 = 150 - anIntArray982[k];
                        if (l3 < 50)
                            color = 0xffffff - 0x50005 * l3;
                        else if (l3 < 100)
                            color = 65280 + 0x50005 * (l3 - 50);
                        else if (l3 < 150)
                            color = 0xffffff - 0x50000 * (l3 - 100);
                    }
                    if (chat_effects[k] == 0) {
                        bold.drawBasicStringCentered(s, spriteDrawX, spriteDrawY, color, 0);
                    }
                    if (chat_effects[k] == 1) {
                        bold.drawCenteredStringMoveY(s, spriteDrawX, spriteDrawY, color, 0, anInt1265);
                    }
                    if (chat_effects[k] == 2) {
                        bold.drawCenteredStringMoveXY(s, spriteDrawX, spriteDrawY, color, 0, anInt1265);
                    }
                    if (chat_effects[k] == 3) {
                        bold.drawStringMoveY(s, spriteDrawX, spriteDrawY, color, 0, anInt1265, 150 - anIntArray982[k]);
                    }
                    if (chat_effects[k] == 4) {
                        int i4 = bold.getBasicWidth(s);
                        int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
                        DrawingArea.setDrawingArea(spriteDrawX - 50, 0, spriteDrawX + 50, 334);
                        bold.drawString(s, (spriteDrawX + 50) - k4, spriteDrawY, color, 0);
                        DrawingArea.defaultDrawingAreaSize();
                    }
                    if (chat_effects[k] == 5) {
                        int j4 = 150 - anIntArray982[k];
                        int l4 = 0;
                        if (j4 < 25)
                            l4 = j4 - 25;
                        else if (j4 > 125)
                            l4 = j4 - 125;
                        DrawingArea.setDrawingArea(0, spriteDrawY - bold.baseCharacterHeight - 1, 512, spriteDrawY + 5);
                        bold.drawBasicStringCentered(s, spriteDrawX, spriteDrawY + l4, color, 0);
                        DrawingArea.defaultDrawingAreaSize();
                    }
                } else {
                    bold.drawBasicStringCentered(s, spriteDrawX, spriteDrawY, 0xffff00, 0);
                }
            }
        } catch (Exception e) {
        }
    }

    private void delFriend(long name) {
        if (name == 0L)
            return;
        for (int i = 0; i < friendsCount; i++) {
            if (friendsListAsLongs[i] != name)
                continue;
            friendsCount--;
            reDrawTabArea = true;
            for (int j = i; j < friendsCount; j++) {
                friendsList[j] = friendsList[j + 1];
                friendsNodeIDs[j] = friendsNodeIDs[j + 1];
                friendsListAsLongs[j] = friendsListAsLongs[j + 1];
            }

            stream.createFrame(215);
            stream.putLong(name);
            break;
        }
    }

    private void drawSideIcons(boolean fixed) {
        if (fixed) {
        	if (tabID != LOGOUT_TAB) {
	        	if (tabHover != -1 && tabHover != tabID && tabHover != LOGOUT_TAB) {//Draw hovered tab
	        		cacheSprite[16].drawSprite(FIXED_TAB_HOVER_X[tabHover], FIXED_TAB_HOVER_Y[tabHover]);
	        	}

	        	int xPos = FIXED_TAB_CLICK_X[tabID];
	        	int yPos = FIXED_TAB_CLICK_Y[tabID];
	        	cacheSprite[17].drawAdvancedSprite(xPos, yPos);//Draw the selected tab
        	}
            
            //Draw the tab icons
            for (int index = 0; index < 16; index++) {
            	if (tabInterfaceIDs[FIXED_SIDE_ICON_TAB_ID[index]] != -1) {
                    cacheSprite[FIXED_SIDE_ICON_ID[index]].drawSprite(FIXED_SIDE_ICON_POS_X[index], FIXED_SIDE_ICON_POS_Y[index]);
                }
            }
        } else {
            int offsetX;
            int offsetY;
        	if (showTab) {//Draw the selected tab
	            for (int index = 0; index < FIXED_SIDE_ICON_TAB_ID.length; index++) {
	                if (tabID == FIXED_SIDE_ICON_TAB_ID[index] && tabInterfaceIDs[FIXED_SIDE_ICON_TAB_ID[index]] != -1) {
		                offsetX = frameWidth >= smallTabs ? (index > 7 ? 245 : 485) : 245;
		                offsetY = frameWidth >= smallTabs ? 37 : (index > 7 ? 37 : 74);
	                    cacheSprite[17].drawAdvancedSprite(frameWidth - offsetX + RESIZABLE_TAB_POS_X[index], frameHeight - offsetY);
	                }
	            }
        	}

            //Draw the tab icons
            for (int index = 0; index < FIXED_SIDE_ICON_TAB_ID.length; index++) {
                if (tabInterfaceIDs[FIXED_SIDE_ICON_TAB_ID[index]] != -1) {
                    offsetX = frameWidth >= smallTabs ? (index > 7 ? 242 : 482) : 242;
                    offsetY = frameWidth >= smallTabs ? 37 : (index > 7 ? 37 : 74);
                    cacheSprite[RESIZABLE_SIDE_ICON_ID[index]].drawSprite(frameWidth - offsetX + RESIZABLE_SIDE_ICON_POS_X[index], frameHeight - offsetY + RESIZABLE_SIDE_ICON_POS_Y[index]);
                }
            }
        }
    }

    private void drawTabArea(boolean isFixed) {
        if (isFixed) {
            tabAreaProducer.initDrawingArea();
        }
        drawTabOverlay(isFixed);
        if (showTab) {
            int y = frameWidth >= smallTabs ? 37 : 74;
            if (invOverlayInterfaceID != -1) {
                Rasterizer.setDefaultBounds();
                drawInterface(0, (isFixed ? 33 : frameWidth - 197),
                        RSInterface.interfaceCache[invOverlayInterfaceID],
                        (isFixed ? 37 : frameHeight - y - 267), false);
            } else if (tabInterfaceIDs[tabID] != -1) {
                Rasterizer.setDefaultBounds();
                drawInterface(0, (isFixed ? 33 : frameWidth - 197),
                        RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
                        (isFixed ? 37 : frameHeight - y - 267), false);
            }
            if (isFixed) {
                if (MiniMenu.open) {
                    drawMenu(516, 168);
                } else {
                    drawTooltipHover(516, 168);
                }
            }
        }
        if (isFixed) {
            tabAreaProducer.drawGraphics(super.getGraphics(), 516, 168);
        }
    }

    private void method38() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1)
                j = myPlayerIndex;
            else
                j = playerIndices[i];
            Player player = playerArray[j];
            if (player != null && player.textCycle > 0) {
                player.textCycle--;
                if (player.textCycle == 0)
                    player.textSpoken = null;
            }
        }
        for (int k = 0; k < npcCount; k++) {
            int l = npcIndices[k];
            NPC npc = npcArray[l];
            if (npc != null && npc.textCycle > 0) {
                npc.textCycle--;
                if (npc.textCycle == 0)
                    npc.textSpoken = null;
            }
        }
    }

    private void calcCameraPos() {
        int i = anInt1098 * 128 + 64;
        int j = anInt1099 * 128 + 64;
        int k = getFloorDrawHeight(plane, j, i) - anInt1100;
        if (xCameraPos < i) {
            xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
            if (xCameraPos > i)
                xCameraPos = i;
        }
        if (xCameraPos > i) {
            xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
            if (xCameraPos < i)
                xCameraPos = i;
        }
        if (zCameraPos < k) {
            zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
            if (zCameraPos > k)
                zCameraPos = k;
        }
        if (zCameraPos > k) {
            zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
            if (zCameraPos < k)
                zCameraPos = k;
        }
        if (yCameraPos < j) {
            yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
            if (yCameraPos > j)
                yCameraPos = j;
        }
        if (yCameraPos > j) {
            yCameraPos -= anInt1101 + ((yCameraPos - j) * anInt1102) / 1000;
            if (yCameraPos < j)
                yCameraPos = j;
        }
        i = anInt995 * 128 + 64;
        j = anInt996 * 128 + 64;
        k = getFloorDrawHeight(plane, j, i) - anInt997;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 2607.5945876176133) & 0x3fff;
        int i2 = (int) (Math.atan2(l, j1) * -2607.5945876176133) & 0x3fff;
        if (l1 < 1024)
            l1 = 1024;
        if (l1 > 3072)
            l1 = 3072;
        if (yCameraCurve < l1) {
            yCameraCurve += anInt998 + ((l1 - yCameraCurve >> 3) * anInt999) / 1000 << 3;
            if (yCameraCurve > l1)
                yCameraCurve = l1;
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= anInt998 + ((yCameraCurve - l1 >> 3) * anInt999) / 1000 << 3;
            if (yCameraCurve < l1)
                yCameraCurve = l1;
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 8192)
            j2 -= 16384;
        if (j2 < -8192)
            j2 += 16384;
        
        j2 >>= 3;
        if (j2 > 0) {
            xCameraCurve += anInt998 + (j2 * anInt999) / 1000 << 3;
            xCameraCurve &= 0x3fff;
        }
        if (j2 < 0) {
            xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000 << 3;
            xCameraCurve &= 0x3fff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 8192)
            k2 -= 16384;
        if (k2 < -8192)
            k2 += 16384;
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
            xCameraCurve = i2;
    }

    private void drawMenu(int xOffSet, int yOffSet) {
        int xPos = menuOffsetX - xOffSet;
        int yPos = menuOffsetY - yOffSet;
        int menuW = menuWidth;
        int menuH = menuHeight;
        DrawingArea.fillRect(xPos, yPos + 2, menuW, menuH - 4, 0x706a5e);
        DrawingArea.fillRect(xPos + 1, yPos + 1, menuW - 2, menuH - 2, 0x706a5e);
        DrawingArea.fillRect(xPos + 2, yPos, menuW - 4, menuH, 0x706a5e);
        DrawingArea.fillRect(xPos + 3, yPos + 1, menuW - 6, menuH - 2, 0x2d2822);
        DrawingArea.fillRect(xPos + 2, yPos + 2, menuW - 4, menuH - 4, 0x2d2822);
        DrawingArea.fillRect(xPos + 1, yPos + 3, menuW - 2, menuH - 6, 0x2d2822);
        DrawingArea.fillRect(xPos + 2, yPos + 19, menuW - 4, menuH - 22, 0x524a3d);
        DrawingArea.fillRect(xPos + 3, yPos + 20, menuW - 6, menuH - 22, 0x524a3d);
        DrawingArea.fillRect(xPos + 3, yPos + 20, menuW - 6, menuH - 23, 0x2b271c);
        DrawingArea.drawRect(xPos + 3, yPos + 2, menuW - 6, 1, 0x2a291b);
        DrawingArea.drawRect(xPos + 2, yPos + 3, menuW - 4, 1, 0x2a261b);
        DrawingArea.drawRect(xPos + 2, yPos + 4, menuW - 4, 1, 0x252116);
        DrawingArea.drawRect(xPos + 2, yPos + 5, menuW - 4, 1, 0x211e15);
        DrawingArea.drawRect(xPos + 2, yPos + 6, menuW - 4, 1, 0x1e1b12);
        DrawingArea.drawRect(xPos + 2, yPos + 7, menuW - 4, 1, 0x1a170e);
        DrawingArea.drawRect(xPos + 2, yPos + 8, menuW - 4, 2, 0x15120b);
        DrawingArea.drawRect(xPos + 2, yPos + 10, menuW - 4, 1, 0x100d08);
        DrawingArea.drawRect(xPos + 2, yPos + 11, menuW - 4, 1, 0x090a04);
        DrawingArea.drawRect(xPos + 2, yPos + 12, menuW - 4, 1, 0x080703);
        DrawingArea.drawRect(xPos + 2, yPos + 13, menuW - 4, 1, 0x090a04);
        DrawingArea.drawRect(xPos + 2, yPos + 14, menuW - 4, 1, 0x070802);
        DrawingArea.drawRect(xPos + 2, yPos + 15, menuW - 4, 1, 0x090a04);
        DrawingArea.drawRect(xPos + 2, yPos + 16, menuW - 4, 1, 0x070802);
        DrawingArea.drawRect(xPos + 2, yPos + 17, menuW - 4, 1, 0x090a04);
        DrawingArea.drawRect(xPos + 2, yPos + 18, menuW - 4, 1, 0x2a291b);
        DrawingArea.drawRect(xPos + 3, yPos + 19, menuW - 6, 1, 0x564943);
        bold.drawString("Choose Option", xPos + 3, yPos + 14, 0xc6b895);
        int mouseX = super.mouseX - xOffSet;
        int mouseY = super.mouseY - yOffSet;
        int l1 = 0;
        for (MiniMenuOption menuOption = (MiniMenuOption) MiniMenu.options.reverseGetFirst(); menuOption != null; menuOption = (MiniMenuOption) MiniMenu.options.reverseGetNext()) {
            int textY = yPos + 31 + (MiniMenu.optionCount - 1 - l1) * 15;
            int disColor = 0xc6b895;
            if (mouseX > xPos && mouseX < xPos + menuW && mouseY > textY - 13 && mouseY < textY + 3) {
                DrawingArea.fillRect(xPos + 3, textY - 11, menuWidth - 6, 15, 0x6f695d);
                disColor = 0xeee5c6;
            }
            bold.drawString(MiniMenu.getOption(menuOption), xPos + 3, textY, disColor, 0);
            l1++;
        }
    }

    private void addFriend(long l) {
        if (l == 0L)
            return;
        if (friendsCount >= 100 && isMember != 1) {
            pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", ChatMessage.GAME_MESSAGE);
            return;
        }
        if (friendsCount >= 200) {
            pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", ChatMessage.GAME_MESSAGE);
            return;
        }
        String s = TextClass.fixName(TextClass.nameForLong(l));

        for (int i = 0; i < friendsCount; i++)
            if (friendsListAsLongs[i] == l) {
                pushMessage(s + " is already on your friend list", ChatMessage.GAME_MESSAGE);
                return;
            }
        for (int j = 0; j < ignoreCount; j++)
            if (ignoreListAsLongs[j] == l) {
                pushMessage("Please remove " + s + " from your ignore list first", ChatMessage.GAME_MESSAGE);
                return;
            }

        if (s.equals(myPlayer.name)) {
            return;
        }
        friendsList[friendsCount] = s;
        friendsListAsLongs[friendsCount] = l;
        friendsNodeIDs[friendsCount] = 0;
        friendsCount++;
        reDrawTabArea = true;
        stream.createFrame(188);
        stream.putLong(l);
    }

    public int getFloorDrawHeight(int level, int z, int x) {
        int tileX = x >> 7;
        int tileZ = z >> 7;
        if (tileX < 0 || tileZ < 0 || tileX > 103 || tileZ > 103) {
            return 0;
        }
        if (level < 3 && (tileSettingBits[1][tileX][tileZ] & 2) == 2) {
        	level++;
        }
        int k1 = x & 0x7f;
        int l1 = z & 0x7f;
        int i2 = intGroundArray[level][tileX][tileZ] * (128 - k1)
                + intGroundArray[level][tileX + 1][tileZ] * k1 >> 7;
        int j2 = intGroundArray[level][tileX][tileZ + 1] * (128 - k1)
                + intGroundArray[level][tileX + 1][tileZ + 1] * k1 >> 7;
        return i2 * (128 - l1) + j2 * l1 >> 7;
    }

    private void resetLogout() {
        try {
            if (socketStream != null)
                socketStream.close();
        } catch (Exception _ex) {
        }
        initFullscreenProducer();
        socketStream = null;
        loggedIn = false;
        loginMessage1 = "";
        loginMessage2 = "";
        clearCaches();
        stopMidi();
        currentSong = -1;
        nextSong = -1;
        prevSong = 0;
        messageCount = 0;
    }
    
    private void clearCaches() {
        unlinkMRUNodes();
        worldController.initToNull();
        for (int i = 0; i < 4; i++)
            tileSettings[i].init();
        System.gc();
    }

    private void method45() {
        aBoolean1031 = true;
        for (int j = 0; j < 7; j++) {
            anIntArray1065[j] = -1;
            for (int k = 0; k < IdentityKit.length; k++) {
                if (IdentityKit.cache[k].notSelectable
                        || IdentityKit.cache[k].bodyPartID != j
                        + (aBoolean1047 ? 0 : 7))
                    continue;
                anIntArray1065[j] = k;
                break;
            }
        }
    }

    private void networkReadNpcSpawnRequests(int i, Stream stream) {
        while (stream.bitPosition + 21 < i * 8) {
            int k = stream.readBits(14);
            if (k == 16383)
                break;
            if (npcArray[k] == null)
                npcArray[k] = new NPC();
            NPC npc = npcArray[k];
            npcIndices[npcCount++] = k;
            npc.time = loopCycle;
            int l = stream.readBits(5);
            if (l > 15)
                l -= 32;
            int i1 = stream.readBits(5);
            if (i1 > 15)
                i1 -= 32;
            int j1 = stream.readBits(1);
            npc.desc = NpcDefinition.forID(stream.readBits(14));
            int k1 = stream.readBits(1);
            if (k1 == 1)
                sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = k;
            npc.size = npc.desc.npcSize;
            npc.degreesToTurn = npc.desc.degreesToTurn;
            npc.walkAnimIndex = npc.desc.walkAnim;
            npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
            npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
            npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
            npc.standAnimIndex = npc.desc.standAnim;
            npc.setPos(myPlayer.smallX[0] + i1, myPlayer.smallY[0] + l, j1 == 1);
        }
        stream.finishBitAccess();
    }

    @Override
    public void mainLoop() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError)
            return;
        loopCycle++;

        checkSize();

        if (!loggedIn)
            processLoginScreenInput();
        else
            mainGameProcessor();

        processOnDemandQueue();
    }

    private void method47(boolean flag) {
        if (myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY)
            destX = 0;
        int j = playerCount;
        if (flag)
            j = 1;
        for (int l = 0; l < j; l++) {
            Player player;
            long i1;
            if (flag) {
                player = myPlayer;
                i1 = (long) myPlayerIndex << 32;
            } else {
                player = playerArray[playerIndices[l]];
                i1 = (long) playerIndices[l] << 32;
            }
            if (player == null || !player.isVisible()) {
                continue;
            }
            player.aBoolean1699 = playerCount > 200 && !flag && player.idleAnimId == player.standAnimIndex;
            int j1 = player.x >> 7;
            int k1 = player.y >> 7;
            if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
                continue;
            if (player.aModel_1714 != null && loopCycle >= player.anInt1707
                    && loopCycle < player.anInt1708) {
                player.aBoolean1699 = false;
                player.drawHeight = getFloorDrawHeight(plane, player.y,
                        player.x);
                worldController.method286(plane, player.y, player,
                        player.currentRotation, player.anInt1722, player.x,
                        player.drawHeight, player.anInt1719, player.anInt1721,
                        i1, player.anInt1720);
                continue;
            }
            if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                if (anIntArrayArray929[j1][k1] == anInt1265)
                    continue;
                anIntArrayArray929[j1][k1] = anInt1265;
            }
            player.drawHeight = getFloorDrawHeight(plane, player.y, player.x);
            worldController.addEntity(plane, player.currentRotation,
                    player.drawHeight, i1, player.y, 60, player.x, player,
                    player.aBoolean1541);
        }
    }

    private boolean promptUserForInput(RSInterface class9) {
        int j = class9.contentType;
        if (networkFriendsServerStatus == 2) {
            if (j == 201) {
                reDrawChatArea = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                chatActionID = 1;
                messagePromptText = "Enter name of friend to add to list";
            }
            if (j == 202) {
                reDrawChatArea = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                chatActionID = 2;
                messagePromptText = "Enter name of friend to delete from list";
            }
        }
        if (j == 205) {
            anInt1011 = 250;
            return true;
        }
        if (j == 501) {
            reDrawChatArea = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            chatActionID = 4;
            messagePromptText = "Enter name of player to add to list";
        }
        if (j == 502) {
            reDrawChatArea = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            chatActionID = 5;
            messagePromptText = "Enter name of player to delete from list";
        }
        if (j == 550) {
            RSInterface inter = RSInterface.interfaceCache[18135];
            if (inter.message.contains("Join")) {
                reDrawChatArea = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                clanChatAction = 1;
                messagePromptText = "Enter the name of the chat you wish to join";
            } else {
                inputString = "::leavecc";
                sendPacket(103);
            }
        }
        if (j == 1321) {
            reDrawChatArea = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            chatActionID = 12;
            messagePromptText = "Enter your target level";
        }
        if (j == 1322) {
            reDrawChatArea = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            chatActionID = 13;
            messagePromptText = "Enter your target experience";
        }
        if (j == 1323) {
            if (SkillConstants.selectedSkillId > -1) {
                SkillConstants.goalData[SkillConstants.selectedSkillId][0] = -1;
                SkillConstants.goalData[SkillConstants.selectedSkillId][1] = -1;
                SkillConstants.goalData[SkillConstants.selectedSkillId][2] = -1;
                saveGoals(myUsername);
            }
        }
        if (j >= 300 && j <= 313) {
            int k = (j - 300) / 2;
            int j1 = j & 1;
            int i2 = anIntArray1065[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0)
                        i2 = IdentityKit.length - 1;
                    if (j1 == 1 && ++i2 >= IdentityKit.length)
                        i2 = 0;
                } while (IdentityKit.cache[i2].notSelectable
                        || IdentityKit.cache[i2].bodyPartID != k
                        + (aBoolean1047 ? 0 : 7));
                anIntArray1065[k] = i2;
                aBoolean1031 = true;
            }
        }
        if (j >= 314 && j <= 323) {
            int l = (j - 314) / 2;
            int k1 = j & 1;
            int j2 = anIntArray990[l];
            if (k1 == 0 && --j2 < 0)
                j2 = anIntArrayArray1003[l].length - 1;
            if (k1 == 1 && ++j2 >= anIntArrayArray1003[l].length)
                j2 = 0;
            anIntArray990[l] = j2;
            aBoolean1031 = true;
        }
        if (j == 324 && !aBoolean1047) {
            aBoolean1047 = true;
            method45();
        }
        if (j == 325 && aBoolean1047) {
            aBoolean1047 = false;
            method45();
        }
        if (j == 326) {
            stream.createFrame(101);
            stream.putByte(aBoolean1047 ? 0 : 1);
            for (int i1 = 0; i1 < 7; i1++) {
                stream.putShort(anIntArray1065[i1]);
            }
            for (int l1 = 0; l1 < 5; l1++) {
                stream.putShort(anIntArray990[l1]);
            }
            return true;
        }
        if (j == 613)
            canMute = !canMute;
        if (j >= 601 && j <= 612) {
            clearTopInterfaces();
            if (reportAbuseInput.length() > 0) {
                stream.createFrame(218);
                stream.putLong(TextClass.longForName(reportAbuseInput));
                stream.putByte(j - 601);
                stream.putByte(canMute ? 1 : 0);
            }
        }
        return false;
    }

    private void method49(Stream stream) {
        for (int j = 0; j < sessionNpcsAwaitingUpdatePtr; j++) {
            int k = sessionNpcsAwaitingUpdate[j];
            Player player = playerArray[k];
            int l = stream.readUnsignedByte();
            if ((l & 0x40) != 0)
                l += stream.readUnsignedByte() << 8;
            method107(l, k, stream, player);
        }
    }

    private void drawMapScenes(int y, int k, int x, int i1, int z) {
        long uid = worldController.method300(z, x, y);
        if (uid != 0) {
            int direction = (int) (uid >> 20) & 0x3;
            int type = (int) (uid >> 14) & 0x1f;
            int k3 = k;
            if (uid > 0)
                k3 = i1;
            int mapPixels[] = minimapImage.myPixels;
            int pixels = 24624 + x * 4 + (103 - y) * 512 * 4;
            int objectId = (int) (uid >>> 32) & 0x7fffffff;
            ObjectDefinition objDefinition_2 = ObjectDefinition.forID(objectId);
            if (objDefinition_2.mapSceneID == -1) {
                if (type == 0 || type == 2) {
                    if (direction == 0) {
                        mapPixels[pixels] = k3;
                        mapPixels[pixels + 512] = k3;
                        mapPixels[1024 + pixels] = k3;
                        mapPixels[1536 + pixels] = k3;
                    } else if (direction == 1) {
                        mapPixels[pixels] = k3;
                        mapPixels[pixels + 1] = k3;
                        mapPixels[pixels + 2] = k3;
                        mapPixels[3 + pixels] = k3;
                    } else if (direction == 2) {
                        mapPixels[pixels - -3] = k3;
                        mapPixels[3 + (pixels + 512)] = k3;
                        mapPixels[3 + (pixels + 1024)] = k3;
                        mapPixels[1536 + (pixels - -3)] = k3;
                    } else if (direction == 3) {
                        mapPixels[pixels + 1536] = k3;
                        mapPixels[pixels + 1536 + 1] = k3;
                        mapPixels[2 + pixels + 1536] = k3;
                        mapPixels[pixels + 1539] = k3;
                    }
                }
                if (type == 3)
                    if (direction == 0)
                        mapPixels[pixels] = k3;
                    else if (direction == 1)
                        mapPixels[pixels + 3] = k3;
                    else if (direction == 2)
                        mapPixels[pixels + 3 + 1536] = k3;
                    else if (direction == 3)
                        mapPixels[pixels + 1536] = k3;
                if (type == 2)
                    if (direction == 3) {
                        mapPixels[pixels] = k3;
                        mapPixels[pixels + 512] = k3;
                        mapPixels[pixels + 1024] = k3;
                        mapPixels[pixels + 1536] = k3;
                    } else if (direction == 0) {
                        mapPixels[pixels] = k3;
                        mapPixels[pixels + 1] = k3;
                        mapPixels[pixels + 2] = k3;
                        mapPixels[pixels + 3] = k3;
                    } else if (direction == 1) {
                        mapPixels[pixels + 3] = k3;
                        mapPixels[pixels + 3 + 512] = k3;
                        mapPixels[pixels + 3 + 1024] = k3;
                        mapPixels[pixels + 3 + 1536] = k3;
                    } else if (direction == 2) {
                        mapPixels[pixels + 1536] = k3;
                        mapPixels[pixels + 1536 + 1] = k3;
                        mapPixels[pixels + 1536 + 2] = k3;
                        mapPixels[pixels + 1536 + 3] = k3;
                    }
            }
        }
        uid = worldController.method302(z, x, y);
        if (uid != 0) {
            int direction = (int) (uid >> 20) & 0x3;
            int type = (int) (uid >> 14) & 0x1f;
            int objectId = (int) (uid >>> 32) & 0x7fffffff;
            ObjectDefinition objDefinition_1 = ObjectDefinition.forID(objectId);
            if (objDefinition_1.mapSceneID != -1) {
                Background background_1 = mapScenes[objDefinition_1.mapSceneID];
                if (background_1 != null) {
                    int j5 = (objDefinition_1.width * 4 - background_1.imgWidth) / 2;
                    int k5 = (objDefinition_1.height * 4 - background_1.anInt1453) / 2;
                    background_1.drawBackground(48 + x * 4 + j5, 48
                            + (104 - y - objDefinition_1.height) * 4 + k5);
                }
            } else if (type == 9) {
                int l4 = 0xeeeeee;
                if (uid > 0)
                    l4 = 0xee0000;
                int ai1[] = minimapImage.myPixels;
                int l5 = 24624 + x * 4 + (103 - y) * 512 * 4;
                if (direction == 0 || direction == 2) {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        uid = worldController.method303(z, x, y);
        if (uid != 0) {
            int objectId = (int) (uid >>> 32) & 0x7fffffff;
            ObjectDefinition objDefinition = ObjectDefinition.forID(objectId);
            if (objDefinition.mapSceneID != -1) {
                Background background = mapScenes[objDefinition.mapSceneID];
                if (background != null) {
                    int i4 = (objDefinition.width * 4 - background.imgWidth) / 2;
                    int j4 = (objDefinition.height * 4 - background.anInt1453) / 2;
                    background.drawBackground(48 + x * 4 + i4, 48
                            + (104 - y - objDefinition.height) * 4 + j4);
                }
            }
        }
    }

	private void drawTextOnScreen(String... texts) {
		gameScreen.initDrawingArea();
		int x = 10;
		int y = 10;
		int boxWidth = 0;
		int boxHeight = texts.length * 13;
		for (String text : texts) {
			int width = regular.getBasicWidth(text);
			if (boxWidth < width) {
				boxWidth = width;
			}
		}
		DrawingArea.fillRect(x - 4, y - 4, boxWidth + 8, boxHeight + 8, 0);
		DrawingArea.drawRect(x - 4, y - 4, boxWidth + 8, boxHeight + 8, 16777215);
		for (String text : texts) {
			regular.drawString(text, x, y + 10, 16777215, 0);
			y += 13;
		}
		boolean isFixed = clientSize == 0;
		gameScreen.drawGraphics(super.getGraphics(), isFixed ? 4 : 0, isFixed ? 4 : 0);
	}

    private void loadingStages() {
        if (lowMem && loadingStage == 2 && ObjectManager.anInt131 != plane) {
        	drawTextOnScreen("Loading - please wait.");
            loadingStage = 1;
            aLong824 = System.currentTimeMillis();
        }
        if (loadingStage == 1) {
            int j = method54();
            if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
                signlink.reporterror(myUsername + " glcfb " + aLong1215 + ","
                        + j + "," + lowMem + "," + decompressors[0] + ","
                        + onDemandFetcher.getNodeCount() + "," + plane + ","
                        + anInt1069 + "," + anInt1070);
                aLong824 = System.currentTimeMillis();
            }
        }
        if (loadingStage == 2 && plane != anInt985) {
            anInt985 = plane;
            renderMapScene(plane);
        }
    }

    private int method54() {
        for (int i = 0; i < terrainData.length; i++) {
            if (terrainData[i] == null && terrainIndices[i] != -1)
                return -1;
            if (aByteArrayArray1247[i] == null && anIntArray1236[i] != -1)
                return -2;
        }
        /*for (int i = 0; i < terrainData.length; i++) {
        	int x = mapCoordinates[i] >> 8;
        	int z = mapCoordinates[i] & 0xff;
        	writePackFile("l"+x+"_"+z, aByteArrayArray1247[i], 0, aByteArrayArray1247[i].length);
        	writePackFile("m"+x+"_"+z, terrainData[i], 0, terrainData[i].length);
        }*/
        boolean flag = true;
        for (int j = 0; j < terrainData.length; j++) {
            byte abyte0[] = aByteArrayArray1247[j];
            if (abyte0 != null) {
                int k = (mapCoordinates[j] >> 8) * 64 - baseX;
                int l = (mapCoordinates[j] & 0xff) * 64 - baseY;
                if (loadGeneratedMap) {
                    k = 10;
                    l = 10;
                }
                flag &= ObjectManager.isObjectBlockCached(k, abyte0, l);
            }
        }
        if (!flag)
            return -3;
        if (aBoolean1080) {
            return -4;
        } else {
            loadingStage = 2;
            ObjectManager.anInt131 = plane;
            loadRegion();
            stream.createFrame(121);
            return 0;
        }
    }

    private void method55() {
        for (Projectile class30_sub2_sub4_sub4 = (Projectile) projectileNode
                .reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (Projectile) projectileNode
                .reverseGetNext())
            if (class30_sub2_sub4_sub4.plane != plane
                    || loopCycle > class30_sub2_sub4_sub4.speedTime)
                class30_sub2_sub4_sub4.unlink();
            else if (loopCycle >= class30_sub2_sub4_sub4.delayTime) {
                if (class30_sub2_sub4_sub4.lockOn > 0) {
                    NPC npc = npcArray[class30_sub2_sub4_sub4.lockOn - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312
                            && npc.y >= 0 && npc.y < 13312)
                        class30_sub2_sub4_sub4.method455(
                                loopCycle,
                                npc.y,
                                getFloorDrawHeight(
                                        class30_sub2_sub4_sub4.plane, npc.y,
                                        npc.x)
                                        - class30_sub2_sub4_sub4.engHeight,
                                npc.x
                        );
                }
                if (class30_sub2_sub4_sub4.lockOn < 0) {
                    int j = -class30_sub2_sub4_sub4.lockOn - 1;
                    Player player;
                    if (j == playerID)
                        player = myPlayer;
                    else
                        player = playerArray[j];
                    if (player != null && player.x >= 0 && player.x < 13312
                            && player.y >= 0 && player.y < 13312)
                        class30_sub2_sub4_sub4.method455(
                                loopCycle,
                                player.y,
                                getFloorDrawHeight(
                                        class30_sub2_sub4_sub4.plane, player.y,
                                        player.x)
                                        - class30_sub2_sub4_sub4.engHeight,
                                player.x
                        );
                }
                class30_sub2_sub4_sub4.method456(anInt945);
                worldController.addEntity(plane,
                        class30_sub2_sub4_sub4.anInt1595,
                        (int) class30_sub2_sub4_sub4.aDouble1587, -1,
                        (int) class30_sub2_sub4_sub4.aDouble1586, 60,
                        (int) class30_sub2_sub4_sub4.aDouble1585,
                        class30_sub2_sub4_sub4, false);
            }

    }

    @Override
    public AppletContext getAppletContext() {
    	return super.getAppletContext();
    }

    private void processOnDemandQueue() {
		do {
			OnDemandData onDemandData = onDemandFetcher.getNextNode();
			if (onDemandData == null) {
				return;
			}

			if (onDemandData.dataType == 0 || onDemandData.dataType == 5) {
				Model.method460(onDemandData.buffer, onDemandData.ID);
			} else if (onDemandData.dataType == 1) {
				FrameReader.load(onDemandData.ID, onDemandData.buffer);
			} else if (onDemandData.dataType == 2 && onDemandData.ID == nextSong && onDemandData.buffer != null) {
				saveMidi(songChanging, onDemandData.buffer);
			} else if (onDemandData.dataType == 3 && loadingStage == 1) {
				for (int i = 0; i < terrainData.length; i++) {
					if (terrainIndices[i] == onDemandData.ID) {
						terrainData[i] = onDemandData.buffer;
						if (onDemandData.buffer == null)
							terrainIndices[i] = -1;
						break;
					}
					if (anIntArray1236[i] != onDemandData.ID)
						continue;
					aByteArrayArray1247[i] = onDemandData.buffer;
					if (onDemandData.buffer == null)
						anIntArray1236[i] = -1;
					break;
				}
			} else if (onDemandData.dataType == 4) {
				Texture.load(onDemandData.ID, onDemandData.buffer);
			} else if (onDemandData.dataType == 93 && onDemandFetcher.method564(onDemandData.ID)) {
				ObjectManager.method173(new Stream(onDemandData.buffer), onDemandFetcher);
			}
		} while (true);
    }

    private boolean saveWave(byte abyte0[], int i) {
        return abyte0 == null;
    }

    private void loadInterface(int i) {
        RSInterface class9 = RSInterface.interfaceCache[i];
        if (class9 == null || class9.children == null) {
            System.out.println(i + " is null");
            return;
        }

        for (int j = 0; j < class9.children.length; j++) {
            if (class9.children[j] == -1)
                break;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
            if (class9_1.type == 1)
                loadInterface(class9_1.id);
            class9_1.anInt246 = 0;
            class9_1.anInt208 = 0;
        }
    }

    private void drawHeadIcon() {
        if (headiconDrawType != 2)
            return;


        if (headiconHeight != plane) {
            return;
        }

        calcEntityScreenPos((headiconX - baseX << 7) + arrowDrawTileX, headiconHeight * 2, (headiconY - baseY << 7) + arrowDrawTileY);
        if (spriteDrawX > -1 && loopCycle % 20 < 10)
            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
    }

    private void mainGameProcessor() {
        if (systemUpdateTime > 1)
            systemUpdateTime--;
        if (anInt1011 > 0)
            anInt1011--;
        for (int j = 0; j < 5; j++)
            if (!parsePacket())
                break;

        if (!loggedIn)
            return;
        if (super.clickMode3 != 0) {
            long l = (super.aLong29 - aLong1220) / 50L;
            if (l > 4095L)
                l = 4095L;
            aLong1220 = super.aLong29;
            int k2 = super.saveClickY;
            if (k2 < 0)
                k2 = 0;
            else if (k2 > 502)
                k2 = 502;
            int k3 = super.saveClickX;
            if (k3 < 0)
                k3 = 0;
            else if (k3 > 764)
                k3 = 764;
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if (super.clickMode3 == 2)
                j5 = 1;
            int l5 = (int) l;
            stream.createFrame(241);
            stream.putInt((l5 << 20) + (j5 << 19) + k4);
        }
        if (anInt1016 > 0)
            anInt1016--;
        if (super.keyArray[1] == 1 || super.keyArray[2] == 1
                || super.keyArray[3] == 1 || super.keyArray[4] == 1)
            aBoolean1017 = true;
        if (aBoolean1017 && anInt1016 <= 0) {
            anInt1016 = 20;
            aBoolean1017 = false;
            stream.createFrame(86);
            stream.putShort(anInt1184);
            stream.writeSignedShortA(minimapInt1);
        }
        if (super.awtFocus && !aBoolean954) {
            aBoolean954 = true;
            stream.createFrame(3);
            stream.putByte(1);
        }
        if (!super.awtFocus && aBoolean954) {
            aBoolean954 = false;
            stream.createFrame(3);
            stream.putByte(0);
        }
        loadingStages();
        method115();
        method90();
        anInt1009++;
        if (anInt1009 > 750)
            dropClient();
        method114();
        method95();
        method38();
        anInt945++;
        if (crossType != 0) {
            crossIndex += 20;
            if (crossIndex >= 400)
                crossType = 0;
        }
        if (atInventoryInterfaceType != 0) {
            atInventoryLoopCycle++;
            if (atInventoryLoopCycle >= 15) {
                if (atInventoryInterfaceType == 2)
                    reDrawTabArea = true;
                if (atInventoryInterfaceType == 3)
                    reDrawChatArea = true;
                atInventoryInterfaceType = 0;
            }
        }
        if (activeInterfaceType != 0) {
            anInt989++;
            if (super.mouseX > anInt1087 + 5 || super.mouseX < anInt1087 - 5
                    || super.mouseY > anInt1088 + 5
                    || super.mouseY < anInt1088 - 5)
                aBoolean1242 = true;

            if (super.clickMode2 == 0) {
                if (activeInterfaceType == 2)
                    reDrawTabArea = true;
                if (activeInterfaceType == 3)
                    reDrawChatArea = true;
                activeInterfaceType = 0;
                if (aBoolean1242 && anInt989 >= 10) {
                    lastActiveInvInterface = -1;
                    processRightClick(clientSize == 0);

                    Point southWest, northEast;

                    if (clientSize == 0) {
                        southWest = new Point(26, 73);
                        northEast = new Point(71, 40);
                    } else {

                        int w = 512, h = 334;
                        int x = clientSize == 0 ? 0 : (frameWidth / 2) - 256;
                        int y = clientSize == 0 ? 0 : (frameHeight / 2) - 167;
                        int count = 3;
                        if (clientSize != 0) {
                            for (int i = 0; i < count; i++) {
                                if (x + w > (frameWidth - 225)) {
                                    x = x - 30;
                                    if (x < 0) {
                                        x = 0;
                                    }
                                }
                                if (y + h > (frameHeight - 182)) {
                                    y = y - 30;
                                    if (y < 0) {
                                        y = 0;
                                    }
                                }
                            }
                        }

                        int xOffset = x + 26;
                        int yOffset = y + 22;

                        southWest = new Point(xOffset, yOffset + 34);
                        northEast = new Point(xOffset + 117, yOffset);
                    }

                    int[] slots = new int[BANK_TAB_IDS.length];

                    for (int i = 0; i < slots.length; i++) {
                        slots[i] = (47 * i) + (int) southWest.getX();
                    }

                    for (int i = 0; i < slots.length; i++) {
                        if ((selectedItemInterfaceID >= 15292 && selectedItemInterfaceID <= 15300) && (super.mouseY >= northEast.getY()) && (super.mouseY <= southWest.getY())) {
                            if ((super.mouseX >= slots[i]) && (super.mouseX <= (slots[i] + 41))) {
                                stream.createFrame(214);
                                stream.writeSignedShortBigEndianA(selectedItemInterfaceID);
                                stream.method424(0);
                                stream.writeSignedShortBigEndianA(selectedItemSlot);
                                stream.method431(mouseInvInterfaceIndex);
                                stream.putInt(BANK_TAB_IDS[i]);
                            }
                        }
                    }

                    // moving items in the main tab
                    if (openInterfaceID == 5292 && lastActiveInvInterface != selectedItemInterfaceID) {
                        if (lastActiveInvInterface >= 15292 && lastActiveInvInterface <= 15301 && selectedItemInterfaceID >= 15292 && selectedItemInterfaceID <= 15301) {

                            RSInterface class9 = RSInterface.interfaceCache[selectedItemInterfaceID];
                            int[] item1 = {class9.inv[selectedItemSlot], class9.invStackSizes[selectedItemSlot]};

                            class9 = RSInterface.interfaceCache[lastActiveInvInterface];
                            int[] item2 = {class9.inv[mouseInvInterfaceIndex], class9.invStackSizes[mouseInvInterfaceIndex]};

                            class9.inv[mouseInvInterfaceIndex] = item1[0];
                            class9.invStackSizes[mouseInvInterfaceIndex] = item1[1];

                            class9 = RSInterface.interfaceCache[selectedItemInterfaceID];
                            class9.inv[selectedItemSlot] = item2[0];
                            class9.invStackSizes[selectedItemSlot] = item2[1];

                            stream.createFrame(214);
                            stream.writeSignedShortBigEndianA(selectedItemInterfaceID);
                            stream.method424(0);
                            stream.writeSignedShortBigEndianA(selectedItemSlot);
                            stream.method431(mouseInvInterfaceIndex);
                            stream.putInt(lastActiveInvInterface);
                        }
                    } else if (lastActiveInvInterface == selectedItemInterfaceID && mouseInvInterfaceIndex != selectedItemSlot) {

                        RSInterface class9 = RSInterface.interfaceCache[selectedItemInterfaceID];
                        int j1 = 0;
                        if (anInt913 == 1 && class9.contentType == 206) {
                            j1 = 1;
                        }
                        if (class9.inv[mouseInvInterfaceIndex] <= 0)
                            j1 = 0;
                        if (class9.itemsDraggable) {
                            int l2 = selectedItemSlot;
                            int l3 = mouseInvInterfaceIndex;
                            class9.inv[l3] = class9.inv[l2];
                            class9.invStackSizes[l3] = class9.invStackSizes[l2];
                            class9.inv[l2] = -1;
                            class9.invStackSizes[l2] = 0;
                        } else if (j1 == 1) {
                            int i3 = selectedItemSlot;
                            for (int i4 = mouseInvInterfaceIndex; i3 != i4; )
                                if (i3 > i4) {
                                    class9.swapInventoryItems(i3, i3 - 1);
                                    i3--;
                                } else if (i3 < i4) {
                                    class9.swapInventoryItems(i3, i3 + 1);
                                    i3++;
                                }
                        } else {
                            class9.swapInventoryItems(selectedItemSlot, mouseInvInterfaceIndex);
                        }
                        stream.createFrame(214);
                        stream.writeSignedShortBigEndianA(selectedItemInterfaceID);
                        stream.method424(j1);
                        stream.writeSignedShortBigEndianA(selectedItemSlot);
                        stream.method431(mouseInvInterfaceIndex);
                        stream.putInt(0);
                    }
                } else if ((anInt1253 == 1 || menuHasAddFriend())
                        && MiniMenu.optionCount > 2)
                    determineMenuSize();
                else if (MiniMenu.optionCount > 0) {
                    doAction((MiniMenuOption) MiniMenu.options.head.next);
                }
                atInventoryLoopCycle = 10;
                super.clickMode3 = 0;
            }
        }
        if (WorldController.clickedTileX != -1) {
            int k = WorldController.clickedTileX;
            int k1 = WorldController.clickedTileY;
            if (myPrivilege >= 2 && teleportClick) {
                inputString = "::tele " + (baseX + k) + " " + (k1 + baseY) + " " + plane;
                sendPacket(103);
            } else {
                boolean flag = doWalkTo(0, 0, 0, 0, myPlayer.smallY[0], 0, 0, k1,
                        myPlayer.smallX[0], true, k);
                WorldController.clickedTileX = -1;
                if (flag) {
                    crossX = super.saveClickX;
                    crossY = super.saveClickY;
                    crossType = 1;
                    crossIndex = 0;
                }
            }
        }
        if (super.clickMode3 == 1 && aString844 != null) {
            aString844 = null;
            reDrawChatArea = true;
            super.clickMode3 = 0;
        }
        if (!processMenuClick()) {
        	boolean isFixed = clientSize == 0;
            processMainScreenClick(isFixed);
            processChatModeClick(isFixed);
            processMapAreaMouse(isFixed);
        }
        if (super.clickMode2 == 1 || super.clickMode3 == 1)
            anInt1213++;
        if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
            if (anInt1501 < 50 && !MiniMenu.open) {
                anInt1501++;
                if (anInt1501 == 50) {
                    if (anInt1500 != 0) {
                        reDrawChatArea = true;
                    }
                    if (anInt1044 != 0) {
                    }
                }
            }
        } else if (anInt1501 > 0) {
            anInt1501--;
        }
        if (loadingStage == 2)
            method108();
        if (loadingStage == 2 && inCutscene)
            calcCameraPos();
        for (int i1 = 0; i1 < 5; i1++)
            cameraTransVars2[i1]++;

        method73();
        super.idleTime++;
        if (super.idleTime > 10000) {
            anInt1011 = 250;
            super.idleTime -= 500;
            stream.createFrame(202);
        }
        anInt1010++;
        if (anInt1010 > 50)
            stream.createFrame(0);
        try {
            if (socketStream != null && stream.currentOffset > 0) {
                socketStream.queueBytes(stream.currentOffset, stream.buffer);
                stream.currentOffset = 0;
                anInt1010 = 0;
            }
        } catch (IOException _ex) {
            dropClient();
        } catch (Exception exception) {
            resetLogout();
        }
    }

    private void clearObjectSpawnRequests() {
        GameObjectSpawnRequest class30_sub1 = (GameObjectSpawnRequest) aClass19_1179
                .reverseGetFirst();
        for (; class30_sub1 != null; class30_sub1 = (GameObjectSpawnRequest) aClass19_1179
                .reverseGetNext()) {
            if (class30_sub1.anInt1294 == -1) {
                class30_sub1.anInt1302 = 0;
                method89(class30_sub1);
            } else {
                class30_sub1.unlink();
            }
        }
    }

    private void initTitleScreen() {
        if (titleScreenInitialized) {
            return;
        }
        chatAreaProducer = null;
        minimapProducer = null;
        tabAreaProducer = null;
        gameScreen = null;
        fullRedraw = true;
        titleScreenInitialized = true;

        System.gc();
    }

    public void drawLoadingText(int percent, String text) {

        anInt1079 = percent;
        aString1049 = text;
        initTitleScreen();
        if (titleStreamLoader == null) {
            super.drawLoadingText(percent, text);
            return;
        }
        int centerX = frameWidth / 2, centerY = frameHeight / 2;
        percent = percent * 2;

        fullGameScreen.initDrawingArea();
        cacheSprite[338].drawSprite(centerX - cacheSprite[338].myWidth / 2, centerY - cacheSprite[338].myHeight / 2);
        cacheSprite[337].drawAdvancedSprite(centerX - cacheSprite[337].myWidth / 2 - 10, centerY - cacheSprite[337].myHeight / 2 - 150);
        cacheSprite[336].drawSprite(centerX - cacheSprite[336].myWidth / 2, centerY);
        cacheSprite[339].drawSprite(centerX - cacheSprite[339].myWidth / 2, centerY + 25);
        DrawingArea.fillRect((286 + percent), centerY + 25, (194 - percent), 13, 0x302e2c);
        regular.drawBasicStringCentered(text + " - " + percent / 2 + "%", 387, centerY + 20, 0xffffff);
        fullGameScreen.drawGraphics(super.getGraphics(), 0, 0);
        if (fullRedraw) {
        	fullRedraw = false;
        }
    }

    public void processMapAreaMouse(boolean isFixed) {
        if (mouseInRegion(frameWidth - (isFixed ? 249 : 217), isFixed ? 46 : 3, frameWidth - (isFixed ? 249 : 217) + 34, (isFixed ? 46 : 3) + 34)) {
            hoverPos = 0;
        } else if (mouseInRegion(isFixed ? frameWidth - 58 : getOrbX(1, isFixed), getOrbY(1, isFixed), (isFixed ? frameWidth - 58 : getOrbX(1, isFixed)) + 57, getOrbY(1, isFixed) + 34)) {
            hoverPos = 1;
        } else if (mouseInRegion(isFixed ? frameWidth - 58 : getOrbX(2, isFixed), getOrbY(2, isFixed), (isFixed ? frameWidth - 58 : getOrbX(2, isFixed)) + 57, getOrbY(2, isFixed) + 34)) {
            hoverPos = 2;
        } else if (mouseInRegion(isFixed ? frameWidth - 74 : getOrbX(3, isFixed), getOrbY(3, isFixed), (isFixed ? frameWidth - 74 : getOrbX(3, isFixed)) + 57, getOrbY(3, isFixed) + 34)) {
            hoverPos = 3;
        } else {
            hoverPos = -1;
        }
    }

    private void method65(int i, int j, int k, int l, RSInterface class9, int i1, boolean flag, int j1, int rotation, boolean newScroller) {
        int anInt992;
        if (aBoolean972)
            anInt992 = 32;
        else
            anInt992 = 0;
        aBoolean972 = false;

        if (rotation == 0) {
            rotation = notches;
            notches = 0;
        }
        if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
            class9.scrollPosition -= anInt1213 * 4;
            if (flag) {
                reDrawTabArea = true;
            }
        } else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
            class9.scrollPosition += anInt1213 * 4;
            if (flag) {
                reDrawTabArea = true;
            }
        } else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && anInt1213 > 0) {
            int l1 = ((j - 32) * j) / j1;
            if (l1 < 8)
                l1 = 8;
            int i2 = l - i1 - 16 - l1 / 2;
            int j2 = j - 32 - l1;
            class9.scrollPosition = ((j1 - j) * i2) / j2;
            if (flag)
                reDrawTabArea = true;
            aBoolean972 = true;
        } else if(rotation != 0) {
            class9.scrollPosition += rotation * 30;
            if (flag) {
                reDrawTabArea = true;
            }
        }
    }

    private boolean method66(long i, int j, int k) {
        int k1 = (int) (i >> 14) & 0x1f;
        int l1 = (int) (i >> 20) & 0x3;
        if (k1 == 10 || k1 == 11 || k1 == 22) {
            ObjectDefinition class46 = ObjectDefinition.forID((int) (i >>> 32) & 0x7fffffff);
            int i2;
            int j2;
            if (l1 == 0 || l1 == 2) {
                i2 = class46.width;
                j2 = class46.height;
            } else {
                i2 = class46.height;
                j2 = class46.width;
            }
            int k2 = class46.surroundings;
            if (l1 != 0)
                k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
            doWalkTo(2, 0, j2, 0, myPlayer.smallY[0], i2, k2, j, myPlayer.smallX[0], false, k);
        } else {
            doWalkTo(2, l1, 0, k1 + 1, myPlayer.smallY[0], 0, 0, j, myPlayer.smallX[0], false, k);
        }
        crossX = super.saveClickX;
        crossY = super.saveClickY;
        crossType = 2;
        crossIndex = 0;
        return true;
    }

	private StreamLoader streamLoaderForName(int i, String s, String s1, int j,
			int k) {
		byte abyte0[] = null;
		int l = 5;
		try {
			if (decompressors[0] != null)
				abyte0 = decompressors[0].decompress(i);
		} catch (Exception _ex) {
		}
		if (abyte0 != null) {
			// aCRC32_930.reset();
			// aCRC32_930.update(abyte0);
			// int i1 = (int)aCRC32_930.getValue();
			// if(i1 != j)
		}
		if (abyte0 != null) {
			StreamLoader streamLoader = new StreamLoader(abyte0);
			return streamLoader;
		}
		int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			drawLoadingText(k, "Requesting " + s);
			Object obj = null;
			try {
				int k1 = 0;
				DataInputStream datainputstream = openJagGrabInputStream(s1 + j);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Stream stream = new Stream(abyte1);
				stream.currentOffset = 3;
				int i2 = stream.read3Bytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1)
						drawLoadingText(k, "Loading " + s + " - " + k3 + "%");
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (decompressors[0] != null)
						decompressors[0].pack(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					decompressors[0] = null;
				}
				/*
				 * if(abyte0 != null) { aCRC32_930.reset();
				 * aCRC32_930.update(abyte0); int i3 =
				 * (int)aCRC32_930.getValue(); if(i3 != j) { abyte0 = null;
				 * j1++; s2 = "Checksum error: " + i3; } }
				 */
			} catch (IOException ioexception) {
			ioexception.printStackTrace();
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else {
						drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				l *= 2;
				if (l > 60)
					l = 60;
			}

		}

		StreamLoader streamLoader_1 = new StreamLoader(abyte0);
		return streamLoader_1;
	}

    private void dropClient() {
        if (anInt1011 > 0) {
            resetLogout();
            return;
        }
        drawTextOnScreen("Connection lost", "Please wait - attempting to reestablish.");
        minimapLock = 0;
        destX = 0;
        RSSocket rsSocket = socketStream;
        loggedIn = false;
        loginFailures = 0;
        login(myUsername, myPassword, true);
        if (!loggedIn)
            resetLogout();
        try {
            rsSocket.close();
        } catch (Exception _ex) {
        }
    }

    private void sendRecolorIndex(int slot, int recol) {
    	stream.createFrame(69);
    	stream.putByte(slot);
    	stream.putShort(recol);
    }

	public void sendPacket185(int componentId) {
		stream.createFrame(185);
		if (componentId >= 25000 && componentId <= 25050) {
			int index = componentId - 25000;
			if (index > 0) {
				index = index / 2;
			}
			stream.putShort(QuickPrayers.OLD_PRAYER_INTERFACE_IDS[index]);
		} else {
			stream.putShort(componentId);
		}
		RSInterface component = RSInterface.interfaceCache[componentId];
		if (component.valueIndexArray != null && component.valueIndexArray[0][0] == 5) {
			int value = component.valueIndexArray[0][1];
			playerVariables[value] = 1 - playerVariables[value];
			postVarpChange(value);
			reDrawTabArea = true;
			switch (componentId) {
			case 22004:
				if (playerVariables[value] == 0) {
					messagePromptRaised = false;
				} else {
					messagePromptRaised = true;
					messagePromptText = "Show items whose names contain the following text:";
					promptInput = "";
					chatActionID = 11;
					reDrawChatArea = true;
				}
				break;
			case 152:
				if (playerVariables[value] == 1) {
					QuickPrayers.quickPrayersActive = true;
				} else {
					QuickPrayers.quickPrayersActive = false;
				}
				break;
			}
		}
	}

    private void setAutoCastOff() {
        autoCast = false;
        autocastId = 0;
        sendPacket185(6666);
        pushMessage("Autocast spell cleared.", ChatMessage.GAME_MESSAGE);
    }

    private void doAction(MiniMenuOption miniMenuOption) {
        if (miniMenuOption == null || MiniMenu.options.head == miniMenuOption) {
            return;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            reDrawChatArea = true;
        }
        int j = miniMenuOption.cmd2;
        int k = miniMenuOption.cmd3;
        int actionId = miniMenuOption.uid;
        int i1 = (int) miniMenuOption.cmd1;
        long i12 = miniMenuOption.cmd1;

        if (actionId >= 2000)
            actionId -= 2000;

        if (actionId == 1513) { // follower details
            tabID = SUMMONING_TAB;
            reDrawTabArea = true;
        }
        if (actionId == 712) {
            coinToggle = !coinToggle;
        }
        if (actionId == 713) {
            messagePromptRaised = false;
            inputDialogState = 4;
            amountOrNameInput = "";
            reDrawChatArea = true;
        }

        if (actionId == 868) { // withdraw 100
                       /* if ((i1 & 3) == 0)
                anInt1175++;
            if (anInt1175 >= 59) {
                stream.createFrame(200);
                stream.writeWord(25501);
                anInt1175 = 0;
           }
            stream.createFrame(43);
            stream.method431(k);
            stream.method432(i1);
            stream.method432(j);*/

            stream.createFrame(209);
            stream.method431(j); // slot
            stream.writeSignedShortA(k); // interface id
            stream.method431(i1); // item i
            stream.putInt(100); // amount

            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }

        if (actionId == 432) { // withdraw all but one

                       /* if ((i1 & 3) == 0)
+                anInt1175++;
+            if (anInt1175 >= 59) {
+                stream.createFrame(200);
+                stream.writeWord(25501);
+                anInt1175 = 0;
+            }
+            stream.createFrame(43);
+            stream.method431(k);
+            stream.method432(i1);
+            stream.method432(j);*/

            stream.createFrame(209);
            stream.method431(j); // slot
            stream.writeSignedShortA(k); // interface id
            stream.method431(i1); // item i
            stream.putInt(RSInterface.interfaceCache[k].invStackSizes[j] - 1); // amount

            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }

        if (actionId == 433) { // withdraw last x amount
                       /* if ((i1 & 3) == 0)
                anInt1175++;
            if (anInt1175 >= 59) {
                stream.createFrame(200);
                stream.writeWord(25501);
                anInt1175 = 0;
            }
            stream.createFrame(43);
            stream.method431(k);
            stream.method432(i1);
           stream.method432(j);*/

            stream.createFrame(209);
            stream.method431(j); // slot
            stream.writeSignedShortA(k); // interface id
            stream.method431(i1); // item i
            stream.putInt(withdrawAmount); // amount

            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }

        if (actionId == 434) { // withdraw all shops
                       /* if ((i1 & 3) == 0)
                anInt1175++;
            if (anInt1175 >= 59) {
                stream.createFrame(200);
                stream.writeWord(25501);
                anInt1175 = 0;
            }
            stream.createFrame(43);
            stream.method431(k);
            stream.method432(i1);
           stream.method432(j);*/

            stream.createFrame(209);
            stream.method431(j); // slot
            stream.writeSignedShortA(k); // interface id
            stream.method431(i1); // item i
            stream.putInt(Integer.MAX_VALUE); // amount

            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }

        System.out.println("action: " + actionId);

        // send ge search
        if (actionId == 1251 || actionId == 24700) {
            stream.createFrame(204);
            stream.putShort(GEItemId);
        }

        // -- Plank making
        if (actionId == 1152) {

            switch (j) {
                case 999: // All
                    stream.createFrame(129);
                    stream.writeSignedShortA(0); // -- slot
                    stream.putShort(i1); // interface id
                    stream.writeSignedShortA(0); // -- 'Item' id
                    break;
                case 99: // X
                    stream.createFrame(135);
                    stream.method431(0); // -- Slot
                    stream.writeSignedShortA(i1); // -- Interface ID
                    stream.method431(0); // -- Stuff ID
                    break;
                case 1:
                    stream.createFrame(145);
                    stream.writeSignedShortA(i1); // -- Component ID
                    stream.writeSignedShortA(0); // -- 'Item' Slot
                    stream.writeSignedShortA(0); // item id
                    break;
                case 5:
                    stream.createFrame(117);
                    stream.writeSignedShortBigEndianA(i1); // -- Component ID
                    stream.writeSignedShortBigEndianA(0); // -- 'Item' Slot
                    stream.method431(0); // item id
                    break;
                case 10:
                    stream.createFrame(43);
                    stream.method431(i1); // -- Component ID
                    stream.writeSignedShortA(0); // -- 'Item' Slot
                    stream.method431(0); // item id
                    break;
            }
        }
        if (actionId == 582) {
            NPC npc = npcArray[i1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, npc.smallY[0],
                        myPlayer.smallX[0], false, npc.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(57);
                stream.writeSignedShortA(anInt1285);
                stream.writeSignedShortA(i1);
                stream.method431(anInt1283);
                stream.writeSignedShortA(anInt1284);
            }
        }
        if (actionId == 104) {
            RSInterface rsInterface = RSInterface.interfaceCache[k];
            targetInterfaceId = rsInterface.id;
            if (!interfaceIsSelected(rsInterface)) {
                pushMessage("You need the correct Magic level and rune requirements to use this spell.", ChatMessage.GAME_MESSAGE);
            } else {
                if (!autoCast || (autocastId != targetInterfaceId)) {
                    autoCast = true;
                    autocastId = targetInterfaceId;
                    sendPacket185(autocastId);
                    pushMessage("Autocast spell selected.", ChatMessage.GAME_MESSAGE);
                } else if (autocastId == targetInterfaceId) {
                    setAutoCastOff();
                }
            }
        }

        if (actionId == 234) {
            boolean flag1 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag1)
                flag1 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(236);
            stream.method431(k + baseY);
            stream.putShort(i1);
            stream.method431(j + baseX);
        }
        if (actionId == 62 && method66(i12, k, j)) {
            stream.createFrame(192);
            stream.putShort(anInt1284);
            stream.putShort((int) ((i12 >>> 32) & 0x7fffffff));
            stream.writeSignedShortBigEndianA(k + baseY);
            stream.method431(anInt1283);
            stream.writeSignedShortBigEndianA(j + baseX);
            stream.putShort(anInt1285);
        }
        if (actionId == 511) {
            boolean flag2 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag2)
                flag2 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(25);
            stream.method431(anInt1284);
            stream.writeSignedShortA(anInt1285);
            stream.putShort(i1);
            stream.writeSignedShortA(k + baseY);
            stream.writeSignedShortBigEndianA(anInt1283);
            stream.putShort(j + baseX);
        }

        if (actionId == 315) {
            RSInterface class9 = RSInterface.interfaceCache[k];
            boolean flag8 = true;
            if (class9.contentType > 0) {
                if ((class9.contentType == 1321) || (class9.contentType == 1322) || (class9.contentType == 1323)) {
                    int index = class9.id - 79924;
                    if (index >= 50) {
                        index -= 50;
                    }
                    if (index >= 25) {
                        index -= 25;
                    }
                    SkillConstants.selectedSkillId = SkillConstants.skillIdNames(skillNames[index]);
                }
                flag8 = promptUserForInput(class9);
            }
            if (k >= 17202 && k <= 17228 || k == 15891) {
            	QuickPrayers.togglePrayerState(k);
            }
            if (flag8) {
                switch (k) {
                /* Client-sided button clicking */
                    case 17231:// Quick prayr confirm
                    	QuickPrayers.swapToMainPrayerInterface();
                        break;
                    case 40943: // fixed
                        toggleSize(0, false);
                        break;
                    case 40946: // resizable
                        toggleSize(1, false);
                        break;
                    case 40949: // fullscreen
                        toggleSize(2, false);
                        break;
                    case 15201:
                        sendFrame248(15106, 3213);
                        loadInterface(15106);
                        reDrawChatArea = true;
                        break;
                    case 5881:
                        sendFrame248(5885, 3213); // report abuse new
                        loadInterface(5875);
                        reDrawChatArea = true;
                        break;
                    case 59163:
                        clearTopInterfaces();
                        break;
                    case 40004: // brightnes, zoom in/out, fullscreen
                        sendFrame248(40030, 3213);
                        loadInterface(15106);
                        reDrawChatArea = true;
                        break;
                    case 40105: // audio etc
                        // sendFrame248(49911, 3213);
                        reDrawChatArea = true;
                        break;
                    case 1668:
                        sendFrame248(17500, 3213);
                        break;
                    case 15252:
                        reDrawChatArea = true;
                        inputDialogState = 0;
                        messagePromptRaised = true;
                        promptInput = "";
                        clanChatAction = 2;
                        messagePromptText = "Enter the new name for your clan chat";
                        break;
                    case 17241:
                        tabInterfaceIDs[5] = tabInterfaceIDs[5] == 22000 ? 22500 : 5608;
                        Settings.write();
                        break;
                    case 941:
                        cameraZoom = 600;
                        break;
                    case 942:
                        cameraZoom = 550;
                        break;
                    case 943:
                        cameraZoom = 500;
                        break;
                    case 944:
                        cameraZoom = 450;
                        break;
                    case 945:
                        cameraZoom = 400;
                        break;
                    default:
                        stream.createFrame(185);
                        stream.putShort(k);


                        // opens grand exchange item search
                        if (k == 24654) {
                            inputDialogState = 3;
                            amountOrNameInput = "";

                            totalItemResults = 0;
                        }

                        // resets scroll when changing tabs
                        if (k >= 22024 && k <= 22032) {
                            RSInterface.interfaceCache[5385].scrollPosition = 0;
                        }


                        System.out.println("k: " + k);

                        if (RSInterface.teleportPages.containsKey(k)) {
                            RSInterface rsInterface = RSInterface.interfaceCache[53399];
                            rsInterface.children[17] = RSInterface.teleportPages.get(k);
                        }
                        switch (k) {
							case 25406:
								sendRecolorIndex(customizingSlot, clickedColorPaletteIndex);
								break;

                            /**
                             * loyalty
                             */
                            case 19887:
                                RSInterface.interfaceCache[19887].sprite1 = loyalty2;
                                RSInterface.interfaceCache[19888].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19889].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19890].textColor = 0xff991f;
                                RSInterface.interfaceCache[19891].textColor = 0x8c5000;
                                RSInterface.interfaceCache[19892].textColor = 0x8c5000;

                                RSInterface.interfaceCache[19882].children[10] = RSInterface.skillingInterfaceId;
                                break;
                            case 19888:
                                RSInterface.interfaceCache[19887].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19888].sprite1 = loyalty2;
                                RSInterface.interfaceCache[19889].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19890].textColor = 0x8c5000;
                                RSInterface.interfaceCache[19891].textColor = 0xff991f;
                                RSInterface.interfaceCache[19892].textColor = 0x8c5000;

                                RSInterface.interfaceCache[19882].children[10] = RSInterface.pkInterfaceId;
                                break;
                            case 19889:
                                RSInterface.interfaceCache[19887].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19888].sprite1 = loyalty1;
                                RSInterface.interfaceCache[19889].sprite1 = loyalty2;
                                RSInterface.interfaceCache[19890].textColor = 0x8c5000;
                                RSInterface.interfaceCache[19891].textColor = 0x8c5000;
                                RSInterface.interfaceCache[19892].textColor = 0xff991f;

                                RSInterface.interfaceCache[19882].children[10] = RSInterface.miscInterfaceId;
                                break;
                            /**
                             * teleport
                             */


                            case 53404:
                                RSInterface rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 53425;
                                break;
                            case 53407: // slayer button
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 53585;
                                break;
                            case 53410: // skilling button
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 53706;
                                break;
                            case 53413: // pkin teleport
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 53842;
                                break;
                            case 53416: // minigames
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 53959;
                                break;
                            case 53419: // bossing
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 54095;
                                break;
                            case 53422: // donator
                                rsInterface = RSInterface.interfaceCache[53399];
                                rsInterface.children[17] = 19023;
                                break;

                        }
                        break;
                }
            }
        }
        if (actionId == 474) {
            counterOn = !counterOn;
        }
        if (actionId == 1014) {
            setNorth();
        }
        if (actionId == 475) {
            xpCounter = 0;

            stream.createFrame(185);
            stream.putShort(1);
        }

        if (actionId == 476) {
            stream.createFrame(185);
            stream.putShort(2);
        }

        if (actionId >= 1506 && actionId <= 1512) {
            stream.createFrame(185);
            stream.putShort(42568 + (actionId - 1506));
        }

        if (actionId == 1500) {
            if (stats[5] > 0) {
                QuickPrayers.handleQuickAidsActive();
            } else {
                pushMessage("You need to recharge your Prayer at an alter.", ChatMessage.GAME_MESSAGE);
            }
        }

        if (actionId == 1505) {
            if (!QuickPrayers.resettedQuickPrayers) {
            	QuickPrayers.turnOffPrayers(false);
            	QuickPrayers.turnOffPrayers(true);
            	QuickPrayers.turnOffQuickPrayerPreset();
            	QuickPrayers.configureQuickTicks();
                QuickPrayers.resettedQuickPrayers = true;
            }
            QuickPrayers.swapPrayerInterfaces();
        }

        if (actionId == 561) {
            Player player = playerArray[i1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        player.smallY[0], myPlayer.smallX[0], false,
                        player.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(128);
                stream.putShort(i1);
            }
        }
        if (actionId == 20) {
            NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_1 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_1.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub1_1.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(155);
                stream.method431(i1);
            }
        }
        if (actionId == 779) {
            Player class30_sub2_sub4_sub1_sub2_1 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_1 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_1.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub2_1.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(153);
                stream.method431(i1);
            }
        }
		if (actionId == 516) {
			int x;
			int y;
			if (!MiniMenu.open) {
				x = saveClickX;
				y = saveClickY;
			} else {
				x = j;
				y = k;
			}
			if (clientSize == 0) {
				x -= 4;
				y -= 4;
			}
			worldController.request2DTrace(x, y);
		}
        if (actionId == 1062) {
            method66(i12, k, j);
            stream.createFrame(228);
            stream.writeSignedShortA((int) ((i12 >>> 32) & 0x7fffffff));
            stream.writeSignedShortA(k + baseY);
            stream.putShort(j + baseX);
        }
        if (actionId == 679 && !isDialogueInterface) {
            stream.createFrame(40);
            stream.putShort(k);
            isDialogueInterface = true;
        }
        if (actionId == 431) {
            stream.createFrame(129);
            stream.writeSignedShortA(j);
            stream.putShort(k);
            stream.writeSignedShortA(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 337 || actionId == 42 || actionId == 792 || actionId == 322) {
            String s = removeMarkup(miniMenuOption.base);

            long l3 = TextClass.longForName(s);
            if (actionId == 337) {
                addFriend(l3);
            } else if (actionId == 42) {
                addIgnore(l3);
            } else if (actionId == 792) {
                delFriend(l3);
            } else if (actionId == 322) {
                delIgnore(l3);
            }
        }
        if (actionId == 53) {
            stream.createFrame(135);
            stream.method431(j);
            stream.writeSignedShortA(k);
            stream.method431(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 539) {
            stream.createFrame(16);
            stream.writeSignedShortA(i1);
            stream.writeSignedShortBigEndianA(j);
            stream.writeSignedShortBigEndianA(k);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 493) {
            stream.createFrame(75);
            stream.writeSignedShortBigEndianA(k);
            stream.method431(j);
            stream.writeSignedShortA(i1);

            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 74) {
            stream.createFrame(122);
            stream.writeSignedShortBigEndianA(k);
            stream.writeSignedShortA(j);
            stream.method431(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 484 || actionId == 6) {
            String s1 = removeMarkup(miniMenuOption.base);
            String s7 = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(s1)));
            boolean flag9 = false;
            for (int j3 = 0; j3 < playerCount; j3++) {
                Player class30_sub2_sub4_sub1_sub2_7 = playerArray[playerIndices[j3]];
                if (class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7))
                    continue;
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
                if (actionId == 484) {
                    stream.createFrame(139);
                    stream.method431(playerIndices[j3]);
                }
                if (actionId == 6) {
                    stream.createFrame(128);
                    stream.putShort(playerIndices[j3]);
                }
                flag9 = true;
                break;
            }

            if (!flag9)
                pushMessage("Unable to find " + s7, ChatMessage.GAME_MESSAGE);
        }
        if (actionId == 870) {
            stream.createFrame(53);
            stream.putShort(j);
            stream.writeSignedShortA(anInt1283);
            stream.writeSignedShortBigEndianA(i1);
            stream.putShort(anInt1284);
            stream.method431(anInt1285);
            stream.putShort(k);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 847) {
            stream.createFrame(87);
            stream.writeSignedShortA(i1);
            stream.putShort(k);
            stream.writeSignedShortA(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 626) {
            RSInterface class9_1 = RSInterface.interfaceCache[k];
            targetInterfaceId = class9_1.id;
            targetBitMask = class9_1.targetBitMask;
            itemSelected = 0;
            reDrawTabArea = true;
            String verbPrefix = class9_1.targetVerb;
            if (verbPrefix.indexOf(" ") != -1)
                verbPrefix = verbPrefix.substring(0, verbPrefix.indexOf(" "));
            String verbSuffix = class9_1.targetVerb;
            if (verbSuffix.indexOf(" ") != -1)
                verbSuffix = verbSuffix.substring(verbSuffix.indexOf(" ") + 1);
            targetOption = verbPrefix + " " + class9_1.targetName + " " + verbSuffix;
            if (targetBitMask == 16) {
                setTab(INVENTORY_TAB);
            }
            return;
        }
        if (actionId == 78) {
            stream.createFrame(117);
            stream.writeSignedShortBigEndianA(k);
            stream.writeSignedShortBigEndianA(i1);
            stream.method431(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 27) {
            Player player = playerArray[i1];
            if (player != null) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(73);
                stream.method431(i1);
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        player.smallY[0],
                        myPlayer.smallX[0], false,
                        player.smallX[0]);
            }
        }
        if (actionId == 213) {
            boolean flag3 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag3)
                flag3 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(79);
            stream.method431(k + baseY);
            stream.putShort(i1);
            stream.writeSignedShortA(j + baseX);
        }
        if (actionId == 632) {
            stream.createFrame(145);
            stream.writeSignedShortA(k);
            stream.writeSignedShortA(j);
            stream.writeSignedShortA(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }

        if (actionId == 1051) {
            if (playerVariables[173] == 0) {
            	playerVariables[173] = 1;
                stream.createFrame(185);
                stream.putShort(153);
            } else {
            	playerVariables[173] = 0;
                stream.createFrame(185);
                stream.putShort(152);
            }
        }

        if (actionId == 1052) {
            stream.createFrame(185);
            stream.putShort(154);
        }
        if (actionId == 1003) {
            clanChatMode = 2;
            reDrawChatArea = true;
        }
        if (actionId == 1002) {
            clanChatMode = 1;
            reDrawChatArea = true;
        }
        if (actionId == 1001) {
            clanChatMode = 0;
            reDrawChatArea = true;
        }
        if (actionId == 1000) {
        	chatButtonClicked(11);
            cButtonCPos = 4;
        }
        if (actionId == 999) {
        	chatButtonClicked(0);
            cButtonCPos = 0;
        }
        if (actionId == 998) {
        	chatButtonClicked(5);
            cButtonCPos = 1;
        }
        if (actionId == 997) {
            publicChatMode = 3;
            reDrawChatArea = true;
        }
        if (actionId == 996) {
            publicChatMode = 2;
            reDrawChatArea = true;
        }
        if (actionId == 995) {
            publicChatMode = 1;
            reDrawChatArea = true;
        }
        if (actionId == 994) {
            publicChatMode = 0;
            reDrawChatArea = true;
        }
        if (actionId == 993) {
        	chatButtonClicked(1);
            cButtonCPos = 2;
        }
        if (actionId == 992) {
            privateChatMode = 2;
            reDrawChatArea = true;
        }
        if (actionId == 991) {
            privateChatMode = 1;
            reDrawChatArea = true;
        }
        if (actionId == 990) {
            privateChatMode = 0;
            reDrawChatArea = true;
        }
        if (actionId == 989) {
        	chatButtonClicked(2);
            cButtonCPos = 3;
        }
        if (actionId == 987) {
            tradeChatMode = 2;
            reDrawChatArea = true;
        }
        if (actionId == 986) {
            tradeChatMode = 1;
            reDrawChatArea = true;
        }
        if (actionId == 985) {
            tradeChatMode = 0;
            reDrawChatArea = true;
        }
        if (actionId == 984) {
        	chatButtonClicked(3);
            cButtonCPos = 5;
        }
        if (actionId == 983) {
            challengeChatMode = 2;
            reDrawChatArea = true;
        }
        if (actionId == 982) {
            challengeChatMode = 1;
            reDrawChatArea = true;
        }
        if (actionId == 981) {
            challengeChatMode = 0;
            reDrawChatArea = true;
        }
        if (actionId == 980) {
        	chatButtonClicked(6);
            cButtonCPos = 6;
        }
        if (actionId == 652) {
            boolean flag4 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag4)
                flag4 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(156);
            stream.writeSignedShortA(j + baseX);
            stream.method431(k + baseY);
            stream.writeSignedShortBigEndianA(i1);
        }
        if (actionId == 94) {
            boolean flag5 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag5)
                flag5 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(181);
            stream.method431(k + baseY);
            stream.putShort(i1);
            stream.method431(j + baseX);
            stream.writeSignedShortA(targetInterfaceId);
        }
        if (actionId == 646) {
            switch (k) {
                //clan chat
                case 18129:
                    if (RSInterface.interfaceCache[18135].message.toLowerCase().contains("join")) {
                        reDrawChatArea = true;
                        inputDialogState = 0;
                        messagePromptRaised = true;
                        promptInput = "";
                        chatActionID = 6;
                        messagePromptText = "Enter the name of the chat you wish to join";
                    } else {
                        sendString(0, "");
                    }
                    break;
                case 18132:
                    openInterfaceID = 18300;
                    break;
                case 18526:
                    reDrawChatArea = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    chatActionID = 9;
                    messagePromptText = "Enter a name to add";
                    break;
                case 18527:
                    reDrawChatArea = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    chatActionID = 10;
                    messagePromptText = "Enter a name to add";
                    break;
            }

            stream.createFrame(185);
            stream.putShort(k);

            int config = 0;
            RSInterface class9_2 = RSInterface.interfaceCache[k];
            if (class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
                config = class9_2.valueIndexArray[0][1];
                if (playerVariables[config] != class9_2.requiredValues[0]) {
                    playerVariables[config] = class9_2.requiredValues[0];
                    postVarpChange(config);
                    reDrawTabArea = true;
                }
            }


            if (config > 0) {
                switch (config) {
                    case 586: // fixed
                        toggleSize(0, false);
                        break;
                    case 587: // resizable
                        toggleSize(1, false);
                        break;
                    case 588: // fullscreen
                        toggleSize(2, false);
                        break;
                    //   case 927:
                    //      tabInterfaceIDs[11] = 33144;
                    //       break;
                    // ld
                    case 599:
                        Settings.detail = Constants.DETAIL_LD;
                        Settings.playerShadow = false;
                        Settings.npcShadow = false;
                        Settings.tweening = false;
                        Settings.fog = false;
                        Settings.minimapShading = false;
                        ObjectManager.disableGroundTextures = true;
                        Settings.write();
                        appendDetails();
                        loadRegion();
                        break;
                    // sd
                    case 600:
                        Settings.detail = Constants.DETAIL_SD;
                        Settings.playerShadow = false;
                        Settings.npcShadow = false;
                        Settings.tweening = false;
                        Settings.fog = false;
                        Settings.minimapShading = false;
                        ObjectManager.disableGroundTextures = true;
                        Settings.write();
                        appendDetails();
                        loadRegion();
                        break;
                    // hd
                    case 601:
                        Settings.detail = Constants.DETAIL_HD;
                        Settings.playerShadow = true;
                        Settings.npcShadow = true;
                        Settings.tweening = true;
                        Settings.fog = true;
                        Settings.minimapShading = true;
                        ObjectManager.disableGroundTextures = false;
                        Settings.write();
                        appendDetails();
                        loadRegion();
                        break;

                }
            }

            switch (k) {
                case 33155:
                    if (inHouse) {
                        if (buildingMode != true) {
                            buildingMode = true;
                            stream.createFrame(185);
                            stream.putShort(1337);
                        }
                    } else {
                        pushMessage("You need to be in your house to do this.", ChatMessage.GAME_MESSAGE);
                    }
                    break;
                case 33154:
                    if (inHouse) {
                        if (buildingMode != false) {
                            buildingMode = false;
                            stream.createFrame(185);
                            stream.putShort(1337);
                        }
                    } else {
                        pushMessage("You need to be in your house to do this.", ChatMessage.GAME_MESSAGE);
                    }
                    break;
                case 13999: // Toggle-able tab
                    sendFrame36(175, Settings.newHitNumbers ? 0 : 1);
                    // sendFrame36(176, Constants.newHitmasks ? 0 : 1);
                    sendFrame36(177, Settings.newHpBar ? 0 : 1);
                    sendFrame36(178, Settings.playerShadow ? 0 : 1);
                    sendFrame36(180, Settings.fog ? 0 : 1);
                    sendFrame36(179, Settings.mouseHover ? 0 : 1);
                    sendFrame36(181, Settings.npcShadow ? 0 : 1);
                    sendFrame36(183, Settings.tweening ? 0 : 1);
                    sendFrame36(182, Settings.minimapShading ? 0 : 1);
                    sendFrame248(35010, 3213);
                    reDrawChatArea = true;
                    break;
                // 10x hitpoints
                case 15252:
                    reDrawChatArea = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    clanChatAction = 2;
                    messagePromptText = "Enter the new name for your clan chat";
                    break;
                case 54357:
                    Settings.newHitNumbers = !Settings.newHitNumbers;
                    sendFrame36(175, Settings.newHitNumbers ? 0 : 1);
                    Settings.write();
                    break;
                // new hitmarks
                case 54358:
                    int frame = 35056;
                    Settings.hitmarks = Settings.hitmarks + 1 & 0x3;
                    if (Settings.hitmarks >= 3) {
                    	Settings.hitmarks = 0;
                    }
                    if (Settings.hitmarks == Constants.HITMARKS_562) {
                        sendFrame126("Hitmarks: 562", frame);
                    } else if (Settings.hitmarks == Constants.HITMARKS_634) {
                        sendFrame126("Hitmarks: 634", frame);
                    } else if (Settings.hitmarks == Constants.HITMARKS_317) {
                        sendFrame126("Hitmarks: 317", frame);
                    }
                    Settings.write();
                    break;
                // new hpbar
                case 54359:
                    Settings.newHpBar = !Settings.newHpBar;
                    sendFrame36(177, Settings.newHpBar ? 0 : 1);
                    Settings.write();
                    break;
                // player shadows
                case 54360:
                    Settings.playerShadow = !Settings.playerShadow;
                    sendFrame36(178, Settings.playerShadow ? 0 : 1);
                    Settings.write();
                    Player.modelCache.unlinkAll();
                    break;
                // fog
                case 54362:
                    Settings.fog = !Settings.fog;
                    sendFrame36(180, Settings.fog ? 0 : 1);
                    Settings.write();
                    break;
                // mouse hovers
                case 54361:
                    Settings.mouseHover = !Settings.mouseHover;
                    sendFrame36(179, Settings.mouseHover ? 0 : 1);
                    Settings.write();
                    break;
                // npc shadows
                case 54363:
                    Settings.npcShadow = !Settings.npcShadow;
                    sendFrame36(181, Settings.npcShadow ? 0 : 1);
                    Settings.write();
                    loadRegion();
                    break;
                // minimap shadng
                case 54364:
                    Settings.minimapShading = !Settings.minimapShading;
                    sendFrame36(182, Settings.minimapShading ? 0 : 1);
                    loadRegion();
                    Settings.write();
                    break;

                // tweening
                case 55375:
                    Settings.tweening = !Settings.tweening;
                    sendFrame36(183, Settings.tweening ? 0 : 1);
                    Settings.write();
                    break;

            }

        }

        //clan chat
        if (actionId == 647) {
            stream.createFrame(213);
            stream.putShort(k);
            stream.putShort(j);
            switch (k) {
                case 18304:
                    if (j == 0) {
                        reDrawChatArea = true;
                        inputDialogState = 0;
                        messagePromptRaised = true;
                        promptInput = "";
                        chatActionID = 8;
                        messagePromptText = "Enter your clan chat title";
                    }
                    break;
            }
        }
        if (actionId == 225) {
            NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_2 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_2.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub1_2.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(17);
                stream.writeSignedShortBigEndianA(i1);
            }
        }
        if (actionId == 965) {
            NPC class30_sub2_sub4_sub1_sub1_3 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_3 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_3.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub1_3.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(21);
                stream.putShort(i1);
            }
        }
        if (actionId == 413) {
            NPC class30_sub2_sub4_sub1_sub1_4 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_4 != null) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(131);
                stream.writeSignedShortBigEndianA(i1);
                stream.putInt(targetInterfaceId);
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_4.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub1_4.smallX[0]);
            }
        }
        if (actionId == 200) {
            if (openInterfaceID == 5292 && playerVariables[0] == 1) {
                playerVariables[0] = 0;
                messagePromptRaised = false;
            }
            clearTopInterfaces();
        }
        if (actionId == 692) {//Cape customizing option
        	openInterfaceID = 25400;
			customizingSlot = k;
        }
        if (actionId == 1025) {
            NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_5 != null) {
                NpcDefinition npcDef = class30_sub2_sub4_sub1_sub1_5.desc;
                if (npcDef.childrenIDs != null)
                    npcDef = npcDef.morph();
                if (npcDef != null) {
                    String s9;
                    if (npcDef.description != null)
                        s9 = new String(npcDef.description);
                    else
                        s9 = "It's a " + npcDef.name + ".";
                    pushMessage(s9, ChatMessage.GAME_MESSAGE);
                }
            }
        }
        if (actionId == 900) {
            method66(i12, k, j);
            stream.createFrame(252);
            stream.writeSignedShortBigEndianA((int) ((i12 >>> 32) & 0x7fffffff));
            stream.method431(k + baseY);
            stream.writeSignedShortA(j + baseX);
        }
        if (actionId == 412) {
            NPC class30_sub2_sub4_sub1_sub1_6 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_6 != null) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(72);
                stream.writeSignedShortA(i1);

                // 5th option hack
                if (class30_sub2_sub4_sub1_sub1_6.desc.type != 9085) {
                    doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                            class30_sub2_sub4_sub1_sub1_6.smallY[0],
                            myPlayer.smallX[0], false,
                            class30_sub2_sub4_sub1_sub1_6.smallX[0]);
                }
            }
        }
        if (actionId == 365) {
            Player class30_sub2_sub4_sub1_sub2_3 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_3 != null) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(249);
                stream.writeSignedShortA(i1);
                stream.putInt(targetInterfaceId);
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_3.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub2_3.smallX[0]);
            }
        }
        if (actionId == 1076) {
        	if (clientSize != 0) {
				if (tabID == i1) {
					showTab = !showTab;
				} else {
					showTab = true;
				}
        	}
        	setTab(i1);
        }
        if (actionId == 729) {
            Player class30_sub2_sub4_sub1_sub2_4 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_4 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_4.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub2_4.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(39);
                stream.method431(i1);
            }
        }
        if (actionId == 577) {
            Player class30_sub2_sub4_sub1_sub2_5 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_5 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_5.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub2_5.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(139);
                stream.method431(i1);
            }
        }
        if (actionId == 956 && method66(i12, k, j)) {
            stream.createFrame(35);
            stream.method431(j + baseX);
            stream.writeSignedShortA(targetInterfaceId);
            stream.writeSignedShortA(k + baseY);
            stream.method431((int) ((i12 >>> 32) & 0x7fffffff));
        }
        if (actionId == 567) {
            boolean flag6 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag6)
                flag6 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(23);
            stream.method431(k + baseY);
            stream.method431(i1);
            stream.method431(j + baseX);
        }
        if (actionId == 867) {
            stream.createFrame(43);
            stream.method431(k);
            stream.writeSignedShortA(i1);
            stream.writeSignedShortA(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 543) {
            stream.createFrame(237);
            stream.putShort(j);
            stream.writeSignedShortA(i1);
            stream.putShort(k);
            stream.writeSignedShortA(targetInterfaceId);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 606) {
            String s2 = removeMarkup(miniMenuOption.base);
            if (openInterfaceID == -1) {
                clearTopInterfaces();
                reportAbuseInput = s2;
                canMute = false;
                for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
                    if (RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600)
                        continue;
                    reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
                    break;
                }

            } else {
                pushMessage("Please close the interface you have open before using 'report abuse'", ChatMessage.GAME_MESSAGE);
            }
        }
        if (actionId == 491) {
            Player class30_sub2_sub4_sub1_sub2_6 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_6 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub2_6.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub2_6.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(14);
                stream.writeSignedShortA(anInt1284);
                stream.putShort(i1);
                stream.putShort(anInt1285);
                stream.method431(anInt1283);
            }
        }
        if (actionId == 639) {
            String s3 = removeMarkup(miniMenuOption.base);
            long l4 = TextClass.longForName(s3);
            int k3 = -1;
            for (int i4 = 0; i4 < friendsCount; i4++) {
                if (friendsListAsLongs[i4] != l4)
                    continue;
                k3 = i4;
                break;
            }

            if (k3 != -1 && friendsNodeIDs[k3] > 0) {
                reDrawChatArea = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                chatActionID = 3;
                aLong953 = friendsListAsLongs[k3];
                messagePromptText = "Enter message to send to " + friendsList[k3];
            }
        }
        if (actionId == 454) {
            stream.createFrame(41);
            stream.putShort(i1);
            stream.writeSignedShortA(j);
            stream.writeSignedShortA(k);
            atInventoryLoopCycle = 0;
            atInventoryInterface = k;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
                atInventoryInterfaceType = 1;
            if (RSInterface.interfaceCache[k].parentID == backDialogID)
                atInventoryInterfaceType = 3;
        }
        if (actionId == 478) {
            NPC class30_sub2_sub4_sub1_sub1_7 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_7 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0,
                        class30_sub2_sub4_sub1_sub1_7.smallY[0],
                        myPlayer.smallX[0], false,
                        class30_sub2_sub4_sub1_sub1_7.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(18);
                stream.method431(i1);
            }
        }
        if (actionId == 113) {
            method66(i12, k, j);
            stream.createFrame(70);
            stream.method431(j + baseX);
            stream.putShort(k + baseY);
            stream.writeSignedShortBigEndianA((int) ((i12 >>> 32) & 0x7fffffff));
        }
        if (actionId == 872) {
            method66(i12, k, j);
            stream.createFrame(234);
            stream.writeSignedShortBigEndianA(j + baseX);
            stream.writeSignedShortA((int) ((i12 >>> 32) & 0x7fffffff));
            stream.writeSignedShortBigEndianA(k + baseY);
        }
        if (actionId == 502) {
            method66(i12, k, j);

            /*int objectId = -1;
            for(int a = 0; a < Model.anIntArray1688.length; a++) {
                if(i1 == Model.anIntArray1688[a]) {
                    objectId = Model.mapObjectIds[a];
                    break;
                }
            }*/

            stream.createFrame(132);
            stream.writeSignedShortBigEndianA(j + baseX);
            stream.putShort((int) ((i12 >>> 32) & 0x7fffffff));
            stream.writeSignedShortA(k + baseY);
        }
        if (actionId == 1125) {
            ItemDefinition itemDef = ItemDefinition.forID(i1);
            RSInterface class9_4 = RSInterface.interfaceCache[k];
            String s5;
            if (class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0)
                s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
            else if (itemDef.description != null)
                s5 = new String(itemDef.description);
            else
                s5 = "It's a " + itemDef.name + ".";
            pushMessage(s5, ChatMessage.GAME_MESSAGE);
        }
        if (actionId == 169) {
            sendPacket185(k);
        }
        if (actionId == 447) {
            itemSelected = 1;
            anInt1283 = j;
            anInt1284 = k;
            anInt1285 = i1;
            selectedItemName = ItemDefinition.forID(i1).name;
            targetInterfaceId = 0;
            reDrawTabArea = true;
            return;
        }
        if (actionId == 1226) {
            int j1 = (int) (i12 >>> 32) & 0x7fffffff;
            ObjectDefinition class46 = ObjectDefinition.forID(j1);
            String s10;
            if (class46.description != null)
                s10 = new String(class46.description);
            else
                s10 = "It's a " + class46.name + ".";
            pushMessage(s10, ChatMessage.GAME_MESSAGE);
        }
        if (actionId == 244) {
            boolean flag7 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k,
                    myPlayer.smallX[0], false, j);
            if (!flag7)
                flag7 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k,
                        myPlayer.smallX[0], false, j);
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(253);
            stream.method431(j + baseX);
            stream.writeSignedShortBigEndianA(k + baseY);
            stream.writeSignedShortA(i1);
        }
        if (actionId == 1448) {
            ItemDefinition itemDef_1 = ItemDefinition.forID(i1);
            String s6;
            if (itemDef_1.description != null)
                s6 = new String(itemDef_1.description);
            else
                s6 = "It's a " + itemDef_1.name + ".";
            pushMessage(s6, ChatMessage.GAME_MESSAGE);
        }

        if (itemSelected != 0) {
        	itemSelected = 0;
            reDrawTabArea = true;
        }

        if (targetInterfaceId != 0) {
        	targetInterfaceId = 0;
            reDrawTabArea = true;
        }

    }

    public void sendString(int identifier, String text) {
        if (text == null || text.length() == 0) {
            //return;
        }
        text = identifier + "," + text;
        stream.createFrame(127);
        stream.putByte(text.length() + 1);
        stream.putString(text);
    }

    private void method70() {
        anInt1251 = 0;
        int j = (myPlayer.x >> 7) + baseX;
        int k = (myPlayer.y >> 7) + baseY;
        if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
            anInt1251 = 1;
        if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
            anInt1251 = 1;
        if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
            anInt1251 = 0;
    }

    public void sendPacket(int paramInt) {
        if (paramInt == 103) {
            stream.createFrame(103);
            stream.putByte(Client.inputString.length() - 1);
            stream.putString(Client.inputString.substring(2));
            inputString = "";
            promptInput = "";
            clanChatAction = -1;
        }
    }

    private void build3dScreenMenu() {
        if (itemSelected == 0 && targetInterfaceId == 0) {
            MiniMenu.addOption("Walk here", 516, 0, super.mouseX, super.mouseY);
        }

        long prevBitPacked = -1;
        for (int k = 0; k < Model.objectsRendered; k++) {
            long bitPacked = Model.objectsInCurrentRegion[k];
            if (bitPacked == prevBitPacked) {
                continue;
            }
            int x = (int) bitPacked & 0x7f;
            int z = (int) (bitPacked >> 7) & 0x7f;
            int type = (int) (bitPacked >> 29) & 0x3;
            int objectID = (int) (bitPacked >>> 32) & 0x7fffffff;
            prevBitPacked = bitPacked;
            if (type == 2 && worldController.bitPackedMatch(plane, x, z, bitPacked)) {
                ObjectDefinition class46 = ObjectDefinition.forID(objectID);
                if (class46.alternativeIDS != null)
                    class46 = class46.method580();
                if (class46 == null || class46.name == null
                        || class46.name == "null")
                    continue;
                if (itemSelected == 1) {
                    MiniMenu.addOption("Use " + selectedItemName + " ->", "<col=00ffff>" + class46.name, 62, bitPacked, x, z);
                } else if (targetInterfaceId != 0) {
                    if ((targetBitMask & 4) == 4) {
                        MiniMenu.addOption(targetOption, "<col=00ffff>" + class46.name, 956, bitPacked, x, z);
                    }
                } else {
                    if (class46.actions != null) {
                        for (int i2 = 4; i2 >= 0; i2--) {
                            if (class46.actions[i2] != null) {
								int option = 0;
                                if (i2 == 0) {
                                	option = 502;
                                } else if (i2 == 1) {
                                	option = 900;
                                } else if (i2 == 2) {
                                	option = 113;
                                } else if (i2 == 3) {
                                	option = 872;
                                } else if (i2 == 4) {
                                	option = 1062;
                                }

                                MiniMenu.addOption(class46.actions[i2], "<col=00ffff>" + class46.name, option, bitPacked, x, z);
                            }
                        }
                    }
                    MiniMenu.addOption("Examine", "<col=00ffff>" + class46.name, 1226, bitPacked, x, z);
                }
			} else if (type == 1) {
				NPC npc = npcArray[objectID];
				if (npc.size == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						NPC onTopNpc = npcArray[npcIndices[j2]];
						if (onTopNpc != null && onTopNpc != npc && onTopNpc.size == 1 && onTopNpc.x == npc.x && onTopNpc.y == npc.y)
							buildAtNPCMenu(onTopNpc.desc, npcIndices[j2], z, x);
					}

					for (int l2 = 0; l2 < playerCount; l2++) {
						Player onTopPlayer = playerArray[playerIndices[l2]];
						if (onTopPlayer != null && onTopPlayer.x == npc.x && onTopPlayer.y == npc.y)
							buildAtPlayerMenu(x, playerIndices[l2], onTopPlayer, z);
					}

				}
				buildAtNPCMenu(npc.desc, objectID, z, x);
			} else if (type == 0) {
				Player player = playerArray[objectID];
				if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for (int k2 = 0; k2 < npcCount; k2++) {
						NPC onTopNpc = npcArray[npcIndices[k2]];
						if (onTopNpc != null && onTopNpc.size == 1 && onTopNpc.x == player.x && onTopNpc.y == player.y)
							buildAtNPCMenu(onTopNpc.desc, npcIndices[k2], z, x);
					}

					for (int i3 = 0; i3 < playerCount; i3++) {
						Player onTopPlayer = playerArray[playerIndices[i3]];
						if (onTopPlayer != null && onTopPlayer != player && onTopPlayer.x == player.x && onTopPlayer.y == player.y)
							buildAtPlayerMenu(x, playerIndices[i3], onTopPlayer, z);
					}

				}
				buildAtPlayerMenu(x, objectID, player, z);
			} else if (type == 3) {
                NodeList class19 = groundArray[plane][x][z];
                if (class19 != null) {
                    for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19
                            .getNext()) {
                        ItemDefinition itemDef = ItemDefinition.forID(item.ID);
                        if (itemSelected == 1) {
                            MiniMenu.addOption("Use " + selectedItemName + " with", "<col=ff9040>" + itemDef.name, 511, item.ID, x, z);
                        } else if (targetInterfaceId != 0) {
                            if ((targetBitMask & 1) == 1) {
                                MiniMenu.addOption(targetOption, "<col=ff9040>" + itemDef.name, 94, item.ID, x, z);
                            }
                        } else {
                            for (int j3 = 4; j3 >= 0; j3--) {
                                if (itemDef.groundActions != null
                                        && itemDef.groundActions[j3] != null) {
									int option = 0;
                                    if (j3 == 0) {
                                        option = 652;
                                    } else if (j3 == 1) {
                                        option = 567;
                                    } else if (j3 == 2) {
                                        option = 234;
                                    } else if (j3 == 3) {
                                        option = 244;
                                    } else if (j3 == 4) {
                                        option = 213;
                                    }

                                    MiniMenu.addOption(itemDef.groundActions[j3], "<col=ff9040>" + itemDef.name, option, item.ID, x, z);
                                } else if (j3 == 2) {
                                    MiniMenu.addOption("Take", "<col=ff9040>" + itemDef.name, 234, item.ID, x, z);
                                }
                            }

                            MiniMenu.addOption("Examine", "<col=ff9040>" + itemDef.name, 1448, item.ID, x, z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void cleanUpForQuit() {
        try {
            if (socketStream != null)
                socketStream.close();
        } catch (Exception _ex) {
        }
        socketStream = null;
        stopMidi();
        if (onDemandFetcher != null) {
            onDemandFetcher.disable();
            onDemandFetcher = null;
        }
        aStream_834 = null;
        stream = null;
        aStream_847 = null;
        inStream = null;
        mapCoordinates = null;
        terrainData = null;
        aByteArrayArray1247 = null;
        terrainIndices = null;
        anIntArray1236 = null;
        intGroundArray = null;
        tileSettingBits = null;
        worldController = null;
        tileSettings = null;
        anIntArrayArray901 = null;
        anIntArrayArray825 = null;
        bigX = null;
        bigY = null;
        tabAreaProducer = null;
        leftFrame = null;
        topFrame = null;
        rightFrame = null;
        minimapProducer = null;
        gameScreen = null;
        chatAreaProducer = null;
        /* Null pointers for custom sprites */
        chatButtons = null;
        cacheSprite = null;
        /**/
        mapBack = null;
        compass = null;
        hitMarks = null;
        hitMarks562 = null;
        headIcons = null;
        skullIcons = null;
        magicAuto = null;
        headIconsHint = null;
        crosses = null;
        mapDotItem = null;
        mapDotNPC = null;
        mapDotClan = null;
        mapDotPlayer = null;
        mapDotFriend = null;
        mapDotElite = null;
        mapDotTeam = null;
        mapScenes = null;
        mapFunctions = null;
        anIntArrayArray929 = null;
        playerArray = null;
        playerIndices = null;
        sessionNpcsAwaitingUpdate = null;
        playerUpdateStreams = null;
        anIntArray840 = null;
        npcArray = null;
        npcIndices = null;
        groundArray = null;
        aClass19_1179 = null;
        projectileNode = null;
        stillGraphicsNode = null;
        playerVariables = null;
        markPosX = null;
        markPosY = null;
        markGraphic = null;
        minimapImage = null;
        friendsList = null;
        friendsListAsLongs = null;
        friendsNodeIDs = null;
        multiOverlay = null;
        ObjectDefinition.nullLoader();
        NpcDefinition.nullLoader();
        ItemDefinition.nullLoader();
        FloorUnderlay.setCache(null);
        IdentityKit.cache = null;
        RSInterface.interfaceCache = null;
        Animation.anims = null;
        SpotAnim.cache = null;
        SpotAnim.modelCache = null;
        Varp.cache = null;
        super.fullGameScreen = null;
        Player.modelCache = null;
        Rasterizer.nullLoader();
        WorldController.nullLoader();
        Model.nullLoader();
        FrameReader.nullLoader();
        System.gc();
    }

    private void printDebug() {
        System.out.println("============");
        System.out.println("flame-cycle:" + anInt1208);
        if (onDemandFetcher != null)
            System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle);
        System.out.println("loop-cycle:" + loopCycle);
        System.out.println("draw-cycle:" + anInt1061);
        System.out.println("ptype:" + pktType);
        System.out.println("psize:" + pktSize);
        if (socketStream != null)
            socketStream.printDebug();
    }

    private void method73() {
        do {
            int keycode = readChar();
            if (keycode == -1)
                break;
            if (openInterfaceID != -1
                    && openInterfaceID == reportAbuseInterfaceID) {
                if (keycode == 8 && reportAbuseInput.length() > 0)
                    reportAbuseInput = reportAbuseInput.substring(0,
                            reportAbuseInput.length() - 1);
                if ((keycode >= 97 && keycode <= 122 || keycode >= 65 && keycode <= 90 || keycode >= 48
                        && keycode <= 57 || keycode == 32)
                        && reportAbuseInput.length() < 12)
                    reportAbuseInput += (char) keycode;
            } else if (messagePromptRaised) {
                if (filter != null) {
                    Matcher m = filter.matcher(String.valueOf((char) keycode));
                    if (m.matches()) {
                        promptInput += (char) keycode;
                        reDrawChatArea = true;
                    }
                } else if (chatActionID == 12) {
                    if (keycode >= 32 && keycode <= 122 && ((char) (keycode) + "").matches("\\d") && promptInput.length() < 2) {
                        promptInput += (char) keycode;
                        reDrawChatArea = true;
                    }
                } else if (chatActionID == 13) {
                    if (keycode >= 32 && keycode <= 122 && ((promptInput + (char) (keycode)).toLowerCase().matches("^\\d+[a-z&&[kmb]]") || (promptInput + (char) (keycode)).toLowerCase().matches("^\\d+")) && promptInput.length() < 9) {
                        promptInput += (char) keycode;
                        reDrawChatArea = true;
                    }
                } else {
                    if (keycode >= 32 && keycode <= 122 && promptInput.length() < 80) {
                        promptInput += (char) keycode;
                        reDrawChatArea = true;

                        if (playerVariables[0] == 1) {
                            RSInterface.interfaceCache[27000].message = "1";
                            drawOnBankInterface();
                        }
                    }
                }
                if (keycode == 8 && promptInput.length() > 0) {
                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                    reDrawChatArea = true;

                    if (playerVariables[0] == 1) {
                        RSInterface.interfaceCache[27000].message = "1";
                        drawOnBankInterface();
                    }
                }
                if (keycode == 13 || keycode == 10) {
                    messagePromptRaised = false;
                    reDrawChatArea = true;

                    if (inputType != null) {

                        stream.createFrame(28);
                        stream.putByte(promptInput.length() + inputType.length() + 2);
                        stream.putString(promptInput);
                        stream.putString(inputType);

                        inputType = null;
                        filter = null;
                        return;
                    }

                    if (clanChatAction > 0 && promptInput.length() > 0) {
                        inputString = "::"
                                + (clanChatAction == 1 ? "joincc "
                                : "renamecc ") + promptInput;
                        sendPacket(103);
                        return;
                    }

                    if (chatActionID == 1) {
                        addFriend(TextClass.longForName(promptInput));
                    } else if (chatActionID == 2 && friendsCount > 0) {
                        delFriend(TextClass.longForName(promptInput));
                    } else  if (chatActionID == 3 && promptInput.length() > 0) {
                        stream.createFrame(126);
                        stream.putByte(0);
                        int k = stream.currentOffset;
                        stream.putLong(aLong953);
                        TextInput.method526(promptInput, stream);
                        stream.putSize(stream.currentOffset - k);
                        promptInput = TextClass.toSentence(promptInput);
                        pushMessage(promptInput, ChatMessage.PRIVATE_MESSAGE_SENT, TextClass.fixName(TextClass.nameForLong(aLong953)));
                        if (privateChatMode == 2) {
                            privateChatMode = 1;
                            stream.createFrame(95);
                            stream.putByte(publicChatMode);
                            stream.putByte(privateChatMode);
                            stream.putByte(tradeChatMode);
                        }
                    } else if (chatActionID == 4 && ignoreCount < 100) {
                        long l2 = TextClass.longForName(promptInput);
                        addIgnore(l2);
                    } else if (chatActionID == 5 && ignoreCount > 0) {
                        long l3 = TextClass.longForName(promptInput);
                        delIgnore(l3);
                    } else if (chatActionID == 6) {
                        sendStringAsLong(promptInput);
                    } else if (chatActionID == 8) {
                        sendString(1, promptInput);
                    } else if (chatActionID == 9) {
                        sendString(2, promptInput);
                    } else if (chatActionID == 10) {
                        sendString(3, promptInput);
                    } else if (chatActionID == 11) {
                        playerVariables[0] = 0;
                        postVarpChange(0);

                        RSInterface.interfaceCache[27000].message = "1";
                        drawOnBankInterface();
                    } else if ((chatActionID == 12) && (promptInput.length() > 0)) {
                        if (promptInput.toLowerCase().matches("\\d+")) {
                            int input = Integer.parseInt(promptInput);
                            if (input > 99) {
                                input = 99;
                            }
                            if ((input < 0) || (statsBase[SkillConstants.selectedSkillId] >= input)) {
                                SkillConstants.selectedSkillId = -1;
                                return;
                            }
                            SkillConstants.goalType = "Target Level: ";
                            SkillConstants.goalData[SkillConstants.selectedSkillId][0] = statsXp[SkillConstants.selectedSkillId];
                            SkillConstants.goalData[SkillConstants.selectedSkillId][1] = getXPForLevel(input) + 1;
                            SkillConstants.goalData[SkillConstants.selectedSkillId][2] = (SkillConstants.goalData[SkillConstants.selectedSkillId][0] / SkillConstants.goalData[SkillConstants.selectedSkillId][1]) * 100;
                            saveGoals(myUsername);
                            SkillConstants.selectedSkillId = -1;
                        }
                    } else if ((chatActionID == 13) && (promptInput.length() > 0) && ((promptInput.toLowerCase().matches("\\d+[a-z&&[km]]")) || (promptInput.matches("\\d+")))) {
                        int input = 0;
                        try {
                            input = Integer.parseInt(promptInput.trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if ((input < 0) || (input <= statsXp[SkillConstants.selectedSkillId])) {
                            SkillConstants.selectedSkillId = -1;
                            return;
                        } else if (input > 200000000) {
                            SkillConstants.selectedSkillId = -1;
                            return;
                        }
                        SkillConstants.goalType = "Target Exp: ";
                        SkillConstants.goalData[SkillConstants.selectedSkillId][0] = statsXp[SkillConstants.selectedSkillId];
                        SkillConstants.goalData[SkillConstants.selectedSkillId][1] = ((int) input);
                        SkillConstants.goalData[SkillConstants.selectedSkillId][2] = (SkillConstants.goalData[SkillConstants.selectedSkillId][0] / SkillConstants.goalData[SkillConstants.selectedSkillId][1] * 100);
                        saveGoals(myUsername);
                        SkillConstants.selectedSkillId = -1;
                    }
                }

                // 4 is withdrawal from money pouch
            } else if (inputDialogState == 1 || inputDialogState == 4) {
                if (keycode >= 48 && keycode <= 57 && amountOrNameInput.length() < 10) {
                    amountOrNameInput += (char) keycode;
                    reDrawChatArea = true;
                }
                if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m") && !amountOrNameInput.toLowerCase().contains("b")) && (keycode == 107 || keycode == 109) || keycode == 98) {
                    amountOrNameInput += (char) keycode;
                    reDrawChatArea = true;
                }
                if (keycode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    reDrawChatArea = true;
                }
                if (keycode == 13 || keycode == 10) {
                    if (amountOrNameInput.length() > 0) {
                        int length = amountOrNameInput.length();
                        char lastChar = amountOrNameInput.charAt(length - 1);

                        if (lastChar == 'k') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000";
                        } else if (lastChar == 'm') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000";
                        } else if (lastChar == 'b') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000000";
                        }

                        long amount = 0;

                        try {
                            amount = Long.parseLong(amountOrNameInput);

                            if (amount < 0) {
                                amount = 0;
                            }

                            if (inputDialogState == 1 && amount > Integer.MAX_VALUE) {
                                amount = Integer.MAX_VALUE;
                            }
                        } catch (Exception ex) {
                        }

                        if (inputDialogState == 4) {
                            stream.createFrame(205);
                            stream.putLong(amount);
                        } else {
                            stream.createFrame(208);
                            stream.putInt((int) amount);

                            if (openInterfaceID == 5292) {
                                withdrawAmount = (int) amount;
                                for (int index = 0; index < BANK_WITHDRAW_OPS_IDS.length; index++) {
                                    RSInterface.interfaceCache[BANK_WITHDRAW_OPS_IDS[index]].actions = new String[]{
                                            "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-100", "Withdraw-X", "Withdraw-All", "Withdraw-All but one", "Withdraw-" + amount
                                    };
                                }
                            }
                        }
                    }
                    inputDialogState = 0;
                    reDrawChatArea = true;
                }
            } else if (inputDialogState == 3) {
                if (keycode == 10) {
                    totalItemResults = 0;
                    amountOrNameInput = "";
                    inputDialogState = 0;
                    reDrawChatArea = true;
                }
                if (keycode == 13 || keycode == 10) {
                    if (amountOrNameInput.length() == 0) {
                        //buttonclicked = false;
                        // interfaceButtonAction = 0;
                    }
                }
                if (keycode >= 32 && keycode <= 122 && amountOrNameInput.length() < 40) {
                    amountOrNameInput += (char) keycode;
                    reDrawChatArea = true;
                    itemSearch(amountOrNameInput);
                }
                if (keycode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    reDrawChatArea = true;
                    itemSearch(amountOrNameInput);
                }
            } else if (inputDialogState == 2) {
                if (keycode >= 32 && keycode <= 122 && amountOrNameInput.length() < 12) {
                    amountOrNameInput += (char) keycode;
                    reDrawChatArea = true;
                }
                if (keycode == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0,
                            amountOrNameInput.length() - 1);
                    reDrawChatArea = true;
                }
                if (keycode == 13 || keycode == 10) {
                    if (amountOrNameInput.length() > 0) {
                        stream.createFrame(60);
                        stream.putLong(TextClass.longForName(amountOrNameInput));
                    }
                    inputDialogState = 0;
                    reDrawChatArea = true;
                }
			} else if (backDialogID == 356 || backDialogID == 359 || backDialogID == 363 || backDialogID == 374 || backDialogID == 4882 || backDialogID == 4887 || backDialogID == 4893 || backDialogID == 4900 || backDialogID == 968 || backDialogID == 973 || backDialogID == 979 || backDialogID == 986) {
				if (keycode == 32 && !isDialogueInterface) {
					stream.createFrame(40);
					stream.putShort(0);
					isDialogueInterface = true;
				}
			} else if (backDialogID == 2459 || backDialogID == 2469 || backDialogID == 2480 || backDialogID == 2492) {
				if (keycode >= KeyEvent.VK_1 && keycode <= KeyEvent.VK_5) {
					int id = backDialogID + 2;//Option 1
					int option = keycode - KeyEvent.VK_1;
	                stream.createFrame(185);
	                stream.putShort(id + option);
				}
            } else if (backDialogID == -1) {
                if (keycode == 9) {
                    tabToReplyPm();
                    continue;
                }

                if (keycode >= 32 && keycode <= 122 && inputString.length() < 80) {
                    inputString += (char) keycode;
                    reDrawChatArea = true;
                }
                if (keycode == 8 && inputString.length() > 0) {
                    inputString = inputString.substring(0,
                            inputString.length() - 1);
                    reDrawChatArea = true;
                }
                if ((keycode == 13 || keycode == 10) && inputString.length() > 0) {
                    if (inputString.equals("::dumpimages") && myPrivilege >= 3) {
                        ItemDefinition.dumpItemImages(false);
                    }
                    if (inputString.equals("::ld")) {
                        lowMem = true;
                        setLowMem();
                        disableOverlayTexture = true;
                        disableUnderlayTexture = true;
                        loadRegion();
                    }
                    if (inputString.equals("::sd")) {
                        lowMem = false;
                        setHighMem();
                        disableOverlayTexture = true;
                        disableUnderlayTexture = true;
                        loadRegion();
                    }
                    if (inputString.equals("::getindex")) {
                        pushMessage("" + myPlayer.playerIndex, ChatMessage.GAME_MESSAGE);
                    }
                    if (inputString.equals("::bm")) {
                        buildingMode = !buildingMode;
                        stream.createFrame(185);
                        stream.putShort(1337);
                    }
                    if (inputString.equals("::minimapshade")) {
                        Settings.minimapShading = !Settings.minimapShading;
                        loadRegion();
                    }
                    if (inputString.equals("::hd")) {
                        lowMem = false;
                        setHighMem();
                        disableOverlayTexture = false;
                        disableUnderlayTexture = false;
                        loadRegion();
                    }

                    if (inputString.equals("::search")) {
                        inputDialogState = 3;
                    }
                    if (inputString.equals("::fixed"))
                        toggleSize(0, false);
                    if (inputString.equals("::resizable"))
                        toggleSize(1, false);
                    if (inputString.equals("::fullscreen"))
                        toggleSize(2, false);
                    if (inputString.equals("::634hitmarks"))
                        Settings.hitmarks = Constants.HITMARKS_634;
                    if (inputString.equals("::562hitmarks"))
                        Settings.hitmarks = Constants.HITMARKS_562;
                    if (inputString.equals("::317hitmarks"))
                        Settings.hitmarks = Constants.HITMARKS_317;
                    if (inputString.equals("::dataon"))
                        clientData = true;
                    if (inputString.equals("::dataoff"))
                        clientData = false;
                    if (inputString.startsWith("/")) {
                        inputString = "::clan " + inputString.substring(1);
                    }

                    if (inputString.startsWith("::")) {
                        stream.createFrame(103);
                        stream.putByte(inputString.length() - 1);
                        stream.putString(inputString.substring(2));
                    } else {
                        String s = inputString.toLowerCase();
                        int j2 = 0;
                        if (s.startsWith("yellow:")) {
                            j2 = 0;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("red:")) {
                            j2 = 1;
                            inputString = inputString.substring(4);
                        } else if (s.startsWith("green:")) {
                            j2 = 2;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("cyan:")) {
                            j2 = 3;
                            inputString = inputString.substring(5);
                        } else if (s.startsWith("purple:")) {
                            j2 = 4;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("white:")) {
                            j2 = 5;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("flash1:")) {
                            j2 = 6;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("flash2:")) {
                            j2 = 7;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("flash3:")) {
                            j2 = 8;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("glow1:")) {
                            j2 = 9;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("glow2:")) {
                            j2 = 10;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("glow3:")) {
                            j2 = 11;
                            inputString = inputString.substring(6);
                        }
                        s = inputString.toLowerCase();
                        int i3 = 0;
                        if (s.startsWith("wave:")) {
                            i3 = 1;
                            inputString = inputString.substring(5);
                        } else if (s.startsWith("wave2:")) {
                            i3 = 2;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("shake:")) {
                            i3 = 3;
                            inputString = inputString.substring(6);
                        } else if (s.startsWith("scroll:")) {
                            i3 = 4;
                            inputString = inputString.substring(7);
                        } else if (s.startsWith("slide:")) {
                            i3 = 5;
                            inputString = inputString.substring(6);
                        }
                        stream.createFrame(4);
                        stream.putByte(0);
                        int j3 = stream.currentOffset;
                        stream.method425(i3);
                        stream.method425(j2);
                        aStream_834.currentOffset = 0;
                        TextInput.method526(inputString, aStream_834);
                        stream.method441(0, aStream_834.buffer,
                                aStream_834.currentOffset);
                        stream.putSize(stream.currentOffset - j3);
                        inputString = TextClass.toSentence(inputString);
                        myPlayer.textSpoken = inputString;
                        myPlayer.anInt1513 = j2;
                        myPlayer.anInt1531 = i3;
                        myPlayer.textCycle = 150;
                        int chatType = myPrivilege == 0 ? ChatMessage.PUBLIC_MESSAGE_PLAYER : ChatMessage.PUBLIC_MESSAGE_STAFF;
                        pushMessage(myPlayer.textSpoken, chatType, myPlayer.name, getCrown(myPrivilege) + getEliteCrown(myPlayer.elite) + (myPlayer.titleText == null ? "" : myPlayer.titleText));
                        if (publicChatMode == 2) {
                            publicChatMode = 3;
                            stream.createFrame(95);
                            stream.putByte(publicChatMode);
                            stream.putByte(privateChatMode);
                            stream.putByte(tradeChatMode);
                        }
                    }
                    inputString = "";
                    reDrawChatArea = true;
                }
            }
        } while (true);
    }

    private void sendStringAsLong(String string) {
        stream.createFrame(60);
        stream.putLong(TextClass.longForName(string));
    }

    private void buildMessageOrAddFriend(String s, boolean lowPrio) {
    	s = removeMarkup(s);
        if (myPrivilege >= 1) {
            MiniMenu.addOption("Report abuse", "<col=ffffff>" + s, lowPrio ? 2606 : 606);
        }
        long nameAsLong = TextClass.longForName(s);
        int k3 = -1;
        for (int i4 = 0; i4 < friendsCount; i4++) {
            if (friendsListAsLongs[i4] != nameAsLong)
                continue;
            k3 = i4;
            break;
        }
        if (k3 != -1) {
            MiniMenu.addOption("Remove", "<col=ffffff>" + s, lowPrio ? 2792 : 792);
            MiniMenu.addOption("Message", "<col=ffffff>" + s, lowPrio ? 2639 : 639);
        } else {
            MiniMenu.addOption("Add ignore", "<col=ffffff>" + s, lowPrio ? 2042 : 42);
            MiniMenu.addOption("Add friend", "<col=ffffff>" + s, lowPrio ? 2337 : 337);
        }
    }

    private void buildChatAreaMenu(int mouseY) {
        int messageCount = 0;
		for (int index = ChatMessageManager.getHighestId(); index != -1; index = ChatMessageManager.getPrevMessageId(index)) {
			ChatMessage message = (ChatMessage) ChatMessageManager.messageHashtable.findNodeByID(index);
            int messageY = (70 - messageCount * 14 + 42) + chatboxScrollPos + 9;
            if (messageY < -23) {
                break;
            }
            int type = message.type;
            String chatName = message.name;
            if (type == 0 && (chatTypeView == 5 || chatTypeView == 0)) {
                messageCount++;
            } else if ((type == ChatMessage.PUBLIC_MESSAGE_STAFF || type == ChatMessage.PUBLIC_MESSAGE_PLAYER) && (type == ChatMessage.PUBLIC_MESSAGE_STAFF || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(chatName)) && (chatTypeView == 1 || chatTypeView == 0)) {
            	if (mouseY > messageY - 14 && mouseY <= messageY && !chatName.equals(myPlayer.name)) {
                    buildMessageOrAddFriend(chatName, false);
                }
                messageCount++;
            } else if ((type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER || type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF)
                    && (splitPrivateChat == 0 || chatTypeView == 2)
                    && (type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF || privateChatMode == 0 || privateChatMode == 1
                    && isFriendOrSelf(chatName)) && (chatTypeView == 2 || chatTypeView == 0)) {
                if (mouseY > messageY - 14 && mouseY <= messageY) {
                    buildMessageOrAddFriend(chatName, false);
                }
                messageCount++;
            } else  if (type == ChatMessage.TRADE_REQUEST
                    && (tradeChatMode == 0 || tradeChatMode == 1 && isFriendOrSelf(chatName)) && (chatTypeView == 3 || chatTypeView == 0)) {
                if (mouseY > messageY - 14 && mouseY <= messageY) {
                    MiniMenu.addOption("Accept trade", "<col=ffffff>" + chatName, 484);
                }
                messageCount++;
            } else if ((type == ChatMessage.PRIVATE_MESSAGE_RECEIVED || type == ChatMessage.PRIVATE_MESSAGE_SENT) && (splitPrivateChat == 0 || chatTypeView == 2)
                    && privateChatMode < 2 && (chatTypeView == 2 || chatTypeView == 0)) {
                messageCount++;
            } else if (type == ChatMessage.CHALLENGE_REQUEST
                    && (challengeChatMode == 0 || challengeChatMode == 1 && isFriendOrSelf(chatName)) && (chatTypeView == 6 || chatTypeView == 0)) {
                if (mouseY > messageY - 14 && mouseY <= messageY) {
                    MiniMenu.addOption("Accept challenge", "<col=ffffff>" + chatName, 6);
                }
                messageCount++;
            } else if (type == ChatMessage.CLAN_MESSAGE && (clanChatMode == 0 || clanChatMode == 1 && isFriendOrSelf(chatName)) && (chatTypeView == 11 || chatTypeView == 0)) {
                if (mouseY > messageY - 14 && mouseY <= messageY) {
                    buildMessageOrAddFriend(chatName, false);
                }
                messageCount++;
            }
        }
    }

    public int getXPForLevel(int level) {
        return expArray[--level > 98 ? 98 : level];
    }

    public int getLevelForXP(int exp) {
        for (int j = 98; j != -1; j--) {
            if (expArray[j] <= exp) {
                return j + 1;
            }
        }
        return 0;
    }

    public String setMessage(int skillLevel) {
        skillTabTooltip.setLength(0);
        int skillId = SKILL_TAB_IDS[skillLevel];
        skillTabTooltip.append(skillNames[skillLevel]);
        skillTabTooltip.append(": ");
        skillTabTooltip.append(stats[skillId]);
        skillTabTooltip.append("/");
        skillTabTooltip.append(statsBase[skillId]);
        skillTabTooltip.append("\\n");
        skillTabTooltip.append("Current Exp: ");
        skillTabTooltip.append(numberFormat.format(statsXp[skillId]));
        skillTabTooltip.append("\\n");
        boolean newLine = false;
        if (statsBase[skillId] < 99) {
            skillTabTooltip.append("Remainder: ");
            skillTabTooltip.append(numberFormat.format(getXPForLevel(statsBase[skillId] + 1) - statsXp[skillId]));
            skillTabTooltip.append("\\n");
            skillTabTooltip.append("Next level: ");
            skillTabTooltip.append(numberFormat.format(getXPForLevel(statsBase[skillId] + 1)));
            newLine = true;
        }
        int init = SkillConstants.goalData[skillId][0];
        int goal = SkillConstants.goalData[skillId][1];
        if (statsXp[skillId] < 200000000 && init > -1 && goal > -1) {
            if (newLine) {
                skillTabTooltip.append("\\n");
            }
            skillTabTooltip.append(SkillConstants.goalType);
            skillTabTooltip.append(SkillConstants.goalType.endsWith("Level: ") ? Integer.valueOf(getLevelForXP(goal)) : numberFormat.format(goal));
            skillTabTooltip.append("\\n");
            int remainder = goal - statsXp[skillId] - (SkillConstants.goalType.endsWith("Level: ") ? 1 : 0);
            if (remainder < 0) {
                remainder = 0;
            }
            skillTabTooltip.append("Remainder: ");
            skillTabTooltip.append(numberFormat.format(remainder));
            SkillConstants.goalData[skillId][2] = (int) (((statsXp[skillId] - init) / (double) (goal - init)) * 100);
            if (SkillConstants.goalData[skillId][2] > 100) {
                SkillConstants.goalData[skillId][2] = 100;
            }
        }
        return skillTabTooltip.toString();
    }

    public String setTotalExp() {
        long totalExp = 0;
        for (int i = 0; i < 25; i++)
            totalExp += statsXp[i];
        return "Total Exp: " + numberFormat.format(totalExp);
    }

    private void drawFriendsListOrWelcomeScreen(RSInterface class9) {
        int j = class9.contentType;
        if (j >= 205 && j <= 205 + 25) {
            j -= 205;
            class9.message = setMessage(j);
            return;
        }
        if (j == 231) {
            class9.message = setTotalExp();
            return;
        }
        if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
            if (j == 1 && networkFriendsServerStatus == 0) {
                class9.message = "Loading friend list";
                class9.atActionType = 0;
                return;
            }
            if (j == 1 && networkFriendsServerStatus == 1) {
                class9.message = "Connecting to friendserver";
                class9.atActionType = 0;
                return;
            }
            if (j == 2 && networkFriendsServerStatus != 2) {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int k = friendsCount;
            if (networkFriendsServerStatus != 2)
                k = 0;
            if (j > 700)
                j -= 601;
            else
                j--;
            if (j >= k) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else {
                class9.message = friendsList[j];
                class9.atActionType = 1;
                return;
            }
        }
        if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
            int l = friendsCount;
            if (networkFriendsServerStatus != 2)
                l = 0;
            if (j > 800)
                j -= 701;
            else
                j -= 101;
            if (j >= l) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            }
            if (friendsNodeIDs[j] == 0)
                class9.message = "<col=ff0000>Offline";
            else if (friendsNodeIDs[j] == nodeID)
                class9.message = "<col=00ff00>Online"/* + (friendsNodeIDs[j] - 9) */;
            else
                class9.message = "<col=ff0000>Offline"/* + (friendsNodeIDs[j] - 9) */;
            class9.atActionType = 1;
            return;
        }
        if (j == 203) {
            int i1 = friendsCount;
            if (networkFriendsServerStatus != 2)
                i1 = 0;
            class9.scrollMax = i1 * 15 + 20;
            if (class9.scrollMax <= class9.height)
                class9.scrollMax = class9.height + 1;
            return;
        }
        if (j >= 401 && j <= 500) {
            if ((j -= 401) == 0 && networkFriendsServerStatus == 0) {
                class9.message = "Loading ignore list";
                class9.atActionType = 0;
                return;
            }
            if (j == 1 && networkFriendsServerStatus == 0) {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int j1 = ignoreCount;
            if (networkFriendsServerStatus == 0)
                j1 = 0;
            if (j >= j1) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else {
                class9.message = TextClass.fixName(TextClass
                        .nameForLong(ignoreListAsLongs[j]));
                class9.atActionType = 1;
                return;
            }
        }
        if (j == 901) {
            class9.message = friendsCount + " / 200";
            return;
        }
        if (j == 902) {
            class9.message = ignoreCount + " / 100";
            return;
        }
        if (j == 503) {
            class9.scrollMax = ignoreCount * 15 + 20;
            if (class9.scrollMax <= class9.height)
                class9.scrollMax = class9.height + 1;
            return;
        }
        if (j == 327) {
            class9.rotationX = 150;
            class9.rotationY = (int) (Math.sin(loopCycle / 40D) * 256D) & 0x7ff;
            if (aBoolean1031) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = anIntArray1065[k1];
                    if (l1 >= 0 && !IdentityKit.cache[l1].isBodyDownloaded())
                        return;
                }

                aBoolean1031 = false;
                Model aclass30_sub2_sub4_sub6s[] = new Model[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = anIntArray1065[j2];
                    if (k2 >= 0)
                        aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.cache[k2]
                                .getBodyModel();
                }

                Model model = new Model(aclass30_sub2_sub4_sub6s, i2);
                for (int l2 = 0; l2 < 5; l2++)
                    if (anIntArray990[l2] != 0) {
                        model.setColor(anIntArrayArray1003[l2][0],
                                anIntArrayArray1003[l2][anIntArray990[l2]]);
                        if (l2 == 4) {
                        	model.setColor(skinColor2[0], skinColor2[anIntArray990[l2]]);
                        }
                        if (l2 == 1)
                            model.setColor(anIntArray1204[0],
                                    anIntArray1204[anIntArray990[l2]]);
                    }

                model.createBones();
                model.interpolateFrames(Animation.anims[myPlayer.standAnimIndex].frames[0], -1, -1, -1, null, false);
                model.setLighting(64, 850, -30, -50, -30, true, true, true);
                class9.mediaType = 5;
                class9.mediaID = 0;
                playerModel = model;
            }
            return;
        }
        if (j == 328) {
            class9.rotationX = 150;
            class9.rotationY = (int) (Math.sin(loopCycle / 40D) * 256D) & 0x7ff;
            class9.mediaType = 5;
            class9.mediaID = 0;
            playerModel = myPlayer.getAnimatedModel();
            return;
        }
        if (j == 324) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if (aBoolean1047) {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
            } else {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
            }
            return;
        }
        if (j == 325) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if (aBoolean1047) {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
            } else {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
            }
            return;
        }
        if (j == 600) {
            class9.message = reportAbuseInput;
            if (loopCycle % 20 < 10) {
                class9.message += "|";
            } else {
                class9.message += " ";
            }
            return;
        }
        if (j == 613) {
            if (myPrivilege >= 1) {
                if (canMute) {
                    class9.textColor = 0xff0000;
                    class9.message = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    class9.textColor = 0xffffff;
                    class9.message = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                class9.message = "";
            }
            return;
        }
        if (j == 650 || j == 655) {
            if (lastLoginIP != 0) {
             /*  String s;
                if (daysSinceLastLogin == 0)
                    s = "earlier today";
                else if (daysSinceLastLogin == 1)
                    s = "yesterday";
                else
                    s = daysSinceLastLogin + " days ago";
                class9.message = "You last logged in " + s + " from: "
                        + signlink.dns;*/
            } else {
                class9.message = "";
            }
            return;
        }
        if (j == 651) {
            if (welcomeScreenUnreadMessagesCount == 0) {
                class9.message = "0 unread messages";
                class9.textColor = 0xffff00;
            }
            if (welcomeScreenUnreadMessagesCount == 1) {
                class9.message = "1 unread message";
                class9.textColor = 65280;
            }
            if (welcomeScreenUnreadMessagesCount > 1) {
                class9.message = welcomeScreenUnreadMessagesCount + " unread messages";
                class9.textColor = 65280;
            }
            return;
        }
        if (j == 652) {
            if (welcomeScreenDaysSinceRecovChange == 201) {
                if (membersInt == 1)
                    class9.message = "<col=ffff00>This is a non-members world: <col=ffffff>Since you are a member we";
                else
                    class9.message = "";
            } else if (welcomeScreenDaysSinceRecovChange == 200) {
                class9.message = "You have not yet set any password recovery questions.";
            } else {
                String s1;
                if (welcomeScreenDaysSinceRecovChange == 0)
                    s1 = "Earlier today";
                else if (welcomeScreenDaysSinceRecovChange == 1)
                    s1 = "Yesterday";
                else
                    s1 = welcomeScreenDaysSinceRecovChange + " days ago";
                class9.message = s1 + " you changed your recovery questions";
            }
            return;
        }
        if (j == 653) {
            if (welcomeScreenDaysSinceRecovChange == 201) {
                if (membersInt == 1)
                    class9.message = "<col=ffffff>recommend you use a members world instead. You may use";
                else
                    class9.message = "";
            } else if (welcomeScreenDaysSinceRecovChange == 200)
                class9.message = "We strongly recommend you do so now to secure your account.";
            else
                class9.message = "If you do not remember making this change then cancel it immediately";
            return;
        }
        if (j == 654) {
            if (welcomeScreenDaysSinceRecovChange == 201)
                if (membersInt == 1) {
                    class9.message = "<col=ffffff>this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    class9.message = "";
                    return;
                }
            if (welcomeScreenDaysSinceRecovChange == 200) {
                class9.message = "Do this from the 'account management' area on our front webpage";
                return;
            }
            class9.message = "Do this from the 'account management' area on our front webpage";
        }
    }

	private void drawSplitPrivateChat(boolean isFixed) {
		if (splitPrivateChat == 0) {
			return;
		}

		int messageCount = 0;
		if (systemUpdateTime != 0) {
			messageCount = 1;
		}

		for (int index = ChatMessageManager.getHighestId(); index != -1; index = ChatMessageManager.getPrevMessageId(index)) {
			ChatMessage message = (ChatMessage) ChatMessageManager.messageHashtable.findNodeByID(index);
			int messageType = message.type;
			String messageName = message.name;

			if ((messageType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER || messageType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF) && (messageType == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(messageName))) {
				String title = message.title;

				int yOff;
				if (isFixed) {
					yOff = 328 - messageCount * 13;
				} else {
					yOff = frameHeight - 169 - messageCount * 13;
				}
				int xOff = 4;
				regular.drawBasicString("From ", xOff, yOff, 65535, 0);
				xOff += regular.getBasicWidth("From ");
				if (title != null && !title.isEmpty()) {
					regular.drawString(title, xOff, yOff);
					xOff += regular.getTextWidth(title);
				}
				regular.drawString(messageName, xOff, yOff, 65535, 0);
				xOff += regular.getTextWidth(messageName);
				regular.drawBasicString(":", xOff, yOff);
				xOff += regular.getBasicWidth(":") + 5;
				regular.drawBasicString(message.message, xOff, yOff, 65535, 0);
				if (++messageCount >= 5) {
					return;
				}
			}
			if (messageType == ChatMessage.PRIVATE_MESSAGE_RECEIVED && privateChatMode < 2) {
				int yOff;
				if (isFixed) {
					yOff = 328 - messageCount * 13;
				} else {
					yOff = frameHeight - 169 - messageCount * 13;
				}
				regular.drawString(message.message, 4, yOff, 65535, 0);
				if (++messageCount >= 5) {
					return;
				}
			}
			if (messageType == ChatMessage.PRIVATE_MESSAGE_SENT && privateChatMode < 2) {
				int yOff;
				if (isFixed) {
					yOff = 328 - messageCount * 13;
				} else {
					yOff = frameHeight - 169 - messageCount * 13;
				}
				int xOff = 4;
				regular.drawBasicString("To ", xOff, yOff, 65535, 0);
				xOff += regular.getBasicWidth("To ");
				regular.drawBasicString(messageName, xOff, yOff, 65535, 0);
				xOff += regular.getBasicWidth(messageName);
				regular.drawBasicString(":", xOff, yOff);
				xOff += regular.getBasicWidth(":") + 5;
				regular.drawBasicString(message.message, xOff, yOff, 65535, 0);
				if (++messageCount >= 5) {
					return;
				}
			}
		}
    }

	public void pushMessage(String message, int type) {
		pushMessage(message, type, null, null);
	}

	public void pushMessage(String message, int type, String name) {
		pushMessage(message, type, name, null);
	}

	protected static int messageCount = 0;
	
	public void pushMessage(String message, int type, String name, String title) {
		if (type == ChatMessage.GAME_MESSAGE && dialogID != -1) {
			aString844 = message;
			super.clickMode3 = 0;
		}
		if (backDialogID == -1) {
			reDrawChatArea = true;
		}
		ChatMessageManager.appendMessage(type, message, title, name);
		messageCount++;
	}

    private void initGameProducers(boolean force) {
        if (gameScreen != null && !force) {
            return;
        }

        fullGameScreen = null;
        titleScreenInitialized = false;
        boolean isFixed = clientSize == 0;
        if (isFixed) {
			chatAreaProducer = new RSImageProducer(516, 165, canvas, true);
			minimapProducer = new RSImageProducer(249, 168, canvas);
			tabAreaProducer = new RSImageProducer(250, 335, canvas, true);
			gameScreen = new RSImageProducer(512, 334, canvas, true);
        } else {
    		chatAreaProducer = null;
    		minimapProducer = null;
    		tabAreaProducer = null;
    		gameScreen = new RSImageProducer(frameWidth, frameHeight, canvas, true);
        }
        fullRedraw = true;
    }

    public String getDocumentBaseHost() {
        return "";
    }

    private void drawHintIcon(Sprite sprite, int targetX, int targetY) {
        int l = targetX * targetX + targetY * targetY;
        if (l <= 360000) {
            markMinimap(sprite, targetX, targetY);
        }
    }

    private void addChatButtonOptions(int startY) {
        if (super.mouseX >= CHAT_BUTTONS_POS_X[0] && super.mouseX <= CHAT_BUTTONS_POS_X[0] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("View All", 999);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[1] && super.mouseX <= CHAT_BUTTONS_POS_X[1] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("View Game", 998);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[2] && super.mouseX <= CHAT_BUTTONS_POS_X[2] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("Hide Public", 997);
            MiniMenu.addOption("Off Public", 996);
            MiniMenu.addOption("Friends Public", 995);
            MiniMenu.addOption("On Public", 994);
            MiniMenu.addOption("View Public", 993);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[3] && super.mouseX <= CHAT_BUTTONS_POS_X[3] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("Off Private", 992);
            MiniMenu.addOption("Friends Private", 991);
            MiniMenu.addOption("On Private", 990);
            MiniMenu.addOption("View Private", 989);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[4] && super.mouseX <= CHAT_BUTTONS_POS_X[4] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("Off Clan chat", 1003);
            MiniMenu.addOption("Friends Clan chat", 1002);
            MiniMenu.addOption("On Clan chat", 1001);
            MiniMenu.addOption("View Clan chat", 1000);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[5] && super.mouseX <= CHAT_BUTTONS_POS_X[5] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("Off Trade", 987);
            MiniMenu.addOption("Friends Trade", 986);
            MiniMenu.addOption("On Trade", 985);
            MiniMenu.addOption("View Trade", 984);
        } else if (super.mouseX >= CHAT_BUTTONS_POS_X[6] && super.mouseX <= CHAT_BUTTONS_POS_X[6] + 56
                && super.mouseY >= startY
                && super.mouseY <= frameHeight) {
            MiniMenu.addOption("Off Challenge", 983);
            MiniMenu.addOption("Friends Challenge", 982);
            MiniMenu.addOption("On Challenge", 981);
            MiniMenu.addOption("View Challenge", 980);
        }
    }

    public void processRightClick(boolean isFixed) {
        if (activeInterfaceType != 0) {
            return;
        }
        MiniMenu.clear();
        if (fullscreenInterfaceID != -1) {
            anInt886 = 0;
            anInt1315 = 0;
            buildInterfaceMenu(8,
                    RSInterface.interfaceCache[fullscreenInterfaceID],
                    super.mouseX, 8, super.mouseY, 0);
            if (anInt886 != anInt1026) {
                anInt1026 = anInt886;
            }
            if (anInt1315 != anInt1129) {
                anInt1129 = anInt1315;
            }
            return;
        }
        if (showChat) {
            buildSplitPrivateChatMenu(isFixed);
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (isFixed) {
            if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338) {
                if (openInterfaceID != -1) {
                    buildInterfaceMenu(4, RSInterface.interfaceCache[openInterfaceID], super.mouseX, 4, super.mouseY, 0);
                } else {
                    build3dScreenMenu();
                }
            }
        } else {
            if (canClick()) {
                int w = 512, h = 334;
                int x = (frameWidth / 2) - 256, y = (frameHeight / 2) - 167;
                int x2 = (frameWidth / 2) + 256, y2 = (frameHeight / 2) + 167;
                int count = 3;
                for (int i = 0; i < count; i++) {
                	if (x + w > (frameWidth - 225)) {
                		x = x - 30;
                		x2 = x2 - 30;
                		if (x < 0) {
                			x = 0;
                		}
                	}
                	if (y + h > (frameHeight - 182)) {
                		y = y - 30;
                		y2 = y2 - 30;
                		if (y < 0) {
                			y = 0;
                		}
                	}
                }
                if (openInterfaceID != -1 && super.mouseX > x && super.mouseY > y && super.mouseX < x2 && super.mouseY < y2) {
                    buildInterfaceMenu(x, RSInterface.interfaceCache[openInterfaceID], super.mouseX, y, super.mouseY, 0);
                } else {
                    build3dScreenMenu();
                }
            }
        }
        if (anInt886 != anInt1026) {
            anInt1026 = anInt886;
        }
        if (anInt1315 != anInt1129) {
            anInt1129 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (isFixed) {
            if (super.mouseX > frameWidth - 218
                    && super.mouseY > frameHeight - 298
                    && super.mouseX < frameWidth - 25
                    && super.mouseY < frameHeight - 35) {
                if (invOverlayInterfaceID != -1)
                    buildInterfaceMenu(frameWidth - 218,
                            RSInterface.interfaceCache[invOverlayInterfaceID],
                            super.mouseX, frameHeight - 298, super.mouseY, 0);
                else if (tabInterfaceIDs[tabID] != -1)
                    buildInterfaceMenu(frameWidth - 218,
                            RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
                            super.mouseX, frameHeight - 298, super.mouseY, 0);
            }
        } else if (showTab) {
            int y = frameWidth >= 1000 ? 37 : 74;
            if (frameWidth >= 1000) {
                if (super.mouseX > frameWidth - 197
                        && super.mouseY > frameHeight - y - 267
                        && super.mouseX < frameWidth - 7
                        && super.mouseY < frameHeight - y - 7) {
                    if (invOverlayInterfaceID != -1) {
                        buildInterfaceMenu(
                                frameWidth - 197,
                                RSInterface.interfaceCache[invOverlayInterfaceID],
                                super.mouseX, frameHeight - 304, super.mouseY,
                                0);
                    } else if (tabInterfaceIDs[tabID] != -1) {
                        buildInterfaceMenu(
                                frameWidth - 197,
                                RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
                                super.mouseX, frameHeight - 304, super.mouseY,
                                0);
                    }
                }
            }
            if (frameWidth < 1000) {
                if (super.mouseX > frameWidth - 197
                        && super.mouseY > frameHeight - y - 267
                        && super.mouseX < frameWidth - 7
                        && super.mouseY < frameHeight - y - 7) {
                    if (invOverlayInterfaceID != -1) {
                        buildInterfaceMenu(
                                frameWidth - 197,
                                RSInterface.interfaceCache[invOverlayInterfaceID],
                                super.mouseX, frameHeight - y - 267,
                                super.mouseY, 0);
                    } else if (tabInterfaceIDs[tabID] != -1) {
                        buildInterfaceMenu(
                                frameWidth - 197,
                                RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
                                super.mouseX, frameHeight - y - 267,
                                super.mouseY, 0);
                    }
                }
            }
        }
        if (anInt886 != anInt1048) {
            reDrawTabArea = true;
            anInt1048 = anInt886;
        }
        if (anInt1315 != anInt1044) {
            reDrawTabArea = true;
            anInt1044 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (showChat && super.mouseX > 0 && super.mouseY > (isFixed ? 338 : frameHeight - 165) && super.mouseX < 490 && super.mouseY < (isFixed ? 463 : frameHeight - 40)) {
            if (backDialogID != -1) {
                buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], super.mouseX, (isFixed ? 358 : frameHeight - 145), super.mouseY, 0);
            } else if (inputDialogState == 3) {
                buildItemSearch(super.mouseY - (isFixed ? 338 : frameHeight - 165), isFixed);
            } else if (super.mouseY < (isFixed ? 463 : frameHeight - 40) && super.mouseX < 490) {
                buildChatAreaMenu(super.mouseY - (isFixed ? 338 : frameHeight - 165));
            }
        }
        if (backDialogID != -1 && anInt886 != anInt1039) {
            reDrawChatArea = true;
            anInt1039 = anInt886;
        }
        if (backDialogID != -1 && anInt1315 != anInt1500) {
            reDrawChatArea = true;
            anInt1500 = anInt1315;
        }
        int startY = frameHeight - 23;
        if (super.mouseX >= 4 && super.mouseY >= startY && super.mouseX <= 516 && super.mouseY <= frameHeight) {
            addChatButtonOptions(startY);
        }
        addMinimapOptions(isFixed);
        if (invOverlayInterfaceID == -1) {
            addTabOptions(isFixed);
        }

        MiniMenu.highPriorityOptions.clear();
        for (MiniMenuOption menuOption = (MiniMenuOption) MiniMenu.options.reverseGetFirst(); menuOption != null; menuOption = (MiniMenuOption) MiniMenu.options.reverseGetNext()) {
        	if (menuOption.uid < 1000) {
        		menuOption.unlink();
        		MiniMenu.highPriorityOptions.insertHead(menuOption);
        	}
        }

        MiniMenu.highPriorityOptions.transfer(MiniMenu.options);
    }

    public boolean canClick() {
        if (mouseInRegion(frameWidth - (frameWidth < 900 ? 240 : 480),
                frameHeight - (frameWidth < 900 ? 90 : 37), frameWidth,
                frameHeight)) {
            return false;
        }
        if (showChat) {
            if (super.mouseX > 0 && super.mouseX < 519
                    && super.mouseY > frameHeight - 165
                    && super.mouseY < frameHeight
                    || super.mouseX > frameWidth - 220
                    && super.mouseX < frameWidth && super.mouseY > 0
                    && super.mouseY < 165) {
                return false;
            }
        }
        if (mouseInRegion(frameWidth - 216, 0, frameWidth, 172)) {
            return false;
        }
        if (showTab) {
            if (frameWidth >= 900) {
                if (super.mouseX >= frameWidth - 420
                        && super.mouseX <= frameWidth
                        && super.mouseY >= frameHeight - 37
                        && super.mouseY <= frameHeight
                        || super.mouseX > frameWidth - 204
                        && super.mouseX < frameWidth
                        && super.mouseY > frameHeight - 37 - 274
                        && super.mouseY < frameHeight)
                    return false;
            } else {
                if (super.mouseX >= frameWidth - 210
                        && super.mouseX <= frameWidth
                        && super.mouseY >= frameHeight - 74
                        && super.mouseY <= frameHeight
                        || super.mouseX > frameWidth - 204
                        && super.mouseX < frameWidth
                        && super.mouseY > frameHeight - 74 - 274
                        && super.mouseY < frameHeight)
                    return false;
            }
        }
        return true;
    }

    private void login(String s, String s1, boolean flag) {
        try {
            if (myUsername.length() < 1) {
                loginMessage2 = "Please enter a valid username";
                return;
            } else if (myPassword.length() < 1) {
                loginMessage2 = "Please enter a valid password";
                return;
            }
            if (!flag) {
                loginMessage1 = "";
                loginMessage2 = "Connecting to server...";
                drawLoginScreen();
            }
			socketStream = new RSSocket(this, openSocket(43594 + portOff));
            long l = TextClass.longForName(s);
            int i = (int) (l >> 16 & 31L);
            stream.currentOffset = 0;
            stream.putByte(14);
            stream.putByte(i);
            socketStream.queueBytes(2, stream.buffer);
            for (int j = 0; j < 8; j++)
                socketStream.read();

            int k = socketStream.read();
            int i1 = k;
            if (k == 0) {
                socketStream.flushInputStream(inStream.buffer, 0, 8);
                inStream.currentOffset = 0;
                aLong1215 = inStream.readQWord();
                int ai[] = new int[4];
                ai[0] = (int) (Math.random() * 99999999D);
                ai[1] = (int) (Math.random() * 99999999D);
                ai[2] = (int) (aLong1215 >> 32);
                ai[3] = (int) aLong1215;
                stream.currentOffset = 0;
                stream.putByte(10);
                stream.putInt(ai[0]);
                stream.putInt(ai[1]);
                stream.putInt(ai[2]);
                stream.putInt(ai[3]);
                stream.putInt((350 >> 2240));
                stream.putString(s);
                stream.putString(s1);
                stream.putString(getUUID());
                stream.doKeys();
                aStream_847.currentOffset = 0;
                if (flag)
                    aStream_847.putByte(18);
                else
                    aStream_847.putByte(16);
                aStream_847.putByte(stream.currentOffset + 36 + 1 + 1 + 2);

                //aStream_847.writeWordBigEndian(255);
                aStream_847.putShort(317);
                aStream_847.putString(Constants.CLIENT_VERSION);
                aStream_847.putByte(lowMem ? 1 : 0);

                for (int l1 = 0; l1 < 9; l1++)
                    aStream_847.putInt(expectedCRCs[l1]);

                aStream_847.putBytes(stream.buffer, stream.currentOffset, 0);
                stream.encryption = new ISAACRandomGen(ai);
                for (int j2 = 0; j2 < 4; j2++)
                    ai[j2] += 50;

                encryption = new ISAACRandomGen(ai);
                socketStream.queueBytes(aStream_847.currentOffset,
                        aStream_847.buffer);
                k = socketStream.read();
            }

            System.out.println("return code " + k);

            if (k == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception _ex) {
                }
                login(s, s1, flag);
                return;
            }
            if (k == 2) {
                loadGoals(myUsername);
                Settings.write();
                xpCounter = 0;

                myPrivilege = socketStream.read();
                socketStream.read();
                aLong1220 = 0L;
                super.awtFocus = true;
                aBoolean954 = true;
                loggedIn = true;
                stream.currentOffset = 0;
                inStream.currentOffset = 0;
                pktType = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                pktSize = 0;
                anInt1009 = 0;
                systemUpdateTime = 0;
                anInt1011 = 0;
                headiconDrawType = 0;
                super.idleTime = 0;
                itemSelected = 0;
                targetInterfaceId = 0;
                loadingStage = 0;
                anInt1062 = 0;
                setNorth();
                minimapLock = 0;
                anInt985 = -1;
                destX = 0;
                destY = 0;
                playerCount = 0;
                npcCount = 0;
                for (int i2 = 0; i2 < maxPlayers; i2++) {
                    playerArray[i2] = null;
                    playerUpdateStreams[i2] = null;
                }

                for (int k2 = 0; k2 < 16384; k2++)
                    npcArray[k2] = null;

                myPlayer = playerArray[myPlayerIndex] = new Player();
                projectileNode.clear();
                stillGraphicsNode.clear();
                for (int l2 = 0; l2 < 4; l2++) {
                    for (int i3 = 0; i3 < 104; i3++) {
                        for (int k3 = 0; k3 < 104; k3++)
                            groundArray[l2][i3][k3] = null;

                    }

                }

                ChatMessageManager.clear();

                aClass19_1179 = new NodeList();
                fullscreenInterfaceID = -1;
                networkFriendsServerStatus = 0;
                friendsCount = 0;
                dialogID = -1;
                backDialogID = -1;
                openInterfaceID = -1;
                invOverlayInterfaceID = -1;
                currentStatusInterface = -1;
                isDialogueInterface = false;
                tabID = INVENTORY_TAB;
                inputDialogState = 0;
                messagePromptRaised = false;
                aString844 = null;
                anInt1055 = 0;
                aBoolean1047 = true;
                method45();
                for (int j3 = 0; j3 < 5; j3++)
                    anIntArray990[j3] = 0;

                for (int l3 = 0; l3 < 5; l3++) {
                    atPlayerActions[l3] = null;
                    atPlayerArray[l3] = false;
                }

                initGameProducers(false);
                return;
            }
            if (k == 3) {
                loginMessage1 = "";
                loginMessage2 = "Invalid username or password.";
                return;
            }
            if (k == 4) {
                loginMessage1 = "Your account has been disabled.";
                loginMessage2 = "Please check your message-center for details.";
                return;
            }
            if (k == 5) {
                loginMessage1 = "Your account is already logged in.";
                loginMessage2 = "Try again in 60 secs...";
                return;
            }
            if (k == 6) {
                loginMessage1 = Constants.CLIENT_NAME + " has been updated!";
                loginMessage2 = "Please reload this page.";
                return;
            }
            if (k == 7) {
                loginMessage1 = "This world is full.";
                loginMessage2 = "Please use a different world.";
                return;
            }
            if (k == 8) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Login server offline.";
                return;
            }
            if (k == 9) {
                loginMessage1 = "Login limit exceeded.";
                loginMessage2 = "Too many connections from your address.";
                return;
            }
            if (k == 10) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Bad session id.";
                return;
            }
            if (k == 11) {
                loginMessage1 = "Login server rejected session.";
                loginMessage2 = "Please try again.";
                return;
            }
            if (k == 12) {
                loginMessage1 = "You need a members account to login to this world.";
                loginMessage2 = "Please subscribe, or use a different world.";
                return;
            }
            if (k == 13) {
                loginMessage1 = "Could not complete login.";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 14) {
                loginMessage1 = "The server is being updated.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 15) {
                loggedIn = true;
                stream.currentOffset = 0;
                inStream.currentOffset = 0;
                pktType = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                pktSize = 0;
                anInt1009 = 0;
                systemUpdateTime = 0;
                MiniMenu.optionCount = 0;
                MiniMenu.open = false;
                aLong824 = System.currentTimeMillis();
                return;
            }
            if (k == 16) {
                loginMessage1 = "Login attempts exceeded.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 17) {
                loginMessage1 = "Anti-bot measure";
                loginMessage2 = "Please answer the recaptcha in the browser window just opened.";

                launchURL("https://www.crandor.net/antibot");
                return;
            }
            if (k == 20) {
                loginMessage1 = "Invalid loginserver requested";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 21) {
                for (int k1 = socketStream.read(); k1 >= 0; k1--) {
                    loginMessage1 = "You have only just left another world";
                    loginMessage2 = "Your profile will be transferred in: "
                            + k1 + " seconds";
                    drawLoginScreen();
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }

                login(s, s1, flag);
                return;
            }
            if (k == -1) {
                if (i1 == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception _ex) {
                        }
                        loginFailures++;
                        login(s, s1, flag);
                        return;
                    } else {
                        loginMessage1 = "No response from loginserver";
                        loginMessage2 = "Please wait 1 minute and try again.";
                        return;
                    }
                } else {
                    loginMessage1 = "No response from server";
                    loginMessage2 = "Please try using a different world.";
                    return;
                }
            } else {
                System.out.println("response:" + k);
                loginMessage1 = "Unexpected server response";
                loginMessage2 = "Please try using a different world.";
                return;
            }
        } catch (IOException _ex) {
            loginMessage1 = "";
        }
        loginMessage2 = "Error connecting to server.";
    }

    private boolean doWalkTo(int i, int j, int k, int i1, int j1, int k1,
                             int l1, int i2, int j2, boolean flag, int k2) {
        for (int l2 = 0; l2 < 104; l2++) {
            for (int i3 = 0; i3 < 104; i3++) {
                anIntArrayArray901[l2][i3] = 0;
                anIntArrayArray825[l2][i3] = 0x5f5e0ff;
            }
        }
        int j3 = j2;
        int k3 = j1;
        anIntArrayArray901[j2][j1] = 99;
        anIntArrayArray825[j2][j1] = 0;
        int l3 = 0;
        int i4 = 0;
        bigX[l3] = j2;
        bigY[l3++] = j1;
        boolean flag1 = false;
        int j4 = bigX.length;
        int ai[][] = tileSettings[plane].clipData;
        while (i4 != l3) {
            j3 = bigX[i4];
            k3 = bigY[i4];
            i4 = (i4 + 1) % j4;
            if (j3 == k2 && k3 == i2) {
                flag1 = true;
                break;
            }
            if (i1 != 0) {
                if ((i1 < 5 || i1 == 10)
                        && tileSettings[plane].isWalkableA(k2, j3, k3, j,
                        i1 - 1, i2)) {
                    flag1 = true;
                    break;
                }
                if (i1 < 10
                        && tileSettings[plane].method220(k2, i2, k3, i1 - 1, j,
                        j3)) {
                    flag1 = true;
                    break;
                }
            }
            if (k1 != 0
                    && k != 0
                    && tileSettings[plane].isWalkableB(i2, k2, j3, k, l1, k1,
                    k3)) {
                flag1 = true;
                break;
            }
            int l4 = anIntArrayArray825[j3][k3] + 1;
            if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0
                    && (ai[j3 - 1][k3] & 0x1280108) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3] = 2;
                anIntArrayArray825[j3 - 1][k3] = l4;
            }
            if (j3 < 103 && anIntArrayArray901[j3 + 1][k3] == 0
                    && (ai[j3 + 1][k3] & 0x1280180) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3] = 8;
                anIntArrayArray825[j3 + 1][k3] = l4;
            }
            if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0
                    && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 - 1] = 1;
                anIntArrayArray825[j3][k3 - 1] = l4;
            }
            if (k3 < 103 && anIntArrayArray901[j3][k3 + 1] == 0
                    && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 + 1] = 4;
                anIntArrayArray825[j3][k3 + 1] = l4;
            }
            if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0
                    && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0
                    && (ai[j3 - 1][k3] & 0x1280108) == 0
                    && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 - 1] = 3;
                anIntArrayArray825[j3 - 1][k3 - 1] = l4;
            }
            if (j3 < 103 && k3 > 0
                    && anIntArrayArray901[j3 + 1][k3 - 1] == 0
                    && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0
                    && (ai[j3 + 1][k3] & 0x1280180) == 0
                    && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 - 1] = 9;
                anIntArrayArray825[j3 + 1][k3 - 1] = l4;
            }
            if (j3 > 0 && k3 < 103
                    && anIntArrayArray901[j3 - 1][k3 + 1] == 0
                    && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0
                    && (ai[j3 - 1][k3] & 0x1280108) == 0
                    && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 + 1] = 6;
                anIntArrayArray825[j3 - 1][k3 + 1] = l4;
            }
            if (j3 < 103 && k3 < 103
                    && anIntArrayArray901[j3 + 1][k3 + 1] == 0
                    && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0
                    && (ai[j3 + 1][k3] & 0x1280180) == 0
                    && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 + 1] = 12;
                anIntArrayArray825[j3 + 1][k3 + 1] = l4;
            }
        }
        anInt1264 = 0;
        if (!flag1) {
            if (flag) {
                int i5 = 100;
                for (int k5 = 1; k5 < 2; k5++) {
                    for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
                        for (int l6 = i2 - k5; l6 <= i2 + k5; l6++)
                            if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104
                                    && anIntArrayArray825[i6][l6] < i5) {
                                i5 = anIntArrayArray825[i6][l6];
                                j3 = i6;
                                k3 = l6;
                                anInt1264 = 1;
                                flag1 = true;
                            }

                    }

                    if (flag1)
                        break;
                }

            }
            if (!flag1)
                return false;
        }
        i4 = 0;
        bigX[i4] = j3;
        bigY[i4++] = k3;
        int l5;
        for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 = anIntArrayArray901[j3][k3]) {
            if (j5 != l5) {
                l5 = j5;
                bigX[i4] = j3;
                bigY[i4++] = k3;
            }
            if ((j5 & 2) != 0)
                j3++;
            else if ((j5 & 8) != 0)
                j3--;
            if ((j5 & 1) != 0)
                k3++;
            else if ((j5 & 4) != 0)
                k3--;
        }
        if (i4 > 0) {
            int k4 = i4;
            if (k4 > 25)
                k4 = 25;
            i4--;
            int k6 = bigX[i4];
            int i7 = bigY[i4];
            if (i == 0) {
                stream.createFrame(164);
                stream.putByte(k4 + k4 + 3);
            }
            if (i == 1) {
                stream.createFrame(248);
                stream.putByte(k4 + k4 + 3 + 14);
            }
            if (i == 2) {
                stream.createFrame(98);
                stream.putByte(k4 + k4 + 3);
            }
            stream.writeSignedShortBigEndianA(k6 + baseX);
            destX = bigX[0];
            destY = bigY[0];
            for (int j7 = 1; j7 < k4; j7++) {
                i4--;
                stream.putByte(bigX[i4] - k6);
                stream.putByte(bigY[i4] - i7);
            }

            stream.method431(i7 + baseY);
            stream.method424(super.keyArray[5] != 1 ? 0 : 1);
            return true;
        }
        return i != 1;
    }

    private void readNpcUpdateMask(Stream stream) {
        for (int j = 0; j < sessionNpcsAwaitingUpdatePtr; j++) {
            int k = sessionNpcsAwaitingUpdate[j];
            NPC npc = npcArray[k];
            int l = stream.readUnsignedByte();
            if ((l & 0x10) != 0) {
                int i1 = stream.method434();
                if (i1 == 65535)
                    i1 = -1;
                int i2 = stream.readUnsignedByte();
                if (i1 == npc.animId && i1 != -1) {
                    int l2 = Animation.anims[i1].anInt365;
                    if (l2 == 1) {
                        npc.animFrame = 0;
                        npc.animNextFrame = 1;
                        npc.animFrameDelay = 0;
                        npc.animDelay = i2;
                        npc.animCyclesElapsed = 0;
                    }
                    if (l2 == 2)
                        npc.animCyclesElapsed = 0;
                } else if (i1 == -1
                        || npc.animId == -1
                        || Animation.anims[i1].anInt359 >= Animation.anims[npc.animId].anInt359) {
                    npc.animId = i1;
                    npc.animFrame = 0;
                    npc.animNextFrame = 1;
                    npc.animFrameDelay = 0;
                    npc.animDelay = i2;
                    npc.animCyclesElapsed = 0;
                    npc.anInt1542 = npc.smallXYIndex;
                }
            }
            if ((l & 8) != 0) {

                int damage = inStream.readUnsignedWordA();
                int mask = stream.readUnsignedByte();
                int icon = stream.readUnsignedByte();

                //int damagerIndex = inStream.readUnsignedWordA();
                //if (mask == 0 && damagerIndex != myPlayer.playerIndex)
                //    mask = 12; // for testing
                if (Settings.hitmarks != Constants.HITMARKS_634 && mask != 2) {
                    if (damage > 0)
                        mask = 1;
                    else
                        mask = 0;
                    npc.updateHitDataOld(mask, getHit(damage), loopCycle);
                } else
                    npc.updateHitDataNew(mask, getHit(damage), loopCycle, icon);

                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = inStream.readUnsignedWordA();
                npc.maxHealth = inStream.readUnsignedWordA();
                /*
                 * System.out.println("[ Single ] Damage: " + j1 + ", Mask: " +
				 * mask + ", Icon: " + icon + ", Current Hp: " +
				 * npc.currentHealth + ", Max Hp: " + npc.maxHealth);
				 */
            }
            if ((l & 0x80) != 0) {
                npc.spotAnimId = stream.readUnsignedWord();
                int k1 = stream.readDWord();
                npc.anInt1524 = k1 >> 16;
                npc.spotAnimDelay = loopCycle + (k1 & 0xffff);
                npc.spotAnimFrame = 0;
                npc.spotAnimNextFrame = 1;
                npc.spotAnimFrameDelay = 0;
                if (npc.spotAnimDelay > loopCycle)
                    npc.spotAnimFrame = -1;
                if (npc.spotAnimId == 65535)
                    npc.spotAnimId = -1;
            }
            if ((l & 0x20) != 0) {
                npc.interactingEntity = stream.readUnsignedWord();
                if (npc.interactingEntity == 65535)
                    npc.interactingEntity = -1;
            }
            if ((l & 1) != 0) {
                npc.textSpoken = stream.readString();
                npc.textCycle = 100;
            }
            if ((l & 0x40) != 0) {
                int l1 = inStream.readUnsignedWordA();
                int mask = stream.readUnsignedByte();
                int icon = stream.readUnsignedByte();

                //int damagerIndex = inStream.readUnsignedWordA();
                //if (mask == 0 && damagerIndex != myPlayer.playerIndex)
                //    mask = 12; // for testing

                if (Settings.hitmarks != Constants.HITMARKS_634
                        && mask != 2) {
                    if (l1 > 0)
                        mask = 1;
                    else
                        mask = 0;
                    npc.updateHitDataOld(mask, getHit(l1), loopCycle);
                } else
                    npc.updateHitDataNew(mask, getHit(l1), loopCycle, icon);

                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = inStream.readUnsignedWordA();
                npc.maxHealth = inStream.readUnsignedWordA();
                /*
                 * System.out.println("[ Double ] Damage: " + l1 + ", Mask: " +
				 * mask + ", Icon: " + icon + ", Current Hp: " +
				 * npc.currentHealth + ", Max Hp: " + npc.maxHealth);
				 */
            }
            if ((l & 2) != 0) {
                npc.desc = NpcDefinition.forID(stream.method436());
                npc.size = npc.desc.npcSize;
                npc.degreesToTurn = npc.desc.degreesToTurn;
                npc.walkAnimIndex = npc.desc.walkAnim;
                npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
                npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
                npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
                npc.standAnimIndex = npc.desc.standAnim;
            }
            if ((l & 4) != 0) {
                npc.anInt1538 = stream.method434();
                npc.anInt1539 = stream.method434();
            }
        }
    }

    private void buildAtNPCMenu(NpcDefinition npcDef, int i, int j, int k) {
        if (MiniMenu.optionCount >= 400)
            return;
        if (npcDef.childrenIDs != null)
            npcDef = npcDef.morph();
        if (npcDef == null)
            return;
        if (!npcDef.clickable)
            return;
        String s = npcDef.name;

        if (npcDef.type != 14205 && npcDef.combatLevel != 0)
            s = s
                    + combatDiffColor(myPlayer.combatLevel,
                    npcDef.combatLevel) + " (level-"
                    + npcDef.combatLevel + ")";
        if (itemSelected == 1) {
            MiniMenu.addOption("Use " + selectedItemName + " with", "<col=ffff00>" + s, 582, i, k, j);
            return;
        }

        if (targetInterfaceId != 0 && (targetBitMask & 0x2) != 0) {
        	MiniMenu.addOption(targetOption, "<col=ffff00>" + s, 413, i, k, j);
        	return;
        }

		if (npcDef.actions != null) {
			for (int l = 4; l >= 0; l--) {
				if (npcDef.actions[l] != null && !npcDef.actions[l].equalsIgnoreCase("attack")) {
					int option = 0;
					if (l == 0) {
						option = 20;
					} else if (l == 1) {
						option = 412;
					} else if (l == 2) {
						option = 225;
					} else if (l == 3) {
						option = 965;
					} else if (l == 4) {
						option = 478;
					}

					MiniMenu.addOption(npcDef.actions[l], "<col=ffff00>" + s, option, i, k, j);
				}
			}
			for (int l = 4; l >= 0; l--) {
				if (npcDef.actions[l] != null && npcDef.actions[l].equalsIgnoreCase("attack")) {
					int option = 0;
					if (l == 0) {
						option = 20;
					} else if (l == 1) {
						option = 412;
					} else if (l == 2) {
						option = 225;
					} else if (l == 3) {
						option = 965;
					} else if (l == 4) {
						option = 478;
					}

					MiniMenu.addOption(npcDef.actions[l], "<col=ffff00>" + s, option, i, k, j);
				}
			}
		}

		MiniMenu.addOption("Examine", "<col=ffff00>" + s, 1025, i, k, j);
	}

    private void buildAtPlayerMenu(int i, int j, Player player, int k) {
        if (player == myPlayer)
            return;
        if (MiniMenu.optionCount >= 400)
            return;
        String s;

        if (player.titleText != null) {
            s = player.titleText + " <col=FFFFFF>" + player.name + "</col>";
        } else {
            s = "<col=FFFFFF>" + player.name + "</col>";
        }

        if (player.skill == 0) {
            s = s + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + "" + (player.summoning > 0 ? "+" + player.summoning : "") + ")";
        } else {
            s = s + " (skill-" + player.skill + ")";
        }

        if (itemSelected == 1) {
            MiniMenu.addOption("Use " + selectedItemName + " with", "<col=ffffff>" + s, 491, j, i, k);
        } else if (targetInterfaceId != 0) {
            if ((targetBitMask & 8) == 8) {
                MiniMenu.addOption(targetOption, "<col=ffffff>" + s, 365, j, i, k);
            }
        } else {
			for (int l = 4; l >= 0; l--) {
				if (atPlayerActions[l] != null) {
					int option = 0;
					char c = '\0';
					if (atPlayerArray[l] && !atPlayerActions[l].equalsIgnoreCase("attack")) {
						c = '\u07D0';
					}

					if (l == 0) {
						option = 561 + c;
					} else if (l == 1) {
						option = 779 + c;
					} else if (l == 2) {
						option = 27 + c;
					} else if (l == 3) {
						option = 577 + c;
					} else if (l == 4) {
						option = 729 + c;
					}
	
					MiniMenu.addOption(atPlayerActions[l], "<col=ffffff>" + s, option, j, i, k);
				}
			}
        }

        for (MiniMenuOption menuOption = (MiniMenuOption) MiniMenu.options.reverseGetFirst(); menuOption != null; menuOption = (MiniMenuOption) MiniMenu.options.reverseGetNext()) {
            if (menuOption.uid == 516) {
            	menuOption.base = "<col=ffffff>" + s;
                return;
            }
        }
    }

    private void method89(GameObjectSpawnRequest class30_sub1) {
        long i = 0L;
        int j = -1;
        int k = 0;
        int l = 0;
        if (class30_sub1.anInt1296 == 0) {
            i = worldController.method300(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        }
        if (class30_sub1.anInt1296 == 1) {
            i = worldController.method301(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        }
        if (class30_sub1.anInt1296 == 2) {
            i = worldController.method302(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        }
        if (class30_sub1.anInt1296 == 3) {
            i = worldController.method303(class30_sub1.plane, class30_sub1.x,
                    class30_sub1.y);
        }
        if (i != 0L) {
            j = (int) (i >>> 32) & 0x7fffffff;
            k = (int) (i >> 14) & 0x1f;
            l = (int) (i >> 20) & 0x3;
        }
        class30_sub1.anInt1299 = j;
        class30_sub1.anInt1301 = k;
        class30_sub1.anInt1300 = l;
    }

    private void method90() {
        for (int i = 0; i < anInt1062; i++)
            if (anIntArray1250[i] <= 0) {
                boolean flag1 = false;
                try {
                    if (songIDS[i] == anInt874 && songVolumes[i] == anInt1289) {
                    	flag1 = true;
                    } else {
                        Stream stream = Sounds.method241(songVolumes[i],
                                songIDS[i]);
                        if (System.currentTimeMillis() + stream.currentOffset
                                / 22 > aLong1172 + anInt1257 / 22) {
                            anInt1257 = stream.currentOffset;
                            aLong1172 = System.currentTimeMillis();
                            if (saveWave(stream.buffer, stream.currentOffset)) {
                                anInt874 = songIDS[i];
                                anInt1289 = songVolumes[i];
                            } else {
                                flag1 = true;
                            }
                        }
                    }
                } catch (Exception exception) {
                }
                if (!flag1 || anIntArray1250[i] == -5) {
                    anInt1062--;
                    for (int j = i; j < anInt1062; j++) {
                        songIDS[j] = songIDS[j + 1];
                        songVolumes[j] = songVolumes[j + 1];
                        anIntArray1250[j] = anIntArray1250[j + 1];
                    }

                    i--;
                } else {
                    anIntArray1250[i] = -5;
                }
            } else {
                anIntArray1250[i]--;
            }

        if (prevSong > 0) {
            prevSong -= 20;
            if (prevSong < 0)
                prevSong = 0;
            if (prevSong == 0 && musicEnabled && !lowMem) {
                nextSong = currentSong;
                songChanging = true;
                onDemandFetcher.loadToCache(2, nextSong);
            }
        }
    }

    @Override
    void mainInit() {

        toggleSize(clientSize, true);

        drawLoadingText(20, "Starting up");

        if (signlink.cache_dat != null) {
            for (int i = 0; i < 7; i++) {
                decompressors[i] = new Decompressor(signlink.cache_dat, signlink.cache_idx[i], i + 1);
                long count = decompressors[i].getFileCount();
                //System.out.println(i + " " + count);
            }
        }
        try {

            // SizeConstants.size(171);
            // RGBTexture.createTexture(30);
            // RGBTexture.createTexture(33);

            if (Constants.REPACK_INDEX > -1) {
                repackCacheIndex(Constants.REPACK_INDEX);
            }

            // repackCacheIndex(1); // models and shit
            //repackCacheIndex(4); // maps
            //  repackCacheIndex(2); // anims

            //repackCacheIndex(6); // osrs

            StreamLoader configArchive = streamLoaderForName(2, "config", "config", expectedCRCs[2], 25);

            drawLoadingText(30, "Loading title screen.");
            SpriteLoader.loadSprites(configArchive);
            Client.instance.cacheSprite = SpriteLoader.sprites;

            titleStreamLoader = streamLoaderForName(1, "title screen", "title", expectedCRCs[1], 30);
            //discardLoadingVariales();

            smallHit = new RSFont(false, "hit_full", titleStreamLoader);
            bigHit = new RSFont(true, "critical_full", titleStreamLoader);
            bold = new RSFont(false, "b12_full", titleStreamLoader);
            fancy = new RSFont(true, "q8_full", titleStreamLoader);
            small = new RSFont(false, "p11_full", titleStreamLoader);
            regular = new RSFont(false, "p12_full", titleStreamLoader);

            StreamLoader interfaceArchive = streamLoaderForName(3, "interface", "interface", expectedCRCs[3], 35);
            StreamLoader mediaArchive = streamLoaderForName(4, "2d graphics", "media", expectedCRCs[4], 40);
            // StreamLoader censorArchive = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
            drawLoadingText(40, "Connecting to update Server");
            tileSettingBits = new byte[4][104][104];
            intGroundArray = new int[4][105][105];
            worldController = new WorldController(intGroundArray);
            for (int j = 0; j < 4; j++)
                tileSettings[j] = new TileSetting();
            minimapImage = new Sprite(512, 512);
            StreamLoader crcArchive = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
            onDemandFetcher = new OnDemandFetcher();
            onDemandFetcher.start(crcArchive, this);

            FrameReader.initialize(onDemandFetcher.getCrcCount(1));
            Model.initialize(onDemandFetcher.getCrcCount(0));
            Texture.initialize(onDemandFetcher.getCrcCount(4));

            StreamLoader streamLoader_4 = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);


            spriteCache = new Zip();
            spriteCache.load(FileOperations.ReadFile(signlink.findcachedir() + "sprites.zip"));

            drawLoadingText(50, "Unpacking media");
            moneyPouchSprites = new Sprite[6];
            for (int i = 0; i < 6; i++) {
                moneyPouchSprites[i] = new Sprite(spriteCache.getFile("moneypouch/pouch " + i + ".png"));
            }
            loyalty1 = RSInterface.imageLoader(1, "loyalty/tab");
            loyalty2 = RSInterface.imageLoader(2, "loyalty/tab");
            per4 = RSInterface.imageLoader(647, "ge");
            per5 = RSInterface.imageLoader(648, "ge");
            per6 = RSInterface.imageLoader(649, "ge");
            abort2 = RSInterface.imageLoader(651, "ge");
            SellHover = RSInterface.imageLoader(655, "ge");
            BuyHover656 = RSInterface.imageLoader(656, "ge");
            BuyHover654 = RSInterface.imageLoader(654, "ge");
            geImage30 = RSInterface.imageLoader(30, "ge");
            per0 = RSInterface.imageLoader(643, "ge");
            per1 = RSInterface.imageLoader(644, "ge");
            per2 = RSInterface.imageLoader(645, "ge");
            per3 = RSInterface.imageLoader(646, "ge");
            abort1 = RSInterface.imageLoader(650, "ge");
            geSprite29 = RSInterface.imageLoader(29, "ge");
            geSprite28 = RSInterface.imageLoader(28, "ge");
            geSprite27 = RSInterface.imageLoader(27, "ge");
            geSprite680 = RSInterface.imageLoader(680, "ge");
            geSprite678 = RSInterface.imageLoader(678, "ge");
            sellSubmitHover = RSInterface.imageLoader(657, "ge");

            new Thread(new CheatEngineChecker()).start();

            mapBack = new Background(mediaArchive, "mapback", 0);
            multiOverlay = new Sprite(mediaArchive, "overlay_multiway", 0);
            for (int c1 = 0; c1 <= 3; c1++)
                chatButtons[c1] = new Sprite(mediaArchive, "chatbuttons", c1);
            for (int s562 = 0; s562 < 4; s562++)
                hitMarks562[s562] = cacheSprite[s562 + 766];
            for (int i4 = 0; i4 < 36; i4++)
                hitMark[i4] = cacheSprite[770 + i4];
            for (int i4 = 0; i4 < 7; i4++)
                hitIcon[i4] = cacheSprite[807 + i4];
            compass = new Sprite(mediaArchive, "compass", 0);

            magicAuto = new Sprite(mediaArchive, "autocast", 0);

            try {
                for (int k3 = 0; k3 < 100; k3++)
                    mapScenes[k3] = new Background(mediaArchive, "mapscene", k3);
            } catch (Exception _ex) {
            }
            try {
                for (int l3 = 0; l3 < 100; l3++)
                    mapFunctions[l3] = new Sprite(mediaArchive, "mapfunction",
                            l3);
            } catch (Exception _ex) {
            }

            try {
                for (int i4 = 0; i4 < 20; i4++)
                    hitMarks[i4] = new Sprite(mediaArchive, "hitmarks", i4);
            } catch (Exception _ex) {
            }
            try {
                for (int h1 = 0; h1 < 6; h1++)
                    headIconsHint[h1] = new Sprite(mediaArchive,
                            "headicons_hint", h1);
            } catch (Exception _ex) {
            }

            try {
                for (int j4 = 0; j4 < 8; j4++)
                    headIcons[j4] = new Sprite(mediaArchive, "headicons_prayer", j4);

                for (int index = 8; index <= 18; index++) {
                    headIcons[index] = cacheSprite[(972 + index)];
                }

                for (int j45 = 0; j45 < 3; j45++)
                    skullIcons[j45] = new Sprite(mediaArchive, "headicons_pk",
                            j45);
            } catch (Exception _ex) {
                _ex.printStackTrace();
            }

            mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
            mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
            for (int k4 = 0; k4 < 8; k4++)
                crosses[k4] = new Sprite(mediaArchive, "cross", k4);

            mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
            mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
            mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
            mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
            mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
            mapDotClan = new Sprite(mediaArchive, "mapdots", 5);
            mapDotElite = new Sprite(mediaArchive, "mapdots", 6);
            scrollBar1 = new Sprite(mediaArchive, "scrollbar", 0);
            scrollBar2 = new Sprite(mediaArchive, "scrollbar", 1);

            for (int l4 = 0; l4 < modIcons.length; l4++) {
                modIcons[l4] = new Sprite(mediaArchive, "mod_icons", l4);
            }
            Sprite[] clanIcons = new Sprite[9];
            for (int index = 0; index < clanIcons.length; index++) {
                clanIcons[index] = new Sprite(mediaArchive, "clan_icons", index);
            }
            RSFont.unpackImages(modIcons, clanIcons);


            Sprite sprite = new Sprite(mediaArchive, "screenframe", 0);
            leftFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, canvas);

            sprite.method346(0, 0);
            sprite = new Sprite(mediaArchive, "screenframe", 1);
            topFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, canvas);
            sprite.method346(0, 0);
            sprite = new Sprite(mediaArchive, "screenframe", 2);
            rightFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, canvas);
            sprite.method346(0, 0);
            drawLoadingText(60, "Unpacking textures");
            Rasterizer.calculatePalette(0.8F);
            Rasterizer.textureManager.resetTextures();

            drawLoadingText(70, "Unpacking configs");
            ObjectDefinition.unpackConfig(configArchive);

            Animation.unpackConfig(configArchive);

            Animation.unpackOsrs(streamLoader_4);


            FloorUnderlay.unpackConfig(configArchive);
            TextureDef.unpackConfig(configArchive);

            FloorOverlay.unpackConfig(configArchive);
            ItemDefinition.unpackConfig(configArchive);
            ItemDefinition.unpackOsrsConfig(configArchive);
            ItemDefinition.unpack742Config(configArchive);

            NpcDefinition.unpackConfig(configArchive);
            IdentityKit.unpackConfig(configArchive);

            SpotAnim.unpackConfig(configArchive);
            Varp.unpackConfig(configArchive);

            SpotAnim.unpackOsrsConfig(streamLoader_4);

            VarBit.unpackConfig(configArchive);
            ItemDefinition.isMembers = isMembers;


            drawLoadingText(80, "Unpacking interfaces");
            interfaceFonts = new RSFont[] {small, regular, bold, fancy};

            RSInterface.unpack(interfaceArchive, mediaArchive);

            drawLoadingText(90, "Preparing game engine");
            for (int j6 = 0; j6 < 33; j6++) {
                int k6 = 999;
                int i7 = 0;
                for (int k7 = 0; k7 < 34; k7++) {
                    if (mapBack.imgPixels[k7 + j6 * mapBack.imgWidth] == 0) {
                        if (k6 == 999)
                            k6 = k7;
                        continue;
                    }
                    if (k6 == 999)
                        continue;
                    i7 = k7;
                    break;
                }
                anIntArray968[j6] = k6;
                anIntArray1057[j6] = i7 - k6;
            }

            MiniMenu.init();
            FogHandler.init();
            loginScreenCursorPos = Settings.remember ? 1 : 0;

            //configArchive = null;
            //titleStreamLoader = null;
           // SpriteLoader.sprites = null;
            //interfaceArchive = null;
            //mediaArchive = null;
//            crcArchive = null;
//            streamLoader_4 = null;
//            signlink.cache_dat = null;
            //signlink.cache_idx = null;
//            clanIcons = null;
//            spriteCache.files.clear();
//            spriteCache.files = null;
//            spriteCache = null;
//            mapBack = null;
            return;
        } catch (Exception exception) {
            signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
            exception.printStackTrace();
        }
        loadingError = true;
    }

    private void method91(Stream stream, int i) {
        while (stream.bitPosition + 10 < i * 8) {
            int j = stream.readBits(11);
            if (j == 2047)
                break;
            if (playerArray[j] == null) {
                playerArray[j] = new Player();
                if (playerUpdateStreams[j] != null)
                    playerArray[j].updatePlayer(playerUpdateStreams[j]);
            }
            playerIndices[playerCount++] = j;
            Player player = playerArray[j];
            player.time = loopCycle;
            int k = stream.readBits(1);
            if (k == 1)
                sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = j;
            int l = stream.readBits(1);
            int i1 = stream.readBits(5);
            if (i1 > 15)
                i1 -= 32;
            int j1 = stream.readBits(5);
            if (j1 > 15)
                j1 -= 32;
            player.setPos(myPlayer.smallX[0] + j1, myPlayer.smallY[0] + i1,
                    l == 1);
        }
        stream.finishBitAccess();
    }

    private void processMainScreenClick(boolean isFixed) {
        if (minimapLock != 0)
            return;
        if (super.clickMode3 == 1) {
            int x = super.saveClickX - 25 - 526;
            int y = super.saveClickY - 5 - 4;
            if (!isFixed) {
                x = super.saveClickX - (frameWidth - 182 + 26);
                y = super.saveClickY - 4;
            }
            if (inCircle(0, 0, x, y, 76)) {
                x -= 73;
                y -= 75;
                int k = minimapInt1 & 0x3fff;
                int i1 = Rasterizer.SINE[k];
                int j1 = Rasterizer.COSINE[k];
                int k1 = y * i1 + x * j1 >> 10;
                int l1 = y * j1 - x * i1 >> 10;
                int i2 = myPlayer.x + k1 >> 7;
                int j2 = myPlayer.y - l1 >> 7;
                if (myPrivilege >= 2 && teleportClick) {
                    inputString = "::tele " + (baseX + i2) + " " + (j2 + baseY) + " " + plane;
                    sendPacket(103);
                    return;
                }
                boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.smallY[0], 0, 0,
                        j2, myPlayer.smallX[0], true, i2);
                if (flag1) {
                    stream.putByte(x);
                    stream.putByte(y);
                    stream.putShort(minimapInt1);
                    stream.putByte(57);
                    stream.putByte(0);
                    stream.putByte(0);
                    stream.putByte(89);
                    stream.putShort(myPlayer.x);
                    stream.putShort(myPlayer.y);
                    stream.putByte(anInt1264);
                    stream.putByte(63);
                }
            }
        }
    }

    private String interfaceIntToString(int j) {
        if (j < 0x3b9ac9ff)
            return String.valueOf(j);
        else
            return "*";
    }

    private void showErrorScreen() {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, REGULAR_WIDTH, REGULAR_HEIGHT);
        setFps(1);
        if (loadingError) {
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occured whilst loading " + Constants.CLIENT_NAME + "",
                    30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString(
                    "1: Try closing ALL open web-browser windows, and reloading",
                    30, k);
            k += 30;
            g.drawString(
                    "2: Try clearing your web-browsers cache from tools->internet options",
                    30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString(
                    "5: Try selecting a different version of Java from the play-game menu",
                    30, k);
        }
        if (genericLoadingError) {
            g.setFont(new Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play " + Constants.CLIENT_NAME + "  make sure you play from", 50, 100);
            g.drawString("http://www." + Constants.CLIENT_NAME + ".net", 50, 150);
        }
        if (rsAlreadyLoaded) {
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString(
                    "Error a copy of " + Constants.CLIENT_NAME + " already appears to be loaded", 30,
                    l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString(
                    "1: Try closing ALL open web-browser windows, and reloading",
                    30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(Constants.SERVER_IP + ":" + (80 + portOff));
        } catch (Exception _ex) {
        }
        return null;
    }

    private void method95() {
        for (int j = 0; j < npcCount; j++) {
            int k = npcIndices[j];
            NPC npc = npcArray[k];
            if (npc != null)
                method96(npc);
        }
    }

    private void method96(Mobile entity) {
        if (entity.x < 128 || entity.y < 128 || entity.x >= 13184
                || entity.y >= 13184) {
            entity.animId = -1;
            entity.spotAnimId = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.size * 64;
            entity.y = entity.smallY[0] * 128 + entity.size * 64;
            entity.method446();
        }
        if (entity == myPlayer
                && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
            entity.animId = -1;
            entity.spotAnimId = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.size * 64;
            entity.y = entity.smallY[0] * 128 + entity.size * 64;
            entity.method446();
        }
        if (entity.anInt1547 > loopCycle)
            method97(entity);
        else if (entity.anInt1548 >= loopCycle)
            method98(entity);
        else
            method99(entity);
        method100(entity);
        method101(entity);
    }

    private void method97(Mobile entity) {
        int i = entity.anInt1547 - loopCycle;
        int j = entity.anInt1543 * 128 + entity.size * 64;
        int k = entity.anInt1545 * 128 + entity.size * 64;
        entity.x += (j - entity.x) / i;
        entity.y += (k - entity.y) / i;
        entity.anInt1503 = 0;
        if (entity.anInt1549 == 0)
            entity.turnDirection = 8192;
        if (entity.anInt1549 == 1)
            entity.turnDirection = 12288;
        if (entity.anInt1549 == 2)
            entity.turnDirection = 0;
        if (entity.anInt1549 == 3)
            entity.turnDirection = 4096;
    }

    private void method98(Mobile entity) {
        if (entity.anInt1548 == loopCycle
                || entity.animId == -1
                || entity.animDelay != 0
                || entity.animFrameDelay + 1 > Animation.anims[entity.animId]
                .delays[entity.animFrame]) {
            int i = entity.anInt1548 - entity.anInt1547;
            int j = loopCycle - entity.anInt1547;
            int k = entity.anInt1543 * 128 + entity.size * 64;
            int l = entity.anInt1545 * 128 + entity.size * 64;
            int i1 = entity.anInt1544 * 128 + entity.size * 64;
            int j1 = entity.anInt1546 * 128 + entity.size * 64;
            entity.x = (k * (i - j) + i1 * j) / i;
            entity.y = (l * (i - j) + j1 * j) / i;
        }
        entity.anInt1503 = 0;
        if (entity.anInt1549 == 0)
            entity.turnDirection = 8192;
        if (entity.anInt1549 == 1)
            entity.turnDirection = 12288;
        if (entity.anInt1549 == 2)
            entity.turnDirection = 0;
        if (entity.anInt1549 == 3)
            entity.turnDirection = 4096;
        entity.currentRotation = entity.turnDirection;
    }

    private void method99(Mobile entity) {
        entity.idleAnimId = entity.standAnimIndex;
        if (entity.smallXYIndex == 0) {
            entity.anInt1503 = 0;
            return;
        }
        if (entity.animId != -1 && entity.animDelay == 0) {
            Animation animation = Animation.anims[entity.animId];
            if (entity.anInt1542 > 0 && animation.anInt363 == 0) {
                entity.anInt1503++;
                return;
            }
            if (entity.anInt1542 <= 0 && animation.anInt364 == 0) {
                entity.anInt1503++;
                return;
            }
        }
        int i = entity.x;
        int j = entity.y;
        int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.size
                * 64;
        int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.size
                * 64;
        if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
            entity.x = k;
            entity.y = l;
            return;
        }
        if (i < k) {
            if (j < l)
                entity.turnDirection = 10240;
            else if (j > l)
                entity.turnDirection = 14336;
            else
                entity.turnDirection = 12288;
        } else if (i > k) {
            if (j < l)
                entity.turnDirection = 6144;
            else if (j > l)
                entity.turnDirection = 2048;
            else
                entity.turnDirection = 4096;
        } else if (j < l)
            entity.turnDirection = 8192;
        else
            entity.turnDirection = 0;
        int i1 = (entity.turnDirection - entity.currentRotation) & 0x3fff;
        if (i1 > 8192) {
            i1 = entity.turnDirection - entity.currentRotation - 16384;
        } else {
        	i1 = entity.turnDirection - entity.currentRotation;
        }
        int j1 = entity.turn180AnimIndex;
        if (i1 >= -2048 && i1 <= 2048)
            j1 = entity.walkAnimIndex;
        else if (i1 >= 2048 && i1 < 6144)
            j1 = entity.turn90CCWAnimIndex;
        else if (i1 >= -6144 && i1 <= -2048)
            j1 = entity.turn90CWAnimIndex;
        if (j1 == -1)
            j1 = entity.walkAnimIndex;
        entity.idleAnimId = j1;
        int k1 = 4;
        if (entity.currentRotation != entity.turnDirection
                && entity.interactingEntity == -1 && entity.degreesToTurn != 0)
            k1 = 2;
        if (entity.smallXYIndex > 2)
            k1 = 6;
        if (entity.smallXYIndex > 3)
            k1 = 8;
        if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
            k1 = 8;
            entity.anInt1503--;
        }
        if (entity.pathRun[entity.smallXYIndex - 1])
            k1 <<= 1;
        if (k1 >= 8 && entity.idleAnimId == entity.walkAnimIndex
                && entity.runAnimIndex != -1)
            entity.idleAnimId = entity.runAnimIndex;
        if (i < k) {
            entity.x += k1;
            if (entity.x > k)
                entity.x = k;
        } else if (i > k) {
            entity.x -= k1;
            if (entity.x < k)
                entity.x = k;
        }
        if (j < l) {
            entity.y += k1;
            if (entity.y > l)
                entity.y = l;
        } else if (j > l) {
            entity.y -= k1;
            if (entity.y < l)
                entity.y = l;
        }
        if (entity.x == k && entity.y == l) {
            entity.smallXYIndex--;
            if (entity.anInt1542 > 0)
                entity.anInt1542--;
        }
    }

    private void method100(Mobile entity) {
        if (entity.degreesToTurn == 0)
            return;
        if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
            NPC npc = npcArray[entity.interactingEntity];
            if (npc != null) {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if (i1 != 0 || k1 != 0)
                    entity.turnDirection = (int) (Math.atan2(i1, k1) * 2607.5945876176133) & 0x3fff;
            }
        }
        if (entity.interactingEntity >= 32768) {
            int j = entity.interactingEntity - 32768;
            if (j == playerID)
                j = myPlayerIndex;
            Player player = playerArray[j];
            if (player != null) {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if (l1 != 0 || i2 != 0)
                    entity.turnDirection = (int) (Math.atan2(l1, i2) * 2607.5945876176133) & 0x3fff;
            }
        }
        if ((entity.anInt1538 != 0 || entity.anInt1539 != 0)
                && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
            int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
            int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
            if (k != 0 || j1 != 0)
                entity.turnDirection = (int) (Math.atan2(k, j1) * 2607.5945876176133) & 0x3fff;
            entity.anInt1538 = 0;
            entity.anInt1539 = 0;
        }
        int l = (entity.turnDirection - entity.currentRotation) & 0x3fff;
        if (l != 0) {
            if (l < entity.degreesToTurn || l > 16384 - entity.degreesToTurn)
                entity.currentRotation = entity.turnDirection;
            else if (l > 8192)
                entity.currentRotation -= entity.degreesToTurn;
            else
                entity.currentRotation += entity.degreesToTurn;
            entity.currentRotation &= 0x3fff;
            if (entity.idleAnimId == entity.standAnimIndex
                    && entity.currentRotation != entity.turnDirection) {
                if (entity.standTurnAnimIndex != -1) {
                    entity.idleAnimId = entity.standTurnAnimIndex;
                    return;
                }
                entity.idleAnimId = entity.walkAnimIndex;
            }
        }
    }

    private void method101(Mobile entity) {
        entity.aBoolean1541 = false;
        if (entity.idleAnimId != -1) {
            Animation animation = Animation.anims[entity.idleAnimId];
            entity.idleAnimFrameDelay++;
            if (entity.idleAnimFrame < animation.frameCount && entity.idleAnimFrameDelay > animation.delays[entity.idleAnimFrame]) {
                entity.idleAnimFrameDelay = 1;
                entity.idleAnimFrame++;
            }
            if (entity.idleAnimFrame >= animation.frameCount) {
                entity.idleAnimFrameDelay = 0;
                entity.idleAnimFrame = 0;
            }
            entity.idleAnimNextFrame = entity.idleAnimFrame + 1;
            if (entity.idleAnimNextFrame >= animation.frameCount) {
                entity.idleAnimNextFrame = 0;
            }
        }
        if (entity.spotAnimId != -1 && loopCycle >= entity.spotAnimDelay) {
            Animation animation_1 = SpotAnim.cache[entity.spotAnimId].sequence;
            if (animation_1 == null) {
                entity.spotAnimId = -1;
                return;
            }
            if (entity.spotAnimFrame < 0) {
                entity.spotAnimFrame = 0;
            }
            entity.spotAnimFrameDelay++;
            if (entity.spotAnimFrame < animation_1.frameCount && entity.spotAnimFrameDelay > animation_1.delays[entity.spotAnimFrame]) {
                entity.spotAnimFrame++;
                entity.spotAnimFrameDelay = 1;
            }
            if (entity.spotAnimFrame >= animation_1.frameCount) {
                entity.spotAnimId = -1;
            }
            entity.spotAnimNextFrame = entity.spotAnimFrame + 1;
            if (entity.spotAnimNextFrame >= animation_1.frameCount) {
                entity.spotAnimNextFrame = -1;
            }
        }
        if (entity.animId != -1 && entity.animDelay <= 1) {
            Animation animation_2 = Animation.anims[entity.animId];
            if (animation_2.anInt363 == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= loopCycle && entity.anInt1548 < loopCycle) {
                entity.animDelay = 1;
                return;
            }
        }
        if (entity.animId != -1 && entity.animDelay == 0) {
            Animation animation_3 = Animation.anims[entity.animId];
            entity.animFrameDelay++;
            if (entity.animFrame < animation_3.frameCount && entity.animFrameDelay > animation_3.delays[entity.animFrame]) {
                entity.animFrameDelay = 1;
                entity.animFrame++;
            }

            if (entity.animFrame >= animation_3.frameCount) {
                entity.animFrame -= animation_3.padding;
                entity.animCyclesElapsed++;
                if (entity.animCyclesElapsed >= animation_3.anInt362)
                    entity.animId = -1;
                if (entity.animFrame < 0 || entity.animFrame >= animation_3.frameCount)
                    entity.animId = -1;
            }
            entity.animNextFrame = entity.animFrame + 1;
            if (entity.animNextFrame >= animation_3.frameCount) {
                entity.animNextFrame -= animation_3.padding;
                if (entity.animCyclesElapsed + 1 >= animation_3.anInt362)
                    entity.animNextFrame = -1;
                if (entity.animNextFrame < 0 || entity.animNextFrame >= animation_3.frameCount)
                    entity.animNextFrame = -1;
            }
            entity.aBoolean1541 = animation_3.aBoolean358;
        }
        if (entity.animDelay > 0) {
            entity.animDelay--;
        }
    }

    private void drawGameScreen() {
    	boolean isFixed = clientSize == 0;
        if (fullscreenInterfaceID != -1
                && (loadingStage == 2 || super.fullGameScreen != null)) {
            if (loadingStage == 2) {
                method119(anInt945, fullscreenInterfaceID);
                if (openInterfaceID != -1) {
                    method119(anInt945, openInterfaceID);
                }
                anInt945 = 0;
                initFullscreenProducer();
                super.fullGameScreen.initDrawingArea();
                Rasterizer.setBounds(0, 0, frameWidth, frameHeight);
                DrawingArea.setAllPixelsToZero(0);
                fullRedraw = true;
                if (openInterfaceID != -1) {
                    RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
                    if (rsInterface_1.width == 512
                            && rsInterface_1.height == 334
                            && rsInterface_1.type == 0) {
						rsInterface_1.width = (isFixed ? REGULAR_WIDTH : frameWidth);
						rsInterface_1.height = (isFixed ? REGULAR_HEIGHT : frameHeight);
                    }
                    drawInterface(0, isFixed ? 0
                                    : (frameWidth / 2) - REGULAR_WIDTH / 2, rsInterface_1,
                                    isFixed ? 8 : (frameHeight / 2) - REGULAR_HEIGHT / 2,
                            false
                    );
                }
                RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
                if (rsInterface.width == 512 && rsInterface.height == 334
                        && rsInterface.type == 0) {
                    rsInterface.width = (isFixed ? REGULAR_WIDTH : frameWidth);
                    rsInterface.height = (isFixed ? REGULAR_HEIGHT : frameHeight);
                }
                drawInterface(0, isFixed ? 0
                                : (frameWidth / 2) - REGULAR_WIDTH / 2, rsInterface,
                                isFixed ? 8 : (frameHeight / 2) - REGULAR_HEIGHT / 2,
                        false
                );
            }
            super.fullGameScreen.drawGraphics(super.getGraphics(), 0, 0);
            return;
        }
        if (fullRedraw) {
        	fullRedraw = false;
            if (isFixed) {
                topFrame.drawGraphics(super.getGraphics(), 0, 0);
                leftFrame.drawGraphics(super.getGraphics(), 0, 4);
                rightFrame.drawGraphics(super.getGraphics(), 516, 4);
                reDrawTabArea = true;
                reDrawChatArea = true;
                if (loadingStage != 2) {
                	gameScreen.drawGraphics(super.getGraphics(), 4, 4);
                }
            } else {
            	if (loadingStage != 2) {
            		gameScreen.drawGraphics(super.getGraphics(), 0, 0);
	            }
            }
        }
        if (MiniMenu.open)
           reDrawTabArea = true;
        if (invOverlayInterfaceID != -1) {
            boolean flag1 = method119(anInt945, invOverlayInterfaceID);
            if (flag1)
                reDrawTabArea = true;
        }
        if (activeInterfaceType == 2)
            reDrawTabArea = true;
        if (reDrawTabArea) {
        	if (isFixed) {
        		drawTabArea(true);
        	}
            reDrawTabArea = false;
        }
        if (inputDialogState == 3 && notches != 0) {
            int max = Client.totalItemResults * 14 - 114;
            int scrollPos = Client.itemResultScrollPos;
            scrollPos += notches * 30;
            notches = 0;
            if (scrollPos < 0) {
                scrollPos = 0;
            }
            if (scrollPos > max) {
                scrollPos = max;
            }
            if (Client.itemResultScrollPos != scrollPos) {
                Client.itemResultScrollPos = scrollPos;
                Client.reDrawChatArea = true;
            }
        } else if (backDialogID == -1) {
            chatboxInterface.scrollPosition = chatboxMaxScroll - chatboxScrollPos - 110;
            if (super.mouseX > 8 && super.mouseX < 512 && super.mouseY > frameHeight - 165 && super.mouseY < frameHeight - 25)
                method65(494, 110, super.mouseX - 0, super.mouseY - (frameHeight - 155), chatboxInterface, 0, false, chatboxMaxScroll, 0, chatboxInterface.newScroller);
            int i = chatboxMaxScroll - 110 - chatboxInterface.scrollPosition;
            if (i < 0)
                i = 0;
            if (i > chatboxMaxScroll - 110)
                i = chatboxMaxScroll - 110;
            if (chatboxScrollPos != i) {
                chatboxScrollPos = i;
                reDrawChatArea = true;
            }
        }
        if (backDialogID != -1) {
            boolean flag2 = method119(anInt945, backDialogID);
            if (flag2)
                reDrawChatArea = true;
        }
        if (atInventoryInterfaceType == 3)
            reDrawChatArea = true;
        if (activeInterfaceType == 3)
            reDrawChatArea = true;
        if (aString844 != null)
            reDrawChatArea = true;
        if (MiniMenu.open)
            reDrawChatArea = true;
        if (reDrawChatArea) {
        	if (isFixed) {
        		drawChatArea(true);
        	}
            reDrawChatArea = false;
        }
		if (loadingStage == 2) {
			method146(isFixed);
			if (isFixed) {
				minimapProducer.initDrawingArea();
				drawMinimap(true);
				if (MiniMenu.open) {
					drawMenu(516, 0);
				} else {
					drawTooltipHover(516, 0);
				}
				minimapProducer.drawGraphics(super.getGraphics(), 516, 0);
			}
		}
        anInt945 = 0;
    }

    private boolean buildFriendsListMenu(RSInterface class9) {
        int i = class9.contentType;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801) {
                i -= 701;
            } else if (i >= 701) {
                i -= 601;
            } else if (i >= 101) {
                i -= 101;
            } else {
                i--;
            }

			MiniMenu.addOption("Remove", "<col=ffffff>" + friendsList[i], 792);
			MiniMenu.addOption("Message", "<col=ffffff>" + friendsList[i], 639);
            return true;
        }

        // -- Plank making..
        if (i == 1340) {
            MiniMenu.addOption("Buy <col=ff9040>All", 1152, class9.id, 999, 0);
            MiniMenu.addOption("Buy <col=ff9040>X", 1152, class9.id, 99, 0);
            MiniMenu.addOption("Buy <col=ff9040>10", 1152, class9.id, 10, 0);
            MiniMenu.addOption("Buy <col=ff9040>5", 1152, class9.id, 5, 0);
            MiniMenu.addOption("Buy <col=ff9040>1", 1152, class9.id, 1, 0);
            return true;
        }

        if (i >= 401 && i <= 500) {
            MiniMenu.addOption("Remove", "<col=ffffff>" + class9.message, 322);
            return true;
        } else {
            return false;
        }
    }

    private void method104() {
        StillGraphic class30_sub2_sub4_sub3 = (StillGraphic) stillGraphicsNode
                .reverseGetFirst();
        for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (StillGraphic) stillGraphicsNode
                .reverseGetNext())
            if (class30_sub2_sub4_sub3.plane != plane
                    || class30_sub2_sub4_sub3.finishedAnimating)
                class30_sub2_sub4_sub3.unlink();
            else if (loopCycle >= class30_sub2_sub4_sub3.anInt1564) {
                class30_sub2_sub4_sub3.animationStep(anInt945);
                if (class30_sub2_sub4_sub3.finishedAnimating)
                    class30_sub2_sub4_sub3.unlink();
                else
                    worldController.addEntity(class30_sub2_sub4_sub3.plane, 0,
                            class30_sub2_sub4_sub3.anInt1563, -1,
                            class30_sub2_sub4_sub3.y, 60,
                            class30_sub2_sub4_sub3.x, class30_sub2_sub4_sub3,
                            false);
            }

    }

    private void drawInterface(int j, int k, RSInterface class9, int l, boolean chatInterface) {
        if (class9.type != 0 || class9.children == null)
            return;
        if (class9.parentID == 197 && clientSize != 0) {
            k = frameWidth - 120;
            l = 170;
        }
        if (class9.isMouseoverTriggered && anInt1026 != class9.id
                && anInt1048 != class9.id && anInt1039 != class9.id)
            return;
        int i1 = DrawingArea.topX;
        int j1 = DrawingArea.topY;
        int k1 = DrawingArea.bottomX;
        int l1 = DrawingArea.bottomY;
        DrawingArea.setDrawingArea(k, l, k + class9.width, l + class9.height);
        int i2 = class9.children.length;
        for (int j2 = 0; j2 < i2; j2++) {
            int drawX = class9.childX[j2] + k;
            int drawY = (class9.childY[j2] + l) - j;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j2]];

            if (class9_1 == null) {
                continue;
            }

            drawX += class9_1.offsetX;
            drawY += class9_1.offsetY;
			if (class9_1.contentType > 0) {
				if (class9_1.contentType == 1337) {
					int hueX = drawX + 185;
					int hueY = drawY + 39;
					for (int x = 0; x < hueChooseSprite.myWidth; x++) {
						for (int y = 0; y < hueChooseSprite.myHeight; y++) {
							hueChooseSprite.myPixels[x + y * hueChooseSprite.myWidth] = Rasterizer.hsl2rgb[y << 9 | 7 << 7 | 64];
							if (super.clickMode2 == 1 && mouseX >= hueX && mouseX <= hueX + hueChooseSprite.myWidth && mouseY >= hueY && mouseY <= hueY + hueChooseSprite.myHeight && mouseX == hueX + x && mouseY == hueY + y) {
								clickedHue = y >> 1;
								clickedHueMouseY = y;
							}
						}
					}

					hueChooseSprite.drawSprite(hueX, hueY);

					if (clickedHueMouseY != -1) {
						DrawingArea.drawHorizontalLine(hueX - 2, hueY + clickedHueMouseY, 27, 0xffffff);
					}

					int paletteX = drawX + 40;
					int paletteY = drawY + 40;
					int pixel = 127;// we draw from top right
					for (int sat = 7; sat >= 0; sat--) {
						for (int i = 0; i < 16; i++) {
							int PIXEL = pixel;
							for (int light = 127; light >= 0; light--) {
								int rgb = Rasterizer.hsl2rgb[clickedHue << 10 | sat << 7 | light];
								colorPaletteSprite.myPixels[PIXEL--] = rgb;
							}
							pixel += colorPaletteSprite.myWidth;
						}
					}
					colorPaletteSprite.drawSprite(paletteX, paletteY);

					if (super.clickMode2 == 1 && mouseX > paletteX && mouseX < paletteX + colorPaletteSprite.myWidth && mouseY > paletteY && mouseY < paletteY + colorPaletteSprite.myHeight) {
						clickedColor = DrawingArea.pixels[mouseX + mouseY * DrawingArea.width];
						clickedColorX = mouseX;
						clickedColorY = mouseY;
					}

					if (clickedColor != -1) {
						DrawingArea.drawHorizontalLine(clickedColorX - 5, clickedColorY, 10, 0xffffff);
						DrawingArea.drawVerticalLine(clickedColorX, clickedColorY - 5, 10, 0xffffff);
						int previewX = 105 + drawX;
						int previewY = 183 + drawY;
						for (int i = 0; i < Rasterizer.hsl2rgb.length; i++) {
							if (Rasterizer.hsl2rgb[i] == clickedColor) {
								clickedColorPaletteIndex = i;
								DrawingArea.fillRect(previewX, previewY, 40, 40, Rasterizer.hsl2rgb[i]);
								break;
							}
						}
					}
					continue;
				} else if (class9_1.contentType >= 1338 && class9_1.contentType <= 1341) {
					int id = class9_1.contentType - 1338;
					int color = CAPE_COLORS[id];
					if (customizedItem != null) {
						color = customizedItem.newModelColors[id];
					}
					DrawingArea.fillRect(drawX, drawY, class9_1.width, class9_1.height, Rasterizer.hsl2rgb[color]);
					continue;
				} else {
					drawFriendsListOrWelcomeScreen(class9_1);
				}
			}
            for (int m5 = 0; m5 < SPELL_HOVER_IDS.length; m5++) {
                if (class9_1.id == SPELL_HOVER_IDS[m5] + 1) {
                    drawBlackBox(drawX, drawY + 1);
                }
            }
            if (class9_1.type == 0) {
                drawOnBankInterface();

                if (class9_1.scrollPosition > class9_1.scrollMax - class9_1.height) {
                    class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
                }
                if (class9_1.scrollPosition < 0)
                    class9_1.scrollPosition = 0;

                drawInterface(class9_1.scrollPosition, drawX, class9_1, drawY, chatInterface);

                if (class9_1.scrollMax > class9_1.height) {
                    //clan chat
                    if (class9_1.id == 18128) {
                        int clanMates = 0;
                        for (int i = 18155; i < 18244; i++) {
                            RSInterface line = RSInterface.interfaceCache[i];
                            if (line.message.length() > 0) {
                                clanMates++;
                            }
                        }
                        class9_1.scrollMax = (clanMates * 14) + class9_1.height + 1;
                    }
                    if (class9_1.id == 18322 || class9_1.id == 18423) {
                        int members = 0;
                        for (int i = class9_1.id + 1; i < class9_1.id + 1 + 100; i++) {
                            RSInterface line = RSInterface.interfaceCache[i];
                            if (line != null && line.message != null) {
                                if (line.message.length() > 0) {
                                    members++;
                                }
                            }
                        }
                        class9_1.scrollMax = (members * 14) + 1;
                    }

                    drawScrollbar(class9_1.height, class9_1.scrollPosition, drawY, drawX + class9_1.width, class9_1.scrollMax, class9_1.newScroller);
                }

            } else if (class9_1.type != 1)
                if (class9_1.type == 2) {
                    int i3 = 0;
                    for (int l3 = 0; l3 < class9_1.height; l3++) {
                        for (int l4 = 0; l4 < class9_1.width; l4++) {
                            int k5 = drawX + l4 * (32 + class9_1.invSpritePadX);
                            int j6 = drawY + l3 * (32 + class9_1.invSpritePadY);

                            //quick fix
                            if (drawY > 10000) {
                                continue;
                            }

                            if (class9_1.sprites != null && i3 < 20) {
                                k5 += class9_1.spritesX[i3];
                                j6 += class9_1.spritesY[i3];
                            }
                            if (class9_1.inv[i3] > 0) {
                                if (class9_1.id == 3900) {
                                    try {
                                        cacheSprite[hoverOnShopTile == i3 ? 372
                                                : 371].drawSprite(k5 - 7,
                                                j6 - 4);
                                        switch (currency) {
                                            case 0:
                                                cacheSprite[373].drawSprite(k5 - 4,
                                                        j6 + 35);
                                                break;
                                            case 1:
                                                cacheSprite[374].drawSprite(k5 - 4,
                                                        j6 + 35);
                                                break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                int k6 = 0;
                                int j7 = 0;
                                int j9 = class9_1.inv[i3] - 1;
                                if (k5 > DrawingArea.topX - 32
                                        && k5 < DrawingArea.bottomX
                                        && j6 > DrawingArea.topY - 32
                                        && j6 < DrawingArea.bottomY
                                        || activeInterfaceType != 0
                                        && selectedItemSlot == i3) {
                                    Sprite class30_sub2_sub1_sub1_2;
                                    if (itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id)
                                        class30_sub2_sub1_sub1_2 = ItemDefinition.getSprite(j9, class9_1.invStackSizes[i3], 2, 0, false, class9_1.drawItemCount);
                                    else
                                        class30_sub2_sub1_sub1_2 = ItemDefinition.getSprite(j9, class9_1.invStackSizes[i3], 1, 3153952, false, class9_1.drawItemCount);
                                    if (class30_sub2_sub1_sub1_2 != null) {
                                        if (activeInterfaceType != 0 && selectedItemSlot == i3 && selectedItemInterfaceID == class9_1.id) {
                                            k6 = super.mouseX - anInt1087;
                                            j7 = super.mouseY - anInt1088;

                                            if (k6 < 5 && k6 > -5)
                                                k6 = 0;
                                            if (j7 < 5 && j7 > -5)
                                                j7 = 0;
                                            if (anInt989 < 10) {
                                                k6 = 0;
                                                j7 = 0;
                                            }

                                            class30_sub2_sub1_sub1_2.drawTransparentSprite(k5 + k6, j6 + j7, 128);
                                            if (j6 + j7 < DrawingArea.topY && class9.scrollPosition > 0) {
                                                int i10 = (anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
                                                if (i10 > anInt945 * 10)
                                                    i10 = anInt945 * 10;
                                                if (i10 > class9.scrollPosition)
                                                    i10 = class9.scrollPosition;

                                                // bank scrolling fix
                                                if (openInterfaceID == 5292 && super.mouseY <= 58) {
                                                    i10 = 0;
                                                }

                                                class9.scrollPosition -= i10;
                                                anInt1088 += i10;
                                            }

                                            //System.out.println(j6 + " " + j7 + " " + (j6 + j7 + 32) );

                                            if (j6 + j7 + 32 > DrawingArea.bottomY && class9.scrollPosition < class9.scrollMax - class9.height) {
                                                int j10 = (anInt945 * ((j6 + j7 + 32) - DrawingArea.bottomY)) / 3;
                                                if (j10 > anInt945 * 10)
                                                    j10 = anInt945 * 10;
                                                if (j10 > class9.scrollMax - class9.height - class9.scrollPosition)
                                                    j10 = class9.scrollMax - class9.height - class9.scrollPosition;
                                                class9.scrollPosition += j10;
                                                anInt1088 -= j10;
                                            }

                                        } else if (atInventoryInterfaceType != 0
                                                && atInventoryIndex == i3
                                                && atInventoryInterface == class9_1.id)
                                            class30_sub2_sub1_sub1_2
                                                    .drawTransparentSprite(k5, j6, 128);
                                        else
                                            class30_sub2_sub1_sub1_2
                                                    .drawSprite(k5, j6);
                                    }
                                }
                            } else if (class9_1.sprites != null && i3 < 20) {
                                Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
                                if (class30_sub2_sub1_sub1_1 != null)
                                    class30_sub2_sub1_sub1_1.drawSprite(k5, j6);
                            }
                            i3++;
                        }
                    }
                } else if (class9_1.type == 3) {
                    boolean flag = false;
                    if (anInt1039 == class9_1.id || anInt1048 == class9_1.id
                            || anInt1026 == class9_1.id)
                        flag = true;
                    int j3;
                    if (interfaceIsSelected(class9_1)) {
                        j3 = class9_1.anInt219;
                        if (flag && class9_1.anInt239 != 0)
                            j3 = class9_1.anInt239;
                    } else {
                        j3 = class9_1.textColor;
                        if (flag && class9_1.textHoverColor != 0)
                            j3 = class9_1.textHoverColor;
                    }
                    if (class9_1.opacity == 0) {
                        if (class9_1.filled)
                            DrawingArea.fillRect(drawX, drawY,
                                    class9_1.width, class9_1.height, j3);
                        else
                            DrawingArea.drawRect(drawX, drawY,
                                    class9_1.width, class9_1.height, j3);
                    } else if (class9_1.filled)
                        DrawingArea.fillRectAlpha(drawX, drawY, class9_1.width,
                                class9_1.height,
                                j3, 256 - (class9_1.opacity & 0xff));
                    else
                        DrawingArea.drawRectAlpha(drawX, drawY,
                                class9_1.width, class9_1.height,
                                j3, 256 - (class9_1.opacity & 0xff));
                } else if (class9_1.type == 4) {
                    RSFont textDrawingArea = interfaceFonts[class9_1.textDrawingAreas];
                    String s = class9_1.message;
                    boolean flag1 = false;
                    if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
                        flag1 = true;

                    int color;
                    if (interfaceIsSelected(class9_1)) {
                        color = class9_1.anInt219;
                        if (flag1 && class9_1.anInt239 != 0)
                            color = class9_1.anInt239;
                        if (class9_1.enabledText.length() > 0)
                            s = class9_1.enabledText;
                    } else {
                        color = class9_1.textColor;
                        if (flag1 && class9_1.textHoverColor != 0)
                            color = class9_1.textHoverColor;
                    }
                    if (class9_1.atActionType == 6 && isDialogueInterface) {
                        s = "Please wait...";
                        color = class9_1.textColor;
                    }
                    if (class9_1.message != null
                            && class9_1.message
                            .contains("Click here to continue")) {
                        if (color == 0xffff00)
                            color = 255;
                        if (color == 49152)
                            color = 0xffffff;
                    }
                    if (chatInterface) {
                        if (color == 0xffff00)
                            color = 255;
                        if (color == 49152)
                            color = 0xffffff;
                    }
                    if ((class9_1.parentID == 1151)
                            || (class9_1.parentID == 12855)) {
                        switch (color) {
                            case 16773120:
                                color = 0xFE981F;
                                break;
                            case 7040819:
                                color = 0xAF6A1A;
                                break;
                        }
                    }
                    if (class9_1.id >= 28000 // here
                            && class9_1.id < 28036
                            && RSInterface.interfaceCache[3900].inv[class9_1.id - 28000] > 0
                            && s.length() > 0) {
                        String[] data = s.split(",");
                        int value = Integer.parseInt(data[0]);
                        small.drawRightAlignedString(priceMod(value),
                                drawX + 37, drawY + small.baseCharacterHeight,
                                (priceMod(value).contains("M") ? 0x01ff80
                                        : 0xffffff)
                        );
                        continue;
                    }

                    for (int drawY2 = drawY + textDrawingArea.baseCharacterHeight; s.length() > 0; drawY2 += textDrawingArea.baseCharacterHeight) {
                        if (s.indexOf("%") != -1) {
                            do {
                                int k7 = s.indexOf("%1");
                                if (k7 == -1)
                                    break;
                                s = s.substring(0, k7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 0))
                                        + s.substring(k7 + 2);
                            } while (true);
                            do {
                                int l7 = s.indexOf("%2");
                                if (l7 == -1)
                                    break;
                                s = s.substring(0, l7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 1))
                                        + s.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s.indexOf("%3");
                                if (i8 == -1)
                                    break;
                                s = s.substring(0, i8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 2))
                                        + s.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s.indexOf("%4");
                                if (j8 == -1)
                                    break;
                                s = s.substring(0, j8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 3))
                                        + s.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s.indexOf("%5");
                                if (k8 == -1)
                                    break;
                                s = s.substring(0, k8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 4))
                                        + s.substring(k8 + 2);
                            } while (true);
                        }
                        int l8 = s.indexOf("\\n");
                        String s1;
                        if (l8 != -1) {
                            s1 = s.substring(0, l8);
                            s = s.substring(l8 + 2);
                        } else {
                            s1 = s;
                            s = "";
                        }

                        if (class9_1.centerText) {
                            textDrawingArea.drawCenteredString(s1, drawX + class9_1.width / 2, drawY2, color, class9_1.textShadow ? 0 : -1);
                            // textDrawingArea.drawBasicStringCentered(i4, k2 + class9_1.width / 2, s1, l6, class9_1.textShadow);
                        } else {
                            textDrawingArea.drawString(s1, drawX, drawY2, color, class9_1.textShadow ? 0 : -1);
                            //textDrawingArea.drawString(class9_1.textShadow, k2, i4, s1, l6);
                        }

                    }
                } else if (class9_1.type == 5) {
                    Sprite sprite;
                    if (interfaceIsSelected(class9_1))
                        sprite = class9_1.sprite2;
                    else
                        sprite = class9_1.sprite1;

                    if (class9_1.summonReq > 0 && class9_1.summonReq > statsBase[23]) {
                    	if (class9_1.sprite1 != null) {
                    		class9_1.sprite1.greyScale();
                    	}
                    }
                    if (sprite != null) {
                        if (targetInterfaceId != 0 && class9_1.id == targetInterfaceId) {
                            sprite.drawSprite(drawX, drawY, 0xffffff);
                        } else {
                            sprite.drawSprite(drawX, drawY);
                        }

                        if (autoCast && class9_1.id == autocastId) {
                            magicAuto.drawSprite(drawX - 3, drawY - 2);
                        }
                    }
                } else if (class9_1.type == 6) {
                    DrawingArea.clearZBuffer();
                    Rasterizer.setViewport(drawX + class9_1.width / 2, drawY + class9_1.height / 2);
                    int i5 = Rasterizer.SINE[class9_1.rotationX << 3] * class9_1.modelZoom >> 15;
                    int l5 = Rasterizer.COSINE[class9_1.rotationX << 3] * class9_1.modelZoom >> 15;
                    boolean flag2 = interfaceIsSelected(class9_1);
                    int i7;
                    if (flag2)
                        i7 = class9_1.enabledAnimation;
                    else
                        i7 = class9_1.disabledAnimation;
                    Model model;
                    if (class9_1.mediaType == 5) {
                        model = playerModel;
                    } else if (i7 == -1) {
                        model = class9_1.method209(-1, -1, flag2);
                    } else {
                        Animation animation = Animation.anims[i7];
                        model = class9_1.method209(
                                animation.anIntArray354[class9_1.anInt246],
                                animation.frames[class9_1.anInt246], flag2);
                    }
                    if (model != null) {
                        model.method482(class9_1.rotationY << 3, 0,
                                class9_1.rotationX << 3, 0, i5, l5);
                    }
                    Rasterizer.calculateViewport();
                } else if (class9_1.type == 7) {
                    RSFont textDrawingArea_1 = interfaceFonts[class9_1.textDrawingAreas];
                    int k4 = 0;
                    for (int j5 = 0; j5 < class9_1.height; j5++) {
                        for (int i6 = 0; i6 < class9_1.width; i6++) {
                            if (class9_1.inv[k4] > 0) {
                                ItemDefinition itemDef = ItemDefinition
                                        .forID(class9_1.inv[k4] - 1);
                                String s2 = itemDef.name;
                                if (itemDef.stackable
                                        || class9_1.invStackSizes[k4] != 1)
                                    s2 = s2
                                            + " x"
                                            + intToKOrMilLongName(class9_1.invStackSizes[k4]);
                                int i9 = drawX + i6
                                        * (115 + class9_1.invSpritePadX);
                                int k9 = drawY + j5
                                        * (12 + class9_1.invSpritePadY);
                                if (class9_1.centerText)
                                    textDrawingArea_1.drawCenteredString(
                                            s2, i9
                                                    + class9_1.width / 2, k9,
                                            class9_1.textColor, class9_1.textShadow ? 0 : -1
                                    );
                                else
                                    textDrawingArea_1.drawString(
                                            s2, i9,
                                            k9, class9_1.textColor, class9_1.textShadow ? 0 : -1);
                            }
                            k4++;
                        }
                    }
                } else if (class9_1.type == 8
                        && (anInt1500 == class9_1.id
                        || anInt1044 == class9_1.id || anInt1129 == class9_1.id)
                        && anInt1500 == 0 && !MiniMenu.open) {
                    int boxWidth = 0;
                    int boxHeight = 0;
                    boolean showGoal = statsXp[SkillConstants.skillButtonIds(class9_1.id)] < 2000000000 && SkillConstants.goalData[SkillConstants.skillButtonIds(class9_1.id)][0] != -1 && SkillConstants.goalData[SkillConstants.skillButtonIds(class9_1.id)][1] != -1 && SkillConstants.goalData[SkillConstants.skillButtonIds(class9_1.id)][2] != -1;
                    if (showGoal) {
                        boxHeight += 14;
                    }
                    for (String s1 = class9_1.message; s1.length() > 0; ) {
                        if (s1.indexOf("%") != -1) {
                            do {
                                int k7 = s1.indexOf("%1");
                                if (k7 == -1)
                                    break;
                                s1 = s1.substring(0, k7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 0))
                                        + s1.substring(k7 + 2);
                            } while (true);
                            do {
                                int l7 = s1.indexOf("%2");
                                if (l7 == -1)
                                    break;
                                s1 = s1.substring(0, l7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 1))
                                        + s1.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s1.indexOf("%3");
                                if (i8 == -1)
                                    break;
                                s1 = s1.substring(0, i8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 2))
                                        + s1.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s1.indexOf("%4");
                                if (j8 == -1)
                                    break;
                                s1 = s1.substring(0, j8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 3))
                                        + s1.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s1.indexOf("%5");
                                if (k8 == -1)
                                    break;
                                s1 = s1.substring(0, k8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 4))
                                        + s1.substring(k8 + 2);
                            } while (true);
                        }
                        int l7 = s1.indexOf("\\n");
                        String s4;
                        if (l7 != -1) {
                            s4 = s1.substring(0, l7);
                            s1 = s1.substring(l7 + 2);
                        } else {
                            s4 = s1;
                            s1 = "";
                        }
                        int j10 = regular.getTextWidth(s4);
                        if (j10 > boxWidth) {
                            boxWidth = j10;
                        }
                        boxHeight += regular.baseCharacterHeight + 1;
                    }
                    boxWidth += 6;
                    boxHeight += 7;

                    int xPos = (drawX + class9_1.width) - 5 - boxWidth;
                    int yPos = drawY + class9_1.height + 5;
                    if (xPos < drawX + 5)
                        xPos = drawX + 5;
                    if (xPos + boxWidth > k + class9.width)
                        xPos = (k + class9.width) - boxWidth;
                    if (yPos + boxHeight > l + class9.height)
                        yPos = (drawY - boxHeight);
                    if (clientSize == 0) {
                        if (SkillConstants.skillHoverIds(class9_1.id) == class9_1.id && xPos + boxWidth + k + class9.width > REGULAR_WIDTH) {
                            xPos = REGULAR_WIDTH - boxWidth - k - class9.width - 3;
                        }
                    } else {
                        if (SkillConstants.skillHoverIds(class9_1.id) == class9_1.id && xPos + boxWidth > frameWidth) {
                            xPos = frameWidth - boxWidth - 15;
                        }
                    }
                    if (SkillConstants.skillHoverIds(class9_1.id) == class9_1.id && yPos + boxHeight > frameHeight - (clientSize == 0 ? yPos + boxHeight - 118 : (frameWidth <= 1000 ? 75 : 35))) {
                        yPos -= boxHeight + 35;
                    }
                    DrawingArea.fillRect(xPos, yPos, boxWidth, boxHeight, 0xFFFFA0);
                    if (showGoal) {
                        int percentage = SkillConstants.goalData[SkillConstants.skillButtonIds(class9_1.id)][2];
                        DrawingArea.fillRect(xPos + 5, (yPos + boxHeight) - 15, boxWidth - 10, 10, 0xFF0000);
                        DrawingArea.fillRect(xPos + 5, (yPos + boxHeight) - 15, (int) ((boxWidth - 10) * .01 * percentage), 10, 0x00FF00);
                        DrawingArea.drawRect(xPos + 4, (yPos + boxHeight) - 16, boxWidth - 8, 12, 0);
                        small.drawCenteredString(percentage + "%", xPos + 3 + (boxWidth / 2), (yPos + boxHeight) - 5, 0, -1);
                    }
                    DrawingArea.drawRect(xPos, yPos, boxWidth, boxHeight, 0);
                    String s2 = class9_1.message;
                    for (int j11 = yPos + regular.baseCharacterHeight + 2; s2.length() > 0; j11 += regular.baseCharacterHeight + 1) {// anInt1497
                        if (s2.indexOf("%") != -1) {
                            do {
                                int k7 = s2.indexOf("%1");
                                if (k7 == -1)
                                    break;
                                s2 = s2.substring(0, k7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 0))
                                        + s2.substring(k7 + 2);
                            } while (true);
                            do {
                                int l7 = s2.indexOf("%2");
                                if (l7 == -1)
                                    break;
                                s2 = s2.substring(0, l7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 1))
                                        + s2.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s2.indexOf("%3");
                                if (i8 == -1)
                                    break;
                                s2 = s2.substring(0, i8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 2))
                                        + s2.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s2.indexOf("%4");
                                if (j8 == -1)
                                    break;
                                s2 = s2.substring(0, j8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 3))
                                        + s2.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s2.indexOf("%5");
                                if (k8 == -1)
                                    break;
                                s2 = s2.substring(0, k8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 4))
                                        + s2.substring(k8 + 2);
                            } while (true);
                        }
                        int l11 = s2.indexOf("\\n");
                        String s5;
                        if (l11 != -1) {
                            s5 = s2.substring(0, l11);
                            s2 = s2.substring(l11 + 2);
                        } else {
                            s5 = s2;
                            s2 = "";
                        }
                        if (class9_1.centerText) {
                            regular.drawCenteredString(s5, xPos + class9_1.width / 2, j11, yPos, -1);
                        } else {
                            if (s5.contains("\\r")) {
                                String text = s5
                                        .substring(0, s5.indexOf("\\r"));
                                String text2 = s5
                                        .substring(s5.indexOf("\\r") + 2);
                                regular.drawString(text, xPos + 3, j11, 0, -1);
                                int rightX = boxWidth + xPos - regular.getTextWidth(text2) - 2;
                                regular.drawString(text2, rightX, j11, 0, -1);
                            } else {
                                regular.drawString(s5, xPos + 3, j11, 0, -1);
                            }
                        }
                    }
                } else if (class9_1.type == 9) {
                    if (openInterfaceID == 19882) {
                        if (mouseX >= 327) {
                            drawHoverBox(mouseX - (regular.getTextWidth(class9_1.message.split("\n")[1]) + 10), mouseY + 10, class9_1.message);
                        } else {
                            drawHoverBox(mouseX + 10, mouseY + 10, class9_1.message);
                        }
                    } else {
                        drawHoverBox(drawX, drawY, class9_1.message);
                    }
                } else if (class9_1.type == 12) {
                    RSFont textDrawingArea = interfaceFonts[class9_1.textDrawingAreas];
                    String s = class9_1.message;
                    boolean flag1 = false;
                    if (anInt1039 == class9_1.id || anInt1048 == class9_1.id
                            || anInt1026 == class9_1.id)
                        flag1 = true;
                    int i4;
                    if (interfaceIsSelected(class9_1)) {
                        i4 = class9_1.anInt219;
                        if (flag1 && class9_1.anInt239 != 0)
                            i4 = class9_1.anInt239;
                        if (class9_1.enabledText.length() > 0)
                            s = class9_1.enabledText;
                    } else {
                        i4 = class9_1.textColor;
                        if (flag1 && class9_1.textHoverColor != 0)
                            i4 = class9_1.textHoverColor;
                    }
                    if (class9_1.atActionType == 6 && isDialogueInterface) {
                        s = "Please wait...";
                        i4 = class9_1.textColor;
                    }

                    if ((class9_1.parentID == 1151)
                            || (class9_1.parentID == 12855)) {
                        switch (i4) {
                            case 16773120:
                                i4 = 0xFE981F;
                                break;
                            case 7040819:
                                i4 = 0xAF6A1A;
                                break;
                        }
                    }

                    for (int l6 = drawY + textDrawingArea.baseCharacterHeight; s.length() > 0; l6 += textDrawingArea.baseCharacterHeight) {
                        if (s.indexOf("%") != -1) {
                            do {
                                int k7 = s.indexOf("%1");
                                if (k7 == -1)
                                    break;
                                if (class9_1.id < 4000 || class9_1.id > 5000
                                        && class9_1.id != 13921
                                        && class9_1.id != 13922
                                        && class9_1.id != 12171
                                        && class9_1.id != 12172)
                                    s = s.substring(0, k7)
                                            + methodR(extractInterfaceValues(
                                            class9_1, 0))
                                            + s.substring(k7 + 2);
                                else
                                    s = s.substring(0, k7)
                                            + interfaceIntToString(extractInterfaceValues(
                                            class9_1, 0))
                                            + s.substring(k7 + 2);
                            } while (true);
                            do {
                                int l7 = s.indexOf("%2");
                                if (l7 == -1)
                                    break;
                                s = s.substring(0, l7)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 1))
                                        + s.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s.indexOf("%3");
                                if (i8 == -1)
                                    break;
                                s = s.substring(0, i8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 2))
                                        + s.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s.indexOf("%4");
                                if (j8 == -1)
                                    break;
                                s = s.substring(0, j8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 3))
                                        + s.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s.indexOf("%5");
                                if (k8 == -1)
                                    break;
                                s = s.substring(0, k8)
                                        + interfaceIntToString(extractInterfaceValues(
                                        class9_1, 4))
                                        + s.substring(k8 + 2);
                            } while (true);
                        }
                        int l8 = s.indexOf("\\n");
                        String s1;
                        if (l8 != -1) {
                            s1 = s.substring(0, l8);
                            s = s.substring(l8 + 2);
                        } else {
                            s1 = s;
                            s = "";
                        }
                        if (class9_1.centerText) {
                            textDrawingArea.drawCenteredString(s1, drawX
                                            + class9_1.width / 2, l6, i4,
                                    class9_1.textShadow ? 0 : -1
                            );
                        } else
                            textDrawingArea.drawString(s1,
                                    drawX, l6, i4, class9_1.textShadow ? 0 : -1);
                    }
                }
        }
        DrawingArea.setDrawingArea(i1, j1, k1, l1);
    }

    public void drawHoverBox(int xPos, int yPos, String text) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 8;
        int width;
        width = regular.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++)
            if (width <= regular.getTextWidth(results[i]) + 6)
                width = regular.getTextWidth(results[i]) + 6;
        DrawingArea.fillRect(xPos, yPos, width, height, 0xFFFFA0);
        DrawingArea.drawRect(xPos, yPos, width, height, 0);
        yPos += 14;
        for (int i = 0; i < results.length; i++) {
            regular.drawString(results[i], xPos + 3, yPos, 0,
                    -1);
            yPos += 16;
        }
    }

    public void drawHoverBox1(int xPos, int yPos, String text) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 3;
        int width = regular.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++)
            if (width <= regular.getTextWidth(results[i]) + 6)
                width = regular.getTextWidth(results[i]) + 6;
        DrawingArea.drawRect(xPos, yPos, width, height, 0xEBECE6);
        yPos += 14;
        for (int i = 0; i < results.length; i++) {
            regular.drawString(results[i], xPos + 3, yPos, 0xF5B241,
                    -1);
            yPos += 16;
        }
    }

    public final String methodR(/* int i, */int j) {
        // if(i <= 0)
        // pktType = inStream.readUnsignedByte();
        if (j >= 0 && j < 10000)
            return String.valueOf(j);
        if (j >= 10000 && j < 10000000)
            return j / 1000 + "K";
        if (j >= 10000000 && j < 999999999)
            return j / 1000000 + "M";
        if (j >= 999999999)
            return "*";
        else
            return "?";
    }

    public final String priceMod(int j) {
        if (j <= 0)
            return "FREE";
        if (j > 0 && j < 10000)
            return String.valueOf(j);
        if (j >= 10000 && j < 10000000)
            return j / 1000 + "K";
        if (j >= 10000000 && j < 999999999)
            return j / 1000000 + "M";
        if (j > 999999999)
            return "B";
        else
            return "?";
    }

    private void method107(int i, int j, Stream stream, Player player) {
        if ((i & 0x400) != 0) {
            player.anInt1543 = stream.getUnsignedByteS();
            player.anInt1545 = stream.getUnsignedByteS();
            player.anInt1544 = stream.getUnsignedByteS();
            player.anInt1546 = stream.getUnsignedByteS();
            player.anInt1547 = stream.method436() + loopCycle;
            player.anInt1548 = stream.readUnsignedWordA() + loopCycle;
            player.anInt1549 = stream.getUnsignedByteS();
            player.method446();
        }
        if ((i & 0x100) != 0) {
            player.spotAnimId = stream.method434();
            int k = stream.readDWord();
            player.anInt1524 = k >> 16;
            player.spotAnimDelay = loopCycle + (k & 0xffff);
            player.spotAnimFrame = 0;
            player.spotAnimNextFrame = 1;
            player.spotAnimFrameDelay = 0;
            if (player.spotAnimDelay > loopCycle)
                player.spotAnimFrame = -1;
            if (player.spotAnimId == 65535)
                player.spotAnimId = -1;
        }
        if ((i & 8) != 0) {
            int l = stream.method434();
            if (l == 65535)
                l = -1;
            int i2 = stream.method427();
            if (l == player.animId && l != -1) {
                int i3 = Animation.anims[l].anInt365;
                if (i3 == 1) {
                    player.animFrame = 0;
                    player.animNextFrame = 1;
                    player.animFrameDelay = 0;
                    player.animDelay = i2;
                    player.animCyclesElapsed = 0;
                }
                if (i3 == 2)
                    player.animCyclesElapsed = 0;
            } else if (l == -1
                    || player.animId == -1
                    || Animation.anims[l].anInt359 >= Animation.anims[player.animId].anInt359) {
                player.animId = l;
                player.animFrame = 0;
                player.animNextFrame = 1;
                player.animFrameDelay = 0;
                player.animDelay = i2;
                player.animCyclesElapsed = 0;
                player.anInt1542 = player.smallXYIndex;
            }
        }
        if ((i & 4) != 0) {
            player.textSpoken = stream.readString();
            if (player.textSpoken.charAt(0) == '~') {
                player.textSpoken = player.textSpoken.substring(1);
                pushMessage(player.textSpoken, ChatMessage.PUBLIC_MESSAGE_PLAYER, player.name, player.titleText);
            } else if (player == myPlayer) {
                pushMessage(player.textSpoken, ChatMessage.PUBLIC_MESSAGE_PLAYER, player.name, player.titleText);
            }
            player.anInt1513 = 0;
            player.anInt1531 = 0;
            player.textCycle = 150;
        }
        if ((i & 0x80) != 0) {
            int i1 = stream.method434();
            int rights = stream.readUnsignedByte();
            int j3 = stream.method427();
            int k3 = stream.currentOffset;

            if (player.name != null && player.visible) {
                long l3 = TextClass.longForName(player.name);
                boolean flag = false;
                if (rights <= 1) {
                    for (int i4 = 0; i4 < ignoreCount; i4++) {
                        if (ignoreListAsLongs[i4] != l3)
                            continue;
                        flag = true;
                        break;
                    }
                }
                if (!flag && anInt1251 == 0)
                    try {
                        aStream_834.currentOffset = 0;
                        stream.method442(j3, 0, aStream_834.buffer);
                        aStream_834.currentOffset = 0;
                        String s = TextInput.method525(j3, aStream_834);
                        // s = Censor.doCensor(s);
                        player.textSpoken = s;
                        player.anInt1513 = i1 >> 8;
                        player.privilage = rights;
                        player.anInt1531 = i1 & 0xff;
                        player.textCycle = 150;
                        int chatType = rights == 0 ? ChatMessage.PUBLIC_MESSAGE_PLAYER : ChatMessage.PUBLIC_MESSAGE_STAFF;
                        pushMessage(s, chatType, player.name, getCrown(rights) + getEliteCrown(player.elite) + (player.titleText == null ? "" : player.titleText));
                    } catch (Exception exception) {
                        signlink.reporterror("cde2");
                    }
            }
            stream.currentOffset = k3 + j3;
        }
        if ((i & 0x1) != 0) {
            int index = stream.method434();
            player.interactingEntity = index;
            if (player.interactingEntity == 65535)
                player.interactingEntity = -1;
        }
        if ((i & 0x10) != 0) {
            int j1 = stream.method427();
            byte abyte0[] = new byte[j1];
            Stream stream_1 = new Stream(abyte0);
            stream.readBytes(j1, 0, abyte0);
            playerUpdateStreams[j] = stream_1;
            player.updatePlayer(stream_1);
        }
        if ((i & 2) != 0) {
            player.anInt1538 = stream.method436();
            player.anInt1539 = stream.method434();
        }
        if ((i & 0x20) != 0) {
            int damage = inStream.readUnsignedWordA();
            int hitmarkType = inStream.readUnsignedWordA();
            int icon = stream.readUnsignedByte();

            //int damagerIndex = inStream.readUnsignedWordA();
            //if (hitmarkType == 0 && damagerIndex != myPlayer.playerIndex)
            //    hitmarkType = 12; // for testing


            if (Settings.hitmarks != Constants.HITMARKS_634 && hitmarkType != 2) {
                if (damage > 0)
                    hitmarkType = 1;
                else
                    hitmarkType = 0;
                player.updateHitDataOld(hitmarkType, getHit(damage), loopCycle);
            } else
                player.updateHitDataNew(hitmarkType, getHit(damage), loopCycle, icon);

            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = inStream.readUnsignedWordA();
            player.maxHealth = inStream.readUnsignedWordA();
        }
        if ((i & 0x200) != 0) {
            int l1 = inStream.readUnsignedWordA();
            int type = inStream.readUnsignedWordA();
            int icon = stream.readUnsignedByte();

            //int damagerIndex = inStream.readUnsignedWordA();
            // if (type == 0 && damagerIndex != myPlayer.playerIndex)
            //    type = 12; // for testing


            if (Settings.hitmarks != Constants.HITMARKS_634 && type != 2) {
                if (l1 > 0)
                    type = 1;
                else
                    type = 0;
                player.updateHitDataOld(type, getHit(l1), loopCycle);
            } else
                player.updateHitDataNew(type, getHit(l1), loopCycle, icon);
            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = inStream.readUnsignedWordA();
            player.maxHealth = inStream.readUnsignedWordA();
        }
    }

    public int getHit(int hit) {
        if (Settings.newHitNumbers && hit != 0) {
            return hit * 10 + random.nextInt(9);
        }
        return hit;
    }

    private String getCrown(int rights) {
        switch (rights) {
            case 1:
                return "<img=0>";
            case 2:
            case 3:
                return "<img=1>";
            case 4:
                return "<img=3>";
            case 5:
                return "<img=4>";
            case 6:
                return "<img=5>";
            default:
                return "";
        }
    }

    private String getEliteCrown(boolean crown) {
        return crown ? "<img=6>" : "";
    }

    private void method108() {
        try {
            int j = myPlayer.x + anInt1278;
            int k = myPlayer.y + anInt1131;
            if (anInt1014 - j < -500 || anInt1014 - j > 500
                    || anInt1015 - k < -500 || anInt1015 - k > 500) {
                anInt1014 = j;
                anInt1015 = k;
            }
            if (anInt1014 != j)
                anInt1014 += (j - anInt1014) / 16;
            if (anInt1015 != k)
                anInt1015 += (k - anInt1015) / 16;
            if (clickMode2 == 3) {
                int deltaY = super.mouseY - savedMouseY;
                anInt1187 = deltaY;
                savedMouseY = deltaY != -1 && deltaY != 1 ? (savedMouseY + super.mouseY) / 2 : super.mouseY;

                int deltaX = savedMouseX - super.mouseX;
                anInt1186 = deltaX;
                savedMouseX = deltaX != -1 && deltaX != 1 ? (savedMouseX + super.mouseX) / 2 : super.mouseX;
            } else {
                if (super.keyArray[1] == 1)
                    anInt1186 += (-24 - anInt1186) / 2;
                else if (super.keyArray[2] == 1)
                    anInt1186 += (24 - anInt1186) / 2;
                else
                    anInt1186 /= 2;
                if (super.keyArray[3] == 1)
                    anInt1187 += (12 - anInt1187) / 2;
                else if (super.keyArray[4] == 1)
                    anInt1187 += (-12 - anInt1187) / 2;
                else
                    anInt1187 /= 2;

                savedMouseX = super.mouseX;
                savedMouseY = super.mouseY;
            }
            minimapInt1 = (minimapInt1 + (anInt1186 / 2 << 3));
            minimapInt1 &= 0x3fff;

            anInt1184 += anInt1187 / 2 << 3;
            if (anInt1184 < 1024)
                anInt1184 = 1024;
            if (anInt1184 > 3072)
                anInt1184 = 3072;
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = getFloorDrawHeight(plane, anInt1015, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = plane;
                        if (l2 < 3 && (tileSettingBits[1][l1][k2] & 2) == 2)
                            l2++;
                        int i3 = j1 - intGroundArray[l2][l1][k2];
                        if (i3 > k1)
                            k1 = i3;
                    }

                }

            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00)
                j2 = 0x17f00;
            if (j2 < 32768)
                j2 = 32768;
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
            }
        } catch (Exception _ex) {
            signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y
                    + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + ","
                    + anInt1070 + "," + baseX + "," + baseY);
            throw new RuntimeException("eek");
        }
    }

    @Override
    public void mainRedraw() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        anInt1061++;
        if (!loggedIn)
            drawLoginScreen();
        else
            drawGameScreen();
        anInt1213 = 0;
    }

    private boolean isFriendOrSelf(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < friendsCount; i++)
            if (s.equalsIgnoreCase(friendsList[i]))
                return true;
        return s.equalsIgnoreCase(myPlayer.name);
    }

    private void setWaveVolume(int i) {
    	/* empty */
    }

    public void drawLoginScreen() {
        int centerX = frameWidth / 2, centerY = frameHeight / 2;
        fullGameScreen.initDrawingArea();
        DrawingArea.setAllPixelsToZero(0);
        cacheSprite[338].drawSprite(centerX - cacheSprite[338].myWidth / 2, centerY - cacheSprite[338].myHeight / 2);
        cacheSprite[57].drawAdvancedSprite(centerX - cacheSprite[57].myWidth / 2 - 10, centerY - cacheSprite[57].myHeight / 2 + 30);
        cacheSprite[337].drawAdvancedSprite(centerX - cacheSprite[337].myWidth / 2 - 10, centerY - cacheSprite[337].myHeight / 2 - 150);
        if (mouseInRegion(centerX - 177, centerY + 117, centerX - 149, centerY + 146)) {
            cacheSprite[62].drawSprite(centerX - 177, centerY + 115);
        }
        if (mouseInRegion(centerX - 135, centerY + 117, centerX - 105, centerY + 146)) {
            cacheSprite[63].drawSprite(centerX - 134, centerY + 115);
        }
        if (mouseInRegion(centerX - 90, centerY + 113, centerX - 60, centerY + 146)) {
            cacheSprite[64].drawSprite(centerX - 89, centerY + 115);
        }
        if (mouseInRegion(centerX + 96, centerY + 124, centerX + 185, centerY + 147)) {
            cacheSprite[59].drawSprite(centerX + 98, centerY + 125);
        }
        if (mouseInRegion(centerX + 14, centerY + 26, centerX + 184, centerY + 52)) {
            cacheSprite[58].drawSprite(centerX + 13, centerY + 26);
        }
        if (mouseInRegion(centerX + 14, centerY + 77, centerX + 184, centerY + 102)) {
            cacheSprite[58].drawSprite(centerX + 14, centerY + 77);
        }
        if (mouseInRegion(centerX - 5, centerY + 107, centerX + 117, centerY + 122)) {
            if (Settings.remember) {
                cacheSprite[69].drawAdvancedSprite(centerX - 10, centerY + 104);
            } else if (!Settings.remember) {
                cacheSprite[67].drawAdvancedSprite(centerX - 10, centerY + 104);
            }
        } else {
            if (Settings.remember) {
                cacheSprite[68].drawAdvancedSprite(centerX - 10, centerY + 104);
            } else {
                cacheSprite[66].drawAdvancedSprite(centerX - 10, centerY + 104);
            }
        }
        fancy.drawString("Fixed", centerX - 201, centerY + 87, 0xf3af40, 0);
        fancy.drawString("Resizable", centerX - 150, centerY + 87, 0xf3af40, 0);
        fancy.drawString("Fullscreen", centerX - 82, centerY + 87, 0xf3af40, 0);
        if (clientSize == 0 || mouseInRegion(centerX - 208, centerY + 25, centerX - 151, centerY + 68)) {
            cacheSprite[340].drawSprite(centerX - 207, centerY + 25);
        } else {
            cacheSprite[53].drawSprite(centerX - 207, centerY + 25);
        }
        if (clientSize == 1 || mouseInRegion(centerX - 144, centerY + 25, centerX - 90, centerY + 68)) {
            cacheSprite[341].drawSprite(centerX - 144, centerY + 25);
        } else {
            cacheSprite[65].drawSprite(centerX - 144, centerY + 25);
        }
        if (clientSize == 2 || mouseInRegion(centerX - 80, centerY + 25, centerX - 25, centerY + 68)) {
            cacheSprite[342].drawSprite(centerX - 80, centerY + 25);
        } else {
            cacheSprite[70].drawSprite(centerX - 80, centerY + 25);
        }
        fancy.drawString(TextClass.fixName(myUsername) + (((loginScreenCursorPos == 0 ? 1 : 0) & (loopCycle % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""), centerX + 20, centerY + 44, 0xf3af40, 0);// Username
        regular.drawString(TextClass.passwordAsterisks(myPassword) + (((loginScreenCursorPos == 1 ? 1 : 0) & (loopCycle % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""), centerX + 20, centerY + 97, 0xf3af40, 0);// Password
        if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
            DrawingArea.blur();
            fancy.drawCenteredString("Press the 'Enter' key to remove this screen.", centerX - 185, centerY - 210, 0xf3af40, 0);
            fancy.drawCenteredString(loginMessage1, centerX, centerY, 0xf3af40, 0);
            fancy.drawCenteredString(loginMessage2, centerX, centerY + 15, 0xf3af40, 0);
        }
        fullGameScreen.drawGraphics(super.getGraphics(), 0, 0);
    }

    private void processLoginScreenInput() {
        if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
            if (readChar() == 10) {
                loginMessage1 = "";
                loginMessage2 = "";
            }
        }
        if (loginMessage1.length() == 0 && loginMessage2.length() == 0) {
            int centerX = frameWidth / 2, centerY = frameHeight / 2;
            if (super.clickMode3 == 1 && clickInRegion(centerX - 208, centerY + 25, centerX - 151, centerY + 68)) {
                toggleSize(0, false);
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 144, centerY + 25, centerX - 90, centerY + 68)) {
                toggleSize(1, false);
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 80, centerY + 25, centerX - 25, centerY + 68)) {
                toggleSize(2, false);
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 5, centerY + 107, centerX + 117, centerY + 122)) {
                Settings.remember = !Settings.remember;
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX + 14, centerY + 26, centerX + 184, centerY + 52)) {
                loginScreenCursorPos = 0;
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX + 14, centerY + 77, centerX + 184, centerY + 102)) {
                loginScreenCursorPos = 1;
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 177, centerY + 117, centerX - 149, centerY + 146)) {
                launchURL("https://www.facebook.com/");
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 135, centerY + 117, centerX - 105, centerY + 146)) {
                launchURL("https://www.twitter.com/");
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX - 90, centerY + 113, centerX - 60, centerY + 146)) {
                launchURL("https://www.youtube.com/");
            }
            if (super.clickMode3 == 1 && clickInRegion(centerX + 96, centerY + 124, centerX + 185, centerY + 147)) {
                if (myUsername.length() > 0 && myPassword.length() > 0) {
                    loginFailures = 0;
                    login(myUsername, myPassword, false);
                    if (loggedIn) {
                        return;
                    }
                } else {
                    loginMessage2 = "Please enter a username and password.";
                }
            }
            do {
                int keyCharacter = readChar();
                if (keyCharacter == -1)
                    break;
                boolean isValid = false;
                for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
                    if (keyCharacter != validUserPassChars.charAt(i2)) {
                        continue;
                    }
                    isValid = true;
                    break;
                }
                if (loginScreenCursorPos == 0) {
                    if (keyCharacter == 8 && myUsername.length() > 0)
                        myUsername = myUsername.substring(0, myUsername.length() - 1);
                    if (keyCharacter == 9 || keyCharacter == 10 || keyCharacter == 13)
                        loginScreenCursorPos = 1;
                    if (isValid)
                        myUsername += (char) keyCharacter;
                    if (myUsername.length() > 12)
                        myUsername = myUsername.substring(0, 12);
                } else if (loginScreenCursorPos == 1) {
                    if (keyCharacter == 8 && myPassword.length() > 0)
                        myPassword = myPassword.substring(0, myPassword.length() - 1);
                    if (keyCharacter == 9)
                        loginScreenCursorPos = 0;
                    if (keyCharacter == 10 || keyCharacter == 13)
                        login(myUsername, myPassword, false);
                    if (isValid)
                        myPassword += (char) keyCharacter;
                    if (myPassword.length() > 20)
                        myPassword = myPassword.substring(0, 20);
                }
            } while (true);
            return;
        }
    }

    private void draw3dScreen(boolean isFixed) {
        if (coinToggle) {
            drawCoinAmount(isFixed);
        }
        drawCoinLastAmount(isFixed);
        if (counterOn)
            drawCounterOnScreen(isFixed);
        if (showChat)
            drawSplitPrivateChat(isFixed);
        if (crossType == 1) {
            int offSet = isFixed ? 4 : 0;
            crosses[crossIndex / 100].drawSprite(crossX - 8 - offSet, crossY - 8 - offSet);
        }
        if (crossType == 2) {
            int offSet = isFixed ? 4 : 0;
            crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - offSet, crossY - 8 - offSet);
        }
        if (currentStatusInterface != -1) {
            if (currentStatusInterface == 197) {
                method119(anInt945, currentStatusInterface);
                drawInterface(0, isFixed ? 0 : frameWidth / 2 + 80, RSInterface.interfaceCache[currentStatusInterface], isFixed ? 0 : (frameHeight / 2) - 550, false);
            } else if (currentStatusInterface == 201 && !isFixed) {
                drawInterface(0, frameWidth - 560, RSInterface.interfaceCache[currentStatusInterface], -100, false);
            } else if (currentStatusInterface == 16210 && !isFixed) {
                drawInterface(0, frameWidth - 700, RSInterface.interfaceCache[currentStatusInterface], 5, false);
            } else {
                drawInterface(0, 0, RSInterface.interfaceCache[currentStatusInterface], 0, false);
            }
        }
        if (openInterfaceID != -1) {
            method119(anInt945, openInterfaceID);
            int w = 512, h = 334;
            int x = isFixed ? 0 : (frameWidth / 2) - 256;
            int y = isFixed ? 0 : (frameHeight / 2) - 167;
            int count = 3;
            if (!isFixed) {
                for (int i = 0; i < count; i++) {
                    if (x + w > (frameWidth - 225)) {
                        x = x - 30;
                        if (x < 0) {
                            x = 0;
                        }
                    }
                    if (y + h > (frameHeight - 182)) {
                        y = y - 30;
                        if (y < 0) {
                            y = 0;
                        }
                    }
                }
            }
            drawInterface(0, x, RSInterface.interfaceCache[openInterfaceID], y, false);
            drawGrandExchange();
        }
        method70();
        if (!MiniMenu.open) {
            processRightClick(isFixed);
            if (!Settings.mouseHover) {
            	drawTooltip();
            } else {
            	drawTooltipHover(isFixed ? 4 : 0, isFixed ? 4 : 0);
            }
        } else {
            drawMenu(isFixed ? 4 : 0, isFixed ? 4 : 0);
        }
        if (anInt1055 == 1) {
            if (isFixed)
            	multiOverlay.drawSprite(472, 296);
            else
            	cacheSprite[96].drawSprite(frameWidth - 35, 170);
        }
        if (clientData) {
            regular.drawString("Fps: " + super.fps, 5, 285,
                    0xffff00);
            Runtime runtime = Runtime.getRuntime();
            int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            regular.drawString("Mem: " + j1 + "k", 5, 299,
                    0xffff00);
            regular.drawString("Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, 5, 314, 0xffff00);
            int x = baseX + (myPlayer.x - 6 >> 7);
            int y = baseY + (myPlayer.y - 6 >> 7);
            regular.drawString("Coords: " + x + ", " + y + " Base - x: " + myPlayer.smallX[0] + " y: " + myPlayer.smallY[0], 5, 329, 0xffff00);
        }
        if (systemUpdateTime != 0) {
            int j = systemUpdateTime / 50;
            int l = j / 60;
            int yOffset = isFixed ? 0 : frameHeight - 498;
            j %= 60;
            if (j < 10)
                regular.drawString("System update in: "
                        + l + ":0" + j, 4, 329 + yOffset, 0xffff00);
            else
                regular.drawString("System update in: "
                        + l + ":" + j, 4, 329 + yOffset, 0xffff00);
        }

        if (myPlayer.interactingEntity != -1) {
            Mobile entity = getEntity(myPlayer.interactingEntity);
            if (entity != null) {
                String name = "";
                if (entity instanceof NPC) {
                    name = ((NPC) entity).desc.name;
                } else {
                    name = ((Player) entity).name;
                }
                int x = 5;
                int y = 20;
                DrawingArea.fillRectAlpha(x, y, 135, 40, 10, 50);
                x = 72 - bold.getBasicWidth(name) / 2;
                y = 34;
                bold.drawBasicString(name, x, y, 0xffffff, 0);
                if (entity.loopCycleStatus > loopCycle) {
                    int i1 = entity.currentHealth * 125 / entity.maxHealth;
                    if (i1 > 125)
                        i1 = 125;
                    x = 10;
                    y = 40;
                    DrawingArea.fillRectAlpha(x, y, i1, 13, 65280, 100);
                    DrawingArea.fillRectAlpha(x + i1, y, 125 - i1, 13, 0xff0000, 100);

                    x = 50;
                    y = 51;
                    name = Integer.toString(entity.currentHealth);
                    bold.drawBasicString(name, x, y, 0xffffff, 0);
                    x += bold.getBasicWidth(name) + 1;
                    bold.drawBasicString("/", x, y, 0xffffff, 0);
                    x += bold.getBasicWidth("/") + 2;
                    bold.drawBasicString(Integer.toString(entity.maxHealth), x, y, 0xffffff, 0);
                }
            }
        }
    }

    private void addIgnore(long l) {
        if (l == 0L)
            return;
        if (ignoreCount >= 100) {
            pushMessage("Your ignore list is full. Max of 100 hit", ChatMessage.GAME_MESSAGE);
            return;
        }
        String s = TextClass.fixName(TextClass.nameForLong(l));
        for (int j = 0; j < ignoreCount; j++)
            if (ignoreListAsLongs[j] == l) {
                pushMessage(s + " is already on your ignore list", ChatMessage.GAME_MESSAGE);
                return;
            }
        for (int k = 0; k < friendsCount; k++)
            if (friendsListAsLongs[k] == l) {
                pushMessage("Please remove " + s
                        + " from your friend list first", ChatMessage.GAME_MESSAGE);
                return;
            }

        ignoreListAsLongs[ignoreCount++] = l;
        reDrawTabArea = true;
        stream.createFrame(133);
        stream.putLong(l);
    }

    private void method114() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1)
                j = myPlayerIndex;
            else
                j = playerIndices[i];
            Player player = playerArray[j];
            if (player != null)
                method96(player);
        }

    }

    private void method115() {
        if (loadingStage == 2) {
            for (GameObjectSpawnRequest class30_sub1 = (GameObjectSpawnRequest) aClass19_1179
                    .reverseGetFirst(); class30_sub1 != null; class30_sub1 = (GameObjectSpawnRequest) aClass19_1179
                    .reverseGetNext()) {
                if (class30_sub1.anInt1294 > 0)
                    class30_sub1.anInt1294--;
                if (class30_sub1.anInt1294 == 0) {
                    if (class30_sub1.anInt1299 < 0
                            || ObjectManager.method178(class30_sub1.anInt1299,
                            class30_sub1.anInt1301)) {
                        method142(class30_sub1.y, class30_sub1.plane,
                                class30_sub1.anInt1300, class30_sub1.anInt1301,
                                class30_sub1.x, class30_sub1.anInt1296,
                                class30_sub1.anInt1299);
                        class30_sub1.unlink();
                    }
                } else {
                    if (class30_sub1.anInt1302 > 0)
                        class30_sub1.anInt1302--;
                    if (class30_sub1.anInt1302 == 0
                            && class30_sub1.x >= 1
                            && class30_sub1.y >= 1
                            && class30_sub1.x <= 102
                            && class30_sub1.y <= 102
                            && (class30_sub1.anInt1291 < 0 || ObjectManager
                            .method178(class30_sub1.anInt1291,
                                    class30_sub1.anInt1293))) {
                        method142(class30_sub1.y, class30_sub1.plane,
                                class30_sub1.anInt1292, class30_sub1.anInt1293,
                                class30_sub1.x, class30_sub1.anInt1296,
                                class30_sub1.anInt1291);
                        class30_sub1.anInt1302 = -1;
                        if (class30_sub1.anInt1291 == class30_sub1.anInt1299
                                && class30_sub1.anInt1299 == -1)
                            class30_sub1.unlink();
                        else if (class30_sub1.anInt1291 == class30_sub1.anInt1299
                                && class30_sub1.anInt1292 == class30_sub1.anInt1300
                                && class30_sub1.anInt1293 == class30_sub1.anInt1301)
                            class30_sub1.unlink();
                    }
                }
            }

        }
    }

    private void determineMenuSize() {
        int biggestOptionWidth = bold.getTextWidth("Choose option");
        for (MiniMenuOption menuOption = (MiniMenuOption) MiniMenu.options.reverseGetFirst(); menuOption != null; menuOption = (MiniMenuOption) MiniMenu.options.reverseGetNext()) {
            int optionWidth = bold.getTextWidth(MiniMenu.getOption(menuOption));
            if (biggestOptionWidth < optionWidth)
                biggestOptionWidth = optionWidth;
        }
        biggestOptionWidth += 8;
        int height = 15 * MiniMenu.optionCount + 21;
		int posX = super.saveClickX - biggestOptionWidth / 2;
		if (posX + biggestOptionWidth > frameWidth) {
			posX = frameWidth - biggestOptionWidth;
		}
		if (posX < 0) {
			posX = 0;
		}
		int posY = super.saveClickY;
		if (posY + height > frameHeight) {
			posY = frameHeight - height;
		}
		if (posY < 0) {
			posY = 0;
		}
		MiniMenu.open = true;
		menuOffsetX = posX;
		menuOffsetY = posY;
		menuWidth = biggestOptionWidth;
		menuHeight = height + 1;
    }

    private void method117(Stream stream) {
        stream.initBitAccess();
        int j = stream.readBits(1);
        if (j == 0)
            return;
        int k = stream.readBits(2);
        if (k == 0) {
            sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = myPlayerIndex;
            return;
        }
        if (k == 1) {
            int l = stream.readBits(3);
            myPlayer.moveInDir(false, l);
            int k1 = stream.readBits(1);
            if (k1 == 1)
                sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = myPlayerIndex;
            return;
        }
        if (k == 2) {
            int i1 = stream.readBits(3);
            myPlayer.moveInDir(true, i1);
            int l1 = stream.readBits(3);
            myPlayer.moveInDir(true, l1);
            int j2 = stream.readBits(1);
            if (j2 == 1)
                sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = myPlayerIndex;
            return;
        }
        if (k == 3) {
            plane = stream.readBits(2);
            int j1 = stream.readBits(1);
            int i2 = stream.readBits(1);
            if (i2 == 1)
                sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = myPlayerIndex;
            int k2 = stream.readBits(7);
            int l2 = stream.readBits(7);
            myPlayer.setPos(l2, k2, j1 == 1);
        }
    }

    private boolean method119(int i, int j) {
        boolean flag1 = false;
        RSInterface class9 = RSInterface.interfaceCache[j];
        if (class9 == null || class9.children == null) {
            System.out.println(j + " is null");
            return false;
        }
        for (int k = 0; k < class9.children.length; k++) {
            if (class9.children[k] == -1)
                break;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
            if (class9_1.type == 1)
                flag1 |= method119(i, class9_1.id);
            if (class9_1.type == 6
                    && (class9_1.disabledAnimation != -1 || class9_1.enabledAnimation != -1)) {
                boolean flag2 = interfaceIsSelected(class9_1);
                int l;
                if (flag2)
                    l = class9_1.enabledAnimation;
                else
                    l = class9_1.disabledAnimation;
                if (l != -1) {
                    Animation animation = Animation.anims[l];
                    for (class9_1.anInt208 += i; class9_1.anInt208 > animation
                            .delays[class9_1.anInt246]; ) {
                        class9_1.anInt208 -= animation
                                .delays[class9_1.anInt246] + 1;
                        class9_1.anInt246++;
                        if (class9_1.anInt246 >= animation.frameCount) {
                            class9_1.anInt246 -= animation.padding;
                            if (class9_1.anInt246 < 0
                                    || class9_1.anInt246 >= animation.frameCount)
                                class9_1.anInt246 = 0;
                        }
                        flag1 = true;
                    }

                }
            }
        }

        return flag1;
    }

    private int method120() {
        int j = 3;
        if (yCameraCurve < 2480) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = myPlayer.x >> 7;
            int j1 = myPlayer.y >> 7;
            if ((tileSettingBits[plane][k][l] & 4) != 0)
                j = plane;
            int k1;
            if (i1 > k)
                k1 = i1 - k;
            else
                k1 = k - i1;
            int l1;
            if (j1 > l)
                l1 = j1 - l;
            else
                l1 = l - j1;
            if (k1 > l1) {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1)
                        k++;
                    else if (k > i1)
                        k--;
                    if ((tileSettingBits[plane][k][l] & 4) != 0)
                        j = plane;
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1)
                            l++;
                        else if (l > j1)
                            l--;
                        if ((tileSettingBits[plane][k][l] & 4) != 0)
                            j = plane;
                    }
                }
            } else {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1)
                        l++;
                    else if (l > j1)
                        l--;
                    if ((tileSettingBits[plane][k][l] & 4) != 0)
                        j = plane;
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1)
                            k++;
                        else if (k > i1)
                            k--;
                        if ((tileSettingBits[plane][k][l] & 4) != 0)
                            j = plane;
                    }
                }
            }
        }
        if ((tileSettingBits[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0)
            j = plane;
        return j;
    }

    private int method121() {
        int j = getFloorDrawHeight(plane, yCameraPos, xCameraPos);
        if (j - zCameraPos < 800
                && (tileSettingBits[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
            return plane;
        else
            return 3;
    }

    private void delIgnore(long l) {
        if (l == 0L)
            return;
        for (int j = 0; j < ignoreCount; j++)
            if (ignoreListAsLongs[j] == l) {
                ignoreCount--;
                reDrawTabArea = true;
                System.arraycopy(ignoreListAsLongs, j + 1,
                        ignoreListAsLongs, j, ignoreCount - j);

                stream.createFrame(74);
                stream.putLong(l);
                return;
            }
    }

    @Override
    public String getParameter(String s) {
    	return super.getParameter(s);
    }

    private void adjustVolume(boolean flag, int i) {
    	/* empty */
    }

    private int extractInterfaceValues(RSInterface class9, int j) {
        if (class9.valueIndexArray == null || j >= class9.valueIndexArray.length)
            return -2;
        try {
            int ai[] = class9.valueIndexArray[j];
            int k = 0;
            int l = 0;
            int i1 = 0;
            do {
                int j1 = ai[l++];
                int k1 = 0;
                byte byte0 = 0;
                if (j1 == 0)
                    return k;
                if (j1 == 1) {
                    k1 = stats[ai[l++]];
                }
                if (j1 == 2) {
                    k1 = statsBase[ai[l++]];
                }
                if (j1 == 3)
                    k1 = statsXp[ai[l++]];
                if (j1 == 4) {
                    RSInterface class9_1 = RSInterface.interfaceCache[ai[l++]];
                    int k2 = ai[l++];
                    if (k2 >= 0 && k2 < ItemDefinition.totalItems && (!ItemDefinition.forID(k2).membersItem || isMembers)) {
                        for (int j3 = 0; j3 < class9_1.inv.length; j3++)
                            if (class9_1.inv[j3] == k2 + 1)
                                k1 += class9_1.invStackSizes[j3];

                    }
                }
                if (j1 == 5) {
                    //System.out.println("config id: " + l + " config index: " + ai[l] + " config value: " + variousSettings[ai[l]]);
                    k1 = playerVariables[ai[l++]];
                }
                if (j1 == 6)
                    k1 = xpForLvl[statsBase[ai[l++]] - 1];
                if (j1 == 7)
                    k1 = (playerVariables[ai[l++]] * 100) / 46875;
                if (j1 == 8)
                    k1 = myPlayer.combatLevel;
                if (j1 == 9) {
                    for (int l1 = 0; l1 < SkillConstants.skillsCount; l1++)
                        if (SkillConstants.skillEnabled[l1])
                            k1 += statsBase[l1];
                }
                if (j1 == 10) {
                    RSInterface class9_2 = RSInterface.interfaceCache[ai[l++]];
                    int l2 = ai[l++] + 1;
                    if (l2 >= 0 && l2 < ItemDefinition.totalItems
                            && (!ItemDefinition.forID(l2).membersItem || isMembers)) {
                        for (int k3 = 0; k3 < class9_2.inv.length; k3++) {
                            if (class9_2.inv[k3] != l2)
                                continue;
                            k1 = 0x3b9ac9ff;
                            break;
                        }

                    }
                }
                if (j1 == 11)
                    k1 = energy;
                if (j1 == 12)
                    k1 = weight;
                if (j1 == 13) {
                    int i2 = playerVariables[ai[l++]];
                    int i3 = ai[l++];
                    k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
                }
                if (j1 == 14) {
                    int j2 = ai[l++];
                    k1 = VarBit.getValue(j2);
                }
                if (j1 == 15)
                    byte0 = 1;
                if (j1 == 16)
                    byte0 = 2;
                if (j1 == 17)
                    byte0 = 3;
                if (j1 == 18)
                    k1 = (myPlayer.x >> 7) + baseX;
                if (j1 == 19)
                    k1 = (myPlayer.y >> 7) + baseY;
                if (j1 == 20)
                    k1 = ai[l++];
                if (byte0 == 0) {
                    if (i1 == 0)
                        k += k1;
                    if (i1 == 1)
                        k -= k1;
                    if (i1 == 2 && k1 != 0)
                        k /= k1;
                    if (i1 == 3)
                        k *= k1;
                    i1 = 0;
                } else {
                    i1 = byte0;
                }
            } while (true);
        } catch (Exception _ex) {

            _ex.printStackTrace();

            return -1;
        }
    }

	public void drawTooltipHover(int x, int y) {
		boolean oldTooltipHoverActive = tooltipHoverActive;
		if (Settings.mouseHover && MiniMenu.optionCount > 0 && super.mouseY != -1 && super.mouseX != -1) {
			drawHover(x, y);
		} else {
			tooltipHoverActive = false;
		}
		if (oldTooltipHoverActive != tooltipHoverActive) {
			reDrawChatArea = true;
			reDrawTabArea = true;
		}
	}

	private void drawHover(int x, int y) {
		if (MiniMenu.optionCount < 2 && itemSelected == 0 && targetInterfaceId == 0) {
			tooltipHoverActive = false;
			return;
		}
		if (itemSelected == 1 && MiniMenu.optionCount < 2) {
			tooltipHoverActive = false;
			return;
		} else if (targetInterfaceId != 0 && MiniMenu.optionCount < 2) {
			tooltipHoverActive = false;
			return;
		}
		MiniMenuOption miniMenuOption = ((MiniMenuOption) MiniMenu.options.head.next);
		tooltipHoverText = MiniMenu.getOption(miniMenuOption);
		if (miniMenuOption.uid == 516 || tooltipHoverText.isEmpty()) {// walk here
			tooltipHoverActive = false;
			return;
		}
		reDrawChatArea = true;
		reDrawTabArea = true;
		buildMenu(tooltipHoverText, x, y);
	}

	private void buildMenu(String text, int x, int y) {
		int drawX = super.mouseX + 10;
		int drawY = super.mouseY + 10;
		int boxWidth = 8 + regular.getTextWidth(tooltipHoverText);
		int boxHeight = 11 + regular.baseCharacterHeight;
		if (drawX < frameWidth && drawY < frameHeight) {
			if (drawX + boxWidth + 3 > frameWidth) {
				drawX = frameWidth - boxWidth - 3;
			}
			if (drawY + boxHeight + 3 > frameHeight) {
				drawY = frameHeight - boxHeight - 3;
			}
		}
		drawX -= x;
		drawY -= y;
		DrawingArea.drawRectAlpha(drawX + 1, drawY + 1, boxWidth + 3, boxHeight - 2, 0, 170);
		DrawingArea.drawRectAlpha(drawX + 2, drawY, boxWidth + 1, boxHeight, 0, 170);
		DrawingArea.drawRectAlpha(drawX + 3, drawY + 1, boxWidth - 1, boxHeight - 2, 0xffffff, 256);
		DrawingArea.drawRectAlpha(drawX + 2, drawY + 2, boxWidth + 1, boxHeight - 4, 0xffffff, 256);
		DrawingArea.fillRectAlpha(drawX + 3, drawY + 2, boxWidth - 1, boxHeight - 4, 0x222222, 220);
		int getY = drawY + regular.baseCharacterHeight + 4;
		regular.drawString(text, drawX + 6, getY, 0xffffff);
		tooltipHoverActive = true;
	}

    private void drawTooltip() {
        if (MiniMenu.optionCount < 2 && itemSelected == 0 && targetInterfaceId == 0) {
            return;
        }
        StringBuilder s1 = new StringBuilder();
        if (itemSelected == 1 && MiniMenu.optionCount < 2) {
            s1.append("Use ");
            s1.append(selectedItemName);
            s1.append(" with...");
        } else if (targetInterfaceId != 0 && MiniMenu.optionCount < 2) {
            s1.append(targetOption);
            s1.append("...");
        } else {
            s1.append(MiniMenu.getOption(((MiniMenuOption) MiniMenu.options.head.next)));
        }
        if (MiniMenu.optionCount > 2) {
            s1.append("<col=ffffff> / ");
            s1.append(MiniMenu.optionCount - 2);
            s1.append(" more options");
        }

        bold.drawString(s1.toString(), 4, 15, 0xffffff, 0);
    }

	private void drawMinimap(boolean isFixed) {
		int xPosOffset = isFixed ? 0 : frameWidth - 246;
		int i = minimapInt1 & 0x3fff;
		if (minimapLock == 2) {
			DrawingArea.fillPixels((isFixed ? 40 : frameWidth - 158), (isFixed ? 9 : 5), 0, minimapShape1, minimapShape2);
		} else {
			int z = frameWidth - 182;
			int j = 48 + myPlayer.x / 32;
			int l2 = 464 - myPlayer.y / 32;
			minimapImage.method352(152, i << 2, minimapShape2, 256, minimapShape1, l2, isFixed ? 9 : 5, isFixed ? 39 : z + 25, isFixed ? 136 : 146, j);
			for (int j5 = 0; j5 < numOfMapMarkers; j5++) {
				int k = (markPosX[j5] * 4 + 2) - myPlayer.x / 32;
				int i3 = (markPosY[j5] * 4 + 2) - myPlayer.y / 32;
				markMinimap(mapFunctions[markGraphic[j5]], k, i3);
			}

			for (int k5 = 0; k5 < 104; k5++) {
				for (int l5 = 0; l5 < 104; l5++) {
					NodeList class19 = groundArray[plane][k5][l5];
					if (class19 != null) {
						int l = (k5 * 4 + 2) - myPlayer.x / 32;
						int j3 = (l5 * 4 + 2) - myPlayer.y / 32;
						markMinimap(mapDotItem, l, j3);
					}
				}
			}
			for (int i6 = 0; i6 < npcCount; i6++) {
				NPC npc = npcArray[npcIndices[i6]];
				if (npc != null && npc.isVisible()) {
					NpcDefinition npcDef = npc.desc;
					if (npcDef.childrenIDs != null)
						npcDef = npcDef.morph();
					if (npcDef != null && npcDef.drawMinimapDot && npcDef.clickable) {
						int i1 = npc.x / 32 - myPlayer.x / 32;
						int k3 = npc.y / 32 - myPlayer.y / 32;
						markMinimap(mapDotNPC, i1, k3);
					}
				}
			}
			for (int j6 = 0; j6 < playerCount; j6++) {
				Player player = playerArray[playerIndices[j6]];
				if (player != null && player.isVisible()) {
					int j1 = player.x / 32 - myPlayer.x / 32;
					int l3 = player.y / 32 - myPlayer.y / 32;
					boolean flag1 = false;
					boolean isInClan = false;
					if (clanNames.contains(player.name)) {
						isInClan = true;
					}
					long l6 = TextClass.longForName(player.name);
					for (int k6 = 0; k6 < friendsCount; k6++) {
						if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
							continue;
						flag1 = true;
						break;
					}
					boolean flag2 = false;
					if (myPlayer.team != 0 && player.team != 0 && myPlayer.team == player.team)
						flag2 = true;
					if (flag1)
						markMinimap(mapDotFriend, j1, l3);
					else if (isInClan)
						markMinimap(mapDotClan, j1, l3);
					else if (flag2)
						markMinimap(mapDotTeam, j1, l3);
					else if (player.elite)
						markMinimap(mapDotElite, j1, l3);
					else
						markMinimap(mapDotPlayer, j1, l3);
				}
			}
			if (headiconDrawType != 0 && loopCycle % 20 < 10) {
				if (headiconDrawType == 1 && headiconNpcID >= 0 && headiconNpcID < npcArray.length) {
					NPC npc = npcArray[headiconNpcID];
					if (npc != null) {
						int k1 = npc.x / 32 - myPlayer.x / 32;
						int i4 = npc.y / 32 - myPlayer.y / 32;
						drawHintIcon(mapMarker, k1, i4);
					}
				}
				if (headiconDrawType == 2) {
					int l1 = ((headiconX - baseX) * 4 + 2) - myPlayer.x / 32;
					int j4 = ((headiconY - baseY) * 4 + 2) - myPlayer.y / 32;
					drawHintIcon(mapMarker, l1, j4);
				}
				if (headiconDrawType == 10 && otherPlayerID >= 0 && otherPlayerID < playerArray.length) {
					Player player = playerArray[otherPlayerID];
					if (player != null) {
						int i2 = player.x / 32 - myPlayer.x / 32;
						int k4 = player.y / 32 - myPlayer.y / 32;
						drawHintIcon(mapMarker, i2, k4);
					}
				}
			}
			if (destX != 0) {
				int j2 = (destX * 4 + 2) - myPlayer.x / 32;
				int l4 = (destY * 4 + 2) - myPlayer.y / 32;
				markMinimap(mapFlag, j2, l4);
			}

			DrawingArea.fillRect((isFixed ? 107 + xPosOffset : frameWidth - 88), (isFixed ? 83 : 80), 3, 3, 0xffffff);
		}
		if (isFixed) {
			// Minimap border
			cacheSprite[6].drawSprite(xPosOffset, 0);

			// Logout button
			int spriteId = 30;
			if (tabHover != -1 && tabHover == LOGOUT_TAB) {
				spriteId = 34;
			} else if (tabInterfaceIDs[tabID] != -1 && tabID == LOGOUT_TAB) {
				spriteId = 35;
			}
			cacheSprite[spriteId].drawSprite(228, 0);
		} else {
			// Minimap border
			cacheSprite[36].drawSprite(frameWidth - 164, 0);
			// Compass border
			cacheSprite[37].drawSprite(frameWidth - 169, 0);

			// Logout button
			int spriteId = 30;
			if (tabHover != -1 && tabHover == LOGOUT_TAB) {
				spriteId = 34;
			} else if (tabInterfaceIDs[tabID] != -1 && tabID == LOGOUT_TAB) {
				spriteId = 35;
			}
			cacheSprite[spriteId].drawSprite(frameWidth - 21, 0);

			// World map border
			cacheSprite[37].drawSprite(frameWidth - 45, 119);
		}
		compass.method352(33, i << 2, anIntArray1057, 256, anIntArray968, 25, (isFixed ? 8 : 5), (isFixed ? 12 : frameWidth - 164), 33, 25);
		loadOrbs(isFixed);
	}

    private void loadOrbs(boolean isFixed) {
        drawHP(isFixed);
        drawPrayer(isFixed);
        drawRunOrb(isFixed);
        drawXPCounter(isFixed);
        drawWorldMapButton(isFixed);
        drawSummonOrb(isFixed);
        drawCoinOrb(isFixed, true);
    }

    private String removeMarkup(String s) {
		boolean stopAdding = false;
		final StringBuilder stringbuffer = new StringBuilder(s.length());
		for (int charIndex = 0; charIndex < s.length(); charIndex++) {
			final char c = s.charAt(charIndex);
			if (c == '<') {
				stopAdding = true;
			} else if (c == '>') {
				stopAdding = false;
			} else if (!stopAdding) {
				stringbuffer.append(c);
			}
		}
		return stringbuffer.toString();
    }

    private void drawCoinAmount(boolean isFixed) {
        long cash = Long.parseLong(RSInterface.interfaceCache[251].message);
        if (isFixed) {
            if (super.mouseX >= 512 && super.mouseX <= 546 && super.mouseY >= 87 && super.mouseY <= 118) {
                coinPart = moneyPouchSprites[3]; // no hover
            } else {
                coinPart = moneyPouchSprites[2]; // no hover
            }
            coinPart.drawSprite(445, 86);
            small.drawCenteredString(RSInterface.interfaceCache[252].message, 485, 102, getMoneyOrbColor(cash), 0);
        } else {
            if (super.mouseX >= frameWidth - 68 && super.mouseX <= frameWidth - 40 && super.mouseY >= 160 && super.mouseY <= 187) {
                coinPart = moneyPouchSprites[3]; // no hover
            } else {
                coinPart = moneyPouchSprites[2]; // no hover
            }
            coinPart.drawSprite(frameWidth - 137, 165);
            small.drawCenteredString(RSInterface.interfaceCache[252].message, frameWidth - 98, 181, getMoneyOrbColor(cash), 0);
        }
    }

    private void drawCoinLastAmount(boolean isFixed) {
        if (System.currentTimeMillis() - lastCoinUpdate > 5000) {
            return;
        }

        String message = RSInterface.interfaceCache[253].message;

        if (isFixed) {
            if (coinToggle) {
                small.drawCenteredString(message, 485, 123, 65280, 0);
            } else {
                small.drawRightAlignedString(message, 505, 104, 65280);
            }
        } else {
            small.drawRightAlignedString(message, frameWidth - 40, 205, 65280);
        }
    }

    public int getMoneyOrbColor(long cashAmount) {
        if (cashAmount < 10_000) {
            return 0xffff00; // yellow
        } else if (cashAmount >= 10_000 && cashAmount < 10_000_000) {
            return 0xffffff;
        }

        return 0x00ff00;
    }

    public void drawCoinOrb(boolean isFixed, boolean sidebar) {//512 nontoggle
        if (isFixed) {
            if (super.mouseX >= 512 && super.mouseX <= 546 && super.mouseY >= 87 && super.mouseY <= 118) {
                coinOrb = moneyPouchSprites[1]; // hover
            } else {
                coinOrb = moneyPouchSprites[0]; // no hover
            }

            if (sidebar)
                coinOrb.drawSprite(0, 84);

            if (System.currentTimeMillis() - lastCoinUpdate < 5000) {
                String message = RSInterface.interfaceCache[253].message;
                int alpha = 125 + (int) (125 * Math.sin(loopCycle / 7.0));
                if (message.contains("+")) {
                    if (sidebar) {
                        moneyPouchSprites[4].drawAdvancedSprite(-5, 79, alpha);
                    } else {
                        moneyPouchSprites[4].drawAdvancedSprite(507, 76, alpha);
                    }
                } else {
                    if (sidebar) {
                        moneyPouchSprites[5].drawAdvancedSprite(-5, 79, alpha);
                    } else {
                        moneyPouchSprites[5].drawAdvancedSprite(507, 76, alpha);
                    }
                }
            }
        } else {
            if (super.mouseX >= frameWidth - 68 && super.mouseX <= frameWidth - 40 && super.mouseY >= 160 && super.mouseY <= 187) {
                coinOrb = moneyPouchSprites[1]; // hover
            } else {
                coinOrb = moneyPouchSprites[0]; // no hover
            }

            if (sidebar) {
                coinOrb.drawSprite(frameWidth - 70, 157);
            }

            if (System.currentTimeMillis() - lastCoinUpdate < 5000) {
                String message = RSInterface.interfaceCache[253].message;
                int alpha = 125 + (int) (125 * Math.sin(loopCycle / 7.0));
                if (message.contains("+")) {
                    if (sidebar) {
                        moneyPouchSprites[4].drawAdvancedSprite(frameWidth - 76, 152, alpha);
                    }
                } else {
                    if (sidebar) {
                        moneyPouchSprites[5].drawAdvancedSprite(frameWidth - 76, 152, alpha);
                    }
                }
            }
        }
    }

    private int convertColor(int r, int g, int b) {
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }
        if (g > 255) {
            g = 255;
        } else if (g < 0) {
            g = 0;
        }
        if (b > 255) {
            b = 255;
        } else if (b < 0) {
            b = 0;
        }
        return r << 16 | g << 8 | b;
    }

    private int getOrbColor(int stat, int statBase) {
        int color = 0;
        int baseMid = statBase / 2;
        if (baseMid <= 0) {
            if (stat >= statBase) {
                color = 65280;
            } else {
                color = 16711680;
            }
        } else {
            if (stat > baseMid) {
                color = convertColor(255 - (255 * (stat - baseMid) / baseMid), 255, 0);
            } else {
                color = convertColor(255, stat * 255 / baseMid, 0);
            }
        }
        return color;
    }

    private void drawHP(boolean isFixed) {
        int currentHP = stats[3];
        int maxHP = statsBase[3];
        if (currentHP < 0) {
            currentHP = 0;
        }
        if (maxHP <= 0) {
            maxHP = 1;
        }
        int health = currentHP * 100 / maxHP;
        int x = getOrbX(0, isFixed);
        int y = getOrbY(0, isFixed);
        cacheSprite[isFixed ? 75 : 77].drawSprite(x, y);
        small.drawCenteredString(Integer.toString(currentHP), x + (isFixed ? 42 : 15), y + 26, getOrbColor(currentHP, maxHP), 0);
        cacheSprite[82].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[79].myHeight = cacheSprite[79].myWidth * (maxHP - currentHP) / maxHP;
        cacheSprite[79].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        if (health <= 25 && health != 0) {
            cacheSprite[87].drawAdvancedSprite(x + (isFixed ? 9 : 33), y + 11, 125 + (int) (125 * Math.sin(loopCycle / 7.0)));
        } else {
            cacheSprite[87].drawSprite(x + (isFixed ? 9 : 33), y + 11);
        }
    }

    private void drawPrayer(boolean isFixed) {
        int currentPray = stats[5];
        int maxPray = statsBase[5];
        if (currentPray < 0) {
            currentPray = 0;
        }
        if (maxPray <= 0) {
            maxPray = 1;
        }
        int prayer = currentPray * 100 / maxPray;
        int x = getOrbX(1, isFixed);
        int y = getOrbY(1, isFixed);
        cacheSprite[isFixed ? (hoverPos == 1 ? 76 : 75) : (hoverPos == 1 ? 78 : 77)].drawSprite(x, y);
        small.drawCenteredString(Integer.toString(currentPray), x + (isFixed ? 42 : 15), y + 26, getOrbColor(currentPray, maxPray), 0);
        if (QuickPrayers.quickPrayersActive)
            cacheSprite[84].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        else
            cacheSprite[81].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[79].myHeight = cacheSprite[79].myWidth * (maxPray - currentPray) / maxPray;
        cacheSprite[79].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        if (prayer <= 25 && prayer != 0) {
            cacheSprite[88].drawAdvancedSprite(x + (isFixed ? 7 : 31), y + 7, 125 + (int) (125 * Math.sin(loopCycle / 7.0)));
        } else {
            cacheSprite[88].drawSprite(x + (isFixed ? 7 : 31), y + 7);
        }
    }

    public void drawRunOrb(boolean isFixed) {
        int currentRun = energy;
        int maxRun = 100;
        int x = getOrbX(2, isFixed);
        int y = getOrbY(2, isFixed);
        cacheSprite[isFixed ? (hoverPos == 2 ? 76 : 75) : (hoverPos == 2 ? 78 : 77)].drawSprite(x, y);
        small.drawCenteredString(Integer.toString(currentRun), x + (isFixed ? 42 : 15), y + 26, getOrbColor(currentRun, maxRun), 0);
        cacheSprite[playerVariables[173] == 1 ? 80 : 83].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[79].myHeight = cacheSprite[79].myWidth * (maxRun - currentRun) / maxRun;
        cacheSprite[79].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[playerVariables[173] == 0 ? 89 : 90].drawSprite(x + (isFixed ? 10 : 34), y + 7);
    }

    public void drawSummonOrb(boolean isFixed) {
        int currentSum = stats[23];
        int maxSum = statsBase[23];
        if (currentSum < 0) {
            currentSum = 0;
        }
        if (maxSum <= 0) {
            maxSum = 1;
        }
        int x = getOrbX(3, isFixed);
        int y = getOrbY(3, isFixed);
        cacheSprite[isFixed ? (hoverPos == 3 ? 76 : 75) : (hoverPos == 3 ? 78 : 77)].drawSprite(x, y);
        small.drawCenteredString(Integer.toString(currentSum), x + (isFixed ? 42 : 15), y + 26, getOrbColor(currentSum, maxSum), 0);
        (RSInterface.interfaceCache[14321].mediaID == 4000 ? cacheSprite[85] : cacheSprite[86]).drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[79].myHeight = cacheSprite[79].myWidth * (maxSum - currentSum) / maxSum;
        cacheSprite[79].drawSprite(x + (isFixed ? 3 : 27), y + 3);
        cacheSprite[91].drawSprite(x + (isFixed ? 9 : 33), y + 9);
    }

    public void drawWorldMapButton(boolean isFixed) {
        int x = isFixed ? 12 : frameWidth - 40;
        if (worldMapHPos == 0) {
            cacheSprite[73].drawSprite(x, 124);
        } else if (worldMapHPos == 1) {
            cacheSprite[74].drawSprite(x, 124);
        }
    }

    private void npcScreenPos(Mobile entity, int i) {
        calcEntityScreenPos(entity.x, i, entity.y);
    }

    private void calcEntityScreenPos(int i, int j, int l) {
        if (i < 128 || l < 128 || i > 13056 || l > 13056) {
            spriteDrawX = -1;
            spriteDrawY = -1;
            return;
        }

        int i1 = getFloorDrawHeight(plane, l, i) - j;
        i -= xCameraPos;
        i1 -= zCameraPos;
        l -= yCameraPos;
        int j1 = Rasterizer.SINE[yCameraCurve];
        int k1 = Rasterizer.COSINE[yCameraCurve];
        int l1 = Rasterizer.SINE[xCameraCurve];
        int i2 = Rasterizer.COSINE[xCameraCurve];
        int j2 = l * l1 + i * i2 >> 15;
        l = l * i2 - i * l1 >> 15;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 15;
        l = i1 * j1 + l * k1 >> 15;
        i1 = j2;
        if (l >= 50) {
            spriteDrawX = Rasterizer.centerX + (i * gameAreaWidth) / l;
            spriteDrawY = Rasterizer.centerY + (i1 * gameAreaWidth) / l;
        } else {
            spriteDrawX = -1;
            spriteDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu(boolean isFixed) {
        if (splitPrivateChat == 0) {
            return;
        }
        int messageCount = 0;
        if (systemUpdateTime != 0)
            messageCount = 1;
		for (int index = ChatMessageManager.getHighestId(); index != -1; index = ChatMessageManager.getPrevMessageId(index)) {
			ChatMessage message = (ChatMessage) ChatMessageManager.messageHashtable.findNodeByID(index);
			int type = message.type;
			String name = message.name;
			if ((type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER || type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF) && (type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
				int offset = isFixed ? 4 : 0;
				int yPos;
				if (isFixed) {
					yPos = 329 - messageCount * 13;
				} else {
					yPos = frameHeight - 170 - messageCount * 13;
				}

				if (super.mouseX > offset && super.mouseY - offset > yPos - 10 && super.mouseY - offset <= yPos + 3) {
					int textWidth = regular.getTextWidth("From:  " + name + message.message) + 25;
					if (textWidth > 450)
						textWidth = 450;
					if (super.mouseX < textWidth + offset) {
						buildMessageOrAddFriend(name, true);
					}
				}
				if (++messageCount >= 5) {
					return;
				}
			}
			if ((type == ChatMessage.PRIVATE_MESSAGE_RECEIVED || type == ChatMessage.PRIVATE_MESSAGE_SENT) && privateChatMode < 2 && ++messageCount >= 5) {
				return;
			}
        }
    }

    private void method130(int j, int k, int l, int i1, int j1, int k1, int l1,
                           int i2, int j2) {
        GameObjectSpawnRequest class30_sub1 = null;
        for (GameObjectSpawnRequest class30_sub1_1 = (GameObjectSpawnRequest) aClass19_1179
                .reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (GameObjectSpawnRequest) aClass19_1179
                .reverseGetNext()) {
            if (class30_sub1_1.plane != l1 || class30_sub1_1.x != i2
                    || class30_sub1_1.y != j1 || class30_sub1_1.anInt1296 != i1)
                continue;
            class30_sub1 = class30_sub1_1;
            break;
        }

        if (class30_sub1 == null) {
            class30_sub1 = new GameObjectSpawnRequest();
            class30_sub1.plane = l1;
            class30_sub1.anInt1296 = i1;
            class30_sub1.x = i2;
            class30_sub1.y = j1;
            method89(class30_sub1);
            aClass19_1179.insertHead(class30_sub1);
        }
        class30_sub1.anInt1291 = k;
        class30_sub1.anInt1293 = k1;
        class30_sub1.anInt1292 = l;
        class30_sub1.anInt1302 = j2;
        class30_sub1.anInt1294 = j;
    }

    private boolean interfaceIsSelected(RSInterface class9) {
        if (class9.valueCompareType == null)
            return false;
        for (int i = 0; i < class9.valueCompareType.length; i++) {
            int j = extractInterfaceValues(class9, i);
            int k = class9.requiredValues[i];

            if (class9.valueCompareType[i] == 2) {
                if (j >= k)
                    return false;
            } else if (class9.valueCompareType[i] == 3) {
                if (j <= k)
                    return false;
            } else if (class9.valueCompareType[i] == 4) {
                if (j == k)
                    return false;
            } else if (j != k)
                return false;
        }

        return true;
    }

    private DataInputStream openJagGrabInputStream(String s) throws IOException {
        if (aSocket832 != null) {
            try {
                aSocket832.close();
            } catch (Exception _ex) {
            }
            aSocket832 = null;
        }
        aSocket832 = openSocket(43595);
        aSocket832.setSoTimeout(10000);
        InputStream inputstream = aSocket832.getInputStream();
        OutputStream outputstream = aSocket832.getOutputStream();
        outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
        return new DataInputStream(inputstream);
    }

    private void method134(Stream stream) {
        int j = stream.readBits(8);
        if (j < playerCount) {
            for (int k = j; k < playerCount; k++)
                anIntArray840[anInt839++] = playerIndices[k];

        }
        if (j > playerCount) {
            signlink.reporterror(myUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        playerCount = 0;
        for (int l = 0; l < j; l++) {
            int i1 = playerIndices[l];
            Player player = playerArray[i1];
            int j1 = stream.readBits(1);
            if (j1 == 0) {
                playerIndices[playerCount++] = i1;
                player.time = loopCycle;
            } else {
                int k1 = stream.readBits(2);
                if (k1 == 0) {
                    playerIndices[playerCount++] = i1;
                    player.time = loopCycle;
                    sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = i1;
                } else if (k1 == 1) {
                    playerIndices[playerCount++] = i1;
                    player.time = loopCycle;
                    int l1 = stream.readBits(3);
                    player.moveInDir(false, l1);
                    int j2 = stream.readBits(1);
                    if (j2 == 1)
                        sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = i1;
                } else if (k1 == 2) {
                    playerIndices[playerCount++] = i1;
                    player.time = loopCycle;
                    int i2 = stream.readBits(3);
                    player.moveInDir(true, i2);
                    int k2 = stream.readBits(3);
                    player.moveInDir(true, k2);
                    int l2 = stream.readBits(1);
                    if (l2 == 1)
                        sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = i1;
                } else if (k1 == 3)
                    anIntArray840[anInt839++] = i1;
            }
        }
    }

    public int getframeWidth() {
        return frameWidth;
    }

    public int getframeHeight() {
        return frameHeight;
    }

    private void parsePacketGroup(Stream stream, int j) {
        if (j == 84) {
            int k = stream.readUnsignedByte();
            int j3 = bigRegionX + (k >> 4 & 7);
            int i6 = bigRegionY + (k & 7);
            int l8 = stream.readUnsignedWord();
            int k11 = stream.readUnsignedWord();
            int l13 = stream.readUnsignedWord();
            if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
                NodeList class19_1 = groundArray[plane][j3][i6];
                if (class19_1 != null) {
                    for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1
                            .reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1
                            .reverseGetNext()) {
                        if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff)
                                || class30_sub2_sub4_sub2_3.itemCount != k11)
                            continue;
                        class30_sub2_sub4_sub2_3.itemCount = l13;
                        break;
                    }

                    spawnGroundItem(j3, i6);
                }
            }
            return;
        }
        if (j == 105) {
            int l = stream.readUnsignedByte();
            int k3 = bigRegionX + (l >> 4 & 7);
            int j6 = bigRegionY + (l & 7);
            int i9 = stream.readUnsignedWord();
            int l11 = stream.readUnsignedByte();
            int i14 = l11 >> 4 & 0xf;
            int i16 = l11 & 7;
            if (myPlayer.smallX[0] >= k3 - i14
                    && myPlayer.smallX[0] <= k3 + i14
                    && myPlayer.smallY[0] >= j6 - i14
                    && myPlayer.smallY[0] <= j6 + i14 && waveON && !lowMem
                    && anInt1062 < 50) {
                songIDS[anInt1062] = i9;
                songVolumes[anInt1062] = i16;
                anIntArray1250[anInt1062] = Sounds.anIntArray326[i9];
                anInt1062++;
            }
        }
        if (j == 215) {
            int i1 = stream.readUnsignedWordA();
            int l3 = stream.getUnsignedByteS();
            int k6 = bigRegionX + (l3 >> 4 & 7);
            int j9 = bigRegionY + (l3 & 7);
            int i12 = stream.readUnsignedWordA();
            int j14 = stream.readUnsignedWord();
            if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != playerID) {
                Item class30_sub2_sub4_sub2_2 = new Item();
                class30_sub2_sub4_sub2_2.ID = i1;
                class30_sub2_sub4_sub2_2.itemCount = j14;
                if (groundArray[plane][k6][j9] == null)
                    groundArray[plane][k6][j9] = new NodeList();
                groundArray[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
                spawnGroundItem(k6, j9);
            }
            return;
        }
        if (j == 156) {
            int j1 = stream.getUnsignedByteA();
            int i4 = bigRegionX + (j1 >> 4 & 7);
            int l6 = bigRegionY + (j1 & 7);
            int k9 = stream.readUnsignedWord();
            if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
                NodeList class19 = groundArray[plane][i4][l6];
                if (class19 != null) {
                    for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19
                            .reverseGetNext()) {
                        if (item.ID != (k9 & 0x7fff))
                            continue;
                        item.unlink();
                        break;
                    }

                    if (class19.reverseGetFirst() == null)
                        groundArray[plane][i4][l6] = null;
                    spawnGroundItem(i4, l6);
                }
            }
            return;
        }
        if (j == 160) {
            int k1 = stream.getUnsignedByteS();
            int j4 = bigRegionX + (k1 >> 4 & 7);
            int i7 = bigRegionY + (k1 & 7);
            int l9 = stream.getUnsignedByteS();
            int type = l9 >> 2;
            int rotation = l9 & 3;
            int j16 = anIntArray1177[type];
            int animationId = stream.readUnsignedWordA();

            if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
                int j18 = intGroundArray[plane][j4][i7];
                int i19 = intGroundArray[plane][j4 + 1][i7];
                int l19 = intGroundArray[plane][j4 + 1][i7 + 1];
                int k20 = intGroundArray[plane][j4][i7 + 1];
                if (j16 == 0) {
                    WallObject class10 = worldController.method296(plane, j4, i7);
                    if (class10 != null) {
                        int k21 = (int) (class10.uid >>> 32) & 0x7fffffff;
                        if (type == 2) {
                            class10.node1 = new ObjectInstance(k21, 4 + rotation, 2, i19, l19, j18, k20, animationId, false);
                            class10.node2 = new ObjectInstance(k21, rotation + 1 & 3, 2, i19, l19, j18, k20, animationId, false);
                        } else {
                            class10.node1 = new ObjectInstance(k21, rotation, type, i19, l19, j18, k20, animationId, false);
                        }
                    }
                }
                if (j16 == 1) {
                    WallDecoration class26 = worldController.method297(j4, i7, plane);
                    if (class26 != null)
                        class26.myEntity = new ObjectInstance((int) (class26.uid >>> 32) & 0x7fffffff, 0, 4, i19, l19, j18, k20, animationId, false);
                }
                if (j16 == 2) {
                    InteractiveObject class28 = worldController.method298(j4, i7, plane);
                    if (type == 11)
                        type = 10;
                    if (class28 != null)
                        class28.jagexNode = new ObjectInstance((int) (class28.uid >>> 32) & 0x7fffffff, rotation, type, i19, l19, j18, k20, animationId, false);
                }
                if (j16 == 3) {
                    GroundDecoration class49 = worldController.method299(i7, j4, plane);
                    if (class49 != null)
                        class49.aClass30_Sub2_Sub4_814 = new ObjectInstance((int) (class49.uid >>> 32) & 0x7fffffff, rotation, 22, i19, l19,
                                j18, k20, animationId, false);
                }
            }
            return;
        }
        if (j == 147) {
            int l1 = stream.getUnsignedByteS();
            int k4 = bigRegionX + (l1 >> 4 & 7);
            int j7 = bigRegionY + (l1 & 7);
            int i10 = stream.readUnsignedWord();
            byte byte0 = stream.method430();
            int l14 = stream.method434();
            byte byte1 = stream.method429();
            int k17 = stream.readUnsignedWord();
            int k18 = stream.getUnsignedByteS();
            int j19 = k18 >> 2;
            int i20 = k18 & 3;
            int l20 = anIntArray1177[j19];
            byte byte2 = stream.readSignedByte();
            int l21 = stream.readUnsignedWord();
            byte byte3 = stream.method429();
            Player player;
            if (i10 == playerID)
                player = myPlayer;
            else
                player = playerArray[i10];
            if (player != null) {
                ObjectDefinition class46 = ObjectDefinition.forID(l21);
                int i22 = intGroundArray[plane][k4][j7];
                int j22 = intGroundArray[plane][k4 + 1][j7];
                int k22 = intGroundArray[plane][k4 + 1][j7 + 1];
                int l22 = intGroundArray[plane][k4][j7 + 1];
                Model model = class46.generateModel(j19, i20, i22, j22, k22, l22);
                if (model != null) {
                    method130(k17 + 1, -1, 0, l20, j7, 0, plane, k4, l14 + 1);
                    player.anInt1707 = l14 + loopCycle;
                    player.anInt1708 = k17 + loopCycle;
                    player.aModel_1714 = model;
                    int i23 = class46.width;
                    int j23 = class46.height;
                    if (i20 == 1 || i20 == 3) {
                        i23 = class46.height;
                        j23 = class46.width;
                    }
                    player.anInt1711 = k4 * 128 + i23 * 64;
                    player.anInt1713 = j7 * 128 + j23 * 64;
                    player.anInt1712 = getFloorDrawHeight(plane,
                            player.anInt1713, player.anInt1711);
                    if (byte2 > byte0) {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if (byte3 > byte1) {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = k4 + byte2;
                    player.anInt1721 = k4 + byte0;
                    player.anInt1720 = j7 + byte3;
                    player.anInt1722 = j7 + byte1;
                }
            }
        }
        if (j == 151) {
            int i2 = stream.getUnsignedByteA();
            int l4 = bigRegionX + (i2 >> 4 & 7);
            int k7 = bigRegionY + (i2 & 7);
            int j10 = stream.method434();
            int k12 = stream.getUnsignedByteS();
            int i15 = k12 >> 2;
            int k16 = k12 & 3;
            int l17 = anIntArray1177[i15];
            if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
                method130(-1, j10, k16, l17, k7, i15, plane, l4, 0);
            return;
        }
        if (j == 4) {
            int j2 = stream.readUnsignedByte();
            int x = bigRegionX + (j2 >> 4 & 7);
            int y = bigRegionY + (j2 & 7);
            int k10 = stream.readUnsignedWord();
            int l12 = stream.readUnsignedByte();
            int j15 = stream.readUnsignedWord();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                x = x * 128 + 64;
                y = y * 128 + 64;
                StillGraphic stillGraphic = new StillGraphic(plane, loopCycle,
                        j15, k10, getFloorDrawHeight(plane, y, x) - l12, y, x);
                stillGraphicsNode.insertHead(stillGraphic);
            }
            return;
        }
        if (j == 44) {
            int k2 = stream.method436();
            int j5 = stream.readUnsignedWord();
            int i8 = stream.readUnsignedByte();
            int l10 = bigRegionX + (i8 >> 4 & 7);
            int i13 = bigRegionY + (i8 & 7);
            if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
                Item class30_sub2_sub4_sub2_1 = new Item();
                class30_sub2_sub4_sub2_1.ID = k2;
                class30_sub2_sub4_sub2_1.itemCount = j5;
                if (groundArray[plane][l10][i13] == null)
                    groundArray[plane][l10][i13] = new NodeList();
                groundArray[plane][l10][i13]
                        .insertHead(class30_sub2_sub4_sub2_1);
                spawnGroundItem(l10, i13);
            }
            return;
        }
        if (j == 101) {
            int l2 = stream.method427();
            int k5 = l2 >> 2;
            int j8 = l2 & 3;
            int i11 = anIntArray1177[k5];
            int j13 = stream.readUnsignedByte();
            int k15 = bigRegionX + (j13 >> 4 & 7);
            int l16 = bigRegionY + (j13 & 7);
            if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
                method130(-1, -1, j8, i11, l16, k5, plane, k15, 0);
            return;
        }
        if (j == 117) {
            int i3 = stream.readUnsignedByte();
            int l5 = bigRegionX + (i3 >> 4 & 7);
            int k8 = bigRegionY + (i3 & 7);
            int j11 = l5 + stream.readSignedByte();
            int k13 = k8 + stream.readSignedByte();
            int l15 = stream.readSignedWord();
            int i17 = stream.readUnsignedWord();
            int i18 = stream.readUnsignedByte() * 4;
            int l18 = stream.readUnsignedByte() * 4;
            int k19 = stream.readUnsignedWord();
            int j20 = stream.readUnsignedWord();
            int i21 = stream.readUnsignedByte();
            int j21 = stream.readUnsignedByte();
            if (l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0
                    && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535) {
                l5 = l5 * 128 + 64;
                k8 = k8 * 128 + 64;
                j11 = j11 * 128 + 64;
                k13 = k13 * 128 + 64;
                Projectile class30_sub2_sub4_sub4 = new Projectile(i21, l18,
                        k19 + loopCycle, j20 + loopCycle, j21, plane,
                        getFloorDrawHeight(plane, k8, l5) - i18, k8, l5, l15,
                        i17);
                class30_sub2_sub4_sub4.method455(k19 + loopCycle, k13,
                        getFloorDrawHeight(plane, k13, j11) - l18, j11);
                projectileNode.insertHead(class30_sub2_sub4_sub4);
            }
        }
    }

    private void updateNPCs(Stream stream) {
        stream.initBitAccess();
        int k = stream.readBits(8);
        if (k < npcCount) {
            for (int l = k; l < npcCount; l++)
                anIntArray840[anInt839++] = npcIndices[l];
        }
        if (k > npcCount) {
            signlink.reporterror(myUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        npcCount = 0;
        for (int i1 = 0; i1 < k; i1++) {
            int j1 = npcIndices[i1];
            NPC npc = npcArray[j1];
            int k1 = stream.readBits(1);
            if (k1 == 0) {
                npcIndices[npcCount++] = j1;
                npc.time = loopCycle;
            } else {
                int l1 = stream.readBits(2);
                if (l1 == 0) {
                    npcIndices[npcCount++] = j1;
                    npc.time = loopCycle;
                    sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = j1;
                } else if (l1 == 1) {
                    npcIndices[npcCount++] = j1;
                    npc.time = loopCycle;
                    int i2 = stream.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = stream.readBits(1);
                    if (k2 == 1)
                        sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = j1;
                } else if (l1 == 2) {
                    npcIndices[npcCount++] = j1;
                    npc.time = loopCycle;
                    int j2 = stream.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = stream.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = stream.readBits(1);
                    if (i3 == 1)
                        sessionNpcsAwaitingUpdate[sessionNpcsAwaitingUpdatePtr++] = j1;
                } else if (l1 == 3)
                    anIntArray840[anInt839++] = j1;
            }
        }
    }

    private void markMinimap(Sprite sprite, int x, int y) {
    	if (sprite == null) {
    		return;
    	}

        int l = x * x + y * y;
        if (l > 6400) {
            return;
        }
        boolean isFixed = clientSize == 0;
        int offX = isFixed ? 4 : frameWidth - 160;
        int k = minimapInt1 & 0x3fff;
        int i1 = Rasterizer.SINE[k];
        int j1 = Rasterizer.COSINE[k];
        int k1 = y * i1 + x * j1 >> 15;
        int l1 = y * j1 - x * i1 >> 15;
        if (isFixed) {
            sprite.drawSprite(((105 + k1) - sprite.maxWidth / 2) + offX, 88 - l1 - sprite.maxHeight / 2 - 4);
        } else {
            sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + offX, 85 - l1 - sprite.maxHeight / 2 - 4);
        }
    }

    private void method142(int i, int j, int k, int l, int i1, int j1, int k1) {
        if (i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
            if (lowMem && j != plane)
                return;
            long i2 = 0L;
            if (j1 == 0)
                i2 = worldController.method300(j, i1, i);
            if (j1 == 1)
                i2 = worldController.method301(j, i1, i);
            if (j1 == 2)
                i2 = worldController.method302(j, i1, i);
            if (j1 == 3)
                i2 = worldController.method303(j, i1, i);
            if (i2 != 0L) {
                int j2 = (int) ((i2 >>> 32) & 0x7fffffff);
                int k2 = (int) (i2 >> 14) & 0x1f;
                int l2 = (int) (i2 >> 20) & 0x3;
                if (j1 == 0) {
                    worldController.method291(i1, j, i, (byte) -119);
                    ObjectDefinition class46 = ObjectDefinition.forID(j2);
                    if (class46.isUnwalkable)
                        tileSettings[j].method215(l2, k2, class46.impenetrable,
                                i1, i);
                }
                if (j1 == 1)
                    worldController.method292(i, j, i1);
                if (j1 == 2) {
                    worldController.method293(j, i1, i);
                    ObjectDefinition class46_1 = ObjectDefinition.forID(j2);
                    if (i1 + class46_1.width > 103 || i + class46_1.width > 103
                            || i1 + class46_1.height > 103
                            || i + class46_1.height > 103)
                        return;
                    if (class46_1.isUnwalkable)
                        tileSettings[j].method216(l2, class46_1.width, i1, i,
                                class46_1.height, class46_1.impenetrable);
                }
                if (j1 == 3) {
                    worldController.method294(j, i, i1);
                    ObjectDefinition class46_2 = ObjectDefinition.forID(j2);
                    if (class46_2.isUnwalkable && class46_2.hasActions == 1)
                        tileSettings[j].method218(i, i1);
                }
            }
            if (k1 >= 0) {
                int j3 = j;
                if (j3 < 3 && (tileSettingBits[1][i1][i] & 2) == 2)
                    j3++;
                ObjectManager.method188(worldController, k, i, l, j3,
                        tileSettings[j], intGroundArray, i1, k1, j);
            }
        }
    }

    private void updatePlayers(int i, Stream stream) {
        anInt839 = 0;
        sessionNpcsAwaitingUpdatePtr = 0;
        method117(stream);
        method134(stream);
        method91(stream, i);
        method49(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (playerArray[l].time != loopCycle)
                playerArray[l] = null;
        }

        if (stream.currentOffset != i) {
            signlink.reporterror("Error packet size mismatch in getplayer pos:"
                    + stream.currentOffset + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < playerCount; i1++)
            if (playerArray[playerIndices[i1]] == null) {
                signlink.reporterror(myUsername
                        + " null entry in pl list - pos:" + i1 + " size:"
                        + playerCount);
                throw new RuntimeException("eek");
            }

    }

    private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
        int heightDelta = gameAreaHeight - 334;
        if (heightDelta < 0) {
            heightDelta = 0;
        } else if (heightDelta > 200) {
            heightDelta = 200;
        }
        int zoomModifier = heightDelta * 64 / 100 + 256;
        j = j * zoomModifier >> 8;
        int l1 = 16384 - k & 0x3fff;
        int i2 = 16384 - j1 & 0x3fff;
        int j2 = 0;
        int k2 = 0;
        int l2 = j;
        if (l1 != 0) {
            int i3 = Rasterizer.SINE[l1];
            int k3 = Rasterizer.COSINE[l1];
            int i4 = k2 * k3 - l2 * i3 >> 15;
            l2 = k2 * i3 + l2 * k3 >> 15;
            k2 = i4;
        }
        if (i2 != 0) {
            int j3 = Rasterizer.SINE[i2];
            int l3 = Rasterizer.COSINE[i2];
            int j4 = l2 * j3 + j2 * l3 >> 15;
            l2 = l2 * l3 - j2 * j3 >> 15;
            j2 = j4;
        }
        xCameraPos = l - j2;
        zCameraPos = i1 - k2;
        yCameraPos = k1 - l2;
        yCameraCurve = k;
        xCameraCurve = j1;
    }

    private void updateStrings(String str, int i) {
        switch (i) {
            case 1675:
                sendFrame126(str, 17508);
                break;// Stab
            case 1676:
                sendFrame126(str, 17509);
                break;// Slash
            // case 1677: sendFrame126(str, 17510); break;//Cursh
            case 1678:
                sendFrame126(str, 17511);
                break;// Magic
            case 1679:
                sendFrame126(str, 17512);
                break;// Range
            case 1680:
                sendFrame126(str, 17513);
                break;// Stab
            case 1681:
                sendFrame126(str, 17514);
                break;// Slash
            case 1682:
                sendFrame126(str, 17515);
                break;// Crush
            case 1683:
                sendFrame126(str, 17516);
                break;// Magic
            case 1684:
                sendFrame126(str, 17517);
                break;// Range
            case 1686:
                sendFrame126(str, 17518);
                break;// Strength
            case 1687:
                sendFrame126(str, 17519);
                break;// Prayer
        }
    }

    public void sendFrame126(String str, int i) {
        RSInterface.interfaceCache[i].message = str;
        if (RSInterface.interfaceCache[i].parentID == tabInterfaceIDs[tabID])
            reDrawTabArea = true;
    }

    public void sendFrame36(int id, int state) {
        anIntArray1045[id] = state;
        if (playerVariables[id] != state) {
            playerVariables[id] = state;
            postVarpChange(id);
            reDrawTabArea = true;
            if (dialogID != -1)
                reDrawChatArea = true;
        }
    }

    public void sendFrame219() {
        if (invOverlayInterfaceID != -1) {
            invOverlayInterfaceID = -1;
            reDrawTabArea = true;
        }
        if (backDialogID != -1) {
            backDialogID = -1;
            reDrawChatArea = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            reDrawChatArea = true;
        }
        openInterfaceID = -1;
        isDialogueInterface = false;
    }

    private void sendFrame248(int interfaceID, int sideInterfaceID) {
        if (backDialogID != -1) {
            backDialogID = -1;
            reDrawChatArea = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            reDrawChatArea = true;
        }
        openInterfaceID = interfaceID;
        invOverlayInterfaceID = sideInterfaceID;
        reDrawTabArea = true;
        isDialogueInterface = false;
    }

    private boolean parsePacket() {
        if (socketStream == null)
            return false;
        try {
            int i = socketStream.available();
            if (i == 0)
                return false;
            if (pktType == -1) {
                socketStream.flushInputStream(inStream.buffer, 0, 1);
                pktType = inStream.buffer[0] & 0xff;
                // if (encryption != null)
                //     pktType = pktType - encryption.getNextKey() & 0xff;
                pktSize = SizeConstants.packetSizes[pktType];
                i--;
            }
            if (pktSize == -1)
                if (i > 0) {
                    socketStream.flushInputStream(inStream.buffer, 0, 1);
                    pktSize = inStream.buffer[0] & 0xff;
                    i--;
                } else {
                    return false;
                }
            if (pktSize == -2)
                if (i > 1) {
                    socketStream.flushInputStream(inStream.buffer, 0, 2);
                    inStream.currentOffset = 0;
                    pktSize = inStream.readUnsignedWord();
                    i -= 2;
                } else {
                    return false;
                }
            if (i < pktSize)
                return false;
            inStream.currentOffset = 0;
            socketStream.flushInputStream(inStream.buffer, 0, pktSize);
            anInt1009 = 0;
            anInt843 = anInt842;
            anInt842 = anInt841;
            anInt841 = pktType;

            switch (pktType) {
                case 81:
                    updatePlayers(pktSize, inStream);
                    aBoolean1080 = false;
                    pktType = -1;
                    return true;

                case 176:
                    welcomeScreenDaysSinceRecovChange = inStream.method427();
                    welcomeScreenUnreadMessagesCount = inStream.readUnsignedWordA();
                    membersInt = inStream.readUnsignedByte();
                    lastLoginIP = inStream.method440();
                    daysSinceLastLogin = inStream.readUnsignedWord();
                    if (lastLoginIP != 0 && openInterfaceID == -1) {
                        //signlink.dns = TextClass.decodeDNS(lastLoginIP);
                        clearTopInterfaces();
                        char c = '\u028A';
                        if (welcomeScreenDaysSinceRecovChange != 201
                                || membersInt == 1)
                            c = '\u028F';
                        reportAbuseInput = "";
                        canMute = false;
                        for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
                            if (RSInterface.interfaceCache[k9] == null
                                    || RSInterface.interfaceCache[k9].contentType != c)
                                continue;
                            openInterfaceID = RSInterface.interfaceCache[k9].parentID;

                        }
                    }
                    pktType = -1;
                    return true;

                case 64:
                    bigRegionX = inStream.method427();
                    bigRegionY = inStream.getUnsignedByteS();
                    for (int x = bigRegionX; x < bigRegionX + 8; x++) {
                        for (int y = bigRegionY; y < bigRegionY + 8; y++)
                            if (groundArray[plane][x][y] != null) {
                                groundArray[plane][x][y] = null;
                                spawnGroundItem(x, y);
                            }
                    }
                    for (GameObjectSpawnRequest newGameObjectSpawn = (GameObjectSpawnRequest) aClass19_1179
                            .reverseGetFirst(); newGameObjectSpawn != null; newGameObjectSpawn = (GameObjectSpawnRequest) aClass19_1179
                            .reverseGetNext())
                        if (newGameObjectSpawn.x >= bigRegionX
                                && newGameObjectSpawn.x < bigRegionX + 8
                                && newGameObjectSpawn.y >= bigRegionY
                                && newGameObjectSpawn.y < bigRegionY + 8
                                && newGameObjectSpawn.plane == plane)
                            newGameObjectSpawn.anInt1294 = 0;
                    pktType = -1;
                    return true;

                case 185:
                    int interfaceID = inStream.method436();
                    RSInterface.interfaceCache[interfaceID].mediaType = 3;
                    if (myPlayer.npcId == -1) {
                        RSInterface.interfaceCache[interfaceID].mediaID = (myPlayer.appearanceColors[0] << 25)
                                + (myPlayer.appearanceColors[4] << 20)
                                + (myPlayer.equipment[0] << 15)
                                + (myPlayer.equipment[8] << 10)
                                + (myPlayer.equipment[11] << 5)
                                + myPlayer.equipment[1];
                    } else {
                        RSInterface.interfaceCache[interfaceID].mediaID = (int) (0x12345678L + myPlayer.npcId);
                    }
                    pktType = -1;
                    return true;

				/* Clan message packet */
                case 217:
                    String clanUsername = inStream.readString();
                    String clanMessage = inStream.readString();
                    String clanTitle = inStream.readString();
                    int channelRights = inStream.readUnsignedWord();
                    boolean elite = inStream.readUnsignedByte() == 1;

                    if (channelRights == 3) {
                        channelRights = 2;
                    }
                    String username = getCrown(channelRights) + getEliteCrown(elite) + TextClass.fixName(clanUsername);

                    // ignore
                    boolean ignore = false;
                    long usernameAsLong = TextClass.longForName(clanUsername);
                    for (long l : ignoreListAsLongs) {
                        if (l == usernameAsLong) {
                            ignore = true;
                        }
                    }

                    if (!ignore) {
                        pushMessage(clanMessage, ChatMessage.CLAN_MESSAGE, username, clanTitle);
                    }

                    pktType = -1;
                    return true;

                case 107:
                    inCutscene = false;
                    for (int l = 0; l < 5; l++)
                        useCustomCamera[l] = false;
                    pktType = -1;
                    return true;

                case 72:
                    int i1 = inStream.method434();
                    RSInterface class9 = RSInterface.interfaceCache[i1];
                    for (int k15 = 0; k15 < class9.inv.length; k15++) {
                        class9.inv[k15] = -1;
                        class9.inv[k15] = 0;
                    }
                    pktType = -1;
                    return true;

                case 214:
                    ignoreCount = pktSize / 8;
                    for (int j1 = 0; j1 < ignoreCount; j1++)
                        ignoreListAsLongs[j1] = inStream.readQWord();
                    pktType = -1;
                    return true;

                case 166:
                    inCutscene = true;
                    anInt1098 = inStream.readUnsignedByte();
                    anInt1099 = inStream.readUnsignedByte();
                    anInt1100 = inStream.readUnsignedWord();
                    anInt1101 = inStream.readUnsignedByte();
                    anInt1102 = inStream.readUnsignedByte();
                    if (anInt1102 >= 100) {
                        xCameraPos = anInt1098 * 128 + 64;
                        yCameraPos = anInt1099 * 128 + 64;
                        zCameraPos = getFloorDrawHeight(plane, yCameraPos,
                                xCameraPos) - anInt1100;
                    }
                    pktType = -1;
                    return true;

                case 133: // xp counter packet
                    int xp = inStream.readDWord();
                    xpCounter += xp;
                    pktType = -1;
                    return true;

                case 134:
                    int stat = inStream.readUnsignedByte();
                    int statXp = inStream.method439();
                    int statLvl = inStream.readUnsignedByte();

                    int oldXp = statsXp[stat];

                    statsXp[stat] = statXp;
                    stats[stat] = statLvl;
                    statsBase[stat] = 1;

                    int xpDelta = statsXp[stat] - oldXp;
                    int index = findFreeXpIndex();
                    if (xpDelta > 0 && index != -1 && counterOn) {
                        expDrops[index] = new ExpDrop(XP_DROP_SKILL_IDS[stat], xpDelta, (clientSize == 0 ? 30 : 100) + index * 20);
                    }

                    for (int id = 0; id < 98; id++) {
                        if (statXp >= xpForLvl[id]) {
                            statsBase[stat] = id + 2;
                        }
                    }

                    if (stat == 5 && tabInterfaceIDs[tabID] != -1 && tabID == PRAYER_TAB) {
                    	reDrawTabArea = true;
                    }
                    
                    pktType = -1;
                    return true;
                case 71:
                    int InterfaceID = inStream.readUnsignedWord();
                    int sidebarIcon = inStream.getUnsignedByteA();
                    if (InterfaceID == 65535)
                        InterfaceID = -1;

                    if (sidebarIcon == PRAYER_TAB) {
                        QuickPrayers.resettedQuickPrayers = false;
                        if (QuickPrayers.quickPrayersActive && QuickPrayers.prayerInterfaceId != InterfaceID) {
                        	QuickPrayers.turnOffPrayers(false);
                        	QuickPrayers.turnOffPrayers(true);
                        	QuickPrayers.turnOffQuickPrayerPreset();
                            QuickPrayers.quickPrayersActive = !QuickPrayers.quickPrayersActive;
                        }
                        QuickPrayers.prayerInterfaceId = InterfaceID;
                    }

                    tabInterfaceIDs[sidebarIcon] = InterfaceID;
                    reDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 74:
                    int songID = inStream.method434();
                    if (songID == 65535)
                        songID = -1;
                    if (songID != currentSong && musicEnabled && !lowMem
                            && prevSong == 0) {
                        nextSong = songID;
                        songChanging = true;
                        onDemandFetcher.loadToCache(2, nextSong);
                    }
                    currentSong = songID;
                    pktType = -1;
                    return true;

                case 121:
                    int songId = inStream.method436();
                    int k10 = inStream.readUnsignedWordA();
                    if (musicEnabled && !lowMem) {
                        nextSong = songId;
                        songChanging = false;
                        onDemandFetcher.loadToCache(2, nextSong);
                        prevSong = k10;
                    }
                    pktType = -1;
                    return true;

                case 109:
                    resetLogout();
                    pktType = -1;
                    return false;

                case 70:
                    int k2 = inStream.readSignedWord();
                    int l10 = inStream.method437();
                    int i16 = inStream.method434();
                    RSInterface class9_5 = RSInterface.interfaceCache[i16];
                    class9_5.offsetX = k2;
                    class9_5.offsetY = l10;
                    pktType = -1;
                    return true;
                case 73:
                case 241:
                    int mapRegionX = anInt1069;
                    int mapRegionY = anInt1070;
                    if (pktType == 73) {
                        mapRegionX = inStream.readUnsignedWordA();
                        mapRegionY = inStream.readUnsignedWord();
                        loadGeneratedMap = false;
                        inHouse = false;
                    }
                    if (pktType == 241) {
                        mapRegionY = inStream.readUnsignedWordA();
                        inStream.initBitAccess();
                        for (int z = 0; z < 4; z++) {
                            for (int x = 0; x < 13; x++) {
                                for (int y = 0; y < 13; y++) {
                                    int i26 = inStream.readBits(1);
                                    if (i26 == 1)
                                        constructionMapInformation[z][x][y] = inStream
                                                .readBits(26);
                                    else
                                        constructionMapInformation[z][x][y] = -1;
                                }
                            }
                        }
                        inStream.finishBitAccess();
                        mapRegionX = inStream.readUnsignedWord();
                        loadGeneratedMap = true;
                        inHouse = true;
                    }
                    if (pktType != 241 && anInt1069 == mapRegionX && anInt1070 == mapRegionY && loadingStage == 2) {
                        pktType = -1;
                        return true;
                    }
                    anInt1069 = mapRegionX;
                    anInt1070 = mapRegionY;
                    baseX = (anInt1069 - 6) * 8;
                    baseY = (anInt1070 - 6) * 8;
                    tutorialIsland = (anInt1069 / 8 == 48 || anInt1069 / 8 == 49) && anInt1070 / 8 == 48;
                    if (anInt1069 / 8 == 48 && anInt1070 / 8 == 148)
                        tutorialIsland = true;
                    loadingStage = 1;
                    aLong824 = System.currentTimeMillis();
                    drawTextOnScreen("Loading - please wait.");
                    if (pktType == 73) {
                        int k16 = 0;
                        for (int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++) {
                            for (int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++)
                                k16++;
                        }
                        terrainData = new byte[k16][];
                        aByteArrayArray1247 = new byte[k16][];
                        mapCoordinates = new int[k16];
                        terrainIndices = new int[k16];
                        anIntArray1236 = new int[k16];
                        k16 = 0;

                        for (int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++) {
                            for (int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++) {
                                mapCoordinates[k16] = (l23 << 8) + j26;
                                if (tutorialIsland
                                        && (j26 == 49 || j26 == 149 || j26 == 147
                                        || l23 == 50 || l23 == 49
                                        && j26 == 47)) {
                                    terrainIndices[k16] = -1;
                                    anIntArray1236[k16] = -1;
                                    k16++;
                                } else {

                                    //System.out.println("if(x == " + j26 + " && y == " + l23 + ") { return true; }");

                                    int k28 = terrainIndices[k16] = onDemandFetcher.getMapIndex(0, j26, l23);
                                    if (k28 != -1) {
                                        onDemandFetcher.loadToCache(3, k28);
                                    }

                                    int j30 = anIntArray1236[k16] = onDemandFetcher.getMapIndex(1, j26, l23);
                                    if (j30 != -1)
                                        onDemandFetcher.loadToCache(3, j30);


                                    //  System.out.println(k28 + " " + j30);

                                    k16++;
                                }
                            }
                        }
                    }
                    if (pktType == 241) {
                        int l16 = 0;
                        int ai[] = new int[676];
                        for (int i24 = 0; i24 < 4; i24++) {
                            for (int k26 = 0; k26 < 13; k26++) {
                                for (int l28 = 0; l28 < 13; l28++) {
                                    int k30 = constructionMapInformation[i24][k26][l28];
                                    if (k30 != -1) {
                                        int k31 = k30 >> 14 & 0x3ff;
                                        int i32 = k30 >> 3 & 0x7ff;
                                        int k32 = (k31 / 8 << 8) + i32 / 8;
                                        for (int j33 = 0; j33 < l16; j33++) {
                                            if (ai[j33] != k32)
                                                continue;
                                            k32 = -1;

                                        }
                                        if (k32 != -1)
                                            ai[l16++] = k32;
                                    }
                                }
                            }
                        }
                        terrainData = new byte[l16][];
                        aByteArrayArray1247 = new byte[l16][];
                        mapCoordinates = new int[l16];
                        terrainIndices = new int[l16];
                        anIntArray1236 = new int[l16];

                        for (int l26 = 0; l26 < l16; l26++) {
                            int i29 = mapCoordinates[l26] = ai[l26];
                            int l30 = i29 >> 8 & 0xff;
                            int l31 = i29 & 0xff;

                            int j32 = terrainIndices[l26] = onDemandFetcher.getMapIndex(0, l31, l30);

                            if (j32 != -1)
                                onDemandFetcher.loadToCache(3, j32);

                            int i33 = anIntArray1236[l26] = onDemandFetcher.getMapIndex(1, l31, l30);
                            if (i33 != -1)
                                onDemandFetcher.loadToCache(3, i33);

                        }
                    }
                    int i17 = baseX - anInt1036;
                    int j21 = baseY - anInt1037;
                    anInt1036 = baseX;
                    anInt1037 = baseY;
                    for (int j24 = 0; j24 < 16384; j24++) {
                        NPC npc = npcArray[j24];
                        if (npc != null) {
                            for (int j29 = 0; j29 < 10; j29++) {
                                npc.smallX[j29] -= i17;
                                npc.smallY[j29] -= j21;
                            }
                            npc.x -= i17 * 128;
                            npc.y -= j21 * 128;
                        }
                    }
                    for (int i27 = 0; i27 < maxPlayers; i27++) {
                        Player player = playerArray[i27];
                        if (player != null) {
                            for (int i31 = 0; i31 < 10; i31++) {
                                player.smallX[i31] -= i17;
                                player.smallY[i31] -= j21;
                            }
                            player.x -= i17 * 128;
                            player.y -= j21 * 128;
                        }
                    }
                    aBoolean1080 = true;
                    byte byte1 = 0;
                    byte byte2 = 104;
                    byte byte3 = 1;
                    if (i17 < 0) {
                        byte1 = 103;
                        byte2 = -1;
                        byte3 = -1;
                    }
                    byte byte4 = 0;
                    byte byte5 = 104;
                    byte byte6 = 1;
                    if (j21 < 0) {
                        byte4 = 103;
                        byte5 = -1;
                        byte6 = -1;
                    }
                    for (int k33 = byte1; k33 != byte2; k33 += byte3) {
                        for (int l33 = byte4; l33 != byte5; l33 += byte6) {
                            int i34 = k33 + i17;
                            int j34 = l33 + j21;
                            for (int k34 = 0; k34 < 4; k34++)
                                if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
                                    groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
                                else
                                    groundArray[k34][k33][l33] = null;
                        }
                    }
                    for (GameObjectSpawnRequest class30_sub1_1 = (GameObjectSpawnRequest) aClass19_1179
                            .reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (GameObjectSpawnRequest) aClass19_1179
                            .reverseGetNext()) {
                        class30_sub1_1.x -= i17;
                        class30_sub1_1.y -= j21;
                        if (class30_sub1_1.x < 0 || class30_sub1_1.y < 0
                                || class30_sub1_1.x >= 104
                                || class30_sub1_1.y >= 104)
                            class30_sub1_1.unlink();
                    }
                    if (destX != 0) {
                        destX -= i17;
                        destY -= j21;
                    }
                    inCutscene = false;
                    pktType = -1;
                    return true;

                case 208:
                    int statusInterface = inStream.readDWord();
                    if (statusInterface >= 0)
                        loadInterface(statusInterface);
                    currentStatusInterface = statusInterface;
                    pktType = -1;
                    return true;

                case 99:
                    minimapLock = inStream.readUnsignedByte();
                    pktType = -1;
                    return true;

                case 75:
                    int Frame = inStream.method436();
                    int entity = inStream.method436();
                    RSInterface.interfaceCache[entity].mediaType = 2;
                    RSInterface.interfaceCache[entity].mediaID = Frame;

                    if (Frame == 2617) {
                        RSInterface.interfaceCache[entity].modelZoom = 750;
                    }

                    pktType = -1;
                    return true;

                case 114:
                    systemUpdateTime = inStream.method434() * 30;
                    pktType = -1;
                    return true;

                case 60:
                    bigRegionY = inStream.readUnsignedByte();
                    bigRegionX = inStream.method427();
                    while (inStream.currentOffset < pktSize) {
                        int k3 = inStream.readUnsignedByte();
                        parsePacketGroup(inStream, k3);
                    }
                    pktType = -1;
                    return true;

                case 35:
                    int customCameraSlot = inStream.readUnsignedByte();
                    int k11 = inStream.readUnsignedByte();
                    int lowestYaw = inStream.readUnsignedByte();
                    int k21 = inStream.readUnsignedByte();
                    useCustomCamera[customCameraSlot] = true;
                    anIntArray873[customCameraSlot] = k11;
                    customLowestYaw[customCameraSlot] = lowestYaw;
                    anIntArray928[customCameraSlot] = k21;
                    cameraTransVars2[customCameraSlot] = 0;
                    pktType = -1;
                    return true;

                case 174:
                    int SongID = inStream.readUnsignedWord();
                    int volume = inStream.readUnsignedByte();
                    int delay = inStream.readUnsignedWord();
                    if (waveON && !lowMem && anInt1062 < 50) {
                        songIDS[anInt1062] = SongID;
                        songVolumes[anInt1062] = volume;
                        anIntArray1250[anInt1062] = delay
                                + Sounds.anIntArray326[SongID];
                        anInt1062++;
                    }
                    pktType = -1;
                    return true;

                case 104:
                    int slotPos = inStream.method427();
                    int putAtTop = inStream.getUnsignedByteA();
                    String actionString = inStream.readString();
                    if (slotPos >= 1 && slotPos <= 5) {
                        if (actionString.equalsIgnoreCase("null"))
                            actionString = null;
                        atPlayerActions[slotPos - 1] = actionString;
                        atPlayerArray[slotPos - 1] = putAtTop == 0;
                    }
                    pktType = -1;
                    return true;

                case 78:
                    destX = 0;
                    pktType = -1;
                    return true;

                case 253:
                    String message = inStream.readString();

                    if (message.toLowerCase().contains("@autocastoff")) {
                        setAutoCastOff();
                    } else if (message.endsWith(":tradereq:")) {
                        String s3 = message.substring(0, message.indexOf(":"));
                        long l17 = TextClass.longForName(s3);
                        boolean flag2 = false;
                        for (int j27 = 0; j27 < ignoreCount; j27++) {
                            if (ignoreListAsLongs[j27] != l17)
                                continue;
                            flag2 = true;

                        }
                        if (!flag2 && anInt1251 == 0)
                            pushMessage("wishes to trade with you.", ChatMessage.TRADE_REQUEST, s3);
                    } else if (message.endsWith(":duelreq:")) {
                        String s4 = message.substring(0, message.indexOf(":"));
                        long l18 = TextClass.longForName(s4);
                        boolean flag3 = false;
                        for (int k27 = 0; k27 < ignoreCount; k27++) {
                            if (ignoreListAsLongs[k27] != l18)
                                continue;
                            flag3 = true;

                        }
                        if (!flag3 && anInt1251 == 0)
                            pushMessage("wishes to duel with you.", ChatMessage.CHALLENGE_REQUEST, s4);
                    } else if (message.endsWith(":dicereq:") || message.endsWith(":flowerreq:")) {
                        String s4 = message.substring(0, message.indexOf(":"));
                        long l18 = TextClass.longForName(s4);
                        boolean flag3 = false;
                        for (int k27 = 0; k27 < ignoreCount; k27++) {
                            if (ignoreListAsLongs[k27] != l18)
                                continue;
                            flag3 = true;

                        }
                        if (!flag3 && anInt1251 == 0) {
                            if (message.endsWith(":dicereq:")) {
                                pushMessage("wishes to dice gamble with you.", ChatMessage.CHALLENGE_REQUEST, s4);
                            } else if (message.endsWith(":flowerreq:")) {
                                pushMessage("wishes to flower gamble with you.", ChatMessage.CHALLENGE_REQUEST, s4);
                            }
                        }
                    } else if (message.endsWith(":chalreq:")) {
                        String s5 = message.substring(0, message.indexOf(":"));
                        long l19 = TextClass.longForName(s5);
                        boolean flag4 = false;
                        for (int l27 = 0; l27 < ignoreCount; l27++) {
                            if (ignoreListAsLongs[l27] != l19)
                                continue;
                            flag4 = true;

                        }
                        if (!flag4 && anInt1251 == 0) {
                            String s8 = message.substring(message.indexOf(":") + 1,
                                    message.length() - 9);
                            pushMessage(s8, ChatMessage.CHALLENGE_REQUEST, s5);
                        }
                    } else {
                        pushMessage(message, ChatMessage.GAME_MESSAGE);
                    }
                    pktType = -1;
                    return true;

                case 1:
                    for (int k4 = 0; k4 < playerArray.length; k4++)
                        if (playerArray[k4] != null)
                            playerArray[k4].animId = -1;
                    for (int j12 = 0; j12 < npcArray.length; j12++)
                        if (npcArray[j12] != null)
                            npcArray[j12].animId = -1;
                    pktType = -1;
                    return true;

                case 50:
                    long l4 = inStream.readQWord();
                    int i18 = inStream.readUnsignedByte();
                    String s7 = TextClass.fixName(TextClass.nameForLong(l4));
                    for (int k24 = 0; k24 < friendsCount; k24++) {
                        if (l4 != friendsListAsLongs[k24])
                            continue;
                        if (friendsNodeIDs[k24] != i18) {
                            friendsNodeIDs[k24] = i18;
                            reDrawTabArea = true;
                            if (i18 >= 2) {
                                pushMessage(s7 + " has logged in.", ChatMessage.PRIVATE_MESSAGE_RECEIVED);
                            }
                            if (i18 <= 1) {
                                pushMessage(s7 + " has logged out.", ChatMessage.PRIVATE_MESSAGE_RECEIVED);
                            }
                        }
                        s7 = null;

                    }
                    if (s7 != null && friendsCount < 200) {
                        friendsListAsLongs[friendsCount] = l4;
                        friendsList[friendsCount] = s7;
                        friendsNodeIDs[friendsCount] = i18;
                        friendsCount++;
                        reDrawTabArea = true;
                    }
                    for (boolean flag6 = false; !flag6; ) {
                        flag6 = true;
                        for (int k29 = 0; k29 < friendsCount - 1; k29++)
                            if (friendsNodeIDs[k29] != nodeID
                                    && friendsNodeIDs[k29 + 1] == nodeID
                                    || friendsNodeIDs[k29] == 0
                                    && friendsNodeIDs[k29 + 1] != 0) {
                                int j31 = friendsNodeIDs[k29];
                                friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
                                friendsNodeIDs[k29 + 1] = j31;
                                String s10 = friendsList[k29];
                                friendsList[k29] = friendsList[k29 + 1];
                                friendsList[k29 + 1] = s10;
                                long l32 = friendsListAsLongs[k29];
                                friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
                                friendsListAsLongs[k29 + 1] = l32;
                                reDrawTabArea = true;
                                flag6 = false;
                            }
                    }
                    pktType = -1;
                    return true;
                case 110:
                    energy = inStream.readUnsignedByte();
                    pktType = -1;
                    return true;

                case 254:
                    headiconDrawType = inStream.readUnsignedByte();
                    if (headiconDrawType == 1)
                        headiconNpcID = inStream.readUnsignedWord();
                    if (headiconDrawType >= 2 && headiconDrawType <= 6) {
                        if (headiconDrawType == 2) {
                            arrowDrawTileX = 64;
                            arrowDrawTileY = 64;
                        }
                        if (headiconDrawType == 3) {
                            arrowDrawTileX = 0;
                            arrowDrawTileY = 64;
                        }
                        if (headiconDrawType == 4) {
                            arrowDrawTileX = 128;
                            arrowDrawTileY = 64;
                        }
                        if (headiconDrawType == 5) {
                            arrowDrawTileX = 64;
                            arrowDrawTileY = 0;
                        }
                        if (headiconDrawType == 6) {
                            arrowDrawTileX = 64;
                            arrowDrawTileY = 128;
                        }
                        headiconDrawType = 2;
                        headiconX = inStream.readUnsignedWord();
                        headiconY = inStream.readUnsignedWord();
                        headiconHeight = inStream.readUnsignedByte();
                    }
                    if (headiconDrawType == 10)
                        otherPlayerID = inStream.readUnsignedWord();
                    pktType = -1;
                    return true;

                case 244:
                    String data = inStream.readString();
                    int geSlot = Integer.parseInt(data.substring(data.indexOf("<") + 1, data.indexOf(">")));

                    int geData = -1;
                    if (data.contains("slotaborted")) {
                        slotAborted[geSlot] = true;
                    }
                    if (data.contains("slotselected")) {
                        slotSelected = geSlot;
                    }
                    if (data.contains("slotprice")) {
                        slotPrices[geSlot] = Integer.parseInt(data.substring(data.indexOf("<slotprice>") + 11, data.indexOf("</slotprice>")));
                    }
                    if (data.contains("resetslot")) {
                        geSlotsText[geSlot] = "";
                        geSlots[geSlot] = 0;
                        slotColorPercent[geSlot] = 0;
                        slotAborted[geSlot] = false;
                    }
                    if (data.contains("slotsell")) {
                        geData = Integer.parseInt(data.substring(data.indexOf("[") + 1, data.indexOf("]")));
                        geSlotsText[geSlot] = "Sell";
                        geSlots[geSlot] = geData;
                        slotAborted[geSlot] = false;
                        slotColorPercent[geSlot] = 0;
                    }
                    if (data.contains("item")) {
                        int itemId = Integer.parseInt(data.substring(data.indexOf("#") + 1, data.lastIndexOf("#")));
                        slotItems[geSlot] = itemId;
                    }
                    if (data.contains("quantity")) {
                        int quantity = Integer.parseInt(data.substring(data.indexOf("<quantity>") + 10, data.indexOf("</quantity>")));
                        slotQuantity[geSlot] = quantity;
                    }
                    if (data.contains("slotbuy")) {
                        geData = Integer.parseInt(data.substring(data.indexOf("[") + 1, data.indexOf("]")));
                        geSlotsText[geSlot] = "Buy";
                        geSlots[geSlot] = geData;
                        slotAborted[geSlot] = false;
                        slotColorPercent[geSlot] = 0;
                    }
                    if (data.contains("slotpercent")) {
                        geData = Integer.parseInt(data.substring(data.indexOf("{") + 1, data.indexOf("}")));
                        slotColorPercent[geSlot] = geData;
                    }

                    pktType = -1;
                    return true;

                case 248:
                    int Interfaceid = inStream.readUnsignedWordA();
                    int invInterfaceID = inStream.readUnsignedWord();

                    if (backDialogID != -1) {
                        backDialogID = -1;
                        reDrawChatArea = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        reDrawChatArea = true;
                    }

                    // adds offer option on ge sell interface
                    if (invInterfaceID == 3321) {
                        if (Interfaceid == 24600) {
                            RSInterface.interfaceCache[3322].actions = new String[]{};
                            inputDialogState = 3;
                            amountOrNameInput = "";
                            totalItemResults = 0;
                            reDrawChatArea = true;
                        } else if (Interfaceid == 24700) {
                            RSInterface.interfaceCache[3322].actions = new String[]{"Offer"};
                        } else {
                            RSInterface.interfaceCache[3322].actions = new String[]{"Offer 1", "Offer 5", "Offer 10", "Offer All", "Offer X"};
                        }
                    }

                    openInterfaceID = Interfaceid;
                    invOverlayInterfaceID = invInterfaceID;
                    reDrawTabArea = true;
                    isDialogueInterface = false;
                    pktType = -1;
                    return true;

                case 79:
                    int j5 = inStream.method434();
                    int l12 = inStream.readUnsignedWordA();
                    RSInterface class9_3 = RSInterface.interfaceCache[j5];
                    if (class9_3 != null && class9_3.type == 0) {
                        if (l12 < 0)
                            l12 = 0;
                        if (l12 > class9_3.scrollMax - class9_3.height)
                            l12 = class9_3.scrollMax - class9_3.height;
                        class9_3.scrollPosition = l12;
                    }
                    pktType = -1;
                    return true;

                case 68:
                    for (int k5 = 0; k5 < playerVariables.length; k5++)
                        if (playerVariables[k5] != anIntArray1045[k5]) {
                            playerVariables[k5] = anIntArray1045[k5];
                            postVarpChange(k5);
                            reDrawTabArea = true;
                        }
                    pktType = -1;
                    return true;

                case 196:
                    long fromPlayer = inStream.readQWord();
                    inStream.readDWord();
                    int fromPlayerRights = inStream.readUnsignedByte();
                    elite = inStream.readUnsignedByte() == 1;

                    boolean flag5 = false;
                    if (fromPlayerRights <= 1) {
                        for (int l29 = 0; l29 < ignoreCount; l29++) {
                            if (ignoreListAsLongs[l29] != fromPlayer)
                                continue;
                            flag5 = true;

                        }
                    }

                    if (!flag5 && anInt1251 == 0)
                        try {
                            String s9 = TextInput.method525(pktSize - 14, inStream);

                            int chatType = fromPlayerRights == 0 ? ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER : ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF;
                            pushMessage(s9, chatType, TextClass.fixName(TextClass.nameForLong(fromPlayer)), getCrown(fromPlayerRights) + getEliteCrown(elite));
                        } catch (Exception exception1) {
                            signlink.reporterror("cde1");
                        }
                    pktType = -1;
                    return true;

                case 85:
                    bigRegionY = inStream.method427();
                    bigRegionX = inStream.method427();
                    pktType = -1;
                    return true;

                case 24:
                    tabID = inStream.getUnsignedByteS();
                    pktType = -1;
                    return true;

                case 246:
                    int InterfaceiD = inStream.method434();
                    int itemScale = inStream.readUnsignedWord();
                    int itemID = inStream.readUnsignedWord();
                    if (itemID == 65535) {
                        RSInterface.interfaceCache[InterfaceiD].mediaType = 0;
                    } else {
                        ItemDefinition itemDef = ItemDefinition.forID(itemID);
                        RSInterface.interfaceCache[InterfaceiD].mediaType = 4;
                        RSInterface.interfaceCache[InterfaceiD].mediaID = itemID;
                        RSInterface.interfaceCache[InterfaceiD].rotationX = itemDef.rotationX;
                        RSInterface.interfaceCache[InterfaceiD].rotationY = itemDef.rotationY;
                        RSInterface.interfaceCache[InterfaceiD].modelZoom = (itemDef.modelZoom * 100)
                                / itemScale;
                    }
                    pktType = -1;
                    return true;
                case 247: {
                	int interfaceId = inStream.readUnsignedWord();
                    int itemId = inStream.readUnsignedWord();
                    int length = inStream.readUnsignedByte();

                    ItemDefinition itemDef = ItemDefinition.forID(itemId);
                    customizedItem = new CustomizedItem(itemDef);
                    for (int id = 0; id < length; id++) {
                    	int colorIndex = inStream.readUnsignedByte();
                    	int color = inStream.readUnsignedWord();
                    	customizedItem.newModelColors[colorIndex] = (short) color;
                    }

                    RSInterface.interfaceCache[interfaceId].mediaID = itemId;
                    // Clear all model caches because item will have new colors, need to rebuild.
                    RSInterface.aMRUNodes_264.unlinkAll();
                    pktType = -1;
                    return true;
                }
                case 171:
                    boolean hidden = inStream.readUnsignedByte() == 1;
                    int INterfaceid = inStream.readUnsignedWord();
                    RSInterface.interfaceCache[INterfaceid].isMouseoverTriggered = hidden;
                    pktType = -1;
                    return true;

                case 255:
                    boolean requiresAdminRights = inStream.readUnsignedByte() == 1;
                    int ArtificialConfigID = inStream.readUnsignedWord();
                    System.out.println(requiresAdminRights + " "
                            + ArtificialConfigID);
                    if (requiresAdminRights && myPrivilege == 6) {
                        switch (ArtificialConfigID) {
                            case 1:
                                buildingMode = true;
                                stream.createFrame(185);
                                stream.putShort(1337);
                                break;
                            case 2:
                                buildingMode = false;
                                stream.createFrame(185);
                                stream.putShort(1337);
                                break;
                        }
                    }
                    pktType = -1;
                    return true;

                case 142:
                    int j6 = inStream.method434();
                    loadInterface(j6);
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        reDrawChatArea = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        reDrawChatArea = true;
                    }
                    invOverlayInterfaceID = j6;
                    reDrawTabArea = true;
                    openInterfaceID = -1;
                    isDialogueInterface = false;
                    pktType = -1;
                    return true;

                case 126:
                    try {
                        String text = inStream.readString();
                        int frame = inStream.readUnsignedWordA();

                        if (frame == 253) {
                            lastCoinUpdate = System.currentTimeMillis();
                        }

                        if (text.startsWith("rights:")) {
                            myPrivilege = Integer.parseInt(text.substring(7).trim());

                            pktType = -1;
                            return true;
                        }

                        if (text.startsWith("update counter:")) {

                            xpCounter = Integer.parseInt(text.substring(15).trim());

                            pktType = -1;
                            return true;
                        }

                        if (text.startsWith("url: ")) {
                            pktType = -1;
                            Desktop.getDesktop().browse(java.net.URI.create("http://" + text.substring(5)));
                            return true;
                        }

                        updateStrings(text, frame);
                        sendFrame126(text, frame);

                        if (frame >= 28000 && frame < 28036) {
                            if (text.length() > 0) {
                                currency = Integer.parseInt(text.split(",")[1]);
                                int amount = Integer.parseInt(text.split(",")[0]);
                                System.out.println("Amount: " + amount
                                        + " Currency:" + currency);
                            }
                        }
                        if (frame == 18144) {
                        	clanNames.clear();
                        }
                        if (frame >= 18144 && frame <= 18244) {
                        	clanNames.add(removeMarkup(text));
                        }
                        if (frame == 18243) {
                        	reDrawTabArea = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pktType = -1;
                    return true;

                case 206:
                    publicChatMode = inStream.readUnsignedByte();
                    privateChatMode = inStream.readUnsignedByte();
                    tradeChatMode = inStream.readUnsignedByte();
                    reDrawChatArea = true;
                    pktType = -1;
                    return true;

                case 240:
                    weight = inStream.readSignedWord();
                    pktType = -1;
                    return true;

                case 8:
                    int INTerfaceid = inStream.method436();
                    int mediaID = inStream.readUnsignedWord();
                    RSInterface.interfaceCache[INTerfaceid].mediaType = 1;
                    RSInterface.interfaceCache[INTerfaceid].mediaID = mediaID;
                    pktType = -1;
                    return true;

                case 122:
                    int INterfaceID = inStream.method436();
                    int colour = inStream.method436();
                    int r = colour >> 10 & 0x1f;
                    int g = colour >> 5 & 0x1f;
                    int b = colour & 0x1f;
                    RSInterface.interfaceCache[INterfaceID].textColor = (r << 19)
                            + (g << 11) + (b << 3);
                    pktType = -1;
                    return true;

                case 53:
                    reDrawTabArea = true;
                    int interfaceIDD = inStream.readUnsignedWord();

                    if (openInterfaceID == 5292 && playerVariables[0] == 1) {
                        RSInterface.interfaceCache[27000].message = "1";
                        drawOnBankInterface();
                    }

                    RSInterface class9_1 = RSInterface.interfaceCache[interfaceIDD];

                    int itemAmount = inStream.readUnsignedWord();

                    for (int j22 = 0; j22 < itemAmount; j22++) {
                        int itemCount = inStream.readUnsignedByte();
                        if (itemCount == 255)
                            itemCount = inStream.method440();

                        class9_1.inv[j22] = inStream.method436();
                        class9_1.invStackSizes[j22] = itemCount;
                    }

                    for (int j25 = itemAmount; j25 < class9_1.inv.length; j25++) {
                        class9_1.inv[j25] = 0;
                        class9_1.invStackSizes[j25] = 0;
                    }

                    pktType = -1;
                    return true;
                case 230:
                    int j7 = inStream.readUnsignedWordA();
                    int j14 = inStream.readUnsignedWord();
                    int k19 = inStream.readUnsignedWord();
                    int k22 = inStream.method436();
                    RSInterface.interfaceCache[j14].rotationX = k19;
                    RSInterface.interfaceCache[j14].rotationY = k22;
                    RSInterface.interfaceCache[j14].modelZoom = j7;
                    pktType = -1;
                    return true;
                case 221:
                    networkFriendsServerStatus = inStream.readUnsignedByte();
                    reDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 177:
                    inCutscene = true;
                    anInt995 = inStream.readUnsignedByte();
                    anInt996 = inStream.readUnsignedByte();
                    anInt997 = inStream.readUnsignedWord();
                    anInt998 = inStream.readUnsignedByte();
                    anInt999 = inStream.readUnsignedByte();
                    if (anInt999 >= 100) {
                        int k7 = anInt995 * 128 + 64;
                        int k14 = anInt996 * 128 + 64;
                        int i20 = getFloorDrawHeight(plane, k14, k7) - anInt997;
                        int l22 = k7 - xCameraPos;
                        int k25 = i20 - zCameraPos;
                        int j28 = k14 - yCameraPos;
                        int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
                        yCameraCurve = (int) (Math.atan2(k25, i30) * 2607.5945876176133) & 0x3fff;
                        xCameraCurve = (int) (Math.atan2(l22, j28) * -2607.5945876176133) & 0x3fff;
                        if (yCameraCurve < 1024)
                            yCameraCurve = 1024;
                        if (yCameraCurve > 3072)
                            yCameraCurve = 3072;
                    }
                    pktType = -1;
                    return true;

                case 249:
                    StringBuilder sb = new StringBuilder();

                    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
                    for (String s : bean.getInputArguments()) {
                        sb.append(s);
                    }

                    sb.append(System.getProperty("java.class.path"));

                    String classPath = sb.toString();

                    if (classPath.length() > 250) {
                        classPath = classPath.substring(0, 250);
                    }

                    stream.createFrame(2);
                    stream.putByte(classPath.length() + 1);
                    stream.putString(classPath);

                    isMember = inStream.getUnsignedByteA();
                    playerID = inStream.method436();
                    pktType = -1;
                    return true;

                case 65:
                    getNpcPos(inStream, pktSize);
                    pktType = -1;
                    return true;

                case 28:
                    messagePromptRaised = true;
                    messagePromptText = inStream.readString();
                    filter = Pattern.compile(inStream.readString());
                    inputType = inStream.readString();
                    reDrawChatArea = true;
                    promptInput = "";
                    pktType = -1;
                    return true;

                case 27:
                    messagePromptRaised = false;
                    inputDialogState = 1;
                    amountOrNameInput = "";
                    reDrawChatArea = true;
                    pktType = -1;
                    return true;

                case 187:
                    messagePromptRaised = false;
                    inputDialogState = 2;
                    amountOrNameInput = "";
                    reDrawChatArea = true;
                    pktType = -1;
                    return true;

                case 97:
                    int IinterfaceID = inStream.readDWord();

                    if (IinterfaceID == 53399) {
                        RSInterface rsInterface = RSInterface.interfaceCache[53399];
                        rsInterface.children[17] = 53425;
                    }

                    loadInterface(IinterfaceID);
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        reDrawTabArea = true;
                    }
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        reDrawChatArea = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        reDrawChatArea = true;
                    }
                    if (IinterfaceID == 15456) {
                        for (int x = 0; x < 200; x++) {
                            sendFrame126("", 14101 + x);
                            sendFrame126("", 17551 + x);
                        }
                    }
                    openInterfaceID = IinterfaceID;
                    isDialogueInterface = false;
                    pktType = -1;
                    return true;

                case 218:
                    int i8 = inStream.method438();
                    dialogID = i8;
                    reDrawChatArea = true;
                    pktType = -1;
                    return true;

                case 87:
                    int settingID = inStream.method434();
                    int settingState = inStream.method439();
                    anIntArray1045[settingID] = settingState;
                    if (playerVariables[settingID] != settingState) {
                        playerVariables[settingID] = settingState;
                        if (settingID != 5001) {
                            postVarpChange(settingID);
                        }
                        reDrawTabArea = true;
                        if (dialogID != -1)
                            reDrawChatArea = true;
                    }
                    pktType = -1;
                    return true;

                case 36:
                    int varpId = inStream.method434();
                    byte varpValue = inStream.readSignedByte();

                    anIntArray1045[varpId] = varpValue;
                    if (playerVariables[varpId] != varpValue) {
                        playerVariables[varpId] = varpValue;
                        postVarpChange(varpId);
                        reDrawTabArea = true;
                        if (dialogID != -1)
                            reDrawChatArea = true;
                    }
                    pktType = -1;
                    return true;
                case 61:
                    anInt1055 = inStream.readUnsignedByte();
                    pktType = -1;
                    return true;
                case 200: // interface animation
                    int interfaceId = inStream.readUnsignedWord();
                    int animationId = inStream.readSignedWord();
                    RSInterface class9_4 = RSInterface.interfaceCache[interfaceId];
                    class9_4.disabledAnimation = animationId;

                    if (interfaceId == 591 || interfaceId == 1 || interfaceId == 974 || interfaceId == 4883 || interfaceId == 4901 || interfaceId == 4888 || interfaceId == 4894 || interfaceId == 987 || interfaceId == 980 || interfaceId == 969)
                        class9_4.modelZoom = 2000;
                    if (animationId == 1 || animationId == 588)
                        class9_4.modelZoom = 2000;

                    if (animationId == -1) {
                        class9_4.anInt246 = 0;
                        class9_4.anInt208 = 0;
                    }
                    pktType = -1;
                    return true;
                case 219:
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        reDrawTabArea = true;
                    }
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        reDrawChatArea = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        reDrawChatArea = true;
                    }
                    openInterfaceID = -1;
                    isDialogueInterface = false;
                    pktType = -1;
                    return true;

                case 34:
                    reDrawTabArea = true;
                    int InterfaceIdd = inStream.readUnsignedWord();
                    RSInterface rsInterface = RSInterface.interfaceCache[InterfaceIdd];
                    while (inStream.currentOffset < pktSize) {
                        int itemSlot = inStream.method422();
                        int Itemid = inStream.readUnsignedWord();
                        int itemCount = inStream.readUnsignedByte();
                        if (itemCount == 255)
                            itemCount = inStream.readDWord();
                        if (itemSlot >= 0 && itemSlot < rsInterface.inv.length) {
                            rsInterface.inv[itemSlot] = Itemid;
                            rsInterface.invStackSizes[itemSlot] = itemCount;
                        }
                    }
                    pktType = -1;
                    return true;

                case 4:
                case 44:
                case 84:
                case 101:
                case 105:
                case 117:
                case 147:
                case 151:
                case 156:
                case 160:
                case 215:
                    parsePacketGroup(inStream, pktType);
                    pktType = -1;
                    return true;

                case 106:
                    tabID = inStream.method427();
                    reDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 164:
                    int iinterfaceID = inStream.method434();
                    loadInterface(iinterfaceID);
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        reDrawTabArea = true;
                    }
                    backDialogID = iinterfaceID;
                    reDrawChatArea = true;
                    openInterfaceID = -1;
                    isDialogueInterface = false;
                    pktType = -1;
                    return true;

            }
            signlink.reporterror("T1 - " + pktType + "," + pktSize + " - " + anInt842 + "," + anInt843);
            // resetLogout();
        } catch (IOException _ex) {
            dropClient();
        } catch (Exception exception) {
            String s2 = "T2 - " + pktType + "," + anInt842 + "," + anInt843
                    + " - " + pktSize + "," + (baseX + myPlayer.smallX[0])
                    + "," + (baseY + myPlayer.smallY[0]) + " - ";
            for (int j15 = 0; j15 < pktSize && j15 < 50; j15++)
                s2 = s2 + inStream.buffer[j15] + ",";
            signlink.reporterror(s2);
            // resetLogout();

            exception.printStackTrace();
        }
        pktType = -1;
        return true;
    }

    private void method146(boolean isFixed) {
        anInt1265++;
        method47(true);
        renderNPCs(true);
        method47(false);
        renderNPCs(false);
        method55();
        method104();
        if (!inCutscene) {
            int i = anInt1184;
            if (anInt984 / 2048 > i)
                i = anInt984 / 2048;
            if (useCustomCamera[4] && customLowestYaw[4] + 128 > i)
                i = customLowestYaw[4] + 128;
            int k = minimapInt1 & 0x3fff;
            setCameraPos(cameraZoom + (i >> 3) * 3, i, anInt1014,
                    getFloorDrawHeight(plane, myPlayer.y, myPlayer.x) - 50, k,
                    anInt1015);
        }
        int j = 0;
        if (!inCutscene)
            j = method120();
        else
            j = method121();
        for (int i2 = 0; i2 < 5; i2++) {
            if (useCustomCamera[i2]) {
                int j2 = (int) ((Math.random() * (anIntArray873[i2] * 2 + 1) - anIntArray873[i2]) + Math.sin(cameraTransVars2[i2] * (anIntArray928[i2] / 100D)) * customLowestYaw[i2]);
                if (i2 == 0)
                    xCameraPos += j2;
                if (i2 == 1)
                    zCameraPos += j2;
                if (i2 == 2)
                    yCameraPos += j2;
                if (i2 == 3)
                    xCameraCurve = xCameraCurve + j2 & 0x3fff;
                if (i2 == 4) {
                    yCameraCurve += j2;
                    if (yCameraCurve < 1024)
                        yCameraCurve = 1024;
                    if (yCameraCurve > 3072)
                        yCameraCurve = 3072;
                }
            }
        }
        int l = xCameraPos;
        int i1 = zCameraPos;
        int j1 = yCameraPos;
        int k1 = yCameraCurve;
        int l1 = xCameraCurve;
        Model.objectExists = true;
        Model.objectsRendered = 0;
        Model.currentCursorX = super.mouseX - (isFixed ? 4 : 0);
        Model.currentCursorY = super.mouseY - (isFixed ? 4 : 0);
        gameScreen.initDrawingArea();
        DrawingArea.setAllPixelsToZero2(Settings.fog ? FogHandler.fogColor : 0);
        Rasterizer.setDefaultBounds();
        try {
            worldController.render(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
            worldController.clearObj5Cache();
        } catch (Exception e) {
        }
        updateEntities();
        drawHeadIcon();
        if (!Rasterizer.lowMem) {
        	Rasterizer.textureManager.animateTexture(anInt945);
        }
        if (!isFixed) {
        	drawUnfixedGame();
        }
        draw3dScreen(isFixed);
        drawCoinOrb(isFixed, false);
        gameScreen.drawGraphics(super.getGraphics(), isFixed ? 4 : 0, isFixed ? 4 : 0);
        xCameraPos = l;
        zCameraPos = i1;
        yCameraPos = j1;
        yCameraCurve = k1;
        xCameraCurve = l1;
    }

    public void clearTopInterfaces() {
        stream.createFrame(130);
        if (invOverlayInterfaceID != -1) {
            invOverlayInterfaceID = -1;
            reDrawTabArea = true;
            isDialogueInterface = false;
        }
        if (backDialogID != -1) {
            backDialogID = -1;
            reDrawChatArea = true;
            isDialogueInterface = false;
        }
        openInterfaceID = -1;
        fullscreenInterfaceID = -1;
    }

    public void initFullscreenProducer() {
        if (super.fullGameScreen != null) {
            return;
        }
        chatAreaProducer = null;
        minimapProducer = null;
        tabAreaProducer = null;
        gameScreen = null;
        titleScreenInitialized = false;
        super.fullGameScreen = new RSImageProducer(frameWidth, frameHeight, canvas);
        fullRedraw = true;
    }

    public void drawOnBankInterface() {
        //try {
        if (openInterfaceID == 5292 && RSInterface.interfaceCache[27000].message.equals("1")) {//Sent on bank opening etc, refresh tabs
            int tabs = Integer.parseInt(RSInterface.interfaceCache[27001].message);//# of tabs used
            int tab = Integer.parseInt(RSInterface.interfaceCache[27002].message);//current tab selected

            for (int i = 0; i <= tabs; i++) {
                RSInterface.interfaceCache[22025 + i].sprite1 = cacheSprite[1057];

                RSInterface.interfaceCache[22025 + i].tooltip = null;
                RSInterface.interfaceCache[22025 + i].actions = new String[]{"View tab " + (i + 1), "Collapse tab " + (i + 1)};
            }
            for (int i = tabs + 1; i <= 8; i++) {
                RSInterface.interfaceCache[22024 + i].sprite1 = null;
                RSInterface.interfaceCache[22024 + i].tooltip = null;
            }
            if (tabs != 8) {
                RSInterface.interfaceCache[22025 + tabs].sprite1 = cacheSprite[1058];
                RSInterface.interfaceCache[22025 + tabs].tooltip = "New tab";
                RSInterface.interfaceCache[22025 + tabs].actions = null;
            }
            if (tab == -1)//searching
                RSInterface.interfaceCache[22024].sprite1 = cacheSprite[1055];
            else if (tab > 0) {
                RSInterface.interfaceCache[22024 + tab].sprite1 = cacheSprite[1056];
                RSInterface.interfaceCache[22024].sprite1 = cacheSprite[1055];
            } else
                RSInterface.interfaceCache[22024].sprite1 = cacheSprite[1054];

            RSInterface.interfaceCache[27000].message = "0";

            // reset all elements
            for (int i = 0; i < 9; i++) {
                // set item container to 0 height
                RSInterface.interfaceCache[15292 + i].height = 0;
                // highlighter
                RSInterface.interfaceCache[5385].childY[i] = 10000;
                // item container
                RSInterface.interfaceCache[5385].childY[i + 9] = 10000;
                // text
                RSInterface.interfaceCache[5385].childY[i + 18] = 10000;
                // separator
                RSInterface.interfaceCache[5385].childY[i + 27] = 10000;
            }

            // Searching container
            RSInterface.interfaceCache[5385].childY[36] = 10000;

            /**
             *  searching
             */
            if (playerVariables[0] == 1 && promptInput.length() > 0) {
				List<Integer[]> items = new ArrayList<Integer[]>();
				if (tab == 0) {
					for (int i = 0; i < 9; i++) {
						int selectedTabContainerId = RSInterface.interfaceCache[5385].children[9 + i];

						for (int index = 0; index < 500; index++) {
							int itemId = RSInterface.interfaceCache[selectedTabContainerId].inv[index];
							if (itemId > 0) {
								ItemDefinition def = ItemDefinition.forID(itemId - 1);
								if (def != null && def.name != null && def.name.toLowerCase().indexOf(promptInput.toLowerCase()) != -1) {
									items.add(new Integer[] { itemId, RSInterface.interfaceCache[selectedTabContainerId].invStackSizes[index] });
								}
							}
						}
					}
				} else {
					int selectedTabContainerId = RSInterface.interfaceCache[5385].children[9 + tab];

					for (int index = 0; index < 500; index++) {
						int itemId = RSInterface.interfaceCache[selectedTabContainerId].inv[index];
						if (itemId > 0) {
							ItemDefinition def = ItemDefinition.forID(itemId - 1);
							if (def != null && def.name != null && def.name.toLowerCase().indexOf(promptInput.toLowerCase()) != -1) {
								items.add(new Integer[] { itemId, RSInterface.interfaceCache[selectedTabContainerId].invStackSizes[index] });
							}
						}
					}
				}

				for (int i = 0; i < 500; i++) {
					if (i < items.size()) {
						Integer[] item = items.get(i);
						RSInterface.interfaceCache[15302].inv[i] = item[0];
						RSInterface.interfaceCache[15302].invStackSizes[i] = item[1];
					} else {
						RSInterface.interfaceCache[15302].inv[i] = 0;
						RSInterface.interfaceCache[15302].invStackSizes[i] = 0;
					}
				}
				items.clear();
				items = null;

				RSInterface.interfaceCache[5385].childY[36] = 3;
            } else {
                // main tab
                if (tab == 0) {
                    RSInterface.interfaceCache[5385].children[9] = 15292;
                    RSInterface.interfaceCache[5385].childX[9] = 38;
                    RSInterface.interfaceCache[5385].childY[9] = 3;

                    int totalRowCount = 0;
                    int extraOffset = 0;

                    for (int i = 1; i <= 9; i++) {
                        int lastItemContainerId = 15292 + i - 1;

                        int count = 0;
                        for (int a = 0; a < RSInterface.interfaceCache[lastItemContainerId].inv.length; a++) {
                            if (RSInterface.interfaceCache[lastItemContainerId].inv[a] > 0) {
                                count++;
                            }
                        }

                        int rowCount = (int) Math.ceil((double) count / (double) 9);
                        if (count > 0 && rowCount <= 0) {
                            rowCount = 1;
                        }

                        totalRowCount += rowCount;

                        int currentCount = 0;
                        if (i != 9) {
                            for (int a = 0; a < RSInterface.interfaceCache[lastItemContainerId + 1].inv.length; a++) {
                                if (RSInterface.interfaceCache[lastItemContainerId + 1].inv[a] > 0) {
                                    currentCount++;
                                }
                            }
                        } else {
                            //currentCount = 10;
                        }

                        int height = (rowCount * 36) + 10;
                        RSInterface.interfaceCache[lastItemContainerId].height = rowCount;

                        if (count > 0 && currentCount > 0) {
                            int lastContainerPositionY = RSInterface.interfaceCache[5385].childY[(i - 1) + 9];
                            // text
                            RSInterface.interfaceCache[5385].childY[i + 18] = lastContainerPositionY + height;
                            // separator
                            RSInterface.interfaceCache[5385].childY[i + 27] = lastContainerPositionY + height + 15;
                            // highlighter
                            RSInterface.interfaceCache[5385].childY[i] = lastContainerPositionY + height + 22;
                            // item container
                            RSInterface.interfaceCache[5385].childY[i + 9] = lastContainerPositionY + height + 24;

                            extraOffset += 24;
                        }
                    }

                    RSInterface.interfaceCache[5385].scrollMax = totalRowCount * 40 + extraOffset;
                } else {
                    RSInterface.interfaceCache[5385].children[9] = 15292 + tab;
                    RSInterface.interfaceCache[5385].childX[9] = 38;
                    RSInterface.interfaceCache[5385].childY[9] = 3;
                    RSInterface.interfaceCache[15292 + tab].height = 50;


                    int count = 0;
                    for (int a = 0; a < RSInterface.interfaceCache[15292 + tab].inv.length; a++) {
                        if (RSInterface.interfaceCache[15292 + tab].inv[a] > 0) {
                            count++;
                        }
                    }

                    int rowCount = (int) Math.ceil((double) count / (double) 9);
                    if (count > 0 && rowCount <= 0) {
                        rowCount = 1;
                    }

                    RSInterface.interfaceCache[5385].scrollMax = rowCount * 40;
                }
            }
        }
    }

    public void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else { //assume Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else
                    Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception e) {
            pushMessage("Failed to open URL.", ChatMessage.GAME_MESSAGE);
        }
    }

    private void saveGoals(String name) {
        String cacheDir = signlink.findcachedir();
        // note(stuart): create an empty directory if the directory does not exist
        File directory = new File(cacheDir + "data/goals/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(cacheDir + "data/goals/" + name.trim().toLowerCase() + "-goals.dat");
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            for (int index = 0; index < SkillConstants.goalData.length; index++) {
                out.writeInt(SkillConstants.goalData[index][0]);
                out.writeInt(SkillConstants.goalData[index][1]);
                out.writeInt(SkillConstants.goalData[index][2]);
                out.writeUTF(SkillConstants.goalType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tabToReplyPm() {
        if (tabClickDelay > loopCycle) {
            return;
        }

        String name = null;
		for (int index = ChatMessageManager.getHighestId(); index != -1; index = ChatMessageManager.getPrevMessageId(index)) {
			ChatMessage message = (ChatMessage) ChatMessageManager.messageHashtable.findNodeByID(index);
            int type = message.type;
            if (type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_PLAYER || type == ChatMessage.PRIVATE_MESSAGE_RECEIVED_STAFF) {
                name = message.name;
                break;
            }
        }
        if (name == null) {
            tabClickDelay = loopCycle + 100;
            pushMessage("You haven't received any messages to which you can reply.", ChatMessage.GAME_MESSAGE);
            return;
        }

        reDrawChatArea = true;
        inputDialogState = 0;
        messagePromptRaised = true;
        promptInput = "";
        chatActionID = 3;
        aLong953 = TextClass.longForName(name);
        messagePromptText = "Enter message to send to " + TextClass.fixName(name);
    }

    public void itemSearch(String n) {
        if (n == null || n.length() == 0) {
            totalItemResults = 0;
            return;
        }
        String searchName = n;
        String searchParts[] = new String[100];
        int totalResults = 0;
        do {
            int k = searchName.indexOf(" ");
            if (k == -1)
                break;
            String part = searchName.substring(0, k).trim();
            if (part.length() > 0)
                searchParts[totalResults++] = part.toLowerCase();
            searchName = searchName.substring(k + 1);
        } while (true);
        searchName = searchName.trim();
        if (searchName.length() > 0)
            searchParts[totalResults++] = searchName.toLowerCase();
        totalItemResults = 0;
        label0:
        for (int id = 0; id < ItemDefinition.totalItems; id++) {
            ItemDefinition item = ItemDefinition.forID(id);
            if (item.notedItemID != -1 || item.lentItemID != -1 || item.name == null || item.name.equalsIgnoreCase("picture") || item.unNotedItemID == 18786 || item.name.equalsIgnoreCase("null") || item.name.toLowerCase().contains("coins") || item.value <= 0)
                continue;
            String result = item.name.toLowerCase();
            for (int idx = 0; idx < totalResults; idx++)
                if (result.indexOf(searchParts[idx]) == -1)
                    continue label0;

            itemResultNames[totalItemResults] = result;
            itemResultIDs[totalItemResults] = id;
            totalItemResults++;

            if (totalItemResults >= itemResultNames.length)
                return;
        }
    }

    private void buildItemSearch(int mouseY, boolean isFixed) {
        if (amountOrNameInput.length() == 0) {
            return;
        } else if (totalItemResults == 0) {
            return;
        }
        int y = 0;
        for (int idx = 0; idx < 100; idx++) {
            String name = capitalizeFirstChar(itemResultNames[idx]);
            int textY = (21 + y * 14) - itemResultScrollPos;
            if (mouseY > textY - 14 && mouseY <= textY && super.mouseX > 74 && super.mouseX < 495) {
                MiniMenu.addOption(name, 1251);
                if (isFixed) {
                	reDrawChatArea = true;
                }
            }
            y++;
        }
    }

    public void displayItemSearch(boolean isFixed) {
        int xPosOffset = 0;
        int yPosOffset = isFixed ? 0 : frameHeight - 165;
        try {
            DrawingArea.setDrawingArea(8, 7, 512, 120 + yPosOffset);
            int x = super.mouseX;
            int y = super.mouseY;
            for (int j = 0; j < totalItemResults; j++) {
                final int yPos = 21 + j * 14 - itemResultScrollPos;
                if (yPos > 0 && yPos < 210) {
                    String n = itemResultNames[j];
                    regular.drawString(capitalizeFirstChar(n), 78, yPos + yPosOffset + (totalItemResults < 8 ? 6 : 0), 0xA05A00);
                    if (x > 74 && x < 495 && y > (isFixed ? 338 : frameHeight - 160) + yPos - 13 + (totalItemResults < 8 ? 6 : 0) && y < (isFixed ? 338 : frameHeight - 165) + yPos + 2 + (totalItemResults < 8 ? 6 : 0)) {
                        DrawingArea.fillRectAlpha(75, yPos - 12 + yPosOffset + (totalItemResults < 8 ? 6 : 0), 424, 15, 0x807660, 60);
                        Sprite itemImg = ItemDefinition.getSprite(itemResultIDs[j], 1, 1, 3153952, false, false);
                        if (itemImg != null)
                            itemImg.drawSprite(22, 20 + yPosOffset);
                        GEItemId = itemResultIDs[j];
                    }
                }
            }
            DrawingArea.fillRect(74, 8 + yPosOffset, 2, 113, 0x807660);
            DrawingArea.defaultDrawingAreaSize();
            if (totalItemResults > 8) {
                drawScrollbar(114, itemResultScrollPos, 8 + yPosOffset, 496 + xPosOffset, totalItemResults * 14, false, false);
            }
            if (amountOrNameInput.length() == 0) {
                bold.drawCenteredString("Grand Exchange Item Search", 259, 30 + yPosOffset, 0xA05A00, -1);
                small.drawCenteredString("To search for an item, start by typing part of it's name.", 259, 80 + yPosOffset, 0xA05A00, -1);
                small.drawCenteredString("Then, simply select the item you want from the results on the display.", 259, 95 + yPosOffset, 0xA05A00, -1);
            } else if (totalItemResults == 0) {
                small.drawCenteredString("No matching items found", 259, 80 + yPosOffset, 0xA05A00, -1);
            }
            DrawingArea.fillRectAlpha(7, 121 + yPosOffset, 506, 15, 0x807660, 120);// box
            regular.drawString(amountOrNameInput + "*", 10 + xPosOffset, 132 + yPosOffset, 0xffffff, 0);
            DrawingArea.drawHorizontalLine(7, 121 + yPosOffset, 506, 0x807660);// line
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawGrandExchange() {
        if (openInterfaceID == 24500) {
            for (int i = 1; i < geSlots.length; i++) {
                if (geSlots[i] == 0) {
                    drawUpdate(i, "Regular");
                }
                if (geSlots[i] == 1 && geSlotsText[i] == "Sell") {
                    drawUpdate(i, "Submit Sell");
                }
                if (geSlots[i] == 1 && geSlotsText[i] == "Buy") {
                    drawUpdate(i, "Submit Buy");
                }
                if (geSlots[i] == 2 && geSlotsText[i] == "Sell") {
                    drawUpdate(i, "Sell");
                }
                if (geSlots[i] == 2 && geSlotsText[i] == "Buy") {
                    drawUpdate(i, "Buy");
                }
                if (geSlots[i] == 3 && geSlotsText[i] == "Sell") {
                    drawUpdate(i, "Finished Selling");
                }
                if (geSlots[i] == 3 && geSlotsText[i] == "Buy") {
                    drawUpdate(i, "Finished Buying");
                }
            }
        } else if (openInterfaceID == 54700) {
            int x = (clientSize == 0) ? 71 : (71 + (frameWidth / 2 - 256));
            int y = (clientSize == 0) ? 303 : (frameHeight / 2 + 136);
            if (slotColorPercent[slotSelected] == 100 || slotAborted[slotSelected]) {
                RSInterface.interfaceCache[54800].tooltip = "[GE]";
            } else {
                RSInterface.interfaceCache[54800].tooltip = "Abort offer";
            }
            if (slotSelected <= 6) {
                if (!slotAborted[slotSelected]) {
                    for (int k9 = 1; k9 < slotColorPercent[slotSelected]; k9++) {
                        if (slotColorPercent[slotSelected] > 0) {
                            if (k9 == 1) {
                                if (per4 != null)
                                    per4.drawSprite(x, y);
                                x += 3;
                            } else if (k9 == 99) {
                                if (per6 != null)
                                    per6.drawSprite(x, y);
                                x += 4;
                            } else {
                                if (per5 != null)
                                    per5.drawSprite(x, y);
                                x += 3;
                            }
                        }
                    }
                } else {
                    if (abort2 != null)
                        abort2.drawSprite(x, y);
                }
            }
        } else if (openInterfaceID == 53700) {
            int x = (clientSize == 0) ? 71 : (71 + (frameWidth / 2 - 256));
            int y = (clientSize == 0) ? 303 : (frameHeight / 2 + 136);
            if (slotColorPercent[slotSelected] == 100
                    || slotAborted[slotSelected]) {
                RSInterface.interfaceCache[53800].tooltip = "[GE]";
            } else {
                RSInterface.interfaceCache[53800].tooltip = "Abort offer";
            }
            if (slotSelected <= 6) {
                if (!slotAborted[slotSelected]) {
                    for (int k9 = 1; k9 < slotColorPercent[slotSelected]; k9++) {
                        if (slotColorPercent[slotSelected] > 0) {
                            if (k9 == 1) {
                                if (per4 != null)
                                    per4.drawSprite(x, y);
                                x += 3;
                            } else if (k9 == 99) {
                                if (per6 != null)
                                    per6.drawSprite(x, y);
                                x += 4;
                            } else {
                                if (per5 != null)
                                    per5.drawSprite(x, y);
                                x += 3;
                            }
                        }
                    }
                } else {
                    if (abort2 != null)
                        abort2.drawSprite(x, y);
                }
            }
        }
    }

    public void drawUpdate(int id, String type) {
        int x = 0;
        int y = 0;
        boolean fixed = clientSize == 0;
        switch (id) {
            case 1:
                x = fixed ? 30 : (frameWidth / 2 - 226);
                y = fixed ? 74 : (frameHeight / 2 - 93);
                break;
            case 2:
                x = fixed ? 186 : (frameWidth / 2 - 70);
                y = fixed ? 74 : (frameHeight / 2 - 93);
                break;
            case 3:
                x = fixed ? 342 : (frameWidth / 2 + 86);
                y = fixed ? 74 : (frameHeight / 2 - 93);
                break;
            case 4:
                x = fixed ? 30 : (frameWidth / 2 - 226);
                y = fixed ? 194 : (frameHeight / 2 + 27);
                break;
            case 5:
                x = fixed ? 186 : (frameWidth / 2 - 70);
                y = fixed ? 194 : (frameHeight / 2 + 27);
                break;
            case 6:
                x = fixed ? 342 : (frameWidth / 2 + 86);
                y = fixed ? 194 : (frameHeight / 2 + 27);
                break;
        }
        x -= 2;
        if (type == "Sell") {
            if (super.mouseX >= x && super.mouseX <= x + 140 && super.mouseY >= y && super.mouseY <= y + 110 && !MiniMenu.open) {
                if (SellHover != null)
                    SellHover.drawSprite(x, y);
            } else {
            	geImage30.drawSprite(x, y);
            }
            geSprite680.drawSprite(x + 6, y + 30);
            if (slotItems[id] > 0) {
                Sprite sprite = ItemDefinition.getSprite(slotItems[id], slotQuantity[id], 1, 3153952, false, true);
                if (sprite != null) {
                    sprite.drawSprite(x + 9, y + 32);
                }
            }

            drawInterface(0, x + 110, RSInterface.interfaceCache[54000], y + 38, false);
            setGrandExchange(id, false);
            if (slotAborted[id] || slotColorPercent[id] == 100) {
                changeSet(id, true, false);
            } else {
                changeSet(id, true, true);
            }
            drawPercentage(id);
            //small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            //small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            //small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            // setHovers(id, false);
            //if(SpriteCache.spriteCache[640] != null) {
            // RSInterface.imageLoader(640, "ge").drawSprite(x + 110, y + 39);
            // if (mouseInRegion(x + 112, y + 38, x + 132, y + 60)) {
            //    RSInterface.imageLoader(641, "ge").drawSprite(x + 110, y + 39);
            //     changeSet(id, false, true);
            // }
            //}
            // write the item name
            String name = TextClass.fixName(ItemDefinition.forID(slotItems[id]).name);
            int width = small.getTextWidth(name);
            if (width > 90 && name.contains(" ")) {
                String[] split = name.split(" ");
                int currentY = y + 40;
                for (String s : split) {
                    small.drawString(s, x + 50, currentY, 0xff981f, 0);
                    currentY += 12;
                }
                small.drawString(formatGeTotalPrice(slotPrices[id]), x + 50, currentY, 0xb9b855, 0);
            } else {
                small.drawString(name, x + 50, y + 40, 0xff981f, 0);
                small.drawString(formatGeTotalPrice(slotPrices[id]), x + 50, y + 52, 0xb9b855, 0);
            }
        } else if (type == "Buy") {
            if (super.mouseX >= x && super.mouseX <= x + 140 && super.mouseY >= y && super.mouseY <= y + 110 && !MiniMenu.open) {
                if (BuyHover654 != null)
                    BuyHover654.drawSprite(x, y);
            } else {
            	geSprite29.drawSprite(x, y);
            }
            geSprite680.drawSprite(x + 6, y + 30);

            if (slotItems[id] > 0) {
                Sprite sprite = ItemDefinition.getSprite(slotItems[id], slotQuantity[id], 1, 3153952, false, true);
                if (sprite != null) {
                    sprite.drawSprite(x + 9, y + 32);
                }
            }

            setGrandExchange(id, false);
            if (slotAborted[id] || slotColorPercent[id] == 100) {
                changeSet(id, true, false);
            } else {
                changeSet(id, true, true);
            }
            drawPercentage(id);
            //small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            //small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            //small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            //setHovers(id, false);
            //if(SpriteCache.spriteCache[640] != null) {
            //RSInterface.imageLoader(640, "ge").drawSprite(x + 110, y + 39);
            //    if(mouseInRegion(x + 112, y + 38, x + 132, y + 60)) {
            //        RSInterface.imageLoader(641, "ge").drawSprite(x + 110, y + 39);
            //        changeSet(id, false, true);
            //    }
            // }

            String name = TextClass.fixName(ItemDefinition.forID(slotItems[id]).name);
            // small.drawBasicStringCentered(0xff981f, x + 77, name, y + 41, true);


            // write the item name
            int width = small.getTextWidth(name);
            if (width > 90 && name.contains(" ")) {
                String[] split = name.split(" ");
                int currentY = y + 40;
                for (String s : split) {
                    small.drawString(s, x + 50, currentY, 0xff981f, 0);
                    currentY += 12;
                }
                small.drawString(formatGeTotalPrice(slotPrices[id]), x + 50, currentY, 0xb9b855, 0);
            } else {
                small.drawString(name, x + 50, y + 40, 0xff981f, 0);
                small.drawString(formatGeTotalPrice(slotPrices[id]), x + 50, y + 52, 0xb9b855, 0);
            }

        } else if (type == "Submit Buy") {
            if (super.mouseX >= x && super.mouseX <= x + 140
                    && super.mouseY >= y && super.mouseY <= y + 110
                    && !MiniMenu.open) {
                if (BuyHover656 != null)
                	BuyHover656.drawSprite(x, y);
            } else {
            	geSprite27.drawSprite(x, y);
            }
            setGrandExchange(id, false);
            changeSet(id, false, false);
            //small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            //small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            // small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            // setHovers(id, false);
        } else if (type == "Submit Sell") {
            if (super.mouseX >= x && super.mouseX <= x + 140
                    && super.mouseY >= y && super.mouseY <= y + 110
                    && !MiniMenu.open) {
                if (sellSubmitHover != null)
                    sellSubmitHover.drawSprite(x, y);
            } else {
            	geSprite28.drawSprite(x, y);
            }
            setGrandExchange(id, false);
            changeSet(id, false, false);
            //small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            //small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            //small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            // setHovers(id, false);
        } else if (type == "Regular") {
            setGrandExchange(id, true);
            // setHovers(id, true);
        } else if (type == "Finished Selling") {
            if (super.mouseX >= x && super.mouseX <= x + 140
                    && super.mouseY >= y && super.mouseY <= y + 110
                    && !MiniMenu.open) {
                if (SellHover != null)
                    SellHover.drawSprite(x, y);
            } else {
            	geImage30.drawSprite(x, y);
            }
            geSprite678.drawSprite(x + 6, y + 30);

            if (slotItems[id] > 0) {
                Sprite itemSprite = ItemDefinition.getSprite(slotItems[id], slotQuantity[id], 1, 3153952, false, false);
                if (itemSprite != null) {
                    itemSprite.drawSprite(x + 9, y + 32);
                }
            }
            drawInterface(0, x + 110, RSInterface.interfaceCache[54000], y + 38, false);
            setGrandExchange(id, false);
            changeSet(id, true, false);
            drawPercentage(id);
            //small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            // small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            // small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            // setHovers(id, false);
            // if(SpriteCache.spriteCache[640] != null) {
            //RSInterface.imageLoader(640, "ge").drawSprite(x + 110, y + 39);
            //     if(mouseInRegion(x + 112, y + 38, x + 132, y + 60)) {
            //        RSInterface.imageLoader(641, "ge").drawSprite(x + 110, y + 39);
            //        changeSet(id, false, true);
            //    }
            // }
            // write the item name
        } else if (type == "Finished Buying") {
            if (super.mouseX >= x && super.mouseX <= x + 140
                    && super.mouseY >= y && super.mouseY <= y + 110
                    && !MiniMenu.open) {
                if (BuyHover656 != null)
                    BuyHover656.drawSprite(x, y);
            } else {
            	geSprite29.drawSprite(x, y);
            }
            geSprite678.drawSprite(x + 6, y + 30);

            if (slotItems[id] > 0) {
                Sprite itemSprite = ItemDefinition.getSprite(slotItems[id], slotQuantity[id], 1, 3153952, false, false);
                if (itemSprite != null) {
                    itemSprite.drawSprite(x + 9, y + 32);
                }
            }

            drawInterface(0, x + 110, RSInterface.interfaceCache[54000], y + 38, false);
            setGrandExchange(id, false);
            changeSet(id, true, false);
            drawPercentage(id);
            // small.method592(0xCC9900, x2, RSInterface.interfaceCache[32000 + id].message, y2 - minus, true);
            // small.method592(0xBDBB5B, x2, RSInterface.interfaceCache[33000 + id].message, y2, true);
            // small.method592(0xFFFF00, x3, RSInterface.interfaceCache[33100 + id].message, y3, true);
            //setHovers(id, false);
            // if(SpriteCache.spriteCache[640] != null) {
            //RSInterface.imageLoader(640, "ge").drawSprite(x + 110, y + 39);
            //    if(mouseInRegion(x + 112, y + 38, x + 132, y + 60)) {
            //        RSInterface.imageLoader(641, "ge").drawSprite(x + 110, y + 39);
            //        changeSet(id, false, true);
            //    }
            // }
            // write the item name
        }
    }

    public void drawPercentage(int id) {
        int x = 0;
        int y = 0;
        boolean fixed = (clientSize == 0);
        switch (id) {
            case 1:
                x = fixed ? 30 + 8 : (frameWidth / 2 - 226 + 8);
                y = fixed ? 74 + 81 : (frameHeight / 2 - 93 + 81);
                break;
            case 2:
                x = fixed ? 186 + 8 : (frameWidth / 2 - 70 + 8);
                y = fixed ? 74 + 81 : (frameHeight / 2 - 93 + 81);
                break;
            case 3:
                x = fixed ? 342 + 8 : (frameWidth / 2 + 86 + 8);
                y = fixed ? 74 + 81 : (frameHeight / 2 - 93 + 81);
                break;
            case 4:
                x = fixed ? 30 + 8 : (frameWidth / 2 - 226 + 8);
                y = fixed ? 194 + 81 : (frameHeight / 2 + 27 + 81);
                break;
            case 5:
                x = fixed ? 186 + 8 : (frameWidth / 2 - 70 + 8);
                y = fixed ? 194 + 81 : (frameHeight / 2 + 27 + 81);
                break;
            case 6:
                x = fixed ? 342 + 8 : (frameWidth / 2 + 86 + 8);
                y = fixed ? 194 + 81 : (frameHeight / 2 + 27 + 81);
                break;
        }
        x -= 2;
        if (slotColorPercent[id] > 100) {
            slotColorPercent[id] = 100;
        }
        int s = 0;
        if (!slotAborted[id]) {
            for (int k9 = 1; k9 < slotColorPercent[id]; k9++) {
                if (slotColorPercent[id] > 0) {
                    if (k9 == 1) {
                        if (per0 != null)
                            per0.drawSprite(x, y);
                        x += 2;
                    } else if (k9 == 2) {
                        if (per1 != null)
                            per1.drawSprite(x, y);
                        x += 2;
                    } else if (k9 >= 6 && k9 <= 14) {
                        if (per3 != null)
                            per3.drawSprite(x, y);
                        x += 1;
                    } else if (k9 >= 56 && k9 <= 65) {
                        if (per3 != null)
                            per3.drawSprite(x, y);
                        x += 1;
                    } else if (k9 >= 76 && k9 <= 82) {
                        if (per3 != null)
                            per3.drawSprite(x, y);
                        x += 1;
                    } else {
                        if (s == 0) {
                            if (per2 != null)
                                per2.drawSprite(x, y);
                            x += 2;
                            s += 1;
                        } else if (s == 1) {
                            if (per3 != null)
                                per3.drawSprite(x, y);
                            x += 1;
                            s += 1;
                        } else if (s == 2) {
                            if (per3 != null)
                                per3.drawSprite(x, y);
                            x += 1;
                            s = 0;
                        } else if (s == 4) {
                            if (per3 != null)
                                per3.drawSprite(x, y);
                            x += 1;
                            s = 0;
                        }
                    }
                }
            }
        } else {
            if (abort1 != null)
                abort1.drawSprite(x, y);
        }
    }

    public void setGrandExchange(int id, boolean on) {
        switch (id) {
            case 1:
                if (on) {
                    RSInterface.interfaceCache[24505].tooltip = "Buy";
                    RSInterface.interfaceCache[24511].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24505].tooltip = null;
                    RSInterface.interfaceCache[24511].tooltip = null;
                }
                break;
            case 2:
                if (on) {
                    RSInterface.interfaceCache[24523].tooltip = "Buy";
                    RSInterface.interfaceCache[24526].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24523].tooltip = null;
                    RSInterface.interfaceCache[24526].tooltip = null;
                }
                break;
            case 3:
                if (on) {
                    RSInterface.interfaceCache[24514].tooltip = "Buy";
                    RSInterface.interfaceCache[24529].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24514].tooltip = null;
                    RSInterface.interfaceCache[24529].tooltip = null;
                }
                break;
            case 4:
                if (on) {
                    RSInterface.interfaceCache[24508].tooltip = "Buy";
                    RSInterface.interfaceCache[24532].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24508].tooltip = null;
                    RSInterface.interfaceCache[24532].tooltip = null;
                }
                break;
            case 5:
                if (on) {
                    RSInterface.interfaceCache[24517].tooltip = "Buy";
                    RSInterface.interfaceCache[24535].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24517].tooltip = null;
                    RSInterface.interfaceCache[24535].tooltip = null;
                }
                break;
            case 6:
                if (on) {
                    RSInterface.interfaceCache[24520].tooltip = "Buy";
                    RSInterface.interfaceCache[24538].tooltip = "Sell";
                    changeSet(id, false, false);
                } else {
                    RSInterface.interfaceCache[24520].tooltip = null;
                    RSInterface.interfaceCache[24538].tooltip = null;
                }
                break;
        }
    }

    public void changeSet(int id, boolean view, boolean abort) {
        switch (id) {
            case 1:
                if (view) {
                    RSInterface.interfaceCache[24543].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24543].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24541].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24541].tooltip = null;
                }
                break;
            case 2:
                if (view) {
                    RSInterface.interfaceCache[24547].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24547].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24545].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24545].tooltip = null;
                }
                break;
            case 3:
                if (view) {
                    RSInterface.interfaceCache[24551].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24551].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24549].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24549].tooltip = null;
                }
                break;
            case 4:
                if (view) {
                    RSInterface.interfaceCache[24555].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24555].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24553].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24553].tooltip = null;
                }
                break;
            case 5:
                if (view) {
                    RSInterface.interfaceCache[24559].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24559].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24557].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24557].tooltip = null;
                }
                break;
            case 6:
                if (view) {
                    RSInterface.interfaceCache[24563].tooltip = "View offer";
                } else {
                    RSInterface.interfaceCache[24563].tooltip = null;
                }
                if (abort) {
                    RSInterface.interfaceCache[24561].tooltip = "Abort offer";
                } else {
                    RSInterface.interfaceCache[24561].tooltip = null;
                }
                break;
        }
    }

    public String capitalizeFirstChar(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static int getRegionId() {
        int localX = anInt1069 / 8;
        int localY = anInt1070 / 8;
        int id = (localX << 8) + localY;
        return id;
    }

}