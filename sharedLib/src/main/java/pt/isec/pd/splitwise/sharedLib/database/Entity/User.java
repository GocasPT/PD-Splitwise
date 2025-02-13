package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public final class User extends Entity {
	private String username;
	private String email;
	private String phoneNumber;
	private String password;
}
