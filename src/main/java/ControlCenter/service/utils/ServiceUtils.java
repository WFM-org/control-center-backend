package ControlCenter.service.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public abstract class ServiceUtils {

    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(pd -> pd.getName())
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}
