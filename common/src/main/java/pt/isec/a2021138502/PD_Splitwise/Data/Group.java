package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Arrays;

public record Group(int id, String name, User[] users) implements Serializable {
	@Override
	public String toString() {
		return "Group [id: " + id + ", name: " + name + ", users: " + Arrays.toString(users) + "]";
	}
}
