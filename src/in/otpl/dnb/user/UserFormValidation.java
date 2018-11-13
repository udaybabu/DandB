package in.otpl.dnb.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserFormValidation implements Validator{

	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private String STRING_PATTERN = "[a-zA-Z_ ]{0,50}+";
	private String STRING_NUMBER_PATTERN = "[a-zA-Z0-9_ ]{0,50}+";
	private String MOBILE_PATTERN = "^[0-9]+$|^[+][0-9]+$";
	private String spcharvalue ="`\\~^\"|<>;{},/[]";
	private String spchars ="`()\\~^+\"|=<>;{},/.[]?'%&*";
	int counter=0,count1=0;

	@Override
	public boolean supports(Class<?> paramClass) {
		return UserForm.class.equals(paramClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm userForm = (UserForm) target;
		ValidationUtils.rejectIfEmpty(errors, "firstName", "user.errors.firstname.required");
		if ((userForm.getFirstName() != null && !(userForm.getFirstName().trim().equals("")))) {
			pattern = Pattern.compile(STRING_PATTERN);
			matcher = pattern.matcher(userForm.getFirstName());
			if (!matcher.matches()) {
				errors.rejectValue("firstName", "user.errors.firstname.format");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "lastName", "user.errors.lastname.required");
		if ((userForm.getLastName() != null && !(userForm.getLastName().trim().equals("")))) {
			pattern = Pattern.compile(STRING_PATTERN);
			matcher = pattern.matcher(userForm.getLastName());
			if (!matcher.matches()) {
				errors.rejectValue("lastName", "user.errors.lastname.format");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "loginName", "user.errors.userid.required");
		if(userForm.getLoginName() != null && userForm.getLoginName().trim() != ""){
			for(int k=0;k<userForm.getLoginName().length();k++){
				for(int j=0;j<spchars.length();j++){
					if(userForm.getLoginName().charAt(k) == spchars.charAt(j)){
						counter++;
					}
				}
			}
			if(counter != 0){
				errors.rejectValue("loginName", "user.errors.loginid.format");
			}
		}
		if(userForm.getFlag() == "save"){
			ValidationUtils.rejectIfEmpty(errors, "password", "user.errors.password.required");
			if ((userForm.getPassword() != null && !(userForm.getPassword().trim().equals("")))) {
				if(userForm.getPassword().length() < 6){
					errors.rejectValue("password", "user.errors.password.minLength");
				}
			ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "user.errors.confirmpassword.required");
			if ((userForm.getConfirmPassword() != null && userForm.getConfirmPassword().trim().equals(""))) {
				if(userForm.getPassword() != userForm.getConfirmPassword()){
					errors.rejectValue("confirmPassword", "user.errors.confirmpassword.match");
				}
			}
		}
		}

		ValidationUtils.rejectIfEmpty(errors, "employeeNumber", "user.errors.employeenumber.required");
		if ((userForm.getEmployeeNumber()!= null && !(userForm.getEmployeeNumber().trim().equals("")))) {
			pattern = Pattern.compile(STRING_NUMBER_PATTERN);
			matcher = pattern.matcher(userForm.getEmployeeNumber());
			if (!matcher.matches()) {
				errors.rejectValue("employeeNumber", "user.errors.employeeNumber.format");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "ptn", "user.errors.phonenumber.required");
		if ((userForm.getPtn() != null && !(userForm.getPtn().trim().equals("")))) {
			pattern = Pattern.compile(MOBILE_PATTERN);
			matcher = pattern.matcher(userForm.getPtn());
			if (!matcher.matches()) {
				errors.rejectValue("ptn", "user.errors.phoneNumber.format");
			}
			if(userForm.getPtn().length() < 10){
				errors.rejectValue("ptn", "user.error.mobile.lessDigits");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "emailAddress", "user.errors.email.required");
		if ((userForm.getEmailAddress() != null && !(userForm.getEmailAddress().trim().equals("")))) {
			pattern = Pattern.compile(EMAIL_PATTERN);
			matcher = pattern.matcher(userForm.getEmailAddress());
			if (!matcher.matches()) {
				errors.rejectValue("emailAddress", "user.errors.email.notvalid");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "designation", "user.errors.designation.required");
		if ((userForm.getDesignation() != null && !(userForm.getDesignation().trim().equals("")))) {
			for(int k=0;k<userForm.getDesignation().length();k++){
				for(int j=0;j<spcharvalue.length();j++){
					if(userForm.getDesignation().charAt(k) == spcharvalue.charAt(j)){
						counter++;
					}
				}
			}
			if(counter != 0){
				errors.rejectValue("designation", "user.errors.designation.format");
			}
		}
		ValidationUtils.rejectIfEmpty(errors, "imei", "user.errors.imei.required");
		if ((userForm.getImei() != null && !(userForm.getImei().trim().equals("")))) {
			pattern = Pattern.compile(STRING_NUMBER_PATTERN);
			matcher = pattern.matcher(userForm.getImei());
			if (!matcher.matches()) {
				errors.rejectValue("imei", "user.errors.imei.format");
			}
			if(userForm.getImei().length()< 15 || userForm.getImei().length() > 50){
				errors.rejectValue("imei", "user.errors.imei.minLength");
			}
		}
	}

	boolean validation(UserForm userForm) {
		boolean status = true;
		if(!Pattern.matches(STRING_PATTERN, userForm.getFirstName().trim())){
			userForm.setKeyValue("FIRST NAME");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getFirstName().length() > 50){
			userForm.setKeyValue("FIRST NAME LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(!Pattern.matches(STRING_PATTERN, userForm.getLastName().trim())) {
			userForm.setKeyValue("LAST NAME");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getLastName().length() > 50){
			userForm.setKeyValue("LAST NAME LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(!Pattern.matches(STRING_NUMBER_PATTERN, userForm.getEmployeeNumber().trim())) {
			userForm.setKeyValue("EMPLOYEE NUMBER");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getEmployeeNumber().length() > 50){
			userForm.setKeyValue("EMPLOYEE NUMBER LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(!Pattern.matches(EMAIL_PATTERN, userForm.getEmailAddress().trim())){
			userForm.setKeyValue("EMAIL ID");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getEmailAddress().length() > 50){
			userForm.setKeyValue("EMAIL ID LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(!Pattern.matches(MOBILE_PATTERN, userForm.getPtn().trim())) {
			userForm.setKeyValue("MOBILE NUMBER");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getPtn().length() < 8 || userForm.getPtn().length() > 16){
			userForm.setKeyValue("MOBILE NUMBER LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getLoginName() != null && userForm.getLoginName().trim() != ""){
			for(int k=0;k<userForm.getLoginName().length();k++){
				for(int j=0;j<spchars.length();j++){
					if(userForm.getLoginName().charAt(k) == spchars.charAt(j)){
						counter++;
					}
				}
			}
			if(counter != 0){
				userForm.setKeyValue("LOGIN ID");
				userForm.setValidateType(2);
				status= false;
			}
		}
		if(userForm.getLoginName().length() > 50){
			userForm.setKeyValue("LOGIN ID LENGTH");
			userForm.setValidateType(2);
			status = false;
		}
		if(userForm.getPassword().length() < 6 || userForm.getPassword().length() > 25 ) {
			userForm.setKeyValue("PASSWORD");
			userForm.setValidateType(2);
			status = false;
		}
		if((userForm.getDesignation() != null && !(userForm.getDesignation().trim().equals("")))){
			for(int k=0;k<userForm.getDesignation().length();k++){
				for(int j=0;j<spcharvalue.length();j++){
					if(userForm.getDesignation().charAt(k) == spcharvalue.charAt(j)){
						count1++;
					}
				}
			}
			if(count1 != 0){
				userForm.setKeyValue("DESIGNATION");
				userForm.setValidateType(2);
				status = false;
			}
			if(userForm.getDesignation().length() > 50){
				userForm.setKeyValue("DESIGNATION LENGTH");
				userForm.setValidateType(2);
				status = false;
			}
		}
		if(userForm.getImei() != null && userForm.getImei().trim() !=""){
			if(!Pattern.matches(STRING_NUMBER_PATTERN, userForm.getImei().trim())){
				userForm.setKeyValue("IMEI NUMBER");
				userForm.setValidateType(2);
				status = false;
			}
			if(userForm.getImei().length()< 15 || userForm.getImei().length() > 50){
				userForm.setKeyValue("IMEI NUMBER LENGTH");
				userForm.setValidateType(2);
				status = false;
			}
		}
				return status;
	}

	public void validatePassword(Object target, Errors errors) {
		UserForm userForm = (UserForm) target;
		ValidationUtils.rejectIfEmpty(errors, "oldPassword", "user.errors.password.curr.required");
		ValidationUtils.rejectIfEmpty(errors, "password", "user.errors.password.required");
		ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "user.errors.confirmpassword.required");

		if ((userForm.getPassword() != null && !(userForm.getPassword().trim().equals("")))) {
			if(userForm.getPassword().length() < 6){
				errors.rejectValue("password", "user.errors.newpassword.minLength");
			}

		}

		if ((userForm.getConfirmPassword() != null && userForm.getConfirmPassword().trim().equals(""))) {
			if(userForm.getPassword() != userForm.getConfirmPassword()){
				errors.rejectValue("confirmPassword", "user.errors.confirmNewPassword.match");
			}
		}
	}

	public void validateForgotPassword(Object target, Errors errors) {
		UserLoginForm userForm = (UserLoginForm) target;
		ValidationUtils.rejectIfEmpty(errors, "password", "user.errors.password.required");
		ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "user.errors.confirmpassword.required");

		if ((userForm.getPassword() != null && !(userForm.getPassword().trim().equals("")))) {
			if(userForm.getPassword().length() < 6){
				errors.rejectValue("password", "user.errors.newpassword.minLength");
			}

		}

		if ((userForm.getConfirmPassword() != null && userForm.getConfirmPassword().trim().equals(""))) {
			if(userForm.getPassword() != userForm.getConfirmPassword()){
				errors.rejectValue("confirmPassword", "user.errors.confirmNewPassword.match");
			}
		}
	}

}
