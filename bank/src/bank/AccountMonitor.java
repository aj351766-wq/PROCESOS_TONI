package bank;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import bank.BankProcess.Operation;

public class AccountMonitor {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final String CURRENCY = "EUR";

	private String fileName;

	public AccountMonitor(String fileName) {
		this.fileName = fileName;
	}

	// Write operation to the account statement
	public void writeOperation(Operation operation, String concept, double amount) {
		//TODO Implement file locking to prevent concurrent write issues
		
		try (RandomAccessFile file = new RandomAccessFile(fileName,"rw");
				
				FileChannel channel= file.getChannel(); 
				FileLock lock= channel.tryLock();) {

            if (lock != null) {	
			
				LocalDate date= LocalDate.now();
					
				String sign= (operation==Operation.DEPOSIT) ? "+" : "-";
				String entry=String.format("%s | %s%.2f %s | %s%n",concept, sign,amount, date.format(FORMATTER));
					
					
                file.seek(file.length());

                file.writeBytes(entry);
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		
	}
}

