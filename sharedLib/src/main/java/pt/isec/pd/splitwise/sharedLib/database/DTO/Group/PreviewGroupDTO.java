package pt.isec.pd.splitwise.sharedLib.database.DTO.Group;

import java.io.Serializable;

public record PreviewGroupDTO(int id, String name, int membersNumber) implements Serializable {
	@Override
	public String toString() {
		return "PreviewGroupDTO{" +
		       "id: " + id +
		       ", name: '" + name + '\'' +
		       ", membersNumber: " + membersNumber +
		       '}';
	}
}
