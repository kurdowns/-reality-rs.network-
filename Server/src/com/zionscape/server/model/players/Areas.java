package com.zionscape.server.model.players;

import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.minigames.clanwars.ClanWars;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.npcs.combat.kraken.Kraken;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;

public final class Areas {

	public static boolean inExtremeArea(Location location) {
		return new Area(3010, 3067, 4354, 4412).in(location); }

	public static boolean inRespectedArea(Location location) {
		if(new Area(2180, 2298, 3277, 3383).in(location)) {
			return true;
		}
		return new Area(2755, 2812, 3836, 3871).in(location);
	}

	public static boolean inHomeArea(Location location) {
		return new Area(3260, 3278, 3156, 3186).in(location);
	}

	public static boolean inNexArea(Location location) {
		return new Area(2911, 2939, 5188, 5218).in(location);
	}

	public static boolean inCorpArea(Location location) {
		return new Area(2921, 2970, 4360, 4404).in(location);
	}

	private static class Area {

		private final int lowerX;
		private final int higherX;
		private final int lowerY;
		private final int higherY;

		public Area(int lowerX, int higherX, int lowerY, int higherY) {
			this.lowerX = lowerX;
			this.higherX = higherX;
			this.lowerY = lowerY;
			this.higherY = higherY;
		}

		public boolean in(Location location) {
			return location.getX() >= lowerX && location.getX() <= higherX && location.getY() >= lowerY && location.getY() <= higherY;
		}

	}

	/**
	 * old player
	 */
	/*public boolean inMulti() {

		// revs
		if (absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}

		if (Zulrah.inArea(this)) {
			return true;
		}

		if (Zombies.inGameArea(getLocation())) {
			return true;
		}

		// nex
		if (absX >= 2911 && absX <= 2939 && absY >= 5188 && absY <= 5218) {
			return true;
		}

		if (ClanWars.inArena(this)) {
			return true;
		}

		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3650 && absX <= 3687 && absY >= 3471 && absY <= 3505)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX > 2777 && absX < 2812 && absY > 3771 && absY < 3779)
				|| (absX >= 2494 && absX <= 2540 && absY >= 4627 && absY <= 4664)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				||
				// (absX >= 3079 && absX <= 3100 && absY >= 3481 && absY <= 3501) ||
				(absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2380 && absX <= 2420 && absY >= 3092 && absY <= 3115)
				|| (absX >= 2710 && absX <= 2733 && absY >= 2742 && absY <= 2773)
				|| (absX >= 3244 && absX <= 3276 && absY >= 2915 && absY <= 2938)
				|| (absX >= 2862 && absX <= 2878 && absY >= 5350 && absY <= 5371)
				|| // bandos chamber
				(absX >= 2823 && absX <= 2843 && absY >= 5295 && absY <= 5309)
				|| // arma chamber
				(absX >= 2894 && absX <= 2908 && absY >= 5257 && absY <= 5273)
				|| // sara chamber
				(absX >= 2917 && absX <= 2938 && absY >= 5317 && absY <= 5332)
				|| // zammy chamber
				(absX >= 2672 && absX <= 2747 && absY >= 5054 && absY <= 5117)
				|| // avatar dest
				// this.inFunPk() ||
				this.inSeaTroll() || this.inKQ() || (absX > 2921 && absX < 2970 && absY > 4355 && absY < 4412)
				|| (absX > 3484 && absX < 3516 && absY > 3560 && absY < 3582)
				|| (absX > 2686 && absX < 2734 && absY > 9987 && absY < 10013)
				|| (absX >= 2358 && absX <= 2378 && absY >= 4952 && absY <= 4969)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 3151 && absX <= 3199 && absY >= 2961 && absY <= 2996)//bandits

				|| (absX >= 2735 && absX <= 2792 && absY >= 2757 && absY <= 2812)) {// ape atoll
			return true;
		}
		return false;
	}*/

	/**
	 * old npc
	 */
	/*public boolean inMulti() {

		if (Zulrah.inArea(this)) {
			return true;
		}

		// revs
		if (absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}

		if (Zombies.inGameArea(getLocation())) {
			return true;
		}

		// nex
		if (absX >= 2911 && absX <= 2939 && absY >= 5188 && absY <= 5218) {
			return true;
		}

		// nardah
		if (absX >= 3398 && absX <= 3453 && absY >= 2899 && absY <= 2942) {
			return true;
		}

		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3650 && absX <= 3687 && absY >= 3471 && absY <= 3505)
				|| (absX > 2921 && absX < 2970 && absY > 4355 && absY < 4412)
				|| // corp
				(absX > 2686 && absX < 2734 && absY > 9987 && absY < 10013)
				|| // bork
				(absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2494 && absX <= 2540 && absY >= 4627 && absY <= 4664)
				|| (absX > 2777 && absX < 2812 && absY > 3771 && absY < 3779)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX > 2493 && absX < 2564 && absY > 4544 && absY < 4601)
				|| (absX > 3455 && absX < 3526 && absY > 9474 && absY < 9535)
				|| (absX >= 3206 && absX <= 3249 && absY >= 2752 && absY <= 2775)
				|| (absX >= 3465 && absX <= 3502 && absY >= 9494 && absY <= 9506)
				|| (absX >= 3465 && absX <= 3502 && absY >= 9494 && absY <= 9506)
				|| (absX >= 2710 && absX <= 2733 && absY >= 2742 && absY <= 2773) || // jungle demon
				(absX >= 3244 && absX <= 3276 && absY >= 2915 && absY <= 2938) || // bork test3331
				(absX >= 2735 && absX <= 2792 && absY >= 2757 && absY <= 2812) || // ape atoll
				(absX >= 2862 && absX <= 2878 && absY >= 5350 && absY <= 5371) || // bandos chamber
				(absX >= 2823 && absX <= 2843 && absY >= 5295 && absY <= 5309) || // arma chamber
				(absX >= 2894 && absX <= 2908 && absY >= 5257 && absY <= 5273) || // sara chamber
				(absX >= 2917 && absX <= 2938 && absY >= 5317 && absY <= 5332) || // zammy chamber
				(absX >= 2672 && absX <= 2747 && absY >= 5054 && absY <= 5117) || // avatar dest
				// (absX >= 3345 && absX <= 3375 && absY >= 5842 && absY <= 5872) || // nomad

				(absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}*/

	public static boolean inMulti(Entity e) {
// revs
		if (e.getX() > 2941 && e.getX() < 3392 && e.getY() > 9918 && e.getY() < 10366) {
			return true;
		}

		if (Zulrah.inArea(e)) {
			return true;
		}

		if(Kraken.inArea(e.getLocation())) {
			return true;
		}

		if (Zombies.inGameArea(e.getLocation())) {
			return true;
		}

		// nex
		if (e.getX() >= 2911 && e.getX() <= 2939 && e.getY() >= 5188 && e.getY() <= 5218) {
			return true;
		}

		// venetis
		if (e.getX() >= 3356 && e.getX() <= 3387 && e.getY() >= 3724 && e.getY() <= 3762) {
			return true;
		}

		if (ClanWars.inArena(e)) {
			return true;
		}

		if ((e.getX() >= 3136 && e.getX() <= 3327 && e.getY() >= 3519 && e.getY() <= 3607)
				|| (e.getX() >= 3650 && e.getX() <= 3687 && e.getY() >= 3471 && e.getY() <= 3505)
				|| (e.getX() >= 3190 && e.getX() <= 3327 && e.getY() >= 3648 && e.getY() <= 3839)
				|| (e.getX() >= 3200 && e.getX() <= 3390 && e.getY() >= 3840 && e.getY() <= 3967)
				|| (e.getX() > 2777 && e.getX() < 2812 && e.getY() > 3771 && e.getY() < 3779)
				|| (e.getX() >= 2494 && e.getX() <= 2540 && e.getY() >= 4627 && e.getY() <= 4664)
				|| (e.getX() >= 2992 && e.getX() <= 3007 && e.getY() >= 3912 && e.getY() <= 3967)
				||
				// (e.getX() >= 3079 && e.getX() <= 3100 && e.getY() >= 3481 && e.getY() <= 3501) ||
				(e.getX() >= 2946 && e.getX() <= 2959 && e.getY() >= 3816 && e.getY() <= 3831)
				|| (e.getX() >= 3008 && e.getX() <= 3199 && e.getY() >= 3856 && e.getY() <= 3903)
				|| (e.getX() >= 3008 && e.getX() <= 3071 && e.getY() >= 3600 && e.getY() <= 3711)
				|| (e.getX() >= 3072 && e.getX() <= 3327 && e.getY() >= 3608 && e.getY() <= 3647)
				|| (e.getX() >= 2624 && e.getX() <= 2690 && e.getY() >= 2550 && e.getY() <= 2619)
				|| (e.getX() >= 2371 && e.getX() <= 2422 && e.getY() >= 5062 && e.getY() <= 5117)
				|| (e.getX() >= 2896 && e.getX() <= 2927 && e.getY() >= 3595 && e.getY() <= 3630)
				|| (e.getX() >= 2892 && e.getX() <= 2932 && e.getY() >= 4435 && e.getY() <= 4464)
				|| (e.getX() >= 2380 && e.getX() <= 2420 && e.getY() >= 3092 && e.getY() <= 3115)
				|| (e.getX() >= 2710 && e.getX() <= 2733 && e.getY() >= 2742 && e.getY() <= 2773)
				|| (e.getX() >= 3244 && e.getX() <= 3276 && e.getY() >= 2915 && e.getY() <= 2938)
				|| (e.getX() >= 2862 && e.getX() <= 2878 && e.getY() >= 5350 && e.getY() <= 5371)
				|| // bandos chamber
				(e.getX() >= 2823 && e.getX() <= 2843 && e.getY() >= 5295 && e.getY() <= 5309)
				|| // arma chamber
				(e.getX() >= 2894 && e.getX() <= 2908 && e.getY() >= 5257 && e.getY() <= 5273)
				|| // sara chamber
				(e.getX() >= 2917 && e.getX() <= 2938 && e.getY() >= 5317 && e.getY() <= 5332)
				|| // zammy chamber
				(e.getX() >= 2672 && e.getX() <= 2747 && e.getY() >= 5054 && e.getY() <= 5117)
				|| // avatar dest
				// this.inFunPk() ||
				inSeaTroll(e.getX(), e.getY()) || inKQ(e.getX(), e.getY()) || (e.getX() > 2921 && e.getX() < 2970 && e.getY() > 4355 && e.getY() < 4412)
				|| (e.getX() > 3484 && e.getX() < 3516 && e.getY() > 3560 && e.getY() < 3582)
				|| (e.getX() > 2686 && e.getX() < 2734 && e.getY() > 9987 && e.getY() < 10013)
				|| (e.getX() >= 2358 && e.getX() <= 2378 && e.getY() >= 4952 && e.getY() <= 4969)
				|| (e.getX() >= 2256 && e.getX() <= 2287 && e.getY() >= 4680 && e.getY() <= 4711)
				|| (e.getX() >= 3151 && e.getX() <= 3199 && e.getY() >= 2961 && e.getY() <= 2996)//bandits

				|| (e.getX() >= 2735 && e.getX() <= 2792 && e.getY() >= 2757 && e.getY() <= 2812)) {// ape atoll
			return true;
		}

		return false;
	}

	public static boolean inSeaTroll(int absX, int absY) {
		return absX > 2493 && absX < 2564 && absY > 4544 && absY < 4601;
	}

	public static boolean inKQ(int absX, int absY) {
		return absX > 3455 && absX < 3526 && absY > 9474 && absY < 9535;
	}

	public static boolean inJail(Entity e) {
		return new Area(2590, 2620, 4760, 4780).in(e.getLocation());
	}

}
