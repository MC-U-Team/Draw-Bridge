package info.u_team.draw_bridge.util;

import java.util.function.Predicate;

public class Predicates {
	
	public static <T> Predicate<T> not(Predicate<T> predicate) {
		return predicate.negate();
	}
	
}
