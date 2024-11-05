module common {
	requires com.google.gson;
	requires org.xerial.sqlitejdbc;
	requires org.slf4j;

	exports pt.isec.a2021138502.PD_Splitwise.Data;
	exports pt.isec.a2021138502.PD_Splitwise.Message;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Request;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Request.User;
	exports pt.isec.a2021138502.PD_Splitwise.Message.Response;
}