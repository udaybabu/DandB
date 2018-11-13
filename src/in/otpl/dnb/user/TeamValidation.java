package in.otpl.dnb.user;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TeamValidation implements Validator{

	private String spcharvalue ="`\\~^\"|<>;{},/[]";
	int counter=0;
	@Override
	public boolean supports(Class<?> paramClass) {
		return TeamForm.class.equals(paramClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TeamForm teamForm = (TeamForm) target;

		ValidationUtils.rejectIfEmpty(errors, "teamName", "team.error.required.teamName");
		if ((teamForm.getTeamName() != null && !(teamForm.getTeamName().trim().equals("")))) {
			for(int k=0;k<teamForm.getTeamName().length();k++){
				for(int j=0;j<spcharvalue.length();j++){
					if(teamForm.getTeamName().charAt(k) == spcharvalue.charAt(j)){
						counter++;
					}
				}
			}
			if(counter != 0){
				errors.rejectValue("designation", "team.error.specialcharacters");
			}
		}
	
		
		ValidationUtils.rejectIfEmpty(errors, "teamLeadId", "team.error.required.teamLeadId");

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selected", "team.errors.selectedUsers.required");
	}
}