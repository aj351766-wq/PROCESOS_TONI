package bank;

public class BankProcess {
	public enum Operation {
		DEPOSIT, WITHDRAW;

		String sign() {
			return DEPOSIT.equals(this) ? "+" : "-";
		}
	}

	private static final String STATEMENT_FILE = "statement-01.txt";

	public static void main(String[] args) {

		AccountMonitor monitor = new AccountMonitor(STATEMENT_FILE);

		while (true) {

			Operation operation = ProcessInputUtils.readOperationMenu();

			if (operation == null) {
				break;
			}

			String concept = ProcessInputUtils.readConcept();
			double amount = ProcessInputUtils.readAmount();

			monitor.writeOperation(operation, concept, amount);

			if (ProcessInputUtils.confirmAnotherOperation()) {
				continue;
			}

			if (!(ProcessInputUtils.confirmExit())) {
				continue;
			}

			break;

		}
		// TODO Use ProcessInputUtils to read user input
		// TODO Use AccountMonitor to write operations to the statement file
		// TODO Handle exit condition gracefully

		System.out.println("Bank process finished.");
	}
}
