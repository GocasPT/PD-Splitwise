package Message.Request;

public enum EComands {
	REGISTER, LOGIN, EDIT_USER, LOGOUT, // User
	CREATE_GROUP, INVITE_USER, GET_INVITATIONS, INVITE_RESPONSE, GET_GROUPS, EDIT_GROUP, DELETE_GROUP, EXIT_GROUP,  // Group
	INSERT_EXPENSE, GET_TOTAL_EXPENSES, GET_HISTORY, EXPORT, EDIT_EXPENSE, DELETE_EXPENSE, // Expense
	INSERT_PAYMENT, GET_PAYMENTS, DELETE_PAYMENT, VIEW_BALANCE, // Payment
}
