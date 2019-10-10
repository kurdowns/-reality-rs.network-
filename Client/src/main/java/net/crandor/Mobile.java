package net.crandor;

abstract class Mobile extends Entity {

    final int[] smallX;
    final int[] smallY;
    final int[] hitArray;
    final int[] hitMarkTypes;
    final int[] hitsLoopCycle;
    final boolean[] pathRun;
    public int spotAnimFrame;
    public int animNextFrame;
    public int idleAnimNextFrame;
    public int spotAnimNextFrame;
    int interactingEntity;
    int anInt1503;
    int degreesToTurn;
    int runAnimIndex;
    String textSpoken;
    int height;
    int turnDirection;
    int standAnimIndex;
    int standTurnAnimIndex;
    int anInt1513;
    int idleAnimId;
    int idleAnimFrame;
    int idleAnimFrameDelay;
    int spotAnimId;
    int spotAnimFrameDelay;
    int spotAnimDelay;
    int anInt1524;
    int smallXYIndex;
    int animId;
    int animFrame;
    int animFrameDelay;
    int animDelay;
    int animCyclesElapsed;
    int anInt1531;
    int loopCycleStatus;
    int currentHealth;
    int maxHealth;
    int textCycle;
    int time;
    int anInt1538;
    int anInt1539;
    int size;
    boolean aBoolean1541;
    int anInt1542;
    int anInt1543;
    int anInt1544;
    int anInt1545;
    int anInt1546;
    int anInt1547;
    int anInt1548;
    int anInt1549;
    int x;
    int y;
    int currentRotation;
    int walkAnimIndex;
    int turn180AnimIndex;
    int turn90CWAnimIndex;
    int turn90CCWAnimIndex;
    int[] hitmarkMove = new int[4];
    int[] hitmarkTrans = new int[4];
    int[] hitIcon = new int[4];

    Mobile() {
        smallX = new int[10];
        smallY = new int[10];
        interactingEntity = -1;
        degreesToTurn = 256;
        runAnimIndex = -1;
        height = 200;
        standAnimIndex = -1;
        standTurnAnimIndex = -1;
        hitArray = new int[4];
        hitMarkTypes = new int[4];
        hitsLoopCycle = new int[4];
        idleAnimId = -1;
        spotAnimId = -1;
        animId = -1;
        loopCycleStatus = -1000;
        textCycle = 100;
        size = 1;
        aBoolean1541 = false;
        pathRun = new boolean[10];
        walkAnimIndex = -1;
        turn180AnimIndex = -1;
        turn90CWAnimIndex = -1;
        turn90CCWAnimIndex = -1;
    }

    final void setPos(int i, int j, boolean flag) {
        if (animId != -1 && Animation.anims[animId].anInt364 == 1)
            animId = -1;
        if (!flag) {
            int k = i - smallX[0];
            int l = j - smallY[0];
            if (k >= -8 && k <= 8 && l >= -8 && l <= 8) {
                if (smallXYIndex < 9)
                    smallXYIndex++;
                for (int i1 = smallXYIndex; i1 > 0; i1--) {
                    smallX[i1] = smallX[i1 - 1];
                    smallY[i1] = smallY[i1 - 1];
                    pathRun[i1] = pathRun[i1 - 1];
                }

                smallX[0] = i;
                smallY[0] = j;
                pathRun[0] = false;
                return;
            }
        }
        smallXYIndex = 0;
        anInt1542 = 0;
        anInt1503 = 0;
        smallX[0] = i;
        smallY[0] = j;
        x = smallX[0] * 128 + size * 64;
        y = smallY[0] * 128 + size * 64;
    }

    final void method446() {
        smallXYIndex = 0;
        anInt1542 = 0;
    }

    final void moveInDir(boolean flag, int i) {
        int j = smallX[0];
        int k = smallY[0];
        if (i == 0) {
            j--;
            k++;
        }
        if (i == 1)
            k++;
        if (i == 2) {
            j++;
            k++;
        }
        if (i == 3)
            j--;
        if (i == 4)
            j++;
        if (i == 5) {
            j--;
            k--;
        }
        if (i == 6)
            k--;
        if (i == 7) {
            j++;
            k--;
        }
        if (animId != -1 && Animation.anims[animId].anInt364 == 1)
            animId = -1;
        if (smallXYIndex < 9)
            smallXYIndex++;
        for (int l = smallXYIndex; l > 0; l--) {
            smallX[l] = smallX[l - 1];
            smallY[l] = smallY[l - 1];
            pathRun[l] = pathRun[l - 1];
        }
        smallX[0] = j;
        smallY[0] = k;
        pathRun[0] = flag;
    }

    public boolean isVisible() {
        return false;
    }

    final void updateHitDataOld(int j, int k, int l) {
        for (int i1 = 0; i1 < 4; i1++)
            if (hitsLoopCycle[i1] <= l) {
                hitArray[i1] = k;
                hitMarkTypes[i1] = j;
                hitsLoopCycle[i1] = l + 70;
                return;
            }
    }

    final void updateHitDataNew(int markType, int damage, int l, int icon) {
        for (int i1 = 0; i1 < 4; i1++)
            if (hitsLoopCycle[i1] <= l) {
                hitIcon[i1] = icon;
                hitmarkMove[i1] = 5;
                hitmarkTrans[i1] = 230;
                hitArray[i1] = damage;
                hitMarkTypes[i1] = markType;
                hitsLoopCycle[i1] = l + 70;
                return;
            }
    }
}
