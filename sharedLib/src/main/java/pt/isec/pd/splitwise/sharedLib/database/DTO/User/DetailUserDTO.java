package pt.isec.pd.splitwise.sharedLib.database.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class DetailUserDTO implements Serializable {
	private int id;
	private String username;
	private String email;
	private String phoneNumber;
}
