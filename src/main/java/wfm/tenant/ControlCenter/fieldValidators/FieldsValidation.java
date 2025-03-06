package wfm.tenant.ControlCenter.fieldValidators;

import org.springframework.stereotype.Component;

@Component
public class FieldsValidation {

    public static void validate(Object updatedObject, Object existingObject) {
        FieldValidationService.validateBlockedFields(updatedObject, existingObject);
    }
}
