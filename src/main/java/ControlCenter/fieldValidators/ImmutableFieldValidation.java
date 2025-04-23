package ControlCenter.fieldValidators;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import ControlCenter.exception.ImmutableUpdateException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ImmutableFieldValidation {

    public static <T> void validate(T updatedObj, T currentObj) throws ImmutableUpdateException {
        Set<String> immutableFieldList = Arrays
                .stream(updatedObj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ImmutableField.class))
                .map(Field::getName)
                .collect(Collectors.toSet());

        Set<String> immutableUpdatedList = immutableFieldList.stream().filter(field -> {
            Object updated = new BeanWrapperImpl(updatedObj).getPropertyValue(field);
            Object current = new BeanWrapperImpl(currentObj).getPropertyValue(field);
            return updated != null && !updated.equals(current);
        }).collect(Collectors.toSet());

        if (!immutableUpdatedList.isEmpty()) {
            throw new ImmutableUpdateException(immutableUpdatedList.toString());
        }
    }
}