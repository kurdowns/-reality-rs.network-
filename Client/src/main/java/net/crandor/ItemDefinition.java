package net.crandor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;

public final class ItemDefinition extends NodeSub {

    ///75436
    public static final String[] SKILL_NAMES = {
            "Attack",
            "Defence",
            "Strength",
            "Hitpoints",
            "Ranged",
            "Prayer",
            "Magic",
            "Cooking",
            "Woodcutting",
            "Fletching",
            "Fishing",
            "Firemaking",
            "Crafting",
            "Smithing",
            "Mining",
            "Herblore",
            "Agility",
            "Thieving",
            "Slayer",
            "Farming",
            "Runecrafting",
            "Construction",
            "Hunter",
            "Summoning",
            "Dungeoneering"
    };

    private static final int[][] ITEMS_742 = {
            {25107, 22950}, // Empty pressure flask (a)
            {25108, 22951}, // Empty pressure flask (b)
            {25109, 22952}, // Full pressure flask (a)
            {25110, 22953}, // Full pressure flask (b)
            {25111, 23131}, // Juju mining flask (6)
            {25112, 23132}, // Juju mining flask (5)
            {25113, 23133}, // Juju mining flask (4)
            {25114, 23134}, // Juju mining flask (3)
            {25115, 23135}, // Juju mining flask (2)
            {25116, 23136}, // Juju mining flask (1)
            {25117, 23137}, // Juju cooking flask (6)
            {25118, 23138}, // Juju cooking flask (5)
            {25119, 23139}, // Juju cooking flask (4)
            {25120, 23140}, // Juju cooking flask (3)
            {25121, 23141}, // Juju cooking flask (2)
            {25122, 23142}, // Juju cooking flask (1)
            {25123, 23143}, // Juju farming flask (6)
            {25124, 23144}, // Juju farming flask (5)
            {25125, 23145}, // Juju farming flask (4)
            {25126, 23146}, // Juju farming flask (3)
            {25127, 23147}, // Juju farming flask (2)
            {25128, 23148}, // Juju farming flask (1)
            {25129, 23149}, // Juju woodcutting flask (6)
            {25130, 23150}, // Juju woodcutting flask (5)
            {25131, 23151}, // Juju woodcutting flask (4)
            {25132, 23152}, // Juju woodcutting flask (3)
            {25133, 23153}, // Juju woodcutting flask (2)
            {25134, 23154}, // Juju woodcutting flask (1)
            {25135, 23155}, // Juju fishing flask (6)
            {25136, 23156}, // Juju fishing flask (5)
            {25137, 23157}, // Juju fishing flask (4)
            {25138, 23158}, // Juju fishing flask (3)
            {25139, 23159}, // Juju fishing flask (2)
            {25140, 23160}, // Juju fishing flask (1)
            {25141, 23161}, // Juju hunter flask (6)
            {25142, 23162}, // Juju hunter flask (5)
            {25143, 23163}, // Juju hunter flask (4)
            {25144, 23164}, // Juju hunter flask (3)
            {25145, 23165}, // Juju hunter flask (2)
            {25146, 23166}, // Juju hunter flask (1)
            {25147, 23167}, // Scentless flask (6)
            {25148, 23168}, // Scentless flask (5)
            {25149, 23169}, // Scentless flask (4)
            {25150, 23170}, // Scentless flask (3)
            {25151, 23171}, // Scentless flask (2)
            {25152, 23172}, // Scentless flask (1)
            {25153, 23173}, // Saradomin's blessing flask (6)
            {25154, 23174}, // Saradomin's blessing flask (5)
            {25155, 23175}, // Saradomin's blessing flask (4)
            {25156, 23176}, // Saradomin's blessing flask (3)
            {25157, 23177}, // Saradomin's blessing flask (2)
            {25158, 23178}, // Saradomin's blessing flask (1)
            {25159, 23179}, // Guthix's gift flask (6)
            {25160, 23180}, // Guthix's gift flask (5)
            {25161, 23181}, // Guthix's gift flask (4)
            {25162, 23182}, // Guthix's gift flask (3)
            {25163, 23183}, // Guthix's gift flask (2)
            {25164, 23184}, // Guthix's gift flask (1)
            {25165, 23185}, // Zamorak's favour flask (6)
            {25166, 23186}, // Zamorak's favour flask (5)
            {25167, 23187}, // Zamorak's favour flask (4)
            {25168, 23188}, // Zamorak's favour flask (3)
            {25169, 23189}, // Zamorak's favour flask (2)
            {25170, 23190}, // Zamorak's favour flask (1)
            {25171, 23195}, // Attack flask (6)
            {25172, 23196}, // Attack flask (6)
            {25173, 23197}, // Attack flask (5)
            {25174, 23198}, // Attack flask (5)
            {25175, 23199}, // Attack flask (4)
            {25176, 23200}, // Attack flask (4)
            {25177, 23201}, // Attack flask (3)
            {25178, 23202}, // Attack flask (3)
            {25179, 23203}, // Attack flask (2)
            {25180, 23204}, // Attack flask (2)
            {25181, 23205}, // Attack flask (1)
            {25182, 23206}, // Attack flask (1)
            {25183, 23207}, // Strength flask (6)
            {25184, 23208}, // Strength flask (6)
            {25185, 23209}, // Strength flask (5)
            {25186, 23210}, // Strength flask (5)
            {25187, 23211}, // Strength flask (4)
            {25188, 23212}, // Strength flask (4)
            {25189, 23213}, // Strength flask (3)
            {25190, 23214}, // Strength flask (3)
            {25191, 23215}, // Strength flask (2)
            {25192, 23216}, // Strength flask (2)
            {25193, 23217}, // Strength flask (1)
            {25194, 23218}, // Strength flask (1)
            {25195, 23219}, // Restore flask (6)
            {25196, 23220}, // Restore flask (6)
            {25197, 23221}, // Restore flask (5)
            {25198, 23222}, // Restore flask (5)
            {25199, 23223}, // Restore flask (4)
            {25200, 23224}, // Restore flask (4)
            {25201, 23225}, // Restore flask (3)
            {25202, 23226}, // Restore flask (3)
            {25203, 23227}, // Restore flask (2)
            {25204, 23228}, // Restore flask (2)
            {25205, 23229}, // Restore flask (1)
            {25206, 23230}, // Restore flask (1)
            {25207, 23231}, // Defence flask (6)
            {25208, 23232}, // Defence flask (6)
            {25209, 23233}, // Defence flask (5)
            {25210, 23234}, // Defence flask (5)
            {25211, 23235}, // Defence flask (4)
            {25212, 23236}, // Defence flask (4)
            {25213, 23237}, // Defence flask (3)
            {25214, 23238}, // Defence flask (3)
            {25215, 23239}, // Defence flask (2)
            {25216, 23240}, // Defence flask (2)
            {25217, 23241}, // Defence flask (1)
            {25218, 23242}, // Defence flask (1)
            {25219, 23243}, // Prayer flask (6)
            {25220, 23244}, // Prayer flask (6)
            {25221, 23245}, // Prayer flask (5)
            {25222, 23246}, // Prayer flask (5)
            {25223, 23247}, // Prayer flask (4)
            {25224, 23248}, // Prayer flask (4)
            {25225, 23249}, // Prayer flask (3)
            {25226, 23250}, // Prayer flask (3)
            {25227, 23251}, // Prayer flask (2)
            {25228, 23252}, // Prayer flask (2)
            {25229, 23253}, // Prayer flask (1)
            {25230, 23254}, // Prayer flask (1)
            {25231, 23255}, // Super attack flask (6)
            {25232, 23256}, // Super attack flask (6)
            {25233, 23257}, // Super attack flask (5)
            {25234, 23258}, // Super attack flask (5)
            {25235, 23259}, // Super attack flask (4)
            {25236, 23260}, // Super attack flask (4)
            {25237, 23261}, // Super attack flask (3)
            {25238, 23262}, // Super attack flask (3)
            {25239, 23263}, // Super attack flask (2)
            {25240, 23264}, // Super attack flask (2)
            {25241, 23265}, // Super attack flask (1)
            {25242, 23266}, // Super attack flask (1)
            {25243, 23267}, // Fishing flask (6)
            {25244, 23268}, // Fishing flask (6)
            {25245, 23269}, // Fishing flask (5)
            {25246, 23270}, // Fishing flask (5)
            {25247, 23271}, // Fishing flask (4)
            {25248, 23272}, // Fishing flask (4)
            {25249, 23273}, // Fishing flask (3)
            {25250, 23274}, // Fishing flask (3)
            {25251, 23275}, // Fishing flask (2)
            {25252, 23276}, // Fishing flask (2)
            {25253, 23277}, // Fishing flask (1)
            {25254, 23278}, // Fishing flask (1)
            {25255, 23279}, // Super strength flask (6)
            {25256, 23280}, // Super strength flask (6)
            {25257, 23281}, // Super strength flask (5)
            {25258, 23282}, // Super strength flask (5)
            {25259, 23283}, // Super strength flask (4)
            {25260, 23284}, // Super strength flask (4)
            {25261, 23285}, // Super strength flask (3)
            {25262, 23286}, // Super strength flask (3)
            {25263, 23287}, // Super strength flask (2)
            {25264, 23288}, // Super strength flask (2)
            {25265, 23289}, // Super strength flask (1)
            {25266, 23290}, // Super strength flask (1)
            {25267, 23291}, // Super defence flask (6)
            {25268, 23292}, // Super defence flask (6)
            {25269, 23293}, // Super defence flask (5)
            {25270, 23294}, // Super defence flask (5)
            {25271, 23295}, // Super defence flask (4)
            {25272, 23296}, // Super defence flask (4)
            {25273, 23297}, // Super defence flask (3)
            {25274, 23298}, // Super defence flask (3)
            {25275, 23299}, // Super defence flask (2)
            {25276, 23300}, // Super defence flask (2)
            {25277, 23301}, // Super defence flask (1)
            {25278, 23302}, // Super defence flask (1)
            {25279, 23303}, // Ranging flask (6)
            {25280, 23304}, // Ranging flask (6)
            {25281, 23305}, // Ranging flask (5)
            {25282, 23306}, // Ranging flask (5)
            {25283, 23307}, // Ranging flask (4)
            {25284, 23308}, // Ranging flask (4)
            {25285, 23309}, // Ranging flask (3)
            {25286, 23310}, // Ranging flask (3)
            {25287, 23311}, // Ranging flask (2)
            {25288, 23312}, // Ranging flask (2)
            {25289, 23313}, // Ranging flask (1)
            {25290, 23314}, // Ranging flask (1)
            {25291, 23315}, // Antipoison flask (6)
            {25292, 23316}, // Antipoison flask (6)
            {25293, 23317}, // Antipoison flask (5)
            {25294, 23318}, // Antipoison flask (5)
            {25295, 23319}, // Antipoison flask (4)
            {25296, 23320}, // Antipoison flask (4)
            {25297, 23321}, // Antipoison flask (3)
            {25298, 23322}, // Antipoison flask (3)
            {25299, 23323}, // Antipoison flask (2)
            {25300, 23324}, // Antipoison flask (2)
            {25301, 23325}, // Antipoison flask (1)
            {25302, 23326}, // Antipoison flask (1)
            {25303, 23327}, // Super antipoison flask (6)
            {25304, 23328}, // Super antipoison flask (6)
            {25305, 23329}, // Super antipoison flask (5)
            {25306, 23330}, // Super antipoison flask (5)
            {25307, 23331}, // Super antipoison flask (4)
            {25308, 23332}, // Super antipoison flask (4)
            {25309, 23333}, // Super antipoison flask (3)
            {25310, 23334}, // Super antipoison flask (3)
            {25311, 23335}, // Super antipoison flask (2)
            {25312, 23336}, // Super antipoison flask (2)
            {25313, 23337}, // Super antipoison flask (1)
            {25314, 23338}, // Super antipoison flask (1)
            {25315, 23339}, // Zamorak brew flask (6)
            {25316, 23340}, // Zamorak brew flask (6)
            {25317, 23341}, // Zamorak brew flask (5)
            {25318, 23342}, // Zamorak brew flask (5)
            {25319, 23343}, // Zamorak brew flask (4)
            {25320, 23344}, // Zamorak brew flask (4)
            {25321, 23345}, // Zamorak brew flask (3)
            {25322, 23346}, // Zamorak brew flask (3)
            {25323, 23347}, // Zamorak brew flask (2)
            {25324, 23348}, // Zamorak brew flask (2)
            {25325, 23349}, // Zamorak brew flask (1)
            {25326, 23350}, // Zamorak brew flask (1)
            {25327, 23351}, // Saradomin brew flask (6)
            {25328, 23352}, // Saradomin brew flask (6)
            {25329, 23353}, // Saradomin brew flask (5)
            {25330, 23354}, // Saradomin brew flask (5)
            {25331, 23355}, // Saradomin brew flask (4)
            {25332, 23356}, // Saradomin brew flask (4)
            {25333, 23357}, // Saradomin brew flask (3)
            {25334, 23358}, // Saradomin brew flask (3)
            {25335, 23359}, // Saradomin brew flask (2)
            {25336, 23360}, // Saradomin brew flask (2)
            {25337, 23361}, // Saradomin brew flask (1)
            {25338, 23362}, // Saradomin brew flask (1)
            {25339, 23363}, // Antifire flask (6)
            {25340, 23364}, // Antifire flask (6)
            {25341, 23365}, // Antifire flask (5)
            {25342, 23366}, // Antifire flask (5)
            {25343, 23367}, // Antifire flask (4)
            {25344, 23368}, // Antifire flask (4)
            {25345, 23369}, // Antifire flask (3)
            {25346, 23370}, // Antifire flask (3)
            {25347, 23371}, // Antifire flask (2)
            {25348, 23372}, // Antifire flask (2)
            {25349, 23373}, // Antifire flask (1)
            {25350, 23374}, // Antifire flask (1)
            {25351, 23375}, // Energy flask (6)
            {25352, 23376}, // Energy flask (6)
            {25353, 23377}, // Energy flask (5)
            {25354, 23378}, // Energy flask (5)
            {25355, 23379}, // Energy flask (4)
            {25356, 23380}, // Energy flask (4)
            {25357, 23381}, // Energy flask (3)
            {25358, 23382}, // Energy flask (3)
            {25359, 23383}, // Energy flask (2)
            {25360, 23384}, // Energy flask (2)
            {25361, 23385}, // Energy flask (1)
            {25362, 23386}, // Energy flask (1)
            {25363, 23387}, // Super energy flask (6)
            {25364, 23388}, // Super energy flask (6)
            {25365, 23389}, // Super energy flask (5)
            {25366, 23390}, // Super energy flask (5)
            {25367, 23391}, // Super energy flask (4)
            {25368, 23392}, // Super energy flask (4)
            {25369, 23393}, // Super energy flask (3)
            {25370, 23394}, // Super energy flask (3)
            {25371, 23395}, // Super energy flask (2)
            {25372, 23396}, // Super energy flask (2)
            {25373, 23397}, // Super energy flask (1)
            {25374, 23398}, // Super energy flask (1)
            {25375, 23399}, // Super restore flask (6)
            {25376, 23400}, // Super restore flask (6)
            {25377, 23401}, // Super restore flask (5)
            {25378, 23402}, // Super restore flask (5)
            {25379, 23403}, // Super restore flask (4)
            {25380, 23404}, // Super restore flask (4)
            {25381, 23405}, // Super restore flask (3)
            {25382, 23406}, // Super restore flask (3)
            {25383, 23407}, // Super restore flask (2)
            {25384, 23408}, // Super restore flask (2)
            {25385, 23409}, // Super restore flask (1)
            {25386, 23410}, // Super restore flask (1)
            {25387, 23411}, // Agility flask (6)
            {25388, 23412}, // Agility flask (6)
            {25389, 23413}, // Agility flask (5)
            {25390, 23414}, // Agility flask (5)
            {25391, 23415}, // Agility flask (4)
            {25392, 23416}, // Agility flask (4)
            {25393, 23417}, // Agility flask (3)
            {25394, 23418}, // Agility flask (3)
            {25395, 23419}, // Agility flask (2)
            {25396, 23420}, // Agility flask (2)
            {25397, 23421}, // Agility flask (1)
            {25398, 23422}, // Agility flask (1)
            {25399, 23423}, // Magic flask (6)
            {25400, 23424}, // Magic flask (6)
            {25401, 23425}, // Magic flask (5)
            {25402, 23426}, // Magic flask (5)
            {25403, 23427}, // Magic flask (4)
            {25404, 23428}, // Magic flask (4)
            {25405, 23429}, // Magic flask (3)
            {25406, 23430}, // Magic flask (3)
            {25407, 23431}, // Magic flask (2)
            {25408, 23432}, // Magic flask (2)
            {25409, 23433}, // Magic flask (1)
            {25410, 23434}, // Magic flask (1)
            {25411, 23435}, // Hunter flask (6)
            {25412, 23436}, // Hunter flask (6)
            {25413, 23437}, // Hunter flask (5)
            {25414, 23438}, // Hunter flask (5)
            {25415, 23439}, // Hunter flask (4)
            {25416, 23440}, // Hunter flask (4)
            {25417, 23441}, // Hunter flask (3)
            {25418, 23442}, // Hunter flask (3)
            {25419, 23443}, // Hunter flask (2)
            {25420, 23444}, // Hunter flask (2)
            {25421, 23445}, // Hunter flask (1)
            {25422, 23446}, // Hunter flask (1)
            {25423, 23447}, // Combat flask (6)
            {25424, 23448}, // Combat flask (6)
            {25425, 23449}, // Combat flask (5)
            {25426, 23450}, // Combat flask (5)
            {25427, 23451}, // Combat flask (4)
            {25428, 23452}, // Combat flask (4)
            {25429, 23453}, // Combat flask (3)
            {25430, 23454}, // Combat flask (3)
            {25431, 23455}, // Combat flask (2)
            {25432, 23456}, // Combat flask (2)
            {25433, 23457}, // Combat flask (1)
            {25434, 23458}, // Combat flask (1)
            {25435, 23459}, // Crafting flask (6)
            {25436, 23460}, // Crafting flask (6)
            {25437, 23461}, // Crafting flask (5)
            {25438, 23462}, // Crafting flask (5)
            {25439, 23463}, // Crafting flask (4)
            {25440, 23464}, // Crafting flask (4)
            {25441, 23465}, // Crafting flask (3)
            {25442, 23466}, // Crafting flask (3)
            {25443, 23467}, // Crafting flask (2)
            {25444, 23468}, // Crafting flask (2)
            {25445, 23469}, // Crafting flask (1)
            {25446, 23470}, // Crafting flask (1)
            {25447, 23471}, // Fletching flask (6)
            {25448, 23472}, // Fletching flask (6)
            {25449, 23473}, // Fletching flask (5)
            {25450, 23474}, // Fletching flask (5)
            {25451, 23475}, // Fletching flask (4)
            {25452, 23476}, // Fletching flask (4)
            {25453, 23477}, // Fletching flask (3)
            {25454, 23478}, // Fletching flask (3)
            {25455, 23479}, // Fletching flask (2)
            {25456, 23480}, // Fletching flask (2)
            {25457, 23481}, // Fletching flask (1)
            {25458, 23482}, // Fletching flask (1)
            {25459, 23483}, // Recover special flask (6)
            {25460, 23484}, // Recover special flask (5)
            {25461, 23485}, // Recover special flask (4)
            {25462, 23486}, // Recover special flask (3)
            {25463, 23487}, // Recover special flask (2)
            {25464, 23488}, // Recover special flask (1)
            {25465, 23489}, // Super antifire flask (6)
            {25466, 23490}, // Super antifire flask (5)
            {25467, 23491}, // Super antifire flask (4)
            {25468, 23492}, // Super antifire flask (3)
            {25469, 23493}, // Super antifire flask (2)
            {25470, 23494}, // Super antifire flask (1)
            {25471, 23495}, // Extreme attack flask (6)
            {25472, 23496}, // Extreme attack flask (5)
            {25473, 23497}, // Extreme attack flask (4)
            {25474, 23498}, // Extreme attack flask (3)
            {25475, 23499}, // Extreme attack flask (2)
            {25476, 23500}, // Extreme attack flask (1)
            {25477, 23501}, // Extreme strength flask (6)
            {25478, 23502}, // Extreme strength flask (5)
            {25479, 23503}, // Extreme strength flask (4)
            {25480, 23504}, // Extreme strength flask (3)
            {25481, 23505}, // Extreme strength flask (2)
            {25482, 23506}, // Extreme strength flask (1)
            {25483, 23507}, // Extreme defence flask (6)
            {25484, 23508}, // Extreme defence flask (5)
            {25485, 23509}, // Extreme defence flask (4)
            {25486, 23510}, // Extreme defence flask (3)
            {25487, 23511}, // Extreme defence flask (2)
            {25488, 23512}, // Extreme defence flask (1)
            {25489, 23513}, // Extreme magic flask (6)
            {25490, 23514}, // Extreme magic flask (5)
            {25491, 23515}, // Extreme magic flask (4)
            {25492, 23516}, // Extreme magic flask (3)
            {25493, 23517}, // Extreme magic flask (2)
            {25494, 23518}, // Extreme magic flask (1)
            {25495, 23519}, // Extreme ranging flask (6)
            {25496, 23520}, // Extreme ranging flask (5)
            {25497, 23521}, // Extreme ranging flask (4)
            {25498, 23522}, // Extreme ranging flask (3)
            {25499, 23523}, // Extreme ranging flask (2)
            {25500, 23524}, // Extreme ranging flask (1)
            {25501, 23525}, // Super prayer flask (6)
            {25502, 23526}, // Super prayer flask (5)
            {25503, 23527}, // Super prayer flask (4)
            {25504, 23528}, // Super prayer flask (3)
            {25505, 23529}, // Super prayer flask (2)
            {25506, 23530}, // Super prayer flask (1)
            {25507, 23531}, // Overload flask (6)
            {25508, 23532}, // Overload flask (5)
            {25509, 23533}, // Overload flask (4)
            {25510, 23534}, // Overload flask (3)
            {25511, 23535}, // Overload flask (2)
            {25512, 23536}, // Overload flask (1)
            {25513, 23537}, // Relicym's balm flask (6)
            {25514, 23538}, // Relicym's balm flask (6)
            {25515, 23539}, // Relicym's balm flask (5)
            {25516, 23540}, // Relicym's balm flask (5)
            {25517, 23541}, // Relicym's balm flask (4)
            {25518, 23542}, // Relicym's balm flask (4)
            {25519, 23543}, // Relicym's balm flask (3)
            {25520, 23544}, // Relicym's balm flask (3)
            {25521, 23545}, // Relicym's balm flask (2)
            {25522, 23546}, // Relicym's balm flask (2)
            {25523, 23547}, // Relicym's balm flask (1)
            {25524, 23548}, // Relicym's balm flask (1)
            {25525, 23549}, // Serum 207 flask (6)
            {25526, 23550}, // Serum 207 flask (5)
            {25527, 23551}, // Serum 207 flask (4)
            {25528, 23552}, // Serum 207 flask (3)
            {25529, 23553}, // Serum 207 flask (2)
            {25530, 23554}, // Serum 207 flask (1)
            {25531, 23555}, // Guthix balance flask (6)
            {25532, 23556}, // Guthix balance flask (6)
            {25533, 23557}, // Guthix balance flask (5)
            {25534, 23558}, // Guthix balance flask (5)
            {25535, 23559}, // Guthix balance flask (4)
            {25536, 23560}, // Guthix balance flask (4)
            {25537, 23561}, // Guthix balance flask (3)
            {25538, 23562}, // Guthix balance flask (3)
            {25539, 23563}, // Guthix balance flask (2)
            {25540, 23564}, // Guthix balance flask (2)
            {25541, 23565}, // Guthix balance flask (1)
            {25542, 23566}, // Guthix balance flask (1)
            {25543, 23567}, // Sanfew serum flask (6)
            {25544, 23568}, // Sanfew serum flask (6)
            {25545, 23569}, // Sanfew serum flask (5)
            {25546, 23570}, // Sanfew serum flask (5)
            {25547, 23571}, // Sanfew serum flask (4)
            {25548, 23572}, // Sanfew serum flask (4)
            {25549, 23573}, // Sanfew serum flask (3)
            {25550, 23574}, // Sanfew serum flask (3)
            {25551, 23575}, // Sanfew serum flask (2)
            {25552, 23576}, // Sanfew serum flask (2)
            {25553, 23577}, // Sanfew serum flask (1)
            {25554, 23578}, // Sanfew serum flask (1)
            {25555, 23579}, // Antipoison+ flask (6)
            {25556, 23580}, // Antipoison+ flask (6)
            {25557, 23581}, // Antipoison+ flask (5)
            {25558, 23582}, // Antipoison+ flask (5)
            {25559, 23583}, // Antipoison+ flask (4)
            {25560, 23584}, // Antipoison+ flask (4)
            {25561, 23585}, // Antipoison+ flask (3)
            {25562, 23586}, // Antipoison+ flask (3)
            {25563, 23587}, // Antipoison+ flask (2)
            {25564, 23588}, // Antipoison+ flask (2)
            {25565, 23589}, // Antipoison+ flask (1)
            {25566, 23590}, // Antipoison+ flask (1)
            {25567, 23591}, // Antipoison++ flask (6)
            {25568, 23592}, // Antipoison++ flask (6)
            {25569, 23593}, // Antipoison++ flask (5)
            {25570, 23594}, // Antipoison++ flask (5)
            {25571, 23595}, // Antipoison++ flask (4)
            {25572, 23596}, // Antipoison++ flask (4)
            {25573, 23597}, // Antipoison++ flask (3)
            {25574, 23598}, // Antipoison++ flask (3)
            {25575, 23599}, // Antipoison++ flask (2)
            {25576, 23600}, // Antipoison++ flask (2)
            {25577, 23601}, // Antipoison++ flask (1)
            {25578, 23602}, // Antipoison++ flask (1)
            {25579, 23603}, // Serum 208 flask (6)
            {25580, 23604}, // Serum 208 flask (5)
            {25581, 23605}, // Serum 208 flask (4)
            {25582, 23606}, // Serum 208 flask (3)
            {25583, 23607}, // Serum 208 flask (2)
            {25584, 23608}, // Serum 208 flask (1)
            {25585, 23609}, // Prayer renewal flask (6)
            {25586, 23610}, // Prayer renewal flask (6)
            {25587, 23611}, // Prayer renewal flask (5)
            {25588, 23612}, // Prayer renewal flask (5)
            {25589, 23613}, // Prayer renewal flask (4)
            {25590, 23614}, // Prayer renewal flask (4)
            {25591, 23615}, // Prayer renewal flask (3)
            {25592, 23616}, // Prayer renewal flask (3)
            {25593, 23617}, // Prayer renewal flask (2)
            {25594, 23618}, // Prayer renewal flask (2)
            {25595, 23619}, // Prayer renewal flask (1)
            {25596, 23620}, // Prayer renewal flask (1)
            {25597, 23621}, // Summoning flask (6)
            {25598, 23622}, // Summoning flask (6)
            {25599, 23623}, // Summoning flask (5)
            {25600, 23624}, // Summoning flask (5)
            {25601, 23625}, // Summoning flask (4)
            {25602, 23626}, // Summoning flask (4)
            {25603, 23627}, // Summoning flask (3)
            {25604, 23628}, // Summoning flask (3)
            {25605, 23629}, // Summoning flask (2)
            {25606, 23630}, // Summoning flask (2)
            {25607, 23631}, // Summoning flask (1)
            {25608, 23632}, // Summoning flask (1)
            {25609, 23633}, // Magic essence flask (6)
            {25610, 23634}, // Magic essence flask (5)
            {25611, 23635}, // Magic essence flask (4)
            {25612, 23636}, // Magic essence flask (3)
            {25613, 23637}, // Magic essence flask (2)
            {25614, 23638}, // Magic essence flask (1)
            {25615, 23191}, // Potion flask
            {25616, 23192}, // Potion flask
            {25617, 23194}, // Red standstone
            {25618, 23193}, // robust glass
    };

    private static final int[][] OSRS_ITEMS = {
            {25036, 12922}, // Tanzanite fang
            {25037, 12923}, // Tanzanite fang
            {25038, 12924}, // Toxic blowpipe (empty)
            {25039, 12925}, // Toxic blowpipe (empty)
            {25040, 12926}, // Toxic blowpipe
            {25041, 12927}, // Serpentine visage
            {25042, 12928}, // Serpentine visage
            {25043, 12929}, // Serpentine helm
            {25044, 12930}, // Serpentine helm
            {25045, 12931}, // Serpentine helm
            {25046, 12932}, // Magic fang
            {25047, 12933}, // Magic fang
            {25048, 12934}, // Zulrah's scales
            {25049, 12902}, // Toxic staff (uncharged)
            {25050, 12903}, // Toxic staff (uncharged)
            {25051, 12904}, // Toxic staff of the dead
            {25053, 13576}, // dragon warhammer
            {25054, 13577}, // dragon warhammer noted
            {25055, 11995}, // Pet chaos elemental
            {25056, 12643}, // Pet dagannoth supreme
            {25057, 12644}, // Pet dagannoth prime
            {25058, 12645}, // Pet dagannoth rex
            {25059, 12648}, // Pet smoke devil
            {25060, 12649}, // Pet kree'arra
            {25061, 12650}, // Pet general graardor
            {25062, 12651}, // Pet zilyana
            {25063, 12652}, // Pet k'ril tsutsaroth
            {25064, 12653}, // Prince black dragon
            {25065, 12654}, // Kalphite princess
            {25066, 12655}, // Pet Kraken
            {25067, 12656}, // Baby mole
            {25068, 12657}, // Pet penance queen
            {25069, 12816}, // Pet dark core
            {25070, 12921}, // Pet snakeling
            {25071, 13262}, // Abyssal orphan
            {25072, 13178}, // Callisto cub
            {25073, 13247}, // Hell puppy
            {25074, 13225}, // Tzrek-jad
            {25075, 13181}, // Scorpia's offspring
            {25076, 13177}, // Venenatis spiderline
            {25077, 13179}, // Vet'ion jr.
            {25078, 13323}, // Baby chinchompa
            {25079, 13324}, // Baby chinchompa
            {25080, 13325}, // Baby chinchompa
            {25081, 13326}, // Baby chinchompa
            {25082, 13322}, // Beaver
            {25083, 13320}, // heron
            {25084, 13321}, // rock golem
            {25094, 11905}, // Trident of the seas (full)
            {25095, 11906}, // Trident of the seas (full)
            {25096, 11907}, // Trident of the seas stack
            {25097, 11908}, // Uncharged trident stack
            {25098, 11909}, // Uncharged trident stack
            {25099, 12899}, // charged trident of swamp
            {25100, 12900}, // un charged
            {25101, 12901}, // un charged noted
            {25102, 12691}, // Tyrannical ring (i)
            {25103, 12692}, // Treasonous ring
            {25104, 12002}, // Occult necklace
            {25105, 12601}, // ing of the gods (i)
            {25106, 11785}, // armadyl crossbow
            {25619, 20659}, // Giant squirrel
            {25620, 20661}, // Tangleroot
            {25621, 20663}, // Rocky
            {25622, 20665}, // Rift guardian
            {25623, 20667}, // Rift guardian
            {25624, 20669}, // Rift guardian
            {25625, 20671}, // Rift guardian
            {25626, 20673}, // Rift guardian
            {25627, 20675}, // Rift guardian
            {25628, 20677}, // Rift guardian
            {25629, 20679}, // Rift guardian
            {25630, 20681}, // Rift guardian
            {25631, 20683}, // Rift guardian
            {25632, 20685}, // Rift guardian
            {25633, 20687}, // Rift guardian
            {25634, 20689}, // Rift guardian
            {25635, 20691}, // Rift guardian
            {25636, 20693}, // Phoenix pet
            {25637, 20211}, // team cape
            {25638, 20214}, // team cape
            {25639, 20217}, // team cape
    };

    public static Sprite vine = Client.instance.cacheSprite[559];
    public static MRUNodes itemSpriteCache = new MRUNodes(100);
    public static MRUNodes modelCache = new MRUNodes(50);
    public static MRUNodes recentUse = new MRUNodes(64);
    public static boolean isMembers = true;
    public static Stream stream;
    public static int[] streamIndices;
    public static int totalItems = 30001;
    public static int[] osrsStreamIndices;
    public static Stream osrsStream;

    public static int[] item742StreamIndices;
    public static Stream item742Stream;

    public boolean oldschoolRs;
    public boolean item742;
    public byte equippedModelFemaleTranslateY;
    public int value;
    public short[] originalModelColors;
    public int id;
    public short[] newModelColors;
    public boolean membersItem;
    public int equippedModelFemale3;
    public int notedItemID;
    public int equippedModelFemale2;
    public int equippedModelMale1;
    public int maleDialogueModel2;
    public int modelScaleX;
    public String groundActions[];
    public int translateX;
    public String name;
    public int femaleDialogueModel2;
    public int inventoryModel;
    public int maleDialogueModel1;
    public boolean stackable;
    public String description;
    public int unNotedItemID;
    public int modelZoom;
    public int lightMagnitude;
    public int equippedModelMale3;
    public int equippedModelMale2;
    public String actions[];
    public int rotationX;
    public int modelScaleZ;
    public int modelScaleY;
    public int[] stackIDs;
    public int translateYZ;
    public int lightIntensity;
    public int femaleDialogueModel1;
    public int rotationY;
    public int equippedModelFemale1;
    public int[] stackAmounts;
    public int team;
    public int rotationZ;
    public byte equippedModelMaleTranslateY;
    public int lendID;
    public int lentItemID;

    public ItemDefinition() {
        id = -1;
    }

    public static void nullLoader() {
        modelCache = null;
        itemSpriteCache = null;
        streamIndices = null;
        stream = null;
    }

    /**
     * Dumps the item images for all items in the cache.
     *
     * @param dumpByName
     */
    public static void dumpItemImages(boolean dumpByName) {
        for (int id = 0; id < ItemDefinition.totalItems; id++) {
            Sprite image = ItemDefinition.getSprite(id, 10, 1, 0, false, false);
            if (image != null)
                dumpImage(
                        image,
                        dumpByName ? ItemDefinition.forID(id).name : Integer
                                .toString(id)
                );
        }
    }

    /**
     * Dumps a sprite with the specified name.
     *
     * @param image
     * @param name
     */
    public static void dumpImage(Sprite image, String name) {
        File directory = new File(signlink.findcachedir() + "rsimg/dump/");
        if (!directory.exists()) {
            directory.mkdir();
        }
        BufferedImage bi = new BufferedImage(image.myWidth, image.myHeight,
                BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, image.myWidth, image.myHeight, image.myPixels, 0,
                image.myWidth);
        Image img = makeColorTransparent(bi, new Color(0, 0, 0));
        BufferedImage trans = imageToBufferedImage(img);
        try {
            File out = new File(signlink.findcachedir() + "rsimg/dump/" + name
                    + ".png");
            ImageIO.write(trans, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Turns an Image into a BufferedImage.
     *
     * @param image
     * @return
     */
    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * Makes the specified color transparent in a buffered image.
     *
     * @param im
     * @param color
     * @return
     */
    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        RGBImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static void unpackConfig(StreamLoader streamLoader) {
        stream = new Stream(streamLoader.getDataForName("obj.dat"));
        Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
        int x = stream.readUnsignedWord();

        totalItems = 26000;

        System.out.println("634 Items Amount: " + totalItems);
        streamIndices = new int[totalItems];
        int i = 2;
        for (int j = 0; j < x; j++) {
            streamIndices[j] = i;
            i += stream.readUnsignedWord();
        }
    }

    public static void unpackOsrsConfig(StreamLoader streamLoader) {
        osrsStream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "itemsosrs.dat"));
        Stream stream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "itemsosrs.idx"));
        int x = stream.readUnsignedWord();

        System.out.println("osrs Items Amount: " + x);
        osrsStreamIndices = new int[x];
        int i = 2;
        for (int j = 0; j < x; j++) {
            osrsStreamIndices[j] = i;
            i += stream.readUnsignedWord();
        }

        /*for(i = 0; i < x; i++) {
            ItemDefinition def = forID(i);
            if(def != null && def.name != null) {
                System.out.println(i + " " + def.name);
            }
        }*/

        for (int index = 0; index < OSRS_ITEMS.length; index++) {

            /*ItemDefinition item = forID(OSRS_ITEMS[index][0]);
            if(item != null) {
                System.out.println(item.id + " " + item.name + " " + item.inventoryModel + " " + item.equippedModelFemale1 + " " + item.equippedModelMale1);
                int[] models = new int[] { item.inventoryModel, item.equippedModelFemale1, item.equippedModelMale1};
                for(int id : models) {
                    System.out.println("MODEL: " + id);
                    try {
                        Files.copy(Paths.get("./model_dump/" + id + ".dat"), Paths.get("./item_models/" + id + ".dat"), REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            if (OSRS_ITEMS[index][0] > totalItems) {
                totalItems = OSRS_ITEMS[index][0];
            }
        }

        /*ItemDefinition item = forID(25105);
        int[] models = new int[] { item.inventoryModel, item.equippedModelFemale1, item.equippedModelMale1};
        for(int id : models) {
            System.out.println("MODEL: " + id);
            try {
                Files.copy(Paths.get("./osrs_models/" + id + ".dat"), Paths.get("./item_models/" + id + ".dat"), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void unpack742Config(StreamLoader streamLoader) {
        item742Stream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "items742.dat"));
        Stream stream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "items742.idx"));
        int x = stream.readUnsignedWord();

        System.out.println("742 Items Amount: " + x);
        item742StreamIndices = new int[x];
        int i = 0;
        for (int j = 0; j < x; j++) {
            item742StreamIndices[j] = i;
            i += stream.readUnsignedWord();
        }

        for (int index = 0; index < ITEMS_742.length; index++) {
            if (ITEMS_742[index][0] > totalItems) {
                totalItems = ITEMS_742[index][0];
            }
        }
    }


    public static ItemDefinition forID(int id) {
        return forID(id, false, false);
    }

    public static ItemDefinition forID(int i, boolean oldschool, boolean item742) {
        if (i > totalItems || i < 0) {
            return null;
        }

        ItemDefinition itemDef = (ItemDefinition) recentUse.get(i);
        if (itemDef != null) {
            return itemDef;
        }

        itemDef = new ItemDefinition();
        itemDef.id = i;
        itemDef.setDefaults();

        int osrsId = -1;
        for (int index = 0; index < OSRS_ITEMS.length; index++) {
            if (OSRS_ITEMS[index][0] == i) {
                osrsId = OSRS_ITEMS[index][1];
            }
        }
        itemDef.oldschoolRs = osrsId > -1;
        if (oldschool) {
            osrsId = i;
        }

        int item742Id = -1;
        for (int index = 0; index < ITEMS_742.length; index++) {
            if (ITEMS_742[index][0] == i) {
                item742Id = ITEMS_742[index][1];
            }
        }

        itemDef.item742 = item742Id > -1;
        if (item742) {
            item742Id = i;
        }

        if (item742Id > -1 || item742) {
            item742Stream.currentOffset = item742StreamIndices[item742Id];
            itemDef.readValues(item742Stream);

            if (itemDef.actions != null) {
                for (int index = 0; index < itemDef.actions.length; index++) {
                    if (itemDef.actions[index] == null) {
                        continue;
                    }
                    if (itemDef.actions[index].equalsIgnoreCase("drop")) {
                        itemDef.actions[index] = null;
                    }
                }
            }


        } else if (osrsId > -1 || oldschool) {
            osrsStream.currentOffset = osrsStreamIndices[osrsId];
            itemDef.readOsrsValues(osrsStream);
        } else {
            stream.currentOffset = streamIndices[i];
            itemDef.readValues(stream);
        }

        /*if (itemDef.notedItemID != -1)
            itemDef.toNote();
        if (itemDef.lentItemID != -1)
            itemDef.toLend();
            */

        if (!isMembers && itemDef.membersItem) {
            itemDef.name = "Members Object";
            itemDef.description = "Login to a members' server to use this object.";
            itemDef.groundActions = null;
            itemDef.actions = null;
            itemDef.team = 0;
        }

        // master capes
        if (i >= 7692 && i <= 7716) {
            int index = i - 7692;
            itemDef.inventoryModel = 75436 + (index * 2) + 1;
            itemDef.equippedModelMale1 = 75436 + (index * 2);
            itemDef.equippedModelFemale1 = 75436 + (index * 2);
            itemDef.name = SKILL_NAMES[index] + " master cape";
            itemDef.description = "This must of taken a while...";
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wear";
            itemDef.actions[4] = "Drop";
            itemDef.rotationX = 300;
            itemDef.rotationZ = 100;

            itemDef.rotationY = 1150;

            itemDef.modelZoom = 2000;
        }

        switch (i) {
            case 964:
                itemDef.name = "Death";
                break;
            case 9722:
                itemDef.name = "Wilderness key";
                break;
            case 25085:
                itemDef.name = "Fishing hat";
                itemDef.inventoryModel = 70804;
                itemDef.equippedModelFemale1 = 70800;
                itemDef.equippedModelMale1 = 70796;
                itemDef.modelZoom = 1100;
                itemDef.rotationX = 229;
                itemDef.rotationY = 1980;
                itemDef.translateX = -1;
                itemDef.translateYZ = -36;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Destroy";
                break;
            case 25086:
                itemDef.name = "Fishing jacket";
                itemDef.inventoryModel = 70805;
                itemDef.equippedModelFemale1 = 70802;
                itemDef.equippedModelMale1 = 70798;
                itemDef.modelZoom = 1500;
                itemDef.rotationX = 606;
                itemDef.rotationY = 0;
                itemDef.translateX = 0;
                itemDef.translateYZ = -1;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Destroy";
                break;
            case 25087:
                itemDef.name = "Fishing waders";
                itemDef.inventoryModel = 70806;
                itemDef.equippedModelFemale1 = 70801;
                itemDef.equippedModelMale1 = 70797;
                itemDef.modelZoom = 1850;
                itemDef.rotationX = 458;
                itemDef.rotationY = 269;
                itemDef.translateX = 3;
                itemDef.translateYZ = 1;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Destroy";
                break;
            case 25088:
                itemDef.name = "Fishing boots";
                itemDef.inventoryModel = 70807;
                itemDef.equippedModelFemale1 = 70799;
                itemDef.equippedModelMale1 = 70795;
                itemDef.modelZoom = 920;
                itemDef.rotationX = 222;
                itemDef.rotationY = 10;
                itemDef.translateX = 2;
                itemDef.translateYZ = -37;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Destroy";
                break;
            case 25089:
                itemDef.name = "Sous chef's toque";
                itemDef.inventoryModel = 76945;
                itemDef.equippedModelFemale1 = 76896;
                itemDef.equippedModelMale1 = 76863;
                itemDef.modelZoom = 789;
                itemDef.rotationX = 175;
                itemDef.rotationY = 162;
                itemDef.translateX = 0;
                itemDef.translateYZ = -4;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                // itemDef.actions[3] = "Check duration";
                itemDef.actions[4] = "Destroy";
                break;
            case 25090:
                itemDef.name = "Sous chef's jacket";
                itemDef.inventoryModel = 76934;
                itemDef.equippedModelFemale1 = 76920;
                itemDef.equippedModelMale1 = 76886;
                itemDef.modelZoom = 1316;
                itemDef.rotationX = 539;
                itemDef.rotationY = 0;
                itemDef.translateX = 1;
                itemDef.translateYZ = 0;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                // itemDef.actions[3] = "Check duration";
                itemDef.actions[4] = "Destroy";
                break;
            case 25091:
                itemDef.name = "Sous chef's trousers";
                itemDef.inventoryModel = 76941;
                itemDef.equippedModelFemale1 = 76912;
                itemDef.equippedModelMale1 = 76878;
                itemDef.modelZoom = 1776;
                itemDef.rotationX = 525;
                itemDef.rotationY = 242;
                itemDef.translateX = -4;
                itemDef.translateYZ = -7;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                //itemDef.actions[3] = "Check duration";
                itemDef.actions[4] = "Destroy";
                break;
            case 25092:
                itemDef.name = "Sous chef's shoes";
                itemDef.inventoryModel = 76943;
                itemDef.equippedModelFemale1 = 76851;
                itemDef.equippedModelMale1 = 76851;
                itemDef.modelZoom = 724;
                itemDef.rotationX = 269;
                itemDef.rotationY = 229;
                itemDef.translateX = 3;
                itemDef.translateYZ = 17;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                //itemDef.actions[3] = "Check duration";
                itemDef.actions[4] = "Destroy";
                break;
            case 25093:
                itemDef.name = "Sous chef's mitts";
                itemDef.inventoryModel = 76927;
                itemDef.equippedModelFemale1 = 76858;
                itemDef.equippedModelMale1 = 76858;
                itemDef.modelZoom = 592;
                itemDef.rotationX = 391;
                itemDef.rotationY = 444;
                itemDef.translateX = 1;
                itemDef.translateYZ = 1;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                //itemDef.actions[3] = "Check duration";
                itemDef.actions[4] = "Destroy";
                break;

            case 995:
                itemDef.actions = new String[]{null, null, null, "Deposit into money pouch", "Drop"};
                break;
            case 25048: // zulrah scales
                itemDef.stackAmounts = null;
                itemDef.stackIDs = null;
                break;
            case 21393:
                itemDef.inventoryModel = 19229;
                itemDef.equippedModelMale1 = 19228;
                itemDef.equippedModelFemale1 = 19228;
                itemDef.name = "some cape";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 11283:
            case 11284:
            case 11285:
                itemDef.equippedModelMaleTranslateY = 18;
                itemDef.equippedModelFemaleTranslateY = 11;
                break;

            case 18803:
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Teleport";
                itemDef.actions[4] = "Drop";
                break;
            case 24992:
                itemDef.name = "Hood of subjugation";
                itemDef.modelZoom = 724;
                itemDef.rotationX = 162;
                itemDef.rotationY = 229;
                itemDef.translateX = 1;
                itemDef.translateYZ = -4;
                itemDef.equippedModelMale1 = 14936;
                itemDef.equippedModelFemale1 = 14937;
                itemDef.value = 14000;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 24993;
                itemDef.notedItemID = -1;
                itemDef.membersItem = true;
                itemDef.inventoryModel = 14938;
                break;
            case 24993:
                itemDef.name = "Hood of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 24992;
                itemDef.notedItemID = 799;
                break;
            case 24995:
                itemDef.name = "Garb of subjugation";
                itemDef.modelZoom = 1447;
                itemDef.rotationX = 553;
                itemDef.inventoryModel = 14939;
                itemDef.value = 51000;
                itemDef.equippedModelMale1 = 14840;
                itemDef.equippedModelFemale1 = 14841;
                itemDef.translateX = 0;
                itemDef.translateYZ = -1;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 24997;
                itemDef.notedItemID = -1;
                break;
            case 24996:
                itemDef.name = "Garb of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 24995;
                itemDef.notedItemID = 799;
                break;
            case 24998:
                itemDef.name = "Gown of subjugation";
                itemDef.inventoryModel = 14942;
                itemDef.modelZoom = 1579;
                itemDef.rotationX = 553;
                itemDef.rotationY = 256;
                itemDef.value = 48000;
                itemDef.equippedModelMale1 = 14943;
                itemDef.equippedModelFemale1 = 14944;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 24999;
                itemDef.notedItemID = -1;
                break;
            case 24999:
                itemDef.name = "Gown of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 24998;
                itemDef.notedItemID = 799;
                break;
            case 25007:
                itemDef.name = "Gloves of subjugation";
                itemDef.inventoryModel = 14945;
                itemDef.modelZoom = 526;
                itemDef.rotationX = 189;
                itemDef.rotationY = 1064;
                itemDef.value = 80000;
                itemDef.translateX = 3;
                itemDef.translateYZ = -3;
                itemDef.equippedModelMale1 = 14951;
                itemDef.equippedModelFemale1 = 14951;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25008;
                itemDef.notedItemID = -1;
                break;
            case 25008:
                itemDef.name = "Gloves of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25007;
                itemDef.notedItemID = 799;
                break;
            case 25004:
                itemDef.name = "Boots of subjugation";
                itemDef.inventoryModel = 14959;
                itemDef.modelZoom = 921;
                itemDef.rotationX = 269;
                itemDef.rotationY = 229;
                itemDef.translateX = 1;
                itemDef.translateYZ = -5;
                itemDef.value = 19000;
                itemDef.equippedModelMale1 = 14966;
                itemDef.equippedModelFemale1 = 14966;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25005;
                itemDef.notedItemID = -1;
                break;
            case 25005:
                itemDef.name = "Boots of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25004;
                itemDef.notedItemID = 799;
                break;
            case 25001:
                itemDef.name = "Ward of subjugation";
                itemDef.inventoryModel = 14967;
                itemDef.rotationX = 512;
                itemDef.rotationY = 162;
                itemDef.value = 47000;
                itemDef.translateX = 1;
                itemDef.translateYZ = 3;
                itemDef.equippedModelMale1 = 14998;
                itemDef.equippedModelFemale1 = 14998;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25002;
                itemDef.notedItemID = -1;
                break;
            case 25002:
                itemDef.name = "Ward of subjugation";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25001;
                itemDef.notedItemID = 799;
                break;
            case 25028:
                itemDef.name = "Saradomin's whisper";
                itemDef.inventoryModel = 14999;
                itemDef.modelZoom = 592;
                itemDef.rotationX = 319;
                itemDef.rotationY = 129;
                itemDef.value = 40000;
                itemDef.translateX = 5;
                itemDef.translateYZ = 46;
                itemDef.equippedModelMale1 = 15000;
                itemDef.equippedModelFemale1 = 15001;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25029;
                itemDef.notedItemID = -1;
                break;
            case 25029:
                itemDef.name = "Saradomin's whisper";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25028;
                itemDef.notedItemID = 799;
                break;
            case 25031:
                itemDef.name = "Saradomin's hiss";
                itemDef.modelZoom = 592;
                itemDef.inventoryModel = 75201;
                itemDef.modelZoom = 592;
                itemDef.rotationX = 318;
                itemDef.rotationY = 129;
                itemDef.value = 40000;
                itemDef.translateX = 5;
                itemDef.translateYZ = 46;
                itemDef.equippedModelMale1 = 15031;
                itemDef.equippedModelFemale1 = 15040;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25032;
                itemDef.notedItemID = -1;
                break;
            case 25032:
                itemDef.name = "Saradomin's hiss";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25031;
                itemDef.notedItemID = 799;
                break;
            case 25034:
                itemDef.name = "Saradomin's murmur";
                itemDef.inventoryModel = 15102;
                itemDef.modelZoom = 592;
                itemDef.rotationX = 318;
                itemDef.rotationY = 129;
                itemDef.value = 40000;
                itemDef.translateX = 5;
                itemDef.translateYZ = 46;
                itemDef.equippedModelMale1 = 15041;
                itemDef.equippedModelFemale1 = 15042;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25035;
                itemDef.notedItemID = -1;
                break;
            case 25035:
                itemDef.name = "Saradomin's murmur";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25034;
                itemDef.notedItemID = 799;
                break;
            case 25013:
                itemDef.name = "Armadyl buckler";
                itemDef.inventoryModel = 15043;
                itemDef.modelZoom = 1000;
                itemDef.rotationX = 426;
                itemDef.rotationY = 1179;
                itemDef.value = 47000;
                itemDef.translateX = 5;
                itemDef.translateYZ = 7;
                itemDef.equippedModelMale1 = 15044;
                itemDef.equippedModelFemale1 = 15044;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25014;
                itemDef.notedItemID = -1;
                break;
            case 25014:
                itemDef.name = "Armadyl buckler";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25013;
                itemDef.notedItemID = 799;
                break;
            case 25010:
                itemDef.name = "Armadyl boots";
                itemDef.inventoryModel = 15063;
                itemDef.modelZoom = 855;
                itemDef.rotationX = 215;
                itemDef.rotationY = 94;
                itemDef.value = 19000;
                itemDef.equippedModelMale1 = 15064;
                itemDef.equippedModelFemale1 = 15064;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25011;
                itemDef.notedItemID = -1;
                itemDef.translateX = 4;
                itemDef.translateYZ = -32;
                break;
            case 25011:
                itemDef.name = "Armadyl boots";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25010;
                itemDef.notedItemID = 799;
                break;
            case 25016:
                itemDef.name = "Armadyl gloves";
                itemDef.modelZoom = 592;
                itemDef.inventoryModel = 15073;
                itemDef.rotationX = 323;
                itemDef.rotationY = 1710;
                itemDef.value = 19000;
                itemDef.translateX = 3;
                itemDef.translateYZ = 5;
                itemDef.equippedModelMale1 = 15079;
                itemDef.equippedModelFemale1 = 15079;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.unNotedItemID = 25017;
                itemDef.notedItemID = -1;
                break;
            case 25017:
                itemDef.name = "Armadyl gloves";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25016;
                itemDef.notedItemID = 799;
                break;

            case 25640:
                itemDef.name = "Robust glass";
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 25618;
                itemDef.notedItemID = 799;
                itemDef.stackable = true;
                if (itemDef.notedItemID != -1) {
                    itemDef.toNote();
                }
                break;

            case 8465:
                itemDef.name = "TokHaar-Kal";
                itemDef.equippedModelMale1 = 62575;
                itemDef.equippedModelFemale1 = 62582;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.translateX = -4;
                itemDef.inventoryModel = 62592;
                itemDef.description = "A cape made of ancient, enchanted rocks.";
                itemDef.modelZoom = 1616;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.translateYZ = 0;
                itemDef.rotationX = 339;
                itemDef.rotationY = 192;
                break;

            case 17748:
                itemDef.name = "Abyssal vine whip";
                itemDef.modelZoom = 848;
                itemDef.rotationX = 280;
                itemDef.rotationY = 0;
                itemDef.translateYZ = 0;
                itemDef.translateX = 0;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wield", "Split", null, "Drop"};
                itemDef.inventoryModel = 10247;
                itemDef.equippedModelMale1 = 10253;
                itemDef.equippedModelFemale1 = 10253;
                break;

            case 15343: // noted id
                itemDef.name = "Fury Shark"; // needs to match original name
                itemDef.modelZoom = 2000;
                itemDef.unNotedItemID = 20429; // original id
                itemDef.notedItemID = 799;
                itemDef.stackable = true;
                if (itemDef.notedItemID != -1) {
                    itemDef.toNote();
                }
                break;

            case 996:
            case 997:
            case 998:
            case 999:
            case 1000:
            case 1001:
            case 1002:
            case 1003:
            case 1004:
                itemDef.name = "Coins";
                break;


            case 25107:
                itemDef.name = "Potion flask";
                itemDef.inventoryModel = 61741;
                itemDef.equippedModelFemale1 = -1;
                itemDef.equippedModelMale1 = -1;
                itemDef.modelZoom = 804;
                itemDef.rotationX = 131;
                itemDef.rotationY = 198;
                itemDef.translateX = 1;
                itemDef.translateYZ = -1;
                itemDef.actions = new String[5];
                itemDef.actions[4] = "drop";
                break;
            case 25108:
                itemDef.name = "Potion flask";
                itemDef.inventoryModel = 0;
                itemDef.equippedModelFemale1 = -1;
                itemDef.equippedModelMale1 = -1;
                itemDef.modelZoom = 2000;
                itemDef.rotationX = 0;
                itemDef.rotationY = 0;
                itemDef.translateX = 0;
                itemDef.translateYZ = 0;
                itemDef.actions = new String[5];
                itemDef.actions[4] = "drop";
                break;

        }

        recentUse.put(itemDef, i);
        return itemDef;
    }

    private static float[] depthBuffer = new float[36 * 32 + 1];
    public static Sprite getSprite(int id, int count, int outline, int shadow, boolean shrink, boolean drawCount) {
        final int uid1 = (drawCount ? 65536 : 0) + id + (outline << 17) + (shadow << 19);
        final long uid = 3147483667L * count + 3849834839L * uid1;
        Sprite cachedSprite = (Sprite) itemSpriteCache.get(uid);
        if (cachedSprite != null) {
            return cachedSprite;
        }
        ItemDefinition itemDef = forID(id);
        if (itemDef.inventoryModel == 10247)
            return vine;
        if (count > 1 && itemDef.stackIDs != null) {
            int newItemId = -1;
            for (int i = 0; i < 10; i++)
                if (count >= itemDef.stackAmounts[i]
                        && itemDef.stackAmounts[i] != 0)
                    newItemId = itemDef.stackIDs[i];
            if (newItemId != -1)
                itemDef = forID(newItemId);
        }
        Model model = itemDef.getInventoryModel(1);
        if (model == null) {
            return null;
        }
        Sprite linkSprite = null;
        if (itemDef.notedItemID != -1) {
            linkSprite = getSprite(itemDef.unNotedItemID, 10, 1, 0, true, false);
            if (linkSprite == null) {
                return null;
            }
        }
        if (itemDef.lentItemID != -1) {
            linkSprite = getSprite(itemDef.lendID, count, outline, shadow, false, false);
            if (linkSprite == null) {
                return null;
            }
        }
        float depth[] = DrawingArea.depthBuffer;
        int ai1[] = DrawingArea.pixels;
        int i2 = DrawingArea.width;
        int j2 = DrawingArea.height;
        int k2 = DrawingArea.topX;
        int i3 = DrawingArea.topY;
        int l2 = DrawingArea.bottomX;
        int j3 = DrawingArea.bottomY;
        Rasterizer.notTextured = false;
        Sprite sprite = new Sprite(36, 32);
        DrawingArea.initDrawingArea(sprite.myPixels, 36, 32, depthBuffer);
        DrawingArea.setAllPixelsToZero2(0);
        Rasterizer.setDefaultBounds();
        Rasterizer.setViewport(16, 16);
        int zoom = itemDef.modelZoom;
        if (shrink)
            zoom *= 1.5;
        if (outline == 2)
            zoom *= 1.04;
        int l3 = Rasterizer.SINE[itemDef.rotationX << 3] * zoom >> 15;
        int i4 = Rasterizer.COSINE[itemDef.rotationX << 3] * zoom >> 15;
        model.calculateDiagonals();
        model.method482(itemDef.rotationY << 3, itemDef.rotationZ << 3,
                itemDef.rotationX << 3, itemDef.translateX, l3
                        + model.highestY / 2 + itemDef.translateYZ, i4
                        + itemDef.translateYZ
        );
        if (outline >= 1) {
            sprite.outline(1);
            if (outline >= 2) {
                sprite.outline(16777215);
            }
            DrawingArea.initDrawingArea(sprite.myPixels, 36, 32, depthBuffer);
        }
        if (shadow != 0) {
            sprite.shadow(shadow);
        }
        if (itemDef.notedItemID != -1) {
            linkSprite.drawSprite(0, 0);
        } else if (itemDef.lentItemID != -1) {
            DrawingArea.initDrawingArea(linkSprite.myPixels, 36, 32, depthBuffer);
            sprite.drawSprite(0, 0);
            sprite = linkSprite;
        }
        if (drawCount && (itemDef.stackable || count != 1) && count != -1) {
            Client.small.drawString(Client.formatObjCount(count), 0, 9, 16776960, 1);
        }
        DrawingArea.initDrawingArea(ai1, i2, j2, depth);
        DrawingArea.setDrawingArea(k2, i3, l2, j3);
        Rasterizer.setDefaultBounds();

        if (sprite != null && !Rasterizer.notTextured) {
            itemSpriteCache.put(sprite, uid);
        }
        return sprite;
    }

    public boolean isDialogueModelCached(int gender) {
        int model1 = maleDialogueModel1;
        int model2 = maleDialogueModel2;
        if (gender == 1) {
            model1 = femaleDialogueModel1;
            model2 = femaleDialogueModel2;
        }
        if (model1 == -1)
            return true;
        boolean cached = true;
        if (!Model.isCached(model1))
            cached = false;
        if (model2 != -1 && !Model.isCached(model2))
            cached = false;
        return cached;
    }

    public Model getChatModel(int gender) {
        int dialogueModel = maleDialogueModel1;
        int dialogueHatModel = maleDialogueModel2;
        if (gender == 1) {
            dialogueModel = femaleDialogueModel1;
            dialogueHatModel = femaleDialogueModel2;
        }
        if (dialogueModel == -1)
            return null;
        Model model = Model.getModel(dialogueModel);
        if (dialogueHatModel != -1) {
            Model model_1 = Model.getModel(dialogueHatModel);
            Model models[] = {model, model_1};
            model = new Model(models, 2);
        }
        if (originalModelColors != null) {
            for (int i1 = 0; i1 < originalModelColors.length; i1++)
                model.setColor(originalModelColors[i1], newModelColors[i1]);
        }
        return model;
    }

    public boolean isEquippedModelCached(int gender) {
        int primaryModel = equippedModelMale1;
        int secondaryModel = equippedModelMale2;
        int emblem = equippedModelMale3;
        if (gender == 1) {
            primaryModel = equippedModelFemale1;
            secondaryModel = equippedModelFemale2;
            emblem = equippedModelFemale3;
        }
        if (primaryModel == -1)
            return true;
        boolean cached = true;
        if (!Model.isCached(primaryModel))
            cached = false;
        if (secondaryModel != -1 && !Model.isCached(secondaryModel))
            cached = false;
        if (emblem != -1 && !Model.isCached(emblem))
            cached = false;
        return cached;
    }

    public Model getEquippedModel(int gender, CustomizedItem customizedItem) {
        int primaryModel = equippedModelMale1;
        int secondaryModel = equippedModelMale2;
        int emblem = equippedModelMale3;
        if (gender == 1) {
            primaryModel = equippedModelFemale1;
            secondaryModel = equippedModelFemale2;
            emblem = equippedModelFemale3;
        }
        if (primaryModel == -1)
            return null;
        Model model = Model.getModel(primaryModel);
        if (secondaryModel != -1)
            if (emblem != -1) {
                Model model_1 = Model.getModel(secondaryModel);
                Model model_3 = Model.getModel(emblem);
                Model model_1s[] = {model, model_1, model_3};
                model = new Model(model_1s, 3);
            } else {
                Model model_2 = Model.getModel(secondaryModel);
                Model models[] = {model, model_2};
                model = new Model(models, 2);
            }
        if (gender == 0 && equippedModelMaleTranslateY != 0)
            model.translate(0, equippedModelMaleTranslateY, 0);
        if (gender == 1 && equippedModelFemaleTranslateY != 0)
            model.translate(0, equippedModelFemaleTranslateY, 0);

        //Cheap fix for the offsets of female wield models
        if (gender == 1) {
            if (id == 11283 || id == 11284 || id == 11285) {
                model.translate(-3, 0, 0);
            }
            for (String itemActions : actions) {
                if (itemActions == null || itemActions.length() == 0) {
                    continue;
                }
                if (itemActions.equalsIgnoreCase("Wield")) {
                    model.translate(3, equippedModelFemaleTranslateY - 12, 5);
                }
            }
        }

		if (originalModelColors != null) {
			short[] newModelColors;
			if (customizedItem != null && customizedItem.id == id && customizedItem.newModelColors != null) {
				newModelColors = customizedItem.newModelColors;
			} else {
				newModelColors = this.newModelColors;
			}

			for (int i1 = 0; i1 < originalModelColors.length; i1++)
				model.setColor(originalModelColors[i1], newModelColors[i1]);
		}
        return model;
    }

    public void setDefaults() {
        inventoryModel = 0;
        name = null;
        description = null;
        originalModelColors = null;
        newModelColors = null;
        modelZoom = 2000;
        rotationX = 0;
        rotationY = 0;
        rotationZ = 0;
        translateX = 0;
        translateYZ = 0;
        stackable = false;
        value = 1;
        membersItem = false;
        groundActions = null;
        actions = null;
        equippedModelMale1 = -1;
        equippedModelMale2 = -1;
        equippedModelMaleTranslateY = 0;
        equippedModelFemale1 = -1;
        equippedModelFemale2 = -1;
        equippedModelFemaleTranslateY = 0;
        equippedModelMale3 = -1;
        equippedModelFemale3 = -1;
        maleDialogueModel1 = -1;
        maleDialogueModel2 = -1;
        femaleDialogueModel1 = -1;
        femaleDialogueModel2 = -1;
        stackIDs = null;
        stackAmounts = null;
        unNotedItemID = -1;
        notedItemID = -1;
        modelScaleX = 128;
        modelScaleY = 128;
        modelScaleZ = 128;
        lightIntensity = 0;
        lightMagnitude = 0;
        team = 0;
        lendID = -1;
        lentItemID = -1;
    }

    private void readValues(Stream stream) {
        if (id >= 7692 && id <= 7716) {
            return;
        }

        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                return;
            if (i == 1) {
                inventoryModel = stream.readUnsignedWord();
            } else if (i == 2) {
                name = stream.readString();
            } else if (i == 3)
                description = stream.readString();
            else if (i == 4)
                modelZoom = stream.readUnsignedWord();
            else if (i == 5)
                rotationX = stream.readUnsignedWord();
            else if (i == 6)
                rotationY = stream.readUnsignedWord();
            else if (i == 7) {
                translateX = stream.readUnsignedWord();
                if (translateX > 32767)
                    translateX -= 0x10000;
            } else if (i == 8) {
                translateYZ = stream.readUnsignedWord();
                if (translateYZ > 32767)
                    translateYZ -= 0x10000;
            } else if (i == 10)
                stream.readUnsignedWord();
            else if (i == 11)
                stackable = true;
            else if (i == 12)
                value = stream.readDWord();
            else if (i == 16)
                membersItem = true;
            else if (i == 23) {
                equippedModelMale1 = stream.readUnsignedWord();
                equippedModelMaleTranslateY = stream.readSignedByte();
            } else if (i == 24)
                equippedModelMale2 = stream.readUnsignedWord();
            else if (i == 25) {
                equippedModelFemale1 = stream.readUnsignedWord();
                equippedModelFemaleTranslateY = stream.readSignedByte();
            } else if (i == 26)
                equippedModelFemale2 = stream.readUnsignedWord();
            else if (i >= 30 && i < 35) {
                if (groundActions == null)
                    groundActions = new String[5];
                groundActions[i - 30] = stream.readString();
                if (groundActions[i - 30].equalsIgnoreCase("hidden"))
                    groundActions[i - 30] = null;
            } else if (i >= 35 && i < 40) {
                if (actions == null)
                    actions = new String[5];
                actions[i - 35] = stream.readString();
                if (actions[i - 35].equalsIgnoreCase("null"))
                    actions[i - 35] = null;
            } else if (i == 40) {
                int j = stream.readUnsignedByte();
                originalModelColors = new short[j];
                newModelColors = new short[j];
                for (int k = 0; k < j; k++) {
                    originalModelColors[k] = (short) stream.readUnsignedWord();
                    newModelColors[k] = (short) stream.readUnsignedWord();
                }
            } else if (i == 78)
                equippedModelMale3 = stream.readUnsignedWord();
            else if (i == 79)
                equippedModelFemale3 = stream.readUnsignedWord();
            else if (i == 90)
                maleDialogueModel1 = stream.readUnsignedWord();
            else if (i == 91)
                femaleDialogueModel1 = stream.readUnsignedWord();
            else if (i == 92)
                maleDialogueModel2 = stream.readUnsignedWord();
            else if (i == 93)
                femaleDialogueModel2 = stream.readUnsignedWord();
            else if (i == 95)
                rotationZ = stream.readUnsignedWord();
            else if (i == 97) {
                unNotedItemID = stream.readUnsignedWord();
            } else if (i == 98) {
                notedItemID = stream.readUnsignedWord();
            } else if (i >= 100 && i < 110) {
                if (stackIDs == null) {
                    stackIDs = new int[10];
                    stackAmounts = new int[10];
                }
                stackIDs[i - 100] = stream.readUnsignedWord();
                stackAmounts[i - 100] = stream.readUnsignedWord();
            } else if (i == 110)
                modelScaleX = stream.readUnsignedWord();
            else if (i == 111)
                modelScaleY = stream.readUnsignedWord();
            else if (i == 112)
                modelScaleZ = stream.readUnsignedWord();
            else if (i == 113)
                lightIntensity = stream.readSignedByte();
            else if (i == 114)
                lightMagnitude = stream.readSignedByte() * 5;
            else if (i == 115)
                team = stream.readUnsignedByte();
            else if (i == 121)
                lendID = stream.readUnsignedWord();
            else if (i == 122)
                lentItemID = stream.readUnsignedWord();
        } while (true);
    }


    private void readOsrsValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                return;
            if (i == 1)
                inventoryModel = stream.readUnsignedWord();
            else if (i == 2)
                name = stream.readString();
            else if (i == 3)
                description = stream.readString();
            else if (i == 4)
                modelZoom = stream.readUnsignedWord();
            else if (i == 5)
                rotationX = stream.readUnsignedWord();
            else if (i == 6)
                rotationY = stream.readUnsignedWord();
            else if (i == 7) {
                translateX = stream.readUnsignedWord();
                if (translateX > 32767)
                    translateX -= 0x10000;
            } else if (i == 8) {
                translateYZ = stream.readUnsignedWord();
                if (translateYZ > 32767)
                    translateYZ -= 0x10000;
            } else if (i == 10)
                stream.readUnsignedWord();
            else if (i == 11)
                stackable = true;
            else if (i == 12)
                value = stream.readDWord();
            else if (i == 16)
                membersItem = true;
            else if (i == 23) {
                equippedModelMale1 = stream.readUnsignedWord();
                equippedModelMaleTranslateY = stream.readSignedByte();
            } else if (i == 24)
                equippedModelMale2 = stream.readUnsignedWord();
            else if (i == 25) {
                equippedModelFemale1 = stream.readUnsignedWord();
                equippedModelFemaleTranslateY = stream.readSignedByte();
            } else if (i == 26)
                equippedModelFemale2 = stream.readUnsignedWord();
            else if (i >= 30 && i < 35) {
                if (groundActions == null)
                    groundActions = new String[5];
                groundActions[i - 30] = stream.readString();
                if (groundActions[i - 30].equalsIgnoreCase("hidden"))
                    groundActions[i - 30] = null;
            } else if (i >= 35 && i < 40) {
                if (actions == null)
                    actions = new String[5];
                actions[i - 35] = stream.readString();
            } else if (i == 40) {
                int j = stream.readUnsignedByte();
                originalModelColors = new short[j];
                newModelColors = new short[j];
                for (int k = 0; k < j; k++) {
                    newModelColors[k] = (short) stream.readUnsignedWord();
                    originalModelColors[k] = (short) stream.readUnsignedWord();
                }

            } else if (i == 78)
                equippedModelMale3 = stream.readUnsignedWord();
            else if (i == 79)
                equippedModelFemale3 = stream.readUnsignedWord();
            else if (i == 90)
                maleDialogueModel1 = stream.readUnsignedWord();
            else if (i == 91)
                femaleDialogueModel1 = stream.readUnsignedWord();
            else if (i == 92)
                maleDialogueModel2 = stream.readUnsignedWord();
            else if (i == 93)
                femaleDialogueModel2 = stream.readUnsignedWord();
            else if (i == 95)
                rotationZ = stream.readUnsignedWord();
            else if (i == 97)
                unNotedItemID = stream.readUnsignedWord();
            else if (i == 98)
                notedItemID = stream.readUnsignedWord();
            else if (i == 100) {
                int length = stream.readUnsignedByte();
                stackIDs = new int[length];
                stackAmounts = new int[length];
                for (int i2 = 0; i2 < length; i2++) {
                    stackIDs[i2] = stream.readUnsignedWord();
                    stackAmounts[i2] = stream.readUnsignedWord();
                }
            } else if (i == 110)
                modelScaleX = stream.readUnsignedWord();
            else if (i == 111)
                modelScaleY = stream.readUnsignedWord();
            else if (i == 112)
                modelScaleZ = stream.readUnsignedWord();
            else if (i == 113)
                lightIntensity = stream.readSignedByte();
            else if (i == 114)
                lightMagnitude = stream.readSignedByte() * 5;
            else if (i == 115)
                team = stream.readUnsignedByte();
        } while (true);
    }


    public void toNote() {
        ItemDefinition itemDef = forID(notedItemID, oldschoolRs, item742);
        inventoryModel = itemDef.inventoryModel;
        modelZoom = itemDef.modelZoom;
        rotationX = itemDef.rotationX;
        rotationY = itemDef.rotationY;
        rotationZ = itemDef.rotationZ;
        translateX = itemDef.translateX;
        translateYZ = itemDef.translateYZ;
        originalModelColors = itemDef.originalModelColors;
        newModelColors = itemDef.newModelColors;
        ItemDefinition itemDef_1 = forID(unNotedItemID, oldschoolRs, item742);
        name = itemDef_1.name;
        membersItem = itemDef_1.membersItem;
        value = itemDef_1.value;
        String s = "a";
        char c = itemDef_1.name.charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
            s = "an";
        description = ("Swap this note at any bank for " + s + " "
                + itemDef_1.name + ".");
        stackable = true;
    }

    private void toLend() {
        ItemDefinition itemDef = forID(lentItemID);
        actions = new String[5];
        inventoryModel = itemDef.inventoryModel;
        translateX = itemDef.translateX;
        rotationY = itemDef.rotationY;
        translateYZ = itemDef.translateYZ;
        modelZoom = itemDef.modelZoom;
        rotationX = itemDef.rotationX;
        rotationZ = itemDef.rotationZ;
        value = 0;
        ItemDefinition itemDef_1 = forID(lendID);

        if (itemDef_1 == null) {
            return;
        }

        maleDialogueModel2 = itemDef_1.maleDialogueModel2;
        originalModelColors = itemDef_1.originalModelColors;
        equippedModelMale3 = itemDef_1.equippedModelMale3;
        equippedModelMale2 = itemDef_1.equippedModelMale2;
        femaleDialogueModel2 = itemDef_1.femaleDialogueModel2;
        maleDialogueModel1 = itemDef_1.maleDialogueModel1;
        groundActions = itemDef_1.groundActions;
        equippedModelMale1 = itemDef_1.equippedModelMale1;
        name = itemDef_1.name;
        equippedModelFemale1 = itemDef_1.equippedModelFemale1;
        membersItem = itemDef_1.membersItem;
        femaleDialogueModel1 = itemDef_1.femaleDialogueModel1;
        equippedModelFemale2 = itemDef_1.equippedModelFemale2;
        equippedModelFemale3 = itemDef_1.equippedModelFemale3;
        newModelColors = itemDef_1.newModelColors;
        team = itemDef_1.team;
        if (itemDef_1.actions != null) {
            for (int i_33_ = 0; i_33_ < 4; i_33_++)
                actions[i_33_] = itemDef_1.actions[i_33_];
        }
        actions[4] = "Discard";
    }

    public Model getInventoryModel(int stackSize) {
        if (stackIDs != null && stackSize > 1) {
            int stackItemID = -1;
            for (int k = 0; k < 10; k++)
                if (stackSize >= stackAmounts[k] && stackAmounts[k] != 0)
                    stackItemID = stackIDs[k];
            if (stackItemID != -1)
                return forID(stackItemID).getInventoryModel(1);
        }
        Model model = (Model) modelCache.get(id);
        if (model != null)
            return model;
        model = Model.getModel(inventoryModel);
        if (model == null)
            return null;
        if (modelScaleX != 128 || modelScaleY != 128 || modelScaleZ != 128)
            model.scale(modelScaleX, modelScaleY, modelScaleZ);
        if (originalModelColors != null) {
            for (int l = 0; l < originalModelColors.length; l++)
                model.setColor(originalModelColors[l], newModelColors[l]);
        }

        model.setLighting(64 + lightIntensity, 768 + lightMagnitude, -50, -10, -50, true,
                false, true);
        model.rendersWithinOneTile = true;
        modelCache.put(model, id);
        return model;
    }

    public Model getUnshadedModel(int stackSize) {
        if (stackIDs != null && stackSize > 1) {
            int stackItemID = -1;
            for (int k = 0; k < 10; k++)
                if (stackSize >= stackAmounts[k] && stackAmounts[k] != 0)
                    stackItemID = stackIDs[k];
            if (stackItemID != -1)
                return forID(stackItemID).getUnshadedModel(1);
        }
        Model model = Model.getModel(inventoryModel);
        if (model == null)
            return null;
        if (originalModelColors != null) {
            for (int l = 0; l < originalModelColors.length; l++)
                model.setColor(originalModelColors[l], newModelColors[l]);
        }

        return model;
    }

}
