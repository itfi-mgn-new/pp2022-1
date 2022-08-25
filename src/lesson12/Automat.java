package lesson12;

import sun.net.NetworkServer;

public class Automat {
	private static final int	TERM_DERNU = 0;
	private static final int	TERM_OTPUSHU = 1;
	
	private static final Rule[]	RULES = {
			new Rule(0, TERM_DERNU, 1, ()->System.err.println("ON!")),
			new Rule(1, TERM_OTPUSHU, 2, ()->{}),
			new Rule(2, TERM_DERNU, 3, ()->System.err.println("OFF!")),
			new Rule(3, TERM_OTPUSHU, 0, ()->{}),
		};
	static int	state = 0;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		automat(TERM_DERNU);
		automat(TERM_OTPUSHU);
		automat(TERM_OTPUSHU);
		automat(TERM_DERNU);
		automat(TERM_OTPUSHU);
	}

	static void automat(final int terminal) {
		for (Rule item : RULES) {
			if (item.state == state && item.terminal == terminal) {
				state = item.newState;
				item.action.run();
				return;
			}
		}
		System.err.println("Fail: "+state+" and "+terminal);
	}
	
	
	static class Rule {
		final int		state;
		final int		terminal;
		final int		newState;
		final Runnable	action;

		public Rule(int state, int terminal, int newState, Runnable action) {
			this.state = state;
			this.terminal = terminal;
			this.newState = newState;
			this.action = action;
		}

		@Override
		public String toString() {
			return "Rule [state=" + state + ", terminal=" + terminal + ", newState=" + newState + "]";
		}
	}
}
