package pt.isec.pd.sharedLib.database.DTO.Group;

import java.io.Serializable;

public record PreviewGroupDTO(int id, String name, int members_number) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewGroupDTO{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", members_number=" + members_number +
		       '}';
	}
}
