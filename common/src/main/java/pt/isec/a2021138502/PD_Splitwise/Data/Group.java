package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record Group(String name, User[] users) implements Serializable {
	@Override
	public String toString() {
		return "Group [name: " + name + ", users: " + users.length + "]";
	}
}
