package <%= package %>.<%= project %>.content;

import com.zxy.common.base.exception.Code;

import java.io.Serializable;

/**
 * @author <%= user %>
 *
 */
public enum ErrorCode implements Code, Serializable {

	OrganizationNotExist(900901, "organization not exist"),
	NoBuyCourse(900902, "the company of the user has not buy the course"),
	CompanyConfigNull(900903, "CompanyConfig is null"),
	CallbackUrlEmpty(900904, "CallbackUrl is empty"),
	CallbackReturnEmpty(900905, "callback return result is empty"),
	CallMsgFail(900906, "callback return msg is fail"),
	OrgCodeDifferent(900907, "organization code is different"),
	RequiredParamNull(900908, "the required param of security-center has null value"),
	SecurityCenterReturnEmpty(900909, "security-center return value is empty"),
	SecurityCenterStatusFail(900910, "security-center return status is not 200");

	private final int code;

	ErrorCode(int code, String desc) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
