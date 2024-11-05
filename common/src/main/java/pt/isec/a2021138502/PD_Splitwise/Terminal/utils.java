package pt.isec.a2021138502.PD_Splitwise.Terminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class utils {
	private static final Logger logger = LoggerFactory.getLogger(utils.class);

	public static String printProgress(long current, long total) {
		int percentage = (int) ((current * 100.0) / total);
		int progressChars = (int) ((60.0 * current) / total);
		return "\r[" +
				"=".repeat(progressChars) +
				" ".repeat(60 - progressChars) +
				String.format("] %d%% (%d/%d bytes)", percentage, current, total);
	}
}
