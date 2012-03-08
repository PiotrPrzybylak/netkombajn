package pl.netolution.sklep3.web.form;

public class ChangePasswordForm {

	private String oldPassword;
	private String newPassword;
	private String newPasswordRepeated;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordRepeated() {
		return newPasswordRepeated;
	}

	public void setNewPasswordRepeated(String newPasswordRepeated) {
		this.newPasswordRepeated = newPasswordRepeated;
	}

}
