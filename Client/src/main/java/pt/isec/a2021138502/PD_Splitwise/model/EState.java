package pt.isec.a2021138502.PD_Splitwise.model;

public enum EState {
	LOGIN("Login"),
	REGISTER("Register"),
	GROUPS_PAGE("Groups Page"),
	INVITES_PAGE("Invites Page"),
	USER_PAGE("User Page"),
	EDIT_USER_PAGE("Edit User Page"),
	GROUP_PAGE("Group Page"),
	EDIT_GROUP_PAGE("Edit Group Page"),
	;

	private final String str;

	EState(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}

}
