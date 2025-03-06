package wfm.tenant.ControlCenter.fieldValidators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FieldValidationService {

    public static <T> void validateBlockedFields(T updatedObject, T existingObject) {
        Set<String> blockedFields = getBlockedFields(updatedObject.getClass());
        BeanWrapper updatedWrapper = new BeanWrapperImpl(updatedObject);
        BeanWrapper existingWrapper = new BeanWrapperImpl(existingObject);

        for (String field : blockedFields) {
            Object updatedValue = updatedWrapper.getPropertyValue(field);
            Object existingValue = existingWrapper.getPropertyValue(field);

            if (updatedValue != null && !updatedValue.equals(existingValue)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Field %s is not updatable", field));
            }
        }
    }

    private static Set<String> getBlockedFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ImmutableField.class))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

}
