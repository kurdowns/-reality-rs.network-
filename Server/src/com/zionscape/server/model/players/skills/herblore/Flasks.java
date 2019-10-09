package com.zionscape.server.model.players.skills.herblore;

import com.zionscape.server.model.content.PotionMixing;
import com.zionscape.server.util.CollectionUtil;

import java.util.Optional;

public enum Flasks {

    OVERLOAD(PotionMixing.CombiningDoses.OVERLOADS, 25512, 25511, 25510, 25509, 25508, 25507),
    SARA(PotionMixing.CombiningDoses.SARADOMIN_BREW, 25337, 25335, 25333, 25331, 25329, 25327),
    EXTREME_ATT(PotionMixing.CombiningDoses.EXTREME_ATT, 25476, 25475, 25474, 25473, 25472, 25471),
    EXTREME_STR(PotionMixing.CombiningDoses.EXTREME_STR, 25482, 25481, 25480, 25479, 25478, 25477),
    EXTREME_DEF(PotionMixing.CombiningDoses.EXTREME_DEF, 25488, 25487, 25486, 25485, 25484, 25483),
    ANTIFIRE(PotionMixing.CombiningDoses.ANTIFIRE, 25349, 25347, 25345, 25343, 25341, 25339),
    ANTIPOISON(PotionMixing.CombiningDoses.ANTIPOISON, 25301, 25299, 25297, 25295, 25293, 25291),
    SUPER_ATT(PotionMixing.CombiningDoses.SUPER_ATTACK, 25241, 25239, 25237, 25235, 25233, 25231),
    SUPER_STR(PotionMixing.CombiningDoses.SUPER_STRENGTH, 25265, 25263, 25261, 25259, 25257, 25255),
    SUPER_DEF(PotionMixing.CombiningDoses.SUPER_DEFENCE, 25277, 25275, 25273, 25271, 25269, 25267),
    SUPER_RESTORE(PotionMixing.CombiningDoses.SUPER_RESTORE, 25385, 25383, 25381, 25379, 25377, 25375),
    MAGIC(PotionMixing.CombiningDoses.MAGIC_POTION, 25409, 25407, 25405, 25403, 25401, 25399),
    RANGE(PotionMixing.CombiningDoses.RANGING_POTION, 25289, 25287, 25285, 25283, 25281, 25279),
    PRAYER(PotionMixing.CombiningDoses.PRAYER, 25229, 25227, 25225, 25223, 25221, 25219),
    EXTREME_MAGIC(PotionMixing.CombiningDoses.EXTREME_MAGIC, 25494, 25493, 25492, 25491, 25490, 25489),
    EXTEME_RANGE(PotionMixing.CombiningDoses.EXTREME_RANGE, 25500, 25499, 25498, 25497, 25496, 25495);

    private final int[] ids;
    private final PotionMixing.CombiningDoses combiningDoses;

    Flasks( PotionMixing.CombiningDoses combiningDoses, int ... ids) {
        this.ids = ids;
        this.combiningDoses = combiningDoses;
    }

    public int[] getIds() {
        return ids;
    }

    public PotionMixing.CombiningDoses getCombiningDoses() {
        return combiningDoses;
    }

    public static Optional<Flasks> getFlask(int item) {
        for(Flasks flask : Flasks.values()) {
            if(CollectionUtil.getIndexOfValue(flask.getIds(), item) > -1) {
                return Optional.of(flask);
            }
        }
        return Optional.empty();
    }

}
