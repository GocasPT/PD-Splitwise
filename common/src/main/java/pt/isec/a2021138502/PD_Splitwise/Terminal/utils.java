package pt.isec.a2021138502.PD_Splitwise.Terminal;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class utils {
	public static void printProgress(long current, long total) {
		int percentage = (int) ((current * 100.0) / total);
		int progressChars = (int) ((60.0 * current) / total);
		String progress = "\r[" +
				"=".repeat(progressChars) +
				" ".repeat(60 - progressChars) +
				String.format("] %d%% (%d/%d bytes)", percentage, current, total);
		System.out.println(progress);
	}

	public static String getTimeTag() {
		return "<" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> ";
	}
}
