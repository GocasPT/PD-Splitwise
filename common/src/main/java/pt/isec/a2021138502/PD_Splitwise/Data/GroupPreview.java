package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record GroupPreview(int id, String name, int memberCount) implements Serializable {
	@Override
	public String toString() {
		return "GroupPreview [id: " + id + ", name: " + name + ", users: " + memberCount + "]";
	}
}
