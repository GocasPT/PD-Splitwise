package pt.isec.a2021138502.PD_Splitwise.model;

public enum EState {
	LOGIN("Login"),
	REGISTER("Register"),
	GROUPS_PAGE("Groups Page"),
	INVITES_PAGE("Invites Page"),
	USER_PAGE("User Page"),
	GROUP_PAGE("Group Page");
	//TODO: add more states

	private final String str;

	EState(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}

}
