package bank;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bank.BankProcess.Operation;

public class AccountMonitor {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final String CURRENCY = "€";

	private String fileName;

	public AccountMonitor(String fileName) {
		this.fileName = fileName;
	}

	// Write operation to the account statement
	public void writeOperation(Operation operation, String concept, double amount) {
		// TODO Implement file locking to prevent concurrent write issues

		try (RandomAccessFile file = new RandomAccessFile(fileName, "rw");

				FileChannel channel = file.getChannel();
				FileLock lock = channel.tryLock();) {

			if (lock != null) {

				LocalDateTime date = LocalDateTime.now();

				String sign = (operation == Operation.DEPOSIT) ? "+" : "-";

				String entry = String.format("%s | %-10s| %-21s| %s%9.2f %s%n", date.format(FORMATTER), operation,
						concept.toUpperCase(), sign, Math.abs(amount), CURRENCY);

				file.seek(file.length());

				file.write(entry.getBytes(StandardCharsets.UTF_8));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
