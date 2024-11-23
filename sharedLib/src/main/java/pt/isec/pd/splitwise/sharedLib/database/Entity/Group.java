package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public final class Group extends Entity {
	private String name;
	private int numUsers;
	private List<User> members;
	private List<Expense> expenses;
	private List<Payment> payments;
}
