package bank;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import bank.BankProcess.Operation;

public class AccountMonitorMalo {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final String CURRENCY = "€";

	private String fileName;

	public AccountMonitorMalo(String fileName) {
		this.fileName = fileName;
	}

	// Escribe una operación SIN bloquear el archivo
	public void writeOperation(Operation operation, String concept, double amount) {

		try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {

			LocalDateTime date = LocalDateTime.now();

			double signedAmount = (operation == Operation.DEPOSIT) ? Math.abs(amount) : -Math.abs(amount);

			String entry = String.format(Locale.US, "%s | %-10s| %-21s| %+10.2f %s%n", date.format(FORMATTER),
					operation, concept, signedAmount, CURRENCY);

			// Calcula dónde termina actualmente el archivo
			file.seek(file.length());

			System.out.println("[MALO] Posición de escritura: " + file.getFilePointer());

			// PAUSA ARTIFICIAL: solo para demostrar la condición de carrera
			System.out.println("[MALO] Esperando 10 segundos...");
			Thread.sleep(10000);

			// Escribe sin comprobar si otro proceso ha escrito entretanto
			file.write(entry.getBytes(StandardCharsets.UTF_8));

			System.out.println("[MALO] Operación escrita.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}