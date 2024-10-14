package Client.ui.TUI;

import Message.Request.Expense.*;
import Message.Request.Group.*;
import Message.Request.Payment.DeletePayment;
import Message.Request.Payment.GetPayments;
import Message.Request.Payment.InserPayment;
import Message.Request.Payment.ViewBalance;
import Message.Request.Request;
import Message.Request.User.EditUser;
import Message.Request.User.Login;
import Message.Request.User.Logout;
import Message.Request.User.Register;

import java.util.Date;

/**
 * The type Get request.
 */
//TODO: better way to converter command to request (factory or something more dynamic)
public class GetRequest {
	public static Request getRequest(String[] cmdArgs) {
		return switch (cmdArgs[0]) {
			case "help" -> {
				System.out.println("Commands:" +
						"\nregister <username> <password> <name> <email>" +
						"\nlogin <username> <password>" +
						"\neditUser <username> <password> <name> <email>" +
						"\nlogout" +
						"\ncreateGroup <groupId>" +
						"\ninviteUser <groupId> <username>" +
						"\nviewInvites" +
						"\nrespondInvite <inviteId> <accept|decline>" +
						"\nlistGroups" +
						"\neditgroupId <groupId> <newgroupId>" +
						"\ndeleteGroup <groupId>" +
						"\nleaveGroup <groupId>" +
						"\naddExpense <groupId> <date> <amount> <description>" +
						"\nviewTotal <groupId>" +
						"\nexpenseHistory <groupId>" +
						"\nexportToFile <groupId>" +
						"\neditExpense <expenseId> <groupdId> <date> <amount> <description>" +
						"\ndeleteExpense <expenseId>" +
						"\naddPayment <groupId> <userId_buyer> <userId_receiver> <date> <amount>" +
						"\nlistPayments <groupId>" +
						"\ndeletePayment <paymentId>" +
						"\nviewBalances <groupId>");

				yield null;
			}

			case "register" -> {
				if (cmdArgs.length != 5) {
					throw new RuntimeException("Usage: register <username> <password> <name> <email>");
				} else
					yield new Register(cmdArgs[1], cmdArgs[2], cmdArgs[3], cmdArgs[4]);
			}

			case "login" -> {
				if (cmdArgs.length != 3) {
					throw new RuntimeException("Usage: login <username> <password>");
				} else
					yield new Login(cmdArgs[1], cmdArgs[2]);
			}

			case "editUser" -> {
				if (cmdArgs.length != 5) {
					throw new RuntimeException("Usage: editUser <username> <password> <name> <email>");
				} else
					yield new EditUser(cmdArgs[1], cmdArgs[2], cmdArgs[3], cmdArgs[4]);
			}

			case "logout" -> {
				if (cmdArgs.length != 1) {
					throw new RuntimeException("Usage: logout");
				} else
					yield new Logout();
			}

			case "createGroup" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: createGroup <groupId>");
				} else
					yield new CreateGroup(cmdArgs[1]);
			}

			case "inviteUser" -> {
				if (cmdArgs.length != 3) {
					throw new RuntimeException("Usage: inviteUser <groupId> <username>");
				} else
					yield new InviteUser(Integer.parseInt(cmdArgs[1]), cmdArgs[2]);
			}

			case "viewInvites" -> {
				if (cmdArgs.length != 1) {
					throw new RuntimeException("Usage: viewInvites");
				} else
					yield new GetInvites();
			}

			case "respondInvite" -> {
				if (cmdArgs.length != 3) {
					throw new RuntimeException("Usage: respondInvite <inviteId> <accept|decline>");
				} else
					yield new InviteResponse(Integer.parseInt(cmdArgs[1]), cmdArgs[2].equals("accept"));
			}

			case "listGroups" -> {
				if (cmdArgs.length != 1) {
					throw new RuntimeException("Usage: listGroups");
				} else
					yield new GetGroups();
			}

			case "editgroupId" -> {
				if (cmdArgs.length != 3) {
					throw new RuntimeException("Usage: editgroupId <groupId> <newgroupId>");
				} else
					yield new EditGroup(Integer.parseInt(cmdArgs[1]), cmdArgs[2]);
			}

			case "deleteGroup" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: deleteGroup <groupId>");
				} else
					yield new DeleteGroup(Integer.parseInt(cmdArgs[1]));
			}

			case "leaveGroup" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: leaveGroup <groupId>");
				} else
					yield new ExitGroup(Integer.parseInt(cmdArgs[1]));
			}

			case "addExpense" -> {
				if (cmdArgs.length != 5) {
					throw new RuntimeException("Usage: addExpense <groupId> <date> <amount> <description>");
				} else
					//yield new InsertExpense(Integer.parseInt(cmdArgs[1]), new Date(cmdArgs[2]), Double.parseDouble(cmdArgs[3]), cmdArgs[4]);
					yield new InsertExpense(Integer.parseInt(cmdArgs[1]), new Date(), Double.parseDouble(cmdArgs[3]), cmdArgs[4]);
			}

			case "viewTotal" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: viewTotal <groupId>");
				} else
					yield new GetTotalExpenses(Integer.parseInt(cmdArgs[1]));
			}

			case "expenseHistory" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: expenseHistory <groupId>");
				} else
					yield new GetHistory(Integer.parseInt(cmdArgs[1]));
			}

			case "exportToFile" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: exportToFile <groupId>");
				} else
					yield new Export(Integer.parseInt(cmdArgs[1]));
			}

			case "editExpense" -> {
				if (cmdArgs.length != 6) {
					throw new RuntimeException("Usage: editExpense <expenseId> <groupdId> <date> <amount> <description>");
				} else
					//yield new EditExpense(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]), new Date(cmdArgs[3]), Double.parseDouble(cmdArgs[4]), cmdArgs[5]);
					yield new EditExpense(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]), new Date(), Double.parseDouble(cmdArgs[4]), cmdArgs[5]);
			}

			case "deleteExpense" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: deleteExpense <expenseId>");
				} else
					yield new DeleteExpense(Integer.parseInt(cmdArgs[1]));
			}

			case "addPayment" -> {
				if (cmdArgs.length != 6) {
					throw new RuntimeException("Usage: addPayment <groupId> <userId_buyer> <userId_receiver> <date> <amount>");
				} else
					//yield new InserPayment(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]), Integer.parseInt(cmdArgs[3]), new Date(cmdArgs[4]), Double.parseDouble(cmdArgs[5]));
					yield new InserPayment(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]), Integer.parseInt(cmdArgs[3]), new Date(), Double.parseDouble(cmdArgs[5]));
			}

			case "listPayments" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: listPayments <groupId>");
				} else
					yield new GetPayments(Integer.parseInt(cmdArgs[1]));
			}

			case "deletePayment" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: deletePayment <paymentId>");
				} else
					yield new DeletePayment(Integer.parseInt(cmdArgs[1]));
			}

			case "viewBalances" -> {
				if (cmdArgs.length != 2) {
					throw new RuntimeException("Usage: viewBalances <groupId>");
				} else
					yield new ViewBalance(Integer.parseInt(cmdArgs[1]));
			}

			default -> throw new RuntimeException("Invalid command.");

		};
	}
}
